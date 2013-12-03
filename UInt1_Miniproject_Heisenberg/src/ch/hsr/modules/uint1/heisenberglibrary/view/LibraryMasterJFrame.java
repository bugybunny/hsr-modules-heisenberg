package ch.hsr.modules.uint1.heisenberglibrary.view;

import java.awt.AWTKeyStroke;
import java.awt.Dimension;
import java.awt.KeyboardFocusManager;
import java.awt.event.ActionEvent;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.util.HashSet;
import java.util.Set;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.KeyStroke;
import javax.swing.border.EmptyBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import ch.hsr.modules.uint1.heisenberglibrary.model.BookDO;
import ch.hsr.modules.uint1.heisenberglibrary.model.Library;
import ch.hsr.modules.uint1.heisenberglibrary.model.Loan;

/**
 * 
 * @author msyfrig
 */
public class LibraryMasterJFrame extends JFrame {
    private static final long                     serialVersionUID = 8186612854405487707L;

    /**
     * The table that displays all different booktypes in the library, not the
     * actual copies.
     */
    private JPanel                                contentPanel;
    private JTabbedPane                           tabbedPane;
    private AbstractSearchableTableJPanel<BookDO> booksPanel;
    private AbstractSearchableTableJPanel<Loan>   loanPanel;

    private Library                               bookMasterlibrary;

    /**
     * Creates the frame.
     * 
     * @param aBooks
     */
    public LibraryMasterJFrame(Library library) {
        bookMasterlibrary = library;

        initComponents();
        initHandlers();
        setVisible(true);
    }

