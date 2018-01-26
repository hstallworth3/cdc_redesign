var increment = 0;
var incr = 0;
var nvDelimiter = "~";
var pairDelimiter = "^";
var lineDelimiter = "|";
var startChildDelimiter = "[";
var endChildDelimiter = "]";

var codeSetPairDelimiter="|";
var codeSetNVDelimiter="$";

var ChildWindowHandle = null;
var popupBackupData = "";


var gBatchEntryDelete=true;
var gBatchEntryIndex=0;
var gSubmitOnce = true;
var gXSLType = "";

var onChangeFlag = "";

var popUpWidth = 650;
var popUpHeight = 500;
var gChildWindowPrompt = "You have opened a child window and are attempting ignore it. Please close all child windows before continuing."; 

var turnOffParentSubmit = false;

var validationArray = new Array(5);
var batchEntryValidationArray = new Array(5);
var tabValidationArray = new Array(5);
var tabHotKeyArray = new Array();
var tabSwitchFunctionArray = new Array();

/*
 * New functions for the browser compatibility
 * */

/*
blockEnterKey(): prevent the page of being submitted when enter key is pressed in new browsers
*/

function blockEnterKey()
{
	    document.onkeypress = function (e){
			if (e.which=='13' && !(document.activeElement!=null && document.activeElement!=undefined && !(document.activeElement.name=="text"
			|| document.activeElement.name=='undefined'
			|| document.activeElement.name==null
			|| document.activeElement.name.indexOf("_textbox")!=-1)))
				return false;
		}
}

/*
nextElementSibling(): browser agnostic method for nextSibling
*/

function nextElementSibling(elementSibiling) {
	
	do {
		elementSibiling = elementSibiling.nextSibling
	}while ( elementSibiling && elementSibiling.nodeType !== 1 );
	
	return elementSibiling;
}

 function disableAllBrowsers(element){
	
	element.attr("disabled", true);
	element.css("color","grey");//Firefox
}
function enableAllBrowsers(element){
	
	element.attr("disabled", false);
	element.css("color","black");//Firefox
}


/*
setAttributeClass(): set the className with the corresponding value
*/

function setAttributeClass(element, value){
		element.className = value;//IE
		element.setAttribute("class",value);
}

/*
setText(): set the text value to the element
*/

function setText(element,value){
	element.innerText = value;
	element.textContent = value;
}

/*
 * openWindow():
 * 
 * 
 * */
function openWindow(URL, o, dialogFeatures, divElt, name){
	 
	 var modWin;
	 
	 if (name==null)
		 name="";
	
	if(window.showModalDialog!=undefined)
		modWin = window.showModalDialog(URL, o, dialogFeatures);
	else{
		
		dialogFeatures = dialogFeatures.replace("dialogWidth","width").replace("dialogHeight",",height").replace("dialog Height","height").replace(new RegExp(";","g"),",").replace(new RegExp(":", "g"),"=");
		//if the last characters is ",", it is removed:
		
		var dialogFeaturesSize = dialogFeatures.length;
		if(dialogFeatures.charAt(dialogFeaturesSize-1)==",")
			dialogFeatures=dialogFeatures.substring(0,dialogFeaturesSize-1);
		ChildWindowHandle = window.open(URL,name,dialogFeatures);
		modWin=ChildWindowHandle;
		
	}
		
	if(divElt!=null && divElt !=undefined)
		divElt.style.display = "";

	return modWin;
}
 
/*
 * getDialogArgument():
 * 
 * 
 * */

function getDialogArgument(){
	
	var o = window.dialogArguments;
	var opener;
	 
	if(o==undefined)
		opener = window.opener;
	else
		opener = o.opener;
	
	return opener;
}



/*
 * getCorrectFirstChild():
 * 
 * 
 * */



function getCorrectFirstChild(element){


	var correctChild;

	
	if(element.firstElementChild!=null)
		correctChild = element.firstElementChild;//No IE
	else
		correctChild = element.firstChild;//IE
		
	
	return correctChild;
	
}


/*
getCorrectAttribute(): if the IE pattern (element.attribute) works, it returns attributeIE (element.attribute).
if it doesn't work and it isn't a text, it returns element.getAttribute(attribute).
*/

function getCorrectAttribute(element, attribute, attributeIE){
	
	
	var correctAttribute;
		
	if(attributeIE!=null && attributeIE!=undefined)
		correctAttribute=attributeIE;
	else
		if(element!=null && element!=undefined){
			
			//If it is not a text
			if(element.nodeName!=null && element.nodeName.indexOf("#text")==-1)
				correctAttribute=element.getAttribute(attribute);
			
		}
	
	return correctAttribute;

}

/*
 getElementByIdOrByName(): if getElementById return null or undefined, use getElementByName
 */
function getElementByIdOrByName(name){
	
	var element;
		if(document.getElementById(name)==null || document.getElementById(name)==undefined)
			element = document.getElementsByName(name)[0];
		else
			element = document.getElementById(name);
		
		return element;
}
	
function getElementByIdOrByNameNode(name, node){
	
	var element;
		if(node.getElementById(name)==null || node.getElementById(name)==undefined)
			element = node.getElementsByName(name)[0];
		else
			element = node.getElementById(name);
		
		return element;
}

function setElementValueByIdName(elementId, val)
{
  // We find id.
  if( $j("#" + elementId).length > 0 )   {
    $j("#" + elementId).val( val );
  } else {
    $j("input[name=" + elementId + "]").val(val);
  }
}

function CheckForChildWindow(){

	if(ChildWindowHandle!=null && !(ChildWindowHandle.closed)){
		//alert(gChildWindowPrompt);
		return false;
	} else
		return true;
}

/*
refreshSelectOne(): There's an issue with IE11 and select-one elements. After selecting an element on a batch entry table,
 the second time the drop down icon is clicked, it still shows the previous status (position on the list of the last 
 element selected). This method changes the value of one of the properties of the drop down in order to refresh it.
*/

function refreshSelectOne(){

	var selectElements = document.getElementsByTagName("select");

	if(selectElements!=null){
		for(i=0;i<selectElements.length;i++){
			if(selectElements[i].type=="select-one"){ 
				var size = selectElements[i].size;
				selectElements[i].size=size+1;
				selectElements[i].size=size;
			}
		}
	}
}

/*
refreshLabels(): IE11 issue with legacy pages. After clicking on the dropdown and click outside, the label doesn't show.
*/

function refreshLabels(){

	var labels = document.getElementsByTagName("label");

	if(labels!=null){
		for(i=0;i<labels.length;i++){
				labels[i].textContent = labels[i].textContent;
		}
	}
}

function NoOp()
{
	return;
}
function showAlert()
{
   var labelList = new Array();
   var alertMessage = makeErrorMsg('ERR162', labelList.concat(""));
   //alert("Please select Cancel button to exit out of this screen");
   alert(alertMessage);
}

/** This will open a modal window to select a condition. The background is grayed out
	when this window is displayed. */
function selectCondition()
{
	var parentElt = getElementByIdOrByName("blockparent");
	parentElt.setAttribute("class", "modalWindowParent");
    parentElt.style.display = "block";
     
    var o = new Object();
    o.opener = self;
    
    var modWin = openWindow("/nbs/AggregateSummary.do?method=loadConditions", o, GetDialogFeatures(560, 250, false, true), parentElt, "");
 
}

function navbarHitTarget(target)
{
	if(CheckForChildWindow())
		window.location = target;
}
/*
function checkAll(field) {
field.checked=true;
for (i = 0; i < field.length; i++) {
field[i].checked = true; }
}
function unCheckAll(field) {
field.checked=false;
for (i = 0; i < field.length; i++) {
field[i].checked = false; }
}
*/



function findNameForValue(list, code)		{
	var name = "";
	var wholeList = getElementByIdOrByName(list);
	if (wholeList != null)	{
		var items = wholeList.value.split(codeSetPairDelimiter);
		for (var i=0; i < items.length-1; i++) {
			var nameValue = items[i].split(codeSetNVDelimiter);
			if (nameValue.length > 1)	{
				var codeValue = nameValue[0];
				var codeName = nameValue[1];
				if (codeValue == code)	{
					name = codeName;
				}
			}
		}
	}

	return name;
}

function PassSelectDescriptionText(currentHiddenNodeId, currentSelect) {

	var hiddenNode = getElementByIdOrByName("hiddenSelectDescTxt" + currentHiddenNodeId);
	
	if(currentSelect.selectedIndex>-1)
		hiddenNode.value = currentSelect.options[currentSelect.selectedIndex].text;
}

//move focus to the next input element when string length limit reached
function moveFocusParsedInputs(position, sub, name, e) {
	
	var keyvar;
	
	if(e!=null)
		keyvar=e.which;
	else
		keyvar=event.keyCode;


	if ((9==keyvar || 16==keyvar)) {
		//don't switch focus when shift+tab 
		
	} else {
		if(position == 1){
			
			if(sub.value.length==Number(sub.getAttribute("maxlength")))
				getElementByIdOrByName(name+"2").focus();
			
		}
		if(position == 2){
			
			if(sub.value.length==Number(sub.getAttribute("maxlength")))
				getElementByIdOrByName(name+"3").focus();
			
		}
	}
}

function addPhoneString(position, subTel, name) {
	  var telephone = getElementByIdOrByName(name);

	  //split the phone in to hyphen pieces
	  if(telephone.value=="")
	  	telephone.value = "--";

	  var pieces = telephone.value.split("-");


		if(position == 1){
			pieces[0]=subTel.value;

		}
		if(position == 2){
			pieces[1]=subTel.value;
		}
		if(position == 3){
			pieces[2]=subTel.value;
		}

	//recreate the telephone
	telephone.value=pieces[0]+"-"+pieces[1]+"-"+pieces[2];
	if(telephone.value=="--")
		telephone.value="";


}

