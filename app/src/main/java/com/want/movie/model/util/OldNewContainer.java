package com.want.movie.model.util;

import java.util.List;

public class OldNewContainer<T> {
    private final List<T> oldList;
    private final List<T> newList;

    public OldNewContainer(List<T> oldList, List<T> newList) {
        this.oldList = oldList;
        this.newList = newList;
    }

    public List<T> getOldList() {
        return oldList;
    }

    public List<T> getNewList() {
        return newList;
    }
}
