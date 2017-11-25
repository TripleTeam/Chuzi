package com.want.movie.ui.decorations;

import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.View;

public class HorizontalSpaceDecoration extends RecyclerView.ItemDecoration {

    private final int spacePx;

    public HorizontalSpaceDecoration(int spacePx) {
        this.spacePx = spacePx;
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        super.getItemOffsets(outRect, view, parent, state);
        if (parent.getChildAdapterPosition(view) != 0) {
            outRect.left = spacePx;
        }
    }
}