function showPhoneString(name){
	var teleHiddenNode = getElementByIdOrByName(name);
	var telePart1 = getElementByIdOrByName(name+"1");
	var telePart2 = getElementByIdOrByName(name+"2");
	var telePart3 = getElementByIdOrByName(name+"3");

	telePart1.value=teleHiddenNode.value.substr(0,3);
	telePart2.value=teleHiddenNode.value.substr(4,3);
	telePart3.value=teleHiddenNode.value.substr(8,4);
}

function disableTelephoneTextboxes(name){

	var teleHiddenNode = getElementByIdOrByName(name);
	var telePart1 = getElementByIdOrByName(name+"1");
	var telePart2 = getElementByIdOrByName(name+"2");
	var telePart3 = getElementByIdOrByName(name+"3");


	telePart1.disabled=true;
	telePart2.disabled=true;
	telePart3.disabled=true;
}


function addSSNString(position, subSSN, name) {
	  var SSN = getElementByIdOrByName(name);


	  if(SSN.value=="")
	  	  	SSN.value = "--";

	  var pieces = SSN.value.split("-");

	if(position == 1){
		pieces[0]=subSSN.value;
	}
	if(position == 2){
		pieces[1]=subSSN.value;
	}
	if(position == 3){
		pieces[2]=subSSN.value;
	}

	SSN.value=pieces[0]+"-"+pieces[1]+"-"+pieces[2];
		if(SSN.value=="--")
			SSN.value="";

}
function updateHHMM(text, hidden){
	
	var numericVal1Node = getElementByIdOrByName(hidden+".numericValue1");
	var numericVal2Node = getElementByIdOrByName(hidden+".numericValue2");
	var numericSeparatorNode = getElementByIdOrByName(hidden+".separatorCd");
	
	var pieces = text.value.split(":");
	numericVal1Node.value=pieces[0];
	numericVal2Node.value=pieces[1];
	numericSeparatorNode.value=":";
	
}


function getDataWait(current,target, parentID)
{
	
	window.anyObject2=current;
	var timeoutInt = window.setTimeout("getData(anyObject2" + ",'"+ target +"','"+ parentID + "')", 1000);
}

function getData(current,target, parentID){
	if(ChildWindowHandle==null || (ChildWindowHandle!=null && ChildWindowHandle.closed==true))
	{
		var x = screen.availWidth;
		var y = screen.availHeight;
		
		var selectedCd = "";
		//alert(current.options);
		//alert(getElementByIdOrByName('defaultStateFlagEx'));
		//alert(getElementByIdOrByName('defaultStateFlagEx').value);
		//alert(onChangeFlag);
                if(current.options == null && getElementByIdOrByName('address[i].thePostalLocatorDT_s.stateCd')!=null)
                {
                   selectedCd = getElementByIdOrByName('address[i].thePostalLocatorDT_s.stateCd').value;
                }
                else if(current.options != null && getElementByIdOrByName('defaultStateFlagEx')!= null && getElementByIdOrByName('defaultStateFlagEx').value =="true" && current.selectedIndex==0)
		{
		   selectedCd = getElementByIdOrByName('defaultState').value;
		   if(getElementByIdOrByName('address[i].thePostalLocatorDT_s.stateCd')!=null)
		      getElementByIdOrByName('address[i].thePostalLocatorDT_s.stateCd').value = selectedCd;
		      
		}
                else
                {
			if(current.selectedIndex<0)
				return;
                   	selectedCd = current.options[current.selectedIndex].value;
		}
		var sParent = "";
		if(getCorrectAttribute(current,"parent",current.parent))
			sParent = getCorrectAttribute(current,"parent",current.parent);
		//purge target options
		var targetNode = getElementByIdOrByName(target);
		var removeOpt = getCorrectFirstChild(targetNode);
			
			 while(removeOpt != null){
				  var temp = removeOpt.nextSibling;
				  targetNode.removeChild(removeOpt);
				  removeOpt= temp;
			 }
		getElementByIdOrByName(target + "_textbox").value="";
		targetNode.className="none";
		targetNode.setAttribute("className","none");
		
		
		ChildWindowHandle = window.open("/nbs/dynamicSelect?elementName="+target + '&amp;inputCd=' + selectedCd +'&amp;batchName=' + sParent,"getData","left=" + x + ", top=" + y + ", width=10, height=10, menubar=no,titlebar=no,toolbar=no,scrollbars=no,location=no");
		//self.focus();
		var updateNode = getElementByIdOrByName(target);
		
	}
	else
		getDataWait(current,target, parentID);
}
function setDefaultStateFlagToFalse()
{
   
   if(getElementByIdOrByName('defaultStateFlag')!=null)
   {
     //alert("In the setDefaultStateFlag :"+getElementByIdOrByName('defaultStateFlag').value);
     getElementByIdOrByName('defaultStateFlag').value = false;
   }
   if(getElementByIdOrByName('defaultStateFlagEx')!=null)
   {
     //alert("In the setDefaultStateFlag 2:"+getElementByIdOrByName('defaultStateFlagEx').value);
     getElementByIdOrByName('defaultStateFlagEx').value = false;
   }
}
function setDefaultStateFlagToTrue()
{
   //alert("setting the flag true");
   if(getElementByIdOrByName('defaultStateFlag')!=null)
   {
     //alert("In the setDefaultStateFlag :"+getElementByIdOrByName('defaultStateFlag').value);
     getElementByIdOrByName('defaultStateFlag').value = true;
   }
   if(getElementByIdOrByName('defaultStateFlagEx')!=null)
   {
     //alert("In the setDefaultStateFlag 2:"+getElementByIdOrByName('defaultStateFlagEx').value);
     getElementByIdOrByName('defaultStateFlagEx').value = true;
   }
}

function ReturnState()
{
    //if there is a ChildWindowHandle
    if( (window.opener != null) && (window.opener.closed == false) )
    {
        var loc = window.location;
        if(loc.href.search(/ContextAction=view/)==-1)
        {
            var parent = window.opener;
            var parentDoc = parent.document;
            var parentDataNode = parentDoc.getElementById("PopupDataResult");
            if(parentDataNode!=null)
            {
                var temp = parentDataNode.value;
                parentDataNode.value = temp.replace(/\$\$\$/,popupBackupData);
                parentDataNode.onfocus();
            }
        }
    }
}

function SubmitOnEnter()
{

	
	if(turnOffParentSubmit==false){
		if(event!=null){
		var key=event.keyCode
		if (key==13) {			
			var form = getElementByIdOrByName("nedssForm");
			if (form == null) 
				return false; //only do shortcut for nedssForm			
			submitForm();			
		}
		}
	}
	turnOffParentSubmit=false;
}

function GiveFirstInputFocus(){	
	
	if(gXSLType=='view') {
		callDisableData();
		return;
	}
	var divColl = document.getElementsByTagName("div");
	var contentCellNode = null;
	for( var i=0; i < divColl.length; i++) {
		if(divColl.item(i).getAttribute("focus")=="content"){
			contentCellNode = divColl.item(i);
			break;
		}
	}

	if(contentCellNode!=null){
		var inputColl = contentCellNode.getElementsByTagName("input");
		var selectColl = contentCellNode.getElementsByTagName("select");
		var hrefColl = contentCellNode.getElementsByTagName("a");
		var textElement = null;
		for( var y=0; y < inputColl.length; y++) {
			if (inputColl.item(y).type=="text" || inputColl.item(y).type=="radio"){
				textElement=inputColl.item(y);
				break;
			}
		}
		

	    	var frm = getElementByIdOrByName("nedssForm");
		if (frm != null)
		{
			var a = 9999; 
			var b = 9999; 
			var c = 9999;
			
			if(selectColl.item(0)!=null){
				if(selectColl.item(0).offsetWidth!=0)
					a = getIndex(frm, selectColl.item(0).name);
			}
			if(textElement!=null)
				b = getIndex(frm, textElement.name);		
			if(hrefColl.item(0)!=null)
				c = getIndex(frm, hrefColl.item(0).name);
				
			/*
			alert("selectColl order = " + a);	
			alert("textElement order = " + b);	
			alert("hrefColl order = " + c);	
			
			if ((a < b) && (a < c))
				selectColl.item(0).focus();
			else if ((b < a) && (b < c))
				textElement.focus();
			else if ((c < a) && (c < b))
				hrefColl.item(0).focus();
			*/	
			if (a < b){
				if(selectColl.item(0).offsetWidth!=0)
					selectColl.item(0).focus();
			}else if (b < a){
				if(!textElement.disabled)
					textElement.focus();
			}else {
				if (c != 9999 && hrefColl.item(0) != "javascript:NoOp();")
					hrefColl.item(0).focus();
			}
			return;
		}

		if(selectColl.item(0)!=null){
			if(selectColl.item(0).offsetWidth!=0)
				selectColl.item(0).focus();
			return;
		}else if(textElement!=null){
			if(!textElement.disabled)
				textElement.focus();
			return;
		}else if(hrefColl.item(0)!=null){
			hrefColl.item(0).focus();
			return;
		}

	}
}

function AutoCompleteFormatting(input,mask){

	var key=event.keyCode
	//alert(key);
	//return;

	if(key!=8 && key!=46){

		var position = input.value.length;
		var nextMaskChar = mask.charAt(position);


		if(nextMaskChar!="#")
			input.value = input.value + nextMaskChar;



	}
}

function AutoCompleteFormattingEndCheck(input,mask, focusTarget){
	//var key=event.keyCode
	//alert(Number(key));
	if(input.value.length==mask.length)	{// end of mask , move focus to the next element
	//alert(focusTarget);
		var nextElement = getElementByIdOrByName(focusTarget);

		if(nextElement!=null)
			nextElement.focus();
	}
}

function populateMMWR(yearNode, weekNode, aDate)
{

	var aMMWR = CalcMMWR(aDate);
	var varYear = getElementByIdOrByName(yearNode);
	var varWeek = getElementByIdOrByName(weekNode);
	if(varYear == null || varYear.value == undefined || varYear.value == "")
	varYear.value = aMMWR[0];
	if(varWeek == null || varWeek.value == undefined || varWeek.value == "")
	varWeek.value = aMMWR[1];
}

