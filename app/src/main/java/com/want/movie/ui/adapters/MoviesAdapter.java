package com.want.movie.ui.adapters;

import android.content.Intent;
import android.net.Uri;
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
import com.want.movie.ui.App;
import com.want.movie.ui.listeners.OnItemClickedListener;

import java.util.List;

public class MoviesAdapter extends RecyclerView.Adapter<MoviesAdapter.MovieViewHolder> implements OnItemClickedListener {


    private final List<Movie> movieList;

    public MoviesAdapter(@NonNull List<Movie> movieList) {
        this.movieList = movieList;
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
        openMovie(movie);
    }

    private void openMovie(Movie movie) {
        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(movie.getUrl()));
        browserIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        App.getContext().startActivity(browserIntent);
    }

    static class MovieViewHolder extends RecyclerView.ViewHolder {

        TextView titleView = itemView.findViewById(R.id.movieTitle);
        ImageView imageView = itemView.findViewById(R.id.movieCover);

        MovieViewHolder(View itemView, final OnItemClickedListener onItemClickedListener) {
            super(itemView);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onItemClickedListener.onItemClicked(getAdapterPosition());
                }
            });
        }

        void bindMovie(Movie movie) {
            titleView.setText(movie.getTitle());
            Glide
                    .with(imageView.getContext())
                    .load(movie.getCover())
                    .apply(RequestOptions.fitCenterTransform())
                    .into(imageView);

        }
    }
}
