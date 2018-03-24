package cc.zsakvo.ninecswd.classes;

/**
 * Created by akvo on 2018/2/9.
 */

public class BookList {
    private String book_name;
    private String book_intro;
    private String book_detail;

    public String getBook_url() {
        return book_url;
    }

    private String book_url;

    public String getList_name() {
        return book_name;
    }

    public String getList_intro() {
        return book_intro;
    }

    public String getList_detail() {
        return book_detail;
    }

    public BookList(String book_name, String book_intro, String book_detail,String book_url){
        this.book_name = book_name;
        this.book_intro = book_intro;
        this.book_detail = book_detail;
        this.book_url = book_url;
    }
}
