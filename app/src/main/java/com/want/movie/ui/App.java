package com.want.movie.ui;

import android.app.Application;
import android.content.Context;

import com.want.movie.model.data.MovieRepository;
import com.want.movie.model.data.MovieRepositoryMock;


/**
 * App contains a lot of hack logic for DI. It should be redesigned in testable way
 */
public class App extends Application {

    private static App instance;

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
    }

    public static Context getContext() {
        return instance.getApplicationContext();
    }

    public static MovieRepository getMovieRepository() {
        return MovieRepositoryMock.INSTANCE;
    }
}
