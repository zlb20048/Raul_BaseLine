package com.raul.basic.android.ui.basic.listview;

import android.content.Context;
import android.util.AttributeSet;
import android.util.SparseBooleanArray;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.ListView;

import com.raul.basic.android.util.Log;

/**
 * 
 * 支持长按功能的自定义ListView
 * 
 * @author xiaomiaomiao
 */
public class LongClickListView extends ListView {

	/**
	 * 支持长按多选的mode
	 */
	public static final int MODE_LONG_CLICK = 1;

	/**
	 * 支持长按单选的mode
	 */
	public static final int MODE_LONG_CLICK_SINGLE = 2;

	/**
	 * TAG
	 */
	private static final String TAG = "LongClickListView";

	/**
	 * 记录当前的Mode
	 */
	private int mode;

	/**
	 * 长按事件给页面的回调
	 */
	private LongClickCallBack mLongClickCallBack;

	// 是否在长按mode中
	private boolean onLongClickStatus;

	private LongClickListener mLongClickListener = new LongClickListener();

	private ItemClickListener mItemClickListener = new ItemClickListener();

	// 用于存储item状态键值对
	private SparseBooleanArray mCheckedRecords;

	private BaseListAdapter mAdapter;

	/**
	 * 构造方法
	 * 
	 * @param context
	 *            Context
	 * @param attrs
	 *            AttributeSet
	 */
	public LongClickListView(Context context, AttributeSet attrs) {
		super(context, attrs);
		super.setOnItemLongClickListener(mLongClickListener);
		super.setOnItemClickListener(mItemClickListener);
	}

	/**
	 * 是否是长按状态
	 * 
	 * @return boolean 长按状态
	 */
	public boolean isOnLongClickStatus() {
		return onLongClickStatus;
	}

	/**
	 * 
	 * 设置listView的mode
	 * 不调用此方法，就是普通的listview
	 * 
	 * @param mode
	 *            listview的mode
	 */
	public void setMode(int mode) {
		this.mode = mode;
	}

	/**
	 * 
	 * 设置长按事件回调
	 * 
	 * @param callBack
	 *            长按事件回调
	 */
	public void setLongClickCallBack(LongClickCallBack callBack) {
		if (mode == MODE_LONG_CLICK || mode == MODE_LONG_CLICK_SINGLE) {
			this.mLongClickCallBack = callBack;
		}
	}

