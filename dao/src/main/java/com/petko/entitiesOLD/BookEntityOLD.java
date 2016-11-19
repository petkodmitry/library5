package com.petko.entitiesOLD;

public class BookEntityOLD extends EntityOLD {
    private int bookId;
    private String title;
    private String author;
    private boolean isBusy;

    @Override
    public String toString() {
        return String.format("Book [bookId=%d, title=%s, author=%s, isBusy=%b]", bookId, title, author, isBusy);
    }

    public int getBookId() {
        return bookId;
    }

    public void setBookId(int bookId) {
        this.bookId = bookId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public boolean isBusy() {
        return isBusy;
    }

    public void setBusy(boolean busy) {
        isBusy = busy;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof BookEntityOLD)) return false;

        BookEntityOLD that = (BookEntityOLD) o;

        if (getTitle() != null ? !getTitle().equals(that.getTitle()) : that.getTitle() != null) return false;
        return getAuthor() != null ? getAuthor().equals(that.getAuthor()) : that.getAuthor() == null;
    }

    @Override
    public int hashCode() {
        int result = getTitle() != null ? getTitle().hashCode() : 0;
        result = 31 * result + (getAuthor() != null ? getAuthor().hashCode() : 0);
        return result;
    }
}
