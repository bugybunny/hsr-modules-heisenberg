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

import java.util.EventListener;

import javax.swing.event.TableModelListener;

/**
 * Defines a listener to listen for state changes in a model. It is different
 * from {@link TableModelListener} in the way, that this listener fires events
 * before the table is updated.
 * 
 * <br>Still use {@link TableModelListener} to get a detailed event about what
 * changed.
 * 
 * @author msyfrig
 * @see TableModelListener
 */
public interface TableModelChangeListener extends EventListener {
    public void tableIsAboutToUpdate();

    public void tableChanged();
}
