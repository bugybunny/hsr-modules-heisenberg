package ch.hsr.modules.uint1.heisenberglibrary.model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.GregorianCalendar;
import java.util.List;

public class Library extends ObservableObject {

    private List<Copy>     copies;
    private List<Customer> customers;
    private List<Loan>     loans;
    private List<BookDO>   books;
    private int            activeLoanCount;

    public Library() {
        copies = new ArrayList<>();
        customers = new ArrayList<>();
        loans = new ArrayList<>();
        books = new ArrayList<>();
    }

    public Loan createAndAddLoan(Customer customer, Copy copy) {
        if (!isCopyLent(copy)) {
            Loan l = new Loan(customer, copy);
            loans.add(l);
            doNotify(new ObservableModelChangeEvent(
                    ModelChangeTypeEnums.Loan.ADDED, null, l));
            doNotify(new ObservableModelChangeEvent(
                    ModelChangeTypeEnums.Loan.NUMBER, Integer.valueOf(loans
                            .size() - 1), Integer.valueOf(loans.size())));
            doNotify(new ObservableModelChangeEvent(
                    ModelChangeTypeEnums.Loan.ACTIVE_NUMBER,
                    Integer.valueOf(activeLoanCount),
                    Integer.valueOf(++activeLoanCount)));
            return l;
        }
        return null;
    }

    public Customer createAndAddCustomer(String name, String surname) {
        Customer c = new Customer(name, surname);
        customers.add(c);
        doNotify(new ObservableModelChangeEvent(
                ModelChangeTypeEnums.Customer.ADDED, null, c));
        doNotify(new ObservableModelChangeEvent(
                ModelChangeTypeEnums.Customer.NUMBER, Integer.valueOf(customers
                        .size() - 1), Integer.valueOf(customers.size())));
        return c;
    }

    public BookDO createAndAddBook(String name) {
        BookDO b = new BookDO(name);
        books.add(b);
        doNotify(new ObservableModelChangeEvent(
                ModelChangeTypeEnums.Book.ADDED, null, b));
        doNotify(new ObservableModelChangeEvent(
                ModelChangeTypeEnums.Book.NUMBER,
                Integer.valueOf(books.size() - 1),
                Integer.valueOf(books.size())));

        return b;
    }

    public Copy createAndAddCopy(BookDO title) {
        Copy c = new Copy(title);
        copies.add(c);

        doNotify(new ObservableModelChangeEvent(
                ModelChangeTypeEnums.Copy.ADDED, null, c));
        doNotify(new ObservableModelChangeEvent(
                ModelChangeTypeEnums.Copy.NUMBER,
                Integer.valueOf(copies.size() - 1), Integer.valueOf(copies
                        .size())));

        return c;
    }

    public void removeCopy(Copy aCopyToRemove) {
        // return the loan for the copy to delete so we have it in the history
        returnCopy(aCopyToRemove);
        copies.remove(aCopyToRemove);

        doNotify(new ObservableModelChangeEvent(
                ModelChangeTypeEnums.Copy.REMOVED, aCopyToRemove, null));
        doNotify(new ObservableModelChangeEvent(
                ModelChangeTypeEnums.Copy.NUMBER,
                Integer.valueOf(copies.size() - 1), Integer.valueOf(copies
                        .size())));
    }

    public void removeCopies(Collection<Copy> someCopiesToDelete) {
        for (Copy tempCopy : someCopiesToDelete) {
            removeCopy(tempCopy);
        }
    }

    /**
     * Returns a copy to the library. The copy will be available again and will
     * be removed from the customer loan.
     * 
     * @param aCopyToReturn
     *            the copy to return to the library
     * @return the changed loan object or {@code null} if a not lent out book
     *         has been passed
     */
    public Loan returnCopy(Copy aCopyToReturn) {
        return returnCopy(aCopyToReturn, new GregorianCalendar());
    }

    /**
     * Returns a copy to the library and sets the return date as given. The copy
     * will be available again and will be removed from the customer loan.
     * 
     * @param aCopyToReturn
     *            the copy to return to the library
     * @param aReturnDate
     *            date when the copy has been returned
     * 
     * @return the changed loan object or {@code null} if a not lent out book
     *         has been passed
     */
    public Loan returnCopy(Copy aCopyToReturn, GregorianCalendar aReturnDate) {
        Loan activeLoan = getActiveLoanForCopy(aCopyToReturn);
        if (activeLoan != null) {
            try {
                activeLoan.returnCopy(aReturnDate);
            }
            catch (IllegalLoanOperationException anEx) {
                // do nothing
            }
            doNotify(new ObservableModelChangeEvent(
                    ModelChangeTypeEnums.Loan.RETURNED, activeLoan, activeLoan));
            doNotify(new ObservableModelChangeEvent(
                    ModelChangeTypeEnums.Loan.ACTIVE_NUMBER,
                    Integer.valueOf(activeLoanCount),
                    Integer.valueOf(--activeLoanCount)));
        }
        return activeLoan;
    }

