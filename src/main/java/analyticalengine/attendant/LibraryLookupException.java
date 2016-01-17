/**
 * LibraryLookupException.java - raised when including library file fails
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

/**
 * Raised when a request to include a file from the library of built-in
 * functions fails.
 * 
 * @author Jeffrey Finkelstein &lt;jeffrey.finkelstein@gmail.com&gt;
 * @since 0.0.1
 */
public class LibraryLookupException extends Exception {

    /** Default generated serial version UID. */
    private static final long serialVersionUID = 8289739223720835497L;

    /**
     * Instantiates this exception with the specified error message.
     * 
     * @param message
     *            The error message.
     */
    public LibraryLookupException(final String message) {
        super(message);
    }

    /**
     * Instantiates this exception with the specified error message and the
     * specified exception that caused the exception.
     * 
     * @param message
     *            The error message.
     * @param cause
     *            The throwable that caused this exception.
     */
    public LibraryLookupException(final String message,
            final Throwable cause) {
        super(message, cause);
    }

}
