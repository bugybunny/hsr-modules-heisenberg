package ch.hsr.modules.uint1.heisenberglibrary.view.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

import ch.hsr.modules.uint1.heisenberglibrary.model.BookDO;
import ch.hsr.modules.uint1.heisenberglibrary.model.IModelChangeType;
import ch.hsr.modules.uint1.heisenberglibrary.model.Library;
import ch.hsr.modules.uint1.heisenberglibrary.model.ModelChangeTypeEnums;
import ch.hsr.modules.uint1.heisenberglibrary.model.ObservableModelChangeEvent;
import ch.hsr.modules.uint1.heisenberglibrary.view.UiComponentStrings;

/**
 * Lists all loans in a JTable with columns: Status, Exemplar #, booktitle, lent
 * until, lent datum.
 * 
 * @author msyfrig
 */
public class BookTableModel extends AbstractExtendendedEventTableModel<BookDO>
        implements Observer {
    private static final long   serialVersionUID = 4449419618706874102L;
    private static List<String> columnNames      = new ArrayList<>(5);

    static {
        columnNames.add(UiComponentStrings
                .getString("BookTableModel.bookTableColumn.available"));  //$NON-NLS-1$
        columnNames.add(UiComponentStrings
                .getString("BookTableModel.bookTableColumn.title")); //$NON-NLS-1$
        columnNames.add(UiComponentStrings
                .getString("BookTableModel.bookTableColumn.author"));  //$NON-NLS-1$
        columnNames.add(UiComponentStrings
                .getString("BookTableModel.bookTableColumn.publisher"));  //$NON-NLS-1$
    }
    private Library             library;

    public BookTableModel(Library aLibrary) {
        super(aLibrary.getBooks());
        library = aLibrary;
        addObserverForObservable(library, this);
    }

    @Override
    public int getColumnCount() {
        return columnNames.size();
    }

    /**
     * First column is not editable.
     */
    @Override
    public boolean isCellEditable(int aRowIndex, int aColumnIndex) {
        return aColumnIndex != 0;
    }

    @Override
    public Object getValueAt(int aRowIndex, int aColumnIndex) {
        BookDO book = data.get(aRowIndex);
        Object ret = null;

        switch (aColumnIndex) {
            case 0:
                ret = Integer.valueOf(library.getAvailableCopiesForBook(book)
                        .size());
                break;
            case 1:
                ret = book.getTitle();
                break;
            case 2:
                ret = book.getAuthor();
                break;
            case 3:
                ret = book.getPublisher();
                break;
            default:
                ret = "";
        }
        return ret;
    }

    @Override
    public void setValueAt(Object value, int aRowIndex, int aColumnIndex) {
        if (!value.toString().isEmpty()) {
            BookDO book = data.get(aRowIndex);
            switch (aColumnIndex) {
                case 1:
                    book.setTitle((String) value);
                    break;
                case 2:
                    book.setAuthor((String) value);
                    break;
                case 3:
                    book.setPublisher((String) value);
                    break;
                default:
                    // do nothing
            }
        }
        updateTableData();
    }

    @Override
    public void update(Observable anObservable, Object anArgument) {
        if (anArgument instanceof ObservableModelChangeEvent) {
            final ObservableModelChangeEvent modelChange = (ObservableModelChangeEvent) anArgument;
            IModelChangeType type = modelChange.getChangeType();
            if (type == ModelChangeTypeEnums.Book.ADDED) {
                addObserverForObservable(
                        (Observable) modelChange.getNewValue(), this);
            }
        }

        super.update(anObservable, anArgument);
    }

    @Override
    public String getColumnName(int aColumn) {
        return columnNames.get(aColumn);
    }

    @Override
    public Class<?> getColumnClass(int aColumnIndex) {
        Class<?> ret = String.class;
        switch (aColumnIndex) {
            case 0:
                ret = Integer.class;
                break;
            case 1:
            case 2:
            case 3:
                ret = String.class;
                break;
            default:
                ret = String.class;
                break;
        }
        return ret;
    }
}
