package cc.zsakvo.a99demo.task;

import android.os.AsyncTask;
import android.widget.ImageView;
import android.widget.TextView;


import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;

import cc.zsakvo.a99demo.BookDetailActivity;
import cc.zsakvo.a99demo.application.MyApplication;
import cc.zsakvo.a99demo.listener.Interface;
import cc.zsakvo.a99demo.utils.SplitUtil;

/**
 * Created by akvo on 2018/2/22.
 */

public class GetBookDetailTask extends AsyncTask<String,Void,String[]>{

    private Interface.GetBookDetailFinish gbdf;



    public GetBookDetailTask(Interface.GetBookDetailFinish gbdf){
        this.gbdf = gbdf;
    }

    @Override
    protected String[] doInBackground(String... strings) {
        String url = strings[0];
        String title = "";
        String intro = "";
        String detail = "";
        String coverUrl = "";
        try {
            Document doc = Jsoup.connect(url).timeout(20000).get();
            Element element_info = doc.getElementById("book_info");
            title = element_info.selectFirst("h2").text();
            coverUrl = "http://www.99lib.net" + element_info.selectFirst("img").attr("src");
            detail = "书籍简介："+element_info.selectFirst("div.intro").text();
            String author = SplitUtil.splitElement(element_info.selectFirst("h4"));
            int i = element_info.select("h4").size();
            if (i == 2){
                String label = SplitUtil.splitElement(element_info.select("h4").get(1),title,author.replace("作者:",""));
                intro = author+"\n\n"+label;
            }else if (i == 3){
                String trans = SplitUtil.splitElement(element_info.select("h4").get(1));
                String label = SplitUtil.splitElement(element_info.select("h4").get(2),title,author.replace("作者:",""));
                author = author + "  " + trans;
                intro = author+"\n\n"+label;
            }
        } catch (IOException e) {
            e.printStackTrace ();
        }
        return new String[]{title,intro,detail,coverUrl};
    }

    @Override
    protected void onPostExecute(String[] strings){
        super.onPostExecute (strings);
        if (strings[3].length ()!=0){
            gbdf.GetSuccessful (strings[0],strings[1],strings[2],strings[3]);
        }else {
            gbdf.GetFailed ();
        }
    }

}
