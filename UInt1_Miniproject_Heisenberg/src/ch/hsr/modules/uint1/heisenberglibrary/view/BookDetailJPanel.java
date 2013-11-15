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
import java.text.MessageFormat;
import java.util.List;
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
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import ch.hsr.modules.uint1.heisenberglibrary.controller.ModelStateChangeEvent;
import ch.hsr.modules.uint1.heisenberglibrary.controller.ModelStateChangeListener;
import ch.hsr.modules.uint1.heisenberglibrary.model.BookDO;
import ch.hsr.modules.uint1.heisenberglibrary.model.Copy;
import ch.hsr.modules.uint1.heisenberglibrary.model.Library;
import ch.hsr.modules.uint1.heisenberglibrary.model.ModelChangeType;
import ch.hsr.modules.uint1.heisenberglibrary.model.ModelChangeTypeEnums;
import ch.hsr.modules.uint1.heisenberglibrary.model.ObservableModelChangeEvent;
import ch.hsr.modules.uint1.heisenberglibrary.model.Shelf;
import ch.hsr.modules.uint1.heisenberglibrary.view.model.BookCopyModel;

/**
 * This class shows a single {@link BookDO} with all its properties and
 * existings copies.
 * 
 * <br> If the underlying model (the displayed {@link BookDO} changes, it is
 * updated via observer pattern.
 * 
 * <br>The {@link #titleTextfield} requests the focus as soon as this component
 * is shown, means when the tab gets selected.
 * 
 * @author msyfrig
 * @author twinter
 */
// TODO copies are not updated in table if a new book has been added
public class BookDetailJPanel extends JPanel implements Observer {
    private static final long serialVersionUID = 3353323227207467624L;

    /**
     * The displayed book that can be edited and is observed via observer
     * pattern.
     */
    private BookDO            displayedBook;

    private Library           library;

    /**
     * Flag to indicate if there are unsaved changes in this panel.
     */
    private boolean           dirty;

    // components
    private JTextField        titleTextfield;
    private JTextField        authorTextfield;
    private JTextField        publisherTextfield;
    private JComboBox<Shelf>  comboShelf;
    private Component         rigidArea;
    private JTable            bookCopyTable;
    private JButton           addBookButton;
    private JButton           addCopyButton;
    private JButton           removeSelectedCopiesButton;
    private JLabel            numberOfCopiesLabel;
    private JLabel            numberOfAvailableCopiesLabel;

    /**
     * Creates a new instance of this class and sets the models.
     */
    public BookDetailJPanel(BookDO aBookDo, Library aLibrary) {
        library = aLibrary;
        initEverything(aBookDo);
        library.addObserver(this);
    }

    /**
     * Sets the model for this dialog and adds observer to it or removes the
     * observer if a new book is set. This happens only when a new book has been
     * added.
     */
    private void setBook(BookDO aNewBook) {

        // do nothing if same book is set
        if (aNewBook != displayedBook) {
            // delete observers for this old, not anymore displayed book
            if (displayedBook != null) {
                displayedBook.deleteObserver(this);
            }
            displayedBook = aNewBook;
            // add observers for new book object
            if (displayedBook != null) {
                displayedBook.addObserver(this);
                addCopyButton.setEnabled(true);
            } else {
                addCopyButton.setEnabled(false);
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
        setBook(aBookDo);
        updateNumberOfCopiesLabel();
        updateNumberOfAvailableCopiesLabel();
        bookCopyTable.setModel(new BookCopyModel(displayedBook, library));
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
        GridBagConstraints gbcShelfLable = new GridBagConstraints();
        gbcShelfLable.anchor = GridBagConstraints.EAST;
        gbcShelfLable.insets = new Insets(0, 0, 5, 5);
        gbcShelfLable.gridx = 1;
        gbcShelfLable.gridy = 4;
        northPanel.add(shelfLable, gbcShelfLable);

        comboShelf = new JComboBox<>();
        for (Shelf tempShelfValues : Shelf.values()) {
            comboShelf.addItem(tempShelfValues);
        }
        comboShelf.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (displayedBook != null) {
                    displayedBook.setShelf((Shelf) comboShelf.getSelectedItem());
                }
            }
        });
        GridBagConstraints gbcComboShelf = new GridBagConstraints();
        gbcComboShelf.insets = new Insets(0, 0, 5, 0);
        gbcComboShelf.fill = GridBagConstraints.HORIZONTAL;
        gbcComboShelf.gridx = 3;
        gbcComboShelf.gridy = 4;
        northPanel.add(comboShelf, gbcComboShelf);

