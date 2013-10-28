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

import javax.swing.RowFilter;

/**
 * Filters a table based on a given string. This string can be in any row and is
 * case insensitive.
 * 
 * 
 * TODO schreiben wieso nicht einfach RowFilter.regexFilter verwendet wird
 * 
 * @author msyfrig
 */
public class TextBookTableFilter<M, I> extends RowFilter<M, I> {
    private final String searchText;

    // private final Pattern matchingPattern;

    public TextBookTableFilter(String aSearchText) {
        searchText = aSearchText;
        // matchingPattern = Pattern.compile("(?i).*" + searchText + ".*");
    }

    /**
     * Shows the row if the {@code searchText} can be found anywhere Means in
     * all columns, at the beginning, somewhere between or at the end of a cell
     * value. <b>Ignores case sensitivity</b>
     * 
     * @return {@code true} if the searchstring is empty {@code contains()}
     *         returns true on any columncell
     */
    @Override
    public boolean include(
            javax.swing.RowFilter.Entry<? extends M, ? extends I> anEntry) {
        // show all rows if searchstring is empty, no need to go through all
        // cells and check, should be 75% faster
        if (searchText.isEmpty()) {
            return true;
        }
        boolean ret = false;
        for (int i = 0; i < anEntry.getValueCount(); i++) {
            // Matcher matcher = matchingPattern
            // .matcher(anEntry.getStringValue(i));
            // if (matcher.find()) {
            if (anEntry.getStringValue(i).toLowerCase()
                    .contains(searchText.toLowerCase())) {
                ret = true;
                break;
            }
        }
        return ret;
    }
}
