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
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.util.HashMap;
import java.util.Map;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JFrame;
import javax.swing.JTabbedPane;
import javax.swing.KeyStroke;

import ch.hsr.modules.uint1.heisenberglibrary.domain.book.BookDO;

/**
 * This dialog holds all opened details views for all books. Each tab represents
 * one {@link BookDO}. If a view for a book is already opened, it will be
 * focused.
 * 
 * @author msyfrig
 */
public class BookDetailJDialog extends AbstractDefaultJDialog {
    private static final long             serialVersionUID = 439819991326389792L;
    private JTabbedPane                   tabbedPane;

    private Map<BookDO, BookDetailJPanel> openBookTabMap   = new HashMap<>();

    /**
     * Create the dialog.
     */
    public BookDetailJDialog(JFrame anOwner) {
        super(anOwner, UiComponentStrings.getString("BookDetailJDialog.title")); //$NON-NLS-1$
    }

    @Override
    protected void initComponents() {
        setBounds(100, 100, 900, 400);
        getContentPane().setLayout(new BorderLayout());

        tabbedPane = new JTabbedPane(JTabbedPane.TOP);
        getContentPane().add(tabbedPane, BorderLayout.CENTER);
    }

    /**
     * Opens a new tab for the given book or focuses the tab, if the book has
     * been opened previously.
     * 
     * @param aBookToOpen
     *            the book domain object to open in a tab
     */
    public void openBookTab(BookDO aBookToOpen) {
        BookDetailJPanel detailBookPanel = openBookTabMap.get(aBookToOpen);
        if (detailBookPanel == null) {
            detailBookPanel = new BookDetailJPanel(aBookToOpen);

            // title of the tab consists of the first 15 chars of the title, if
            // the title is too short, 15 first chars from title, author and
            // publisher are used, if this is still too short, we just display
            // the whole BookDO.toString()
            String tabTitle;
            if (aBookToOpen.getTitle().length() >= 15) {
                tabTitle = aBookToOpen.getTitle().substring(0, 15);
            } else if (aBookToOpen.toString().length() >= 15) {
                tabTitle = aBookToOpen.toString().substring(0, 15);
            } else {
                tabTitle = aBookToOpen.toString();
            }
            tabbedPane.addTab(tabTitle, null, detailBookPanel,
                    aBookToOpen.toString());

            Map<KeyStroke, Action> actionMapForBookTab = new HashMap<>(2);
            actionMapForBookTab.put(
                    KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0),
                    new DisposeAction("dispose", detailBookPanel)); //$NON-NLS-1$
            actionMapForBookTab.put(
                    KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, 0),
                    new SaveAction("save", detailBookPanel)); //$NON-NLS-1$
            detailBookPanel.setAncestorActions(actionMapForBookTab);

            openBookTabMap.put(aBookToOpen, detailBookPanel);

        } else {
            tabbedPane.setSelectedComponent(detailBookPanel);
        }
    }

    @Override
    protected boolean save() {
        // TODO probably something to do later, if not delete it
        return false;
    }

    /**
     * Closes an opened tab for a book, removes it from the list of opened books
     * so it can be reopened.
     * 
     * <br> Instance is set to null because it is unknown if the gc would
     * collect it, most likely the panels won't be set to null if we close it
     * and it is not used anymore. TODO set instance to null
     * 
     * @param aBookDetailTabToClose
     *            the book detail tab to close
     */
    private void closeTab(BookDetailJPanel aBookDetailTabToClose) {
        if (aBookDetailTabToClose != null) {
            tabbedPane.remove(aBookDetailTabToClose);
            openBookTabMap.remove(aBookDetailTabToClose.getDisplayedBookDO());
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

}
