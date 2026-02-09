package com.minorks.finAutomation;

import java.util.ArrayList;

public class CustomizationDet {
	private String tableName;
	private String synonymName;
	private String isModTableReqd;
	private String isCustomMenu;
	private String repName;
	private String className;
	private String fetchScrName;
	private String submitScrName;
	// Front-end fields
	private String menuName;
	private String isFuncCodePresent;
	private String pageType;

	private ArrayList<?> fieldInfoArrList;
	
	public String getTableName() {
		return tableName;
	}
	public void setTableName(String tableName) {
		this.tableName = tableName;
	}
	public String getSynonymName() {
		return synonymName;
	}
	public void setSynonymName(String synonymName) {
		this.synonymName = synonymName;
	}
	public String getIsModTableReqd() {
		return isModTableReqd;
	}
	public void setIsModTableReqd(String isModTableReqd) {
		this.isModTableReqd = isModTableReqd;
	}
	public String getIsCustomMenu() {
		return isCustomMenu;
	}
	public void setIsCustomMenu(String isCustomMenu) {
		this.isCustomMenu = isCustomMenu;
	}
	public String getRepName() {
		return repName;
	}
	public void setRepName(String repName) {
		this.repName = repName;
	}
	public String getClassName() {
		return className;
	}
	public void setClassName(String className) {
		this.className = className;
	}
	public String getFetchScrName() {
		return fetchScrName;
	}
	public void setFetchScrName(String fetchScrName) {
		this.fetchScrName = fetchScrName;
	}
	public String getSubmitScrName() {
		return submitScrName;
	}
	public void setSubmitScrName(String submitScrName) {
		this.submitScrName = submitScrName;
	}
	public ArrayList<?> getFieldInfoArrList() {
		return fieldInfoArrList;
	}
	public void setFieldInfoArrList(ArrayList<?> fieldInfoArrList) {
		this.fieldInfoArrList = fieldInfoArrList;
	}
	public String getMenuName() {
		return menuName;
	}
	public void setMenuName(String menuName) {
		this.menuName = menuName;
	}
	public String getIsFuncCodePresent() {
		return isFuncCodePresent;
	}
	public void setIsFuncCodePresent(String isFuncCodePresent) {
		this.isFuncCodePresent = isFuncCodePresent;
	}
	public String getPageType() {
		return pageType;
	}
	public void setPageType(String pageType) {
		this.pageType = pageType;
	}
}
