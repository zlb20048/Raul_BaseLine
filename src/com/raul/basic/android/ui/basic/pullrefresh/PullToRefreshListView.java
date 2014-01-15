package com.raul.basic.android.ui.basic.pullrefresh;

import android.content.Context;
import android.util.AttributeSet;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.raul.basic.android.R;
import com.raul.basic.android.ui.basic.listview.LongClickListView;
import com.raul.basic.android.ui.basic.pullrefresh.internal.EmptyViewMethodAccessor;
import com.raul.basic.android.ui.basic.pullrefresh.internal.LoadingLayout;
import com.raul.basic.android.util.Log;

/**
 * 
 * 下拉刷新类
 * 
 * @author xiaomiaomiao
 */
public class PullToRefreshListView extends
		PullToRefreshAdapterViewBase<LongClickListView> {
	/**
	 * TAG
	 */
	private static final String TAG = "PullToRefreshListView";

	/**
	 * 头部的布局
	 */
	private LoadingLayout mHeaderLoadingView;

	/**
	 * 底部的view，目前是一个阴影+加载更多操作栏
	 */
	private View mFootView;

	/**
	 * 底部view,获得更多布局
	 */
	private RelativeLayout mGetMoreLayout;

	/**
	 * 底部view,获得更多布局的ProgressBar
	 */
	private ProgressBar mGetMoreProgressBar;

	/**
	 * 底部view,获得更多布局的TextView
	 */
	private TextView mGetMoreTextView;

	/**
	 * 底部view,获得更多布局的点击获取更多按钮
	 */
	private Button mGetMoreButton;

	/**
	 * 
	 * 内部Listview
	 * 
	 */
	class InternalListView extends LongClickListView implements
			EmptyViewMethodAccessor {
		/**
		 * 
		 * 构造函数
		 * 
		 * @param context
		 * @param attrs
		 */
		public InternalListView(Context context, AttributeSet attrs) {
			super(context, attrs);
		}

		@Override
		public void setEmptyView(View emptyView) {
			PullToRefreshListView.this.setEmptyView(emptyView);
		}

		@Override
		public void setEmptyViewInternal(View emptyView) {
			super.setEmptyView(emptyView);
		}

		/**
		 * 
		 * 获得菜单信息
		 * 
		 * @return super.getContextMenuInfo()
		 * @see android.widget.AbsListView#getContextMenuInfo()
		 */
		public ContextMenuInfo getContextMenuInfo() {
			return super.getContextMenuInfo();
		}
	}

	/**
	 * 构造函数
	 * 
	 * @param context
	 *            Context
	 */
	public PullToRefreshListView(Context context) {
		super(context);
	}

	/**
	 * 构造函数
	 * 
	 * @param context
	 *            context
	 * @param attrs
	 *            设置滚动刷新方式
	 */
	public PullToRefreshListView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	@Override
	public ContextMenuInfo getContextMenuInfo() {
		return ((InternalListView) getRefreshableView()).getContextMenuInfo();
	}

	/**
	 * 
	 * 设置refresh标签
	 * 
	 * @param refreshingLabel
	 *            设置刷新标签
	 */
	public void setRefreshingLabel(String refreshingLabel) {
		super.setRefreshingLabel(refreshingLabel);

		if (null != mHeaderLoadingView) {
			mHeaderLoadingView.setRefreshingLabel(refreshingLabel);
		}
	}

	@Override
	protected final LongClickListView createRefreshableView(Context context,
			AttributeSet attrs) {
		LongClickListView lv = new InternalListView(context, attrs);

		// Loading View Strings
		String pullLabel = context
				.getString(R.string.pull_to_refresh_pull_label);
		String refreshingLabel = context
				.getString(R.string.pull_to_refresh_refreshing_default_label);
		String releaseLabel = context
				.getString(R.string.pull_to_refresh_release_label);

		// Add Loading Views
		FrameLayout frame = new FrameLayout(context);
		mHeaderLoadingView = new LoadingLayout(context, releaseLabel,
				pullLabel, refreshingLabel);
		frame.addView(mHeaderLoadingView, FrameLayout.LayoutParams.FILL_PARENT,
				FrameLayout.LayoutParams.WRAP_CONTENT);
		mHeaderLoadingView.setVisibility(View.GONE);
		lv.addHeaderView(frame, null, false);

		mFootView = inflate(context, R.layout.pull_to_refresh_get_more, null);
		lv.addFooterView(mFootView, null, false);
		mGetMoreLayout = (RelativeLayout) mFootView
				.findViewById(R.id.get_more_layout);
		mGetMoreProgressBar = (ProgressBar) mFootView
				.findViewById(R.id.get_more_refresh_progress);
		mGetMoreTextView = (TextView) mFootView
				.findViewById(R.id.get_more_refresh_text);
		mGetMoreButton = (Button) mFootView.findViewById(R.id.click_get_more);
		showGetMore(false);
		return lv;
	}

	/**
	 * 控制显示和隐藏GetMore布局
	 * 
	 * @param isShow
	 *            true:展示getMore布局 false:隐藏getMore布局
	 */
	public void showGetMore(boolean isShow) {
		if (isShow) {
			mGetMoreLayout.setVisibility(View.VISIBLE);
		} else {
			mGetMoreLayout.setVisibility(View.GONE);
		}
	}

	/**
	 * 获取GetMore布局
	 * 
	 * @return GetMore布局
	 */
	public Button getGetMoreButton() {
		return mGetMoreButton;
	}

	/**
	 * 设置获得更多按钮文字
	 * 
	 * @param text
	 *            获得更多按钮文字
	 */
	public void setGetMoreButtonText(String text) {
		mGetMoreButton.setText(text);
	}

	/**
	 * 设置正在获取更多
	 */
	public void setGettingMoreView() {
		mGetMoreButton.setVisibility(View.GONE);
		mGetMoreProgressBar.setVisibility(View.VISIBLE);
		mGetMoreTextView.setVisibility(View.VISIBLE);
	}

	/**
	 * 设置获取更多结束,恢复Get More布局
	 */
	public void setGetMoreFinishView() {
		mGetMoreButton.setVisibility(View.VISIBLE);
		mGetMoreProgressBar.setVisibility(View.GONE);
		mGetMoreTextView.setVisibility(View.GONE);
	}

	/**
	 * 
	 * 是否正在加载更多
	 * 
	 * @return 是否正在加载更多
	 */
	public boolean isLoadingMore() {
		return mGetMoreProgressBar.getVisibility() == View.VISIBLE ? true
				: false;
	}

	@Override
	public void setRefreshingInternal(boolean doScroll) {
		// If we're not showing the Refreshing view, or the list is empty, then
		// the header/footer views won't show so we use the
		// normal method

		super.setRefreshingInternal(doScroll);

		final LoadingLayout originalLoadingLayout, listViewLoadingLayout;
		final int selection, scrollToY;

		originalLoadingLayout = getHeaderLayout();
		listViewLoadingLayout = mHeaderLoadingView;
		selection = 0;
		scrollToY = getScrollY() + getHeaderHeight();

		if (doScroll) {
			// We scroll slightly so that the ListView's header/footer is at the
			// same Y position as our normal header/footer
			setHeaderScroll(scrollToY);
		}

		// Hide our original Loading View
		originalLoadingLayout.setVisibility(View.INVISIBLE);

		// Show the ListView Loading View and set it to refresh
		listViewLoadingLayout.setVisibility(View.VISIBLE);
		listViewLoadingLayout.refreshing();

		if (doScroll) {
			// Make sure the ListView is scrolled to show the loading
			// header/footer
			getRefreshableView().setSelection(selection);

			// Smooth scroll as normal
			smoothScrollTo(0);
		}
	}

	@Override
	protected void resetHeader() {
		// If we're not showing the Refreshing view, or the list is empty, then
		// the header/footer views won't show so we use the
		// normal method
		// ListAdapter adapter = getRefreshableView().getAdapter();
		// if (null == adapter || adapter.isEmpty())
		// {
		// super.resetHeader();
		// return;
		// }

		LoadingLayout originalLoadingLayout;
		LoadingLayout listViewLoadingLayout;

		int scrollToHeight = getHeaderHeight();
		int selection;

		originalLoadingLayout = getHeaderLayout();
		listViewLoadingLayout = mHeaderLoadingView;
		scrollToHeight *= -1;
		selection = 0;

		// Set our Original View to Visible
		originalLoadingLayout.setVisibility(View.VISIBLE);

		// Scroll so our View is at the same Y as the ListView header/footer,
		// but only scroll if we've pulled to refresh
		getRefreshableView().setSelection(selection);
		setHeaderScroll(scrollToHeight);

		// Hide the ListView Header/Footer
		listViewLoadingLayout.setVisibility(View.GONE);

		super.resetHeader();
	}

	/**
	 * 得到头部view的数值
	 * 
	 * @return 0 for non-ListView views, possibly 1 for ListView
	 * @see com.huawei.basic.android.zone.utils.pullrefresh.PullToRefreshAdapterViewBase#getNumberInternalHeaderViews()
	 */
	protected int getNumberInternalHeaderViews() {
		return null != mHeaderLoadingView ? 1 : 0;
	}

	/**
	 * 设置头部时间
	 * 
	 * @param time
	 *            时间字符串
	 */
	public void setListHeaderTime(String time) {
		try {
			if (null != mHeaderLoadingView) {
				mHeaderLoadingView.setHeaderTime(time);
				setHeaderTime(time);
			}
		} catch (Exception ex) {
			Log.e(TAG, "Exception " + ex.getMessage());
		}
	}

	/**
	 * 清除所有view
	 */
	public void clearAll() {
		try {
			removeAllViews();
		} catch (Exception ex) {
			Log.e(TAG, "clearAll()");
		}
	}

}
