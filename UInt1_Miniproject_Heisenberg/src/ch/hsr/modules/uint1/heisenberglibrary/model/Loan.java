package ch.hsr.modules.uint1.heisenberglibrary.model;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.GregorianCalendar;

import ch.hsr.modules.uint1.heisenberglibrary.util.DateUtil;

public class Loan extends ObservableObject {

    private Copy     copy;
    private Customer customer;
    private GregorianCalendar pickupDate, returnDate, dueDate;
    public final static int   DAYS_TO_RETURN_BOOK = 30;

    public Loan(Customer aCustomer, Copy aCopy) {
        copy = aCopy;
        customer = aCustomer;
        pickupDate = new GregorianCalendar();
        calculateDueDate();
    }

    private void calculateDueDate() {
        dueDate = (GregorianCalendar) pickupDate.clone();
        dueDate.add(GregorianCalendar.DAY_OF_YEAR, DAYS_TO_RETURN_BOOK);
        dueDate.add(GregorianCalendar.HOUR_OF_DAY, 23);
        dueDate.add(GregorianCalendar.MINUTE, 59);
        dueDate.add(GregorianCalendar.SECOND, 59);
    }

    public boolean isLent() {
        return returnDate == null;
    }

    public boolean returnCopy() {
        try {
            returnCopy(new GregorianCalendar());
        }
        catch (IllegalLoanOperationException e) {
            return false;
        }
        return true;
    }

    public void returnCopy(GregorianCalendar aReturnDate)
            throws IllegalLoanOperationException {
        if (aReturnDate.before(pickupDate)) {
            throw new IllegalLoanOperationException(
                    "Return Date is before pickupDate");
        }
        returnDate = aReturnDate;
    }

    public void setPickupDate(GregorianCalendar aPickupDate)
            throws IllegalLoanOperationException {
        if (!isLent()) {
            throw new IllegalLoanOperationException("Loan is already retuned");
        }
        pickupDate = aPickupDate;
        calculateDueDate();
    }

    public GregorianCalendar getPickupDate() {
        return pickupDate;
    }

    public GregorianCalendar getReturnDate() {
        return returnDate;
    }

    /**
     * @return the dueDate
     */
    public GregorianCalendar getDueDate() {
        return dueDate;
    }

    public Copy getCopy() {
        return copy;
    }

    public Customer getCustomer() {
        return customer;
    }

    @Override
    public String toString() {
        return "Loan of: " + copy.getTitle().getTitle() + "\tFrom: "
                + customer.getName() + " " + customer.getSurname()
                + "\tPick up: " + getFormattedDate(pickupDate) + "\tReturn: "
                + getFormattedDate(returnDate) + "\tDays: "
                + getDaysOfLoanDuration();
    }

    private static String getFormattedDate(GregorianCalendar aDate) {
        if (aDate != null) {
            DateFormat f = SimpleDateFormat.getDateInstance();
            return f.format(aDate.getTime());
        }
        return "00.00.00";
    }

    public int getDaysOfLoanDuration() {
        if (returnDate != null) {
            return (int) (returnDate.getTimeInMillis() - pickupDate
                    .getTimeInMillis()) / 1000 / 60 / 60 / 24;
        }
        return -1;
    }

    public long getDaysOverdue() {
        if (!isOverdue()) {
            return 0;
        }

        return DateUtil.daysDiff(new Date(), dueDate.getTime());
    }

    public boolean isOverdue() {
        if (!isLent()) {
            return false;
        }
        return (new GregorianCalendar().after(dueDate));
    }
}
