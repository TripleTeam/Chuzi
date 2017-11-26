package com.want.movie.ui.adapters;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.want.movie.R;
import com.want.movie.model.entities.Movie;
import com.want.movie.model.navigators.MovieNavigator;
import com.want.movie.ui.listeners.OnItemClickedListener;

import java.util.List;

public class MoviesAdapter extends RecyclerView.Adapter<MoviesAdapter.MovieViewHolder> implements OnItemClickedListener {


    private final List<Movie> movieList;
    private final MovieNavigator movieNavigator;

    public MoviesAdapter(@NonNull List<Movie> movieList, MovieNavigator movieNavigator) {
        this.movieList = movieList;
        this.movieNavigator = movieNavigator;
    }

    @Override
    public MovieViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.view_movie_item, parent, false);
        return new MovieViewHolder(view, this);
    }

    @Override
    public void onBindViewHolder(MovieViewHolder holder, int position) {
        holder.bindMovie(movieList.get(position));
    }

    @Override
    public int getItemCount() {
        return movieList.size();
    }

    @Override
    public void onItemClicked(int position) {
        Movie movie = movieList.get(position);
        movieNavigator.navigateToMovie(movie);
    }

    static class MovieViewHolder extends RecyclerView.ViewHolder {

        TextView titleView = itemView.findViewById(R.id.movieTitle);
        ImageView imageView = itemView.findViewById(R.id.movieCover);
        RequestOptions options;

        MovieViewHolder(View itemView, final OnItemClickedListener onItemClickedListener) {
            super(itemView);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onItemClickedListener.onItemClicked(getAdapterPosition());
                }
            });
            options = new RequestOptions()
                    .centerCrop()
                    .placeholder(R.drawable.placeholder_background)
                    .error(R.drawable.placeholder_background);
        }

        void bindMovie(Movie movie) {
            titleView.setText(movie.getTitle());
            Glide
                    .with(imageView.getContext())
                    .setDefaultRequestOptions(options)
                    .load(movie.getCover())
                    .apply(RequestOptions.fitCenterTransform())
                    .into(imageView);

        }
    }
}
