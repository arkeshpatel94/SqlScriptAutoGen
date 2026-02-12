package com.minorks.finAutomation;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

public class FrontEndGeneration {

	String menuName;
	String pageType;
	String isFuncCodePresent;
	String isCustomMenu;
	String repName;
	String className;
	String fetchScrName;
	String submitScrName;
	final String NL = "\n";
	final String TAB = "\t";
	final String DQ = "\"";
	int fltCounter = 900001;

	// Setters
	public void setMenuName(String menuName) { this.menuName = menuName; }
	public void setPageType(String pageType) { this.pageType = pageType; }
	public void setIsFuncCodePresent(String isFuncCodePresent) { this.isFuncCodePresent = isFuncCodePresent; }
	public void setIsCustomMenu(String isCustomMenu) { this.isCustomMenu = isCustomMenu; }
	public void setRepName(String repName) { this.repName = repName; }
	public void setClassName(String className) { this.className = className; }
	public void setFetchScrName(String fetchScrName) { this.fetchScrName = fetchScrName; }
	public void setSubmitScrName(String submitScrName) { this.submitScrName = submitScrName; }

	private String nextFLT() {
		return "FLT" + (fltCounter++);
	}

	private void writeToFile(String content, String filePath) {
		try {
			File file = new File(filePath);
			file.getParentFile().mkdirs();
			FileWriter fw = new FileWriter(filePath);
			BufferedWriter bw = new BufferedWriter(fw);
			bw.write(content);
			bw.close();
		} catch (IOException e) {
			System.out.println("Error writing to file '" + filePath + "'");
			e.printStackTrace();
		}
	}

	// Helper: format comma-separated string as JS array content: "a","b","c"
	private String formatJSArray(String commaSeparated) {
		if (commaSeparated == null || commaSeparated.isEmpty()) return "";
		String[] parts = commaSeparated.split(",");
		StringBuilder result = new StringBuilder();
		for (int i = 0; i < parts.length; i++) {
			if (i > 0) result.append(",");
			result.append("\"" + parts[i].trim() + "\"");
		}
		return result.toString();
	}

	// Helper: null-safe getter with default
	private String safe(String val, String dflt) {
		return (val != null && !val.isEmpty()) ? val : dflt;
	}

	/**
	 * Generate mandatory wrapper functions for cd_atf_functions helpers.
	 * These are emitted once at the top of each glink.js file.
	 */
	private String generateMandatoryWrappers() {
		StringBuffer sb = new StringBuffer();
		sb.append("// Mandatory indicator wrapper functions for cd_atf_functions helpers" + NL);

		// addMandatoryCustomTextField
		sb.append("function addMandatoryCustomTextField(fldLiteral, isMandatory, fldNameOrId, fldDataType, fldMaxLength, fldReadOnly, fldDfltValue, onBlurFunc, searcherFunc, optionalHTMLAttr) {" + NL);
		sb.append(TAB + "var label = fldLiteral;" + NL);
		sb.append(TAB + "if (isMandatory == \"Y\") { label = fldLiteral + '<script>setMandatory(\"Y\");<\\/script>'; }" + NL);
		sb.append(TAB + "addCustomTextField(label, fldNameOrId, fldDataType, fldMaxLength, fldReadOnly, fldDfltValue, onBlurFunc, searcherFunc, optionalHTMLAttr);" + NL);
		sb.append("}" + NL + NL);

		// addMandatoryCustomDateField
		sb.append("function addMandatoryCustomDateField(fldLiteral, isMandatory, fldName_ui, fldReadOnly, fldDfltValue, validationFunc, optionalHTMLAttr) {" + NL);
		sb.append(TAB + "var label = fldLiteral;" + NL);
		sb.append(TAB + "if (isMandatory == \"Y\") { label = fldLiteral + '<script>setMandatory(\"Y\");<\\/script>'; }" + NL);
		sb.append(TAB + "addCustomDateField(label, fldName_ui, fldReadOnly, fldDfltValue, validationFunc, optionalHTMLAttr);" + NL);
		sb.append("}" + NL + NL);

		// addMandatoryCustomDropDown
		sb.append("function addMandatoryCustomDropDown(fldLiteral, isMandatory, fldNameOrId, fldLiteralArr, fldValueArr, fldReadOnly, validationFunc, optionalHTMLAttr) {" + NL);
		sb.append(TAB + "var label = fldLiteral;" + NL);
		sb.append(TAB + "if (isMandatory == \"Y\") { label = fldLiteral + '<script>setMandatory(\"Y\");<\\/script>'; }" + NL);
		sb.append(TAB + "addCustomDropDown(label, fldNameOrId, fldLiteralArr, fldValueArr, fldReadOnly, validationFunc, optionalHTMLAttr);" + NL);
		sb.append("}" + NL + NL);

		// addMandatoryCustomRadioButton
		sb.append("function addMandatoryCustomRadioButton(fldLiteral, isMandatory, fldNameOrId, fldLiteralArr, fldValueArr, fldReadOnly, validationFunc, optionalHTMLAttr) {" + NL);
		sb.append(TAB + "var label = fldLiteral;" + NL);
		sb.append(TAB + "if (isMandatory == \"Y\") { label = fldLiteral + '<script>setMandatory(\"Y\");<\\/script>'; }" + NL);
		sb.append(TAB + "addCustomRadioButton(label, fldNameOrId, fldLiteralArr, fldValueArr, fldReadOnly, validationFunc, optionalHTMLAttr);" + NL);
		sb.append("}" + NL + NL);

		// addMandatoryCustomCheckBox
		sb.append("function addMandatoryCustomCheckBox(fldLiteral, isMandatory, fldNameOrId, fldReadOnly, fldDfltValue, validationFunc, optionalHTMLAttr) {" + NL);
		sb.append(TAB + "var label = fldLiteral;" + NL);
		sb.append(TAB + "if (isMandatory == \"Y\") { label = fldLiteral + '<script>setMandatory(\"Y\");<\\/script>'; }" + NL);
		sb.append(TAB + "addCustomCheckBox(label, fldNameOrId, fldReadOnly, fldDfltValue, validationFunc, optionalHTMLAttr);" + NL);
		sb.append("}" + NL + NL);

		return sb.toString();
	}

	/**
	 * Append the helper call for a single field based on its fieldType.
	 * After each helper call, sets the element name to include subGroupName.
	 */
	private void appendFieldHelperCall(StringBuffer sb, FieldDetails fd, String fltCode) {
		String fieldType = safe(fd.getFieldType(), "a");
		String mand = "Y".equals(fd.getMandatory()) ? "Y" : "N";
		String maxLen = safe(fd.getMaxLength(), "50");
		String readOnly = "Y".equals(fd.getReadOnly()) ? "Y" : "N";
		String defaultVal = safe(fd.getDefaultValue(), "");
		String onBlur = safe(fd.getOnBlurFunction(), "");
		String htmlAttr = safe(fd.getHtmlAttributes(), "");
		String customVal = safe(fd.getCustomValidation(), "");
		String size = safe(fd.getFieldSize(), "20");

		sb.append(NL + TAB + "//" + fd.getIdName() + NL);

		if ("a".equals(fieldType)) {
			sb.append(TAB + "addMandatoryCustomTextField(jspResArr.get(\"" + fltCode + "\"), \"" + mand + "\", \"" + fd.getIdName() + "\", \"String\", \"" + maxLen + "\", \"" + readOnly + "\", \"" + defaultVal + "\", \"" + onBlur + "\", \"\", \"" + htmlAttr + "\");" + NL);
			sb.append(TAB + "document.getElementById(\"" + fd.getIdName() + "\").name = subGroupName + \"." + fd.getIdName() + "\";" + NL);

		} else if ("b".equals(fieldType) || "c".equals(fieldType)) {
			String searcherCfg = fd.getSearcherConfig();
			String searcher;
			if (searcherCfg != null && !searcherCfg.isEmpty()) {
				searcher = searcherCfg + "(document.forms[0]." + fd.getIdName() + ")";
			} else {
				searcher = fd.getIdName() + "Searcher(document.forms[0]." + fd.getIdName() + ")";
			}
			sb.append(TAB + "addMandatoryCustomTextField(jspResArr.get(\"" + fltCode + "\"), \"" + mand + "\", \"" + fd.getIdName() + "\", \"String\", \"" + maxLen + "\", \"" + readOnly + "\", \"" + defaultVal + "\", \"" + onBlur + "\", \"" + searcher + "\", \"" + htmlAttr + "\");" + NL);
			sb.append(TAB + "document.getElementById(\"" + fd.getIdName() + "\").name = subGroupName + \"." + fd.getIdName() + "\";" + NL);

		} else if ("d".equals(fieldType)) {
			String labels = safe(fd.getFieldTypeLabels(), "");
			String values = safe(fd.getFieldTypeValueCodes(), "");
			sb.append(TAB + "addMandatoryCustomDropDown(jspResArr.get(\"" + fltCode + "\"), \"" + mand + "\", \"" + fd.getIdName() + "\", new Array(" + formatJSArray(labels) + "), new Array(" + formatJSArray(values) + "), \"" + readOnly + "\", \"" + customVal + "\", \"" + htmlAttr + "\");" + NL);
			sb.append(TAB + "document.getElementById(\"" + fd.getIdName() + "\").name = subGroupName + \"." + fd.getIdName() + "\";" + NL);

		} else if ("e".equals(fieldType)) {
			sb.append(TAB + "addMandatoryCustomDateField(jspResArr.get(\"" + fltCode + "\"), \"" + mand + "\", \"" + fd.getIdName() + "_ui\", \"" + readOnly + "\", \"" + defaultVal + "\", \"" + customVal + "\", \"" + htmlAttr + "\");" + NL);
			sb.append(TAB + "document.getElementById(\"" + fd.getIdName() + "_ui\").name = subGroupName + \"." + fd.getIdName() + "_ui\";" + NL);
			sb.append(TAB + "document.getElementById(\"" + fd.getIdName() + "\").name = subGroupName + \"." + fd.getIdName() + "\";" + NL);

		} else if ("f".equals(fieldType)) {
			String labels = safe(fd.getFieldTypeLabels(), "");
			String values = safe(fd.getFieldTypeValueCodes(), "");
			sb.append(TAB + "addMandatoryCustomRadioButton(jspResArr.get(\"" + fltCode + "\"), \"" + mand + "\", \"" + fd.getIdName() + "\", new Array(" + formatJSArray(labels) + "), new Array(" + formatJSArray(values) + "), \"" + readOnly + "\", \"" + customVal + "\", \"" + htmlAttr + "\");" + NL);
			sb.append(TAB + "document.getElementById(\"" + fd.getIdName() + "\").name = subGroupName + \"." + fd.getIdName() + "\";" + NL);

		} else if ("g".equals(fieldType)) {
			sb.append(TAB + "addMandatoryCustomCheckBox(jspResArr.get(\"" + fltCode + "\"), \"" + mand + "\", \"" + fd.getIdName() + "\", \"" + readOnly + "\", \"" + defaultVal + "\", \"" + customVal + "\", \"" + htmlAttr + "\");" + NL);
			sb.append(TAB + "document.getElementById(\"" + fd.getIdName() + "\").name = subGroupName + \"." + fd.getIdName() + "\";" + NL);

		} else if ("h".equals(fieldType)) {
			// Textarea - no cd_atf_functions helper, use raw document.write
			String mandHTML = "Y".equals(fd.getMandatory()) ? "<script>setMandatory(\\\"Y\\\");<\\/script>" : "";
			sb.append(TAB + "with (document){" + NL);
			sb.append(TAB + "write('<td class=\"textlabel\">' + jspResArr.get(\"" + fltCode + "\") + '" + mandHTML + "</td>');" + NL);
			sb.append(TAB + "write('<td>');" + NL);
			sb.append(TAB + "write('<textarea class=\"textfieldfont\" name=\"' + subGroupName + '." + fd.getIdName() + "\" id=\"" + fd.getIdName() + "\" rows=\"3\" cols=\"" + size + "\" ' + " + menuName + "Props.get(\"" + fd.getIdName() + "_ENABLED\") + '></textarea>');" + NL);
			sb.append(TAB + "write('</td>');" + NL);
			sb.append(TAB + "}" + NL);

		} else {
			// Default text input
			sb.append(TAB + "addMandatoryCustomTextField(jspResArr.get(\"" + fltCode + "\"), \"" + mand + "\", \"" + fd.getIdName() + "\", \"String\", \"" + maxLen + "\", \"" + readOnly + "\", \"" + defaultVal + "\", \"" + onBlur + "\", \"\", \"" + htmlAttr + "\");" + NL);
			sb.append(TAB + "document.getElementById(\"" + fd.getIdName() + "\").name = subGroupName + \"." + fd.getIdName() + "\";" + NL);
		}
	}

