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
package ch.hsr.modules.uint1.heisenberglibrary.util;

import javax.swing.UIManager;

//@formatter:off
/**
 * Change some nimbus look and feel colors.
 * 
 * @author msyfrig
 * @see http://docs.oracle.com/javase/tutorial/uiswing/lookandfeel/_nimbusDefaults.html
 */
//@formatter:on
public class NimbusLookAndFeelInitalizer {
    public static void changeColors() {
        // default nimbus backgroundcolor is almost not readable and also red
        // shows that something is wrong.
        UIManager.put("ToolTip.background", Colors.RED_NEGATIVE);
        // make the difference between tablerows a bit more remarkable
        UIManager.put("Table.alternateRowColor", Colors.LIGHT_BLUE);
    }
}
