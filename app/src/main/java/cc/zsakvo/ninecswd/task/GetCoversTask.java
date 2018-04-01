package cc.zsakvo.ninecswd.task;

import android.os.AsyncTask;
import android.util.Log;

import com.alibaba.fastjson.JSON;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import cc.zsakvo.ninecswd.listener.Interface;

/**
 * Created by akvo on 2018/3/31.
 */

public class GetCoversTask extends AsyncTask<String,Void,List<String>> {

    private Interface.GetCoverUrls gcu;

    public GetCoversTask(Interface.GetCoverUrls gcu){
        this.gcu = gcu;
    }

    @Override
    protected List<String> doInBackground(String... strings) {
        String engine = strings[1];
        switch (engine){
            case "Bing":
                return searchBing(strings[0]);
        }
        return null;
    }

    private List<String> searchBing(String str){
        List<String> urls = new ArrayList<> ();
        try {
            Log.e ( "searchBing: ","http://cn.bing.com/images/search?sp=-1&q="+str+"&qft=+filterui:aspect-tall+filterui:imagesize-custom_300_430&FORM=IRFLTR" );
            Document doc = Jsoup.connect("http://cn.bing.com/images/search?sp=-1&q="+str+"&qft=+filterui:aspect-tall+filterui:imagesize-custom_300_430&FORM=IRFLTR").get();
            Elements elements = elements = doc.select("a[m*=murl]");
            for (Element e: elements){
                String s = e.toString ();
                String url = s.substring (s.indexOf ("murl")+4,s.indexOf ("turl"));
                String url_c = url.replace (":&quot;","")
                        .replace (",&quot;","")
                        .replace ("&quot;","");
                urls.add (url_c);
            }
            return urls;
        } catch (IOException e) {
            e.printStackTrace ();
            return null;
        }
    }

    @Override
    protected void onPostExecute(List<String> list){
        super.onPostExecute (list);
        if (list!=null){
            gcu.GetCoverUrls (list);
        }else{
            gcu.GetCoverUrlsFailed ();
        }
    }
}
