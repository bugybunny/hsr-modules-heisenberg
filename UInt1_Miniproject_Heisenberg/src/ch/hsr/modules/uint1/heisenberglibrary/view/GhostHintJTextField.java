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

//@formatter:off
/**
 * Textfield with a so called ghost hint. If the content is empty, a greyed out
 * hint of what to enter in this field is shown. As soon as this field gets the
 * foucs, the hint is removed and the user can enter his own text.
 * The following cases exist:
 * <table border=1>
 *  <tr>
 *      <th></th>
 *      <th>focused</th>
 *      <th>focus somewhere else</th>
 *  </tr>
 *  <tr>
 *      <td>empty</td>
 *      <td>nothing visible, empty</td>
 *      <td>hint in a light grey font</td>
 *  </tr>
 *  <tr>
 *      <td>user entered text "abc"</td>
 *      <td>"abc" in normal color</td>
 *      <td>"abc" in normal color</td>
 *  </tr>
 *  
 * Based on an example from stackoverflow {@link http://stackoverflow.com/a/1739037}, cc license.
 * Added all comments, implemented foreground color and code cleanup.
 * 
 * @author msyfrig
 */
//@formatter:on
public class GhostHintJTextField extends JTextField implements FocusListener {
    private static final long serialVersionUID = 2764559471513955654L;
    private final String      hint;
    private boolean           showingHint;

    /**
     * @return the showingHint
     */
    public boolean isShowingHint() {
        return showingHint;
    }

    /**
     * Creates a new textfield with the given hint.
     * 
     * @param aHint
     */
    public GhostHintJTextField(final String aHint) {
        super(aHint);
        hint = aHint;
        showingHint = true;
        addFocusListener(this);
        setForeground(Color.LIGHT_GRAY);
    }

    @Override
    public void focusGained(FocusEvent aFocusGainedEvent) {
        if (this.getText().isEmpty()) {
            setForeground(null);
            setText(UiComponentStrings.getString("empty"));
            showingHint = false;
        }
    }

    @Override
    public void focusLost(FocusEvent aFocusLostEvent) {
        if (this.getText().isEmpty()) {
            setForeground(Color.LIGHT_GRAY);
            setText(hint);
            showingHint = true;
        }
    }

    /**
     * Returns an empty string if the hint is visible.
     * 
     * @see JTextField#getText()
     */
    @Override
    public String getText() {
        return showingHint ? "" : super.getText();
    }

    /**
     * Returns whether this textfield contains text or if just the hint is set.
     * 
     * @return {@code true} only the hint is set or textfield has the focus and
     *         is really empty {@code false} if there is some user entered text
     */
    public boolean isEmpty() {
        return showingHint ? true : getText().isEmpty();
    }
}