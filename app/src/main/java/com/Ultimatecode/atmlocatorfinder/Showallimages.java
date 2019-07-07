package com.Ultimatecode.atmlocatorfinder;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.ActionMenuView;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;
import java.util.ArrayList;
import java.util.HashMap;

import adpaterrecycler.ImageAdapter;
import controlers.all.AnalyticsApplication;

public class Showallimages extends AppCompatActivity {

    private ArrayList<HashMap<String, Object>> imagelist;
    private AnalyticsApplication analyticsApplication;
    private Context context;
    private Activity activity;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.showall_images);
        context = Showallimages.this;

        activity = this;

        analyticsApplication = new AnalyticsApplication();

        setCustomActionBar();

        imagelist = (ArrayList<HashMap<String, Object>>) getIntent().getExtras().getSerializable("imagelist");

        RecyclerView allimages = (RecyclerView) findViewById(R.id.allimages);
        allimages.setLayoutManager(new GridLayoutManager(allimages.getContext(), 2));
        allimages.setAdapter(new ImageAdapter(allimages.getContext(), imagelist));
    }

    @Override
    protected void onStart() {
        super.onStart();

        if (analyticsApplication.InternetCheck(context)) {
            AnalyticsApplication application = (AnalyticsApplication) getApplication();
            Tracker mTracker = application.getDefaultTracker();
            mTracker.send(new HitBuilders.EventBuilder()
                    .setCategory("GalleryVisitor")
                    .setAction("Visitor")
                    .build());

        }
    }

    //Custom Action Bar
    private void setCustomActionBar() {

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        SearchView search = (SearchView) toolbar.findViewById(R.id.menu_search);
        search.setVisibility(View.GONE);


        getSupportActionBar().setTitle(R.string.virtualmap);

    }

}
