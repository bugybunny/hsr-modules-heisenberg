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
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.security.InvalidParameterException;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.KeyStroke;

/**
 * Superclass for all dialogs in this project. All dialogs need to be non-modal
 * and have a title.
 * 
 * <br>The default exit operation is dispose but can be overridden.
 * 
 * <br> The contract for the subclasses is, that they implement the
 * {@link #save()} method to save all changes before the dialog is closed and
 * {@link #initComponents()}. All constructors already call
 * {@link #initComponents()} and after this {@link #addKeyHandlers()} to ensure
 * the default behavior.
 * 
 * <br> Adds a keyhandler for <ESC> and <Enter> button to all components.
 * Subclasses need to call {@link #addKeyHandlers()} to add the default behavior
 * or {@link #addKeyHandlers(KeyListener)} to add custom behavior after they
 * added all components.
 * 
 * @author msyfrig
 * @see #setDefaultCloseOperation(int)
 */
public abstract class AbstractDefaultJDialog extends JDialog {
    private static final long serialVersionUID = 2551909774692437970L;

    private Action            saveAction;
    private Action            disposeAction;

    /**
     * Creates a new dialog and sets the owner and title. Initializes all
     * components and adds the default keyhandler.
     * 
     * @param anOwner
     *            frame owner of this dialog
     * @param aTitle
     *            title of this dialog, no empty strings allowed
     */
    public AbstractDefaultJDialog(Frame anOwner, String aTitle) {
        this(anOwner, aTitle, null);
    }

    /**
     * Creates a new dialog and sets the owner and title. Initializes all
     * components and a given keyhandler.
     * 
     * @param anOwner
     *            frame owner of this dialog
     * @param aTitle
     *            title of this dialog, no empty strings allowed
     * @param aGlobalKeyListener
     *            keylistener that listen for events on all components in this
     *            dialog. {@code null} for the default behavior.
     */
    public AbstractDefaultJDialog(Frame anOwner, String aTitle,
            KeyListener aGlobalKeyListener) {
        super(anOwner, aTitle);
        initEverything(aTitle, aGlobalKeyListener);
    }

    /**
     * Creates a new dialog and sets the owner and title. Initializes all
     * components and adds the default keyhandler.
     * 
     * @param anOwner
     *            dialog owner of this dialog
     * @param aTitle
     *            title of this dialog, no empty strings allowed
     */
    public AbstractDefaultJDialog(Dialog anOwner, String aTitle) {
        this(anOwner, aTitle, null);
    }

    /**
     * Creates a new dialog and sets the owner and title. Initializes all
     * components and adds the given keyhandler.
     * 
     * @param anOwner
     *            dialog owner of this dialog
     * @param aTitle
     *            title of this dialog, no empty strings allowed
     * @param aGlobalKeyListener
     *            keylistener that listen for events on all components in this
     *            dialog. {@code null} for the default behavior.
     */
    public AbstractDefaultJDialog(Dialog anOwner, String aTitle,
            KeyListener aGlobalKeyListener) {
        super(anOwner, aTitle);
        initEverything(aTitle, aGlobalKeyListener);
    }

    private void initEverything(String aTitle, KeyListener aGlobalKeyListener) {
        if (aTitle.isEmpty()) {
            // TODO dient nur dazu, dass wir nicht ausversehen irgendwo den
            // titel vergessen
            throw new InvalidParameterException("please set a title");
        }
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        initComponents();
        initHandlers();

        saveAction = new SaveAction("save");
        disposeAction = new DisposeAction("dispose");

        getRootPane()
                .getInputMap(JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT)
                .put(KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0),
                        disposeAction.getValue(Action.NAME));
        getRootPane().getActionMap().put(disposeAction.getValue(Action.NAME),
                disposeAction);

        getRootPane()
                .getInputMap(JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT)
                .put(KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, 0),
                        saveAction.getValue(Action.NAME));
        getRootPane().getActionMap().put(saveAction.getValue(Action.NAME),
                saveAction);

        setLocationByPlatform(true);
    }

    /**
     * Initializes and adds all components to the dialog.
     */
    protected abstract void initComponents();

    /**
     * Adds all listeners/handlers. This is just so we separate the code and it
     * becomes more readable.
     */
    protected abstract void initHandlers();

    /**
     * Subclasses need to implement this function. In the default behavior (if
     * {@link #AbstractDefaultJDialog(Dialog, String)} or
     * {@link #AbstractDefaultJDialog(Frame, String)} has been called) the save
     * action is called as soon as the user presses enter.
     * 
     * @return if the save was successful or not if any values were invalid
     */
    protected abstract boolean save();

    @Override
    public void setModal(boolean aModal) {
        throw new UnsupportedOperationException("cannot change modality");
    }

    private class SaveAction extends AbstractAction {
        private static final long serialVersionUID = -4275362945903839390L;

        private SaveAction(String anActionName) {
            super(anActionName);
        }

        /*
         * (non-Javadoc)
         * 
         * @see
         * java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent
         * )
         */
        @Override
        public void actionPerformed(ActionEvent anActionEvent) {
            save();
            dispose();
        }
    }

    private class DisposeAction extends AbstractAction {
        private static final long serialVersionUID = 2752048542262499446L;

        /**
         * Creates a new instance of this class.
         */
        private DisposeAction(String anActionName) {
            super(anActionName);
        }

        /*
         * (non-Javadoc)
         * 
         * @see
         * java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent
         * )
         */
        @Override
        public void actionPerformed(ActionEvent aE) {
            dispose();
        }
    }
}
