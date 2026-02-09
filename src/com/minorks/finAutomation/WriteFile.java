package com.minorks.finAutomation;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

import com.minorks.finAutomation.SrvFieldDetails;
import com.minorks.finAutomation.FieldDetails;

public class WriteFile
{
	String flds = "";
	String idxFlds = "";
	int idxFlds1 = 0;
	String cmnFlds = "";
	String isModtabRqd = "";
	String fetchSrvName = "";
	String submitSrvName = "";
	String finOper = "";
	final String doubleQuote = "\"";
	final String tabSpaceChar = "\t";
	final String nextLineChar = "\n";
	String repName;
	String className;
	String fetchScrName;
	String submitScrName;
	String pageName = null;
	String mainTableName = null;
	String modTableName = null;
	String mainSynName = null;
	String modSynName = null;
	boolean isModTabRqd = true;
	String fieldValues = "";
	String custRepVal = "";
	boolean isCustMenuRqd = true;
	boolean isValflg = false;
	String fsFlg;
	
	public String getMainTableName() {
		return mainTableName;
	}

	public void setMainTableName(String mainTableName) {
		this.mainTableName = mainTableName;
	}

	public String getModTableName() {
		return modTableName;
	}

	public void setModTableName(String modTableName) {
		this.modTableName = modTableName;
	}

	public String getMainSynName() {
		return mainSynName;
	}

	public void setMainSynName(String mainSynName) {
		this.mainSynName = mainSynName;
	}

	public String getModSynName() {
		return modSynName;
	}

	public void setModSynName(String modSynName) {
		this.modSynName = modSynName;
	}

	public boolean isModTabRqd() {
		return isModTabRqd;
	}

	public void setModTabRqd(boolean isModTabRqd) {
		this.isModTabRqd = isModTabRqd;
	}
	
	public boolean getIsCustMenuRqd() {
		return isCustMenuRqd;
	}

