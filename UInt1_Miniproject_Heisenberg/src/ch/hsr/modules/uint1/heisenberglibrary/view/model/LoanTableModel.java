package ch.hsr.modules.uint1.heisenberglibrary.view.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

import javax.swing.table.AbstractTableModel;

import ch.hsr.modules.uint1.heisenberglibrary.controller.TableModelChangeListener;
import ch.hsr.modules.uint1.heisenberglibrary.model.Loan;
import ch.hsr.modules.uint1.heisenberglibrary.model.LoanStatus;
import ch.hsr.modules.uint1.heisenberglibrary.view.UiComponentStrings;
import ch.hsr.modules.uint1.heisenberglibrary.view.util.DateFormatterUtil;

/**
 * Lists all books in a JTable with columns: Status, Exemplar-ID, Title, Lent
 * until and Lent to.
 * 
 * <br><b>This tablemodel is not editable!</b>
 * 
 * @author msyfrig
 */
public class LoanTableModel extends AbstractTableModel implements Observer {
    private static final long   serialVersionUID = 4449419618706874102L;
    private static List<String> columnNames      = new ArrayList<>(4);

    static {
        columnNames.add(UiComponentStrings
                .getString("LoanTableModel.loanTableColumn.status")); //$NON-NLS-1$
        columnNames.add(UiComponentStrings
                .getString("LoanTableModel.loanTableColumn.exemplarid")); //$NON-NLS-1$
        columnNames.add(UiComponentStrings
                .getString("LoanTableModel.loanTableColumn.title")); //$NON-NLS-1$
        columnNames.add(UiComponentStrings
                .getString("LoanTableModel.loanTableColumn.lentuntil")); //$NON-NLS-1$
        columnNames.add(UiComponentStrings
                .getString("LoanTableModel.loanTableColumn.lentto")); //$NON-NLS-1$
    }
    private List<Loan>          data;

    public LoanTableModel(List<Loan> someLoans) {
        data = someLoans;

        for (Loan tempLoan : data) {
            tempLoan.addObserver(this);
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

    /** Non editable table. */
    @Override
    public boolean isCellEditable(int aRowIndex, int aColumnIndex) {
        return false;
    }

    /*
     * (non-Javadoc)
     * 
     * @see javax.swing.table.TableModel#getValueAt(int, int)
     */
    // TODO: specific books with availability
    @Override
    public Object getValueAt(int aRowIndex, int aColumnIndex) {
        Loan currentLoan = data.get(aRowIndex);
        Object ret = null;

        switch (aColumnIndex) {
            case 0:
                ret = currentLoan.isOverdue() ? LoanStatus.DUE : LoanStatus.OK;
                break;
            case 1:
                ret = Long.valueOf(currentLoan.getCopy().getInventoryNumber());
                break;
            case 2:
                ret = currentLoan.getCopy().getTitle().getTitle();
                break;
            case 3:
                // TODO resttage auch ausgeben, anderes format für de_CH und
                // en_US
                ret = DateFormatterUtil.getFormattedDate(currentLoan
                        .getReturnDate());
                break;
            case 4:
                ret = currentLoan.getCustomer().getSurname() + " "
                        + currentLoan.getCustomer().getName();
                break;
            default:
                ret = UiComponentStrings.getString("empty"); //$NON-NLS-1$
        }
        return ret;
    }

    @Override
    public void setValueAt(Object value, int aRowIndex, int aColumnIndex) {
        // do nothing since not editable
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
        Class<?> ret = String.class;
        switch (aColumnIndex) {
            case 0:
                ret = LoanStatus.class;
                break;
            case 1:
                ret = Long.class;
                break;
            default:
                ret = String.class;
                break;
        }
        return ret;
    }

    /**
     * Adds a listener that listens for events when this model is about to fire
     * a {@link #fireTableDataChanged()} or any other table data updates.
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
     * Updates all table cells with the data from the model and reselects all
     * previously selected cells if they still exist. More technically, all
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

        for (TableModelChangeListener tempListener : listenerList
                .getListeners(TableModelChangeListener.class)) {
            tempListener.tableChanged();
        }
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
}
