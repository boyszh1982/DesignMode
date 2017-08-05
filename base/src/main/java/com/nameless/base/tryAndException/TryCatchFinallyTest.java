package com.nameless.base.tryAndException;

public class TryCatchFinallyTest {

	public static void main(String[] args) {
		//finally 永远会被执行
		
		int a = 0;
		
		try {
			a = 1/0;
		} 
		catch(ArithmeticException e) {
			e.printStackTrace();
			a = -1 ;
		}
		finally {
			a = 100;
		}
		System.out.println(a);
		
		try {
			a = 1/1;
		} 
		catch(ArithmeticException e) {
			e.printStackTrace();
			a = -1 ;
		}
		finally {
			a = 100;
		}
		System.out.println(a);
	}
}
