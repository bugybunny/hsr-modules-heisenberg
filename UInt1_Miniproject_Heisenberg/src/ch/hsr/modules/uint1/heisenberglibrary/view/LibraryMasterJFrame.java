package ch.hsr.modules.uint1.heisenberglibrary.view;

import java.awt.AWTKeyStroke;
import java.awt.KeyboardFocusManager;
import java.awt.event.ActionEvent;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.util.HashSet;
import java.util.Observable;
import java.util.Observer;
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

import ch.hsr.modules.uint1.heisenberglibrary.model.Library;

public class LibraryMasterJFrame extends JFrame implements Observer {
    private static final long serialVersionUID = 8186612854405487707L;

    /**
     * The table that displays all different booktypes in the library, not the
     * actual copies.
     */
    private JPanel            contentPanel;
    private JTabbedPane       tabbedPane;
    private JPanel            booksPanel;
    private JPanel            lendingPanel;

    private Library           bookMasterlibrary;

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
        ImageIcon frameIcon = new ImageIcon(
                LibraryMasterJFrame.class.getResource("/images/library.png"));
        setIconImage(frameIcon.getImage());
        setTitle(UiComponentStrings.getString("LibraryMasterJFrame.title")); //$NON-NLS-1$
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 1200, 441);
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
        lendingPanel = new LendingMainJPanel(bookMasterlibrary);
        tabbedPane
                .addTab(UiComponentStrings
                        .getString("LibraryMasterJFrame.tab.lending.text"), null, lendingPanel, null); //$NON-NLS-1$
    }

    private void initHandlers() {
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
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.util.Observer#update(java.util.Observable, java.lang.Object)
     */
    @Override
    public void update(Observable aO, Object aArg) {

    }
}
