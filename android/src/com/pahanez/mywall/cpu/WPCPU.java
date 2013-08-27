package com.pahanez.mywall.cpu;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

/**
 * Singleton. Return CPU info about process.
 * 
 * @author pindziukou
 * */
public final class WPCPU {
	// **********private interface***********//
	private static WPCPU mInstance;
	private List<String> mProcessList;
	// Overall statistics

	// Process wise statistics


	// **********constant section***********//
	private static final int BUFFER_SIZE 		= 	4096;
	// **********public interface***********//
	public static final synchronized WPCPU getInstance() {
		return (mInstance == null ? mInstance = new WPCPU() : mInstance);
	}

	/**
	 * Parse data from unix top command. Duration almost 1.5 sec.
	 * 
	 * @return List of <WPProcessItems>.
	 * */
	public List<String> getRawTopData() {
		BufferedReader in = null;
		mProcessList = new ArrayList<String>();
		try {
			Process process = Runtime.getRuntime().exec("top -n 1 -d 1");

			in = new BufferedReader(new InputStreamReader(process.getInputStream()),BUFFER_SIZE);

			String line = "";
			int i = 0;
			while ((line = in.readLine()) != null) {
				if(i > 6){
					String [] tmp = line.trim().split(" ");
					String goal = null;
					for(String s:tmp){
						if(s.contains("%"))	goal = s;
					}
					mProcessList.add("[ " + tmp[0] + " | " + goal + " | " + tmp[tmp.length-1] + " ]");
				}
				i++;
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (in != null) {
				try {
					in.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return mProcessList;
	}
	
}
