package com.raul.basic.android.ui.basic.dialog;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;

import com.raul.basic.android.R;


/**
 * 自定义Dialog
 * 
 */
public class BasicDialog extends Dialog

{
    private DialogController mDialogController;
    
    private boolean mCancelable;
    
    /**
     * 
     * Basicdialog构造函数，未指定theme，为默认
     * 
     * @param context
     *            context
     */
    public BasicDialog(Context context)
    {
        this(context, 0);
    }
    
    /**
     * 
     * 构造函数，实例化一个DialogController对象
     * 
     * @param context 上下文context
     * @param theme 指定的theme
     */
    public BasicDialog(Context context, int theme)
    {
        this(context, theme, true);
    }
    
    /**
     * 带参数构造，可设置是否可取消当前的过程
     * @param context 上下文context
     * @param theme 指定的theme
     * @param cancelable 是否可取消标志
     */
    public BasicDialog(Context context, int theme, boolean cancelable)
    {
        super(context, theme);
        
        mDialogController = new DialogController(getContext(), this,
                getWindow());
        
        mCancelable = cancelable;
    }
    
    /**
     * 获取dialog上的button
     * @param whichButton 
     *               DialogInterface.BUTTON_POSITIVE 左按钮
     *               DialogInterface.BUTTON_NEGATIVE 右按钮
     * @return button
     */
    public Button getButton(int whichButton)
    {
        return mDialogController.getButton(whichButton);
    }
    
    /**
     * dialog中的listview
     * @return 列表式dialog中的listview
     */
    public ListView getListView()
    {
        return mDialogController.getListView();
    }
    
    /**
     * 获取自定义视图
     * @return 自定义视图 
     */
    public View getCustomContentView()
    {
        return mDialogController.getContentView();
    }
    
    /**
     * 设置标题
     * @param title 标题
     * @see android.app.Dialog#setTitle(java.lang.CharSequence)
     */
    public void setTitle(CharSequence title)
    {
        mDialogController.setTitle(title);
    }
    
    /**
     * 设置定制布局
     * @param view 自定义布局
     */
    public void setCustomContentView(View view)
    {
        mDialogController.setContentView(view);
    }
    
    /**
     * 设置button
     * @param whichButton 
     *               DialogInterface.BUTTON_POSITIVE 左按钮
     *               DialogInterface.BUTTON_NEGATIVE 右按钮
     * @param text  button上显示的文字
     * @param listener  button监听
     */
    public void setButton(int whichButton, CharSequence text,
            OnClickListener listener)
    {
        mDialogController.setButton(whichButton, text, listener);
    }
    
