package com.nameless.zk.curator.locker.SharedReentrantLock.mytest;

/**
 * 商品库存资源
 * @author Administrator
 *
 */
public class GoodsStoreResource {

	//库存10000件
	private int cnt = 0;
	
	public GoodsStoreResource(int storenum) {
		this.cnt = storenum;
	}
	
	//减库存方法,返回剩余库存。
	public int saleone(){
		return cnt--;
	}
	
	//获取库存
	public int getStore(){
		return cnt;
	}
}
