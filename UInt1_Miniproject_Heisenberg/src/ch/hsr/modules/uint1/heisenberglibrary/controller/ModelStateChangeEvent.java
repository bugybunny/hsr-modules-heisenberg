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
package ch.hsr.modules.uint1.heisenberglibrary.controller;

import javax.swing.event.ChangeEvent;

/**
 * ModelStateChangeEvent is used to notify interested parties that the state of
 * a model has changed in the event source.
 * 
 * @author msyfrig
 */
public class ModelStateChangeEvent extends ChangeEvent {
    private static final long serialVersionUID               = 6099763792690518779L;
    public static final byte  MODEL_CHANGED_TO_DIRTY         = 1;
    public static final byte  MODEL_STILL_DIRTY              = 2;
    public static final byte  MODEL_UPDATE_FROM_OTHER_SOURCE = 4;
    public static final byte  MODEL_CHANGED_TO_SAVED         = 8;

    private byte              state;

    /**
     * Creates a new instance of this class.
     * 
     * @param aSource
     */
    public ModelStateChangeEvent(Object aSource, byte aNewState) {
        super(aSource);
        state = aNewState;
    }

    /**
     * @return the state
     */
    public byte getState() {
        return state;
    }
}
