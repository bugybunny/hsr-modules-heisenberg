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

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Observable;
import java.util.Set;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;

import ch.hsr.modules.uint1.heisenberglibrary.controller.TableModelChangeListener;
import ch.hsr.modules.uint1.heisenberglibrary.view.model.AbstractExtendendedEventTableModel;

/**
 * TODO COMMENT ME!
 * 
 * @author msyfrig
 */
// TODO ctrl+f für searchfieldfocus auch hier reinnehmen und allenfalls die
// parentcomponent suchen und dann dort zur input-/actionmap hinzufügen
public class SearchableTableJPanel<E extends Observable> extends JPanel {
    private static final long     serialVersionUID = 3953982564280733109L;
    protected JTable              table;
    // protected TableFilter<? extends TableModel> tableFilter;
    protected JScrollPane         tableScrollPane;
    protected GhostHintJTextField searchTextField;

    protected List<E>             dataList;

    public SearchableTableJPanel(List<E> aDataList) {
        this(aDataList, new JTable(), new GhostHintJTextField(
                UiComponentStrings.getString("empty")));//$NON-NLS-1$
    }

    public SearchableTableJPanel(List<E> aDataList, JTable aTable,
            GhostHintJTextField aSearchField) {
        dataList = aDataList;
        table = aTable;
        searchTextField = aSearchField;
        tableScrollPane = new JScrollPane(table);
    }

    protected void initTable(AbstractExtendendedEventTableModel<E> aTableModel) {
        if (table == null) {
            table = new JTable();
        }
        table.setModel(aTableModel);
        // aTableFilter = new TableFilter<>(table, searchTextField);
        table.getTableHeader().setReorderingAllowed(false);
        table.setAutoCreateRowSorter(true);
        table.setCellSelectionEnabled(true);
        table.setFillsViewportHeight(true);
        table.setColumnSelectionAllowed(false);

        table.getSelectionModel().setSelectionMode(
                ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);

        tableScrollPane.setViewportView(table);
        addTableHandlers();
        autoSizeTableColumns();
    }

    // well we have checked, no way to tell the compiler we did
    @SuppressWarnings("unchecked")
    private void addTableHandlers() {
        if (table.getModel() instanceof AbstractExtendendedEventTableModel) {
            AbstractExtendendedEventTableModel<E> tableModel = (AbstractExtendendedEventTableModel<E>) table
                    .getModel();
            tableModel
                    .addTableModelChangeListener(new TableModelChangeListener() {
                        private Collection<E> previouslySelectedBooks;

                        @Override
                        public void tableIsAboutToUpdate() {
                            previouslySelectedBooks = saveSelectedRows();
                        }

                        @Override
                        public void tableChanged() {
                            restoreSelectedRows(previouslySelectedBooks);
                        }
                    });
        }
    }

    protected void initSearchTextField(String aHint) {
        searchTextField = new GhostHintJTextField(aHint);
        searchTextField.setColumns(10);
    }

    protected void autoSizeTableColumns() {
        ColumnsAutoSizer.sizeColumnsToFit(table);
    }

    /**
     * Saves the selected entries in the table. The actual entry instances are
     * saved since entries can be added or removed so only saving the row index
     * is not enough.
     * 
     * @return set of currently selected entry instances
     */
    protected Set<E> saveSelectedRows() {
        Set<E> selectedEntries = new HashSet<>(table.getSelectedRowCount());
        for (int selectionIndex : table.getSelectedRows()) {
            E singleSelectedBook = dataList.get(table
                    .convertRowIndexToModel(selectionIndex));
            selectedEntries.add(singleSelectedBook);
        }
        return selectedEntries;
    }

    /**
     * Reselect the given entries in the table if they still exist.
     * 
     * @param someEntriesToSelect
     *            the books to select
     */
    protected void restoreSelectedRows(Collection<E> someEntriesToSelect) {
        for (E tempEntryToSelect : someEntriesToSelect) {

            int indexInList = dataList.indexOf(tempEntryToSelect);
            // do nothing if not found and entry has been removed
            if (indexInList > -1) {
                int indexToSelectInView = table
                        .convertRowIndexToView(indexInList);
                table.getSelectionModel().addSelectionInterval(
                        indexToSelectInView, indexToSelectInView);
            }
        }
    }

    protected void setTable(JTable aTable) {
        table = aTable;
        tableScrollPane.setViewportView(table);
        repaint();
    }

    JTable getTable() {
        return table;
    }

    protected void setSearchTextField(GhostHintJTextField aSearchTextField) {
        searchTextField = aSearchTextField;
        repaint();
    }

    GhostHintJTextField getSearchTextField() {
        return searchTextField;
    }

}