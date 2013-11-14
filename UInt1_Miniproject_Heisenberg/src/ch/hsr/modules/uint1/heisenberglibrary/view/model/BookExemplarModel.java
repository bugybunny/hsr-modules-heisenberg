package ch.hsr.modules.uint1.heisenberglibrary.view.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

import javax.swing.table.AbstractTableModel;

import ch.hsr.modules.uint1.heisenberglibrary.model.BookDO;
import ch.hsr.modules.uint1.heisenberglibrary.model.Copy;
import ch.hsr.modules.uint1.heisenberglibrary.model.Library;
import ch.hsr.modules.uint1.heisenberglibrary.model.Loan;
import ch.hsr.modules.uint1.heisenberglibrary.view.UiComponentStrings;
import ch.hsr.modules.uint1.heisenberglibrary.view.util.DateFormatterUtil;

/**
 * The tableModel for the jTable in BookDetailJPanel.
 * 
 * @author twinter
 */
public class BookExemplarModel extends AbstractTableModel implements Observer {
    private static final long   serialVersionUID = -1293482132910701521L;
    private static List<String> columnNames      = new ArrayList<>(3);

    static {
        columnNames.add(UiComponentStrings
                .getString("BookExemplarModel.exemplarTableColumn.id")); //$NON-NLS-1$
        columnNames
                .add(UiComponentStrings
                        .getString("BookExemplarModel.exemplarTableColumn.availability")); //$NON-NLS-1$
        columnNames
                .add(UiComponentStrings
                        .getString("BookExemplarModel.exemplarTableColumn.borrowedUntil")); //$NON-NLS-1$
    }

    private List<Copy>          copyList;
    private Library             library;
    private BookDO              specificBook;

    /**
     * Creates a new instance of this class.
     * 
     * @param aDisplayedBookDO
     *            the specific book from the DetailPanel
     */
    public BookExemplarModel(BookDO aDisplayedBookDO, Library aLibrary) {
        library = aLibrary;
        specificBook = aDisplayedBookDO;
        copyList = library.getCopiesOfBook(aDisplayedBookDO);
    }

    /*
     * (non-Javadoc)
     * 
     * @see javax.swing.table.TableModel#getRowCount()
     */
    @Override
    public int getRowCount() {
        return copyList.size();
    }

    /*
     * (non-Javadoc)
     * 
     * @see javax.swing.table.TableModel#getColumnCount()
     */
    @Override
    public int getColumnCount() {
        return columnNames.size();
    }

    /*
     * (non-Javadoc)
     * 
     * @see javax.swing.table.TableModel#getValueAt(int, int)
     */
    @Override
    public Object getValueAt(int aRowIndex, int aColumnIndex) {
        Copy copyOfSpecificBook = copyList.get(aRowIndex);

        String availability = "available";
        if (library.isCopyLent(copyOfSpecificBook)) {
            availability = "unavailable";
        }

        Object borrowedUntil = UiComponentStrings
                .getString("BookExemplarModel.copy.date.isAvailable");

        Loan tempLoan = library.getActiveLoanForCopy(copyOfSpecificBook);
        if (tempLoan != null) {
            borrowedUntil = DateFormatterUtil.getFormattedDate(tempLoan
                    .getDueDate());
        }

        Object ret = null;

        switch (aColumnIndex) {
            case 0:
                ret = Long.valueOf(copyOfSpecificBook.getInventoryNumber());
                break;
            case 1:
                ret = availability;
                break;
            case 2:
                ret = borrowedUntil;
                break;
            default:
                ret = UiComponentStrings.getString("empty"); //$NON-NLS-1$ 
        }
        return ret;
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.util.Observer#update(java.util.Observable, java.lang.Object)
     */
    @Override
    public void update(Observable anObservable, Object anArgument) {
        // TODO Auto-generated method stub

    }

    /*
     * (non-Javadoc)
     * 
     * @see javax.swing.table.AbstractTableModel#getColumnName(int)
     */
    @Override
    public String getColumnName(int aColumnIndex) {
        return columnNames.get(aColumnIndex);
    }

}
