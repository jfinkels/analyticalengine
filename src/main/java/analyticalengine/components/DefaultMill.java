package analyticalengine.components;

import java.math.BigInteger;

public class DefaultMill implements Mill {
    public static final BigInteger MAX = BigInteger.TEN.pow(50);
    public static final BigInteger MIN = MAX.negate();
    private Operation currentOperation = null;

    @Override
    public void reset() {
        this.currentOperation = null;
        this.currentAxis = 0;
        this.ingressAxes = new BigInteger[3];
        this.egressAxes = new BigInteger[2];
        this.runUp = false;
    }

    @Override
    public BigInteger maxValue() {
        return MAX;
    }

    @Override
    public BigInteger minValue() {
        return MIN;
    }

    private int currentAxis = 0;
    private BigInteger[] ingressAxes = new BigInteger[3];
    private BigInteger[] egressAxes = new BigInteger[2];
    private boolean runUp;

    // @Override
    // public void transferIn(BigInteger value) {
    // this.transferIn(value, false);
    // }

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
            result = this.egressAxes[2];
        }
        this.mostRecentValue = result;
        return result;
    }

    @Override
    public boolean hasRunUp() {
        return this.runUp;
    }

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
        case NOOP:
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
        default:
            break;
        }
    }

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
    public void leftShift(int shift) {
        // TODO Auto-generated method stub

    }

    @Override
    public void rightShift(int shift) {
        // TODO Auto-generated method stub

    }

    // TODO set this after each operation, each shift, and each transfer
    private BigInteger mostRecentValue = null;

    @Override
    public BigInteger mostRecentValue() {
        return this.mostRecentValue;
    }
}
