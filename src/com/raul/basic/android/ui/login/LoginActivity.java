package com.raul.basic.android.ui.login;

import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import com.raul.basic.android.R;
import com.raul.basic.android.logic.login.ILoginLogic;
import com.raul.basic.android.ui.basic.BasicActivity;
import com.raul.basic.android.ui.basic.EmailListActivity;
import com.raul.basic.android.ui.basic.ExplorerActivity;

public class LoginActivity extends BasicActivity {
	private TextView mTextView = null;
	private ILoginLogic mLoginLogic = null;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.login);
		mTextView = (TextView) this.findViewById(R.id.text);
		Button sendBtn = (Button) this.findViewById(R.id.send);
		sendBtn.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				mLoginLogic.send();
			}
		});
		Button forwardBtn = (Button) this.findViewById(R.id.forward);
		forwardBtn.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				mLoginLogic.forward();
			}
		});

	}

	@Override
	protected void initLogics() {
		mLoginLogic = (ILoginLogic) getLogicByInterfaceClass(ILoginLogic.class);
	}

	@Override
	protected boolean isPrivateHandler() {
		return true;
	}

	@Override
	protected void handleStateMessage(Message msg) {
		int what = msg.what;
		Object obj = msg.obj;
		switch (what) {
		case 1:
			String txt1 = (String) obj;
			mTextView.setText(txt1 + "," + System.currentTimeMillis());
			Intent intent1 = new Intent();
			intent1.setClass(LoginActivity.this, EmailListActivity.class);
			LoginActivity.this.startActivity(intent1);
			break;
		case 2:
			String txt2 = (String) obj;
			mTextView.setText(txt2 + "," + System.currentTimeMillis());
			break;
		case 3:
			Intent intent = new Intent();
			intent.setClass(LoginActivity.this, ExplorerActivity.class);
			LoginActivity.this.startActivity(intent);
			break;
		}
	}

}