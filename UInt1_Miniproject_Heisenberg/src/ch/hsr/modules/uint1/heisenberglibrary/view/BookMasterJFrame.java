package ch.hsr.modules.uint1.heisenberglibrary.view;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Observable;
import java.util.Observer;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import ch.hsr.modules.uint1.heisenberglibrary.domain.book.BookDO;

public class BookMasterJFrame extends JFrame implements Observer {
    private static final long serialVersionUID = 8186612854405487707L;

    private JPanel            contentPane;
    private JPanel            centerPane;
    public JList<BookDO>      bookJList;

    /**
     * Create the frame.
     */
    public BookMasterJFrame() {
        setTitle("BookMaster");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 450, 300);
        contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        contentPane.setLayout(new BorderLayout(0, 0));
        setContentPane(contentPane);

        JPanel topPane = new JPanel();
        contentPane.add(topPane, BorderLayout.NORTH);

        JPanel bottomPane = new JPanel();
        contentPane.add(bottomPane, BorderLayout.SOUTH);

        JButton editButtonMasterList = new JButton("Edit");
        editButtonMasterList.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                BookDetailJDialog frame = new BookDetailJDialog(bookJList
                        .getSelectedValue());
                frame.setVisible(true);
            }
        });
        bottomPane.add(editButtonMasterList);

        centerPane = new JPanel();
        contentPane.add(centerPane, BorderLayout.CENTER);

        bookJList = new JList<>();
        centerPane.add(bookJList);

        /*
         * String[] data = { "one", "two", "three", "four" }; final JList
         * dataList = new JList(data);
         * 
         * dataList.addListSelectionListener(new ListSelectionListener() {
         * 
         * @Override public void valueChanged(ListSelectionEvent arg0) { if
         * (!arg0.getValueIsAdjusting()) {
         * label.setText(dataList.getSelectedValue().toString()); } } });
         * add(dataList); add(label);
         */
    }

    @Override
    public void update(Observable o, Object arg) {
        // TODO Auto-generated method stub
        bookJList.repaint();
    }
}
