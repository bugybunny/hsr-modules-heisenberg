package ch.hsr.modules.uint1.heisenberglibrary.view;

import java.util.Observable;
import java.util.Observer;

import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
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
        setVisible(true);
    }

    private void initComponents() {
        ImageIcon frameIcon = new ImageIcon(
                LibraryMasterJFrame.class.getResource("/images/library.png"));
        System.out.println(LibraryMasterJFrame.class
                .getResource("/images/library.png"));
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

    /*
     * (non-Javadoc)
     * 
     * @see java.util.Observer#update(java.util.Observable, java.lang.Object)
     */
    @Override
    public void update(Observable aO, Object aArg) {

    }
}
