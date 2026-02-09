package com.minorks.finAutomation;

import java.util.ArrayList;

import com.minorks.finAutomation.FieldDetails;

public class CallingMethods {

	final String doubleQuote = "\"";
	final String tabSpaceChar = "\t";
	final String nextLineChar = "\n";
	final String space = " ";
	int counter = 1;
	int counter1 = 1;
	String bindVars1 = "";
	boolean isValflg = false;

	public StringBuffer repCreation(String repName, String className, boolean isRepReqd) {
		StringBuffer sb = new StringBuffer();
		sb.append(tabSpaceChar + "#-----------------------#" + nextLineChar);
		sb.append(tabSpaceChar + "#  Creating Repositary  #"+ nextLineChar);
		sb.append(tabSpaceChar + "#-----------------------#" + nextLineChar);
		if (isRepReqd) {
			sb.append(tabSpaceChar + "IF (REPEXISTS(" + doubleQuote + repName + doubleQuote + ") == 0) THEN" + nextLineChar);
			sb.append(tabSpaceChar + "#{" + nextLineChar);
			sb.append(tabSpaceChar + tabSpaceChar + "CREATEREP(" + doubleQuote + repName + doubleQuote + ")" + nextLineChar);
			sb.append(tabSpaceChar + "#}" + nextLineChar);
			sb.append(tabSpaceChar + "ENDIF" + nextLineChar);
			sb.append(nextLineChar);
		}
		sb.append(tabSpaceChar + "#-----------------------#" + nextLineChar);
		sb.append(tabSpaceChar + "#  Creating Class       #"+ nextLineChar);
		sb.append(tabSpaceChar + "#-----------------------#" + nextLineChar);
		sb.append(tabSpaceChar + "IF (CLASSEXISTS(" + doubleQuote + repName + doubleQuote + "," + doubleQuote + className + doubleQuote
				+ ") == 0)THEN" + nextLineChar);
		sb.append(tabSpaceChar + "#{" + nextLineChar);
		sb.append(tabSpaceChar + tabSpaceChar + "CREATECLASS(" + doubleQuote + repName + doubleQuote + "," + doubleQuote + className + doubleQuote
				+ ",5)" + nextLineChar);
		sb.append(tabSpaceChar + "#}" + nextLineChar);
		sb.append(tabSpaceChar + "ENDIF" + nextLineChar);
		StringBuffer repCreationStr = sb;
		return repCreationStr;
	}

	public StringBuffer parseONSCustData(String repName, String className, ArrayList<?> getFlds) {
		StringBuffer sb1 = new StringBuffer();
		//System.out.println("size of array list:" + getFlds.size());
		sb1.append(tabSpaceChar + "sv_u = urhk_B2k_PrintRepos(" + doubleQuote + "ONS" + doubleQuote + ")" + nextLineChar);
		sb1.append(tabSpaceChar + "PRINT(sv_u)" + nextLineChar);
		sb1.append(nextLineChar);
		sb1.append(tabSpaceChar + "IF(FIELDEXISTS(ONS.CUSTOM.custDataIn)) THEN" + nextLineChar);
		sb1.append(tabSpaceChar + "#{" + nextLineChar);
		sb1.append(tabSpaceChar + tabSpaceChar + "sv_u = urhk_ParseONSCustData(ONS.CUSTOM.custDataIn) " + nextLineChar);
		sb1.append(tabSpaceChar + tabSpaceChar + "IF(sv_u == 0) THEN" + nextLineChar);
		sb1.append(tabSpaceChar + tabSpaceChar + "#{" + nextLineChar);

		String pageNameCom = "";
		
		for (int i = 0; i < getFlds.size(); i++) {
			FieldDetails fa = (FieldDetails) getFlds.get(i);
			String pageName = fa.getPageName();
			String isCustFld = fa.getIsCustomfld();
			String idName = fa.getIdName();
			if (isCustFld.equals("Y")) {
				if (pageName.equals(pageNameCom)) {
					counter++;
					sb1.append(tabSpaceChar + tabSpaceChar + tabSpaceChar + "IF(FIELDEXISTS(BANCS.INPUT." + pageName + "_FIELD_" + counter
							+ ")) THEN" + nextLineChar);
					sb1.append(tabSpaceChar + tabSpaceChar + tabSpaceChar + "#{" + nextLineChar);
					sb1.append(tabSpaceChar + tabSpaceChar + tabSpaceChar + tabSpaceChar + repName + "." + className + "." + idName
							+ " = BANCS.INPUT." + pageName + "_FIELD_" + counter + nextLineChar);
					sb1.append(tabSpaceChar + tabSpaceChar + tabSpaceChar + tabSpaceChar + "PRINT(" + repName + "." + className + "." + idName + ")"
							+ nextLineChar);
					sb1.append(tabSpaceChar + tabSpaceChar + tabSpaceChar + "#}" + nextLineChar);
					sb1.append(tabSpaceChar + tabSpaceChar + tabSpaceChar + "ELSE" + nextLineChar);
					sb1.append(tabSpaceChar + tabSpaceChar + tabSpaceChar + "#{" + nextLineChar);
					sb1.append(tabSpaceChar + tabSpaceChar + tabSpaceChar + tabSpaceChar + repName + "." + className + "." + idName + " = "
							+ doubleQuote + doubleQuote + nextLineChar);
					sb1.append(tabSpaceChar + tabSpaceChar + tabSpaceChar + "#}" + nextLineChar);
					sb1.append(tabSpaceChar + tabSpaceChar + tabSpaceChar + "ENDIF" + nextLineChar);
				} else {
					counter = 1;
					sb1.append(tabSpaceChar + tabSpaceChar + tabSpaceChar + "IF(FIELDEXISTS(BANCS.INPUT." + pageName + "_FIELD_" + counter
							+ ")) THEN" + nextLineChar);
					sb1.append(tabSpaceChar + tabSpaceChar + tabSpaceChar + "#{" + nextLineChar);
					sb1.append(tabSpaceChar + tabSpaceChar + tabSpaceChar + tabSpaceChar + repName + "." + className + "." + idName
							+ " = BANCS.INPUT." + pageName + "_FIELD_" + counter + nextLineChar);
					sb1.append(tabSpaceChar + tabSpaceChar + tabSpaceChar + tabSpaceChar + "PRINT(" + repName + "." + className + "." + idName + ")"
							+ nextLineChar);
					sb1.append(tabSpaceChar + tabSpaceChar + tabSpaceChar + "#}" + nextLineChar);
					sb1.append(tabSpaceChar + tabSpaceChar + tabSpaceChar + "ELSE" + nextLineChar);
					sb1.append(tabSpaceChar + tabSpaceChar + tabSpaceChar + "#{" + nextLineChar);
					sb1.append(tabSpaceChar + tabSpaceChar + tabSpaceChar + tabSpaceChar + repName + "." + className + "." + idName + " = "
							+ doubleQuote + doubleQuote + nextLineChar);
					sb1.append(tabSpaceChar + tabSpaceChar + tabSpaceChar + "#}" + nextLineChar);
					sb1.append(tabSpaceChar + tabSpaceChar + tabSpaceChar + "ENDIF" + nextLineChar);
				}
			}
			pageNameCom = pageName;
		}

		sb1.append(tabSpaceChar + tabSpaceChar + "#}" + nextLineChar);
		sb1.append(tabSpaceChar + tabSpaceChar + "ELSE" + nextLineChar);
		sb1.append(tabSpaceChar + tabSpaceChar + "#{" + nextLineChar);
		sb1.append(tabSpaceChar + tabSpaceChar + "\tGOTO ERRHANDLER" + nextLineChar);
		sb1.append(tabSpaceChar + tabSpaceChar + "#}" + nextLineChar);
		sb1.append(tabSpaceChar + tabSpaceChar + "ENDIF" + nextLineChar);
		sb1.append(tabSpaceChar + "#}" + nextLineChar);
		sb1.append(tabSpaceChar + "ELSE" + nextLineChar);
		sb1.append(tabSpaceChar + "#{" + nextLineChar);
		sb1.append(tabSpaceChar + tabSpaceChar + "GOTO ENDOFSCRIPT" + nextLineChar);
		sb1.append(tabSpaceChar + "#}" + nextLineChar);
		sb1.append(tabSpaceChar + "ENDIF" + nextLineChar);
		StringBuffer ONSCustRet = sb1;
		return ONSCustRet;
	}

