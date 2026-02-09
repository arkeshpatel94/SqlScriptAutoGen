/*
	Function gets called on click of prev or next arrow for Page wise list display
	direction is next or prev based on the arrow, next or prev respectively.
*/

function fnArrowClick(direction)
{
	var pageNumber = "pageNumber";
	document.forms[0].target = "listWindow";
	
	var temp  = document.forms[0].inputs.value ;
	var index1 = temp.indexOf(pageNumber);
	var inputNameValues = temp.substring(0,index1+1 + pageNumber.length);	
	var queryString1 = temp.substr(index1+1 + pageNumber.length);
	var index2 = queryString1.indexOf("|");
	var pageNumber = queryString1.substring(0,index2);
	if(direction == 'next')
		pageNumber = parseInt(pageNumber) + 1;
	else
		pageNumber = parseInt(pageNumber) - 1;
	
	var queryString2 = pageNumber + queryString1.substr(index2);

	document.forms[0].inputs.value = inputNameValues.concat(queryString2);
	document.forms[0].submit();
}

function printHTML()
{
	with(document)
	{		
		write('<table width="100%" border="0" cellpadding="0" cellspacing="0" >');
		write('<tr>');
		write('<td class="popuptab">' + pageTitle + '</td>');
		write('</tr>');
		write('</table>');
		
		/* Header Data Start*/
		if((headerNamesBuff[0] == '&nbsp;')||(headerNamesBuff[0] == '')||(headerNamesBuff[0] == '.')){
			//alert("right");
		}else{
			write('<table width="100%" border="0" cellpadding="0" cellspacing="0" >');
			for (var headerCount=0; headerCount<headerNamesBuff.length; headerCount++){
				write('<tr>');
				write('<td class="textlabel">' + headerNamesBuff[headerCount] + '</td>');
				write('<td class="textfielddisplaylabel">' + headerValuesBuff[headerCount] + '</td>');
				write('<td class="textlabel">&nbsp;</td>');
				write('<td class="textfielddisplaylabel">&nbsp;</td>');
				write('</tr>');
			}
			
				if(headerCount > 1){
					write('<tr>');
					write('<td class="textlabel">&nbsp;</td>');
					write('<td class="textlabel">&nbsp;</td>');
					write('<td class="textlabel">&nbsp;</td>');
					write('<td class="textlabel">&nbsp;</td>');
					write('</tr>');
				}
			write('</table>');
		}
		/* Header Data End*/
		
		write('<table class="tableborder" width="100%" border="0" cellpadding="0" cellspacing="0" >');
		write('<tr>');
		write('<td><table width="100%" border="0" cellpadding="0" cellspacing="0" class="innertable">');
		write('<tr>');
		write('<td valign="top">');
		write('<table class="innertabletop1" border="0" cellpadding="0" cellspacing="0" width="100%" frame="vsides">');	

		if (!displayRecs){
			showErrors();
		}else{
			if(multiSelect == "Y")
			{
				isPageWiseListReq = "false";
			}
			/* If page wise list is required then only prev and next arrows are shown */						
			if(isPageWiseListReq == "true"){
				write('<tr><td class="ctext" colspan="' + colsLen + '" align="right">');
				write('<a id="sLnk1">');
				if(pageNumber > 1){
					write('<img  hotKeyId="Prev" src="../Renderer/images/'+applangcode+'/arrowpre.gif" id="prevpage" name="prevpage" onClick="javaScript:fnArrowClick(\'prev\');" width="7" height="14" border="0" >&nbsp;');
					write('</img>');
				}
				write('</a>&nbsp;');
				write('<a id="sLnk2">');
				if((totRecs > 0 )&&(totRecs == pageSize)){
					write('<img  hotKeyId="Next" src="../Renderer/images/'+applangcode+'/arrownext.gif" id="nextpage" name="nextpage" onClick="javaScript:fnArrowClick(\'next\');" width="7" height="14" border="0" >');
					write('</img>');
				}
				write('</a>');
				write('</td>');
				write('</tr>');						
			}
			
			/* Literal Names */

			write('<tr>');
			if(multiSelect == "Y")
			{
			write('<TD class="searcheader">SELECT</TD>');
			colsLen = colsLen-1;
			}
			for (var colCount=0; colCount<colsLen; colCount++){
				write('<td class="searcheader">' + literalsBuff[colCount] + '</td>');
			}
			write('</tr>');

			if(totRecs == 0){
				write('<TR class="searclist1a">');
				write('<TD colspan="' + colsLen + '" ALIGN="left">No records were fetched.');
				write('</TD>');
				write('</TR>');	
			}else{
				showList();
			}
		}
		
		write('</table>');
		write('</td>');
		write('</tr>');
		write('</table>');
		write('</td>');
		write('</tr>');
		write('</table>');

		write('<table width="100%" border="0" cellpadding="4" cellspacing="0">');
		write('<tr>');
		write('<td>');
		write('<br>');
		if((printFlg == "Y") && (parseInt(totRecs)>0)){
			write('<input class="button" type="button" name="Print" value="Print" onclick="javaScript:fnOnClickPrint();"  hotKeyId="Print" >&nbsp;');
		}
		if((printAllFlg == "Y") && (parseInt(totRecs)>0)){
			write('<input class="button" type="button" name="PrintAll" value="Print All" onclick="javaScript:fnOnClickPrintAll();"  hotKeyId="Print" >&nbsp;');
		}
		if(multiSelect == "Y")
		{
		write('<input class="button" type="button" name="Accept" value="Accept" onclick="javaScript:fnSetAndClose();"  hotKeyId="Go" >');		
		write('<input class="button" type="button" name="Cancel" value="Cancel" onclick="javaScript:fnClose();"  hotKeyId="Cancel" >');		
		}
		else
		{
		write('<input class="button" type="button" name="Close" value="Close" onclick="javaScript:fnClose();"  hotKeyId="Cancel" >');		
		}
		write('</td>');
		write('</tr>');
		write('</table>');
	}
}
function chkUnchkBox(recordNum,recId,recValue)
{
	//alert("recordNum="+recordNum+" recId="+recId+" recValue="+recValue);
}

