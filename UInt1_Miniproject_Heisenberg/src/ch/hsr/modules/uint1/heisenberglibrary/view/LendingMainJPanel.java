package ch.hsr.modules.uint1.heisenberglibrary.view;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Observable;
import java.util.Observer;
import java.util.Set;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.RowFilter;
import javax.swing.border.EmptyBorder;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableRowSorter;

import ch.hsr.modules.uint1.heisenberglibrary.controller.TableModelChangeListener;
import ch.hsr.modules.uint1.heisenberglibrary.model.BookDO;
import ch.hsr.modules.uint1.heisenberglibrary.model.Library;
import ch.hsr.modules.uint1.heisenberglibrary.view.model.LoanTableModel;

public class LendingMainJPanel extends JPanel implements Observer {
    private static final long              serialVersionUID = 8186612854405487707L;

    /**
     * The table that displays all different booktypes in the library, not the
     * actual copies.
     */
    private JTable                         lendingTable;
    private TableRowSorter<LoanTableModel> tableSorter;
    private JPanel                         centerPanel;
    private JButton                        viewSelectedButton;
    private JButton                        addLoanButton;
    private JPanel                         inventoryStatisticsPanel;
    private JPanel                         inventoryPanel;
    private JLabel                         numberOfLoansLabel;
    private JLabel                         numberOfCurrentyLoanedCopiesLabel;
    private JLabel                         overdueLabel;
    private GhostHintJTextField            searchTextField;
    private JCheckBox                      onlyOverdueCheckbox;
    private Component                      horizontalStrut;
    private JPanel                         panel;
    private JPanel                         loanInventoryPanel;
    private JPanel                         outerStatisticsPanel;
    private BookDetailJDialog              loanDetailDialog;

    private List<BookDO>                   bookList;
    private Library                        bookMasterlibrary;

