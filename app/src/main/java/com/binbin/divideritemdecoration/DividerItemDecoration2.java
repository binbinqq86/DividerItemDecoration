package com.binbin.divideritemdecoration;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.support.annotation.ColorInt;
import android.support.annotation.ColorRes;
import android.support.annotation.DrawableRes;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.View;

/**
 * RecyclerView分割线
 * 暂时只支持LinearLayoutManager,GridLayoutManager...
 * 上下左右边界分别设置
 */

public class DividerItemDecoration2 extends RecyclerView.ItemDecoration {
    private static final String TAG = "tianbin";
    private Drawable mDivider;
    private int dividerHeight;
    private int dividerWidth;
    private int dividerColor;
    private static final int[] ATTRS = new int[]{android.R.attr.listDivider};
    private static final int HORIZONTAL_LIST = LinearLayoutManager.HORIZONTAL;
    private static final int VERTICAL_LIST = LinearLayoutManager.VERTICAL;
    /**
     * 设置是否显示左右边界线
     */
    private boolean drawBorderLeftAndRight = false;
    /**
     * 设置是否显示上下边界线
     */
    private boolean drawBorderTopAndBottom = false;

    /**
     * 是否只留空白，不画分割线
     */
    private boolean onlySetItemOffsetsButNoDraw=false;

    /**
     * 是否是线性布局
     */
    private boolean isLinearLayoutManager=true;
    /**
     * 布局方向
     */
    private int orientation=VERTICAL_LIST;

    public DividerItemDecoration2(Context context) {
        final TypedArray a = context.obtainStyledAttributes(ATTRS);
        mDivider = a.getDrawable(0);
        a.recycle();
    }

    /**
     * 自定义分割线
     *
     * @param context
     * @param drawableId           分割线图片
     */
    public DividerItemDecoration2(Context context, @DrawableRes int drawableId) {
        mDivider = ContextCompat.getDrawable(context, drawableId);
    }

//    public DividerItemDecoration2(Context context, @ColorInt int color,int dividerWidth,int dividerHeight) {
//        mDivider = new ColorDrawable(color);
//        mDivider.setBounds(0,0,dividerWidth,dividerHeight);
//        this.dividerHeight=dividerHeight;
//        this.dividerWidth=dividerWidth;
//    }
//    public DividerItemDecoration2(Context context, @ColorRes int color,int dividerWidth,int dividerHeight) {
//        mDivider = new ColorDrawable(color);
//        this.dividerHeight=dividerHeight;
//        this.dividerWidth=dividerWidth;
//    }


    /**
     * 垂直滚动，item宽度充满，高度自适应
     * 水平滚动,item高度充满，宽度自适应
     * 在itemView绘制完成之前调用，也就是说此方法draw出来的效果将会在itemView的下面
     * onDrawOver方法draw出来的效果将叠加在itemView的上面
     * @param c
     * @param parent
     * @param state
     */
    @Override
    public void onDraw(Canvas c, RecyclerView parent, RecyclerView.State state) {
        if(onlySetItemOffsetsButNoDraw){
            return;
        }
        if(isLinearLayoutManager){
            drawLinearItemDivider(c,parent);
        }else{
            drawHorizontalLine(c, parent);
            drawVerticalLine(c, parent);
        }

    }

