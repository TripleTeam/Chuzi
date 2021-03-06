package com.want.movie.ui.activities;

import android.graphics.Color;
import android.media.AudioAttributes;
import android.media.SoundPool;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v4.view.ViewPager;
import android.support.v7.util.DiffUtil;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SnapHelper;
import android.util.Pair;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.want.movie.R;
import com.want.movie.model.entities.Filter;
import com.want.movie.model.entities.Movie;
import com.want.movie.model.navigators.MovieNavigator;
import com.want.movie.model.util.OldNewContainer;
import com.want.movie.ui.App;
import com.want.movie.ui.adapters.FilterPagerAdapter;
import com.want.movie.ui.adapters.MoviesAdapter;
import com.want.movie.ui.decorations.HorizontalSpaceDecoration;
import com.want.movie.ui.fragments.MovieInfoDialogFragment;
import com.want.movie.ui.util.GenericDiffUtilCallback;
import com.want.movie.ui.util.StartSnapHelper;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.BiFunction;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;
import io.reactivex.subjects.PublishSubject;
import me.relex.circleindicator.CircleIndicator;

public class MainActivity extends ActivityBase implements FilterPagerAdapter.FilterAdapterCallback, MovieNavigator {

    private static final String MOVIE_DETAIL_TAG = "movie_detail_tag";
    private static final long FILTER_DEBOUNCE_MILLIS = 300L;
    private boolean isPointerShown = false;
    private ViewPager pager;
    private FilterPagerAdapter adapter;
    private TextView f1;
    private TextView f2;
    private TextView f3;
    private TextView f4;

    private View r1;
    private View r2;
    private View r3;
    private View r4;

    private CompositeDisposable compositeDisposable = new CompositeDisposable();
    private RecyclerView movieRecyclerView;

    private final PublishSubject<Filter> filterPublishSubject = PublishSubject.create();
    private final List<Movie> movies = new ArrayList<>();
    private final Filter filter = new Filter(50, 50, 50, 50);

    private SoundPool pool;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initStatusBar();

        initViews();
        initRecyclerView();
        initClickableTabs();

