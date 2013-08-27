package com.pahanez.mywall.cpu;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.TimeUnit;

import com.pahanez.mywall.MainExecutor;
import com.pahanez.mywall.MainExecutor.CallBack;
import com.pahanez.mywall.MainExecutor.Task;
import com.pahanez.mywall.R;
import com.pahanez.mywall.WallApplication;
import com.pahanez.mywall.utils.WLog;

public class TopParser {
	private static final String TOP = "top -n 1 -m 100";
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
				Process p = Runtime.getRuntime().exec(TOP);
				BufferedReader br = new BufferedReader(new InputStreamReader(p.getInputStream()));
				String str;
				while ((str = br.readLine()) != null) {

					String[] tmp = str.trim().split(" ");
					String goal = null;
					for (String s : tmp) {
						if (s.contains("%"))
							goal = s;
					}
					if (!str.trim().isEmpty())
						process.add("[ " + tmp[0] + " | " + goal + " | " + tmp[tmp.length - 1] + " ]");
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
			WLog.e("" + mData.size());
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