	/**
	 * Append field with layout positioning (L/R/FULL) and section grouping.
	 */
	private void appendFieldWithLayout(StringBuffer sb, ArrayList<FieldDetails> fields, int index, String fltCode) {
		FieldDetails fd = fields.get(index);
		String layout = safe(fd.getLayoutPosition(), "FULL");
		String section = fd.getSectionName();

		// Section sub-header
		if (section != null && !section.isEmpty()) {
			// Check if this is a new section
			boolean isNewSection = true;
			if (index > 0) {
				String prevSection = fields.get(index - 1).getSectionName();
				if (section.equals(prevSection)) isNewSection = false;
			}
			if (isNewSection) {
				sb.append(TAB + "addSubHeader(\"" + section + "\", 5);" + NL);
			}
		}

		if ("R".equals(layout)) {
			// R follows an L - add column spacer, field, then close row
			sb.append(TAB + "columnAlignment();" + NL);
			appendFieldHelperCall(sb, fd, fltCode);
			sb.append(TAB + "rowEnd();" + NL);
		} else {
			// L or FULL - start new row
			sb.append(TAB + "rowStart();" + NL);
			appendFieldHelperCall(sb, fd, fltCode);

			// Check if next field is R
			boolean nextIsR = false;
			if ("L".equals(layout) && index + 1 < fields.size()) {
				String nextLayout = safe(fields.get(index + 1).getLayoutPosition(), "FULL");
				if ("R".equals(nextLayout)) nextIsR = true;
			}
			if (!nextIsR) {
				sb.append(TAB + "columnAlignment();" + NL);
				sb.append(TAB + "rowEnd();" + NL);
			}
		}
	}

	/**
	 * Main entry point - generates all front-end files
	 */
	public void generateAllFrontEndFiles(ArrayList<?> fieldList, String generatePath) {
		if (isCustomMenu.equals("Y")) {
			generateCustomMenuFiles(fieldList, generatePath);
		} else {
			generateProductMenuJSP(fieldList, generatePath);
		}
	}

	/**
	 * Generate all 13 front-end files for Custom Menu
	 */
	private void generateCustomMenuFiles(ArrayList<?> fieldList, String basePath) {
		if (!basePath.endsWith(File.separator))
			basePath = basePath + File.separator;

		System.out.println("Generating Custom Menu front-end files for: " + menuName);

		generateGroupXML(fieldList, basePath);
		generatePropsJS(fieldList, basePath);
		generateCritJSP(fieldList, basePath);
		if ("3page".equals(pageType)) {
			generateDetJSP(fieldList, basePath);
		}
		generateResJSP(basePath);

		fltCounter = 900001; // Reset for crit INFENG
		generateCritINFENGJS(fieldList, basePath);
		fltCounter = 900001; // Reset for det INFENG
		if ("3page".equals(pageType)) {
			generateDetINFENGJS(fieldList, basePath);
		}
		fltCounter = 900001; // Reset for res INFENG
		generateResINFENGJS(basePath);

		generateCritGlinkJS(fieldList, basePath);
		if ("3page".equals(pageType)) {
			generateDetGlinkJS(fieldList, basePath);
		}
		generateResGlinkJS(basePath);

		generateCritLinkJS(fieldList, basePath);
		if ("3page".equals(pageType)) {
			generateDetLinkJS(fieldList, basePath);
		}

		System.out.println("Front-end file generation complete.");
	}

	// ===================== GROUP XML =====================

	private void generateGroupXML(ArrayList<?> fieldList, String basePath) {
		StringBuffer sb = new StringBuffer();
		sb.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>" + NL);
		sb.append("<group_details>" + NL);
		sb.append("  <multi_tab_menu>N</multi_tab_menu>" + NL);

		// Page list
		sb.append("  <page_list>" + NL);
		sb.append("    <page_details>" + NL);
		sb.append("      <type_of_page>INITIAL</type_of_page>" + NL);
		sb.append("      <page_name>" + menuName + "_crit.jsp</page_name>" + NL);
		sb.append("    </page_details>" + NL);
		if ("3page".equals(pageType)) {
			sb.append("    <page_details>" + NL);
			sb.append("      <type_of_page>DETAILS</type_of_page>" + NL);
			sb.append("      <page_name>" + menuName + "_det.jsp</page_name>" + NL);
			sb.append("    </page_details>" + NL);
		}
		sb.append("    <page_details>" + NL);
		sb.append("      <type_of_page>RESULT</type_of_page>" + NL);
		sb.append("      <page_name>" + menuName + "_res.jsp</page_name>" + NL);
		sb.append("    </page_details>" + NL);
		sb.append("  </page_list>" + NL);

		// Invocation list
		sb.append("  <invocation_list>" + NL);
		if ("Y".equals(isFuncCodePresent)) {
			String[] funcCodes = {"A", "V", "M", "I", "X"};
			for (String fc : funcCodes) {
				sb.append("    <invocation_details>" + NL);
				sb.append("      <action_code>GETDATA</action_code>" + NL);
				sb.append("      <function_code>" + fc + "</function_code>" + NL);
				sb.append("      <invocation_name>" + fetchScrName + "</invocation_name>" + NL);
				sb.append("    </invocation_details>" + NL);
			}
		} else {
			sb.append("    <invocation_details>" + NL);
			sb.append("      <action_code>GETDATA</action_code>" + NL);
			sb.append("      <function_code>A</function_code>" + NL);
			sb.append("      <invocation_name>" + fetchScrName + "</invocation_name>" + NL);
			sb.append("    </invocation_details>" + NL);
		}
		sb.append("    <invocation_details>" + NL);
		sb.append("      <action_code>SUBMIT</action_code>" + NL);
		sb.append("      <function_code>NA</function_code>" + NL);
		sb.append("      <invocation_name>" + submitScrName + "</invocation_name>" + NL);
		sb.append("    </invocation_details>" + NL);
		sb.append("    <invocation_details>" + NL);
		sb.append("      <action_code>VALIDATE</action_code>" + NL);
		sb.append("      <function_code>NA</function_code>" + NL);
		sb.append("      <invocation_name>" + submitScrName + "</invocation_name>" + NL);
		sb.append("    </invocation_details>" + NL);
		sb.append("  </invocation_list>" + NL);

		// Tab details
		sb.append("  <tab_details_list>" + NL);
		sb.append("    <header_fields />" + NL);
		sb.append("  </tab_details_list>" + NL);
		sb.append("  <multirec_list />" + NL);

		// Field list
		sb.append("  <field_list>" + NL);
		sb.append("    <field>" + NL);
		sb.append("      <name>funcCode</name>" + NL);
		sb.append("      <datatype>String</datatype>" + NL);
		sb.append("      <mr_field>N</mr_field>" + NL);
		sb.append("      <group_field>Y</group_field>" + NL);
		sb.append("    </field>" + NL);
		sb.append("    <field>" + NL);
		sb.append("      <name>Message</name>" + NL);
		sb.append("      <datatype>String</datatype>" + NL);
		sb.append("      <mr_field>N</mr_field>" + NL);
		sb.append("      <group_field>N</group_field>" + NL);
		sb.append("    </field>" + NL);
		for (int i = 0; i < fieldList.size(); i++) {
			FieldDetails fd = (FieldDetails) fieldList.get(i);
			sb.append("    <field>" + NL);
			sb.append("      <name>" + fd.getIdName() + "</name>" + NL);
			sb.append("      <datatype>String</datatype>" + NL);
			sb.append("      <mr_field>N</mr_field>" + NL);
			sb.append("      <group_field>Y</group_field>" + NL);
			sb.append("    </field>" + NL);
		}
		sb.append("  </field_list>" + NL);
		sb.append("  <module_name />" + NL);
		sb.append("</group_details>" + NL);

		writeToFile(sb.toString(), basePath + "GroupXML" + File.separator + menuName + ".xml");
		System.out.println("Generated: GroupXML/" + menuName + ".xml");
	}

	// ===================== PROPS JS =====================

	private void generatePropsJS(ArrayList<?> fieldList, String basePath) {
		StringBuffer sb = new StringBuffer();
		sb.append("var " + menuName + "LocObj ={" + NL);
		sb.append(TAB + "Validate_MANDATORY:\"N\", " + NL);
		sb.append(TAB + "Submit_MANDATORY:\"N\", " + NL);
		sb.append(TAB + "Ok_ENABLED:\"enabled\", " + NL);
		sb.append(TAB + "Cancel_ENABLED:\"enabled\", " + NL);
		sb.append(TAB + "Validate_ENABLED:\"enabled\", " + NL);
		sb.append(TAB + "Submit_ENABLED:\"enabled\", " + NL);
		sb.append(TAB + "Cancel_MANDATORY:\"N\", " + NL);
		sb.append(TAB + "Ok_MANDATORY:\"N\"," + NL);
		sb.append(TAB + "funcCode_MANDATORY:\"N\", " + NL);
		sb.append(TAB + "funcCode_ENABLED:\"enabled\"");

		for (int i = 0; i < fieldList.size(); i++) {
			FieldDetails fd = (FieldDetails) fieldList.get(i);
			sb.append("," + NL);
			String mand = (fd.getMandatory() != null && fd.getMandatory().equals("Y")) ? "Y" : "N";
			sb.append(TAB + fd.getIdName() + "_MANDATORY:\"" + mand + "\", " + NL);
			sb.append(TAB + fd.getIdName() + "_ENABLED:\"enabled\"");
		}
		sb.append(NL + TAB + "};" + NL);
		sb.append("var " + menuName + "Props =  new Properties(" + menuName + "LocObj);" + NL);

		writeToFile(sb.toString(), basePath + "props" + File.separator + menuName + "props.js");
		System.out.println("Generated: props/" + menuName + "props.js");
	}

	// ===================== CRIT JSP =====================

	private void generateCritJSP(ArrayList<?> fieldList, String basePath) {
		StringBuffer sb = new StringBuffer();
		sb.append("<%--" + NL);
		sb.append("//Generated by Customization WB Generator" + NL);
		sb.append("//Do not edit this file directly." + NL);
		sb.append("//Instead, modify the properties in the tool and regenerate." + NL);
		sb.append("--%>" + NL + NL);
		sb.append("<%@ page import=\"applcommon.ParseValue\" %>" + NL);
		sb.append("<%@ page import=\"com.infy.bbu.jsputil.*\"%>" + NL);
		sb.append("<%@ page import=\"java.util.Vector\"%>" + NL);
		sb.append("<%@ taglib uri=\"taglib.tld\" prefix=\"arjsp\" %>" + NL);
		sb.append("<arjsp:init groupName=\"Customize\" isEntryPoint=\"false\" />" + NL + NL);
		sb.append("<%"+ NL);
		sb.append(TAB + "String sProfileId = ProfilesManager.getProfileInSession(session);" + NL);
		sb.append(TAB + "String sSubGrpName = (String)ARJspCurr.getInput(\"subGroupName\",\"\");" + NL);
		sb.append(TAB + "String sGrpName = ARJspCurr.getCurrentGroup();" + NL);
		sb.append(TAB + "String pageName = (String)ARJspCurr.getInput(\"jspName\",\"\");" + NL);
		sb.append(TAB + "String sPopUpExceptionWindow = (String)ARJspCurr.getInput(sGrpName+\".PopUpExceptionWindow\" ,\"false\");" + NL);
		sb.append(TAB + "String sReferralMode = (String)ARJspCurr.getInput(\"refSubMode\" ,\"\");" + NL);
		sb.append(TAB + "ARJspCurr.setInput(sGrpName+\".PopUpExceptionWindow\",\"false\");" + NL + NL);
		sb.append("%>" + NL);
		sb.append("<script>" + NL + NL);
		sb.append(TAB + "var sPopUpExceptionWindow = '<%=ParseValue.checkString(sPopUpExceptionWindow)%>';" + NL);
		sb.append(TAB + "var sReferralMode = '<%=ParseValue.checkString(sReferralMode)%>';" + NL);
		sb.append(TAB + "var subGroupName = '<%=ParseValue.checkString(ARJspCurr.getInput(\"subGroupName\",\"\"))%>';" + NL);
		sb.append(TAB + "var funcCode = '<%=ParseValue.checkString(ARJspCurr.getInput(sSubGrpName+\".funcCode\",\"\"))%>';" + NL);

		// Key fields for crit page
		for (int i = 0; i < fieldList.size(); i++) {
			FieldDetails fd = (FieldDetails) fieldList.get(i);
			if ("Y".equals(fd.getIsKeyfld())) {
				sb.append(TAB + "var " + fd.getIdName() + " = '<%=ParseValue.checkString(ARJspCurr.getInput(sSubGrpName+\"." + fd.getIdName() + "\",\"\"))%>';" + NL);
			}
		}
		sb.append("</script>" + NL + NL);
		sb.append("<script language=\"javascript\" src=\"../Renderer/javascripts/lists/<%=VRPKeys.getFile(\"showCustId.js\",sProfileId)%>\" > </script>" + NL + NL);
		sb.append("<script language=\"JavaScript\">" + NL);
		sb.append("</script>" + NL + NL);
		sb.append("<script language=\"JavaScript\">" + NL);
		sb.append(TAB + "printBlock();" + NL);
		sb.append(TAB + "printFooterBlock();" + NL);
		sb.append("</script>" + NL);

		writeToFile(sb.toString(), basePath + menuName + File.separator + menuName + "_crit_ginc.jsp");
		System.out.println("Generated: " + menuName + "/" + menuName + "_crit_ginc.jsp");
	}

