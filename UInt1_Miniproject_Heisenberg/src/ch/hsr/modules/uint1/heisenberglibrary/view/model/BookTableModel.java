package ch.hsr.modules.uint1.heisenberglibrary.view.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

import javax.swing.table.AbstractTableModel;

import ch.hsr.modules.uint1.heisenberglibrary.controller.TableModelChangeListener;
import ch.hsr.modules.uint1.heisenberglibrary.model.BookDO;
import ch.hsr.modules.uint1.heisenberglibrary.model.Library;
import ch.hsr.modules.uint1.heisenberglibrary.view.UiComponentStrings;

/**
 * Lists all loans in a JTable with columns: Status, Exemplar #, booktitle, lent
 * until, lent datum.
 * 
 * @author msyfrig
 */
public class BookTableModel extends AbstractTableModel implements Observer {
    private static final long   serialVersionUID = 4449419618706874102L;
    private static List<String> columnNames      = new ArrayList<>(5);

    static {
        columnNames.add(UiComponentStrings
                .getString("BookTableModel.bookTableColumn.available"));  //$NON-NLS-1$
        columnNames.add(UiComponentStrings
                .getString("BookTableModel.bookTableColumn.title")); //$NON-NLS-1$
        columnNames.add(UiComponentStrings
                .getString("BookTableModel.bookTableColumn.author"));  //$NON-NLS-1$
        columnNames.add(UiComponentStrings
                .getString("BookTableModel.bookTableColumn.publisher"));  //$NON-NLS-1$
    }
    private Library             library;
    private List<BookDO>        bookData;

    public BookTableModel(Library aLibrary) {
        library = aLibrary;
        bookData = library.getBooks();

        library.addObserver(this);
        for (BookDO tempBook : bookData) {
            tempBook.addObserver(this);
        }
    }

    @Override
    public int getRowCount() {
        return bookData.size();
    }

    @Override
    public int getColumnCount() {
        return columnNames.size();
    }

    /**
     * First column is not editable.
     */
    @Override
    public boolean isCellEditable(int aRowIndex, int aColumnIndex) {
        return aColumnIndex != 0;
    }

    @Override
    public Object getValueAt(int aRowIndex, int aColumnIndex) {
        BookDO book = bookData.get(aRowIndex);
        Object ret = null;

        switch (aColumnIndex) {
            case 0:
                ret = Integer.valueOf(library.getAvailableCopiesForBook(book)
                        .size());
                break;
            case 1:
                ret = book.getTitle();
                break;
            case 2:
                ret = book.getAuthor();
                break;
            case 3:
                ret = book.getPublisher();
                break;
            default:
                ret = "";
        }
        return ret;
    }

    @Override
    public void setValueAt(Object value, int aRowIndex, int aColumnIndex) {
        BookDO book = bookData.get(aRowIndex);
        switch (aColumnIndex) {
            case 1:
                book.setTitle((String) value);
                break;
            case 2:
                book.setAuthor((String) value);
                break;
            case 3:
                book.setPublisher((String) value);
                break;
            default:
                // do nothing
        }
        updateTableData();
    }

    @Override
    public String getColumnName(int aColumn) {
        return columnNames.get(aColumn);
    }

    @Override
    public Class<?> getColumnClass(int aColumnIndex) {
        Class<?> ret = String.class;
        switch (aColumnIndex) {
            case 0:
                ret = Integer.class;
                break;
            case 1:
            case 2:
            case 3:
                ret = String.class;
                break;
            default:
                ret = String.class;
                break;
        }
        return ret;
    }

    /**
     * Adds a listener that listens for events when this model is about to fire
     * a {@link #fireTableDataChanged()} or any other table bookData updates.
     */
    public void addTableModelChangeListener(TableModelChangeListener aListener) {
        listenerList.add(TableModelChangeListener.class, aListener);
    }

    /**
     * Removes a {@code TableModelChangeListener} from this table model.
     * 
     * @param aListener
     *            the listener to remove
     */
    public void removeTableModelChangeListener(
            TableModelChangeListener aListener) {
        listenerList.remove(TableModelChangeListener.class, aListener);
    }

    /**
     * Updates the whole table if a value for a book has changed.
     */
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
    private void updateTableData() {
        notifyListenersBeforeUpdate();
        /*
         * since we cannot easily determine the column or even the row that has
         * changed we have to update the whole table (done in some miliseconds)
         * the problem is, that rows can be added or deleted and we cannot just
         * search for the index of the updatet book and update this row and also
         * the shown data can be reduced by filtering
         */
        fireTableDataChanged();
        notifyListenersAfterUpdate();
    }

    /**
     * Notifies all listeners that the model is about to change.
     */
    private void notifyListenersBeforeUpdate() {
        for (TableModelChangeListener tempListener : listenerList
                .getListeners(TableModelChangeListener.class)) {
            tempListener.tableIsAboutToUpdate();
        }
    }

    /**
     * Notifies all listeners that the model has changed and the table has been
     * updated.
     */
    private void notifyListenersAfterUpdate() {
        for (TableModelChangeListener tempListener : listenerList
                .getListeners(TableModelChangeListener.class)) {
            tempListener.tableChanged();
        }
    }
}
