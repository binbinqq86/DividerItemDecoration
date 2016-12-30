package com.binbin.divideritemdecoration;

import android.graphics.Canvas;
import android.support.v7.widget.GridLayoutManager;

import android.graphics.Rect;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.GridLayoutManager.SpanSizeLookup;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;

/**
 * <p>ItemDecoration which works well with different span size in a single RecyclerView. </p>
 * <p>
 * Spacings will only be add between rows(horizontally) and columns(vertically).
 * If you need spacing above 1st row / below last row / to left of first column / to right of last column,
 * please use padding of RecyclerView.
 * </p>
 */
public class D2 extends RecyclerView.ItemDecoration {
    private static final String TAG = "D2";
    int mHorizontalSpacing;
    int mVerticalSpacing;

    private boolean drawBorderLine=false;

    public D2(int spacing) {
        this(spacing, spacing);
    }

    public D2(int horizontalSpacing, int verticalSpacing) {
        mHorizontalSpacing = horizontalSpacing;
        mVerticalSpacing = verticalSpacing;
    }

    @Override
    public void onDrawOver(Canvas c, RecyclerView parent, RecyclerView.State state) {
        super.onDrawOver(c, parent, state);
        for (int i = 0; i < parent.getChildCount(); i++) {
            View child=parent.getChildAt(i);
            Log.e(TAG, "onDrawOver: "+child.getMeasuredWidth()+"#"+parent.getLayoutManager().getLeftDecorationWidth(child)+"$"+parent.getLayoutManager().getRightDecorationWidth(child) );
//            Log.e(TAG, "onDrawOver: "+child.getLeft()+"#"+child.getTop()+"#"+child.getRight()+"#"+child.getBottom());
            RecyclerView.LayoutParams lp= (RecyclerView.LayoutParams) child.getLayoutParams();
//            final GridLayoutManager.LayoutParams lp = (GridLayoutManager.LayoutParams) child.getLayoutParams();
//            final Rect decorInsets = lp.mDecorInsets;
//            Log.e(TAG, "onDrawOver: "+child.getPaddingLeft()+"#"+child.getPaddingTop()+"#"+child.getPaddingRight()+"#"+child.getPaddingBottom());
//            Log.e(TAG, "onDrawOver: "+lp.leftMargin+"#"+lp.topMargin+"#"+lp.rightMargin+"#"+lp.bottomMargin);
        }
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent,
                               RecyclerView.State state) {
        super.getItemOffsets(outRect, view, parent, state);
        if (!(parent.getLayoutManager() instanceof GridLayoutManager)) {
            throw new IllegalStateException(
                    "GridSpacingDecoration must be used with GridLayoutManager");
        }

        GridLayoutManager gridLayoutManager = ((GridLayoutManager) parent.getLayoutManager());
        int spanCount = gridLayoutManager.getSpanCount();

        // invalid span count
        if (spanCount < 1) return;

        int childCount = parent.getAdapter().getItemCount();
        int itemPos = parent.getChildAdapterPosition(view);
        GridLayoutManager.SpanSizeLookup spanSizeLookup = gridLayoutManager.getSpanSizeLookup();

        // add top spacing for items except for first row
//        Log.e(TAG, "getItemOffsets: "+spanSizeLookup.getSpanGroupIndex(itemPos, spanCount) );

        // to which span the left border of this item belongs
        int spanIndexLeft = spanSizeLookup.getSpanIndex(itemPos, spanCount);
//        Log.e(TAG, "getItemOffsets: "+spanIndexLeft +"#"+itemPos);
        // to which span the right border of this item belongs
        int spanIndexRight = spanIndexLeft - 1 + spanSizeLookup.getSpanSize(itemPos);
        if(drawBorderLine){
            if (spanSizeLookup.getSpanGroupIndex(itemPos, spanCount) == 0) {
                outRect.top = mVerticalSpacing;
            } else {
                outRect.top = 0;
            }
            outRect.bottom = mVerticalSpacing;
            outRect.left=mHorizontalSpacing/spanCount*(spanCount-spanIndexLeft);
            outRect.right=mHorizontalSpacing/spanCount*(spanIndexRight+1);
        }else {
            if (spanSizeLookup.getSpanGroupIndex(itemPos, spanCount) == 0) {
                outRect.top = 0;
            } else {
                outRect.top = mVerticalSpacing;
            }
            outRect.bottom=0;
            outRect.left = mHorizontalSpacing * spanIndexLeft / spanCount;
            outRect.right = mHorizontalSpacing * (spanCount - spanIndexRight - 1) / spanCount;
            outRect.bottom=0;
        }

//        Log.e(TAG, "getItemOffsets: "+outRect.left+"#"+outRect.top+"#"+outRect.right+"#"+outRect.bottom );
    }

    public boolean isDrawBorderLine() {
        return drawBorderLine;
    }

    public void setDrawBorderLine(boolean drawBorderLine) {
        this.drawBorderLine = drawBorderLine;
    }
}
