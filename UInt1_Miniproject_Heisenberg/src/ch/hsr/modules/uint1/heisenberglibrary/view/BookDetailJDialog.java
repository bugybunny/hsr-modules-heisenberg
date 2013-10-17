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

    public BookDetailJDialog(JFrame anOwner, BookDO aBookDo) {
        super(anOwner, aBookDo.getTitle());
        setBookDO(aBookDo);
    }

    /**
     * Sets the model for this dialog and adds observer to it or removes the
     * observer if a new book is set (which should never happen).
     * 
     * @param book
     */
    private void setBookDO(BookDO aNewBookDo) {
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
                .getBorder("TitledBorder.border"),//$NON-NLS-1$
                UiComponentStrings
                        .getString("BookDetailJDialog.border.title.label"), //$NON-NLS-1$
                TitledBorder.LEADING, TitledBorder.TOP, null, null));
        contentPane.add(northPanel, BorderLayout.CENTER);
        GridBagLayout gblPanel = new GridBagLayout();
        gblPanel.columnWidths = new int[] { 0, 0, 0, 0, 0 };
        gblPanel.rowHeights = new int[] { 30, 0, 0, 0, 0, 0, 0 };
        gblPanel.columnWeights = new double[] { 0.0, 0.0, 0.0, 1.0,
                Double.MIN_VALUE };
        gblPanel.rowWeights = new double[] { 0.0, 0.0, 0.0, 0.0, 0.0, 0.0,
                Double.MIN_VALUE };
        northPanel.setLayout(gblPanel);

        JLabel titleLabel = new JLabel(
                UiComponentStrings
                        .getString("BookDetailJDialog.label.title.label")); //$NON-NLS-1$
        GridBagConstraints gbcTitleLabel = new GridBagConstraints();
        gbcTitleLabel.insets = new Insets(0, 0, 5, 5);
        gbcTitleLabel.gridx = 1;
        gbcTitleLabel.gridy = 1;
        northPanel.add(titleLabel, gbcTitleLabel);

        titleTextfield = new JTextField();
        GridBagConstraints gbcTitleTextfield = new GridBagConstraints();
        gbcTitleTextfield.fill = GridBagConstraints.HORIZONTAL;
        gbcTitleTextfield.insets = new Insets(0, 0, 5, 0);
        gbcTitleTextfield.gridx = 3;
        gbcTitleTextfield.gridy = 1;
        northPanel.add(titleTextfield, gbcTitleTextfield);
        titleTextfield.setColumns(10);

        authorLabel = new JLabel(
                UiComponentStrings
                        .getString("BookDetailJDialog.label.author.label")); //$NON-NLS-1$
        GridBagConstraints gbcAuthorLabel = new GridBagConstraints();
        gbcAuthorLabel.anchor = GridBagConstraints.NORTH;
        gbcAuthorLabel.insets = new Insets(0, 0, 5, 5);
        gbcAuthorLabel.gridx = 1;
        gbcAuthorLabel.gridy = 2;
        northPanel.add(authorLabel, gbcAuthorLabel);

        authorTextfield = new JTextField();
        GridBagConstraints gbcAuthorTextfield = new GridBagConstraints();
        gbcAuthorTextfield.insets = new Insets(0, 0, 5, 0);
        gbcAuthorTextfield.fill = GridBagConstraints.HORIZONTAL;
        gbcAuthorTextfield.gridx = 3;
        gbcAuthorTextfield.gridy = 2;
        northPanel.add(authorTextfield, gbcAuthorTextfield);
        authorTextfield.setColumns(10);

        publisherLabel = new JLabel(
                UiComponentStrings
                        .getString("BookDetailJDialog.label.publisher.label")); //$NON-NLS-1$
        GridBagConstraints gbcPublisherLabel = new GridBagConstraints();
        gbcPublisherLabel.insets = new Insets(0, 0, 5, 5);
        gbcPublisherLabel.gridx = 1;
        gbcPublisherLabel.gridy = 3;
        northPanel.add(publisherLabel, gbcPublisherLabel);

        publisherTextfield = new JTextField();
        GridBagConstraints gbcPublisherTextfield = new GridBagConstraints();
        gbcPublisherTextfield.insets = new Insets(0, 0, 5, 0);
        gbcPublisherTextfield.fill = GridBagConstraints.HORIZONTAL;
        gbcPublisherTextfield.gridx = 3;
        gbcPublisherTextfield.gridy = 3;
        northPanel.add(publisherTextfield, gbcPublisherTextfield);
        publisherTextfield.setColumns(10);

        conditionLabel = new JLabel(
                UiComponentStrings
                        .getString("BookDetailJDialog.label.condition.label")); //$NON-NLS-1$
        GridBagConstraints gbcConditionLabel = new GridBagConstraints();
        gbcConditionLabel.insets = new Insets(0, 0, 0, 5);
        gbcConditionLabel.gridx = 1;
        gbcConditionLabel.gridy = 4;
        northPanel.add(conditionLabel, gbcConditionLabel);

        comboBox = new JComboBox<>();
        for (Copy.Condition tempBookCondition : Copy.Condition.values()) {
            comboBox.addItem(tempBookCondition);
        }
        GridBagConstraints gbcComboBox = new GridBagConstraints();
        gbcComboBox.fill = GridBagConstraints.HORIZONTAL;
        gbcComboBox.gridx = 3;
        gbcComboBox.gridy = 4;
        northPanel.add(comboBox, gbcComboBox);

        JPanel southPanel = new JPanel();
        southPanel.setBorder(new TitledBorder(null, UiComponentStrings
                .getString("BookDetailJDialog.border.inventory.label"), //$NON-NLS-1$
                TitledBorder.LEADING, TitledBorder.TOP, null, null));
        contentPane.add(southPanel, BorderLayout.SOUTH);
        southPanel.setLayout(new BoxLayout(southPanel, BoxLayout.X_AXIS));

        labelCopies = new JLabel("Total Copies: 8"); //$NON-NLS-1$
        labelCopies.setAlignmentX(Component.RIGHT_ALIGNMENT);
        southPanel.add(labelCopies);

        rigidArea = Box.createRigidArea(new Dimension(20, 20));
        southPanel.add(rigidArea);

        labelAvailable = new JLabel("Total available: 2"); //$NON-NLS-1$
        southPanel.add(labelAvailable);

        glue = Box.createGlue();
        southPanel.add(glue);

        buttonAddCopy = new JButton(
                UiComponentStrings
                        .getString("BookDetailJDialog.button.addcopy.text")); //$NON-NLS-1$
        buttonAddCopy.setAlignmentX(Component.RIGHT_ALIGNMENT);
        southPanel.add(buttonAddCopy);
    }

    /*
     * (non-Javadoc)
     * 
     * @see ch.hsr.modules.uint1.heisenberglibrary.view.AbstractDefaultJDialog#
     * initHandlers()
     */
    @Override
    protected void initHandlers() {

    }

    @Override
    public void update(Observable o, Object arg) {
        updateDisplay();
    }

    private void updateDisplay() {
        // this case should never happen, that the book is null but it still
        // prevents us from a npe
        if (displayedBookDO == null) {
            titleTextfield.setText(UiComponentStrings.getString("empty")); //$NON-NLS-1$
            titleTextfield.setEnabled(true);
            authorTextfield.setText(UiComponentStrings.getString("empty")); //$NON-NLS-1$
            authorLabel.setEnabled(true);
            publisherTextfield.setText(UiComponentStrings.getString("empty")); //$NON-NLS-1$
            publisherLabel.setEnabled(true);

        } else {
            titleTextfield.setText(displayedBookDO.getTitle());
            titleTextfield.setEnabled(true);
            titleTextfield.setCaretPosition(0);
            authorTextfield.setText(displayedBookDO.getAuthor());
            authorTextfield.setEnabled(true);
            publisherTextfield.setText(displayedBookDO.getPublisher());
            publisherTextfield.setEnabled(true);
        }
    }

    /**
     * Saves the content from all fields to the bookDO.
     */
    @Override
    public boolean save() {
        displayedBookDO.set(titleTextfield.getText(),
                authorTextfield.getText(), publisherTextfield.getText(),
                displayedBookDO.getShelf());
        // TODO getShelf anpassen sobald implementiert im GUI
        return true;
    }
}