	public StringBuffer parseBANCSCustData(String repName, String className, ArrayList<?> getFlds) {
		StringBuffer sb2 = new StringBuffer();
		//System.out.println("size of array list:" + getFlds.size());
		sb2.append(tabSpaceChar + "sv_u = urhk_B2k_PrintRepos(" + doubleQuote + "BANCS" + doubleQuote + ")" + nextLineChar);
		sb2.append(tabSpaceChar + "PRINT(sv_u)" + nextLineChar);
		sb2.append(nextLineChar);
		sb2.append(tabSpaceChar + "IF(sv_u == 0) THEN" + nextLineChar);
		sb2.append(tabSpaceChar + "#{" + nextLineChar);

		for (int i = 0; i < getFlds.size(); i++) {
			FieldDetails fa = (FieldDetails) getFlds.get(i);
			String idName = fa.getIdName();
			System.out.println("Name"+idName);
					System.out.println("IFidName"+idName);
					sb2.append(tabSpaceChar + tabSpaceChar + "IF(FIELDEXISTS(BANCS.INPUT." + idName 
							+ ")) THEN" + nextLineChar);
					sb2.append(tabSpaceChar + tabSpaceChar + "#{" + nextLineChar);
					sb2.append(tabSpaceChar + tabSpaceChar + tabSpaceChar + repName + "." + className + "." + idName
							+ " = BANCS.INPUT." + idName + nextLineChar);
					sb2.append(tabSpaceChar + tabSpaceChar + tabSpaceChar + "PRINT(" + repName + "." + className + "." + idName + ")"
							+ nextLineChar);
					sb2.append(tabSpaceChar + tabSpaceChar + "#}" + nextLineChar);
					sb2.append(tabSpaceChar + tabSpaceChar + "ELSE" + nextLineChar);
					sb2.append(tabSpaceChar + tabSpaceChar + "#{" + nextLineChar);
					sb2.append(tabSpaceChar + tabSpaceChar + tabSpaceChar + repName + "." + className + "." + idName + " = "
							+ doubleQuote + doubleQuote + nextLineChar);
					sb2.append(tabSpaceChar + tabSpaceChar + "#}" + nextLineChar);
					sb2.append(tabSpaceChar + tabSpaceChar + "ENDIF" + nextLineChar);
		}
		sb2.append(tabSpaceChar + "#}" + nextLineChar);
		sb2.append(tabSpaceChar + "ELSE" + nextLineChar);
		sb2.append(tabSpaceChar + "#{" + nextLineChar);
		sb2.append(tabSpaceChar + tabSpaceChar + "GOTO ENDOFSCRIPT" + nextLineChar);
		sb2.append(tabSpaceChar + "#}" + nextLineChar);
		sb2.append(tabSpaceChar + "ENDIF" + nextLineChar);
		StringBuffer BANCSCustRet = sb2;
		return BANCSCustRet;
	}
	public StringBuffer srvGetValToCustRep(String repName, String className, ArrayList<?> getFlds) {
		// TODO Auto-generated method stub
		StringBuffer sb2 = new StringBuffer();
		for (int i = 0; i < getFlds.size(); i++) {
			FieldDetails fa = (FieldDetails) getFlds.get(i);
			String pageName = fa.getPageName();
			String isCustFld = fa.getIsCustomfld();
			String idName = fa.getIdName();
			if (isCustFld.equals("N")) {
				sb2.append(tabSpaceChar + "sv_l = urhk_SRV_GetVal(" + doubleQuote + pageName + "." + idName + doubleQuote + ")" + nextLineChar);
				sb2.append(tabSpaceChar + "PRINT(sv_l)" + nextLineChar);
				sb2.append(nextLineChar);
				sb2.append(tabSpaceChar + "IF(sv_l == 0) THEN" + nextLineChar);
				sb2.append(tabSpaceChar + "#{" + nextLineChar);
				sb2.append(tabSpaceChar + tabSpaceChar + repName + "." + className + "." + idName + " = BANCS.OUTPARAM.srvValue" + nextLineChar);
				sb2.append(tabSpaceChar + "#}" + nextLineChar);
				sb2.append(tabSpaceChar + "ELSE" + nextLineChar);
				sb2.append(tabSpaceChar + "#{" + nextLineChar);
				sb2.append(tabSpaceChar + tabSpaceChar + repName + "." + className + "." + idName + " = " + doubleQuote + doubleQuote + nextLineChar);
				sb2.append(tabSpaceChar + "#}" + nextLineChar);
				sb2.append(tabSpaceChar + "ENDIF" + nextLineChar);
				sb2.append(tabSpaceChar + "PRINT(" + repName + "." + className + "." + idName + ")" + nextLineChar);
			}
		}
		StringBuffer srvGet = sb2;
		return srvGet;
	}

