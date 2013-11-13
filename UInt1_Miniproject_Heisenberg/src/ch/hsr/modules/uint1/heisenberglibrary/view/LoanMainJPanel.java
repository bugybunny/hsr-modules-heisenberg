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
import java.awt.event.ActionListener;
import java.awt.event.InputEvent;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.KeyEvent;
import java.text.MessageFormat;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Observable;
import java.util.Observer;
import java.util.Set;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.KeyStroke;
import javax.swing.ListSelectionModel;
import javax.swing.RowFilter;
import javax.swing.border.EmptyBorder;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.AbstractTableModel;

import ch.hsr.modules.uint1.heisenberglibrary.controller.TableFilter;
import ch.hsr.modules.uint1.heisenberglibrary.controller.TableModelChangeListener;
import ch.hsr.modules.uint1.heisenberglibrary.model.Library;
import ch.hsr.modules.uint1.heisenberglibrary.model.Loan;
import ch.hsr.modules.uint1.heisenberglibrary.model.LoanStatus;
import ch.hsr.modules.uint1.heisenberglibrary.view.model.LoanTableModel;

public class LoanMainJPanel extends JPanel implements Observer {
    private static final long                         serialVersionUID = 8186612854405487707L;

    private JTable                                    loanTable;
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
    private GhostHintJTextField                       searchTextField;
    private JCheckBox                                 onlyOverdueCheckbox;
    private Component                                 horizontalStrut;
    private JPanel                                    panel;
    private JPanel                                    loanInventoryPanel;
    private JPanel                                    outerStatisticsPanel;
    private BookDetailJDialog                         loanDetailDialog;

    private List<Loan>                                loanList;
    private Library                                   bookMasterlibrary;

    /**
     * Creates the panel.
     */
    public LoanMainJPanel(Library library) {
        loanList = library.getLoans();
        bookMasterlibrary = library;

        initComponents();
        initHandlers();
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
                Integer.valueOf(bookMasterlibrary.getLoans().size()));
        numberOfLoansLabel = new JLabel(numberOfLoansText);
        inventoryStatisticsPanel.add(numberOfLoansLabel);

        inventoryStatisticsPanel.add(Box.createHorizontalStrut(50));

        String numberOfCurrentlyLoanedCopiesText = MessageFormat
                .format(UiComponentStrings
                        .getString("LoanMainJPanel.label.currentlyloaned.text"), //$NON-NLS-1$
                        Integer.valueOf(bookMasterlibrary.getLentOutBooks()
                                .size()));
        numberOfCurrentyLoanedCopiesLabel = new JLabel(
                numberOfCurrentlyLoanedCopiesText);
        inventoryStatisticsPanel.add(numberOfCurrentyLoanedCopiesLabel);

        inventoryStatisticsPanel.add(Box.createHorizontalStrut(50));

        String numberOfOverdueLoansText = MessageFormat
                .format(UiComponentStrings
                        .getString("LoanMainJPanel.label.overdueloans.text"), //$NON-NLS-1$
                        Integer.valueOf(bookMasterlibrary.getOverdueLoans()
                                .size()));
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

        searchTextField = new GhostHintJTextField(
                UiComponentStrings
                        .getString("LoanMainJPanel.textfield.search.defaulttext")); //$NON-NLS-1$
        inventoryPanel.add(searchTextField);
        searchTextField.setColumns(10);

        onlyOverdueCheckbox = new JCheckBox(
                UiComponentStrings
                        .getString("LoanMainJPanel.checkbox.onlyoverdue.text")); //$NON-NLS-1$
        inventoryPanel.add(onlyOverdueCheckbox);

        horizontalStrut = Box.createHorizontalStrut(50);
        inventoryPanel.add(horizontalStrut);

        addLoanButton = new JButton(
                UiComponentStrings
                        .getString("LoanMainJPanel.button.addloan.text")); //$NON-NLS-1$
        inventoryPanel.add(addLoanButton);

        viewSelectedButton = new JButton(
                UiComponentStrings
                        .getString("LoanMainJPanel.button.viewselected.text")); //$NON-NLS-1$
        inventoryPanel.add(viewSelectedButton);
        viewSelectedButton
                .setToolTipText(UiComponentStrings
                        .getString("LoanMainJPanel.button.viewselected.disabled.tooltip")); //$NON-NLS-1$
        viewSelectedButton.setEnabled(false);
        viewSelectedButton.setMnemonic('v');

        centerPanel = new JPanel();
        loanInventoryPanel.add(centerPanel, BorderLayout.CENTER);

        centerPanel.setLayout(new BorderLayout(0, 0));