function checkboxObservationData(checkboxNode)
{
	var nameArr = checkboxNode.name.split(/\|/);
	
	var observationCodeNode = getElementByIdOrByName(nameArr[0]);
	if(observationCodeNode!=null)
	{
	
		if (checkboxNode.checked)
		{

			observationCodeNode.value=checkboxNode.value;

		}
		else
		{

			observationCodeNode.value="";

		}
	}
	
}

function getIndex(what,which) {
    for (var i=0;i < what.elements.length;i++)
        if (what.elements[i].name == which)
            return i;
    return 9999;
}



/**
 * Description:	activates tab change event based on hot key combination of ctrl+t
 *
 * param 
 */
document.onkeypress = function () {
   if (window.event)
     if (window.event.ctrlKey) {
       
       if(event.keyCode==20 && tabHotKeyArray.length>1){	// activate tab switch
       	
       		//need to figure out what tab is current
       		var divNodes = document.getElementsByTagName("div");
       		var currentTab = null;
       		for (var i=0; i < divNodes.length; i++) {
			if( divNodes.item(i).selected == "true" && divNodes.item(i).type=="tab") {
				currentTab = divNodes.item(i).name;
			}
		}
       		//need to switch to the next tab based on the current tab, last tab will go to the first tab
       		
       		for (var i=0; i < tabHotKeyArray.length; i++) {
       			
       			if(tabHotKeyArray[i]==currentTab){
       				
       				if(i+1 > tabHotKeyArray.length-1){
       					
       					Toggle(tabHotKeyArray[0]);	
       				}else{
       					
       					Toggle(tabHotKeyArray[i+1]);
       				}
       			}
       		}
	}
      }
       
}



/**
 * Description:	sorts table based on column
 *
 * param oLink	this object, an anchor link 
 */

function sortTableOnColumn(oLink){
	
	// check if this is ascending or decending
	
	if(getCorrectAttribute(oLink, "sortorder",oLink.sortOrder)==null){
		var sortAttribute = document.createAttribute("sortorder");
		sortAttribute.nodeValue = "ascending";
		oLink.setAttributeNode(sortAttribute);
	} else if(getCorrectAttribute(oLink, "sortorder",oLink.sortOrder)=="ascending")
		oLink.setAttribute("sortorder", "descending");
	else
		oLink.setAttribute("sortorder", "ascending");
		
	
	var sortArray = new Array();
	// need to find the column we are sorting on
	var tdNode = oLink.parentNode;
	while(tdNode.nodeName!="TD")
	{
			tdNode = tdNode.parentNode;
	}
	var currentColumn = getCorrectAttribute(tdNode,"columnNumber",tdNode.columnNumber);
	//alert("Column Number is: " + tdNode.columnNumber);
	
	
	
	var tableNode = oLink.parentNode;
	while(tableNode!=null && (tableNode.nodeName!="TABLE" || tableNode.id!="root"))
	{
			tableNode = tableNode.parentNode;
	}
	//no data condition
	if(tableNode==null)
		return;
	
	//alert(tableNode.innerHTML);
	var tFootNodeArr = tableNode.getElementsByTagName("TFOOT");
	var tFootNode = tFootNodeArr[0];
	var radioButtonValue = null;
	//alert(tFootNodeArr[0].innerHTML);
	
	var counter=0;

	var trNodeArr = tFootNode.getElementsByTagName("tr");
	//alert( "trNodeArr.length is : " + trNodeArr.length );
	for (var i=0; i < trNodeArr.length; i++) {
		if(trNodeArr[i].id=="sortParent"){
	
			var tdNodeArr = trNodeArr[i].getElementsByTagName("td");
			for (var j=0; j < tdNodeArr.length; j++) {
				
				if(getCorrectAttribute(tdNodeArr[j],"columnNumber",tdNodeArr[j].columnNumber)==currentColumn){
					
				//	alert(tdNodeArr[j].firstChild.nodeValue);		
					//initialize array that will be sorted
					//make sure the node type is text
					var firstChildNode = getCorrectFirstChild(tdNodeArr[j]);
				
				//	alert("tdNodeArr[j]==="+tdNodeArr[j]);
			
						
					if(firstChildNode != null){
						radioButtonValue = null;
						//alert("before while " +firstChildNode.nodeType);

						while( (firstChildNode!=null) && (firstChildNode.nodeType!=3) ) {

							if( firstChildNode.nodeName=="INPUT" ) {
					//		      alert( "row=" + j + "  Got <input>" );
							}
							if( (firstChildNode.nodeName=="INPUT")  && 
							    (firstChildNode.type=="radio") && 
							    (firstChildNode.value) ) {
							      radioButtonValue = firstChildNode.value;
							     // alert( "!!!8!RADIO!!!!   >> " + radioButtonValue );
							    }
							firstChildNode = getCorrectFirstChild(firstChildNode);
						}
						
						var comparable=null;
						//alert( "bool: " + radioButtonValue );
						if(radioButtonValue!=null ){
						  comparable = radioButtonValue;
						  //for multiple column sort of date right after yes/no column
						  		if(tdNodeArr[j+3]!=null){
						  			var node = getCorrectFirstChild(tdNodeArr[j+3]);
									while( (node!=null) && (node.nodeType!=3) ) {
										node = getCorrectFirstChild(node);
									}
									if(node!=null){
										if(isDate(node.nodeValue)) {
											var appendDate = DateToSortableString(new Date(node.nodeValue));
											comparable = comparable+appendDate;
										} 
									}
								}
						  /////////////////////////////////////////////////////////////
						} else {
							if(firstChildNode!=null)
						  		comparable = firstChildNode.nodeValue;
						  	else
						  		comparable="";
						}
						
						
						
						var siblingNode = nextElementSibling(trNodeArr[i]);
						if(siblingNode!=null && getCorrectAttribute(siblingNode,"id",siblingNode.id)=="sortChild")
							sortArray[counter] = new Sortable(comparable.toUpperCase(),trNodeArr[i], siblingNode);
						else
							sortArray[counter] = new Sortable(comparable.toUpperCase(),trNodeArr[i]);
						counter=counter+1;

						
					}
				}
				//need to check if the node is a hyperlink
				//alert(tdNodeArr[j].firstChild.nodeValue);
			}
		}
	}
	
	
	// sort the array
	
	sortArray.sort(function(a,b){
					if(a.comparable < b.comparable)
					{
						return -1;
					}
					if(a.comparable > b.comparable)
					{
						return 1;
					}
					return 0;	
		
					});
	
	
	// clear the table if there is a sortable array
	if(sortArray.length>0){
		var removeTR = getCorrectFirstChild(tFootNode);
		while(removeTR != null){
			  var temp = removeTR.nextSibling;
			  tFootNode.removeChild(removeTR);
			  removeTR= temp;
		}
	}
	//	check the sort order and reverse the array if it is descending
	if(getCorrectAttribute(oLink, "sortorder",oLink.sortOrder)=="descending")
		sortArray.reverse();
		
	//	reinitialize the page attribute because pages have been reshuffled because of sort	
	var tHeadNodeArr = tableNode.getElementsByTagName("THEAD");
	
	if(tHeadNodeArr[0]!=null){
		
		for (var i=0; i < sortArray.length; i++) {	
			//alert( "i=" + i + " size:" + sortArray.length + "  sortArray[i] is: " + sortArray[i] + " page=" + (Math.floor(i / tHeadNodeArr[0].pageSize) + 1 )  );
			sortArray[i].rowNode.page = Math.floor(i / getCorrectAttribute(tHeadNodeArr[0],"pageSize",tHeadNodeArr[0].pageSize)) + 1;


		}
	}
	
	var currentPage = null;
	
	var tFootNodeCurrentPage = getCorrectAttribute(tFootNode, "currentPage", tFootNode.currentPage);
	if(tFootNodeCurrentPage==null)
		currentPage=1;
	else
		currentPage = tFootNodeCurrentPage;
	//	add the sorted rows back	
	
	var backColor="Shaded";
	
	for (var i=0; i < sortArray.length; i++) {
		
		if(tHeadNodeArr[0]!=null)
			if(getCorrectAttribute(sortArray[i].rowNode,"page",sortArray[i].rowNode.page)==currentPage){
				
				sortArray[i].rowNode.className = backColor;
				setAttributeClass(sortArray[i].rowNode, backColor);
				if(backColor=="Shaded")
					backColor="NotShaded";
				else
				
					backColor="Shaded";
			}else{
				
				sortArray[i].rowNode.className = "none";
				setAttributeClass(sortArray[i].rowNode, "none");
			}
			
		
		tFootNode.appendChild(sortArray[i].rowNode);
		
		var sortArrayRowChildNode=getCorrectAttribute(sortArray[i],"rowChildNode",sortArray[i].rowChildNode);
		if(sortArrayRowChildNode){
			sortArrayRowChildNode.className = sortArray[i].rowNode.className;
			setAttributeClass(sortArrayRowChildNode, sortArray[i].rowNode.className);
			tFootNode.appendChild(sortArrayRowChildNode);
			
		}
		
		
		
	}
	
	
	
}

/**
 * Description:	object that stores what is being compared and a handle to the tr node so it can be reintroduced into the table
 *
 * param t	the string to be compared
 * param r	handle to the tr node 
 */
function Sortable(t, r, c){


	//use function from nedss.js to determine if comparable is a date type
	t = trimBlanks(t);
	if(isDate(t)) {
		t = DateToSortableString(new Date(t));
	} 
	
	this.comparable = t;
	this.rowNode = r;
	this.rowChildNode=c;
	
}

