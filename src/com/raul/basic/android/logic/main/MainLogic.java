package com.raul.basic.android.logic.main;

import com.raul.basic.android.framework.logic.BaseLogic;



public class MainLogic extends BaseLogic implements IMainLogic{

    @Override
	public void send() {
		sendMessage(2, "Send form main logic!");
	}
}
