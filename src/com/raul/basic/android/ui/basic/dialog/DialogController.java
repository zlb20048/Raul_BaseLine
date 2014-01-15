package com.raul.basic.android.ui.basic.dialog;

import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;

import com.raul.basic.android.R;

/**
 * 
 * @author xiaomiaomiao
 * 
 */
public class DialogController {
	/**
	 * DialogInterface
	 */
	private final DialogInterface mDialogInterface;

	/**
	 * 窗体
	 */
	private final Window mWindow;

	/**
	 * 标题
	 */
	private CharSequence mTitle;

	/**
	 * 标题视图
	 */
	private TextView mTitleView;

	/**
	 * 消息体
	 */
	private CharSequence mMgeText;

	/**
	 * 消息体视图
	 */
	private TextView mMessageView;

	/**
	 * 左按钮文字
	 */
	private CharSequence mPositiveButtonText;

	/**
	 * 左按钮
	 */
	private Button mPositiveButton;

	/**
	 * 右按钮文字
	 */
	private CharSequence mNegativeButtonText;

	/**
	 * 右按钮
	 */
	private Button mNegativeButton;

	/**
	 * 自定义视图
	 */
	private View mCustomContentView;

	/**
	 * 列表式dialog的listview
	 */
	private ListView mListView;

	/**
	 * 列表式dialog的listview的adapter
	 */
	private ListAdapter mAdapter;

	/**
	 * listview被选中的item
	 */
	private int mCheckedItem = -1;

	/**
	 * 带有checkbox的dialog的checkbox布局数组
	 */
	private View[] mCheckBoxViews;

	/**
	 * 左按钮监听
	 */
	private DialogInterface.OnClickListener mPositiveButtonListener;

	/**
	 * 右按钮监听
	 */
	private DialogInterface.OnClickListener mNegativeButtonListener;

	/**
	 * [构造简要说明]
	 * 
	 * @param context
	 *            context
	 * @param di
	 *            DialogInterface
	 * @param window
	 *            window
	 */
	public DialogController(Context context, DialogInterface di, Window window) {
		mDialogInterface = di;
		mWindow = window;
	}

	/**
	 * 绘制界面
	 */
	public void installContent() {
		mWindow.setContentView(R.layout.basic_dialog);
		setUpView();
	}

	private void setUpView() {
		// title面板
		LinearLayout topPanel = (LinearLayout) mWindow
				.findViewById(R.id.topPanel);
		setUpTitle(topPanel);

		// button面板
		LinearLayout bottomPanel = (LinearLayout) mWindow
				.findViewById(R.id.bottomPanel);
		setUpButton(bottomPanel);

		// 内容面板
		LinearLayout contentPanel = (LinearLayout) mWindow
				.findViewById(R.id.contentPanel);
		setUpContent(contentPanel);
	}

	private void setUpTitle(LinearLayout topPanel) {
		// 有标题
		if (null != mTitle && !"".equals(mTitle)) {
			topPanel.setVisibility(View.VISIBLE);
			// 设置标题
			mTitleView = (TextView) topPanel.findViewById(R.id.title);
			mTitleView.setText(mTitle);
		}
	}

	private void setUpButton(LinearLayout bottomPanel) {
		// 设置确定按钮
		if (null != mPositiveButtonText) {
			// 按钮父类布局可见
			bottomPanel.setVisibility(View.VISIBLE);
			mPositiveButton = (Button) bottomPanel
					.findViewById(R.id.positiveButton);
			// 确定按钮可见
			mPositiveButton.setVisibility(View.VISIBLE);
			mPositiveButton.setText(mPositiveButtonText);
			mPositiveButton.setOnClickListener(new View.OnClickListener() {
				public void onClick(View v) {
					if (mPositiveButtonListener != null) {
						mPositiveButtonListener.onClick(mDialogInterface,
								DialogInterface.BUTTON_POSITIVE);
					}
					mDialogInterface.dismiss();
				}
			});
		}

		// 设置取消按鈕
		if (null != mNegativeButtonText) {
			// 按钮父类布局可见
			bottomPanel.setVisibility(View.VISIBLE);
			mNegativeButton = (Button) bottomPanel
					.findViewById(R.id.negativeButton);
			// 右按钮可见性
			mNegativeButton.setVisibility(View.VISIBLE);
			mNegativeButton.setText(mNegativeButtonText);
			mNegativeButton.setOnClickListener(new View.OnClickListener() {
				public void onClick(View v) {
					if (mNegativeButtonListener != null) {
						mNegativeButtonListener.onClick(mDialogInterface,
								DialogInterface.BUTTON_NEGATIVE);
					}
					mDialogInterface.dismiss();
				}
			});

		}

		// 如果左右按钮都有需要有分割线
		if (null != mNegativeButtonText && null != mPositiveButtonText) {
			bottomPanel.findViewById(R.id.dialog_button_divider).setVisibility(
					View.VISIBLE);
		}
	}

