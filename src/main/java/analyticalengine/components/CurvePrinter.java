/**
 * CurvePrinter.java - device that plots curves on Euclidean plane
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

/**
 * Device that plots curves produced by the Analytical Engine.
 * 
 * @author Jeffrey Finkelstein &lt;jeffrey.finkelstein@gmail.com&gt;
 * @since 0.0.1
 */
public interface CurvePrinter {
    /**
     * Sets the x coordinate of the plotting pen.
     * 
     * @param x
     *            The x coordinate of the plotting pen.
     */
    void setX(BigInteger x);

    /**
     * Sets the y coordinate of the plotting pen.
     * 
     * @param y
     *            The y coordinate of the plotting pen.
     */
    void setY(BigInteger y);

    /**
     * Draws to the current location specified by the x and y coordinates.
     */
    void draw();

    /**
     * Moves the pen (without drawing) to the current location specified by the
     * x and y coordinates.
     */
    void move();

    /**
     * Resets the position of the pen to the origin and clears the current
     * plot.
     */
    void reset();
}