	public void setIsCustMenuRqd(boolean isCustMenuRqd) {
		this.isCustMenuRqd = isCustMenuRqd;
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

	CallingMethods cm = new CallingMethods();
	@SuppressWarnings("unchecked")
	public void generateTableSQL(String tableName,String synName,ArrayList getFlds, String generatePath) {
		// TODO Auto-generated method stub

		System.out.println("write table Name:"+tableName);
			
		for(int i = 0; i < getFlds.size(); i++)
		{
			FieldDetails fa = (FieldDetails) getFlds.get(i);
			
			if(!fa.getDataType().equalsIgnoreCase("DATE"))
			{
				flds = flds +"	"+ fa.getFldName()+ tabSpaceChar + tabSpaceChar + fa.getDataType()+"("+fa.getLength()+")," + nextLineChar;
			}
			else
			{
				flds = flds +"	"+ fa.getFldName()+ tabSpaceChar + tabSpaceChar + fa.getDataType() + "," + nextLineChar;
			}
			if(fa.getIsKeyfld().equals("Y"))
			{
				System.out.println("idx1->"+idxFlds);
				idxFlds = idxFlds + fa.getFldName();
				if(idxFlds != null)
				{
					idxFlds = idxFlds + ",";
				}
			}  
		}
		idxFlds = idxFlds + "BANK_ID";
		cmnFlds = cmnFlds + tabSpaceChar +"ENTITY_CRE_FLG" + tabSpaceChar +"CHAR(1 BYTE)," + nextLineChar;
		cmnFlds = cmnFlds + tabSpaceChar +"DEL_FLG" + tabSpaceChar +"CHAR(1 BYTE)," + nextLineChar;
		cmnFlds = cmnFlds + tabSpaceChar +"RCRE_USER_ID" +"	"+"VARCHAR2(15 CHAR)," + nextLineChar;
		cmnFlds = cmnFlds + tabSpaceChar +"RCRE_TIME" + tabSpaceChar + tabSpaceChar +"DATE," + nextLineChar;
		cmnFlds = cmnFlds + tabSpaceChar +"LCHG_USER_ID" +"	"+"VARCHAR2(15 CHAR)," + nextLineChar;
		cmnFlds = cmnFlds + tabSpaceChar +"LCHG_TIME" + tabSpaceChar + tabSpaceChar +"DATE," + nextLineChar;
		cmnFlds = cmnFlds + tabSpaceChar +"BANK_ID" +"	" + tabSpaceChar +"VARCHAR2(8 CHAR)" + nextLineChar;
		
		String fileName = "Create_"+tableName+".sql";
		try {
			
            // Assume default encoding.
            FileWriter fileWriter = new FileWriter(generatePath + fileName );

            // Always wrap FileWriter in BufferedWriter.
            BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);

            // Note that write() does not automatically
            // append a newline character.
            // #TODO header should add
            bufferedWriter.write("DROP TABLE CUSTOM."+tableName+";" + nextLineChar);
            bufferedWriter.write("DROP SYNONYM CUSTOM."+synName+";" + nextLineChar);
            bufferedWriter.write("CREATE TABLE CUSTOM."+tableName + nextLineChar);
            bufferedWriter.write("(" + nextLineChar);
            bufferedWriter.write(flds);
            bufferedWriter.write(cmnFlds);
            bufferedWriter.write(");" + nextLineChar);
            
            if(!idxFlds.equals(""))
			{
            bufferedWriter.write("CREATE UNIQUE INDEX CUSTOM.C_IDX_"+synName+" ON CUSTOM."+tableName+"("+idxFlds+");" + nextLineChar);                      
			}

            bufferedWriter.write("CREATE OR REPLACE SYNONYM CUSTOM."+synName+" FOR CUSTOM."+tableName+";" + nextLineChar);
            bufferedWriter.write("GRANT SELECT, INSERT, UPDATE, DELETE on CUSTOM."+synName+" TO tbautil,tbaadm,tbagen; " + nextLineChar);
            // Always close files.
            flds = "";
            idxFlds = "";
            cmnFlds = "";
            bufferedWriter.close();
        }
        catch(IOException ex) {
            System.out.println(
                "Error writing to file '"
                + fileName + "'");
            // Or we could just do this:
            // ex.printStackTrace();
        }
	}
	public void generateScriptFile(ArrayList<SrvFieldDetails> arValues, String generatePath) {
		// TODO Auto-generated method stub
		CallingMethods cm = new CallingMethods();
		
		for(int j = 0; j < arValues.size(); j++)
		{
			SrvFieldDetails sf = (SrvFieldDetails) arValues.get(j);
			finOper = sf.getFinOpr();
			//System.out.println("Finacle Operation:" +finOper);
			fetchSrvName = sf.getFetchSrvName();
			//System.out.println("Fetch SRV Name:" +fetchSrvName);
			submitSrvName = sf.getSubmitSrvName();
			//System.out.println("Submit SRV Name:" +submitSrvName);
			try {
			StringBuffer funcReturnFetch = null;
			//if(!fetchSrvName.equals(""))
			if ((fetchSrvName != null) && (!fetchSrvName.equals("")))
			{
				
				// Assume default encoding.
				FileWriter fileWriter = new FileWriter(generatePath + fetchSrvName);
				
				// Always wrap FileWriter in BufferedWriter.
				BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
				
				// Note that write() does not automatically
		        // append a newline character.
				bufferedWriter.write("<--START " + nextLineChar);
				bufferedWriter.write("TRACE ON " + nextLineChar);
				bufferedWriter.write(tabSpaceChar + "#Creating Repository And Class " + nextLineChar);
				funcReturnFetch = cm.repCreation(repName, className, true);
				bufferedWriter.write(funcReturnFetch.toString());
				bufferedWriter.write(tabSpaceChar + repName + "." + className + ".functionCode = " + doubleQuote + finOper + doubleQuote);
				bufferedWriter.write(nextLineChar);
				bufferedWriter.write(tabSpaceChar + "CALL(" + doubleQuote + fetchScrName + doubleQuote + ")");
				bufferedWriter.write(nextLineChar);
				bufferedWriter.write("TRACE OFF " + nextLineChar);
				bufferedWriter.write("END--> " + nextLineChar);
				//funcReturnFetch = null;
		        bufferedWriter.close();
			}
			funcReturnFetch = null;
		}
		catch(IOException ex) {
            System.out.println(
                "Error writing to file '"
                + fetchSrvName + "'");
            // Or we could just do this:
            ex.printStackTrace();
        }	
		try {
			if ((submitSrvName != null) && (!submitSrvName.equals("")))
			{
				// Assume default encoding.
				FileWriter fileWriter = new FileWriter(generatePath + submitSrvName);
				
				// Always wrap FileWriter in BufferedWriter.
				BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
				
				// Note that write() does not automatically
		        // append a newline character.
				bufferedWriter.write("<--START " + nextLineChar);
				bufferedWriter.write("TRACE ON " + nextLineChar);
				bufferedWriter.write(tabSpaceChar + "#Creating Repository And Class " + nextLineChar);
				StringBuffer funcReturnFetch1 = cm.repCreation(repName, className, true);
				bufferedWriter.write(funcReturnFetch1.toString());

				bufferedWriter.write(tabSpaceChar + repName + "." + className + ".functionCode = " + doubleQuote + finOper + doubleQuote);
				bufferedWriter.write(nextLineChar);
				bufferedWriter.write(tabSpaceChar + "CALL(" + doubleQuote + submitScrName + doubleQuote + ")");
				bufferedWriter.write(nextLineChar);
				bufferedWriter.write("TRACE OFF " + nextLineChar);
				bufferedWriter.write("END--> " + nextLineChar);
		        
				funcReturnFetch1 = null;
		        bufferedWriter.close();
			}
		}
		catch(IOException ex) {
            System.out.println(
                "Error writing to file '"
                + submitSrvName + "'");
            // Or we could just do this:
            ex.printStackTrace();
        }
		}		
	}
	
