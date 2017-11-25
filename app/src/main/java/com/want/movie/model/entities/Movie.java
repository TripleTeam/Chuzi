package com.want.movie.model.entities;

import android.os.Parcel;
import android.os.Parcelable;

public class Movie implements Parcelable {
    private String title;
    private String cover;
    private String url;

    public Movie(String title, String cover, String url) {
        this.title = title;
        this.cover = cover;
        this.url = url;
    }

    public String getTitle() {
        return title;
    }

    public String getCover() {
        return cover;
    }

    public String getUrl() {
        return url;
    }

    private Movie(Parcel in) {
        title = in.readString();
        cover = in.readString();
        url = in.readString();
    }

    public static final Creator<Movie> CREATOR = new Creator<Movie>() {
        @Override
        public Movie createFromParcel(Parcel in) {
            return new Movie(in);
        }

        @Override
        public Movie[] newArray(int size) {
            return new Movie[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(title);
        dest.writeString(cover);
        dest.writeString(url);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Movie movie = (Movie) o;

        if (title != null ? !title.equals(movie.title) : movie.title != null) return false;
        if (cover != null ? !cover.equals(movie.cover) : movie.cover != null) return false;
        return url != null ? url.equals(movie.url) : movie.url == null;
    }

    @Override
    public int hashCode() {
        int result = title != null ? title.hashCode() : 0;
        result = 31 * result + (cover != null ? cover.hashCode() : 0);
        result = 31 * result + (url != null ? url.hashCode() : 0);
        return result;
    }
}
