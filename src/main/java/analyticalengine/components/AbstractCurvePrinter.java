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

package analyticalengine.components;

import java.math.BigInteger;

/**
 * @author Jeffrey Finkelstein <jeffrey.finkelstein@gmail.com>
 * @since 0.0.1
 */
public abstract class AbstractCurvePrinter implements CurvePrinter {

    private BigInteger x = BigInteger.ZERO;
    private BigInteger y = BigInteger.ZERO;

    /*
     * (non-Javadoc)
     * 
     * @see analyticalengine.components.CurvePrinter#setX(java.math.BigInteger)
     */
    @Override
    public void setX(BigInteger x) {
        this.x = x;
    }

    /*
     * (non-Javadoc)
     * 
     * @see analyticalengine.components.CurvePrinter#setY(java.math.BigInteger)
     */
    @Override
    public void setY(BigInteger y) {
        this.y = y;
    }

    protected BigInteger x() {
        return this.x;
    }

    protected BigInteger y() {
        return this.y;
    }

    /*
     * (non-Javadoc)
     * 
     * @see analyticalengine.components.CurvePrinter#reset()
     */
    @Override
    public void reset() {
        this.x = BigInteger.ZERO;
        this.y = BigInteger.ZERO;
    }

}
