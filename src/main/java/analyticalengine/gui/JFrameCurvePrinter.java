/**
 * JFrameCurvePrinter.java - plots a curve in a JFrame window
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

import java.awt.BorderLayout;
import java.math.BigInteger;

import javax.swing.JFrame;

import analyticalengine.CurvePrinter;

/**
 * Plots the curve produced by the Analytical Engine in a JFrame.
 * 
 * @author Jeffrey Finkelstein &lt;jeffrey.finkelstein@gmail.com&gt;
 * @since 0.0.1
 */
public class JFrameCurvePrinter extends JFrame implements CurvePrinter {

    /** Default generated serial version UID. */
    private static final long serialVersionUID = -8540059681376020801L;

    /** The height of the canvas, in pixels. */
    public static final int HEIGHT = 400;

    /** The width of the canvas, in pixels. */
    public static final int WIDTH = 400;

    /**
     * The current polyline to which new points will be added on draw commands.
     */
    private Polyline currentPolyline;

    /** The canvas on which to plot the curve. */
    private Plotter plotter = new Plotter();

    /** The current x location of the drawing pen. */
    private BigInteger x = BigInteger.ZERO;

    /** The current y location of the drawing pen. */
    private BigInteger y = BigInteger.ZERO;

    /**
     * Instantiates this printer with a canvas on which to plot the curve.
     */
    public JFrameCurvePrinter() {
        super("Analytical Engine - Plotter");

        // Hide the frame when closed, but don't exit the program.
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Create the plotter and place it in the frame.
        this.plotter.setSize(WIDTH, HEIGHT);
        this.getContentPane().add(this.plotter, BorderLayout.CENTER);

        // Size the frame.
        this.pack();

        // The stylus starts at the origin.
        this.currentPolyline = new Polyline();
        this.currentPolyline.addPoint(BigInteger.ZERO, BigInteger.ZERO);
        this.plotter.addPolyline(this.currentPolyline);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void draw() {
        if (!this.isVisible()) {
            this.setVisible(true);
        }
        this.currentPolyline.addPoint(x, y);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void move() {
        // end the current polyline and create a new one
        this.currentPolyline = new Polyline();
        this.currentPolyline.addPoint(this.x, this.y);
        this.plotter.addPolyline(this.currentPolyline);
    }

    /**
     * {@inheritDoc}
     * 
     * @see analyticalengine.CurvePrinter#reset()
     */
    @Override
    public void reset() {
        this.x = BigInteger.ZERO;
        this.y = BigInteger.ZERO;
        this.currentPolyline = new Polyline();
        this.plotter.clear();

        // The plotter should have access to the current polyline, and the
        // current polyline should have the origin as its only point.
        this.currentPolyline.addPoint(this.x, this.y);
        this.plotter.addPolyline(this.currentPolyline);
    }

    /**
     * {@inheritDoc}
     * 
     * @param x
     *            {@inheritDoc}
     * @see analyticalengine.CurvePrinter#setX(java.math.BigInteger)
     */
    @Override
    public void setX(final BigInteger x) {
        this.x = x;
    }

    /**
     * {@inheritDoc}
     * 
     * @param y
     *            {@inheritDoc}
     * @see analyticalengine.CurvePrinter#setY(java.math.BigInteger)
     */
    @Override
    public void setY(final BigInteger y) {
        this.y = y;
    }
}
