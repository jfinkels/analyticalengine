/**
 * PathConverter.java - converts a string to a path
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

import com.beust.jcommander.IStringConverter;

/**
 * Converts a string containing a path to a path object.
 * 
 * @author Jeffrey Finkelstein <jeffrey.finkelstein@gmail.com>
 * @since 0.0.1
 */
public class PathConverter implements IStringConverter<Path> {

    /**
     * Converts a string to a path.
     * 
     * @param value
     *            A string representation of a path.
     * @return A path parsed from {@code value}.
     * @see com.beust.jcommander.IStringConverter#convert(java.lang.String)
     */
    @Override
    public Path convert(final String value) {
        return Paths.get(value);
    }

}