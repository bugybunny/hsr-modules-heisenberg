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

import javax.swing.JPanel;
import javax.swing.JTable;

/**
 * TODO COMMENT ME!
 * 
 * @author msyfrig
 */
// TODO kommentieren und vieles der beiden MainJPanel hier reinnehmen, da beide
// sehr vieles gleich oder ähnlich machen
public class SearchableTableJPanel extends JPanel {
    private static final long   serialVersionUID = 3953982564280733109L;
    private JTable              table;
    private GhostHintJTextField searchTextField;

    // public SearchableTableJPanel(JTable aTable, GhostHintJTextField
    // aSearchField) {
    // table = aTable;
    // searchTextField = aSearchField;
    // }

    void setTable(JTable aTable) {
        table = aTable;
        repaint();
    }

    JTable getTable() {
        return table;
    }

    public void setSearchTextField(GhostHintJTextField aSearchTextField) {
        searchTextField = aSearchTextField;
        repaint();
    }

    GhostHintJTextField getSearchTextField() {
        return searchTextField;
    }

}
