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
import java.awt.event.ActionEvent;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Observable;
import java.util.Observer;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JFrame;
import javax.swing.JTabbedPane;
import javax.swing.KeyStroke;

import ch.hsr.modules.uint1.heisenberglibrary.model.Customer;
import ch.hsr.modules.uint1.heisenberglibrary.model.Library;

/**
 * 
 * @author msyfrig
 */
public class LoanDetailJDialog extends NonModalJDialog implements Observer {
    private static final long              serialVersionUID     = 1528185325072558131L;
    /** The tabbed pane that holds all detailviews as tabs for all loans. */
    private JTabbedPane                    tabbedPane;
    private List<CustomerLoanDetailJPanel> openCustomerLoanList = new ArrayList<>();

    public LoanDetailJDialog(JFrame anOwner) {
        super(anOwner, UiComponentStrings.getString("LoanDetailJDialog.title")); //$NON-NLS-1$
    }

    @Override
    protected void initComponents() {
        setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
        setSize(100, 100);
        getContentPane().setLayout(new BorderLayout());
        tabbedPane = new JTabbedPane(JTabbedPane.TOP);
        getContentPane().add(tabbedPane, BorderLayout.CENTER);
    }

    @Override
    protected void initHandlers() {
        addComponentListener(new ComponentAdapter() {
            /**
             * All opened book tabs are closed before hiding this dialog so they
             * are not still open if the user selects a new book and expects to
             * open a completely new dialog.
             */
            @Override
            public void componentHidden(ComponentEvent aComponentHiddenEvent) {
                openCustomerLoanList.clear();
                tabbedPane.removeAll();
            }
        });
    }

    public void openCustomerLoanTab(Customer aCustomerToOpen, Library aLibrary) {
        // check if a tab with the given tab is already open
        CustomerLoanDetailJPanel detailCustomerLoanPanel = getTabForCustomer(aCustomerToOpen);

        // if not open yet, create it and all listeners and actions
        if (detailCustomerLoanPanel == null) {
            detailCustomerLoanPanel = new CustomerLoanDetailJPanel(
                    aCustomerToOpen, aLibrary);

            String tabTitle = "add new book";
            if (aCustomerToOpen != null) {
                // add observer to this book so we notice when the title has
                // changed
                aCustomerToOpen.addObserver(this);
                tabTitle = getTabTitleForCustomer(aCustomerToOpen);

                tabbedPane.addTab(tabTitle, null, detailCustomerLoanPanel,
                        aCustomerToOpen.toString());
            } else {
                tabbedPane.addTab(tabTitle, null, detailCustomerLoanPanel,
                        "Entering a new book");
            }
            addHandlersToTab(detailCustomerLoanPanel);
            openCustomerLoanList.add(detailCustomerLoanPanel);
        }
        tabbedPane.setSelectedComponent(detailCustomerLoanPanel);
        pack();
    }

    private static String getTabTitleForCustomer(Customer aCustomer) {
        StringBuffer title = new StringBuffer(15);
        String customerString = aCustomer.toString();
        if (customerString.length() >= 15) {
            title.append(customerString.substring(0, 15));
        } else {
            title.append(customerString);
        }
        return title.toString();
    }

    private void addHandlersToTab(CustomerLoanDetailJPanel aCustomerLoanPanel) {
        Map<KeyStroke, Action> actionMapForBookTab = new HashMap<>(2);
        actionMapForBookTab.put(KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0),
                new DisposeAction("dispose", aCustomerLoanPanel)); //$NON-NLS-1$
        aCustomerLoanPanel.addAncestorActions(actionMapForBookTab);
    }

    private CustomerLoanDetailJPanel getTabForCustomer(Customer aCustomer) {
        CustomerLoanDetailJPanel detailCustomerLoanPanel = null;
        if (aCustomer != null) {
            for (CustomerLoanDetailJPanel tempBookDetailView : openCustomerLoanList) {
                if (tempBookDetailView.getDisplayedCustomer() == aCustomer) {
                    detailCustomerLoanPanel = tempBookDetailView;
                }
            }
        }
        return detailCustomerLoanPanel;
    }

    @Override
    public void update(Observable aO, Object aArg) {
        // TODO Auto-generated method stub

    }

    /**
     * Closes an opened tab for a book, removes it from the list of opened books
     * so it can be reopened. If the closed tab was the last one, this dialog is
     * closed.
     * 
     * @param aCustomerLoanTabToClose
     *            the book detail tab to close
     */
    private void closeTab(CustomerLoanDetailJPanel aCustomerLoanTabToClose) {
        if (aCustomerLoanTabToClose != null) {
            tabbedPane.remove(aCustomerLoanTabToClose);
            openCustomerLoanList.remove(aCustomerLoanTabToClose);
            // close this dialog if this was the last open tab
            if (openCustomerLoanList.isEmpty()) {
                dispose();
            }
        }
    }

    /**
     * Action to handle the closing of a tab. Informs the user if there are
     * unsaved changes in the tab before closing.
     * 
     * @author msyfrig
     */
    private class DisposeAction extends AbstractAction {
        private static final long        serialVersionUID = -7356098093520315826L;
        private CustomerLoanDetailJPanel customerLoanDetailTab;

        /**
         * Creates a new action with the given name and the associated book tab
         * in which this
         */
        private DisposeAction(String anActionName,
                CustomerLoanDetailJPanel anAssociatedCustomerLoanTab) {
            super(anActionName);
            customerLoanDetailTab = anAssociatedCustomerLoanTab;
        }

        /**
         * Checks if the tab has unsaved changes and informs the user if so and
         * closes the tab.
         */
        @Override
        public void actionPerformed(ActionEvent anActionEvent) {
            closeTab(customerLoanDetailTab);
        }
    }

}