function findCorrespondingNextPageHref(someNode) {
	var tableNode = getParentTable( someNode );
	if( tableNode == null ) {
		return( null );
	}
	var tFootNodeArr = tableNode.getElementsByTagName("TFOOT");
	//alert( "size = " + tFootNodeArr.length )
	if( (tFootNodeArr==null) || (tFootNodeArr[0]==null) ) {
		return( findCorrespondingNextPageHref( tableNode.parentNode ) );
	}
	var nextPageHref = getNextPageHrefOfTable( tableNode );
	if( nextPageHref == null ) {
		//alert( "findCorrespondingNextPageHref: " + findCorrespondingNextPageHref( tableNode ) );
		return( findCorrespondingNextPageHref( tableNode ) );
	} else {
		return( nextPageHref );
	}
}
function getNextPageHrefOfTable( tableNode ) {
	if( tableNode == null)
		return( null );
	var tHeadNodeArr = tableNode.getElementsByTagName("THEAD");
	if( tHeadNodeArr == null)
		return( null );
	var tHeadNode = tHeadNodeArr[0];
	if( tHeadNode == null )
		return( null );
	var nextPrevTableNodeArr = tHeadNode.getElementsByTagName("TABLE");
	if( nextPrevTableNodeArr == null )
		return( null );
	var nextPrevTableNode = nextPrevTableNodeArr[0];
	if( nextPrevTableNode == null )
		return( null );
	var nextOrPrevHrefArr = nextPrevTableNode.getElementsByTagName("A");
	if( nextOrPrevHrefArr == null )
		return( null );
	var nextOrPrevHref = nextOrPrevHrefArr[1];
	//alert( "getNextPageHrefOfTable returns: " + nextOrPrevHref + "  id=" + nextOrPrevHref.id );
	return( nextOrPrevHref );
}
function findAllNextPrevTableNodes( mainNextPrefHref ) {
	var tableNode = getParentTable( mainNextPrefHref );
	var mainNextPrevTableNode = tableNode;

	tableNode = getParentTable( tableNode );
	var recordsTableNode = tableNode;

	tableNode = getParentTable(tableNode);
	var mostOutterTableNode = tableNode;

	var allInerHrefsArr = mostOutterTableNode.getElementsByTagName("A");	
	//alert( "size of href array: " + allInerHrefsArr.length );
	var nextPrevTableNodesArr = new Array();
	var nextPrevTAbleNodesArrCounter = 0;
	for(var i=0; i<allInerHrefsArr.length; i++ ) {
	  if( allInerHrefsArr[i].id == "next" ) {
	    nextPrevTableNodesArr[nextPrevTAbleNodesArrCounter] = 
		getParentTable(allInerHrefsArr[i]);
	    nextPrevTAbleNodesArrCounter++;
	  }
	}
	return( nextPrevTableNodesArr );
}
function getParentTable( someNode ) {
	var tableNode = someNode.parentNode;
	while(tableNode.nodeName!="TABLE")
	{
		tableNode = tableNode.parentNode;
	}
	return( tableNode );
}

function nextPage(a) {
	
	//alert( "Just a test: " + a );
	var tableNode = getParentTable( a );
	var nextPrevTableNode = tableNode;

	tableNode = getParentTable( tableNode );

	var tFootNodeArr = tableNode.getElementsByTagName("tfoot");
	var tFootNode = tFootNodeArr[0];
	
	var trNodeArr = tFootNode.getElementsByTagName("tr");
	var tHeadNodeArr = tableNode.getElementsByTagName("THEAD");
	
	//initialize the current page attribute
	if(getCorrectAttribute(tFootNode,"currentPage",tFootNode.currentPage)==null){
		var currentPageAttribute = document.createAttribute("currentPage");
		currentPageAttribute.nodeValue = 1;
		tFootNode.setAttributeNode(currentPageAttribute);
	} 
	var nextPage = parseInt(getCorrectAttribute(tFootNode,"currentPage",tFootNode.currentPage))+1;
	var trCount = 0;
	var backColor="Shaded";
	for (var i=0; i < trNodeArr.length; i++) {
		
			if(trNodeArr[i].id=="sortParent"){
				trCount=trCount+1;
				if(trNodeArr[i].page==nextPage){
					trNodeArr[i].className = backColor;
					setAttributeClass(trNodeArr[i], backColor);
					var siblingNode = trNodeArr[i].nextSibling;
					if(siblingNode!=null && siblingNode.id=="sortChild"){
						siblingNode.className=backColor;
						setAttributeClass(siblingNode, backColor);
				}
					if(backColor=="Shaded")
						backColor="NotShaded";
					else
						backColor="Shaded";
				}else{
					trNodeArr[i].className = "none";
					setAttributeClass(trNodeArr[i], "none");
					var siblingNode = trNodeArr[i].nextSibling;
					if(siblingNode!=null && siblingNode.id=="sortChild"){
						siblingNode.className="none";
						setAttributeClass(siblingNode, "none");
					}
				}
			}
	}
	
	
	//initialize the maxPage attribute
		
	if(getCorrectAttribute(a, "maxPage", a.maxPage)==null)
	{
		var maxPageAttribute = document.createAttribute("maxPage");
		maxPageAttribute.nodeValue = (trCount / getCorrectAttribute(tHeadNodeArr[0],"pageSize",tHeadNodeArr[0].pageSize));


		a.setAttributeNode(maxPageAttribute);
			
	} 
	
	
	//set the currentpage attribute
	tFootNode.currentPage = nextPage;

	// now we need to go through the heirarcy to find all NextPrev tables
	// and update each of them.
	var allNextPrevTablesArr = findAllNextPrevTableNodes(a);
	//alert( "FOUND " + allNextPrevTablesArr.length );
	for (var i=0; i < allNextPrevTablesArr.length; i++) {
		// Enable/disable next/previous links
		enableDisableNextPrevOnNextClick( 
			tFootNode, allNextPrevTablesArr[i], getCorrectAttribute(a, "maxPage", a.maxPage)); 
	}

}



function enhancedNextPage(a) {
	
	//alert( "enhancedNextPage: " + a );

	var tableNode = getParentTable( a );
	var nextPrevTableNode = tableNode;

	tableNode = getParentTable( tableNode );


	var tFootNodeArr = tableNode.getElementsByTagName("TFOOT");
	var tFootNode = tFootNodeArr[0];
	
	var trNodeArr = tFootNode.getElementsByTagName("tr");
	var tHeadNodeArr = tableNode.getElementsByTagName("THEAD");
	
	//initialize the current page attribute
	if(getCorrectAttribute(tFootNode, "currentPage", tFootNode.currentPage)==null){
		var currentPageAttribute = document.createAttribute("currentPage");
		currentPageAttribute.nodeValue = 1;
		tFootNode.setAttributeNode(currentPageAttribute);
	} 
	var nextPage = getCorrectAttribute(tFootNode, "currentPage", tFootNode.currentPage) +1;
	var trCount = 0;
	//alert( "number of TRs: " + trNodeArr.length );
	for (var i=0; i < trNodeArr.length; i++) {
				//if( (trNodeArr[i].page!=null) && (trNodeArr[i].rowID!=null) && (trCount<trNodeArr[i].rowID) )  {
				    //trCount = trNodeArr[i].rowID;
					//alert( "trCount:   i=" + i + "  page=" + trNodeArr[i].page )
				//}
				if( trNodeArr[i].page!=null ) 
					trCount = trNodeArr[i].page;
				if( (trNodeArr[i].page!=null) && (trNodeArr[i].page==nextPage) ) {
		//alert( "About to make row #" + i + " visible" );
					trNodeArr[i].className = "visible";
					setAttributeClass(trNodeArr[i].className, "visible");
		//alert( "delete code reminder" );
					var childTablesArr = trNodeArr[i].getElementsByTagName("INPUT");
					if( childTablesArr !=null )
					  for(var t=0; t<childTablesArr.length; t++ ) {
					    childTablesArr[t].className = "visible";
						setAttribute(childTablesArr[t], "visible");
					  }
				/*
					var childTablesArr = trNodeArr[i].getElementsByTagName("TABLE");
					if( childTablesArr !=null )
					  for(var t=0; t<childTablesArr.length; t++ ) {
					    childTablesArr[t].className = "visible";
					  }
					var childTablesArr = trNodeArr[i].getElementsByTagName("TEXTAREA");
					if( childTablesArr !=null )
					  for(var t=0; t<childTablesArr.length; t++ ) {
					    childTablesArr[t].className = "visible";
					  }
					var childTablesArr = trNodeArr[i].getElementsByTagName("TD");
					if( childTablesArr !=null )
					  for(var t=0; t<childTablesArr.length; t++ ) {
					    childTablesArr[t].className = "visible";
					  }
				*/
					var siblingNode = trNodeArr[i].nextSibling;
					//alert( "done making row #" + i + " visible" );
					if(siblingNode!=null && siblingNode.id=="sortChild"){
						siblingNode.className="visible";
					setAttributeClass(siblingNode, "visible");
					}
				}else if( trNodeArr[i].page!=null ) {
					trNodeArr[i].className = "none";
					setAttributeClass(trNodeArr[i], "none");
					var siblingNode = trNodeArr[i].nextSibling;
					if(siblingNode!=null && siblingNode.id=="sortChild"){
						siblingNode.className="none";
						setAttributeClass(siblingNode, "none");
					}
				}
	}
	
	
	//initialize the maxPage attribute
		
	if(getCorrectAttribute(a, "maxPage", a.maxPage)==null)
	{
		var maxPageAttribute = document.createAttribute("maxPage");
		//maxPageAttribute.nodeValue = (trCount / tHeadNodeArr[0].pageSize);
		maxPageAttribute.nodeValue = trCount;
		a.setAttributeNode(maxPageAttribute);
	} 

		//alert( "trCount=" + trCount + " pageSize=" + tHeadNodeArr[0].pageSize + "\n  maxPage=" + a.maxPage + "  nextPage=" + nextPage );
	
	
	//set the currentpage attribute
	tFootNode.currentPage = nextPage;

	// now we need to go through the heirarcy to find all NextPrev tables
	// and update each of them.
	var allNextPrevTablesArr = findAllNextPrevTableNodes(a);
	for (var i=0; i < allNextPrevTablesArr.length; i++) {
		// Enable/disable next/previous links
		enableDisableNextPrevOnNextClick( 
			tFootNode, allNextPrevTablesArr[i], getCorrectAttribute(a, "maxPage", a.maxPage)); 
	}
}


