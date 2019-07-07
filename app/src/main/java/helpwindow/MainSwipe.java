package helpwindow;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.Ultimatecode.atmlocatorfinder.AfterSplash;
import com.Ultimatecode.atmlocatorfinder.R;

import adpaterrecycler.ViewPagerAdapter;
import controlers.all.AnalyticsApplication;

public class MainSwipe extends AppCompatActivity implements View.OnClickListener, ViewPager.OnPageChangeListener {

    private ImageView firstdot, seconddot, thirddot, fourthdot, fifthdot;
    private int color;
    private Context context;
    private TextView tvHelp;
    private Intent intent;
    private boolean firstt;
    private int[] mResources = {
            R.drawable.fisrtdashboard,
            R.drawable.seconddashboard,
            R.drawable.thirddashboard,
            R.drawable.fourthdashboard,
            R.drawable.fifthdashboard
    };
    private int[] mResources_bg = {
            R.drawable.mapbg,
            R.drawable.splashbg,
            R.drawable.aftersplashbg,
            R.drawable.mapbg,
            R.drawable.splashbg
    };

    private Button tvSkip, tvFinish;
    private String[] mIntroMsges;
    private AnalyticsApplication analyticsApplication;
    private ImageView image_change;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.swipe_screen);

        context = MainSwipe.this;
        analyticsApplication = new AnalyticsApplication();

        mIntroMsges = new String[]{
                getResources().getString(R.string.swipe1),
                getResources().getString(R.string.swipe2),
                getResources().getString(R.string.swipe3),
                getResources().getString(R.string.swipe4),
                getResources().getString(R.string.virtualmap)
        };


        firstt = getIntent().getExtras().getBoolean("firstt");

        if (!firstt) {
            analyticsApplication.set_all(context, "helpmenu", true, getString(R.string.Boolean));
        }

        initilize();
    }

    private void initilize() {


        ViewPagerAdapter adapter = new ViewPagerAdapter(this, mResources_bg);
        ViewPager myPager = (ViewPager) findViewById(R.id.viewPager);
        myPager.setAdapter(adapter);
        myPager.setCurrentItem(0);
        myPager.addOnPageChangeListener(this);

        image_change = (ImageView) findViewById(R.id.image_change);
        firstdot = (ImageView) findViewById(R.id.firstdot);
        seconddot = (ImageView) findViewById(R.id.seconddot);
        thirddot = (ImageView) findViewById(R.id.thirddot);
        fourthdot = (ImageView) findViewById(R.id.fourthdot);
        fifthdot = (ImageView) findViewById(R.id.fifthdot);
        tvSkip = (Button) findViewById(R.id.tvSkip);
        tvFinish = (Button) findViewById(R.id.tvFinish);
        tvHelp = (TextView) findViewById(R.id.tvHelp);
        tvSkip.setOnClickListener(this);
        tvFinish.setOnClickListener(this);

        color = Color.parseColor("#FFFFFF");

        firstdot.setImageResource(R.drawable.dotselected);
        seconddot.setImageResource(R.drawable.dotunselected);
        thirddot.setImageResource(R.drawable.dotunselected);
        fourthdot.setImageResource(R.drawable.dotunselected);
        fifthdot.setImageResource(R.drawable.dotunselected);

        firstdot.setColorFilter(color);
        seconddot.setColorFilter(color);
        thirddot.setColorFilter(color);
        fourthdot.setColorFilter(color);
        fifthdot.setColorFilter(color);

        tvFinish.setVisibility(View.GONE);
        tvHelp.setText(mIntroMsges[0]);
        image_change.setImageResource(mResources[0]);
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.tvSkip:

                if (firstt) {
                   GcMethod();
                    finish();
                } else {
                    GcMethod();
                    intent = new Intent(context, AfterSplash.class);
                    startActivity(intent);
                    finish();
                }
                break;
            case R.id.tvFinish:
                if (firstt) {
                    GcMethod();
                    finish();
                } else {
                    GcMethod();
                    intent = new Intent(context, AfterSplash.class);
                    startActivity(intent);
                    finish();
                }

                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        GcMethod();
    }


    private void GcMethod() {
        System.gc();
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
    }

    @Override
    public void onPageSelected(int dotpostion) {
        if (dotpostion == 0) {

            firstdot.setImageResource(R.drawable.dotselected);
            seconddot.setImageResource(R.drawable.dotunselected);
            thirddot.setImageResource(R.drawable.dotunselected);
            fourthdot.setImageResource(R.drawable.dotunselected);
            fifthdot.setImageResource(R.drawable.dotunselected);
            tvFinish.setVisibility(View.GONE);
            tvHelp.setText(mIntroMsges[0]);
            image_change.setImageResource(mResources[0]);

        } else if (dotpostion == 1) {

            firstdot.setImageResource(R.drawable.dotunselected);
            seconddot.setImageResource(R.drawable.dotselected);
            thirddot.setImageResource(R.drawable.dotunselected);
            fourthdot.setImageResource(R.drawable.dotunselected);
            fifthdot.setImageResource(R.drawable.dotunselected);
            tvFinish.setVisibility(View.GONE);

            tvHelp.setText(mIntroMsges[1]);
            image_change.setImageResource(mResources[1]);

        } else if (dotpostion == 2) {

            firstdot.setImageResource(R.drawable.dotunselected);
            seconddot.setImageResource(R.drawable.dotunselected);
            thirddot.setImageResource(R.drawable.dotselected);
            fourthdot.setImageResource(R.drawable.dotunselected);
            fifthdot.setImageResource(R.drawable.dotunselected);
            tvFinish.setVisibility(View.GONE);

            tvHelp.setText(mIntroMsges[2]);
            image_change.setImageResource(mResources[2]);

        } else if (dotpostion == 3) {

            firstdot.setImageResource(R.drawable.dotunselected);
            seconddot.setImageResource(R.drawable.dotunselected);
            thirddot.setImageResource(R.drawable.dotunselected);
            fourthdot.setImageResource(R.drawable.dotselected);
            fifthdot.setImageResource(R.drawable.dotunselected);
            tvFinish.setVisibility(View.GONE);

            tvHelp.setText(mIntroMsges[3]);
            image_change.setImageResource(mResources[3]);

        } else if (dotpostion == 4) {

            firstdot.setImageResource(R.drawable.dotunselected);
            seconddot.setImageResource(R.drawable.dotunselected);
            thirddot.setImageResource(R.drawable.dotunselected);
            fourthdot.setImageResource(R.drawable.dotunselected);
            fifthdot.setImageResource(R.drawable.dotselected);
            tvFinish.setVisibility(View.VISIBLE);

            tvHelp.setText(mIntroMsges[4]);
            image_change.setImageResource(mResources[4]);
        }

    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }
}