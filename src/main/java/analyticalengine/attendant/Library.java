/**
 * Library.java - a library of built-in functions
 * 
 * Copyright 2014-2016 Jeffrey Finkelstein.
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

import java.nio.file.Path;
import java.util.List;

import analyticalengine.cards.Card;

/**
 * A library containing built-in functions that can be included by user
 * programs.
 * 
 * @author Jeffrey Finkelstein &lt;jeffrey.finkelstein@gmail.com&gt;
 * @since 0.0.1
 */
public interface Library {

    /**
     * Adds the specified path to the list of paths to search when including
     * external functions.
     * 
     * Multiple invocations of this method append new paths to the end of the
     * list, so paths added first will be searched first.
     * 
     * @param path
     *            A path to search for included functions.
     */
    void addLibraryPath(Path path);

    /**
     * Adds each of the specified paths to the list of paths to search when
     * including external functions.
     * 
     * The paths will be searched in the order specified by the iterator.
     * Multiple invocations of this method append new paths to the end of the
     * list.
     * 
     * @param paths
     *            A list of paths to search for included functions.
     */
    void addLibraryPaths(List<Path> paths);

    /** Empties the known library search paths. */
    void clear();

    /**
     * Returns the list of cards stored in the library file with the specified
     * name.
     * 
     * If no file extension is specified, this method searches for the file
     * with name {@code filename + ".ae"}.
     * 
     * Searches the list of paths specified previously in the
     * {@link #addLibraryPath(Path)} and {@link #addLibraryPaths(List)} methods
     * for the specified file.
     * 
     * @param filename
     *            The file for which to search.
     * @return The list of cards stored in the specified library file.
     * @throws LibraryLookupException
     *             if the specified file is not found.
     */
    List<Card> find(String filename) throws LibraryLookupException;
}
