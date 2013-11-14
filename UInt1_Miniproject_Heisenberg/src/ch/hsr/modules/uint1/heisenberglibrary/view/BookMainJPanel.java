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
import ch.hsr.modules.uint1.heisenberglibrary.model.ModelChangeType;
import ch.hsr.modules.uint1.heisenberglibrary.model.ModelChangeTypeEnums;
import ch.hsr.modules.uint1.heisenberglibrary.model.ObservableModelChangeEvent;
import ch.hsr.modules.uint1.heisenberglibrary.view.model.BookTableModel;

/**
 * 
 * @author msyfrig
 */
public class BookMainJPanel extends JPanel implements Observer {
    private static final long              serialVersionUID = 8186612854405487707L;

    /**
     * The table that displays all different booktypes in the library, not the
     * actual copies.
     */
    private JTable                         bookTable;
    private TableRowSorter<BookTableModel> tableSorter;
    private JPanel                         centerPanel;
    private JButton                        viewSelectedButton;
    private JButton                        addBookButton;
    private JPanel                         inventoryStatisticsPanel;
    private JPanel                         inventoryPanel;
    private JLabel                         numberOfBooksLabel;
    private JLabel                         numberOfCopiesLabel;
    private GhostHintJTextField            searchTextField;
    private JCheckBox                      onlyAvailableCheckbox;
    private Component                      horizontalStrut;
    private JPanel                         panel;
    private JPanel                         bookInventoryPanel;
    private JPanel                         outerStatisticsPanel;
    private BookDetailJDialog              bookDetailDialog;

    private List<BookDO>                   bookList;
    private Library                        bookMasterlibrary;

    public BookMainJPanel(Library library) {
        bookList = library.getBooks();
        bookMasterlibrary = library;
        initComponents();
        initHandlers();
        bookMasterlibrary.addObserver(this);
    }

    private void initComponents() {
        setBounds(100, 100, 1200, 441);
        setBorder(new EmptyBorder(5, 5, 5, 5));
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

        outerStatisticsPanel = new JPanel();
        outerStatisticsPanel.setBorder(new TitledBorder(new EtchedBorder(
                EtchedBorder.LOWERED, null, null), UiComponentStrings
                .getString("BookMainJPanel.border.inventory.text"), //$NON-NLS-1$
                TitledBorder.LEADING, TitledBorder.TOP, null, null));
        add(outerStatisticsPanel);
        outerStatisticsPanel.setLayout(new FlowLayout(FlowLayout.LEFT, 5, 5));

        inventoryStatisticsPanel = new JPanel();
        outerStatisticsPanel.add(inventoryStatisticsPanel);
        ((FlowLayout) inventoryStatisticsPanel.getLayout())
                .setAlignment(FlowLayout.LEFT);

        String numberOfBooksText = MessageFormat.format(UiComponentStrings
                .getString("BookMainJPanel.label.numberofbooks.text"), //$NON-NLS-1$
                Integer.valueOf(bookMasterlibrary.getBooks().size()));
        numberOfBooksLabel = new JLabel(numberOfBooksText);
        inventoryStatisticsPanel.add(numberOfBooksLabel);

        inventoryStatisticsPanel.add(Box.createHorizontalStrut(50));

        String numberOfCopiesText = MessageFormat.format(UiComponentStrings
                .getString("BookMainJPanel.label.copiesnumber.text"), //$NON-NLS-1$
                Integer.valueOf(bookMasterlibrary.getCopies().size()));
        numberOfCopiesLabel = new JLabel(numberOfCopiesText);
        inventoryStatisticsPanel.add(numberOfCopiesLabel);

        bookInventoryPanel = new JPanel();
        bookInventoryPanel.setBorder(new TitledBorder(null, UiComponentStrings
                .getString("BookMainJPanel.border.bookinventory.text"), //$NON-NLS-1$
                TitledBorder.LEADING, TitledBorder.TOP, null, null));
        add(bookInventoryPanel);
        bookInventoryPanel.setLayout(new BorderLayout(0, 0));

        JPanel topPane = new JPanel();
        bookInventoryPanel.add(topPane, BorderLayout.NORTH);
        topPane.setLayout(new BoxLayout(topPane, BoxLayout.Y_AXIS));

        panel = new JPanel();
        ((FlowLayout) panel.getLayout()).setAlignment(FlowLayout.LEFT);
        topPane.add(panel);

        inventoryPanel = new JPanel();
        topPane.add(inventoryPanel);
        inventoryPanel.setLayout(new FlowLayout(FlowLayout.LEFT, 5, 5));

        searchTextField = new GhostHintJTextField(
                UiComponentStrings
                        .getString("BookMainJPanel.textfield.search.defaulttext")); //$NON-NLS-1$
        inventoryPanel.add(searchTextField);
        searchTextField.setColumns(10);

        onlyAvailableCheckbox = new JCheckBox(
                UiComponentStrings
                        .getString("BookMainJPanel.checkbox.onlyavailable.text")); //$NON-NLS-1$
        inventoryPanel.add(onlyAvailableCheckbox);

        horizontalStrut = Box.createHorizontalStrut(50);
        inventoryPanel.add(horizontalStrut);

        addBookButton = new JButton(
                UiComponentStrings
                        .getString("BookMainJPanel.button.addbook.text")); //$NON-NLS-1$
        inventoryPanel.add(addBookButton);

        viewSelectedButton = new JButton(
                UiComponentStrings
                        .getString("BookMainJPanel.button.viewselected.text")); //$NON-NLS-1$
        inventoryPanel.add(viewSelectedButton);
        viewSelectedButton
                .setToolTipText(UiComponentStrings
                        .getString("BookMainJPanel.button.viewselected.disabled.tooltip")); //$NON-NLS-1$
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
        bookTable.setAutoCreateRowSorter(true);
        bookTable.setCellSelectionEnabled(true);
        bookTable.setFillsViewportHeight(true);
        bookTable.setColumnSelectionAllowed(false);

        bookTable.getSelectionModel().setSelectionMode(
                ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);

        JScrollPane jsp = new JScrollPane(bookTable);
        centerPanel.add(jsp);
    }

