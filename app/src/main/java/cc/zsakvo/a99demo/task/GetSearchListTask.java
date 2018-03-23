package cc.zsakvo.a99demo.task;

import android.os.AsyncTask;

import com.github.nukc.stateview.StateView;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import cc.zsakvo.a99demo.adapter.ListAdapter;
import cc.zsakvo.a99demo.classes.BookList;
import cc.zsakvo.a99demo.listener.Interface;
import cc.zsakvo.a99demo.listener.OnDataFinishedListener;
import cc.zsakvo.a99demo.utils.SplitUtil;

/**
 * Created by akvo on 2018/2/22.
 */

public class GetSearchListTask extends AsyncTask<Object,Void,List<BookList>> {
    private int totalPage;
    private Interface.GetSearch gs;

    public GetSearchListTask( Interface.GetSearch gs){
        this.gs = gs;
    }

    @Override
    protected List<BookList> doInBackground(Object... objects) {
        String url = (String) objects[0];
        int page = (int) objects[1];
        List<BookList> listDetails = new ArrayList<> ();
        try {
            Document doc = Jsoup.connect(url).timeout(20000).get();
            if (page==1){
                listDetails.clear();
                try {
                    totalPage = (int) Math.ceil ((double) Integer.parseInt (doc.selectFirst ("strong").text ()) / 15.0);
                }catch (Exception e){
                    return null;
                }
            }
            Element element = doc.selectFirst("ul.list_box");
            Elements ele_li = element.select("li");
            for (Element e:ele_li){
                if (e.selectFirst("h2")==null){
                    return null;
                }
                String title = e.selectFirst("h2").text();
                String author = SplitUtil.splitElement(e.select("h4").get(0));
                String category = SplitUtil.splitElement(e.select("h4").get(1));
                String label = SplitUtil.splitElement(e.select("h4").get(2),title,author.replace("作者:",""));
                String intro = "简介："+e.selectFirst("div.intro").text();
                String book_url = "http://www.99lib.net"+e.selectFirst("a").attr("href");
                listDetails.add(new BookList(title,author+"\n"+category+"\n"+label,intro,book_url));
            }
            if (isCancelled())
            {
                return (null);
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
        if (listDetails==null){
            gs.GetFailed ();
        }else {
            gs.GetOK (listDetails,totalPage);
        }
    }

}
