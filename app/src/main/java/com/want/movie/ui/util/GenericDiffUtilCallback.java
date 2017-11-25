package com.want.movie.ui.util;

import android.support.annotation.NonNull;
import android.support.v7.util.DiffUtil;

import java.util.List;

public class GenericDiffUtilCallback<T> extends DiffUtil.Callback {

    @NonNull
    private final List<T> oldList;
    @NonNull
    private final List<T> newList;

    public GenericDiffUtilCallback(@NonNull List<T> oldList, @NonNull List<T> newList) {
        this.oldList = oldList;
        this.newList = newList;
    }

    @Override
    public int getOldListSize() {
        return oldList.size();
    }

    @Override
    public int getNewListSize() {
        return newList.size();
    }

    @Override
    public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {
        return checkEquals(oldItemPosition, newItemPosition);
    }

    @Override
    public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
        return checkEquals(oldItemPosition, newItemPosition);
    }

    private boolean checkEquals(int oldItemPosition, int newItemPosition) {
        T currentItem = oldList.get(oldItemPosition);
        T nextItem = newList.get(newItemPosition);
        return currentItem.equals(nextItem);
    }
}
