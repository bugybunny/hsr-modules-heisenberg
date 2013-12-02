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
package ch.hsr.modules.uint1.heisenberglibrary.view;

import javax.swing.table.DefaultTableCellRenderer;

import ch.hsr.modules.uint1.heisenberglibrary.util.Colors;

/**
 * @author msyfrig
 */
public class AvailableCopyCellRenderer extends DefaultTableCellRenderer {
    private static final long serialVersionUID = 5892159039750728850L;

    public AvailableCopyCellRenderer() {
        setOpaque(true);
    }

    @Override
    protected void setValue(Object aValue) {
        int value = Integer.parseInt(aValue.toString());
        setText(aValue.toString());
        if (value > 0) {
            setBackground(Colors.GREEN_POSITIVE);
        } else {
            setBackground(Colors.RED_NEGATIVE);
        }
    }

}
