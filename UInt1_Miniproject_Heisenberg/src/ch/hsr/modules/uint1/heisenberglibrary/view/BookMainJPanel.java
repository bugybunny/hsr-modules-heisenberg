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
public class BookMainJPanel extends AbstractSearchableTableJPanel implements
        Observer {
    private static final long                           serialVersionUID = 8186612854405487707L;

    /**
     * The table that displays all different booktypes in the library, not the
     * actual copies.
     */
    private JTable                                      bookTable;
    private TableFilter<BookTableModel>                 tableFilter;
    private OnlyAvailableFilter<BookTableModel, Object> onlyAvailableFilter;
    private JPanel                                      centerPanel;
    private JButton                                     viewSelectedButton;
    private JButton                                     addBookButton;
    private JPanel                                      inventoryStatisticsPanel;
    private JPanel                                      inventoryPanel;
    private JLabel                                      numberOfBooksLabel;
    private JLabel                                      numberOfCopiesLabel;
    private GhostHintJTextField                         searchTextField;
    private JCheckBox                                   onlyAvailableCheckbox;
    private Component                                   horizontalStrut;
    private JPanel                                      panel;
    private JPanel                                      bookInventoryPanel;
    private JPanel                                      outerStatisticsPanel;
    private BookDetailJDialog                           bookDetailDialog;

    private List<BookDO>                                bookList;
    private Library                                     library;

    public BookMainJPanel(Library aLibrary) {
        bookList = aLibrary.getBooks();
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
                Integer.valueOf(library.getBooks().size()));
        numberOfBooksLabel = new JLabel(numberOfBooksText);
        inventoryStatisticsPanel.add(numberOfBooksLabel);

        inventoryStatisticsPanel.add(Box.createHorizontalStrut(50));

        String numberOfCopiesText = MessageFormat.format(UiComponentStrings
                .getString("BookMainJPanel.label.copiesnumber.text"), //$NON-NLS-1$
                Integer.valueOf(library.getCopies().size()));
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

        String addBookButtonText = UiComponentStrings
                .getString("BookMainJPanel.button.addbook.text");//$NON-NLS-1$
        String addBookButtonEnabledTooltip = UiComponentStrings
                .getString("BookMainJPanel.button.addbook.enabled.tooltip"); //$NON-NLS-1$
        String addBookButtonDisabledTooltip = UiComponentStrings
                .getString("BookMainJPanel.button.addbook.disabled.tooltip"); //$NON-NLS-1$
        addBookButton = new ToolTipJButton(addBookButtonText,
                addBookButtonEnabledTooltip, addBookButtonDisabledTooltip);
        inventoryPanel.add(addBookButton);

        String viewSelectedButtonText = UiComponentStrings
                .getString("BookMainJPanel.button.viewselected.text"); //$NON-NLS-1$
        String viewSelectedButtonEnabledToolTip = UiComponentStrings
                .getString("BookMainJPanel.button.viewselected.enabled.tooltip"); //$NON-NLS-1$
        String viewSelectedButtonDisabledToolTip = UiComponentStrings
                .getString("BookMainJPanel.button.viewselected.disabled.tooltip"); //$NON-NLS-1$

        viewSelectedButton = new ToolTipJButton(viewSelectedButtonText,
                viewSelectedButtonEnabledToolTip,
                viewSelectedButtonDisabledToolTip);

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
        bookTable.setModel(new BookTableModel(library));
        tableFilter = new TableFilter<>(bookTable, searchTextField);
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
        AddBookAction addBookAction = new AddBookAction(addBookButton.getText());
        addBookButton.setAction(addBookAction);
        // needs to be set after setAction, setAction seems to reset the set
        // mnemonic
        // TODO googlen wieso zur Hölle das so ist
        addBookButton.setMnemonic('n');

        // TODO sollte in LibraryMasterJFrame sein, damit Keystrokes auf der
        // JTabbedPane auch noch erkannt werden, der Keystroke müsste irgendwie
        // an den aktuell selektierten Tab delegiert werden
        // ctrl+n: add book for Mac OS x users since they don't have mnemonics
        KeyStroke ctrlN = KeyStroke.getKeyStroke(KeyEvent.VK_N,
                InputEvent.CTRL_DOWN_MASK);
        getInputMap(JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT).put(ctrlN,
                addBookAction.getValue(Action.NAME));
        getActionMap().put(addBookAction.getValue(Action.NAME), addBookAction);

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

        onlyAvailableFilter = new OnlyAvailableFilter<>();
        onlyAvailableCheckbox.addItemListener(new ItemListener() {

            @Override
            public void itemStateChanged(ItemEvent anItemStateChangedEvent) {
                if (anItemStateChangedEvent.getStateChange() == ItemEvent.SELECTED) {
                    tableFilter.addFilter(onlyAvailableFilter);
                } else {
                    tableFilter.removeFilter(onlyAvailableFilter);
                }
                tableFilter.filterTable();
            }
        });
        ColumnsAutoSizer.sizeColumnsToFit(bookTable, 5);
    }

    @Override
    public GhostHintJTextField getSearchTextField() {
        return searchTextField;
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
                bookDetailDialog.openBookTab(selectedBook, library);
            }
        }
    }

    private class AddBookAction extends AbstractAction {
        private static final long serialVersionUID = -8717212722696829048L;

        private AddBookAction(String anActionName) {
            super(anActionName);
        }

        /**
         * Opens an empty detail view with the ability to save a new book.
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

            bookDetailDialog.openBookTab(null, library);
        }
    }

    private class BookTableSelectionListener implements ListSelectionListener {
        /**
         * Enables the edit button if at least one book is selected, else
         * disables it.
         */
        @Override
        public void valueChanged(ListSelectionEvent aSelectionEvent) {
            if (bookTable.getSelectedRowCount() > 0) {
                viewSelectedButton.setEnabled(true);
            } else {
                viewSelectedButton.setEnabled(false);
            }
        }
    }

    // TODO folgendes verwenden, habe aber noch Probleme mit Generics
    // combiningRowFilterList.add(RowFilter.numberFilter(
    // RowFilter.ComparisonType.AFTER, Integer.valueOf(0), 0));
    private class OnlyAvailableFilter<M extends BookTableModel, I extends Object>
            extends RowFilter<M, I> {
        @Override
        public boolean include(
                javax.swing.RowFilter.Entry<? extends M, ? extends I> anEntry) {
            int copiesAvailable = ((Integer) anEntry.getModel().getValueAt(
                    ((Integer) anEntry.getIdentifier()).intValue(), 0))
                    .intValue();
            return copiesAvailable > 0;
        }
    }
}
