package com.nameless.base.enumt;

/**
 * 
 * 写一个开关程序，当执行了关闭方法，返回状态开；执行力开方法，返回状态关；
 * 1、开关只有两个值，开和关。
 * @author Administrator
 *
 */
public abstract class WhenNoEnumWeCanToItLikeThis {
	
	public static void main(String[] args) {
		
		Controller ctrl = Controller.ON;
		Controller ctrl_1 = ctrl.touch();
		Controller ctrl_2 = ctrl_1.touch();
		System.out.println(ctrl_1.touch());
		System.out.println(ctrl_2.touch());
	}
}

