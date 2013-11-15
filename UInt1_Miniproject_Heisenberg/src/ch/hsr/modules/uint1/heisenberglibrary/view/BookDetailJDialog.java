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
import java.awt.event.ActionEvent;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Observable;
import java.util.Observer;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JFrame;
import javax.swing.JTabbedPane;
import javax.swing.KeyStroke;

import ch.hsr.modules.uint1.heisenberglibrary.controller.ModelStateChangeEvent;
import ch.hsr.modules.uint1.heisenberglibrary.controller.ModelStateChangeListener;
import ch.hsr.modules.uint1.heisenberglibrary.model.BookDO;
import ch.hsr.modules.uint1.heisenberglibrary.model.Library;
import ch.hsr.modules.uint1.heisenberglibrary.model.ModelChangeType;
import ch.hsr.modules.uint1.heisenberglibrary.model.ModelChangeTypeEnums;
import ch.hsr.modules.uint1.heisenberglibrary.model.ObservableModelChangeEvent;

/**
 * This dialog holds all opened details views for all books. Each tab represents
 * one {@link BookDO}. If a view for a book is already opened, it will be
 * focused.
 * 
 * @author msyfrig
 */
// TODO update booktitle if changed or new book added
public class BookDetailJDialog extends AbstractDefaultJDialog implements
        Observer {
    private static final long      serialVersionUID = 439819991326389792L;
    /** The tabbed pane that holds all detailviews as tabs for all books. */
    private JTabbedPane            tabbedPane;

    /**
     * The list of all opened book detailviews. To check wheter a {@link BookDO}
     * is already open an iteration over all items is necessary and then call
     * {@link BookDetailJPanel#getDisplayedBookDO()} and compare it is
     * necessary.
     * 
     * <br> And if you ask yourself why this is not implemented as a map with
     * the book as key and the opened tab as value: the behavior for mutated
     * keys is undefined and since we can modify the book it would not be
     * possible to ever delete this book instance. See javadoc of map for
     * undefined behavior for mutable objects as keys.
     * 
     * @see Map
     */
    private List<BookDetailJPanel> openBookTabList  = new ArrayList<>();

    /**
     * Create the dialog.
     */
    public BookDetailJDialog(JFrame anOwner) {
        super(anOwner, UiComponentStrings.getString("BookDetailJDialog.title")); //$NON-NLS-1$
    }

    @Override
    protected void initComponents() {
        setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
        setBounds(100, 100, 900, 400);
        getContentPane().setLayout(new BorderLayout());
        tabbedPane = new JTabbedPane(JTabbedPane.TOP);
        getContentPane().add(tabbedPane, BorderLayout.CENTER);
    }

    @Override
    protected void initHandlers() {
        addComponentListener(new ComponentAdapter() {
            /**
             * All opened book tabs are closed before hiding this dialog so they
             * are not still open if the user selects a new book and expects to
             * open a completely new dialog.
             */
            @Override
            public void componentHidden(ComponentEvent aComponentHiddenEvent) {
                openBookTabList.clear();
                tabbedPane.removeAll();
            }
        });
    }

    /**
     * Opens a new tab for the given book or focuses the tab, if the book has
     * been opened previously.
     * 
     * @param aBookToOpen
     *            the book domain object to open in a tab
     */
    public void openBookTab(BookDO aBookToOpen, Library aLibrary) {
        // check if a tab with the given tab is already open
        BookDetailJPanel detailBookPanel = getTabForBook(aBookToOpen);

        // if not open yet, create it and all listeners and actions
        if (detailBookPanel == null) {
            detailBookPanel = new BookDetailJPanel(aBookToOpen, aLibrary);

            String tabTitle = "add new book";
            if (aBookToOpen != null) {
                // add observer to this book so we notice when the title has
                // changed
                aBookToOpen.addObserver(this);

                tabTitle = getTabTitleForBook(aBookToOpen);

                tabbedPane.addTab(tabTitle, null, detailBookPanel,
                        aBookToOpen.toString());
            } else {
                tabbedPane.addTab(tabTitle, null, detailBookPanel,
                        "Entering a new book");
            }
            // focus the newly created tab, well should happen automatically but
            // does not always work
            tabbedPane.setSelectedComponent(detailBookPanel);
            Map<KeyStroke, Action> actionMapForBookTab = new HashMap<>(2);
            actionMapForBookTab.put(
                    KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0),
                    new DisposeAction("dispose", detailBookPanel)); //$NON-NLS-1$
            actionMapForBookTab.put(
                    KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, 0),
                    new SaveAction("save", detailBookPanel)); //$NON-NLS-1$
            detailBookPanel.addAncestorActions(actionMapForBookTab);
            // add asteriks in tabtitle if tab has unsaved changes and listen
            // for added books to set the title
            detailBookPanel
                    .addModelStateChangeListener(new BookDetailModelChangeListener(
                            detailBookPanel));
            openBookTabList.add(detailBookPanel);
            pack();
        }
        // if already opened, just bring this tab to the front
        else {
            tabbedPane.setSelectedComponent(detailBookPanel);
        }
    }

    private BookDetailJPanel getTabForBook(BookDO aBook) {
        BookDetailJPanel detailBookPanel = null;
        if (aBook != null) {
            for (BookDetailJPanel tempBookDetailView : openBookTabList) {
                if (tempBookDetailView.getDisplayedBookDO() == aBook) {
                    detailBookPanel = tempBookDetailView;
                }
            }
        }
        return detailBookPanel;
    }

    private static String getTabTitleForBook(BookDO aBook) {
        return (aBook.toString().length() >= 15) ? aBook.toString().substring(
                0, 15) : aBook.toString();
    }

    @Override
    protected boolean save() {
        return false;
    }

    /**
     * Closes an opened tab for a book, removes it from the list of opened books
     * so it can be reopened. If the closed tab was the last one, this dialog is
     * closed.
     * 
     * @param aBookDetailTabToClose
     *            the book detail tab to close
     */
    private void closeTab(BookDetailJPanel aBookDetailTabToClose) {
        if (aBookDetailTabToClose != null) {
            tabbedPane.remove(aBookDetailTabToClose);
            openBookTabList.remove(aBookDetailTabToClose);
            // close this dialog if this was the last open tab
            if (openBookTabList.isEmpty()) {
                dispose();
            }
        }
    }

    /**
     * Action to handle the save actions. Saves all unsaved changes and closes
     * the tab.
     * 
     * @author msyfrig
     */
    private class SaveAction extends AbstractAction {
        private static final long serialVersionUID = -4275362945903839390L;
        private BookDetailJPanel  bookDetailTab;

        private SaveAction(String anActionName,
                BookDetailJPanel anAssociatedBookTab) {
            super(anActionName);
            bookDetailTab = anAssociatedBookTab;
        }

        /**
         * Call {@link #save()} and close the tab.
         */
        @Override
        public void actionPerformed(ActionEvent anActionEvent) {
            bookDetailTab.save();
            closeTab(bookDetailTab);
        }
    }

    /**
     * Action to handle the closing of a tab. Informs the user if there are
     * unsaved changes in the tab before closing.
     * 
     * @author msyfrig
     */
    private class DisposeAction extends AbstractAction {
        private static final long serialVersionUID = 2752048542262499446L;
        private BookDetailJPanel  bookDetailTab;

        /**
         * Creates a new action with the given name and the associated book tab
         * in which this
         */
        private DisposeAction(String anActionName,
                BookDetailJPanel anAssociatedBookTab) {
            super(anActionName);
            bookDetailTab = anAssociatedBookTab;
        }

        /**
         * Checks if the tab has unsaved changes and informs the user if so and
         * closes the tab.
         */
        @Override
        public void actionPerformed(ActionEvent anActionEvent) {
            if (bookDetailTab.isDirty()) {
                // TODO Warnung anzeigen, dass Änderungen verloren gehen und
                // allenfalls Option geben, noch zu speichern oder abzubrechen
            }
            closeTab(bookDetailTab);
        }
    }

    /**
     * Lists for changes in the model for a specific tab and if it has unsaved
     * changes, the title gets an asterisk indicating the unsaved changes in
     * this tab.
     * 
     * @author msyfrig
     */
    private class BookDetailModelChangeListener implements
            ModelStateChangeListener {
        private BookDetailJPanel associatedDetailView;

        public BookDetailModelChangeListener(
                BookDetailJPanel anAssociatedDetailView) {
            associatedDetailView = anAssociatedDetailView;
        }

        @Override
        public void stateChanged(ModelStateChangeEvent aModelStateChange) {
            if (associatedDetailView != null) {
                int tabIndex = tabbedPane
                        .indexOfComponent(associatedDetailView);
                if (tabIndex >= 0) {
                    String oldTitle = tabbedPane.getTitleAt(tabIndex);
                    switch (aModelStateChange.getState()) {
                        case ModelStateChangeEvent.MODEL_CHANGED_TO_DIRTY:
                            tabbedPane.setTitleAt(tabIndex, "*" + oldTitle);
                            break;
                        case ModelStateChangeEvent.MODEL_CHANGED_TO_SAVED:
                            if (oldTitle.charAt(0) == '*') {
                                tabbedPane.setTitleAt(tabIndex,
                                        oldTitle.substring(1));
                            }
                            break;
                        case ModelStateChangeEvent.NEW_ENTRY_ADDED:
                            // add observer for newly created book
                            Component tempComp = tabbedPane
                                    .getComponentAt(tabIndex);
                            if (tempComp instanceof BookDetailJPanel) {
                                BookDetailJPanel detailPanel = (BookDetailJPanel) tempComp;
                                detailPanel.getDisplayedBookDO().addObserver(
                                        BookDetailJDialog.this);
                            }

                            tabbedPane.setTitleAt(tabIndex,
                                    getTabTitleForBook(associatedDetailView
                                            .getDisplayedBookDO()));
                            break;
                        default:
                            break;
                    }
                }
            }
        }
    }

    @Override
    public void update(Observable anObservable, Object anArgument) {
        if (anArgument instanceof ObservableModelChangeEvent) {
            ObservableModelChangeEvent modelChange = (ObservableModelChangeEvent) anArgument;
            ModelChangeType type = modelChange.getChangeType();
            if (type == ModelChangeTypeEnums.Book.TITLE
                    || type == ModelChangeTypeEnums.Book.EVERYTHING_CHANGED) {
                BookDetailJPanel detailBookPanel = getTabForBook((BookDO) anObservable);
                int tabIndex = tabbedPane.indexOfComponent(detailBookPanel);
                tabbedPane.setTitleAt(tabIndex,
                        getTabTitleForBook((BookDO) anObservable));
            }
        }
    }
}
