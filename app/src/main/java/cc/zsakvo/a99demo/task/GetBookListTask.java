package cc.zsakvo.a99demo.task;

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


/**
 * Created by akvo on 2018/2/21.
 */

public class GetBookListTask extends AsyncTask<String,java.lang.Void,Integer> {

    private List<BookList> listDetails;
    private ListAdapter adapter;
    private StateView mStateView;

    public GetBookListTask(ListAdapter adapter, List<BookList> listDetails,StateView mStateView){
        this.listDetails = listDetails;
        this.adapter = adapter;
        this.mStateView = mStateView;
    }

    @Override
    protected void onPreExecute() {
        mStateView.showLoading ();
    }

    @Override
    protected Integer doInBackground(String... strings) {
        String url = strings[0];
        String page = url.substring (url.indexOf ("page=")+5);
        if (page.equals ("1")){
            listDetails.clear();
        }
        try {
            Document doc = Jsoup.connect (url).timeout (20000).get ();
            Element element = doc.getElementById("list_box");
            Elements ele_li = element.select("li");
            if (url.contains ("index.php?page")){
                getBookStoreDatas (ele_li);
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

    private void getBookStoreDatas(Elements ele_li){
        for (Element e:ele_li){
            String title = e.selectFirst("a").attr("title");
            String author = splitElement(e.select("h4").get(0));
            String category = splitElement(e.select("h4").get(1));
            String label = splitElement(e.select("h4").get(2),title,author.replace("作者:",""));
            String intro = "简介："+e.selectFirst("div.intro").text();
            String book_url = "http://www.99lib.net"+e.selectFirst("a").attr("href");
            listDetails.add(new BookList (title,author+"\n"+category+"\n"+label,intro,book_url));
        }
    }

    @Override
    protected void onPostExecute(Integer i){
        super.onPostExecute (i);
        if (i==0){
            mStateView.showEmpty ();
        }else {
            adapter.notifyDataSetChanged ();
            mStateView.showContent ();
        }
    }
}
