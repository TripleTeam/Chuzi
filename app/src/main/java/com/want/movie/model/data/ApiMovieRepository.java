package com.want.movie.model.data;

import android.support.annotation.NonNull;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.want.movie.model.entities.Filter;
import com.want.movie.model.entities.Movie;

import java.util.List;

import io.reactivex.Single;
import retrofit2.Converter;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public enum ApiMovieRepository implements MovieRepository {
    INSTANCE;

    private static final String BASE_URL = "http://176.192.212.47:8886/api/";
    private final MovieService movieService;

    ApiMovieRepository() {

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(generateConverterFactory())
                .build();

        movieService = retrofit.create(MovieService.class);
    }


    private Converter.Factory generateConverterFactory() {
        Gson gson = new GsonBuilder()
                .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
                .create();
        return GsonConverterFactory.create(gson);
    }


    @Override
    public Single<List<Movie>> getMovies(@NonNull Filter filter) {
        return movieService.getMovies(filter.getHappiness(),
                filter.getBrightness(),
                filter.getBullets(),
                filter.getSexuality()
        );
    }
}