	// ===================== DET JSP =====================

	private void generateDetJSP(ArrayList<?> fieldList, String basePath) {
		StringBuffer sb = new StringBuffer();
		sb.append("<%--" + NL);
		sb.append("//Generated by Customization WB Generator" + NL);
		sb.append("//Do not edit this file directly." + NL);
		sb.append("//Instead, modify the properties in the tool and regenerate." + NL);
		sb.append("--%>" + NL + NL);
		sb.append("<%@ page import=\"java.util.HashMap,applcommon.ParseValue,finbranchUtil.CustomMultiRecHandler\" %>" + NL);
		sb.append("<%@ page import=\"com.infy.bbu.jsputil.*\"%>" + NL);
		sb.append("<%@ page import=\"java.util.Vector\"%>" + NL);
		sb.append("<%@ page import=\"FABCommon.SecurityInfo70\"%>" + NL);
		sb.append("<%@ taglib uri=\"taglib.tld\" prefix=\"arjsp\" %>" + NL);
		sb.append("<arjsp:init groupName=\"Customize\" isEntryPoint=\"false\" />" + NL + NL);
		sb.append("<%"+ NL);
		sb.append(TAB + "String sProfileId = ProfilesManager.getProfileInSession(session);" + NL);
		sb.append(TAB + "String sSubGrpName = (String)ARJspCurr.getInput(\"subGroupName\",\"\");" + NL);
		sb.append(TAB + "String sGrpName = ARJspCurr.getCurrentGroup();" + NL);
		sb.append(TAB + "String pageName = (String)ARJspCurr.getInput(\"jspName\",\"\");" + NL);
		sb.append(TAB + "String sPopUpExceptionWindow = (String)ARJspCurr.getInput(sGrpName+\".PopUpExceptionWindow\" ,\"false\");" + NL);
		sb.append(TAB + "String sReferralMode = (String)ARJspCurr.getInput(\"refSubMode\" ,\"\");" + NL);
		sb.append(TAB + "ARJspCurr.setInput(sGrpName+\".PopUpExceptionWindow\",\"false\");" + NL);
		sb.append(TAB + "SecurityInfo70 securityInfo = (SecurityInfo70)session.getAttribute(\"FinUserInfo\");" + NL);
		sb.append("%>" + NL);
		sb.append("<script>" + NL + NL);
		sb.append(TAB + "var sPopUpExceptionWindow = '<%=ParseValue.checkString(sPopUpExceptionWindow)%>';" + NL);
		sb.append(TAB + "var sReferralMode = '<%=ParseValue.checkString(sReferralMode)%>';" + NL);
		sb.append(TAB + "var subGroupName = '<%=ParseValue.checkString(ARJspCurr.getInput(\"subGroupName\",\"\"))%>';" + NL);
		sb.append(TAB + "var BODDate= '<%= ParseValue.checkString((securityInfo.bodDate).substring(0,10))%>';" + NL);
		sb.append(TAB + "var funcCode = '<%=ParseValue.checkString(ARJspCurr.getInput(sSubGrpName+\".funcCode\",\"\"))%>';" + NL);

		// Key fields (display only on det)
		for (int i = 0; i < fieldList.size(); i++) {
			FieldDetails fd = (FieldDetails) fieldList.get(i);
			if ("Y".equals(fd.getIsKeyfld())) {
				sb.append(TAB + "var " + fd.getIdName() + " = '<%=ParseValue.checkString(ARJspCurr.getInput(sSubGrpName+\"." + fd.getIdName() + "\",\"\"))%>';" + NL);
			}
		}
		// Data fields
		for (int i = 0; i < fieldList.size(); i++) {
			FieldDetails fd = (FieldDetails) fieldList.get(i);
			if ("N".equals(fd.getIsKeyfld())) {
				sb.append(TAB + "var " + fd.getIdName() + " = '<%=ParseValue.checkString(ARJspCurr.getInput(sSubGrpName+\"." + fd.getIdName() + "\",\"\"))%>';" + NL);
			}
		}

		sb.append(NL + "</script>" + NL);
		sb.append(TAB + "<script language=\"javascript\" src=\"../Renderer/javascripts/<%=VRPKeys.getFile(\"resource_functions.js\",sProfileId)%>\" > </script>" + NL);
		sb.append(TAB + "<script language=\"javascript\" src=\"../Renderer/custom/javascripts/custom_functions.js\"></script>" + NL);
		sb.append(TAB + "<script language=\"javascript\" src=\"../Renderer/javascripts/<%=VRPKeys.getFile(\"common_functions.js\",sProfileId)%>\"></script>" + NL);
		sb.append(NL + NL);
		sb.append("<script language=\"JavaScript\">" + NL);
		sb.append("</script>" + NL + NL);
		sb.append("<script language=\"JavaScript\">" + NL);
		sb.append(TAB + "printBlock();" + NL);
		sb.append(TAB + "printFooterBlock();" + NL);
		sb.append("</script>" + NL);

		writeToFile(sb.toString(), basePath + menuName + File.separator + menuName + "_det_ginc.jsp");
		System.out.println("Generated: " + menuName + "/" + menuName + "_det_ginc.jsp");
	}

	// ===================== RES JSP =====================

	private void generateResJSP(String basePath) {
		StringBuffer sb = new StringBuffer();
		sb.append("<%--" + NL);
		sb.append("//Generated by Customization WB Generator" + NL);
		sb.append("//Do not edit this file directly." + NL);
		sb.append("//Instead, modify the properties in the tool and regenerate." + NL);
		sb.append("--%>" + NL + NL);
		sb.append("<%@ page import=\"applcommon.ParseValue\" %>" + NL);
		sb.append("<%@ page import=\"com.infy.bbu.jsputil.*\"%>" + NL);
		sb.append("<%@ page import=\"java.util.Vector\"%>" + NL);
		sb.append("<%@ taglib uri=\"taglib.tld\" prefix=\"arjsp\" %>" + NL);
		sb.append("<arjsp:init groupName=\"Customize\" isEntryPoint=\"false\" />" + NL + NL);
		sb.append("<%"+ NL);
		sb.append(TAB + "String sProfileId = ProfilesManager.getProfileInSession(session);" + NL);
		sb.append(TAB + "String sSubGrpName = (String)ARJspCurr.getInput(\"subGroupName\",\"\");" + NL);
		sb.append("%>" + NL);
		sb.append("<script>" + NL + NL);
		sb.append(TAB + "var Message = '<%=ParseValue.checkString(ARJspCurr.getInputWithGroup(\"RESULT_MSG\",\"\"))%>';" + NL + NL);
		sb.append("</script>" + NL + NL + NL);
		sb.append("<script language=\"JavaScript\">" + NL);
		sb.append("</script>" + NL + NL);
		sb.append("<script language=\"JavaScript\">" + NL);
		sb.append(TAB + "printBlock();" + NL);
		sb.append(TAB + "printFooterBlock();" + NL);
		sb.append("</script>" + NL);

		writeToFile(sb.toString(), basePath + menuName + File.separator + menuName + "_res_ginc.jsp");
		System.out.println("Generated: " + menuName + "/" + menuName + "_res_ginc.jsp");
	}

	// ===================== CRIT INFENG JS =====================

	private void generateCritINFENGJS(ArrayList<?> fieldList, String basePath) {
		StringBuffer sb = new StringBuffer();
		sb.append("//GENERATED BY CUSTOMIZATION GENERATOR.DO NOT MODIFY" + NL + NL);
		sb.append("var jspRes={");

		String fltGo = nextFLT();
		String fltTitle = nextFLT();
		sb.append(fltGo + ":\"Go\"," + fltTitle + ":\"" + menuName.toUpperCase() + " DATA ENTRY FORM\"");

		if ("Y".equals(isFuncCodePresent)) {
			String fltFunc = nextFLT();
			sb.append("," + fltFunc + ":\"Function\"");
		}

		// Key field literals
		for (int i = 0; i < fieldList.size(); i++) {
			FieldDetails fd = (FieldDetails) fieldList.get(i);
			if ("Y".equals(fd.getIsKeyfld())) {
				String lit = (fd.getLiteralName() != null) ? fd.getLiteralName() : fd.getIdName();
				sb.append("," + nextFLT() + ":\"" + lit + "\"");
			}
		}

		sb.append("," + nextFLT() + ":\"Go\"");
		sb.append("," + nextFLT() + ":\"Clear\"");

		if ("Y".equals(isFuncCodePresent)) {
			sb.append("," + nextFLT() + ":\"--Select--\"");
			sb.append("," + nextFLT() + ":\"A-Add\"");
			sb.append("," + nextFLT() + ":\"M-Modify\"");
			sb.append("," + nextFLT() + ":\"I-Inquire\"");
			sb.append("," + nextFLT() + ":\"V-Verify\"");
			sb.append("," + nextFLT() + ":\"D-Delete\"");
			sb.append("," + nextFLT() + ":\"U-Undelete\"");
			sb.append("," + nextFLT() + ":\"X-Cancel\"");
		}

		sb.append("};" + NL);
		sb.append("var jspResALT = {" + NL + "};" + NL + NL);
		sb.append("var arrJspArr = new Array (jspRes, jspResALT);" + NL);
		sb.append("var jspResArr = new litMap(arrJspArr);" + NL + NL);
		sb.append("//GENERATED BY CUSTOMIZATION GENERATOR.DO NOT MODIFY " + NL);
		sb.append("var jspErr={};" + NL);
		sb.append("var jspErrALT = {" + NL + "};" + NL + NL);
		sb.append("var arrJspErr = new Array (jspErr, jspErrALT);" + NL);
		sb.append("var jspErrResArr = new litMap(arrJspErr);" + NL);

		writeToFile(sb.toString(), basePath + "javascripts" + File.separator + "jspjs" + File.separator + "INFENG" + File.separator + menuName + "_crit_INFENG.js");
		System.out.println("Generated: javascripts/jspjs/INFENG/" + menuName + "_crit_INFENG.js");
	}

	// ===================== DET INFENG JS =====================

