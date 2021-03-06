/**
 * NullCurvePrinter.java - a plotter that does nothing
 * 
 * Copyright 2014-2016 Jeffrey Finkelstein.
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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * A plotter that does nothing.
 * 
 * This should be used in an environment where the user will ignore or be
 * unable to access plotter output from the Analytical Engine.
 * 
 * @author Jeffrey Finkelstein &lt;jeffrey.finkelstein@gmail.com&gt;
 * @since 0.0.1
 */
public class NullCurvePrinter implements CurvePrinter {

    /** The logger for this class. */
    private static final transient Logger LOG = LoggerFactory
            .getLogger(NullCurvePrinter.class);

    /**
     * Does nothing.
     * 
     * @see analyticalengine.components.CurvePrinter#draw()
     */
    @Override
    public void draw() {
        LOG.debug("ignoring draw command");
    }

    /**
     * Does nothing.
     * 
     * @see analyticalengine.components.CurvePrinter#move()
     */
    @Override
    public void move() {
        LOG.debug("ignoring move command");
    }

    /**
     * Does nothing.
     * 
     * @see analyticalengine.components.CurvePrinter#reset()
     */
    @Override
    public void reset() {
        LOG.debug("ignoring reset command");
    }

    /**
     * Does nothing.
     * 
     * @param x
     *            {@inheritDoc}
     * 
     * @see analyticalengine.components.CurvePrinter#setX(java.math.BigInteger)
     */
    @Override
    public void setX(final BigInteger x) {
        LOG.debug("ignoring setX command");
    }

    /**
     * Does nothing.
     * 
     * @param y
     *            {@inheritDoc}
     * 
     * @see analyticalengine.components.CurvePrinter#setY(java.math.BigInteger)
     */
    @Override
    public void setY(final BigInteger y) {
        LOG.debug("ignoring setY command");
    }

}
