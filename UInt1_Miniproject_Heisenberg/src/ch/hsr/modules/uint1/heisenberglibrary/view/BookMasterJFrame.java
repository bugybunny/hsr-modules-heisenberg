package ch.hsr.modules.uint1.heisenberglibrary.view;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.text.MessageFormat;
import java.util.HashMap;
import java.util.Map;
import java.util.Observable;
import java.util.Observer;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.border.EmptyBorder;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.AbstractTableModel;

import ch.hsr.modules.uint1.heisenberglibrary.domain.book.BookDO;
import ch.hsr.modules.uint1.heisenberglibrary.model.BookTableModel;
import ch.hsr.modules.uint1.heisenberglibrary.model.Library;

public class BookMasterJFrame extends JFrame implements Observer {
    private static final long    serialVersionUID  = 8186612854405487707L;

    private JPanel               contentPanel;
    private JPanel               centerPanel;
    private Map<BookDO, JDialog> openBookDialogMap = new HashMap<>();
    private JButton              viewSelectedButton;
    private JButton              addBookButton;
    public JTable                bookTable;
    private JPanel               inventoryStatisticsPanel;
    private JPanel               inventoryPanel;
    private JTabbedPane          tabbedPane;
    private JPanel               booksPanel;
    private JLabel               numberOfBooksLabel;
    private JLabel               numberOfExemplarsLabel;
    private JTextField           searchField;
    private JCheckBox            onlyAvailableCheckboxMasterList;
    private Component            horizontalStrut;
    private JPanel               lendingPanel;
    private JPanel               panel;
    private JLabel               lblNewLabel;
    private JPanel               bookInventoryPanel;
    private JPanel               outerStatisticsPanel;
    // TODO wahrscheinlich rauslöschen, oder wieso wird das gebraucht? muss
    // nicht global sein
    private int                  numberOfBooks     = 0;

    /**
     * Create the frame.
     */
    public BookMasterJFrame() {
        setTitle("BookMaster"); //$NON-NLS-1$
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 1200, 441);
        contentPanel = new JPanel();
        contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
        setContentPane(contentPanel);
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.X_AXIS));

        tabbedPane = new JTabbedPane(JTabbedPane.TOP);
        contentPanel.add(tabbedPane);

        booksPanel = new JPanel();
        tabbedPane.addTab("Books", null, booksPanel, null);
        booksPanel.setLayout(new BoxLayout(booksPanel, BoxLayout.Y_AXIS));

        outerStatisticsPanel = new JPanel();
        outerStatisticsPanel.setBorder(new TitledBorder(new EtchedBorder(
                EtchedBorder.LOWERED, null, null),
                "Inventory outerStatisticsPanel", TitledBorder.LEADING,
                TitledBorder.TOP, null, null));
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
        bookInventoryPanel.setBorder(new TitledBorder(null, "Book Inventory",
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

        searchField = new JTextField();
        inventoryPanel.add(searchField);
        searchField.setText(UiComponentStrings
                .getString("BookMasterJFrame.textfield.search.defaulttext")); //$NON-NLS-1$
        searchField.setColumns(10);

        onlyAvailableCheckboxMasterList = new JCheckBox(
                UiComponentStrings
                        .getString("BookMasterJFrame.checkbox.onlyavailable.text")); //$NON-NLS-1$
        inventoryPanel.add(onlyAvailableCheckboxMasterList);

        horizontalStrut = Box.createHorizontalStrut(50);
        inventoryPanel.add(horizontalStrut);

        addBookButton = new JButton(
                UiComponentStrings
                        .getString("BookMasterJFrame.button.addbook.label")); //$NON-NLS-1$
        inventoryPanel.add(addBookButton);

        viewSelectedButton = new JButton(
                UiComponentStrings
                        .getString("BookMasterJFrame.button.viewselected.label")); //$NON-NLS-1$
        inventoryPanel.add(viewSelectedButton);
        viewSelectedButton
                .setToolTipText(UiComponentStrings
                        .getString("BookMasterJFrame.button.viewselected.disabled.tooltip")); //$NON-NLS-1$
        viewSelectedButton.addActionListener(new ViewSelectedButtonListener());
        viewSelectedButton.setEnabled(false);
        viewSelectedButton.setMnemonic('v');

        centerPanel = new JPanel();
        bookInventoryPanel.add(centerPanel, BorderLayout.CENTER);

        centerPanel.setLayout(new BorderLayout(0, 0));

        bookTable = new JTable();
        bookTable.setCellSelectionEnabled(true);
        bookTable.setFillsViewportHeight(true);
        bookTable.getSelectionModel().addListSelectionListener(
                new BookTableSelectionListener());
        bookTable.getSelectionModel().setSelectionMode(
                ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);

        JScrollPane jsp = new JScrollPane(bookTable);
        centerPanel.add(jsp);

        lendingPanel = new JPanel();
        tabbedPane.addTab("Lending", null, lendingPanel, null);

        // TODO: Wahrscheinlich schlecht, wegen update schauen.

        System.out.println("BLUBB" + numberOfBooks);

        // numberOfBooksJLable.setText(UiComponentStrings.getString("BookMasterJFrame.numberOfBooksJLable.text")+numberOfBooks);

    }

    @Override
    public void update(Observable anObservable, Object anArgument) {
        if (anObservable instanceof Library) {
            System.out.println("number of books: " + anArgument);
        }
        ((AbstractTableModel) bookTable.getModel()).fireTableDataChanged();
        // numberOfBooks =
        // TODO updatet erst bei erneuter Selektion
    }

    private class ViewSelectedButtonListener implements ActionListener {
        /**
         * Opens all selected booksPanel and their detailview. If a dialog for a
         * detailview is already open and not on top of the z-order stack, it
         * will be placed on top and focused.
         */
        @Override
        public void actionPerformed(ActionEvent anActionEvent) {
            for (int tempBook : bookTable.getSelectedRows()) {
                final BookDO selectedBook = ((BookTableModel) bookTable
                        .getModel()).getBookByRowNumber(bookTable
                        .convertRowIndexToModel(tempBook));
                // check first if the dialog for this dialog is already open if
                // so, bring it to the top and focus it
                JDialog bookDetailDialog = openBookDialogMap.get(selectedBook);
                if (bookDetailDialog != null) {
                    bookDetailDialog.toFront();
                } else {
                    bookDetailDialog = new BookDetailJDialog(
                            BookMasterJFrame.this, selectedBook);
                    bookDetailDialog.addWindowListener(new WindowAdapter() {
                        // remove the dialog from the list of opened
                        // detaildialogs
                        @Override
                        public void windowClosed(WindowEvent aWindowEvent) {
                            openBookDialogMap.remove(selectedBook);
                        }
                    });
                    ;
                    bookDetailDialog.setVisible(true);

                    openBookDialogMap.put(selectedBook, bookDetailDialog);
                }
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
}