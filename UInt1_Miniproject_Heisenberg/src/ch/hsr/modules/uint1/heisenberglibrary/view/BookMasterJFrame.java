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
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
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
import ch.hsr.modules.uint1.heisenberglibrary.view.model.BookTableModel;

public class BookMasterJFrame extends JFrame implements Observer {
    private static final long              serialVersionUID = 8186612854405487707L;

    /**
     * The table that displays all different booktypes in the library, not the
     * actual copies.
     */
    private JTable                         bookTable;
    private TableRowSorter<BookTableModel> tableSorter;
    private JPanel                         contentPanel;
    private JPanel                         centerPanel;
    private JButton                        viewSelectedButton;
    private JButton                        addBookButton;
    private JPanel                         inventoryStatisticsPanel;
    private JPanel                         inventoryPanel;
    private JTabbedPane                    tabbedPane;
    private JPanel                         booksPanel;
    private JLabel                         numberOfBooksLabel;
    private JLabel                         numberOfExemplarsLabel;
    private GhostHintJTextField            searchTextField;
    private JCheckBox                      onlyAvailableCheckboxMasterList;
    private Component                      horizontalStrut;
    private JPanel                         lendingPanel;
    private JPanel                         panel;
    private JLabel                         lblNewLabel;
    private JPanel                         bookInventoryPanel;
    private JPanel                         outerStatisticsPanel;
    private BookDetailJDialog              bookDetailDialog;
    // TODO wahrscheinlich rauslöschen, oder wieso wird das gebraucht? muss
    // nicht global sein
    private int                            numberOfBooks    = 0;
    private List<BookDO>                   bookList;
    private Library                        bookMasterlibrary;

    /**
     * Creates the frame.
     * 
     * @param aBooks
     */
    public BookMasterJFrame(Library library) {
        bookList = library.getBooks();
        bookMasterlibrary = library;

        initComponents();
        initHandlers();
    }

