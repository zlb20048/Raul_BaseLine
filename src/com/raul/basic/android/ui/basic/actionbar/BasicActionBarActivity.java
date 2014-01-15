package com.raul.basic.android.ui.basic.actionbar;

import java.util.ArrayList;

import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.TextView;

import com.raul.basic.android.R;
import com.raul.basic.android.ui.basic.BasicActivity;
import com.raul.basic.android.ui.basic.listview.BaseListAdapter;
import com.raul.basic.android.ui.basic.richeditext.LimitedEditText;
import com.raul.basic.android.ui.basic.spinner.DropDownSpinner;
import com.raul.basic.android.ui.basic.spinner.ListPopupWindow;
import com.raul.basic.android.util.Log;

/**
 * 控制actionbar显示的基类 页面需要实现带actionbar的，需要继承该类，并实现指定的方法,子类无需调用setContentView()
 * 该类会提供一些接口给子类调用，以达到实时改变actionbar的显示
 * 
 * @author xiaomiaomiao
 * 
 */
public abstract class BasicActionBarActivity extends BasicActivity implements
		OnClickListener, MutiBack {

	protected static final int LEFT = 0;

	protected static final int RIGHT = 1;

	/**
	 * 非多选状态下的topbar
	 */
	protected View mActionBar;

	/**
	 * 输入框
	 */
	protected LimitedEditText mEditText;

	/**
	 * 返回按钮组件
	 */
	private View mBackContainer;

	/**
	 * 子类内容容器
	 */
	private LinearLayout mContentContainer;

	/**
	 * actionbar 显示的页面图标
	 */
	private ImageView mLogo;

	/**
	 * 返回按钮图片
	 */
	private ImageView mBack;

	/**
	 * actionbar上的title
	 */
	private TextView mTitle;

	/**
	 * 菜单部分的容器
	 */
	private View mMenuContainer;

	/**
	 * 搜索框容器
	 */
	private View mSearchBar;

	/**
	 * 搜索框容器
	 */
	private View mSearchButton;

	/**
	 * 搜索框前面的logo
	 */
	private ImageView mSearchBarLogo;

	/**
	 * 清除搜索框中的文字
	 */
	private ImageView mCancelButton;

	/**
	 * 第一个菜单项
	 */
	private ImageView mMenu1;

	/**
	 * 第二个菜单项
	 */
	private ImageView mMenu2;

	/**
	 * 更多菜单项
	 */
	private DropDownSpinner mMenuMore;

	private DropDownSpinner[] spinners;

	/**
	 * 放置菜单的集合
	 */
	private ArrayList<ActionBarMenuItem> mMenuItems;

	/**
	 * 是否弹出输入框
	 */
	private boolean showSearch;

	/**
	 * 
	 * 得到子类需要显示页面的view id
	 * 
	 * @return int 子类需要显示的view id
	 */
	protected abstract int getViewId();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		super.setContentView(getPageViewId());

		initView();
		showMenus();

		// 把子类的view加入到容器中
		mContentContainer = (LinearLayout) findViewById(R.id.content_container);
		LayoutInflater inflater = LayoutInflater.from(this);
		View content = inflater.inflate(getViewId(), null);
		// 设定该view（content）的宽高占满屏幕，使用如下方法add view。
		mContentContainer.addView(content, LayoutParams.FILL_PARENT,
				LayoutParams.FILL_PARENT);
	}

	/**
	 * 
	 * 设置内容容器与actionbar的margin
	 * 
	 * @param left
	 *            左
	 * @param top
	 *            上
	 * @param right
	 *            右
	 * @param bottom
	 *            下
	 */
	protected void setContainerMargin(int left, int top, int right, int bottom) {
		LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
				mContentContainer.getLayoutParams());
		params.setMargins(left, top, right, bottom);
		mContentContainer.setLayoutParams(params);
	}

	/**
	 * 得到主页面的View Id
	 * 
	 * @return int 主页面的View Id
	 */
	protected int getPageViewId() {
		return R.layout.basic_action_bar;
	}

	// 初始化页面，主要是加载actionbar组件
	private void initView() {
		mActionBar = findViewById(R.id.actionbar);
		// 返回按钮和页面图标
		mBackContainer = findViewById(R.id.actionbar_back_container);
		mLogo = (ImageView) findViewById(R.id.actionbar_logo);
		mBack = (ImageView) findViewById(R.id.actionbar_back);
		mBackContainer.setOnClickListener(this);

		mTitle = (TextView) findViewById(R.id.actionbar_title);

		spinners = new DropDownSpinner[2];

		spinners[LEFT] = (DropDownSpinner) findViewById(R.id.left_spinner);
		spinners[LEFT].setDropDownWidth(ListPopupWindow.ROOT_VIEW_WIDTH);
		spinners[RIGHT] = (DropDownSpinner) findViewById(R.id.right_spinner);
		spinners[RIGHT].setDropDownWidth(ListPopupWindow.ROOT_VIEW_WIDTH);

		// 菜单部分
		mMenuContainer = findViewById(R.id.actionbar_menu_container);
		mMenu1 = (ImageView) findViewById(R.id.actionbar_menu_1);
		mMenu2 = (ImageView) findViewById(R.id.actionbar_menu_2);
		mMenuMore = (DropDownSpinner) findViewById(R.id.actionbar_menu_more);
		mMenuMore.setDropDownWidth(ListPopupWindow.ROOT_VIEW_WIDTH);
		mMenu1.setOnClickListener(this);
		mMenu2.setOnClickListener(this);
		mMenuMore.setOnClickListener(this);

		// 输入框部分
		findViewById(R.id.searchbar_back_container).setOnClickListener(this);
		mSearchButton = findViewById(R.id.search_right_icon);
		mSearchButton.setOnClickListener(this);

		mSearchBar = findViewById(R.id.search_bar);
		mSearchBarLogo = (ImageView) findViewById(R.id.searchbar_logo);

		mCancelButton = (ImageView) findViewById(R.id.cancel);
		mCancelButton.setOnClickListener(this);

		mEditText = (LimitedEditText) findViewById(R.id.search_friend);
		mEditText.setMaxCharLength(20);
		mEditText.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {

			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {

			}

			@Override
			public void afterTextChanged(Editable s) {
				String text = s.toString();

				// 搜索的时候 % _ 加上转义

				String newTextString = text.replace("%", "/%").replace("_",
						"/_");

				mCancelButton.setVisibility(text.length() > 0 ? View.VISIBLE
						: View.GONE);
				if (showSearch) {
					textChanged(newTextString);
				}
			}
		});
	}

	/**
	 * 
	 * 在action bar上显示需要的菜单项 [功能详细描述]
	 */
	protected final void showMenus() {
		if (null == mMenuItems) {
			mMenuItems = new ArrayList<ActionBarMenuItem>();
		}
		mMenuItems.clear();
		// 子类实现该方法创建Menu
		final int onBar = onCreateMenu(mMenuItems);
		int size = mMenuItems.size();
		// 如果size 是0，隐藏菜单部分
		if (size > 0) {
			mMenuContainer.setVisibility(View.VISIBLE);
			if (onBar > 0) {
				setMenu(mMenu1, mMenuItems.get(0));
				findViewById(R.id.menu_1_separator).setVisibility(View.VISIBLE);
			} else {
				mMenu1.setVisibility(View.GONE);
				findViewById(R.id.menu_1_separator).setVisibility(View.GONE);
			}
			if (size > 1 && onBar > 1) {
				setMenu(mMenu2, mMenuItems.get(1));
				findViewById(R.id.menu_2_separator).setVisibility(View.VISIBLE);
			} else {
				mMenu2.setVisibility(View.GONE);
				findViewById(R.id.menu_2_separator).setVisibility(View.GONE);
			}
			if (size > onBar) {
				mMenuMore.setVisibility(View.VISIBLE);
				findViewById(R.id.menu_more_separator).setVisibility(
						View.VISIBLE);

				ArrayList<ActionBarMenuItem> moreItems = new ArrayList<ActionBarMenuItem>();

				for (int i = onBar; i < size; i++) {
					moreItems.add(mMenuItems.get(i));
				}
				DropSpinnerAdapter moreAdapter = new DropSpinnerAdapter();
				moreAdapter.setData(moreItems);
				mMenuMore.setAdapter(moreAdapter);
				mMenuMore.setOnItemClickListener(moreAdapter);
			} else {
				mMenuMore.setVisibility(View.GONE);
				findViewById(R.id.menu_more_separator).setVisibility(View.GONE);
			}
		} else {
			mMenuContainer.setVisibility(View.GONE);
		}
	}

	/**
	 * 
	 * 设置菜单项
	 * 
	 * @param menu
	 *            菜单View
	 * @param item
	 *            菜单项
	 */
	protected final void setMenu(ImageView menu, ActionBarMenuItem item) {
		menu.setVisibility(View.VISIBLE);
		menu.setTag(item.getId());
		menu.setImageResource(item.getDrawableId());
		menu.setEnabled(item.isEnabled());
	}

	/**
	 * 
	 * 设置返回图标的显示属性
	 * 
	 * @param visibility
	 *            返回图标的显示属性，gone or visible or invisible
	 */
	protected final void setBackIconVisibility(int visibility) {
		mBack.setVisibility(visibility);
		if (View.VISIBLE == visibility) {
			mBackContainer.setBackgroundResource(R.drawable.bg_titlebar);
			mBackContainer.setEnabled(true);
		} else {
			mBackContainer.setBackgroundDrawable(null);
			mBackContainer.setEnabled(false);
		}
	}

	/**
	 * Set the enabled state of BackIcon. The interpretation of the enabled
	 * state varies by subclass.
	 * 
	 * @param enabled
	 *            True if this view is enabled, false otherwise.
	 */
	protected final void setBackIconEnable(boolean enabled) {
		mBackContainer.setEnabled(enabled);
	}

	/**
	 * 
	 * 设置页面图标的背景 [功能详细描述]
	 * 
	 * @param drawableId
	 *            背景id
	 */
	protected final void setLogo(int drawableId) {
		mLogo.setImageResource(drawableId);
		setLogoVisibility(View.VISIBLE);
	}

	/**
	 * 
	 * 设置页面图标的显示属性
	 * 
	 * @param visibility
	 *            页面图标的显示属性，gone or visible or invisible
	 */
	protected final void setLogoVisibility(int visibility) {
		mLogo.setVisibility(visibility);
	}

	/**
	 * 
	 * 设置actionbar上title显示的内容
	 * 
	 * @param strTitle
	 *            title显示字符串
	 */
	protected final void setActionBarTitle(CharSequence strTitle) {
		mTitle.setText(strTitle);
		setTitleVisibility(View.VISIBLE);
	}

	/**
	 * 
	 * 设置actionbar上title显示的内容 [功能详细描述]
	 * 
	 * @param titleId
	 *            title显示的资源ID
	 */
	protected final void setActionBarTitle(int titleId) {
		mTitle.setText(titleId);
		setTitleVisibility(View.VISIBLE);
	}

	/**
	 * 
	 * 设置菜单的可用性
	 * 
	 * @param witch
	 *            菜单项位置
	 * @param enabled
	 *            是否可用
	 */
	protected final void setMenuEnabled(int witch, boolean enabled) {
		if (LEFT == witch) {
			mMenu1.setEnabled(enabled);
		} else {
			mMenu2.setEnabled(enabled);
		}
	}

	/**
	 * 
	 * 设置更多菜单的显示属性
	 * 
	 * @param visibility
	 *            返回图标的显示属性，gone or visible or invisible
	 */
	protected final void setMenuMoreVisibility(int visibility) {
		if (View.VISIBLE == visibility && mMenuItems.isEmpty()) {
			mMenuMore.setEnabled(false);
			mMenuContainer.setVisibility(visibility);
		}
		mMenuMore.setVisibility(visibility);
	}

	/**
	 * 
	 * 设置title的显示属性 [功能详细描述]
	 * 
	 * @param visibility
	 *            title的显示属性，gone or visible or invisible
	 */
	protected final void setTitleVisibility(int visibility) {
		mTitle.setVisibility(visibility);
	}

	/**
	 * 设置下拉框显示属性
	 * 
	 * @param witch
	 *            int 下拉框位置
	 * @param visibility
	 *            int 显示属性
	 */
	protected final void setSpinnerVisibility(int witch, int visibility) {
		spinners[witch].setVisibility(visibility);
		if (RIGHT == witch) {
			findViewById(R.id.right_spinner_separator)
					.setVisibility(visibility);
		}
	}

	/**
	 * 设置下拉框适配器
	 * 
	 * @param witch
	 *            int 下拉框位置
	 * @param adapter
	 *            ListAdapter 适配器
	 */
	protected final void setSpinnerAdapter(int witch, ListAdapter adapter) {
		spinners[witch].setAdapter(adapter);
	}

	/**
	 * 设置下拉提示（列表头）
	 * 
	 * @param witch
	 *            int 下拉框位置
	 * @param prompt
	 *            View 提示
	 */
	protected final void setSpinnerPromptView(int witch, View prompt) {
		spinners[witch].setPromptView(prompt);
	}

	/**
	 * 设置下拉标题
	 * 
	 * @param witch
	 *            int 下拉框位置
	 * @param titleView
	 *            View 标题
	 */
	protected final void setSpinnerTitleView(int witch, View titleView) {
		spinners[witch].setTitleView(titleView);
		setSpinnerVisibility(witch, View.VISIBLE);
	}

	/**
	 * 设置下拉标点击事件监听
	 * 
	 * @param witch
	 *            int 下拉框位置
	 * @param onItemClickListener
	 *            OnItemClickListener 点击事件监听器
	 */
	protected final void setSpinnerOnItemClickListener(int witch,
			OnItemClickListener onItemClickListener) {
		spinners[witch].setOnItemClickListener(onItemClickListener);
	}

	/**
	 * 设置下拉标最大显示条目数
	 * 
	 * @param witch
	 *            int 下拉框位置
	 * @param count
	 *            int 最大条目数
	 */
	protected final void setSpinnerDisplayItem(int witch, int count) {
		spinners[witch].setDisplayItem(count);
	}

	/**
	 * 设置聊天页面的聊天对象title名称
	 * 
	 * @param name
	 *            聊天页面的聊天对象title名称
	 */
	protected final void setChatTitleName(String name) {
		((TextView) findViewById(R.id.chat_title_name)).setText(name
				.toUpperCase());
		findViewById(R.id.chat_title).setVisibility(View.VISIBLE);
	}

	/**
	 * 设置聊天页面的聊天对象title状态
	 * 
	 * @param statusResId
	 *            聊天页面的聊天对象title状态图片引用
	 */
	protected final void setChatTitleStatus(int statusResId) {
		ImageView status = (ImageView) findViewById(R.id.chat_title_status);
		status.setImageResource(statusResId);
		status.setVisibility(View.VISIBLE);
	}

	/**
	 * 
	 * 获取标题栏的高度
	 * 
	 * @return 标题栏高度
	 */
	protected final int getActionBarHeight() {
		if (mActionBar != null) {
			return mActionBar.getHeight();
		}
		return 0;
	}

	/**
	 * 
	 * 设置搜索框前面的logo
	 * 
	 * @param resid
	 *            资源id
	 */
	protected void setSearchBarLogo(int resid) {
		mSearchBarLogo.setBackgroundResource(resid);
	}

	/**
	 * 显示右边的搜索按钮
	 */
	protected final void showRightSearchIcon() {
		mSearchButton.setVisibility(View.VISIBLE);
		findViewById(R.id.search_right_icon_separator).setVisibility(
				View.VISIBLE);
		findViewById(R.id.search_left_icon).setVisibility(View.GONE);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		// 处理返回按钮
		case R.id.actionbar_back_container:
		case R.id.searchbar_back_container:
			Log.d("ActionBar>>>", "searchbar_back_container pressed ");
			onBackContainer();
			break;
		// 处理菜单1,2的响应，调用子类的方法
		case R.id.actionbar_menu_1:
		case R.id.actionbar_menu_2:
			onMenuClick((Integer) v.getTag());
			break;
		case R.id.actionbar_menu_more:
			showMoreMenu();
			break;
		case R.id.cancel:
			mEditText.setText(null);
			// 如果有搜索按钮，清除需要重新搜索一次
			if (mSearchButton.getVisibility() == View.VISIBLE) {
				onSearch(null);
			}
			break;
		case R.id.search_right_icon:
			hideInputWindow(this);
			String searchValue = mEditText.getText().toString();
			onSearch(searchValue.length() > 0 ? searchValue : null);
			break;
		default:
			break;
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void onBackPressed() {
		onBackContainer();
	}

	/**
	 * /**
	 * 
	 * 创建menu菜单 添加需要显示的menu菜单,菜单显示顺序按添加顺序，多于2个的，第3个开始放到更多菜单项中
	 * 
	 * @param menus
	 *            menu组合
	 * @return int 显示的菜单条目
	 */
	protected int onCreateMenu(ArrayList<ActionBarMenuItem> menus) {
		return 2;
	}

	/**
	 * 
	 * 点击菜单项的处理 [功能详细描述]
	 * 
	 * @param menuId
	 *            菜单id
	 */
	protected void onMenuClick(int menuId) {

	}

	/**
	 * 
	 * 响应点击返回按钮事件 点击返回按钮的统一处理，子类可以根据需要进行重写
	 */
	protected void onBackContainer() {

		if (!onMutiBack()) {
			finish();
		}
	}

	/**
	 * 当搜索按钮按下时
	 * 
	 * @param searchValue
	 *            搜索内容
	 */
	protected void onSearch(String searchValue) {
		onBackContainer();
	}

	/**
	 * 弹出更多菜单
	 */
	protected void showMoreMenu() {
		mMenuMore.show();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean onMutiBack() {
		// 点返回键隐藏软键盘
		InputMethodManager imm = (InputMethodManager) mBackContainer
				.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
		if (imm.isActive()) {
			imm.hideSoftInputFromWindow(
					mBackContainer.getApplicationWindowToken(), 0);
		}
		if (showSearch) {
			closeSearch();
			return true;
		}
		return false;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void clean() {

	}

	/**
	 * 打开搜索框
	 */
	protected void openSearch() {
		openSearch(true);
	}

	/**
	 * 
	 * 打开搜索框，并记录打开状态
	 * 
	 * @param searchStatus
	 *            搜索框状态
	 */
	protected void openSearch(boolean searchStatus) {
		mEditText.requestFocus();
		mActionBar.setVisibility(View.GONE);
		mSearchBar.setVisibility(View.VISIBLE);
		showSearch = searchStatus;
		showInputWindow(mEditText);
	}

	/**
	 * 关闭搜索框
	 */
	protected void closeSearch() {
		showSearch = false;
		mActionBar.setVisibility(View.VISIBLE);
		mSearchBar.setVisibility(View.GONE);
		mEditText.setText(null);
		hideInputWindow(mEditText);
	}

	/**
	 * 搜索文字发生变动时的回调方法
	 * 
	 * @param text
	 *            输入框内的文字
	 */
	protected void textChanged(String text) {

	}

	/**
	 * 下拉菜单的adapter
	 * 
	 */
	public class DropSpinnerAdapter extends BaseListAdapter implements
			OnItemClickListener {
		/**
		 * {@inheritDoc}
		 */
		@Override
		protected View getItemView(int position, View convertView,
				ViewGroup parent) {
			DropSpinnerViewHolder viewHolder = null;
			if (convertView == null) {
				convertView = LayoutInflater.from(BasicActionBarActivity.this)
						.inflate(R.layout.spinner_menu_row, null);
				viewHolder = new DropSpinnerViewHolder();
				viewHolder.mTitle = (TextView) convertView
						.findViewById(R.id.spinner_menu_content);
				convertView.setTag(viewHolder);
			} else {
				viewHolder = (DropSpinnerViewHolder) convertView.getTag();
			}
			ActionBarMenuItem item = (ActionBarMenuItem) getItem(position);
			convertView.setEnabled(item.isEnabled());
			viewHolder.mTitle.setText(item.getMenuTitle());
			return convertView;
		}

		/**
		 * {@inheritDoc}
		 * 
		 * @see android.widget.BaseAdapter#isEnabled(int)
		 */
		@Override
		public boolean isEnabled(int position) {
			return ((ActionBarMenuItem) getItem(position)).isEnabled();
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
				long arg3) {
			onMenuClick(((ActionBarMenuItem) getItem(arg2)).getId());
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		protected boolean setViewBackground(int position, View itemView,
				boolean onLongClickStatus, boolean selected) {
			return true;
		}

	}

	/**
	 * 封转图片内容的bean
	 * 
	 */
	private static class DropSpinnerViewHolder {
		private TextView mTitle;
	}

	/**
	 * 是否展示了 搜索框
	 * 
	 * @return 返回当前的搜索框状态
	 */
	public boolean isShowSearchBar() {
		return showSearch;
	}

}