	private void generateDetINFENGJS(ArrayList<?> fieldList, String basePath) {
		StringBuffer sb = new StringBuffer();
		sb.append("//GENERATED BY CUSTOMIZATION GENERATOR.DO NOT MODIFY" + NL);
		sb.append("var jspRes={" + NL);

		String fltGo = nextFLT();
		String fltTitle = nextFLT();
		sb.append(fltGo + ":\"Go\"," + NL);
		sb.append(fltTitle + ":\"" + menuName.toUpperCase() + " DATA ENTRY FORM\"," + NL);

		if ("Y".equals(isFuncCodePresent)) {
			sb.append(nextFLT() + ":\"Function\"," + NL);
		}

		// Key field literals (displayed as labels on det page)
		for (int i = 0; i < fieldList.size(); i++) {
			FieldDetails fd = (FieldDetails) fieldList.get(i);
			if ("Y".equals(fd.getIsKeyfld())) {
				String lit = (fd.getLiteralName() != null) ? fd.getLiteralName() : fd.getIdName();
				sb.append(nextFLT() + ":\"" + lit + "\"," + NL);
			}
		}

		// Data field literals
		for (int i = 0; i < fieldList.size(); i++) {
			FieldDetails fd = (FieldDetails) fieldList.get(i);
			if ("N".equals(fd.getIsKeyfld())) {
				String lit = (fd.getLiteralName() != null) ? fd.getLiteralName() : fd.getIdName();
				sb.append(nextFLT() + ":\"" + lit + "\"," + NL);
			}
		}

		sb.append(nextFLT() + ":\"Submit\"," + NL);
		sb.append(nextFLT() + ":\"Cancel\"," + NL);
		sb.append(nextFLT() + ":\"OK\"");

		if ("Y".equals(isFuncCodePresent)) {
			sb.append("," + NL);
			sb.append(nextFLT() + ":\"A - Add\"," + NL);
			sb.append(nextFLT() + ":\"M - Modify\"," + NL);
			sb.append(nextFLT() + ":\"I - Inquire\"," + NL);
			sb.append(nextFLT() + ":\"V - Verify\"," + NL);
			sb.append(nextFLT() + ":\"X - Cancel\"");
		}

		sb.append(NL + "};" + NL);
		sb.append("var jspResALT = {" + NL + "};" + NL + NL);
		sb.append("var arrJspArr = new Array (jspRes, jspResALT);" + NL);
		sb.append("var jspResArr = new litMap(arrJspArr);" + NL + NL);
		sb.append("//GENERATED BY CUSTOMIZATION GENERATOR.DO NOT MODIFY " + NL);
		sb.append("var jspErr={};" + NL);
		sb.append("var jspErrALT = {" + NL + "};" + NL + NL);
		sb.append("var arrJspErr = new Array (jspErr, jspErrALT);" + NL);
		sb.append("var jspErrResArr = new litMap(arrJspErr);" + NL);

		writeToFile(sb.toString(), basePath + "javascripts" + File.separator + "jspjs" + File.separator + "INFENG" + File.separator + menuName + "_det_INFENG.js");
		System.out.println("Generated: javascripts/jspjs/INFENG/" + menuName + "_det_INFENG.js");
	}

	// ===================== RES INFENG JS =====================

	private void generateResINFENGJS(String basePath) {
		StringBuffer sb = new StringBuffer();
		sb.append("//GENERATED BY CUSTOMIZATION GENERATOR.DO NOT MODIFY" + NL + NL);
		String fltGo = nextFLT();
		String fltTitle = nextFLT();
		String fltOk = nextFLT();
		sb.append("var jspRes={" + fltGo + ":\"Go\"," + fltTitle + ":\"" + menuName.toUpperCase() + " Result\"," + fltOk + ":\"Ok\"};" + NL);
		sb.append("var jspResALT = {" + NL + "};" + NL + NL);
		sb.append("var arrJspArr = new Array (jspRes, jspResALT);" + NL);
		sb.append("var jspResArr = new litMap(arrJspArr);" + NL + NL);
		sb.append("//GENERATED BY CUSTOMIZATION GENERATOR.DO NOT MODIFY " + NL);
		sb.append("var jspErr={};" + NL);
		sb.append("var jspErrALT = {" + NL + "};" + NL + NL);
		sb.append("var arrJspErr = new Array (jspErr, jspErrALT);" + NL);
		sb.append("var jspErrResArr = new litMap(arrJspErr);" + NL);

		writeToFile(sb.toString(), basePath + "javascripts" + File.separator + "jspjs" + File.separator + "INFENG" + File.separator + menuName + "_res_INFENG.js");
		System.out.println("Generated: javascripts/jspjs/INFENG/" + menuName + "_res_INFENG.js");
	}

	// ===================== CRIT GLINK JS (REFACTORED) =====================

	private void generateCritGlinkJS(ArrayList<?> fieldList, String basePath) {
		StringBuffer sb = new StringBuffer();
		// Use a local FLT counter matching the crit INFENG order
		int localFlt = 900001;

		String fltGo = "FLT" + (localFlt++);
		String fltTitle = "FLT" + (localFlt++);
		String fltFunc = "Y".equals(isFuncCodePresent) ? "FLT" + (localFlt++) : null;

		// Collect key field FLT codes
		ArrayList<String> keyFltCodes = new ArrayList<String>();
		ArrayList<FieldDetails> keyFields = new ArrayList<FieldDetails>();
		for (int i = 0; i < fieldList.size(); i++) {
			FieldDetails fd = (FieldDetails) fieldList.get(i);
			if ("Y".equals(fd.getIsKeyfld())) {
				keyFltCodes.add("FLT" + (localFlt++));
				keyFields.add(fd);
			}
		}

		String fltGoBtn = "FLT" + (localFlt++);
		String fltClearBtn = "FLT" + (localFlt++);

		String fltSelect = null, fltAdd = null, fltModify = null, fltInquire = null, fltVerify = null, fltDelete = null, fltUndelete = null, fltCancel = null;
		if ("Y".equals(isFuncCodePresent)) {
			fltSelect = "FLT" + (localFlt++);
			fltAdd = "FLT" + (localFlt++);
			fltModify = "FLT" + (localFlt++);
			fltInquire = "FLT" + (localFlt++);
			fltVerify = "FLT" + (localFlt++);
			fltDelete = "FLT" + (localFlt++);
			fltUndelete = "FLT" + (localFlt++);
			fltCancel = "FLT" + (localFlt++);
		}

		// Mandatory wrapper functions
		sb.append(generateMandatoryWrappers());

		// printBlock
		sb.append("function printBlock()" + NL + "{" + NL);
		sb.append(TAB + "writeCustomHeader(\"" + menuName + "_crit\");" + NL);

		// Outer page structure (raw HTML)
		sb.append(TAB + "with (document){" + NL);
		sb.append(TAB + "write('<table border=\"0\" cellspacing=\"0\" cellpadding=\"0\" class=\"ctable\">');" + NL);
		sb.append(TAB + "write('<tr>');" + NL);
		sb.append(TAB + "write('<td>');" + NL);
		sb.append(TAB + "write('<table border=\"0\" cellspacing=\"0\" cellpadding=\"0\">');" + NL);
		sb.append(TAB + "write('<tr>');" + NL);
		sb.append(TAB + "write('<td class=\"page-heading\">' + jspResArr.get(\"" + fltTitle + "\") + '</td>');" + NL);
		sb.append(TAB + "write('</tr>');" + NL);
		sb.append(TAB + "write('</table>');" + NL);
		sb.append(TAB + "write('<!-- DETAILSBLOCK-BEGIN -->');" + NL);
		sb.append(TAB + "write('<table border=\"0\" cellpadding=\"0\" cellspacing=\"0\" width=\"100%\">');" + NL);
		sb.append(TAB + "write('<tr>');" + NL);
		sb.append(TAB + "write('<td valign=\"top\">');" + NL);
		sb.append(TAB + "} //End with()" + NL + NL);

		// setTableHeader (replaces tableborder > innertable > innertabletop1 nesting)
		sb.append(TAB + "setTableHeader();" + NL + NL);

		// Help link (raw HTML)
		sb.append(TAB + "with (document){" + NL);
		sb.append(TAB + "write('<tr>');" + NL);
		sb.append(TAB + "write('<td height=\"25\" colspan=\"5\" align=\"right\">');" + NL);
		sb.append(TAB + "write('<table border=\"0\" cellspacing=\"0\" cellpadding=\"0\">');" + NL);
		sb.append(TAB + "write('<tr>');" + NL);
		sb.append(TAB + "write('<td align=\"right\">');" + NL);
		sb.append(TAB + "write('<a href=\"javascript:showHelpFile(\\'" + menuName + "_crit_help.htm\\');\" id=\"sLnk1\">');" + NL);
		sb.append(TAB + "write('<img  hotKeyId=\"finHelp\" src=\"../Renderer/images/'+applangcode+'/help.gif\" width=\"47\" height=\"21\" vspace=\"1\" border=\"0\" />');" + NL);
		sb.append(TAB + "write('</a>');" + NL);
		sb.append(TAB + "write('</td>');" + NL);
		sb.append(TAB + "write('</tr>');" + NL);
		sb.append(TAB + "write('</table>');" + NL);
		sb.append(TAB + "write('</td>');" + NL);
		sb.append(TAB + "write('</tr>');" + NL);
		sb.append(TAB + "} //End with()" + NL);

		// FuncCode dropdown (raw HTML)
		if ("Y".equals(isFuncCodePresent)) {
			sb.append(NL + TAB + "//FUNCTION" + NL);
			sb.append(TAB + "with (document){" + NL);
			sb.append(TAB + "write('<tr>');" + NL);
			sb.append(TAB + "write('<td class=\"textlabel\" style=\"height: 15px\">' + jspResArr.get(\"" + fltFunc + "\") + '<script>setMandatory(\"Y\");</script></td>');" + NL);
			sb.append(TAB + "write('<td class=\"textfield\">');" + NL);
			sb.append(TAB + "write('<select name=\"' + subGroupName + '.funcCode\" id=\"funcCode\"  fdt=\"String\" ' + " + menuName + "Props.get(\"funcCode_ENABLED\") + ' class=\"listboxfont\">');" + NL);
			sb.append(TAB + "write('<option value=\"\">' + jspResArr.get(\"" + fltSelect + "\") + '</option>');" + NL);
			sb.append(TAB + "write('<option value=\"A\">' + jspResArr.get(\"" + fltAdd + "\") + '</option>');" + NL);
			sb.append(TAB + "write('<option value=\"M\">' + jspResArr.get(\"" + fltModify + "\") + '</option>');" + NL);
			sb.append(TAB + "write('<option value=\"I\">' + jspResArr.get(\"" + fltInquire + "\") + '</option>');" + NL);
			sb.append(TAB + "write('<option value=\"V\">' + jspResArr.get(\"" + fltVerify + "\") + '</option>');" + NL);
			sb.append(TAB + "write('<option value=\"X\">' + jspResArr.get(\"" + fltCancel + "\") + '</option>');" + NL);
			sb.append(TAB + "write('</select>');" + NL);
			sb.append(TAB + "write('</td>');" + NL);
			sb.append(TAB + "write('<td class=\"columnwidth\"> </td>');" + NL);
			sb.append(TAB + "write('</tr>');" + NL);
			sb.append(TAB + "} //End with()" + NL);
		}

		// Key fields using cd_atf_functions helpers
		for (int i = 0; i < keyFields.size(); i++) {
			appendFieldWithLayout(sb, keyFields, i, keyFltCodes.get(i));
		}

		// setTableFooter + close outer structure
		sb.append(NL + TAB + "setTableFooter();" + NL + NL);

		sb.append(TAB + "with (document){" + NL);
		sb.append(TAB + "write('</td>');" + NL);
		sb.append(TAB + "write('</tr>');" + NL);
		sb.append(TAB + "write('</table>');" + NL);
		sb.append(TAB + "write('<!-- DETAILSBLOCK-END -->');" + NL);
		sb.append(TAB + "write('</td>');" + NL);
		sb.append(TAB + "write('</tr>');" + NL);
		sb.append(TAB + "write('</table>');" + NL);
		sb.append(TAB + "} //End with()" + NL);
		sb.append("} //End function" + NL + NL);

		// printFooterBlock (unchanged)
		sb.append("function printFooterBlock()" + NL + "{" + NL);
		sb.append(TAB + "with (document) {" + NL);
		sb.append(TAB + "if ((sReferralMode == 'I')||(sReferralMode == 'S')){" + NL);
		sb.append(TAB + "write('<div align=\"left\" class=\"ctable\">');" + NL);
		sb.append(TAB + "if (sReferralMode == 'S'){" + NL);
		sb.append(TAB + "write('<input type=\"button\" class=\"Button\" id=\"Submit\" value=\"'+jspResArr.get(\"FLT900001\")+ '\" onClick=\"javascript:return doRefSubmit(this);\" hotKeyId=\"Submit\" >');" + NL);
		sb.append(TAB + "}" + NL);
		sb.append(TAB + "writeRefFooter();" + NL);
		sb.append(TAB + "write('<input type=\"button\" class=\"Button\" id=\"_BackRef_\" value=\"Cancel\" onClick=\"javascript:return doSubmit(this.id);\" hotKeyId=\"Cancel\" >');" + NL);
		sb.append(TAB + "write('</div>');" + NL);
		sb.append(TAB + "}else{" + NL);
		sb.append(TAB + "write('<div class=\"ctable\">');" + NL);
		sb.append(TAB + "write('<input id=\"Accept\" name=\"Go\" type=\"button\" class=\"button\" onClick=\"javascript:return " + menuName + "_crit_ONCLICK1(this,this);\" value=\"' + jspResArr.get(\"" + fltGoBtn + "\") + '\" hotKeyId=\"Go\">');" + NL);
		sb.append(TAB + "write('<input id=\"Clear\" name=\"Clear\" type=\"button\" class=\"button\" value=\"' + jspResArr.get(\"" + fltClearBtn + "\") + '\" onClick=\"javascript:return " + menuName + "_crit_ONCLICK2(this);\">');" + NL);
		sb.append(TAB + "writeFooter();" + NL);
		sb.append(TAB + "write('</div>');" + NL);
		sb.append(TAB + "}" + NL);
		sb.append(TAB + "} //End with()" + NL);
		sb.append("}//End function" + NL + NL);

		// fnOnLoad (unchanged)
		sb.append("function fnOnLoad()" + NL + "{" + NL);
		sb.append(TAB + "var ObjForm = document.forms[0];" + NL + NL);
		sb.append(TAB + "initFocusHandler();" + NL + NL);
		sb.append(TAB + "pre_ONLOAD('" + menuName + "_crit',this);" + NL + NL);
		sb.append(TAB + "var funcName = \"this.\"+\"locfnOnLoad\";" + NL);
		sb.append(TAB + "if(eval(funcName) != undefined){" + NL);
		sb.append(TAB + TAB + "eval(funcName).call(this);" + NL);
		sb.append(TAB + "}" + NL + NL);
		sb.append(TAB + "fnPopulateControlValues();" + NL + NL);
		sb.append(TAB + "fnPopUpExceptionWindow(ObjForm.actionCode);" + NL);
		sb.append(TAB + "if((typeof(WF_IN_PROGRESS) != \"undefined\") && (WF_IN_PROGRESS == \"PEAS\")){" + NL);
		sb.append(TAB + TAB + "checkCustErrExecNextStep(Message);" + NL);
		sb.append(TAB + "} " + NL);
		if ("Y".equals(isFuncCodePresent)) {
			sb.append(TAB + "ObjForm.funcCode.focus();" + NL);
		} else if (keyFields.size() > 0) {
			sb.append(TAB + "ObjForm." + keyFields.get(0).getIdName() + ".focus();" + NL);
		}
		sb.append(TAB + "post_ONLOAD('" + menuName + "_crit',this);" + NL);
		sb.append("}" + NL + NL);

		// fnCheckMandatoryFields (unchanged)
		sb.append("function fnCheckMandatoryFields()" + NL + "{" + NL);
		sb.append(TAB + "var ObjForm = document.forms[0];" + NL);
		sb.append(TAB + "return true;" + NL);
		sb.append("}" + NL + NL);

		// fnPopulateControlValues (unchanged)
		sb.append("function fnPopulateControlValues() " + NL + "{" + NL);
		sb.append(TAB + "var ObjForm = document.forms[0];" + NL);
		if ("Y".equals(isFuncCodePresent)) {
			sb.append(TAB + "ObjForm.funcCode.value = funcCode;" + NL);
		}
		for (FieldDetails fd : keyFields) {
			sb.append(TAB + "ObjForm." + fd.getIdName() + ".value = " + fd.getIdName() + ";" + NL);
		}
		sb.append("}" + NL + NL);

		// ONCLICK1, ONCLICK2, pre_ONCLICK (unchanged)
		sb.append("function " + menuName + "_crit_ONCLICK1(obj,p1)" + NL + "{" + NL);
		sb.append(TAB + "var retVal = \"\";" + NL);
		sb.append(TAB + "if (preEventCall('" + menuName + "_crit',obj,'ONCLICK') == false) { " + NL);
		sb.append(TAB + TAB + "return false;" + NL);
		sb.append(TAB + "}" + NL);
		sb.append(TAB + "if ((retVal =  fnValAndSubmit(p1)) == false) {" + NL);
		sb.append(TAB + TAB + "return false;" + NL);
		sb.append(TAB + "}" + NL);
		sb.append(TAB + "if (postEventCall('" + menuName + "_crit',obj,'ONCLICK') == false) { " + NL);
		sb.append(TAB + TAB + "return false;" + NL);
		sb.append(TAB + "}" + NL);
		sb.append(TAB + "return (retVal == undefined) ? true : retVal;" + NL);
		sb.append("}" + NL + NL);

		sb.append("function " + menuName + "_crit_ONCLICK2(obj)" + NL + "{" + NL);
		sb.append(TAB + "var retVal = \"\";" + NL);
		sb.append(TAB + "if (preEventCall('" + menuName + "_crit',obj,'ONCLICK') == false) { " + NL);
		sb.append(TAB + TAB + "return false;" + NL);
		sb.append(TAB + "}" + NL);
		sb.append(TAB + "if ((retVal =  fnClearFields()) == false) {" + NL);
		sb.append(TAB + TAB + "return false;" + NL);
		sb.append(TAB + "}" + NL);
		sb.append(TAB + "if (postEventCall('" + menuName + "_crit',obj,'ONCLICK') == false) { " + NL);
		sb.append(TAB + TAB + "return false;" + NL);
		sb.append(TAB + "}" + NL);
		sb.append(TAB + "return (retVal == undefined) ? true : retVal; " + NL);
		sb.append("}" + NL + NL);

		sb.append("function " + menuName + "_pre_ONCLICK()" + NL + "{" + NL);
		sb.append(TAB + "setFieldsToCustomData(\"funcCode\");" + NL);
		sb.append("}" + NL + NL);

		writeToFile(sb.toString(), basePath + "javascripts" + File.separator + menuName + File.separator + menuName + "_crit_glink.js");
		System.out.println("Generated: javascripts/" + menuName + "/" + menuName + "_crit_glink.js");
	}

