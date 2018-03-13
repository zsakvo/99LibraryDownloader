package cc.zsakvo.a99demo.task;

import android.os.AsyncTask;
import android.support.v7.widget.Toolbar;
import android.widget.TextView;

import com.github.nukc.stateview.StateView;
import com.zzhoujay.richtext.RichText;

import cc.zsakvo.a99demo.utils.DecodeUtils;

/**
 * Created by akvo on 2018/2/21.
 */

public class GetArticleContentTask extends AsyncTask<String, Integer,String> {

    private TextView textView;
    private Toolbar toolbar;
    private StateView mStateView;

    public GetArticleContentTask(TextView textView, Toolbar toolbar,StateView mStateView){
        this.textView = textView;
        this.toolbar = toolbar;
        this.mStateView = mStateView;
    }


    @Override
    protected String doInBackground(String ... params) {
        return DecodeUtils.url (params[0]);
    }

    @Override
    protected void onPostExecute(String content){
        super.onPostExecute (content);
        if (content==null){
            mStateView.showEmpty ();
        }else {
            RichText.fromHtml (content).into (textView);
        }
    }
}
