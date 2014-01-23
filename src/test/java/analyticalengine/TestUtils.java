/**
 * TestUtils.java - utility methods for JUnit tests
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

/**
 * Utilities for testing, including a method which fails a test after
 * outputting an Exception's message.
 * 
 * @author Jeffrey Finkelstein <jeffrey.finkelstein@gmail.com>
 * @since 0.0.1
 */
public final class TestUtils {

    /**
     * The message produced when {@link #shouldHaveThrownException()} is
     * called.
     */
    private static final String EXCEPTION_MSG = "Exception should have been thrown on previous line.";

    /**
     * Prints the stack trace of the specified Exception to stderr and calls
     * {@link org.junit.Assert#fail(String)} in order to fail the test in which
     * this method is called.
     * 
     * @param exception
     *            The Exception which caused the test failure.
     */
    public static synchronized void fail(final Throwable exception) {
        exception.printStackTrace(System.err);
        org.junit.Assert.fail(exception.getMessage());
    }

    /**
     * Fails the test in which this method is called with the message that an
     * Exception should have been thrown on the previous line.
     */
    public static void shouldHaveThrownException() {
        org.junit.Assert.fail(EXCEPTION_MSG);
    }

    /** Instantiation disallowed. */
    private TestUtils() {
        // intentionally unimplemented
    }
}
