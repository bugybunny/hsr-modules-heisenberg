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

import java.awt.Dialog;
import java.awt.Frame;
import java.security.InvalidParameterException;

import javax.swing.JDialog;
import javax.swing.JFrame;

/**
 * Superclass for all dialogs in this project. All dialogs need to be non-modal
 * and have a title.
 * 
 * <br>The default exit operation is dispose but can be overridden.
 * 
 * <br> The contract for the subclasses is, that they implement the
 * {@link #initComponents()} method and optionally {@link #initHandlers()}. All
 * constructors already call {@link #initComponents()} and after this
 * {@link #initHandlers()}.
 * 
 * @author msyfrig
 * @see #setDefaultCloseOperation(int)
 */
public abstract class NonModalJDialog extends JDialog {
    private static final long serialVersionUID = 2551909774692437970L;

    /**
     * Creates a new dialog and sets the owner and title. Initializes all
     * components and sets the title.
     * 
     * @see #initComponents()
     * @see #initHandlers()
     * @param anOwner
     *            frame owner of this dialog
     * @param aTitle
     *            title of this dialog, no empty strings allowed
     */
    public NonModalJDialog(Frame anOwner, String aTitle) {
        super(anOwner, aTitle);
        initEverything(aTitle);
    }

    /**
     * Creates a new dialog and sets the owner and title. Initializes all
     * components and sets the title.
     * 
     * @see NonModalJDialog#initComponents()
     * @see #initHandlers()d
     * @param anOwner
     *            dialog owner of this dialog
     * @param aTitle
     *            title of this dialog, no empty strings allowed
     */
    public NonModalJDialog(Dialog anOwner, String aTitle) {
        super(anOwner, aTitle);
        initEverything(aTitle);
    }

    private void initEverything(String aTitle) {
        if (aTitle.isEmpty()) {
            // TODO dient nur dazu, dass wir nicht ausversehen irgendwo den
            // titel vergessen
            throw new InvalidParameterException("please set a title");
        }
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        initComponents();
        initHandlers();

        setLocationByPlatform(true);
    }

    /**
     * Initializes and adds all components to the dialog.
     */
    protected abstract void initComponents();

    /**
     * Adds all listeners/handlers. This is just so we separate the code and it
     * becomes more readable. <br> Can be empty and does not need to be
     * implemented.
     */
    protected void initHandlers() {
    }

    @Override
    public void setModal(boolean aModal) {
        throw new UnsupportedOperationException("cannot change modality");
    }
}
