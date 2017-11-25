package com.want.movie.model.data;

import com.want.movie.model.entities.Movie;

import java.util.List;

import io.reactivex.Single;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface MovieService {
    @GET("get")
    Single<List<Movie>> getMovies(
            @Query("happiness") int happiness,
            @Query("bullets") int bullets,
            @Query("brightness") int brightness,
            @Query("sexuality") int sexuality
    );
}
