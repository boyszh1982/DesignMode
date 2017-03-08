package com.nameless.base.factory;

public class Customer {
 
	public Car buy(String brand) {
		return CarFactory.produce(brand);
	}
	
	public static void main(String[] args) {
		
		Customer c = new Customer();
		Car car = null;
		car = c.buy("Buick");
		System.out.println(car.getClass());
		car = c.buy("Cadillac");
		System.out.println(car.getClass());
		car = c.buy("Chrysler");
		System.out.println(car.getClass());
		car = c.buy("Ford");
		System.out.println(car.getClass());
	}
	 
}
 
