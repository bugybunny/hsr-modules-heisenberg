package ch.hsr.modules.uint1.heisenberglibrary.model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Observable;
import java.util.Observer;
import java.util.Set;

import javax.swing.JTable;
import javax.swing.event.TableModelListener;
import javax.swing.table.AbstractTableModel;

import ch.hsr.modules.uint1.heisenberglibrary.domain.book.BookDO;
import ch.hsr.modules.uint1.heisenberglibrary.view.BookMasterJFrame;
import ch.hsr.modules.uint1.heisenberglibrary.view.UiComponentStrings;

/**
 * Lists all books in a JTable with columns: Available, Title, Author and
 * Publisher.
 * 
 * @author twinter
 */
public class BookTableModel extends AbstractTableModel implements Observer {
    private static final long   serialVersionUID = 4449419618706874102L;
    private static List<String> columnNames      = new ArrayList<>(4);

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
    // TODO auf Set ändern und jedes mal wenn etwas hinzugefügt werden will
    // prüfen ob es nicht hinzugefügt wird/eine exception gibt, falls ja ist ein
    // duplikat
    private List<BookDO>        data;
    /**
     * The instance that holds this tablemodel. This is only needed to save the
     * selection before the {@link #fireTableCellUpdated(int, int)} is called
     * and restore it after since we have no possibility to get the selection
     * without the jtable instance and we would have to save the whole selection
     * if the selection changes if we implemented it in {@link BookMasterJFrame}
     * . So that means, add a selectionlistener, check for removed or added
     * selections or always get all selected books and add it into a collection
     * and additionally add a {@link TableModelListener} and after an update
     * restore the selection. So it's much easier (despite having bad code and
     * know the instance of the holding jtable) to do this hear, much more
     * efficient.
     */
    private JTable              table;

    /**
     * 
     * Creates a new instance of this class.
     * 
     * @param anAssociatedTable
     *            the {@code JTable} instance that holds this model. This is
     *            only needed to save the selection before
     * @param someBooks
     *            the books to display
     */
    public BookTableModel(JTable anAssociatedTable, List<BookDO> someBooks) {
        data = someBooks;
        table = anAssociatedTable;

        for (BookDO tempBook : someBooks) {
            tempBook.addObserver(this);
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see javax.swing.table.TableModel#getRowCount()
     */
    @Override
    public int getRowCount() {
        return data.size();
    }

    /*
     * (non-Javadoc)
     * 
     * @see javax.swing.table.TableModel#getColumnCount()
     */
    @Override
    public int getColumnCount() {
        return columnNames.size();
    }

    /*
     * (non-Javadoc)
     * 
     * @see javax.swing.table.TableModel#getValueAt(int, int)
     */
    // TODO: specific books with availability
    @Override
    public Object getValueAt(int aRowIndex, int aColumnIndex) {
        BookDO book = data.get(aRowIndex);
        Object ret = null;

        switch (aColumnIndex) {
            case 0:
                ret = "1 (todo)"; //$NON-NLS-1$
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
        BookDO book = data.get(aRowIndex);
        switch (aColumnIndex) {
            case 0:
                break; // TODO: availability
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
        fireTableCellUpdated(aRowIndex, aColumnIndex);
    }

    /*
     * (non-Javadoc)
     * 
     * @see javax.swing.table.AbstractTableModel#getColumnName(int)
     */
    @Override
    public String getColumnName(int aColumn) {
        return columnNames.get(aColumn);
    }

    /*
     * (non-Javadoc)
     * 
     * @see javax.swing.table.AbstractTableModel#getColumnClass(int)
     */
    @Override
    public Class<?> getColumnClass(int aColumnIndex) {
        switch (aColumnIndex) {
            case 0:
            case 1:
            case 2:
            case 3:
                return String.class;
            default:
                return String.class;
        }
    }

    public BookDO getBookByRowNumber(int i) {
        return data.get(i);
    }

    /**
     * Updates the whole table if a value for a book has changed.
     */
    @Override
    public void update(Observable anObservable, Object anArgument) {
        updateTableData();
    }

    /**
     * Updates all table cells with the data from the model and reselects all
     * previously selected cells if they still exist. More technically, all
     * selected books with their fields are saved in a collection and reselected
     * after the update if they still can be found. Very bad performance but
     * user friendly.
     */
    private void updateTableData() {
        Collection<BookDO> previouslySelectedBooks = saveSelectedRows();
        /*
         * since we cannot easily determine the column or even the row that has
         * changed we have to update the whole table (done in some miliseconds)
         * the problem is, that rows can be added or deleted and we cannot just
         * search for the index of the updatet book and update this row and also
         * the shown data can be reduced by filtering
         */
        fireTableDataChanged();
        restoreSelectedRows(previouslySelectedBooks);

    }

    /**
     * Saves the selected books in the table. The actual book instances are
     * saved since books can be added or removed so only saving the row index is
     * not enough.
     * 
     * @return
     */
    private Set<BookDO> saveSelectedRows() {
        Set<BookDO> selectedBooks = new HashSet<>(table.getSelectedRowCount());
        for (int selectionIndex : table.getSelectedRows()) {
            BookDO singleSelectedBook = getBookByRowNumber(table
                    .convertRowIndexToModel(selectionIndex));
            selectedBooks.add(singleSelectedBook);
        }
        return selectedBooks;
    }

    /**
     * Reselect the given books in the table if they still exist.
     * 
     * @param someBooksToSelect
     *            the books to select
     */
    private void restoreSelectedRows(Collection<BookDO> someBooksToSelect) {
        for (BookDO tempBookToSelect : someBooksToSelect) {
            int indexInList = data.indexOf(tempBookToSelect);
            // do nothing if not found and books has been removed
            if (indexInList > -1) {
                int indexToSelectInView = table
                        .convertRowIndexToView(indexInList);
                table.getSelectionModel().addSelectionInterval(
                        indexToSelectInView, indexToSelectInView);
            }
        }
    }
}
