package com.pahanez.mywall;

import java.io.File;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnDismissListener;
import android.content.Intent;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CheckedTextView;
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
	private static final int FONTS_DIALOG = 105;
	private TextView mTextSizeTV, mBackgroundColorPickerTV, mTextColorPickerTV, mFrameRateTV, mElementCountTV, mDataTypeTV, mFontsTV;
	private CheckBox mRandomColorCB, mRandomTextSizeCB;
	private ListView mListView;
	private Settings mSettings;
	// textsizesettings
	private SeekBar mTextSettingsMinSeekBar, mTextSettingsMaxSeekBar, mSeekBar;
	private TextView mTextValueMin, mTextValueMax, mTextSettingsMin, mSeekValue;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.settings);
		mSettings = Settings.getInstance();
	}

	@Override
	protected void onResume() {
		super.onResume();
		WLog.d(TAG, " onResume() ");
		initUI();
	}

	private void initUI() {

		mFontsTV = (TextView) findViewById(R.id.fonts_tv);
		mFontsTV.setOnClickListener(this);

		mDataTypeTV = (TextView) findViewById(R.id.type_of_data_tv);
		mDataTypeTV.setOnClickListener(this);

		mRandomColorCB = (CheckBox) findViewById(R.id.random_color_checkbox);
		mRandomColorCB.setChecked(mSettings.isRandomColor());
		mRandomColorCB.setOnCheckedChangeListener(this);

		mRandomTextSizeCB = (CheckBox) findViewById(R.id.random_textsize_checkbox);
		mRandomTextSizeCB.setChecked(mSettings.isRandomTextSize());
		mRandomTextSizeCB.setOnCheckedChangeListener(this);

		mTextSizeTV = (TextView) findViewById(R.id.textsize_tv);
		mTextSizeTV.setOnClickListener(this);

		mBackgroundColorPickerTV = (TextView) findViewById(R.id.background_color_picker_tv);
		mBackgroundColorPickerTV.setOnClickListener(this);

		mFrameRateTV = (TextView) findViewById(R.id.frame_rate_tv);
		mFrameRateTV.setOnClickListener(this);

		mElementCountTV = (TextView) findViewById(R.id.element_count_tv);
		mElementCountTV.setOnClickListener(this);

		mTextColorPickerTV = (TextView) findViewById(R.id.text_color_picker_tv);
		mTextColorPickerTV.setOnClickListener(this);
		mTextColorPickerTV.setEnabled(!mSettings.isRandomColor());
	}

	@Override
	public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
		switch (buttonView.getId()) {
		case R.id.random_color_checkbox:
			mSettings.setRandomColor(isChecked);
			mTextColorPickerTV.setEnabled(!mSettings.isRandomColor());
			break;
		case R.id.random_textsize_checkbox:
			mSettings.setRandomTextSize(isChecked);
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
			ColorPickerDialog mBackgroundColorDialog = new ColorPickerDialog(this, mSettings.getCustomBackgroundColor());
			mBackgroundColorDialog.setOnColorChangedListener(this);
			mBackgroundColorDialog.setId(WConstants.BACKGROUNG_COLOR_DIALOG);
			mBackgroundColorDialog.setAlphaSliderVisible(true);
			mBackgroundColorDialog.show();

			break;
		case R.id.text_color_picker_tv:
			ColorPickerDialog mTextColorDialog = new ColorPickerDialog(this, mSettings.getCustomTextColor());
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
		case R.id.fonts_tv:
			showDialog(FONTS_DIALOG);
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
			View viewDataType = (LinearLayout) getLayoutInflater().inflate(R.layout.list_layout, null);
			adb.setView(viewDataType);
			mListView = (ListView) viewDataType.findViewById(R.id.custom_list);
			ArrayAdapter<String> adapterDataTypes = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_single_choice, getResources().getStringArray(R.array.data_type_values));
			mListView.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
			mListView.setAdapter(adapterDataTypes);
			return adb.create();
		case FONTS_DIALOG:
			WLog.i(TAG, "FONTS_DIALOG");
			adb.setTitle(getString(R.string.fonts_menu));
			View viewFont = (LinearLayout) getLayoutInflater().inflate(R.layout.list_layout, null);
			adb.setView(viewFont);
			mListView = (ListView) viewFont.findViewById(R.id.custom_list);
			
			return adb.create();

		default:
			break;
		}
		return null;
	}

	private class CustomAdapter extends ArrayAdapter<String> {
		private LayoutInflater mInflater;
		private String[] mValues;

		public CustomAdapter(Context context, int textViewResourceId, String[] objects) {
			super(context, textViewResourceId, objects);
			mInflater = getLayoutInflater();
			mValues = objects;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			CheckedTextView ctv;
			if (convertView == null) {
				View view = mInflater.inflate(android.R.layout.simple_list_item_single_choice, null);
				ctv = (CheckedTextView) view.findViewById(android.R.id.text1);
				convertView = view;
			} else
				ctv = (CheckedTextView) convertView.findViewById(android.R.id.text1);
			Typeface tmpTypeFace;
			switch (position) {
			case 0:
				tmpTypeFace = Typeface.DEFAULT;
				break;
			case 1:
				tmpTypeFace = WallController.getInstance().getCustomTypeface("spectrumsmudged.ttf");
				break;
			case 2:
				tmpTypeFace = WallController.getInstance().getCustomTypeface("merchant.ttf");
				break;
			case 3:
				tmpTypeFace = WallController.getInstance().getCustomTypeface("codeman.ttf");
				break;
			case 4:
				tmpTypeFace = WallController.getInstance().getCustomTypeface("5X5basic.ttf");
				break;
			case 5:
				tmpTypeFace = WallController.getInstance().getCustomTypeface("advocut.ttf");
				break;
			case 6:
				tmpTypeFace = WallController.getInstance().getCustomTypeface("arcade.ttf");
				break;
			case 7:
				tmpTypeFace = WallController.getInstance().getCustomTypeface("xspace.ttf");
				break;

			default:
				tmpTypeFace = Typeface.DEFAULT;
				break;
			}
			ctv.setTypeface(tmpTypeFace);
			ctv.setHeight((int)getResources().getDimension(R.dimen.settings_main_item_height));
			ctv.setText(mValues[position]);
			return convertView;
		}

	}

	@Override
	protected void onPrepareDialog(int id, final Dialog dialog) {

		switch (id) {
		case TEXTSIZE_DIALOG:
			dialog.setOnDismissListener(new OnDismissListener() {

				@Override
				public void onDismiss(DialogInterface dialog) {
					mSettings.setMaxTextSize(mTextSettingsMaxSeekBar.getProgress());
					mSettings.setMinTextSize(mTextSettingsMinSeekBar.getProgress());
				}
			});
			if (mSettings.isRandomTextSize()) {
				dialog.findViewById(R.id.min_val_layout).setVisibility(View.VISIBLE);

				mTextSettingsMaxSeekBar.setProgress((int) mSettings.getMaxTextSize());
				mTextSettingsMinSeekBar.setProgress((int) mSettings.getMinTextSize());
				mTextValueMin.setText(String.valueOf((int) mSettings.getMinTextSize()));
				mTextValueMax.setText(String.valueOf((int) mSettings.getMaxTextSize()));
				mTextSettingsMin.setText(getString(R.string.textsize_min));
			} else {
				dialog.findViewById(R.id.min_val_layout).setVisibility(View.GONE);
				mTextSettingsMin.setText(getString(R.string.textsize));
				mTextSettingsMinSeekBar.setProgress((int) mSettings.getMinTextSize());
				mTextValueMin.setText(String.valueOf((int) mSettings.getMinTextSize()));
			}

			break;
		case FRAMERATE_DIALOG:
			mSeekBar.setTag(FRAMERATE_DIALOG);
			mSeekBar.setProgress(mSettings.getFrameRate());
			mSeekValue.setText(String.valueOf(mSettings.getFrameRate()));
			break;
		case ELEMENT_COUNT:
			mSeekBar.setTag(ELEMENT_COUNT);
			mSeekBar.setProgress(mSettings.getElementsPerFrame());
			mSeekValue.setText(String.valueOf(mSettings.getElementsPerFrame()));
			break;
		case DATA_TYPE_DIALOG:
			mListView.setItemChecked(mSettings.getDataTypeValue(), true);
			mListView.setOnItemClickListener(new OnItemClickListener() {

				@Override
				public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
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
					mSettings.setDataType(position);
					dialog.cancel();
				}
			});
			break;
		case FONTS_DIALOG:
			ArrayAdapter<String> adapterFonts = new CustomAdapter(this, android.R.layout.simple_list_item_single_choice, getResources().getStringArray(R.array.fonts_values));
			mListView.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
			mListView.setAdapter(adapterFonts);
			mListView.setItemChecked(mSettings.getFontValue(), true);
			mListView.setOnItemClickListener(new OnItemClickListener() {

				@Override
				public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
					switch (position) {
					case 0:
						mSettings.setFont(null);
						break;
					case 1:
						mSettings.setFont("spectrumsmudged.ttf");
						break;
					case 2:
						mSettings.setFont("merchant.ttf");
						break;
					case 3:
						mSettings.setFont("codeman.ttf");
						break;
					case 4:
						mSettings.setFont("5X5basic.ttf");
						break;
					case 5:
						mSettings.setFont("advocut.ttf");
						break;
					case 6:
						mSettings.setFont("arcade.ttf");
						break;
					case 7:
						mSettings.setFont("xspace.ttf");
						break;

					default:
						break;
					}
					WallController.getInstance().setCurrentTypeFace();
					mSettings.setFontValue(position);
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
		WLog.e(TAG, "" + requestCode + " , " + resultCode + " , ");
		switch (requestCode) {
		case DATA_TYPE_REQUEST_CODE:
			if (resultCode == RESULT_OK) {
				// The URI of the selected file
				final Uri uri = data.getData();
				// Create a File from this Uri
				File file = FileUtils.getFile(uri);
				mSettings.setExternalFilePath(file.getAbsolutePath());
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
				// if(mSettings.isRandomTextSize())
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
				mSettings.setFrameRate(seekBar.getProgress());
			} else if (seekBar.getTag().equals(ELEMENT_COUNT)) {
				mSettings.setElementsPerFrame(seekBar.getProgress());
			}
		}

	}

	@Override
	public void onColorChanged(int id, int color) {
		switch (id) {
		case WConstants.BACKGROUNG_COLOR_DIALOG:
			mSettings.setCustomBackgroundColor(color);
			break;
		case WConstants.TEXT_COLOR_DIALOG:
			mSettings.setCustomTextColor(color);
			break;
		}
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		WallController.getInstance().onOptionsChanged();
	}

}
