package com.raul.basic.android.ui.basic.pullrefresh.internal;

/**
 * 下拉刷新监听
 * @author xiaomiaomiao
 *
 */
public class ListViewListener
{
    // ===========================================================
    // Inner and Anonymous Classes
    // ===========================================================
    
    /**
     * Simple Listener to listen for any callbacks to Refresh.
     * 
     * @author Chris Banes
     */
    public static interface OnRefreshListener
    {
        
        /**
         * onRefresh will be called for both Pull Down from top, and Pull Up
         * from Bottom
         */
        public void onRefresh();
        
    }
}
