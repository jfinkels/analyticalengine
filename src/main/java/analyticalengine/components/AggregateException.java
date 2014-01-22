/**
 * AggregateException.java - 
 *
 * Copyright 2014 Jeffrey Finkelstein.
 */
package analyticalengine.components;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Jeffrey Finkelstein <jeffrey.finkelstein@gmail.com>
 * @since 0.0.1
 */
public class AggregateException extends Exception {

    /**
     * 
     */
    private static final long serialVersionUID = 7439949511528858384L;

    private List<Exception> exceptions = new ArrayList<Exception>();
    
    public int size() {
        return this.exceptions.size();
    }
    
    public void add(Exception exception) {
        this.exceptions.add(exception);
    }
}
