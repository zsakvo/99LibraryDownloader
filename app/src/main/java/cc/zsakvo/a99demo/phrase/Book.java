package cc.zsakvo.a99demo.phrase;

import java.util.List;

/**
 * Created by akvo on 2018/2/16.
 */

public class Book {
    private String bookID;

    public String getBookID() {
        return bookID;
    }

    public String getBookName() {
        return bookName;
    }

    public String getBookAuthor() {
        return bookAuthor;
    }

    public String getBookCoverURL() {
        return bookCoverURL;
    }

    public List<String> getChapters() {
        return chapters;
    }

    public List<String> getTitles() {
        return titles;
    }

    private String bookName;
    private String bookAuthor;
    private String bookCoverURL;
    private List<String> chapters;
    private List<String> titles;

    public Book(String bookID, String bookName, String bookAuthor, String bookCoverURL, List<String> chapters, List<String> titles){
        this.bookID = bookID;
        this.bookName = bookName;
        this.bookAuthor = bookAuthor;
        this.bookCoverURL = bookCoverURL;
        this.chapters = chapters;
        this.titles = titles;
    }
}
