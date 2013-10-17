package ch.hsr.modules.uint1.heisenberglibrary.view;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
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
import javax.swing.border.EmptyBorder;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;
import javax.swing.table.JTableHeader;

import ch.hsr.modules.uint1.heisenberglibrary.domain.BookTableModel;
import ch.hsr.modules.uint1.heisenberglibrary.domain.book.BookDO;

public class BookMasterJFrame extends JFrame implements Observer {
    private static final long    serialVersionUID  = 8186612854405487707L;

    private JPanel               contentPane;
    private JPanel               centerPane;
    private Map<BookDO, JDialog> openBookDialogMap = new HashMap<>();
    private JButton              editButtonMasterList;
    public JTable                table;
    private JPanel               statistics;
    private JPanel               inventory;
    private JTabbedPane          tabbedPane;
    private JPanel               Books;
    private JButton              selectButtonMasterList;
    private JLabel               numberOfBooksLable;
    private JLabel               numberOfExemplarsLable;
    private JTextField           searchFieldBooksMasterList;
    private JCheckBox            onlyAvailableCheckboxMasterList;
    private Component            horizontalStrut;
    private JPanel               Lending;
    private Component            horizontalStrut_1;
    private JPanel               panel;
    private JLabel               lblNewLabel;
    private JPanel BookInventory;
    private JPanel Statistics;

    /**
     * Create the frame.
     */
    public BookMasterJFrame() {
        setTitle("BookMaster"); //$NON-NLS-1$
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 1200, 300);
        contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        setContentPane(contentPane);
        contentPane.setLayout(new BoxLayout(contentPane, BoxLayout.X_AXIS));

        tabbedPane = new JTabbedPane(JTabbedPane.TOP);
        contentPane.add(tabbedPane);

        Books = new JPanel();
        tabbedPane.addTab("Books", null, Books, null);
        Books.setLayout(new BoxLayout(Books, BoxLayout.Y_AXIS));
        
        Statistics = new JPanel();
        Statistics.setBorder(new TitledBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null), "Inventory Statistics", TitledBorder.LEADING, TitledBorder.TOP, null, null));
        Books.add(Statistics);
                Statistics.setLayout(new FlowLayout(FlowLayout.LEFT, 5, 5));
        
                statistics = new JPanel();
                Statistics.add(statistics);
                FlowLayout flowLayout_1 = (FlowLayout) statistics.getLayout();
                flowLayout_1.setAlignment(FlowLayout.LEFT);
                
                        numberOfBooksLable = new JLabel(
                                UiComponentStrings.getString("BookMasterJFrame.lblDd.text")); //$NON-NLS-1$
                        statistics.add(numberOfBooksLable);
                        
                                horizontalStrut_1 = Box.createHorizontalStrut(50);
                                statistics.add(horizontalStrut_1);
                                
                                        numberOfExemplarsLable = new JLabel(
                                                UiComponentStrings
                                                        .getString("BookMasterJFrame.lblAnzahlExemplare.text")); //$NON-NLS-1$
                                        statistics.add(numberOfExemplarsLable);
        
        BookInventory = new JPanel();
        BookInventory.setBorder(new TitledBorder(null, "Book Inventory", TitledBorder.LEADING, TitledBorder.TOP, null, null));
        Books.add(BookInventory);
        BookInventory.setLayout(new BorderLayout(0, 0));
                
                        JPanel topPane = new JPanel();
                        BookInventory.add(topPane, BorderLayout.NORTH);
                        topPane.setLayout(new BoxLayout(topPane, BoxLayout.Y_AXIS));
                        
                                panel = new JPanel();
                                FlowLayout flowLayout_2 = (FlowLayout) panel.getLayout();
                                flowLayout_2.setAlignment(FlowLayout.LEFT);
                                topPane.add(panel);
                                
                                        lblNewLabel = new JLabel(
                                                UiComponentStrings
                                                        .getString("BookMasterJFrame.lblNewLabel.text")); //$NON-NLS-1$
                                        panel.add(lblNewLabel);
                                        
                                                inventory = new JPanel();
                                                topPane.add(inventory);
                                                inventory.setLayout(new FlowLayout(FlowLayout.LEFT, 5, 5));
                                                
                                                        searchFieldBooksMasterList = new JTextField();
                                                        inventory.add(searchFieldBooksMasterList);
                                                        searchFieldBooksMasterList.setText(UiComponentStrings
                                                                .getString("BookMasterJFrame.textField.text_1"));
                                                        searchFieldBooksMasterList.setColumns(10);
                                                        
                                                                onlyAvailableCheckboxMasterList = new JCheckBox(
                                                                        UiComponentStrings
                                                                                .getString("BookMasterJFrame.chckbxNewCheckBox.text"));
                                                                inventory.add(onlyAvailableCheckboxMasterList);
                                                                
                                                                        horizontalStrut = Box.createHorizontalStrut(50);
                                                                        inventory.add(horizontalStrut);
                                                                        
                                                                                selectButtonMasterList = new JButton(
                                                                                        UiComponentStrings
                                                                                                .getString("BookMasterJFrame.btnNewButton.text")); //$NON-NLS-1$
                                                                                inventory.add(selectButtonMasterList);
                                                                                
                                                                                        editButtonMasterList = new JButton(
                                                                                                UiComponentStrings
                                                                                                        .getString("BookMasterJFrame.button.edit.label"));
                                                                                        inventory.add(editButtonMasterList);
                                                                                        editButtonMasterList.setToolTipText(UiComponentStrings
                                                                                                .getString("BookMasterJFrame.button.edit.disabled.tooltip")); //$NON-NLS-1$
                                                                                        editButtonMasterList.addActionListener(new EditButtonListener());
                                                                                        editButtonMasterList.setEnabled(false);
                                                                                        editButtonMasterList.setMnemonic('e');
        
                centerPane = new JPanel();
                BookInventory.add(centerPane, BorderLayout.CENTER);
                
                        centerPane.setLayout(new BorderLayout(0, 0));
                        
                                table = new JTable();
                                JTableHeader header = table.getTableHeader();
                                table.setCellSelectionEnabled(true);
                                table.setFillsViewportHeight(true);
                                
                                        JScrollPane jsp = new JScrollPane(table);
                                        centerPane.add(jsp);

        Lending = new JPanel();
        tabbedPane.addTab("Lending", null, Lending, null);

        // table.setAutoResizeMode(1);

    }

    @Override
    public void update(Observable o, Object arg) {
        // TODO updatet erst bei erneuter Selektion
        // bookJList.repaint();
    }

    private class EditButtonListener implements ActionListener {
        /**
         * Opens all selected books and their detailview. If a dialog for a
         * detailview is already open and not on top of the z-order stack, it
         * will be placed on top and focused.
         */
        @Override
        public void actionPerformed(ActionEvent aE) {
            for (int tempBook : table.getSelectedRows()) {
                BookDO selectedBook = ((BookTableModel) table.getModel())
                        .getBookByRowNumber(table
                                .convertRowIndexToModel(tempBook));
                // check first if the dialog for this dialog is already open if
                // so, bring it to the top and focus it
                JDialog bookDetailDialog = openBookDialogMap.get(tempBook);
                if (bookDetailDialog != null) {
                    openBookDialogMap.get(tempBook).toFront();
                } else {
                    bookDetailDialog = new BookDetailJDialog(
                            BookMasterJFrame.this, selectedBook);
                    bookDetailDialog.setVisible(true);
                    openBookDialogMap.put(selectedBook, bookDetailDialog);
                }
            }

        }
    }
}
