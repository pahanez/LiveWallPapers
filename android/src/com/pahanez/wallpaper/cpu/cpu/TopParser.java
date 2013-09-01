package com.pahanez.wallpaper.cpu.cpu;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.TimeUnit;

import com.pahanez.wallpaper.cpu.MainExecutor;
import com.pahanez.wallpaper.cpu.MainExecutor.CallBack;
import com.pahanez.wallpaper.cpu.MainExecutor.Task;
import com.pahanez.wallpaper.cpu.R;
import com.pahanez.wallpaper.cpu.WallApplication;
import com.pahanez.wallpaper.cpu.settings.SettingsHolder;

public class TopParser {
	private static final String TOP = "top -n 1 -m ";
	private static final int DELAY = 2;
	private List<String> mData = new CopyOnWriteArrayList<String>();
	private Random mRandom = new Random();
	
	public TopParser() {
		MainExecutor.getInstance().executeWhileVisible(new Parser(), new TopResults());
	}


	class Parser implements Task<ArrayList<String>> {

		@Override
		public ArrayList<String> fullfill() {
			try {
				ArrayList<String> process = new ArrayList<String>();
				String command = TOP + SettingsHolder.mProcessCount;
				Process p = Runtime.getRuntime().exec(command);
				BufferedReader br = new BufferedReader(new InputStreamReader(p.getInputStream()));
				String str;
				int i = 0;
				while ((str = br.readLine()) != null) {
					if(i++<=5)	continue;
					 
					String[] tmp = str.trim().split(" ");
					String goal = null;
					for (String s : tmp) {
						if (s.contains("%"))
							goal = s; 
					}
					if (!str.trim().isEmpty())
						process.add("* " + tmp[0] + " : " + goal + " : " + tmp[tmp.length - 1] + " *");
				}
				return process;
			} catch (IOException e) {
				e.printStackTrace();
			}
			return null;
		}
	}

	class TopResults implements CallBack<ArrayList<String>> {

		@Override
		public void fullfilled(ArrayList<String> o) {
			mData.clear();
			mData.addAll(o);
			try {
				TimeUnit.SECONDS.sleep(DELAY);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			MainExecutor.getInstance().executeWhileVisible(new Parser(), new TopResults());
		}
	}
	
	public String getRandomTopElement(){
		if (mData == null || mData.size() == 0) 	return WallApplication.getContext().getString(R.string.cpu_loading);
		
		return mData.get(mRandom.nextInt(mData.size()));
	}
}
