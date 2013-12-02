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

import java.awt.AWTKeyStroke;
import java.awt.Dialog;
import java.awt.Frame;
import java.awt.KeyboardFocusManager;
import java.awt.event.ActionEvent;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Observer;
import java.util.Set;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JOptionPane;
import javax.swing.JTabbedPane;
import javax.swing.KeyStroke;

import ch.hsr.modules.uint1.heisenberglibrary.model.ObservableObject;

/**
 * 
 * @author msyfrig
 */
public abstract class AbstractTabbedPaneDialog<M extends ObservableObject>
        extends NonModalJDialog implements Observer {
    private static final long                         serialVersionUID  = 6510505636405014546L;
    protected JTabbedPane                             tabbedPane;

    /**
     * The list of all opened book detailviews. To check wheter a
     * {@link ch.hsr.modules.uint1.heisenberglibrary.model.BookDO} is already
     * open an iteration over all items is necessary and then call
     * {@link BookDetailJPanel#getDisplayedBookDO()} and compare it is
     * necessary.
     * 
     * <br> And if you ask yourself why this is not implemented as a map with
     * the book as key and the opened tab as value: the behavior for mutated
     * keys is undefined and since we can modify the book it would not be
     * possible to ever delete this book instance. See javadoc of map for
     * undefined behavior for mutable objects as keys.
     * 
     * @see java.util.Map
     */
    protected List<AbstractObservableObjectJPanel<M>> openObjectTabList = new ArrayList<>();

    public AbstractTabbedPaneDialog(Frame anOwner, String aTitle) {
        super(anOwner, aTitle);
    }

    public AbstractTabbedPaneDialog(Dialog anOwner, String aTitle) {
        super(anOwner, aTitle);
    }

    protected void initTabbedPane() {
        tabbedPane = new JTabbedPane(JTabbedPane.TOP,
                JTabbedPane.SCROLL_TAB_LAYOUT);
        addTabbedPaneHandlers();
    }

    @Override
    protected void initHandlers() {
        setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent aWindowClosingEvent) {
                List<AbstractObservableObjectJPanel<M>> tabsToClose = new ArrayList<>(
                        openObjectTabList.size());
                for (int i = 0; i < openObjectTabList.size(); i++) {
                    tabsToClose.add(openObjectTabList.get(i));
                }

                while (!tabsToClose.isEmpty()) {
                    closeTab(tabsToClose.get(0));
                    tabsToClose.remove(0);
                }
            }

            @Override
            public void windowClosed(WindowEvent aWindowClosedEvent) {
                if (openObjectTabList.isEmpty()) {
                    tabbedPane.removeAll();
                    setVisible(false);
                }
            }
        });
    }

    private void addTabbedPaneHandlers() {
        addComponentListener(new ComponentAdapter() {
            /**
             * All opened book tabs are closed before hiding this dialog so they
             * are not still open if the user selects a new book and expects to
             * open a completely new dialog.
             */
            @Override
            public void componentHidden(ComponentEvent aComponentHiddenEvent) {

            }

        });

        KeyStroke ctrlTab = KeyStroke.getKeyStroke(KeyEvent.VK_TAB,
                InputEvent.CTRL_DOWN_MASK);
        KeyStroke ctrlShiftTab = KeyStroke.getKeyStroke(KeyEvent.VK_TAB,
                InputEvent.CTRL_DOWN_MASK | InputEvent.SHIFT_DOWN_MASK);
        removeExistingSwitchingBehaviour(ctrlTab, ctrlShiftTab);
        addTabSwitchingInputBehaviour(ctrlTab, ctrlShiftTab);
    }

    private void removeExistingSwitchingBehaviour(
            KeyStroke anExistingForwardTrigger,
            KeyStroke anExistingBackwardTrigger) {
        // Remove ctrl-tab from normal focus traversal
        Set<AWTKeyStroke> forwardKeys = new HashSet<>(
                tabbedPane
                        .getFocusTraversalKeys(KeyboardFocusManager.FORWARD_TRAVERSAL_KEYS));
        forwardKeys.remove(anExistingForwardTrigger);
        tabbedPane.setFocusTraversalKeys(
                KeyboardFocusManager.FORWARD_TRAVERSAL_KEYS, forwardKeys);

        // Remove ctrl-shift-tab from normal focus traversal
        Set<AWTKeyStroke> backwardKeys = new HashSet<>(
                tabbedPane
                        .getFocusTraversalKeys(KeyboardFocusManager.BACKWARD_TRAVERSAL_KEYS));
        backwardKeys.remove(anExistingBackwardTrigger);
        tabbedPane.setFocusTraversalKeys(
                KeyboardFocusManager.BACKWARD_TRAVERSAL_KEYS, backwardKeys);
    }

    protected void addTabSwitchingInputBehaviour(KeyStroke aForwardTrigger,
            KeyStroke aBackwardTrigger) {

        // ctrl+tab: switch to next tab
        Action navigateToNextTabAction = new AbstractAction("navigateToNextTab") { //$NON-NLS-1$
            private static final long serialVersionUID = -6626318103198277780L;

            @Override
            public void actionPerformed(ActionEvent anActionEvent) {
                int newSelectedTabIndex = tabbedPane.getSelectedIndex() + 1;
                if (newSelectedTabIndex > tabbedPane.getTabCount() - 1) {
                    newSelectedTabIndex = 0;
                }
                tabbedPane.setSelectedIndex(newSelectedTabIndex);
            }
        };

        addAncestorAction(aForwardTrigger, navigateToNextTabAction);

        // ctrl+shift+tab: switch to previous tab
        Action navigateToPreviousTabAction = new AbstractAction(
                "navigateToPreviousTab") { //$NON-NLS-1$
            private static final long serialVersionUID = -6626318103198277780L;

            @Override
            public void actionPerformed(ActionEvent anActionEvent) {
                int newSelectedTabIndex = tabbedPane.getSelectedIndex() - 1;
                if (newSelectedTabIndex < 0) {
                    newSelectedTabIndex = tabbedPane.getTabCount() - 1;
                }
                tabbedPane.setSelectedIndex(newSelectedTabIndex);
            }
        };
        addAncestorAction(aBackwardTrigger, navigateToPreviousTabAction);
    }

    protected void addAncestorAction(KeyStroke anInputTrigger, Action anAction) {
        getRootPane()
                .getInputMap(JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT)
                .put(anInputTrigger, anAction.getValue(Action.NAME));
        getRootPane().getActionMap().put(anAction.getValue(Action.NAME),
                anAction);
    }

    protected abstract String getTabTitleForObject(M anObject, boolean isDirty);

    protected abstract void objectInTabUpdated(M anUpdatedObject);

    protected void addHandlersToTab(AbstractObservableObjectJPanel<M> aTab) {
        Map<KeyStroke, Action> actionMapForBookTab = new HashMap<>(2);
        actionMapForBookTab.put(KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0),
                new DisposeAction("dispose", aTab)); //$NON-NLS-1$
        actionMapForBookTab.put(KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, 0),
                new SaveAction("save", aTab)); //$NON-NLS-1$
        aTab.addAncestorActions(actionMapForBookTab);
    }

    protected AbstractObservableObjectJPanel<M> getTabForObject(M anObject) {
        AbstractObservableObjectJPanel<M> detailBookPanel = null;
        if (anObject != null) {
            for (AbstractObservableObjectJPanel<M> tempBookDetailView : openObjectTabList) {
                if (tempBookDetailView.getDisplayedObject() == anObject) {
                    detailBookPanel = tempBookDetailView;
                }
            }
        }
        return detailBookPanel;
    }

    /**
     * Closes an opened tab for an object, removes it from the list of opened
     * object so it can be reopened. If the closed tab was the last one, this
     * dialog is closed.
     * 
     * @param anObjectTabToClose
     *            the object tab to close
     */
    protected boolean closeTab(
            AbstractObservableObjectJPanel<M> anObjectTabToClose) {
        tabbedPane.setSelectedComponent(anObjectTabToClose);
        boolean closed = false;
        if (anObjectTabToClose != null) {
            if (anObjectTabToClose.isDirty()) {
                if (anObjectTabToClose.hasValidContent()) {
                    handleDirtyValidTabClosing(anObjectTabToClose);
                } else {
                    handleDirtyInvalidTabClosing(anObjectTabToClose);
                }
            } else {
                closed = closeTabNow(anObjectTabToClose);
            }
        }
        return closed;
    }

    private boolean handleDirtyValidTabClosing(
            AbstractObservableObjectJPanel<M> aDirtyValideTabToClose) {
        boolean closed = false;
        Object[] options = {
                UiComponentStrings
                        .getString("AbstractTabbedPaneDialog.optionpane.dirtypanel.option.save"), //$NON-NLS-1$
                UiComponentStrings
                        .getString("AbstractTabbedPaneDialog.optionpane.dirtypanel.option.discard"), //$NON-NLS-1$
                UiComponentStrings
                        .getString("AbstractTabbedPaneDialog.optionpane.dirtypanel.option.cancel") }; //$NON-NLS-1$

        String message = MessageFormat
                .format(UiComponentStrings
                        .getString("AbstractTabbedPaneDialog.optionpane.dirtypanel.message"), //$NON-NLS-1$
                        aDirtyValideTabToClose.getDisplayedObject());

        int chosenSaveOption = JOptionPane
                .showOptionDialog(
                        this,
                        message,
                        UiComponentStrings
                                .getString("AbstractTabbedPaneDialog.optionpane.dirtypanel.option.title"), //$NON-NLS-1$
                        JOptionPane.YES_NO_CANCEL_OPTION,
                        JOptionPane.QUESTION_MESSAGE, null, options, options[2]);

        switch (chosenSaveOption) {
            case 0:
                aDirtyValideTabToClose.save();
                closed = closeTabNow(aDirtyValideTabToClose);
                break;
            case 1:
                closed = closeTabNow(aDirtyValideTabToClose);
                break;
            default:
                // do nothing
                break;
        }
        return closed;
    }

    private boolean handleDirtyInvalidTabClosing(
            AbstractObservableObjectJPanel<M> aDirtyInvalidTabToClose) {
        boolean closed = false;
        Object[] options = {
                UiComponentStrings
                        .getString("AbstractTabbedPaneDialog.optionpane.dirtypanel.option.discard"), //$NON-NLS-1$
                UiComponentStrings
                        .getString("AbstractTabbedPaneDialog.optionpane.dirtypanel.option.cancel") }; //$NON-NLS-1$

        int chosenSaveOption = JOptionPane
                .showOptionDialog(
                        this,
                        UiComponentStrings
                                .getString("AbstractTabbedPaneDialog.optionpane.dirtyinvalidpanel.message"), //$NON-NLS-1$
                        UiComponentStrings
                                .getString("AbstractTabbedPaneDialog.optionpane.dirtyinvalidpanel.option.title"), //$NON-NLS-1$
                        JOptionPane.YES_NO_OPTION,
                        JOptionPane.QUESTION_MESSAGE, null, options, options[1]);

        if (chosenSaveOption == 0) {
            closed = closeTabNow(aDirtyInvalidTabToClose);
        }
        return closed;
    }

    private boolean closeTabNow(
            AbstractObservableObjectJPanel<M> anObjectTabToClose) {
        boolean closed = false;
        anObjectTabToClose.cleanUpBeforeDispose();
        tabbedPane.remove(anObjectTabToClose);
        openObjectTabList.remove(anObjectTabToClose);
        closed = true;
        // close this dialog if this was the last open tab
        if (openObjectTabList.isEmpty()) {
            dispose();
        }
        return closed;
    }

    /**
     * Action to handle the save actions. Saves all unsaved changes and closes
     * the tab.
     * 
     * @author msyfrig
     */
    protected class SaveAction extends AbstractAction {
        private static final long                 serialVersionUID = -4275362945903839390L;
        private AbstractObservableObjectJPanel<M> objectTab;

        protected SaveAction(String anActionName,
                AbstractObservableObjectJPanel<M> anAssociatedTab) {
            super(anActionName);
            objectTab = anAssociatedTab;
        }

        /**
         * Call {@link #save()} and close the tab.
         */
        @Override
        public void actionPerformed(ActionEvent anActionEvent) {
            if (objectTab.save()) {
                closeTab(objectTab);
            }
        }
    }

    /**
     * Action to handle the closing of a tab. Informs the user if there are
     * unsaved changes in the tab before closing.
     * 
     * @author msyfrig
     */
    protected class DisposeAction extends AbstractAction {
        private static final long                 serialVersionUID = 2752048542262499446L;
        private AbstractObservableObjectJPanel<M> objectTab;

        /**
         * Creates a new action with the given name and the associated book tab
         * in which this
         */
        protected DisposeAction(String anActionName,
                AbstractObservableObjectJPanel<M> anAssociatedTab) {
            super(anActionName);
            objectTab = anAssociatedTab;
        }

        /**
         * Checks if the tab has unsaved changes and informs the user if so and
         * closes the tab.
         */
        @Override
        public void actionPerformed(ActionEvent anActionEvent) {
            closeTab(objectTab);
        }
    }
}
