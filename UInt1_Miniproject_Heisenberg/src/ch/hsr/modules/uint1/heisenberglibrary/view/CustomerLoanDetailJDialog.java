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
import java.awt.Dimension;
import java.util.Observable;
import java.util.Observer;

import javax.swing.JFrame;

import ch.hsr.modules.uint1.heisenberglibrary.model.Customer;
import ch.hsr.modules.uint1.heisenberglibrary.model.Library;

/**
 * 
 * @author msyfrig
 */
public class CustomerLoanDetailJDialog extends
        AbstractTabbedPaneDialog<Customer> implements Observer {
    private static final long serialVersionUID = 1528185325072558131L;

    public CustomerLoanDetailJDialog(JFrame anOwner) {
        super(anOwner, UiComponentStrings
                .getString("CustomerLoanDetailJDialog.title")); //$NON-NLS-1$
    }

    @Override
    protected void initComponents() {
        setMinimumSize(new Dimension(650, 370));
        setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
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

            String tabTitle = UiComponentStrings
                    .getString("CustomerLoanDetailJDialog.tab.title.enteringnewloan.text"); //$NON-NLS-1$
            if (aCustomerToOpen != null) {
                addObserverForObservable(aCustomerToOpen, this);
                tabTitle = getTabTitleForObject(aCustomerToOpen, false);

                tabbedPane.addTab(tabTitle, null, detailCustomerLoanPanel,
                        aCustomerToOpen.toString());
            } else {
                tabbedPane
                        .addTab(tabTitle,
                                null,
                                detailCustomerLoanPanel,
                                UiComponentStrings
                                        .getString("CustomerLoanDetailJDialog.tab.title.enteringnewloan.tooltip")); //$NON-NLS-1$
            }
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
        title.append("  ");
        return title.toString();
    }

    @Override
    public void update(Observable anObservable, Object anArgument) {
        // not used since customers cannot be changed
    }

    @Override
    protected void objectInTabUpdated(Customer anUpdatedCustomer) {
        // not used since customers cannot be changed
    }
}
