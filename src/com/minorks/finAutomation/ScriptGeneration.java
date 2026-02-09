package com.minorks.finAutomation;

import java.io.IOException;

public class ScriptGeneration {
	static String srvFileName;
	static String generatePath;
	static String fileName;
	
	public static void main(String[] args) throws IOException {
		
		//TODO Auto-generated method stub
		int argslength = args.length;
		if(argslength > 1){
			fileName = args[0].toString();
			if(argslength > 2){
				srvFileName = args[1].toString();
				generatePath  = args[2].toString();
			}
			else{	
				generatePath  = args[1].toString();
			}	
		}
		else
		{
			throw new IllegalArgumentException("Incorrect number of command line arguments: "+ argslength 
					+ "\n Should be 2 or 3");
		}
		
		@SuppressWarnings("unused")
		ScriptGeneration scriptAutoGen = new ScriptGeneration();
		AutoSourceGen	autoSourceGen = new AutoSourceGen();
		try{
			autoSourceGen.generateSource(fileName, srvFileName, generatePath);
		}
		catch(ArrayIndexOutOfBoundsException ex){
			System.out.println("Array Out Of Bound Exception");
			ex.printStackTrace();
		}
	}

}