	// ===================== DET GLINK JS (REFACTORED) =====================

	private void generateDetGlinkJS(ArrayList<?> fieldList, String basePath) {
		StringBuffer sb = new StringBuffer();

		// Collect key and data fields
		ArrayList<FieldDetails> keyFields = new ArrayList<FieldDetails>();
		ArrayList<FieldDetails> dataFields = new ArrayList<FieldDetails>();
		for (int i = 0; i < fieldList.size(); i++) {
			FieldDetails fd = (FieldDetails) fieldList.get(i);
			if ("Y".equals(fd.getIsKeyfld())) {
				keyFields.add(fd);
			} else {
				dataFields.add(fd);
			}
		}

		// Use a local counter matching det INFENG order
		int localFlt = 900001;
		String fltGo = "FLT" + (localFlt++);
		String fltTitle = "FLT" + (localFlt++);
		String fltFunc = "Y".equals(isFuncCodePresent) ? "FLT" + (localFlt++) : null;

		ArrayList<String> keyFltCodes = new ArrayList<String>();
		for (int i = 0; i < keyFields.size(); i++) {
			keyFltCodes.add("FLT" + (localFlt++));
		}
		ArrayList<String> dataFltCodes = new ArrayList<String>();
		for (int i = 0; i < dataFields.size(); i++) {
			dataFltCodes.add("FLT" + (localFlt++));
		}

		String fltSubmit = "FLT" + (localFlt++);
		String fltCancelBtn = "FLT" + (localFlt++);
		String fltOk = "FLT" + (localFlt++);

		String fltAAdd = null, fltMModify = null, fltIInquire = null, fltVVerify = null, fltXCancel = null;
		if ("Y".equals(isFuncCodePresent)) {
			fltAAdd = "FLT" + (localFlt++);
			fltMModify = "FLT" + (localFlt++);
			fltIInquire = "FLT" + (localFlt++);
			fltVVerify = "FLT" + (localFlt++);
			fltXCancel = "FLT" + (localFlt++);
		}

		// Check if any date fields exist
		boolean hasDateFields = false;
		for (FieldDetails fd : dataFields) {
			if ("e".equals(fd.getFieldType())) { hasDateFields = true; break; }
		}

		// Mandatory wrapper functions
		sb.append(generateMandatoryWrappers());

		// printBlock
		sb.append("function printBlock()" + NL + "{" + NL);
		sb.append(TAB + "writeCustomHeader(\"" + menuName + "_det\");" + NL);

		// Outer page structure (raw HTML)
		sb.append(TAB + "with (document){" + NL);
		sb.append(TAB + "write('<table border=\"0\" cellspacing=\"0\" cellpadding=\"0\" class=\"ctable\">');" + NL);
		sb.append(TAB + "write('<tr>');" + NL);
		sb.append(TAB + "write('<td>');" + NL);
		sb.append(TAB + "write('<table border=\"0\" cellspacing=\"0\" cellpadding=\"0\">');" + NL);
		sb.append(TAB + "write('<tr>');" + NL);
		sb.append(TAB + "write('<td class=\"page-heading\">' + jspResArr.get(\"" + fltTitle + "\") + '</td>');" + NL);
		sb.append(TAB + "write('</tr>');" + NL);
		sb.append(TAB + "write('</table>');" + NL);

		// Header area - display funcCode and key fields as labels
		sb.append(TAB + "write('<table border=\"0\" cellpadding=\"0\" cellspacing=\"0\" width=\"100%\">');" + NL);
		if ("Y".equals(isFuncCodePresent)) {
			sb.append(TAB + "write('<tr>');" + NL);
			sb.append(TAB + "write('<td class=\"textlabel\">' + jspResArr.get(\"" + fltFunc + "\") + '</td>');" + NL);
			sb.append(TAB + "write('<td class=\"textfielddisplaylabel\">');" + NL);
			sb.append(TAB + "if(funcCode == \"A\") { write('<label id=\"compField\">' + jspResArr.get(\"" + fltAAdd + "\") + '</label>'); }" + NL);
			sb.append(TAB + "if(funcCode == \"M\") { write('<label id=\"compField\">' + jspResArr.get(\"" + fltMModify + "\") + '</label>'); }" + NL);
			sb.append(TAB + "if(funcCode == \"I\") { write('<label id=\"compField\">' + jspResArr.get(\"" + fltIInquire + "\") + '</label>'); }" + NL);
			sb.append(TAB + "if(funcCode == \"V\") { write('<label id=\"compField\">' + jspResArr.get(\"" + fltVVerify + "\") + '</label>'); }" + NL);
			sb.append(TAB + "if(funcCode == \"X\") { write('<label id=\"compField\">' + jspResArr.get(\"" + fltXCancel + "\") + '</label>'); }" + NL);
			sb.append(TAB + "write('</td>');" + NL);
			sb.append(TAB + "write('<td class=\"columnwidth\">&nbsp; </td>');" + NL);
		}

		// Key fields as display labels
		for (int i = 0; i < keyFields.size(); i++) {
			FieldDetails fd = keyFields.get(i);
			sb.append(TAB + "write('<td class=\"textlabel\">' + jspResArr.get(\"" + keyFltCodes.get(i) + "\") + '</td>');" + NL);
			sb.append(TAB + "write('<td class=\"textfielddisplaylabel\">');" + NL);
			sb.append(TAB + "write('<label id=\"compField\">' + " + fd.getIdName() + " + '</label>');" + NL);
			sb.append(TAB + "write('</td>');" + NL);
		}
		sb.append(TAB + "write('</tr>');" + NL);
		sb.append(TAB + "write('</table>');" + NL);
		sb.append(TAB + "write('<br />');" + NL);

		// Details block outer structure
		sb.append(TAB + "write('<!-- DETAILSBLOCK-BEGIN -->');" + NL);
		sb.append(TAB + "write('<table border=\"0\" cellpadding=\"0\" cellspacing=\"0\" width=\"100%\">');" + NL);
		sb.append(TAB + "write('<tr>');" + NL);
		sb.append(TAB + "write('<td valign=\"top\">');" + NL);
		sb.append(TAB + "} //End with()" + NL + NL);

		// setTableHeader
		sb.append(TAB + "setTableHeader();" + NL + NL);

		// Help link (raw HTML)
		sb.append(TAB + "with (document){" + NL);
		sb.append(TAB + "write('<tr>');" + NL);
		sb.append(TAB + "write('<td height=\"25\" colspan=\"5\" align=\"right\">');" + NL);
		sb.append(TAB + "write('<table border=\"0\" cellspacing=\"0\" cellpadding=\"0\">');" + NL);
		sb.append(TAB + "write('<tr>');" + NL);
		sb.append(TAB + "write('<td align=\"right\">');" + NL);
		sb.append(TAB + "write('<a href=\"javascript:showHelpFile(\\'" + menuName + "_det_help.htm\\');\" id=\"sLnk1\">');" + NL);
		sb.append(TAB + "write('<img  hotKeyId=\"finHelp\" src=\"../Renderer/images/'+applangcode+'/help.gif\" width=\"47\" height=\"21\" vspace=\"1\" border=\"0\" />');" + NL);
		sb.append(TAB + "write('</a>');" + NL);
		sb.append(TAB + "write('</td>');" + NL);
		sb.append(TAB + "write('</tr>');" + NL);
		sb.append(TAB + "write('</table>');" + NL);
		sb.append(TAB + "write('</td>');" + NL);
		sb.append(TAB + "write('</tr>');" + NL);
		sb.append(TAB + "} //End with()" + NL);

		// Data fields using cd_atf_functions helpers
		for (int i = 0; i < dataFields.size(); i++) {
			appendFieldWithLayout(sb, dataFields, i, dataFltCodes.get(i));
		}

		// setTableFooter + close outer structure
		sb.append(NL + TAB + "setTableFooter();" + NL + NL);

		sb.append(TAB + "with (document){" + NL);
		sb.append(TAB + "write('</td>');" + NL);
		sb.append(TAB + "write('</tr>');" + NL);
		sb.append(TAB + "write('</table>');" + NL);
		sb.append(TAB + "write('<!-- DETAILSBLOCK-END -->');" + NL);
		sb.append(TAB + "write('</td>');" + NL);
		sb.append(TAB + "write('</tr>');" + NL);
		sb.append(TAB + "write('</table>');" + NL);
		sb.append(TAB + "} //End with()" + NL);
		sb.append("} //End function" + NL + NL);

		// printFooterBlock (unchanged)
		sb.append("function printFooterBlock()" + NL + "{" + NL);
		sb.append(TAB + "with (document) {" + NL);
		sb.append(TAB + "if ((sReferralMode == 'I')||(sReferralMode == 'S')){" + NL);
		sb.append(TAB + "write('<div align=\"left\" class=\"ctable\">');" + NL);
		sb.append(TAB + "if (sReferralMode == 'S'){" + NL);
		sb.append(TAB + "write('<input type=\"button\" class=\"Button\" id=\"Submit\" value=\"'+jspResArr.get(\"" + fltSubmit + "\")+ '\" onClick=\"javascript:return doRefSubmit(this);\" hotKeyId=\"Submit\" >');" + NL);
		sb.append(TAB + "}" + NL);
		sb.append(TAB + "writeRefFooter();" + NL);
		sb.append(TAB + "write('<input type=\"button\" class=\"Button\" id=\"_BackRef_\" value=\"'+jspResArr.get(\"" + fltCancelBtn + "\")+ '\" onClick=\"javascript:return doSubmit(this.id);\" hotKeyId=\"Cancel\" >');" + NL);
		sb.append(TAB + "write('</div>');" + NL);
		sb.append(TAB + "}else{" + NL);
		if ("Y".equals(isFuncCodePresent)) {
			sb.append(TAB + "if(funcCode !='I'){" + NL);
		}
		sb.append(TAB + "write('<div class=\"ctable\">');" + NL);
		sb.append(TAB + "write('<input id=\"Submit\" name=\"Submit\" type=\"button\" class=\"button\" onClick=\"javascript:return " + menuName + "_det_ONCLICK1(this,this);\" value=\"' + jspResArr.get(\"" + fltSubmit + "\") + '\" hotKeyId=\"Submit\">');" + NL);
		sb.append(TAB + "write('<input id=\"Cancel\" name=\"Cancel\" type=\"button\" class=\"button\" value=\"' + jspResArr.get(\"" + fltCancelBtn + "\") + '\" onClick=\"javascript:return " + menuName + "_det_ONCLICK3(this,this.id);\" hotKeyId=\"Cancel\">');" + NL);
		if ("Y".equals(isFuncCodePresent)) {
			sb.append(TAB + "}else{" + NL);
			sb.append(TAB + "write('<div class=\"ctable\">');" + NL);
			sb.append(TAB + "write('<input class=\"button\" type=\"button\" id=\"Back\" value=\"'+jspResArr.get(\"" + fltOk + "\")+ '\" onClick=\"javascript:return doSubmit(this.id)\" hotKeyId=\"Ok\">');" + NL);
			sb.append(TAB + "}" + NL);
		}
		sb.append(TAB + "writeFooter();" + NL);
		sb.append(TAB + "write('</div>');" + NL);
		sb.append(TAB + "}" + NL);
		sb.append(TAB + "} //End with()" + NL);
		sb.append("}//End function" + NL + NL);

		// fnOnLoad (unchanged)
		sb.append("function fnOnLoad()" + NL + "{" + NL);
		sb.append(TAB + "var ObjForm = document.forms[0];" + NL + NL);
		sb.append(TAB + "initFocusHandler();" + NL + NL);
		sb.append(TAB + "pre_ONLOAD('" + menuName + "_det',this);" + NL + NL);
		sb.append(TAB + "var funcName = \"this.\"+\"locfnOnLoad\";" + NL);
		sb.append(TAB + "if(eval(funcName) != undefined){" + NL);
		sb.append(TAB + TAB + "eval(funcName).call(this);" + NL);
		sb.append(TAB + "}" + NL + NL);
		sb.append(TAB + "fnPopulateControlValues();" + NL);
		if (hasDateFields) {
			sb.append(TAB + "fnAssignDateOnLoad(ObjForm);" + NL);
		}

		if ("Y".equals(isFuncCodePresent)) {
			sb.append(TAB + "if(funcCode =='V' || funcCode =='I' || funcCode =='D' || funcCode =='U' || funcCode =='X' || sReferralMode =='I' || sReferralMode =='S'){" + NL);
			sb.append(TAB + TAB + "fnDisableFormDataControls('V',ObjForm,0);" + NL);
			sb.append(TAB + "}" + NL);
		}

		sb.append(TAB + "fnPopUpExceptionWindow(ObjForm.actionCode);" + NL);
		sb.append(TAB + "if((typeof(WF_IN_PROGRESS) != \"undefined\") && (WF_IN_PROGRESS == \"PEAS\")){" + NL);
		sb.append(TAB + TAB + "checkCustErrExecNextStep(Message);" + NL);
		sb.append(TAB + "}" + NL);

		if ("Y".equals(isFuncCodePresent)) {
			sb.append(TAB + "if(funcCode =='V' || funcCode =='I' || funcCode =='X'){" + NL);
			for (FieldDetails fd : dataFields) {
				String fieldType = fd.getFieldType();
				if ("e".equals(fieldType)) {
					sb.append(TAB + TAB + "document.forms[0]." + fd.getIdName() + "_ui.disabled=true;" + NL);
				} else {
					sb.append(TAB + TAB + "document.forms[0]." + fd.getIdName() + ".disabled=true;" + NL);
				}
			}
			sb.append(TAB + "}" + NL);
		}

		sb.append(NL + TAB + "post_ONLOAD('" + menuName + "_det',this);" + NL);
		sb.append("}" + NL + NL);

		// fnCheckMandatoryFields (unchanged)
		sb.append("function fnCheckMandatoryFields()" + NL + "{" + NL);
		sb.append(TAB + "var ObjForm = document.forms[0];" + NL);
		sb.append(TAB + "return true;" + NL);
		sb.append("}" + NL + NL);

		// fnPopulateControlValues (unchanged)
		sb.append("function fnPopulateControlValues() " + NL + "{" + NL);
		sb.append(TAB + "var ObjForm = document.forms[0];" + NL);
		for (FieldDetails fd : dataFields) {
			String fieldType = fd.getFieldType();
			if ("e".equals(fieldType)) {
				sb.append(TAB + "ObjForm." + fd.getIdName() + "_ui.value = " + fd.getIdName() + ";" + NL);
				sb.append(TAB + menuName + "_det_ONBLUR1(ObjForm." + fd.getIdName() + "_ui,ObjForm." + fd.getIdName() + "_ui,ObjForm." + fd.getIdName() + "_ui);" + NL + NL);
			} else {
				sb.append(TAB + "ObjForm." + fd.getIdName() + ".value = " + fd.getIdName() + ";" + NL);
			}
		}
		sb.append("}" + NL + NL);

		// ONCLICK handlers (unchanged)
		sb.append("function " + menuName + "_det_ONCLICK1(obj,p1)" + NL + "{" + NL);
		sb.append(TAB + "var retVal = \"\";" + NL);
		sb.append(TAB + "if (preEventCall('" + menuName + "_det',obj,'ONCLICK') == false) { return false; }" + NL);
		sb.append(TAB + "if ((retVal = fnValAndSubmit(p1)) == false) { return false; }" + NL);
		sb.append(TAB + "if (postEventCall('" + menuName + "_det',obj,'ONCLICK') == false) { return false; }" + NL);
		sb.append(TAB + "return (retVal == undefined) ? true : retVal;" + NL);
		sb.append("}" + NL + NL);

		sb.append("function " + menuName + "_det_ONCLICK3(obj,p1)" + NL + "{" + NL);
		sb.append(TAB + "var retVal = \"\";" + NL);
		sb.append(TAB + "if (preEventCall('" + menuName + "_det',obj,'ONCLICK') == false) { return false; }" + NL);
		sb.append(TAB + "if ((retVal = doSubmit(p1)) == false) { return false; }" + NL);
		sb.append(TAB + "if (postEventCall('" + menuName + "_det',obj,'ONCLICK') == false) { return false; }" + NL);
		sb.append(TAB + "return (retVal == undefined) ? true : retVal;" + NL);
		sb.append("}" + NL + NL);

		// Date ONBLUR handler (unchanged)
		if (hasDateFields) {
			sb.append("function " + menuName + "_det_ONBLUR1(obj,p1,p2)" + NL + "{" + NL);
			sb.append(TAB + "var retVal = \"\";" + NL);
			sb.append(TAB + "if (preEventCall('" + menuName + "_det',obj,'ONBLUR') == false) { return false; }" + NL);
			sb.append(TAB + "if ((retVal = onBlurFormatDate(p1)) == false) { return false; }" + NL);
			sb.append(TAB + "if ((retVal = fnAssignDateOnEnter(p2)) == false) { return false; }" + NL);
			sb.append(TAB + "if (postEventCall('" + menuName + "_det',obj,'ONBLUR') == false) { return false; }" + NL);
			sb.append(TAB + "if(!validateTypes(document.forms[0])) { obj.value = \"\"; return false; }" + NL);
			sb.append(TAB + "return (retVal == undefined) ? true : retVal;" + NL);
			sb.append("}" + NL + NL);
		}

		writeToFile(sb.toString(), basePath + "javascripts" + File.separator + menuName + File.separator + menuName + "_det_glink.js");
		System.out.println("Generated: javascripts/" + menuName + "/" + menuName + "_det_glink.js");
	}

