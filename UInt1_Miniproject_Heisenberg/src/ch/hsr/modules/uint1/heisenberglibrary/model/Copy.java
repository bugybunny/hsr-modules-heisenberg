package ch.hsr.modules.uint1.heisenberglibrary.model;

import ch.hsr.modules.uint1.heisenberglibrary.domain.book.BookDO;

public class Copy {

    public enum Condition {
        NEW, GOOD, DAMAGED, WASTE, LOST
    }

    public static long   nextInventoryNumber = 1;

    private final long   inventoryNumber;
    private final BookDO bookDO;
    private Condition    condition;

    public Copy(BookDO title) {
        this.bookDO = title;
        inventoryNumber = nextInventoryNumber++;
        condition = Condition.NEW;
    }

    public BookDO getTitle() {
        return bookDO;
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
