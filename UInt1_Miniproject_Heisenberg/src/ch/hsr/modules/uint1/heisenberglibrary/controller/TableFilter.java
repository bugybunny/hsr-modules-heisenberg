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

//@formatter:off
/**
 * Class to help filtering a table. A table needs to have an associated
 * {@link GhostHintJTextField} as it's search field.
 * 
 * <br>The input made in this textfield is handled automatically by this class
 * and additional filters can be added and removed via
 * {@link #addFilter(RowFilter)} and {@link #removeFilter(RowFilter)} when
 * events happen outside of the table that should trigger the filter.
 * 
 * <br>To add additonal filters when for example a {@link javax.swing.JCheckBox} should be
 * considerer when filtering and should trigger the filter, add or remove the
 * filter when the checkbox is selected or deselected and <b>call
 * {@link #filterTable()} after adding or removing the filter</b>. Make sure to
 * use the same generics as in this instance.
 * 
 * For example
 * <blockquote>
 * <pre>
 * checkbox.addItemListener(new ItemListener() {
 *     public void itemStateChanged(ItemEvent anItemStateChangedEvent) {
 *         if (anItemStateChangedEvent.getStateChange() == ItemEvent.SELECTED) {
 *             tableFilter.addFilter(filter);
 *         } else {
 *             tableFilter.removeFilter(filter);
 *         }
 *         tableFilter.filterTable();
 *     }
 * });
 * </pre>
 *</blockquote>
 *
 * @author msyfrig
 */
//@formatter:on
public class TableFilter<M extends TableModel> {
    private JTable                    table;
    private GhostHintJTextField       searchField;
    /**
     * Additional filters to the text contains filter that are combined with
     * {@link #combiningFilterType} when {@link #filterTable()} is called.
     */
    private Set<RowFilter<M, Object>> additionalFilters;
    private FilterType                combiningFilterType;

    /**
     * 
     * 
     * @param aTable
     * @param aSearchField
     */
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
        additionalFilters = new HashSet<>();
        combiningFilterType = aCombiningFilterType;
        searchField.getDocument().addDocumentListener(
                new SearchFieldDocumentListener());
    }

    public void filterTable() {
        List<RowFilter<M, Object>> combiningRowFilterList = new ArrayList<>(
                additionalFilters.size() + 1);
        combiningRowFilterList.add(new TextBookTableFilter<M, Object>(
                searchField.getText()));
        combiningRowFilterList.addAll(additionalFilters);
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
            if (tableSorter.getViewRowCount() == 1) {
                table.getSelectionModel().setSelectionInterval(0, 0);
            }
        } else {
            searchField.setNegativeBackground();
        }
    }

    public boolean addFilter(RowFilter<M, Object> aFilterToAdd) {
        return additionalFilters.add(aFilterToAdd);
    }

    public boolean removeFilter(RowFilter<M, Object> aFilterToRemove) {
        return additionalFilters.remove(aFilterToRemove);
    }

    /**
     * Change how the filters should be combined in {@link #filterTable()}.
     * {@link #filterTable()} needs to be called seperately after this if
     * needed.
     */
    public void setCombiningFilterType(FilterType aCombiningFilterType) {
        combiningFilterType = aCombiningFilterType;
    }

    public FilterType getCombiningFilterType() {
        return combiningFilterType;
    }

    /** Possible values how all the filters should be combined. */
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
