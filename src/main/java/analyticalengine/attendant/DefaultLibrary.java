/**
 * DefaultLibrary.java - basic implementation of library of built-in functions
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
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.FileSystem;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import analyticalengine.cards.Card;
import analyticalengine.cards.UnknownCard;

/**
 * A basic implementation of a library of built-in functions.
 * 
 * @author Jeffrey Finkelstein &lt;jeffrey.finkelstein@gmail.com&gt;
 * @since 0.0.1
 */
public class DefaultLibrary implements Library {

    /**
     * A list of paths to search when a card requesting a library function is
     * encountered.
     */
    private List<Path> paths = new ArrayList<Path>();

    /**
     * {@inheritDoc}
     * 
     * @param path
     *            {@inheritDoc}
     */
    @Override
    public void addLibraryPath(final Path path) {
        this.paths.add(path);
    }

    /**
     * {@inheritDoc}
     * 
     * @param paths
     *            {@inheritDoc}
     */
    @Override
    public void addLibraryPaths(final List<Path> paths) {
        this.paths.addAll(paths);
    }

    /**
     * Returns the cards contained in the program at the specified URL.
     * 
     * @param fileurl
     *            The URL of the file containing the cards to return.
     * @return The cards contained in the program at the specified URL.
     * @throws URISyntaxException
     *             if the given URL has an unknown or invalid syntax.
     * @throws IOException
     *             if there is a problem reading the library file, or the JAR
     *             containing the library file.
     * @throws UnknownCard
     *             if the requested library file has a syntax error.
     * @throws IllegalArgumentException
     *             if the requested URL indicates a JAR file instead of a file
     *             within a JAR file.
     */
    private List<Card> cardsFromResource(final URL fileurl)
            throws URISyntaxException, IOException, UnknownCard {
        // Create some cards which will be placed at the beginning and end of
        // the card chain to indicate to the user that the intermediate cards
        // come from an included library file.
        Card initialComment = Card.commentCard("Begin interpolation of "
                + fileurl + " from library by attendant");
        Card terminalComment = Card.commentCard("End interpolation of "
                + fileurl + " from library by attendant");

        // Get the scheme and scheme-specific part of the given URL. Getting
        // the raw scheme-specific part (that is, without URL-encoded
        // characters) seems to be necessary due to limitations in Java.
        URI uri = fileurl.toURI();
        String scheme = uri.getScheme();
        String spec = uri.getRawSchemeSpecificPart();

        // If the scheme indicates that the file is inside a JAR file, as
        // might happen if this code is running from an executable JAR and the
        // requested library file is contained within the JAR, do some black
        // magic to open the JAR file and read the program from a file
        // contained within it.
        if (scheme.equals("jar")) {

            // The part after the "!/" is the path to the requested file
            // relative to the root of the JAR file (which is just a zipped
            // directory).
            int sep = spec.indexOf("!/");

            if (sep == -1) {
                throw new IllegalArgumentException(
                        "Cannot load a JAR file directly.");
            }
            URI zipUri = new URI(scheme, spec.substring(0, sep), null);

            try (FileSystem zipFs = FileSystems.newFileSystem(zipUri,
                    Collections.<String, Object> emptyMap())) {
                Path programPath = Paths.get(uri);
                List<Card> result = new ArrayList<Card>();
                result.add(initialComment);
                for (String line : Files.readAllLines(programPath)) {
                    result.add(Card.fromString(line));
                }
                result.add(terminalComment);
                return result;
            }
        }

        // The scheme did not indicate a JAR file, so we get the path to the
        // file as usual, without any black magic.
        Path programPath = Paths.get(new URI(scheme, spec, null));
        List<Card> result = new ArrayList<Card>();
        result.add(initialComment);
        for (String line : Files.readAllLines(programPath)) {
            result.add(Card.fromString(line));
        }
        result.add(terminalComment);
        return result;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void clear() {
        this.paths.clear();
    }

    /**
     * {@inheritDoc}
     * 
     * @param filename
     *            {@inheritDoc}
     * @return {@inheritDoc}
     * @throws LibraryLookupException
     *             {@inheritDoc}
     */
    public List<Card> find(final String filename)
            throws LibraryLookupException {
        // we assume library files have the .ae file extension
        String fileWithExt = filename;
        if (!fileWithExt.endsWith(".ae")) {
            fileWithExt += ".ae";
        }

        /*
         * Check if the file to load lives in the current JAR file, or the
         * current directory in which this code is being executed. If this
         * class lives in an executable JAR, the URL will be something like
         * 
         * jar:file:/path/to/analyticalengine.jar!/analyticalengine/filename.ae
         * 
         * If this class lives on a real filesystem, the URL will be something
         * like
         * 
         * file:/path/to/analyticalengine/filename.ae
         */
        URL fileurl = this.getClass().getClassLoader()
                .getResource("analyticalengine/" + fileWithExt);

        // If the file exists as a resource available to the class loader, then
        // load the cards from that location.
        if (fileurl != null) {
            try {
                return this.cardsFromResource(fileurl);
            } catch (URISyntaxException | IOException | UnknownCard e) {
                throw new LibraryLookupException(
                        "Failed to load library file", e);
            }
        }

        // If the above resource did not exist, look for the file in the list
        // of known paths.
        Optional<Path> filePath = this.findInPath(fileWithExt);

        // Raise an exception if the requested library file could not be found
        // in any of the known paths.
        if (!filePath.isPresent()) {
            throw new LibraryLookupException("Could not find library file: "
                    + fileWithExt);
        }

        // If the file was found somewhere in one of the library paths, load
        // the program directly from that file.
        Path path = filePath.get();
        List<Card> cards = new ArrayList<Card>();
        try {
            for (String line : Files.readAllLines(path)) {
                cards.add(Card.fromString(line));
            }
        } catch (IOException | UnknownCard e) {
            throw new LibraryLookupException("Failed to load library file", e);
        }
        return cards;
    }

    /**
     * Searches the known library directories for file with the specified name.
     * 
     * @param filename
     *            The basename of the file to search for.
     * @return The path to the first occurrence of a file with the specified
     *         name, or an empty {@link Optional} if no such file could be
     *         found.
     */
    private Optional<Path> findInPath(final String filename) {
        for (Path path : this.paths) {
            Path libraryFile = path.resolve(filename);
            if (Files.exists(libraryFile)) {
                return Optional.of(libraryFile);
            }
        }
        return Optional.empty();
    }
}
