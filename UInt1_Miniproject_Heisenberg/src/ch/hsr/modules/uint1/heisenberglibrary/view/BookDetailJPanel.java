/*
//    This program is free software: you can redistribute it and/or modify
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
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

import javax.swing.AbstractAction;
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
import javax.swing.KeyStroke;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import ch.hsr.modules.uint1.heisenberglibrary.controller.IModelStateChangeListener;
import ch.hsr.modules.uint1.heisenberglibrary.controller.ModelStateChangeEvent;
import ch.hsr.modules.uint1.heisenberglibrary.model.BookDO;
import ch.hsr.modules.uint1.heisenberglibrary.model.Copy;
import ch.hsr.modules.uint1.heisenberglibrary.model.IModelChangeType;
import ch.hsr.modules.uint1.heisenberglibrary.model.Library;
import ch.hsr.modules.uint1.heisenberglibrary.model.ModelChangeTypeEnums;
import ch.hsr.modules.uint1.heisenberglibrary.model.ObservableModelChangeEvent;
import ch.hsr.modules.uint1.heisenberglibrary.model.Shelf;
import ch.hsr.modules.uint1.heisenberglibrary.view.model.BookCopyTableModel;
import ch.hsr.modules.uint1.heisenberglibrary.view.model.IDisposable;

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
public class BookDetailJPanel extends AbstractObservableObjectJPanel<BookDO>
        implements Observer {
    private static final long               serialVersionUID = 3353323227207467624L;

    private Library                         library;

    // components
    private StatusBackgroundColorJTextField titleTextfield;
    private StatusBackgroundColorJTextField authorTextfield;
    private StatusBackgroundColorJTextField publisherTextfield;
    private JComboBox<Shelf>                comboShelf;
    private Component                       rigidArea;
    private JTable                          bookCopyTable;
    private JButton                         addBookButton;
    private JButton                         addCopyButton;
    private JButton                         removeSelectedCopiesButton;
    private JLabel                          numberOfCopiesLabel;
    private JLabel                          numberOfAvailableCopiesLabel;

    // actions
    private AddCopyAction                   addCopyAction;
    private RemoveCopyAction                removeCopyAction;
    private SaveBookAction                  saveBookAction;
    private JLabel                          errorLabel;

    /**
     * Creates a new instance of this class and sets the models.
     */
    public BookDetailJPanel(BookDO aBookDo, Library aLibrary) {
        super(aBookDo);
        library = aLibrary;
        initEverything(aBookDo);
        addObserverForObservable(library, this);
    }

    /**
     * Sets the model for this dialog and adds observer to it or removes the
     * observer if a new book is set. This happens only when a new book has been
     * added.
     */
    private void setBook(BookDO aNewBook) {
        setDisplayedObject(aNewBook);
        if (displayedObject != null) {
            addCopyAction.setEnabled(true);
        } else {
            addCopyAction.setEnabled(false);
        }
        bookCopyTable
                .setModel(new BookCopyTableModel(displayedObject, library));
        updateDisplay();
    }

    /**
     * Initializes everything :p
     * 
     * @param aBookDo
     *            the model to set
     */
    private void initEverything(BookDO aBookDo) {
        initComponents();
        initHandlersForNewBook();
        setBook(aBookDo);
        if (displayedObject != null) {
            initHandlersForSetBook();
        }
        updateNumberOfCopiesLabel();
        updateNumberOfAvailableCopiesLabel();

        titleTextfield.getDocument().addDocumentListener(
                new SaveValidationListener());

        authorTextfield.getDocument().addDocumentListener(
                new SaveValidationListener());

        publisherTextfield.getDocument().addDocumentListener(
                new SaveValidationListener());

    }

    /**
     * Initializes and adds all components to the panel.
     */
    private void initComponents() {
        setBorder(new EmptyBorder(5, 5, 5, 5));
        setLayout(new BorderLayout(0, 0));

        JPanel northPanel = new JPanel();
        northPanel.setBorder(new TitledBorder(UIManager
                .getBorder("TitledBorder.border"),//$NON-NLS-1$
                UiComponentStrings
                        .getString("BookDetailJDialog.border.title.text"), //$NON-NLS-1$
                TitledBorder.LEADING, TitledBorder.TOP, null, null));
        add(northPanel, BorderLayout.NORTH);
        GridBagLayout gblPanel = new GridBagLayout();
        gblPanel.columnWidths = new int[] { 0, 0, 0, 396, 0, 0 };
        gblPanel.rowHeights = new int[] { 30, 0, 0, 0, 0, 0, 0, 0 };
        gblPanel.columnWeights = new double[] { 0.0, 0.0, 0.0, 0.0, 1.0,
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

        titleTextfield = new StatusBackgroundColorJTextField();
        titleTextfield.setNegativeBackground();
        GridBagConstraints gbcTitleTextfield = new GridBagConstraints();
        gbcTitleTextfield.gridwidth = 2;
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

        authorTextfield = new StatusBackgroundColorJTextField();
        GridBagConstraints gbcAuthorTextfield = new GridBagConstraints();
        gbcAuthorTextfield.gridwidth = 2;
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

        publisherTextfield = new StatusBackgroundColorJTextField();
        GridBagConstraints gbcPublisherTextfield = new GridBagConstraints();
        gbcPublisherTextfield.gridwidth = 2;
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
        GridBagConstraints gbcComboShelf = new GridBagConstraints();
        gbcComboShelf.gridwidth = 2;
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

        errorLabel = new JLabel();
        GridBagConstraints gbcErrorLabel = new GridBagConstraints();
        gbcErrorLabel.anchor = GridBagConstraints.EAST;
        gbcErrorLabel.insets = new Insets(0, 0, 0, 5);
        gbcErrorLabel.gridx = 3;
        gbcErrorLabel.gridy = 6;
        northPanel.add(errorLabel, gbcErrorLabel);
        addBookButton = new ToolTipJButton(addBookButtonText,
                addBookButtonEnabledTooltip, addBookButtonDisabledTooltip);

        GridBagConstraints gbcBtnAddABook = new GridBagConstraints();
        gbcBtnAddABook.anchor = GridBagConstraints.EAST;
        gbcBtnAddABook.gridx = 4;
        gbcBtnAddABook.gridy = 6;
        northPanel.add(addBookButton, gbcBtnAddABook);

        JPanel southPanel = new JPanel(new BorderLayout());
        southPanel.setBorder(new TitledBorder(null, UiComponentStrings
                .getString("BookDetailJDialog.border.inventory.text"), //$NON-NLS-1$
                TitledBorder.LEADING, TitledBorder.TOP, null, null));

        JPanel southInformationPanel = new JPanel();
        southPanel.add(southInformationPanel, BorderLayout.NORTH);
        southInformationPanel.setLayout(new BoxLayout(southInformationPanel,
                BoxLayout.X_AXIS));

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
        southInformationPanel.add(removeSelectedCopiesButton);

        String addCopyButtonText = UiComponentStrings
                .getString("BookDetailJDialog.button.addcopy.text"); //$NON-NLS-1$
        String addCopyButtonEnabledTooltip = UiComponentStrings
                .getString("BookDetailJDialog.button.addcopy.enabled.tooltip"); //$NON-NLS-1$
        String addCopyButtonDisabledTooltip = UiComponentStrings
                .getString("BookDetailJDialog.button.addcopy.disabled.tooltip"); //$NON-NLS-1$
        addCopyButton = new ToolTipJButton(addCopyButtonText,
                addCopyButtonEnabledTooltip, addCopyButtonDisabledTooltip);
        addCopyButton.setAlignmentX(Component.RIGHT_ALIGNMENT);
        southInformationPanel.add(addCopyButton);

        bookCopyTable = new JTable();

        bookCopyTable.getTableHeader().setReorderingAllowed(false);
        bookCopyTable.setAutoCreateRowSorter(true);
        bookCopyTable.setCellSelectionEnabled(true);
        bookCopyTable.setFillsViewportHeight(true);
        bookCopyTable.setColumnSelectionAllowed(false);

        JScrollPane jsp = new JScrollPane(bookCopyTable);
        jsp.setPreferredSize(new Dimension((int) jsp.getPreferredSize()
                .getWidth(), 200));
        southPanel.add(jsp, BorderLayout.CENTER);
        add(southPanel);
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
    private void initHandlersForNewBook() {
        titleTextfield.setNegativeBackground();
        authorTextfield.setNegativeBackground();
        publisherTextfield.setNegativeBackground();

        saveBookAction = new SaveBookAction(addBookButton.getText());
        saveBookAction.setEnabled(false);
        addBookButton.setAction(saveBookAction);
        addBookButton.setMnemonic('s');

        addCopyAction = new AddCopyAction(addCopyButton.getText());
        addCopyButton.setAction(addCopyAction);
        addCopyAction.setEnabled(false);
        addCopyButton.setMnemonic('n');

        removeCopyAction = new RemoveCopyAction(
                removeSelectedCopiesButton.getText());
        removeSelectedCopiesButton.setAction(removeCopyAction);
        removeCopyAction.setEnabled(false);
        removeSelectedCopiesButton.setMnemonic('r');

        bookCopyTable.getSelectionModel().addListSelectionListener(
                new ListSelectionListener() {
                    @Override
                    public void valueChanged(
                            ListSelectionEvent aListSelectionEvent) {
                        if (bookCopyTable.getSelectedRowCount() > 0) {
                            removeCopyAction.setEnabled(true);
                        } else {
                            removeCopyAction.setEnabled(false);
                        }
                    }
                });

        addModelStateChangeListener(new IModelStateChangeListener() {
            @Override
            public void stateChanged(ModelStateChangeEvent aModelStateChange) {
                if (validateSaveAndLockButton()) {
                    saveBookAction.setEnabled(isDirty());
                }
            }
        });
        // set focus to titlefield after tab has been switched
        addComponentListener(new ComponentAdapter() {
            @Override
            public void componentShown(ComponentEvent aComponentShownEvent) {
                titleTextfield.requestFocus();
            }
        });

        KeyStroke ctrlS = KeyStroke.getKeyStroke(KeyEvent.VK_S,
                InputEvent.CTRL_DOWN_MASK);
        getInputMap(JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT).put(ctrlS,
                saveBookAction.getValue(Action.NAME));
        getActionMap()
                .put(saveBookAction.getValue(Action.NAME), saveBookAction);
    }

    private void initHandlersForSetBook() {
        saveBookAction.setEnabled(false);
        addBookButton.setText(UiComponentStrings
                .getString("BookDetailJPanel.button.addBookButton.save.text")); //$NON-NLS-1$

        comboShelf.setSelectedItem(displayedObject.getShelf());

        comboShelf.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (displayedObject != null) {
                    checkIfModified();
                }
            }
        });

        ChangeToDirtyDocumentListener dirtyChangeListener = new ChangeToDirtyDocumentListener();
        titleTextfield.getDocument().addDocumentListener(dirtyChangeListener);

        authorTextfield.getDocument().addDocumentListener(dirtyChangeListener);

        publisherTextfield.getDocument().addDocumentListener(
                dirtyChangeListener);
        validateSave();
    }

    /**
     * Saves all fields in the book object.
     * 
     * @return if something has been saved, is not the case if
     *         {@code dirty==false}
     */
    @Override
    protected boolean save() {
        boolean saveSuccess = false;
        if (displayedObject == null) {
            if (validateSave()) {
                BookDO createdBook = library.createAndAddBook(titleTextfield
                        .getText());
                createdBook.set(titleTextfield.getText(),
                        authorTextfield.getText(),
                        publisherTextfield.getText(),
                        (Shelf) comboShelf.getSelectedItem());
                setBook(createdBook);
                ModelStateChangeEvent newState = new ModelStateChangeEvent(
                        this, ModelStateChangeEvent.NEW_ENTRY_ADDED);
                notifyListenersAboutModelChange(newState);
                initHandlersForSetBook();
                saveBookAction.setEnabled(false);
                saveSuccess = true;
            }
        }
        if (isDirty()) {
            if (validateSave()) {
                displayedObject.set(titleTextfield.getText(),
                        authorTextfield.getText(),
                        publisherTextfield.getText(),
                        (Shelf) comboShelf.getSelectedItem());
                checkIfModified();
                // setDirty(false);
                saveSuccess = true;
            }
        }
        return saveSuccess;
    }

    protected boolean validateSave() {
        boolean validationResult = true;
        boolean hasEmptyFields = false;
        String title = titleTextfield.getText();
        String author = authorTextfield.getText();
        String publisher = publisherTextfield.getText();

        {  // Set the color textfields by checking each
            if (title.isEmpty()) {
                titleTextfield.setNegativeBackground();
                hasEmptyFields = true;
            } else {
                titleTextfield.setPositiveBackground();
            }

            if (author.isEmpty()) {
                authorTextfield.setNegativeBackground();
                hasEmptyFields = true;
            } else {
                authorTextfield.setPositiveBackground();
            }

            if (publisher.isEmpty()) {
                publisherTextfield.setNegativeBackground();
                hasEmptyFields = true;
            } else {
                publisherTextfield.setPositiveBackground();
            }
        }

        // Check if all fields contain text for validation
        if (hasEmptyFields) {
            errorLabel
                    .setText(UiComponentStrings
                            .getString("BookDetailJPanel.label.error.text.emptyFields")); //$NON-NLS-1$
            validationResult = false;
        } else {
            // Check if book-title + author already exist, but only if it is a
            // new book we can't check this way when dealing with existing
            // books.
            // TODO set book need to be checked too
            errorLabel.setText(UiComponentStrings.getString("empty")); //$NON-NLS-1$
            ArrayList<BookDO> tempBooks = library.findAllBooksByTitle(title);
            for (BookDO b : tempBooks) {
                // !b.equals(displayedObject) is for checking wheter it is
                // actually a copy or we just found the displayedBook again
                if (b.getAuthor().equalsIgnoreCase(author)
                        && !b.equals(displayedObject)) {
                    titleTextfield.setNegativeBackground();
                    authorTextfield.setNegativeBackground();
                    validationResult = false;
                    errorLabel
                            .setText(UiComponentStrings
                                    .getString("BookDetailJPanel.label.error.text.duplicateBook")); //$NON-NLS-1$
                    break;
                }
            }
        }
        valid = validationResult;
        return validationResult;
    }

    private void checkIfModified() {
        if (displayedObject != null) {
            // check if text differs from the loaded book object, if
            // not set this panel to dirty
            BookDO tempBook = new BookDO(titleTextfield.getText());
            tempBook.set(titleTextfield.getText(), authorTextfield.getText(),
                    publisherTextfield.getText(),
                    (Shelf) comboShelf.getSelectedItem());
            if (displayedObject.equals(tempBook)) {
                setDirty(false);
            } else {
                setDirty(true);
            }
        }
    }

    public boolean validateSaveAndLockButton() {
        boolean validation = validateSave();
        saveBookAction.setEnabled(validation);
        return validation;
    }

    @Override
    public void update(Observable anObservable, Object anArgument) {
        if (anArgument instanceof ObservableModelChangeEvent) {
            ObservableModelChangeEvent modelChange = (ObservableModelChangeEvent) anArgument;
            IModelChangeType type = modelChange.getChangeType();
            if (type == ModelChangeTypeEnums.Copy.REMOVED
                    || type == ModelChangeTypeEnums.Copy.ADDED) {
                SwingUtilities.invokeLater(new Runnable() {
                    @Override
                    public void run() {
                        ((BookCopyTableModel) bookCopyTable.getModel())
                                .updateTableData();
                    }
                });
            } else if (type == ModelChangeTypeEnums.Copy.NUMBER) {
                SwingUtilities.invokeLater(new Runnable() {
                    @Override
                    public void run() {
                        updateNumberOfCopiesLabel();
                        updateNumberOfAvailableCopiesLabel();
                    }
                });
            } else if (type == ModelChangeTypeEnums.Book.EVERYTHING_CHANGED
                    || type == ModelChangeTypeEnums.Book.AUTHOR
                    || type == ModelChangeTypeEnums.Book.PUBLISHER
                    || type == ModelChangeTypeEnums.Book.SHELF
                    || type == ModelChangeTypeEnums.Book.TITLE) {
                // just update everything, no need for premature optimization
                SwingUtilities.invokeLater(new Runnable() {
                    @Override
                    public void run() {
                        updateDisplay();
                    }
                });
            }
        }
    }

    private void updateDisplay() {
        // this case should never happen, that the book is null but it still
        // prevents us from a npe
        if (displayedObject == null) {
            titleTextfield.setText(UiComponentStrings.getString("empty")); //$NON-NLS-1$
            titleTextfield.setEnabled(true);
            authorTextfield.setText(UiComponentStrings.getString("empty")); //$NON-NLS-1$
            authorTextfield.setEnabled(true);
            publisherTextfield.setText(UiComponentStrings.getString("empty")); //$NON-NLS-1$
            publisherTextfield.setEnabled(true);

        } else {
            // TODO save caret position
            titleTextfield.setText(displayedObject.getTitle());
            titleTextfield.setEnabled(true);
            authorTextfield.setText(displayedObject.getAuthor());
            authorTextfield.setEnabled(true);
            publisherTextfield.setText(displayedObject.getPublisher());
            publisherTextfield.setEnabled(true);
            comboShelf.setSelectedItem(displayedObject.getShelf());
            comboShelf.setEnabled(true);
        }
    }

    private void updateNumberOfCopiesLabel() {
        String numberOfCopiesString = MessageFormat
                .format(UiComponentStrings
                        .getString("BookDetailJPanel.label.numberofcopies.text"), //$NON-NLS-1$
                        Integer.valueOf(library
                                .getCopiesOfBook(displayedObject).size()));
        numberOfCopiesLabel.setText(numberOfCopiesString);
    }

    private void updateNumberOfAvailableCopiesLabel() {
        String numberOfAvailableCopiesString = MessageFormat
                .format(UiComponentStrings
                        .getString("BookDetailJPanel.label.numberofavailablecopies.text"), //$NON-NLS-1$
                        Integer.valueOf(library.getAvailableCopiesForBook(
                                displayedObject).size()));
        numberOfAvailableCopiesLabel.setText(numberOfAvailableCopiesString);
    }

    private void removeSelectedCopiesIfAllowed() {
        if (removeCopyAction.isEnabled()) {
            List<Copy> copyList = library.getCopiesOfBook(displayedObject);

            // we need an extra list because we would need to update the
            // copyList each time we deleted one copy with the new list in the
            // library with copyList = library.getCopiesOfBook(displayedObject)
            // because the indexes changes
            List<Copy> copiesToDelete = new ArrayList<>(
                    bookCopyTable.getSelectedRowCount());
            for (int tempCopy : bookCopyTable.getSelectedRows()) {
                copiesToDelete.add(copyList.get(bookCopyTable
                        .convertRowIndexToModel(tempCopy)));

            }
            library.removeCopies(copiesToDelete);
            copyList = library.getCopiesOfBook(displayedObject);
        }
    }

    private void createCopyIfAllowed() {
        if (addCopyAction.isEnabled()) {
            library.createAndAddCopy(displayedObject);
        }
    }

    private class SaveBookAction extends AbstractAction {
        private static final long serialVersionUID = -2974999148987189986L;

        private SaveBookAction(String anActionName) {
            super(anActionName);
        }

        @Override
        public void actionPerformed(ActionEvent anActionEvent) {
            save();
        }
    }

    private class RemoveCopyAction extends AbstractAction {
        private static final long serialVersionUID = 5191625301014725399L;

        private RemoveCopyAction(String anActionName) {
            super(anActionName);
        }

        @Override
        public void actionPerformed(ActionEvent anActionEvent) {
            removeSelectedCopiesIfAllowed();
        }
    }

    private class AddCopyAction extends AbstractAction {
        private static final long serialVersionUID = -4973712671800673981L;

        private AddCopyAction(String anActionName) {
            super(anActionName);
        }

        @Override
        public void actionPerformed(ActionEvent anActionEvent) {
            createCopyIfAllowed();
        }
    }

    /**
     * Checks if a value in a textfield has changed to its original content
     * given in {@code aStringToCheck} and if so, set this panel to dirty (=has
     * unsaved changes).
     * 
     * @author msyfrig
     */
    protected class ChangeToDirtyDocumentListener implements DocumentListener {
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

    class SaveValidationListener implements DocumentListener {

        @Override
        public void insertUpdate(DocumentEvent anInsertEvent) {
            validateSaveAndLockButton();
        }

        @Override
        public void removeUpdate(DocumentEvent aRemoveEvent) {
            validateSaveAndLockButton();
        }

        @Override
        public void changedUpdate(DocumentEvent aChangedEvent) {
            validateSaveAndLockButton();
        }

    }

    @Override
    public void cleanUpBeforeDispose() {
        removeAllObservers();
        if (bookCopyTable.getModel() instanceof IDisposable) {
            ((IDisposable) bookCopyTable.getModel()).cleanUpBeforeDispose();
        }
    }

    @Override
    public void createNew() {
        createCopyIfAllowed();
    }

    @Override
    public void tableRequestFocus() {
        if (bookCopyTable != null) {
            bookCopyTable.requestFocus();
        }
    }

    @Override
    public void removeSelected() {
        removeSelectedCopiesIfAllowed();
    }

    @Override
    public JButton getDefaultButton() {
        return addBookButton;
    }
}