        subscribeToFilterUpdates();
        filterPublishSubject.onNext(filter);
    }

    private int sLoad;
    private int sDrop;
    private int sShot;
    private int sWhistle;

    @Override
    protected void onResume() {
        super.onResume();
        if (pool == null) {
            pool = new SoundPool.Builder()
                    .setAudioAttributes(new AudioAttributes.Builder().setUsage(AudioAttributes.USAGE_MEDIA).build())
                    .setMaxStreams(16)
                    .build();
            sLoad = pool.load(MainActivity.this, R.raw.clip_load, 1);
            sDrop = pool.load(MainActivity.this, R.raw.bullet_drop, 1);
            sWhistle = pool.load(MainActivity.this, R.raw.whistle, 1);
            sShot = pool.load(MainActivity.this, R.raw.gun_shot, 1);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        pool.release();
        pool = null;
    }

    private void initStatusBar() {
        getWindow().getDecorView().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
    }

    private void initViews() {
        f1 = findViewById(R.id.filter_1);
        f2 = findViewById(R.id.filter_2);
        f3 = findViewById(R.id.filter_3);
        f4 = findViewById(R.id.filter_4);

        r1 = findViewById(R.id.round1);
        r2 = findViewById(R.id.round2);
        r3 = findViewById(R.id.round3);
        r4 = findViewById(R.id.round4);


        pager = findViewById(R.id.main_pager);
        adapter = new FilterPagerAdapter(this, this);
        pager.setAdapter(adapter);
        pager.setOffscreenPageLimit(4);
        CircleIndicator indicator = findViewById(R.id.indicator);
        indicator.setViewPager(pager);

        pager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                pageSelected(position);
                if (position == 1 && selectedPage == 0 && filter.getBullets() == 100) {
                    if (pool != null) {
                        pool.play(sShot, 1f, 1f, 1, 0, 1f);
                    }
                }
                selectedPage = position;
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        pageSelected(0);
    }

    int selectedPage = 0;

    private void pageSelected(int position) {
        FilterPagerAdapter.FilterForPager[] filters = adapter.filters;
        FilterPagerAdapter.FilterForPager filter = filters[position];
        int backgroundColor = adapter.getFilterColor(filter);
        int textColor = backgroundColorToTextColor(backgroundColor);
        if (position == 0) {
            r2.setBackgroundColor(Color.TRANSPARENT);
            r3.setBackgroundColor(Color.TRANSPARENT);
            r4.setBackgroundColor(Color.TRANSPARENT);

            f2.setTextColor(Color.WHITE);
            f3.setTextColor(Color.WHITE);
            f4.setTextColor(Color.WHITE);


            r1.setBackgroundColor(backgroundColor);
            f1.setTextColor(textColor);
        } else if (position == 1) {
            r1.setBackgroundColor(Color.TRANSPARENT);
            r3.setBackgroundColor(Color.TRANSPARENT);
            r4.setBackgroundColor(Color.TRANSPARENT);


            f1.setTextColor(Color.WHITE);
            f3.setTextColor(Color.WHITE);
            f4.setTextColor(Color.WHITE);

            r2.setBackgroundColor(backgroundColor);
            f2.setTextColor(textColor);
        } else if (position == 2) {
            r1.setBackgroundColor(Color.TRANSPARENT);
            r2.setBackgroundColor(Color.TRANSPARENT);
            r4.setBackgroundColor(Color.TRANSPARENT);


            f1.setTextColor(Color.WHITE);
            f2.setTextColor(Color.WHITE);
            f4.setTextColor(Color.WHITE);

            r3.setBackgroundColor(backgroundColor);
            f3.setTextColor(textColor);
        } else if (position == 3) {
            r1.setBackgroundColor(Color.TRANSPARENT);
            r2.setBackgroundColor(Color.TRANSPARENT);
            r3.setBackgroundColor(Color.TRANSPARENT);


            f1.setTextColor(Color.WHITE);
            f2.setTextColor(Color.WHITE);
            f3.setTextColor(Color.WHITE);
            r4.setBackgroundColor(backgroundColor);
            f4.setTextColor(textColor);
        }

    }

    private void initClickableTabs() {
        initClickableTab(r1, 0);
        initClickableTab(r2, 1);
        initClickableTab(r3, 2);
        initClickableTab(r4, 3);
    }

    private void initClickableTab(View view, final int position) {
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pager.setCurrentItem(position);
            }
        });
    }


    @Override
    public void changeState(int pos, float value, int color, boolean playSound, int sound, boolean ignoreColor) {
        int intValue = (int) value;
        String text = String.format(Locale.US, "%d%%", intValue);
        switch (pos) {
            case 0:
                updateBullets(text, intValue, color, ignoreColor);
                if (playSound && pool != null) {
                    if (sound == 0) pool.play(sLoad, 1f, 1f, 1, 0, 1f);
                    if (sound == 1) pool.play(sDrop, 1f, 1f, 1, 0, 1f);
                }
                break;
            case 1:
                updateHappiness(text, intValue, color, ignoreColor);
                break;
            case 2:
                updateBrightness(text, intValue, color, ignoreColor);
                break;
            case 3:
                updateSexuality(text, intValue, color, ignoreColor);
                if (playSound && pool != null) {
                    pool.play(sWhistle, 0.5f, 0.5f, 1, 0, 1f);
                }
                break;
        }

        filterPublishSubject.onNext(filter);
    }

    private void clearBackroundsAndTextColor() {
        r1.setBackgroundColor(Color.TRANSPARENT);
        r2.setBackgroundColor(Color.TRANSPARENT);
        r3.setBackgroundColor(Color.TRANSPARENT);
        r4.setBackgroundColor(Color.TRANSPARENT);

        f1.setTextColor(Color.WHITE);
        f2.setTextColor(Color.WHITE);
        f3.setTextColor(Color.WHITE);
        f4.setTextColor(Color.WHITE);
    }


    private void updateBullets(String text, int intValue, int color, boolean ignoreColor) {
        f1.setText(text);
        filter.setBullets(intValue);
        if (!ignoreColor) {
            r1.setBackgroundColor(color);
            int textColor = backgroundColorToTextColor(color);
            f1.setTextColor(textColor);
        }
    }

    private void updateHappiness(String text, int intValue, int color, boolean ignoreColor) {
        f2.setText(text);
        filter.setHappiness(intValue);
        if (!ignoreColor) {
            r2.setBackgroundColor(color);
            int textColor = backgroundColorToTextColor(color);
            f2.setTextColor(textColor);
        }
    }

    private void updateBrightness(String text, int intValue, int color, boolean ignoreColor) {
        f3.setText(text);
        filter.setBrightness(intValue);
        if (!ignoreColor) {
            r3.setBackgroundColor(color);
            int textColor = backgroundColorToTextColor(color);
            f3.setTextColor(textColor);
        }
    }

    private void updateSexuality(String text, int intValue, int color, boolean ignoreColor) {
        f4.setText(text);
        filter.setSexuality(intValue);
        if (!ignoreColor) {
            r4.setBackgroundColor(color);
            int textColor = backgroundColorToTextColor(color);
            f4.setTextColor(textColor);
        }
    }

    private int backgroundColorToTextColor(int color) {
        int r = (color & 0xFF0000) >> 16;
        int g = (color & 0x00FF00) >> 8;
        int b = color & 0x0000FF;
        return 1 - (0.299 * r + 0.587 * g + 0.114 * b) / 255 < 0.5 ? Color.BLACK : Color.WHITE;
    }

    private void initRecyclerView() {
        movieRecyclerView = findViewById(R.id.movieRecyclerView);
        movieRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        movieRecyclerView.setAdapter(new MoviesAdapter(movies, this));

        int spacePx = getResources().getDimensionPixelSize(R.dimen.movie_horizontal_space);
        movieRecyclerView.addItemDecoration(new HorizontalSpaceDecoration(spacePx));
        SnapHelper snapHelper = new StartSnapHelper();
        snapHelper.attachToRecyclerView(movieRecyclerView);
    }

    // TODO: 25/11/2017 get out from activity
    private void subscribeToFilterUpdates() {
        Disposable disposable = filterPublishSubject
                .observeOn(AndroidSchedulers.mainThread())
                .debounce(FILTER_DEBOUNCE_MILLIS, TimeUnit.MILLISECONDS)
                .observeOn(Schedulers.io())
                .flatMap(new Function<Filter, Observable<OldNewContainer<Movie>>>() {
                             @Override
                             public Observable<OldNewContainer<Movie>> apply(Filter filter) throws Exception {
                                 return oldNewMoviesStream(filter);
                             }
                         }
                )
                .map(wrapWithDiffUtil())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(onSuccess(), onError());
        compositeDisposable.add(disposable);
    }


    private Observable<OldNewContainer<Movie>> oldNewMoviesStream(Filter filter) {
        return Single.zip(Single.just(movies), App.getMovieRepository().getMovies(filter), new BiFunction<List<Movie>, List<Movie>, OldNewContainer<Movie>>() {
            @Override
            public OldNewContainer<Movie> apply(List<Movie> oldList, List<Movie> newList) throws Exception {
                return new OldNewContainer<>(oldList, newList);
            }
        }).toObservable();
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


    @NonNull
    private Consumer<Pair<DiffUtil.DiffResult, List<Movie>>> onSuccess() {
        return new Consumer<Pair<DiffUtil.DiffResult, List<Movie>>>() {
            @Override
            public void accept(Pair<DiffUtil.DiffResult, List<Movie>> diffResultListPair) throws Exception {
                movies.clear();
                movies.addAll(diffResultListPair.second);

//                movieRecyclerView.getAdapter().notifyDataSetChanged();
                diffResultListPair.first.dispatchUpdatesTo(
                        movieRecyclerView.getAdapter()
                );
                movieRecyclerView.scrollToPosition(0);
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
                subscribeToFilterUpdates();
            }
        };
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        compositeDisposable.clear();
    }

    @Override
    public void navigateToMovie(Movie movie) {
        DialogFragment dialogFragment = MovieInfoDialogFragment.newInstance(movie);
        if (!dialogFragment.isAdded()) {
            dialogFragment.show(getSupportFragmentManager(), MOVIE_DETAIL_TAG);
        }
    }
}
