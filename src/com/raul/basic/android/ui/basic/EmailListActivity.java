package com.raul.basic.android.ui.basic;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import android.content.Context;
import android.os.Bundle;
import android.os.Message;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.TextView;

import com.raul.basic.android.R;
import com.raul.basic.android.ui.basic.actionbar.ActionBarMenuItem;
import com.raul.basic.android.ui.basic.listview.BaseListAdapter;
import com.raul.basic.android.ui.basic.listview.BasicMutiChoiceListActivity;
import com.raul.basic.android.ui.basic.listview.LongClickListView;
import com.raul.basic.android.ui.basic.pullrefresh.PullToRefreshListView;
import com.raul.basic.android.ui.basic.pullrefresh.internal.ListViewListener.OnRefreshListener;
import com.raul.basic.android.ui.basic.spinner.DropDownSpinner;
import com.raul.basic.android.ui.basic.spinner.SpinnerAdapter;
import com.raul.basic.android.ui.basic.spinner.SpinnerAdapter.SpinnerContentModel;
import com.raul.basic.android.util.Log;

public class EmailListActivity extends BasicMutiChoiceListActivity {

	private static final String TAG = "EmailListActivity";

	/**
	 * 删除选中Email
	 */
	private static final int MENU_DELETE_EMAIL = 4;

	/**
	 * 新建Email
	 */
	private static final int MENU_ADD_EMAIL = 0;

	/**
	 * 搜索Email
	 */
	private static final int MENU_SEARCH_EMAIL = 1;

	/**
	 * 将Email设置为已读
	 */
	private static final int MENU_UNREAD_EMAIL = 2;

	private static final int SPINNER_MODEL_TYPE_ADD_ACCONT = 1;

	/**
	 * 导航栏的adapter
	 */
	private EmailAccountNavigateAdapter mNavigateAdapter;

	/**
	 * ActionBar Title
	 */
	private TextView mSpinnerTitle;

	/**
	 * 未读email总数
	 */
	private TextView mUnreadNumText;

	/**
	 * EmailMoreAdapter
	 */
	private EmailMoreAdapter mAdapter;

	private PullToRefreshListView mPullToRefreshListview;

	@Override
	protected void handleStateMessage(Message msg) {
		super.handleStateMessage(msg);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		initBarView();
		initListData();
	}

	/**
	 * 初始化视图
	 */
	protected void initBarView() {
		initBar();
	}

	/**
	 * 创建menu菜单 添加需要显示的menu菜单,菜单显示顺序按添加顺序，多于2个的，第3个开始放到更多菜单项中
	 * 
	 * @param menus
	 *            menu组合
	 * @return int 显示的菜单条目
	 */
	protected int onCreateMenu(ArrayList<ActionBarMenuItem> menus) {
		menus.add(new ActionBarMenuItem(MENU_ADD_EMAIL,
				R.drawable.icon_email_newemail));
		menus.add(new ActionBarMenuItem(MENU_SEARCH_EMAIL,
				R.drawable.icon_titlebar_more, R.string.email_more_search));
		menus.add(new ActionBarMenuItem(MENU_UNREAD_EMAIL,
				R.drawable.icon_titlebar_more, R.string.email_more_unread));
		return 1;
	}

	/**
	 * 点击菜单项的处理
	 * 
	 * @param menuId
	 *            菜单id
	 */
	protected void onMenuClick(int menuId) {
		switch (menuId) {
		case MENU_ADD_EMAIL:
			showToast("Add Email!");
			break;
		case MENU_SEARCH_EMAIL:
			showToast("Search Email!");

			break;
		case MENU_UNREAD_EMAIL:
			showToast("Unread Email!");
			break;
		default:
			break;
		}
	}