    /**
     * Creates the frame.
     * 
     * @param aBooks
     */
    public LendingMainJPanel(Library library) {
        bookList = library.getBooks();
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
                .getString("LendingMainJPanel.border.inventory.text"), //$NON-NLS-1$
                TitledBorder.LEADING, TitledBorder.TOP, null, null));
        add(outerStatisticsPanel);
        outerStatisticsPanel.setLayout(new FlowLayout(FlowLayout.LEFT, 5, 5));

        inventoryStatisticsPanel = new JPanel();
        outerStatisticsPanel.add(inventoryStatisticsPanel);
        ((FlowLayout) inventoryStatisticsPanel.getLayout())
                .setAlignment(FlowLayout.LEFT);

        String numberOfLoansText = MessageFormat.format(UiComponentStrings
                .getString("LendingMainJPanel.label.loannumber.text"), //$NON-NLS-1$
                Integer.valueOf(bookMasterlibrary.getLoans().size()));
        numberOfLoansLabel = new JLabel(numberOfLoansText);
        inventoryStatisticsPanel.add(numberOfLoansLabel);

        inventoryStatisticsPanel.add(Box.createHorizontalStrut(50));

        String numberOfCurrentlyLoanedCopiesText = MessageFormat
                .format(UiComponentStrings
                        .getString("LendingMainJPanel.label.currentlyloaned.text"), //$NON-NLS-1$
                        Integer.valueOf(bookMasterlibrary.getLentOutBooks()
                                .size()));
        numberOfCurrentyLoanedCopiesLabel = new JLabel(
                numberOfCurrentlyLoanedCopiesText);
        inventoryStatisticsPanel.add(numberOfCurrentyLoanedCopiesLabel);

        inventoryStatisticsPanel.add(Box.createHorizontalStrut(50));

        String numberOfOverdueLoansText = MessageFormat
                .format(UiComponentStrings
                        .getString("LendingMainJPanel.label.overdueloans.text"), //$NON-NLS-1$
                        Integer.valueOf(bookMasterlibrary.getOverdueLoans()
                                .size()));
        overdueLabel = new JLabel(numberOfOverdueLoansText);
        inventoryStatisticsPanel.add(overdueLabel);

        loanInventoryPanel = new JPanel();
        loanInventoryPanel.setBorder(new TitledBorder(null, UiComponentStrings
                .getString("LendingMainJPanel.border.loaninventory.text"), //$NON-NLS-1$
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
                        .getString("LendingMainJPanel.textfield.search.defaulttext")); //$NON-NLS-1$
        inventoryPanel.add(searchTextField);
        searchTextField.setColumns(10);

        onlyOverdueCheckbox = new JCheckBox(
                UiComponentStrings
                        .getString("LendingMainJPanel.checkbox.onlyoverdue.text")); //$NON-NLS-1$
        inventoryPanel.add(onlyOverdueCheckbox);

        horizontalStrut = Box.createHorizontalStrut(50);
        inventoryPanel.add(horizontalStrut);

        addLoanButton = new JButton(
                UiComponentStrings
                        .getString("LendingMainJPanel.button.addloan.text")); //$NON-NLS-1$
        inventoryPanel.add(addLoanButton);

        viewSelectedButton = new JButton(
                UiComponentStrings
                        .getString("LendingMainJPanel.button.viewselected.text")); //$NON-NLS-1$
        inventoryPanel.add(viewSelectedButton);
        viewSelectedButton
                .setToolTipText(UiComponentStrings
                        .getString("LendingMainJPanel.button.viewselected.disabled.tooltip")); //$NON-NLS-1$
        viewSelectedButton.setEnabled(false);
        viewSelectedButton.setMnemonic('v');

        centerPanel = new JPanel();
        loanInventoryPanel.add(centerPanel, BorderLayout.CENTER);

        centerPanel.setLayout(new BorderLayout(0, 0));

        lendingTable = new JTable();
        lendingTable.setRowSorter(tableSorter);
        lendingTable.setModel(new LoanTableModel(bookMasterlibrary.getLoans()));
        tableSorter = new TableRowSorter<>(
                (LoanTableModel) lendingTable.getModel());
        lendingTable.getTableHeader().setReorderingAllowed(false);
        lendingTable.setAutoCreateRowSorter(true);
        lendingTable.setCellSelectionEnabled(true);
        lendingTable.setFillsViewportHeight(true);
        lendingTable.setColumnSelectionAllowed(false);

        lendingTable.getSelectionModel().setSelectionMode(
                ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);

        JScrollPane jsp = new JScrollPane(lendingTable);
        centerPanel.add(jsp);
    }

    private void initHandlers() {
        viewSelectedButton.addActionListener(new ViewSelectedButtonListener());
        addLoanButton.addActionListener(new AddBookButtonListener());

        lendingTable.getSelectionModel().addListSelectionListener(
                new BookTableSelectionListener());

        ((LoanTableModel) lendingTable.getModel())
                .addTableModelChangeListener(new TableModelChangeListener() {
                    private Collection<BookDO> previouslySelectedBooks;

                    @Override
                    public void tableIsAboutToUpdate() {
                        previouslySelectedBooks = saveSelectedRows();
                    }

                    @Override
                    public void tableChanged() {
                        restoreSelectedRows(previouslySelectedBooks);
                    }
                });

        searchTextField.getDocument().addDocumentListener(
                new SearchFieldDocumentListener(searchTextField));
        onlyOverdueCheckbox.addItemListener(new ItemListener() {

            @Override
            public void itemStateChanged(ItemEvent anItemChangeEvent) {
                filterTable(searchTextField.getText());
            }
        });
    }

    /**
     * Saves the selected books in the table. The actual book instances are
     * saved since books can be added or removed so only saving the row index is
     * not enough.
     * 
     * @return set of currently selected books
     */
    private Set<BookDO> saveSelectedRows() {
        Set<BookDO> selectedBooks = new HashSet<>(
                lendingTable.getSelectedRowCount());
        for (int selectionIndex : lendingTable.getSelectedRows()) {
            BookDO singleSelectedBook = bookList.get(lendingTable
                    .convertRowIndexToModel(selectionIndex));
            selectedBooks.add(singleSelectedBook);
        }
        return selectedBooks;
    }

    /**
     * Reselect the given books in the table if they still exist.
     * 
     * @param someBooksToSelect
     *            the books to select
     */
    private void restoreSelectedRows(Collection<BookDO> someBooksToSelect) {
        for (BookDO tempBookToSelect : someBooksToSelect) {

            int indexInList = bookList.indexOf(tempBookToSelect);
            // do nothing if not found and books has been removed
            if (indexInList > -1) {
                int indexToSelectInView = lendingTable
                        .convertRowIndexToView(indexInList);
                lendingTable.getSelectionModel().addSelectionInterval(
                        indexToSelectInView, indexToSelectInView);
            }
        }
    }

    /**
     * Filters the booktable based on some rules. The search field
     */
    private void filterTable(final String aSearchText) {
        List<RowFilter<LoanTableModel, Object>> combiningRowFilterList = new ArrayList<>(
                2);
        combiningRowFilterList
                .add(new TextBookTableFilter<LoanTableModel, Object>(
                        aSearchText));
        if (onlyOverdueCheckbox.isSelected()) {
            RowFilter<LoanTableModel, Object> onlyAvailableFilter = new RowFilter<LoanTableModel, Object>() {
                @Override
                public boolean include(
                        javax.swing.RowFilter.Entry<? extends LoanTableModel, ? extends Object> anEntry) {
                    int copiesAvailable = ((Integer) anEntry
                            .getModel()
                            .getValueAt(
                                    ((Integer) anEntry.getIdentifier())
                                            .intValue(),
                                    0)).intValue();
                    return copiesAvailable > 0;
                }
            };
            // TODO folgendes verwenden, habe aber noch Probleme mit Generics
            // combiningRowFilterList.add(RowFilter.numberFilter(
            // RowFilter.ComparisonType.AFTER, Integer.valueOf(0), 0));
            combiningRowFilterList.add(onlyAvailableFilter);
        }
        tableSorter.setRowFilter(RowFilter.andFilter(combiningRowFilterList));
        lendingTable.setRowSorter(tableSorter);
    }

    @Override
    public void update(Observable anObservable, Object anArgument) {
        if (anObservable instanceof Library) {
            System.out.println("number of books" + anArgument); //$NON-NLS-1$
        }
        ((AbstractTableModel) lendingTable.getModel()).fireTableDataChanged();
        // numberOfBooks =
    }

    /**
     * Listener to open the selected book rows in {@code lendingTable} in a
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

            for (int tempBook : lendingTable.getSelectedRows()) {
                BookDO selectedBook = bookList.get(lendingTable
                        .convertRowIndexToModel(tempBook));
                loanDetailDialog.openBookTab(selectedBook, bookMasterlibrary);
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
            if (lendingTable.getSelectedRows().length > 0) {
                viewSelectedButton.setEnabled(true);
                UiComponentStrings
                        .getString("LendingMainJPanel.button.viewselected.enabled.tooltip"); //$NON-NLS-1$
            } else {
                viewSelectedButton.setEnabled(false);
                UiComponentStrings
                        .getString("LendingMainJPanel.button.viewselected.disabled.tooltip"); //$NON-NLS-1$
            }
        }
    }

    private class SearchFieldDocumentListener implements DocumentListener {
        private GhostHintJTextField searchField;

        public SearchFieldDocumentListener(GhostHintJTextField aSearchField) {
            searchField = aSearchField;
        }

        private void filter() {
            filterTable(searchField.getText());
        }

        @Override
        public void insertUpdate(DocumentEvent anInsertUpdateEvent) {
            filter();
        }

        @Override
        public void removeUpdate(DocumentEvent aRemoveUpdateEvent) {
            filter();
        }

        @Override
        public void changedUpdate(DocumentEvent aChangedUpdateEvent) {
            filter();
        }
    }
}