    private void initHandlers() {
        viewSelectedButton.addActionListener(new ViewSelectedButtonListener());
        addBookButton.addActionListener(new AddBookButtonListener());

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
        onlyAvailableCheckbox.addItemListener(new ItemListener() {

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
        if (onlyAvailableCheckbox.isSelected()) {
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
        bookTable.setRowSorter(tableSorter);
    }

    @Override
    public void update(Observable anObservable, Object anArgument) {
        if (anArgument instanceof ObservableModelChangeEvent) {
            ObservableModelChangeEvent modelChange = (ObservableModelChangeEvent) anArgument;
            ModelChangeType type = modelChange.getChangeType();
            if (type == ModelChangeTypeEnums.Book.ADDED
                    || type == ModelChangeTypeEnums.Book.REMOVED) {
                ((AbstractTableModel) bookTable.getModel())
                        .fireTableDataChanged();
            } else if (type == ModelChangeTypeEnums.Book.NUMBER) {
                numberOfBooksLabel
                        .setText(MessageFormat.format(
                                UiComponentStrings
                                        .getString("BookMainJPanel.label.numberofbooks.text"), //$NON-NLS-1$
                                modelChange.getNewValue()));
            } else if (type == ModelChangeTypeEnums.Copy.NUMBER) {
                numberOfCopiesLabel
                        .setText(MessageFormat.format(
                                UiComponentStrings
                                        .getString("BookMainJPanel.label.copiesnumber.text"), //$NON-NLS-1$
                                modelChange.getNewValue()));
            }
        }

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
                bookDetailDialog = new BookDetailJDialog(null);
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

    private class AddBookButtonListener implements ActionListener {
        /**
         * Opens an empty detail view with the ability to save a new book.
         */
        @Override
        public void actionPerformed(ActionEvent anActionEvent) {
            // TODO Opens a new window, should open a new tab instead

            // check first if the detaildialog is already opened, if so bring it
            // to the front
            if (bookDetailDialog == null) {
                bookDetailDialog = new BookDetailJDialog(null);
            }
            bookDetailDialog.setVisible(true);
            bookDetailDialog.toFront();

            bookDetailDialog.openBookTab(null, bookMasterlibrary);

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
                        .getString("BookMainJPanel.button.viewselected.enabled.tooltip"); //$NON-NLS-1$
            } else {
                viewSelectedButton.setEnabled(false);
                UiComponentStrings
                        .getString("BookMainJPanel.button.viewselected.disabled.tooltip"); //$NON-NLS-1$
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
