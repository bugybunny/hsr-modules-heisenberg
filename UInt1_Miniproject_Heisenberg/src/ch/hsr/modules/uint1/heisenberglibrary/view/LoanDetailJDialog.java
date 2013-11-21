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

import java.awt.BorderLayout;
import java.util.Observable;
import java.util.Observer;

import javax.swing.JFrame;

import ch.hsr.modules.uint1.heisenberglibrary.model.Customer;
import ch.hsr.modules.uint1.heisenberglibrary.model.Library;

/**
 * 
 * @author msyfrig
 */
public class LoanDetailJDialog extends AbstractTabbedPaneDialog<Customer>
        implements Observer {
    private static final long serialVersionUID = 1528185325072558131L;

    public LoanDetailJDialog(JFrame anOwner) {
        super(anOwner, UiComponentStrings.getString("LoanDetailJDialog.title")); //$NON-NLS-1$
    }

    @Override
    protected void initComponents() {
        setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
        setSize(100, 100);
        getContentPane().setLayout(new BorderLayout());
        initTabbedPane();
        getContentPane().add(tabbedPane, BorderLayout.CENTER);
    }

    public void openCustomerLoanTab(Customer aCustomerToOpen, Library aLibrary) {
        // check if a tab with the given tab is already open
        AbstractObservableObjectJPanel<Customer> detailCustomerLoanPanel = getTabForObject(aCustomerToOpen);

        // if not open yet, create it and all listeners and actions
        if (detailCustomerLoanPanel == null) {
            detailCustomerLoanPanel = new CustomerLoanDetailJPanel(
                    aCustomerToOpen, aLibrary);

            String tabTitle = "enter new loan";
            if (aCustomerToOpen != null) {
                // add observer to this book so we notice when the title has
                // changed
                aCustomerToOpen.addObserver(this);
                tabTitle = getTabTitleForObject(aCustomerToOpen, false);

                tabbedPane.addTab(tabTitle, null, detailCustomerLoanPanel,
                        aCustomerToOpen.toString());
            } else {
                tabbedPane.addTab(tabTitle, null, detailCustomerLoanPanel,
                        "Entering new loan");
            }
            addHandlersToTab(detailCustomerLoanPanel);
            openObjectTabList.add(detailCustomerLoanPanel);
        }
        tabbedPane.setSelectedComponent(detailCustomerLoanPanel);
        pack();
    }

    @Override
    protected String getTabTitleForObject(Customer aCustomer, boolean isDirty) {
        StringBuffer title = new StringBuffer(15);
        String customerString = aCustomer.toString();
        if (customerString.length() >= 15) {
            title.append(customerString.substring(0, 15));
        } else {
            title.append(customerString);
        }
        return title.toString();
    }

    @Override
    public void update(Observable aO, Object aArg) {
        // TODO Auto-generated method stub
    }

    @Override
    protected void objectInTabUpdated(Customer anUpdatedCustomer) {
        // TODO prüfen ob gebraucht
        AbstractObservableObjectJPanel<Customer> detailCustomerPanel = getTabForObject(anUpdatedCustomer);
        if (detailCustomerPanel != null) {
            int tabIndex = tabbedPane.indexOfComponent(detailCustomerPanel);

        }
    }
}
