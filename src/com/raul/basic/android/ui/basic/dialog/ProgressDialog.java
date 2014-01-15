package com.raul.basic.android.ui.basic.dialog;

import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.content.DialogInterface.OnKeyListener;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.raul.basic.android.R;

/**
 * 
 * @author xiaomiaomiao
 *
 */
public class ProgressDialog
{
    /**
     * progressdialog实例
     */
    private BasicDialog mProgressDialog = null;
    
    /**
     * 构造函数
     * 创建一个basicdialog实例
     * @param context context
     */
    public ProgressDialog(Context context)
    {
        this(context, R.style.Translucent_NoTitle);
    }
    
    /**
     * 构造函数
     * 创建一个basicdialog实例
     * @param context context
     * @param isCancelable 是否可取消对话框的标志
     */
    public ProgressDialog(Context context, boolean isCancelable)
    {
        this(context, R.style.Translucent_NoTitle, isCancelable);
    }
    
    /**
     * 构造函数
     * 创建一个basicdialog实例
     * @param context context
     * @param theme 主题
     */
    public ProgressDialog(Context context, int theme)
    {
        this(context, theme, true);
    }
    
    /**
     * 构造函数
     * 创建一个basicdialog实例
     * @param context context
     * @param theme 主题
     * @param isCancelable 是否可取消对话框的标志
     */
    public ProgressDialog(Context context, int theme, boolean isCancelable)
    {
        mProgressDialog = new BasicDialog(context, theme, isCancelable);
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.progress_dialog, null);
        mProgressDialog.setCustomContentView(view);
    }
    
    /**
     * 设置需要显示的消息<BR>
     * @param strMessage 需要显示的提示消息
     * @return progressdialog实例
     */
    public ProgressDialog setMessage(String strMessage)
    {
        TextView tvMsg = (TextView) mProgressDialog.getCustomContentView()
                .findViewById(R.id.progress_msg);
        
        if (null != tvMsg)
        {
            tvMsg.setText(strMessage);
        }
        return this;
    }
    
    /**
     * showdialog<BR>
     */
    public void show()
    {
        mProgressDialog.show();
    }
    
    /**
     * 关闭dialog<BR>
     */
    public void dismiss()
    {
        mProgressDialog.dismiss();
    }
    
    /**
     * dialog是否在显示<BR>
     * @return true 在显示 ；false 不在显示
     */
    public boolean isShowing()
    {
        return mProgressDialog.isShowing();
    }
    
    /**
     * Sets the callback that will be called if a key is dispatched to the dialog.
     * @param onKeyListener KeyListener
     */
    public void setOnKeyListener(OnKeyListener onKeyListener)
    {
        mProgressDialog.setOnKeyListener(onKeyListener);
    }
    
    /**
     * Set a listener to be invoked when the dialog is canceled.
     * <p>
     * This will only be invoked when the dialog is canceled, if the creator
     * needs to know when it is dismissed in general, use
     * {@link #setOnDismissListener}.
     * 
     * @param onCancelListener The {@link DialogInterface.OnCancelListener} to use.
     */
    public void setOnCancelListener(OnCancelListener onCancelListener)
    {
        mProgressDialog.setOnCancelListener(onCancelListener);
    }
}
