package ch.hsr.modules.uint1.heisenberglibrary.test;

import ch.hsr.modules.uint1.heisenberglibrary.model.Customer;
import junit.framework.TestCase;

public class CustomerTest extends TestCase {
	
	public void testCreateCustomer() {
		Customer cu1 = new Customer("Keller", "Heinz");
		cu1.setAdress("Zelgweg 12", 8000, "Z�rich");
		
		assertEquals("Keller", cu1.getName());
		assertEquals("Heinz", cu1.getSurname());
		
		assertEquals("Zelgweg 12",cu1.getStreet());
		assertEquals(8000,cu1.getZip());
		assertEquals("Z�rich",cu1.getCity());
	}

}