    private void drawLinearItemDivider(Canvas c, RecyclerView parent){
        int spanCount = getSpanCount(parent);
        int allChildCount = parent.getAdapter().getItemCount();
        int top=0,bottom=0,left=0,right=0;
        for (int i = 0; i < parent.getChildCount(); i++) {
            final View child = parent.getChildAt(i);
            final RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) child.getLayoutParams();
            if(orientation==VERTICAL_LIST){//画横线
                left = child.getLeft() - params.leftMargin;
                right = child.getRight() + params.rightMargin;
                if(drawBorderTopAndBottom){
                    //加上第一条
                    if(isFirstRaw(parent,params.getViewLayoutPosition(),spanCount)){
                        top=child.getTop()-params.topMargin-mDivider.getIntrinsicHeight();
                        bottom = top + mDivider.getIntrinsicHeight();
                        mDivider.setBounds(left, top, right, bottom);
                        mDivider.draw(c);
                    }
                }else{
                    if(isLastRaw(parent,params.getViewLayoutPosition(),spanCount,allChildCount)){
                        continue;
                    }
                }
                top = child.getBottom() + params.bottomMargin;
                bottom = top + mDivider.getIntrinsicHeight();
                mDivider.setBounds(left, top, right, bottom);
                mDivider.draw(c);
            }else{//画竖线
                top=child.getTop()-params.topMargin;
                bottom=child.getBottom()+params.bottomMargin;
                if(drawBorderLeftAndRight){
                    //加上第一条
                    if(isFirstColumn(parent,params.getViewLayoutPosition(),spanCount)){
                        left=child.getLeft()-params.leftMargin-mDivider.getIntrinsicWidth();
                        right = left + mDivider.getIntrinsicWidth();
                        mDivider.setBounds(left, top, right, bottom);
                        mDivider.draw(c);
                    }
                }else{
                    if(isLastColum(parent,params.getViewLayoutPosition(),spanCount,allChildCount)){
                        continue;
                    }
                }
                left = child.getRight() + params.rightMargin;
                right = left + mDivider.getIntrinsicHeight();
                mDivider.setBounds(left, top, right, bottom);
                mDivider.draw(c);
            }
        }
        if(orientation==VERTICAL_LIST){
            if(drawBorderLeftAndRight){
                top = parent.getPaddingTop();
                bottom = parent.getHeight() - parent.getPaddingBottom();
                left=parent.getPaddingLeft();
                right=left+mDivider.getIntrinsicWidth();
                //画左边界
                mDivider.setBounds(left,top,right,bottom);
                mDivider.draw(c);
                left=parent.getWidth()-parent.getPaddingRight()-mDivider.getIntrinsicWidth();
                right=left+mDivider.getIntrinsicWidth();
//                Log.e(TAG, "drawLinearItemDivider: "+parent.getWidth()+"#"+parent.getPaddingLeft()+"#"+mDivider.getIntrinsicWidth() );
                //画右边界
                mDivider.setBounds(left,top,right,bottom);
                mDivider.draw(c);
            }
        }else if(orientation==HORIZONTAL_LIST){
            if(drawBorderTopAndBottom){
                left=parent.getPaddingLeft();
                right=parent.getWidth()-parent.getPaddingRight();
                top=parent.getPaddingTop();
                bottom=top+mDivider.getIntrinsicHeight();
                //画上边界
                mDivider.setBounds(left,top,right,bottom);
                mDivider.draw(c);
                top=parent.getHeight()-parent.getPaddingBottom()-mDivider.getIntrinsicHeight();
                bottom=top+mDivider.getIntrinsicHeight();
                //画下边界
                mDivider.setBounds(left,top,right,bottom);
                mDivider.draw(c);
            }
        }
    }

    /**
     * 画水平分割线
     * @param c
     * @param parent
     */
    private void drawHorizontalLine(Canvas c, RecyclerView parent) {
        int spanCount = getSpanCount(parent);
        for (int i = 0; i < parent.getChildCount(); i++) {
            final View child = parent.getChildAt(i);
            final RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) child.getLayoutParams();
            int top=0,bottom=0,left=0,right=0;
            left = child.getLeft() - params.leftMargin;
            //因为getItemOffsets中为竖线留了空隙，所以要加上分割线的宽度（在此处处理，下面不用处理）
            right = child.getRight() + params.rightMargin+ mDivider.getIntrinsicWidth();
            if(i==parent.getChildCount()-1 && !drawBorderLeftAndRight){
                right-=mDivider.getIntrinsicWidth();//防止最后一个越界
            }

            if(isFirstColumn(parent,params.getViewLayoutPosition(),spanCount) && drawBorderLeftAndRight){
                left-=mDivider.getIntrinsicWidth();//下面不用处理
            }
            if(drawBorderTopAndBottom){
                //加上第一条
                if(isFirstRaw(parent,params.getViewLayoutPosition(),spanCount)){
                    top=child.getTop()-params.topMargin-mDivider.getIntrinsicHeight();
                    bottom = top + mDivider.getIntrinsicHeight();
                    mDivider.setBounds(left, top, right, bottom);
                    mDivider.draw(c);
                }
            }else{
                int allChildCount = parent.getAdapter().getItemCount();
                if(isLastRaw(parent,params.getViewLayoutPosition(),spanCount,allChildCount)){
                    continue;
                }
            }
//            Log.e(TAG, "drawVertical: "+params.getViewLayoutPosition()+"@@"+mDivider.getIntrinsicWidth()+"#"+parent.getLayoutManager().getLeftDecorationWidth(child) );
            top = child.getBottom() + params.bottomMargin;
            bottom = top + mDivider.getIntrinsicHeight();
            mDivider.setBounds(left, top, right, bottom);
            mDivider.draw(c);
        }
    }

    /**
     * 画垂直分割线
     * @param c
     * @param parent
     */
    private void drawVerticalLine(Canvas c, RecyclerView parent) {
        int spanCount = getSpanCount(parent);
        for (int i = 0; i < parent.getChildCount(); i++) {
            final View child = parent.getChildAt(i);
            final RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) child .getLayoutParams();
            int left=0,right=0,top=0,bottom=0;
            top=child.getTop()-params.topMargin;
            //因为getItemOffsets中为横线留了空隙，所以要加上分割线的高度(上下加一处即可)
            bottom=child.getBottom()+params.bottomMargin/**+mDivider.getIntrinsicHeight()*/;
//            if(isFirstRaw(parent,params.getViewLayoutPosition(),spanCount) && drawBorderTopAndBottom){
//                        top-=mDivider.getIntrinsicHeight();
//            }
            if(drawBorderLeftAndRight){
                //加上第一条
                if(isFirstColumn(parent,params.getViewLayoutPosition(),spanCount)){
                    left=child.getLeft()-params.leftMargin-mDivider.getIntrinsicWidth();
                    right = left + mDivider.getIntrinsicWidth();
                    mDivider.setBounds(left, top, right, bottom);
                    mDivider.draw(c);
                }
            }else{
                int allChildCount = parent.getAdapter().getItemCount();
                if(isLastColum(parent,params.getViewLayoutPosition(),spanCount,allChildCount)){
                    continue;
                }
            }

            left = child.getRight() + params.rightMargin;
            right = left + mDivider.getIntrinsicHeight();
            mDivider.setBounds(left, top, right, bottom);
            mDivider.draw(c);
        }
    }

    /**
     * item的偏移，如果不设置，也能画线，但是画在了item后面(被item挡住了，可以通过设置item背景透明度证实)
     * @param outRect
     * @param view
     * @param parent
     * @param state
     */
    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        RecyclerView.LayoutManager layoutManager = parent.getLayoutManager();
        if(mDivider==null){
            return;
        }
        if(!(layoutManager instanceof LinearLayoutManager)){
            throw new IllegalStateException("The LayoutManager must be LinearLayoutManager or it's subclass!!!");
        }
        isLinearLayoutManager=(layoutManager instanceof LinearLayoutManager) && !(layoutManager instanceof GridLayoutManager);
        if(isLinearLayoutManager){
            orientation=((LinearLayoutManager) layoutManager).getOrientation();
        }else{
            orientation=(layoutManager instanceof GridLayoutManager)?((GridLayoutManager) layoutManager).getOrientation():((StaggeredGridLayoutManager)layoutManager).getOrientation();
        }
        int spanCount = getSpanCount(parent);
        int childCount = parent.getAdapter().getItemCount();
        int itemPosition=((RecyclerView.LayoutParams) view.getLayoutParams()).getViewLayoutPosition();
