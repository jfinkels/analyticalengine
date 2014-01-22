/**
 * CardChain.java -
 * 
 * Copyright 2014 Jeffrey Finkelstein.
 */
package analyticalengine.components.cards;

/**
 * @author Jeffrey Finkelstein <jeffrey.finkelstein@gmail.com>
 * @since 0.0.1
 */
public interface CardChain extends Iterable<Card> {
    //void replace(int index, Card newCard);

    void add(Card card);

    CardChain shallowCopy();

    void clear();

    /**
     * @return
     */
    int size();

    /**
     * @param i
     * @return
     */
    Card get(int i);
}
