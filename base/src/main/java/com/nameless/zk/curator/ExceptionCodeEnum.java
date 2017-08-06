package com.nameless.zk.curator;

public enum ExceptionCodeEnum {

	CuratorNoParamsException_NoParams("E0000001","public abstract void ready(String connectString, int baseSleepTimeMs, int maxRetries) 没有呗实现。");
	
	private String errCode = null ;
	private String errmsg = null ;
	
	ExceptionCodeEnum(String errCode , String errMsg){
		this.errCode = errCode ;
		this.errmsg = errMsg ;
	}
	
	public String getErrCode() {
		return errCode;
	}
	
	public String getErrmsg() {
		return errmsg;
	}
}
