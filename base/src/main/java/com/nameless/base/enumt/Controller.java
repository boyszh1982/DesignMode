package com.nameless.base.enumt;

/**
 * 这个累比在touch方法中使用判断来的更优美些。
 * @author Administrator
 *
 */
public abstract class Controller {

	private Controller(){}
	
	//只有两个控制器，开控制器，关控制器
	//关控制器
	public static final Controller OFF = new Controller(){
		@Override
		public Controller touch() {
			// touch 之后返回 ON的控制器，以备下次触碰。
			return ON;
		}

		@Override
		public String toString() {
			// TODO Auto-generated method stub
			return "ON";
		}
	};
	
	//只有两个控制器，开控制器，关控制器
	//开控制器
	public static final Controller ON = new Controller(){
		@Override
		public Controller touch() {
			// touch 之后返回 OFF的控制器，以备下次触碰。
			return OFF;
		}

		@Override
		public String toString() {
			// TODO Auto-generated method stub
			return "OFF";
		}
	};
	
	//触碰按钮方法
	public abstract Controller touch();
	
	public abstract String toString();
	
}
