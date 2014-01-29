/**
 * AbstractCurvePrinter.java - base class for curve printers
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
 * Curve printer that stores x and y coordinates.
 * 
 * @author Jeffrey Finkelstein <jeffrey.finkelstein@gmail.com>
 * @since 0.0.1
 */
public abstract class AbstractCurvePrinter implements CurvePrinter {

    /** The current x location of the drawing pen. */
    private BigInteger x = BigInteger.ZERO;
    /** The current y location of the drawing pen. */
    private BigInteger y = BigInteger.ZERO;

    /**
     * {@inheritDoc}
     * 
     * @param x
     *            {@inheritDoc}
     * @see analyticalengine.components.CurvePrinter#setX(java.math.BigInteger)
     */
    @Override
    public void setX(BigInteger x) {
        this.x = x;
    }

    /**
     * {@inheritDoc}
     * 
     * @param x
     *            {@inheritDoc}
     * @see analyticalengine.components.CurvePrinter#setY(java.math.BigInteger)
     */
    @Override
    public void setY(BigInteger y) {
        this.y = y;
    }

    /**
     * Gets the x location of the drawing pen.
     * 
     * @return The x location of the drawing pen.
     */
    protected BigInteger x() {
        return this.x;
    }

    /**
     * Gets the y location of the drawing pen.
     * 
     * @return The y location of the drawing pen.
     */
    protected BigInteger y() {
        return this.y;
    }

    /**
     * {@inheritDoc}
     * 
     * @see analyticalengine.components.CurvePrinter#reset()
     */
    @Override
    public void reset() {
        this.x = BigInteger.ZERO;
        this.y = BigInteger.ZERO;
    }

}