	public StringBuffer selectCount(String repName, String className, String selectStr, ArrayList<?> getFlds, String tableName, boolean isCustMenuRqd) {
		// TODO Auto-generated method stub
		StringBuffer sb3 = new StringBuffer();
		sb3.append(tabSpaceChar + "sv_s = " + doubleQuote + doubleQuote + nextLineChar);
		sb3.append(tabSpaceChar + "sv_s = sv_s + " + doubleQuote + selectStr + "|SELECT COUNT(1), ENTITY_CRE_FLG, DEL_FLG" + doubleQuote + nextLineChar);
		sb3.append(tabSpaceChar + "sv_s = sv_s + " + doubleQuote + " FROM " + tableName + doubleQuote + nextLineChar);



		String functionReturn = buildWhereCondition(repName, className, getFlds,isCustMenuRqd);
		System.out.println("Where Condition:" + functionReturn);
		String[] qry = functionReturn.split("~");

		sb3.append(qry[0]);
		sb3.append(tabSpaceChar + "sv_s = sv_s + " + doubleQuote + " GROUP BY ENTITY_CRE_FLG, DEL_FLG" + doubleQuote + nextLineChar);
		sb3.append(tabSpaceChar + "PRINT(sv_s)" + nextLineChar);
		sb3.append(nextLineChar);
		sb3.append(qry[1] + nextLineChar);
		sb3.append(tabSpaceChar + "PRINT(BANCS.INPARAM.BINDVARS)" + nextLineChar);
		sb3.append(nextLineChar);
		
		sb3.append(tabSpaceChar + "sv_b = urhk_dbSelectWithBind(sv_s)" + nextLineChar);
		sb3.append(tabSpaceChar + "PRINT(sv_b)" + nextLineChar);
		sb3.append(nextLineChar);
		sb3.append(tabSpaceChar + "IF(sv_b == 0) THEN" + nextLineChar);
		sb3.append(tabSpaceChar + "#{ " + nextLineChar);
		
		String []temp = selectStr.split(",");		
		for (int i=0; i<temp.length; i++) {
		sb3.append(tabSpaceChar + tabSpaceChar + repName + "." + className + "." + temp[i] + " = BANCS.OUTPARAM." + temp[i] + nextLineChar);
		sb3.append(tabSpaceChar + tabSpaceChar + "PRINT(" + repName + "." + className + "." + temp[i] + ")" + nextLineChar);
		}
		sb3.append(tabSpaceChar + "#}" + nextLineChar);
		sb3.append(tabSpaceChar + "ELSE" + nextLineChar);
		sb3.append(tabSpaceChar + "#{ " + nextLineChar);
		sb3.append(tabSpaceChar + tabSpaceChar + repName + "." + className + ".ERRMSG = " + doubleQuote + "Query Execution Failed" + doubleQuote
				+ nextLineChar);
		//sb3.append(tabSpaceChar + tabSpaceChar + "GOTO ERRHANDLER " + nextLineChar);
		sb3.append(tabSpaceChar + "#}" + nextLineChar);
		sb3.append(tabSpaceChar + "ENDIF" + nextLineChar);
		StringBuffer selCount = sb3;
		return selCount;
	}

	public String buildWhereCondition(String repName, String className, ArrayList<?> getFlds,boolean isValflg) {
		// TODO Auto-generated method stub
		String whereQuery = tabSpaceChar + "sv_s = sv_s + " + doubleQuote + " WHERE BANK_ID = ?SVAR" + doubleQuote + nextLineChar;
		String bindVars =  tabSpaceChar + "BANCS.INPARAM.BINDVARS = " + repName + "." + className + ".bankId";
		
		for (int i = 0; i < getFlds.size(); i++) {
			FieldDetails fa = (FieldDetails) getFlds.get(i);
			String isKeyFld = fa.getIsKeyfld();
			String fldName = fa.getFldName();
			String idName = fa.getIdName();
		
			if (isKeyFld.equals("Y")) {
				if (!fa.getDataType().equalsIgnoreCase("NUMBER"))
				{
					if(isValflg)
						whereQuery = whereQuery + tabSpaceChar + tabSpaceChar;
					else
						whereQuery = whereQuery + tabSpaceChar;
					whereQuery = whereQuery + "sv_s = sv_s + " + doubleQuote + " AND " + fldName + " = ?SVAR" + doubleQuote + nextLineChar;
				}
				else 
				{
					if(isValflg)
						whereQuery = whereQuery + tabSpaceChar + tabSpaceChar;
					else
						whereQuery = whereQuery + tabSpaceChar;
					whereQuery = whereQuery + tabSpaceChar + "sv_s = sv_s + " + doubleQuote + " AND " + fldName + " = ?NVAR" + doubleQuote + nextLineChar;
				}
				bindVars = bindVars + " + " + doubleQuote + "|" + doubleQuote + " + " + repName + "." + className + "." + idName;
			}

		}
		String whereCondition ;
		if(isValflg)
			 whereCondition =  whereQuery  + "~" + tabSpaceChar + bindVars;
		else
		 whereCondition =  whereQuery  + "~"  + bindVars;
		return whereCondition;
	}

	public StringBuffer endOfScript() {
		// TODO Auto-generated method stub
		StringBuffer sb4 = new StringBuffer();
		sb4.append("ENDOFSCRIPT: " + nextLineChar);
		sb4.append(tabSpaceChar + "EXITSCRIPT " + nextLineChar);
		StringBuffer endOfScr = sb4;
		return endOfScr;
	}

