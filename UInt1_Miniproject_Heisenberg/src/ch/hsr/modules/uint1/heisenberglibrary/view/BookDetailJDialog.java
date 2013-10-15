package ch.hsr.modules.uint1.heisenberglibrary.view;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.util.Observable;
import java.util.Observer;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.UIManager;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;

import ch.hsr.modules.uint1.heisenberglibrary.domain.Copy;
import ch.hsr.modules.uint1.heisenberglibrary.domain.book.BookDO;

public class BookDetailJDialog extends AbstractDefaultJDialog implements
        Observer {

    private BookDO                    displayedBookDO;

    private static final long         serialVersionUID = 5145001889718027524L;
    private JPanel                    contentPane;
    private JTextField                isbnTextfield;
    private JTextField                titleTextfield;
    private JLabel                    authorLabel;
    private JTextField                authorTextfield;
    private JLabel                    publisherLabel;
    private JTextField                publisherTextfield;
    private JLabel                    conditionLabel;
    private JLabel                    labelCopies;
    private JLabel                    labelAvailable;
    private JButton                   buttonAddCopy;
    private JComboBox<Copy.Condition> comboBox;
    private Component                 glue;
    private Component                 rigidArea;

    /**
     * Create the application.
     * 
     * @param book
     */

    public void setBookDO(BookDO aNewBookDo) {
        // do nothing if same book is set
        if (aNewBookDo != displayedBookDO) {
            // delete observers for this old, not anymore displayed book
            if (displayedBookDO != null) {
                displayedBookDO.deleteObserver(this);
            }
            displayedBookDO = aNewBookDo;
            // add observers for new book object
            if (displayedBookDO != null) {
                displayedBookDO.addObserver(this);
            }
            updateDisplay();
        }
    }

    public BookDetailJDialog(JFrame anOwner, BookDO aBookDo) {
        super(anOwner, aBookDo.getTitle());
        setBookDO(aBookDo);
    }

    /*
     * (non-Javadoc)
     * 
     * @see ch.hsr.modules.uint1.heisenberglibrary.view.AbstractDefaultJDialog#
     * initComponents()
     */
    @Override
    protected void initComponents() {
        setBounds(100, 100, 482, 358);
        contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        setContentPane(contentPane);
        contentPane.setLayout(new BorderLayout());

        JPanel northPanel = new JPanel();
        northPanel.setBorder(new TitledBorder(UIManager
                .getBorder("TitledBorder.border"), "Title Information",
                TitledBorder.LEADING, TitledBorder.TOP, null, null));
        contentPane.add(northPanel, BorderLayout.CENTER);
        GridBagLayout gbl_panel = new GridBagLayout();
        gbl_panel.columnWidths = new int[] { 0, 0, 0, 0, 0 };
        gbl_panel.rowHeights = new int[] { 30, 0, 0, 0, 0, 0, 0 };
        gbl_panel.columnWeights = new double[] { 0.0, 0.0, 0.0, 1.0,
                Double.MIN_VALUE };
        gbl_panel.rowWeights = new double[] { 0.0, 0.0, 0.0, 0.0, 0.0, 0.0,
                Double.MIN_VALUE };
        northPanel.setLayout(gbl_panel);

        JLabel isbnLabel = new JLabel("ISBN:");
        GridBagConstraints gbc_isbnLabel = new GridBagConstraints();
        gbc_isbnLabel.insets = new Insets(0, 0, 5, 5);
        gbc_isbnLabel.gridx = 1;
        gbc_isbnLabel.gridy = 1;
        northPanel.add(isbnLabel, gbc_isbnLabel);

        isbnTextfield = new JTextField();
        GridBagConstraints gbc_isbnTextfield = new GridBagConstraints();
        gbc_isbnTextfield.insets = new Insets(0, 0, 5, 0);
        gbc_isbnTextfield.fill = GridBagConstraints.HORIZONTAL;
        gbc_isbnTextfield.gridx = 3;
        gbc_isbnTextfield.gridy = 1;
        northPanel.add(isbnTextfield, gbc_isbnTextfield);
        isbnTextfield.setColumns(10);

        JLabel titleLabel = new JLabel("Title:");
        GridBagConstraints gbc_titleLabel = new GridBagConstraints();
        gbc_titleLabel.insets = new Insets(0, 0, 5, 5);
        gbc_titleLabel.gridx = 1;
        gbc_titleLabel.gridy = 2;
        northPanel.add(titleLabel, gbc_titleLabel);

        titleTextfield = new JTextField();
        GridBagConstraints gbc_titleTextfield = new GridBagConstraints();
        gbc_titleTextfield.fill = GridBagConstraints.HORIZONTAL;
        gbc_titleTextfield.insets = new Insets(0, 0, 5, 0);
        gbc_titleTextfield.gridx = 3;
        gbc_titleTextfield.gridy = 2;
        northPanel.add(titleTextfield, gbc_titleTextfield);
        titleTextfield.setColumns(10);

        authorLabel = new JLabel("Author:");
        GridBagConstraints gbc_authorLabel = new GridBagConstraints();
        gbc_authorLabel.anchor = GridBagConstraints.NORTH;
        gbc_authorLabel.insets = new Insets(0, 0, 5, 5);
        gbc_authorLabel.gridx = 1;
        gbc_authorLabel.gridy = 3;
        northPanel.add(authorLabel, gbc_authorLabel);

        authorTextfield = new JTextField();
        GridBagConstraints gbc_authorTextfield = new GridBagConstraints();
        gbc_authorTextfield.insets = new Insets(0, 0, 5, 0);
        gbc_authorTextfield.fill = GridBagConstraints.HORIZONTAL;
        gbc_authorTextfield.gridx = 3;
        gbc_authorTextfield.gridy = 3;
        northPanel.add(authorTextfield, gbc_authorTextfield);
        authorTextfield.setColumns(10);

        publisherLabel = new JLabel("Publisher:");
        GridBagConstraints gbc_publisherLabel = new GridBagConstraints();
        gbc_publisherLabel.insets = new Insets(0, 0, 5, 5);
        gbc_publisherLabel.gridx = 1;
        gbc_publisherLabel.gridy = 4;
        northPanel.add(publisherLabel, gbc_publisherLabel);

        publisherTextfield = new JTextField();
        GridBagConstraints gbc_publisherTextfield = new GridBagConstraints();
        gbc_publisherTextfield.insets = new Insets(0, 0, 5, 0);
        gbc_publisherTextfield.fill = GridBagConstraints.HORIZONTAL;
        gbc_publisherTextfield.gridx = 3;
        gbc_publisherTextfield.gridy = 4;
        northPanel.add(publisherTextfield, gbc_publisherTextfield);
        publisherTextfield.setColumns(10);

        conditionLabel = new JLabel("Condition:");
        GridBagConstraints gbc_conditionLabel = new GridBagConstraints();
        gbc_conditionLabel.insets = new Insets(0, 0, 0, 5);
        gbc_conditionLabel.gridx = 1;
        gbc_conditionLabel.gridy = 5;
        northPanel.add(conditionLabel, gbc_conditionLabel);

        comboBox = new JComboBox<>();
        for (Copy.Condition tempBookCondition : Copy.Condition.values()) {
            comboBox.addItem(tempBookCondition);
        }
        GridBagConstraints gbc_comboBox = new GridBagConstraints();
        gbc_comboBox.fill = GridBagConstraints.HORIZONTAL;
        gbc_comboBox.gridx = 3;
        gbc_comboBox.gridy = 5;
        northPanel.add(comboBox, gbc_comboBox);

        JPanel southPanel = new JPanel();
        southPanel.setBorder(new TitledBorder(null, "Inventory Information",
                TitledBorder.LEADING, TitledBorder.TOP, null, null));
        contentPane.add(southPanel, BorderLayout.SOUTH);
        southPanel.setLayout(new BoxLayout(southPanel, BoxLayout.X_AXIS));

        labelCopies = new JLabel("Total Copies: 8");
        labelCopies.setAlignmentX(Component.RIGHT_ALIGNMENT);
        southPanel.add(labelCopies);

        rigidArea = Box.createRigidArea(new Dimension(20, 20));
        southPanel.add(rigidArea);

        labelAvailable = new JLabel("Total available: 2");
        southPanel.add(labelAvailable);

        glue = Box.createGlue();
        southPanel.add(glue);

        buttonAddCopy = new JButton("Add a copy");
        buttonAddCopy.setAlignmentX(Component.RIGHT_ALIGNMENT);
        southPanel.add(buttonAddCopy);
    }

    @Override
    public void update(Observable o, Object arg) {
        updateDisplay();
    }

    private void updateDisplay() {
        if (displayedBookDO == null) {
            titleTextfield.setText("");
            titleTextfield.setEnabled(true);
            authorTextfield.setText("");
            authorLabel.setEnabled(true);
            publisherTextfield.setText("");
            publisherLabel.setEnabled(true);

        } else {
            titleTextfield.setText(displayedBookDO.getTitle());
            titleTextfield.setEnabled(true);
            authorTextfield.setText(displayedBookDO.getAuthor());
            authorTextfield.setEnabled(false);
            publisherTextfield.setText(displayedBookDO.getPublisher());
            publisherTextfield.setEnabled(false);
        }
    }

    /**
     * Saves the content from all fields to the bookDO.
     */
    // TODO Herr Stolze fragen ob wir eigentlich immer updaten müssen wenn ein
    // Key gedrückt würde, das Buch wird ja sowieso nur in einem Dialog angzeigt
    // und es verursacht unnnötige Updates
    // TODO mit Theo abklären
    @Override
    protected boolean save() {
        displayedBookDO.set(titleTextfield.getText(),
                authorTextfield.getText(), publisherTextfield.getText(),
                displayedBookDO.getShelf());
        return true;
    }
}