function enhancedPreviousPage(a) {
	
	var tableNode = getParentTable( a );
	var nextPrevTableNode = tableNode;

	tableNode = getParentTable( tableNode );


	var tFootNodeArr = tableNode.getElementsByTagName("TFOOT");
	var tFootNode = tFootNodeArr[0];
	
	var trNodeArr = tFootNode.getElementsByTagName("tr");
	var tHeadNodeArr = tableNode.getElementsByTagName("THEAD");
	
	//initialize the current page attribute
	if(getCorrectAttribute(tFootNode, "currentPage", tFootNode.currentPage)==null){
		var currentPageAttribute = document.createAttribute("currentPage");
		currentPageAttribute.nodeValue = 1;
		tFootNode.setAttributeNode(currentPageAttribute);
	} 
	var nextPage = getCorrectAttribute(tFootNode, "currentPage", tFootNode.currentPage) -1;
	var trCount = 0;
	for (var i=0; i < trNodeArr.length; i++) {
				if( trNodeArr[i].page!=null ) 
				    trCount = trNodeArr[i].page;
				if( (trNodeArr[i].page!=null) && (trNodeArr[i].page==nextPage) ) {
					//alert( "About to make row #" + i + " visible" );
					trNodeArr[i].className = "visible";
					setAttributeClass(trNodeArr[i], "visible");
					var siblingNode = trNodeArr[i].nextSibling;
					//alert( "done making row #" + i + " visible" );
					if(siblingNode!=null && siblingNode.id=="sortChild"){
						siblingNode.className="visible";
						setAttributeClass(siblingNode, "visible");
				}
				}else if( trNodeArr[i].page!=null ) {
					trNodeArr[i].className = "none";
					setAttributeClass(trNodeArr[i], "none");
					var siblingNode = trNodeArr[i].nextSibling;
					if(siblingNode!=null && siblingNode.id=="sortChild"){
						siblingNode.className="none";
				setAttributeClass(siblingNode, "none");}

				}
	}
	
	
	//initialize the maxPage attribute
		
	if(getCorrectAttribute(a, "maxPage", a.maxPage)==null)
	{
		var maxPageAttribute = document.createAttribute("maxPage");
		//maxPageAttribute.nodeValue = (trCount / tHeadNodeArr[0].pageSize);
		maxPageAttribute.nodeValue = trCount;
		a.setAttributeNode(maxPageAttribute);
	} 
	
	
	//set the currentpage attribute
	tFootNode.currentPage = nextPage;

	// now we need to go through the heirarcy to find all NextPrev tables
	// and update each of them.
	var allNextPrevTablesArr = findAllNextPrevTableNodes(a);
	//alert( "FOUND " + allNextPrevTablesArr.length );
	for (var i=0; i < allNextPrevTablesArr.length; i++) {
		// Enable/disable next/previous links
		enableDisableNextPrevOnNextClick( 
			tFootNode, allNextPrevTablesArr[i], getCorrectAttribute(a, "maxPage", a.maxPage)); 
	}
}

// Enable/disable next/previous links when Next link is clicked
function enableDisableNextPrevOnNextClick (tFootNode, someNextPrevTableNode, maxPage) {
	//alert( "inside of enableDisableNextPrevOnNextClick()" );
	if( (tFootNode == null) || (someNextPrevTableNode == null) )
		return;
	var nextPrevTDs = someNextPrevTableNode.getElementsByTagName( "TD" );
	if( nextPrevTDs == null )
		return;
	if( getCorrectAttribute(tFootNode, "currentPage", tFootNode.currentPage)!=null) {
		if (getCorrectAttribute(tFootNode, "currentPage", tFootNode.currentPage)>1) {
		  nextPrevTDs[0].className = "visible";
		  setAttributeClass(nextPrevTDs[0], "visible");
		} else {
		  nextPrevTDs[0].className = "none";
		  setAttributeClass(nextPrevTDs[0], "none");
		}
		
		if (getCorrectAttribute(tFootNode, "currentPage", tFootNode.currentPage)<maxPage) {
		  nextPrevTDs[2].className = "visible";
		  setAttributeClass(nextPrevTDs[2], "visible");
		} else {
		  nextPrevTDs[2].className = "none";
		   setAttributeClass(nextPrevTDs[2], "none");
		}

		if( (getCorrectAttribute(tFootNode, "currentPage", tFootNode.currentPage)==1) || (getCorrectAttribute(tFootNode, "currentPage", tFootNode.currentPage)==maxPage) ) {
		  nextPrevTDs[1].className = "none";
		  setAttributeClass(nextPrevTDs[1], "none");
		} else {
		  nextPrevTDs[1].className = "visible";
		  setAttributeClass(nextPrevTDs[1], "visible");
		}
	}
}

// Enable/disable next/previous links when Previous link is clicked
function enableDisableNextPrevOnPrevClick (tFootNode, someNextPrevTableNode) {
	enableDisableNextPrevOnNextClick( tFootNode, someNextPrevTableNode );
}

function previousPage(a){
	var tableNode = getParentTable( a );
	var nextPrevTableNode = tableNode;
	tableNode = getParentTable( tableNode );

	var tFootNodeArr = tableNode.getElementsByTagName("tfoot");
	var tFootNode = tFootNodeArr[0];
	var trNodeArr = tFootNode.getElementsByTagName("tr");
	var tHeadNodeArr = tableNode.getElementsByTagName("THEAD");
	
	if(getCorrectAttribute(a, "maxPage",a.maxPage)==null) {
		var maxPageAttribute = document.createAttribute("maxPage");
		maxPageAttribute.nodeValue = (trNodeArr.length / getCorrectAttribute(tHeadNodeArr[0],"pageSize",tHeadNodeArr[0].pageSize));
		a.setAttributeNode(maxPageAttribute);
	}
	var nextPage = getCorrectAttribute(tFootNode, "currentPage", tFootNode.currentPage)-1;//getCorrectAttribute(tFootNode, "currentPage", tFootNode.currentPage) -1;
	var backColor="Shaded";	
	for (var i=0; i < trNodeArr.length; i++) {
			if(trNodeArr[i].id=="sortParent"){
				if(trNodeArr[i].page==nextPage){
					trNodeArr[i].className = backColor;
					setAttributeClass(trNodeArr[i], backColor);
					var siblingNode = trNodeArr[i].nextSibling;
					if(siblingNode!=null && siblingNode.id=="sortChild"){
						siblingNode.className=backColor;
						setAttributeClass(siblingNode, backColor);
					}
						
					if(backColor=="Shaded")
						backColor="NotShaded";
					else
						backColor="Shaded";					
				}else{
					trNodeArr[i].className = "none";
					setAttributeClass(trNodeArr[i], "none");
					var siblingNode = trNodeArr[i].nextSibling;
					if(siblingNode!=null && siblingNode.id=="sortChild"){
						siblingNode.className="none";
					setAttributeClass(siblingNode, "none");
					}
				}
			}
	}
	//set the currentpage attribute
	tFootNode.currentPage = nextPage;


        // now we need to go through the heirarcy to find all NextPrev tables
        // and update each of them.
        var allNextPrevTablesArr = findAllNextPrevTableNodes(a);
        //alert( "FOUND " + allNextPrevTablesArr.length );
        for (var i=0; i < allNextPrevTablesArr.length; i++) {
                // Enable/disable next/previous links
                enableDisableNextPrevOnPrevClick(
                        tFootNode, allNextPrevTablesArr[i], getCorrectAttribute(a, "maxPage", a.maxPage));
        }
}


