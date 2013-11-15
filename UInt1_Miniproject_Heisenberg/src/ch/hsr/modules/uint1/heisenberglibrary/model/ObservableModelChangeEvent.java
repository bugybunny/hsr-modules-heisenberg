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

public class ObservableModelChangeEvent {
    private ModelChangeType changeType;
    private Object          oldValue;
    private Object          newValue;

    public ObservableModelChangeEvent(ModelChangeType aChangeType, Object anOldValue,
            Object aNewValue) {
        changeType = aChangeType;
        oldValue = anOldValue;
        newValue = aNewValue;
    }

    public ModelChangeType getChangeType() {
        return changeType;
    }

    public Object getOldValue() {
        return oldValue;
    }

    public Object getNewValue() {
        return newValue;
    }
}