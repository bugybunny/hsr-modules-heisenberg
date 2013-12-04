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
import java.awt.Dimension;
import java.util.Observable;
import java.util.Observer;

import javax.swing.JFrame;

import ch.hsr.modules.uint1.heisenberglibrary.controller.IModelStateChangeListener;
import ch.hsr.modules.uint1.heisenberglibrary.controller.ModelStateChangeEvent;
import ch.hsr.modules.uint1.heisenberglibrary.model.BookDO;
import ch.hsr.modules.uint1.heisenberglibrary.model.IModelChangeType;
import ch.hsr.modules.uint1.heisenberglibrary.model.Library;
import ch.hsr.modules.uint1.heisenberglibrary.model.ModelChangeTypeEnums;
import ch.hsr.modules.uint1.heisenberglibrary.model.ObservableModelChangeEvent;

/**
 * This dialog holds all opened details views for all books. Each tab represents
 * one {@link BookDO}. If a view for a book is already opened, it will be
 * focused.
 * 
 * @author msyfrig
 */

public class BookDetailJDialog extends AbstractTabbedPaneDialog<BookDO>
        implements Observer {
    private static final long serialVersionUID = 439819991326389792L;

    public BookDetailJDialog(JFrame anOwner) {
        super(anOwner, UiComponentStrings.getString("BookDetailJDialog.title")); //$NON-NLS-1$
    }

    @Override
    protected void initComponents() {
        setMinimumSize(new Dimension(500, 500));
        setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
        initTabbedPane();
        getContentPane().setLayout(new BorderLayout());
        getContentPane().add(tabbedPane, BorderLayout.CENTER);
    }

    /**
     * Opens a new tab for the given book or focuses the tab, if the book has
     * been opened previously.
     * 
     * @param aBookToOpen
     *            the book to open in a tab
     */
    public void openBookTab(BookDO aBookToOpen, Library aLibrary) {
        // check if a tab with the given tab is already open
        AbstractObservableObjectJPanel<BookDO> detailBookPanel = getTabForObject(aBookToOpen);

        // if not open yet, create it and all listeners and actions
        if (detailBookPanel == null) {
            detailBookPanel = new BookDetailJPanel(aBookToOpen, aLibrary);

            String tabTitle = UiComponentStrings
                    .getString("BookDetailJDialog.tab.title.enternewbook.text"); //$NON-NLS-1$
            if (aBookToOpen != null) {
                // add observer to this book so we notice when the title has
                // changed
                addObserverForObservable(aBookToOpen, this);
                tabTitle = getTabTitleForObject(aBookToOpen, false);

                tabbedPane.addTab(tabTitle, null, detailBookPanel,
                        aBookToOpen.toString());
            } else {
                tabbedPane
                        .addTab(tabTitle,
                                null,
                                detailBookPanel,
                                UiComponentStrings
                                        .getString("BookDetailJDialog.tab.title.enternewbook.tooltip")); //$NON-NLS-1$
            }
            // add asteriks in tabtitle if tab has unsaved changes and listen
            // for added books to set the title
            detailBookPanel
                    .addModelStateChangeListener(new BookDetailModelChangeListener());
            openObjectTabList.add(detailBookPanel);
        }
        tabbedPane.setSelectedComponent(detailBookPanel);
        pack();
    }

    @Override
    protected String getTabTitleForObject(BookDO aBook, boolean isDirty) {
        StringBuffer title = new StringBuffer(15);
        String bookTitle = aBook.toString();
        if (isDirty) {
            title.append('*');
            if (bookTitle.length() >= 14) {
                title.append(bookTitle.substring(0, 14));
            } else {
                title.append(bookTitle);
            }
        } else {
            if (bookTitle.length() >= 15) {
                title.append(bookTitle.substring(0, 15));
            } else {
                title.append(bookTitle);
            }
        }
        return title.toString();
    }

    @Override
    public void update(Observable anObservable, Object anArgument) {
        if (anArgument instanceof ObservableModelChangeEvent) {
            ObservableModelChangeEvent modelChange = (ObservableModelChangeEvent) anArgument;
            IModelChangeType type = modelChange.getChangeType();
            if (type == ModelChangeTypeEnums.Book.TITLE
                    || type == ModelChangeTypeEnums.Book.AUTHOR
                    || type == ModelChangeTypeEnums.Book.PUBLISHER
                    || type == ModelChangeTypeEnums.Book.EVERYTHING_CHANGED) {
                objectInTabUpdated((BookDO) anObservable);
            }
        }
    }

    /**
     * Refreshes the tabtitle and tooltip.
     */
    @Override
    protected void objectInTabUpdated(BookDO anUpdatedBook) {
        AbstractObservableObjectJPanel<BookDO> detailBookPanel = getTabForObject(anUpdatedBook);
        if (detailBookPanel != null) {
            int tabIndex = tabbedPane.indexOfComponent(detailBookPanel);
            tabbedPane.setTitleAt(
                    tabIndex,
                    getTabTitleForObject(anUpdatedBook,
                            detailBookPanel.isDirty()));
            tabbedPane.setToolTipTextAt(tabIndex, anUpdatedBook.toString());
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
            IModelStateChangeListener {
        @Override
        public void stateChanged(ModelStateChangeEvent aModelStateChange) {
            if (aModelStateChange.getSource() instanceof BookDetailJPanel) {
                BookDetailJPanel associatedDetailView = (BookDetailJPanel) aModelStateChange
                        .getSource();
                if (associatedDetailView != null) {
                    int tabIndex = tabbedPane
                            .indexOfComponent(associatedDetailView);
                    if (tabIndex >= 0) {
                        switch (aModelStateChange.getState()) {
                            case ModelStateChangeEvent.MODEL_CHANGED_TO_DIRTY:
                            case ModelStateChangeEvent.MODEL_CHANGED_TO_SAVED:
                                objectInTabUpdated(associatedDetailView
                                        .getDisplayedObject());
                                break;
                            case ModelStateChangeEvent.NEW_ENTRY_ADDED:
                                // add observer for newly created book
                                addObserverForObservable(
                                        associatedDetailView
                                                .getDisplayedObject(),
                                        BookDetailJDialog.this);
                                objectInTabUpdated(associatedDetailView
                                        .getDisplayedObject());
                                break;
                            default:
                                break;
                        }
                    }
                }
            }
        }
    }
}
