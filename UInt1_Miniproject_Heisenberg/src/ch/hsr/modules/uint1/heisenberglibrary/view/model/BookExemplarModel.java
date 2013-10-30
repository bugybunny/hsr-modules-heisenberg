package ch.hsr.modules.uint1.heisenberglibrary.view.model;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

import javax.swing.table.AbstractTableModel;

import ch.hsr.modules.uint1.heisenberglibrary.model.BookDO;
import ch.hsr.modules.uint1.heisenberglibrary.model.Copy;
import ch.hsr.modules.uint1.heisenberglibrary.model.Library;
import ch.hsr.modules.uint1.heisenberglibrary.model.Loan;
import ch.hsr.modules.uint1.heisenberglibrary.view.UiComponentStrings;

/**
 * The tableModel for the jTable in BookDetailJPanel.
 * 
 * @author twinter
 */
public class BookExemplarModel extends AbstractTableModel implements Observer {
    private static final long   serialVersionUID = -1293482132910701521L;
    private static List<String> columnNames      = new ArrayList<>(3);

    static {
        columnNames.add(UiComponentStrings.getString("BookExemplarModel.exemplarTableColumn.id")); //$NON-NLS-1$
        columnNames.add(UiComponentStrings.getString("BookExemplarModel.exemplarTableColumn.availability")); //$NON-NLS-1$
        columnNames.add(UiComponentStrings.getString("BookExemplarModel.exemplarTableColumn.borrowedUntil")); //$NON-NLS-1$
    }

    private Library             exemplarLibrary;
    private BookDO              specificBook;

    /**
     * Creates a new instance of this class.
     * 
     * @param aDisplayedBookDO
     *            the specific book from the DetailPanel
     */
    public BookExemplarModel(BookDO aDisplayedBookDO, Library aLibrary) {
        specificBook = aDisplayedBookDO;
        exemplarLibrary = aLibrary;
    }

    /*
     * (non-Javadoc)
     * 
     * @see javax.swing.table.TableModel#getRowCount()
     */
    @Override
    public int getRowCount() {
        return exemplarLibrary.getCopiesOfBook(specificBook).size();
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
        Copy copyOfSpecificBook = exemplarLibrary.getCopiesOfBook(specificBook)
                .get(aRowIndex);

        List<Loan> loanedBooks = exemplarLibrary
                .getLentCopiesOfBook(specificBook);

        // Copy exemplarOfLoanedBook;
        // boolean loaned = false;

        String availability = UiComponentStrings.getString("BookExemplarModel.copy.isAvailable"); //$NON-NLS-1$
        Object borrowedUntil = UiComponentStrings.getString("BookExemplarModel.copy.date.isAvailable"); //$NON-NLS-1$

        for (int i = 0; i < loanedBooks.size(); i++) {
            if (loanedBooks.get(i).getCopy().getInventoryNumber() == copyOfSpecificBook
                    .getInventoryNumber()) {
                // exemplarOfLoanedBook = loanedBooks.get(i).getCopy();
                // loaned = true;
                availability = UiComponentStrings.getString("BookExemplarModel.copy.notAvailable"); //$NON-NLS-1$

                GregorianCalendar dueDate = loanedBooks.get(i).getPickupDate();
                dueDate.add(GregorianCalendar.DAY_OF_YEAR, loanedBooks.get(i)
                        .getDaysOfLoanDuration());

                DateFormat df = new SimpleDateFormat(UiComponentStrings.getString("BookExemplarModel.dateformat")); //$NON-NLS-1$

                borrowedUntil = df.format(dueDate.getTime());
            }
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