// Enable/disable next/previous links when Prev link is clicked
function enableDisableNextPrevOnPrevClick (tFootNode, someNextPrevTableNode, maxPage) {
	//alert( "inside of enableDisableNextPrevOnNextClick()" );
	if( (tFootNode == null) || (someNextPrevTableNode == null) )
		return;
	var nextPrevTDs = someNextPrevTableNode.getElementsByTagName( "TD" );
	if( getCorrectAttribute(tFootNode, "currentPage", tFootNode.currentPage)!=null) {
		if (getCorrectAttribute(tFootNode, "currentPage", tFootNode.currentPage)<maxPage) {
	 	  //alert( "making NEXT visible" );
		  nextPrevTDs[1].className = "visible";
		  nextPrevTDs[2].className = "visible";
		  setAttributeClass(nextPrevTDs[1], "visible");
		  setAttributeClass(nextPrevTDs[2], "visible");
		} else {
	 	  //alert( "making NEXT NOT visible" );
		  nextPrevTDs[1].className = "none";
		  nextPrevTDs[2].className = "none";
		  		  setAttributeClass(nextPrevTDs[1], "none");
		  setAttributeClass(nextPrevTDs[2], "none");
		}
		if (getCorrectAttribute(tFootNode, "currentPage", tFootNode.currentPage)>1) {
	 	  //alert( "making PREV visible" );
		  nextPrevTDs[0].className = "visible";
		  nextPrevTDs[1].className = "visible";
		  	  		  setAttributeClass(nextPrevTDs[0], "visible");
		  setAttributeClass(nextPrevTDs[1], "visible");
		} else {
	 	  //alert( "making PREV NOT visible" );
		  nextPrevTDs[0].className = "none";
		  nextPrevTDs[1].className = "none";
		  	  		  setAttributeClass(nextPrevTDs[0], "none");
		  setAttributeClass(nextPrevTDs[1], "none");
		}
	}
}
function defaultcountry(form)
{
   var state = "";
	      
   if(form == "basic")
   {
    
    	state = getElementByIdOrByName("stateCd");
    	if(state!=null && state.value !=null && state.value!="")
	{
		getElementByIdOrByName("countryCd").value="840";
		getElementByIdOrByName("hiddenCountryCd").value="840";
		getElementByIdOrByName("countryCd").disabled = true;
		//type ahead integration
		getElementByIdOrByName("countryCd_textbox").value=getElementByIdOrByName("countryCd").options[getElementByIdOrByName("countryCd").selectedIndex].text;
		getElementByIdOrByName("countryCd_textbox").disabled=true;
		getElementByIdOrByName("countryCd_button").className="none";
	}
	else
	{
		getElementByIdOrByName("countryCd").value="";
		getElementByIdOrByName("countryCd").disabled=false;
		getElementByIdOrByName("countryCd_textbox").value="";
		getElementByIdOrByName("countryCd_textbox").disabled=false;
		getElementByIdOrByName("countryCd_button").className="visible";
		
	}
   }
   else if(form == "extended")
   {
	state = getElementByIdOrByName("address[i].thePostalLocatorDT_s.stateCd");
	if(state!=null && state.value !=null && state.value!="")
	{
		var cntryNode = getElementByIdOrByName("address[i].thePostalLocatorDT_s.cntryCd");
		cntryNode.value="840";
		cntryNode.disabled=true;
		//type ahead integration
		getElementByIdOrByName("address[i].thePostalLocatorDT_s.cntryCd_textbox").value=cntryNode.options[cntryNode.selectedIndex].text;
		getElementByIdOrByName("address[i].thePostalLocatorDT_s.cntryCd_textbox").disabled=true;
		getElementByIdOrByName("address[i].thePostalLocatorDT_s.cntryCd_button").className="none";
	}
	else
	{
		getElementByIdOrByName("address[i].thePostalLocatorDT_s.cntryCd").value="";
		getElementByIdOrByName("address[i].thePostalLocatorDT_s.cntryCd").disabled=false;
		getElementByIdOrByName("address[i].thePostalLocatorDT_s.cntryCd_textbox").value="";
		getElementByIdOrByName("address[i].thePostalLocatorDT_s.cntryCd_textbox").disabled=false;
		getElementByIdOrByName("address[i].thePostalLocatorDT_s.cntryCd_button").className="visible";
	}
   }
   else if(form == "event")
   {
      if(getElementByIdOrByName("countryCd")!=null && getElementByIdOrByName("countryCd").value == "840")
      {
                getElementByIdOrByName("hiddenCountryCd").value="840";
		getElementByIdOrByName("countryCd").disabled = true;
      }
   }
   
}


	
function notificationCheck(a){
		
		var labelList = new Array();

		var notificationExists = getElementByIdOrByName("NotificationExists");
		
		if(notificationExists.value == "true"){
			//var confirmMsg = "There is a NND notification message request against this investigation, \nYour edits will change the contents of the message.";
			var confirmMsg = makeErrorMsg('ERR137', labelList.concat(""));
			var returnVariable = confirm(confirmMsg);
			if(returnVariable)
				getPage(a);
			else
				return false;
		}
		else{
			getPage(a);
		}
	}

/**
 * Description:	controls tree view 
 *		
 * param @oSwitchImage	image object
 */
function treeViewControl(oSwitchImage){
	
	var tableNode = oSwitchImage.parentNode;
	while(tableNode.nodeName!="TABLE")
	{
			tableNode = tableNode.parentNode;
	}
	
	//switch image
	if(oSwitchImage.src.search(/minus_sign.gif/)==-1){
		// flip the icon to minus
		oSwitchImage.src = "minus_sign.gif";

	} else {
		
		// flip the icon to plus
		oSwitchImage.src = "plus_sign.gif";
	}
	var tBodyNodes = tableNode.getElementsByTagName("TBODY");
	
	
					
			if(tBodyNodes[0].className=="none"){
				tBodyNodes[0].setAttribute("className", "visible");
				tBodyNodes[0].setAttribute("class", "visible");
				
			} else {
				tBodyNodes[0].setAttribute("className", "none");
				tBodyNodes[0].setAttribute("class", "none");
				
			}
		
	
	
}
/**
 * Description:	autocomplete combobox for select input types 
 *		makes visible or non visible based on the state of the clickable icon
 * param 
 */
function AutocompleteExpandListbox(textbox, selectbox)
{
	
  var varTextbox = getElementByIdOrByName(textbox);

  
  if(getCorrectAttribute(varTextbox, "disabled", varTextbox.disabled)!=true){
	  
	  //var _lbox = getElementByIdOrByName(selectbox);
	  
	  var _lbox; 
	  
		var elements = document.getElementsByName(selectbox);

		if(elements!=null && elements!=undefined && elements.length>1){
			for(var i=0; i<elements.length; i++)
				if(elements[i].type=="select-one")
					_lbox=elements[i];
		}
		else
			_lbox = getElementByIdOrByName(selectbox);
		
	  //This is the solution for the dropdown issue with IE11
	  var length = _lbox.getAttribute("length");
	  if(length==null)
		  length=_lbox.length;
	  
	 if(length>5)
		_lbox.setAttribute("size",6);
	 
	  var varOptions = getCorrectAttribute(_lbox, "options",_lbox.options);
	  if (getCorrectAttribute(_lbox,"bNum",_lbox.bNum) == null)
		AutocompleteWidthAdjustment(varTextbox, varOptions);
	  if (_lbox.className == 'none') {
		_lbox.className = 'visible';
		setAttributeClass(_lbox, "visible");
		_lbox.focus();
	  } else{
		_lbox.className = "none";
		setAttributeClass(_lbox, "none");
	  }
  }
  //Fix issue with IE11 and select-one elements
  refreshSelectOne();
}

// store the current cursor location
function storeCaret(textEl) {
	if (textEl.createTextRange)
		if (typeof window.getSelection != "undefined") {
			textEl.caretPos = window.getSelection();
		}
}
 
//	dynamically adjust width based on the listbox
function AutocompleteWidthAdjustment(varTextbox, varOptions) {
	var bNum = 0;
	if (getCorrectAttribute(varTextbox, "bNum", varTextbox.bNum) == null) {
		for (i = 0; i < varOptions.length; i++) {
			bNum = varOptions[i].text.length > bNum ? varOptions[i].text.length : bNum;
		} 
		var bNumAttr = document.createAttribute("bNum");
		bNumAttr.nodeValue = bNum;
		varTextbox.setAttributeNode(bNumAttr);
		if (bNum > 0)
			varTextbox.size = bNum;
		
		if (bNum >= 55)
			varTextbox.size = 55;
	}
}

// variable to store whether we did onchange already during tab, don't want to activate event again on blur
var bProcessTab = false;
var acInitialValue="";

function AutocompleteStoreOnFocusValue(_textbox) {
	acInitialValue = _textbox.value;
}

//	synch the text box with the list box when the user moves focus out
function AutocompleteSynch(textbox, listbox) {
	//var varTextbox = getElementByIdOrByName(textbox);
	//var varListbox = getElementByIdOrByName(listbox);
	
	var varTextbox = getElementByIdOrByName(textbox);
	var varListbox = getElementByIdOrByName(listbox);
	
	var varOptions = varListbox.options;
	
	if (varTextbox == null || varTextbox == undefined) {
	    varTextbox = document.getElementsByName(textbox)[0];
	  }
	
	if (varListbox != null && varOptions != null && varListbox != null) {
		if (varListbox.selectedIndex > -1) {
			varTextbox.value = varOptions[varListbox.selectedIndex].text;

			if (varListbox.onchange && !bProcessTab && varTextbox.value != acInitialValue)
				varListbox.onchange();

		} else
			varTextbox.value = "";
	}
	//reset
	bProcessTab = false;
}


function getCaretPosition (ctrl) {
	var CaretPos = 0;  
	// IE Support
	if (window.getSelection()) {
	ctrl.focus ();
	var Sel = window.getSelection();
	Sel.moveStart ('character', -ctrl.value.length);
	CaretPos = Sel.text.length;

	}
	// Firefox support
	else if (ctrl.selectionStart || ctrl.selectionStart == '0')
	CaretPos = ctrl.selectionStart;
	return (CaretPos);

	}

	function setCaretPosition(element, pos){

		if(ctrl.setSelectionRange)
		{
			ctrl.focus();
			ctrl.setSelectionRange(pos,pos);
		}
		else if (ctrl.createTextRange) {
			var range = ctrl.createTextRange();
			range.collapse(true);
			range.moveEnd('character', pos);
			range.moveStart('character', pos);
			range.select();
		}
		
	}
	

		

