package ch.hsr.modules.uint1.heisenberglibrary.domain;

import java.util.List;

import javax.swing.table.AbstractTableModel;

import ch.hsr.modules.uint1.heisenberglibrary.domain.book.BookDO;
import ch.hsr.modules.uint1.heisenberglibrary.view.UiComponentStrings;

/**
 * Lists all Books in a jTable with Rows: Available, Title, Author and
 * Publisher.
 * 
 * @author twinter
 */
public class BookTableModel extends AbstractTableModel {

	private static final long serialVersionUID = 4449419618706874102L;
	private String[] columnNames = {
			UiComponentStrings
					.getString("BookTableModel.bookTableColumn.available"),  //$NON-NLS-1$
					UiComponentStrings.getString("BookTableModel.bookTableColumn.title"), //$NON-NLS-1$
					UiComponentStrings.getString("BookTableModel.bookTableColumn.author"),  //$NON-NLS-1$
					UiComponentStrings.getString("BookTableModel.bookTableColumn.publisher") };  //$NON-NLS-1$
	// private Object[][] data = {{}}; //todo
	// Test
	private List<BookDO> data;

	public BookTableModel(List<BookDO> someBooks) {
		data = someBooks;
	}

	public void getBooksForTable() {

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.swing.table.TableModel#getRowCount()
	 */
	@Override
	public int getRowCount() {
		return data.size();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.swing.table.TableModel#getColumnCount()
	 */
	@Override
	public int getColumnCount() {
		// TODO Auto-generated method stub
		return columnNames.length;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.swing.table.TableModel#getValueAt(int, int)
	 */

	// TODO: specific books with availability
	@Override
	public Object getValueAt(int aRowIndex, int aColumnIndex) {
		BookDO book = data.get(aRowIndex);

		switch (aColumnIndex) {
		case 0:
			return "1 (todo)"; //$NON-NLS-1$
		case 1:
			return book.getTitle();
		case 2:
			return book.getAuthor();
		case 3:
			return book.getPublisher();
		}

		return data;
	}

	public void setValueAt(Object value, int aRowIndex, int aColumnIndex) {
		BookDO book = data.get(aRowIndex);

		switch (aColumnIndex) {
		case 0:
			break; // TODO: availability
		case 1:
			book.setTitle((String) value);
		case 2:
			book.setAuthor((String) value);
		case 3:
			book.setPublisher((String) value);
		}
		fireTableCellUpdated(aRowIndex, aColumnIndex);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.swing.table.AbstractTableModel#getColumnName(int)
	 */
	@Override
	public String getColumnName(int aColumn) {
		// TODO Auto-generated method stub
		return columnNames[aColumn];
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.swing.table.AbstractTableModel#getColumnClass(int)
	 */
	@Override
	public Class<?> getColumnClass(int aColumnIndex) {
		// TODO Auto-generated method stub

		switch (aColumnIndex) {
		case 0:
			return String.class;
		case 1:
			return String.class;
		case 2:
			return String.class;
		case 3:
			return String.class;
		}
		// default
		return String.class;
	}

}
