package com.want.movie.ui.activities;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.Toast;

import com.want.movie.R;
import com.want.movie.ui.adapter.FilterPagerAdapter;
import com.want.movie.model.entities.Filter;
import com.want.movie.model.entities.Movie;
import com.want.movie.ui.App;
import com.want.movie.ui.adapters.MoviesAdapter;
import com.want.movie.ui.decorations.HorizontalSpaceDecoration;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import io.reactivex.Scheduler;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

import java.util.Locale;

public class MainActivity extends AppCompatActivity implements FilterPagerAdapter.FilterAdapterCallback {


    private ViewPager pager;
    private FilterPagerAdapter adapter;
    private TextView f1;
    private TextView f2;
    private TextView f3;
    private TextView f4;

    RecyclerView movieRecyclerView;

    private final List<Movie> movies = new ArrayList<>();
    private final Filter filter = new Filter(50, 50, 50, 50);

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
        initRecyclerView();

        fetchData();
    }

    private void initRecyclerView() {
        movieRecyclerView = findViewById(R.id.movieRecyclerView);
        movieRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        movieRecyclerView.setAdapter(new MoviesAdapter(movies));

        int spacePx = getResources().getDimensionPixelSize(R.dimen.movie_horizontal_space);
        movieRecyclerView.addItemDecoration(new HorizontalSpaceDecoration(spacePx));
    }

    // TODO: 25/11/2017 get out from activity
    private void fetchData() {
        App
                .getMovieRepository()
                .getMovies(filter)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(onSuccess(), onError());
    }

    @NonNull
    private Consumer<List<Movie>> onSuccess() {
        return new Consumer<List<Movie>>() {
            @Override
            public void accept(List<Movie> movieList) throws Exception {
                // TODO: 25/11/2017 use DiffUtil and notify only changed items. it will be affect on UI
                movies.clear();
                movies.addAll(movieList);
                movieRecyclerView.getAdapter().notifyDataSetChanged();
            }
        };
    }

    @NonNull
    private Consumer<Throwable> onError() {
        return new Consumer<Throwable>() {
            @Override
            public void accept(Throwable throwable) throws Exception {
                // TODO: 25/11/2017 show some placeholder view for user
                Toast.makeText(MainActivity.this, "No connection", Toast.LENGTH_SHORT).show();
            }
        };
    }
}
