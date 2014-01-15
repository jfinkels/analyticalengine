package analyticalengine;

import java.math.BigInteger;
import java.util.Vector;

// The Store

class Store {
    private Vector<BigInteger> rack; // Rack of variable columns
    AnnunciatorPanel panel;
    Attendant attendant;
    private boolean trace = false;

    public Store(AnnunciatorPanel p, Attendant a) {
        panel = p;
        attendant = a;
        reset();
    }

    public void set(int which, BigInteger v) {
        rack.setElementAt(v, which);
        panel.changeStoreColumn(which, rack);
        if (trace) {
            attendant.traceLog("Store: V" + which + " = " + v);
        }
    }

    public void set(int which, long v) {
        set(which, BigInteger.valueOf(v));
    }

    public BigInteger get(int which) {
        BigInteger v;

        if (rack.elementAt(which) == null) {
            set(which, 0);
        }
        v = (BigInteger) rack.elementAt(which);
        if (trace) {
            attendant.traceLog("Store: Mill <= V" + which + "(" + v + ")");
        }
        return v;
    }

    public void reset() {
        rack = new Vector<BigInteger>(1000);
        rack.setSize(1000);
        panel.changeStoreColumn(-1, rack);
    }

    public void setTrace(boolean t) {
        trace = t;
    }
}