	private void setUpContent(LinearLayout contentPanel) {
		// scrollview 当内容长度过长时，用来进行滚动
		ScrollView scrollView = (ScrollView) contentPanel
				.findViewById(R.id.scrollView);

		// 如果消息体没有 设置为不可见
		if (null != mMgeText) {
			mMessageView = (TextView) scrollView
					.findViewById(R.id.message_text);
			mMessageView.setVisibility(View.VISIBLE);
			mMessageView.setText(mMgeText);
		}

		// 如果有自定义布局，为可见，将自定义布局放在scrollView中，当内容长度过长时，可以进行滚动
		// 故自定义布局是listview,listview与scrollView结合会有问题，不放在scrollview中
		if (null != mCustomContentView) {
			if (mCustomContentView instanceof ListView) {
				mListView = (ListView) mCustomContentView;
			} else {
				// 设置自定义的布局，去除其中的用来显示消息的视图
				contentPanel.removeAllViews();
				contentPanel.addView(mCustomContentView, new LayoutParams(
						LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT));
			}
		}

		// ListView不为空，是list类型的dialog，需要将scrollView移除直接放在contentPanel中
		if (null != mListView) {
			if (null != mAdapter) {
				mListView.setAdapter(mAdapter);
			}
			if (mCheckedItem > -1) {
				mListView.setItemChecked(mCheckedItem, true);
			}
			// 将listview放在contentPanel中，去除其中的scrollview布局
			contentPanel.removeAllViews();
			contentPanel.addView(mListView, new LayoutParams(
					LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT));
		}
		// 带有checkbox的
		if (null != mCheckBoxViews) {
			for (View view : mCheckBoxViews) {
				// 直接加在contentPanel中，不去除其中用于显示消息的视图，
				// 因为checkbox类的dialog一般需要显示提示消息
				contentPanel.addView(view, new LayoutParams(
						LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT));
			}
		}
	}

	/**
	 * 设置消息体
	 * 
	 * @param message
	 *            message
	 */
	public void setMessage(CharSequence message) {
		mMgeText = message;
	}

	/**
	 * 设置标题
	 * 
	 * @param title
	 *            title
	 */
	public void setTitle(CharSequence title) {
		mTitle = title;
		if (mTitleView != null) {
			mTitleView.setText(title);
		}
	}

	/**
	 * 设置自定义布局
	 * 
	 * @param v
	 *            View
	 */
	public void setContentView(View v) {
		mCustomContentView = v;
	}

	/**
	 * 设置确定按钮
	 * 
	 * @param positiveButtonText
	 *            positiveButtonText
	 * @param listener
	 *            listener
	 */
	public void setPositiveButton(CharSequence positiveButtonText,
			DialogInterface.OnClickListener listener) {
		mPositiveButtonText = positiveButtonText;
		mPositiveButtonListener = listener;
	}

	/**
	 * 设置取消按钮
	 * 
	 * @param negativeButtonText
	 *            negativeButtonText
	 * @param listener
	 *            listener
	 */
	public void setNegativeButton(CharSequence negativeButtonText,
			DialogInterface.OnClickListener listener) {
		mNegativeButtonText = negativeButtonText;
		mNegativeButtonListener = listener;
	}

	/**
	 * 设置button [功能详细描述]
	 * 
	 * @param whichButton
	 *            DialogInterface.BUTTON_POSITIVE 左按钮
	 *            DialogInterface.BUTTON_NEGATIVE 右按钮
	 * @param text
	 *            button上显示的文字
	 * @param listener
	 *            button监听
	 */
	public void setButton(int whichButton, CharSequence text,
			DialogInterface.OnClickListener listener) {
		switch (whichButton) {
		case DialogInterface.BUTTON_POSITIVE:
			setPositiveButton(text, listener);
			break;

		case DialogInterface.BUTTON_NEGATIVE:
			setNegativeButton(text, listener);
			break;
		default:
			throw new IllegalArgumentException("Button does not exist");
		}
	}

	/**
	 * dialog中的listview
	 * 
	 * @return dialog中的listview
	 */
	public ListView getListView() {
		return mListView;
	}

	/**
	 * 获取dialog上的button
	 * 
	 * @param whichButton
	 *            DialogInterface.BUTTON_POSITIVE 左按钮
	 *            DialogInterface.BUTTON_NEGATIVE 右按钮
	 * @return button
	 */
	public Button getButton(int whichButton) {
		switch (whichButton) {
		// 左按钮
		case DialogInterface.BUTTON_POSITIVE:
			return mPositiveButton;
			// 右按钮
		case DialogInterface.BUTTON_NEGATIVE:
			return mNegativeButton;
		default:
			return null;
		}
	}

