/**
 * DefaultMill.java - basic implementation of mill (the arithmetic logic unit)
 * 
 * Copyright 2014 Jeffrey Finkelstein.
 * 
 * This file is part of analyticalengine.
 * 
 * analyticalengine is free software: you can redistribute it and/or modify it
 * under the terms of the GNU General Public License as published by the Free
 * Software Foundation, either version 3 of the License, or (at your option)
 * any later version.
 * 
 * analyticalengine is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY
 * or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for
 * more details.
 * 
 * You should have received a copy of the GNU General Public License along with
 * analyticalengine. If not, see <http://www.gnu.org/licenses/>.
 */
package analyticalengine;

import java.math.BigInteger;

/**
 * Basic implementation of a mill, the arithmetic logic unit of the Analytical
 * Engine.
 * 
 * @author Jeffrey Finkelstein <jeffrey.finkelstein@gmail.com>
 * @since 0.0.1
 */
public class DefaultMill implements Mill {

    /** The maximum value of an integer that can be stored in mill's axes. */
    public static final BigInteger MAX = BigInteger.TEN.pow(50);

    /** The minimum value of an integer that can be stored in mill's axes. */
    public static final BigInteger MIN = MAX.negate();

    /**
     * The index of the next axis to which a number will be loaded, either
     * directly or from the store.
     */
    private int currentAxis = 0;

    /** The operation to apply to the next two numbers loaded into the mill. */
    private Operation currentOperation = null;

    /** The outgoing axes, which store the result of the arithmetic operations. */
    private BigInteger[] egressAxes = new BigInteger[2];

    /**
     * The incoming axes, which store the operands of the arithmetic
     * operations.
     */
    private BigInteger[] ingressAxes = new BigInteger[3];

    /**
     * The integers stored in the most recently used axis.
     * 
     * If a left shift or right shift is performed, this will be the value of
     * the first main ingress axis or the main egress axis, respectively. If a
     * load or store is performed, this will be the integer that is loaded or
     * stored. If an arithmetic operation is performed, this will be the value
     * of the main egress axis (after the operation is performed).
     */
    private BigInteger mostRecentValue = null;

    /**
     * Indicates either an overflow or a change of sign that occurred while
     * computing the result of an arithmetic operation.
     * 
     * It is impossible to know which of those options this flag indicates
     * without knowing which operation has been performed.
     */
    private boolean runUp;

