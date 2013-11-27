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
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;

import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.text.Document;

/**
 * JTextField to can show a positive or negative status via background color
 * (good=green, bad=red).
 * 
 * @author msyfrig
 */
public class StatusBackgroundColorJTextField extends JTextField implements
        FocusListener {
    private static final long serialVersionUID = 8944651872738119454L;
    public static final Color POSITIVE_COLOR   = new Color(240, 255, 240);
    public static final Color NEGATIVE_COLOR   = new Color(255, 205, 205);

    /**
     * Creates a new instance of this class.
     * 
     */
    public StatusBackgroundColorJTextField() {
        super();
        addFocusListener(this);
    }

    public StatusBackgroundColorJTextField(Document aDoc, String aText,
            int aColumns) {
        super(aDoc, aText, aColumns);
        addFocusListener(this);
    }

    public StatusBackgroundColorJTextField(int aColumns) {
        super(aColumns);
        addFocusListener(this);
    }

    public StatusBackgroundColorJTextField(String aText, int aColumns) {
        super(aText, aColumns);
        addFocusListener(this);
    }

    public StatusBackgroundColorJTextField(String aText) {
        super(aText);
        addFocusListener(this);
    }

    public void setPositiveBackground() {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                setBackground(POSITIVE_COLOR);
            }
        });
    }

    public void setNegativeBackground() {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                setBackground(NEGATIVE_COLOR);
            }
        });
    }

    @Override
    public void setText(String aText) {
        int caretPosition = getCaretPosition();
        super.setText(aText);
        int textLength = aText.length();
        if (caretPosition <= aText.length()) {
            setCaretPosition(caretPosition);
        } else {
            setCaretPosition(aText.length());
        }
    }

    @Override
    public void focusGained(FocusEvent aFocusGainedEvent) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                selectAll();
            }
        });
    }

    @Override
    public void focusLost(FocusEvent aFocusLostEvent) {
        // do nothing
    }
}
