package com.Ultimatecode.atmlocatorfinder;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.Locale;

import controlers.all.AnalyticsApplication;
import helpwindow.MainSwipe;

public class Splash extends Activity implements Animation.AnimationListener {

    private Context context;
    private boolean checkfirsthelp;
    private Intent intent;
    private ImageView splashlogo;
    private TextView splash_atmbank1, splash_powerby;
    private Animation mAnim = null;
    private boolean checksplash;
    private AnalyticsApplication analyticsApp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash);

        context = Splash.this;
        analyticsApp = new AnalyticsApplication();
        checksplash = false;
        splashlogo = (ImageView) findViewById(R.id.splash_logo);
        splash_atmbank1 = (TextView) findViewById(R.id.splash_atmbank);
        splash_powerby = (TextView) findViewById(R.id.splash_powerby);

        splash_atmbank1.setVisibility(View.GONE);
        splash_powerby.setVisibility(View.GONE);
        mAnim = AnimationUtils.loadAnimation(this, R.anim.translate);
        mAnim.setAnimationListener(this);
        splashlogo.clearAnimation();
        splashlogo.setAnimation(mAnim);
        checkfirsthelp = (boolean) analyticsApp.get_all(context, "helpmenu", getString(R.string.Boolean));
    }

    @Override
    public void onAnimationStart(Animation animation) {

    }

    @Override
    public void onAnimationEnd(Animation animation) {

        if (!checksplash) {
            splash_atmbank1.setVisibility(View.VISIBLE);
            splash_powerby.setVisibility(View.VISIBLE);

            mAnim = AnimationUtils.loadAnimation(this, R.anim.fade_in);
            mAnim.setAnimationListener(this);

            splash_atmbank1.startAnimation(mAnim);
            splash_powerby.startAnimation(mAnim);
            checksplash = true;

        } else {
            if (checkfirsthelp) {
                intent = new Intent(context, AfterSplash.class);
                startActivity(intent);
                finish();
            } else {
                intent = new Intent(context, MainSwipe.class);
                intent.putExtra("firstt", false);
                startActivity(intent);
                finish();
            }
        }
    }

    @Override
    public void onAnimationRepeat(Animation animation) {

    }
}