	public StringBuffer errorHandler(String repName, String className,boolean isCustMenuRqd,String fsFlg) {
		// TODO Auto-generated method stub
		StringBuffer sb5 = new StringBuffer();
		sb5.append("ERRHANDLER: " + nextLineChar);
		sb5.append(tabSpaceChar + "PRINT(" + repName + "." + className + ".ERRMSG)" + nextLineChar);
		if(isCustMenuRqd){
			if(fsFlg.equals("F")){
		sb5.append(tabSpaceChar + "sv_u = urhk_SetOrbOut(" + doubleQuote + "Error_01|ERROR^" +  doubleQuote +  "+" + repName + "." + className + ".ERRMSG" + "+" + doubleQuote + "^dummy" +  doubleQuote+  ")" +    nextLineChar);
			sb5.append(tabSpaceChar + "PRINT(sv_u) " + nextLineChar);}}
		sb5.append(nextLineChar);
		StringBuffer errorHand = sb5;
		return errorHand;
	}

	public StringBuffer selectQuery(String repName, String className, ArrayList<?> getFlds,boolean isCustMenuRqd,boolean isValflg) {
		// TODO Auto-generated method stub
		StringBuffer sb9 = new StringBuffer();
		String fieldValues = "";
		String custRepVal = "";
		String pageName = "";
		System.out.println("isCust"+isCustMenuRqd);
		for (int i = 0; i < getFlds.size(); i++) 
		{
			FieldDetails fa = (FieldDetails) getFlds.get(i);
			pageName = fa.getPageName();
			String isCustFld = fa.getIsCustomfld();
			String idName = fa.getIdName();
			String fldName = fa.getFldName();
			if ((isCustFld.equals("Y")) || (isCustMenuRqd)){
				custRepVal = custRepVal + idName + ",";
				if(fa.getDataType().equalsIgnoreCase("DATE"))
				{
					fieldValues = fieldValues + "to_char(" + fldName + ",'DD-MM-YYYY') ,";
				}
				else
				{
					fieldValues = fieldValues + fldName + ",";
				}
			}
		}
		fieldValues = fieldValues.substring(0, fieldValues.length()-1);
		custRepVal = custRepVal.substring(0, custRepVal.length()-1);
		System.out.println("Select Query Result1:"+fieldValues);
		System.out.println("Select Query Result2:"+custRepVal);

		sb9.append(tabSpaceChar + "sv_s = " + doubleQuote + doubleQuote + nextLineChar);
		sb9.append(tabSpaceChar + "sv_s = sv_s + " + doubleQuote + custRepVal + "|" + doubleQuote + nextLineChar);
		sb9.append(tabSpaceChar + "sv_s = sv_s + " + doubleQuote +"SELECT " + fieldValues + doubleQuote + nextLineChar);
		sb9.append(tabSpaceChar + "sv_s = sv_s + " + doubleQuote + " FROM " + doubleQuote + "+" + repName + "." + className + ".tableName " + nextLineChar);

		String fetchReturn3 = buildWhereCondition(repName, className, getFlds,isValflg);
		String[] qry = fetchReturn3.split("~");
		sb9.append(qry[0]);
		sb9.append(nextLineChar);
		sb9.append(tabSpaceChar + "PRINT(sv_s) " + nextLineChar);
		sb9.append(qry[1] + nextLineChar);
		sb9.append(tabSpaceChar + "PRINT(BANCS.INPARAM.BINDVARS) " + nextLineChar);
		sb9.append(tabSpaceChar + "GOTO EXECQRY " + nextLineChar);
		sb9.append(nextLineChar);

		sb9.append("EXECQRY: " + nextLineChar);
		sb9.append(tabSpaceChar + "sv_b = urhk_dbSelectWithBind(sv_s) " + nextLineChar);
		sb9.append(tabSpaceChar + "PRINT(sv_b) " + nextLineChar);
		sb9.append(nextLineChar);
		sb9.append(tabSpaceChar + "IF(sv_b == 0) THEN " + nextLineChar);
		sb9.append(tabSpaceChar + "#{ " + nextLineChar);
		String []temp = custRepVal.split(",");
		for (int i=0; i<temp.length; i++) {
			sb9.append(tabSpaceChar + tabSpaceChar + repName + "." + className + "." + temp[i] + " = BANCS.OUTPARAM." + temp[i] + nextLineChar);
			sb9.append(tabSpaceChar + tabSpaceChar + "PRINT(" + repName + "." + className + "." + temp[i] + ")" + nextLineChar);
		}
		for (int j=0;j <temp.length; j++)
		{
			FieldDetails fa = (FieldDetails) getFlds.get(j);
			String idName = fa.getIdName();
			sb9.append(tabSpaceChar + tabSpaceChar + "sv_u = urhk_SetOrbOut(" + doubleQuote  + idName.concat("|") + space   + doubleQuote +  " + " + repName + "." + className + "." + idName + ")" + nextLineChar);
			sb9.append(tabSpaceChar + tabSpaceChar + "PRINT(sv_u) " + nextLineChar);}
		sb9.append(tabSpaceChar + "#} " + nextLineChar);
		sb9.append(tabSpaceChar + "ELSE " + nextLineChar);
		sb9.append(tabSpaceChar + "#{ " + nextLineChar);
		sb9.append(tabSpaceChar + tabSpaceChar + repName + "." + className + ".ERRMSG = " + doubleQuote + "Error while executing query" + doubleQuote + " " + nextLineChar);
		sb9.append(tabSpaceChar + tabSpaceChar + "GOTO ERRHANDLER " + nextLineChar);
		sb9.append(tabSpaceChar + "#} " + nextLineChar);
		sb9.append(tabSpaceChar + "ENDIF " + nextLineChar);
		sb9.append(nextLineChar);
		
		
		String pageNameCom = "";
		String sCustDataOut = "";
		for (int i = 0; i < getFlds.size(); i++) {
			FieldDetails fa = (FieldDetails) getFlds.get(i);
			pageName = fa.getPageName();
			String isCustFld = fa.getIsCustomfld();
			String idName = fa.getIdName();
			if (isCustFld.equals("Y")) {
				if(!isCustMenuRqd)
				{
					if (pageName.equals(pageNameCom)) {
						sCustDataOut = sCustDataOut.concat(" + " + repName + "." + className + "." + idName + " + " + doubleQuote + "|" + doubleQuote);
					} else {
					if (!sCustDataOut.equals(""))
						sCustDataOut = sCustDataOut.concat( " + ");
						sCustDataOut = sCustDataOut.concat(doubleQuote + "~" + pageName + "|" + doubleQuote + " + " + repName + "." + className + "." + idName + " + " + doubleQuote + "|" + doubleQuote );
					}		
				}
			}
			pageNameCom = pageName;
		}		
		
		sb9.append(nextLineChar);
		
		if(!isCustMenuRqd){
		sb9.append(tabSpaceChar + "ONS.CUSTOM.custDataOut = "  + sCustDataOut + nextLineChar);
		sb9.append(tabSpaceChar + "print(ONS.CUSTOM.custDataOut) " + nextLineChar);}
		else{
			sb9.append("NEXTPAGE: " + nextLineChar);
			sb9.append(tabSpaceChar + "sv_u = urhk_SetOrbOut(" + doubleQuote + "SuccessOrFailure|"  + "Y" + doubleQuote + ")" + nextLineChar);
			sb9.append(tabSpaceChar + "PRINT(sv_u) " + nextLineChar);
		}
		StringBuffer selectRet = sb9;
		return selectRet;
	}
	
