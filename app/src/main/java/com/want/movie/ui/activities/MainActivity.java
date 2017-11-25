package com.want.movie.ui.activities;

import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.want.movie.R;
import com.want.movie.ui.adapter.FilterPagerAdapter;

import java.util.Locale;

public class MainActivity extends AppCompatActivity implements FilterPagerAdapter.FilterAdapterCallback {


    private ViewPager pager;
    private FilterPagerAdapter adapter;
    private TextView f1;
    private TextView f2;
    private TextView f3;
    private TextView f4;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initViews();
    }

    private void initViews() {
        pager = findViewById(R.id.main_pager);
        adapter = new FilterPagerAdapter(this, this);
        pager.setAdapter(adapter);
        pager.setOffscreenPageLimit(4);

        f1 = findViewById(R.id.filter_1);
        f2 = findViewById(R.id.filter_2);
        f3 = findViewById(R.id.filter_3);
        f4 = findViewById(R.id.filter_4);
    }

    @Override
    public void changeState(int pos, float value) {
        switch (pos) {
            case 0:
                f1.setText(String.format(Locale.US, "%.2f", value));
                break;
            case 1:
                f2.setText(String.format(Locale.US, "%.2f", value));
                break;
            case 2:
                f3.setText(String.format(Locale.US, "%.2f", value));
                break;
            case 3:
                f4.setText(String.format(Locale.US, "%.2f", value));
                break;
        }
    }
}
