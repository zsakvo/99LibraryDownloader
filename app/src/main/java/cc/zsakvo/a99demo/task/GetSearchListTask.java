package cc.zsakvo.a99demo.task;

import android.os.AsyncTask;

import com.github.nukc.stateview.StateView;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.List;

import cc.zsakvo.a99demo.adapter.ListAdapter;
import cc.zsakvo.a99demo.classes.BookList;
import cc.zsakvo.a99demo.listener.OnDataFinishedListener;

/**
 * Created by akvo on 2018/2/22.
 */

public class GetSearchListTask extends AsyncTask<Object,Void,Integer> {
    private int totalPage;
    private List<BookList> listDetails;
    private OnDataFinishedListener onDataFinishedListener;
    private ListAdapter adapter;
    private StateView mStateView;
    public GetSearchListTask(int totalPage, List<BookList> listDetails, ListAdapter adapter,StateView mStateView){
        this.totalPage = totalPage;
        this.listDetails = listDetails;
        this.adapter = adapter;
        this.mStateView = mStateView;
    }

    @Override
    protected Integer doInBackground(Object... objects) {
        String url = (String) objects[0];
        int page = (int) objects[1];
        try {
            Document doc = Jsoup.connect(url).timeout(20000).get();
            if (page==1){
                listDetails.clear();
                totalPage = (int) Math.ceil((double)Integer.parseInt(doc.selectFirst("strong").text())/15.0);
            }
            Element element = doc.selectFirst("ul.list_box");
            Elements ele_li = element.select("li");
            for (Element e:ele_li){
                if (e.selectFirst("h2")==null){
                    return 0;
                }
                String title = e.selectFirst("h2").text();
                String author = splitElement(e.select("h4").get(0));
                String category = splitElement(e.select("h4").get(1));
                String label = splitElement(e.select("h4").get(2),title,author.replace("作者:",""));
                String intro = "简介："+e.selectFirst("div.intro").text();
                String book_url = "http://www.99lib.net"+e.selectFirst("a").attr("href");
                listDetails.add(new BookList(title,author+"\n"+category+"\n"+label,intro,book_url));
            }
            return 1;
        } catch (IOException e) {
            e.printStackTrace ();
            return 0;
        }
    }

    private String splitElement(Element element){
        Elements es = element.select("a");
        String str = "";
        for (Element e:es){
            String tmp = e.text()+" ";
            str = str + tmp;
        }
        element.select("a").remove();
        String tmp = element.text()+" ";
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
    protected void onPostExecute(Integer i){
        super.onPostExecute (i);
        if (i==0){
            mStateView.showEmpty();
        }else {
            mStateView.showContent();
            adapter.notifyDataSetChanged ();
            onDataFinishedListener.onDataSuccessfully (totalPage);
        }
    }

    public void setOnDataFinishedListener(
            OnDataFinishedListener onDataFinishedListener) {
        this.onDataFinishedListener = onDataFinishedListener;
    }
}
