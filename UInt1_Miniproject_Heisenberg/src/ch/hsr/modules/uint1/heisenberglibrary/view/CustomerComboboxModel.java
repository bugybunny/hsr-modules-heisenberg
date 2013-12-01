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

import java.util.ArrayList;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

import javax.swing.AbstractListModel;
import javax.swing.ComboBoxModel;

import ch.hsr.modules.uint1.heisenberglibrary.model.Customer;
import ch.hsr.modules.uint1.heisenberglibrary.model.IModelChangeType;
import ch.hsr.modules.uint1.heisenberglibrary.model.Library;
import ch.hsr.modules.uint1.heisenberglibrary.model.ModelChangeTypeEnums;
import ch.hsr.modules.uint1.heisenberglibrary.model.ObservableModelChangeEvent;
import ch.hsr.modules.uint1.heisenberglibrary.view.CustomerComboboxModel.DisplayableCustomer;
import ch.hsr.modules.uint1.heisenberglibrary.view.model.IDisposable;

/**
 * TODO COMMENT ME!
 * 
 * @author msyfrig
 */
public class CustomerComboboxModel extends
        AbstractListModel<DisplayableCustomer> implements
        ComboBoxModel<DisplayableCustomer>, Observer, IDisposable {
    private static final long         serialVersionUID = -5830729956015597480L;
    private Library                   library;
    private DisplayableCustomer       selectedCustomer;
    private List<DisplayableCustomer> customers;

    public CustomerComboboxModel(Library aLibrary) {
        library = aLibrary;
        library.addObserver(this);
        initCustomers(library.getCustomers());
        if (customers.size() > 0) {
            selectedCustomer = customers.get(0);
        }
    }

    private void initCustomers(List<Customer> someCustomers) {
        if (customers == null) {
            customers = new ArrayList<>(someCustomers.size());
        } else {
            customers.clear();
        }

        for (Customer tempCustomer : someCustomers) {
            int activeLoanCount = library.getActiveCustomerLoans(tempCustomer)
                    .size();
            DisplayableCustomer customerToAdd = new DisplayableCustomer(
                    tempCustomer, activeLoanCount);
            customers.add(customerToAdd);
        }
    }

    @Override
    public int getSize() {
        return customers.size();
    }

    @Override
    public void setSelectedItem(Object anObject) {
        if ((selectedCustomer != null && !selectedCustomer.equals(anObject))
                || selectedCustomer == null && anObject != null) {
            selectedCustomer = (DisplayableCustomer) anObject;
            fireContentsChanged(this, -1, -1);
        }
    }

    @Override
    public DisplayableCustomer getElementAt(int anIndex) {
        return customers.get(anIndex);
    }

    public DisplayableCustomer getDisplayableCustomerForCustomer(
            Customer aCustomer) {
        return getElementAt(library.getCustomers().indexOf(aCustomer));
    }

    public Customer getCustomerForDisplayableCustomer(
            DisplayableCustomer aDisplayableCustomer) {
        return library.getCustomers().get(
                customers.indexOf(aDisplayableCustomer));
    }

    @Override
    public Object getSelectedItem() {
        return selectedCustomer;
    }

    @Override
    public void update(Observable anObservable, Object anArgument) {
        if (anArgument instanceof ObservableModelChangeEvent) {
            ObservableModelChangeEvent modelChange = (ObservableModelChangeEvent) anArgument;
            IModelChangeType type = modelChange.getChangeType();
            if (type == ModelChangeTypeEnums.Loan.ACTIVE_NUMBER) {
                int selectedIndex = customers.indexOf(selectedCustomer);
                initCustomers(library.getCustomers());
                setSelectedItem(getElementAt(selectedIndex));
            }
        }
    }

    protected class DisplayableCustomer extends Customer {
        private int activeLoanCount;

        private DisplayableCustomer(Customer aCustomer, int anActiveLoanCount) {
            super(aCustomer.getName(), aCustomer.getSurname());
            city = aCustomer.getCity();
            street = aCustomer.getStreet();
            zip = aCustomer.getZip();
            activeLoanCount = anActiveLoanCount;
        }

        public int getActiveLoanCount() {
            return activeLoanCount;
        }

        protected void setActiveLoanCount(int aActiveLoanCount) {
            activeLoanCount = aActiveLoanCount;
        }

        @Override
        public int hashCode() {
            return super.hashCode();
        }

        @Override
        public boolean equals(Object anotherCustomer) {
            if (this == anotherCustomer) {
                return true;
            }
            return super.equals(anotherCustomer);
        }

        @Override
        public String toString() {
            StringBuilder customerString = new StringBuilder(60);
            customerString.append(surname);
            customerString.append(", "); //$NON-NLS-1$
            customerString.append(name);
            customerString.append(", "); //$NON-NLS-1$
            customerString.append(zip);
            customerString.append(" ("); //$NON-NLS-1$
            customerString.append(activeLoanCount);
            customerString.append(')');

            return customerString.toString();
        }

    }

    @Override
    public void cleanUpBeforeDispose() {
        library.deleteObserver(this);
    }
}