    /**
     * onCreate
     * @param savedInstanceState savedInstanceState
     * @see android.app.Dialog#onCreate(android.os.Bundle)
     */
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        mDialogController.installContent();
    }
    
    /**
     * 
     * 屏蔽menu，根据是否可取消标志，处理返回键和搜索键
     * 
     * @param keyCode 按键编码
     * @param event 按键事件
     * @return true
     * @see android.app.Dialog#onKeyDown(int, android.view.KeyEvent)
     */
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event)
    {
        if (keyCode == KeyEvent.KEYCODE_MENU)
        {
            // 屏蔽Menu键
            return true;
        }
        else if ((keyCode == KeyEvent.KEYCODE_BACK || keyCode == KeyEvent.KEYCODE_SEARCH)
                && !mCancelable)
        {
            // 如果是返回键或搜索键，并且设置了不可取消，则返回不再处理
            return true;
        }
        
        return super.onKeyDown(keyCode, event);
    }
    
    /**
     * checkbox状态改变监听
     * [功能详细描述]
     * @author fengdai
     * @version [RCS Client V100R001C03, 2012-7-19]
     */
    public interface OnCheckStateChangeListener
    {
        /**
         * checkbox状态改变回调
         * [功能详细描述]
         * @param which 第几个checkBox被点击
         * @param isChecked checkbox改变后的状态
         */
        public void onCheckedChanged(int which, boolean isChecked);
    }
    
    /**
     * Helper class for creating a custom dialog
     */
    public static class Builder
    {
        /**
         * Dialog元素
         */
        private final DialogController.DialogParams params;
        
        /**
         * 构造函数
         * @param context
         *            上下文context
         */
        public Builder(Context context)
        {
            params = new DialogController.DialogParams(context);
        }
        
        /**
         * 设置消息体
         * @param message
         *            消息体
         * @return Builder
         */
        public Builder setMessage(CharSequence message)
        {
            params.mMgeText = message;
            return this;
        }
        
        /**
         * 设置消息体
         * @param message 消息体资源索引
         * @return Builder
         */
        public Builder setMessage(int message)
        {
            setMessage(params.mContext.getString(message));
            return this;
        }
        
        /**
         * 设置标题
         * @param title 标题
         * @return Builder
         */
        public Builder setTitle(int title)
        {
            params.mTitle = params.mContext.getString(title);
            return this;
        }
        
        /**
         * 设置标题
         * @param title 标题
         * @return Builder
         */
        public Builder setTitle(CharSequence title)
        {
            params.mTitle = title;
            return this;
        }
        
        /**
         * 设置自定义布局
         * @param v
         *            自定义布局View
         * @return Builder
         */
        public Builder setContentView(View v)
        {
            params.mContentView = v;
            return this;
        }
        
        /**
         * 设置自定义布局
         * @param resId
         *            自定义布局资源id
         * @return Builder
         */
        public Builder setContentView(int resId)
        {
            params.mContentViewResId = resId;
            return this;
        }
        
        /**
         * 设置listview的Item
         * @param items 标题
         * @param listener listener
         * @return Builder Builder
         */
        public Builder setItems(CharSequence[] items,
                DialogInterface.OnClickListener listener)
        {
            params.mItems = items;
            params.mOnClickListener = listener;
            return this;
        }
        
        /**
         * 设置listview的adapter
         * @param adapter listview的adapter
         * @param listener listview的点击事件监听
         * @return builder
         */
        public Builder setAdapter(final ListAdapter adapter,
                DialogInterface.OnClickListener listener)
        {
            params.mAdapter = adapter;
            params.mOnClickListener = listener;
            return this;
        }
        
        /**
         * 设置确定按钮
         * @param positiveButtonText
         *            positiveButton文字
         * @param listener
         *            positiveButton监听
         * @return Builder
         */
        public Builder setPositiveButton(int positiveButtonText,
                DialogInterface.OnClickListener listener)
        {
            return setPositiveButton(params.mContext.getString(positiveButtonText),
                    listener);
        }
        
        /**
         * 设置确定按钮
         * 
         * @param positiveButtonText
         *            positiveButton文字
         * @param listener
         *            positiveButton监听
         * @return Builder
         */
        public Builder setPositiveButton(String positiveButtonText,
                DialogInterface.OnClickListener listener)
        {
            params.mPositiveButtonText = positiveButtonText;
            params.mPositiveButtonListener = listener;
            return this;
        }
        
        /**
         * 设置取消按钮
         * 
         * @param negativeButtonText
         *            negativeButton文字
         * @param listener
         *            negativeButton监听
         * @return Builder
         */
        public Builder setNegativeButton(int negativeButtonText,
                DialogInterface.OnClickListener listener)
        {
            return setNegativeButton(params.mContext.getString(negativeButtonText),
                    listener);
        }
        
        /**
         * 设置左按钮
         * @param negativeButtonText
         *            negativeButton文字
         * @param listener
         *            negativeButton监听
         * @return Builder
         */
        public Builder setNegativeButton(String negativeButtonText,
                DialogInterface.OnClickListener listener)
        {
            params.mNegativeButtonText = negativeButtonText;
            params.mNegativeButtonListener = listener;
            return this;
        }
        
        /**
         * 设置多选列表
         * @param items 条目显示的文字
         * @param checkedItems 初始选中的项
         * @param listener listview被选中的监听
         * @return Builder
         */
        public Builder setMultiChoiceItems(CharSequence[] items,
                boolean[] checkedItems,
                final OnMultiChoiceClickListener listener)
        {
            params.mItems = items;
            params.mCheckedItems = checkedItems;
            params.mOnListCheckedListener = listener;
            params.mIsMultiChoice = true;
            return this;
        }
        
        /**
         * 设置单项选择列表
         * @param strArray
         *            条目文字
         * @param checkedItem
         *            默认选中的项
         * @param listener
         *            条目的点击事件监听
         * @return Builder
         */
        public Builder setSingleChoiceItems(String[] strArray, int checkedItem,
                DialogInterface.OnClickListener listener)
        {
            params.mItems = strArray;
            params.mCheckedItem = checkedItem;
            params.mOnClickListener = listener;
            params.mIsSingleChoice = true;
            return this;
        }
        
        /**
         * 设置单项选择列表
         * @param adapter listview的adapter
         * @param checkedItem 默认选中的项
         * @param listener listview的点击事件监听
         * @return Builder
         */
        public Builder setSingleChoiceItems(ListAdapter adapter,
                int checkedItem, OnClickListener listener)
        {
            params.mAdapter = adapter;
            params.mOnClickListener = listener;
            params.mCheckedItem = checkedItem;
            params.mIsSingleChoice = true;
            return this;
        }
        
        /**
         * 设置checkbox组
         * @param checkMsg checkbox消息数组
         * @param defaultValues checkbox默认状态
         * @param listener checkbox状态改变监听
         * @return Builder
         */
        public Builder setCheckBox(String[] checkMsg, boolean[] defaultValues,
                BasicDialog.OnCheckStateChangeListener listener)
        {
            params.mCheckMsg = checkMsg;
            params.mDefaultCheckState = defaultValues;
            params.mCheckBoxStateChangeListner = listener;
            return this;
        }
        
        /**
         * 创建 dialog
         * @return BasicDialog
         */
        public BasicDialog create()
        {
            //创建一个dialog，指定theme
            final BasicDialog dialog = new BasicDialog(params.mContext,
                    R.style.Translucent_NoTitle);
            params.apply(dialog.mDialogController);
            return dialog;
        }
    }
}
