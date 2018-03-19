package cc.zsakvo.a99demo.task;

import android.annotation.SuppressLint;
import android.os.AsyncTask;
import android.support.v7.widget.RecyclerView;


import com.github.nukc.stateview.StateView;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import cc.zsakvo.a99demo.adapter.ArticleAdapter;
import cc.zsakvo.a99demo.adapter.ListAdapter;
import cc.zsakvo.a99demo.classes.BookList;
import cc.zsakvo.a99demo.listener.Interface;
import cc.zsakvo.a99demo.utils.SplitUtil;


/**
 * Created by akvo on 2018/2/21.
 */

public class GetBookListTask extends AsyncTask<String,java.lang.Void,List<BookList>> {

    private List<BookList> listDetails;
    private ListAdapter adapter;
    @SuppressLint("StaticFieldLeak")
    private StateView mStateView;
    private Interface.GetBookListFinish gbf;

    public GetBookListTask(/*ListAdapter adapter, List<BookList> listDetails, StateView mStateView, */Interface.GetBookListFinish gbf){
//        this.listDetails = listDetails;
//        this.adapter = adapter;
//        this.mStateView = mStateView;
        this.gbf = gbf;
    }

    @Override
    protected void onPreExecute() {
//        mStateView.showLoading ();
    }

    @Override
    protected List<BookList> doInBackground(String... strings) {
        String url = strings[0];
//        String page = url.substring (url.indexOf ("page=")+5);
        List<BookList> listDetails = new ArrayList<> ();
        try {
            Document doc = Jsoup.connect (url).timeout (20000).get ();
            Element element = doc.getElementById("list_box");
            Elements ele_li = element.select("li");
            for (Element e:ele_li){
                String title = e.selectFirst("a").attr("title");
                String author = SplitUtil.splitElement(e.select("h4").get(0));
                String category = SplitUtil.splitElement(e.select("h4").get(1));
                String label = SplitUtil.splitElement(e.select("h4").get(2),title,author.replace("作者:",""));
                String intro = "简介："+e.selectFirst("div.intro").text();
                String book_url = "http://www.99lib.net"+e.selectFirst("a").attr("href");
                listDetails.add(new BookList (title,author+"\n"+category+"\n"+label,intro,book_url));
            }
            return listDetails;
        } catch (IOException e) {
            e.printStackTrace ();
            return null;
        }
    }


    @Override
    protected void onPostExecute(List<BookList> listDetails){
        super.onPostExecute (listDetails);
        if (listDetails!=null) {
            gbf.bookList (listDetails);
        }else {
            gbf.booksGetFailed ();
        }
//            mStateView.showContent ();
    }
}
