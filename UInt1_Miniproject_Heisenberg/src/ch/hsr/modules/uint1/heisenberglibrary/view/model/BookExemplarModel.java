package ch.hsr.modules.uint1.heisenberglibrary.view.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

import javax.swing.table.AbstractTableModel;

import ch.hsr.modules.uint1.heisenberglibrary.model.BookDO;
import ch.hsr.modules.uint1.heisenberglibrary.model.Copy;
import ch.hsr.modules.uint1.heisenberglibrary.model.Library;
import ch.hsr.modules.uint1.heisenberglibrary.view.UiComponentStrings;

/**
 * The tableModel for the jTable in BookDetailJPanel.
 *
 * @author twinter
 */

public class BookExemplarModel extends AbstractTableModel implements Observer {
    private static final long serialVersionUID = -1293482132910701521L;
    private static List<String> columnNames      = new ArrayList<>(3);

    static {
        columnNames.add(UiComponentStrings
                .getString("Exemplar ID")); 
        columnNames.add(UiComponentStrings
                .getString("Availability fdddf")); 
        columnNames.add(UiComponentStrings
                .getString("Borrowed until"));  
    }
    
    private Library             exemplarLibrary;
    private BookDO              specificBook;

    
    /**
     * Creates a new instance of this class.
     * 
     * @param aDisplayedBookDO
     *            the specific book from the DetailPanel
     */
    public BookExemplarModel(BookDO aDisplayedBookDO, Library library) {
        specificBook = aDisplayedBookDO;
        exemplarLibrary = library;
    }    
 
    
    /* (non-Javadoc)
     * @see javax.swing.table.TableModel#getRowCount()
     */
    @Override
    public int getRowCount() {
       return exemplarLibrary.getCopiesOfBook(specificBook).size();
    }

    /* (non-Javadoc)
     * @see javax.swing.table.TableModel#getColumnCount()
     */
    @Override
    public int getColumnCount() {
        return columnNames.size();
    }

    /* (non-Javadoc)
     * @see javax.swing.table.TableModel#getValueAt(int, int)
     */
    @Override
    public Object getValueAt(int aRowIndex, int aColumnIndex) {
        Copy copyOfSpecificBook = exemplarLibrary.getCopiesOfBook(specificBook).get(aRowIndex);
        
        Object ret = null;

        switch (aColumnIndex) {
            case 0:
                ret = copyOfSpecificBook.getInventoryNumber();
                break;
            case 1:
                ret = "availability";
                break;
            case 2:
                ret = "borrowed until";
                break;
            default:
                ret = "blubb error";
        }
        return ret;
    }

    /* (non-Javadoc)
     * @see java.util.Observer#update(java.util.Observable, java.lang.Object)
     */
    @Override
    public void update(Observable aO, Object aArg) {
        // TODO Auto-generated method stub
        
    }
    
    /*
     * (non-Javadoc)
     * 
     * @see javax.swing.table.AbstractTableModel#getColumnName(int)
     */
    @Override
    public String getColumnName(int aColumn) {
        return columnNames.get(aColumn);
    }

}