	/**
	 * 获取自定义视图
	 * 
	 * @return 自定义视图
	 */
	public View getContentView() {
		return mCustomContentView;
	}

	/**
	 * dialog参数类
	 * 
	 * @author fengdai
	 * @version [RCS Client V100R001C03, 2012-7-27]
	 */
	public static class DialogParams {
		/**
		 * Context
		 */
		public final Context mContext;

		/**
		 * LayoutInflater
		 */
		public final LayoutInflater mInflater;

		/**
		 * 标题
		 */
		public CharSequence mTitle;

		/**
		 * 消息体
		 */
		public CharSequence mMgeText;

		/**
		 * 确定按钮文字
		 */
		public String mPositiveButtonText;

		/**
		 * 取消按钮文字
		 */
		public String mNegativeButtonText;

		/**
		 * 自定义布局view
		 */
		public View mContentView;

		/**
		 * 自定义布局resId
		 */
		public int mContentViewResId = -1;

		/**
		 * 列表条目数组
		 */
		public CharSequence[] mItems;

		/**
		 * 单选列表数组默认选择的条目
		 */
		public int mCheckedItem = -1;

		/**
		 * 多选列表数组默认选择的条目
		 */
		public boolean[] mCheckedItems;

		/**
		 * checkbox消息数组
		 */
		public String[] mCheckMsg;

		/**
		 * checkBox初始状态
		 */
		public boolean[] mDefaultCheckState;

		/**
		 * list的adapter
		 */
		public ListAdapter mAdapter;

		/**
		 * 是否多选模式
		 */
		public boolean mIsMultiChoice = false;

		/**
		 * 是否单选模式
		 */
		public boolean mIsSingleChoice = false;

		/**
		 * 列表按钮
		 */
		public DialogInterface.OnClickListener mOnClickListener;

		/**
		 * 确定按钮监听
		 */
		public DialogInterface.OnClickListener mPositiveButtonListener;

		/**
		 * 取消按钮监听
		 */
		public DialogInterface.OnClickListener mNegativeButtonListener;

		/**
         * 
         */
		public DialogInterface.OnMultiChoiceClickListener mOnListCheckedListener;

		/**
		 * checkbox监听
		 */
		public BasicDialog.OnCheckStateChangeListener mCheckBoxStateChangeListner;

		/**
		 * [构造简要说明]
		 * 
		 * @param context
		 *            context
		 */
		public DialogParams(Context context) {
			mContext = context;
			mInflater = (LayoutInflater) context
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		}

		/**
		 * 设置DialogController
		 * 
		 * @param dialog
		 *            DialogController
		 */
		public void apply(DialogController dialog) {
			if (null != mTitle && !"".equals(mTitle)) {
				dialog.setTitle(mTitle);
			}
			// 设置确定按钮
			if (null != mPositiveButtonText) {
				dialog.setPositiveButton(mPositiveButtonText,
						mPositiveButtonListener);
			}

			// 设置取消按鈕
			if (null != mNegativeButtonText) {
				dialog.setNegativeButton(mNegativeButtonText,
						mNegativeButtonListener);
			}

			// 如果消息体没有 设置为不可见
			if (null != mMgeText) {
				dialog.setMessage(mMgeText);
			}

			// 设置的是布局资源文件，先inflate得到view
			if (mContentViewResId >= 0x01000000) {
				mContentView = mInflater.inflate(mContentViewResId, null);
			}
			// 设置了自定义视图或自定义视图的resId
			if (null != mContentView) {
				dialog.setContentView(mContentView);
			}

			// 如果自定义布局为ListView没有 设置为不可见
			if (null != mItems || null != mAdapter) {
				// createListView(dialog);
			}
			// 带有checkbox的
			if (null != mCheckMsg) {
				createCheckboxView(dialog);
			}
		}

		// 根据属性创建相应的listview

		private void createCheckboxView(final DialogController dialog) {
			View[] checkboxs = new View[mCheckMsg.length];
			for (int i = 0; i < mCheckMsg.length; i++) {
				View checkBoxItem = mInflater.inflate(
						R.layout.dialog_check_box, null);
				CheckBox cb = (CheckBox) checkBoxItem
						.findViewById(R.id.dialog_checkbox);
				cb.setText(mCheckMsg[i]);
				// 设置checkbox初始状态
				if (null != mDefaultCheckState) {
					cb.setChecked(mDefaultCheckState[i]);
				}
				final int which = i;
				cb.setOnCheckedChangeListener(new OnCheckedChangeListener() {

					@Override
					public void onCheckedChanged(CompoundButton buttonView,
							boolean isChecked) {
						if (null != mCheckBoxStateChangeListner) {
							mCheckBoxStateChangeListner.onCheckedChanged(which,
									isChecked);
						}
					}
				});
				checkboxs[i] = checkBoxItem;
			}
			dialog.mCheckBoxViews = checkboxs;
		}
	}
}
