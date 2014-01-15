package com.raul.basic.android.ui.main;

import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import com.raul.basic.android.R;
import com.raul.basic.android.logic.main.IMainLogic;
import com.raul.basic.android.ui.basic.BasicActivity;
import com.raul.basic.android.ui.login.LoginActivity;

public class MainActivity extends BasicActivity {
	private IMainLogic mMainlogic = null;

	private TextView mTextView = null;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		mTextView = (TextView) this.findViewById(R.id.text);
		Button sendBtn = (Button) this.findViewById(R.id.send);
		sendBtn.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				mMainlogic.send();
			}
		});
		Button forwardBtn = (Button) this.findViewById(R.id.forward);
		forwardBtn.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				Intent intent = new Intent();
				intent.setClass(MainActivity.this, LoginActivity.class);
				MainActivity.this.startActivity(intent);
			}
		});
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
			break;
		case 2:
			String txt2 = (String) obj;
			mTextView.setText(txt2 + "," + System.currentTimeMillis());
			break;
		}

	}

	@Override
	protected void initLogics() {
		mMainlogic = (IMainLogic) getLogicByInterfaceClass(IMainLogic.class);

	}

}
