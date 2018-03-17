package cc.zsakvo.a99demo.classes;

import java.util.List;

/**
 * Created by akvo on 2018/3/17.
 */

public class DownloadDetails {
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

    public List<String> getTitles() {
        return titles;
    }

    public List<String> getChapters() {
        return chapters;
    }

    public List<String> getChapterIDs() {
        return chapterIDs;
    }

    private String bookID;
    private String bookName;
    private String bookAuthor;
    private String bookCoverURL;
    private List<String> titles;
    private List<String> chapters;
    private List<String> chapterIDs;

    public DownloadDetails(String bookID,String bookName,String bookAuthor,String bookCoverURL,List<String> titles,List<String> chapterIDs,List<String> chapters){
        this.bookID = bookID;
        this.bookName = bookName;
        this.bookAuthor = bookAuthor;
        this.bookCoverURL = bookCoverURL;
        this.titles = titles;
        this.chapterIDs = chapterIDs;
        this.chapters = chapters;
    }
}
