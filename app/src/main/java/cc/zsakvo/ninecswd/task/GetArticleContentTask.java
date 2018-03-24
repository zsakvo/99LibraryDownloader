package cc.zsakvo.ninecswd.task;

import android.os.AsyncTask;
import android.support.v7.widget.Toolbar;
import android.widget.TextView;

import com.github.nukc.stateview.StateView;
import com.zzhoujay.richtext.RichText;

import cc.zsakvo.ninecswd.listener.Interface;
import cc.zsakvo.ninecswd.utils.DecodeUtils;

/**
 * Created by akvo on 2018/2/21.
 */

public class GetArticleContentTask extends AsyncTask<String, Integer,String> {

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
    protected String doInBackground(String ... params) {
        String content =  DecodeUtils.url (params[0],2);
        if (content==null){
            return null;
        }else {
            return content;
        }
    }

    @Override
    protected void onPostExecute(String string){
        super.onPostExecute (string);
        if (string==null){
            ga.GetResult (false);
        }else {
            RichText.fromHtml (string).into (textView);
            ga.GetResult (true);
        }
    }
}
