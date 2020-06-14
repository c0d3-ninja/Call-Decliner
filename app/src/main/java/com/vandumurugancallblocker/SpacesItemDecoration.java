package com.vandumurugancallblocker;

import android.graphics.Rect;
import android.view.View;

import androidx.recyclerview.widget.RecyclerView;

public class SpacesItemDecoration extends RecyclerView.ItemDecoration {
    private int space;
    private int totalItemsInaRow;
    public SpacesItemDecoration(int space, int totalItemsInaRow) {
        this.space = space;
        this.totalItemsInaRow=totalItemsInaRow;
    }

    @Override
    public void getItemOffsets(Rect outRect, View view,
                               RecyclerView parent, RecyclerView.State state) {
        int viewPosition = parent.getChildLayoutPosition(view);

        if(totalItemsInaRow==1){
            outRect.left = space/2;
            outRect.right = space/2;
        }else{
            if(viewPosition%2==0){
                outRect.left = space;
                outRect.right = space/2;
            }else if(viewPosition%2==1){
                outRect.left = space/2;
                outRect.right = space;
            }else{
                outRect.left = space/2;
                outRect.right = space/2;
            }
        }
        outRect.bottom = space;
        // Add top margin only for the first item to avoid double space between items
        if (viewPosition<totalItemsInaRow) {
            outRect.top = space;
        } else {
            outRect.top = 0;
        }
    }
}