package ch.hsr.modules.uint1.heisenberglibrary.test;

import java.util.GregorianCalendar;

import junit.framework.TestCase;
import ch.hsr.modules.uint1.heisenberglibrary.domain.Copy;
import ch.hsr.modules.uint1.heisenberglibrary.domain.Customer;
import ch.hsr.modules.uint1.heisenberglibrary.domain.IllegalLoanOperationException;
import ch.hsr.modules.uint1.heisenberglibrary.domain.Loan;
import ch.hsr.modules.uint1.heisenberglibrary.domain.book.BookDO;

public class LoanTest extends TestCase {

    public void testLoanCreation() {
        Loan l = createSampleLoan();
        assertTrue(new GregorianCalendar().equals(l.getPickupDate()));
        assertEquals("Keller", l.getCustomer().getName());
        assertEquals("Design Pattern", l.getCopy().getTitle().getTitle());
    }

    private Loan createSampleLoan() {
        Customer customer = new Customer("Keller", "Peter");
        BookDO title = new BookDO("Design Pattern");
        Copy copy = new Copy(title);
        Loan loan = new Loan(customer, copy);
        return loan;
    }

    public void testReturn() {
        Loan l = createSampleLoan();
        assertTrue(l.isLent());
        l.returnCopy();
        assertFalse(l.isLent());
    }

    public void testDurationCalculation() throws IllegalLoanOperationException {
        Loan l = createSampleLoan();
        assertEquals(-1, l.getDaysOfLoanDuration());

        GregorianCalendar returnDate = (GregorianCalendar) l.getPickupDate()
                .clone();
        returnDate.add(GregorianCalendar.DAY_OF_YEAR, 12);

        l.returnCopy(returnDate);

        assertEquals(12, l.getDaysOfLoanDuration());
        assertEquals(l.getReturnDate(), returnDate);

    }

    public void testDateConsistency() throws IllegalLoanOperationException {
        Loan l = createSampleLoan();
        l.setPickupDate(new GregorianCalendar(2009, 01, 01));
        assertTrue(l.isLent());
        try {
            l.returnCopy(new GregorianCalendar(2008, 12, 31));
            fail("BookDO cannot retuned before the pickup date");
        }
        catch (IllegalLoanOperationException e) {

        }
        assertTrue(l.isLent());
        l.returnCopy(new GregorianCalendar(2009, 12, 31));
        assertFalse(l.isLent());
        try {
            l.setPickupDate(new GregorianCalendar(2008, 10, 31));
            fail("pickup date cannot be set after the book was returned");
        }
        catch (IllegalLoanOperationException e) {
        }
    }
}
