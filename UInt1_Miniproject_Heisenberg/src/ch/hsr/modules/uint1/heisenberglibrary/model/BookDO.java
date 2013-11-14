/*
    This program is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package ch.hsr.modules.uint1.heisenberglibrary.model;

/**
 * Represents a book object.
 * 
 * @author mstolze
 * @author msyfrig
 */
public class BookDO extends AbstractObservable {

    private String title;
    private String author;
    private String publisher;
    private Shelf  shelf;

    public BookDO(String aTitle) {
        title = aTitle;
    }

    public void set(String aTitle, String aAuthor, String aPublisher,
            Shelf aShelf) {
        title = aTitle;
        author = aAuthor;
        publisher = aPublisher;
        shelf = aShelf;
        // TODO buch clonen falls möglich und als oldvalue übergeben
        doNotify(new ObservableModelChangeEvent(
                ModelChangeTypeEnums.Book.EVERYTHING_CHANGED, null, this));
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String aTitle) {
        String oldValue = title;
        title = aTitle;
        doNotify(new ObservableModelChangeEvent(
                ModelChangeTypeEnums.Book.TITLE, oldValue, title));
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String anAuthor) {
        String oldValue = author;
        author = anAuthor;
        doNotify(new ObservableModelChangeEvent(
                ModelChangeTypeEnums.Book.AUTHOR, oldValue, author));
    }

    public String getPublisher() {
        return publisher;
    }

    public void setPublisher(String aPublisher) {
        String oldValue = publisher;
        publisher = aPublisher;
        doNotify(new ObservableModelChangeEvent(
                ModelChangeTypeEnums.Book.PUBLISHER, oldValue, publisher));
    }

    public Shelf getShelf() {
        return shelf;
    }

    public void setShelf(Shelf aShelf) {
        Shelf oldValue = shelf;
        shelf = aShelf;
        doNotify(new ObservableModelChangeEvent(
                ModelChangeTypeEnums.Book.SHELF, oldValue, shelf));
    }

    @Override
    public String toString() {
        return title + ", " + author + ", " + publisher;
    }

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
