package com.raul.basic.android.ui.basic.pullrefresh;

import android.content.Context;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.Interpolator;
import android.widget.LinearLayout;

import com.raul.basic.android.R;
import com.raul.basic.android.ui.basic.pullrefresh.internal.ListViewListener.OnRefreshListener;
import com.raul.basic.android.ui.basic.pullrefresh.internal.LoadingLayout;

/**
 * 下拉刷新的基础类
 * @author xiaomiaomiao
 *
 * @param <T>
 */
public abstract class PullToRefreshBase<T extends View> extends LinearLayout
{
    /**
     * 日志TAG
     */
    public static final String DEBUG_TAG = "PullToRefresh";
    
    /**
     * FRICTION
     */
    private static final float FRICTION = 2.0f;
    
    /**
     * PULL_TO_REFRESH
     */
    private static final int PULL_TO_REFRESH = 0x0;
    
    /**
     * RELEASE_TO_REFRESH
     */
    private static final int RELEASE_TO_REFRESH = 0x1;
    
    /**
     * REFRESHING
     */
    private static final int REFRESHING = 0x2;
    
    /**
     * STATE_SHOW_REFRESHING_VIEW
     */
    @SuppressWarnings("unused")
    private static final String STATE_SHOW_REFRESHING_VIEW = "ptr_show_refreshing_view";
    
    /**
     * TAG
     */
    @SuppressWarnings("unused")
    private static final String TAG = "PullToRefreshBase";
    
    /**
     * 滚动处理类
     */
    final class SmoothScrollRunnable implements Runnable
    {
        static final int ANIMATION_DURATION_MS = 190;
        
        static final int ANIMATION_FPS = 1000 / 60;
        
        private final Interpolator mInterpolator;
        
        private final int mScrollToY;
        
        private final int mScrollFromY;
        
        private final Handler mHandler;
        
        private boolean mContinueRunning = true;
        
        private long mStartTime = -1;
        
        private int mCurrentY = -1;
        
        /**
         * @param handler Handler
         * @param fromY 起始Y坐标
         * @param toY 终止Y坐标
         */
        public SmoothScrollRunnable(Handler handler, int fromY, int toY)
        {
            mHandler = handler;
            mScrollFromY = fromY;
            mScrollToY = toY;
            mInterpolator = new AccelerateDecelerateInterpolator();
        }
        
        @Override
        public void run()
        {
            
            /**
             * Only set mStartTime if this is the first time we're starting,
             * else actually calculate the Y delta
             */
            if (mStartTime == -1)
            {
                mStartTime = System.currentTimeMillis();
            }
            else
            {
                
                /**
                 * We do do all calculations in long to reduce software float
                 * calculations. We use 1000 as it gives us good accuracy and
                 * small rounding errors
                 */
                long normalizedTime = (1000 * (System.currentTimeMillis() - mStartTime))
                        / ANIMATION_DURATION_MS;
                normalizedTime = Math.max(Math.min(normalizedTime, 1000), 0);
                
                final int deltaY = Math.round((mScrollFromY - mScrollToY)
                        * mInterpolator.getInterpolation(normalizedTime / 1000f));
                mCurrentY = mScrollFromY - deltaY;
                setHeaderScroll(mCurrentY);
            }
            
            // If we're not at the target Y, keep going...
            if (mContinueRunning && mScrollToY != mCurrentY)
            {
                mHandler.postDelayed(this, ANIMATION_FPS);
            }
        }
        
        /**
         * progressBar停止
         */
        public void stop()
        {
            mContinueRunning = false;
            mHandler.removeCallbacks(this);
        }
    }
    
    // ===========================================================
    // Fields
    // ===========================================================
    
    private int mTouchSlop;
    
    private float mInitialMotionY;
    
    private float mLastMotionX;
    
    private float mLastMotionY;
    
    private boolean mIsBeingDragged = false;
    
    private int mState = PULL_TO_REFRESH;
    
    private T mRefreshableView;
    
    private boolean mPullToRefreshEnabled = true;
    
    private LoadingLayout mHeaderLayout;
    
    private int mHeaderHeight;
    
    private final Handler mHandler = new Handler();
    
    private OnRefreshListener mOnRefreshListener;
    
