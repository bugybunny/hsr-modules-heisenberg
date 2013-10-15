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

import java.awt.Component;
import java.awt.Container;
import java.awt.Dialog;
import java.awt.Frame;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.security.InvalidParameterException;
import java.util.HashSet;
import java.util.Set;

import javax.swing.JDialog;
import javax.swing.JFrame;

/**
 * Superclass for all dialogs in this project. All dialogs need to be modal and
 * have a title.
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
        if (aTitle.isEmpty()) {
            // TODO dient nur dazu, dass wir nicht ausversehen irgendwo den
            // titel vergessen
            throw new InvalidParameterException("please set a title");
        }
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        initComponents();
        addKeyHandlers(aGlobalKeyListener != null ? aGlobalKeyListener
                : new DefaultDialogKeyHandler());
        setLocationByPlatform(true);
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
        if (aTitle.isEmpty()) {
            // TODO dient nur dazu, dass wir nicht ausversehen irgendwo den
            // titel vergessen
            throw new InvalidParameterException("please set a title");
        }
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        initComponents();
        addKeyHandlers(aGlobalKeyListener != null ? aGlobalKeyListener
                : new DefaultDialogKeyHandler());
        setLocationByPlatform(true);
    }

    /**
     * Initializes and adds all components to the dialog.
     */
    protected abstract void initComponents();

    /**
     * 
     * 
     * @return
     */
    protected abstract boolean save();

    @Override
    public void setModal(boolean aModal) {
        throw new UnsupportedOperationException("cannot change modality");
    }

    /**
     * Adds a default keyhandler to all components in this dialog. Should be
     * called when all components have been added and not before.
     */
    protected void addKeyHandlers() {
        addKeyHandlers(new DefaultDialogKeyHandler());
    }

    /**
     * Adds a default keyhandler to all components in this dialog. Should be
     * called when all components have been added and not before.
     * 
     * @param aCustomKeyListener
     *            keyhandler to for all componens
     */
    protected void addKeyHandlers(KeyListener aCustomKeyListener) {
        for (Component tempComponent : getAllSubComponents(getContentPane())) {
            tempComponent.addKeyListener(aCustomKeyListener);
        }
    }

    /**
     * Recursively iterates over all components from a given parent container
     * and returns them.
     * 
     * @param aParentContainer
     *            parent container from where to start the iteration
     * @return set of all subcomponents in the given container
     * 
     * @author msyfrig
     */
    private Set<Component> getAllSubComponents(Container aParentContainer) {
        Set<Component> subComponents = new HashSet<>();
        for (Component tempComponent : aParentContainer.getComponents()) {
            if (tempComponent instanceof Container) {
                subComponents
                        .addAll(getAllSubComponents((Container) tempComponent));
            }
            subComponents.add(tempComponent);
        }

        return subComponents;
    }

    /**
     * Default keyhandler for dialogs.
     * 
     * <br> Escape: closes the dialog and discards all changes
     * 
     * <br> Enter: closes the dialog and saves all changes
     * 
     * @author msyfrig
     */
    private class DefaultDialogKeyHandler extends KeyAdapter {
        /*
         * (non-Javadoc)
         * 
         * @see java.awt.event.KeyAdapter#keyReleased(java.awt.event.KeyEvent)
         */
        @Override
        public void keyReleased(KeyEvent aKeyEvent) {
            int releasedKeyCode = aKeyEvent.getKeyCode();
            switch (releasedKeyCode) {
                case KeyEvent.VK_ESCAPE:
                    dispose();
                    break;
                case KeyEvent.VK_ENTER:
                    save();
                    dispose();
                    break;
                default:
                    // nothing
                    break;
            }
        }
    }
}
