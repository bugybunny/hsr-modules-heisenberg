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
import java.util.Observable;
import java.util.Observer;

import ch.hsr.modules.uint1.heisenberglibrary.model.Customer;
import ch.hsr.modules.uint1.heisenberglibrary.model.IModelChangeType;
import ch.hsr.modules.uint1.heisenberglibrary.model.Library;
import ch.hsr.modules.uint1.heisenberglibrary.model.Loan;
import ch.hsr.modules.uint1.heisenberglibrary.model.ModelChangeTypeEnums;
import ch.hsr.modules.uint1.heisenberglibrary.model.ObservableModelChangeEvent;
import ch.hsr.modules.uint1.heisenberglibrary.view.UiComponentStrings;

/**
 * The tablemodel for the JTable in CustomerLoanDetailJPanel.
 * 
 * @author msyfrig
 */
public class CustomerLoanTableModel extends
        AbstractExtendendedEventTableModel<Loan> implements Observer {
    private static final long   serialVersionUID = -5057169214989804460L;
    private static List<String> columnNames      = new ArrayList<>(3);

    private Customer            specificCustomer;
    private Library             library;

    static {
        columnNames.add(UiComponentStrings
                .getString("CustomerLoanTableModel.loanTableColumn.book")); //$NON-NLS-1$
        columnNames.add(UiComponentStrings
                .getString("CustomerLoanTableModel.loanTableColumn.copyid")); //$NON-NLS-1$
        columnNames.add(UiComponentStrings
                .getString("CustomerLoanTableModel.loanTableColumn.lentuntil")); //$NON-NLS-1$

    }

    public CustomerLoanTableModel(Customer aDisplayedCustomer, Library aLibrary) {
        super(aLibrary.getActiveCustomerLoans(aDisplayedCustomer));
        library = aLibrary;
        specificCustomer = aDisplayedCustomer;
        library.addObserver(this);
    }

    @Override
    public Class<?> getColumnClass(int aColumnIndex) {
        Class<?> ret = String.class;
        switch (aColumnIndex) {
            case 2:
                ret = Date.class;
                break;
            default:
                ret = String.class;
                break;
        }
        return ret;
    }

    @Override
    public int getColumnCount() {
        return columnNames.size();
    }

    @Override
    public String getColumnName(int aColumnIndex) {
        return columnNames.get(aColumnIndex);
    }

    @Override
    public Object getValueAt(int aRowIndex, int aColumnIndex) {
        Loan specificCustomerLoan = data.get(aRowIndex);

        Object ret = null;

        switch (aColumnIndex) {
            case 0:
                ret = specificCustomerLoan.getCopy().getTitle().getTitle();
                break;
            case 1:
                ret = Long.valueOf(specificCustomerLoan.getCopy()
                        .getInventoryNumber());
                break;
            case 2:
                ret = specificCustomerLoan.getDueDate().getTime();
                break;
            default:
                ret = UiComponentStrings.getString("empty"); //$NON-NLS-1$
                break;
        }

        return ret;
    }

    @Override
    public void update(Observable aAnObservable, Object anArgument) {
        super.update(aAnObservable, anArgument);
        if (anArgument instanceof ObservableModelChangeEvent) {
            ObservableModelChangeEvent modelChange = (ObservableModelChangeEvent) anArgument;
            IModelChangeType type = modelChange.getChangeType();
            if (type == ModelChangeTypeEnums.Loan.ADDED
                    || type == ModelChangeTypeEnums.Loan.RETURNED) {
                data = library.getActiveCustomerLoans(specificCustomer);
                fireTableDataChanged();
            }

        }
    }
}