    /**
     * Executes the arithmetic operation specified by the value of
     * {@link #currentOperation} and stores the result in the egress axes.
     * 
     * This method is invoked after two values (and possibly an additional
     * integer in the prime axis) have been loaded into the ingress axes.
     * 
     * This method sets the value of {@link #mostRecentValue} to be the result
     * of the arithmetic operation.
     * 
     * If there is an arithmetic overflow or a change of operation, the run up
     * flag will be set to {@code true}.
     */
    private void execute() {
        this.runUp = false;
        this.currentAxis = 0;
        BigInteger result = null;
        switch (this.currentOperation) {
        case ADD:
            result = this.ingressAxes[0].add(this.ingressAxes[1]);
            /*
             * If the sum is greater than MAX, compensate for the overflow by
             * subtracting MAX from the sum (essentially computing the sum
             * modulo MAX) and setting the run up (overflow) flag.
             * 
             * If the first addend is nonnegative but the sum is negative, set
             * the run up (overflow) flag.
             * 
             * The run up lever is used to indicate both overflow and change of
             * sign, and it is not possible to distinguish which has happened
             * without knowing the addends.
             */
            if (result.compareTo(MAX) >= 0) {
                this.runUp = true;
                result = result.subtract(MAX);
            } else if (this.ingressAxes[0].signum() >= 0
                    && result.signum() < 0) {
                this.runUp = true;
            }
            this.egressAxes[0] = result;
            this.egressAxes[1] = BigInteger.ZERO;
            break;
        case DIVIDE:
            BigInteger dividend = this.ingressAxes[0];

            if (this.ingressAxes[2].signum() != 0) {
                dividend = dividend.add(this.ingressAxes[2].multiply(MAX));
            }
            if (this.ingressAxes[1].signum() == 0) {
                this.egressAxes[0] = BigInteger.ZERO;
                this.egressAxes[1] = BigInteger.ZERO;
                this.runUp = true;
                break;
            }
            BigInteger[] quotientRemainder = dividend
                    .divideAndRemainder(this.ingressAxes[1]);
            if (quotientRemainder[0].abs().compareTo(MAX) > 0) {
                // Overflow if quotient more than 50 digits
                this.egressAxes[0] = BigInteger.ZERO;
                this.egressAxes[1] = BigInteger.ZERO;
                this.runUp = true;
                break;
            }
            this.egressAxes[0] = quotientRemainder[1];
            this.egressAxes[1] = quotientRemainder[0];
            break;
        case MULTIPLY:
            result = this.ingressAxes[0].multiply(this.ingressAxes[1]);
            /*
             * Check for product longer than one column and set the primed
             * egress axis to the upper part.
             */
            if (result.abs().compareTo(MAX) > 0) {
                BigInteger[] qr = result.divideAndRemainder(MAX);
                this.egressAxes[0] = qr[1];
                this.egressAxes[1] = qr[0];
            } else {
                this.egressAxes[0] = result;
                this.egressAxes[1] = BigInteger.ZERO;
            }
            break;
        case SUBTRACT:
            result = this.ingressAxes[0].subtract(this.ingressAxes[1]);
            /*
             * Check for passage through negative infinity (borrow) and set run
             * up and trim value as a result.
             */
            if (result.compareTo(MIN) <= 0) {
                this.runUp = true;
                result = result.add(MAX).negate();
            } else if (this.ingressAxes[0].signum() >= 0
                    && result.signum() < 0) {
                /*
                 * Run up gets set when the result of a subtraction changes the
                 * sign of the first argument. Note that since the same lever
                 * is used to indicate borrow and change of sign, it is not
                 * possible to distinguish overflow from sign change.
                 */
                this.runUp = true;
            }
            this.egressAxes[0] = result;
            this.egressAxes[1] = BigInteger.ZERO;
            break;
        default:
            break;
        }
        this.mostRecentValue = this.egressAxes[0];
    }

    /**
     * {@inheritDoc}
     * 
     * @return {@inheritDoc}
     */
    @Override
    public boolean hasRunUp() {
        return this.runUp;
    }

    /*
     * From the original code:
     * 
     * In Section 1.[5] of his 26 December 1837 "On the Mathematical Powers of
     * the Calculating Engine", Babbage remarks: "The termination of the
     * Multiplication arises from the action of the Counting apparatus which at
     * a certain time directs the barrels to order the product thus obtained to
     * be stepped down so the decimal point may be in its proper place,...",
     * which implies a right shift as an integral part of the multiply
     * operation. This makes enormous sense, since it would take only a tiny
     * fraction of the time a full-fledged divide would require to renormalise
     * the number. I have found no description in this or later works of how
     * the number of digits to shift was to be conveyed to the mill. So, I am
     * introducing a rather curious operation for this purpose. Invoked after a
     * multiplication, but before the result has been emitted from the egress
     * axes, it shifts the double-length product right by a fixed number of
     * decimal places, and leaves the result in the egress axes. Thus, to
     * multiple V11 and V12, scale the result right 10 decimal places, and
     * store the scaled product in V10, one would write:
     * 
     * × L011 L012 >10 S010
     * 
     * Similarly, we provide a left shift for prescaling fixed point dividends
     * prior to division; this operation shifts the two ingress axes containing
     * the dividend by the given amount, and must be done after the ingress
     * axes are loaded but before the variable card supplying the divisor is
     * given. For example, if V11 and V12 contain the lower and upper halves of
     * the quotient, respectively, and we wish to shift this quantity left 10
     * digits before dividing by the divisor in V13, we use:
     * 
     * ÷ L011 L012' <10 L013 S010
     * 
     * Note that shifting does not change the current operation for which the
     * mill is set; it merely shifts the axes in place.
     */