	public StringBuffer buildInsertQry(String repName, String className, ArrayList<?> getFlds) {
		// TODO Auto-generated method stub
		StringBuffer sb6 = new StringBuffer();
		String fieldValues = "";
		String svarValues = "";
		String bindValues = "";

		for (int i = 0; i < getFlds.size(); i++) {
			FieldDetails fa = (FieldDetails) getFlds.get(i);
			String isCustFld = fa.getIsCustomfld();
			String fldName = fa.getFldName();
			String idName = fa.getIdName();
			if (isCustFld.equals("Y")) {
				if (fa.getDataType().equalsIgnoreCase("NUMBER"))
				{
					fieldValues = fieldValues +  fldName + ",";
					svarValues = svarValues + "?NVAR,";
				}
				else if(fa.getDataType().equalsIgnoreCase("DATE"))
				{
					fieldValues = fieldValues +  fldName + ",";
					svarValues = svarValues + "to_date(?SVAR,'DD-MM-YYYY'),";
				}
				else 
				{
					fieldValues = fieldValues +  fldName + ",";
					svarValues = svarValues + "?SVAR,";
				}
				bindValues = bindValues + repName + "." + className + "." + idName + " + " + doubleQuote + "|" + doubleQuote + " + ";
			}
			else
			{
				fieldValues = fieldValues +  fldName + ",";
				svarValues = svarValues + "?SVAR,";
				bindValues = bindValues + repName + "." + className + "." + idName + " + " + doubleQuote + "|" + doubleQuote + " + ";
			}
		}

		String fieldValues1 = "ENTITY_CRE_FLG,DEL_FLG,RCRE_USER_ID,RCRE_TIME,LCHG_USER_ID,LCHG_TIME,BANK_ID";
		String svarValues1 = "?SVAR,?SVAR,?SVAR,to_date(?SVAR,'DD-MM-YYYY'),?SVAR,?SVAR,?SVAR";
		fieldValues = fieldValues + fieldValues1;
		svarValues = svarValues + svarValues1;

		sb6.append(tabSpaceChar + "sv_s = " + doubleQuote + doubleQuote + nextLineChar);
		sb6.append(tabSpaceChar + "sv_s = sv_s + " +doubleQuote + "INSERT INTO " + doubleQuote + " + " +repName + "." + className + ".tableName" + nextLineChar);
		sb6.append(tabSpaceChar + "sv_s = sv_s + " + doubleQuote + "(" + fieldValues + ")" +doubleQuote + nextLineChar);
		sb6.append(tabSpaceChar + "sv_s = sv_s + " + doubleQuote + " VALUES(" + svarValues + ")" + doubleQuote + nextLineChar);
		sb6.append(tabSpaceChar + "PRINT(sv_s)" + nextLineChar);
		sb6.append(nextLineChar); 
		sb6.append(tabSpaceChar + "BANCS.INPARAM.BINDVARS = " + bindValues + repName + "." + className + ".updEntCreFlg" + nextLineChar);
		sb6.append(tabSpaceChar + "BANCS.INPARAM.BINDVARS = BANCS.INPARAM.BINDVARS + " + doubleQuote + "|" + doubleQuote + " + "  + repName + "." + className + ".updDelFlg" + nextLineChar);
		sb6.append(tabSpaceChar + "BANCS.INPARAM.BINDVARS = BANCS.INPARAM.BINDVARS + " + doubleQuote + "|" + doubleQuote + " + "  + repName + "." + className + ".rcreUserId" + nextLineChar);
		sb6.append(tabSpaceChar + "BANCS.INPARAM.BINDVARS = BANCS.INPARAM.BINDVARS + " + doubleQuote + "|" + doubleQuote + " + "  + repName + "." + className + ".rcreTime" + nextLineChar);
		sb6.append(tabSpaceChar + "BANCS.INPARAM.BINDVARS = BANCS.INPARAM.BINDVARS + " + doubleQuote + "|" + doubleQuote + " + "  + repName + "." + className + ".lchgUserId" + nextLineChar);
		sb6.append(tabSpaceChar + "BANCS.INPARAM.BINDVARS = BANCS.INPARAM.BINDVARS + " + doubleQuote + "|" + doubleQuote + " + "  + repName + "." + className + ".lchgTime" + nextLineChar);
		sb6.append(tabSpaceChar + "BANCS.INPARAM.BINDVARS = BANCS.INPARAM.BINDVARS + " + doubleQuote + "|" + doubleQuote + " + "  + repName + "." + className + ".bankId" + nextLineChar);
		sb6.append(tabSpaceChar + "PRINT(BANCS.INPARAM.BINDVARS)" + nextLineChar);
		sb6.append(nextLineChar);  
		sb6.append(tabSpaceChar + "GOSUB EXECQRY" + nextLineChar);
		sb6.append(tabSpaceChar + "GOTO ENDOFSCRIPT" + nextLineChar);
		
		StringBuffer insertRet = sb6;
		return insertRet;
	}

