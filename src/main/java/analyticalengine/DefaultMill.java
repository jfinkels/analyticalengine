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
import java.util.Arrays;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Basic implementation of a mill, the arithmetic logic unit of the Analytical
 * Engine.
 * 
 * @author Jeffrey Finkelstein <jeffrey.finkelstein@gmail.com>
 * @since 0.0.1
 */
public class DefaultMill implements Mill {

    /** The logger for this class. */
    private static final transient Logger LOG = LoggerFactory
            .getLogger(DefaultMill.class);

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

    /**
     * The outgoing axes, which store the result of the arithmetic operations.
     * 
     * The first element of this array is the main egress axis. The last
     * element of this array is the prime egress axis, which represents the
     * high-order bits of the main egress axis, if any.
     * */
    private BigInteger[] egressAxes = new BigInteger[2];

    /**
     * The incoming axes, which store the operands of the arithmetic
     * operations.
     * 
     * The first two elements of this array are the main ingress axes. The last
     * element of this array is the prime ingress axis, which represents the
     * high-order bits of the first ingress axis (that is, the axis at index
     * 0), if any.
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
     * without knowing the operation and the operands.
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
        LOG.debug("Executing operation: " + this.currentOperation);
        LOG.debug("Ingress axes: " + Arrays.toString(this.ingressAxes));
        this.runUp = false;
        this.currentAxis = 0;
        BigInteger result = null;
        switch (this.currentOperation) {
        case ADD:
            result = this.ingressAxes[0].add(this.ingressAxes[1]);
            LOG.debug("Adding " + this.ingressAxes[0] + " to "
                    + this.ingressAxes[1]);
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
                LOG.debug("Run up lever set: compensating for overflow.");
                this.runUp = true;
                result = result.subtract(MAX);
            } else if (this.ingressAxes[0].signum() >= 0
                    && result.signum() < 0) {
                LOG.debug("Run up lever set: change of sign.");
                this.runUp = true;
            }
            this.egressAxes[0] = result;
            this.egressAxes[1] = BigInteger.ZERO;
            LOG.debug("Sum: " + this.egressAxes[0]);
            break;
        case DIVIDE:
            BigInteger dividend = this.ingressAxes[0];

            // check for division by zero
            if (this.ingressAxes[1].signum() == 0) {
                LOG.warn("Division by zero detected.");
                this.egressAxes[0] = BigInteger.ZERO;
                this.egressAxes[1] = BigInteger.ZERO;
                this.runUp = true;
                break;
            }

            // add the high-order digits to the dividend
            if (this.ingressAxes[2].signum() != 0) {
                dividend = dividend.add(this.ingressAxes[2].multiply(MAX));
            }
            LOG.debug("Computed dividend: " + dividend);

            // compute the quotient and the remainder
            BigInteger[] quotientRemainder = dividend
                    .divideAndRemainder(this.ingressAxes[1]);

            // overflow if the quotient is more than maximum number of digits
            if (quotientRemainder[0].abs().compareTo(MAX) > 0) {
                LOG.debug("Overflow detected.");
                this.egressAxes[0] = BigInteger.ZERO;
                this.egressAxes[1] = BigInteger.ZERO;
                this.runUp = true;
                break;
            }

            this.egressAxes[0] = quotientRemainder[1];
            this.egressAxes[1] = quotientRemainder[0];
            LOG.debug("Remainder and quotient: "
                    + Arrays.toString(this.egressAxes));
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

    /**
     * {@inheritDoc}
     * 
     * @param shift
     *            {@inheritDoc}
     */
    @Override
    public void leftShift(final int shift) {
        LOG.debug("Performing left shift by " + shift);
        /*
         * A left shift is performed before a fixed point division, so the two
         * ingress axes containing the dividend are shifted.
         */
        BigInteger value = this.ingressAxes[0];
        if (this.ingressAxes[2].signum() != 0) {
            value = value.add(this.ingressAxes[2].multiply(MAX));
        }
        LOG.debug("Value to shift: " + value);

        BigInteger sf = BigInteger.TEN.pow(shift);
        BigInteger pr = value.multiply(sf);

        LOG.debug("Shifted to: " + pr);

        if (pr.compareTo(MAX) != 0) {
            BigInteger[] pq = pr.divideAndRemainder(MAX);
            this.ingressAxes[0] = pq[1];
            this.ingressAxes[2] = pq[0];
        } else {
            this.ingressAxes[0] = pr;
            this.ingressAxes[2] = BigInteger.ZERO;
        }
        LOG.debug("Set new ingress axes: " + Arrays.toString(this.ingressAxes));
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
     * @param value
     *            {@inheritDoc}
     */
    @Override
    public void transferIn(final BigInteger value) {
        this.transferIn(value, false);
    }

    /**
     * {@inheritDoc}
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
        // TODO document this
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
     * @return {@inheritDoc}
     */
    @Override
    public BigInteger transferOut() {
        return this.transferOut(false);
    }

    /**
     * {@inheritDoc}
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