    /**
     * {@inheritDoc}
     * 
     * This method sets the value of {@link #mostRecentValue} to be the value
     * stored in the main ingress axis (after the shift).
     * 
     * @param shift
     *            {@inheritDoc}
     */
    @Override
    public void leftShift(final int shift) {

        /*
         * A left shift is performed before a fixed point division, so the two
         * ingress axes containing the dividend are shifted.
         */
        BigInteger value = this.ingressAxes[0];
        if (this.ingressAxes[2].signum() != 0) {
            value = value.add(this.ingressAxes[2].multiply(MAX));
        }

        BigInteger sf = BigInteger.TEN.pow(shift);
        BigInteger pr = value.multiply(sf);

        if (pr.compareTo(MAX) != 0) {
            BigInteger[] pq = pr.divideAndRemainder(MAX);
            this.ingressAxes[0] = pq[1];
            this.ingressAxes[2] = pq[0];
        } else {
            this.ingressAxes[0] = pr;
            this.ingressAxes[2] = BigInteger.ZERO;
        }

        this.mostRecentValue = this.ingressAxes[0];
    }

    /**
     * {@inheritDoc}
     * 
     * @return {@inheritDoc}
     */
    @Override
    public BigInteger maxValue() {
        return MAX;
    }

    /**
     * {@inheritDoc}
     * 
     * @return {@inheritDoc}
     */
    @Override
    public BigInteger minValue() {
        return MIN;
    }

    /**
     * {@inheritDoc}
     * 
     * @return {@inheritDoc}
     */
    @Override
    public BigInteger mostRecentValue() {
        return this.mostRecentValue;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void reset() {
        this.currentOperation = null;
        this.currentAxis = 0;
        this.ingressAxes = new BigInteger[3];
        this.egressAxes = new BigInteger[2];
        this.runUp = false;
    }

    /**
     * {@inheritDoc}
     * 
     * This method sets the value of {@link #mostRecentValue} to be the value
     * stored in the main ingress axis (after the shift).
     * 
     * @param shift
     *            {@inheritDoc}
     */
    @Override
    public void rightShift(final int shift) {
        /*
         * A right shift is used to normalise after a fixed point
         * multiplication, so the egress axes are used.
         */
        BigInteger value = this.egressAxes[0];
        if (this.egressAxes[1].signum() != 0) {
            value = value.add(this.egressAxes[1].multiply(MAX));
        }

        BigInteger shiftFactor = BigInteger.TEN.pow(shift);
        BigInteger[] qr = value.divideAndRemainder(shiftFactor);

        if (qr[0].compareTo(MAX) != 0) {
            qr = qr[0].divideAndRemainder(MAX);
            this.egressAxes[0] = qr[1];
            this.egressAxes[1] = qr[0];
        } else {
            this.egressAxes[0] = qr[0];
            this.egressAxes[1] = BigInteger.ZERO;
        }

        this.mostRecentValue = this.egressAxes[0];
    }

    /**
     * {@inheritDoc}
     * 
     * @param operation
     *            {@inheritDoc}
     */
    @Override
    public void setOperation(final Operation operation) {
        this.currentOperation = operation;
        this.currentAxis = 0;
    }

    /**
     * {@inheritDoc}
     * 
     * This method sets the value of {@link #mostRecentValue} to be the value
     * transferred in.
     * 
     * @param value
     *            {@inheritDoc}
     * @param prime
     *            {@inheritDoc}
     */
    @Override
    public void transferIn(final BigInteger value, final boolean prime) {
        this.mostRecentValue = value;

        if (prime) {
            this.ingressAxes[2] = value;
            return;
        }

        this.ingressAxes[this.currentAxis] = value;
        // When first ingress axis set, clear prime axis
        if (this.currentAxis == 0) {
            this.ingressAxes[2] = BigInteger.ZERO;
        }
        // Rotate to next ingress axis
        this.currentAxis = (this.currentAxis + 1) % 2;
        // Once enough arguments have been transferred in, implicitly execute
        // the requested operation.
        if (this.currentAxis == 0) {
            this.execute();
        }
    }

    /**
     * {@inheritDoc}
     * 
     * This method sets the value of {@link #mostRecentValue} to be the value
     * transferred out.
     * 
     * @return {@inheritDoc}
     */
    @Override
    public BigInteger transferOut() {
        return this.transferOut(false);
    }

    /**
     * {@inheritDoc}
     * 
     * This method sets the value of {@link #mostRecentValue} to be the value
     * transferred out.
     * 
     * @param prime
     *            {@inheritDoc}
     * @return {@inheritDoc}
     */
    @Override
    public BigInteger transferOut(final boolean prime) {
        BigInteger result = null;
        if (prime) {
            result = this.egressAxes[1];
        } else {
            result = this.egressAxes[0];
        }
        this.mostRecentValue = result;
        return result;
    }
}
