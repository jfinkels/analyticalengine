/**
 * AnalyticalEngine.java - simulation of Babbage's Analytical Engine
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
package analyticalengine.components;

/**
 * The Analytical Engine aggregates the components of the computing system and
 * allows them to communicate with one another.
 * 
 * @author Jeffrey Finkelstein <jeffrey.finkelstein@gmail.com>
 * @since 0.1.1
 */
public interface AnalyticalEngine {
    void setAttendant(Attendant attendant);

    void setMill(Mill mill);

    void setStore(Store store);

    void setCardReader(CardReader reader);

    void setPrinter(Printer printer);

    void setCurvePrinter(CurvePrinter printer);

    void reset();
    
    /**
     * Runs the program specified by the card chain in the card reader.
     * 
     * This method sequentially executes each card in the card chain, alerting
     * the attendant if his or her action is required.
     */
    void run();
}
