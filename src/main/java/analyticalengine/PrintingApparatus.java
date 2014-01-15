package analyticalengine;

/*
 * 
 * The Printing Apparatus
 * 
 * This class is parent to all fancier implementations of printers. It just
 * prints on standard output.
 */

public class PrintingApparatus extends OutputApparatus {

    // Print a string

    public void Output(String s) {
        System.out.print(s);
    }
}
