package com.nameless.zk.curator;

public class CuratorParams {

	public String getConnectString() {
		return connectString;
	}
	public void setConnectString(String connectString) {
		this.connectString = connectString;
	}
	public int getBaseSleepTimeMs() {
		return baseSleepTimeMs;
	}
	public void setBaseSleepTimeMs(int baseSleepTimeMs) {
		this.baseSleepTimeMs = baseSleepTimeMs;
	}
	public int getMaxRetries() {
		return maxRetries;
	}
	public void setMaxRetries(int maxRetries) {
		this.maxRetries = maxRetries;
	}
	private String connectString = null ;
	private int baseSleepTimeMs = 0 ;
	private int maxRetries = 0 ;
	
}
