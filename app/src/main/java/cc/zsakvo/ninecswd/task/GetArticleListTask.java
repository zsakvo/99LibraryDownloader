package cc.zsakvo.ninecswd.task;

import android.os.AsyncTask;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import cc.zsakvo.ninecswd.classes.ArticleList;
import cc.zsakvo.ninecswd.listener.Interface;

/**
 * Created by akvo on 2018/3/21.
 */

public class GetArticleListTask extends AsyncTask<String,Integer,List<ArticleList>> {

    private Interface.GetArticleList gal;
    private int totalPage = 0;

    public GetArticleListTask(Interface.GetArticleList gal){
        this.gal = gal;
    }

    @Override
    protected List<ArticleList> doInBackground(String... strings) {
        String url = strings[0];
        int page = Integer.parseInt (url.replace ("http://www.99lib.net/article/index.php?page=",""));
        List<ArticleList> listDetails = new ArrayList<> ();
        try {
            Document doc = Jsoup.connect(url).timeout(20000).get();
            if (page==1){
                listDetails.clear();
                totalPage = Integer.parseInt(doc.selectFirst("span.total").text().replace("1/",""));
            }
            Element element = doc.selectFirst("ul.list_box");
            Elements ele_li = element.select("li");
            for (Element e:ele_li){
                Element n = e.select("a").get(1);
                String author = e.selectFirst("span").text().replace("(","").replace(")","");
                String title = n.text();
                String artical_url = "http://www.99lib.net"+n.attr("href");
                listDetails.add(new ArticleList(title,author,artical_url));
            }
            if (isCancelled())
            {
                return (null);
            }
            return listDetails;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    protected void onPostExecute(List<ArticleList> listDetails){
        super.onPostExecute (listDetails);
        if (listDetails!=null) {
            gal.GetOK (listDetails,totalPage);
        }else {
            gal.GetFailed ();
        }
    }
}
