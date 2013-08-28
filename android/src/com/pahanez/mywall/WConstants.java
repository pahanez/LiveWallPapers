package com.pahanez.mywall;

public interface WConstants {
	final int DEFAULT_BACKGROUND_COLOR = 0xFF000000;
	final int BACKGROUNG_COLOR_DIALOG = 101; 
	final int TEXT_COLOR_DIALOG = 102;
	
	
	// handler constants
	final int CPU_LOOP = 102;
	final int GENERATE_FONTS = 103;
	final int CPU_DELAY = 2000;
	final int RANDOMIZER_DELAY = 20000;
	// ~handler constants
	
	// string constants
	final String COLORS_VALUE = "color";
	final String FONTS_FOLDER = "fonts/";
	final String [] FONT_FILES = {"spectrumsmudged.ttf","merchant.ttf","codeman.ttf","5X5basic.ttf","advocut.ttf","arcade.ttf","xspace.ttf"};
	
	final int MAX_FRAMERATE 			= 30;
	final int MAX_ELEMENTS_PER_FRAME 	= 50;
	
	final int CPU_TYPE  = 0;
	final int TIME_TYPE = 1;
	final int FILE_TYPE = 2;
}
