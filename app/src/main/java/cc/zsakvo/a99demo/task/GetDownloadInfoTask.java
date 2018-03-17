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

import cc.zsakvo.a99demo.classes.DownloadDetails;
import cc.zsakvo.a99demo.listener.OnDataFinishedListener;
import cc.zsakvo.a99demo.listener.OnDatasFinishedListener;
import cc.zsakvo.a99demo.utils.DecodeUtils;
import cc.zsakvo.a99demo.utils.EpubUtils;
import cc.zsakvo.a99demo.utils.ReplaceUtils;
import cc.zsakvo.a99demo.utils.SplitUtil;

/**
 * Created by akvo on 2018/2/22.
 */

public class GetDownloadInfoTask extends AsyncTask<Object,Integer,DownloadDetails> {

    private OnDataFinishedListener onDataFinishedListener;
    private String[] chapt;
    private int size;
    private DownloadDetails downloadDetails;

//    public GetDownloadInfoTask(DownloadDetails downloadDetails){
//        this.downloadDetails = downloadDetails;
//    }

    @Override
    protected DownloadDetails doInBackground(Object... objs) {
        String url = (String)objs[0];
//        DownloadDetails downloadDetails = (DownloadDetails)objs[1];
        Document doc = null;
        String bookID = "";
        String bookName = "";
        String bookAuthor = "";
        String bookCoverURL = "";
        List<String> titles = new ArrayList<> ();
        List<String> chapters = new ArrayList<> ();
        List<Integer> chapterIDs = new ArrayList<> ();
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
            chapterIDs.add(ReplaceUtils.getChapterID (bookID,e.selectFirst("a").attr("href")));
            }
            size = chapters.size ();
            chapt = new String[size];
        } catch (IOException e) {
            e.printStackTrace ();
        }
        return new DownloadDetails (bookID,
                bookName,
                bookAuthor,
                bookCoverURL,
                titles,
                null,
                null);
    }

    @Override
    protected void onPostExecute(DownloadDetails downloadDetails) {
        super.onPostExecute (downloadDetails);
        onDataFinishedListener.onDataSuccessfully(downloadDetails);
    }

    public void setOnDataFinishedListener(
            OnDataFinishedListener onDataFinishedListener) {
        this.onDataFinishedListener = onDataFinishedListener;
    }
}