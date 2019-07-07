package Funality_class;

import android.app.Activity;
import android.content.Context;

public class Animationall {

    public void Animall(Context context,int start, int end) {
        ((Activity) context).overridePendingTransition(start, end);;
    }
}
