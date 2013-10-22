package ch.hsr.modules.uint1.heisenberglibrary.model;

import java.util.List;
import java.util.Observable;
import java.util.Observer;

import javax.swing.table.AbstractTableModel;

import ch.hsr.modules.uint1.heisenberglibrary.domain.book.BookDO;
import ch.hsr.modules.uint1.heisenberglibrary.view.UiComponentStrings;

/**
 * Lists all Books in a jTable with Rows: Available, Title, Author and
 * Publisher.
 * 
 * @author twinter
 */
public class BookTableModel extends AbstractTableModel implements Observer {

    private static final long serialVersionUID = 4449419618706874102L;
    private String[]          columnNames      = {
            UiComponentStrings
                    .getString("BookTableModel.bookTableColumn.available"),  //$NON-NLS-1$
            UiComponentStrings
                    .getString("BookTableModel.bookTableColumn.title"), //$NON-NLS-1$
            UiComponentStrings
                    .getString("BookTableModel.bookTableColumn.author"),  //$NON-NLS-1$
            UiComponentStrings
                    .getString("BookTableModel.bookTableColumn.publisher") };  //$NON-NLS-1$
    // private Object[][] data = {{}}; //todo
    // Test
    private List<BookDO>      data;

    public BookTableModel(List<BookDO> someBooks) {
        data = someBooks;

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
        return columnNames.length;
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
            case 1:
                ret = book.getTitle();
            case 2:
                ret = book.getAuthor();
            case 3:
                ret = book.getPublisher();
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
        return columnNames[aColumn];
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
        saveSelectedCells();
        /*
         * since we cannot easily determine the column or even the row that has
         * changed we have to update the whole table (done in some miliseconds)
         * the problem is, that rows can be added or deleted and we cannot just
         * search for the index of the updatet book and update this row and also
         * the shown data can be reduced by filtering
         */
        fireTableDataChanged();
    }

    /**
     * Updates all table cells with the data from the model and reselects all
     * previously selected cells if they still exist. More technically, all
     * selected books with their fields are saved in a collection and reselected
     * after the update if they still can be found. Very bad performance but
     * user friendly.
     */
    private void updateTableData() {
        // TODO implement, as suggested by Stolze, this model should know the
        // jtable instance. easiest way
    }

    private void saveSelectedCells() {

    }
}