	public StringBuffer buildUpdateQry(String repName, String className, ArrayList<?> getFlds, boolean isValflg) {
		// TODO Auto-generated method stub
		StringBuffer sb7 = new StringBuffer();
		sb7.append(tabSpaceChar + "sv_s = " + doubleQuote + doubleQuote + nextLineChar);
		sb7.append(tabSpaceChar + "sv_s = sv_s + " +doubleQuote +"UPDATE " + doubleQuote + " + " + repName + "." + className + ".tableName" + nextLineChar);

		String fieldValuesUpdate = "";
		String bindValuesUpdate = "";

		for (int i = 0; i < getFlds.size(); i++) {
			FieldDetails fa = (FieldDetails) getFlds.get(i);
			String isCustFld = fa.getIsCustomfld();
			String fldName = fa.getFldName();
			String idName = fa.getIdName();
			System.out.println("update size:" + getFlds.size());
			System.out.println("update size1:" + i);
			if(i != 0)
			{
				if (isCustFld.equals("Y")) {
					if (fa.getDataType().equalsIgnoreCase("NUMBER"))
					{
						fieldValuesUpdate = fieldValuesUpdate + tabSpaceChar + "sv_s = sv_s + " + doubleQuote + " " + fldName + " = ?NVAR ," + doubleQuote + nextLineChar;
					}
					else if(fa.getDataType().equalsIgnoreCase("DATE"))
					{
						fieldValuesUpdate = fieldValuesUpdate + tabSpaceChar + "sv_s = sv_s + " + doubleQuote + " " + fldName + " = to_date(?SVAR,'DD-MM-YYYY') ," + doubleQuote + nextLineChar;
					}
					else
					{
						fieldValuesUpdate = fieldValuesUpdate + tabSpaceChar + "sv_s = sv_s + " + doubleQuote + " " + fldName + " = ?SVAR ," + doubleQuote + nextLineChar;
					}
					bindValuesUpdate = bindValuesUpdate + tabSpaceChar + "BANCS.INPARAM.BINDVARS = BANCS.INPARAM.BINDVARS + " + doubleQuote + "|" + doubleQuote + " + " + repName + "." + className + "." + idName + nextLineChar;
				}
			}
			else
			{
				fieldValuesUpdate = fieldValuesUpdate + tabSpaceChar + "sv_s = sv_s + " + doubleQuote + " SET ";
				if (fa.getDataType().equalsIgnoreCase("NUMBER"))
				{
					fieldValuesUpdate = fieldValuesUpdate +  fldName + " = ?NVAR ," + doubleQuote + nextLineChar;
				}
				else if(fa.getDataType().equalsIgnoreCase("DATE"))
				{
					fieldValuesUpdate = fieldValuesUpdate + fldName + " = to_date(?SVAR,'DD-MM-YYYY') ," + doubleQuote + nextLineChar;
				}
				else
				{
					fieldValuesUpdate = fieldValuesUpdate + fldName + " = ?SVAR ," + doubleQuote + nextLineChar;
				}
				bindValuesUpdate = bindValuesUpdate + "BANCS.INPARAM.BINDVARS = " + repName + "." + className + "." + idName + nextLineChar;
				//+ doubleQuote + nextLineChar;
			}
		}

		String fieldValuesUpdate1 = tabSpaceChar+ "sv_s = sv_s + " + doubleQuote + " ENTITY_CRE_FLG = ?SVAR, DEL_FLG = ?SVAR," + doubleQuote + nextLineChar;
		fieldValuesUpdate1 = fieldValuesUpdate1 + tabSpaceChar + "sv_s = sv_s + " + doubleQuote + " LCHG_USER_ID = ?SVAR, LCHG_TIME = to_date(?SVAR,'DD-MM-YYYY')" + doubleQuote + nextLineChar;
		fieldValuesUpdate = fieldValuesUpdate + fieldValuesUpdate1;
		System.out.println("UPDATE FIELD VALUES:"+fieldValuesUpdate);
		System.out.println("UPDATE BINDVARS:"+bindValuesUpdate);
		
		
		sb7.append(fieldValuesUpdate);
		String functionReturn1 = buildWhereCondition(repName, className, getFlds,isValflg);
		String[] qry = functionReturn1.split("~");
		sb7.append(qry[0]);
		sb7.append(tabSpaceChar+"PRINT(sv_s) "+nextLineChar);
		sb7.append(nextLineChar);
		sb7.append(tabSpaceChar + bindValuesUpdate + nextLineChar);
		sb7.append(tabSpaceChar + "BANCS.INPARAM.BINDVARS = BANCS.INPARAM.BINDVARS + " + doubleQuote + "|" + doubleQuote + " + " + repName + "." + className + ".updEntCreFlg" + nextLineChar);
		sb7.append(tabSpaceChar + "BANCS.INPARAM.BINDVARS = BANCS.INPARAM.BINDVARS + " + doubleQuote + "|" + doubleQuote + " + " + repName + "." + className + ".updDelFlg" + nextLineChar);
		sb7.append(tabSpaceChar + "BANCS.INPARAM.BINDVARS = BANCS.INPARAM.BINDVARS + " + doubleQuote + "|" + doubleQuote + " + " + repName + "." + className + ".lchgUserId" + nextLineChar);
		sb7.append(tabSpaceChar + "BANCS.INPARAM.BINDVARS = BANCS.INPARAM.BINDVARS + " + doubleQuote + "|" + doubleQuote + " + " + repName + "." + className + ".lchgTime" + nextLineChar);
		String[] qry1 = qry[1].split("=");
		String bindVar = qry1[0] + "= " +  qry1[0].replaceAll("\\s", "") + " + " + doubleQuote + "|" + doubleQuote + " + " + qry1[1];
		sb7.append(bindVar + nextLineChar);
		sb7.append(tabSpaceChar + "PRINT(BANCS.INPARAM.BINDVARS)" + nextLineChar);
		sb7.append(nextLineChar);
		sb7.append(tabSpaceChar+"GOSUB EXECQRY "+nextLineChar);
		sb7.append(nextLineChar);
		StringBuffer updateRet = sb7;
		return updateRet;
	}

