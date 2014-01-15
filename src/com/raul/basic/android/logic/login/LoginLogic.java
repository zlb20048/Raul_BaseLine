package com.raul.basic.android.logic.login;

import android.net.Uri;

import com.raul.basic.android.framework.logic.BaseLogic;



public class LoginLogic extends BaseLogic implements ILoginLogic{
	@Override
	public void send() {
	    this.sendMessage(1, "Send form login logic!");
	}

    @Override
    public void forward()
    {
        this.sendEmptyMessage(3);
        
    }

    @Override
    protected Uri[] getObserverUris()
    {
        return new Uri[]{Uri.parse("content://com.chinaunicom.woyou.database/UserConfig")};
    }

    @Override
    protected void onChangeByUri(boolean selfChange, Uri uri)
    {
    }
    
    
}
