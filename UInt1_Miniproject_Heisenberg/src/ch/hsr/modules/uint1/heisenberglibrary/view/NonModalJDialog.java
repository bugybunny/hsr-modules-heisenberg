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
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.security.InvalidParameterException;
import java.util.HashMap;
import java.util.Map;
import java.util.Observable;
import java.util.Observer;

import javax.swing.JDialog;
import javax.swing.JFrame;

import ch.hsr.modules.uint1.heisenberglibrary.view.model.IDisposable;

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
public abstract class NonModalJDialog extends JDialog implements IDisposable {
    private static final long         serialVersionUID = 2551909774692437970L;

    /**
     * Map that holds all added observers for a panel. The corresponding
     * observers will be removed bevor this panel is closed so the gc can
     * collect all the dead references.
     */
    private Map<Observable, Observer> observerMap      = new HashMap<>();

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
            throw new InvalidParameterException("please set a title"); //$NON-NLS-1$
        }
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        initComponents();
        initHandlers();
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent aWindowClosingEvent) {
                removeAllObservers();
            }
        });

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

    protected boolean addObserverForObservable(Observable anObservable,
            Observer anObserver) {
        anObservable.addObserver(anObserver);
        return observerMap.put(anObservable, anObserver) != null;
    }

    protected boolean deleteObserverForObservable(Observable anObservable) {
        if (anObservable != null) {
            anObservable.deleteObserver(observerMap.remove(anObservable));
            return true;
        }
        return false;
    }

    void removeAllObservers() {
        for (Map.Entry<Observable, Observer> tempEntry : observerMap.entrySet()) {
            if (tempEntry != null) {
                tempEntry.getKey().deleteObserver(tempEntry.getValue());
            }
        }
        observerMap.clear();
    }

    @Override
    public void setModal(boolean aModal) {
        throw new UnsupportedOperationException("cannot change modality"); //$NON-NLS-1$
    }

    @Override
    public void cleanUpBeforeDispose() {
        removeAllObservers();
    }
}