//	type ahead functionality
function AutocompleteComboBox(textbox, listbox, pRestricted, pChanged, e) {
	if (pRestricted == null) {
		pRestricted = true;
	}
	//  Create temp variables.
	//var varTextbox = getElementByIdOrByName(textbox);

	//var varListbox = getElementByIdOrByName(listbox);
	
	var varTextbox = getElementByIdOrByName(textbox);
	//var varListbox = getElementByIdOrByName(listbox);

	var varListbox;

	var elements = document.getElementsByName(listbox);

		if(elements!=null && elements!=undefined && elements.length>1){
			for(var i=0; i<elements.length; i++)
				if(elements[i].type=="select-one")
					varListbox=elements[i];

}
		else
			varListbox= getElementByIdOrByName(listbox);
		
	var varOptions = varListbox.options;
	
	//stores the largest string size for option
	if (getCorrectAttribute(varTextbox, "bNum", varTextbox.bNum) == null)
		AutocompleteWidthAdjustment(varTextbox, varOptions);

	varTextbox.style.borderColor = '';
	varTextbox.style.borderStyle = 'inset';
	var varTextValue = varTextbox.value.toUpperCase();
	var varListValue = null;
	var varFound = false;
	var x = 0;
	var y = varOptions.length;
	//  When the listbox changes, just update the textbox and return.
	if (pChanged != null) {
		x = varListbox.selectedIndex;
		if (x != -1) {
			varTextbox.value = varOptions[x].text;
		}
		return;
	}

	//var k = window.event.keyCode;
	
	
	var k;
	
	if(window.event!= null && window.event!=undefined)
			k=window.event.keyCode;
		else
			if(e!=null && e!=undefined)
				k=e.which;
	
	//up arrow key
	if (k == 38) {
		if (varListbox.selectedIndex > 0) {
			varListbox.value = varOptions[varListbox.selectedIndex - 1].value;
			varTextbox.value = varOptions[varListbox.selectedIndex].text;
			if(varTextbox.createTextRange){
				var varRange = varTextbox.createTextRange();
				varRange.select();
			}else
				varTextbox.setSelectionRange(0,varTextbox.value.length);
		}

		return;
	}
	//down arrow key
	if (k == 40) {
		if (varListbox.selectedIndex < varOptions.length - 1) {
			varListbox.value = varOptions[varListbox.selectedIndex + 1].value;
			varTextbox.value = varOptions[varListbox.selectedIndex].text;
			if(varTextbox.createTextRange){
				var varRange = varTextbox.createTextRange();
				varRange.select();
			}else
				varTextbox.setSelectionRange(0,varTextbox.value.length);
		}
		return;
	}

	//  Look for what's in the textbox to be in a listbox option.
	for (x = 0; x < y; x++) {
		varListValue = varOptions[x].text.toUpperCase();
		if (varListValue.indexOf(varTextValue) == 0) {
			varFound = true;
			break;
		}
	}
	//  If you get a match, select it.
	//  Otherwise, deselect whatever is selected.
	varListbox.selectedIndex = (varFound == true) ? x : -1;

	//  Select the characters that were found and entered for you.
	//For IE
	if (varTextbox.createTextRange) {

		if ((pRestricted == true) && (varFound == false)) {

			//removes based on caret position instead of removing last char in string
			if (varTextbox.caretPos != null)
				varTextbox.caretPos.text = "";
			//varTextbox.value = varTextbox.value.substring(0, varTextbox.value.length-1);
			varTextbox.style.borderStyle = 'solid';
			varTextbox.style.borderColor = 'red';

			return;
		}

		var varKeys = ";8;46;37;38;39;40;33;34;35;36;45;";

		if (varKeys.indexOf(";" + k + ";") == -1) {

			var varRange = varTextbox.createTextRange();

			var varOldValue = varRange.text;
			var varNewValue = (varFound == true) ? varOptions[x].text : varOldValue;
			if (varNewValue != varTextbox.value) {
				varTextbox.value = varNewValue;
				varRange = varTextbox.createTextRange();
				varRange.moveStart('character', varOldValue.length);
				varRange.select();

				//perform onchange for i.e. conditional entry

			}
		}
	}//For the rest of browsers
	else{
		
		if ((pRestricted == true) && (varFound == false)) {

			varTextbox.value=varTextbox.value.substring(0, varTextbox.value.length-1);
			
			varTextbox.style.borderStyle = 'solid';
			varTextbox.style.borderColor = 'red';

			return;
		}

		var varKeys = ";8;46;37;38;39;40;33;34;35;36;45;";

		if (varKeys.indexOf(";" + k + ";") == -1) {

			//var varRange = createTextRange();
			var varOldValue=varTextbox.value;
			var varNewValue = (varFound == true) ? varOptions[x].text : varOldValue;

			if (varNewValue != varTextbox.value) {
				
				varTextbox.value = varNewValue;
				varTextbox.setSelectionRange(varOldValue.length,varNewValue.length);
				
			}
		}
		
	}

}

function CheckTab(sel,el)
{
    var fireRulePresent = false;

    // Execute the fireRule method if one exists in the on change event of 
    // the corresponding select box. The fireRuleAndChangeFocusOnTabKey is called 
    // with so that fireRule() is called with moveFocusToNextField param set to
    // true
    var selectBox = getElementByIdOrByName(sel);
    if (selectBox.onchange) 
    {
        var onchangeEvt = "" + selectBox.onchange;
        if (onchangeEvt.indexOf("fireRule") != -1) {
            fireRulePresent = true;
        }
    }
    
    if (fireRulePresent == true) {
       fireRuleAndChangeFocusOnTabKey(sel, selectBox);
    }
    else {
        // Run only in IE
        // and if tab key is pressed
        // and if the control key is pressed
        if(acInitialValue != el.value)
        {
           if ((document.all) && (9==event.keyCode) && !(event.shiftKey))
           {
                // Cache the selection
                if(getElementByIdOrByName(sel).onchange)
                bProcessTab=true;
                setTimeout("ProcessTab('"+ sel + "','" + el.name + "')",0);
                //ProcessTab(sel,el.name );
           }
        }
    }
}

// explicitly move focus based on the onchange method
function ProcessTab(sel,id) {
	
	var focusCandidate = getElementByIdOrByName(getElementByIdOrByName(id).focusCandidate);
	var focusCandidateAC = getElementByIdOrByName(getElementByIdOrByName(id).focusCandidate+"_textbox");
	var focusCandidateClose = getElementByIdOrByName(getElementByIdOrByName(id).focusCandidateClose);
	var focusCandidateCloseAC = getElementByIdOrByName(getElementByIdOrByName(id).focusCandidateClose+"_textbox");
	
	if(getElementByIdOrByName(sel).onchange){
		//bProcessTab=true;
		
		getElementByIdOrByName(sel).onchange();
		
		if(focusCandidateAC!=null){
			
			if(focusCandidateAC.offsetWidth!=0){
				focusCandidateAC.focus();
				
			}else{
				if(focusCandidateCloseAC != null)
					if(focusCandidateCloseAC.offsetWidth!=0)
						focusCandidateCloseAC.focus();
				else if(focusCandidateClose!=null)
					if(focusCandidateClose.offsetWidth!=0)
						focusCandidateClose.focus();
				
			}
		}
		else if(focusCandidate!=null){
			if(focusCandidate.offsetWidth!=0)
				focusCandidate.focus();
			else{
				
				if(focusCandidateCloseAC != null)
					if(focusCandidateCloseAC.offsetWidth!=0)
						focusCandidateCloseAC.focus();
				else if(focusCandidateClose!=null){
					if(focusCandidateClose.offsetWidth!=0)
						focusCandidateClose.focus();
				}
			}
		}
		
	}
}

function getCalDate(obj,anchor)
{
  var cal = new CalendarPopup();
  cal.showYearNavigation();
  var newObj = getElementByIdOrByName(obj);
  cal.select(newObj,anchor,'MM/dd/yyyy');
}


function showTimeronStatusBar(min, sec) {
	var disp;
	if (min <= 9) disp = " 0";
	else disp = " ";
	disp += min + ":";
	if (sec <= 9) disp += "0" + sec;
	else disp += sec; 
	return (disp);
}

function getTimerCountDown() {
	sec--;      
	if (sec == -1) { sec = 59; min--; }

	window.status = "YOUR SESSION WILL TIMEOUT IN: " + showTimeronStatusBar(min, sec);

	if (min == 2 && sec == 0) {
		window.focus();
		alert("YOUR SESSION WILL TIMEOUT IN 2 MINUTES.");
	}	

	if (min == 0 && sec == 0) {
		window.focus();
		alert("YOUR SESSION HAS TIMED OUT.");
		window.location.href = "/nbs/timeout";
	}
	else setTimeout("getTimerCountDown()", 1000);
}

function startCountdown() {}


function focusCriteria()
{
	
	var actSearchCriteria = false;
	if(getElementByIdOrByName("personSearch.actId")!=null && isEmpty(getElementByIdOrByName("personSearch.actId").value) == false) actSearchCriteria = true;
	if(getElementByIdOrByName("personSearch.actType")!=null && isEmpty(getElementByIdOrByName("personSearch.actType").value) == false) actSearchCriteria = true;

	if (actSearchCriteria==true){
		getElementByIdOrByName("personSearch.actId").focus();
	}

	else if(getElementByIdOrByName("personSearch.lastName"))
	{
		getElementByIdOrByName("personSearch.lastName").focus();
	}
	else if(getElementByIdOrByName("providerSearch.lastName"))
	{
		getElementByIdOrByName("providerSearch.lastName").focus();
	}
	else if(getElementByIdOrByName("organizationSearch.nmTxt"))
	{
		getElementByIdOrByName("organizationSearch.nmTxt").focus();
	}
	
}

//This function should be called by every JSP that has enabled the autocomplete to populate values on createLoad and EditLoad
// to populate the selected dropdown value into the autocomplete textbox on top of it.
//Note this also set's the size of the textbox to the longest selection. GT 12-2010

function autocompTxtValuesForJSP()
{
  var selNodes = document.getElementsByTagName("select");
  for (var i = 0; i < selNodes.length; i++) {
    var element = selNodes[i];
    var populate = "";
    var opts = element.options;
    var optsLength = opts.length
    var maxLen = 2;
    var tmpVal = " ";
    for (var j = 0; j < optsLength; j++) {
      tmpVal = opts[j].text;
      if (tmpVal.length > maxLen) {
        maxLen = tmpVal.length;
      }
      if (opts[j].selected) {
        populate = opts[j].text; 
        //var txtbox = getElementByIdOrByName(element.id + "_textbox");
        var txtbox = getElementByIdOrByName(element.id + "_textbox");
        if (txtbox != null)
          txtbox.value = populate;
      }
    }
    //var txtBoxToSz = getElementByIdOrByName(element.id + "_textbox");
    var txtBoxToSz = getElementByIdOrByName(element.id + "_textbox");
    if (txtBoxToSz != null) {
      if (maxLen < 88 && optsLength > 1) {
        txtBoxToSz.size = maxLen;
        if (optsLength < 5)
          element.size = optsLength;
      }
    }
  }
}
	
