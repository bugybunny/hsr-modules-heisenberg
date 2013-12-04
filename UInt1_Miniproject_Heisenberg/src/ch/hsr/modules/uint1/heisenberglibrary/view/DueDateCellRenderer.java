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

import java.text.ChoiceFormat;
import java.text.Format;
import java.text.MessageFormat;
import java.text.NumberFormat;
import java.util.Date;

import javax.swing.table.DefaultTableCellRenderer;

import ch.hsr.modules.uint1.heisenberglibrary.util.DateUtil;

/**
 * @author msyfrig
 */
//@formatter:off
public class DueDateCellRenderer extends DefaultTableCellRenderer {
    private static final long serialVersionUID = -654153417111861663L;

    // things to format the days until duedate string
    private static final double[] DAY_RULES = { ChoiceFormat.previousDouble(-1), -1, 0, 1, ChoiceFormat.nextDouble(1) };

    private static final String ONE_DAY_LEFT_PATTERN = UiComponentStrings
            .getString("LoanTableModel.column.lentuntil.content.choice.oneDayLeft");  //$NON-NLS-1$
    private static final String ZERO_OR_MORE_DAYS_LEFT_PATTERN = UiComponentStrings
            .getString("LoanTableModel.column.lentuntil.content.choice.moreDaysLeft"); //$NON-NLS-1$
    private static final String ONE_DAY_LATE_PATTERN = UiComponentStrings
            .getString("LoanTableModel.column.lentuntil.content.choice.oneDayLate");  //$NON-NLS-1$
    private static final String MORE_DAYS_LATE_PATTERN = UiComponentStrings
            .getString("LoanTableModel.column.lentuntil.content.choice.moreDaysLate"); //$NON-NLS-1$

    private static final String[] DAY_STRINGS = {MORE_DAYS_LATE_PATTERN, ONE_DAY_LATE_PATTERN,
            ZERO_OR_MORE_DAYS_LEFT_PATTERN, ONE_DAY_LEFT_PATTERN,
            ZERO_OR_MORE_DAYS_LEFT_PATTERN};

    private static final ChoiceFormat DAY_FORMATTER_CHOICE = new ChoiceFormat(DAY_RULES, DAY_STRINGS);
    private static final Format[] DAYS_UNTIL_FORMATS = { null, DAY_FORMATTER_CHOICE, NumberFormat.getInstance() };
    private static final MessageFormat DAYS_UNTIL_FORMATTER = new MessageFormat(UiComponentStrings.
            getString("LoanTableModel.column.lentuntil.content")); //$NON-NLS-1$
    //@formatter:on

    static {
        DAYS_UNTIL_FORMATTER.setFormats(DAYS_UNTIL_FORMATS);
    }

    public DueDateCellRenderer() {
        setOpaque(true);

    }

    @Override
    protected void setValue(Object aValue) {
        if (aValue instanceof Date) {
            Date dueDate = (Date) aValue;

            long daysFromTodayToDueDate = DateUtil
                    .daysDiff(new Date(), dueDate);
            Object[] messageArguments = { DateUtil.getFormattedDate(dueDate),
                    Long.valueOf(daysFromTodayToDueDate),
                    Long.valueOf(Math.abs(daysFromTodayToDueDate)) };
            setText(DAYS_UNTIL_FORMATTER.format(messageArguments));
        }
    }
}
