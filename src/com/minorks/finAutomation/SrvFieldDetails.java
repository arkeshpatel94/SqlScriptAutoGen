package com.minorks.finAutomation;

import java.util.ArrayList;

public class SrvFieldDetails {
	
	private String finFuncCode;
	private String finOpr;
	private String fetchSrvName;
	private String submitSrvName;
	private ArrayList<?> fieldInfoArrList;
	
	public String getFinFuncCode() {
		return finFuncCode;
	}
	public void setFinFuncCode(String finFuncCode) {
		this.finFuncCode = finFuncCode;
	}
	public String getFinOpr() {
		return finOpr;
	}
	public void setFinOpr(String finOpr) {
		this.finOpr = finOpr;
	}
	public String getFetchSrvName() {
		return fetchSrvName;
	}
	public void setFetchSrvName(String fetchSrvName) {
		this.fetchSrvName = fetchSrvName;
	}
	public String getSubmitSrvName() {
		return submitSrvName;
	}
	public void setSubmitSrvName(String submitSrvName) {
		this.submitSrvName = submitSrvName;
	}
	public ArrayList<?> getFieldInfoArrList() {
		return fieldInfoArrList;
	}
	public void setFieldInfoArrList(ArrayList<?> fieldInfoArrList) {
		this.fieldInfoArrList = fieldInfoArrList;
	}
	
	

}