	// ===================== RES GLINK JS (UNCHANGED) =====================

	private void generateResGlinkJS(String basePath) {
		int localFlt = 900001;
		String fltGo = "FLT" + (localFlt++);
		String fltTitle = "FLT" + (localFlt++);
		String fltOk = "FLT" + (localFlt++);

		StringBuffer sb = new StringBuffer();
		sb.append("function printBlock()" + NL + "{" + NL);
		sb.append(TAB + "writeCustomHeader(\"" + menuName + "_res\");" + NL);
		sb.append(TAB + "with (document){" + NL);
		sb.append(TAB + "write('<table border=\"0\" cellspacing=\"0\" cellpadding=\"0\" class=\"ctable\">');" + NL);
		sb.append(TAB + "write('<tr>');" + NL);
		sb.append(TAB + "write('<td>');" + NL);
		sb.append(TAB + "write('<table width=\"100%\" border=\"0\" cellpadding=\"0\" cellspacing=\"0\">');" + NL);
		sb.append(TAB + "write('<tr>');" + NL);
		sb.append(TAB + "write('<td class=\"page-heading\">' + jspResArr.get(\"" + fltTitle + "\") + '</td>');" + NL);
		sb.append(TAB + "write('</tr>');" + NL);
		sb.append(TAB + "write('</table>');" + NL);
		sb.append(TAB + "write('</td>');" + NL);
		sb.append(TAB + "write('</tr>');" + NL);
		sb.append(TAB + "write('</table>');" + NL);

		sb.append(TAB + "write('<table width=\"100%\" border=\"0\" cellpadding=\"0\" cellspacing=\"0\" class=\"ctable\">');" + NL);
		sb.append(TAB + "write('<tr>');" + NL);
		sb.append(TAB + "write('<td valign=\"top\">');" + NL);
		sb.append(TAB + "write('<table width=\"100%\" border=\"0\" cellpadding=\"0\" cellspacing=\"0\" class=\"tableborder\">');" + NL);
		sb.append(TAB + "write('<tr>');" + NL);
		sb.append(TAB + "write('<td>');" + NL);
		sb.append(TAB + "write('<table width=\"100%\" border=\"0\" cellpadding=\"0\" cellspacing=\"0\" class=\"innertable\">');" + NL);
		sb.append(TAB + "write('<tr class=\"textfielddisplaylabel1\" valign=\"middle\">');" + NL);
		sb.append(TAB + "write('<td colspan=\"5\">');" + NL);
		sb.append(TAB + "write('<table width=\"100%\" border=\"0\" cellpadding=\"0\" cellspacing=\"0\" class=\"resultpage\">');" + NL);
		sb.append(TAB + "write('<tr>');" + NL);
		sb.append(TAB + "write('<td width=\"3%\">');" + NL);
		sb.append(TAB + "write('<img class=\"img\" src=\"../Renderer/images/info.gif\" width=\"29\" height=\"29\" align=\"right\" border=\"0\">');" + NL);
		sb.append(TAB + "write('</img></td>');" + NL);
		sb.append(TAB + "if(Message != \"NULL\"){" + NL);
		sb.append(TAB + "write('<td width=\"97%\" > ' + Message + '</td>');" + NL);
		sb.append(TAB + "}" + NL);
		sb.append(TAB + "else if(Message == \"\"){" + NL);
		sb.append(TAB + "write('<td>Set the value for the variable RESULT_MSG using the userhook SetOrbOut in script.</td>');" + NL);
		sb.append(TAB + "}" + NL);
		sb.append(TAB + "else{" + NL);
		sb.append(TAB + "write('<td>&nbsp;&nbsp;&nbsp;</td>');" + NL);
		sb.append(TAB + "}" + NL);
		sb.append(TAB + "write('</tr>');" + NL);
		sb.append(TAB + "write('</table>');" + NL);
		sb.append(TAB + "write('</td></tr>');" + NL);
		sb.append(TAB + "write('</table></td></tr>');" + NL);
		sb.append(TAB + "write('</table></td></tr>');" + NL);
		sb.append(TAB + "write('</table></td></tr>');" + NL);
		sb.append(TAB + "write('</table>');" + NL);
		sb.append(TAB + "} //End with()" + NL);
		sb.append("} //End function" + NL + NL);

		// printFooterBlock
		sb.append("function printFooterBlock()" + NL + "{" + NL);
		sb.append(TAB + "with (document) {" + NL);
		sb.append(TAB + "write('<div class=\"ctable\">');" + NL);
		sb.append(TAB + "write('<input id=\"Ok\" name=\"Ok\" type=\"button\" class=\"button\" value=\"' + jspResArr.get(\"" + fltOk + "\") + '\" onClick=\"javascript:return " + menuName + "_res_ONCLICK1(this);\" hotKeyId=\"Go\">');" + NL);
		sb.append(TAB + "writeFooter();" + NL);
		sb.append(TAB + "write('</div>');" + NL);
		sb.append(TAB + "}" + NL);
		sb.append("}//End function" + NL + NL);

		// ONCLICK1
		sb.append("function " + menuName + "_res_ONCLICK1(obj)" + NL + "{" + NL);
		sb.append(TAB + "var retVal = \"\";" + NL);
		sb.append(TAB + "if (preEventCall('" + menuName + "_res',obj,'ONCLICK') == false) { return false; }" + NL);
		sb.append(TAB + "if ((retVal = fnLastButtonClick()) == false) { return false; }" + NL);
		sb.append(TAB + "if (postEventCall('" + menuName + "_res',obj,'ONCLICK') == false) { return false; }" + NL);
		sb.append(TAB + "return (retVal == undefined) ? true : retVal;" + NL);
		sb.append("}" + NL);

		writeToFile(sb.toString(), basePath + "javascripts" + File.separator + menuName + File.separator + menuName + "_res_glink.js");
		System.out.println("Generated: javascripts/" + menuName + "/" + menuName + "_res_glink.js");
	}

