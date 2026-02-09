package com.minorks.finAutomation;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

import com.minorks.finAutomation.FieldDetails;

public class InputFileRead {

	String line = null;
	int lineNumber = 0;	
	String deLmtr = "~";
	
	public CustomizationDet fileParse(String fileName)
	{
		CustomizationDet objfld = new CustomizationDet();
		try {
		
			ArrayList <String>storeValues = new ArrayList<String>();
					
            // FileReader reads text files in the default encoding.
            FileReader fileReader = new FileReader(fileName);

            // Always wrap FileReader in BufferedReader.
            BufferedReader bufferedReader = new BufferedReader(fileReader);
			
            while((line = bufferedReader.readLine()) != null) 
			{
				storeValues.add(line);				
				lineNumber++;
            }
			bufferedReader.close(); 
			
			ArrayList<FieldDetails> alValues = new ArrayList<FieldDetails>();
		
			for(int x=0;x<storeValues.size();x++)
			{					
				String[] values = storeValues.get(x).split(deLmtr);
				if( x == 0)
				{
					objfld.setTableName(values[0]);
					objfld.setSynonymName(values[1]);
					objfld.setIsModTableReqd(values[2]);
					objfld.setIsCustomMenu(values[3]);
					// New front-end fields
					if(values.length > 4)
						objfld.setMenuName(values[4]);
					if(values.length > 5)
						objfld.setIsFuncCodePresent(values[5]);
				}
				else if( x ==1)
				{
					//Set Custom Repository & Script Name
					objfld.setRepName(values[0]);
					objfld.setClassName(values[1]);
					objfld.setFetchScrName(values[2]);
					objfld.setSubmitScrName(values[3]);
					// New front-end field
					if(values.length > 4)
						objfld.setPageType(values[4]);
				}
				else
				{
					String CustomMenu = objfld.getIsCustomMenu();
					FieldDetails fd = new  FieldDetails();
					fd.setIdName(values[0]);
					fd.setFldName(values[1]);
					fd.setDataType(values[2]);
					fd.setLength(values[3]);
					fd.setIsKeyfld(values[4]);
					fd.setIsCustomfld(values[5]);
					//set PageName for Product Menu
					if(CustomMenu.equals("N"))
						fd.setPageName(values[6]);
					// New front-end fields
					if(values.length > 7)
						fd.setLiteralName(values[7]);
					if(values.length > 8)
						fd.setMandatory(values[8]);
					if(values.length > 9)
						fd.setFieldSize(values[9]);
					if(values.length > 10)
						fd.setMaxLength(values[10]);
					if(values.length > 11)
						fd.setReadOnly(values[11]);
					if(values.length > 12)
						fd.setDefaultValue(values[12]);
					if(values.length > 13) {
						String fTypeRaw = values[13];
						// For dropdown/radio/checkbox, extract type letter and values
						if(fTypeRaw.length() > 1 && (fTypeRaw.startsWith("d") || fTypeRaw.startsWith("f") || fTypeRaw.startsWith("g"))) {
							fd.setFieldType(fTypeRaw.substring(0,1));
							fd.setFieldTypeValues(fTypeRaw.substring(1));
						} else {
							fd.setFieldType(fTypeRaw);
						}
					}
					if(values.length > 14)
						fd.setValidationType(values[14]);
					alValues.add(fd);
				}
			}
			objfld.setFieldInfoArrList(alValues);			
        }
		
        catch(FileNotFoundException ex) {
            System.out.println(
                "Unable to open file '" + 
                fileName + "'");                
        }
        catch(IOException ex) {
            System.out.println(
                "Error reading file '" 
                + fileName + "'");                  
            // Or we could just do this: 
            // ex.printStackTrace();
        }
       
       return objfld;
	}
	
	public ArrayList<SrvFieldDetails> parseSrvInput(String srvFileName) throws IOException
	{
		ArrayList<SrvFieldDetails> alValues1 = new ArrayList<SrvFieldDetails>();
		try {
			
			ArrayList<String> values = new ArrayList<String>();
			
			// FileReader reads text files in the default encoding.
			FileReader fr = new FileReader(srvFileName);
			// Always wrap FileReader in BufferedReader.
			BufferedReader br = new BufferedReader(fr);
			
			while((line = br.readLine()) != null)
			{
				values.add(line);
				lineNumber++;
			}
			br.close();
			
			for(int i=0;i<values.size();i++)
			{
				String[] fValues = values.get(i).split(deLmtr);
				//set Fin Operation and SRV Script Name
				SrvFieldDetails objfld1 = new SrvFieldDetails();
				if (fValues.length == 4) {
					objfld1.setFinFuncCode(fValues[0]);
					objfld1.setFinOpr(fValues[1]);
					objfld1.setFetchSrvName(fValues[2]);
					objfld1.setSubmitSrvName(fValues[3]);
					
				} else {
					
					objfld1.setFinFuncCode(fValues[0]);
					objfld1.setFinOpr(fValues[1]);
					objfld1.setFetchSrvName(fValues[2]);
				}
				
				alValues1.add(objfld1);
			}
			
		} 
		catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return alValues1;
	}
	
}
