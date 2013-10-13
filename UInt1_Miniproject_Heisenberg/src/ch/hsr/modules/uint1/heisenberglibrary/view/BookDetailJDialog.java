package ch.hsr.modules.uint1.heisenberglibrary.view;


import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.EventQueue;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.Observable;
import java.util.Observer;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import ch.hsr.modules.uint1.heisenberglibrary.domain.book.Book;



public class BookDetailJDialog extends JDialog implements Observer{
	
	private Book editBook;

	private JButton okbutton;
	private JTextField isbn;
	private JTextField title;
	private JTextField author;
	private JTextField publisher;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					BookDetailJDialog frame = new BookDetailJDialog(new Book("test"));
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 * @param book 
	 */
	
	public void setEditBook (Book newEditBook) {
		if (newEditBook != editBook) {
			if (editBook!=null) {
				editBook.deleteObserver(this);
			}
			editBook=newEditBook;
			if (editBook!=null) {
				editBook.addObserver(this);
			}
			updateDisplay();
		}
	}
	
	public BookDetailJDialog(Book book) {
		this();
		setEditBook(book);
	}

	public BookDetailJDialog() {
		// TODO Auto-generated constructor stub
		setBounds(100, 100, 446, 287);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		getContentPane().setLayout(new BorderLayout(0, 0));
		
		JPanel bottomPane = new JPanel();
		getContentPane().add(bottomPane, BorderLayout.SOUTH);
		bottomPane.setLayout(new FlowLayout(FlowLayout.LEFT, 5, 5));
		
		JLabel lblNewLabel = new JLabel("Total Copies: 8            ");
		bottomPane.add(lblNewLabel);
		
		JLabel lblNewLabel_1 = new JLabel("Total available: 2");
		bottomPane.add(lblNewLabel_1);
		
		Component horizontalStrut = Box.createHorizontalStrut(60);
		bottomPane.add(horizontalStrut);
		
		okbutton = new JButton("Add a copy");
		bottomPane.add(okbutton);
		
		Component glue = Box.createGlue();
		bottomPane.add(glue);
		
		JPanel mainPane = new JPanel();
		getContentPane().add(mainPane, BorderLayout.CENTER);
		mainPane.setLayout(new BoxLayout(mainPane, BoxLayout.Y_AXIS));
		
		JPanel panel_3 = new JPanel();
		panel_3.setAlignmentX(Component.RIGHT_ALIGNMENT);
		mainPane.add(panel_3);
		GridBagLayout gbl_panel_3 = new GridBagLayout();
		gbl_panel_3.columnWidths = new int[]{17, 0, 266, 0, 0};
		gbl_panel_3.rowHeights = new int[]{19, 0, 0, 0, 0, 64, 0};
		gbl_panel_3.columnWeights = new double[]{0.0, 0.0, 1.0, 0.0, Double.MIN_VALUE};
		gbl_panel_3.rowWeights = new double[]{0.0, 0.0, 0.0, 0.0, 0.0, 1.0, Double.MIN_VALUE};
		panel_3.setLayout(gbl_panel_3);
		
		JLabel lblNewLabel_3 = new JLabel("ISBN:");
		GridBagConstraints gbc_lblNewLabel_3 = new GridBagConstraints();
		gbc_lblNewLabel_3.anchor = GridBagConstraints.WEST;
		gbc_lblNewLabel_3.insets = new Insets(0, 0, 5, 5);
		gbc_lblNewLabel_3.gridx = 1;
		gbc_lblNewLabel_3.gridy = 1;
		panel_3.add(lblNewLabel_3, gbc_lblNewLabel_3);
		
		isbn = new JTextField();
		GridBagConstraints gbc_isbn = new GridBagConstraints();
		gbc_isbn.insets = new Insets(0, 0, 5, 5);
		gbc_isbn.fill = GridBagConstraints.HORIZONTAL;
		gbc_isbn.gridx = 2;
		gbc_isbn.gridy = 1;
		panel_3.add(isbn, gbc_isbn);
		isbn.setColumns(10);
		
		JLabel lblTitle = new JLabel("Title:");
		GridBagConstraints gbc_lblTitle = new GridBagConstraints();
		gbc_lblTitle.anchor = GridBagConstraints.WEST;
		gbc_lblTitle.insets = new Insets(0, 0, 5, 5);
		gbc_lblTitle.gridx = 1;
		gbc_lblTitle.gridy = 2;
		panel_3.add(lblTitle, gbc_lblTitle);
		
		
		title = new JTextField();
		title.setColumns(10);
		GridBagConstraints gbc_title = new GridBagConstraints();
		gbc_title.anchor = GridBagConstraints.NORTH;
		gbc_title.insets = new Insets(0, 0, 5, 5);
		gbc_title.fill = GridBagConstraints.HORIZONTAL;
		gbc_title.gridx = 2;
		gbc_title.gridy = 2;
		panel_3.add(title, gbc_title);
		title.addKeyListener(new MyDocumentListener());
		
		JLabel lblAuthor = new JLabel("Author:");
		GridBagConstraints gbc_lblAuthor = new GridBagConstraints();
		gbc_lblAuthor.anchor = GridBagConstraints.WEST;
		gbc_lblAuthor.insets = new Insets(0, 0, 5, 5);
		gbc_lblAuthor.gridx = 1;
		gbc_lblAuthor.gridy = 3;
		panel_3.add(lblAuthor, gbc_lblAuthor);
		
		author = new JTextField();
		author.setColumns(10);
		GridBagConstraints gbc_author = new GridBagConstraints();
		gbc_author.anchor = GridBagConstraints.NORTH;
		gbc_author.insets = new Insets(0, 0, 5, 5);
		gbc_author.fill = GridBagConstraints.HORIZONTAL;
		gbc_author.gridx = 2;
		gbc_author.gridy = 3;
		panel_3.add(author, gbc_author);
		
		JLabel lblPublisher = new JLabel("Publisher:");
		GridBagConstraints gbc_lblPublisher = new GridBagConstraints();
		gbc_lblPublisher.anchor = GridBagConstraints.WEST;
		gbc_lblPublisher.insets = new Insets(0, 0, 5, 5);
		gbc_lblPublisher.gridx = 1;
		gbc_lblPublisher.gridy = 4;
		panel_3.add(lblPublisher, gbc_lblPublisher);
		
		publisher = new JTextField();
		publisher.setColumns(10);
		GridBagConstraints gbc_publisher = new GridBagConstraints();
		gbc_publisher.anchor = GridBagConstraints.NORTH;
		gbc_publisher.insets = new Insets(0, 0, 5, 5);
		gbc_publisher.fill = GridBagConstraints.HORIZONTAL;
		gbc_publisher.gridx = 2;
		gbc_publisher.gridy = 4;
		panel_3.add(publisher, gbc_publisher);
		
		JLabel lblCondition = new JLabel("Condition:");
		GridBagConstraints gbc_lblCondition = new GridBagConstraints();
		gbc_lblCondition.anchor = GridBagConstraints.NORTH;
		gbc_lblCondition.insets = new Insets(0, 0, 0, 5);
		gbc_lblCondition.gridx = 1;
		gbc_lblCondition.gridy = 5;
		panel_3.add(lblCondition, gbc_lblCondition);
		
		JComboBox condition = new JComboBox();
		GridBagConstraints gbc_condition = new GridBagConstraints();
		gbc_condition.insets = new Insets(0, 0, 0, 5);
		gbc_condition.anchor = GridBagConstraints.NORTH;
		gbc_condition.fill = GridBagConstraints.HORIZONTAL;
		gbc_condition.gridx = 2;
		gbc_condition.gridy = 5;
		panel_3.add(condition, gbc_condition);
		pack();
	}

