package cc.zsakvo.ninecswd.listener;

import android.graphics.Bitmap;

import java.util.List;

import cc.zsakvo.ninecswd.classes.ArticleList;
import cc.zsakvo.ninecswd.classes.BookList;

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

    public interface GetCover{
        void GetCoverOK(Bitmap bitmap);
    }

    public interface GetCoverUrls{
        void GetCoverUrls(List<String> list);
        void GetCoverUrlsFailed();
    }

    public interface GetArticleList{
        void GetOK(List<ArticleList> listDetails, int totalPages);
        void GetFailed();
    }

    public interface GetArticle{
        void GetResult(Boolean result);
    }

    public interface GetCategoryList{
        void GetOK(List<BookList> listDetails);
        void GetFailed();
    }

    public interface GetSearch{
        void GetOK(List<BookList> listDetails,int totalPages);
        void GetFailed();
    }
}
