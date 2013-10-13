package ch.hsr.modules.uint1.heisenberglibrary.domain.book;

import ch.hsr.modules.uint1.heisenberglibrary.domain.AbstractObservableDO;
import ch.hsr.modules.uint1.heisenberglibrary.domain.Shelf;

/**
 * TODO comment
 * 
 * @author msyfrig
 */
public class BookDO extends AbstractObservableDO {

    private String title, author, publisher;
    private Shelf  shelf;

    public BookDO(String name) {
        this.title = name;
    }

    public void set(String aTitle, String aAuthor, String aPublisher,
            Shelf aShelf) {
        title = aTitle;
        author = aAuthor;
        publisher = aPublisher;
        shelf = aShelf;
        super.set();
    }

    /**
     * @return the title
     */
    public String getTitle() {
        return title;
    }

    /**
     * @param aTitle
     *            the title to set
     */
    public void setTitle(String aTitle) {
        title = aTitle;
        doNotify();
    }

    /**
     * @return the author
     */
    public String getAuthor() {
        return author;
    }

    /**
     * @param aAuthor
     *            the author to set
     */
    public void setAuthor(String aAuthor) {
        author = aAuthor;
        doNotify();
    }

    /**
     * @return the publisher
     */
    public String getPublisher() {
        return publisher;
    }

    /**
     * @param aPublisher
     *            the publisher to set
     */
    public void setPublisher(String aPublisher) {
        publisher = aPublisher;
        doNotify();
    }

    /**
     * @return the shelf
     */
    public Shelf getShelf() {
        return shelf;
    }

    /**
     * @param aShelf
     *            the shelf to set
     */
    public void setShelf(Shelf aShelf) {
        shelf = aShelf;
        doNotify();
    }

    @Override
    public String toString() {
        return title + ", " + author + ", " + publisher;
    }

}
