package adpaterrecycler;

import android.app.Activity;
import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageView;

/**
 * Created by dhiman on 02-07-2016.
 */
public class ViewPagerAdapter extends PagerAdapter {

    Activity activity;
    int imageArray[];

    public ViewPagerAdapter(Activity act, int[] imgArra) {
        imageArray = imgArra;
        activity = act;
    }

    public int getCount() {
        return imageArray.length;
    }

    public Object instantiateItem(ViewGroup collection, int position) {
        ImageView view = new ImageView(activity);
        view.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT,
                LayoutParams.MATCH_PARENT));
        view.setScaleType(ImageView.ScaleType.FIT_XY);
        view.setBackgroundResource(imageArray[position]);
        ((ViewPager) collection).addView(view, 0);
        return view;
    }

    @Override
    public void destroyItem(ViewGroup arg0, int arg1, Object arg2) {
        ((ViewPager) arg0).removeView((View) arg2);
    }

    @Override
    public boolean isViewFromObject(View arg0, Object arg1)
    {
        return arg0 == ((View) arg1);
    }

    @Override
    public Parcelable saveState() {
        return null;
    }
}