	// ===================== CRIT LINK JS - ENHANCED VALIDATION =====================

	private void generateCritLinkJS(ArrayList<?> fieldList, String basePath) {
		StringBuffer sb = new StringBuffer();
		sb.append("<!--\tThis is getting executing on click of submit and validate button -->" + NL);
		sb.append("function fnValidateData() {" + NL);
		sb.append(TAB + TAB + "if (!fnCheckMandatoryFields())" + NL);
		sb.append(TAB + TAB + "{" + NL);
		sb.append(TAB + TAB + TAB + "return false;" + NL);
		sb.append(TAB + TAB + "}" + NL);

		if ("Y".equals(isFuncCodePresent)) {
			sb.append(TAB + TAB + "if(!checkFieldMandatory(\"funcCode\", \"Select Function Code\")) { return false; }" + NL);
		}

		for (int i = 0; i < fieldList.size(); i++) {
			FieldDetails fd = (FieldDetails) fieldList.get(i);
			if (!"Y".equals(fd.getIsKeyfld())) continue;

			String lit = (fd.getLiteralName() != null) ? fd.getLiteralName() : fd.getIdName();
			String valType = fd.getValidationType();
			String fieldType = safe(fd.getFieldType(), "a");
			String fldRef = ("e".equals(fieldType)) ? fd.getIdName() + "_ui" : fd.getIdName();
			String customVal = fd.getCustomValidation();

			// Mandatory check using cd_atf_functions helper
			if ("Y".equals(fd.getMandatory())) {
				sb.append(TAB + TAB + "if(!checkFieldMandatory(\"" + fldRef + "\", \"Enter " + lit + "\")) { return false; }" + NL);
			}

			// Special character blocking for text fields
			if ("a".equals(fieldType) || "b".equals(fieldType) || "c".equals(fieldType)) {
				sb.append(TAB + TAB + "if(!fnBlockSpecialCharacters(document.forms[0]." + fldRef + ")) { return false; }" + NL);
			}

			// Validation type checks (kept as-is, no cd_atf_functions equivalent)
			if ("NUM".equals(valType)) {
				sb.append(TAB + TAB + "if(isNaN(document.forms[0]." + fldRef + ".value))" + NL);
				sb.append(TAB + TAB + "{" + NL);
				sb.append(TAB + TAB + TAB + "alert(\"Enter " + lit + " in Numbers\");" + NL);
				sb.append(TAB + TAB + TAB + "document.forms[0]." + fldRef + ".focus();" + NL);
				sb.append(TAB + TAB + TAB + "return false;" + NL);
				sb.append(TAB + TAB + "}" + NL);
			} else if ("ALPHA".equals(valType)) {
				sb.append(TAB + TAB + "if(!/^[a-zA-Z]*$/.test(document.forms[0]." + fldRef + ".value))" + NL);
				sb.append(TAB + TAB + "{" + NL);
				sb.append(TAB + TAB + TAB + "alert(\"Enter " + lit + " in Alphabets only\");" + NL);
				sb.append(TAB + TAB + TAB + "document.forms[0]." + fldRef + ".focus();" + NL);
				sb.append(TAB + TAB + TAB + "return false;" + NL);
				sb.append(TAB + TAB + "}" + NL);
			} else if ("ALPHANUM".equals(valType)) {
				sb.append(TAB + TAB + "if(!/^[a-zA-Z0-9]*$/.test(document.forms[0]." + fldRef + ".value))" + NL);
				sb.append(TAB + TAB + "{" + NL);
				sb.append(TAB + TAB + TAB + "alert(\"Enter " + lit + " in Alphanumeric only\");" + NL);
				sb.append(TAB + TAB + TAB + "document.forms[0]." + fldRef + ".focus();" + NL);
				sb.append(TAB + TAB + TAB + "return false;" + NL);
				sb.append(TAB + TAB + "}" + NL);
			}

			// Custom validation function
			if (customVal != null && !customVal.isEmpty()) {
				sb.append(TAB + TAB + "if(!" + customVal + "()) { return false; }" + NL);
			}
		}

		sb.append(TAB + TAB + "return true;" + NL);
		sb.append("}" + NL);

		writeToFile(sb.toString(), basePath + "javascripts" + File.separator + menuName + File.separator + menuName + "_crit_link.js");
		System.out.println("Generated: javascripts/" + menuName + "/" + menuName + "_crit_link.js");
	}

	// ===================== DET LINK JS - ENHANCED VALIDATION =====================

	private void generateDetLinkJS(ArrayList<?> fieldList, String basePath) {
		StringBuffer sb = new StringBuffer();
		sb.append("<!--\tThis is getting executing on click of submit and validate button -->" + NL);
		sb.append("function fnValidateData() {" + NL);
		sb.append(TAB + TAB + "if (!fnCheckMandatoryFields())" + NL);
		sb.append(TAB + TAB + "{" + NL);
		sb.append(TAB + TAB + TAB + "return false;" + NL);
		sb.append(TAB + TAB + "}" + NL);

		for (int i = 0; i < fieldList.size(); i++) {
			FieldDetails fd = (FieldDetails) fieldList.get(i);
			if (!"N".equals(fd.getIsKeyfld())) continue; // Only data fields

			String lit = (fd.getLiteralName() != null) ? fd.getLiteralName() : fd.getIdName();
			String valType = fd.getValidationType();
			String fieldType = safe(fd.getFieldType(), "a");
			String fldRef = ("e".equals(fieldType)) ? fd.getIdName() + "_ui" : fd.getIdName();
			String customVal = fd.getCustomValidation();

			// Mandatory check using cd_atf_functions helper
			if ("Y".equals(fd.getMandatory())) {
				sb.append(TAB + TAB + "if(!checkFieldMandatory(\"" + fldRef + "\", \"Enter " + lit + "\")) { return false; }" + NL);
			}

			// Special character blocking for text fields
			if ("a".equals(fieldType) || "b".equals(fieldType) || "c".equals(fieldType)) {
				sb.append(TAB + TAB + "if(!fnBlockSpecialCharacters(document.forms[0]." + fldRef + ")) { return false; }" + NL);
			}

			// Date validation using cd_atf_functions helpers
			if ("e".equals(fieldType) && customVal != null && !customVal.isEmpty()) {
				if ("AFTER_BOD".equals(customVal)) {
					sb.append(TAB + TAB + "if(!dateAfterBoDDate(\"" + fd.getIdName() + "_ui\", BODDate, \"" + lit + " must be after BOD Date\")) { return false; }" + NL);
				} else if ("BEFORE_BOD".equals(customVal)) {
					sb.append(TAB + TAB + "if(!dateBeforeBoDDate(\"" + fd.getIdName() + "_ui\", BODDate, \"" + lit + " must be before BOD Date\")) { return false; }" + NL);
				} else {
					// Custom validation function
					sb.append(TAB + TAB + "if(!" + customVal + "()) { return false; }" + NL);
				}
			}

			// Validation type checks
			if ("NUM".equals(valType)) {
				sb.append(TAB + TAB + "if(isNaN(document.forms[0]." + fldRef + ".value))" + NL);
				sb.append(TAB + TAB + "{" + NL);
				sb.append(TAB + TAB + TAB + "alert(\"Enter " + lit + " in Numbers\");" + NL);
				sb.append(TAB + TAB + TAB + "document.forms[0]." + fldRef + ".focus();" + NL);
				sb.append(TAB + TAB + TAB + "return false;" + NL);
				sb.append(TAB + TAB + "}" + NL);
			} else if ("ALPHA".equals(valType)) {
				sb.append(TAB + TAB + "if(!/^[a-zA-Z]*$/.test(document.forms[0]." + fldRef + ".value))" + NL);
				sb.append(TAB + TAB + "{" + NL);
				sb.append(TAB + TAB + TAB + "alert(\"Enter " + lit + " in Alphabets only\");" + NL);
				sb.append(TAB + TAB + TAB + "document.forms[0]." + fldRef + ".focus();" + NL);
				sb.append(TAB + TAB + TAB + "return false;" + NL);
				sb.append(TAB + TAB + "}" + NL);
			} else if ("ALPHANUM".equals(valType)) {
				sb.append(TAB + TAB + "if(!/^[a-zA-Z0-9]*$/.test(document.forms[0]." + fldRef + ".value))" + NL);
				sb.append(TAB + TAB + "{" + NL);
				sb.append(TAB + TAB + TAB + "alert(\"Enter " + lit + " in Alphanumeric only\");" + NL);
				sb.append(TAB + TAB + TAB + "document.forms[0]." + fldRef + ".focus();" + NL);
				sb.append(TAB + TAB + TAB + "return false;" + NL);
				sb.append(TAB + TAB + "}" + NL);
			}

			// Custom validation for non-date fields
			if (!"e".equals(fieldType) && customVal != null && !customVal.isEmpty()) {
				sb.append(TAB + TAB + "if(!" + customVal + "()) { return false; }" + NL);
			}
		}

		sb.append(TAB + TAB + "return true;" + NL + NL);
		sb.append("}" + NL);

		writeToFile(sb.toString(), basePath + "javascripts" + File.separator + menuName + File.separator + menuName + "_det_link.js");
		System.out.println("Generated: javascripts/" + menuName + "/" + menuName + "_det_link.js");
	}

