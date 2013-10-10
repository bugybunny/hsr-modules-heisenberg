package ch.hsr.modules.uint1.heisenberglibrary.domain;

import ch.hsr.modules.uint1.heisenberglibrary.domain.book.Book;

public class Copy {
	
	public enum Condition {NEW, GOOD, DAMAGED, WASTE, LOST }
	
	public static long nextInventoryNumber = 1;
	
	private final long inventoryNumber;
	private final Book book;
	private Condition condition;
	
	public Copy(Book title) {
		this.book = title;
		inventoryNumber = nextInventoryNumber++;
		condition = Condition.NEW;
	}

	public Book getTitle() {
		return book;
	}

	public Condition getCondition() {
		return condition;
	}

	public void setCondition(Condition condition) {
		this.condition = condition;
	}

	public long getInventoryNumber() {
		return inventoryNumber;
	}
}