    private void initComponents() {
        setMinimumSize(new Dimension(650, 400));
        ImageIcon frameIcon = new ImageIcon(
                LibraryMasterJFrame.class.getResource(UiComponentStrings
                        .getString("LibraryMasterJFrame.icon.user"))); //$NON-NLS-1$
        setIconImage(frameIcon.getImage());
        setTitle(UiComponentStrings.getString("LibraryMasterJFrame.title")); //$NON-NLS-1$
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 1200, 600);
        contentPanel = new JPanel();
        contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
        setContentPane(contentPanel);
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.X_AXIS));

        tabbedPane = new JTabbedPane(JTabbedPane.TOP);
        contentPanel.add(tabbedPane);

        booksPanel = new BookMainJPanel(bookMasterlibrary);
        tabbedPane
                .addTab(UiComponentStrings
                        .getString("LibraryMasterJFrame.tab.books.text"), null, booksPanel, null); //$NON-NLS-1$
        booksPanel.setLayout(new BoxLayout(booksPanel, BoxLayout.Y_AXIS));
        loanPanel = new LoanMainJPanel(bookMasterlibrary);
        tabbedPane
                .addTab(UiComponentStrings
                        .getString("LibraryMasterJFrame.tab.loan.text"), null, loanPanel, null); //$NON-NLS-1$
    }

    private void initHandlers() {
        getRootPane().setDefaultButton(
                ((AbstractSearchableTableJPanel<?>) tabbedPane
                        .getSelectedComponent()).getDefaultButton());
        tabbedPane.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent aStateChangedEvent) {
                getRootPane().setDefaultButton(
                        ((AbstractSearchableTableJPanel<?>) tabbedPane
                                .getSelectedComponent()).getDefaultButton());
            }
        });

        KeyStroke ctrlTab = KeyStroke.getKeyStroke(KeyEvent.VK_TAB,
                InputEvent.CTRL_DOWN_MASK);
        KeyStroke ctrlShiftTab = KeyStroke.getKeyStroke(KeyEvent.VK_TAB,
                InputEvent.CTRL_DOWN_MASK | InputEvent.SHIFT_DOWN_MASK);

        // Remove ctrl-tab from normal focus traversal
        Set<AWTKeyStroke> forwardKeys = new HashSet<>(
                tabbedPane
                        .getFocusTraversalKeys(KeyboardFocusManager.FORWARD_TRAVERSAL_KEYS));
        forwardKeys.remove(ctrlTab);
        tabbedPane.setFocusTraversalKeys(
                KeyboardFocusManager.FORWARD_TRAVERSAL_KEYS, forwardKeys);

        // Remove ctrl-shift-tab from normal focus traversal
        Set<AWTKeyStroke> backwardKeys = new HashSet<>(
                tabbedPane
                        .getFocusTraversalKeys(KeyboardFocusManager.BACKWARD_TRAVERSAL_KEYS));
        backwardKeys.remove(ctrlShiftTab);
        tabbedPane.setFocusTraversalKeys(
                KeyboardFocusManager.BACKWARD_TRAVERSAL_KEYS, backwardKeys);

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
        getRootPane()
                .getInputMap(JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT)
                .put(ctrlShiftTab,
                        navigateToNextTabAction.getValue(Action.NAME));
        getRootPane().getActionMap().put(
                navigateToNextTabAction.getValue(Action.NAME),
                navigateToNextTabAction);

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

        getRootPane()
                .getInputMap(JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT)
                .put(ctrlShiftTab,
                        navigateToPreviousTabAction.getValue(Action.NAME));
        getRootPane().getActionMap().put(
                navigateToPreviousTabAction.getValue(Action.NAME),
                navigateToPreviousTabAction);

        /*
         * cltr+f for all subpanels is implemented here (and also the following)
         * because after switching the tab for the first time, the focus is on
         * the tabbedpane and ctrl+f does nothing when we add it just to the
         * subpanels and all its ancestors. and getting the parent components
         * from both tabs and add all actions to that input/action map would be
         * really bad code
         */
        // ctrl+f: switch focus to searchfield for table
        Action searchAction = new AbstractAction("search") { //$NON-NLS-1$
            private static final long serialVersionUID = -6626318103198277780L;

            @Override
            public void actionPerformed(ActionEvent anActionEvent) {
                getSelectedTab().searchFieldRequestFocus();
            }
        };

        KeyStroke ctrlF = KeyStroke.getKeyStroke(KeyEvent.VK_F,
                InputEvent.CTRL_DOWN_MASK);
        getRootPane()
                .getInputMap(JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT)
                .put(ctrlF, searchAction.getValue(Action.NAME));
        getRootPane().getActionMap().put(searchAction.getValue(Action.NAME),
                searchAction);

        // ctrl+n: add book for Mac OS x users since they don't have mnemonics
        Action newAction = new AbstractAction("new") { //$NON-NLS-1$
            private static final long serialVersionUID = -3181581596036016373L;

            @Override
            public void actionPerformed(ActionEvent anActionEvent) {
                getSelectedTab().createNew();
            }
        };

        KeyStroke ctrlN = KeyStroke.getKeyStroke(KeyEvent.VK_N,
                InputEvent.CTRL_DOWN_MASK);
        getRootPane()
                .getInputMap(JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT)
                .put(ctrlN, newAction.getValue(Action.NAME));
        getRootPane().getActionMap().put(newAction.getValue(Action.NAME),
                newAction);

        // ctrl+e/alt+e: set focus to table
        Action focusTableAction = new AbstractAction("focusTableAction") { //$NON-NLS-1$
            private static final long serialVersionUID = -3181581596036016373L;

            @Override
            public void actionPerformed(ActionEvent anActionEvent) {
                getSelectedTab().tableRequestFocus();
            }
        };

        KeyStroke altE = KeyStroke.getKeyStroke(KeyEvent.VK_E,
                InputEvent.ALT_DOWN_MASK);
        getRootPane()
                .getInputMap(JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT)
                .put(altE, focusTableAction.getValue(Action.NAME));
        KeyStroke ctrlE = KeyStroke.getKeyStroke(KeyEvent.VK_E,
                InputEvent.CTRL_DOWN_MASK);
        getRootPane()
                .getInputMap(JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT)
                .put(ctrlE, focusTableAction.getValue(Action.NAME));
        getRootPane().getActionMap().put(
                focusTableAction.getValue(Action.NAME), focusTableAction);

        // alt+c: check/uncheck combobox
        Action focusCheckBoxAction = new AbstractAction("focusCheckBoxAction") { //$NON-NLS-1$
            private static final long serialVersionUID = -3181581596036016373L;

            @Override
            public void actionPerformed(ActionEvent anActionEvent) {
                getSelectedTab().setSelectedCheckBox();
            }
        };

        KeyStroke altC = KeyStroke.getKeyStroke(KeyEvent.VK_C,
                InputEvent.ALT_DOWN_MASK);
        getRootPane()
                .getInputMap(JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT)
                .put(altC, focusCheckBoxAction.getValue(Action.NAME));
        getRootPane().getActionMap().put(
                focusCheckBoxAction.getValue(Action.NAME), focusCheckBoxAction);
    }

    private AbstractSearchableTableJPanel<?> getSelectedTab() {
        return ((AbstractSearchableTableJPanel<?>) tabbedPane
                .getSelectedComponent());
    }
}
