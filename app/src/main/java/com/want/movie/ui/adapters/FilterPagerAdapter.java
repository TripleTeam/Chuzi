package com.want.movie.ui.adapters;

import android.content.Context;
import android.graphics.Color;
import android.os.SystemClock;
import android.support.v4.view.PagerAdapter;
import android.util.Log;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import com.want.movie.R;

/**
 * Created by Slavik on 25-Nov-17.
 * Yippie-Kay-Yay!
 */

public class FilterPagerAdapter extends PagerAdapter implements View.OnTouchListener {

    private FilterAdapterCallback callback;
    private int item;
    private final GestureDetector detector;

    public FilterPagerAdapter(Context context, FilterAdapterCallback callback) {
        this.callback = callback;
        detector = new GestureDetector(context, new MyGestureDetector());
    }

    @Override
    public Object instantiateItem(final ViewGroup container, int position) {
        View view = LayoutInflater.from(container.getContext()).inflate(R.layout.item_filter, container, false);

        switch (position) {
            case 0:
                view.setBackgroundColor(Color.BLACK);
                break;
            case 1:
                view.setBackgroundColor(Color.RED);
                break;
            case 2:
                view.setBackgroundColor(Color.GREEN);
                break;
            case 3:
                view.setBackgroundColor(Color.BLUE);
                break;
        }

        view.setTag(position);
        container.addView(view);
        return view;
    }


    @Override
    public int getCount() {
        return 4;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return (view == object);
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }

    @Override
    public void setPrimaryItem(ViewGroup container, int position, Object object) {
        super.setPrimaryItem(container, position, object);
        for (int i = 0; i < container.getChildCount(); i++) {
            View childAt = container.getChildAt(i);
            childAt.setOnTouchListener(null);
        }
        View v = (View) object;


        v.setOnTouchListener(this);
        item = position;
    }

    long clickTime = 0;

    @Override
    public boolean onTouch(View v, MotionEvent event) {

        int action = event.getAction();

        switch (action) {
            case MotionEvent.ACTION_DOWN:
                clickTime = SystemClock.uptimeMillis();
                break;
            case MotionEvent.ACTION_UP:
                clickTime = 0;
                break;
        }

        if (detector.onTouchEvent(event) && clickTime != 0 && SystemClock.uptimeMillis() - clickTime > 100L) {
            float screenY = event.getY();
            float y = screenY - v.getTop();
            float t = (v.getHeight() - y) / v.getHeight();
            if (t > 1) t = 1;
            else if (t < 0) t = 0;
            float power = 100 * t;
//            Log.d("onTouch", "item=" + item + " power=" + power);
            callback.changeState(item, power);
            v.setBackgroundColor(Color.rgb((int) (255 * t + 0 * (1 - t)), (int) (0 * t + 255 * (1 - t)), (int) (0 * t + 0 * (1 - t))));
        }


        return true;
    }

    public interface FilterAdapterCallback {
        void changeState(int pos, float value);
    }

    private static final int SWIPE_MIN_DISTANCE = 120;
    private static final int SWIPE_MAX_OFF_PATH = 250;
    private static final int SWIPE_THRESHOLD_VELOCITY = 200;

    static class MyGestureDetector extends GestureDetector.SimpleOnGestureListener {
        @Override
        public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
            Log.d("onScroll", "x=" + distanceX + "y= " + distanceY);
            Log.d("onScroll", "e1=" + e1.getEventTime() + "e2= " + e2.getEventTime());
            Log.d("onScroll", "e1=" + e1.getDownTime() + "e2= " + e2.getDownTime());
            return true;
        }

        @Override
        public boolean onSingleTapConfirmed(MotionEvent e) {
            return true;
        }

        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {

            return false;
        }
    }
}
