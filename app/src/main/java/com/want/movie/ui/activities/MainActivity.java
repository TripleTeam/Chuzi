package com.want.movie.ui.activities;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SnapHelper;
import android.widget.Toast;

import com.want.movie.R;
import com.want.movie.model.entities.Filter;
import com.want.movie.model.entities.Movie;
import com.want.movie.ui.App;
import com.want.movie.ui.adapters.MoviesAdapter;
import com.want.movie.ui.decorations.HorizontalSpaceDecoration;
import com.want.movie.ui.util.StartSnapHelper;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import io.reactivex.Scheduler;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

public class MainActivity extends AppCompatActivity {

    RecyclerView movieRecyclerView;

    private final List<Movie> movies = new ArrayList<>();
    private final Filter filter = new Filter(50, 50, 50, 50);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initRecyclerView();

        fetchData();
    }

    private void initRecyclerView() {
        movieRecyclerView = findViewById(R.id.movieRecyclerView);
        movieRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        movieRecyclerView.setAdapter(new MoviesAdapter(movies));

        int spacePx = getResources().getDimensionPixelSize(R.dimen.movie_horizontal_space);
        movieRecyclerView.addItemDecoration(new HorizontalSpaceDecoration(spacePx));
        SnapHelper snapHelper = new StartSnapHelper();
        snapHelper.attachToRecyclerView(movieRecyclerView);
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
