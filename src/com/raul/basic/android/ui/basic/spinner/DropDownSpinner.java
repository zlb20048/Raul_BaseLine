package com.raul.basic.android.ui.basic.spinner;

import android.content.Context;
import android.graphics.PixelFormat;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.PopupWindow.OnDismissListener;

import com.raul.basic.android.R;

/**
 * 实现弹出下拉框功能的控件
 * @author xiaomiaomiao
 */
public class DropDownSpinner extends LinearLayout
{
    
    private OnItemClickListener mOnItemClickListener;
    
    private WindowManager mWindowManager;
    
    private DropdownPopup mListPopupWindow;
    
    private View grayView;
    
    private boolean mRowLight;
    
    private int mDropDownWidth = ListPopupWindow.WRAP_CONTENT;
    
    private PopupWindow.OnDismissListener mOnDismissListener;
    
    /**
     * 构造方法
     * 
     * @param context context
     */
    public DropDownSpinner(Context context)
    {
        this(context, null);
    }
    
    /**
     * 构造方法
     * 
     * @param context context
     * @param attrs attrs
     */
    public DropDownSpinner(Context context, AttributeSet attrs)
    {
        super(context, attrs);
        setClickable(true);
        mWindowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        mListPopupWindow = new DropdownPopup(context, attrs);
    }
    
    //    /**
    //     * 构造方法
    //     * 
    //     * @param context context
    //     * @param attrs attrs
    //     * @param defStyle defStyle
    //     */
    //    public DropDownSpinner(Context context, AttributeSet attrs, int defStyle)
    //    {
    //        super(context, attrs, defStyle);
    //        mListPopupWindow = new DropdownPopup(context, attrs, defStyle);
    //    }
    
    /**
     * 设置弹出列表item的宽度
     * @param unit 宽度的单位
     * @param value 宽度的值
     */
    public void setDropDownWidth(int unit, float value)
    {
        DisplayMetrics metrics = getContext().getResources()
                .getDisplayMetrics();
        float result = 0;
        switch (unit)
        {
            case TypedValue.COMPLEX_UNIT_PX:
                result = value;
            case TypedValue.COMPLEX_UNIT_DIP:
                result = value * metrics.density;
            case TypedValue.COMPLEX_UNIT_SP:
                result = value * metrics.scaledDensity;
            case TypedValue.COMPLEX_UNIT_PT:
                result = value * metrics.xdpi * (1.0f / 72);
            case TypedValue.COMPLEX_UNIT_IN:
                result = value * metrics.xdpi;
            case TypedValue.COMPLEX_UNIT_MM:
                result = value * metrics.xdpi * (1.0f / 25.4f);
        }
        setDropDownWidth(Math.round(result));
    }
    
    /**
     * 设置弹出列表item的宽度
     * @param value 宽度的值
     */
    public void setDropDownWidth(int value)
    {
        if (!mListPopupWindow.isShowing())
        {
            mDropDownWidth = value;
            if (mDropDownWidth == ListPopupWindow.ROOT_VIEW_WIDTH && !mRowLight)
            {
                grayView = LayoutInflater.from(getContext())
                        .inflate(R.layout.gray_view, null);
            }
            else
            {
                grayView = null;
            }
        }
    }
    
    /**
     * 设置显示条目的限制
     * @param count 显示的条目
     */
    public void setDisplayItem(int count)
    {
        mListPopupWindow.setDisplayItem(count);
    }
    
    /**
     * {@inheritDoc}}
     * @see android.view.View#performClick()
     */
    @Override
    public boolean performClick()
    {
        if (!super.performClick())
        {
            show();
        }
        
        return true;
    }
    
    /**
     * 弹出下拉框
     */
    public void show()
    {
        if (!mListPopupWindow.isShowing())
        {
            mListPopupWindow.show();
        }
    }
    
    /**
     * return the spinner is currently showing
     * 
     * @return {@code true} if the spinner is currently showing, {@code false} otherwise.
     */
    public boolean isShowing()
    {
        return mListPopupWindow.isShowing();
    }
    
