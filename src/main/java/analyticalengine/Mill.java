/**
 * Mill.java - the arithmetic logic unit of the engine
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

import java.math.BigInteger;

// the mill is the ALU of the engine
public interface Mill {
    boolean hasRunUp();

    void reset();

    void leftShift(int shift);

    BigInteger maxValue();

    BigInteger minValue();

    void rightShift(int shift);

    void setOperation(Operation operation);

    // void transferIn(BigInteger value);

    void transferIn(BigInteger value, boolean prime);

    BigInteger transferOut();

    BigInteger transferOut(boolean prime);

    BigInteger mostRecentValue();
}
