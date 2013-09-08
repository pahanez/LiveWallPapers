package com.pahanez.wallpaper.cpu.settings;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
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

import com.pahanez.wallpaper.cpu.MainExecutor;
import com.pahanez.wallpaper.cpu.MainExecutor.CallBack;
import com.pahanez.wallpaper.cpu.MainExecutor.Task;
import com.pahanez.wallpaper.cpu.R;
import com.pahanez.wallpaper.cpu.WConstants;
import com.pahanez.wallpaper.cpu.utils.ColorPickerDialog;
import com.pahanez.wallpaper.cpu.utils.ColorPickerDialog.OnColorChangedListener;
import com.pahanez.wallpaper.cpu.utils.Util;
import com.pahanez.wallpaper.cpu.utils.WLog;

public class WallPaperSettings extends Activity implements OnCheckedChangeListener, OnClickListener, OnSeekBarChangeListener, OnColorChangedListener {

	private static final String TAG = WallPaperSettings.class.getSimpleName();
	private static final int PROCESS_DIALOG = 101;
	private static final int ELEMENT_COUNT = 103;
	private static final int TEXT_SIZE_DIALOG = 104;
	private static final int FONTS_DIALOG = 105;
	private TextView mTextSizeTV, mBackgroundColorPickerTV, mTextColorPickerTV, mLinesOnScreen, mProcessQty, mFontsTV;
	private CheckBox mRandomColorCB, mAnimBackCB;
	private ListView mListView;
	private Settings mSettings;
	// textsizesettings
	private SeekBar  mSeekBar;
	private TextView mSeekValue;

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
		if (SettingsHolder.fonts == null) {
			mFontsTV.setEnabled(false);
			MainExecutor.getInstance().execute(new TypefaceTask(), new TypeFaceResult());
		}

		mTextSizeTV = (TextView) findViewById(R.id.text_size_tv);
		mTextSizeTV.setOnClickListener(this);

		mRandomColorCB = (CheckBox) findViewById(R.id.random_color_checkbox);
		mRandomColorCB.setChecked(SettingsHolder.isRandomTextColor);
		mRandomColorCB.setOnCheckedChangeListener(this);

		mAnimBackCB = (CheckBox) findViewById(R.id.animated_background_checkbox);
		mAnimBackCB.setChecked(SettingsHolder.mIsAnimatedBackground);
		mAnimBackCB.setOnCheckedChangeListener(this);

		mBackgroundColorPickerTV = (TextView) findViewById(R.id.background_color_picker_tv);
		mBackgroundColorPickerTV.setOnClickListener(this);
		mBackgroundColorPickerTV.setEnabled(!SettingsHolder.mIsAnimatedBackground);

		mLinesOnScreen = (TextView) findViewById(R.id.elements_tv);
		mLinesOnScreen.setOnClickListener(this);

		mProcessQty = (TextView) findViewById(R.id.process_tv);
		mProcessQty.setOnClickListener(this);

