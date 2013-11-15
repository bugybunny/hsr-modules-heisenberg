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

import java.text.ChoiceFormat;
import java.text.Format;
import java.text.MessageFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

import javax.swing.table.AbstractTableModel;

import ch.hsr.modules.uint1.heisenberglibrary.controller.TableModelChangeListener;
import ch.hsr.modules.uint1.heisenberglibrary.model.Loan;
import ch.hsr.modules.uint1.heisenberglibrary.model.LoanStatus;
import ch.hsr.modules.uint1.heisenberglibrary.view.UiComponentStrings;
import ch.hsr.modules.uint1.heisenberglibrary.view.util.DateUtil;

/**
 * Lists all books in a JTable with columns: Status, Copy-ID, Title, Lent until
 * and Lent to.
 * 
 * <br><b>This tablemodel is not editable!</b>
 * 
 * @author msyfrig
 */
public class LoanTableModel extends AbstractTableModel implements Observer {










    private static final long         serialVersionUID               = 4449419618706874102L;
    private static List<String>       columnNames                    = new ArrayList<>(
                                                                             4);

    //@formatter:off
    // things to format the days until duedate string
    private static final double[] DAY_RULES = { ChoiceFormat.previousDouble(-1), -1, 0, 1, ChoiceFormat.nextDouble(1) };

    private static final String ONE_DAY_LEFT_PATTERN = UiComponentStrings
            .getString("LoanTableModel.column.lentuntil.content.choice.oneDayLeft");  //$NON-NLS-1$
    private static final String ZERO_OR_MORE_DAYS_LEFT_PATTERN = UiComponentStrings
            .getString("LoanTableModel.column.lentuntil.content.choice.moreDaysLeft"); //$NON-NLS-1$
    private static final String ONE_DAY_LATE_PATTERN = UiComponentStrings
            .getString("LoanTableModel.column.lentuntil.content.choice.oneDayLate");  //$NON-NLS-1$
    private static final String MORE_DAYS_LATE_PATTERN = UiComponentStrings
            .getString("LoanTableModel.column.lentuntil.content.choice.moreDaysLate"); //$NON-NLS-1$

    private static final String[] DAY_STRINGS = {MORE_DAYS_LATE_PATTERN, ONE_DAY_LATE_PATTERN,
            ZERO_OR_MORE_DAYS_LEFT_PATTERN, ONE_DAY_LEFT_PATTERN,
            ZERO_OR_MORE_DAYS_LEFT_PATTERN};

    private static final ChoiceFormat DAY_FORMATTER_CHOICE = new ChoiceFormat(DAY_RULES, DAY_STRINGS);
    private static final Format[] DAYS_UNTIL_FORMATS = { null, DAY_FORMATTER_CHOICE, NumberFormat.getInstance() };
    private static final MessageFormat DAYS_UNTIL_FORMATTER = new MessageFormat(UiComponentStrings.
            getString("LoanTableModel.column.lentuntil.content")); //$NON-NLS-1$
    

    //@formatter:on
    static {
        columnNames.add(UiComponentStrings
                .getString("LoanTableModel.loanTableColumn.status")); //$NON-NLS-1$
        columnNames.add(UiComponentStrings
                .getString("LoanTableModel.loanTableColumn.copyid")); //$NON-NLS-1$
        columnNames.add(UiComponentStrings
                .getString("LoanTableModel.loanTableColumn.title")); //$NON-NLS-1$
        columnNames.add(UiComponentStrings
                .getString("LoanTableModel.loanTableColumn.lentuntil")); //$NON-NLS-1$
        columnNames.add(UiComponentStrings
                .getString("LoanTableModel.loanTableColumn.lentto")); //$NON-NLS-1$

        DAYS_UNTIL_FORMATTER.setFormats(DAYS_UNTIL_FORMATS);
    }
    private List<Loan>                 data;

    public LoanTableModel(List<Loan> someLoans) {
        data = someLoans;

        for (Loan tempLoan : data) {
            tempLoan.addObserver(this);
        }
    }

    @Override
    public int getRowCount() {
        return data.size();
    }

    @Override
    public int getColumnCount() {
        return columnNames.size();
    }

    /** Non editable table. */
    @Override
    public boolean isCellEditable(int aRowIndex, int aColumnIndex) {
        return false;
    }

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
                long daysFromTodayToDueDate = DateUtil.daysDiff(new Date(),
                        currentLoan.getDueDate().getTime());
                Object[] messageArguments = {
                        DateUtil.getFormattedDate(currentLoan.getDueDate()),
                        Long.valueOf(daysFromTodayToDueDate),
                        Long.valueOf(Math.abs(daysFromTodayToDueDate)) };
                ret = DAYS_UNTIL_FORMATTER.format(messageArguments);
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
    public String getColumnName(int aColumn) {
        return columnNames.get(aColumn);
    }

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
     * Updates the whole table if a value for a loan or copy has changed.
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
