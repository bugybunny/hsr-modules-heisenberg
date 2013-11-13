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
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.util.Map;
import java.util.Observable;
import java.util.Observer;

import javax.swing.Action;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.KeyStroke;
import javax.swing.UIManager;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import ch.hsr.modules.uint1.heisenberglibrary.controller.ModelStateChangeEvent;
import ch.hsr.modules.uint1.heisenberglibrary.controller.ModelStateChangeListener;
import ch.hsr.modules.uint1.heisenberglibrary.model.BookDO;
import ch.hsr.modules.uint1.heisenberglibrary.model.Library;
import ch.hsr.modules.uint1.heisenberglibrary.model.Shelf;
import ch.hsr.modules.uint1.heisenberglibrary.view.model.BookExemplarModel;

/**
 * This class shows a single {@link BookDO} with all its properties and
 * existings exemplars.
 * 
 * <br> If the underlying model (the displayed {@link BookDO} changes, it is
 * updated via observer pattern.
 * 
 * <br>The {@link #titleTextfield} requests the focus as soon as this component
 * is shown, means when the tab gets selected.
 * 
 * @author msyfrig
 */
public class BookDetailJPanel extends JPanel implements Observer {
    private static final long serialVersionUID = 3353323227207467624L;

    /**
     * The displayed book that can be edited and is observed via observer
     * pattern.
     */
    private BookDO            displayedBookDO;

    private Library           detailLibrary;

    /**
     * Flag to indicate if there are unsaved changes in this panel.
     */
    private boolean           dirty;

    // components
    private JTextField        titleTextfield;

    private JTextField        authorTextfield;
    private JTextField        publisherTextfield;
    private JComboBox<Shelf>  comboShelf;
    private Component         glue;
    private Component         rigidArea;
    private JTable            bookExemplarTable;
    private JButton           btnAddABook;

