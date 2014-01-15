package analyticalengine.main.applet;

//  Web Applet Curve Drawing Apparatus


import java.awt.Canvas;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.Polygon;
import java.util.Enumeration;
import java.util.Vector;

import analyticalengine.AnnunciatorPanel;
import analyticalengine.Attendant;
import analyticalengine.BigInt;
import analyticalengine.CurveDrawingApparatus;

class WebCurvePlot extends Canvas {
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	AnnunciatorPanel panel;
    Attendant attendant;
    Polygon p = null;
    Dimension s;
    private final static BigInt //1234567890123456789012345678901234567890
        K10e25 = BigInt.valueOf("10000000000000000000000000"),
        Kround = BigInt.valueOf( "5000000000000000000000000");
    Vector<Polygon> pvect = new Vector<Polygon>();
    boolean isPenDown = true;
    Color penColour = Color.black;
    int ax, ay;

    //  Constructor

    WebCurvePlot(AnnunciatorPanel p, Attendant a) {
        panel = p;
        attendant = a;
    }

    private static final int scaleNum(BigInt v, int scale) {
//System.out.println("Scale in: " + v + " scale = " + scale);
        v = BigInt.add(v.multiply(scale / 2), Kround.multiply(v.test()));
//System.out.println(v);
        v = BigInt.quotient(v, K10e25);
//System.out.println(v);
//System.out.println("Pixel: " + (v.intValue() + (scale / 2)));
        return v.intValue() + (scale / 2);
    }

    public void paint(Graphics g) {
        Enumeration<Polygon> e = pvect.elements();

        g.setColor(penColour);
        while (e.hasMoreElements()) {
            g.drawPolygon((Polygon) e.nextElement());
        }
    }

    public void penDown() {
        isPenDown = true;
    }

    public void penUp() {
        if (isPenDown) {
            p = null;
        }
    }

    public void addPoint(BigInt px, BigInt py) {
        int ax, ay;

        s = getSize();
        ax = scaleNum(px, s.width);
        ay = s.height - scaleNum(py, s.height);
        if (isPenDown) {
            if (p == null) {
                p = new Polygon();
                pvect.addElement(p);
                p.addPoint(ax, ay);
            }
            p.addPoint(ax, ay);
            invalidate();
        }
    }

    public void setPenColour(Color c) {
        penColour = c;
    }
}

class WebFrameCurveDrawingApparatus extends CurveDrawingApparatus {
    Frame f = null;
    WebCurvePlot cp;

    WebFrameCurveDrawingApparatus(AnnunciatorPanel p, Attendant a) {
        super(p, a);
    }

    WebFrameCurveDrawingApparatus() {
        super();
    }

    protected boolean initialised() {
        if (f == null) {
            f = new Frame("Curve Drawing Apparatus");
            f.add("Center", cp = new WebCurvePlot(panel, attendant));
            cp.setSize(400, 400);
            f.pack();
            f.setVisible(true);
        }
        return super.initialised();
    }

    public synchronized void dispose() {
        if (f != null) {
            f.dispose();
            f = null;
        }
    }

    public void moveTo() {
        if (initialised()) {
            cp.penUp();
//          cp.addPoint(px, py);
        }
    }

    public void drawTo() {
        if (initialised()) {
            cp.penDown();
            cp.addPoint(px, py);
            cp.repaint();
        }
    }

    public void changePen(Color c) {
        if (initialised()) {
            cp.setPenColour(c);
        }
    }

    public void changePaper() {
        if (f != null) {
            f.dispose();
            f = null;
        }
    }
}
