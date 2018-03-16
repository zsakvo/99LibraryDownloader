package cc.zsakvo.a99demo.task;

import android.app.Dialog;
import android.os.AsyncTask;
import android.support.design.widget.Snackbar;
import android.util.Log;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import cc.zsakvo.a99demo.listener.OnDataFinishedListener;
import cc.zsakvo.a99demo.listener.OnDatasFinishedListener;
import cc.zsakvo.a99demo.utils.DecodeUtils;
import cc.zsakvo.a99demo.utils.EpubUtils;
import cc.zsakvo.a99demo.utils.SplitUtil;

/**
 * Created by akvo on 2018/2/22.
 */

public class DownloadTask extends AsyncTask<String,Integer,Object[]> {

    private OnDataFinishedListener onDataFinishedListener;
    private String[] chapt;
    private int size;

    @Override
    protected Object[] doInBackground(String... strings) {
        String url = strings[0];
        Document doc = null;
        String bookID = "";
        String bookName = "";
        String bookAuthor = "";
        String bookCoverURL = "";
        List<String> titles = new ArrayList<> ();
        List<String> chapters = new ArrayList<> ();
        bookID = url.replace ("http://www.99lib.net/book/","").replace ("/index.htm","");
        try {
            doc = Jsoup.connect(url).timeout(20000).get();
            bookName = doc.selectFirst("h2").text();
            bookAuthor = SplitUtil.splitElement(doc.selectFirst("h4"));
            bookCoverURL = "http://www.99lib.net"+doc.selectFirst("img").attr("src");
            Elements elements_drags = doc.getElementById("right").select("dd");
            titles = new ArrayList<> ();
            chapters = new ArrayList<>();
            for (Element e:elements_drags){
            titles.add(e.selectFirst("a").text());
//            chapters.add(DecodeUtils.url ("http://www.99lib.net"+e.selectFirst("a").attr("href")));
            chapters.add("http://www.99lib.net"+e.selectFirst("a").attr("href"));
            }
            size = chapters.size ();
            chapt = new String[size];
        } catch (IOException e) {
            e.printStackTrace ();
        }
        return new Object[]{bookID,bookName,bookAuthor,bookCoverURL,titles,chapters};
    }

    @Override
    protected void onPostExecute(Object[] objs) {
        super.onPostExecute (objs);
        int i = 0;
        for (String url:(List<String>)objs[5]){
            GetChaptersTask gct = new GetChaptersTask (i,chapt);
            gct.execute (url);
            i++;
        }
        List<String> chapters = new ArrayList<> ();
        while(chapt[size-1]!=null){
            System.out.println ("!!!!!!"+chapt[size-1]);
            chapters = Arrays.asList (chapt);
            new EpubUtils ((String)objs[0],
                    (String)objs[1],
                    (String)objs[2],
                    (String)objs[3],
                    chapters,
                    (List<String>)objs[4]).generateEpub();
            onDataFinishedListener.onDataSuccessfully(1);
            break;
        }
    }

    public void setOnDataFinishedListener(
            OnDataFinishedListener onDataFinishedListener) {
        this.onDataFinishedListener = onDataFinishedListener;
    }
}