    /**
     * 收起下拉框
     */
    public void close()
    {
        mListPopupWindow.dismiss();
    }
    
    /**
     * 设置adapter
     * @param adapter ListAdapter
     */
    public void setAdapter(ListAdapter adapter)
    {
        mListPopupWindow.setAdapter(adapter);
    }
    
    /**
     * 设置PromptView
     * @param prompt PromptView
     */
    public void setPromptView(View prompt)
    {
        mListPopupWindow.setPromptView(prompt);
    }
    
    /**
     * Set where the optional prompt view should appear. The default is
     * {@link #POSITION_PROMPT_ABOVE}.
     * 
     * @param position A position constant declaring where the prompt should be displayed.
     * 
     * @see #ListPopupWindow.POSITION_PROMPT_ABOVE
     * @see #ListPopupWindow.POSITION_PROMPT_BELOW
     */
    public void setPromptPosition(int position)
    {
        mListPopupWindow.setPromptPosition(position);
    }
    
    /**
     * 设置TitleView
     * @param titleView TitleView
     */
    public void setTitleView(View titleView)
    {
        removeAllViews();
        addView(titleView, LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT);
    }
    
    /**
     * 有蒙版效果时整行都要亮
     * @param rowLight 是否亮整行
     */
    public void setRowLight(boolean rowLight)
    {
        mRowLight = rowLight;
    }
    
    /**
     * Sets a listener to receive events when a list item is selected.
     * 
     * @param onItemClickListener Listener to register.
     * 
     * @see ListView#setOnItemSelectedListener(android.widget.AdapterView.OnItemSelectedListener)
     */
    public void setOnItemClickListener(OnItemClickListener onItemClickListener)
    {
        mOnItemClickListener = onItemClickListener;
    }
    
    /**
     * Set a listener to receive a callback when the popup is dismissed.
     *
     * @param listener Listener that will be notified when the popup is dismissed.
     */
    public void setOnDismissListener(PopupWindow.OnDismissListener listener)
    {
        mOnDismissListener = listener;
    }
    
    /**
     * 下拉框组件的弹出框部分
     * @author qinyangwang
     * @version [RCS Client V100R001C03, 2012-6-29]
     */
    private class DropdownPopup extends ListPopupWindow
    {
        private CharSequence mHintText;
        
        private ListAdapter mAdapter;
        
        public DropdownPopup(Context context, AttributeSet attrs)
        {
            super(context, attrs);
            
            setAnchorView(DropDownSpinner.this);
            setModal(true);
            setPromptPosition(POSITION_PROMPT_ABOVE);
            setBackgroundDrawable(context.getResources()
                    .getDrawable(R.color.white));
            setOnItemClickListener(new OnItemClickListener()
            {
                @Override
                public void onItemClick(AdapterView<?> parent, View v,
                        int position, long id)
                {
                    dismiss();
                    if (mOnItemClickListener != null)
                    {
                        mOnItemClickListener.onItemClick(parent,
                                v,
                                position,
                                id);
                    }
                }
            });
            setOnDismissListener(new OnDismissListener()
            {
                
                @Override
                public void onDismiss()
                {
                    if (mDropDownWidth == ROOT_VIEW_WIDTH && !mRowLight && grayView != null)
                    {
                        mWindowManager.removeView(grayView);
                    }
                    DropDownSpinner.this.setSelected(false);
                    if (null != mOnDismissListener)
                    {
                        mOnDismissListener.onDismiss();
                    }
                }
            });
        }
        
        @Override
        public void setAdapter(ListAdapter adapter)
        {
            super.setAdapter(adapter);
            mAdapter = adapter;
        }
        
        public CharSequence getHintText()
        {
            return mHintText;
        }
        
        public void setPromptText(CharSequence hintText)
        {
            // Hint text is ignored for dropdowns, but maintain it here.
            mHintText = hintText;
        }
        
