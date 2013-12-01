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

import java.awt.event.ActionEvent;
import java.util.HashMap;
import java.util.Map;
import java.util.Observable;
import java.util.Observer;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.KeyStroke;

import ch.hsr.modules.uint1.heisenberglibrary.controller.IModelStateChangeListener;
import ch.hsr.modules.uint1.heisenberglibrary.controller.ModelStateChangeEvent;
import ch.hsr.modules.uint1.heisenberglibrary.model.ObservableObject;
import ch.hsr.modules.uint1.heisenberglibrary.view.model.IDisposable;

/**
 * 
 * @author msyfrig
 */
public abstract class AbstractObservableObjectJPanel<M extends ObservableObject>
        extends JPanel implements Observer, IDisposable {
    private static final long         serialVersionUID = 5272145643378743929L;
    /**
     * The displayed data/model object that can be edited and is observed via
     * observer pattern.
     */
    protected M                       displayedObject;

    /**
     * Flag to indicate if there are unsaved changes in this panel.
     */
    private boolean                   dirty;

    /**
     * Map that holds all added observers for a panel. The corresponding
     * observers will be removed bevor this panel is closed so the gc can
     * collect all the dead references.
     */
    private Map<Observable, Observer> observerMap      = new HashMap<>();

    protected AbstractObservableObjectJPanel(M anObservableObject) {
        setDisplayedObject(anObservableObject);
    }

    /**
     * Removes and adds the observers.
     */
    protected void setDisplayedObject(M aNewDisplayedObject) {
        // do nothing if same object is set
        if (aNewDisplayedObject != displayedObject) {
            // delete observers for this old, not anymore displayed object
            if (displayedObject != null) {
                deleteObserverForObservable(displayedObject);
            }
            displayedObject = aNewDisplayedObject;
            // add observers for new object
            if (displayedObject != null) {
                addObserverForObservable(displayedObject, this);
            }
        }
        displayedObject = aNewDisplayedObject;
    }

    public M getDisplayedObject() {
        return displayedObject;
    }

    /**
     * @param isDirty
     *            the dirty to set
     */
    public void setDirty(boolean isDirty) {
        ModelStateChangeEvent newState = null;
        if (dirty) {
            if (isDirty) {
                newState = new ModelStateChangeEvent(this,
                        ModelStateChangeEvent.MODEL_STILL_DIRTY);
            } else {
                newState = new ModelStateChangeEvent(this,
                        ModelStateChangeEvent.MODEL_CHANGED_TO_SAVED);
            }
        } else {
            if (isDirty) {
                newState = new ModelStateChangeEvent(this,
                        ModelStateChangeEvent.MODEL_CHANGED_TO_DIRTY);
            } else {
                newState = new ModelStateChangeEvent(this,
                        ModelStateChangeEvent.MODEL_UPDATE_FROM_OTHER_SOURCE);
            }
        }
        dirty = isDirty;
        notifyListenersAboutModelChange(newState);
    }

    /**
     * @return if this tab has unsaved changes
     */
    public boolean isDirty() {
        return dirty;
    }

    protected void notifyListenersAboutModelChange(
            ModelStateChangeEvent aNewState) {
        for (IModelStateChangeListener tempListener : listenerList
                .getListeners(IModelStateChangeListener.class)) {
            tempListener.stateChanged(aNewState);
        }
    }

    /**
     * Adds a listener that listens for events when any object in this panel has
     * changed and {@link #isDirty()} returns {@code true} now.
     * 
     * @param aListener
     *            the listener to add
     */
    public void addModelStateChangeListener(IModelStateChangeListener aListener) {
        listenerList.add(IModelStateChangeListener.class, aListener);
    }

    /**
     * Removes a {@code IModelStateChangeListener} from this panel.
     * 
     * @param aListener
     *            the listener to remove
     */
    public void removeModelStateChangeListener(
            IModelStateChangeListener aListener) {
        listenerList.remove(IModelStateChangeListener.class, aListener);
    }

    /**
     * Registers a collection of actions to a given keystroke on this panel and
     * all subcomponents.
     * 
     * @param someActions
     *            map with keystrokes and their associated actions<br>
     *            {@code key}: keystroke to bind the action to<br> {@code value}
     *            : action that should be fired when keystroke has been pressed
     * 
     */
    void addAncestorActions(Map<KeyStroke, Action> someActions) {
        for (Map.Entry<KeyStroke, Action> tempAction : someActions.entrySet()) {
            Object actionName = tempAction.getValue().getValue(Action.NAME);
            getInputMap(JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT).put(
                    tempAction.getKey(), actionName);
            getActionMap().put(actionName, tempAction.getValue());
        }
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
    }

    /**
     * Saves all fields in this object to the model.
     * 
     * @return if something has been saved, is not the case if
     *         {@code dirty==false}
     */
    abstract protected boolean save();

    protected class SaveObjectAction extends AbstractAction {
        private static final long serialVersionUID = -2974999148987189986L;

        protected SaveObjectAction(String anActionName) {
            super(anActionName);
        }

        @Override
        public void actionPerformed(ActionEvent anActionEvent) {
            save();
        }
    }
}
