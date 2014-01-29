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
package analyticalengine;

import java.io.IOException;
import java.nio.file.Path;
import java.util.List;

import analyticalengine.io.UnknownCard;

/**
 * The operator of the Analytical Engine.
 * 
 * The attendant has several roles throughout the operation of the machine. The
 * attendant
 * <ul>
 * <li>loads the program into the {@link CardReader}</li>
 * <li>receives, formats, and annotates the output from the {@link Printer}</li>
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
 * @author Jeffrey Finkelstein <jeffrey.finkelstein@gmail.com>
 * @since 0.0.1
 */
public interface Attendant {
    void annotate(String message);

    String finalReport();

    void loadProgram(List<Card> cards) throws BadCard, IOException,
            UnknownCard;

    void receiveOutput(String printed);

    void reset();

    void setCardReader(CardReader reader);

    void setFormat(String argument);

    void setLibraryPaths(List<Path> paths);

    void setStripComments(boolean stripComments);

    void writeInColumns();

    void writeInRows();

    void writeNewline();

    void writeWithDecimal();
}
