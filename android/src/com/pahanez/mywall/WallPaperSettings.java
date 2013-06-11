package com.pahanez.mywall;

import java.io.File;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.DialogInterface.OnDismissListener;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;

import com.ipaulpro.afilechooser.utils.FileUtils;
import com.pahanez.mywall.utils.ColorPickerDialog;
import com.pahanez.mywall.utils.ColorPickerDialog.OnColorChangedListener;
import com.pahanez.mywall.utils.Settings;
import com.pahanez.mywall.utils.WLog;

public class WallPaperSettings extends Activity implements OnCheckedChangeListener, OnClickListener, OnSeekBarChangeListener, OnColorChangedListener {

	private static final String TAG = WallPaperSettings.class.getSimpleName();
	private static final int DATA_TYPE_REQUEST_CODE = 107;
	private static final int TEXTSIZE_DIALOG = 101;
	private static final int FRAMERATE_DIALOG = 102;
	private static final int ELEMENT_COUNT = 103;
	private static final int DATA_TYPE_DIALOG = 104;
	private static final int MAX_TEXT_SIZE = 150;
	private TextView mTextSizeTV, mBackgroundColorPickerTV, mTextColorPickerTV, mFrameRateTV, mElementCount, mDataTypeTV;
	private CheckBox mRandomColorCB, mRandomTextSizeCB;
	private ListView mListView;
	// textsizesettings
	private SeekBar mTextSettingsMinSeekBar, mTextSettingsMaxSeekBar, mSeekBar;
	private TextView mTextValueMin, mTextValueMax, mTextSettingsMin, mSeekValue;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.settings);

	}

	@Override
	protected void onResume() {
		super.onResume();
		WLog.d(TAG, " onResume() ");
		initUI();
	}

	private void initUI() {
		mTextSizeTV = (TextView) findViewById(R.id.type_of_data_tv);
		mTextSizeTV.setOnClickListener(this);

		mRandomColorCB = (CheckBox) findViewById(R.id.random_color_checkbox);
		mRandomColorCB.setChecked(Settings.getInstance().isRandomColor());
		mRandomColorCB.setOnCheckedChangeListener(this);

		mRandomTextSizeCB = (CheckBox) findViewById(R.id.random_textsize_checkbox);
		mRandomTextSizeCB.setChecked(Settings.getInstance().isRandomTextSize());
		mRandomTextSizeCB.setOnCheckedChangeListener(this);

		mTextSizeTV = (TextView) findViewById(R.id.textsize_tv);
		mTextSizeTV.setOnClickListener(this);

		mBackgroundColorPickerTV = (TextView) findViewById(R.id.background_color_picker_tv);
		mBackgroundColorPickerTV.setOnClickListener(this);

		mFrameRateTV = (TextView) findViewById(R.id.frame_rate_tv);
		mFrameRateTV.setOnClickListener(this);

		mElementCount = (TextView) findViewById(R.id.element_count_tv);
		mElementCount.setOnClickListener(this);

		mTextColorPickerTV = (TextView) findViewById(R.id.text_color_picker_tv);
		mTextColorPickerTV.setOnClickListener(this);
		mTextColorPickerTV.setEnabled(!Settings.getInstance().isRandomColor());
	}

	@Override
	public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
		switch (buttonView.getId()) {
		case R.id.random_color_checkbox:
			Settings.getInstance().setRandomColor(isChecked);
			mTextColorPickerTV.setEnabled(!Settings.getInstance().isRandomColor());
			break;
		case R.id.random_textsize_checkbox:
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
		case R.id.type_of_data_tv:
			showDialog(DATA_TYPE_DIALOG);
			break;
		case R.id.background_color_picker_tv:
			ColorPickerDialog mBackgroundColorDialog = new ColorPickerDialog(this, Settings.getInstance().getCustomBackgroundColor());
			mBackgroundColorDialog.setOnColorChangedListener(this);
			mBackgroundColorDialog.setId(WConstants.BACKGROUNG_COLOR_DIALOG);
			mBackgroundColorDialog.setAlphaSliderVisible(true);
			mBackgroundColorDialog.show();

			break;
		case R.id.text_color_picker_tv:
			ColorPickerDialog mTextColorDialog = new ColorPickerDialog(this, Settings.getInstance().getCustomTextColor());
			mTextColorDialog.setOnColorChangedListener(this);
			mTextColorDialog.setId(WConstants.TEXT_COLOR_DIALOG);
			mTextColorDialog.setAlphaSliderVisible(true);
			mTextColorDialog.show();

			break;

		case R.id.frame_rate_tv:
			showDialog(FRAMERATE_DIALOG);
			break;
		case R.id.element_count_tv:
			showDialog(ELEMENT_COUNT);
			break;

		default:
			break;
		}
	}

	@Override
	protected Dialog onCreateDialog(int id) {
		AlertDialog.Builder adb = new AlertDialog.Builder(this);
		switch (id) {
		case TEXTSIZE_DIALOG:
			WLog.i(TAG, "TEXTSIZE_DIALOG");
			adb.setTitle(getString(R.string.textsize_tv));
			View viewTextSize = (LinearLayout) getLayoutInflater().inflate(R.layout.settings_textsize, null);
			adb.setView(viewTextSize);
			mTextSettingsMinSeekBar = (SeekBar) viewTextSize.findViewById(R.id.textsize_min_settings_seekbar);
			mTextSettingsMaxSeekBar = (SeekBar) viewTextSize.findViewById(R.id.textsize_max_settings_seekbar);
			mTextSettingsMaxSeekBar.setMax(150);
			mTextSettingsMinSeekBar.setMax(150);
			mTextSettingsMaxSeekBar.setOnSeekBarChangeListener(this);
			mTextSettingsMinSeekBar.setOnSeekBarChangeListener(this);
			mTextValueMin = (TextView) viewTextSize.findViewById(R.id.min_val_tv);
			mTextValueMax = (TextView) viewTextSize.findViewById(R.id.max_val_tv);
			mTextSettingsMin = (TextView) viewTextSize.findViewById(R.id.min_size_textview);

			return adb.create();
		case FRAMERATE_DIALOG:
			WLog.i(TAG, "FRAMERATE_DIALOG");
			adb.setTitle(getString(R.string.frame_rate_tv));
			View viewFrameRate = (LinearLayout) getLayoutInflater().inflate(R.layout.settings_single_seekbar, null);
			adb.setView(viewFrameRate);
			mSeekBar = (SeekBar) viewFrameRate.findViewById(R.id.my_seekbar);
			mSeekBar.setMax(WConstants.MAX_FRAMERATE);
			mSeekBar.setOnSeekBarChangeListener(this);
			mSeekValue = (TextView) viewFrameRate.findViewById(R.id.val_seek_tv);
			return adb.create();
		case ELEMENT_COUNT:
			WLog.i(TAG, "ELEMENT_COUNT");
			adb.setTitle(getString(R.string.element_count_tv));
			View viewElementCount = (LinearLayout) getLayoutInflater().inflate(R.layout.settings_single_seekbar, null);
			adb.setView(viewElementCount);
			mSeekBar = (SeekBar) viewElementCount.findViewById(R.id.my_seekbar);
			mSeekBar.setMax(WConstants.MAX_ELEMENTS_PER_FRAME);
			mSeekBar.setOnSeekBarChangeListener(this);
			mSeekValue = (TextView) viewElementCount.findViewById(R.id.val_seek_tv);
			return adb.create();
		case DATA_TYPE_DIALOG:
			WLog.i(TAG, "DATA_TYPE_DIALOG");
			adb.setTitle(getString(R.string.data_type_name));
			View viewDataType = (LinearLayout) getLayoutInflater().inflate(R.layout.spinner_layout, null);
			adb.setView(viewDataType);
			mListView = (ListView) viewDataType.findViewById(R.id.custom_list);
			ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_single_choice, getResources().getStringArray(R.array.data_type_values));
			mListView.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
			// adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
			mListView.setAdapter(adapter);
			return adb.create();

		default:
			break;
		}
		return null;
	}

	@Override
	protected void onPrepareDialog(int id, final Dialog dialog) {

		switch (id) {
		case TEXTSIZE_DIALOG:
			dialog.setOnDismissListener(new OnDismissListener() {

				@Override
				public void onDismiss(DialogInterface dialog) {
					Settings.getInstance().setMaxTextSize(mTextSettingsMaxSeekBar.getProgress());
					Settings.getInstance().setMinTextSize(mTextSettingsMinSeekBar.getProgress());
				}
			});
			if (Settings.getInstance().isRandomTextSize()) {
				dialog.findViewById(R.id.min_val_layout).setVisibility(View.VISIBLE);

				mTextSettingsMaxSeekBar.setProgress((int) Settings.getInstance().getMaxTextSize());
				mTextSettingsMinSeekBar.setProgress((int) Settings.getInstance().getMinTextSize());
				mTextValueMin.setText(String.valueOf((int) Settings.getInstance().getMinTextSize()));
				mTextValueMax.setText(String.valueOf((int) Settings.getInstance().getMaxTextSize()));
				mTextSettingsMin.setText(getString(R.string.textsize_min));
			} else {
				dialog.findViewById(R.id.min_val_layout).setVisibility(View.GONE);
				mTextSettingsMin.setText(getString(R.string.textsize));
				mTextSettingsMinSeekBar.setProgress((int) Settings.getInstance().getMinTextSize());
				mTextValueMin.setText(String.valueOf((int) Settings.getInstance().getMinTextSize()));
			}

			break;
		case FRAMERATE_DIALOG:
			mSeekBar.setTag(FRAMERATE_DIALOG);
			mSeekBar.setProgress(Settings.getInstance().getFrameRate());
			mSeekValue.setText(String.valueOf(Settings.getInstance().getFrameRate()));
			break;
		case ELEMENT_COUNT:
			mSeekBar.setTag(ELEMENT_COUNT);
			mSeekBar.setProgress(Settings.getInstance().getElementsPerFrame());
			mSeekValue.setText(String.valueOf(Settings.getInstance().getElementsPerFrame()));
			break;
		case DATA_TYPE_DIALOG:
			mListView.setItemChecked(Settings.getInstance().getDataTypeValue(), true);
			mListView.setOnItemClickListener(new OnItemClickListener() {

				@Override
				public void onItemClick(AdapterView<?> parent, View view, int position,long id) {
					switch (position) {
					case WConstants.CPU_TYPE:
						WallController.getInstance().startCpuLoop();
						break;
					case WConstants.TIME_TYPE:
						WallController.getInstance().stopCpuLoop();
						break;
					case WConstants.FILE_TYPE:
						WallController.getInstance().stopCpuLoop();
						
					    Intent target = FileUtils.createGetContentIntent();
					    target.setType("text/*");
					    Intent intent = Intent.createChooser(target, getString(R.string.choose_file));
					    try {
					        startActivityForResult(intent, DATA_TYPE_REQUEST_CODE);
					    } catch (ActivityNotFoundException e) {
					    	e.printStackTrace();
					    }
						
						break;

					default:
						break;
					}
					Settings.getInstance().setDataType(position);
					dialog.cancel();
				}
			});
			break;

		default:
			break;
		}
		super.onPrepareDialog(id, dialog);
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		WLog.e(TAG,"" + requestCode + " , " + resultCode + " , ");
		 switch (requestCode) {
		    case DATA_TYPE_REQUEST_CODE:  
		        if (resultCode == RESULT_OK) {  
		            // The URI of the selected file 
		            final Uri uri = data.getData();
		            // Create a File from this Uri
		            File file = FileUtils.getFile(uri);
		            Settings.getInstance().setExternalFilePath(file.getAbsolutePath());
		            WallController.getInstance().cacheExternalFileData();
		        }
		    }
		super.onActivityResult(requestCode, resultCode, data);
	}

	@Override
	public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
		switch (seekBar.getId()) {
		case R.id.textsize_min_settings_seekbar:
			if (mTextSettingsMaxSeekBar.getProgress() < progress) {
				// if(Settings.getInstance().isRandomTextSize())
				mTextSettingsMaxSeekBar.setProgress(progress);
			}
			mTextValueMin.setText(String.valueOf(progress));
			break;
		case R.id.textsize_max_settings_seekbar:
			if (mTextSettingsMinSeekBar.getProgress() > progress) {
				mTextSettingsMinSeekBar.setProgress(progress);
			}
			mTextValueMax.setText(String.valueOf(progress));
			break;
		case R.id.my_seekbar:

			if (progress == 0)
				mSeekValue.setText("1");
			else
				mSeekValue.setText(String.valueOf(progress));
			break;

		default:
			break;
		}
	}

	@Override
	public void onStartTrackingTouch(SeekBar seekBar) {
	}

	@Override
	public void onStopTrackingTouch(SeekBar seekBar) {
		if (seekBar.getTag() != null) {
			if (seekBar.getTag().equals(FRAMERATE_DIALOG)) {
				Settings.getInstance().setFrameRate(seekBar.getProgress());
			} else if (seekBar.getTag().equals(ELEMENT_COUNT)) {
				Settings.getInstance().setElementsPerFrame(seekBar.getProgress());
			}
		}

	}

	@Override
	public void onColorChanged(int id, int color) {
		switch (id) {
		case WConstants.BACKGROUNG_COLOR_DIALOG:
			Settings.getInstance().setCustomBackgroundColor(color);
			break;
		case WConstants.TEXT_COLOR_DIALOG:
			Settings.getInstance().setCustomTextColor(color);
			break;
		}
	}

}