    private SmoothScrollRunnable mCurrentSmoothScrollRunnable;
    
    public PullToRefreshBase(Context context)
    {
        super(context);
        init(context, null);
    }
    
    /**
     * [构造简要说明]
     * @param context 上下文
     * @param attrs 参数
     */
    public PullToRefreshBase(Context context, AttributeSet attrs)
    {
        super(context, attrs);
        init(context, attrs);
    }
    
    /**
     * Get the Wrapped Refreshable View. Anything returned here has already been
     * added to the content view.
     * 
     * @return The View which is currently wrapped
     */
    public final T getRefreshableView()
    {
        return mRefreshableView;
    }
    
    /**
     * Whether Pull-to-Refresh is enabled
     * 
     * @return enabled
     */
    public final boolean isPullToRefreshEnabled()
    {
        return mPullToRefreshEnabled;
    }
    
    /**
     * 
     * 判断现在正在刷新
     * @return 是否正在刷新
     */
    public final boolean isRefreshing()
    {
        return mState == REFRESHING;
    }
    
    /**
     * Mark the current Refresh as complete. Will Reset the UI and hide the
     * Refreshing View
     */
    public final void onRefreshComplete()
    {
        if (mState != PULL_TO_REFRESH)
        {
            resetHeader();
        }
    }
    
    /**
     * Set OnRefreshListener for the Widget
     * 
     * @param listener
     *            - Listener to be used when the Widget is set to Refresh
     */
    public final void setOnRefreshListener(OnRefreshListener listener)
    {
        mOnRefreshListener = listener;
    }
    
    /**
     * A mutator to enable/disable Pull-to-Refresh for the current View
     * 
     * @param enable
     *            Whether Pull-To-Refresh should be used
     */
    public final void setPullToRefreshEnabled(boolean enable)
    {
        mPullToRefreshEnabled = enable;
    }
    
    /**
     * Set Text to show when the Widget is refreshing
     * 
     * @param refreshingLabel
     *            - String to display
     */
    public void setRefreshingLabel(String refreshingLabel)
    {
        if (null != mHeaderLayout)
        {
            mHeaderLayout.setRefreshingLabel(refreshingLabel);
        }
    }
    
    /**
     * Sets the Widget to be in the refresh mState. The UI will be updated to
     * show the 'Refreshing' view.
     * 
     * @param doScroll
     *            - true if you want to force a scroll to the Refreshing view.
     */
    public final void setRefreshing(boolean doScroll)
    {
        if (!isRefreshing())
        {
            setRefreshingInternal(doScroll);
        }
    }
    
    /**
     * 
     * onTouchEvent，Move的时候的处理方式
     * @param event 事件源
     * @return  返回真假
     */
    private boolean touchMotionEventMove(MotionEvent event)
    {
        if (mIsBeingDragged)
        {
            mLastMotionY = event.getY();
            pullEvent();
            return true;
        }
        return false;
    }
    
    @Override
    public final boolean onTouchEvent(MotionEvent event)
    {
        if (!mPullToRefreshEnabled || isRefreshing())
        {
            return false;
        }
        
        if (event.getAction() == MotionEvent.ACTION_DOWN
                && event.getEdgeFlags() != 0)
        {
            return false;
        }
        
        switch (event.getAction())
        {
        
            case MotionEvent.ACTION_MOVE:
            {
                return touchMotionEventMove(event);
            }
            
            case MotionEvent.ACTION_DOWN:
            {
                if (isReadyForPullDown())
                {
                    mLastMotionY = mInitialMotionY = event.getY();
                    return true;
                }
                break;
            }
            
            case MotionEvent.ACTION_CANCEL:
            case MotionEvent.ACTION_UP:
            {
                if (mIsBeingDragged)
                {
                    mIsBeingDragged = false;
                    
                    if (mState == RELEASE_TO_REFRESH)
                    {
                        return stateReleaseRefresh();
                    }
                    
                    smoothScrollTo(0);
                    return true;
                }
                break;
            }
        }
        
        return false;
    }
    
