package com.nameless.base.factory;

public final class CarFactory {

	private CarFactory(){}
	
	public static Car produce(String brand) {
		// String.format("produce a %s car", brand);
		Car car = null;
		if (brand.equals("Buick")) {
			car = new Buick();
		} else if (brand.equals("Cadillac")) {
			car = new Cadillac();
		} else if (brand.equals("Chrysler")) {
			car = new Chrysler();
		} else if (brand.equals("Ford")) {
			car = new Ford();
		} else {
			car = null;
		}
		return car;
	}

}
