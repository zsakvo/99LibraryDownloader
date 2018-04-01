package cc.zsakvo.ninecswd;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import java.util.List;

import cc.zsakvo.ninecswd.adapter.CoverListAdapter;
import cc.zsakvo.ninecswd.listener.Interface;
import cc.zsakvo.ninecswd.listener.ItemClickListener;
import cc.zsakvo.ninecswd.task.GetCoversTask;

public class ChangeCoverActivity extends AppCompatActivity implements Interface.GetCoverUrls,ItemClickListener{

//    public enum Engine {
//        Bing,
//        Baidu,
//        Sogou
//    }

    Toolbar toolbar;
    String[] engine = new String[]{"Bing","Baidu","Sogou"};
    private String searchStr;
    private String searchEngine = "Bing";
    private RecyclerView recyclerView;
    private CoverListAdapter adapter;
    private List<String> list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate (savedInstanceState);
        setContentView (R.layout.activity_change_cover);
        toolbar = (Toolbar)findViewById (R.id.ccToolBar);
        toolbar.setTitle ("选择封面");
        recyclerView = (RecyclerView)findViewById (R.id.cover_recycler);
        setSupportActionBar (toolbar);
        if (getSupportActionBar ()!=null){
            getSupportActionBar ().setDisplayHomeAsUpEnabled (true);
        }
        searchStr = getIntent ().getStringExtra ("str");
        new GetCoversTask (this).execute (searchStr,searchEngine);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.select_engine_menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(final MenuItem item) {
        if(item.getItemId()==android.R.id.home){
            finish ();
            return true;
        }
        switch (item.getItemId ()){
            case android.R.id.home:
                finish ();
                break;
            case R.id.select_engine:
                AlertDialog dialog = new AlertDialog.Builder(this).setTitle("选择搜索引擎")
                        .setItems(engine, new DialogInterface.OnClickListener () {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                switch (engine[which]){
                                    case "Bing":
                                        item.setIcon (R.drawable.ic_bing);
                                        break;
                                    case "Baidu":
                                        item.setIcon (R.drawable.ic_baidu);
                                        break;
                                    case "Sogou":
                                        item.setIcon (R.drawable.ic_bing);
                                        break;
                                        default:
                                            break;
                                }
                                Toast.makeText(ChangeCoverActivity.this, engine[which], Toast.LENGTH_SHORT).show();
                            }
                        }).create();
                dialog.show();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void GetCoverUrls(List<String> list) {
        this.list = list;
        StaggeredGridLayoutManager layoutManager = new
                StaggeredGridLayoutManager(3,StaggeredGridLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);
        adapter = new CoverListAdapter (list);
        adapter.setOnItemClickListener (this);
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void GetCoverUrlsFailed() {

    }

    @Override
    public void onItemClick(View view, int postion) {
        Log.e ( "onItemClick: ",list.get (postion));
        Intent intent = new Intent ();
        intent.putExtra ("url",list.get (postion));
        setResult(1, intent);
        finish();
    }

    @Override
    public void onBackPressed(){
        super.onBackPressed ();
    }
}