		mTextColorPickerTV = (TextView) findViewById(R.id.text_color_picker_tv);
		mTextColorPickerTV.setOnClickListener(this);
		mTextColorPickerTV.setEnabled(!SettingsHolder.isRandomTextColor);

	}

	class TypefaceTask implements Task<Typeface[]> {

		@Override
		public Typeface[] fullfill() {

			Typeface[] typefaces = new Typeface[WConstants.FONT_FILES.length];
			for (int i = 0; i < typefaces.length; i++) {
				typefaces[i] = Util.getCustomTypeface(WConstants.FONT_FILES[i]);
			}

			return typefaces;
		}

	}

	class TypeFaceResult implements CallBack<Typeface[]> {

		@Override
		public void fullfilled(Typeface[] o) {
			SettingsHolder.fonts = o;
			mFontsTV.post(new Runnable() {

				@Override
				public void run() {
					mFontsTV.setEnabled(true);
				}
			});
		}

	}

	@Override
	public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
		switch (buttonView.getId()) {
		case R.id.random_color_checkbox:
			mSettings.setRandomTextColor(isChecked);
			mTextColorPickerTV.setEnabled(!SettingsHolder.isRandomTextColor);
			break;
		case R.id.animated_background_checkbox:
			mSettings.setAnimatedBackground(isChecked);
			mBackgroundColorPickerTV.setEnabled(!isChecked);
			break;
		default:
			break;
		}
	}

	@SuppressWarnings("deprecation")
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.text_size_tv:
			showDialog(TEXT_SIZE_DIALOG);
			break;
		case R.id.background_color_picker_tv:
			ColorPickerDialog mBackgroundColorDialog = new ColorPickerDialog(this, mSettings.getCustomBackgroundColor());
			mBackgroundColorDialog.setOnColorChangedListener(this);
			mBackgroundColorDialog.setId(WConstants.BACKGROUNG_COLOR_DIALOG);
			mBackgroundColorDialog.setTitle(getString(R.string.choose_background_color_text));
			mBackgroundColorDialog.show();

			break;
		case R.id.text_color_picker_tv:
			ColorPickerDialog mTextColorDialog = new ColorPickerDialog(this, mSettings.getCustomTextColor());
			mTextColorDialog.setOnColorChangedListener(this);
			mTextColorDialog.setId(WConstants.TEXT_COLOR_DIALOG);
			mTextColorDialog.setAlphaSliderVisible(false);
			mTextColorDialog.show();

			break;

		case R.id.elements_tv:
			showDialog(ELEMENT_COUNT);
			break;

		case R.id.process_tv:
			showDialog(PROCESS_DIALOG);
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
		case PROCESS_DIALOG:
			WLog.i(TAG, "PROCESS_DIALOG");
			adb.setTitle(getString(R.string.proces_count_tv));
			View viewProcessQty = (LinearLayout) getLayoutInflater().inflate(R.layout.settings_single_seekbar, null);
			adb.setView(viewProcessQty);
			mSeekBar = (SeekBar) viewProcessQty.findViewById(R.id.my_seekbar);
			mSeekBar.setMax(WConstants.MAX_PROCESS);
			mSeekBar.setOnSeekBarChangeListener(this);
			mSeekValue = (TextView) viewProcessQty.findViewById(R.id.val_seek_tv);
			return adb.create();
		case TEXT_SIZE_DIALOG:
			WLog.i(TAG, "TEXT_SIZE_DIALOG");
			adb.setTitle(getString(R.string.textsize_choose));
			View viewDataType = (LinearLayout) getLayoutInflater().inflate(R.layout.list_layout, null);
			adb.setView(viewDataType);
			mListView = (ListView) viewDataType.findViewById(R.id.custom_list);
			
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
			Typeface tmpTypeFace = SettingsHolder.fonts[position];
			ctv.setTypeface(tmpTypeFace);
			ctv.setHeight((int) getResources().getDimension(R.dimen.settings_main_item_height));
			ctv.setText(mValues[position]);
			ctv.setTextColor(getResources().getColor(android.R.color.white));
			return convertView;
		}
 
	}

	@SuppressWarnings("deprecation")
	@Override
	protected void onPrepareDialog(int id, final Dialog dialog) {

		switch (id) {

		case PROCESS_DIALOG:
			mSeekBar.setTag(PROCESS_DIALOG);
			mSeekBar.setProgress(mSettings.getProcessQty());
			mSeekValue.setText(String.valueOf(mSettings.getProcessQty()));
			break;
		case ELEMENT_COUNT:
			mSeekBar.setTag(ELEMENT_COUNT);
			mSeekBar.setProgress(mSettings.getElementsCount());
			mSeekValue.setText(String.valueOf(mSettings.getElementsCount()));
			break;
		case TEXT_SIZE_DIALOG:
			WLog.e("TEXT_SIZE_DIALOG");
			
			ArrayAdapter<String> adapterDataTypes = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_single_choice, getResources().getStringArray(R.array.fonts_values));
			mListView.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
			mListView.setAdapter(adapterDataTypes);
			
			mListView.setItemChecked(mSettings.getFontSize(), true);
			mListView.setOnItemClickListener(new OnItemClickListener() {

				@Override
				public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
					mSettings.setFontSize(position, ProgressDialog.show(WallPaperSettings.this, null, getString(R.string.generating_font)));
					dialog.cancel();
				}
			});
			break;
		case FONTS_DIALOG:
			ArrayAdapter<String> adapterFonts = new CustomAdapter(this, android.R.layout.simple_list_item_single_choice, getResources().getStringArray(R.array.fonts_names));
			mListView.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
			mListView.setAdapter(adapterFonts);
			mListView.setItemChecked(mSettings.getFont(), true);
			mListView.setOnItemClickListener(new OnItemClickListener() {

				@Override
				public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
					mSettings.setFont(position, ProgressDialog.show(WallPaperSettings.this, null, getString(R.string.generating_font)));
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
	public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
		switch (seekBar.getId()) {
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
			if (seekBar.getTag().equals(PROCESS_DIALOG)) {
				mSettings.setProcessQty(seekBar.getProgress());
			} else if (seekBar.getTag().equals(ELEMENT_COUNT)) {
				mSettings.setElementsCount(seekBar.getProgress());
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

}