//        Log.e(TAG, "getItemOffsets: "+childCount+"$"+spanCount+"$"+itemPosition );
        boolean isLastRaw=isLastRaw(parent, itemPosition, spanCount, childCount);
        boolean isLastColum=isLastColum(parent, itemPosition, spanCount, childCount);
        boolean isFirstRaw=isFirstRaw(parent,itemPosition,spanCount);
        boolean isFirstColumn=isFirstColumn(parent,itemPosition,spanCount);
        int left=0,top=0,right=0,bottom=0;
        if(isLinearLayoutManager){
            if(orientation==VERTICAL_LIST){
                //垂直滚动线性布局
                bottom=mDivider.getIntrinsicHeight();
                if(isLastRaw && !drawBorderTopAndBottom){
                    bottom=0;
                }
                if(isFirstRaw && drawBorderTopAndBottom){
                    top=mDivider.getIntrinsicHeight();
                }
                if(drawBorderLeftAndRight){
                    left=mDivider.getIntrinsicWidth();
                    right=mDivider.getIntrinsicWidth();
                }
            }else if(orientation==HORIZONTAL_LIST){
                right=mDivider.getIntrinsicWidth();
                if(isLastColum && !drawBorderLeftAndRight){
                    right=0;
                }
                if(isFirstColumn && drawBorderLeftAndRight){
                    left=mDivider.getIntrinsicWidth();
                }
                if(drawBorderTopAndBottom){
                    top=mDivider.getIntrinsicWidth();
                    bottom=mDivider.getIntrinsicWidth();
                }
            }
        }else{
            GridLayoutManager.SpanSizeLookup spanSizeLookup = ((GridLayoutManager)layoutManager).getSpanSizeLookup();
            int spanIndexLeft = spanSizeLookup.getSpanIndex(itemPosition, spanCount);//左边的跨度索引值[0,spanCount)之间
            int spanIndexRight = spanIndexLeft - 1 + spanSizeLookup.getSpanSize(itemPosition);//右边的跨度索引值[0,spanCount)之间
//            Log.e(TAG, "getItemOffsets: "+spanIndexLeft +"#"+spanIndexRight+"#"+itemPosition+"$$$"+spanSizeLookup.getSpanGroupIndex(itemPosition, spanCount));
            if(orientation==VERTICAL_LIST){
                if(drawBorderLeftAndRight){
                    left=mDivider.getIntrinsicWidth() * (spanCount - spanIndexLeft) / spanCount;
                    right=mDivider.getIntrinsicWidth() * (spanIndexRight + 1) / spanCount;
                }else{
                    left = mDivider.getIntrinsicWidth() * spanIndexLeft / spanCount;
                    right = mDivider.getIntrinsicWidth() * (spanCount - spanIndexRight - 1) / spanCount;
                }
                if(drawBorderTopAndBottom){
                    if (spanSizeLookup.getSpanGroupIndex(itemPosition, spanCount) == 0) {
                        top = mDivider.getIntrinsicHeight();
                    } else {
                        top = 0;
                    }
                    bottom = mDivider.getIntrinsicHeight();
                }else{
                    if (isLastRaw) {
                        bottom=0;
                    } else {
                        bottom = mDivider.getIntrinsicHeight();
                    }
                    top=0;
                }
            }else if(orientation==HORIZONTAL_LIST){
                if(drawBorderTopAndBottom){
                    top=mDivider.getIntrinsicWidth() * (spanCount - spanIndexLeft) / spanCount;
                    bottom=mDivider.getIntrinsicWidth() * (spanIndexRight + 1) / spanCount;
                }else{
                    top = mDivider.getIntrinsicWidth() * spanIndexLeft / spanCount;
                    bottom = mDivider.getIntrinsicWidth() * (spanCount - spanIndexRight - 1) / spanCount;
                }
                if(drawBorderLeftAndRight){
                    if(isFirstColumn){
                        left=mDivider.getIntrinsicWidth();
                    }else{
                        left=0;
                    }
                    right=mDivider.getIntrinsicWidth();
                }else{
                    if(isLastColum){
                        right=0;
                    }else{
                        right=mDivider.getIntrinsicWidth();
                    }
                    left=0;
                }
            }
        }