	public StringBuffer buildDeleteQry(String repName, String className, ArrayList<?> getFlds, boolean isValflg) {
		// TODO Auto-generated method stub
		StringBuffer sb8 = new StringBuffer();
		sb8.append(tabSpaceChar + "sv_s = " + doubleQuote + doubleQuote + nextLineChar);
		sb8.append(tabSpaceChar + "sv_s = sv_s +" + doubleQuote +"DELETE "+ doubleQuote + " + " + repName + "." + className + ".tableName" + nextLineChar);
		String functionReturn3 = buildWhereCondition(repName, className, getFlds,isValflg);
		String[] qry = functionReturn3.split("~");
		sb8.append(qry[0]);
		sb8.append(tabSpaceChar + "PRINT(sv_s) "+nextLineChar);
		sb8.append(nextLineChar);
		sb8.append(qry[1] + nextLineChar);
		sb8.append(tabSpaceChar + "PRINT(BANCS.INPARAM.BINDVARS)" + nextLineChar);
		sb8.append(nextLineChar);
		sb8.append(tabSpaceChar + "GOSUB EXECQRY" + nextLineChar);
		sb8.append(tabSpaceChar + "GOTO ENDOFSCRIPT" + nextLineChar);
		StringBuffer deleteRet = sb8;
		return deleteRet;
	}
	
