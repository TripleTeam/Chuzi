package com.want.movie.ui.activities;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.view.ViewPager;
import android.support.v7.util.DiffUtil;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SnapHelper;
import android.util.Pair;
import android.widget.TextView;
import android.widget.Toast;

import com.want.movie.R;
import com.want.movie.model.entities.Filter;
import com.want.movie.model.entities.Movie;
import com.want.movie.model.util.OldNewContainer;
import com.want.movie.ui.App;
import com.want.movie.ui.adapters.FilterPagerAdapter;
import com.want.movie.ui.adapters.MoviesAdapter;
import com.want.movie.ui.decorations.HorizontalSpaceDecoration;
import com.want.movie.ui.util.GenericDiffUtilCallback;
import com.want.movie.ui.util.StartSnapHelper;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.BiFunction;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

public class MainActivity extends ActivityBase implements FilterPagerAdapter.FilterAdapterCallback {


    private ViewPager pager;
    private FilterPagerAdapter adapter;
    private TextView f1;
    private TextView f2;
    private TextView f3;
    private TextView f4;

    private CompositeDisposable compositeDisposable = new CompositeDisposable();
    private RecyclerView movieRecyclerView;

    private final List<Movie> movies = new ArrayList<>();
    private final Filter filter = new Filter(50, 50, 50, 50);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initViews();
        initRecyclerView();

        fetchData();
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
        String text = String.format(Locale.US, "%.2f", value);
        switch (pos) {
            case 0:
                f1.setText(text);
                break;
            case 1:
                f2.setText(text);
                break;
            case 2:
                f3.setText(text);
                break;
            case 3:
                f4.setText(text);
                break;
        }
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
        Disposable disposable = oldNewMoviesStream()
                .map(wrapWithDiffUtil())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(onSuccess(), onError());
        compositeDisposable.add(disposable);
    }

    @NonNull
    private Function<OldNewContainer<Movie>, Pair<DiffUtil.DiffResult, List<Movie>>> wrapWithDiffUtil() {
        return new Function<OldNewContainer<Movie>, Pair<DiffUtil.DiffResult, List<Movie>>>() {
            @Override
            public Pair<DiffUtil.DiffResult, List<Movie>> apply(OldNewContainer<Movie> movieOldNewContainer) throws Exception {
                DiffUtil.Callback callback = new GenericDiffUtilCallback<>(movieOldNewContainer.getOldList(), movieOldNewContainer.getNewList());
                return new Pair<>(DiffUtil.calculateDiff(callback), movieOldNewContainer.getNewList());
            }
        };
    }

    private Single<OldNewContainer<Movie>> oldNewMoviesStream() {
        return Single.zip(Single.just(movies), App.getMovieRepository().getMovies(filter), new BiFunction<List<Movie>, List<Movie>, OldNewContainer<Movie>>() {
            @Override
            public OldNewContainer<Movie> apply(List<Movie> oldList, List<Movie> newList) throws Exception {
                return new OldNewContainer<>(oldList, newList);
            }
        });
    }


    @NonNull
    private Consumer<Pair<DiffUtil.DiffResult, List<Movie>>> onSuccess() {
        return new Consumer<Pair<DiffUtil.DiffResult, List<Movie>>>() {
            @Override
            public void accept(Pair<DiffUtil.DiffResult, List<Movie>> diffResultListPair) throws Exception {
                movies.clear();
                movies.addAll(diffResultListPair.second);

                diffResultListPair.first.dispatchUpdatesTo(movieRecyclerView.getAdapter());
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

    @Override
    protected void onDestroy() {
        super.onDestroy();
        compositeDisposable.clear();
    }
}
