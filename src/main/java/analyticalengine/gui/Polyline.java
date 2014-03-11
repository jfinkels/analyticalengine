/**
 * Polyline.java - a sequence of line segments with adjoining endpoints
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
package analyticalengine.gui;

import java.io.Serializable;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * An sequence of line segments with adjoining endpoints.
 * 
 * @author Jeffrey Finkelstein <jeffrey.finkelstein@gmail.com>
 * @since 0.0.1
 */
public class Polyline implements Serializable {

    /** Default generated serial version UID. */
    private static final long serialVersionUID = -7306563886042736197L;

    /** The logger for this class. */
    public static final transient Logger LOG = LoggerFactory
            .getLogger(Polyline.class);

    /** The height of the canvas, in pixels. */
    public static final int HEIGHT = 400;

    /** The width of the canvas, in pixels. */
    public static final int WIDTH = 400;

    /** The scale by which to shrink current x and y coordinates. */
    public static final BigInteger SCALE = BigInteger.TEN.pow(25);

    /** Half of the scale. */
    public static final BigInteger HALFSCALE = SCALE.divide(BigInteger
            .valueOf(2));

    /** The number of vertices in this polyline. */
    private int nPoints = 0;

    /** The list of x coordinates. */
    private List<Integer> xPoints = new ArrayList<Integer>();

    /** The list of y coordinates. */
    private List<Integer> yPoints = new ArrayList<Integer>();

    /**
     * Scales the specified value by the given scaling factor.
     * 
     * @param value
     *            The coordinate to scale.
     * @param scale
     *            The scaling factor (horizontal or vertical).
     * @return The scaled coordinate value.
     */
    private int scaled(final BigInteger value, final int scale) {
        BigInteger halfScale = BigInteger.valueOf(scale / 2);
        BigInteger sign = BigInteger.valueOf(value.signum());
        BigInteger result = value.multiply(halfScale).add(
                HALFSCALE.multiply(sign));
        result = result.divide(SCALE);
        return result.intValue() + scale / 2;
    }

    /**
     * Adds the specified point to this polyline after scaling it to the size
     * of the canvas.
     * 
     * @param x
     *            The unscaled x coordinate.
     * @param y
     *            The unscaped y coordinate.
     */
    void addPoint(final BigInteger x, final BigInteger y) {
        int scaledX = this.scaled(x, WIDTH);
        int scaledY = HEIGHT - this.scaled(y, HEIGHT);

        LOG.debug("Adding point {}, {}", scaledX, scaledY);

        this.xPoints.add(scaledX);
        this.yPoints.add(scaledY);
        this.nPoints++;
    }

    /**
     * Gets the total number of points in this polyline.
     * 
     * @return The total number of points in this polyline.
     */
    int nPoints() {
        return this.xPoints.size();
    }

    /**
     * Gets the list of x coordinates of this polyline.
     * 
     * @return The list of x coordinates of this polyline.
     */
    int[] xPoints() {
        int[] result = new int[this.nPoints];
        for (int i = 0; i < this.nPoints; i++) {
            result[i] = this.xPoints.get(i);
        }
        return result;
    }

    /**
     * Gets the list of y coordinates of this polyline.
     * 
     * @return The list of y coordinates of this polyline.
     */
    int[] yPoints() {
        int[] result = new int[this.nPoints];
        for (int i = 0; i < this.nPoints; i++) {
            result[i] = this.yPoints.get(i);
        }
        return result;
    }
}
