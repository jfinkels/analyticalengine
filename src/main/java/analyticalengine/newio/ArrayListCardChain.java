/**
 * ArrayListCardChain.java - 
 *
 * Copyright 2014 Jeffrey Finkelstein.
 */
package analyticalengine.newio;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import analyticalengine.components.cards.Card;
import analyticalengine.components.cards.CardChain;

/**
 * @author Jeffrey Finkelstein <jeffrey.finkelstein@gmail.com>
 * @since 0.0.1
 */
public class ArrayListCardChain implements CardChain {

    private List<Card> cards = new ArrayList<Card>();
    
    /* (non-Javadoc)
     * @see java.lang.Iterable#iterator()
     */
    @Override
    public Iterator<Card> iterator() {
        return this.cards.iterator();
    }

    /* (non-Javadoc)
     * @see analyticalengine.components.cards.CardChain#replace(int, analyticalengine.components.cards.Card)
     */
//    @Override
//    public void replace(int index, Card newCard) {
//        this.cards.remove(index);
//        this.cards.add(index, newCard);
//    }

    /* (non-Javadoc)
     * @see analyticalengine.components.cards.CardChain#add(analyticalengine.components.cards.Card)
     */
    @Override
    public void add(Card card) {
        this.cards.add(card);
    }

    /* (non-Javadoc)
     * @see analyticalengine.components.cards.CardChain#size()
     */
    @Override
    public int size() {
        // TODO Auto-generated method stub
        return this.cards.size();
    }

    /* (non-Javadoc)
     * @see analyticalengine.components.cards.CardChain#get(int)
     */
    @Override
    public Card get(int i) {
        // TODO Auto-generated method stub
        return this.cards.get(i);
    }

    /* (non-Javadoc)
     * @see analyticalengine.components.cards.CardChain#shallowCopy()
     */
    @Override
    public CardChain shallowCopy() {
        // TODO Auto-generated method stub
        return null;
    }

    /* (non-Javadoc)
     * @see analyticalengine.components.cards.CardChain#clear()
     */
    @Override
    public void clear() {
        // TODO Auto-generated method stub
        this.cards.clear();
    }

}
