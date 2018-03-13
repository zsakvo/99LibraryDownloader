//package cc.zsakvo.a99demo;
//
///**
// * Created by akvo on 2018/3/5.
// */
//
//import android.content.Context;
//import android.util.AttributeSet;
//import android.util.Log;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.widget.Button;
//import android.widget.ImageView;
//import android.widget.LinearLayout;
//import android.widget.TextView;
//
///**
// * Created by yzq on 2016/10/18.
// * 控制布局的切换
// */
//
//public class ShowView extends LinearLayout implements View.OnClickListener {
//
//    private View view;
//
//    private int viewID;
//    private LinearLayout loaddingLayout;//加载中布局
//    private LinearLayout abnormalLayout;//异常布局  （无数据，无网络，请求超时）
//    private ImageView hintImg;//提示图片
//    private TextView hintTv;//提示文字
//    private Button refreshBtn;//刷新按钮
//
//    public static final int LOADDING = 0;//加载中
//    public static final int NO_DATA = 1;//无数据
//    public static final int NO_NET = 2;//无网络
//    public static final int TIME_OUT = 3;//超时
//
//    private RetryListerner retryListener;//重试
//
//    public ShowView(Context context,int viewID) {
//
//        super(context);
//        this.viewID = viewID;
//        initView(context);
//    }
//
//    public ShowView(Context context, AttributeSet attrs) {
//        super(context, attrs);
//        initView(context);
//    }
//
//    public ShowView(Context context, AttributeSet attrs, int defStyleAttr) {
//        super(context, attrs, defStyleAttr);
//        initView(context);
//    }
//
//
//    private void initView(Context context) {
//
//        Log.i("初始化view", "initView");
//        LayoutInflater inflater = (LayoutInflater) context
//                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//
//        view = inflater.inflate(viewID, this);
//        loaddingLayout = (LinearLayout) view.findViewById(R.id.loaddingLayout);
//        abnormalLayout = (LinearLayout) view.findViewById(R.id.abnormalLayout);
//
//        hintImg = (ImageView) view.findViewById(R.id.hintImg);
//        hintTv = (TextView) view.findViewById(R.id.hintTv);
//        refreshBtn = (Button) view.findViewById(R.id.refreshBtn);
//
//        refreshBtn.setOnClickListener(this);
//
//        show(LOADDING);//显示加载中
//
//    }
//
//    @Override
//    public void onClick(View v) {
//        show(LOADDING);//显示加载中
//        retryListener.retry();//执行重试逻辑
//    }
//
//
//    /*设置重试监听*/
//    public void setOnRetryListener(RetryListerner retryListener) {
//        this.retryListener = retryListener;
//    }
//
//    public void show(int i) {
//        view.setVisibility(VISIBLE);
//        switch (i) {
//            case LOADDING:
//                /*加载中*/
//                loaddingLayout.setVisibility(VISIBLE);
//                abnormalLayout.setVisibility(GONE);
//
//                break;
//
//            case NO_DATA:
//                /*暂无数据*/
//                loaddingLayout.setVisibility(GONE);
//                abnormalLayout.setVisibility(VISIBLE);
//
//                hintImg.setImageResource(R.drawable.no_data);
//                hintTv.setText(R.string.noData);
//
//                break;
//            case NO_NET:
//                /*无网络*/
//                loaddingLayout.setVisibility(GONE);
//                abnormalLayout.setVisibility(VISIBLE);
//                hintImg.setImageResource(R.drawable.no_net);
//                hintTv.setText(R.string.noNet);
//
//                break;
//            case TIME_OUT:
//                /*超时*/
//                loaddingLayout.setVisibility(GONE);
//                abnormalLayout.setVisibility(VISIBLE);
//
//                hintImg.setImageResource(R.drawable.no_data);
//                hintTv.setText(R.string.timeOut);
//                break;
//        }
//
//    }
//
//    public interface RetryListerner {
//        void retry();
//    }
//
//}
