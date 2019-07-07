package Funality_class;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.widget.EditText;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Check_all {

    public boolean isNetWorkStatusAvialable(Context applicationContext) {
        ConnectivityManager connectivityManager = (ConnectivityManager)applicationContext.getSystemService(Context.CONNECTIVITY_SERVICE);
        if(connectivityManager != null){
            NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
            if(networkInfo != null) {
                if (networkInfo.isConnected())
                    return true;
            }
        }
        return false;
    }

    public void Snackbarshow(CoordinatorLayout coordinatelayout,String message){
        Snackbar snackbar = Snackbar
                .make(coordinatelayout, message, Snackbar.LENGTH_LONG);

        snackbar.show();

    }

}
