package analyticalengine;

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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import analyticalengine.cards.Card;
import analyticalengine.io.UnknownCard;

public class DefaultLibrary implements Library {

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
     * A list of paths to search when a card requesting a library function is
     * encountered.
     */
    private List<Path> paths = new ArrayList<Path>();

    /** The logger for this class. */
    private static final transient Logger LOG = LoggerFactory
            .getLogger(Library.class);

    static Path uriToPath(URI uri) throws URISyntaxException, IOException {
        String scheme = uri.getScheme();
        String spec = uri.getRawSchemeSpecificPart();
        LOG.warn("SCHEME: " + scheme);
        LOG.warn("SPEC: " + spec);

        if (scheme.equals("jar")) {
            int sep = spec.indexOf("!/");

            URI zipUri = null;
            if (sep == -1) {
                // TODO this should never happen
            } else {
                zipUri = new URI(scheme, spec.substring(0, sep), null);
            }

            try (FileSystem zipFs = FileSystems.newFileSystem(zipUri,
                    Collections.<String, Object> emptyMap())) {
                return Paths.get(uri);
            }
        } else {
            return Paths.get(new URI(scheme, spec, null)); // .toAbsolutePath();
        }
    }

    public List<Card> find(String filename) throws LibraryLookupException {
        List<Card> result = new ArrayList<Card>();

        // we assume library files have the .ae file extension
        if (!filename.endsWith(".ae")) {
            filename += ".ae";
        }

        // Get the URL of the file to load. If this class lives in an
        // executable JAR, the URL will be something like
        //
        // jar:file:/path/to/analyticalengine.jar!/analyticalengine/filename.ae
        //
        // If this class lives on a real filesystem, the URL will be something
        // like
        //
        // file:/path/to/analyticalengine/filename.ae
        URL fileurl = this.getClass().getClassLoader()
                .getResource("analyticalengine/" + filename);

        // if this file exists in the package, use it
        if (fileurl != null) {
            result.add(Card.commentCard("Begin interpolation of " + fileurl
                    + " from library by attendant"));
            try {
                URI uri = fileurl.toURI();
                String scheme = uri.getScheme();
                String spec = uri.getRawSchemeSpecificPart();

                List<Card> cardsFromFile;
                Path programPath;
                if (scheme.equals("jar")) {
                    int sep = spec.indexOf("!/");

                    URI zipUri = null;
                    if (sep == -1) {
                        // TODO this should never happen
                    } else {
                        zipUri = new URI(scheme, spec.substring(0, sep), null);
                    }

                    try (FileSystem zipFs = FileSystems.newFileSystem(zipUri,
                            Collections.<String, Object> emptyMap())) {
                        programPath = Paths.get(uri);
                        cardsFromFile = analyticalengine.io.CardReader
                                .fromPath(programPath);
                    }
                } else {
                    programPath = Paths.get(new URI(scheme, spec, null)); // .toAbsolutePath();
                    cardsFromFile = analyticalengine.io.CardReader
                            .fromPath(programPath);
                }
                result.addAll(cardsFromFile);
            } catch (URISyntaxException | IOException e) {
                throw new LibraryLookupException(
                        "Failed to open built-in file", e);
            } catch (UnknownCard e) {
                throw new LibraryLookupException(
                        "Syntax error in requested file", e);
            }
            result.add(Card.commentCard("End interpolation of " + fileurl
                    + " from library by attendant"));
            return result;
        }

        // look for the file in the known paths
        Path libraryFile = null;
        for (Path path : this.paths) {
            libraryFile = path.resolve(filename);
            if (Files.exists(libraryFile)) {
                break;
            }
            libraryFile = null;
        }

        // raise an exception if the requested library file could
        // not
        // be found in any of the known paths
        if (libraryFile == null) {
            throw new LibraryLookupException("Could not find library file: "
                    + filename);
        }
        return result;
    }

    @Override
    public void clear() {
        // TODO Auto-generated method stub

    }
}