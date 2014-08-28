/**
 * Arguments.java - specifies command-line arguments
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
package analyticalengine.main;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.beust.jcommander.Parameter;

/**
 * Stores arguments parsed from the command-line.
 * 
 * An instance of this class should be provided to the constructor of the
 * {@link com.beust.jcommander.JCommander} class.
 * 
 * @author Jeffrey Finkelstein &lt;jeffrey.finkelstein@gmail.com&gt;
 * @since 0.0.1
 */
public class Arguments {

    /**
     * The list of all positional command-line arguments.
     * 
     * We expect that there is only one command-line argument (other than the
     * options): the file containing the Analytical Engine program to execute.
     */
    @Parameter(description = "<filename>")
    private List<String> args = new ArrayList<String>();

    /** Whether to display the help/usage message. */
    @Parameter(names = { "-h", "--help" }, description = "print this message",
            help = true)
    private boolean help = false;

    /**
     * A list of paths to search when interpreting a request to include a
     * library function.
     */
    @Parameter(names = { "-s", "--library-path" },
            description = "a colon-separated list of library paths",
            converter = PathConverter.class, splitter = ColonSplit.class)
    private List<Path> libraryPath = Arrays.asList(Paths.get("."));

    /**
     * Whether to list (and not run) the cards mounted by the attendant.
     */
    @Parameter(names = { "-l", "--list-only" },
            description = "list the card chain as mounted by the attendant")
    private boolean listOnly = false;

    /**
     * Whether the attendant should remove comment cards from the card chain
     * before mounting them in the card reader.
     */
    @Parameter(names = { "-c", "--strip-comments" },
            description = "remove comment cards from the card chain")
    private boolean stripComments = false;

    /** The amount of information to display while running the program. */
    @Parameter(names = { "-v", "--verbose" },
            description = "set verbosity level")
    private int verbosity = 0;

    /**
     * The positional command-line arguments (excluding the program name) that
     * are not given as options.
     * 
     * @return The position command-line arguments.
     */
    List<String> args() {
        return this.args;
    }

    /**
     * Whether to print usage information then exit.
     * 
     * @return Whether to print the usage message.
     */
    boolean help() {
        return this.help;
    }

    /**
     * A list of paths to search when looking for library functions.
     * 
     * @return A list of paths to search when looking for library functions.
     */
    List<Path> libraryPath() {
        return this.libraryPath;
    }

    /**
     * Whether to list but not run the requested program.
     * 
     * @return Whether to list but not run the requested program.
     */
    boolean listOnly() {
        return this.listOnly;
    }

    /**
     * Whether to remove comments from the program when loading the card chain.
     * 
     * @return Whether to remove comments from the loaded card chain.
     */
    boolean stripComments() {
        return this.stripComments;
    }

    /**
     * Returns the verbosity level, an integer between 0 and 2, inclusive.
     * 
     * @return The verbosity level.
     */
    int verbosity() {
        return this.verbosity;
    }
}
