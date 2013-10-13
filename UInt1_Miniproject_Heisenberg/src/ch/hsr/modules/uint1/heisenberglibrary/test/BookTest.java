package ch.hsr.modules.uint1.heisenberglibrary.test;

import ch.hsr.modules.uint1.heisenberglibrary.domain.Shelf;
import ch.hsr.modules.uint1.heisenberglibrary.domain.book.BookDO;
import junit.framework.TestCase;

public class BookTest extends TestCase {

	public void testBookCreation() {
		BookDO b = new BookDO("The Definitive ANTLR Reference");
		b.setTitle("The Definitive ANTLR Reference");
		b.setAuthor("Terence Parr");
		b.setPublisher("The Pragmatic Programmers");
		b.setShelf(Shelf.A1);
		
		assertEquals("The Definitive ANTLR Reference", b.getTitle());
		b.setTitle("NewName");
		assertEquals("NewName", b.getTitle());
		assertEquals("Terence Parr", b.getAuthor());
		assertEquals("The Pragmatic Programmers", b.getPublisher());
		
	}
}
