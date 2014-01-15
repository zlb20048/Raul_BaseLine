package com.raul.basic.android.ui.basic.listview;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.raul.basic.android.R;
import com.raul.basic.android.ui.basic.actionbar.ActionBarMenuItem;
import com.raul.basic.android.ui.basic.actionbar.BasicActionBarActivity;
import com.raul.basic.android.ui.basic.spinner.DropDownSpinner;
import com.raul.basic.android.ui.basic.spinner.ListPopupWindow;
import com.raul.basic.android.util.Log;

/**
 * 控制多选列表的基类
 * 页面需要实现带actionbar及多选列表的，需要继承该类，并实现指定的方法。
 * @author xiaomiaomiao
 */
public abstract class BasicMutiChoiceListActivity extends
        BasicActionBarActivity
{
    /**
     * DEBUG_TAG
     */
    private static final String TAG = "BasicMutiChoiceListActivity";
    
    /**
     * 多选列表
     */
    private static final String CHOICE_LIST_DONE = "choice list done";
    
    /**
     * 多选列表
     */
    private LongClickListView mListView;
    
    /**
     * 多选状态下的topbar
     */
    private View mChoiceListBar;
    
    /**
     * 多选状态下的title
     */
    private TextView mChoiceListTitle;
    
    /**
     * 下拉框的adapter
     */
    private BaseAdapter spinnerAdapter;
    
    /**
     * 右菜单项，默认完成按钮
     */
    private ImageView mRightListMenu;
    
    /**
     * 左菜单项
     */
    private ImageView mLeftListMenu;
    
    /**
     * 是否在长按mode中
     */
    private boolean onLongClickStatus;
    
    /**
     * 获取多选状态下，返回箭头边上的 小图标<BR>
     * @return 图标的 ID
     */
    protected abstract int getChoiceListBackIcon();
    
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        mChoiceListBar = findViewById(R.id.mutichoice_list_bar);
        mChoiceListBar.findViewById(R.id.mutichoice_back)
                .setVisibility(View.VISIBLE);
        //设置多项状态下 返回按钮旁边的小图标
        ImageView choiceIcon = (ImageView) mChoiceListBar.findViewById(R.id.mutichoice_icon);
        choiceIcon.setVisibility(View.VISIBLE);
        choiceIcon.setImageResource(getChoiceListBackIcon());
        mListView = getListView();
        if (mListView == null)
        {
            mListView = (LongClickListView) findViewById(getListViewId());
        }
        findViewById(R.id.mutichoice_back).setOnClickListener(this);
        mRightListMenu = (ImageView) findViewById(R.id.mutichoice_right_menu);
        mRightListMenu.setTag(CHOICE_LIST_DONE);
        mRightListMenu.setOnClickListener(this);
        
        mLeftListMenu = (ImageView) findViewById(R.id.mutichoice_left_menu);
        mLeftListMenu.setOnClickListener(this);
        if (mListView != null)
        {
            mListView.setAdapter(getlistViewAdapter());
            mListView.setMode(LongClickListView.MODE_LONG_CLICK);
            mListView.setLongClickCallBack(new LongClickCallBack()
            {
                @Override
                public void startLongClick()
                {
                    Log.d(TAG, "startLongClick");
                    onLongClickStatus = true;
                    shwoChoiceBar();
                    listStartLongClick();
                }
                
                @Override
                public void itemStatusChanged(int position,
                        boolean itemSelected, View view)
                {
                    Log.d(TAG, "itemStatusChanged");
                    updateCount(mListView.getSelectedCount());
                    listItemStatusChanged(position, itemSelected, view);
                }
                
                @Override
                public void destroyLongClick()
                {
                    Log.d(TAG, "destroyLongClick");
                    showActionBar();
                    listDestroyLongClick();
                    onLongClickStatus = false;
                }
            });
            mListView.setOnItemClickListener(new OnItemClickListener()
            {
                @Override
                public void onItemClick(AdapterView<?> parent, View view,
                        int position, long id)
                {
                    Log.d(TAG, "onItemClick");
                    listOnItemClick(parent, view, position, id);
                }
            });
        }
        DropDownSpinner spinner = (DropDownSpinner) findViewById(R.id.choice_all);
        View titleView = RelativeLayout.inflate(this,
                R.layout.spinner_all_item,
                null);
        spinner.setTitleView(titleView);
        mChoiceListTitle = (TextView) titleView.findViewById(R.id.count);
        if (-1 != getChoiceListTitleId())
        {
            ((TextView) titleView.findViewById(R.id.mutichoice_title)).setText(getChoiceListTitleId());
        }
        if (needSpinner())
        {
            titleView.findViewById(R.id.arrow).setVisibility(View.VISIBLE);
            spinner.setDropDownWidth(ListPopupWindow.ROOT_VIEW_WIDTH);
            spinnerAdapter = getCustomSpinnerAdapter();
            if (spinnerAdapter == null)
            {
                spinnerAdapter = new BaseAdapter()
                {
                    
                    @Override
                    public int getCount()
                    {
                        return 1;
                    }
                    
                    @Override
                    public Object getItem(int position)
                    {
                        return position;
                    }
                    
                    @Override
                    public long getItemId(int position)
                    {
                        return position;
                    }
                    
                    @Override
                    public View getView(int position, View convertView,
                            ViewGroup parent)
                    {
                        TextView text = null;
                        if (convertView == null)
                        {
                            convertView = LayoutInflater.from(BasicMutiChoiceListActivity.this)
                                    .inflate(R.layout.spinner_menu_row, null);
                            text = (TextView) convertView.findViewById(R.id.spinner_menu_content);
                            convertView.setTag(text);
                        }
                        else
                        {
                            text = (TextView) convertView.getTag();
                        }
                        text.setText(mListView.isChooseAll() ? R.string.choose_none
                                : R.string.choose_all);
                        return convertView;
                    }
                };
            }
            spinner.setAdapter(spinnerAdapter);
            OnItemClickListener listener = getSpinnerOnClickListener();
            if (listener == null)
            {
                listener = new OnItemClickListener()
                {
                    
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view,
                            int position, long id)
                    {
                        if (0 == position)
                        {
                            if (mListView.isChooseAll())
                            {
                                mListView.quitLongClick();
                            }
                            else
                            {
                                mListView.chooseAll();
                                updateCount(mListView.getSelectedCount());
                            }
                        }
                    }
                };
            }
            spinner.setOnItemClickListener(listener);
        }
        else
        {
            titleView.findViewById(R.id.arrow).setVisibility(View.GONE);
        }
    }
    
    @Override
    public void onClick(View v)
    {
        switch (v.getId())
        {
            case R.id.mutichoice_back:
                mListView.quitLongClick();
                break;
            case R.id.mutichoice_right_menu:
                if (CHOICE_LIST_DONE.equals(v.getTag()))
                {
                    onChoiceListDone();
                    mListView.quitLongClick();
                    break;
                }
            case R.id.mutichoice_left_menu:
                onMenuClick((Integer) v.getTag());
                break;
            default:
                super.onClick(v);
                break;
        }
        //        if (v == mButtonDone)
        //        {
        //            onChoicListDone();
        //            mListView.quitLongClick();
        //        }
        //        else
        //        {
        //            super.onClick(v);
        //        }
    }
    
    /**
     * 设置多选列表的菜单
     * 
     * @param witch 菜单项位置
     * @param menu 菜单项
     */
    protected final void setChoiceListMenu(int witch, ActionBarMenuItem menu)
    {
        if (LEFT == witch)
        {
            setMenu(mLeftListMenu, menu);
            findViewById(R.id.mutichoice_left_menu_separator).setVisibility(View.VISIBLE);
        }
        else
        {
            setMenu(mRightListMenu, menu);
        }
    }
    
    /**
     * 设置多选列表的下拉式完成按钮
     * @param adapter ListAdapter
     * @param onItemClickListener OnItemClickListener
     * @param prompt PromptView
     */
    protected final void setChoiceDoneDropDown(ListAdapter adapter,
            OnItemClickListener onItemClickListener, View prompt)
    {
        DropDownSpinner spinner = (DropDownSpinner) findViewById(R.id.mutichoice_right_spinner);
        spinner.setDropDownWidth(ListPopupWindow.ROOT_VIEW_WIDTH);
        spinner.setAdapter(adapter);
        spinner.setOnItemClickListener(onItemClickListener);
        spinner.setPromptView(prompt);
        spinner.setVisibility(View.VISIBLE);
        mRightListMenu.setVisibility(View.GONE);
    }
    
    @Override
    protected abstract int getViewId();
    
    /**
     * 
     * 得到子类多选列表的id
     * @return int 子类多选列表的id
     */
    protected abstract int getListViewId();
    
    protected LongClickListView getListView()
    {
        return null;
    }
    
    /**
     * 
     * 得到子类多选列表的Adapter
     * @return ListAdapter 子类多选列表的Adapter
     */
    protected abstract ListAdapter getlistViewAdapter();
    
    /**
     * 进入多选状态时的回调方法
     */
    protected void listStartLongClick()
    {
        
    }
    
    /**
     * 多选状态下选项状态更新的回调方法
     * @param position 选项下标
     * @param itemSelected 选项状态
     * @param view 选项的View
     */
    protected void listItemStatusChanged(int position, boolean itemSelected,
            View view)
    {
        
    }
    
    /**
     * 
     * 退出多选状态时的回调方法
     */
    protected void listDestroyLongClick()
    {
        
    }
    
    /**
     * 条目被选中时的回调方法
     * @param parent The AdapterView where the click happened.
     * @param view The view within the AdapterView that was clicked (this
     *            will be a view provided by the adapter)
     * @param position The position of the view in the adapter.
     * @param id The row id of the item that was clicked.
     */
    protected void listOnItemClick(AdapterView<?> parent, View view,
            int position, long id)
    {
    }
    
    /**
     * 当进入多选状态时获取topbar上的title信息
     * @return String 多选状态下的title
     */
    protected int getChoiceListTitleId()
    {
        return -1;
    }
    
    /**
     * 多选状态完成时的回调方法
     */
    protected void onChoiceListDone()
    {
        
    }
    
    /**
     * 是否需要下拉框
     * 
     * @return boolean 是否需要下拉框
     */
    protected boolean needSpinner()
    {
        return true;
    }
    
    /**
     * 更新选中条目
     * @param count 选中条目
     */
    protected void updateCount(int count)
    {
        mChoiceListTitle.setText(getString(R.string.selected, count));
        if (null != spinnerAdapter)
        {
            spinnerAdapter.notifyDataSetChanged();
        }
    }
    
    /**
     * 显示多选状态下的topbar
     */
    protected void shwoChoiceBar()
    {
        mActionBar.setVisibility(View.GONE);
        mChoiceListBar.setVisibility(View.VISIBLE);
    }
    
    /**
     * 显示非多选状态下的topbar
     */
    protected void showActionBar()
    {
        mActionBar.setVisibility(View.VISIBLE);
        mChoiceListBar.setVisibility(View.GONE);
    }
    
    /**
     * 设置完成按钮是否可用
     * @param enabled 是否可用
     */
    protected void setButtonDoneEnabled(boolean enabled)
    {
        mRightListMenu.setEnabled(enabled);
    }
    
    @Override
    public boolean onMutiBack()
    {
        if (onLongClickStatus)
        {
            mListView.quitLongClick();
            return true;
        }
        return super.onMutiBack();
    }
    
    /**
     * 返回长按状态
     * @return  返回长按状态
     */
    public boolean isOnLongClickStatus()
    {
        return onLongClickStatus;
    }
    
    protected BaseAdapter getCustomSpinnerAdapter()
    {
        return null;
    }
    
    protected OnItemClickListener getSpinnerOnClickListener()
    {
        return null;
    }
}
