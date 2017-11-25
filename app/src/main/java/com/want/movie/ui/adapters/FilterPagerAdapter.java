package com.want.movie.ui.adapters;

import android.content.Context;
import android.graphics.Color;
import android.os.SystemClock;
import android.support.annotation.DrawableRes;
import android.support.v4.view.PagerAdapter;
import android.util.Log;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.want.movie.R;

/**
 * Created by Slavik on 25-Nov-17.
 * Yippie-Kay-Yay!
 */

public class FilterPagerAdapter extends PagerAdapter implements View.OnTouchListener {

    private FilterAdapterCallback callback;
    private int item;
    private final GestureDetector detector;
    private FilterForPager[] filters;

    public FilterPagerAdapter(Context context, FilterAdapterCallback callback) {
        this.detector = new GestureDetector(context, new MyGestureDetector());
        this.callback = callback;
        filters = new FilterForPager[4];


        filters[0] = new FilterForPager(
                24f,
                Color.parseColor("#3B2ED2"),
                Color.parseColor("#3B2ED2"),
                R.drawable.b0,
                R.drawable.b1,
                R.drawable.b2,
                R.drawable.b3,
                R.drawable.b4,
                R.drawable.b5,
                R.drawable.b6,
                R.drawable.b7,
                R.drawable.b8,
                R.drawable.b9,
                R.drawable.b10,
                R.drawable.b11,
                R.drawable.b12
        );

        filters[1] = new FilterForPager(
                50f,
                Color.parseColor("#FF6B6B"),
                Color.parseColor("#6B6B6B"),
                R.drawable.lips1,
                R.drawable.lips2,
                R.drawable.lips3,
                R.drawable.lips4,
                R.drawable.lips5
        );

        filters[2] = new FilterForPager(
                0,
                Color.RED,
                Color.GRAY,
                R.mipmap.ic_launcher);

        filters[3] = new FilterForPager(
                50,
                Color.parseColor("#27549C"),
                Color.parseColor("#27549C"),
                R.drawable.h13,
                R.drawable.h12,
                R.drawable.h11,
                R.drawable.h10,
                R.drawable.h9,
                R.drawable.h8,
                R.drawable.h7,
                R.drawable.h6,
                R.drawable.h5,
                R.drawable.h4,
                R.drawable.h3,
                R.drawable.h2,
                R.drawable.h1
        );
    }

    @Override
    public Object instantiateItem(final ViewGroup container, int position) {
        View view = LayoutInflater.from(container.getContext()).inflate(R.layout.item_filter, container, false);

        setState(view, filters[position]);

        view.setTag(position);
        container.addView(view);
        return view;
    }

    private void setState(View view, FilterForPager filter) {
        ImageView image = view.findViewById(R.id.filter_image);

        int ceil = (int) Math.floor((filter.state / 101f) * filter.sprites.length);
        if (ceil < 0) ceil = 0;
        image.setImageResource(filter.sprites[ceil]);
        Log.d("FILTER", "state=" + filter.state);
        Log.d("FILTER", "index=" + ceil);

        int r1 = (filter.fullColor & 0xFF0000) >> 16;
        int g1 = (filter.fullColor & 0x00FF00) >> 8;
        int b1 = filter.fullColor & 0x0000FF;
        int r2 = (filter.noneColor & 0xFF0000) >> 16;
        int g2 = (filter.noneColor & 0x00FF00) >> 8;
        int b2 = filter.noneColor & 0x0000FF;

        float t = filter.state / 100;
        int rgb = Color.rgb(
                (int) (r1 * t + r2 * (1 - t)),
                (int) (g1 * t + g2 * (1 - t)),
                (int) (b1 * t + b2 * (1 - t))
        );
        Log.d("FILTER", "color=" + Integer.toHexString(rgb));
        view.setBackgroundColor(rgb);

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

    private long clickTime = 0;

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
            float y = event.getY();
            float yTop = y - v.getTop();
            float yBot = y - v.getBottom();
            if (yTop < 0 || yBot > 0) return true;
//            float t = (v.getHeight() - y) / v.getHeight();
//            if (t > 1) t = 1;
//            else if (t < 0) t = 0;

            float yFirst = event.getHistorySize() < 1 ? event.getY() : event.getHistoricalY(0);
            float yLast = event.getY();
            Log.d("ONTOUCH", "f=" + yFirst + " l=" + yLast);

            float power = filters[item].state + (yFirst - yLast) / 10f;
            if (power >= 100f) power = 100f;
            if (power <= 0f) power = 0f;

            filters[item].state = power;
            callback.changeState(item, power);
            setState(v, filters[item]);
        }


        return true;
    }

    public interface FilterAdapterCallback {
        void changeState(int pos, float value);
    }

    static class MyGestureDetector extends GestureDetector.SimpleOnGestureListener {
        @Override
        public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
            return true;
        }

        @Override
        public boolean onSingleTapConfirmed(MotionEvent e) {
            return true;
        }

        private static final int SWIPE_MIN_DISTANCE = 120;
        private static final int SWIPE_MAX_OFF_PATH = 200;
        private static final int SWIPE_THRESHOLD_VELOCITY = 100;

        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
//            try {
//                // downward swipe
//                if (Math.abs(e1.getY() - e2.getY()) > SWIPE_MAX_OFF_PATH && Math.abs(velocityY) > SWIPE_THRESHOLD_VELOCITY)
//                    return true;
//                else if (Math.abs(e2.getY() - e1.getY()) > SWIPE_MAX_OFF_PATH && Math.abs(velocityY) > SWIPE_THRESHOLD_VELOCITY)
//                    return true;
//                    // right to left swipe
//                else return false;
//            } catch (Exception e) {
//                // nothing
//            }
            return false;
        }
    }

    static class FilterForPager {
        float state;

        int fullColor;

        int noneColor;

        @DrawableRes
        int[] sprites;

        public FilterForPager(float state, int fullColor, int noneColor, @DrawableRes int... sprites) {
            this.state = state;
            this.fullColor = fullColor;
            this.noneColor = noneColor;
            this.sprites = sprites;
        }


    }


}
