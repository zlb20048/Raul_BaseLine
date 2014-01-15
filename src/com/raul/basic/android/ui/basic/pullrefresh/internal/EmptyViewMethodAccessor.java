package com.raul.basic.android.ui.basic.pullrefresh.internal;

import android.view.View;

/**
 * 空视图方法接口
 * 用于PullToRefreshAdapterViewBase对内部Listview 添加空视图
 */
public interface EmptyViewMethodAccessor
{
    
    /**
     * Calls upto AdapterView.setEmptyView()
     * 
     * @param emptyView  to set as Empty View
     */
    public void setEmptyViewInternal(View emptyView);
    
    /**
     * Should call PullToRefreshBase.setEmptyView() which will then
     * automatically call through to setEmptyViewInternal()
     * 
     * @param emptyView  to set as Empty View
     */
    public void setEmptyView(View emptyView);
    
}
