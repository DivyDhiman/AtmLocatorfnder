package Funality_class;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

import com.Ultimatecode.atmlocatorfinder.R;

import java.util.Objects;

public class SharedPrefall {


    private SharedPreferences Logindata;
    private static String file = "nearall";


    public void setAll(Context context,String key, Object value, String type) {
        Logindata = ((Activity) context).getSharedPreferences(file, 0);
        SharedPreferences.Editor editors = Logindata.edit();
        if(type.equals(context.getString(R.string.Integer))){
            editors.putInt(key, (Integer) value);
        }else if(type.equals(context.getString(R.string.String))){
            editors.putString(key, value.toString());
        }else if(type.equals(context.getString(R.string.Boolean))){
            editors.putBoolean(key, (Boolean) value);
        }
        editors.commit();
    }

    public  Object get_all(Context context,String key, String type) {
        Object result = null;
        Logindata = ((Activity) context).getSharedPreferences(file, 0);

        if(type.equals(context.getString(R.string.Integer))){
            result = Logindata.getInt(key, 0);
        }else if(type.equals(context.getString(R.string.String))){
            result = Logindata.getString(key, "AS");
        }else if(type.equals(context.getString(R.string.Boolean))){
            result = Logindata.getBoolean(key, false);
        }
        return result;
    }

    public void removekey(Context context,String key){
        Logindata = ((Activity) context).getSharedPreferences(file, 0);
        SharedPreferences.Editor editors = Logindata.edit();
        editors.remove(key);
        editors.apply();
    }
}