//        Log.e(TAG, "getItemOffsets: "+left+"@"+top+"#"+right+"$"+bottom+"%"+itemPosition );
        outRect.set(left,top,right,bottom);
    }

    private boolean isFirstRaw(RecyclerView parent, int pos, int spanCount){
        if (!isLinearLayoutManager) {
            GridLayoutManager.SpanSizeLookup spanSizeLookup = ((GridLayoutManager)parent.getLayoutManager()).getSpanSizeLookup();
            int spanIndexLeft = spanSizeLookup.getSpanIndex(pos, spanCount);
            if (orientation == GridLayoutManager.VERTICAL) {
//                if(pos<spanCount){
//                    return true;
//                }
                //=========================另一种方案，兼容每个item的spanSize不同的情况==============================
                if(spanSizeLookup.getSpanGroupIndex(pos,spanCount)==0){
                    return true;
                }
            }else{
//                if(pos%spanCount==0){
//                    return true;
//                }
                //=========================另一种方案，兼容每个item的spanSize不同的情况==============================
                if(spanIndexLeft==0){
                    return true;
                }
            }
        }else{
            if (orientation == LinearLayoutManager.VERTICAL) {
                if(pos==0){
                    return true;
                }
            }else{
                //每一个都是第一行，也是最后一行
                return true;
            }
        }
        return false;
    }

    private boolean isFirstColumn(RecyclerView parent, int pos, int spanCount){
        if (!isLinearLayoutManager) {
            GridLayoutManager.SpanSizeLookup spanSizeLookup = ((GridLayoutManager)parent.getLayoutManager()).getSpanSizeLookup();
            int spanIndexLeft = spanSizeLookup.getSpanIndex(pos, spanCount);
            if (orientation == GridLayoutManager.VERTICAL) {
//                if(pos%spanCount==0){
//                    return true;
//                }
                //=========================另一种方案，兼容每个item的spanSize不同的情况==============================
                if(spanIndexLeft==0){
                    return true;
                }
            }else{
//                if(pos<spanCount){
//                    return true;
//                }
                //=========================另一种方案，兼容每个item的spanSize不同的情况==============================
                if(spanSizeLookup.getSpanGroupIndex(pos,spanCount)==0){
                    return true;
                }
            }
        }else{
            if (orientation == LinearLayoutManager.VERTICAL) {
                //每一个都是第一列，也是最后一列
                return true;
            }else{
                if(pos==0){
                    return true;
                }
            }
        }
        return false;
    }

    private boolean isLastColum(RecyclerView parent, int pos, int spanCount, int childCount) {
        if (!isLinearLayoutManager) {
            GridLayoutManager.SpanSizeLookup spanSizeLookup = ((GridLayoutManager)parent.getLayoutManager()).getSpanSizeLookup();
            int spanIndexLeft = spanSizeLookup.getSpanIndex(pos, spanCount);
            if (orientation == GridLayoutManager.VERTICAL) {
                //最后一列或者不能整除的情况下最后一个
//                if ((pos + 1) % spanCount == 0 /**|| pos==childCount-1*/){// 如果是最后一列
//                    return true;
//                }
                //=========================另一种方案，兼容每个item的spanSize不同的情况==============================
                if(spanIndexLeft==spanCount-1 || spanCount==spanSizeLookup.getSpanSize(pos) || pos==childCount-1){
                    return true;
                }
            }else{
//                if(pos>=childCount-spanCount && childCount%spanCount==0){
//                    //整除的情况判断最后一整列
//                    return true;
//                }else if(childCount%spanCount!=0 && pos>=spanCount*(childCount/spanCount)){
//                    //不能整除的情况只判断最后几个
//                    return true;
//                }
                //=========================另一种方案==============================
//                if(pos>=childCount-spanCount){
//                    return true;
//                }
                //=========================另一种方案，兼容每个item的spanSize不同的情况==============================
                int lastItemSpanGroupIndex=spanSizeLookup.getSpanGroupIndex(childCount-1,spanCount);
                if(spanSizeLookup.getSpanGroupIndex(pos,spanCount)==lastItemSpanGroupIndex){
                    return true;//如果与最后一个元素同组则判定为最后一行
                }
            }
        }else{
            if (orientation == LinearLayoutManager.VERTICAL) {
                //每一个都是第一列，也是最后一列
                return true;
            }else{
                if(pos==childCount-1){
                    return true;
                }
            }
        }
        return false;
    }

    private boolean isLastRaw(RecyclerView parent, int pos, int spanCount, int childCount) {
        if (!isLinearLayoutManager) {
            GridLayoutManager.SpanSizeLookup spanSizeLookup = ((GridLayoutManager)parent.getLayoutManager()).getSpanSizeLookup();
            int spanIndexLeft = spanSizeLookup.getSpanIndex(pos, spanCount);
            if (orientation == GridLayoutManager.VERTICAL) {
//                if(pos>=childCount-spanCount && childCount%spanCount==0){
//                    //整除的情况判断最后一整行
//                    return true;
//                }else if(childCount%spanCount!=0 && pos>=spanCount*(childCount/spanCount)){
//                    //不能整除的情况只判断最后几个
//                    return true;
//                }
                //=======================另一种方案===============================
//                if(pos>=childCount-spanCount){
//                    return true;
//                }
                //=========================另一种方案，兼容每个item的spanSize不同的情况==============================
                //判断最后一行是否充满整行
                boolean flag=spanSizeLookup.getSpanIndex(childCount-1, spanCount)==spanCount-1 || spanCount==spanSizeLookup.getSpanSize(childCount-1);
                int lastItemSpanGroupIndex=spanSizeLookup.getSpanGroupIndex(childCount-1,spanCount);
                if(spanSizeLookup.getSpanGroupIndex(pos,spanCount)==lastItemSpanGroupIndex){
                    return true;//如果与最后一个元素同组则判定为最后一行
                }
//                if(flag){
//                }else{
//                    //没有充满则前一行跟最后一行都判定为最后一行
//                    if(spanSizeLookup.getSpanGroupIndex(pos,spanCount)==lastItemSpanGroupIndex || spanSizeLookup.getSpanGroupIndex(pos,spanCount)==lastItemSpanGroupIndex-1){
//                        return true;
//                    }
//                }
            }else{
                //最后一行或者不能整除的情况下最后一个
//                if ((pos + 1) % spanCount == 0 /**|| pos==childCount-1*/){// 如果是最后一行
//                    return true;
//                }
                //=========================另一种方案，兼容每个item的spanSize不同的情况==============================
                if(spanIndexLeft==spanCount-1 || spanCount==spanSizeLookup.getSpanSize(pos) || pos==childCount-1){
                    return true;
                }
            }
        }else{
            if (orientation == LinearLayoutManager.VERTICAL) {
                if(pos==childCount-1){
                    return true;
                }
            }else{
                //每一个都是第一行，也是最后一行
                return true;
            }
        }
        return false;
    }


    private int getSpanCount(RecyclerView parent) {
        // 列数
        int spanCount = -1;
        RecyclerView.LayoutManager layoutManager = parent.getLayoutManager();
        if (layoutManager instanceof GridLayoutManager) {
            spanCount = ((GridLayoutManager) layoutManager).getSpanCount();
        } else if (layoutManager instanceof StaggeredGridLayoutManager) {
            spanCount = ((StaggeredGridLayoutManager) layoutManager).getSpanCount();
        }
        return spanCount;
    }

    public boolean isDrawBorderTopAndBottom() {
        return drawBorderTopAndBottom;
    }

    public void setDrawBorderTopAndBottom(boolean drawBorderTopAndBottom) {
        this.drawBorderTopAndBottom = drawBorderTopAndBottom;
    }

    public boolean isDrawBorderLeftAndRight() {
        return drawBorderLeftAndRight;
    }

    public void setDrawBorderLeftAndRight(boolean drawBorderLeftAndRight) {
        this.drawBorderLeftAndRight = drawBorderLeftAndRight;
    }

    public boolean isOnlySetItemOffsetsButNoDraw() {
        return onlySetItemOffsetsButNoDraw;
    }

    public void setOnlySetItemOffsetsButNoDraw(boolean onlySetItemOffsetsButNoDraw) {
        this.onlySetItemOffsetsButNoDraw = onlySetItemOffsetsButNoDraw;
    }
}
