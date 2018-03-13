package cc.zsakvo.a99demo.classes;

/**
 * Created by akvo on 2018/2/19.
 */

public class ArticleList {
    public String getArticleName() {
        return articleName;
    }

    public String getArticleAuthor() {
        return articleAuthor;
    }

    private String articleName;
    private String articleAuthor;

    public String getArticleUrl() {
        return articleUrl;
    }

    private String articleUrl;
    public ArticleList(String articleName,String articleAuthor,String articleUrl){
        this.articleName = articleName;
        this.articleAuthor = articleAuthor;
        this.articleUrl = articleUrl;
    }
}