	/**
	 * 初始化ActionBar
	 */
	private void initBar() {
		// 设置ActionBar左侧第三方联系人Menu
		View titleView = LinearLayout.inflate(this,
				R.layout.email_navigation_title, null);
		mUnreadNumText = (TextView) titleView
				.findViewById(R.id.unread_text_item);
		mSpinnerTitle = (TextView) titleView.findViewById(R.id.text_item);
		mSpinnerTitle.setText(getString(R.string.email_all_mail).toUpperCase());

		mNavigateAdapter = new EmailAccountNavigateAdapter(this);

		changeNavigateData();
		// 默认设置为all
		updateSpinnerTitle(getString(R.string.email_all_mail).toUpperCase());
		// changeAccount(getString(R.string.email_all_mail).toUpperCase());
		mUnreadNumText.setText("10");
		final DropDownSpinner left = (DropDownSpinner) findViewById(R.id.left_spinner);

		left.setGravity(Gravity.LEFT | Gravity.CENTER);
		left.setTitleView(titleView);
		left.setAdapter(mNavigateAdapter);
		left.setVisibility(View.VISIBLE);
		// 设置单击事件
		left.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				SpinnerContentModel model = (SpinnerContentModel) mNavigateAdapter
						.getItem(position);
				if (model.getType() == SPINNER_MODEL_TYPE_ADD_ACCONT) {

					showToast("ADD Email!");
				} else {
					String address = model.getTitle();
					updateSpinnerTitle(address);
				}
			}
		});

	}

	private List<SpinnerContentModel> getSpinnerList() {
		// 初始化导航栏
		ArrayList<SpinnerContentModel> list = new ArrayList<SpinnerContentModel>();
		// 先添加all email
		SpinnerContentModel model = new SpinnerContentModel();
		model.setIconId(R.drawable.icon_allemail);
		model.setTitle(getString(R.string.email_all_mail));
		list.add(model);
		List<EmailAccountModel> accountList = new ArrayList<EmailListActivity.EmailAccountModel>();

		for (int i = 0; i < 10; i++) {
			EmailAccountModel models1 = new EmailAccountModel();
			models1.setEmailAddress(i + "@126.com");
			if (i % 2 == 0) {
				models1.setEmailDomain("gmail.com");
			} else {
				models1.setEmailDomain("hotmail.com");
			}
			accountList.add(models1);
		}
		if (accountList != null) {
			for (EmailAccountModel accountModel : accountList) {
				model = new SpinnerContentModel();
				model.setTitle(accountModel.getEmailAddress());
				String domainName = accountModel.getEmailDomain();
				if (EmailAccountModel.DOMAIN_GMAIL.equalsIgnoreCase(domainName)) {
					model.setIconId(R.drawable.icon_email_gmail);
				} else if (EmailAccountModel.DOMAIN_HOTMAIL
						.equalsIgnoreCase(domainName)) {
					model.setIconId(R.drawable.icon_email_hotmail);
				} else {
					model.setIconId(R.drawable.icon_email_other);
				}
				list.add(model);
			}
		}
		return list;

	}

	/**
	 * 更新导航栏数据
	 */
	public void changeNavigateData() {
		List<SpinnerContentModel> list = getSpinnerList();
		mNavigateAdapter.setData(list);
		mNavigateAdapter.notifyDataSetChanged();
	}

	/**
	 * 初始化删除ActionBar菜单
	 */
	protected void initDeleteTitle() {
		setChoiceListMenu(RIGHT, new ActionBarMenuItem(MENU_DELETE_EMAIL,
				R.drawable.icon_trash_white));
	}

	/**
	 * 更新actionbar 导航栏的标题
	 * 
	 * @param title
	 *            标题的内容
	 */
	public void updateSpinnerTitle(String title) {
		mSpinnerTitle.setText(title);
	}

	@Override
	protected int getChoiceListBackIcon() {
		return R.drawable.icon_email;
	}

	@Override
	protected int getViewId() {
		return R.layout.email_more;
	}

	@Override
	protected int getListViewId() {
		return 0;
	}

	@Override
	protected ListAdapter getlistViewAdapter() {
		mAdapter = new EmailMoreAdapter();
		return mAdapter;
	}

	private void initListData() {
		ArrayList<EmailBoxModel> modelList = new ArrayList<EmailListActivity.EmailBoxModel>();

		for (int i = 0; i < 10; i++) {
			EmailBoxModel model = new EmailBoxModel();
			model.setName("Box" + i);
			model.setLocalTotalNum(i + 3);
			model.setLocalUnreadNum(i);
			modelList.add(model);
		}
		mAdapter.setData(modelList);
		mAdapter.notifyDataSetChanged();
	}

	@Override
	protected LongClickListView getListView() {

		mPullToRefreshListview = (PullToRefreshListView) findViewById(R.id.email_folder_list);
		mPullToRefreshListview.setPullToRefreshEnabled(false);
		mPullToRefreshListview.setOnRefreshListener(new OnRefreshListener() {
			@Override
			public void onRefresh() {

				mPullToRefreshListview.setRefreshing(true);
			}
		});
		LongClickListView longClick = mPullToRefreshListview
				.getRefreshableView();
		longClick.setLongClickable(true);
		setListHeaderTime();
		return longClick;
	}

	/**
	 * 
	 * 设置下拉刷新时间
	 */
	public void setListHeaderTime() {
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm");
		String date = format.format(new Date());
		mPullToRefreshListview.setListHeaderTime(date);
	}

	/**
	 * 导航栏适配器
	 */
	private class EmailAccountNavigateAdapter extends SpinnerAdapter {

		public EmailAccountNavigateAdapter(Context context) {
			super(context);
		}

		@Override
		protected int getUnreadCount(int position) {
			return 10;
		}

	}

	/**
	 * 
	 * 适配器
	 * 
	 * @author raulxiao
	 */
	public class EmailMoreAdapter extends BaseListAdapter {
		/**
		 * {@inheritDoc}
		 */
		@Override
		protected View getItemView(int position, View convertView,
				ViewGroup parent) {
			// 初始化item里面的控件
			ViewHolder viewHolder;
			if (null == convertView) {
				viewHolder = new ViewHolder();
				convertView = LinearLayout.inflate(EmailListActivity.this,
						R.layout.email_folder_list_item, null);
				viewHolder.folderImage = (ImageView) convertView
						.findViewById(R.id.folder_view);
				viewHolder.folderName = (TextView) convertView
						.findViewById(R.id.folder_name);
				viewHolder.folderSize = (TextView) convertView
						.findViewById(R.id.folder_size_text);
				convertView.setTag(viewHolder);
			} else {
				viewHolder = (ViewHolder) convertView.getTag();
			}
			final EmailBoxModel model = (EmailBoxModel) getDataSrc().get(
					position);
			viewHolder.folderImage
					.setImageResource(R.drawable.email_persona_iconl);
			viewHolder.folderName.setText(model.getName());
			viewHolder.folderSize.setText(model.getLocalUnreadNum() + "/"
					+ model.getLocalTotalNum());
			Log.d(TAG, "UnreadNum = " + model.getLocalUnreadNum());
			Log.d(TAG, "TotalNum = " + model.getLocalTotalNum());
			return convertView;
		}

		@Override
		public int getCount() {
			return super.getCount();
		}

		/**
		 * 
		 * 获取item的背景id 正常状态下的背景Id,父类提供一个默认的，子类可以根据需要重写该方法
		 * 
		 * @param position
		 *            item位置
		 * @return 背景id
		 */
		@Override
		protected int getViewBgId(int position) {
			return R.drawable.bg_listitem_selector;
		}

	}

	/**
	 * 
	 * Folder
	 * 
	 * @author raulxiao
	 */
	private class ViewHolder {
		/**
		 * Folder icon
		 */
		private ImageView folderImage;

		/**
		 * Folder Name
		 */
		private TextView folderName;

		/**
		 * size
		 */
		private TextView folderSize;

	}

	class EmailBoxModel implements Serializable {

		/**
		 * 序列化ID
		 */
		private static final long serialVersionUID = 1L;

		private String name;

		/**
		 * 未读邮件数
		 */
		private int localUnreadNum;

		/**
		 * 全部邮件数据
		 */
		private int localTotalNum;

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}

		public int getLocalUnreadNum() {
			return localUnreadNum;
		}

		public void setLocalUnreadNum(int localUnreadNum) {
			this.localUnreadNum = localUnreadNum;
		}

		public int getLocalTotalNum() {
			return localTotalNum;
		}

		public void setLocalTotalNum(int localTotalNum) {
			this.localTotalNum = localTotalNum;
		}

	}

	class EmailAccountModel implements Serializable {

		/**
		 * gmail域名
		 */
		public static final String DOMAIN_GMAIL = "gmail.com";

		/**
		 * hotmail域名
		 */
		public static final String DOMAIN_HOTMAIL = "hotmail.com";

		/**
		 * other域名
		 */
		public static final String FLAG_OTHER = "other";

		/**
		 * 序列化ID
		 */
		private static final long serialVersionUID = 1L;

		/**
		 * 用户名
		 */
		private String username;

		/**
		 * 域名
		 */
		private String emailDomain;

		/**
		 * 地址全名
		 */
		private String emailAddress;

		public String getUsername() {
			return username;
		}

		public void setUsername(String username) {
			this.username = username;
		}

		public String getEmailDomain() {
			return emailDomain;
		}

		public void setEmailDomain(String emailDomain) {
			this.emailDomain = emailDomain;
		}

		public String getEmailAddress() {
			return emailAddress;
		}

		public void setEmailAddress(String emailAddress) {
			this.emailAddress = emailAddress;
		}
	}

}
