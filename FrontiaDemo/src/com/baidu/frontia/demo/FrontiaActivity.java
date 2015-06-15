package com.baidu.frontia.demo;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.baidu.frontia.Frontia;
import com.baidu.frontia.demo.share.ShareActivity;

public class FrontiaActivity extends Activity {

	private Button authBtn;
	private Button appDataBtn;
	private Button appFileBtn;
	private Button personalFileBtn;
	private Button pushBtn;
	private Button statBtn;
	private Button accountBtn;
	private Button aclBtn;
	private Button lbsBtn;
	private Button shareBtn;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_main);
		boolean b = Frontia.init(this.getApplicationContext(), Conf.APIKEY);
		
		//just for silent update function check
		//FrontiaImpl.get().setSlientUpdateDebugDomain("http://10.26.165.13:8909/build/packet");
        setupViews();
	}

	private void setupViews() {
		authBtn = (Button)findViewById(R.id.authBtn);
		appDataBtn = (Button)findViewById(R.id.appDataBtn);
		appFileBtn = (Button)findViewById(R.id.appFileBtn);
		personalFileBtn = (Button)findViewById(R.id.personalFileBtn);
		pushBtn = (Button)findViewById(R.id.pushBtn);
		statBtn = (Button)findViewById(R.id.statBtn);
		accountBtn = (Button)findViewById(R.id.accountBtn);
		aclBtn = (Button)findViewById(R.id.acl);
		lbsBtn = (Button)findViewById(R.id.lbsBtn);
		shareBtn = (Button)findViewById(R.id.shareBtn);
		
		authBtn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				/*Intent intent = new Intent(FrontiaActivity.this,SocialActivity.class);
				startActivity(intent);*/
			}
		});
		appDataBtn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
			}
		});
		appFileBtn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
			}
		});
		personalFileBtn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
			}
		});
		pushBtn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
			}
		});
		statBtn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
			}
		});
		accountBtn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
			}
		});
		aclBtn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
			}
		});
		lbsBtn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				/*Intent intent = new Intent(FrontiaActivity.this,LBSActivity.class);
				startActivity(intent);*/
			}
		});

		shareBtn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(FrontiaActivity.this,ShareActivity.class);
				startActivity(intent);
			}
		});
	}

}