	/**
	 * 
	 * 退出长按状态
	 */
	public void quitLongClick() {
		if ((mode == MODE_LONG_CLICK || mode == MODE_LONG_CLICK_SINGLE)
				&& onLongClickStatus) {
			onLongClickStatus = false;
			mCheckedRecords.clear();

			mAdapter.setCheckedRecords(mCheckedRecords);
			mAdapter.setOnLongClickStatus(onLongClickStatus);
			mAdapter.notifyDataSetChanged();

			mLongClickCallBack.destroyLongClick();
			super.setOnItemLongClickListener(mLongClickListener);
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setOnItemLongClickListener(OnItemLongClickListener listener) {
		mLongClickListener.setOnItemLongClickListener(listener);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setOnItemClickListener(OnItemClickListener listener) {
		mItemClickListener.setItemClickListener(listener);
	}

	public void setLongClickListener(OnItemLongClickListener longClickListener) {
		super.setOnItemLongClickListener(longClickListener);
	}

	/**
	 * 
	 * 长按事件的操作
	 * 
	 * @param position
	 *            长按的位置
	 * @param view
	 *            长按的View
	 * @return 处理结果
	 */
	public boolean performItemLongClickForSelect(int position, View view) {
		// 先判断是否是长按模式
		if (mode == MODE_LONG_CLICK || mode == MODE_LONG_CLICK_SINGLE) {
			// 是否已经进入长按状态
			if (!onLongClickStatus) {
				super.setOnItemLongClickListener(null);
				initCheckedRecords();
				int count = mAdapter.getCount();
				mLongClickCallBack.startLongClick();
				Log.d(TAG, "position = " + position);
				dealLongClick(mAdapter.newPosition(count, position), view);
			}
			return true;
		}
		return false;
	}

	/**
	 * 在长按mode下，单击item做选中和取消选中操作
	 * 
	 * @param position
	 *            点击的位置
	 * @param view
	 *            点击的View
	 * @return 处理结果
	 */
	public boolean performItemClickToSelect(int position, View view) {
		if ((mode == MODE_LONG_CLICK || mode == MODE_LONG_CLICK_SINGLE)
				&& onLongClickStatus) {
			dealLongClick(position, view);
			return true;
		}
		return false;
	}

	/**
	 * 是否已经全选
	 * 
	 * @return boolean 是否已经全选
	 */
	public boolean isChooseAll() {
		return mAdapter.isChooseAll(mCheckedRecords);
	}

	/**
	 * 全选
	 */
	public void chooseAll() {
		mAdapter.chooseAll(mCheckedRecords);
	}

	/**
	 * 获取已选条目
	 * 
	 * @return int 已选条目
	 */
	public int getSelectedCount() {
		return mAdapter.getSelectedCount();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setAdapter(ListAdapter adapter) {
		mAdapter = (BaseListAdapter) adapter;
		super.setAdapter(adapter);
	}

	/**
	 * 
	 * 长按事件的监听器
	 * 主要是做了一个缓冲，默认先调用该类的方法，不成功则再调用子类的方法
	 * 
	 * @author tjzhang
	 * @version [RCS Client V100R001C03, 2012-6-2]
	 */
	private class LongClickListener implements OnItemLongClickListener {
		private OnItemLongClickListener wrapper;

		/**
		 * 设置OnItemLongClickListener
		 * 
		 * @param listener
		 *            listener
		 */
		public void setOnItemLongClickListener(OnItemLongClickListener listener) {
			this.wrapper = listener;
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public boolean onItemLongClick(AdapterView<?> parent, View view,
				int position, long id) {
			position -= getHeaderViewsCount();
			if (!performItemLongClickForSelect(position, view)) {
				if (null != wrapper) {
					wrapper.onItemLongClick(parent, view, position, id);
				}
			}
			return true;
		}
	}

	/**
	 * 
	 * 点击事件的监听器
	 * 主要是做了一个缓冲，默认先调用该类的方法，不成功则再调用子类的方法
	 * 
	 * @author tjzhang
	 * @version [RCS Client V100R001C03, 2012-6-2]
	 */
	private class ItemClickListener implements OnItemClickListener {
		private OnItemClickListener wrapper;

		/**
		 * 设置OnItemClickListener
		 * 
		 * @param listener
		 *            listener
		 */
		public void setItemClickListener(OnItemClickListener listener) {
			this.wrapper = listener;
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {
			position -= getHeaderViewsCount();
			if (!performItemClickToSelect(position, view)) {
				if (null != wrapper) {
					wrapper.onItemClick(parent, view, position, id);
				}
			}
		}
	}

	/**
	 * 初始化checkedRecords
	 */
	private void initCheckedRecords() {
		if (mCheckedRecords == null) {
			mCheckedRecords = new SparseBooleanArray();
		} else {
			mCheckedRecords.clear();
		}
	}

	/**
	 * 
	 * 处理点击事件
	 * 
	 * @param position
	 *            点击的位置
	 * @param view
	 *            点击的View
	 */
	private void dealLongClick(int position, View view) {
		if (mode == MODE_LONG_CLICK_SINGLE) {
			mCheckedRecords.clear();
		}
		mCheckedRecords.put(position, !mCheckedRecords.get(position));
		onLongClickStatus = hasSelectedItem();
		mAdapter.setCheckedRecords(mCheckedRecords);
		mAdapter.setOnLongClickStatus(onLongClickStatus);
		// ----modified by daifeng
		// 修改点击一个item后，该item很久才能改变选中状态的问题
		mAdapter.setViewBackground(position, view);
		// mAdapter.notifyDataSetChanged();
		// ----modified by daifeng
		mLongClickCallBack.itemStatusChanged(position,
				mCheckedRecords.get(position), view);
		if (!onLongClickStatus) {
			mLongClickCallBack.destroyLongClick();
			super.setOnItemLongClickListener(mLongClickListener);
		}
	}

	private boolean hasSelectedItem() {
		int size = mCheckedRecords.size();
		for (int i = 0; i < size; i++) {
			if (mCheckedRecords.valueAt(i)) {
				return true;
			}
		}
		return false;
	}
}
