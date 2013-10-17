package ch.hsr.modules.uint1.heisenberglibrary.test;

import java.util.List;

import ch.hsr.modules.uint1.heisenberglibrary.domain.book.BookDO;
import ch.hsr.modules.uint1.heisenberglibrary.model.Customer;
import ch.hsr.modules.uint1.heisenberglibrary.model.Library;
import ch.hsr.modules.uint1.heisenberglibrary.model.Loan;
import junit.framework.TestCase;

public class LibraryTest extends TestCase {
	
	Library library;
	
	@Override
	protected void setUp() throws Exception {
		
		library = new Library();
		
		// Books
		BookDO b1 = library.createAndAddBook("Design Pattern");
		BookDO b2 = library.createAndAddBook("Refactoring");
		BookDO b3 = library.createAndAddBook("Clean Code");
		
		// Books
		library.createAndAddCopy(b1);
		library.createAndAddCopy(b1);
		library.createAndAddCopy(b1);
		
		library.createAndAddCopy(b2);
		library.createAndAddCopy(b2);		
		
		library.createAndAddCopy(b3);
		
		
		// Customers
		library.createAndAddCustomer("Keller", "Peter");
		library.createAndAddCustomer("Mueller", "Fritz");
		library.createAndAddCustomer("Meier", "Martin");
		
	}

	public void testGetBooksPerTitle() {
		
		BookDO t = library.findByBookTitle("Design Pattern");
		assertEquals(3, library.getCopiesOfBook(t).size());
		
		BookDO t2 = library.findByBookTitle("Clean Code");
		assertEquals(1, library.getCopiesOfBook(t2).size());
		
		BookDO t3 = library.findByBookTitle("noTitle");
		assertEquals(0, library.getCopiesOfBook(t3).size());
	}
	
	public void testLoans() {
		BookDO t = library.findByBookTitle("Design Pattern");
		
		assertEquals(0, library.getLentCopiesOfBook(t).size());
		
		Customer c = library.getCustomers().get(0);
		
		Loan lo = library.createAndAddLoan(c, library.getCopiesOfBook(t).get(0));
		
		assertEquals(1, library.getLentCopiesOfBook(t).size());
		assertEquals(lo, library.getLentCopiesOfBook(t).get(0));
		
		Loan lo2 = library.createAndAddLoan(c, library.getCopiesOfBook(t).get(0));
		assertNull(lo2);
		
		List<Loan> lo3 = library.getCustomerLoans(c);
		assertEquals(1, lo3.size());
		
	}
	
	public void testAvailability () {
		assertEquals(library.getCopies().size(),library.getAvailableCopies().size());
		
		BookDO t = library.findByBookTitle("Refactoring");
		Customer c = library.getCustomers().get(1);
		library.createAndAddLoan(c, library.getCopiesOfBook(t).get(0));
		
		assertEquals(1,library.getLentOutBooks().size());

	}
	

}