    /**
     * 状态是释放刷新的时候
     * @return 返回true
     */
    public boolean stateReleaseRefresh()
    {
        if (null != mOnRefreshListener)
        {
            setRefreshingInternal(true);
            mOnRefreshListener.onRefresh();
        }
        
        return true;
    }
    
    @Override
    public final boolean onInterceptTouchEvent(MotionEvent event)
    {
        
        if (!mPullToRefreshEnabled || isRefreshing())
        {
            return false;
        }
        
        final int action = event.getAction();
        
        if (action == MotionEvent.ACTION_CANCEL
                || action == MotionEvent.ACTION_UP)
        {
            mIsBeingDragged = false;
            return false;
        }
        
        if (action != MotionEvent.ACTION_DOWN && mIsBeingDragged)
        {
            return true;
        }
        
        switch (action)
        {
            case MotionEvent.ACTION_MOVE:
            {
                motionEventMove(event);
                break;
            }
            case MotionEvent.ACTION_DOWN:
            {
                motionEventDown(event);
                break;
            }
        }
        
        return mIsBeingDragged;
    }
    
    /**
     * 处理移动事件
     * @param event MotionEvent
     */
    private void motionEventMove(MotionEvent event)
    {
        if (isReadyForPullDown())
        {
            final float y = event.getY();
            final float dy = y - mLastMotionY;
            final float yDiff = Math.abs(dy);
            final float xDiff = Math.abs(event.getX() - mLastMotionX);
            
            if (yDiff > mTouchSlop && yDiff > xDiff)
            {
                if (dy >= 0.0001f && isReadyForPullDown())
                {
                    mLastMotionY = y;
                    mIsBeingDragged = true;
                }
            }
        }
    }
    
    /**
     * 事件下拉的处理方式
     * @param event MotionEvent
     */
    private void motionEventDown(MotionEvent event)
    {
        if (isReadyForPullDown())
        {
            mLastMotionY = mInitialMotionY = event.getY();
            mLastMotionX = event.getX();
            mIsBeingDragged = false;
        }
    }
    
    /**
     * 增加可用视图
     * @param context   上下文
     * @param refreshableView 可用视图
     */
    protected void addRefreshableView(Context context, T refreshableView)
    {
        addView(refreshableView, new LinearLayout.LayoutParams(
                LayoutParams.FILL_PARENT, 0, 1.0f));
    }
    
    /**
     * This is implemented by derived classes to return the created View. If you
     * need to use a custom View (such as a custom ListView), override this
     * method and return an instance of your custom class.
     * 
     * Be sure to set the ID of the view in this method, especially if you're
     * using a ListActivity or ListFragment.
     * 
     * @param context
     *            Context to create view with
     * @param attrs
     *            AttributeSet from wrapped class. Means that anything you
     *            include in the XML layout declaration will be routed to the
     *            created View
     * @return New instance of the Refreshable View
     */
    protected abstract T createRefreshableView(Context context,
            AttributeSet attrs);
    
    /**
     * get HeaderLayout
     * @return HeaderLayout
     */
    protected final LoadingLayout getHeaderLayout()
    {
        return mHeaderLayout;
    }
    
    /**
     * get HeaderHeight
     * @return HeaderHeight
     */
    protected final int getHeaderHeight()
    {
        return mHeaderHeight;
    }
    
    /**
     * get State
     * @return State
     */
    protected final int getState()
    {
        return mState;
    }
    
    /**
     * Implemented by derived class to return whether the View is in a mState
     * where the user can Pull to Refresh by scrolling down.
     * 
     * @return true if the View is currently the correct mState (for example,
     *         top of a ListView)
     */
    protected abstract boolean isReadyForPullDown();
    
    /**
     * 重置顶部
     */
    protected void resetHeader()
    {
        mState = PULL_TO_REFRESH;
        mIsBeingDragged = false;
        
        if (null != mHeaderLayout)
        {
            mHeaderLayout.reset();
        }
        
        smoothScrollTo(0);
    }
    
    /**
     * 设置重新刷新
     * @param doScroll  是否滚动
     */
    protected void setRefreshingInternal(boolean doScroll)
    {
        mState = REFRESHING;
        
        if (null != mHeaderLayout)
        {
            mHeaderLayout.refreshing();
        }
        
        if (doScroll)
        {
            smoothScrollTo(-mHeaderHeight);
        }
    }
    