    private void initComponents() {
        ImageIcon frameIcon = new ImageIcon(
                BookMasterJFrame.class.getResource("/images/library.png"));
        setIconImage(frameIcon.getImage());
        setTitle(UiComponentStrings.getString("BookMasterJFrame.title")); //$NON-NLS-1$
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 1200, 441);
        contentPanel = new JPanel();
        contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
        setContentPane(contentPanel);
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.X_AXIS));

        tabbedPane = new JTabbedPane(JTabbedPane.TOP);
        contentPanel.add(tabbedPane);

        booksPanel = new JPanel();
        tabbedPane
                .addTab(UiComponentStrings
                        .getString("BookMasterJFrame.tab.books.text"), null, booksPanel, null); //$NON-NLS-1$
        booksPanel.setLayout(new BoxLayout(booksPanel, BoxLayout.Y_AXIS));

        outerStatisticsPanel = new JPanel();
        outerStatisticsPanel.setBorder(new TitledBorder(new EtchedBorder(
                EtchedBorder.LOWERED, null, null), UiComponentStrings
                .getString("BookMasterJFrame.border.inventory.text"), //$NON-NLS-1$
                TitledBorder.LEADING, TitledBorder.TOP, null, null));
        booksPanel.add(outerStatisticsPanel);
        outerStatisticsPanel.setLayout(new FlowLayout(FlowLayout.LEFT, 5, 5));

        inventoryStatisticsPanel = new JPanel();
        outerStatisticsPanel.add(inventoryStatisticsPanel);
        ((FlowLayout) inventoryStatisticsPanel.getLayout())
                .setAlignment(FlowLayout.LEFT);

        String numberOfBooksText = MessageFormat.format(UiComponentStrings
                .getString("BookMasterJFrame.label.numberofbooks.text"), //$NON-NLS-1$
                Integer.valueOf(numberOfBooks));
        numberOfBooksLabel = new JLabel(numberOfBooksText);
        inventoryStatisticsPanel.add(numberOfBooksLabel);

        inventoryStatisticsPanel.add(Box.createHorizontalStrut(50));

        // TODO numberof exemplars auslesen aus library
        String numberOfExamblesText = MessageFormat.format(UiComponentStrings
                .getString("BookMasterJFrame.label.exemplarnumber.text"), //$NON-NLS-1$
                Integer.valueOf(0));
        numberOfExemplarsLabel = new JLabel(numberOfExamblesText);
        inventoryStatisticsPanel.add(numberOfExemplarsLabel);

        bookInventoryPanel = new JPanel();
        bookInventoryPanel.setBorder(new TitledBorder(null, UiComponentStrings
                .getString("BookMasterJFrame.border.bookinventory.text"), //$NON-NLS-1$
                TitledBorder.LEADING, TitledBorder.TOP, null, null));
        booksPanel.add(bookInventoryPanel);
        bookInventoryPanel.setLayout(new BorderLayout(0, 0));

        JPanel topPane = new JPanel();
        bookInventoryPanel.add(topPane, BorderLayout.NORTH);
        topPane.setLayout(new BoxLayout(topPane, BoxLayout.Y_AXIS));

        panel = new JPanel();
        ((FlowLayout) panel.getLayout()).setAlignment(FlowLayout.LEFT);
        topPane.add(panel);

        lblNewLabel = new JLabel(
                UiComponentStrings
                        .getString("BookMasterJFrame.lblNewLabel.text")); //$NON-NLS-1$
        panel.add(lblNewLabel);

        inventoryPanel = new JPanel();
        topPane.add(inventoryPanel);
        inventoryPanel.setLayout(new FlowLayout(FlowLayout.LEFT, 5, 5));

        searchTextField = new GhostHintJTextField(
                UiComponentStrings
                        .getString("BookMasterJFrame.textfield.search.defaulttext")); //$NON-NLS-1$
        inventoryPanel.add(searchTextField);
        searchTextField.setColumns(10);

        onlyAvailableCheckboxMasterList = new JCheckBox(
                UiComponentStrings
                        .getString("BookMasterJFrame.checkbox.onlyavailable.text")); //$NON-NLS-1$
        inventoryPanel.add(onlyAvailableCheckboxMasterList);

        horizontalStrut = Box.createHorizontalStrut(50);
        inventoryPanel.add(horizontalStrut);

        addBookButton = new JButton(
                UiComponentStrings
                        .getString("BookMasterJFrame.button.addbook.text")); //$NON-NLS-1$
        inventoryPanel.add(addBookButton);

        viewSelectedButton = new JButton(
                UiComponentStrings
                        .getString("BookMasterJFrame.button.viewselected.text")); //$NON-NLS-1$
        inventoryPanel.add(viewSelectedButton);
        viewSelectedButton
                .setToolTipText(UiComponentStrings
                        .getString("BookMasterJFrame.button.viewselected.disabled.tooltip")); //$NON-NLS-1$
        viewSelectedButton.setEnabled(false);
        viewSelectedButton.setMnemonic('v');

        centerPanel = new JPanel();
        bookInventoryPanel.add(centerPanel, BorderLayout.CENTER);

        centerPanel.setLayout(new BorderLayout(0, 0));

        bookTable = new JTable();
        bookTable.setRowSorter(tableSorter);
        bookTable.setModel(new BookTableModel(bookList));
        tableSorter = new TableRowSorter<>(
                (BookTableModel) bookTable.getModel());
        bookTable.getTableHeader().setReorderingAllowed(false);
        // bookTable.setAutoCreateRowSorter(true);
        bookTable.setCellSelectionEnabled(true);
        bookTable.setFillsViewportHeight(true);
        bookTable.setColumnSelectionAllowed(false);

        //
        // TableRowSorter<BookTableModel> rowSorter = new TableRowSorter<>(
        // (BookTableModel) bookTable.getModel());
        // bookTable.setRowSorter(rowSorter);

        bookTable.getSelectionModel().setSelectionMode(
                ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);

        JScrollPane jsp = new JScrollPane(bookTable);
        centerPanel.add(jsp);

        lendingPanel = new JPanel();
        tabbedPane
                .addTab(UiComponentStrings
                        .getString("BookMasterJFrame.tab.lending.text"), null, lendingPanel, null); //$NON-NLS-1$

        // TODO: Wahrscheinlich schlecht, wegen update schauen.
        System.out.println("BLUBB" + numberOfBooks); //$NON-NLS-1$

        // numberOfBooksJLable.setText(UiComponentStrings.getString("BookMasterJFrame.numberOfBooksJLable.text")+numberOfBooks);
    }

    private void initHandlers() {
        viewSelectedButton.addActionListener(new ViewSelectedButtonListener());
        bookTable.getSelectionModel().addListSelectionListener(
                new BookTableSelectionListener());

        ((BookTableModel) bookTable.getModel())
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
        onlyAvailableCheckboxMasterList.addItemListener(new ItemListener() {

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
                bookTable.getSelectedRowCount());
        for (int selectionIndex : bookTable.getSelectedRows()) {
            BookDO singleSelectedBook = bookList.get(bookTable
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
                int indexToSelectInView = bookTable
                        .convertRowIndexToView(indexInList);
                bookTable.getSelectionModel().addSelectionInterval(
                        indexToSelectInView, indexToSelectInView);
            }
        }
    }

    /**
     * Filters the booktable based on some rules. The search field
     */
    private void filterTable(final String aSearchText) {
        List<RowFilter<BookTableModel, Object>> combiningRowFilterList = new ArrayList<>(
                2);
        combiningRowFilterList
                .add(new TextBookTableFilter<BookTableModel, Object>(
                        aSearchText));
        if (onlyAvailableCheckboxMasterList.isSelected()) {
            RowFilter<BookTableModel, Object> onlyAvailableFilter = new RowFilter<BookTableModel, Object>() {
                @Override
                public boolean include(
                        javax.swing.RowFilter.Entry<? extends BookTableModel, ? extends Object> anEntry) {
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
        // TODO Stolze fragen wieso zur Hölle ich das noch machen
        // muss?!?!<?!?!?!?! DAS HAT MICH FUCKING MEHRERE STUNDEN GEKOSTET
        bookTable.setRowSorter(tableSorter);
    }

    @Override
    public void update(Observable anObservable, Object anArgument) {
        if (anObservable instanceof Library) {
            System.out.println("number of books" + anArgument); //$NON-NLS-1$
        }
        ((AbstractTableModel) bookTable.getModel()).fireTableDataChanged();
        // numberOfBooks =
    }

    /**
     * Listener to open the selected book rows in {@code bookTable} in a
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
            if (bookDetailDialog == null) {
                bookDetailDialog = new BookDetailJDialog(BookMasterJFrame.this);
            }
            bookDetailDialog.setVisible(true);
            bookDetailDialog.toFront();

            for (int tempBook : bookTable.getSelectedRows()) {
                BookDO selectedBook = bookList.get(bookTable
                        .convertRowIndexToModel(tempBook));
                bookDetailDialog.openBookTab(selectedBook, bookMasterlibrary);
            }
        }
    }

    private class BookTableSelectionListener implements ListSelectionListener {
        /**
         * Enables the edit button if at least one book is selected, else
         * disables it.
         */
        @Override
        public void valueChanged(ListSelectionEvent aSelectionEvent) {
            if (bookTable.getSelectedRows().length > 0) {
                viewSelectedButton.setEnabled(true);
                UiComponentStrings
                        .getString("BookMasterJFrame.button.viewselected.enabled.tooltip"); //$NON-NLS-1$
            } else {
                viewSelectedButton.setEnabled(false);
                UiComponentStrings
                        .getString("BookMasterJFrame.button.viewselected.disabled.tooltip"); //$NON-NLS-1$
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
