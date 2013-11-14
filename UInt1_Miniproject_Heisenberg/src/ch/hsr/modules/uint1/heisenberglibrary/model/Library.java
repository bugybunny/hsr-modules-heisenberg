package ch.hsr.modules.uint1.heisenberglibrary.model;

import java.util.ArrayList;
import java.util.List;

public class Library extends AbstractObservableDO {

    private List<Copy>     copies;
    private List<Customer> customers;
    private List<Loan>     loans;
    private List<BookDO>   bookDOs;

    public Library() {
        copies = new ArrayList<>();
        customers = new ArrayList<>();
        loans = new ArrayList<>();
        bookDOs = new ArrayList<>();
    }

    public Loan createAndAddLoan(Customer customer, Copy copy) {
        if (!isCopyLent(copy)) {
            Loan l = new Loan(customer, copy);
            loans.add(l);
            doNotify();
            return l;
        }
        return null;
    }

    public Customer createAndAddCustomer(String name, String surname) {
        Customer c = new Customer(name, surname);
        customers.add(c);
        doNotify();
        return c;
    }

    public BookDO createAndAddBook(String name) {
        BookDO b = new BookDO(name);
        bookDOs.add(b);
        doNotify(Integer.valueOf(getBooks().size()));
        return b;
    }

    public Copy createAndAddCopy(BookDO title) {
        Copy c = new Copy(title);
        copies.add(c);
        doNotify(Integer.valueOf(getCopies().size()));
        return c;
    }

    public void removeCopy(BookDO title) {
        copies.remove(new Copy(title));
        doNotify(Integer.valueOf(getCopies().size()));
    }

    public BookDO findByBookTitle(String title) {
        for (BookDO b : bookDOs) {
            if (b.getTitle().equals(title)) {
                return b;
            }
        }
        return null;
    }

    public boolean isCopyLent(Copy copy) {
        for (Loan l : loans) {
            if (l.getCopy().equals(copy) && l.isLent()) {
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

    public List<Loan> getOverdueLoans() {
        List<Loan> overdueLoans = new ArrayList<>();
        for (Loan l : getLoans()) {
            if (l.isOverdue()) {
                overdueLoans.add(l);
            }
        }
        return overdueLoans;
    }

    public List<Copy> getAvailableCopies() {
        return getCopies(false);
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

    public List<Copy> getCopies() {
        return copies;
    }

    public List<Loan> getLoans() {
        return loans;
    }

    public List<BookDO> getBooks() {
        return bookDOs;
    }

    public List<Customer> getCustomers() {
        return customers;
    }

}
