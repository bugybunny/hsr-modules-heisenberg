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
import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.text.MessageFormat;
import java.util.HashSet;
import java.util.Observable;
import java.util.Observer;
import java.util.Set;

import javax.swing.AbstractAction;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.RowFilter;
import javax.swing.border.EmptyBorder;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.AbstractTableModel;

import ch.hsr.modules.uint1.heisenberglibrary.controller.TableFilter;
import ch.hsr.modules.uint1.heisenberglibrary.model.Customer;
import ch.hsr.modules.uint1.heisenberglibrary.model.Library;
import ch.hsr.modules.uint1.heisenberglibrary.model.Loan;
import ch.hsr.modules.uint1.heisenberglibrary.model.LoanStatus;
import ch.hsr.modules.uint1.heisenberglibrary.model.ModelChangeType;
import ch.hsr.modules.uint1.heisenberglibrary.model.ModelChangeTypeEnums;
import ch.hsr.modules.uint1.heisenberglibrary.model.ObservableModelChangeEvent;
import ch.hsr.modules.uint1.heisenberglibrary.view.model.LoanTableModel;

/**
 * 
 * @author msyfrig
 */
public class LoanMainJPanel extends SearchableTableJPanel<Loan> implements
        Observer {
    private static final long                         serialVersionUID = 8186612854405487707L;

    private TableFilter<LoanTableModel>               tableFilter;
    private OnlyOverdueFilter<LoanTableModel, Object> onlyOverdueFilter;
    private JPanel                                    centerPanel;
    private JButton                                   viewSelectedButton;
    private JButton                                   addLoanButton;
    private JPanel                                    inventoryStatisticsPanel;
    private JPanel                                    inventoryPanel;
    private JLabel                                    numberOfLoansLabel;
    private JLabel                                    numberOfCurrentyLoanedCopiesLabel;
    private JLabel                                    overdueLabel;
    private JCheckBox                                 onlyOverdueCheckbox;
    private Component                                 horizontalStrut;
    private JPanel                                    panel;
    private JPanel                                    loanInventoryPanel;
    private JPanel                                    outerStatisticsPanel;
    private LoanDetailJDialog                         loanDetailDialog;

    private ViewSelectedCustomerLoansAction           viewSelectedCustomerLoansAction;
    private AddLoanAction                             addLoanAction;

    private Library                                   library;

    /**
     * Creates the panel.
     */
    public LoanMainJPanel(Library aLibrary) {
        super(aLibrary.getActiveLoans());
        library = aLibrary;
        initComponents();
        initHandlers();
        library.addObserver(this);
    }

    private void initComponents() {
        setBounds(100, 100, 1200, 441);
        setBorder(new EmptyBorder(5, 5, 5, 5));
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

        outerStatisticsPanel = new JPanel();
        outerStatisticsPanel.setBorder(new TitledBorder(new EtchedBorder(
                EtchedBorder.LOWERED, null, null), UiComponentStrings
                .getString("LoanMainJPanel.border.inventory.text"), //$NON-NLS-1$
                TitledBorder.LEADING, TitledBorder.TOP, null, null));
        add(outerStatisticsPanel);
        outerStatisticsPanel.setLayout(new FlowLayout(FlowLayout.LEFT, 5, 5));

        inventoryStatisticsPanel = new JPanel();
        outerStatisticsPanel.add(inventoryStatisticsPanel);
        ((FlowLayout) inventoryStatisticsPanel.getLayout())
                .setAlignment(FlowLayout.LEFT);

        String numberOfLoansText = MessageFormat.format(UiComponentStrings
                .getString("LoanMainJPanel.label.loannumber.text"), //$NON-NLS-1$
                Integer.valueOf(library.getLoans().size()));
        numberOfLoansLabel = new JLabel(numberOfLoansText);
        inventoryStatisticsPanel.add(numberOfLoansLabel);

        inventoryStatisticsPanel.add(Box.createHorizontalStrut(50));

        String numberOfCurrentlyLoanedCopiesText = MessageFormat
                .format(UiComponentStrings
                        .getString("LoanMainJPanel.label.currentlyloaned.text"), //$NON-NLS-1$
                        Integer.valueOf(library.getLentOutBooks().size()));
        numberOfCurrentyLoanedCopiesLabel = new JLabel(
                numberOfCurrentlyLoanedCopiesText);
        inventoryStatisticsPanel.add(numberOfCurrentyLoanedCopiesLabel);

        inventoryStatisticsPanel.add(Box.createHorizontalStrut(50));

        String numberOfOverdueLoansText = MessageFormat.format(
                UiComponentStrings
                        .getString("LoanMainJPanel.label.overdueloans.text"), //$NON-NLS-1$
                Integer.valueOf(library.getOverdueLoans().size()));
        overdueLabel = new JLabel(numberOfOverdueLoansText);
        inventoryStatisticsPanel.add(overdueLabel);

        loanInventoryPanel = new JPanel();
        loanInventoryPanel.setBorder(new TitledBorder(null, UiComponentStrings
                .getString("LoanMainJPanel.border.loaninventory.text"), //$NON-NLS-1$
                TitledBorder.LEADING, TitledBorder.TOP, null, null));
        add(loanInventoryPanel);
        loanInventoryPanel.setLayout(new BorderLayout(0, 0));

        JPanel topPane = new JPanel();
        loanInventoryPanel.add(topPane, BorderLayout.NORTH);
        topPane.setLayout(new BoxLayout(topPane, BoxLayout.Y_AXIS));

        panel = new JPanel();
        ((FlowLayout) panel.getLayout()).setAlignment(FlowLayout.LEFT);
        topPane.add(panel);

        inventoryPanel = new JPanel();
        topPane.add(inventoryPanel);
        inventoryPanel.setLayout(new FlowLayout(FlowLayout.LEFT, 5, 5));

        initSearchTextField(UiComponentStrings
                .getString("LoanMainJPanel.textfield.search.defaulttext")); //$NON-NLS-1$
        inventoryPanel.add(searchTextField);

        onlyOverdueCheckbox = new JCheckBox(
                UiComponentStrings
                        .getString("LoanMainJPanel.checkbox.onlyoverdue.text")); //$NON-NLS-1$
        inventoryPanel.add(onlyOverdueCheckbox);

        horizontalStrut = Box.createHorizontalStrut(50);
        inventoryPanel.add(horizontalStrut);

        String addLoanButtonText = UiComponentStrings
                .getString("LoanMainJPanel.button.addloan.text"); //$NON-NLS-1$
        String addLoanButtonEnabledTooltip = UiComponentStrings
                .getString("LoanMainJPanel.button.addloan.enabled.tooltip");//$NON-NLS-1$
        String addLoanButtonDisabledTooltip = UiComponentStrings
                .getString("LoanMainJPanel.button.addloan.disabled.tooltip");//$NON-NLS-1$
        addLoanButton = new ToolTipJButton(addLoanButtonText,
                addLoanButtonEnabledTooltip, addLoanButtonDisabledTooltip);

        inventoryPanel.add(addLoanButton);

        String viewSelectedButtonText = UiComponentStrings
                .getString("LoanMainJPanel.button.viewselected.text"); //$NON-NLS-1$
        String viewSelectedButtonEnabledToolTip = UiComponentStrings
                .getString("LoanMainJPanel.button.viewselected.enabled.tooltip");
        String viewSelectedButtonDisabledToolTip = UiComponentStrings
                .getString("LoanMainJPanel.button.viewselected.disabled.tooltip");
        viewSelectedButton = new ToolTipJButton(viewSelectedButtonText,
                viewSelectedButtonEnabledToolTip,
                viewSelectedButtonDisabledToolTip);
        inventoryPanel.add(viewSelectedButton);
        viewSelectedButton
                .setToolTipText(UiComponentStrings
                        .getString("LoanMainJPanel.button.viewselected.disabled.tooltip")); //$NON-NLS-1$

        centerPanel = new JPanel();
        loanInventoryPanel.add(centerPanel, BorderLayout.CENTER);

        centerPanel.setLayout(new BorderLayout(0, 0));

        // init the table with the model with all properties and handlers and
        // add it the panel
        initTable(new LoanTableModel(dataList));
        tableFilter = new TableFilter<>(table, searchTextField);
        centerPanel.add(tableScrollPane);
    }

    private void initHandlers() {
        viewSelectedCustomerLoansAction = new ViewSelectedCustomerLoansAction(
                viewSelectedButton.getText());
        viewSelectedCustomerLoansAction.setEnabled(false);
        viewSelectedButton.setAction(viewSelectedCustomerLoansAction);
        viewSelectedButton.setMnemonic('v');

        addLoanAction = new AddLoanAction(addLoanButton.getText());
        addLoanButton.setAction(addLoanAction);
        addLoanButton.setMnemonic('n');

        table.getSelectionModel().addListSelectionListener(
                new BookTableSelectionListener());

        onlyOverdueFilter = new OnlyOverdueFilter<>();
        onlyOverdueCheckbox.addItemListener(new ItemListener() {

            @Override
            public void itemStateChanged(ItemEvent anItemStateChangedEvent) {
                if (anItemStateChangedEvent.getStateChange() == ItemEvent.SELECTED) {
                    tableFilter.addFilter(onlyOverdueFilter);
                } else {
                    tableFilter.removeFilter(onlyOverdueFilter);
                }
                tableFilter.filterTable();
            }
        });
    }

    @Override
    public void update(Observable anObservable, Object anArgument) {
        if (anArgument instanceof ObservableModelChangeEvent) {
            ObservableModelChangeEvent modelChange = (ObservableModelChangeEvent) anArgument;
            ModelChangeType type = modelChange.getChangeType();

            if (type == ModelChangeTypeEnums.Loan.NUMBER) {
                numberOfLoansLabel
                        .setText(MessageFormat.format(
                                UiComponentStrings
                                        .getString("LoanMainJPanel.label.loannumber.text"), //$NON-NLS-1$
                                modelChange.getNewValue()));
            } else if (type == ModelChangeTypeEnums.Loan.ACTIVE_NUMBER) {
                numberOfCurrentyLoanedCopiesLabel
                        .setText(MessageFormat.format(
                                UiComponentStrings
                                        .getString("LoanMainJPanel.label.currentlyloaned.text"), //$NON-NLS-1$
                                modelChange.getNewValue()));
            } else if (type == ModelChangeTypeEnums.Loan.ADDED
                    || type == ModelChangeTypeEnums.Loan.REMOVED) {
                ((AbstractTableModel) table.getModel()).fireTableDataChanged();
            }
            // TODO dataList neu setzen wenn sich bei den activeLoans was ändert
            // TODO event für overdue loan Anzahl Verniedrigung, sobald ein eine
            // overdue Ausleihe zurückgegeben wurde
            // TODO events für remove und update und added von loans mit
            // aktualisierung in tablemodel. achtung: getactiveloans und so muss
            // wahrscheinlich neu aufgerufen und alle observer neu hinzugefügt
            // werden
        }
    }

    /**
     * Listener to open the selected book rows in {@code loanTable} in a
     * {@link BookDetailJPanel} detail tab.
     * 
     * @author msyfrig
     */
    private class ViewSelectedCustomerLoansAction extends AbstractAction {
        private static final long serialVersionUID = 8000376743315270169L;

        private ViewSelectedCustomerLoansAction(String anActionName) {
            super(anActionName);
        }

        /**
         * Opens all selected booksPanel and their detailview. If a dialog for a
         * detailview is already open and not on top of the z-order stack, it
         * will be placed on top and focused.
         */
        @Override
        public void actionPerformed(ActionEvent anActionEvent) {
            // check first if the detaildialog is already opened, if so bring it
            // to the front
            if (loanDetailDialog == null) {
                loanDetailDialog = new LoanDetailJDialog(null);
            }
            loanDetailDialog.setVisible(true);
            loanDetailDialog.toFront();

            // add all unique customers to open to a set, faster than check if
            // the tab is already open
            Set<Customer> customersToOpenSet = new HashSet<>();

            for (int tempLoan : table.getSelectedRows()) {
                Customer selectedCustomerLoan = dataList.get(
                        table.convertRowIndexToModel(tempLoan)).getCustomer();
                customersToOpenSet.add(selectedCustomerLoan);
            }
            for (Customer tempCustomer : customersToOpenSet) {
                loanDetailDialog.openCustomerLoanTab(tempCustomer, library);
            }
        }
    }

    private class AddLoanAction extends AbstractAction {
        private static final long serialVersionUID = -1615608675302687626L;

        private AddLoanAction(String anActionName) {
            super(anActionName);
        }

        /**
         * Opens an empty detail view with the ability to save a new book.
         */
        @Override
        public void actionPerformed(ActionEvent anActionEvent) {
            // check first if the detaildialog is already opened, if so bring it
            // to the front
            if (loanDetailDialog == null) {
                loanDetailDialog = new LoanDetailJDialog(null);
            }
            loanDetailDialog.setVisible(true);
            loanDetailDialog.toFront();
            loanDetailDialog.openCustomerLoanTab(null, library);
        }
    }

    private class BookTableSelectionListener implements ListSelectionListener {
        /**
         * Enables the edit button if at least one book is selected, else
         * disables it.
         */
        @Override
        public void valueChanged(ListSelectionEvent aSelectionEvent) {
            if (table.getSelectedRows().length > 0) {
                viewSelectedCustomerLoansAction.setEnabled(true);
            } else {
                viewSelectedCustomerLoansAction.setEnabled(false);
            }
        }
    }

    private class OnlyOverdueFilter<M extends LoanTableModel, I extends Object>
            extends RowFilter<M, I> {
        @Override
        public boolean include(
                javax.swing.RowFilter.Entry<? extends M, ? extends I> anEntry) {
            LoanStatus loanStatus = (LoanStatus) anEntry.getModel().getValueAt(
                    ((Integer) anEntry.getIdentifier()).intValue(), 0);
            return loanStatus == LoanStatus.DUE;
        }
    }
}
