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

import java.util.List;
import java.util.Observable;
import java.util.Observer;

import javax.swing.AbstractListModel;
import javax.swing.ComboBoxModel;

import ch.hsr.modules.uint1.heisenberglibrary.model.Customer;
import ch.hsr.modules.uint1.heisenberglibrary.model.Library;

/**
 * TODO COMMENT ME!
 * 
 * @author msyfrig
 */
public class CustomerComboboxModel extends AbstractListModel<Customer>
        implements ComboBoxModel<Customer>, Observer {
    private static final long serialVersionUID = -5830729956015597480L;
    private Library           library;
    private Customer          selectedCustomer;
    private List<Customer>    customers;

    public CustomerComboboxModel(Library aLibrary) {
        library = aLibrary;
        library.addObserver(this);
        customers = library.getCustomers();
        if (customers.size() > 0) {
            selectedCustomer = customers.get(0);
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
            selectedCustomer = (Customer) anObject;
            fireContentsChanged(this, -1, -1);
        }
    }

    @Override
    public Customer getElementAt(int anIndex) {
        return customers.get(anIndex);
    }

    @Override
    public Object getSelectedItem() {
        StringBuilder customerString = new StringBuilder(60);
        customerString.append(selectedCustomer.getName());
        customerString.append(", ");
        customerString.append(selectedCustomer.getSurname());
        customerString.append(", ");
        customerString.append(selectedCustomer.getZip());
        customerString.append(" (");
        customerString.append(library.getActiveCustomerLoans(selectedCustomer)
                .size());
        customerString.append(')');

        return customerString.toString();
    }

    @Override
    public void update(Observable aO, Object aArg) {
        // TODO Auto-generated method stub

    }
}