        @Override
        public void show()
        {
            if (null != mAdapter && 0 < mAdapter.getCount())
            {
                final int spinnerPaddingLeft = DropDownSpinner.this.getPaddingLeft();
                final Drawable background = getBackground();
                int bgOffset = 0;
                if (background != null)
                {
                    Rect tempRect = new Rect();
                    background.getPadding(tempRect);
                    bgOffset = -tempRect.left;
                }
                if (mDropDownWidth == WRAP_CONTENT)
                {
                    final int spinnerWidth = DropDownSpinner.this.getWidth();
                    final int spinnerPaddingRight = DropDownSpinner.this.getPaddingRight();
                    setContentWidth(Math.max(getMeasureContentWidth(),
                            spinnerWidth - spinnerPaddingLeft
                                    - spinnerPaddingRight));
                }
                else if (mDropDownWidth == FILL_PARENT)
                {
                    final int spinnerWidth = DropDownSpinner.this.getWidth();
                    final int spinnerPaddingRight = DropDownSpinner.this.getPaddingRight();
                    setContentWidth(spinnerWidth - spinnerPaddingLeft
                            - spinnerPaddingRight);
                }
                else if (mDropDownWidth == ROOT_VIEW_WIDTH)
                {
                    setContentWidth(DropDownSpinner.this.getRootView()
                            .getWidth());
                    setClippingEnabled(false);
                    int[] location = new int[2];
                    DropDownSpinner.this.getLocationOnScreen(location);
                    bgOffset -= location[0];
                }
                else
                {
                    setContentWidth(mDropDownWidth);
                }
                
                //            final int spinnerWidth = DropDownSpinner.this.getWidth();
                //            final int spinnerPaddingRight = DropDownSpinner.this.getPaddingRight();
                //            setContentWidth(spinnerWidth - spinnerPaddingLeft
                //                    - spinnerPaddingRight);
                setHorizontalOffset(bgOffset + spinnerPaddingLeft);
                setInputMethodMode(ListPopupWindow.INPUT_METHOD_NOT_NEEDED);
                DropDownSpinner.this.setSelected(true);
                if (mDropDownWidth == ROOT_VIEW_WIDTH && !mRowLight
                        && grayView != null && !isShowing())
                {
                    WindowManager.LayoutParams p = new WindowManager.LayoutParams();
                    p.gravity = Gravity.LEFT | Gravity.TOP;
                    p.format = PixelFormat.TRANSLUCENT;
                    p.flags |= WindowManager.LayoutParams.FLAG_IGNORE_CHEEK_PRESSES
                            | WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
                            | WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE
                            | WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS;
                    p.type = WindowManager.LayoutParams.TYPE_APPLICATION_PANEL;
                    p.token = DropDownSpinner.this.getWindowToken();
                    p.setTitle("gray:" + Integer.toHexString(hashCode()));
                    p.packageName = DropDownSpinner.this.getContext()
                            .getPackageName();
                    
                    int[] location = new int[2];
                    DropDownSpinner.this.getLocationOnScreen(location);
                    setGrayLayoutParams(R.id.gray_top, -1, location[1]);
                    setGrayLayoutParams(R.id.gray_vertical_center,
                            -1,
                            DropDownSpinner.this.getHeight());
                    if (mRowLight)
                    {
                        setGrayLayoutParams(R.id.gray_left, 0, -1);
                        setGrayLayoutParams(R.id.gray_horizontal_center,
                                DropDownSpinner.this.getRootView().getWidth(),
                                -1);
                    }
                    else
                    {
                        setGrayLayoutParams(R.id.gray_left, location[0], -1);
                        setGrayLayoutParams(R.id.gray_horizontal_center,
                                DropDownSpinner.this.getWidth(),
                                -1);
                    }
                    mWindowManager.addView(grayView, p);
                }
                super.show();
                getListView().setChoiceMode(ListView.CHOICE_MODE_SINGLE);
            }
        }
        
        private void setGrayLayoutParams(int id, int width, int height)
        {
            View view = null;
            if (grayView != null)
            {
                view = grayView.findViewById(id);
            }
            if (view != null)
            {
                ViewGroup.LayoutParams lp = view.getLayoutParams();
                if (width != -1)
                {
                    lp.width = width;
                }
                if (height != -1)
                {
                    lp.height = height;
                }
                view.setLayoutParams(lp);
            }
        }
    }
    
}
