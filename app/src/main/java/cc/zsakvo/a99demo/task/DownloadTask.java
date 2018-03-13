package cc.zsakvo.a99demo.task;

import android.os.AsyncTask;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import cc.zsakvo.a99demo.utils.DecodeUtils;

/**
 * Created by akvo on 2018/2/22.
 */

public class DownloadTask extends AsyncTask<String,Integer,Object[]> {
    @Override
    protected Object[] doInBackground(String... strings) {
        String url = strings[0];
        Document doc = null;
        String bookName = "";
        String bookAuthor = "";
        String bookCoverURL = "";
        List<String> titles = new ArrayList<> ();
        List<String> chapterIDs = new ArrayList<> ();
        String replaceStr = url.replace ("http://www.99lib.net","");
        replaceStr = replaceStr.replace ("index.htm","");
        try {
            doc = Jsoup.connect(url).timeout(20000).get();
            bookName = doc.selectFirst("h2").text();
            bookAuthor = splitElement(doc.selectFirst("h4"));
            bookCoverURL = "http://www.99lib.net"+doc.selectFirst("img").attr("src");
            Elements elements_drags = doc.getElementById("right").select("dd");
            titles = new ArrayList<> ();
            chapterIDs = new ArrayList<>();
            for (Element e:elements_drags){
            titles.add(e.selectFirst("a").text());
            chapterIDs.add(e.selectFirst("a").attr("href").replace (replaceStr,"").replace (".htm",""));
            }
        } catch (IOException e) {
            e.printStackTrace ();
        }
        return new Object[]{bookName,bookAuthor,bookCoverURL,titles,chapterIDs};
    }

    @Override
    protected void onPostExecute(Object[] objs) {
        super.onPostExecute (objs);
        
    }

    private String splitElement(Element element){
        Elements es = element.select("a");
        String str = " ";
        for (Element e:es){
            String tmp = e.text()+" ";
            str = str + tmp;
        }
        element.select("a").remove();
        String tmp = element.text();
        str = tmp+str;
        return str;
    }

    private String splitElement(Element element,String rStr1,String rStr2){
        Elements es = element.select("a");
        String str = " ";
        for (Element e:es){
            String tmp = e.text()+" ";
            str = str + tmp;
        }
        element.select("a").remove();
        String tmp = element.text();
        str = str.replace(rStr1,"");
        str = str.replace(rStr2.replace("作者:",""),"");
        str = tmp+str;
        return str;
    }
}
