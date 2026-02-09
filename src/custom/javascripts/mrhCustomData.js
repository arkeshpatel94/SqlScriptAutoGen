/*****************************************************************************************
	NAME		: mrhCustomData.js
	Module Name	: SET/GET MULTIREC CUSTOM FIELD VALUES
	Description	: Supports MultiRec handlers of type 1, 2 and 3.
				: Can also support Non-Multirec Menus
	Author		: Vignesh.S
	DATE		: 04-AUG-2013
	Modification History:	
	====================
	SrlNo.	DATE		Author				Description
******************************************************************************************/

// Retrieves values from the field 'customData' and assign to the custom fields
function getMultiRecFieldsFromCustomData(totNoOfRec,currentRec,custFldIds,fldDelimiter){
	currentRec = parseInt(currentRec) + 1;
	if(currentRec > totNoOfRec){
		totNoOfRec = currentRec;
	}

	customFldIds = custFldIds;
	if(arguments.length < 4){
		alert("Missing one of the mandatory arguments while calling getMultiRecFieldsFromCustomData method");
		return false;
	}

	fieldDelimiter = fldDelimiter;
	customFldIds = customFldIds.split("|");
	fieldDelimiter = fieldDelimiter.split("|");

	if(fieldDelimiter.length < customFldIds.length){
		for (var i=0;i<customFldIds.length; i++ ){
			if (fieldDelimiter[i] == undefined){
				alert("The delimeter for the field "+customFldIds[i]+" is not specified");
				return false;
			}
		}
	}

	fnSetUpHiddenFields(totNoOfRec,custFldIds);	// Create hidden fields for internal value assignments

	fnAssignValuesToCustFlds(totNoOfRec,currentRec,custFldIds,fldDelimiter);  // On page Load assign values to the custom fields
	return true;
}



//Called from the function getMultiRecFieldsFromCustomData
function fnAssignValuesToCustFlds(totNoOfRec,currentRec,custFldIds,fldDelimiter){
	var custFldIds = custFldIds.split("|");
	var fldDelimiter = fldDelimiter.split("|");	
	for (var j = 0 ;j < customFldIdDataArray.length; j++){

	//Check if the field delimiter is not of Multirec field
		if(fldDelimiter[j].toUpperCase() == "N") document.getElementById(custFldIds[j]).value = document.getElementById(customFldIdDataArray[j]).value;
		else
		{
			if(document.getElementById(customFldIdDataArray[j]).value.split(fldDelimiter[j])[currentRec - 1] != undefined){				
				if(document.getElementById(custFldIds[j]) != undefined)
					document.getElementById(custFldIds[j]).value = document.getElementById(customFldIdDataArray[j]).value.split(fldDelimiter[j])[currentRec - 1];				
			}
		}
	}

	return;
}


// Called from the function getMultiRecFieldsFromCustomData
// Dynamic generation of hidden fields
function fnSetUpHiddenFields(totNoOfRec,custFldIds){

	var custFldIds = custFldIds.split("|");
	if( (typeof(customFldIdDataArray) == undefined) || (typeof(customFldIdDataArray) == "undefined") ) customFldIdDataArray = new Array();

	for (j = 0 ;j < custFldIds.length; j++){
		if(!document.getElementById(custFldIds[j] + 'Data')){
			customFldIdData = custFldIds[j] + 'Data';
			customFldIdDataArray[j] = customFldIdData;
			
			inpFld = document.createElement('<input type="hidden" id = '+customFldIdData+' name = '+customFldIdData+' >');
			document.getElementsByTagName('table')[0].appendChild(inpFld);

		}
	}
	fnGetCustomFieldsFromCustomData(customFldIdDataArray);
	return;
}

// Sets the custom field values to the field 'customData'
function setMultiRecFieldsToCustomData(totNoOfRec,currentRec,custFldIds,fldDelimiter){
	currentRec = parseInt(currentRec) + 1;
	if(currentRec > totNoOfRec){
		totNoOfRec = currentRec;
	}

	if(arguments.length < 4){
		alert("Missing one of the mandatory arguments while calling setMultiRecFieldsToCustomData method");
		return false;
	}
	fieldDelimiter = fldDelimiter;
	fieldDelimiter = fieldDelimiter.split("|");
	customFldIds = custFldIds.split("|");
	if(fieldDelimiter.length < customFldIds.length){
		for (var i=0;i<customFldIds.length; i++ ){
			if (fieldDelimiter[i] == undefined){
				alert("The delimeter for the custom field "+customFldIds[i]+" is not specified");
				return false;
			}
		}		
	}
	fnSaveCustomDataValues(totNoOfRec,currentRec,custFldIds,fldDelimiter);
	return true;
}

//save custom field values to the dynamically created hidden fields
function fnSaveCustomDataValues(totNoOfRec,currentRec,custFldIds,fldDelimiter){

	fnSetUpHiddenFields(totNoOfRec,custFldIds);	// Create hidden fields for internal value assignments

	var custFldIds = custFldIds.split("|");	
	var fldDelimiter = fldDelimiter.split("|");
	//If its the first and the only page in Multirec
	if(totNoOfRec == 1)
	{
		for (var j = 0 ;j < custFldIds.length; j++){
			if(fldDelimiter[j].toUpperCase() == "N"){
				document.getElementById(customFldIdDataArray[j]).value = document.getElementById(custFldIds[j]).value;				
			}
			else
			document.getElementById(customFldIdDataArray[j]).value = document.getElementById(custFldIds[j]).value + fldDelimiter[j];
		}
	}
	else
	{
		// Re - assigning the values to hidden variables
		for (var j = 0 ;j < customFldIdDataArray.length; j++){
			//Check if the field delimiter is not of Multirec field
			if(fldDelimiter[j].toUpperCase() == "N"){
				document.getElementById(customFldIdDataArray[j]).value = document.getElementById(custFldIds[j]).value;				
			}
			else
			{
				tempVal = document.getElementById(customFldIdDataArray[j]).value.split(fldDelimiter[j]);
				tempVal[currentRec - 1] = document.getElementById(custFldIds[j]).value;
				document.getElementById(customFldIdDataArray[j]).value = tempVal.join(fldDelimiter[j]);
			}
		}
	}

	fnSetCustomFieldsToCustomData(customFldIdDataArray);
	return;
}

// set the custom field values to the product hidden field customData
function fnSetCustomFieldsToCustomData(customFldIdDataArray) {
    setInHiddenData(document.forms[0].screenName,customFldIdDataArray);
	//Added for Workflow
	if(this.WF_IN_PROGRESS == "Y" || this.WF_IN_PROGRESS == "PEAS")
	{
		if(eval(document.forms[0].customFieldNames) != undefined){
			setCustomFieldNamesInHiddenField(document.forms[0].screenName,customFldIdDataArray);
		}
	}
	//end
}

// get the custom field values from the hidden field customData
function fnGetCustomFieldsFromCustomData(customFldIdDataArray) {
    getFromHiddenData(document.forms[0].screenName,customFldIdDataArray);
}

