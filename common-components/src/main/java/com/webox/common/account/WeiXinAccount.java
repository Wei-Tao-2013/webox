package com.webox.common.account;

public class WeiXinAccount extends Account {

	public WeiXinAccount() {
		this.accountRegister = new WeiXinAccountRegister();
	}

	public void testLogin() {
		System.out.println("test login");

	}

}