	// ===================== PRODUCT MENU JSP (UNCHANGED) =====================

	public void generateProductMenuJSP(ArrayList<?> fieldList, String basePath) {
		if (!basePath.endsWith(File.separator))
			basePath = basePath + File.separator;

		String mopId = "H" + menuName.toUpperCase();

		// Determine the page name from the first field's pageName
		String pageName = null;
		for (int i = 0; i < fieldList.size(); i++) {
			FieldDetails fd = (FieldDetails) fieldList.get(i);
			if (fd.getPageName() != null && !fd.getPageName().isEmpty()) {
				pageName = fd.getPageName();
				break;
			}
		}
		if (pageName == null) pageName = menuName;

		System.out.println("Generating Product Menu front-end JSP for: " + menuName);

		StringBuffer sb = new StringBuffer();
		sb.append(NL + "<script language=\"javascript\" >" + NL + NL + NL);
		sb.append("if(mopId ==\"" + mopId + "\"){" + NL);
		sb.append(TAB + "with (document)" + NL);
		sb.append(TAB + "{" + NL);

		// Hidden fields for dates
		for (int i = 0; i < fieldList.size(); i++) {
			FieldDetails fd = (FieldDetails) fieldList.get(i);
			if ("e".equals(fd.getFieldType())) {
				sb.append(TAB + TAB + "write('<input type=\"hidden\" id=\"date\" fdt=\"fdate\" mneb1=\"N\" vFldId=\"" + fd.getIdName() + "_ui\" name=\"." + fd.getIdName() + "\">');" + NL);
			}
		}

		sb.append(TAB + TAB + "write('<table border=\"0\" cellspacing=\"0\" cellpadding=\"0\" class=\"ctable\">');" + NL);
		sb.append(TAB + TAB + "write('<tr>');" + NL);
		sb.append(TAB + TAB + "write('<td>');" + NL);
		sb.append(TAB + TAB + "write('<br>');" + NL);
		sb.append(TAB + TAB + "write('<table border=\"0\" cellpadding=\"0\" cellspacing=\"0\" width=\"100%\">');" + NL);
		sb.append(TAB + TAB + "write('<tr>');" + NL);
		sb.append(TAB + TAB + "write('<td valign=\"top\">');" + NL);
		sb.append(TAB + TAB + "write('<table width=\"100%\" border=\"0\" cellpadding=\"0\" cellspacing=\"0\" class=\"table\">');" + NL);
		sb.append(TAB + TAB + "write('<tr>');" + NL);
		sb.append(TAB + TAB + "write('<td>');" + NL);
		sb.append(TAB + TAB + "write('<table width=\"100%\" border=\"0\" cellpadding=\"0\" cellspacing=\"0\" class=\"innertable\">');" + NL);
		sb.append(TAB + TAB + "write('<tr>');" + NL);
		sb.append(TAB + TAB + "write('<td>');" + NL);
		sb.append(TAB + TAB + "write('</td>');" + NL);
		sb.append(TAB + TAB + "write('</tr>');" + NL);

		// Custom fields
		int sLnkCounter = 3;
		for (int i = 0; i < fieldList.size(); i++) {
			FieldDetails fd = (FieldDetails) fieldList.get(i);
			if (!"Y".equals(fd.getIsCustomfld())) continue;

			String lit = (fd.getLiteralName() != null) ? fd.getLiteralName() : fd.getIdName();
			String mand = "Y".equals(fd.getMandatory()) ? "<font color =\"RED\"> * </font>" : "";
			String fieldType = fd.getFieldType();
			if (fieldType == null) fieldType = "a";

			sb.append(NL + TAB + TAB + "write('<tr>');" + NL);
			sb.append(TAB + TAB + "write('<td class=\"textlabel\">" + lit + mand + "');" + NL);
			sb.append(TAB + TAB + "write('</td>');" + NL);
			sb.append(TAB + TAB + "write('<td  class=\"textfield\">');" + NL);

			if ("a".equals(fieldType)) {
				sb.append(TAB + TAB + "write('<input type = \"text\" id=\"" + fd.getIdName() + "\" name=\"" + fd.getIdName() + "\" class=\"textfieldfont\">');" + NL);
			} else if ("b".equals(fieldType) || "c".equals(fieldType)) {
				sb.append(TAB + TAB + "write('<input type = \"text\" id=\"" + fd.getIdName() + "\" name=\"" + fd.getIdName() + "\" class=\"textfieldfont\">');" + NL);
				sb.append(TAB + TAB + "write('&nbsp; <a id=\"slnk" + (sLnkCounter++) + "\" href=\"javascript:" + fd.getIdName() + "Searcher()\">');" + NL);
				sb.append(TAB + TAB + "write('<img border=\"0\" height=\"17\" hotKeyId=\"search" + sLnkCounter + "\" src=\"../Renderer/images/'+applangcode+'/search_icon.gif\" width=\"16\">');" + NL);
				sb.append(TAB + TAB + "write('</a>');" + NL);
			} else if ("e".equals(fieldType)) {
				sb.append(TAB + TAB + "write('<input type = \"text\" id=\"" + fd.getIdName() + "_ui\" name=\"" + fd.getIdName() + "_ui\" class=\"textfieldfont\" fdt=\"uidate\">');" + NL);
				sb.append(TAB + TAB + "write('<a href=\"javascript:openDate(document.forms[0]." + fd.getIdName() + "_ui,BODDate)\" id=\"sLnk" + (sLnkCounter++) + "\"><img align=\"absmiddle\" alt=\"Date picker\" border=\"0\" height=\"19\" src=\"../Renderer/images/'+applangcode+'/calender.gif\" width=\"24\" class=\"img\">');" + NL);
				sb.append(TAB + TAB + "write('</a>');" + NL);
			} else if ("d".equals(fieldType)) {
				sb.append(TAB + TAB + "write('<select id=\"" + fd.getIdName() + "\" name=\"" + fd.getIdName() + "\" class=\"listboxfont\">');" + NL);
				sb.append(TAB + TAB + "write('<option value=\"\">--Select--</option>');" + NL);
				if (fd.getFieldTypeValues() != null) {
					String[] opts = fd.getFieldTypeValues().split(",");
					for (String opt : opts) {
						opt = opt.trim();
						sb.append(TAB + TAB + "write('<option value=\"" + opt + "\">" + opt + "</option>');" + NL);
					}
				}
				sb.append(TAB + TAB + "write('</select>');" + NL);
			} else if ("h".equals(fieldType)) {
				sb.append(TAB + TAB + "write('<textarea id=\"" + fd.getIdName() + "\" name=\"" + fd.getIdName() + "\" class=\"textfieldfont\" rows=\"3\" cols=\"30\"></textarea>');" + NL);
			} else {
				sb.append(TAB + TAB + "write('<input type = \"text\" id=\"" + fd.getIdName() + "\" name=\"" + fd.getIdName() + "\" class=\"textfieldfont\">');" + NL);
			}

			sb.append(TAB + TAB + "write('</td>');" + NL);
			sb.append(TAB + TAB + "write('</td>');" + NL);
			sb.append(TAB + TAB + "write('</tr>');" + NL);
		}

		// Close tables
		sb.append(NL + TAB + TAB + "write('<td class=\"textlabel\"> </td>');" + NL);
		sb.append(TAB + TAB + "write('<td class=\"textfield\"> </td>');" + NL);
		sb.append(TAB + TAB + "write('<td class=\"columnwidth\"> </td>');" + NL);
		sb.append(TAB + TAB + "write('<td class=\"textlabel\"> </td>');" + NL);
		sb.append(TAB + TAB + "write('<td class=\"textfield\"> </td>');" + NL);
		sb.append(TAB + TAB + "write('</tr>');" + NL);
		sb.append(TAB + TAB + "write('<tr>');" + NL);
		sb.append(TAB + TAB + "write('</table>');" + NL);
		sb.append(TAB + TAB + "write('</td>');" + NL);
		sb.append(TAB + TAB + "write('</tr>');" + NL);
		sb.append(TAB + TAB + "write('</table>');" + NL);
		sb.append(TAB + TAB + "write('</td>');" + NL);
		sb.append(TAB + TAB + "write('</tr>');" + NL);
		sb.append(TAB + TAB + "write('</tr>');" + NL);
		sb.append(TAB + TAB + "write('</table>');" + NL);
		sb.append(TAB + TAB + "write('<br>');" + NL);
		sb.append(NL + TAB + "}" + NL);
		sb.append("}" + NL + NL);

		// pre_ONCLICK - setFieldsToCustomData
		sb.append("function " + pageName + "crit_pre_ONCLICK()" + NL + "{" + NL);
		sb.append(TAB + "setFieldsToCustomData(");
		boolean first = true;
		for (int i = 0; i < fieldList.size(); i++) {
			FieldDetails fd = (FieldDetails) fieldList.get(i);
			if (!"Y".equals(fd.getIsCustomfld())) continue;
			if (!first) sb.append(",");
			sb.append("\"" + fd.getIdName() + "\"");
			first = false;
		}
		sb.append(");" + NL);
		sb.append("}" + NL + NL);

		// post_ONLOAD - getFieldsFromCustomData
		sb.append("function " + pageName + "crit_post_ONLOAD()" + NL + "{" + NL);
		sb.append(TAB + "getFieldsFromCustomData(");
		first = true;
		for (int i = 0; i < fieldList.size(); i++) {
			FieldDetails fd = (FieldDetails) fieldList.get(i);
			if (!"Y".equals(fd.getIsCustomfld())) continue;
			if (!first) sb.append(",");
			sb.append("\"" + fd.getIdName() + "\"");
			first = false;
		}
		sb.append(");" + NL);

		// Build input string
		sb.append(TAB + "var a=document.forms[0].customData.value" + NL);
		sb.append(TAB + "var input=");
		first = true;
		for (int i = 0; i < fieldList.size(); i++) {
			FieldDetails fd = (FieldDetails) fieldList.get(i);
			if (!"Y".equals(fd.getIsCustomfld())) continue;
			if (!first) sb.append("+\"|\"+" );
			sb.append("\"" + fd.getIdName() + "|\"+document.forms[0]." + fd.getIdName() + ".value");
			first = false;
		}
		sb.append(";" + NL);
		sb.append(TAB + "var output=\"\";" + NL);
		sb.append(TAB + "var scrName=\"" + fetchScrName + "\";" + NL);
		sb.append(TAB + "var res=appFnExecuteScript(input,output,scrName,true)" + NL);
		sb.append("}" + NL + NL);

		sb.append("</script>" + NL);

		writeToFile(sb.toString(), basePath + pageName + "crit_custom.jsp");
		System.out.println("Generated: " + pageName + "crit_custom.jsp");
	}
}
