package com.pahanez.mywall;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.SeekBar;
import android.widget.TextView;

import com.pahanez.mywall.utils.Settings;
import com.pahanez.mywall.utils.WLog;

public class WallPaperSettings extends Activity implements
		OnCheckedChangeListener, OnClickListener {
	
	
	
	private static final String TAG = WallPaperSettings.class.getSimpleName();
	private static final int TEXTSIZE_DIALOG = 101;
	private TextView mTextSizeTV;
	private CheckBox mRandomColorCB,mRandomTextSizeCB;
	
	//textsizesettings
	private SeekBar mTextSettingsSeekBar;
	private RadioButton mTextSettingsMinRB,mTextSettingsMaxRB;
	private CheckBox mRandomTextSizeCB1;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.settings);

	}
	
	@Override
	protected void onResume() {
		super.onResume();
		initUI();
	}

	private void initUI() {
		mRandomColorCB = (CheckBox) findViewById(R.id.random_color_checkbox);
		mRandomColorCB.setChecked(Settings.getInstance().isRandomColor());
		mRandomColorCB.setOnCheckedChangeListener(this);
		
		mRandomTextSizeCB = (CheckBox) findViewById(R.id.random_textsize_checkbox);
		mRandomTextSizeCB.setChecked(Settings.getInstance().isRandomTextSize());
		mRandomTextSizeCB.setOnCheckedChangeListener(this); 
		
		mTextSizeTV = (TextView) findViewById(R.id.textsize_tv);
		mTextSizeTV.setOnClickListener(this);
	}

	@Override
	public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
		switch (buttonView.getId()) {
		case R.id.random_color_checkbox:
			Settings.getInstance().setRandomColor(isChecked);
			break;
		case R.id.random_textsize_checkbox:
		case R.id.textsize_settings_random_cb:
			WLog.d(TAG, "pressed");
			Settings.getInstance().setRandomTextSize(isChecked);
			break;
		default:
			break;
		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.textsize_tv:
				showDialog(TEXTSIZE_DIALOG);
			break;

		default:
			break;
		}
	}
	
	@Override
	protected Dialog onCreateDialog(int id) {
		switch (id) {
		case TEXTSIZE_DIALOG:
			WLog.e(TAG,"TEXTSIZE_DIALOG");
		 AlertDialog.Builder adb = new AlertDialog.Builder(this);
		    adb.setTitle(getString(R.string.textsize_tv));
		   View view = (LinearLayout) getLayoutInflater()
		        .inflate(R.layout.settings_textsize, null);
		    adb.setView(view);
		    mTextSettingsSeekBar = (SeekBar) view.findViewById(R.id.textsize_settings_seekbar);
		    mTextSettingsMinRB = (RadioButton) view.findViewById(R.id.textsize_settings_rb_min);
		    mTextSettingsMaxRB = (RadioButton) view.findViewById(R.id.textsize_settings_rb_max);
		    mRandomTextSizeCB1 = (CheckBox) view.findViewById(R.id.textsize_settings_random_cb);
		    mRandomTextSizeCB1.setOnCheckedChangeListener(this);
//		    tvCount = (TextView) view.findViewById(R.id.tvCount);
		    return adb.create();
		    
		default:
			break;
		}
		return null;
	}
	
	@Override
	protected void onPrepareDialog(int id, Dialog dialog) {
		switch (id) {
		case TEXTSIZE_DIALOG:
				mRandomTextSizeCB1.setChecked(Settings.getInstance().isRandomTextSize());
				if(!Settings.getInstance().isRandomTextSize()){
					mTextSettingsMaxRB.setVisibility(View.VISIBLE);
				}else{
					mTextSettingsMaxRB.setVisibility(View.GONE);
					mTextSettingsMinRB.setText(getString(R.string.textsize));
				}
				
				mTextSettingsMinRB.setChecked(true);
				
			break;

		default:
			break;
		}
		super.onPrepareDialog(id, dialog);
	}
	
}
