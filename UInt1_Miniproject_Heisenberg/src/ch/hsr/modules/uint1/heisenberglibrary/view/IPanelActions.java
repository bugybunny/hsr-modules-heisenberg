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

import javax.swing.JButton;

/**
 * @author msyfrig
 */
public interface IPanelActions {
    public void createNew();

    public void openSelected();

    public void searchFieldRequestFocus();

    public void setSelectedCheckBox();

    public void tableRequestFocus();

    public void removeSelected();

    public JButton getDefaultButton();
}
