package cc.zsakvo.ninecswd;

import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

public class ChangeCoverActivity extends AppCompatActivity {

//    public enum Engine {
//        Bing,
//        Baidu,
//        Sogou
//    }

    Toolbar toolbar;
    String[] engine = new String[]{"Bing","Baidu","Sogou"};
    private String searchStr;
    private int searchEngine = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate (savedInstanceState);
        setContentView (R.layout.activity_change_cover);
        toolbar = (Toolbar)findViewById (R.id.ccToolBar);
        toolbar.setTitle ("选择封面");
        setSupportActionBar (toolbar);
        if (getSupportActionBar ()!=null){
            getSupportActionBar ().setDisplayHomeAsUpEnabled (true);
        }
        searchStr = getIntent ().getStringExtra ("str");
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

}
