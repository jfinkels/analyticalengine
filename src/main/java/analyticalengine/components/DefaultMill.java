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
package analyticalengine.components;

import java.math.BigInteger;

public class DefaultMill implements Mill {
    
    public static final BigInteger MAX = BigInteger.TEN.pow(50);
    
    public static final BigInteger MIN = MAX.negate();

    private int currentAxis = 0;

    private Operation currentOperation = null;

    private BigInteger[] egressAxes = new BigInteger[2];

    private BigInteger[] ingressAxes = new BigInteger[3];

    // load and store instructions use this
    private BigInteger mostRecentValue = null;

    private boolean runUp;

    // at the end of this method, the egress axes have been set as a side
    // effect
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
            this.mostRecentValue = result;
            break;
        case DIVIDE:
            BigInteger dividend = this.ingressAxes[0];

            if (this.ingressAxes[2].signum() != 0) {
                dividend = dividend.add(this.ingressAxes[2].multiply(MAX));
            }
            if (this.ingressAxes[1].signum() == 0) {
                this.egressAxes[0] = BigInteger.ZERO;
                this.egressAxes[1] = BigInteger.ZERO;
                this.mostRecentValue = BigInteger.ZERO;
                this.runUp = true;
                break;
            }
            BigInteger[] quotientRemainder = dividend
                    .divideAndRemainder(this.ingressAxes[1]);
            if (quotientRemainder[0].abs().compareTo(MAX) > 0) {
                // Overflow if quotient more than 50 digits
                this.egressAxes[0] = BigInteger.ZERO;
                this.egressAxes[1] = BigInteger.ZERO;
                this.mostRecentValue = BigInteger.ZERO;
                this.runUp = true;
                break;
            }
            this.egressAxes[0] = quotientRemainder[1];
            this.egressAxes[1] = quotientRemainder[0];
            this.mostRecentValue = quotientRemainder[1];
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
                this.mostRecentValue = qr[1];
            } else {
                this.egressAxes[0] = result;
                this.egressAxes[1] = BigInteger.ZERO;
                this.mostRecentValue = result;
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
            this.mostRecentValue = result;
            break;
        case NOOP:
        default:
            break;
        }
    }
    @Override
    public boolean hasRunUp() {
        return this.runUp;
    }

    // @Override
    // public void transferIn(BigInteger value) {
    // this.transferIn(value, false);
    // }

    @Override
    public void leftShift(int shift) {

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

    @Override
    public BigInteger maxValue() {
        return MAX;
    }

    @Override
    public BigInteger minValue() {
        return MIN;
    }

    @Override
    public BigInteger mostRecentValue() {
        return this.mostRecentValue;
    }

    @Override
    public void reset() {
        this.currentOperation = null;
        this.currentAxis = 0;
        this.ingressAxes = new BigInteger[3];
        this.egressAxes = new BigInteger[2];
        this.runUp = false;
    }

    @Override
    public void rightShift(int shift) {
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

    @Override
    public void setOperation(Operation operation) {
        this.currentOperation = operation;
        this.currentAxis = 0;
    }

    @Override
    public void transferIn(BigInteger value, boolean prime) {
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

    @Override
    public BigInteger transferOut() {
        return this.transferOut(false);
    }

    @Override
    public BigInteger transferOut(boolean prime) {
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
