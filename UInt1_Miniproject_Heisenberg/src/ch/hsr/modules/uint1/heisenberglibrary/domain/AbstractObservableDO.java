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
package ch.hsr.modules.uint1.heisenberglibrary.domain;

import java.util.Observable;

/**
 * TODO COMMENT ME!
 * 
 * @author msyfrig
 */
public abstract class AbstractObservableDO extends Observable {

    /**
     * Creates a new instance of this class.
     * 
     */
    public AbstractObservableDO() {
    }

    /**
     * Can be called after a field has been set to automatically notifiy all
     * observers.
     * 
     * For future use if we wanna change something globally for all observable
     * objects.
     * 
     * @see AbstractObservableDO#doNotify()
     */
    public void set() {
        doNotify();
    }

    /**
     * Sets the state to changed and notifies all registered observers.
     */
    protected void doNotify() {
        setChanged();
        notifyObservers();
    }
    
    /**
     * Sets the state to changed and notifies all registered observers with anUpdatedObject.
     */
    protected void doNotify(Object anUpdatedObject) {
        setChanged();
        notifyObservers();
    }
}