        String addBookButtonText = UiComponentStrings
                .getString("BookDetailJPanel.button.addbook.text");//$NON-NLS-1$
        String addBookButtonEnabledTooltip = UiComponentStrings
                .getString("BookDetailJPanel.button.addbook.enabled.tooltip"); //$NON-NLS-1$
        String addBookButtonDisabledTooltip = UiComponentStrings
                .getString("BookDetailJPanel.button.addbook.disabled.tooltip"); //$NON-NLS-1$
        addBookButton = new ToolTipJButton(addBookButtonText,
                addBookButtonEnabledTooltip, addBookButtonDisabledTooltip);
        addBookButton.addActionListener(new SaveBookButtonListener());
        addBookButton.setEnabled(false);

        GridBagConstraints gbcBtnAddABook = new GridBagConstraints();
        gbcBtnAddABook.anchor = GridBagConstraints.EAST;
        gbcBtnAddABook.gridx = 3;
        gbcBtnAddABook.gridy = 6;
        northPanel.add(addBookButton, gbcBtnAddABook);

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

        // TODO updaten
        numberOfCopiesLabel = new JLabel();
        southInformationPanel.add(numberOfCopiesLabel);
        numberOfCopiesLabel.setAlignmentX(Component.RIGHT_ALIGNMENT);

        rigidArea = Box.createRigidArea(new Dimension(20, 20));
        southInformationPanel.add(rigidArea);

        numberOfAvailableCopiesLabel = new JLabel();
        southInformationPanel.add(numberOfAvailableCopiesLabel);

        Component glue = Box.createGlue();
        southInformationPanel.add(glue);

        String removeSelectedCopiesButtonText = UiComponentStrings
                .getString("BookDetailJPanel.button.removeselectedcopies.text"); //$NON-NLS-1$
        String removeSelectedCopiesButtonEnabledToolTip = UiComponentStrings
                .getString("BookDetailJPanel.button.removeselectedcopies.enabled.tooltip"); //$NON-NLS-1$
        String removeSelectedCopiesButtonDisabledToolTip = UiComponentStrings
                .getString("BookDetailJPanel.button.removeselectedcopies.disabled.tooltip"); //$NON-NLS-1$

        removeSelectedCopiesButton = new ToolTipJButton(
                removeSelectedCopiesButtonText,
                removeSelectedCopiesButtonEnabledToolTip,
                removeSelectedCopiesButtonDisabledToolTip);
        removeSelectedCopiesButton.setEnabled(false);
        southInformationPanel.add(removeSelectedCopiesButton);

        String addCopyButtonText = UiComponentStrings
                .getString("BookDetailJDialog.button.addcopy.text"); //$NON-NLS-1$
        String addCopyButtonEnabledTooltip = UiComponentStrings
                .getString("BookDetailJDialog.button.addcopy.enabled.tooltip"); //$NON-NLS-1$
        String addCopyButtonDisabledTooltip = UiComponentStrings
                .getString("BookDetailJDialog.button.addcopy.disabled.tooltip"); //$NON-NLS-1$
        addCopyButton = new ToolTipJButton(addCopyButtonText,
                addCopyButtonEnabledTooltip, addCopyButtonDisabledTooltip);
        addCopyButton.setEnabled(false);
        southInformationPanel.add(addCopyButton);
        addCopyButton.setAlignmentX(Component.RIGHT_ALIGNMENT);
        addCopyButton.addActionListener(new AddCopyListener());

