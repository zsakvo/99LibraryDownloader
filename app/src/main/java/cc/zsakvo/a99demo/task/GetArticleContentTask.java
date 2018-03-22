package cc.zsakvo.a99demo.task;

import android.os.AsyncTask;
import android.support.v7.widget.Toolbar;
import android.widget.TextView;

import com.github.nukc.stateview.StateView;
import com.zzhoujay.richtext.RichText;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import cc.zsakvo.a99demo.listener.Interface;
import cc.zsakvo.a99demo.utils.DecodeUtils;

/**
 * Created by akvo on 2018/2/21.
 */

public class GetArticleContentTask extends AsyncTask<String, Integer,String[]> {

    private TextView textView;
    private Toolbar toolbar;
    private StateView mStateView;
    private Interface.GetArticle ga;

    public GetArticleContentTask(TextView textView, Toolbar toolbar, StateView mStateView, Interface.GetArticle ga){
        this.textView = textView;
        this.toolbar = toolbar;
        this.mStateView = mStateView;
        this.ga = ga;
    }


    @Override
    protected String[] doInBackground(String ... params) {
        String content =  DecodeUtils.url (params[0],1);
        if (content==null){
            return new String[]{null,null};
        }else {
            Document doc = Jsoup.parse (content);
            String title = doc.getElementById ("title").text ();
            content = content.replace ("<h2 id=\"title\" class=\"titlel2std\">" + title + "</h2>", "");
            return new String[]{title, content};
        }
    }

    @Override
    protected void onPostExecute(String[] strings){
        super.onPostExecute (strings);
        if (strings[0]==null){
//            mStateView.showEmpty ();
            ga.GetResult (false);
        }else {
            toolbar.setTitle (strings[0]);
            RichText.fromHtml (strings[1]).into (textView);
            ga.GetResult (true);
        }
    }
}
