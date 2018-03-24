package cc.zsakvo.ninecswd.utils;

import android.app.Dialog;
import android.content.Context;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import cc.zsakvo.ninecswd.R;

/**
 * Created by akvo on 2018/3/17.
 */

public class DialogUtils {

    private Context context;
    private Dialog dialog;
    private int progress = 0;
    private int allNum;

    public DialogUtils(Context context,Dialog dialog){
        this.context = context;
        this.dialog = dialog;
    }

    public void setAllNum(int i){
        this.allNum = i;
    }

    public void initDialog(){
        progress = 0;
            LayoutInflater inflater = LayoutInflater.from (context);
            View v = inflater.inflate (R.layout.dialog, null); // 得到加载view
            DisplayMetrics displaymetrics = new DisplayMetrics ();
            displaymetrics = context.getResources ().getDisplayMetrics ();
            int screenWidth = displaymetrics.widthPixels;
            v.setMinimumWidth ((int) (screenWidth * 0.8));
            LinearLayout layout = (LinearLayout) v.findViewById (R.id.dia_lay); // 加载布局
            dialog = new Dialog (context); // 创建自定义样式dialog
            dialog.setCancelable (false); // 不可以用"返回键"取消
            dialog.setContentView (layout, new LinearLayout.LayoutParams (
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT));
            dialog.show ();
    }

    public void setDialogTitle(String title){
        TextView d_tv = dialog.findViewById(R.id.dia_text);
        d_tv.setText(title);
    }

    public void concelDialog(){
        dialog.dismiss ();
    }

    public void addProgress(int allNum){
        progress++;
        String text = "正在下载章节……"+progress+"/"+allNum;
        setDialogTitle (text);
    }
}
