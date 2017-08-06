package com.nameless.zk.curator;

public class CuratorNoParamsException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6017237195609885536L;

	public CuratorNoParamsException() {
		super(String.format("ERROR_CODE=%s MSG=%s", 
				ExceptionCodeEnum.CuratorNoParamsException_NoParams.getErrCode(),
				ExceptionCodeEnum.CuratorNoParamsException_NoParams.getErrmsg()
				));
	}

}
