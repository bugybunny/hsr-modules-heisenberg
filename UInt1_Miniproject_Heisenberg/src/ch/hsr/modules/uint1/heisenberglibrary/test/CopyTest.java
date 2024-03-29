package ch.hsr.modules.uint1.heisenberglibrary.test;

import ch.hsr.modules.uint1.heisenberglibrary.model.BookDO;
import ch.hsr.modules.uint1.heisenberglibrary.model.Copy;
import junit.framework.TestCase;

public class CopyTest extends TestCase {

	public void testBook() {
		BookDO t = new BookDO("Design Pattern");
		Copy c1 = new Copy(t);
		assertEquals(Copy.nextInventoryNumber -1, c1.getInventoryNumber());
		Copy c2 = new Copy(t);
		assertEquals(Copy.nextInventoryNumber -1, c2.getInventoryNumber());
		assertEquals(Copy.Condition.NEW, c2.getCondition());
		
		c1.setCondition(Copy.Condition.DAMAGED);
		
		assertEquals(Copy.Condition.DAMAGED, c1.getCondition());
	}


}
