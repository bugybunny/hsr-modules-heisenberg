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
package ch.hsr.modules.uint1.heisenberglibrary.model;

/**
 * Enum for all possible loan states.
 * 
 * @author msyfrig
 */
public enum LoanStatus {
    OK(ModelStrings.getString("LoanStatus.ok")), DUE(ModelStrings.getString("LoanStatus.due")); //$NON-NLS-1$ //$NON-NLS-2$

    private final String text;

    /**
     * Creates a new enum with the given string.
     * 
     * @param aText
     *            the text for this loanstatus
     */
    private LoanStatus(String aText) {
        text = aText;
    }

    /**
     * @return the text for this loanstatus
     */
    public String getText() {
        return text;
    }

    @Override
    public String toString() {
        return getText();
    }

}
