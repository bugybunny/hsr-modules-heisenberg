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
import java.util.Observable;
import java.util.Observer;

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
import ch.hsr.modules.uint1.heisenberglibrary.model.BookDO;
import ch.hsr.modules.uint1.heisenberglibrary.model.IModelChangeType;
import ch.hsr.modules.uint1.heisenberglibrary.model.Library;
import ch.hsr.modules.uint1.heisenberglibrary.model.ModelChangeTypeEnums;
import ch.hsr.modules.uint1.heisenberglibrary.model.ObservableModelChangeEvent;
import ch.hsr.modules.uint1.heisenberglibrary.view.model.BookTableModel;

/**
 * 
 * @author msyfrig
 */
public class BookMainJPanel extends AbstractSearchableTableJPanel<BookDO>
        implements Observer {
    private static final long                           serialVersionUID = 8186612854405487707L;

    private TableFilter<BookTableModel>                 tableFilter;
    private OnlyAvailableFilter<BookTableModel, Object> onlyAvailableFilter;
    private JPanel                                      centerPanel;
    private JButton                                     viewSelectedButton;
    private JButton                                     addBookButton;
    private JPanel                                      inventoryStatisticsPanel;
    private JPanel                                      inventoryPanel;
    private JLabel                                      numberOfBooksLabel;
    private JLabel                                      numberOfCopiesLabel;
    private JCheckBox                                   onlyAvailableCheckbox;
    private Component                                   horizontalStrut;
    private JPanel                                      panel;
    private JPanel                                      bookInventoryPanel;
    private JPanel                                      outerStatisticsPanel;
    private BookDetailJDialog                           bookDetailDialog;

    private Library                                     library;

    // actions
    private AddBookAction                               addBookAction;
    private ViewSelectedAction                          viewSelectedAction;

    public BookMainJPanel(Library aLibrary) {
        super(aLibrary.getBooks());
        library = aLibrary;
        initComponents();
        initHandlers();
        addObserverForObservable(library, this);
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

        // initialize the searchfield for the table, add all handlers and add it
        // to the layout
        initSearchTextField(UiComponentStrings
                .getString("BookMainJPanel.textfield.search.defaulttext")); //$NON-NLS-1$
        inventoryPanel.add(searchTextField);

        onlyAvailableCheckbox = new JCheckBox(
                UiComponentStrings
                        .getString("BookMainJPanel.checkbox.onlyavailable.text")); //$NON-NLS-1$
        onlyAvailableCheckbox.setToolTipText(UiComponentStrings
                .getString("BookMainJPanel.checkbox.onlyavailable.tooltip")); //$NON-NLS-1$
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

        centerPanel = new JPanel();
        bookInventoryPanel.add(centerPanel, BorderLayout.CENTER);

        centerPanel.setLayout(new BorderLayout(0, 0));

        // init the table with the model with all properties and handlers and
        // add it the panel
        initTable(new BookTableModel(library));
        tableFilter = new TableFilter<>(table, searchTextField);
        centerPanel.add(tableScrollPane);
        table.setDefaultRenderer(Integer.class, new AvailableCopyCellRenderer());
    }

    private void initHandlers() {
        viewSelectedAction = new ViewSelectedAction(
                viewSelectedButton.getText());
        viewSelectedAction.setEnabled(false);
        viewSelectedButton.setAction(viewSelectedAction);
        // needs to be set after setAction, setAction seems to reset the set
        // mnemonic
        viewSelectedButton.setMnemonic('v');

        addBookAction = new AddBookAction(addBookButton.getText());
        addBookButton.setAction(addBookAction);
        // TODO googlen wieso zur Hölle das so ist
        addBookButton.setMnemonic('n');

        table.getSelectionModel().addListSelectionListener(
                new BookTableSelectionListener());

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
    }

    @Override
    public void update(Observable anObservable, Object anArgument) {
        if (anArgument instanceof ObservableModelChangeEvent) {
            ObservableModelChangeEvent modelChange = (ObservableModelChangeEvent) anArgument;
            IModelChangeType type = modelChange.getChangeType();
            if (type == ModelChangeTypeEnums.Book.ADDED
                    || type == ModelChangeTypeEnums.Book.REMOVED) {
                ((AbstractTableModel) table.getModel()).fireTableDataChanged();
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
    private class ViewSelectedAction extends AbstractAction {
        private static final long serialVersionUID = -342270462951723690L;

        private ViewSelectedAction(String anActionName) {
            super(anActionName);
        }

        /**
         * Opens all selected booksPanel and their detailview. If a dialog for a
         * detailview is already open and not on top of the z-order stack, it
         * will be placed on top and focused.
         */
        @Override
        public void actionPerformed(ActionEvent anActionEvent) {
            openSelected();
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
            createNew();
        }
    }

    private class BookTableSelectionListener implements ListSelectionListener {
        /**
         * Enables the edit button if at least one book is selected, else
         * disables it.
         */
        @Override
        public void valueChanged(ListSelectionEvent aSelectionEvent) {
            if (table.getSelectedRowCount() > 0) {
                viewSelectedAction.setEnabled(true);
            } else {
                viewSelectedAction.setEnabled(false);
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

    @Override
    public void setSelectedCheckBox() {
        onlyAvailableCheckbox.setSelected(!onlyAvailableCheckbox.isSelected());
    }

    @Override
    public void createNew() {
        if (addBookAction.isEnabled()) {
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

    @Override
    public void openSelected() {
        if (viewSelectedAction.isEnabled()) {
            if (bookDetailDialog == null) {
                bookDetailDialog = new BookDetailJDialog(null);
            }
            bookDetailDialog.setVisible(true);
            bookDetailDialog.toFront();

            for (int tempBook : table.getSelectedRows()) {
                BookDO selectedBook = dataList.get(table
                        .convertRowIndexToModel(tempBook));
                bookDetailDialog.openBookTab(selectedBook, library);
            }
        }
    }

    @Override
    public JButton getDefaultButton() {
        return viewSelectedButton;
    }

}
