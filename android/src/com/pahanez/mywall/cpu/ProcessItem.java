package com.pahanez.mywall.cpu;

public class ProcessItem {
	private String 	processPID;
	private String 	processName;
	private String	processCPUusage;
	public String getProcessPID() {
		return processPID;
	}
	public void setProcessPID(String processPID) {
		this.processPID = processPID;
	}
	public String getProcessName() {
		return processName;
	}
	public void setProcessName(String processName) {
		this.processName = processName;
	}
	public String getProcessCPUusage() {
		return processCPUusage;
	}
	public void setProcessCPUusage(String processCPUusage) {
		this.processCPUusage = processCPUusage;
	}


}
