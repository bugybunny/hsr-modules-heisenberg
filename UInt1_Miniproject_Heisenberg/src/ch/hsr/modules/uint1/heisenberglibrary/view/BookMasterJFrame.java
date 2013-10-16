package ch.hsr.modules.uint1.heisenberglibrary.view;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.Map;
import java.util.Observable;
import java.util.Observer;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ScrollPaneConstants;
import javax.swing.border.EmptyBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import ch.hsr.modules.uint1.heisenberglibrary.domain.book.BookDO;

public class BookMasterJFrame extends JFrame implements Observer {
    private static final long    serialVersionUID  = 8186612854405487707L;

    private JPanel               contentPane;
    private JPanel               centerPane;
    public JList<BookDO>         bookJList;
    private Map<BookDO, JDialog> openBookDialogMap = new HashMap<>();
    private JButton              editButtonMasterList;

    /**
     * Create the frame.
     */
    public BookMasterJFrame() {
        setTitle("BookMaster"); //$NON-NLS-1$
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 1200, 300);
        contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        contentPane.setLayout(new BorderLayout(0, 0));
        setContentPane(contentPane);

        JPanel topPane = new JPanel();
        contentPane.add(topPane, BorderLayout.NORTH);
        topPane.setLayout(new BoxLayout(topPane, BoxLayout.X_AXIS));

        JPanel bottomPane = new JPanel();
        contentPane.add(bottomPane, BorderLayout.SOUTH);

        editButtonMasterList = new JButton(
                UiComponentStrings
                        .getString("BookMasterJFrame.button.edit.label")); //$NON-NLS-1$
        editButtonMasterList.setToolTipText(UiComponentStrings
                .getString("BookMasterJFrame.button.edit.disabled.tooltip")); //$NON-NLS-1$
        editButtonMasterList.addActionListener(new EditButtonListener());
        editButtonMasterList.setEnabled(false);
        bottomPane.add(editButtonMasterList);
        editButtonMasterList.setMnemonic('e');

        centerPane = new JPanel();
        contentPane.add(centerPane, BorderLayout.CENTER);

        bookJList = new JList<>();
        bookJList.addListSelectionListener(new BookListSelectionListener());

        JScrollPane jsp = new JScrollPane(bookJList,
                ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED,
                ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        centerPane.add(jsp);
    }

    @Override
    public void update(Observable o, Object arg) {
        // TODO updatet erst bei erneuter Selektion
        bookJList.repaint();
    }

    private class EditButtonListener implements ActionListener {
        /**
         * Opens all selected books and their detailview. If a dialog for a
         * detailview is already open and not on top of the z-order stack, it
         * will be placed on top and focused.
         */
        @Override
        public void actionPerformed(ActionEvent aE) {
            for (BookDO tempBook : bookJList.getSelectedValuesList()) {
                System.out.println(tempBook.getTitle());
                // check first if the dialog for this dialog is already open
                // if so, bring it to the top and focus it
                JDialog bookDetailDialog = openBookDialogMap.get(tempBook);
                if (bookDetailDialog != null) {
                    openBookDialogMap.get(tempBook).toFront();
                } else {
                    bookDetailDialog = new BookDetailJDialog(
                            BookMasterJFrame.this, tempBook);
                    bookDetailDialog.setVisible(true);
                    openBookDialogMap.put(tempBook, bookDetailDialog);
                }
            }
        }
    }

    private class BookListSelectionListener implements ListSelectionListener {
        /**
         * Enables the edit button if at least one book is selected, else
         * disables it.
         */
        @Override
        public void valueChanged(ListSelectionEvent aSelectionEvent) {
            if (bookJList.getSelectedIndices().length > 0) {
                editButtonMasterList.setEnabled(true);
                UiComponentStrings
                        .getString("BookMasterJFrame.button.edit.enabled.tooltip");
            } else {
                editButtonMasterList.setEnabled(false);
                UiComponentStrings
                        .getString("BookMasterJFrame.button.edit.disabled.tooltip");
            } 
        }
    }
}
