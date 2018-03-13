package cc.zsakvo.a99demo.task;

import android.os.AsyncTask;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;

import cc.zsakvo.a99demo.BookDetailActivity;
import cc.zsakvo.a99demo.application.MyApplication;

/**
 * Created by akvo on 2018/2/22.
 */

public class GetBookDetailTask extends AsyncTask<String,Void,String[]>{

    private TextView tv_title;
    private TextView tv_intro;
    private TextView tv_detail;
    private ImageView iv_cover;



    public GetBookDetailTask(TextView tv_title, TextView tv_intro, TextView tv_detail, ImageView iv_cover){
        this.tv_title = tv_title;
        this.tv_intro = tv_intro;
        this.tv_detail = tv_detail;
        this.iv_cover = iv_cover;
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
            String author = splitElement(element_info.selectFirst("h4"));
            int i = element_info.select("h4").size();
            if (i == 2){
                String label = splitElement(element_info.select("h4").get(1),title,author.replace("作者:",""));
                intro = author+"\n\n"+label;
            }else if (i == 3){
                String trans = splitElement(element_info.select("h4").get(1));
                String label = splitElement(element_info.select("h4").get(2),title,author.replace("作者:",""));
                author = author + "  " + trans;
                intro = author+"\n\n"+label;
            }
        } catch (IOException e) {
            e.printStackTrace ();
        }
        return new String[]{title,intro,detail,coverUrl};
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

    @Override
    protected void onPostExecute(String[] strings){
        super.onPostExecute (strings);
        tv_title.setText(strings[0]);
        tv_intro.setText(strings[1]);
        tv_detail.setText(strings[2]);
        Glide.with(MyApplication.getContext ())
                .load(strings[3])
                .into(iv_cover);
    }

}
