package analyticalengine.io;

/*
 * 
 * The Card Punching Apparatus
 * 
 * This class is parent to all fancier implementations of card punches. It just
 * prints the cards on standard output.
 */

public class CardPunchingApparatus extends OutputApparatus {

    // Punch a card

    public void Output(String s) {
        System.out.println(s);
    }
}
