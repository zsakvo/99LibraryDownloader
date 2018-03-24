package cc.zsakvo.ninecswd.application;

import android.app.Application;
import android.content.Context;

/**
 * Created by akvo on 2018/2/22.
 */

public class MyApplication extends Application {
    private static Context context;

    @Override
    public void onCreate() {
        super.onCreate ();
        context = getApplicationContext ();
    }

    public static Context getContext(){
        return context;
    }
}