	public void fetchScriptGeneration(ArrayList<?> getFlds, String generatePath) {
		// TODO Auto-generated method stub
		StringBuffer sbFetch = new StringBuffer();
		fsFlg = "F";
		sbFetch.append("");
		//Header Section
		sbFetch.append("###############################################################################"+nextLineChar);
		sbFetch.append("# Script name       	: "+(fetchScrName.substring(fetchScrName.lastIndexOf("\\")+1))+nextLineChar);
		sbFetch.append("# Start Date        	: "+nextLineChar);
		sbFetch.append("# Description       	: "+nextLineChar);
		sbFetch.append("# Reference         	: "+nextLineChar); 
		sbFetch.append("# Modification History	: "+nextLineChar);
		sbFetch.append("# S.No.  Date       Name                   Description "+nextLineChar);
		sbFetch.append("# -----  ---------  ---------------------- ----------------------------- "+nextLineChar);
		sbFetch.append("# "+nextLineChar); 
		sbFetch.append("###############################################################################"+nextLineChar);
		sbFetch.append("<--START " + nextLineChar);
		sbFetch.append("TRACE ON " + nextLineChar);
		StringBuffer funcReturnFetch = cm.repCreation(repName, className, true);
		sbFetch.append(funcReturnFetch);
		sbFetch.append(nextLineChar);
		if(isCustMenuRqd)
			sbFetch.append(tabSpaceChar + repName + "." + className + ".function = BANCS.INPUT.funcCode " + nextLineChar);
		else
			sbFetch.append(tabSpaceChar + repName + "." + className + ".function = " + repName + "." + className + ".functionCode" + nextLineChar);
		sbFetch.append(tabSpaceChar + "PRINT(" + repName + "." + className + ".function)" + nextLineChar);
		sbFetch.append(tabSpaceChar + repName + "." + className + ".bankId = BANCS.STDIN.contextBankId " + nextLineChar);
		sbFetch.append(tabSpaceChar + "PRINT(" + repName + "." + className + ".bankId) " + nextLineChar);
		if(isModTabRqd)
			sbFetch.append(tabSpaceChar + repName + "." + className + ".modTableCount = " + doubleQuote + doubleQuote + nextLineChar);
		sbFetch.append(nextLineChar);
		
		if(!isCustMenuRqd){
		sbFetch.append(tabSpaceChar + "# Get Product Field values using urhk_SRVGetVal " + nextLineChar);
		StringBuffer funcRetSRV = cm.srvGetValToCustRep(repName, className, getFlds);
		sbFetch.append(funcRetSRV);
		}
		else
		{
			sbFetch.append(tabSpaceChar + "# Get Key Field values " + nextLineChar);
			StringBuffer funcRetKey = cm.setKeyFld(repName, className, getFlds);
			sbFetch.append(funcRetKey);
			sbFetch.append(nextLineChar);
		}
		sbFetch.append(nextLineChar);
		if(isModTabRqd){
			sbFetch.append(tabSpaceChar + "####Select Count from MOD table " + nextLineChar);
			sbFetch.append(tabSpaceChar + "sv_s = " + doubleQuote + doubleQuote + nextLineChar);
			sbFetch.append(tabSpaceChar + "sv_s = sv_s + " + doubleQuote + "modTableCount|SELECT COUNT(1)" + doubleQuote + nextLineChar);
			sbFetch.append(tabSpaceChar + "sv_s = sv_s + " + doubleQuote + " FROM " + modSynName + doubleQuote + nextLineChar);
			String fetchReturn = cm.buildWhereCondition(repName, className, getFlds,isValflg);
			String[] qry = fetchReturn.split("~");
			sbFetch.append(qry[0]);
			sbFetch.append(tabSpaceChar + "PRINT(sv_s) " + nextLineChar);
			sbFetch.append(nextLineChar);
			sbFetch.append(qry[1] + nextLineChar);
			sbFetch.append(tabSpaceChar + "PRINT(BANCS.INPARAM.BINDVARS)" + nextLineChar);
			sbFetch.append(nextLineChar);
			sbFetch.append(tabSpaceChar + "sv_b = urhk_dbSelectWithBind(sv_s) " + nextLineChar);
			sbFetch.append(tabSpaceChar + "PRINT(sv_b) " + nextLineChar);
			sbFetch.append(nextLineChar);
			sbFetch.append(tabSpaceChar + "IF(sv_b == 0) THEN " + nextLineChar);
			sbFetch.append(tabSpaceChar + "#{ " + nextLineChar);
			sbFetch.append(tabSpaceChar + tabSpaceChar + repName + "." + className + ".modTableCount = BANCS.OUTPARAM.modTableCount" + nextLineChar);
			sbFetch.append(tabSpaceChar + tabSpaceChar + "PRINT(" + repName + "." + className + ".modTableCount)" + nextLineChar);
			sbFetch.append(tabSpaceChar + "#}" + nextLineChar);
			sbFetch.append(tabSpaceChar + "ELSE" + nextLineChar);
			sbFetch.append(tabSpaceChar + "#{ " + nextLineChar);
			sbFetch.append(tabSpaceChar + tabSpaceChar + repName + "." + className + ".ERRMSG =" + doubleQuote + "Query Execution Failed" + doubleQuote
				+ nextLineChar);
			sbFetch.append(tabSpaceChar + tabSpaceChar + "GOTO ERRHANDLER " + nextLineChar);
			sbFetch.append(tabSpaceChar + "#}" + nextLineChar);
			sbFetch.append(tabSpaceChar + "ENDIF" + nextLineChar);
			sbFetch.append(nextLineChar);
			sbFetch.append(tabSpaceChar + "####Function Code MODIFY/VERIFY/CANCEL " + nextLineChar);
			sbFetch.append(tabSpaceChar + "IF((" + repName + "." + className + ".function == " + doubleQuote + "M" + doubleQuote + ")" +
				" OR (" + repName + "." + className + ".function == " + doubleQuote + "V" + doubleQuote + ") " +
				"OR (" + repName + "." + className + ".function == " + doubleQuote + "X" + doubleQuote + ")) THEN " + nextLineChar);
			sbFetch.append(tabSpaceChar + "#{ " + nextLineChar);
			sbFetch.append(tabSpaceChar + tabSpaceChar + "IF(" + repName + "." + className + ".modTableCount == " + doubleQuote + "1" + doubleQuote
				+ ") THEN " + nextLineChar);
			sbFetch.append(tabSpaceChar + tabSpaceChar + "#{ " + nextLineChar);
			sbFetch.append(tabSpaceChar + tabSpaceChar + tabSpaceChar + repName + "." + className + ".tableName = " + doubleQuote + modSynName
				+ doubleQuote + nextLineChar);
			if(isCustMenuRqd){
				sbFetch.append(tabSpaceChar + tabSpaceChar + tabSpaceChar + "GOTO VALIDATE " + nextLineChar);
			}
			else{
				sbFetch.append(tabSpaceChar + tabSpaceChar + tabSpaceChar + "GOTO SELECTQRY " + nextLineChar);}
			sbFetch.append(tabSpaceChar + tabSpaceChar + "#} " + nextLineChar);
			sbFetch.append(tabSpaceChar + tabSpaceChar + "ELSE " + nextLineChar);
			sbFetch.append(tabSpaceChar + tabSpaceChar + "#{ " + nextLineChar);
			sbFetch.append(tabSpaceChar + tabSpaceChar + tabSpaceChar + repName + "." + className + ".tableName = " + doubleQuote + mainSynName
				+ doubleQuote + nextLineChar);
			if(isCustMenuRqd){
				sbFetch.append(tabSpaceChar + tabSpaceChar + tabSpaceChar + "GOTO VALIDATE " + nextLineChar);}
			else{
			sbFetch.append(tabSpaceChar + tabSpaceChar + tabSpaceChar + "GOTO SELECTQRY " + nextLineChar);}
			sbFetch.append(tabSpaceChar + tabSpaceChar + "#} " + nextLineChar);
			sbFetch.append(tabSpaceChar + tabSpaceChar + "ENDIF " + nextLineChar);
			sbFetch.append(tabSpaceChar + "#} " + nextLineChar);
			sbFetch.append(tabSpaceChar + "ENDIF	 " + nextLineChar);
			sbFetch.append(nextLineChar);
			sbFetch.append(tabSpaceChar + "####Function Code INQUIRE " + nextLineChar);
			sbFetch.append(tabSpaceChar + "IF((" + repName + "." + className + ".function == " + doubleQuote + "I" + doubleQuote + ") " +
				"OR (" + repName + "." + className + ".function == " + doubleQuote + "C" + doubleQuote + ") " +
				"OR (" + repName + "." + className + ".function == " + doubleQuote + "D" + doubleQuote + ") " +
				"OR (" + repName + "." + className + ".function == " + doubleQuote + "U" + doubleQuote + ")) THEN " + nextLineChar);
			sbFetch.append(tabSpaceChar + "#{ " + nextLineChar);
			sbFetch.append(tabSpaceChar + tabSpaceChar + repName + "." + className + ".tableName = " + doubleQuote + mainSynName + doubleQuote
				+ nextLineChar);
			if(isCustMenuRqd){
				sbFetch.append(tabSpaceChar + tabSpaceChar + tabSpaceChar + "GOTO VALIDATE " + nextLineChar);}
			else{
			sbFetch.append(tabSpaceChar + tabSpaceChar + "GOTO SELECTQRY " + nextLineChar);}
			sbFetch.append(tabSpaceChar + "#} " + nextLineChar);
			sbFetch.append(tabSpaceChar + "ENDIF " + nextLineChar);
			sbFetch.append(nextLineChar);
		}
		if(isCustMenuRqd){
		StringBuffer funcValQry = cm.validateQry(repName, className, getFlds,isCustMenuRqd, mainSynName,isModTabRqd);
		sbFetch.append(funcValQry);	}
		sbFetch.append(nextLineChar);
		if(isCustMenuRqd){
			if(!isModTabRqd){
				sbFetch.append(tabSpaceChar + repName + "." + className + ".tableName = " + doubleQuote + mainSynName
		+ doubleQuote + nextLineChar);}
		}
		sbFetch.append("SELECTQRY: " + nextLineChar);
		
		StringBuffer fetchReturn3 = cm.selectQuery(repName,className,getFlds,isCustMenuRqd,isValflg);
		sbFetch.append(fetchReturn3);

		sbFetch.append(nextLineChar);
		sbFetch.append("GOTO ENDOFSCRIPT " + nextLineChar);
		sbFetch.append(nextLineChar);
		StringBuffer fetchReturn1 = cm.errorHandler(repName, className,isCustMenuRqd, fsFlg);
		sbFetch.append(fetchReturn1);
		StringBuffer fetchReturn2 = cm.endOfScript();
		sbFetch.append(fetchReturn2);
		
		sbFetch.append(tabSpaceChar + "TRACE OFF " + nextLineChar);
		sbFetch.append("END--> " + nextLineChar);
		System.out.println(sbFetch);
		
		String fileName = fetchScrName;
		System.out.println("fetchScrName " +fetchScrName);
		try {
			FileWriter fileWriter = new FileWriter(generatePath + fileName);
			// Always wrap FileWriter in BufferedWriter.
	        BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);

	        // Note that write() does not automatically
	        // append a newline character.
	        // #TODO header should add
	        bufferedWriter.write(sbFetch.toString());
	        
	        bufferedWriter.close();
		} 
		catch (IOException e) {
			 System.out.println(
		                "Error writing to file '"
		                + fileName + "'");
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public void submitScriptGeneration(ArrayList<?> getFlds, String generatePath) {
		// TODO Auto-generated method stub
		
		StringBuffer sb = new StringBuffer();
		fsFlg = "S";
		sb.append("");
		sb.append("###############################################################################"+nextLineChar);
		sb.append("# Script name       	: "+(submitScrName.substring(submitScrName.lastIndexOf("\\")+1))+nextLineChar);
		sb.append("# Start Date        	: "+nextLineChar);
		sb.append("# Description       	: "+nextLineChar);
		sb.append("# Reference         	: "+nextLineChar); 
		sb.append("# Modification History	: "+nextLineChar);
		sb.append("# S.No.  Date       Name                   Description "+nextLineChar);
		sb.append("# -----  ---------  ---------------------- ----------------------------- "+nextLineChar);
		sb.append("# "+nextLineChar); 
		sb.append("###############################################################################"+nextLineChar);
		//System.out.println("priya"+isCustMenuRqd);
		sb.append("<--START" + nextLineChar);
		sb.append("TRACE ON" + nextLineChar);
		sb.append(tabSpaceChar + "#Creating Repository And Class" + nextLineChar);
		StringBuffer funcReturn = cm.repCreation(repName, className, true);
		sb.append(funcReturn);
		sb.append(nextLineChar);
		//Kept after delete for testing
		//EXECUTE QUERY
		//end
		sb.append(tabSpaceChar + repName + "." + className + ".ERRMSG = " + doubleQuote + doubleQuote + nextLineChar);
		if(isCustMenuRqd)
			sb.append(tabSpaceChar + repName + "." + className + ".RSTMSG = " + doubleQuote + doubleQuote + nextLineChar);
		sb.append(nextLineChar);
		if(!isModTabRqd)
		{
			sb.append(tabSpaceChar + repName + "." + className + ".tableName = " + doubleQuote + mainSynName + doubleQuote
			+ nextLineChar);
		}
		if(isCustMenuRqd){
			sb.append(tabSpaceChar + "#Print Bancs Repos " + nextLineChar);
			StringBuffer  funcReturn1 = cm.parseBANCSCustData(repName, className, getFlds);
			sb.append(funcReturn1);
			}
		else{	
			sb.append(tabSpaceChar + "#ParseONSData and store in respective custom repos variables " + nextLineChar);
			StringBuffer funcReturn1 = cm.parseONSCustData(repName, className, getFlds);
			sb.append(funcReturn1);
			sb.append(nextLineChar);
			sb.append(tabSpaceChar + "# Get Product Field values using urhk_SRVGetVal " + nextLineChar);
			StringBuffer funcReturn2 = cm.srvGetValToCustRep(repName, className, getFlds);
			sb.append(funcReturn2);
		}

		sb.append(nextLineChar);
		sb.append(tabSpaceChar + repName + "." + className + ".updEntCreFlg = " + doubleQuote + "N" + doubleQuote + nextLineChar);
		sb.append(tabSpaceChar + repName + "." + className + ".updDelFlg = " + doubleQuote + "N" + doubleQuote + nextLineChar);
		if(isModTabRqd){
		sb.append(tabSpaceChar + repName + "." + className + ".modDelFlg = " + doubleQuote + "N" + doubleQuote + nextLineChar);}	
		if(isCustMenuRqd)
			sb.append(tabSpaceChar + repName + "." + className + ".function = " + "BANCS" + "." + "INPUT" + ".funcCode" + nextLineChar);
		else
		sb.append(tabSpaceChar + repName + "." + className + ".function = " + repName + "." + className + ".functionCode" + nextLineChar);
		sb.append(tabSpaceChar + "PRINT(" + repName + "." + className + ".function) " + nextLineChar);
		sb.append(tabSpaceChar + repName + "." + className + ".bankId = BANCS.STDIN.contextBankId " + nextLineChar);
		sb.append(tabSpaceChar + "PRINT(" + repName + "." + className + ".bankId) " + nextLineChar);
		if(isModTabRqd)
		{
			sb.append(tabSpaceChar + repName + "." + className + ".modTableCount = " + doubleQuote + "0" + doubleQuote + nextLineChar);
			sb.append(tabSpaceChar + repName + "." + className + ".mainTableCount = " + doubleQuote + "0" + doubleQuote + nextLineChar);
			sb.append(nextLineChar);
	
			String selectStr = "modTableCount,modEntityCreFlg,modDelFlg";
			StringBuffer funcReturn3 = cm.selectCount(repName, className, selectStr, getFlds, modSynName,isValflg);
			sb.append(funcReturn3);
			sb.append(nextLineChar);
	
			String selectStr1 = "mainTableCount,entityCreFlg,delFlg";
			StringBuffer funcReturn4 = cm.selectCount(repName, className, selectStr1, getFlds, mainSynName,isValflg);
			sb.append(funcReturn4);
			sb.append(nextLineChar);
		
			sb.append(tabSpaceChar + "####Function Code ADD " + nextLineChar);
			sb.append(tabSpaceChar + "IF(" + repName + "." + className + ".function == " + doubleQuote + "A" + doubleQuote + ") THEN " + nextLineChar);
			sb.append(tabSpaceChar + "#{ " + nextLineChar);
			sb.append(tabSpaceChar + tabSpaceChar + repName + "." + className + ".tableName = " + doubleQuote + mainSynName + doubleQuote
					+ nextLineChar);
			
			if(isCustMenuRqd){
				sb.append(tabSpaceChar + tabSpaceChar + repName + "." + className + ".RSTMSG = " + doubleQuote + "Record Added Successfully" + doubleQuote + nextLineChar);
			}
			sb.append(tabSpaceChar + tabSpaceChar + "GOTO INSERTQRY " + nextLineChar);
			sb.append(tabSpaceChar + "#} " + nextLineChar);
			sb.append(tabSpaceChar + "ENDIF " + nextLineChar);
			sb.append(nextLineChar);
		}
		if(!isModTabRqd){
			if(isCustMenuRqd){
			sb.append(tabSpaceChar + "####Function Code ADD " + nextLineChar);	
			sb.append(tabSpaceChar + "IF(" + repName + "." + className + ".function == " + doubleQuote + "A" + doubleQuote + ") THEN " + nextLineChar);
			sb.append(tabSpaceChar + "#{ " + nextLineChar);
			sb.append(tabSpaceChar + tabSpaceChar + repName + "." + className + ".RSTMSG = " + doubleQuote + "Record Added Successfully" + doubleQuote + nextLineChar);
			sb.append(tabSpaceChar + tabSpaceChar + "GOTO INSERTQRY " + nextLineChar);
			sb.append(tabSpaceChar + "#} " + nextLineChar);
			sb.append(tabSpaceChar + "ENDIF " + nextLineChar);
		}
			}
		sb.append(nextLineChar);
		sb.append(tabSpaceChar + "####Function Code MODIFY " + nextLineChar);
		sb.append(tabSpaceChar + "IF(" + repName + "." + className + ".function == " + doubleQuote + "M" + doubleQuote + ") THEN " + nextLineChar);
		sb.append(tabSpaceChar + "#{ " + nextLineChar);
		if(isCustMenuRqd){
			sb.append(tabSpaceChar + tabSpaceChar + repName + "." + className + ".RSTMSG = " + doubleQuote + "Record Modified Successfully" + doubleQuote + nextLineChar);
			//sb.append(tabSpaceChar + tabSpaceChar + repName + "." + className + ".updEntCreFlg = " + doubleQuote + "N" + doubleQuote + nextLineChar);
		}
		if(isModTabRqd)
		{
			sb.append(tabSpaceChar + tabSpaceChar + "IF(" + repName + "." + className + ".modTableCount == " + doubleQuote + "1" + doubleQuote
					+ ") THEN " + nextLineChar);
			sb.append(tabSpaceChar + tabSpaceChar + "#{ " + nextLineChar);
			sb.append(tabSpaceChar + tabSpaceChar + tabSpaceChar + repName + "." + className + ".tableName = " + doubleQuote + modSynName
					+ doubleQuote + nextLineChar);
			sb.append(tabSpaceChar + tabSpaceChar + tabSpaceChar + "GOTO UPDATEQRY " + nextLineChar);
			sb.append(tabSpaceChar + tabSpaceChar + "#} " + nextLineChar);
			sb.append(tabSpaceChar + tabSpaceChar + "ELSE " + nextLineChar);
			sb.append(tabSpaceChar + tabSpaceChar + "#{ " + nextLineChar);
			sb.append(tabSpaceChar + tabSpaceChar + tabSpaceChar + "IF (" + repName + "." + className + ".entityCreFlg == \"Y\") THEN " + nextLineChar);
			sb.append(tabSpaceChar + tabSpaceChar + tabSpaceChar + "#{ " + nextLineChar);
			sb.append(tabSpaceChar + tabSpaceChar + tabSpaceChar + tabSpaceChar + repName + "." + className + ".tableName = " + doubleQuote
					+ modSynName + doubleQuote + nextLineChar);
			sb.append(tabSpaceChar + tabSpaceChar + tabSpaceChar + tabSpaceChar + "GOTO INSERTQRY " + nextLineChar);
			sb.append(tabSpaceChar + tabSpaceChar + tabSpaceChar + "#} " + nextLineChar);
			sb.append(tabSpaceChar + tabSpaceChar + tabSpaceChar + "ELSE " + nextLineChar);
			sb.append(tabSpaceChar + tabSpaceChar + tabSpaceChar + "#{ " + nextLineChar);
			sb.append(tabSpaceChar + tabSpaceChar + tabSpaceChar + tabSpaceChar + repName + "." + className + ".tableName = " + doubleQuote
					+ mainSynName + doubleQuote + nextLineChar);
			sb.append(tabSpaceChar + tabSpaceChar + tabSpaceChar + tabSpaceChar + "GOTO UPDATEQRY " + nextLineChar);
		}
		else
		{
			sb.append(tabSpaceChar + tabSpaceChar + "GOTO UPDATEQRY " + nextLineChar);
		}
		
		if(isModTabRqd)
		{
			sb.append(tabSpaceChar + tabSpaceChar + tabSpaceChar + "#} " + nextLineChar);
			sb.append(tabSpaceChar + tabSpaceChar + tabSpaceChar + "ENDIF " + nextLineChar);
			sb.append(tabSpaceChar + tabSpaceChar + "#} " + nextLineChar);
			sb.append(tabSpaceChar + tabSpaceChar + "ENDIF " + nextLineChar);
		}
		sb.append(tabSpaceChar + "#} " + nextLineChar);
		sb.append(tabSpaceChar + "ENDIF " + nextLineChar);
		
		sb.append(nextLineChar);

		sb.append(tabSpaceChar + "####Function Code VERIFY " + nextLineChar);
		sb.append(tabSpaceChar + "IF(" + repName + "." + className + ".function == " + doubleQuote + "V" + doubleQuote + ")  THEN " + nextLineChar);
		sb.append(tabSpaceChar + "#{ " + nextLineChar);
		if(isCustMenuRqd)
			sb.append(tabSpaceChar + tabSpaceChar + repName + "." + className + ".RSTMSG = " + doubleQuote + "Record Verified Successfully" + doubleQuote + nextLineChar);
		if(isModTabRqd){
			sb.append(tabSpaceChar + tabSpaceChar + repName + "." + className + ".tableName = " + doubleQuote + mainSynName + doubleQuote
				+ nextLineChar);}
		sb.append(tabSpaceChar + tabSpaceChar + repName + "." + className + ".updEntCreFlg = " + doubleQuote + "Y" + doubleQuote + nextLineChar);
		if(isModTabRqd){
			sb.append(tabSpaceChar + tabSpaceChar + "IF(" + repName + "." + className + ".modDelFlg == " + doubleQuote +  "Y" + doubleQuote + ") THEN" + nextLineChar);
			sb.append(tabSpaceChar + tabSpaceChar + "#{" + nextLineChar);
			sb.append(tabSpaceChar + tabSpaceChar + tabSpaceChar + repName + "." + className + ".updDelFlg = " + doubleQuote + "Y" + doubleQuote + nextLineChar);
			sb.append(tabSpaceChar + tabSpaceChar + "#}" + nextLineChar);
			sb.append(tabSpaceChar + tabSpaceChar + "ENDIF" + nextLineChar);
		}
		sb.append(tabSpaceChar + tabSpaceChar + "GOTO UPDATEQRY " + nextLineChar);
		sb.append(tabSpaceChar + "#} " + nextLineChar);
		sb.append(tabSpaceChar + "ENDIF " + nextLineChar);
		sb.append(nextLineChar);

		sb.append(tabSpaceChar + "####Function Code DELETE " + nextLineChar);
		sb.append(tabSpaceChar + "IF(" + repName + "." + className + ".function == " + doubleQuote + "D" + doubleQuote + ")  THEN	 " + nextLineChar);
		sb.append(tabSpaceChar + "#{ " + nextLineChar);
		if(isCustMenuRqd){
			sb.append(tabSpaceChar + tabSpaceChar + repName + "." + className + ".RSTMSG = " + doubleQuote + "Record Deleted Successfully" + doubleQuote + nextLineChar);
			sb.append(tabSpaceChar + tabSpaceChar + repName + "." + className + ".updEntCreFlg = " + doubleQuote + "Y" + doubleQuote + nextLineChar);}
		if(isModTabRqd){
			sb.append(tabSpaceChar + tabSpaceChar + repName + "." + className + ".tableName = " + doubleQuote + modSynName + doubleQuote
				+ nextLineChar);}
		sb.append(tabSpaceChar + tabSpaceChar + repName + "." + className + ".updDelFlg = " + doubleQuote + "Y" + doubleQuote + nextLineChar);
		if(isCustMenuRqd){
			sb.append(tabSpaceChar + tabSpaceChar + "GOTO UPDATEQRY " + nextLineChar);}
		else
			sb.append(tabSpaceChar + tabSpaceChar + "GOTO INSERTQRY " + nextLineChar);
		sb.append(tabSpaceChar + "#} " + nextLineChar);
		sb.append(tabSpaceChar + "ENDIF " + nextLineChar);
		sb.append(nextLineChar);
		
		sb.append(tabSpaceChar + "####Function Code UNDELETE " + nextLineChar);
		sb.append(tabSpaceChar + "IF(" + repName + "." + className + ".function == " + doubleQuote + "U" + doubleQuote + ")  THEN" + nextLineChar);
		sb.append(tabSpaceChar + "#{ " + nextLineChar);
		if(isCustMenuRqd){
			sb.append(tabSpaceChar + tabSpaceChar + repName + "." + className + ".RSTMSG = " + doubleQuote + "Record UnDeleted Successfully" + doubleQuote + nextLineChar);}
		if(isModTabRqd){
			sb.append(tabSpaceChar + tabSpaceChar + repName + "." + className + ".tableName = " + doubleQuote + modSynName + doubleQuote + nextLineChar);}
		sb.append(tabSpaceChar + tabSpaceChar + repName + "." + className + ".updDelFlg = " + doubleQuote + "N" + doubleQuote + nextLineChar);
		if(isCustMenuRqd)
			sb.append(tabSpaceChar + tabSpaceChar + "GOTO UPDATEQRY " + nextLineChar);
		else
			sb.append(tabSpaceChar + tabSpaceChar + "GOTO INSERTQRY " + nextLineChar);
		sb.append(tabSpaceChar + "#} " + nextLineChar);
		sb.append(tabSpaceChar + "ENDIF " + nextLineChar);
		sb.append(nextLineChar);

		sb.append(tabSpaceChar + "### Function code is CANCEL " + nextLineChar);
		sb.append(tabSpaceChar + "IF(" + repName + "." + className + ".function == " + doubleQuote + "X" + doubleQuote + ") THEN " + nextLineChar);
		sb.append(tabSpaceChar + "#{ " + nextLineChar);
		if(isCustMenuRqd)
			sb.append(tabSpaceChar + tabSpaceChar + repName + "." + className + ".RSTMSG = " + doubleQuote + "Record Canceled Successfully" + doubleQuote + nextLineChar);
		if(isModTabRqd){
			sb.append(tabSpaceChar + tabSpaceChar + "IF(" + repName + "." + className + ".modTableCount == " + doubleQuote + "1" + doubleQuote
			+ ") THEN " + nextLineChar);
			sb.append(tabSpaceChar + tabSpaceChar + "#{ " + nextLineChar);
			sb.append(tabSpaceChar + tabSpaceChar + tabSpaceChar + repName + "." + className + ".tableName = " + doubleQuote + modSynName
			+ doubleQuote + nextLineChar);
			sb.append(tabSpaceChar + tabSpaceChar + tabSpaceChar + "GOTO DELETEQRY " + nextLineChar);
			sb.append(tabSpaceChar + tabSpaceChar + "#} " + nextLineChar);
			sb.append(tabSpaceChar + tabSpaceChar + "ELSE " + nextLineChar);
			sb.append(tabSpaceChar + tabSpaceChar + "#{ " + nextLineChar);
			sb.append(tabSpaceChar + tabSpaceChar + tabSpaceChar + repName + "." + className + ".tableName = " + doubleQuote + mainSynName 
			+ doubleQuote + nextLineChar);
		}
		sb.append(tabSpaceChar + tabSpaceChar + "GOTO DELETEQRY " + nextLineChar);
		if(isModTabRqd){
			sb.append(tabSpaceChar + tabSpaceChar + "#} " + nextLineChar);
			sb.append(tabSpaceChar + tabSpaceChar + "ENDIF" + nextLineChar);
		}
		sb.append(tabSpaceChar + "#} " + nextLineChar);
		sb.append(tabSpaceChar + "ENDIF" + nextLineChar);

		sb.append(nextLineChar);
		if(isModTabRqd){
		sb.append("GOTO ENDOFSCRIPT " + nextLineChar);}
		sb.append(nextLineChar);
		
		sb.append("INSERTQRY:" + nextLineChar);
		sb.append(nextLineChar);
		sb.append(tabSpaceChar + repName + "." + className + ".rcreUserId = BANCS.STDIN.userId" + nextLineChar);
		sb.append(tabSpaceChar + repName + "." + className + ".rcreTime = MID$(BANCS.STDIN.BODDate,0,10)" + nextLineChar);
		sb.append(tabSpaceChar + repName + "." + className + ".lchgUserId = " + doubleQuote + doubleQuote + nextLineChar);
		sb.append(tabSpaceChar + repName + "." + className + ".lchgTime = " + doubleQuote + doubleQuote + nextLineChar);
		sb.append(nextLineChar);
		StringBuffer funcReturn7 = cm.buildInsertQry(repName, className, getFlds);
		sb.append(funcReturn7);
		sb.append(nextLineChar);
		sb.append("UPDATEQRY:" + nextLineChar);
		sb.append(nextLineChar);
		sb.append(tabSpaceChar + repName + "." + className + ".lchgUserId = BANCS.STDIN.userId" + nextLineChar);
		sb.append(tabSpaceChar + repName + "." + className + ".lchgTime = MID$(BANCS.STDIN.BODDate,0,10)" + nextLineChar);
		sb.append(nextLineChar);
		StringBuffer funcReturn8 = cm.buildUpdateQry(repName, className, getFlds,isValflg);
		sb.append(funcReturn8);
		if(isModTabRqd){
		sb.append(tabSpaceChar + "IF((" + repName + "." + className + ".function == " + doubleQuote + "V" + doubleQuote+ ") AND (" + repName + "." + className + ".modTableCount == " + doubleQuote + "1"  +doubleQuote+ ")) THEN" + nextLineChar);
		sb.append(tabSpaceChar + "#{ " + nextLineChar);
		sb.append(tabSpaceChar + tabSpaceChar + repName + "." + className + ".tableName = " + doubleQuote + modSynName + doubleQuote + nextLineChar);
		sb.append(tabSpaceChar + tabSpaceChar + "GOTO DELETEQRY" + nextLineChar);
		sb.append(tabSpaceChar + "#} " + nextLineChar);
		sb.append(tabSpaceChar + "ENDIF " + nextLineChar);
		sb.append(nextLineChar);
		}
		sb.append(tabSpaceChar + "GOTO ENDOFSCRIPT" + nextLineChar);
		sb.append(nextLineChar);
		
		sb.append("DELETEQRY: " + nextLineChar);
		StringBuffer funcReturn9 = cm.buildDeleteQry(repName, className, getFlds,isValflg);
		sb.append(funcReturn9);
		sb.append(nextLineChar);
		
		sb.append("EXECQRY:" + nextLineChar);
		sb.append(tabSpaceChar + "sv_u = urhk_dbSQLWithBind(sv_s)" + nextLineChar);
		sb.append(tabSpaceChar + "PRINT(sv_u)" + nextLineChar);
		sb.append(nextLineChar);
		sb.append(tabSpaceChar + "IF(sv_u == 1) THEN" + nextLineChar);
		sb.append(tabSpaceChar + "#{" + nextLineChar);
		sb.append(tabSpaceChar + tabSpaceChar + repName + "." + className + "."+ "ERRMSG = " + doubleQuote + "Query Execution Failed" + doubleQuote + nextLineChar);
		sb.append(tabSpaceChar + tabSpaceChar + "sv_t = urhk_SetOrbOut("+ repName + "." + className + ".ERRMSG)" + nextLineChar);
		sb.append(tabSpaceChar + "#}" + nextLineChar);
		if(isCustMenuRqd){
			sb.append(tabSpaceChar + "ELSE" + nextLineChar);
			sb.append(tabSpaceChar + "#{" + nextLineChar);
			sb.append(tabSpaceChar + tabSpaceChar + "sv_u = urhk_SetOrbOut(" + doubleQuote + "SuccessOrFailure|"  + "Y" + doubleQuote + ")" + nextLineChar);
			sb.append(tabSpaceChar + tabSpaceChar + "PRINT(sv_u) " + nextLineChar);
			sb.append(tabSpaceChar + tabSpaceChar + "sv_u = urhk_SetOrbOut("+ doubleQuote + "RESULT_MSG" + "|" + doubleQuote + " + "  + repName + "." + className + ".RSTMSG" + ")" + nextLineChar);
			sb.append(tabSpaceChar + tabSpaceChar + "PRINT(sv_u) " + nextLineChar);
			sb.append(tabSpaceChar + "#} " + nextLineChar);
		}
		sb.append(tabSpaceChar + "ENDIF" + nextLineChar);
		sb.append(tabSpaceChar + "RETURN" + nextLineChar);
		sb.append(nextLineChar);
		
		StringBuffer funcReturn5 = cm.errorHandler(repName, className, isCustMenuRqd, fsFlg);
		sb.append(funcReturn5);
		StringBuffer funcReturn6 = cm.endOfScript();
		sb.append(funcReturn6);
		sb.append(tabSpaceChar + "TRACE OFF" + nextLineChar);
		sb.append("END-->");
		
		System.out.println(sb);
		
		String fileName = submitScrName;
		try {
			FileWriter fileWriter = new FileWriter(generatePath + fileName);
			// Always wrap FileWriter in BufferedWriter.
	        BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);

	        // Note that write() does not automatically
	        // append a newline character.
	        // #TODO header should add
	        bufferedWriter.write(sb.toString());
	        
	        bufferedWriter.close();
		} 
		catch (IOException e) {
			 System.out.println(
		                "Error writing to file '"
		                + fileName + "'");
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
