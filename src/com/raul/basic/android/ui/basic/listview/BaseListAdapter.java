package com.raul.basic.android.ui.basic.listview;

import java.util.ArrayList;
import java.util.List;

import android.util.SparseBooleanArray;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.raul.basic.android.R;

/**
 * 多选列表的adapter
 */
public abstract class BaseListAdapter extends BaseAdapter

{
	/**
	 * 控制显示选中未选中状态的键值对
	 */
	private SparseBooleanArray mCheckedRecords;

	/**
	 * 是否在长按状态下
	 */
	private boolean mOnLongClickStatus;

	/**
	 * adapter显示的数据源
	 */
	private List<? extends Object> listItems;

	/**
	 * 
	 * 设置list展示的数据源
	 * 
	 * @param list
	 *            数据List
	 */
	public void setData(List<? extends Object> list) {
		listItems = list;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public int getCount() {
		return listItems != null ? listItems.size() : 0;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Object getItem(int position) {
		if (null != listItems && listItems.size() > position) {
			return listItems.get(position);
		}
		return null;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public long getItemId(int position) {
		return position;
	}

	/**
	 * 
	 * 子类adapter 需要实现不同的Item展示
	 * 
	 * @param position
	 *            item位置
	 * @param convertView
	 *            item view
	 * @param parent
	 *            parent
	 * @return 显示的view
	 */
	protected abstract View getItemView(int position, View convertView,
			ViewGroup parent);

	/**
	 * 
	 * 获取item的背景id 正常状态下的背景Id,父类提供一个默认的，子类可以根据需要重写该方法
	 * 
	 * @param position
	 *            item位置
	 * @return 背景id
	 */
	protected int getViewBgId(int position) {
		return getViewBgId();
	}

	/**
	 * 
	 * 获取item的背景id 正常状态下的背景Id,父类提供一个默认的，子类可以根据需要重写该方法
	 * 
	 * @return 背景id
	 */
	protected int getViewBgId() {
		return R.drawable.bg_listitem_selector;
	}

	/**
	 * 
	 * 长按状态下，获取没有选中的item的背景id 获取没有选中的item背景Id,父类提供一个默认的，子类可以根据需要重写该方法
	 * 
	 * @param position
	 *            当前位置
	 * @return 背景id
	 */
	protected int getDefaultViewBgId(int position) {
		return R.drawable.bg_listitem_normal;
	}

	/**
	 * 
	 * 长按状态下，获取选中的item的背景id 获取选中的item背景Id,父类提供一个默认的，子类可以根据需要重写该方法
	 * 
	 * @param position
	 *            当前位置
	 * @return 背景id
	 */
	protected int getSelectedViewBgId(int position) {
		return R.drawable.bg_listitem_selected;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View view = getItemView(position, convertView, parent);
		setViewBackground(position, view);
		return view;
	}

	/**
	 * 设置条目背景
	 * 
	 * @param position
	 *            条目的下标
	 * @param itemView
	 *            条目的view
	 */
	protected void setViewBackground(int position, View itemView) {
		if (setViewBackground(position, itemView, mOnLongClickStatus,
				null != mCheckedRecords && mCheckedRecords.get(position))) {
			return;
		}
		View view = null;
		if (-1 != getSelectedViewId()) {
			view = itemView.findViewById(getSelectedViewId());
		}
		if (mOnLongClickStatus) {
			if (mCheckedRecords.get(position)) {
				itemView.setBackgroundResource(getSelectedViewBgId(position));
				if (null != view) {
					view.setVisibility(View.VISIBLE);
				}
			} else {
				itemView.setBackgroundResource(getDefaultViewBgId(position));
				if (null != view) {
					view.setVisibility(View.INVISIBLE);
				}
			}
		} else {
			itemView.setBackgroundResource(getViewBgId(position));
			if (null != view) {
				view.setVisibility(View.INVISIBLE);
			}
		}
	}

	/**
	 * 
	 * 设置条目背景
	 * 
	 * @param position
	 *            条目的下标
	 * @param itemView
	 *            条目的view
	 * @param onLongClickStatus
	 *            是否长按状态
	 * @param selected
	 *            条目是否选中
	 * @return 是否已设定条目背景
	 */
	protected boolean setViewBackground(int position, View itemView,
			boolean onLongClickStatus, boolean selected) {
		return false;
	}

	/**
	 * 
	 * 长按状态下，获取选中item标记的id
	 * 
	 * @return 选中item标记的id
	 */
	protected int getSelectedViewId() {
		return -1;
	}

	/**
	 * 
	 * 获取当前adapter选中的数目 此方法只有在长按的过程中有效
	 * 
	 * @return 选中的数目
	 */
	public int getSelectedCount() {
		int count = 0;
		if (null != mCheckedRecords && mCheckedRecords.size() > 0) {
			for (int i = 0; i < mCheckedRecords.size(); i++) {
				if (mCheckedRecords.valueAt(i)) {
					count++;
				}
			}
		}
		return count;
	}

	/**
	 * 拿到所有的被选中项的位置集合
	 * 
	 * @return ArrayList<Integer>
	 */
	public ArrayList<Object> getSelectedObject() {
		ArrayList<Object> selectedObjects = new ArrayList<Object>();
		for (int i = 0; i < mCheckedRecords.size(); i++) {
			if (mCheckedRecords.valueAt(i)) {
				selectedObjects.add(getItem(mCheckedRecords.keyAt(i)));
			}
		}
		return selectedObjects;
	}

	/**
	 * 设置选中键值对
	 * 
	 * @param checkedRecords
	 *            the checkedRecords to set
	 */
	public void setCheckedRecords(SparseBooleanArray checkedRecords) {
		this.mCheckedRecords = checkedRecords;
	}

	/**
	 * 设置长按状态
	 * 
	 * @param onLongClickStatus
	 *            the onLongClickStatus to set
	 */
	public void setOnLongClickStatus(boolean onLongClickStatus) {
		this.mOnLongClickStatus = onLongClickStatus;
	}

	/**
	 * 获取数据源
	 * 
	 * @return ArrayList<? extends Object> list
	 */
	public List<? extends Object> getDataSrc() {
		return listItems;
	}

	/**
	 * 
	 * 重新计算选定位置，用于长按状态有变更的列表进入长按状态时。
	 * 
	 * @param oldCount
	 *            变更前总条目
	 * @param oldPosition
	 *            变更前位置
	 * @return int 新位置
	 */
	public int newPosition(int oldCount, int oldPosition) {
		if (getCount() < oldCount) {
			oldPosition--;
		}
		return oldPosition;
	}

	/**
	 * 全选
	 * 
	 * @param checkedRecords
	 *            the SparseBooleanArray to chooseAll
	 */
	public void chooseAll(SparseBooleanArray checkedRecords) {
		for (int i = getCount() - 1; i >= 0; i--) {
			if (isEnabled(i)) {
				checkedRecords.put(i, true);
			}
		}
		notifyDataSetChanged();
	}

	/**
	 * 是否已经全选
	 * 
	 * @param checkedRecords
	 *            the SparseBooleanArray to chooseAll
	 * 
	 * @return boolean 是否已经全选
	 */
	public boolean isChooseAll(SparseBooleanArray checkedRecords) {
		for (int i = getCount() - 1; i >= 0; i--) {
			if (isEnabled(i) && !checkedRecords.get(i)) {
				return false;
			}
		}
		return true;
	}

}
