package com.want.movie.ui.adapters;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Color;
import android.os.SystemClock;
import android.support.annotation.DrawableRes;
import android.support.v4.view.PagerAdapter;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

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
    private String[] texts = new String[]{
            "How many gun shots in the film?",
            "How much happiness in the film?",
            "Is the film bright enough?",
            "What about sexuality in the film?"
    };

    public FilterPagerAdapter(Context context, FilterAdapterCallback callback) {
        this.detector = new GestureDetector(context, new MyGestureDetector());
        this.callback = callback;
        filters = new FilterForPager[4];


        filters[0] = new FilterForPager(
                50f,
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
                50f,
                Color.parseColor("#80C2FF"),
                Color.parseColor("#0F1C3F"),
                R.drawable.lips1,
                R.drawable.lips2,
                R.drawable.lips3,
                R.drawable.lips4,
                R.drawable.lips5
        );

        filters[3] = new FilterForPager(
                50f,
                Color.parseColor("#000000"),
                Color.parseColor("#000000"),
                R.drawable.h1,
                R.drawable.h2,
                R.drawable.h3,
                R.drawable.h4,
                R.drawable.h5,
                R.drawable.h6,
                R.drawable.h7,
                R.drawable.h8,
                R.drawable.h9,
                R.drawable.h10,
                R.drawable.h11,
                R.drawable.h12,
                R.drawable.h13,
                R.drawable.h14,
                R.drawable.h15
        );
    }

    @Override
    public Object instantiateItem(final ViewGroup container, int position) {
        View view = LayoutInflater.from(container.getContext()).inflate(R.layout.item_filter, container, false);

        item = position;
        setState(view, filters[position]);
        setText(view, texts[position]);

        view.setTag(position);
        container.addView(view);
        return view;
    }

    private void setText(View view, String text) {
        TextView textView = view.findViewById(R.id.filterText);
        textView.setText(text);
    }

    private void setState(View view, FilterForPager filter) {
        ImageView image = view.findViewById(R.id.filter_image);

        float state = item == 2 ? Math.abs(filter.state) : filter.state;

        int ceil = (int) Math.floor((state / 101f) * filter.sprites.length);
        if (ceil < 0) ceil = 0;
        image.setImageResource(filter.sprites[ceil]);

        int r1 = (filter.fullColor & 0xFF0000) >> 16;
        int g1 = (filter.fullColor & 0x00FF00) >> 8;
        int b1 = filter.fullColor & 0x0000FF;
        int r2 = (filter.noneColor & 0xFF0000) >> 16;
        int g2 = (filter.noneColor & 0x00FF00) >> 8;
        int b2 = filter.noneColor & 0x0000FF;

        float t = state / 100;
        int rgb = Color.rgb(
                (int) (r1 * t + r2 * (1 - t)),
                (int) (g1 * t + g2 * (1 - t)),
                (int) (b1 * t + b2 * (1 - t))
        );
        view.setBackgroundColor(rgb);

        callback.changeState(item, state);
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

            float yFirst = event.getHistorySize() < 1 ? event.getY() : event.getHistoricalY(0);
            float yLast = event.getY();
            DisplayMetrics metrics = Resources.getSystem().getDisplayMetrics();
            Log.d("ONTOUCH", "d=" + metrics.densityDpi / 160f);


            float power = filters[item].state + (yFirst - yLast) / (metrics.densityDpi / 40f);
            if (item != 2) {
                if (power >= 100f) power = 100f;
                if (power <= 0f) power = 0f;
            } else {
                if (power >= 100f) power -= 200f;
                if (power <= -100f) power += 200f;
            }

            filters[item].state = power;
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

        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
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
