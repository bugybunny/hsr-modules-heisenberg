/*
	This program is free software: you can redistribute it and/or modify
	it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package ch.hsr.modules.uint1.heisenberglibrary.model;

/**
 * Class containing all enums to tag a notify event by an observable what has
 * been changed.
 * 
 * @author msyfrig
 */
public class ModelChangeTypeEnums {
    public enum Book implements ModelChangeType {
        ADDED, REMOVED, NUMBER, TITLE, AUTHOR, PUBLISHER, SHELF, EVERYTHING_CHANGED;
    }

    public enum Copy implements ModelChangeType {
        ADDED, REMOVED, NUMBER, CONDITION, EVERYTHING_CHANGED;
    }

    public enum Loan implements ModelChangeType {
        ADDED, REMOVED, NUMBER, ACTIVE_NUMBER, PICKUP_DATE, EVERYTHING_CHANGED;
    }

    public enum Customer implements ModelChangeType {
        ADDED, REMOVED, NUMBER, NAME, SURNAME, STREET, CITY, ZIP, ADDRESS, EVERYTHING_CHANGED;
    }
}
