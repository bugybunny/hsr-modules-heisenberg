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
import ch.hsr.modules.uint1.heisenberglibrary.view.util.DateUtil;

/**
 * The tableModel for the jTable in BookDetailJPanel.
 * 
 * @author twinter
 */
public class BookCopyModel extends AbstractTableModel implements Observer {
    private static final long   serialVersionUID = -1293482132910701521L;
    private static List<String> columnNames      = new ArrayList<>(3);

    static {
        columnNames.add(UiComponentStrings
                .getString("BookCopyModel.copyTableColumn.id")); //$NON-NLS-1$
        columnNames.add(UiComponentStrings
                .getString("BookCopyModel.copyTableColumn.availability")); //$NON-NLS-1$
        columnNames.add(UiComponentStrings
                .getString("BookCopyModel.copyTableColumn.borrowedUntil")); //$NON-NLS-1$
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
    public BookCopyModel(BookDO aDisplayedBookDO, Library aLibrary) {
        library = aLibrary;
        specificBook = aDisplayedBookDO;
        copyList = library.getCopiesOfBook(aDisplayedBookDO);
    }

    @Override
    public int getRowCount() {
        return copyList.size();
    }

    @Override
    public int getColumnCount() {
        return columnNames.size();
    }

    @Override
    public Object getValueAt(int aRowIndex, int aColumnIndex) {
        Copy copyOfSpecificBook = copyList.get(aRowIndex);

        String availability = "available";
        if (library.isCopyLent(copyOfSpecificBook)) {
            availability = "unavailable";
        }

        Object borrowedUntil = UiComponentStrings
                .getString("BookCopyModel.copy.date.isAvailable");

        Loan tempLoan = library.getActiveLoanForCopy(copyOfSpecificBook);
        if (tempLoan != null) {
            borrowedUntil = DateUtil.getFormattedDate(tempLoan.getDueDate());
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

    @Override
    public void update(Observable anObservable, Object anArgument) {
        // TODO Auto-generated method stub

    }

    @Override
    public String getColumnName(int aColumnIndex) {
        return columnNames.get(aColumnIndex);
    }

    @Override
    public void fireTableDataChanged() {
        copyList = library.getCopiesOfBook(specificBook);
        super.fireTableDataChanged();
    }

}
