/**
 * Plotter.java - a canvas that plots polylines
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

import java.awt.Canvas;
import java.awt.Graphics;
import java.util.ArrayList;


/**
 * A Canvas object that plots polylines.
 * 
 * @author Jeffrey Finkelstein &lt;jeffrey.finkelstein@gmail.com&gt;
 * @since 0.0.1
 */
public class Plotter extends Canvas {
    /** Default generated serial version UID. */
    private static final long serialVersionUID = -5319914534841657078L;

    /** The current list of polylines to print. */
    private ArrayList<Polyline> polylines = new ArrayList<Polyline>();

    /**
     * Adds the specified polyline to the current list of polylines to
     * draw.
     * 
     * @param polyline
     *            The polyline to draw.
     */
    public void addPolyline(final Polyline polyline) {
        this.polylines.add(polyline);
    }

    /** Clear the current list of polylines to draw. */
    public void clear() {
        this.polylines.clear();
    }

    /**
     * Paints the current list of polylines on the canvas.
     * 
     * @param g
     *            The graphics object to use to draw polylines.
     */
    @Override
    public void paint(final Graphics g) {
        for (Polyline polyline : this.polylines) {
            // only draw polylines with more than one point
            if (polyline.nPoints() > 1) {
                g.drawPolyline(polyline.xPoints(), polyline.yPoints(),
                        polyline.nPoints());
            }
        }
    }
}
