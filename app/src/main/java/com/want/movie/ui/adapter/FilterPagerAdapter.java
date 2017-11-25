package com.want.movie.ui.adapter;

import android.graphics.Color;
import android.support.v4.view.PagerAdapter;
import android.util.Log;
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

    public FilterPagerAdapter(FilterAdapterCallback callback) {
        this.callback = callback;
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

    @Override
    public boolean onTouch(View v, MotionEvent event) {

        int action = event.getAction();

        if (action == MotionEvent.ACTION_MOVE) {
            float screenY = event.getY();
            float y = screenY - v.getTop();
            float t = (v.getHeight() - y) / v.getHeight();
            if (t > 1) t = 1;
            else if (t < 0) t = 0;
            float power = 100 * t;
            Log.d("onTouch", "item=" + item + " power=" + power);
            callback.changeState(item, power);
        }


        return true;
    }

    public interface FilterAdapterCallback {
        void changeState(int pos, float value);
    }
}