        JPanel southBookList = new JPanel();

        bookCopyTable = new JTable();

        bookCopyTable.getTableHeader().setReorderingAllowed(false);
        bookCopyTable.setAutoCreateRowSorter(true);
        bookCopyTable.setCellSelectionEnabled(true);
        bookCopyTable.setFillsViewportHeight(true);
        bookCopyTable.setColumnSelectionAllowed(false);
        southBookList.add(bookCopyTable);

        JScrollPane jsp = new JScrollPane(bookCopyTable);
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

        bookCopyTable.getSelectionModel().addListSelectionListener(
                new ListSelectionListener() {
                    @Override
                    public void valueChanged(
                            ListSelectionEvent aListSelectionEvent) {
                        if (bookCopyTable.getSelectedRowCount() > 0) {
                            removeSelectedCopiesButton.setEnabled(true);
                        } else {
                            removeSelectedCopiesButton.setEnabled(false);
                        }
                    }
                });

        addModelStateChangeListener(new ModelStateChangeListener() {
            @Override
            public void stateChanged(ModelStateChangeEvent aModelStateChange) {
                if (isDirty()) {
                    addBookButton.setEnabled(true);
                } else {
                    addBookButton.setEnabled(false);
                }
            }
        });
        removeSelectedCopiesButton.addActionListener(new RemoveCopyListener());

        // set focus to titlefield after tab has been switched
        addComponentListener(new ComponentAdapter() {
            @Override
            public void componentShown(ComponentEvent aComponentShownEvent) {
                titleTextfield.requestFocus();
            }
        });

        if (displayedBook != null) {
            addBookButton
                    .setText(UiComponentStrings
                            .getString("BookDetailJPanel.button.addBookButton.save.text")); //$NON-NLS-1$
            comboShelf.setSelectedItem(displayedBook.getShelf());
            titleTextfield.getDocument().addDocumentListener(
                    new ChangeToDirtyDocumentListener(titleTextfield,
                            displayedBook.getTitle()));

            authorTextfield.getDocument().addDocumentListener(
                    new ChangeToDirtyDocumentListener(authorTextfield,
                            displayedBook.getAuthor()));

            publisherTextfield.getDocument().addDocumentListener(
                    new ChangeToDirtyDocumentListener(publisherTextfield,
                            displayedBook.getPublisher()));
        }