function fnSetAndClose()
{
	var refcode = new Array(1);
	var arg1 = "";
	refcode[0] = arg1;
	
	var counter = 0;
	var returnList = "";
	var recCount = totRecs;
	for(var index = 1; index <= recCount; index++)
	{
		if(document.forms[0].multiSelect[index].checked == true)
		{
			arg1 = arg1 + document.forms[0].multiSelect[index].value + msSeparator;
			msSeparator
			counter = counter + 1;
		}
	}
		arg1 = arg1.substring(0, arg1.length - 1);
	refcode[0] = arg1;
	fnSetValue(arg1,"");
}

function showList(){
	var effColNum = 0;
	var retVal = "";
	var obj = null;
	for (var i=0; i<totRecs; i++){
		document.write('<tr>');
		retVal = getReturnValues(i);

		/* Added for enabling visited link color for the hyperlinks in non-framed modal windows  */
		document.write('<base target=\"_self\">');
		var tabCount = i + 1;
		if( i % 2 == 0){
			locStyle = "searclist1a";
		}
		else{
			locStyle = "searclist2a";
		}
		//document.write('<TR onmouseover="javascript:className=\'searclist3a\' " onmouseout="javascript:className=\' '+locStyle+' \' " class=\' ' +locStyle+' \' >');		
		document.write('<TR class=\' ' +locStyle+' \' >');		

		for(var j=0; j<colsLen; j++){
			/* Column Names are starting with 1. Hence, used effColNum. */
			effColNum = j+1;
			obj = eval("col" + effColNum);
			if(multiSelect == "Y")
			{
				if(effColNum==1)
				{
					with(document)
					{
						write('<TD><input type="checkbox" name="multiSelect" id="multiSelect" value = "' + obj[i] + '" onclick ="chkUnchkBox('+ j+ ',this.id,this.value)"></TD>');
					}
				}
			}
			
			if (isHyperLink(effColNum)){
				if(multiSelect == "Y")
				{
				document.write("<td>"+unescape(obj[i])+"</td>");
				}
				else
				{
				document.write("<td align='"+fieldAlignmentBuff[j]+"'><a onFocus=\"elementFocus=this\" tabindex=\"" + colsLen + "\" href=\'JavaScript:fnSetValue(\""+retVal+"\",\""+j+"\")\'  onkeypress=\'if(window.event.keyCode==13){javascript:fnSetValue(\""+retVal+"\")}\'  onmouseover=\"this.style.cursor = \'hand\';\" onmouseout=\"this.style.cursor = \'default\';\">"+unescape(obj[i])+" </a></td>");
				}
				//document.write("<td><a onFocus=\"elementFocus=this\" tabindex=\"" + colsLen + "\" href=\'JavaScript:fnSetValue(\""+retVal+"\")\'  onkeypress=\'if(window.event.keyCode==13){javascript:fnSetValue(\""+retVal+"\")}\'  onmouseover=\"this.style.cursor = \'hand\';\" onmouseout=\"this.style.cursor = \'default\';\">"+unescape(obj[i])+" </a></td>");
				//document.write("<td>"+unescape(obj[i])+"</td>");
			}else{
				document.write("<td align='"+fieldAlignmentBuff[j]+"'>"+unescape(obj[i])+"</td>");
				//document.write("<td>"+unescape(obj[i])+"</td>");
			}
		}

		document.write('</tr>');
	}
}

function getReturnValues(recNum){
	var str = "";
	for(var i=0; i<colsLen; i++){
		str += eval("col"+(i+1) + "[" + recNum + "]");
		if (i != (colsLen-1))
			str += "|";
	}
	return str;
}

function isHyperLink(colNum){
	var chk = false;
	for(var i=0; i<hyperLnksLen; i++){
		if (parseInt(hyperLnksBuff[i], 10) == colNum){
			chk = true;
			break;
		}
	}
	return chk;
}

function fnClose(){
	window.close();
}

function fnSetValue(str1,str2){
	window.returnValue = str1 + "|" + str2;
	window.close();
}

function fnOnClickPrintAll(){
	var retValArr = [];
	retValArr[0] = "PRINTALL";
	for (var i=0; i<totRecs; i++){
		var retVal = getReturnValues(i);
		retValArr[i+1] = retVal;
	}
	window.returnValue = retValArr;
	window.close();
}

function fnOnClickPrint(){
	var retValArr = [];
	retValArr[0] = "PRINT";
	for (var i=0; i<totRecs; i++){
		var retVal = getReturnValues(i);
		retValArr[i+1] = retVal;
	}
	window.returnValue = retValArr;
	window.close();
}

function showErrors(){
    with (document) {
		write('<TR class="searcheader">');	
		write('<TD ALIGN="left">Error Code</TD>');
		write('<TD ALIGN="left">Type</TD>');
		write('<TD ALIGN="left">Description</TD>');	
		write('</TR>');    

		for (var i=0; i<errCode.length; i++){
			with(document){		
				write('<tr><td align="left">&nbsp;' + errCode[i] + '</td>');
				write('<td align="left">&nbsp;' + errType[i] + '</td>');
				write('<td align="left">&nbsp;' + errDesc[i] + '</td></tr>');
			}
		}
	} //End with()
}
