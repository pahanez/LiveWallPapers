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
	private List<ProcessItem> mProcessList;
	// Overall statistics

	// Process wise statistics


	// **********constant section***********//
	private static final int BUFFER_SIZE 		= 	16000;
	// **********public interface***********//
	public static final synchronized WPCPU getInstance() {
		return (mInstance == null ? mInstance = new WPCPU() : mInstance);
	}

	/**
	 * Parse data from unix top command. Duration almost 1.5 sec.
	 * 
	 * @return List of <WPProcessItems>.
	 * */
	public List<ProcessItem> getRawTopData() {
		BufferedReader in = null;
		mProcessList = new ArrayList<ProcessItem>();
		try {
			Process process = null;
			process = Runtime.getRuntime().exec("top -n 1 -d 1");

			in = new BufferedReader(new InputStreamReader(process.getInputStream()),BUFFER_SIZE);

			String line = "";
			int i = 0;
			while ((line = in.readLine()) != null) {
				if(i > 6){ // skip lines which we needn't
				String[] tmpStr = line.split(" ");
					for(int ik = 0; ik < tmpStr.length; ik++)
						if(tmpStr[ik].contains("%")){
					ProcessItem item = new ProcessItem();
					item.setProcessPID(line.substring(0, 5).trim());
					item.setProcessCPUusage(tmpStr[ik]);
					item.setProcessName(tmpStr[tmpStr.length - 1]);
					mProcessList.add(item);
						}
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
