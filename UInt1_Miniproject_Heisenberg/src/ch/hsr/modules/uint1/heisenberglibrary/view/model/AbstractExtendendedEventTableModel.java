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
package ch.hsr.modules.uint1.heisenberglibrary.view.model;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Observable;
import java.util.Observer;

import javax.swing.table.AbstractTableModel;

import ch.hsr.modules.uint1.heisenberglibrary.controller.ITableModelChangeListener;

/**
 * @author msyfrig
 */
public abstract class AbstractExtendendedEventTableModel<E extends Observable>
        extends AbstractTableModel implements Observer, IDisposable {
    private static final long         serialVersionUID = 3180682000410891172L;
    protected List<E>                 data;

    /**
     * Map that holds all added observers for this table model. The
     * corresponding observers will be removed bevor this table model will be
     * disposed and never used again so the gc can collect all the dead
     * references.
     */
    private Map<Observable, Observer> observerMap      = new HashMap<>();

    protected AbstractExtendendedEventTableModel(List<E> someData) {
        data = someData;

        for (E tempEntry : data) {
            addObserverForObservable(tempEntry, this);
        }
    }

    @Override
    public int getRowCount() {
        return data.size();
    }

    public void setData(List<E> someData) {
        for (E tempEntry : data) {
            deleteObserverForObservable(tempEntry);
        }
        data = someData;

        for (E tempEntry : data) {
            addObserverForObservable(tempEntry, this);
        }
        updateTableData();
    }

    public List<E> getData() {
        return data;
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

    /**
     * Adds a listener that listens for events when this model is about to fire
     * a {@link #fireTableDataChanged()} or any other table bookData updates.
     */
    public void addTableModelChangeListener(ITableModelChangeListener aListener) {
        listenerList.add(ITableModelChangeListener.class, aListener);
    }

    /**
     * Removes a {@code ITableModelChangeListener} from this table model.
     * 
     * @param aListener
     *            the listener to remove
     */
    public void removeTableModelChangeListener(
            ITableModelChangeListener aListener) {
        listenerList.remove(ITableModelChangeListener.class, aListener);
    }

    @Override
    public void update(Observable anObservable, Object anArgument) {
        updateTableData();
    }

    /**
     * Updates all table cells with the bookData from the model and reselects
     * all previously selected cells if they still exist. More technically, all
     * selected books with their fields are saved in a collection and reselected
     * after the update if they still can be found. Very bad performance but
     * user friendly.
     */
    public void updateTableData() {
        notifyListenersBeforeUpdate();
        /*
         * since we cannot easily determine the column or even the row that has
         * changed we have to update the whole table (done in some miliseconds)
         * the problem is, that rows can be added or deleted and we cannot just
         * search for the index of the updatet book and update this row and also
         * the shown bookData can be reduced by filtering
         */
        fireTableDataChanged();
        notifyListenersAfterUpdate();
    }

    /**
     * Notifies all listeners that the model is about to change.
     */
    protected void notifyListenersBeforeUpdate() {
        for (ITableModelChangeListener tempListener : listenerList
                .getListeners(ITableModelChangeListener.class)) {
            tempListener.tableIsAboutToUpdate();
        }
    }

    /**
     * Notifies all listeners that the model has changed and the table has been
     * updated.
     */
    protected void notifyListenersAfterUpdate() {
        for (ITableModelChangeListener tempListener : listenerList
                .getListeners(ITableModelChangeListener.class)) {
            tempListener.tableChanged();
        }
    }

    @Override
    public void cleanUpBeforeDispose() {
        removeAllObservers();
    }
}
