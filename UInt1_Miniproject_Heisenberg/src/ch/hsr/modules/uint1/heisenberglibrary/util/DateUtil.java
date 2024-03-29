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
package ch.hsr.modules.uint1.heisenberglibrary.util;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.GregorianCalendar;

/**
 * @author msyfrig
 */
public class DateUtil {
    private static DateFormat df = SimpleDateFormat.getDateInstance();

    public static String getFormattedDate(Date aDate) {
        if (aDate != null) {
            return df.format(aDate);
        }
        return "00.00.00";
    }

    public static String getFormattedDate(GregorianCalendar aDate) {
        if (aDate != null) {
            return df.format(aDate.getTime());
        }
        return "00.00.00";
    }

    /**
     * Calculates the difference between two dates in days. Can be negative if
     * {@code from > to}.
     * 
     * @param from
     *            first date
     * @param to
     *            secend date
     * @return difference between the two given dates, more formally
     *         {@code to - from} in days, can be negative
     */
    public static long daysDiff(Date from, Date to) {
        return daysDiff(from.getTime(), to.getTime());
    }

    /**
     * Calculates the difference between two dates in days. Can be negative if
     * {@code from > to}.
     * 
     * @param from
     *            first date in milliseconds
     * @param to
     *            secend date in milliseconds
     * @return difference between the two given dates, more formally
     *         {@code to - from} in days, can be negative
     */
    public static long daysDiff(long from, long to) {
        return Math.round((to - from) / 1000 / 60 / 60 / 24);

    }
}
