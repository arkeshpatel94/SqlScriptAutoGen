package com.minorks.finAutomation;

public class FieldDetails {
	
	private String idName;
	private String fldName;
	private String dataType;
	private String length;
	private String isKeyfld;
	private String isCustomfld;
	private String pageName;
	// Front-end fields
	private String literalName;
	private String mandatory;
	private String fieldSize;
	private String maxLength;
	private String readOnly;
	private String defaultValue;
	private String fieldType;
	private String fieldTypeValues;
	private String validationType;
	// New columns for cd_atf_functions helper utilization
	private String onBlurFunction;      // col 16: custom onBlur handler name
	private String searcherConfig;      // col 17: searcher config for type b/c
	private String customValidation;    // col 18: custom validation function name
	private String htmlAttributes;      // col 19: extra HTML attributes
	private String sectionName;         // col 20: section/group name for sub-headers
	private String layoutPosition;      // col 21: L/R/FULL for side-by-side layout
	// For value:label dropdown/radio support (derived from fieldTypeValues parsing)
	private String fieldTypeLabels;     // display labels for d/f (e.g., "Cash,Transfer")
	private String fieldTypeValueCodes; // submit values for d/f (e.g., "C,T")

	public String getIdName() {
		return idName;
	}
	public void setIdName(String idName) {
		this.idName = idName;
	}
	public String getFldName() {
		return fldName;
	}
	public void setFldName(String fldName) {
		this.fldName = fldName;
	}
	public String getDataType() {
		return dataType;
	}
	public void setDataType(String dataType) {
		this.dataType = dataType;
	}
	public String getLength() {
		return length;
	}
	public void setLength(String length) {
		this.length = length;
	}
	public String getIsKeyfld() {
		return isKeyfld;
	}
	public void setIsKeyfld(String isKeyfld) {
		this.isKeyfld = isKeyfld;
	}
	public String getIsCustomfld() {
		return isCustomfld;
	}
	public void setIsCustomfld(String isCustomfld) {
		this.isCustomfld = isCustomfld;
	}
	public String getPageName() {
		return pageName;
	}
	public void setPageName(String pageName) {
		this.pageName = pageName;
	}
	public String getLiteralName() {
		return literalName;
	}
	public void setLiteralName(String literalName) {
		this.literalName = literalName;
	}
	public String getMandatory() {
		return mandatory;
	}
	public void setMandatory(String mandatory) {
		this.mandatory = mandatory;
	}
	public String getFieldSize() {
		return fieldSize;
	}
	public void setFieldSize(String fieldSize) {
		this.fieldSize = fieldSize;
	}
	public String getMaxLength() {
		return maxLength;
	}
	public void setMaxLength(String maxLength) {
		this.maxLength = maxLength;
	}
	public String getReadOnly() {
		return readOnly;
	}
	public void setReadOnly(String readOnly) {
		this.readOnly = readOnly;
	}
	public String getDefaultValue() {
		return defaultValue;
	}
	public void setDefaultValue(String defaultValue) {
		this.defaultValue = defaultValue;
	}
	public String getFieldType() {
		return fieldType;
	}
	public void setFieldType(String fieldType) {
		this.fieldType = fieldType;
	}
	public String getFieldTypeValues() {
		return fieldTypeValues;
	}
	public void setFieldTypeValues(String fieldTypeValues) {
		this.fieldTypeValues = fieldTypeValues;
	}
	public String getValidationType() {
		return validationType;
	}
	public void setValidationType(String validationType) {
		this.validationType = validationType;
	}
	public String getOnBlurFunction() {
		return onBlurFunction;
	}
	public void setOnBlurFunction(String onBlurFunction) {
		this.onBlurFunction = onBlurFunction;
	}
	public String getSearcherConfig() {
		return searcherConfig;
	}
	public void setSearcherConfig(String searcherConfig) {
		this.searcherConfig = searcherConfig;
	}
	public String getCustomValidation() {
		return customValidation;
	}
	public void setCustomValidation(String customValidation) {
		this.customValidation = customValidation;
	}
	public String getHtmlAttributes() {
		return htmlAttributes;
	}
	public void setHtmlAttributes(String htmlAttributes) {
		this.htmlAttributes = htmlAttributes;
	}
	public String getSectionName() {
		return sectionName;
	}
	public void setSectionName(String sectionName) {
		this.sectionName = sectionName;
	}
	public String getLayoutPosition() {
		return layoutPosition;
	}
	public void setLayoutPosition(String layoutPosition) {
		this.layoutPosition = layoutPosition;
	}
	public String getFieldTypeLabels() {
		return fieldTypeLabels;
	}
	public void setFieldTypeLabels(String fieldTypeLabels) {
		this.fieldTypeLabels = fieldTypeLabels;
	}
	public String getFieldTypeValueCodes() {
		return fieldTypeValueCodes;
	}
	public void setFieldTypeValueCodes(String fieldTypeValueCodes) {
		this.fieldTypeValueCodes = fieldTypeValueCodes;
	}
}
