package cc.zsakvo.ninecswd.task;

import android.os.AsyncTask;
import android.util.Log;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.JSONSerializer;
import com.alibaba.fastjson.serializer.PropertyFilter;
import com.alibaba.fastjson.serializer.SerializeWriter;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
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
            case "Baidu":
                return searchBaidu (strings[0]);
        }
        return null;
    }

    private List<String> searchBaidu(String str){

        PropertyFilter filter = new PropertyFilter () {
            public boolean apply(Object source, String name, Object value) {
                if (((String)name).contains ("objURL")) {
                    return true;
                }else {
                    return false;
                }
            }
        };

        List<String> list = new ArrayList<> ();

        try {
            Document document = Jsoup.connect("https://image.baidu.com/search/index?tn=baiduimage&word="+str).get();
            String content = document.body().toString();
            content = content.substring(content.indexOf("setData('imgData',") + "setData('imgData',".length ());
            content = content.substring(0, content.indexOf(");"));
            JSONArray array = JSON.parseObject(content).getJSONArray("data");
            String json = JSON.toJSONString(array, filter);
            JSONArray array_n = JSON.parseArray (json);
            for (Iterator iterator = array_n.iterator(); iterator.hasNext();) {
                JSONObject job = (JSONObject) iterator.next();
                try {
                    list.add (job.get ("objURL").toString ());
                }catch (Exception e){

                }
            }
            return list;
        } catch (IOException e) {
            e.printStackTrace ();
        }
        return null;
    }

    private List<String> searchBing(String str){
        List<String> urls = new ArrayList<> ();
        try {
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
