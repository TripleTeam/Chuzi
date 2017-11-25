package com.want.movie.ui;

import android.app.Application;
import android.content.Context;

import com.want.movie.R;
import com.want.movie.model.data.ApiMovieRepository;
import com.want.movie.model.data.MovieRepository;

import uk.co.chrisjenx.calligraphy.CalligraphyConfig;


/**
 * App contains a lot of hack logic for DI. It should be redesigned in testable way
 */
public class App extends Application {

    private static App instance;

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        CalligraphyConfig.initDefault(new CalligraphyConfig.Builder()
                .setDefaultFontPath("fonts/Futura-Medium.otf")
                .setFontAttrId(R.attr.fontPath)
                .build()
        );
    }

    public static Context getContext() {
        return instance.getApplicationContext();
    }

    public static MovieRepository getMovieRepository() {
        return ApiMovieRepository.INSTANCE;
    }
}
