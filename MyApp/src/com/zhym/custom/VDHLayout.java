package com.zhym.custom;

import android.content.Context;
import android.graphics.Point;
import android.support.v4.widget.ViewDragHelper;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;

/**
 * Created by lenovo on 2015/10/29.
 */
public class VDHLayout extends LinearLayout{

    private ViewDragHelper mDrager;
    private View mDragView;
    private View mAutoBackView;
    private View mEdgeTrackerView;

    private Point mAutoBackOriginPos = new Point();

    public VDHLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        mDrager = ViewDragHelper.create(this, 1.0f, new ViewDragHelper.Callback() {
            @Override
            public boolean tryCaptureView(View child, int pointerId) {
                //mEdgeTrackerView禁止直接移动
                Log.e("VDHLayout:mDrager:tryCaptureView","pointerId="+pointerId);
                return child == mDragView || child == mAutoBackView;
            }

            /**
             * 公共方法：
             * clamp:夹  垂直方向固定，只允许水平方向移动
             * */
            @Override
            public int clampViewPositionHorizontal(View child, int left, int dx) {
                Log.e("VDHLayout:mDrager:clampViewPositionHorizontal","left="+left+", dx="+dx);
                return left;
            }

            @Override
            public int clampViewPositionVertical(View child, int top, int dy) {
                Log.e("VDHLayout:mDrager:clampViewPositionVertical","top="+top+", dy="+dy);
                return top;
            }

            //手指释放的时候回调
            @Override
            public void onViewReleased(View releasedChild, float xvel, float yvel) {
                //mAutoBackView手指释放时可以自动回去
                if(releasedChild == mAutoBackView) {
                    Log.e("VDHLayout:mDrager:onViewReleased","xvel="+xvel+", yvel="+yvel);
                    mDrager.settleCapturedViewAt(mAutoBackOriginPos.x, mAutoBackOriginPos.y);
                    invalidate();
                }
            }

            /**
             * 专用方法：只有沿边拖动时，第三个按钮才会动
             * 在边界拖动时回调
             * */
            @Override
            public void onEdgeDragStarted(int edgeFlags, int pointerId) {
                mDrager.captureChildView(mEdgeTrackerView, pointerId);
                Log.e("VDHLayout:mDrager:onEdgeDragStarted","edgeFlags="+edgeFlags+", pointerId="+pointerId);
            }

            @Override
            public int getViewHorizontalDragRange(View child) {
                Log.e("VDHLayout:mDrager:getViewHorizontalDragRange","getMeasutedWidth="+getMeasuredWidth()+", child.getMeasuredHeight="+child.getMeasuredHeight());
                return getMeasuredWidth() - child.getMeasuredHeight();
            }

            @Override
            public int getViewVerticalDragRange(View child) {
                Log.e("VDHLayout:mDrager:getViewVerticalDragRange","getMeasuredHeight="+getMeasuredHeight()+", child.getMeasuredWidth="+child.getMeasuredWidth());
                return getMeasuredHeight() - child.getMeasuredWidth();
            }

        });
        mDrager.setEdgeTrackingEnabled(ViewDragHelper.EDGE_LEFT);

    }

    /**
     * 公共方法：
     * 拦截控件触摸事件：只有一触摸屏幕中自定义的控件（如：TextView）就会执行此方法。按下鼠标执行一次，松开鼠标执行一次
     * */
    @Override
    public boolean onInterceptTouchEvent(MotionEvent event) {
        Log.e("VDHLayout:onInterceptTouchEvent","event.toString():"+event.toString());
        return mDrager.shouldInterceptTouchEvent(event);
    }

    /**
     * 公共方法
     * 监听触摸屏幕上的所有事件：包括触摸、拖动、松手等
     * （只是触摸但是不拖动，则只在除控件以外的部分调用一次，因为触摸控件但不动只会调用onInterceptTouchEvent方法）
     * */
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        mDrager.processTouchEvent(event);
        Log.e("VDHLayout:onTouchEvent","processTouchEvent:"+event.toString());
        return true;
    }

    @Override
    public void computeScroll() {
        if(mDrager.continueSettling(true)) {
            Log.e("VDHLayout:computeScroll","continueSettling");
            invalidate();
        }
    }

    /**
     * 公共方法：
     *
     * */
    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
        mAutoBackOriginPos.x = mAutoBackView.getLeft();
        mAutoBackOriginPos.y = mAutoBackView.getTop();
        Log.e("VDHLayout:onLayout","mAutoBackOriginPos.x="+mAutoBackOriginPos.x+", mAutoBackOriginPos.y="+mAutoBackOriginPos.y+", l="+l+", t="+t+", r="+r+", b="+b);
    }

    /**
     * 公共方法：
     * 将布局中xml的控件和自定义线性布局控件java类中的视图view对应起来
     * */
    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        mDragView = getChildAt(0);
        mAutoBackView = getChildAt(1);
        mEdgeTrackerView = getChildAt(2);
        Log.e("VDHLayout:onFinishInflate","");
    }

    /**
     * 初始化刚进入页面：onFinishInflate、onLayout
     * 都执行：onInterceptTouchEvent、onTouchEvent、getViewHorizontalDragRange、getViewVerticalDragRange、clampViewPositionHorizontal、clampViewPositionVertical
     *
     * mAutoBackView执行的方法：tryCaptureView、onViewReleased、computeScroll、onLayout
     * mEdgeTrackerView执行的方法:onEdgeDragStarted
     * */
}
