package com.wirelessuda.activity;

import android.app.Application;

public class CardApplication extends Application {
	private String account="";			//’À∫≈
	private String username = ""; 		//—ß∫≈
	private String loginFlag="";		//µ«¬º±Í÷æ
	private String balance="";			//”‡∂Ó
	private String flag="";				//’À∫≈◊¥Ã¨±Í÷æ
	private String preTmpBalance="";	//…œ¥Œπ˝∂…”‡∂Ó
	private String tmpBalance="";		//π˝∂…”‡∂Ó
	
	public String getAccount() {
		return account;
	}
	public void setAccount(String account) {
		this.account = account;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getLoginFlag() {
		return loginFlag;
	}
	public void setLoginFlag(String loginFlag) {
		this.loginFlag = loginFlag;
	}
	public String getBalance() {
		return balance;
	}
	public void setBalance(String balance) {
		this.balance = balance;
	}
	public String getFlag() {
		return flag;
	}
	public void setFlag(String flag) {
		this.flag = flag;
	}
	public String getPreTmpBalance() {
		return preTmpBalance;
	}
	public void setPreTmpBalance(String preTmpBalance) {
		this.preTmpBalance = preTmpBalance;
	}
	public String getTmpBalance() {
		return tmpBalance;
	}
	public void setTmpBalance(String tmpBalance) {
		this.tmpBalance = tmpBalance;
	}
	
}
