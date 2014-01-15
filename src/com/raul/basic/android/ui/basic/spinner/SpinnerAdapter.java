package com.raul.basic.android.ui.basic.spinner;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.raul.basic.android.R;
import com.raul.basic.android.ui.basic.listview.BaseListAdapter;

/**
 * @author xiaomiaomiao
 */
public class SpinnerAdapter extends BaseListAdapter {
	private Context mContext;

	/**
	 * [构造简要说明]
	 * 
	 * @param context
	 *            Context
	 */
	public SpinnerAdapter(Context context) {
		mContext = context;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected View getItemView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder = null;
		if (convertView == null) {
			convertView = LinearLayout.inflate(mContext,
					R.layout.navigation_spinner_row, null);
			holder = new ViewHolder();
			holder.headImage = (ImageView) convertView
					.findViewById(R.id.navigation_spinner_image);
			holder.title = (TextView) convertView
					.findViewById(R.id.navigation_spinner_title);
			holder.subTitle = (TextView) convertView
					.findViewById(R.id.navigation_spinner_sub_title);
			holder.unreadNum = (TextView) convertView
					.findViewById(R.id.navigation_spinner_unread_num);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		SpinnerContentModel model = (SpinnerContentModel) getItem(position);
		holder.headImage.setBackgroundResource(model.getIconId());
		holder.title.setText(model.getTitle());
		holder.subTitle.setText(model.getSubTitle());
		int unreadNum = getUnreadCount(position);
		if (unreadNum > 0) {
			holder.unreadNum.setVisibility(View.VISIBLE);
			holder.unreadNum.setText(String.valueOf(unreadNum));
		} else {
			holder.unreadNum.setVisibility(View.GONE);
		}
		return convertView;
	}

	/**
	 * 获取未读数
	 * 
	 * @param position
	 *            当前位置
	 * @return 未读数
	 */
	protected int getUnreadCount(int position) {
		return 0;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected int getViewBgId() {
		return R.drawable.bg_spinner_item_selector;
	}

	public static class SpinnerContentModel {
		/**
		 * 显示的头像ID
		 */
		private int iconId;

		/**
		 * 显示的内容ID
		 */
		private String title;

		/**
		 * 子标题
		 */
		private String subTitle;

		/**
		 * model类型
		 */
		private int type;

		/**
		 * @return the iconId
		 */
		public int getIconId() {
			return iconId;
		}

		/**
		 * @param iconId
		 *            the iconId to set
		 */
		public void setIconId(int mIconId) {
			this.iconId = mIconId;
		}

		/**
		 * @return the titleId
		 */
		public String getTitle() {
			return title;
		}

		/**
		 * @param titleId
		 *            the titleId to set
		 */
		public void setTitle(String Title) {
			this.title = Title;
		}

		/**
		 * @return the subTitle
		 */
		public String getSubTitle() {
			return subTitle;
		}

		/**
		 * @param subTitle
		 *            the subTitle to set
		 */
		public void setSubTitle(String subTitle) {
			this.subTitle = subTitle;
		}

		/**
		 * @return the type
		 */
		public int getType() {
			return type;
		}

		/**
		 * @param type
		 *            the type to set
		 */
		public void setType(int modelType) {
			this.type = modelType;
		}

	}

	private static class ViewHolder {
		/**
		 * 显示会话列表中的头像
		 */
		private ImageView headImage;

		/**
		 * 显示的主标题
		 */
		private TextView title;

		/**
		 * 显示的子标题
		 */
		private TextView subTitle;

		/**
		 * 未读的消息数目
		 */
		private TextView unreadNum;

	}

}
