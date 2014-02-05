package analyticalengine;

import java.nio.file.Path;
import java.util.List;

import analyticalengine.cards.Card;

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

    /** Empties the known library search paths. */
    void clear();
}