	public StringBuffer setKeyFld(String repName, String className, ArrayList<?> getFlds) {
		// TODO Auto-generated method stub
		StringBuffer sb10 = new StringBuffer();
		sb10.append(tabSpaceChar + repName + "." + className + "." + "userId" + " = " + "BANCS" + "." + "STDIN" + "." + "userId" + nextLineChar);
		for (int i = 0; i < getFlds.size(); i++) {
			FieldDetails fa = (FieldDetails) getFlds.get(i);
			//String pageName = fa.getPageName();
			String isCustFld = fa.getIsCustomfld();
			String idName = fa.getIdName();
			if (isCustFld.equals("N")) {
				sb10.append(tabSpaceChar + repName + "." + className + "." + idName + " = " + "BANCS" + "." + "INPUT" + "." + idName + nextLineChar);
				sb10.append(tabSpaceChar + "PRINT(" + repName + "." + className + "." + idName + ")" + nextLineChar);
			}
		}
		StringBuffer keyFld = sb10;
		return keyFld;
	}
	public StringBuffer validateQry(String repName, String className, ArrayList<?> getFlds,boolean isCustMenuRqd, String mainSynName,boolean isModTabRqd) {
		
		// TODO Auto-generated method stub
		StringBuffer sb11 = new StringBuffer();
		isValflg = true;
		sb11.append("VALIDATE:" + nextLineChar);
		sb11.append(tabSpaceChar + "####Validation Query" + nextLineChar);
		sb11.append(tabSpaceChar + "IF(" + repName + "." + className + ".function != " + doubleQuote + "A" + doubleQuote + ")" + " THEN" + nextLineChar );
		sb11.append(tabSpaceChar + "#{ " + nextLineChar);
		sb11.append(tabSpaceChar + tabSpaceChar + "sv_s = " + doubleQuote + doubleQuote + nextLineChar);
		sb11.append(tabSpaceChar + tabSpaceChar + "sv_s = sv_s + " + doubleQuote + "User,entityCreFlg|SELECT RCRE_USER_ID,ENTITY_CRE_FLG" + doubleQuote + nextLineChar);
		if(!isModTabRqd){
		sb11.append(tabSpaceChar + tabSpaceChar + "sv_s = sv_s + " + doubleQuote + " FROM " + mainSynName + doubleQuote + nextLineChar);}
		else{
			sb11.append(tabSpaceChar + tabSpaceChar + "sv_s = sv_s + " + doubleQuote + " FROM " + doubleQuote + "+" + repName + "." + className + ".tableName " + nextLineChar);}
		String fetchReturn4 =  tabSpaceChar + buildWhereCondition(repName, className, getFlds,isValflg);
		String[] qry1 = fetchReturn4.split("~");
		sb11.append(qry1[0]);
		sb11.append(tabSpaceChar + tabSpaceChar + "PRINT(sv_s) " + nextLineChar);
		sb11.append(qry1[1] + nextLineChar);
		sb11.append(tabSpaceChar + tabSpaceChar + "PRINT(BANCS.INPARAM.BINDVARS) " + nextLineChar);
		sb11.append(tabSpaceChar + tabSpaceChar + "sv_b = urhk_dbSelectWithBind(sv_s) " + nextLineChar);
		sb11.append(tabSpaceChar + tabSpaceChar + "PRINT(sv_b) " + nextLineChar);
		sb11.append(nextLineChar);
		sb11.append(tabSpaceChar + tabSpaceChar + "IF(sv_b == 0) THEN " + nextLineChar);
		sb11.append(tabSpaceChar + tabSpaceChar +  "#{ " + nextLineChar);
		sb11.append(tabSpaceChar + tabSpaceChar + tabSpaceChar + repName + "." + className + ".User = BANCS.OUTPARAM.User" + nextLineChar);
		sb11.append(tabSpaceChar + tabSpaceChar + tabSpaceChar + repName + "." + className + ".entityCreFlg = BANCS.OUTPARAM.entityCreFlg" + nextLineChar);
		sb11.append(tabSpaceChar + tabSpaceChar + tabSpaceChar + "IF(" + repName + "." + className + ".User != " + repName + "." + className + ".userId)" + " THEN" + nextLineChar );
		sb11.append(tabSpaceChar + tabSpaceChar + tabSpaceChar + "#{ " + nextLineChar);
		sb11.append(tabSpaceChar + tabSpaceChar + tabSpaceChar + tabSpaceChar + "IF(" + repName + "." + className + ".function == " + doubleQuote + "M" + doubleQuote + ")" + " THEN" + nextLineChar );
		sb11.append(tabSpaceChar + tabSpaceChar + tabSpaceChar + tabSpaceChar + "#{ " + nextLineChar);
		sb11.append(tabSpaceChar + tabSpaceChar + tabSpaceChar + tabSpaceChar + tabSpaceChar + repName + "." + className + ".ERRMSG =" + doubleQuote + "Only the Same User can Modify this Record" + doubleQuote
				+ nextLineChar);
		sb11.append(tabSpaceChar + tabSpaceChar + tabSpaceChar+ tabSpaceChar + tabSpaceChar + "GOTO ERRHANDLER " + nextLineChar);
		sb11.append(tabSpaceChar + tabSpaceChar + tabSpaceChar + tabSpaceChar + "#} " + nextLineChar);
		sb11.append(tabSpaceChar + tabSpaceChar + tabSpaceChar + tabSpaceChar+ "ENDIF	 " + nextLineChar);
		sb11.append(tabSpaceChar + tabSpaceChar + tabSpaceChar + "#} " + nextLineChar);
		sb11.append(tabSpaceChar + tabSpaceChar + tabSpaceChar + "ELSE	 " + nextLineChar);
		sb11.append(tabSpaceChar + tabSpaceChar + tabSpaceChar + "#{ " + nextLineChar);
		sb11.append(tabSpaceChar + tabSpaceChar + tabSpaceChar + tabSpaceChar + "IF(" + repName + "." + className + ".function == " + doubleQuote + "V" + doubleQuote + ")" + " THEN" + nextLineChar );
		sb11.append(tabSpaceChar + tabSpaceChar + tabSpaceChar + tabSpaceChar + "#{ " + nextLineChar);
		sb11.append(tabSpaceChar + tabSpaceChar + tabSpaceChar + tabSpaceChar + tabSpaceChar + repName + "." + className + ".ERRMSG =" + doubleQuote + "Same User Cannot Verify the Record" + doubleQuote
				+ nextLineChar);
		sb11.append(tabSpaceChar + tabSpaceChar + tabSpaceChar+ tabSpaceChar + tabSpaceChar + "GOTO ERRHANDLER " + nextLineChar);
		sb11.append(tabSpaceChar + tabSpaceChar + tabSpaceChar + tabSpaceChar + "#} " + nextLineChar);
		sb11.append(tabSpaceChar + tabSpaceChar + tabSpaceChar + tabSpaceChar+ "ENDIF	 " + nextLineChar);
		sb11.append(tabSpaceChar + tabSpaceChar + tabSpaceChar + "#} " + nextLineChar);
		sb11.append(tabSpaceChar + tabSpaceChar + tabSpaceChar + "ENDIF	 " + nextLineChar);
		sb11.append(tabSpaceChar + tabSpaceChar + tabSpaceChar + "IF(" + repName + "." + className + ".function == " + doubleQuote + "D" + doubleQuote + ")" + " THEN"  + nextLineChar );
		sb11.append(tabSpaceChar + tabSpaceChar + tabSpaceChar + "#{ " + nextLineChar);
		sb11.append(tabSpaceChar + tabSpaceChar + tabSpaceChar + tabSpaceChar + "IF(" + repName + "." + className + ".entityCreFlg == " + doubleQuote + "N" + doubleQuote + ")" + " THEN" + nextLineChar );
		sb11.append(tabSpaceChar + tabSpaceChar + tabSpaceChar + tabSpaceChar +  "#{ " + nextLineChar);
		sb11.append(tabSpaceChar + tabSpaceChar + tabSpaceChar + tabSpaceChar + tabSpaceChar + repName + "." + className + ".ERRMSG =" + doubleQuote + "Verified Records Only Deleted" + doubleQuote
				+ nextLineChar);
		sb11.append(tabSpaceChar + tabSpaceChar + tabSpaceChar+ tabSpaceChar + tabSpaceChar + "GOTO ERRHANDLER " + nextLineChar);
		sb11.append(tabSpaceChar + tabSpaceChar + tabSpaceChar + tabSpaceChar + "#} " + nextLineChar);
		sb11.append(tabSpaceChar + tabSpaceChar + tabSpaceChar + tabSpaceChar+ "ENDIF	 " + nextLineChar);
		sb11.append(tabSpaceChar + tabSpaceChar + tabSpaceChar + "#} " + nextLineChar);
		sb11.append(tabSpaceChar + tabSpaceChar + tabSpaceChar + "ENDIF	 " + nextLineChar);
		sb11.append(tabSpaceChar + tabSpaceChar + tabSpaceChar + "IF(" + repName + "." + className + ".function == " + doubleQuote + "X" + doubleQuote + ")" + " THEN"  + nextLineChar );
		sb11.append(tabSpaceChar + tabSpaceChar + tabSpaceChar + "#{ " + nextLineChar);
		sb11.append(tabSpaceChar + tabSpaceChar + tabSpaceChar + tabSpaceChar + "IF(" + repName + "." + className + ".entityCreFlg == " + doubleQuote + "Y" + doubleQuote + ")" + " THEN" + nextLineChar );
		sb11.append(tabSpaceChar + tabSpaceChar + tabSpaceChar + tabSpaceChar +  "#{ " + nextLineChar);
		sb11.append(tabSpaceChar + tabSpaceChar + tabSpaceChar + tabSpaceChar + tabSpaceChar + repName + "." + className + ".ERRMSG =" + doubleQuote + "Nothing to Cancel" + doubleQuote
				+ nextLineChar);
		sb11.append(tabSpaceChar + tabSpaceChar + tabSpaceChar+ tabSpaceChar + tabSpaceChar + "GOTO ERRHANDLER " + nextLineChar);
		sb11.append(tabSpaceChar + tabSpaceChar + tabSpaceChar + tabSpaceChar + "#} " + nextLineChar);
		sb11.append(tabSpaceChar + tabSpaceChar + tabSpaceChar + tabSpaceChar+ "ENDIF	 " + nextLineChar);
		sb11.append(tabSpaceChar + tabSpaceChar + tabSpaceChar + "#} " + nextLineChar);
		sb11.append(tabSpaceChar + tabSpaceChar + tabSpaceChar + "ENDIF	 " + nextLineChar);
		sb11.append(tabSpaceChar + tabSpaceChar + "#} " + nextLineChar);
		sb11.append(tabSpaceChar + tabSpaceChar + "ENDIF	 " + nextLineChar);
		sb11.append(tabSpaceChar + "#} " + nextLineChar);
		sb11.append(tabSpaceChar + "ELSE	 " + nextLineChar);
		sb11.append(tabSpaceChar + "#{	 " + nextLineChar);
		sb11.append(tabSpaceChar + tabSpaceChar + "GOTO NEXTPAGE " + nextLineChar);
		sb11.append(tabSpaceChar + "#}	 " + nextLineChar);
		sb11.append(tabSpaceChar + "ENDIF	 " + nextLineChar);
		StringBuffer valRet = sb11;
		return valRet;
	}
}
