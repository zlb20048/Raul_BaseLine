package com.raul.basic.android.ui.basic.pullrefresh;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.AbsListView;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import com.raul.basic.android.ui.basic.pullrefresh.internal.EmptyViewMethodAccessor;

/**
 * 下拉刷新的适配器类
 * 
 * @author xiaomiaomiao
 * 
 * @param <T>
 */
public abstract class PullToRefreshAdapterViewBase<T extends AbsListView>
		extends PullToRefreshBase<T> {

	/**
	 * 空view的显示
	 */
	private View mEmptyView;

	/**
	 * 下拉刷新的布局
	 */
	private FrameLayout mRefreshableViewHolder;

	/**
	 * 构造函数 设置监听
	 * 
	 * @param context
	 *            上下文
	 */
	public PullToRefreshAdapterViewBase(Context context) {
		super(context);
	}

	/**
	 * 构造函数
	 * 
	 * @param context
	 *            上下文
	 * @param attrs
	 *            属性集合
	 */
	public PullToRefreshAdapterViewBase(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	/**
	 * Sets the Empty View to be used by the Adapter View.
	 * 
	 * We need it handle it ourselves so that we can Pull-to-Refresh when the
	 * Empty View is shown.
	 * 
	 * Please note, you do <strong>not</strong> usually need to call this method
	 * yourself. Calling setEmptyView on the AdapterView will automatically call
	 * this method and set everything up. This includes when the Android
	 * Framework automatically sets the Empty View based on it's ID.
	 * 
	 * @param newEmptyView
	 *            - Empty View to be used
	 */
	public final void setEmptyView(View newEmptyView) {
		// If we already have an Empty View, remove it
		if (null != mEmptyView) {
			mRefreshableViewHolder.removeView(mEmptyView);
		}

		if (null != newEmptyView) {
			// New view needs to be clickable so that Android recognizes it as a
			// target for Touch Events
			newEmptyView.setClickable(true);

			ViewParent newEmptyViewParent = newEmptyView.getParent();
			if (null != newEmptyViewParent
					&& newEmptyViewParent instanceof ViewGroup) {
				((ViewGroup) newEmptyViewParent).removeView(newEmptyView);
			}

			mRefreshableViewHolder.addView(newEmptyView,
					ViewGroup.LayoutParams.FILL_PARENT,
					ViewGroup.LayoutParams.FILL_PARENT);

			if (getRefreshableView() instanceof EmptyViewMethodAccessor) {
				((EmptyViewMethodAccessor) getRefreshableView())
						.setEmptyViewInternal(newEmptyView);
			} else {
				getRefreshableView().setEmptyView(newEmptyView);
			}
		}
	}

	/**
	 * 增加可用视图
	 * 
	 * @param context
	 *            Context
	 * @param refreshableView
	 *            AbsListView
	 */
	protected void addRefreshableView(Context context, T refreshableView) {
		mRefreshableViewHolder = new FrameLayout(context);
		mRefreshableViewHolder.addView(refreshableView,
				ViewGroup.LayoutParams.FILL_PARENT,
				ViewGroup.LayoutParams.FILL_PARENT);
		addView(mRefreshableViewHolder, new LinearLayout.LayoutParams(
				LayoutParams.FILL_PARENT, 0, 1.0f));
	};

	/**
	 * 是否准备好下拉
	 * 
	 * @return true已准备好，false 未准备好
	 */
	protected boolean isReadyForPullDown() {
		if (getRefreshableView().getFirstVisiblePosition() == 0) {
			final View firstVisibleChild = getRefreshableView().getChildAt(0);

			if (firstVisibleChild != null) {
				return firstVisibleChild.getTop() >= getRefreshableView()
						.getTop();
			}
		}

		return false;
	}

}
