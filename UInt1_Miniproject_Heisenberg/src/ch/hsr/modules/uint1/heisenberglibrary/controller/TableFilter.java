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
package ch.hsr.modules.uint1.heisenberglibrary.controller;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.swing.JTable;
import javax.swing.RowFilter;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;

import ch.hsr.modules.uint1.heisenberglibrary.view.GhostHintJTextField;
import ch.hsr.modules.uint1.heisenberglibrary.view.TextBookTableFilter;

/**
 * TODO COMMENT ME!
 * 
 * @author msyfrig
 */
public class TableFilter<M extends TableModel> {
    private JTable                    table;
    private GhostHintJTextField       searchField;
    private Set<RowFilter<M, Object>> filters;
    private FilterType                combiningFilterType;

    public TableFilter(JTable aTable, GhostHintJTextField aSearchField) {
        this(aTable, aSearchField, FilterType.AND);
    }

    public TableFilter(JTable aTable, GhostHintJTextField aSearchField,
            FilterType aCombiningFilterType) {
        if (aTable == null || aSearchField == null) {
            throw new NullPointerException("arguments must not be null");
        }
        table = aTable;
        searchField = aSearchField;
        filters = new HashSet<>();
        combiningFilterType = aCombiningFilterType;
        searchField.getDocument().addDocumentListener(
                new SearchFieldDocumentListener());
    }

    public void filterTable() {
        List<RowFilter<M, Object>> combiningRowFilterList = new ArrayList<>(
                filters.size());
        combiningRowFilterList.add(new TextBookTableFilter<M, Object>(
                searchField.getText()));
        combiningRowFilterList.addAll(filters);
        @SuppressWarnings("unchecked")
        TableRowSorter<M> tableSorter = new TableRowSorter<>(
                (M) table.getModel());
        table.setRowSorter(tableSorter);
        RowFilter<M, Object> combinedFilter = null;
        if (combiningFilterType == FilterType.AND) {
            combinedFilter = RowFilter.andFilter(combiningRowFilterList);
        } else {
            combinedFilter = RowFilter.orFilter(combiningRowFilterList);
        }

        tableSorter.setRowFilter(combinedFilter);
        table.setRowSorter(tableSorter);
        if (tableSorter.getViewRowCount() > 0) {
            searchField.setPositiveBackground();
        } else {
            searchField.setNegativeBackground();
        }
    }

    public boolean addFilter(RowFilter<M, Object> aFilterToAdd) {
        return filters.add(aFilterToAdd);
    }

    public boolean removeFilter(RowFilter<M, Object> aFilterToRemove) {
        return filters.remove(aFilterToRemove);
    }

    /**
     * @return the combiningFilterType
     */
    public FilterType getCombiningFilterType() {
        return combiningFilterType;
    }

    /**
     * @param aCombiningFilterType
     *            the combiningFilterType to set
     */
    public void setCombiningFilterType(FilterType aCombiningFilterType) {
        combiningFilterType = aCombiningFilterType;
    }

    enum FilterType {
        AND, OR;
    }

    private class SearchFieldDocumentListener implements DocumentListener {

        private void filter() {
            filterTable();
        }

        @Override
        public void insertUpdate(DocumentEvent anInsertUpdateEvent) {
            filter();
        }

        @Override
        public void removeUpdate(DocumentEvent aRemoveUpdateEvent) {
            filter();
        }

        @Override
        public void changedUpdate(DocumentEvent aChangedUpdateEvent) {
            filter();
        }
    }
}
