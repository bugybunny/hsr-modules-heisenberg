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
import java.awt.Graphics;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

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
 * Loosely based on an example from stackoverflow {@link http://stackoverflow.com/a/4962829}, cc license.
 * Added all comments, implemented foreground color and code cleanup.
 * 
 * @author msyfrig
 */
//@formatter:on
public class GhostHintJTextField extends StatusBackgroundColorJTextField
        implements FocusListener {
    private static final long serialVersionUID = 2764559471513955654L;
    private final String      hint;
    private boolean           showingHint;

    /**
     * @return whether the hint is shown now
     */
    public boolean isShowingHint() {
        return showingHint;
    }

    @Override
    public void setPositiveBackground() {
        if (!showingHint) {
            setBackground(POSITIVE_COLOR);
        }
    }

    @Override
    public void setNegativeBackground() {
        if (!showingHint) {
            setBackground(NEGATIVE_COLOR);
        }
    }

    /**
     * Creates a new textfield with the given hint and the default keyhandler
     * (clear text on escape).
     * 
     * @param aHint
     *            the hint to display if this textfield has no user entered text
     *            and actually not the focus.
     */
    public GhostHintJTextField(final String aHint) {
        this(aHint, true);
    }

    /**
     * Creates a new textfield with the given hint and the default keyhandler
     * (clear text on escape).
     * 
     * @param aHint
     *            the hint to display if this textfield has no user entered text
     *            and actually not the focus.
     * @param isDefaultKeyHandler
     * @wbp.parser.constructor
     */
    public GhostHintJTextField(final String aHint, boolean isDefaultKeyHandler) {
        super();
        hint = aHint;
        showingHint = true;
        addFocusListener(this);
        setForeground(Color.LIGHT_GRAY);
        if (isDefaultKeyHandler) {
            addKeyListener(new KeyAdapter() {
                /**
                 * Clears the text when escape has been released.
                 */
                @Override
                public void keyReleased(KeyEvent aKeyEvent) {
                    if (aKeyEvent.getKeyCode() == KeyEvent.VK_ESCAPE) {
                        setText(UiComponentStrings.getString("empty")); //$NON-NLS-1$
                    }
                }
            });
        }
    }

    @Override
    public void focusGained(FocusEvent aFocusGainedEvent) {
        if (getText().isEmpty()) {
            setForeground(null);
            showingHint = false;
        }
    }

    @Override
    public void focusLost(FocusEvent aFocusLostEvent) {
        if (getText().isEmpty()) {
            setBackground(null);
            setForeground(Color.LIGHT_GRAY);
            showingHint = true;
        }
    }

    //
    @Override
    public void paint(Graphics aGraphics) {
        super.paint(aGraphics);
        if (hint != null && showingHint) {
            int padding = (getHeight() - getFont().getSize()) / 2;
            aGraphics.drawString(hint, 6, getHeight() - padding - 1);
        }
    }
}