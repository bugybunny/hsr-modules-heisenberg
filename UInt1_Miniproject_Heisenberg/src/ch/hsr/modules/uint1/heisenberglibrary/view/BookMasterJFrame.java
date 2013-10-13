package ch.hsr.modules.uint1.heisenberglibrary.view;

import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.util.Observable;
import java.util.Observer;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JList;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class BookMasterJFrame extends JFrame implements Observer {

	private JPanel contentPane;
	private JPanel centerPane;
	public JList bookJList;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					BookMasterJFrame frame = new BookMasterJFrame();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

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
			public void actionPerformed(ActionEvent e) {
				BookDetailJDialog frame = new BookDetailJDialog();
			}
		});
		bottomPane.add(editButtonMasterList);
		
		centerPane = new JPanel();
		contentPane.add(centerPane, BorderLayout.CENTER);
		
		bookJList = new JList();
		centerPane.add(bookJList);
		
		
		/*
        String[] data = { "one", "two", "three", "four" };
        final JList dataList = new JList(data);

        dataList.addListSelectionListener(new ListSelectionListener() {

            @Override
            public void valueChanged(ListSelectionEvent arg0) {
                if (!arg0.getValueIsAdjusting()) {
                  label.setText(dataList.getSelectedValue().toString());
                }
            }
        });
        add(dataList);
        add(label);
        
        */
	}
	
	

	@Override
	public void update(Observable o, Object arg) {
		// TODO Auto-generated method stub
		bookJList.repaint();
		
	}

}
