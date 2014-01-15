package analyticalengine;

import java.math.BigInteger;
import java.util.Vector;

// The Mill

class Mill {
    private BigInteger[] ingress = new BigInteger[3];
    private BigInteger[] egress = new BigInteger[2];
    private BigInteger currentAxis = ingress[0];
    private final static int OP_NONE = 0, OP_ADD = 1, OP_SUBTRACT = 2,
            OP_MULTIPLY = 3, OP_DIVIDE = 4;

    private int operation = OP_NONE, opargs;
    private int index = 0;
    private boolean run_up = false, trace = false;
    private AnnunciatorPanel panel = null;
    private Attendant attendant = null;

    private final static BigInteger K10e50 = new BigInteger("100000000000000000000000000000000000000000000000000"),
            Km10e50 = K10e50.negate(), K10 = BigInteger.valueOf(10);
    private Vector<BigInteger> shiftFactor;

    public Mill(AnnunciatorPanel p, Attendant a) {
        // int i;

        panel = p;
        attendant = a;
        reset();
        shiftFactor = new Vector<BigInteger>(101);
        shiftFactor.setSize(101);
    }

    public boolean hasRunUp() {
        return run_up;
    }

    private String currentOperationString() {
        String s = null;

        switch (operation) {
        case OP_NONE:
            s = " ";
            break;
        case OP_ADD:
            s = "+";
            break;
        case OP_SUBTRACT:
            s = "-";
            break;
        case OP_MULTIPLY:
            s = "×";
            break;
        case OP_DIVIDE:
            s = "÷";
            break;
        }
        return s;
    }

    // Set ingress axis

    private void setIngress(int which, BigInteger v) {
        ingress[which] = v;
        panel.changeIngress(which, ingress[which]);
    }

    private void setIngress(int which, long v) {
        setIngress(which, BigInteger.valueOf(v));
    }

    // Set egress axis

    private void setEgress(int which, BigInteger v) {
        egress[which] = v;
        panel.changeEgress(which, egress[which]);
    }

    private void setEgress(int which, long v) {
        setEgress(which, BigInteger.valueOf(v));
    }

    // Reset the mill

    public void reset() {
        int i;

        operation = OP_NONE;
        panel.changeOperation(currentOperationString());
        for (i = 0; i < 3; i++) {
            setIngress(i, 0);
        }
        for (i = 0; i < 2; i++) {
            setEgress(i, 0);
        }
        currentAxis = BigInteger.ZERO;
        index = 0;
        run_up = false;
        panel.changeRunUp(run_up);
    }

    public void setTrace(boolean t) {
        trace = t;
    }

    public void setOperation(char which) throws IllegalArgumentException {
        switch (which) {
        case '+':
            operation = OP_ADD;
            opargs = 2;
            break;

        case '-':
            operation = OP_SUBTRACT;
            opargs = 2;
            break;

        case '×':
        case '*':
        case 'x':
            operation = OP_MULTIPLY;
            opargs = 2;
            break;

        case '÷':
        case '/':
            operation = OP_DIVIDE;
            opargs = 2;
            break;

        default:
            throw new IllegalArgumentException("Unknown Mill operation");
        }
        index = 0;
        panel.changeOperation(currentOperationString());
    }

    public void transferIn(BigInteger v, boolean upper) {
        if (upper) {
            setIngress(2, currentAxis = v);
        } else {
            setIngress(index, currentAxis = v);
            // When first ingress axis set, clear prime axis
            if (index == 0) {
                setIngress(2, 0);
            }
            index = (index + 1) % 2; // Rotate to next ingress axis
            if (index == opargs || index == 0) {
                crank();
            }
        }
    }

    public void transferIn(BigInteger v) {
        transferIn(v, false);
    }

    public void transferIn(long v) {
        transferIn(BigInteger.valueOf(v));
    }

    public BigInteger transferOut(boolean prime) {
        BigInteger b = egress[prime
                               ? 1
                                   : 0];

        currentAxis = b;
        return b;
    }

    public BigInteger transferOut() {
        return transferOut(false);
    }

