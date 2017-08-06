package com.nameless.zk.curator;

import org.apache.curator.framework.CuratorFramework;

public class ZNodeParams {

	public String getZnode() {
		return znode;
	}
	public String getZnode(String root){
		return String.format("%s%s",root,znode);
	}
	public void setZnode(String znode) {
		this.znode = znode;
	}
	public byte[] getZdata() {
		return zdata.getBytes();
	}
	public void setZdata(byte[] zdata) {
		this.zdata = new String(zdata);
	}
	public void setZdata(String zdata) {
		this.zdata = zdata;
	}
	private String znode;
	private String zdata;
	private CuratorFramework client;
	public CuratorFramework getClient() {
		return client;
	}
	public void setClient(CuratorFramework client) {
		this.client = client;
	}
	
}
