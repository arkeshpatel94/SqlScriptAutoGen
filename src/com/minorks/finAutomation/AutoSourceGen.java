package com.minorks.finAutomation;

import java.io.IOException;
import java.util.ArrayList;
import com.minorks.finAutomation.CustomizationDet;
import com.minorks.finAutomation.InputFileRead;
import com.minorks.finAutomation.SrvFieldDetails;
import com.minorks.finAutomation.WriteFile;

public class AutoSourceGen {
	
	public void generateSource(String fileName,String srvFileName,String generatePath) throws IOException
	{
		InputFileRead fr = new InputFileRead();
		//Passing Input to the Method
		CustomizationDet getFlds = fr.fileParse(fileName);
		
		String tableName = getFlds.getTableName();
		String isModTabRqd = getFlds.getIsModTableReqd();
		String synonymName = getFlds.getSynonymName();
		String isCMenuReq = getFlds.getIsCustomMenu();
		
		
		WriteFile wf = new WriteFile();
		//Assigning values for the WriteFile 
		wf.setRepName(getFlds.getRepName());
		wf.setClassName(getFlds.getClassName());
		wf.setFetchScrName(getFlds.getFetchScrName());
		wf.setSubmitScrName(getFlds.getSubmitScrName());
		wf.setMainTableName(getFlds.getTableName());
		wf.setModTableName(getFlds.getTableName() + "_MOD");
		wf.setMainSynName("CUSTOM."+ getFlds.getSynonymName());
		wf.setModSynName("CUSTOM."+ getFlds.getSynonymName() + "_MOD");
		wf.setIsCustMenuRqd(getFlds.getIsCustomMenu().equals("Y")?true:false);
		wf.setModTabRqd(getFlds.getIsModTableReqd().equals("Y")?true:false);
		
		//Calling Generate SQL Method
		wf.generateTableSQL(tableName,synonymName,getFlds.getFieldInfoArrList(),generatePath);
		
		//Assigning MOD Table Name
		if(isModTabRqd.equals("Y"))
		{
			tableName = tableName + "_MOD";
			synonymName = synonymName + "_MOD";
			//Generate MOD Table SQL File 
			wf.generateTableSQL(tableName,synonymName,getFlds.getFieldInfoArrList(), generatePath);
		}
		System.out.println("Calling Generate Script Method");
		
		//Calling Generate SRV Script Files 
		if(isCMenuReq.equals("N")){
			ArrayList<SrvFieldDetails> arValues = fr.parseSrvInput(srvFileName);
			wf.generateScriptFile(arValues,generatePath);
		}
		
		//Calling Fetch  & Submit Script Generation Method
		wf.fetchScriptGeneration( getFlds.getFieldInfoArrList(),generatePath);
		wf.submitScriptGeneration( getFlds.getFieldInfoArrList(),generatePath);

		//Calling Front-End File Generation
		System.out.println("Calling Front-End Generation Method");
		FrontEndGeneration feg = new FrontEndGeneration();
		feg.setMenuName(getFlds.getMenuName());
		feg.setPageType(getFlds.getPageType());
		feg.setIsFuncCodePresent(getFlds.getIsFuncCodePresent());
		feg.setIsCustomMenu(getFlds.getIsCustomMenu());
		feg.setRepName(getFlds.getRepName());
		feg.setClassName(getFlds.getClassName());
		feg.setFetchScrName(getFlds.getFetchScrName());
		feg.setSubmitScrName(getFlds.getSubmitScrName());
		feg.generateAllFrontEndFiles(getFlds.getFieldInfoArrList(), generatePath);
	}
}