	/**
	 * Initialize the contents of the frame.
	 */

	@Override
	public void update(Observable o, Object arg) {
		updateDisplay();
		
	}

	private void updateDisplay() {
		// TODO Auto-generated method stub
		/*
		private JFrame frame;
		private JButton okbutton;
		private JTextField isbn;
		private JTextField title;
		private JTextField author;
		private JTextField publisher;
		*/
		if (editBook==null){
			title.setText("");
			title.setEnabled(false);
			isbn.setText("");
			isbn.setEnabled(false);
			author.setText("");
			author.setEnabled(false);
			publisher.setText("");
			publisher.setEnabled(false);
			okbutton.setSelected(false);
			okbutton.setEnabled(false);
		}else{
			String newText = editBook.getTitle();
			if (!title.getText().equals(newText)) {
				title.setText(editBook.getTitle());
			}
			title.setEnabled(true);
			author.setEnabled(true);
			author.setText(editBook.getAuthor());
			
			isbn.setEnabled(true);	
			isbn.setText("dd");
			
			publisher.setEnabled(true);
			publisher.setText(editBook.getPublisher());
			
		}
		
	}
	
	class MyDocumentListener extends KeyAdapter {

		@Override
		public void keyReleased(KeyEvent e) {
			// TODO Auto-generated method stub
			int cp = title.getCaretPosition();
			if (editBook != null){
				editBook.setTitle(title.getText());
				title.setCaretPosition(cp); // needed to survive update
			}
		}

		}

}
