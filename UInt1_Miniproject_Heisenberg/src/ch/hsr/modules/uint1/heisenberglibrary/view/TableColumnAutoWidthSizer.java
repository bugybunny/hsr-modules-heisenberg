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

import java.awt.Component;
import java.awt.FontMetrics;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;

import javax.swing.JTable;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableCellRenderer;

/**
 * Helper class to automatically adjust the column size based on the content
 * length.
 * 
 * <br>Calculates all
 * 
 * @author msyfrig
 */
public class TableColumnAutoWidthSizer {
    private JTable table;

    public TableColumnAutoWidthSizer(JTable aTable) {
        table = aTable;
        table.addComponentListener(new ComponentAdapter() {
            /*
             * (non-Javadoc)
             * 
             * @see
             * java.awt.event.ComponentAdapter#componentShown(java.awt.event
             * .ComponentEvent)
             */
            @Override
            public void componentShown(ComponentEvent aE) {
                autoResizeTable();
            }
        });
    }

    public void autoResizeTable() {
        for (int column = 0; column < table.getColumnCount(); column++) {
            autoResizeColumn(column);
        }
    }

    public void autoResizeColumn(int aColumnIndex) {
        int maxWidthForColumn = calculateMaxWidthForColumn(aColumnIndex);
        System.out.println(maxWidthForColumn);
        double percentOfWholeWidth = maxWidthForColumn / table.getWidth();
        table.getColumnModel()
                .getColumn(aColumnIndex)
                .setPreferredWidth(
                        (int) (table.getWidth() * percentOfWholeWidth));
    }

    /**
     * Returns the pixel for the longest entry in this column, can be the width
     * of the column header. Always returns an int bigger than 0.
     * 
     * @param aColumnIndex
     *            the column index to calculate the max width for
     * 
     * @return pixel of the longest entry, >0 (is never zero)
     */
    private int calculateMaxWidthForColumn(int aColumnIndex) {
        int maxWidthForColumn = 0;
        for (int row = 0; row < table.getRowCount(); row++) {
            TableCellRenderer renderer = table.getCellRenderer(row,
                    aColumnIndex);
            Component comp = table.prepareRenderer(renderer, row, aColumnIndex);
            maxWidthForColumn = Math.max(comp.getPreferredSize().width,
                    maxWidthForColumn);
        }

        // check if header is longer than any conent, also to prevent that we
        // return zero, would lead to a division by zero
        JTableHeader tableHeader = table.getTableHeader();
        FontMetrics headerFontMetrics = tableHeader.getFontMetrics(tableHeader
                .getFont());
        int headerWidth = headerFontMetrics.stringWidth(table
                .getColumnName(aColumnIndex));
        maxWidthForColumn = Math.max(headerWidth, maxWidthForColumn);

        return maxWidthForColumn;
    }
}