function autocompTxtValuesForJSPByElement(id) 
{
	//var selNodes = document.getElementsByTagName("select");
	var element = getElementByIdOrByName(id);
	var populate = "";
	var opts = element.options;
	for (var j = 0; j < opts.length; j++) {
		if (opts[j].selected) {
			populate = opts[j].text;
			var txtbox = getElementByIdOrByName(element.id + "_textbox");
			if (txtbox != null)
				txtbox.value = populate;
		}
	}
}

/**
* Display all the options selected in a selected box in the part of the 
* page identified by an ID.
* @param selectElt Reference to the select box element
* @param valuesDisplaySpanId Unique Id of the HTML container (span, table cell, div etc...)
            inside which the options selected in the Select Box are displayed
*/	
function displaySelectedOptions(selectElt, valuesDisplaySpanId) 
{
	var selectedArray = new Array();
	var i;
	var count = 0;
	if (selectElt.options != null) {
		for (i = 0; i < selectElt.options.length; i++) {
			if (selectElt.options[i].selected && selectElt.options[i].text != "") {
				//alert(selectElt.options[i].text);
				selectedArray[count] = selectElt.options[i].text;
				count++;
			}
		}
	}

	var selectedArrayString = "<b> Selected Values: <\/b>";
	if (selectedArray.length == 0) {
		selectedArrayString += "";
	} else {
		selectedArrayString += selectedArray;
	}
	selectedArrayString = selectedArrayString.replace(/,/g, ", ");
	getElementByIdOrByName(valuesDisplaySpanId).innerHTML = selectedArrayString;
}

function displaySelectedSourceVal(selectElt, valuesDisplaySpanId, symbal) 
{
	var selectedArray = new Array();
	var i;
	var count = 0;
	if (selectElt.options != null) {
		for (i = 0; i < selectElt.options.length; i++) {
			if (selectElt.options[i].selected && selectElt.options[i].text != "") {
				//alert(selectElt.options[i].text);
				selectedArray[count] = selectElt.options[i].text;
				count++;
			}
		}
	}

	var selectedArrayString = "<b> Selected Values: <\/b>";
	if (selectedArray.length == 0) {
		selectedArrayString += "";
	} else {
		//selectedArrayString += selectedArray;
		var str = "";
		for (i = 0; i < selectedArray.length; i++) {
			str += selectedArray[i];
			if (selectedArray.length - 1 > i)
				str += " " + symbal + " ";

		}
		selectedArrayString += str;
	}
	//selectedArrayString = selectedArrayString.replace(/,/g , symbal);
	getElementByIdOrByName(valuesDisplaySpanId).innerHTML = selectedArrayString;
}

/**
* Calls the window's print method
*/ 
function printPage()
{
    window.print();
    return false;
}

/**
* Calls the window's close method
*/ 
function closePrinterFriendlyWindow() 
{
	self.close();
	var o = window.dialogArguments;
	var opener = o.opener;
	var pview = getElementByIdOrByName("pamview",opener.document);
	if (pview != null)
		pview.style.display = "none";

	return false;
}

/** 
* Block the UI using the blockUI JQuery plugin
*/
function blockUIDuringFormSubmission()
{
    var saveMsg = '<div class="submissionStatusMsg"> <div class="header"> Saving Changes </div>' +  
        '<div class="body"> <img src="saving_data.gif" alt=""/> Your changes are currently being saved to the system. Please wait ... </div> </div>';         
	$j.blockUI({  
	    message: saveMsg,  
	    css: {  
	        top:  ($j(window).height() - 500) /2 + 'px', 
	        left: ($j(window).width() - 500) /2 + 'px', 
	        width: '500px'
	    }  
	});
}

/** 
* Block the UI using the blockUI JQuery plugin( No Graphic)
*/
function blockUIDuringFormSubmissionNoGraphic()
{
    var saveMsg = '<div class="submissionStatusMsg"> <div class="header"> Page Loading </div>' +  
        '<div class="body">Please wait...  The system is loading the requested page.</div> </div>';         
	$j.blockUI({  
	    message: saveMsg,  
	    css: {  
	        top:  ($j(window).height() - 500) /2 + 'px', 
	        left: ($j(window).width() - 500) /2 + 'px', 
	        width: '500px'
	    }  
	});
}
/** 
* Block the UI using the blockUI JQuery plugin( No Graphic)
*/
function unblockUIDuringFormSubmissionNoGraphic()
{
	$j.unblockUI(); 

}

/**
    Display the errors in the html block identified by the containerId.
    @param containerId - Id of the container that will be used to display the errors.
    @param errors - A string/array of errors        
*/
function displayErrors(containerId, errors)
{
    if ($j("#"+containerId).css("display") == "none") {
        $j("#"+containerId).css("display", "block");
    }
    
    if (errors != null && containerId != null && $j("#"+containerId).length > 0)
    {
        var head = "<b> <a name=\"" + containerId + 
            "_errorMessagesHref\" />  Please fix the following errors: </b> <br/>";
	    var msgElt = $j("#"+containerId).get(0);
	            
	    // determine the type of error value received (using Scriptaculous librarys's
	    // utility methods) and display accordingly.
	    if (Object.isString(errors) && errors.length > 0) {
	        var errorsUl = "<ul><li>" + errors + "</li></ul>";
	        $j(msgElt).html(head + errorsUl);
	    }
	    else if (Object.isArray(errors) && errors.length > 0) {
	        var errorsUl = "<ul>";
	        for (var i = 0; i < errors.length; i++) {
	            errorsUl += "<li>" + errors[i] + "</li>";
	        }
	        errorsUl += "</ul>";
	        $j(msgElt).html(head + errorsUl);
	    }
	    
	    // shift the focus to the error block
	    window.location = "#" + containerId + '_errorMessagesHref';
    }
}

/**
    Hide the global error messages block.
*/
function hideGlobalErrorMessage()
{
    var msgParentElt = $j("#globalFeedbackMessagesBar");
    if (msgParentElt.length > 0)
    {
        if ($j(msgParentElt[0]).find("#errorMessages").length > 0) {
            $j($j(msgParentElt[0]).find("#errorMessages").get(0)).hide();
        }
    }
}

/**
* Display the received message in the error messages container.

* This function tries to see if the error messages container is available 
* for display. If available, it updates the innerHTML of the area with the message.
* If not, it will create a global error message area in the appropriate location
* in the page and then display the message.
* @param errors Array of error messages 
*/ 
function displayGlobalErrorMessage(errors)
{
    // proceed if there are errors
    if (errors != null)
    {
        // construct an error container if one is not available.
        var head = "<b> <a name=\"errorMessagesHref\" />  Please fix the following errors: </b> <br/>";
        var msgParentElt = $j("#globalFeedbackMessagesBar");
        var msgElt = null;
        if (msgParentElt.length > 0)
        {
            if ($j(msgParentElt[0]).find("#errorMessages").length <= 0) {
                $j(msgParentElt[0]).html("<div class=\"infoBox errors\" id=\"errorMessages\"></div>");    
            }
            msgElt = $j(msgParentElt[0]).find("#errorMessages").get(0);
        }
        
        displayErrors("errorMessages", errors);
    }
}

/**
* Display the received message in the global success messages area. 
* This function tries to see if the global success messages area is available 
* for display. If available, it updates the innerHTML of the area with the message.
* If not, it will create a global success message area in the appropriate location
* in the page and then display the message. 
*/ 
function displayGlobalSuccessMessage(message)
{
    var msgParentElt = $j("#globalFeedbackMessagesBar");
    if (msgParentElt.length > 0)
    {
        var msgElt = $j(msgParentElt[0]).find("#successMessages");
        if (msgElt.length > 0) {
            $j(msgElt[0]).html(message);
        }
        else {
            $j(msgParentElt[0]).html("<div class=\"infoBox success\" id=\"successMessages\">" + message + "</div>");
        }
    }
}

function GetDialogFeatures(dialogWidth, dialogHeight, resizable, scroll)
{
var scrollVar="scroll: yes; scrollbars:yes;";


if(scroll==false)
	scrollVar="scroll: no; scrollbars:no;";


    return "dialogWidth: " + dialogWidth + "px;dialogHeight: " + 
            dialogHeight + "px;status: no;unadorned: yes;help: no;" + scrollVar+
            (resizable ? "resizable: yes;" : "");
}

function chkMaxLength(sTxtBox, maxlimit) {
	if (sTxtBox.value.length > maxlimit)
		sTxtBox.value = sTxtBox.value.substring(0, maxlimit);
}

function checkMaxLength(sTxtBox) {
	maxlimit = 2000;
	if (sTxtBox.value.length > maxlimit) {
		sTxtBox.value = sTxtBox.value.substring(0, maxlimit);
	}
}

function openELRViewer(documentID, dateReceivedHidden)
{
  //window.open('/nbs/viewELRDocument.do?method=viewELRDocument&documentUid='+documentID);
  window.open('/nbs/viewELRDocument.do?method=viewELRDocument&documentUid='+documentID+'&dateReceivedHidden='+dateReceivedHidden,'DocumentViewer','width=900,height=700,left=0,top=0,menubar=no,titlebar=no,toolbar=no,scrollbars=yes,location=no, resizable=yes');
}

/**
 * showHideReflex: show/hide the div that contains the Reflex Test Results
 * @param element
 */
    
function showHideReflex(element){

var div = getCorrectFirstChild(element).next().next().next();

if(div.getAttribute("style").indexOf("none")!=-1)
	div.setAttribute("style", "display:");
else
	div.setAttribute("style", "display:none");
}

/**
* indexOfArray: returns true if any of the elements in the array contains the text
*/

function indexOfArray(array,textElement){
	var contains = false;
	for(var i=0; i< array.length; i++){
		if(array[i].indexOf(textElement)!=-1 && array[i].indexOf("<option ")==-1){
			contains=true;
		}
	}
	return contains;
}