    public void crank() {
        BigInteger result = null, tresult = null;
        // int i;

        run_up = false; // Reset run up lever
        switch (operation) {
        case OP_ADD:
            result = ingress[0].add(ingress[1]);
            /*
             * Check for passage through infinity (carry out) and set run up
             * lever if that has occurred. The result is then taken modulo
             * 10^50.
             */
            if (result.compareTo(K10e50) >= 0) {
                run_up = true;
                result = result.subtract(K10e50);
            } else if (ingress[0].signum() >= 0 && result.signum() < 0) {
                /*
                 * Run up gets set when the result of a addition changes the
                 * sign of the first argument. Note that since the same lever
                 * is used to indicate carry and change of sign, it is not
                 * possible to distinguish overflow from sign change.
                 */
                run_up = true;
            }
            setEgress(1, 0);
            if (trace) {
                attendant.traceLog("Mill:  " + ingress[0] + " + " + ingress[1]
                        + " = " + result + (run_up
                                                  ? " Run up"
                                                      : ""));
            }
            break;

        case OP_SUBTRACT:
            result = ingress[0].subtract(ingress[1]);
            /*
             * Check for passage through negative infinity (borrow) and set run
             * up and trim value as a result.
             */
            if (result.compareTo(Km10e50) <= 0) {
                run_up = true;
                result = result.add(K10e50).negate();
            } else if (ingress[0].signum() >= 0 && result.signum() < 0) {
                /*
                 * Run up gets set when the result of a subtraction changes the
                 * sign of the first argument. Note that since the same lever
                 * is used to indicate borrow and change of sign, it is not
                 * possible to distinguish overflow from sign change.
                 */
                run_up = true;
            }
            if (trace) {
                attendant.traceLog("Mill:  " + ingress[0] + " - " + ingress[1]
                        + " = " + result + (run_up
                                                  ? " Run up"
                                                      : ""));
            }
            setEgress(1, 0);
            break;

        case OP_MULTIPLY:
            result = ingress[0].multiply(ingress[1]);
            if (trace) {
                tresult = result;
            }
            /*
             * Check for product longer than one column and set the primed
             * egress axis to the upper part.
             */
            if (result.abs().compareTo(K10e50) > 0) {
                BigInteger qr[] = result.divideAndRemainder(K10e50);
                setEgress(1, qr[0]);
                result = qr[1];
            } else {
                setEgress(1, 0);
            }
            if (trace) {
                attendant.traceLog("Mill:  " + ingress[0] + " × " + ingress[1]
                        + " = " + tresult + (run_up
                                                   ? " Run up"
                                                       : ""));
            }
            break;

        case OP_DIVIDE:
            BigInteger dividend = ingress[0];
            BigInteger[] qr;

            if (ingress[2].signum() != 0) {
                dividend = dividend.add(ingress[2].multiply(K10e50));
            }
            if (ingress[1].signum() == 0) {
                setEgress(1, result = BigInteger.ZERO);
                run_up = true;
                break;
            }
            qr = dividend.divideAndRemainder(ingress[1]);
            if (qr[0].abs().compareTo(K10e50) > 0) {
                // Overflow if quotient more than 50 digits
                setEgress(1, result = BigInteger.ZERO);
                run_up = true;
                break;
            }
            setEgress(1, qr[0]);
            result = qr[1];
            if (trace) {
                attendant.traceLog("Mill:  " + dividend + " / " + ingress[1]
                        + " = " + qr[0] + ", Rem: " + qr[1]
                        + (run_up
                                 ? " Run up"
                                     : ""));
            }
            break;

        case OP_NONE:
            result = currentAxis;
            break;
        }
        setEgress(0, currentAxis = result);
        index = 0;
        panel.changeRunUp(run_up);
    }

    /*
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

    public void shiftAxes(int count) {
        boolean right = count < 0;
        BigInteger sf = BigInteger.ONE, value;

        /*
         * Assemble the value from the axes. For a right shift, used to
         * normalise after a fixed point multiplication, the egress axes are
         * used while for a left shift, performed before a fixed point
         * division, the two ingress axes containing the dividend are shifted.
         */

        if (right) {
            count = -count;
            value = egress[0];
            if (egress[1].signum() != 0) {
                value = value.add(egress[1].multiply(K10e50));
            }
        } else {
            value = ingress[0];
            if (ingress[2].signum() != 0) {
                value = value.add(ingress[2].multiply(K10e50));
            }
        }

        /*
         * If we don't have a ready-made shift factor in stock, create one.
         */

        if (shiftFactor.elementAt(count) == null) {
            int j;

            sf = BigInteger.ONE;
            for (j = 0; j < count; j++) {
                sf = sf.multiply(K10);
            }
            shiftFactor.setElementAt(sf, count);
        }

        /*
         * Perform the shift and put the result back where we got it.
         */

        sf = (BigInteger) shiftFactor.elementAt(count);
        if (right) {
            BigInteger[] qr;
            // BigInt sfo2;

            qr = value.divideAndRemainder(sf);
            // sfo2 = BigInt.quotient(sf, new BigInt(2));
            if (trace) {
                attendant.traceLog("Mill:  " + value + " > " + count + " = "
                        + qr[0]);
            }
            if (qr[0].compareTo(K10e50) != 0) {
                qr = qr[0].divideAndRemainder(K10e50);
                setEgress(1, qr[0]);
                setEgress(0, qr[1]);
            } else {
                setEgress(0, qr[0]);
                setEgress(1, 0);
            }
            currentAxis = egress[0];
        } else {
            BigInteger pr = value.multiply(sf);
            BigInteger[] pq;

            if (trace) {
                attendant.traceLog("Mill:  " + value + " < " + count + " = "
                        + pr);
            }
            if (pr.compareTo(K10e50) != 0) {
                pq = pr.divideAndRemainder(K10e50);
                setIngress(2, pq[0]);
                setIngress(0, pq[1]);
            } else {
                setIngress(0, pr);
                setIngress(2, 0);
            }
            currentAxis = ingress[0];
        }
    }

    public void print(OutputApparatus apparatus) {
        apparatus.Output(Attendant.editToPicture(outAxis()));
        attendant.writeEndOfItem(apparatus);
    }

    public BigInteger outAxis() {
        return currentAxis;
    }
}