    /**
     * Creates a new instance of this class and sets model.
     * 
     * @param aBookDo
     */
    public BookDetailJPanel(BookDO aBookDo, Library library) {
        detailLibrary = library;
        initEverything(aBookDo);
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

    /**
     * Initializes everything :p
     * 
     * @param aBookDo
     *            the model to set
     */
    private void initEverything(BookDO aBookDo) {
        initComponents();
        setBookDO(aBookDo);
        // TODO: PROBLEM BLUBB PROBLEM NOOOoooooooooooooooooooooooooooooooooo!
        bookExemplarTable.setModel(new BookExemplarModel(displayedBookDO,
                detailLibrary));
        initHandlers();
    }

    /**
     * Initializes and adds all components to the panel.
     */
    private void initComponents() {
        setBorder(new EmptyBorder(5, 5, 5, 5));
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

        JPanel northPanel = new JPanel();
        northPanel.setBorder(new TitledBorder(UIManager
                .getBorder("TitledBorder.border"),//$NON-NLS-1$
                UiComponentStrings
                        .getString("BookDetailJDialog.border.title.text"), //$NON-NLS-1$
                TitledBorder.LEADING, TitledBorder.TOP, null, null));
        add(northPanel);
        GridBagLayout gblPanel = new GridBagLayout();
        gblPanel.columnWidths = new int[] { 0, 0, 0, 0, 0 };
        gblPanel.rowHeights = new int[] { 30, 0, 0, 0, 0, 0, 0, 0 };
        gblPanel.columnWeights = new double[] { 0.0, 0.0, 0.0, 1.0,
                Double.MIN_VALUE };
        gblPanel.rowWeights = new double[] { 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0,
                Double.MIN_VALUE };
        northPanel.setLayout(gblPanel);

        JLabel titleLabel = new JLabel(
                UiComponentStrings
                        .getString("BookDetailJDialog.label.title.text")); //$NON-NLS-1$
        GridBagConstraints gbcTitleLabel = new GridBagConstraints();
        gbcTitleLabel.anchor = GridBagConstraints.EAST;
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

        JLabel authorLabel = new JLabel(
                UiComponentStrings
                        .getString("BookDetailJDialog.label.author.text")); //$NON-NLS-1$
        GridBagConstraints gbcAuthorLabel = new GridBagConstraints();
        gbcAuthorLabel.anchor = GridBagConstraints.EAST;
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

        JLabel publisherLabel = new JLabel(
                UiComponentStrings
                        .getString("BookDetailJDialog.label.publisher.text")); //$NON-NLS-1$
        GridBagConstraints gbcPublisherLabel = new GridBagConstraints();
        gbcPublisherLabel.anchor = GridBagConstraints.EAST;
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

        
        JLabel shelfLable = new JLabel(
                UiComponentStrings
                        .getString("BookDetailJDialog.label.condition.text")); //$NON-NLS-1$
        GridBagConstraints gbc_shelfLable = new GridBagConstraints();
        gbc_shelfLable.anchor = GridBagConstraints.EAST;
        gbc_shelfLable.insets = new Insets(0, 0, 5, 5);
        gbc_shelfLable.gridx = 1;
        gbc_shelfLable.gridy = 4;
        northPanel.add(shelfLable, gbc_shelfLable);

        comboShelf = new JComboBox<>();
        for (Shelf tempShelfValues : Shelf.values()) {
            comboShelf.addItem(tempShelfValues);
        }
        comboShelf.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // TODO: TEST
                if (displayedBookDO != null) {
                    displayedBookDO.setShelf((Shelf) comboShelf
                            .getSelectedItem());
                }
            }
        });
        GridBagConstraints gbc_comboShelf = new GridBagConstraints();
        gbc_comboShelf.insets = new Insets(0, 0, 5, 0);
        gbc_comboShelf.fill = GridBagConstraints.HORIZONTAL;
        gbc_comboShelf.gridx = 3;
        gbc_comboShelf.gridy = 4;
        northPanel.add(comboShelf, gbc_comboShelf);

        btnAddABook = new JButton(UiComponentStrings.getString("Add book"));
        btnAddABook.addActionListener(new SaveBookButtonListener());
        btnAddABook.setEnabled(false);

        GridBagConstraints gbc_btnAddABook = new GridBagConstraints();
        gbc_btnAddABook.anchor = GridBagConstraints.EAST;
        gbc_btnAddABook.gridx = 3;
        gbc_btnAddABook.gridy = 6;
        northPanel.add(btnAddABook, gbc_btnAddABook);

        JPanel southPanel = new JPanel();
        southPanel.setBorder(new TitledBorder(null, UiComponentStrings
                .getString("BookDetailJDialog.border.inventory.text"), //$NON-NLS-1$
                TitledBorder.LEADING, TitledBorder.TOP, null, null));
        add(southPanel);
        southPanel.setLayout(new BorderLayout(0, 0));

        JPanel southInformationPanel = new JPanel();
        southPanel.add(southInformationPanel, BorderLayout.NORTH);
        southInformationPanel.setLayout(new BoxLayout(southInformationPanel,
                BoxLayout.X_AXIS));

        JLabel labelCopies = new JLabel("Total Copies: 8"); //$NON-NLS-1$
        southInformationPanel.add(labelCopies);
        labelCopies.setAlignmentX(Component.RIGHT_ALIGNMENT);

        rigidArea = Box.createRigidArea(new Dimension(20, 20));
        southInformationPanel.add(rigidArea);

        JLabel labelAvailable = new JLabel("Total available: 2");
        southInformationPanel.add(labelAvailable);

        glue = Box.createGlue();
        southInformationPanel.add(glue);

        JButton btnRemoveSelected = new JButton(
                UiComponentStrings
                        .getString("BookDetailJPanel.btnRemoveSelected.text")); //$NON-NLS-1$
        southInformationPanel.add(btnRemoveSelected);
        btnRemoveSelected.addActionListener(new RemoveExemplarListener());

        JButton tempbuttonAddCopy = new JButton(
                UiComponentStrings
                        .getString("BookDetailJDialog.button.addcopy.text")); //$NON-NLS-1$
        southInformationPanel.add(tempbuttonAddCopy);
        tempbuttonAddCopy.setAlignmentX(Component.RIGHT_ALIGNMENT);

        JPanel southBookList = new JPanel();

        bookExemplarTable = new JTable();
        bookExemplarTable.setFillsViewportHeight(true);
        southBookList.add(bookExemplarTable);

        // TODO: PROBLEM AERA
        bookExemplarTable.setModel(new BookExemplarModel(displayedBookDO,
                detailLibrary));

        bookExemplarTable.setCellSelectionEnabled(true);
        bookExemplarTable.setColumnSelectionAllowed(false);

        JScrollPane jsp = new JScrollPane(bookExemplarTable);
        southPanel.add(jsp, BorderLayout.CENTER);

        JScrollPane scrollPane = new JScrollPane();
        southBookList.add(scrollPane);

    }

    /**
     * Initializes the handlers in this panel.
     * 
     * <br>Each editable fields gets a listener to listen for changes and if the
     * value differs from the loaded model, the {@link #dirty} flag is set to
     * true.
     * 
     * @see ChangeToDirtyDocumentListener
     */
    protected void initHandlers() {

        addModelStateChangeListener(new ModelStateChangeListener() {

            @Override
            public void stateChanged(ModelStateChangeEvent aModelStateChange) {
                if (isDirty()) {
                    btnAddABook.setEnabled(true);
                } else {
                    btnAddABook.setEnabled(false);
                }
            }
        });

        // set focus to titlefield after tab has been switched
        addComponentListener(new ComponentAdapter() {
            @Override
            public void componentShown(ComponentEvent aComponentShownEvent) {
                titleTextfield.requestFocusInWindow();
            }
        });

        if (displayedBookDO != null) {
            btnAddABook.setText("Save changes");
            comboShelf.setSelectedItem(displayedBookDO.getShelf());
            titleTextfield.getDocument().addDocumentListener(
                    new ChangeToDirtyDocumentListener(titleTextfield,
                            displayedBookDO.getTitle()));

            authorTextfield.getDocument().addDocumentListener(
                    new ChangeToDirtyDocumentListener(authorTextfield,
                            displayedBookDO.getAuthor()));

            publisherTextfield.getDocument().addDocumentListener(
                    new ChangeToDirtyDocumentListener(publisherTextfield,
                            displayedBookDO.getPublisher()));
        }

        if (displayedBookDO == null) {
            btnAddABook.setEnabled(true);
        }
    }

    /**
     * Registers a collection of actions to a given keystroke on this panel and
     * all subcomponents.
     * 
     * @param someActions
     *            map with keystrokes and their associated actions<br>
     *            {@code key}: keystroke to bind the action to<br> {@code value}
     *            : action that should be fired when keystroke has been pressed
     * 
     */
    void setAncestorActions(Map<KeyStroke, Action> someActions) {
        for (Map.Entry<KeyStroke, Action> tempAction : someActions.entrySet()) {
            Object actionName = tempAction.getValue().getValue(Action.NAME);
            getInputMap(JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT).put(
                    tempAction.getKey(), actionName);
            getActionMap().put(actionName, tempAction.getValue());
        }
    }

    /**
     * Saves all fields in the book object.
     * 
     * @return if something has been saved, is not the case if
     *         {@code dirty==false}
     */
    protected boolean save() {
        if (displayedBookDO == null) {
            System.out.println("test");
            displayedBookDO = detailLibrary.createAndAddBook(titleTextfield
                    .getText());
            displayedBookDO.set(titleTextfield.getText(),
                    authorTextfield.getText(), publisherTextfield.getText(),
                    (Shelf) comboShelf.getSelectedItem());
            btnAddABook.setEnabled(false);
            setBookDO(displayedBookDO);
            initHandlers();
        }

        if (isDirty()) {
            displayedBookDO.set(titleTextfield.getText(),
                    authorTextfield.getText(), publisherTextfield.getText(),
                    displayedBookDO.getShelf());
            // TODO getShelf anpassen sobald implementiert im GUI
            setDirty(false);
            return true;
        }
        return false;
    }

    /**
     * @param isDirty
     *            the dirty to set
     */
    public void setDirty(boolean isDirty) {
        ModelStateChangeEvent newState = null;
        if (dirty) {
            if (isDirty) {
                newState = new ModelStateChangeEvent(this,
                        ModelStateChangeEvent.MODEL_STILL_DIRTY);
            } else {
                newState = new ModelStateChangeEvent(this,
                        ModelStateChangeEvent.MODEL_CHANGED_TO_SAVED);
            }
        } else {
            if (isDirty) {
                newState = new ModelStateChangeEvent(this,
                        ModelStateChangeEvent.MODEL_CHANGED_TO_DIRTY);
            }
        }

        dirty = isDirty;

        for (ModelStateChangeListener tempListener : listenerList
                .getListeners(ModelStateChangeListener.class)) {
            tempListener.stateChanged(newState);
        }
    }

    /**
     * @return if this book tab has unsaved changes
     */
    public boolean isDirty() {
        return dirty;
    }

    /**
     * @return the displayedBookDO
     */
    public BookDO getDisplayedBookDO() {
        return displayedBookDO;
    }

    /**
     * Adds a listener that listens for events when any object in this panel has
     * changed and {@link #isDirty()} returns {@code true} now.
     * 
     * @param aListener
     *            the listener to add
     */
    public void addModelStateChangeListener(ModelStateChangeListener aListener) {
        listenerList.add(ModelStateChangeListener.class, aListener);
    }

    /**
     * Removes a {@code ModelStateChangeListener} from this panel.
     * 
     * @param aListener
     *            the listener to remove
     */
    public void removeModelStateChangeListener(
            ModelStateChangeListener aListener) {
        listenerList.remove(ModelStateChangeListener.class, aListener);
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.util.Observer#update(java.util.Observable, java.lang.Object)
     */
    @Override
    public void update(Observable anObservable, Object anArgument) {
        updateDisplay();
    }

    private void updateDisplay() {
        // this case should never happen, that the book is null but it still
        // prevents us from a npe
        if (displayedBookDO == null) {
            titleTextfield.setText(UiComponentStrings.getString("empty")); //$NON-NLS-1$
            titleTextfield.setEnabled(true);
            authorTextfield.setText(UiComponentStrings.getString("empty")); //$NON-NLS-1$
            authorTextfield.setEnabled(true);
            publisherTextfield.setText(UiComponentStrings.getString("empty")); //$NON-NLS-1$
            publisherTextfield.setEnabled(true);

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
     * Checks if a value in a textfield has changed to its original content
     * given in {@code aStringToCheck} and if so, set this panel to dirty (=has
     * unsaved changes).
     * 
     * @author msyfrig
     */
    private class ChangeToDirtyDocumentListener implements DocumentListener {
        private JTextField textFieldToCheck;
        private String     stringToCheck;

        private ChangeToDirtyDocumentListener(JTextField aTextFieldToCheck,
                String aStringToCheck) {
            textFieldToCheck = aTextFieldToCheck;
            stringToCheck = aStringToCheck;
        }

        private void checkIfModified() {
            // check if text differs from the loaded book object, if
            // not set this panel to dirty
            if (!textFieldToCheck.getText().equals(stringToCheck)) {
                setDirty(true);
            }
        }

        @Override
        public void removeUpdate(DocumentEvent aDocumentRemoveEvent) {
            checkIfModified();
        }

        @Override
        public void insertUpdate(DocumentEvent aDocumentInsertEvent) {
            checkIfModified();
        }

        @Override
        public void changedUpdate(DocumentEvent aDocumentChangedEvent) {
            checkIfModified();
        }
    }

    private class SaveBookButtonListener implements ActionListener {
        /**
         * Opens an empty detail view with the ability to save a new book.
         */
        @Override
        public void actionPerformed(ActionEvent anActionEvent) {
            save();
        }
    }

    private class RemoveExemplarListener implements ActionListener {
        /**
         * Opens an empty detail view with the ability to save a new book.
         */
        @Override
        public void actionPerformed(ActionEvent anActionEvent) {
            // TODO: DELETE EXEMPLAR O_O

        }
    }

}
