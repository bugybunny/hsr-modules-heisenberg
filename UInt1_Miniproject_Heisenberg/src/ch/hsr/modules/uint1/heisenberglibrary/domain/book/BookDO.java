package ch.hsr.modules.uint1.heisenberglibrary.domain.book;

import ch.hsr.modules.uint1.heisenberglibrary.model.AbstractObservableDO;
import ch.hsr.modules.uint1.heisenberglibrary.model.Shelf;

/**
 * Represents a book object.
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

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((author == null) ? 0 : author.hashCode());
        result = prime * result
                + ((publisher == null) ? 0 : publisher.hashCode());
        result = prime * result + ((shelf == null) ? 0 : shelf.hashCode());
        result = prime * result + ((title == null) ? 0 : title.hashCode());
        return result;
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        BookDO other = (BookDO) obj;
        if (author == null) {
            if (other.author != null) {
                return false;
            }
        } else if (!author.equals(other.author)) {
            return false;
        }
        if (publisher == null) {
            if (other.publisher != null) {
                return false;
            }
        } else if (!publisher.equals(other.publisher)) {
            return false;
        }
        if (shelf != other.shelf) {
            return false;
        }
        if (title == null) {
            if (other.title != null) {
                return false;
            }
        } else if (!title.equals(other.title)) {
            return false;
        }
        return true;
    }

}