        if (displayedBook == null) {
            addBookButton.setEnabled(true);
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
        if (displayedBook == null) {
            BookDO createdBook = library.createAndAddBook(titleTextfield
                    .getText());
            createdBook.set(titleTextfield.getText(),
                    authorTextfield.getText(), publisherTextfield.getText(),
                    (Shelf) comboShelf.getSelectedItem());
            setBook(createdBook);
            ModelStateChangeEvent newState = new ModelStateChangeEvent(this,
                    ModelStateChangeEvent.NEW_ENTRY_ADDED);
            notifyListenersAboutModelChange(newState);
            addBookButton.setEnabled(false);
        }

        if (isDirty()) {
            displayedBook.set(titleTextfield.getText(),
                    authorTextfield.getText(), publisherTextfield.getText(),
                    displayedBook.getShelf());
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
        notifyListenersAboutModelChange(newState);
        dirty = isDirty;
    }

    private void notifyListenersAboutModelChange(ModelStateChangeEvent aNewState) {
        for (ModelStateChangeListener tempListener : listenerList
                .getListeners(ModelStateChangeListener.class)) {
            tempListener.stateChanged(aNewState);
        }
    }

    /**
     * @return if this book tab has unsaved changes
     */
    public boolean isDirty() {
        return dirty;
    }

    /**
     * @return the displayedBook
     */
    public BookDO getDisplayedBookDO() {
        return displayedBook;
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

    @Override
    public void update(Observable anObservable, Object anArgument) {
        if (anArgument instanceof ObservableModelChangeEvent) {
            ObservableModelChangeEvent modelChange = (ObservableModelChangeEvent) anArgument;
            ModelChangeType type = modelChange.getChangeType();
            if (type == ModelChangeTypeEnums.Copy.REMOVED
                    || type == ModelChangeTypeEnums.Copy.ADDED) {
                ((BookCopyModel) bookCopyTable.getModel())
                        .fireTableDataChanged();
            } else if (type == ModelChangeTypeEnums.Copy.NUMBER) {
                updateNumberOfCopiesLabel();
                updateNumberOfAvailableCopiesLabel();
            } else if (type == ModelChangeTypeEnums.Book.EVERYTHING_CHANGED
                    || type == ModelChangeTypeEnums.Book.AUTHOR
                    || type == ModelChangeTypeEnums.Book.PUBLISHER
                    || type == ModelChangeTypeEnums.Book.SHELF
                    || type == ModelChangeTypeEnums.Book.TITLE) {
                // just update everything, no need for premature optimization
                updateDisplay();
            }
        }
    }

    private void updateDisplay() {
        // this case should never happen, that the book is null but it still
        // prevents us from a npe
        if (displayedBook == null) {
            titleTextfield.setText(UiComponentStrings.getString("empty")); //$NON-NLS-1$
            titleTextfield.setEnabled(true);
            authorTextfield.setText(UiComponentStrings.getString("empty")); //$NON-NLS-1$
            authorTextfield.setEnabled(true);
            publisherTextfield.setText(UiComponentStrings.getString("empty")); //$NON-NLS-1$
            publisherTextfield.setEnabled(true);

        } else {
            // TODO save caret position
            titleTextfield.setText(displayedBook.getTitle());
            titleTextfield.setEnabled(true);
            titleTextfield.setCaretPosition(0);
            authorTextfield.setText(displayedBook.getAuthor());
            authorTextfield.setEnabled(true);
            publisherTextfield.setText(displayedBook.getPublisher());
            publisherTextfield.setEnabled(true);
            comboShelf.setSelectedItem(displayedBook.getShelf());
            comboShelf.setEnabled(true);
        }
    }

    private void updateNumberOfCopiesLabel() {
        String numberOfCopiesString = MessageFormat.format(UiComponentStrings
                .getString("BookDetailJPanel.label.numberofcopies.text"), //$NON-NLS-1$
                Integer.valueOf(library.getCopiesOfBook(displayedBook).size()));
        numberOfCopiesLabel.setText(numberOfCopiesString);
    }

    private void updateNumberOfAvailableCopiesLabel() {
        String numberOfAvailableCopiesString = MessageFormat
                .format(UiComponentStrings
                        .getString("BookDetailJPanel.label.numberofavailablecopies.text"), //$NON-NLS-1$
                        Integer.valueOf(library.getAvailableCopiesForBook(
                                displayedBook).size()));
        numberOfAvailableCopiesLabel.setText(numberOfAvailableCopiesString);
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
        @Override
        public void actionPerformed(ActionEvent anActionEvent) {
            save();
        }
    }

    private class RemoveCopyListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent anActionEvent) {

            List<Copy> copyList = library.getCopiesOfBook(displayedBook);

            for (int tempCopy : bookCopyTable.getSelectedRows()) {
                // TODO wenn ich mit Shift + Page Down selektiere erhalte ich
                // eine IndexOutOfBounds oO untersuchen und beheben
                Copy selectedCopy = copyList.get(bookCopyTable
                        .convertRowIndexToModel(tempCopy));
                library.removeCopy(selectedCopy);
            }
        }
    }

    private class AddCopyListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent anActionEvent) {
            library.createAndAddCopy(displayedBook);
        }
    }
}
