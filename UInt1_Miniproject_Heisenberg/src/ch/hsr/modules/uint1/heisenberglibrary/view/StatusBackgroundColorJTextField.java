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

import java.awt.Color;

import javax.swing.JTextField;
import javax.swing.text.Document;

/**
 * JTextField to can show a positive or negative status via background color
 * (good=green, bad=red).
 * 
 * @author msyfrig
 */
public class StatusBackgroundColorJTextField extends JTextField {
    private static final long serialVersionUID = 8944651872738119454L;
    public static final Color POSITIVE_COLOR   = new Color(130, 240, 130);
    public static final Color NEGATIVE_COLOR   = new Color(240, 130, 130);

    /**
     * Creates a new instance of this class.
     * 
     */
    public StatusBackgroundColorJTextField() {
        super();
    }

    public StatusBackgroundColorJTextField(Document aDoc, String aText,
            int aColumns) {
        super(aDoc, aText, aColumns);
    }

    public StatusBackgroundColorJTextField(int aColumns) {
        super(aColumns);
    }

    public StatusBackgroundColorJTextField(String aText, int aColumns) {
        super(aText, aColumns);
    }

    public StatusBackgroundColorJTextField(String aText) {
        super(aText);
    }

    public void setPositiveBackground() {
        setBackground(POSITIVE_COLOR);
    }

    public void setNegativeBackground() {
        setBackground(NEGATIVE_COLOR);
    }

}
