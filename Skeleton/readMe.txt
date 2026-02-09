#***************************************************#
#	  SCRIPT AND SQL FILE GENERATION				#
#***************************************************#
For Table creation Input file Structure:
First Row:(Table Details)
1. Use ~ as a  delimiter to separate each value. start with table name following with synonym Name and MOD table require or not and custom Menu or not
2. The valid values for MOD require or not is Y/N.
3. The valid values for CUSTOM MENU or not is Y/N.

Second Row:(Script Details)
1. Use ~ as a delimiter to separate each value. start with Repository Name following with Class Name and Fetch and Submit Script Name.Script name end with (.scr).
Third Row:(Column Details)
1. Use delimiter to separate each value.
2. In Product menu Start with product fields or custom menu start with custom fields which you want as a key fields for mapping.
3. id Name, Column Name, Data Type, Size, key fields or not valid values(Y/N), is custom field or not valid values(Y/N),page Name only for Product Menu.
4. In case of product fields give that field structure as it is in doc file at page name place,In case of 
   custom fields give that page name alone.In custom menu no need to give page name.  
EXAMPLE:
		Custom Menu		- Refer  C:\SampleInput\tableDetails_CM.txt
		Product Menu 	- Refer  C:\SampleInput\tableDetails_PM.txt

For SRV creation Input file Structure:
1. Use ~ as a delimiter to separate each value.
2. Finacle function(that actually present in front END) Code and finacle Operation like (A,M,V,X,I) And fetch Service Name and Submit SRV Name
EXAMPLE:
		Product Menu 	- Refer  C:\SampleInput\srvDetails_PM.txt

Execute jar file from Command prompt
 
 	Custom Menu	 - java -cp ScriptGeneration.jar com.isl.scrGen.prodCust.ScriptGeneration  <input filenamewithPath> <outputsource path>
 	Product Menu - java -cp ScriptGeneration.jar com.isl.scrGen.prodCust.ScriptGeneration  <input filenamewithPath> <input filenamewithPath> <outputsource path>
	
	Example : 
		Custom	Menu - java -cp ScriptGeneration.jar com.isl.scrGen.prodCust.ScriptGeneration "C:\Users\Gowtham\Desktop\java\InputSource\tableDetails_ALLIN.txt" "C:\Users\Gowtham\Desktop\java\GenerateSource"
		Product Menu - java -cp ScriptGeneration.jar com.isl.scrGen.prodCust.ScriptGeneration "C:\Users\Gowtham\Desktop\java\InputSource\tableDetails_CCY.txt" "C:\Users\Gowtham\Desktop\java\InputSource\srvDetails_CCY.txt" "C:\Users\Gowtham\Desktop\java\GenerateSource"
	
While executing this jar, the output file generated in the Path mentioned in the SCRIPT_NAME tag