    /**
     * 设置移动到哪
     * @param y 坐标
     */
    protected final void setHeaderScroll(int y)
    {
        scrollTo(0, y);
    }
    
    /**
     * smooth移动到哪
     * @param y 坐标
     */
    protected final void smoothScrollTo(int y)
    {
        if (null != mCurrentSmoothScrollRunnable)
        {
            mCurrentSmoothScrollRunnable.stop();
        }
        
        if (getScrollY() != y)
        {
            mCurrentSmoothScrollRunnable = new SmoothScrollRunnable(mHandler,
                    getScrollY(), y);
            mHandler.post(mCurrentSmoothScrollRunnable);
        }
    }
    
    /**
     * 初始化视图
     * @param context Context
     * @param attrs AttributeSet
     */
    private void init(Context context, AttributeSet attrs)
    {
        
        setOrientation(LinearLayout.VERTICAL);
        
        mTouchSlop = ViewConfiguration.getTouchSlop();
        
        mRefreshableView = createRefreshableView(context, attrs);
        mRefreshableView.setId(NO_ID);
        addRefreshableView(context, mRefreshableView);
        
        // Loading View Strings
        String pullLabel = context.getString(R.string.pull_to_refresh_pull_label);
        String refreshingLabel = context.getString(R.string.pull_to_refresh_refreshing_default_label);
        String releaseLabel = context.getString(R.string.pull_to_refresh_release_label);
        
        // Add Loading Views
        mHeaderLayout = new LoadingLayout(context, releaseLabel, pullLabel,
                refreshingLabel);
        addView(mHeaderLayout, 0, new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.FILL_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT));
        measureView(mHeaderLayout);
        mHeaderHeight = mHeaderLayout.getMeasuredHeight();
        
        // Hide Loading Views
        setPadding(0, -mHeaderHeight, 0, 0);
    }
    
    /**
     * 绘制 View
     * @param child 子视图
     */
    private void measureView(View child)
    {
        ViewGroup.LayoutParams p = child.getLayoutParams();
        if (p == null)
        {
            p = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.FILL_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT);
        }
        
        int childWidthSpec = ViewGroup.getChildMeasureSpec(0, 0, p.width);
        int lpHeight = p.height;
        int childHeightSpec;
        if (lpHeight > 0)
        {
            childHeightSpec = MeasureSpec.makeMeasureSpec(lpHeight,
                    MeasureSpec.EXACTLY);
        }
        else
        {
            childHeightSpec = MeasureSpec.makeMeasureSpec(0,
                    MeasureSpec.UNSPECIFIED);
        }
        child.measure(childWidthSpec, childHeightSpec);
    }
    
    /**
     * Actions a Pull Event
     * 
     * @return true if the Event has been handled, false if there has been no
     *         change
     */
    private boolean pullEvent()
    {
        
        final int newHeight;
        final int oldHeight = getScrollY();
        
        newHeight = Math.round(Math.min(mInitialMotionY - mLastMotionY, 0)
                / FRICTION);
        
        setHeaderScroll(newHeight);
        
        if (newHeight != 0)
        {
            if (mState == PULL_TO_REFRESH
                    && mHeaderHeight < Math.abs(newHeight))
            {
                mState = RELEASE_TO_REFRESH;
                
                mHeaderLayout.releaseToRefresh();
                
                return true;
                
            }
            else if (mState == RELEASE_TO_REFRESH
                    && mHeaderHeight >= Math.abs(newHeight))
            {
                mState = PULL_TO_REFRESH;
                
                mHeaderLayout.pullToRefresh();
                
                return true;
            }
        }
        
        return oldHeight != newHeight;
    }
    
    @Override
    public void setLongClickable(boolean longClickable)
    {
        getRefreshableView().setLongClickable(longClickable);
    }
    
    /**
     * 设置头部时间
     * @param time 设置头部时间
     */
    public void setHeaderTime(String time)
    {
        //        String data = getResources().getString(R.string.wall_last_update_time_minutes);
        //        data = String.format(data, time);
        if (null != mHeaderLayout)
        {
            mHeaderLayout.setHeaderTime(time);
        }
    }
    
}
