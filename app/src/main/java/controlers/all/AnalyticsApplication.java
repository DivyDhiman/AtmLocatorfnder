package controlers.all;

import android.app.Application;
import android.content.Context;
import android.support.design.widget.CoordinatorLayout;
import android.widget.EditText;

import com.Ultimatecode.atmlocatorfinder.R;
import com.google.android.gms.analytics.GoogleAnalytics;
import com.google.android.gms.analytics.Tracker;

import Funality_class.Animationall;
import Funality_class.Check_all;
import Funality_class.DialougeAlert;
import Funality_class.SharedPrefall;


public class AnalyticsApplication extends Application {

    private Check_all checkAll = new Check_all();
    private Animationall animationall = new Animationall();
    private SharedPrefall sharedPrefall = new SharedPrefall();
    private DialougeAlert dialougeAlert = new DialougeAlert();
    private Tracker mTracker;

    public boolean InternetCheck(Context context){
        return checkAll.isNetWorkStatusAvialable(context);
    }

    public void Shackbarall(CoordinatorLayout coordinatelayout, String message){
        checkAll.Snackbarshow(coordinatelayout,message);
    }

    public void Animation_all(Context context,int start , int end){
        animationall.Animall(context,start,end);
    }

    public void set_all(Context context,String key, Object value, String type){
        sharedPrefall.setAll(context,key,value,type);
    }

    public Object get_all(Context context,String key, String type){
        return sharedPrefall.get_all(context,key,type);
    }

    public void Diall_show(Context context, int title, Object message){
        dialougeAlert.AlertMe(context,title,message);
    }

    public  void Remove_key(Context context,String key){
        sharedPrefall.removekey(context,key);
    }

    synchronized public Tracker getDefaultTracker() {
        if (mTracker == null) {
            GoogleAnalytics analytics = GoogleAnalytics.getInstance(this);
            // To enable debug logging use: adb shell setprop log.tag.GAv4 DEBUG
            mTracker = analytics.newTracker(R.xml.global_tracker);
        }
        return mTracker;
    }
}