    // Used by Stolzes LibraryTests - we never use it.
    public BookDO findByBookTitle(String aTitle) {
        for (BookDO b : books) {
            if (b.getTitle().equalsIgnoreCase(aTitle)) {
                return b;
            }
        }
        return null;
    }

    public ArrayList<BookDO> findAllBooksByTitle(String aTitle) {
        ArrayList<BookDO> bookList = new ArrayList<>();
        for (BookDO b : books) {
            if (b.getTitle().equalsIgnoreCase(aTitle)) {
                bookList.add(b);
            }
        }
        return bookList;
    }

    public boolean isCopyLent(Copy aCopy) {
        for (Loan l : loans) {
            if (l.getCopy().equals(aCopy) && l.isLent()) {
                return true;
            }
        }
        return false;
    }

    public List<Copy> getCopiesOfBook(BookDO bookDO) {
        List<Copy> res = new ArrayList<>();
        for (Copy c : copies) {
            if (c.getTitle().equals(bookDO)) {
                res.add(c);
            }
        }

        return res;
    }

    public List<Loan> getLentCopiesOfBook(BookDO bookDO) {
        List<Loan> lentCopies = new ArrayList<>();
        for (Loan l : loans) {
            if (l.getCopy().getTitle().equals(bookDO) && l.isLent()) {
                lentCopies.add(l);
            }
        }
        return lentCopies;
    }

    public Loan getActiveLoanForCopy(Copy aCopy) {
        List<Loan> allLoansForBook = getLentCopiesOfBook(aCopy.getTitle());
        for (Loan tempLoan : allLoansForBook) {
            if (tempLoan.getCopy().equals(aCopy)) {
                return tempLoan;
            }
        }

        return null;
    }

    public List<Loan> getCustomerLoans(Customer customer) {
        List<Loan> lentCopies = new ArrayList<>();
        for (Loan l : loans) {
            if (l.getCustomer().equals(customer)) {
                lentCopies.add(l);
            }
        }
        return lentCopies;
    }

    public List<Loan> getActiveCustomerLoans(Customer customer) {
        List<Loan> lentCopies = new ArrayList<>();
        for (Loan l : loans) {
            if (l.isLent() && l.getCustomer().equals(customer)) {
                lentCopies.add(l);
            }
        }
        return lentCopies;
    }

    public List<Loan> getOverdueLoans() {
        List<Loan> overdueLoans = new ArrayList<>();
        for (Loan l : getLoans()) {
            if (l.isOverdue()) {
                overdueLoans.add(l);
            }
        }
        return overdueLoans;
    }

    public List<Loan> getOverdueLoansForCustomer(Customer aCustomer) {
        List<Loan> overdueLoans = new ArrayList<>();
        for (Loan l : getLoans()) {
            if (l.isOverdue() && l.getCustomer().equals(aCustomer)) {
                overdueLoans.add(l);
            }
        }
        return overdueLoans;
    }

    public List<Copy> getAvailableCopies() {
        return getCopies(false);
    }

    public List<Copy> getAvailableCopiesForBook(BookDO aBook) {
        List<Copy> res = new ArrayList<>();
        for (Copy c : copies) {
            if (c.getTitle().equals(aBook) && !isCopyLent(c)) {
                res.add(c);
            }
        }

        return res;
    }

    public List<Copy> getLentOutBooks() {
        return getCopies(true);
    }

    private List<Copy> getCopies(boolean isLent) {
        List<Copy> retCopies = new ArrayList<>();
        for (Copy c : copies) {
            if (isLent == isCopyLent(c)) {
                retCopies.add(c);
            }
        }
        return retCopies;
    }

    public List<Loan> getActiveLoans() {
        List<Loan> activeLoans = new ArrayList<>();
        for (Loan tempLoan : getLoans()) {
            if (tempLoan.isLent()) {
                activeLoans.add(tempLoan);
            }
        }
        return activeLoans;
    }

    public boolean isCustomerAllowedToLendOut(Customer aCustomerToCheck) {
        int activeLoans = getActiveCustomerLoans(aCustomerToCheck).size();
        return activeLoans < Customer.MAX_LENT_OUT_BOOKS
                && getOverdueLoansForCustomer(aCustomerToCheck).isEmpty();
    }

    public List<Copy> getCopies() {
        return copies;
    }

    public List<Loan> getLoans() {
        return loans;
    }

    public List<BookDO> getBooks() {
        return books;
    }

    public List<Customer> getCustomers() {
        return customers;
    }

    public int getActiveLoanCount() {
        return activeLoanCount;
    }

}
