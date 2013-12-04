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

import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Observable;
import java.util.Observer;
import java.util.Set;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.KeyStroke;
import javax.swing.ListSelectionModel;
import javax.swing.SwingUtilities;

import ch.hsr.modules.uint1.heisenberglibrary.controller.ITableModelChangeListener;
import ch.hsr.modules.uint1.heisenberglibrary.view.model.AbstractExtendendedEventTableModel;
import ch.hsr.modules.uint1.heisenberglibrary.view.model.IDisposable;

/**
 * 
 * @author msyfrig
 */
public abstract class AbstractSearchableTableJPanel<E extends Observable>
        extends JPanel implements IPanelActions, IDisposable {
    private static final long                       serialVersionUID = 3953982564280733109L;
    protected JTable                                table;
    protected JScrollPane                           tableScrollPane;
    protected GhostHintJTextField                   searchTextField;
    protected AbstractExtendendedEventTableModel<E> tableModel;

    protected List<E>                               dataList;

    /**
     * Map that holds all added observers for a panel. The corresponding
     * observers will be removed bevor this panel is closed so the gc can
     * collect all the dead references.
     */
    private Map<Observable, Observer>               observerMap      = new HashMap<>();

    public AbstractSearchableTableJPanel(List<E> aDataList) {
        this(aDataList, new JTable(), new GhostHintJTextField(
                UiComponentStrings.getString("empty")));//$NON-NLS-1$
    }

    public AbstractSearchableTableJPanel(List<E> aDataList, JTable aTable,
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
        tableModel = aTableModel;
        table.setModel(tableModel);
        table.getTableHeader().setReorderingAllowed(false);
        table.setAutoCreateRowSorter(true);
        table.setCellSelectionEnabled(true);
        table.setFillsViewportHeight(true);
        table.setColumnSelectionAllowed(false);
        table.putClientProperty("JTable.autoStartsEdit", Boolean.FALSE); //$NON-NLS-1$

        table.getSelectionModel().setSelectionMode(
                ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);

        tableScrollPane.setViewportView(table);
        addTableHandlers();
        autoSizeTableColumns();
    }

    public void setTableModel(
            AbstractExtendendedEventTableModel<E> aNewTableModel) {
        if (tableModel != aNewTableModel) {
            tableModel.cleanUpBeforeDispose();
            tableModel = aNewTableModel;
            table.setModel(tableModel);
            autoSizeTableColumns();
        }
    }

    private void addTableHandlers() {
        // delete default jtable behavior with enter (default=selecting next
        // row) so default action will be invoked
        Action openSelectedAction = new AbstractAction(
                "openSelectedActionWithEnter") {
            private static final long serialVersionUID = 3744924877943386680L;

            @Override
            public void actionPerformed(ActionEvent anActionEvent) {
                getDefaultButton().getAction().actionPerformed(anActionEvent);
            }
        };

        Object enterKey = table.getInputMap(
                JTable.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT).get(
                KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, 0));
        table.getActionMap().put(enterKey, openSelectedAction);

        tableModel.addTableModelChangeListener(new ITableModelChangeListener() {
            private Collection<E> previouslySelectedObjects;

            @Override
            public void tableIsAboutToUpdate() {
                previouslySelectedObjects = saveSelectedRows();
            }

            @Override
            public void tableChanged() {
                restoreSelectedRows(previouslySelectedObjects);
            }
        });
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
            E singleSelectedObject = dataList.get(table
                    .convertRowIndexToModel(selectionIndex));
            selectedEntries.add(singleSelectedObject);
        }
        return selectedEntries;
    }

    /**
     * Reselect the given entries in the table if they still exist.
     * 
     * @param someEntriesToSelect
     *            the objects to select
     */
    protected void restoreSelectedRows(final Collection<E> someEntriesToSelect) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
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
        });
    }

    protected boolean addObserverForObservable(Observable anObservable,
            Observer anObserver) {
        anObservable.addObserver(anObserver);
        return observerMap.put(anObservable, anObserver) != null;
    }

    protected boolean deleteObserverForObservable(Observable anObservable) {
        if (anObservable != null) {
            anObservable.deleteObserver(observerMap.remove(anObservable));
            return true;
        }
        return false;
    }

    void removeAllObservers() {
        for (Map.Entry<Observable, Observer> tempEntry : observerMap.entrySet()) {
            if (tempEntry != null) {
                tempEntry.getKey().deleteObserver(tempEntry.getValue());
            }
        }
        observerMap.clear();
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

    protected void setDataList(List<E> aDataList) {
        dataList = aDataList;
    }

    @Override
    public void searchFieldRequestFocus() {
        if (searchTextField != null) {
            searchTextField.requestFocus();
        }
    }

    @Override
    public void tableRequestFocus() {
        if (table != null) {
            table.requestFocus();
        }
    }

    @Override
    public void removeSelected() {
        // optional
    }

    @Override
    public void cleanUpBeforeDispose() {
        removeAllObservers();
    }
}