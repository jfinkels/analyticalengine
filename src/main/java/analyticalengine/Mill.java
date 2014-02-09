/**
 * Mill.java - the arithmetic logic unit of the engine
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
 * Device that performs the arithmetic operations required by the Analytical
 * Engine.
 * 
 * The mill is the arithmetic logic unit of the Engine. Client code must use
 * instances of this interface as follows.
 * 
 * First, specify the operation by calling the {@link #setOperation(Operation)}
 * method.
 * 
 * Second, transfer in the two operands by calling the
 * {@link #transferIn(BigInteger)} method. If a double-width operand is
 * required, transfer in the high-order digits of the operand by calling the
 * {@link #transferIn(BigInteger, boolean)} with second argument set to
 * {@code true}. After transferring in both arguments, the result will be
 * computed implicitly (without any other explicit method invocation).
 * 
 * Finally, get the result of the operation by calling the
 * {@link #transferOut()} method. If the result has double-width, call the
 * {@link #transferOut(boolean)} with its argument set to {@code true} to get
 * the high order digits of the result.
 * 
 * <h4>Left and right arithmetic shifts</h4>
 * 
 * <em>John Walker, the author of the original code from which this simulation
 * is based, had the following comments about left and right shifts.</em>
 * 
 * In Section 1.[5] of his 26 December 1837 "On the Mathematical Powers of the
 * Calculating Engine", Babbage remarks: "The termination of the Multiplication
 * arises from the action of the Counting apparatus which at a certain time
 * directs the barrels to order the product thus obtained to be stepped down so
 * the decimal point may be in its proper place,...", which implies a right
 * shift as an integral part of the multiply operation. This makes enormous
 * sense, since it would take only a tiny fraction of the time a full-fledged
 * divide would require to renormalise the number. I have found no description
 * in this or later works of how the number of digits to shift was to be
 * conveyed to the mill. So, I am introducing a rather curious operation for
 * this purpose. Invoked after a multiplication, but before the result has been
 * emitted from the egress axes, it shifts the double-length product right by a
 * fixed number of decimal places, and leaves the result in the egress axes.
 * Thus, to multiple V11 and V12, scale the result right 10 decimal places, and
 * store the scaled product in V10, one would write:
 * 
 * <pre>
 * {@code * L011 L012 &gt;10 S010}
 * </pre>
 * 
 * Similarly, we provide a left shift for prescaling fixed point dividends
 * prior to division; this operation shifts the two ingress axes containing the
 * dividend by the given amount, and must be done after the ingress axes are
 * loaded but before the variable card supplying the divisor is given. For
 * example, if V11 and V12 contain the lower and upper halves of the quotient,
 * respectively, and we wish to shift this quantity left 10 digits before
 * dividing by the divisor in V13, we use:
 * 
 * <pre>
 * {@code / L011 L012' &lt;10 L013 S010}
 * </pre>
 * 
 * Note that shifting does not change the current operation for which the mill
 * is set; it merely shifts the axes in place.
 * 
 * @author Jeffrey Finkelstein <jeffrey.finkelstein@gmail.com>
 * @since 0.0.1
 */
public interface Mill {

    /**
     * Whether the most recent arithmetic operation resulted in an either an
     * overflow or a sign change.
     * 
     * There is no way to know which occurred without also knowing the
     * operation and operands.
     * 
     * @return {@code true} if and only if an overflow or sign change occurred
     *         when computing the result of the most recent operation.
     */
    boolean hasRunUp();

    /**
     * Clears the run up lever, currently set operation, and ingress and egress
     * axes.
     */
    void reset();

    /**
     * Performs a left shift on the ingress axes by the specified number of
     * digits.
     * 
     * This method shifts the ingress axes only because a left shift is
     * performed before a floating point division operation.
     * 
     * This method makes the return value of {@link #mostRecentValue()} to be
     * the value stored in the main egress axis (after the shift).
     * 
     * @param shift
     *            The number of digits by which to shift to the left.
     * @throws IllegalArgumentException
     *             if the specified shift value is negative, or would certainly
     *             cause an overflow.
     */
    void leftShift(int shift);

    /**
     * Performs a right shift on the egress axes by the specified number of
     * digits.
     * 
     * This method shifts the egress axes only because a right shift is
     * performed after a floating point multiplication operation.
     * 
     * This method makes the return value of {@link #mostRecentValue()} to be
     * the value stored in the main egress axis (after the shift).
     * 
     * @param shift
     *            The number of digits by which to shift to the right.
     * @throws IllegalArgumentException
     *             if the specified shift value is negative, or would certainly
     *             cause an overflow.
     */
    void rightShift(int shift);

    /**
     * Specifies the operation to perform on the next two integers loaded into
     * the mill.
     * 
     * @param operation
     *            The operation to be performed.
     */
    void setOperation(Operation operation);

    /**
     * Transfers the specified value into one of the main ingress axes.
     * 
     * The currently available ingress axis switches implicitly each time this
     * method is called. For example, the first invocation sets the first
     * ingress axis and the second sets the second ingress axis. After that,
     * the first ingress axis will be the next one available, and the pattern
     * repeats.
     * 
     * This is a convenience method for
     * {@link #transferIn(BigInteger, boolean)} where the second argument is
     * {@code false}.
     * 
     * This method makes the return value of {@link #mostRecentValue()} to be
     * the value stored in the main ingress axis (after the transfer).
     * 
     * @param value
     *            The value to load into the currently available ingress axis.
     * @throws IllegalArgumentException
     *             if the value is too large or too small to be loaded into the
     *             mill.
     */
    void transferIn(BigInteger value);

    /**
     * Transfers the specified value into one of the main ingress axes, or the
     * prime ingress axis if {@code prime} is {@code true}.
     * 
     * Unless the second argument is {@code true}, the currently available
     * ingress axis switches implicitly each time this method is called. For
     * example, the first invocation sets the first ingress axis and the second
     * sets the second ingress axis. After that, the first ingress axis will be
     * the next one available, and the pattern repeats.
     * 
     * This method makes the return value of {@link #mostRecentValue()} to be
     * the value stored in the main ingress axis (after the shift).
     * 
     * @param value
     *            The value to load into the currently available ingress axis.
     * @param prime
     *            If this is {@code true} the prime ingress axis is set instead
     *            of one of the main ingress axes.
     * @throws IllegalArgumentException
     *             if the value is too large or too small to be loaded into the
     *             mill.
     */
    void transferIn(BigInteger value, boolean prime);

    /**
     * Returns the result of the most recently computed arithmetic operation
     * (that is, the value stored in the main egress axis).
     * 
     * This is a convenience method for {@link #transferOut(boolean)} with an
     * argument of {@code false}
     * 
     * @return The value stored in the main egress axis.
     */
    BigInteger transferOut();

    /**
     * Returns the result of the most recently computed arithmetic operation.
     * 
     * If {@code prime} is true, this returns the value stored in the prime
     * egress axis. Otherwise, it returns the value stored in the main egress
     * axis.
     * 
     * @param prime
     *            Whether to return the value of the prime egress axis.
     * @return The value stored in the main egress axis, or the prime egress
     *         axis if {@code prime} is {@code true}.
     */
    BigInteger transferOut(boolean prime);

    /**
     * The "most recently used" value, which may be the most recently
     * transferred value (in or out), the result of a computation, or the
     * result of a shift.
     * 
     * @return The "most recently used" integer.
     */
    BigInteger mostRecentValue();
}
