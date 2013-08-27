package com.pahanez.mywall.gdx;

import java.util.Random;
import static com.badlogic.gdx.scenes.scene2d.actions.Actions.*;

import com.badlogic.gdx.scenes.scene2d.Action;
	
public class CustomActions {
	private static Random mRandom = new Random();
	private static final int ACTION_TYPE_COUNT = 9;
	
	
	public static final Action getRandomAction() {
		Action action;
		switch (mRandom.nextInt(ACTION_TYPE_COUNT)) {
		case 1:
			action = sequence(alpha(0.3F,.01F),alpha(1.0F,.01F),alpha(0.3F,.01F),alpha(1.0F,.01F),alpha(0.3F,.01F),alpha(1.0F,.01F),alpha(0.3F,.01F),alpha(1.0F,.01F),alpha(0.6F,.01F), fadeOut(mRandom.nextFloat()*5.0F));
			break;
		default:
			action = fadeOut(mRandom.nextFloat()*5.0F);
			break;
		}
		return action;
	}

}
