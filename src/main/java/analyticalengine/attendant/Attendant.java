/**
 * Attendant.java - the operator of the Analytical Engine
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
package analyticalengine.attendant;

import java.io.IOException;
import java.util.List;

import analyticalengine.BadCard;
import analyticalengine.cards.Card;
import analyticalengine.cards.UnknownCard;
import analyticalengine.components.CardReader;
import analyticalengine.components.Printer;

/**
 * The operator of the Analytical Engine.
 * 
 * The attendant has several roles throughout the operation of the machine. The
 * attendant
 * <ul>
 * <li>loads the program into the {@link CardReader}</li>
 * <li>
 * receives, formats, and annotates the output from the {@link Printer}</li>
 * <li>maintains a library of functions</li>
 * </ul>
 * 
 * When the programmer of the Analytical Engine requests that a sequence of
 * cards be loaded into the card reader, the attendant scans the cards and
 * makes any replacements or removals as indicated by the cards (for example,
 * the attendant replaces a request for a library function with the cards for
 * that function).
 * 
 * Client code that uses classes that implement this interface must call the
 * {@link #reset()} method after each run of the Analytical Engine before any
 * subsequent runs, so that the attendant can clear the current report.
 * 
 * @author Jeffrey Finkelstein &lt;jeffrey.finkelstein@gmail.com&gt;
 * @since 0.0.1
 */
public interface Attendant {

    /**
     * Instructs the attendant to annotate the final report with the specified
     * message.
     * 
     * @param message
     *            The message to write to the final report.
     */
    void annotate(String message);

    /**
     * Gets the output from the Analytical Engine with annotations.
     * 
     * If this method is called before the program loaded in the Analytical
     * Engine has completed, then the returned report will reflect everything
     * that has been printed or annotated up to that point.
     * 
     * @return The annotated output from the Analytical Engine.
     */
    String finalReport();

    /**
     * Instructs the attendant to load the specified sequence of cards into the
     * card reader, thereby making them available to the Analytical Engine.
     * 
     * @param cards
     *            The cards representing the program that the Engine will run.
     * @throws BadCard
     *             if one of the cards is used incorrectly (that is, if there
     *             is a syntax error in the program specified by the card
     *             chain)
     * @throws IOException
     *             if the attendant fails to locate the cards requested from an
     *             "include" card.
     * @throws UnknownCard
     *             if a card specified in an "included" file has incorrect
     *             syntax.
     * @throws LibraryLookupException
     *             if a request to load a function from a library file fails.
     */
    void loadProgram(List<Card> cards) throws BadCard, IOException,
            UnknownCard, LibraryLookupException;

    /**
     * Instructs the attendant to record the printed output from the Engine's
     * printer.
     * 
     * @param printed
     *            The output from the printer.
     */
    void receiveOutput(String printed);

    /**
     * Clears the current report and resets the card reader.
     */
    void reset();

    /**
     * Sets the card reader in which the attendant will load cards.
     * 
     * @param reader
     *            The device in which the attendant will load cards.
     */
    void setCardReader(CardReader reader);

    /**
     * Instructs the attendant to record output from the printer in a specific
     * format.
     * 
     * The currently accepted format string is specified (informally) <a
     * href="http://fourmilab.ch/babbage/cards.html#numeric">here</a>.
     * 
     * @param argument
     *            The format in which to write the ouput from the printer.
     */
    void setFormat(String argument);

    /**
     * Specifies the library to use when interpreting requests to include
     * built-in functions.
     * 
     * @param library
     *            The library containing built-in functions.
     */
    void setLibrary(Library library);

    /**
     * Instructs the attendant to remove comment cards when loading a program
     * into the card reader.
     * 
     * @param stripComments
     *            Whether to remove comment cards when loading a program.
     */
    void setStripComments(boolean stripComments);

    /**
     * Instructs the attendant to format the output from the printer either in
     * columns or in rows.
     * 
     * Writing in "rows" means automatically writing a new line after each
     * string received from the printer. Writing in "columns" means that a new
     * line will only be written in the final report when explicitly requested
     * by a "A write new line" card. (This is confusing, but this is how the
     * original code did it.)
     * 
     * @param direction
     *            The direction in which to write (rows or columns).
     */
    void writeInDirection(WriteDirection direction);

    /**
     * Instructs the attendant to write any following output from the printer
     * on a new line.
     */
    void writeNewline();
}
