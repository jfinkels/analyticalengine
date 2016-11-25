/**
 * AnalyticalEngine.java - simulation of Babbage's Analytical Engine
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
package analyticalengine;

import analyticalengine.attendant.Attendant;
import analyticalengine.cards.BadCard;
import analyticalengine.components.CardReader;
import analyticalengine.components.CurvePrinter;
import analyticalengine.components.Mill;
import analyticalengine.components.Printer;
import analyticalengine.components.Store;

/**
 * The Analytical Engine aggregates the components of the computing system and
 * allows them to communicate with one another.
 * 
 * The Engine is similar to a modern processor. The {@link Mill} corresponds to
 * the arithmetic logic unit. The {@link Store} corresponds to random access
 * memory. The {@link Attendant} is a person who operates the machine (an
 * "operator" in the language of the early days of digital computers, in which
 * a programmer provided his or her program as punched cards to an operator who
 * physically inserted them in the computer).
 * 
 * When cards are mounted in it, the {@link CardReader} provides a list of
 * instructions for the Engine. Each {@link analyticalengine.cards.Card}
 * corresponds to an instruction. During execution of the program specified by
 * the mounted cards, output will be produced on the {@link Printer} (or
 * {@link CurvePrinter}, which we would call a "plotter"), possibly annotated
 * by the attendant.
 * 
 * In order to run a program given as a {@link java.util.List} of {@code Card}
 * objects, your code should first instruct the attendant to load the program
 * by invoking the {@link Attendant#loadProgram(java.util.List)} method, then
 * start the engine by invoking the {@link #run()} method. Any output produced
 * by the printer will be available from the attendant after the engine has
 * halted.
 * 
 * @author Jeffrey Finkelstein &lt;jeffrey.finkelstein@gmail.com&gt;
 * @since 0.0.1
 */
public interface AnalyticalEngine {

    /**
     * Sets the attendant, who operates the Analytical Engine.
     * 
     * @param attendant
     *            The operator of the Analytical Engine.
     */
    void setAttendant(Attendant attendant);

    /**
     * Sets the mill, which performs the arithmetic logic.
     * 
     * @param mill
     *            The arithmetic logic unit of the Engine.
     */
    void setMill(Mill mill);

    /**
     * Sets the store, which is the memory of the Engine.
     * 
     * @param store
     *            The memory of the Engine.
     */
    void setStore(Store store);

    /**
     * Sets the card reader, which provides the program for the Engine.
     * 
     * @param reader
     *            The device which provides the program for the Engine.
     */
    void setCardReader(CardReader reader);

    /**
     * Sets the printer, which produces the textual output from the Engine.
     * 
     * @param printer
     *            The device which produces the textual output from the Engine.
     */
    void setPrinter(Printer printer);

    /**
     * Sets the curve printer, which outputs plots of curves.
     * 
     * @param printer
     *            The device which plots curves.
     */
    void setCurvePrinter(CurvePrinter printer);

    /**
     * Resets each of the components of the engine to their initial states.
     * 
     * This includes clearing the store and resetting the mill.
     */
    void reset();

    /**
     * Runs the program specified by the card chain in the card reader.
     * 
     * This method sequentially executes each card in the card chain, alerting
     * the attendant if his or her action is required.
     * 
     * The mill, store, card reader, printer, and attendant properties must all
     * be set before invoking this method. Furthermore, the attendant must have
     * loaded a program into the card reader before invoking this method.
     * 
     * @throws BadCard
     *             if the Engine encounters an invalid card as it is processing
     *             the card chain.
     */
    void run() throws BadCard;
}