        loanTable = new JTable();
        loanTable.setModel(new LoanTableModel(bookMasterlibrary.getLoans()));
        tableFilter = new TableFilter<>(loanTable, searchTextField);
        loanTable.getTableHeader().setReorderingAllowed(false);
        loanTable.setAutoCreateRowSorter(true);
        loanTable.setCellSelectionEnabled(true);
        loanTable.setFillsViewportHeight(true);
        loanTable.setColumnSelectionAllowed(false);
        loanTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        JScrollPane jsp = new JScrollPane(loanTable);
        centerPanel.add(jsp);
    }

    private void initHandlers() {
        viewSelectedButton.addActionListener(new ViewSelectedButtonListener());
        addLoanButton.addActionListener(new AddBookButtonListener());

        loanTable.getSelectionModel().addListSelectionListener(
                new BookTableSelectionListener());

        // ctrl+f: switch focus to searchfield for table
        Action searchAction = new AbstractAction("search") { //$NON-NLS-1$
            private static final long serialVersionUID = -6626318103198277780L;

            @Override
            public void actionPerformed(ActionEvent anActionEvent) {
                searchTextField.requestFocus();
            }
        };

        KeyStroke ctrlF = KeyStroke.getKeyStroke(KeyEvent.VK_F,
                InputEvent.CTRL_DOWN_MASK);
        getInputMap(JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT).put(ctrlF,
                searchAction.getValue(Action.NAME));
        getActionMap().put(searchAction.getValue(Action.NAME), searchAction);

        ((LoanTableModel) loanTable.getModel())
                .addTableModelChangeListener(new TableModelChangeListener() {
                    private Collection<Loan> previouslySelectedBooks;

                    @Override
                    public void tableIsAboutToUpdate() {
                        previouslySelectedBooks = saveSelectedRows();
                    }

                    @Override
                    public void tableChanged() {
                        restoreSelectedRows(previouslySelectedBooks);
                    }
                });
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

    /**
     * Saves the selected loans in the table. The actual loan instances are
     * saved since loans can be added or removed so only saving the row index is
     * not enough.
     * 
     * @return set of currently selected loans
     */
    private Set<Loan> saveSelectedRows() {
        Set<Loan> selectedLoans = new HashSet<>(
                loanTable.getSelectedRowCount());
        for (int selectionIndex : loanTable.getSelectedRows()) {
            Loan singleSelectedBook = loanList.get(loanTable
                    .convertRowIndexToModel(selectionIndex));
            selectedLoans.add(singleSelectedBook);
        }
        return selectedLoans;
    }

    /**
     * Reselect the given loans in the table if they still exist.
     * 
     * @param someLoansToSelect
     *            the loans to select
     */
    private void restoreSelectedRows(Collection<Loan> someLoansToSelect) {
        for (Loan tempLoanToSelect : someLoansToSelect) {

            int indexInList = loanList.indexOf(tempLoanToSelect);
            // do nothing if not found and loan has been removed
            if (indexInList > -1) {
                int indexToSelectInView = loanTable
                        .convertRowIndexToView(indexInList);
                loanTable.getSelectionModel().addSelectionInterval(
                        indexToSelectInView, indexToSelectInView);
            }
        }
    }

    @Override
    public void update(Observable anObservable, Object anArgument) {
        if (anObservable instanceof Library) {
            System.out.println("number of books" + anArgument); //$NON-NLS-1$
        }
        ((AbstractTableModel) loanTable.getModel()).fireTableDataChanged();
        // numberOfBooks =
    }

    /**
     * Listener to open the selected book rows in {@code loanTable} in a
     * {@link BookDetailJPanel} detail tab.
     * 
     * @author msyfrig
     */
    private class ViewSelectedButtonListener implements ActionListener {
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
                loanDetailDialog = new BookDetailJDialog(null);
            }
            loanDetailDialog.setVisible(true);
            loanDetailDialog.toFront();

            for (int tempBook : loanTable.getSelectedRows()) {
                Loan selectedBook = loanList.get(loanTable
                        .convertRowIndexToModel(tempBook));
                // TODO openloan detail
                // loanDetailDialog.openBookTab(selectedBook,
                // bookMasterlibrary);
            }
        }
    }

    private class AddBookButtonListener implements ActionListener {
        /**
         * Opens an empty detail view with the ability to save a new book.
         */
        @Override
        public void actionPerformed(ActionEvent anActionEvent) {
            // TODO Opens a new window, should open a new tab instead

            // check first if the detaildialog is already opened, if so bring it
            // to the front
            if (loanDetailDialog == null) {
                loanDetailDialog = new BookDetailJDialog(null);
            }
            loanDetailDialog.setVisible(true);
            loanDetailDialog.toFront();

            loanDetailDialog.openBookTab(null, bookMasterlibrary);

        }
    }

    private class BookTableSelectionListener implements ListSelectionListener {
        /**
         * Enables the edit button if at least one book is selected, else
         * disables it.
         */
        @Override
        public void valueChanged(ListSelectionEvent aSelectionEvent) {
            if (loanTable.getSelectedRows().length > 0) {
                viewSelectedButton.setEnabled(true);
                UiComponentStrings
                        .getString("LoanMainJPanel.button.viewselected.enabled.tooltip"); //$NON-NLS-1$
            } else {
                viewSelectedButton.setEnabled(false);
                UiComponentStrings
                        .getString("LoanMainJPanel.button.viewselected.disabled.tooltip"); //$NON-NLS-1$
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
