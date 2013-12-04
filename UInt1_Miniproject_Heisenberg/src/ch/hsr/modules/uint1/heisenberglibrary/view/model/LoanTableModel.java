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

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

import ch.hsr.modules.uint1.heisenberglibrary.model.Loan;
import ch.hsr.modules.uint1.heisenberglibrary.model.LoanStatus;
import ch.hsr.modules.uint1.heisenberglibrary.view.UiComponentStrings;

/**
 * Lists all books in a JTable with columns: Status, Copy-ID, Title, Lent until
 * and Lent to.
 * 
 * <br><b>This tablemodel is not editable!</b>
 * 
 * @author msyfrig
 */
public class LoanTableModel extends AbstractExtendendedEventTableModel<Loan> {
    private static final long   serialVersionUID = 4449419618706874102L;
    private static List<String> columnNames      = new ArrayList<>(4);
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
    }

    public LoanTableModel(List<Loan> someLoans) {
        super(someLoans);

        for (Loan tempLoan : data) {
            addObserverForObservable(tempLoan.getCopy().getTitle(), this);
        }
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
                ret = currentLoan.getDueDate().getTime();
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
            case 3:
                ret = Date.class;
                break;
            default:
                ret = String.class;
                break;
        }
        return ret;
    }
}
