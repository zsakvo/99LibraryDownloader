package cc.zsakvo.a99demo.listener;

import java.util.List;

import cc.zsakvo.a99demo.classes.BookList;

/**
 * Created by akvo on 2018/3/19.
 */

public class Interface {
    public interface GetBookListFinish{
        void bookList(List<BookList> listDetails);
        void booksGetFailed();
    }

    public interface GetBookDetailFinish{
        void GetFailed();
        void GetSuccessful(String...strings);
    }

    public interface OutPutTxtFinish{
        void outPutFailed();
        void outPutSuccess(int i);
    }
}
