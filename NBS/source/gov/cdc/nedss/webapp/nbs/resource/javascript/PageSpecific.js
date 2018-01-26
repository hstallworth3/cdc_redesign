//Fix NBS Context issues by disabling Backspace(8) and Enter(13) for appropriate PAGE elements
if (typeof window.event != 'undefined')
  document.onkeydown = function () {
    var t = event.srcElement.type;
    if (t == '' || t == 'undefined' || t == 'button') {
      return;
    }
    var kc = event.keyCode;
    return ((kc != 8 && kc != 13) || (t == 'text' && kc != 13) ||
      (t == 'textarea') || (t == 'submit' && kc == 13) || (t == 'image' && kc == 13));
  }

var newwindow = null;
function parent_disable() {
  if (newwindow && !newwindow.closed) {
    newwindow.focus();
  }
  if (newwindow && newwindow.closed) {
    getElementByIdOrByName("pageview").style.display = "none";
  }
}

function getPage(target) {
  document.forms[0].target = "";
  document.forms[0].action = target;
}


//Pass in the ID of the Jurisdiction Element and the Valid jurisdiactions string found in attributeMap
//If returns "true" new to forward to: SubmitNoViewAccess
function validatePageJurisdiction(jurisID, NBSSecJurisdictionParseString)
{
    var jurisd = getElementByIdOrByName(jurisID);
    if(jurisd == null || (jurisd != null && jurisd.value == "")) {
        return "valid";
    }
    if(NBSSecJurisdictionParseString == null || NBSSecJurisdictionParseString.value == "") {
        return "valid";
    }
    var items = NBSSecJurisdictionParseString.split("|");
    var containsJurisdiction = false;
    var confirmMsg = "If you save the Investigation, you will not be able to view the data because it is outside your permitted Program Area/Jurisdiction. Select OK to continue, or Cancel to not continue.";
    if (items.length > 1) {
        for (var i=0; i < items.length; i++)
        {
            if (items[i]!=""  && items[i] == jurisd.value ) {
                containsJurisdiction = true;
            }
        }
    }
    if(!containsJurisdiction) {
        if(confirm(confirmMsg)) {
            return "true";
        }
        else {
            return "false";
        }
    }

    return "valid";
}

function printAllForms(){
	    var divElt = getElementByIdOrByName("pageview");
	    divElt.style.display = "block";
	    var o = new Object();
	    o.opener = self;

	   // window.showModalDialog("/nbs/PageAction.do?method=printLoadFormPage", o, GetDialogFeatures(400, 340, false));
	    
	    var URL = "/nbs/PageAction.do?method=printLoadFormPage";
	    var modWin = openWindow(URL, o,GetDialogFeatures(400, 420, false, false), divElt, "");
	    
	    return false;
}

function closepopup() {
	self.close();
	var opener = getDialogArgument();
	var invest = getElementByIdOrByName("pageview");
	if (invest == null)
		invest = getElementByIdOrByNameNode("pageview", opener.document);
	if (invest != null)
		invest.style.display = "none";
}

function printPageForm() {
	
	     	var errorMsgArray = new Array();
	     	var errorText;
	var frmName = $j("#formNameid").attr("value");
	var frmContent = $j("input:radio[name=formContent]:checked").val(); 
	         if($j("#formNameid").attr("value")=="") {
	        	 errorText = " Please select a form to print.";
	    	     	errorMsgArray.push(errorText);
	    	     	$j("#formNameid").focus();
	         }
			var params = "&formName="+frmName +"&formContent=" + frmContent;
	         if(getElementByIdOrByName("coinfInv") != null){
	        	 if($j("#coinfInv").attr("value")=="") {
	            	 errorText = " Please select another condition.";
	        	     	errorMsgArray.push(errorText);
	        	     	$j("#coinfInv").focus();
	             }
				var coinf = $j("#coinfInv").attr("value");
				params = params  +"&coinfCondInvUid=" + coinf;
	         }

	     	 if(errorMsgArray.length > 0){
	             displayGlobalErrorMessage(errorMsgArray);
	             return false;
	         }
	     	 
	     	 
	     	//if(window.showModalDialog){
	     		var opener = getDialogArgument();
		opener.document.forms[0].action="/nbs/PageAction.do?method=printSelectedLoad" + params;
		opener.document.forms[0].target = "_blank";
		opener.document.forms[0].submit();
		//Defect 8855 - 2016-05-02
		opener.document.forms[0].target = "_self";
		self.close();
}
/**
    Function used by the popup calendar widget to populate
    the date selected in the text box.
*/
function getCalDate(obj, anchor)
{
    var cal = new CalendarPopup();
    //cal.showNavigationDropdowns(); // dropdowns for year & month

    // do not allow dates starting from the next day.
    var tomorrow = new Date();
    tomorrow.setDate(tomorrow.getDate()+1);
    cal.addDisabledDates(formatDate(tomorrow, "yyyy-MM-dd"), null);

    cal.showYearNavigation(); // << and >> arrows to navigate year & month
    var newObj = getElementByIdOrByName(obj);
    cal.select(newObj,anchor,'MM/dd/yyyy');
}


/**
    Function used by the popup calendar widget to populate
    the date selected in the text box. Allow future dates
*/
function getCalDateFuture(obj, anchor)
{
  var cal = new CalendarPopup();
  cal.showYearNavigation();
  var newObj = getElementByIdOrByName(obj);
  cal.select(newObj,anchor,'MM/dd/yyyy');
}


/** Create a new notification from a PAGE module */
function createPageNotification()
{
    var divElt = getElementByIdOrByName("pageview");
    divElt.style.display = "block";
    var o = new Object();
    o.opener = self;
    //window.showModalDialog("/nbs/page/notification/Notification.jsp", o, GetDialogFeatures(560, 300, false));
    
    var URL = "/nbs/page/notification/Notification.jsp";
    var modWin = openWindow(URL, o,GetDialogFeatures(560, 300, false, true), divElt, "");
    return false;
}

function createNotifications(comments)
{
    JPageForm.updateNotifications(comments, function(data)
    {
        if(data.length == 1) {
            displayGlobalErrorMessage(data[0]);
        }
        else {
            displayGlobalSuccessMessage(data[0]);
        }
        var notifications = data[1];
        if(notifications != null) {
            var notif = getElementByIdOrByName("notificationSection");
            notif.innerHTML = notifications;
            var notifExists = getElementByIdOrByName("NotificationExists");
            notifExists.value="true";

            // set the current value of notification data in the patient summary part of view JSP.
            var tr0 = $j(notif).find("tbody tr").get(0);
            var td5 = $j(tr0).find("td").get(5); // 6th cell contains notification status
            $j("#patientSummaryJSP_view_notificationStatus").html($j(td5).html());
        }
    });
}

function displayNotifications(parentClass)
{
	var childClass = parentClass.replace("parent", "child");
	var tableId = "notificationHistoryTable";
	var tableElt = $j("#" + tableId);
	var parentRowElt = $j(tableElt).find("." + parentClass).get(0);

	var imgSrc = $j($j(parentRowElt).find("img").get(0)).attr("src");
	if (imgSrc.indexOf("minus_sign.gif") >= 0) {
		$j($j(parentRowElt).find("img").get(0)).attr("src", "plus_sign.gif");
	}
	else if (imgSrc.indexOf("plus_sign.gif") >= 0) {
		$j($j(parentRowElt).find("img").get(0)).attr("src", "minus_sign.gif");
	}

	var childRowsElts = $j(tableElt).find("." + childClass);
	for (var i = 0; i < childRowsElts.length; i++) {
		var singleChildRow = $j(childRowsElts[i]);
		if ($j(singleChildRow).css("display") == "none") {
			$j(singleChildRow).removeClass("none");
		}
		else {
			$j(singleChildRow).addClass("none");
		}
	}

}

/** Transfer Ownership of investigation for a PAGE */
function transferPageOwnership()
{
    var divElt = getElementByIdOrByName("pageview");
    divElt.style.display = "block";
    var o = new Object();
    o.opener = self;
    //window.showModalDialog("/nbs/PageAction.do?method=transferOwnershipLoad", o, GetDialogFeatures(850, 500, false));
    var URL = "/nbs/PageAction.do?method=transferOwnershipLoad";
    var modWin = openWindow(URL, o,GetDialogFeatures(850, 500, false, true), divElt, "");
    
    return false;
}

function pageTOwnership(jurisd,exportFacility,comment,documentType ) {
    document.forms[0].action ="/nbs/PageAction.do?method=transferOwnershipSubmit&INV107=" + jurisd+ '&exportFacility=' + exportFacility + '&comment=' + comment;
    
    if (documentType == 'undefined' || documentType == null)
      		document.forms[0].action ="/nbs/PageAction.do?method=transferOwnershipSubmit&INV107=" + jurisd+ '&exportFacility=' + exportFacility + '&comment=' + comment;
      	else
      		document.forms[0].action ="/nbs/PageAction.do?method=transferOwnershipSubmit&INV107=" + jurisd+ '&exportFacility=' + exportFacility + '&comment=' + comment +"&documentType=" +documentType;
    document.forms[0].submit();
}
/** Share PAGE case with other facility*/
function sharePageCaseLoad()
{
    var divElt = getElementByIdOrByName("pageview");
    divElt.style.display = "block";
    var o = new Object();
    o.opener = self;
    //window.showModalDialog("/nbs/PageAction.do?method=sharePageCaseLoad", o, GetDialogFeatures(700, 400, false));
    var URL = "/nbs/PageAction.do?method=sharePageCaseLoad";
    var modWin = openWindow(URL, o,GetDialogFeatures(700, 400, false, true), divElt, "");
    
    return false;
}


function checkAsianRaces()
{
  JBaseForm.clearDetailsAsian(function(data) {
      DWRUtil.removeAllOptions("DEM243");
      DWRUtil.addOptions("DEM243", data, "key", "value");
    });
    var asianRace = getElementByIdOrByName("pageClientVO.asianRace").checked;
    if(asianRace)
    {
        getElementByIdOrByName("DEM243").className="";
        getElementByIdOrByName("asian-multi").className="";
    }
    else
    {
        getElementByIdOrByName("DEM243").className="none";
        getElementByIdOrByName("asian-multi").className="none";

        // reset the selected values displayed
        var selectBox = getElementByIdOrByName("DEM243");
        for (i = 0; i < selectBox.options.length; i++) {
            selectBox.options[i].selected = false;
        }
        displaySelectedOptions(selectBox, "DEM243-selectedValues");
    }
}

function checkHawaiianRaces()
{
  JBaseForm.clearDetailsHawaii(function(data) {
      DWRUtil.removeAllOptions("DEM245");
      DWRUtil.addOptions("DEM245", data, "key", "value");
    });

    var hawaiianRace= getElementByIdOrByName("pageClientVO.hawaiianRace").checked;
    if(hawaiianRace){
        getElementByIdOrByName("DEM245").className="";
        getElementByIdOrByName("hawaiian-multi").className="";
    }
    else
    {
        getElementByIdOrByName("DEM245").className="none";
        getElementByIdOrByName("hawaiian-multi").className="none";

        // reset the selected values displayed
        var selectBox = getElementByIdOrByName("DEM245");
        for (i = 0; i < selectBox.options.length; i++) {
            selectBox.options[i].selected = false;
        }
        displaySelectedOptions(selectBox, "DEM245-selectedValues");
    }
}

function getSelectedOptsString(opts)
{
    var returnVar = "";
    for (var i= 0; i< opts.length; i++)
    {
        if (opts[i].selected) {
            returnVar = returnVar + opts[i].value + ",";
        }
    }

    if(returnVar.length == 0) {
        returnVar = "abcxyz";
    }

    returnVar = returnVar.substring(0,(returnVar.length-1));
    //alert(returnVar);
    return returnVar;
}

/**
    Function called from the window.onload of create/edit of TB and Varicella pages. It gathers all the
    HTML select elements with fireRule function defined in the onchange event. It then proceeds to
    create a new onKeyDown event to all those select boxes found. The onKeyDown event is programmed to look
    for the 'tab key' press, i.e., with a keyCode = 9. If tab key press is detected, the
    MoveFocusToNextField() function to move the focus to the next valid element is called.
*/
function attachMoveFocusFunctionToTabKey()
{
    // attach onKeyDown events for all the select boxes that has a fireRule call assigned to it.
    var selectElts = $j("select[onchange]");
    for (var i = 0; i < selectElts.length; i++)
    {
        var onchangeEvt = "" + $j(selectElts[i]).attr("onchange");
        if (onchangeEvt.indexOf("fireRule") != -1)
        {
            var tmp = onchangeEvt.substring(onchangeEvt.indexOf("fireRule"), onchangeEvt.length);
            var fn = tmp.substring(0, tmp.indexOf(")")+1);
            var fnParamsList = fn.substring(fn.indexOf("(")+1, fn.indexOf(")"));
            var fnParamsArr = fnParamsList.split(",");

            if (fnParamsArr.length >= 2) {
                // fnParamsArr[0] is the HTML id of the select element
                // fnParamsArr[1] is the reference to the select element

                // attach the fireRule call to the onkeydown event for this select box
                $j(selectElts[i]).bind("keydown", function(e){
                    //alert("calling fireRuleAndChangeFocusOnTabKey with params.." + fnParamsArr[0] + ";" + fnParamsArr[1]);
                    var varKey = null;
                    if(window.event!=null) {
                        varKey = window.event.keyCode;
                        // look for tab key. i.e., keyCode = 9
                        if (varKey == "9") {
                            if (window.event.shiftKey) {
                                // if the shiftKey is pressed, return true so that
                                // default tab focus is carried out. i.e., the
                                // focus is moved to the valid HTML element that precedes
                                // this element
                                return true;
                            }
                            else {
                                // if shift key is not pressed, then call the
                                // MoveFocusToNextField() by passing the current element
                                // (select elt) and supress the default browser operation
                                MoveFocusToNextField(this);
                                return false;
                            }
                        }
                    }
                });
            }
        }
    }
}

function fireRuleAndChangeFocusOnTabKey(fieldId, element)
{
    var varKey = null;
    if(window.event!=null) {
        varKey = window.event.keyCode;
        if (varKey == "9") {
            if (window.event.shiftKey) {
                fireRule(fieldId, element, false);
            }
            else {
                fireRule(fieldId, element, true);
            }
        }
    }
}

function fireRule(field, newValue, updateCursorFocus)
{
    var actionMode = getElementByIdOrByName("actionMode") == null ? "" : getElementByIdOrByName("actionMode").value;
    if(actionMode == 'Preview') {
        return;
    }

    var newValueValue = "abcxyz";
    if(newValue.type == 'select-multiple') {
        newValueValue = getSelectedOptsString(newValue.options);
    }
    else {
        if (newValue.value != null && newValue.value != "") {
            newValueValue = newValue.value;
        }
    }

    var fieldAndValue = field + ':' + newValueValue;
    JPageForm.fireRule(fieldAndValue, function(data) {
    	if (data.length == 0) return;
        for (var i = 0; i < data.length; i++) {
            updateFormField(data[i]);
        }

        // move the focus to the first valid field that follows the field that fired this rule.
         if (updateCursorFocus == true) {
            MoveFocusToNextField(newValue);
        }
    });
}

function updateFormField(formField)
{
    // alert("Form Field is changed" + formField.fieldId + ", " +
    // "value is " + formField.string);
    // alert(formField.state.disabled);

    var currentField = $(formField.fieldId);
    var currentFieldTxtBox = $(formField.fieldAutoCompId);
    var currentFieldBtn = $(formField.fieldAutoCompBtn);
    var currentFieldLabelID = formField.fieldId +"L";
    var currentFieldLabel="";
    //alert("currentFieldLabelID. :"+currentFieldLabelID);
    //if(formField.fieldId == "DEM128"){

    currentFieldLabel = getElementByIdOrByName(currentFieldLabelID);
    //alert("currentFieldLabel inside IF :"+currentFieldLabel);
    //}

    // Sets enabled/disabled status, option values, and selected value //
    if (formField.fieldType == "Coded" && currentField != null)
    {
        // alert("Made it into drop-down handler");
        var optionsList = formField.state.values;
        var Lcurrent = currentField.options.length;
        var Lnew = optionsList.length;
        // var newListLarger = Lnew > Lcurrent;
        if (Lnew > Lcurrent) {
            for (var i = 0; i < Lcurrent; i++) {
                currentField.options[i].text = optionsList[i].value;
                currentField.options[i].value = optionsList[i].key;
                currentField.options[i].selected = false;
            }
            for (var i = Lcurrent; i < Lnew; i++) {
                currentField.options[i] = new Option(optionsList[i].value,
                optionsList[i].key);
            }
        }
        else {
            //alert("Is smaller");
            for (var i = 0; i < Lnew; i++) {
                currentField.options[i].text = optionsList[i].value;
                currentField.options[i].value = optionsList[i].key;
                currentField.options[i].selected = false;
            }
            //Temporary Fix for TUB114, Countries multiselect (Need to revisit !!)
            if((formField.fieldId != "TUB228") && (formField.fieldId != "TUB229") && (formField.fieldId != "INV153") && (formField.fieldId != "INV154") && (formField.fieldId != "INV156")){
                currentField.options.length = Lnew;
            }
        }

        // dwr.util.removeAllOptions(formField.fieldId);
        // dwr.util.addOptions(formField.fieldId, formField.state.values, "key", "value" );
        // alert(formField.fieldId+"  " +formField.fieldAutoCompId+" "+formField.state.multiSelVals);
        // alert(formField.value);
        if(formField.value!=null && formField.value!=""){
            dwr.util.setValue(formField.fieldId, formField.value);
            dwr.util.setValue(formField.fieldAutoCompId, formField.value);
            autocompTxtValuesForJSPByElement(formField.fieldId);
         // alert("Here if");
         }else if(formField.state.multiSelVals != null && formField.state.multiSelVals !="" ){

            //alert("Here else");

             dwr.util.setValue(formField.fieldId, formField.state.multiSelVals);
             displaySelectedOptions(getElementByIdOrByName(formField.fieldId), formField.fieldId+'-selectedValues');
         }else if((formField.state.multiSelVals==null || formField.state.multiSelVals=="")){

              dwr.util.setValue(formField.fieldId, "");
	      dwr.util.setValue(formField.fieldAutoCompId, "");
         }

    }

    // Set main control state/value //
    if (currentField != null)
    {
        currentField.disabled = formField.state.disabled;
        if(currentField.disabled) {
            if(currentField.type == 'select-multiple') {
                for (var i=0; i<currentField.options.length; i++) {
                    currentField.options[i].selected = false;
                }
            displaySelectedOptions(getElementByIdOrByName(formField.fieldId), formField.fieldId+'-selectedValues');
            }
            else {
                dwr.util.setValue(formField.fieldId, "");
            }

            dwr.util.setValue(formField.fieldAutoCompId, "");
        }
        //alert("currentFieldLabel In here :"+currentFieldLabel);
        //alert("fieldId In here :"+formField.fieldId);
        //if(formField.fieldId == "DEM128"){
        //alert("currentFieldLabel enabling:"+currentFieldLabel);
        //alert("formField.state.disabled :" +formField.state.disabled);
        //currentFieldLabel.disabled = formField.state.disabled;

        if(currentFieldLabel != null) {
            currentFieldLabel.className = formField.state.disabledString;
        }
        // }

        if (formField.fieldType == "Coded") {
            if(currentFieldTxtBox != null && currentFieldBtn != null) {
                currentFieldTxtBox.disabled = formField.state.disabled;
                currentFieldBtn.disabled = formField.state.disabled;
            }
        }
    }

    //*** Defect15152:This is a temporary fix for "TUB202". Needs to be reworked.
    if((formField.fieldId == "TUB106" || formField.fieldId == "TUB202") && currentField.disabled == false ){
    }
    else
    {
        if(formField.fieldType != "Coded"){
            dwr.util.setValue(formField.fieldId, formField.value);
            dwr.util.setValue(formField.fieldAutoCompId, formField.value);
        }
    }

    if( getElementByIdOrByName(formField.fieldId).type == 'select-multiple'){
        // alert(formField.fieldId);
        var selString = formField.fieldId + '-selectedValues';
        //alert(selString);
        //displaySelectedOptions(formField.fieldId, selString);
    }
}


function pgLoadGetDWRCountiesCities(stateId, countyId)
{

    var actionMode = getElementByIdOrByName("actionMode") == null ? "" : getElementByIdOrByName("actionMode").value;
    if(actionMode != 'Create') {
        return;
    }

    stateNode=getElementByIdOrByName(stateId);
    var state1 = stateNode.value;
    if(state1 == null) {
    	return;
    }
    getDWRCounties(state1, countyId)
    getDWRCitites(state1)
}

/**
 * getValue: this method returns the text of the option with the value received as a parameter
 * @param list
 * @param value
 * @returns {String}
 */

function getValue(list, value){
	
	var textOption = "";
	for(var i = 0; i< list.length; i++){
		if(list.options[i].value==value){
		textOption=list.options[i].text;
		}
	}
	return textOption;
	
}

/**
 * refeshHidden(): this method sets the value selected on the dropdown
 * @param e
 */
function refreshHidden(e){
	var id = e.id;
	getElementByIdOrByName(id+"_Hidden").value=document.getElementById(id).value;
	
}
/**
* selectValue: get the value from the hidden element and sets the value on the dropdown.
Also, onchange event it is added to the dropdown to update the hidden element
*/
function selectValue(id){
	
	if(getElementByIdOrByName(id+"_Hidden")!=null && getElementByIdOrByName(id+"_Hidden")!=undefined){
		var valueSelected = getElementByIdOrByName(id+"_Hidden").value;
		$j("#"+id).val(valueSelected);
		var list = document.getElementById(id);
		var textSelected = 	getValue(list, valueSelected);
		setElementValueByIdName(id + "_textbox", textSelected);
		//refresh the value on the hidden element
		getElementByIdOrByName(id).onchange = function(){refreshHidden(this);};
	}
}

/**
* populateCounty: sets the county on the dropdown
*/
function populateCounty( stateId, countiesId ) { 

	var elementState = getElementByIdOrByName(stateId);
	if(elementState!=null && elementState!=undefined){
		var valueState = elementState.value;
		getDWRCountiesWithValue(valueState, countiesId);//Populates the counties for the state
	}  
}

function pgPopulateCounties(){

	populateCounty ( 'DEM162', 'DEM165');
	populateCounty ('DEM162_W', 'DEM165_W');
	populateCounty ('INV503', 'INV505');
	//Second investigation on merge screen
	populateCounty ( 'DEM162_2', 'DEM165_2');
	populateCounty ('DEM162_W_2', 'DEM165_W_2');
	populateCounty ('INV503_2', 'INV505_2');

}

function getDWRCountiesWithValue(state, elementId)
{
var state1 = state.value;
 
if (state1 == null) {
    state1 = state;
  }
 if (elementId == 'DEM165' || elementId == 'DEM165_W' || elementId == 'DEM165_2' || elementId == 'DEM165_W_2'  || elementId == 'INV505' || elementId == 'AR109') {
    JPageForm.getDwrCountiesForState(state1, function(data)
    {
      DWRUtil.removeAllOptions(elementId);
      setElementValueByIdName(elementId + "_textbox", "");
      DWRUtil.addOptions(elementId, data, "key", "value");
	  selectValue(elementId);
	  
    });
  } else if (elementId == 'INV156') {
    JPageForm.getDwrImportedCountiesForState(state1, function(data)
    {
      DWRUtil.removeAllOptions(elementId);
      setElementValueByIdName(elementId + "_textbox", "");
      DWRUtil.addOptions(elementId, data, "key", "value");
	  selectValue(elementId);
    });
  }
}

function getDWRCounties(state, elementId)
{
var state1 = state.value;
 
if (state1 == null) {
    state1 = state;
  }
 if (elementId == 'DEM165' || elementId == 'DEM165_W' || elementId == 'INV505' || elementId == 'AR109') {
    JPageForm.getDwrCountiesForState(state1, function(data)
    {
      DWRUtil.removeAllOptions(elementId);
      setElementValueByIdName(elementId + "_textbox", "");
      DWRUtil.addOptions(elementId, data, "key", "value");
	  var list = document.getElementById("DEM165_W");
    
    });
  } else if (elementId == 'INV156') {
    JPageForm.getDwrImportedCountiesForState(state1, function(data)
    {
      DWRUtil.removeAllOptions(elementId);
      setElementValueByIdName(elementId + "_textbox", "");
      DWRUtil.addOptions(elementId, data, "key", "value");
    });
  }
}

function getDWRStatesByCountry(state, elementId)
{
  var state1 = state.value;
  if (state1 == null) {
    state1 = state;
  }
  JPageForm.getfilteredStatesByCountry(state1, function(data)
  {
    DWRUtil.removeAllOptions(elementId);
    setElementValueByIdName(elementId + "_textbox", "");
    DWRUtil.addOptions(elementId, data, "key", "value");
  });
}

function getDWRCitites(state)
{
    var stateCode = state.value;
    JBaseForm.getDwrCityList(stateCode, function(data) {
        DWRUtil.removeAllOptions("cityList");
        DWRUtil.addOptions("cityList", data, "value", "key" );
        // dwr.util.setValue("name", data);
    });
}

function checkRacesOptions()
{
    var hawaiianRace= getElementByIdOrByName("pageClientVO.hawaiianRace").checked;
    if(hawaiianRace) {
        getElementByIdOrByName("hawaiianlist").className="";
        getElementByIdOrByName("hawaiian-multi").className="";
    }
    else {
        getElementByIdOrByName("DEM245").className="none";
        getElementByIdOrByName("hawaiian-multi").className="none";
    }

    var asianRace = getElementByIdOrByName("pageClientVO.asianRace").checked;
    if(asianRace) {
        getElementByIdOrByName("asianlist").className="";
        getElementByIdOrByName("asian-multi").className="";
    } else {
        getElementByIdOrByName("DEM243").className="none";
        getElementByIdOrByName("asian-multi").className="none";
    }
}

function pageTabCount()
{
    var disabledTabHandleClass = ".ongletTextDis";
    var errorTabHandleClass    = ".ongletTextErr";
    var enabledTabHandleClass  = ".ongletTextEna";
    var pageTabCount = 0;
    pageTabCount = $j(disabledTabHandleClass).length + $j(errorTabHandleClass).length + $j(enabledTabHandleClass).length;
    return pageTabCount;
}

function pgPopulateMMWR(weekId, yearId)
{

	var todayDT = new Date();
	var dd = todayDT.getDate();
	var mm = todayDT.getMonth()+1;//January is 0!
	var yyyy = todayDT.getFullYear();
	if(dd<10){dd='0'+dd}
	if(mm<10){mm='0'+mm}
	var currentDate = mm+'/'+dd+'/'+yyyy;
	var aMMWR = CalcMMWR(currentDate);
	var varYear = getElementByIdOrByName(yearId);
	var varWeek = getElementByIdOrByName(weekId);
	if(varYear == null || varYear.value == undefined || varYear.value == "")
	varYear.value = aMMWR[0];
	if(varWeek == null || varWeek.value == undefined || varWeek.value == "")
	varWeek.value = aMMWR[1];
	//alert("Week and Year are "+aMMWR[0]+aMMWR[1]);
}

function pgPopulateInfoAsOf(infoAsOfId)
{
	//alert("pgPopulateInfoAsOf");
	var todayDT = new Date();
	var dd = todayDT.getDate();
	var mm = todayDT.getMonth()+1;//January is 0!
	var yyyy = todayDT.getFullYear();
	if(dd<10){dd='0'+dd}
	if(mm<10){mm='0'+mm}
	var currentDate = mm+'/'+dd+'/'+yyyy;
	var varInfoAsOf = getElementByIdOrByName(infoAsOfId);
	if(varInfoAsOf == null || varInfoAsOf.value == undefined || varInfoAsOf.value == "")
	varInfoAsOf.value = currentDate;

}



function pgOnLoadCalcRptAge(dobID,rptAgeId,rptAgeU,asOfDt,othAsOf)
{
    var reportedAgeNode = getElementByIdOrByName(rptAgeId);
    var reportedAgeUnitsNode = getElementByIdOrByName(rptAgeU);

    if(reportedAgeNode!=null && reportedAgeUnitsNode!=null && reportedAgeNode.value == "" && reportedAgeUnitsNode.value == "") {
        pgCalculateReportedAge(dobID,rptAgeId,rptAgeU,asOfDt,othAsOf);
    }
}

//
// Calculate Reported Age from DOB
// Note: required either Other Personal Details As Of Date or the General As Of Date to be set.
// Parameters: Date of Birth Id, Reported Age Id, Reported Age Units Id, Other Personal Detail As Of, General As Of
//
function pgCalculateReportedAge(dobID,rptAgeId,rptAgeU,asOfDt,othAsOf)
{
    var reportedAgeNode = getElementByIdOrByName(rptAgeId);
    var reportedAgeUnitsNode = getElementByIdOrByName(rptAgeU);


    var dobNode = getElementByIdOrByName(dobID);
    // FIXME : make sure it is DEM115 
    var calcDOBNode = getElementByIdOrByName(dobID);

    var asOfDateNode = getElementByIdOrByName(asOfDt);
    if(asOfDateNode==null) {
  	asOfDateNode = getElementByIdOrByName(othAsOf);
    }

    //alert("dobNode:" + dobNode.value);
    //alert("asOfDateNode:" + asOfDateNode.value);
    //alert("reportedAgeNode:" + reportedAgeNode.value);
    //alert("reportedAgeUnitsNode:" + reportedAgeUnitsNode.value);

    if(dobNode!=null && dobNode.value!="" && isDate(dobNode.value)){
        calcDOBNode.value = dobNode.value;
    }

    //var calcDOBDate = new Date(calcDOBNode.value);

    //figure out the reported age and units
    //don't show if calc dob is empty
    if (dobNode!=null && dobNode.value!="" && asOfDateNode!=null && asOfDateNode.value!="")
    {
    	var calcDOBDate = new Date(calcDOBNode.value);
        var asOfDate = new Date(asOfDateNode.value);
        //alert("should reset reportedAgeNode");
        reportedAgeNode.value="";
        reportedAgeUnitsNode.value="";

        var reportedAgeMilliSec = asOfDate.getTime() - calcDOBDate.getTime();
        if(!window.isNaN(reportedAgeMilliSec))
        {
            var reportedAgeSeconds = reportedAgeMilliSec/1000;
            var reportedAgeMinutes = reportedAgeSeconds/60;
            var reportedAgeHours = reportedAgeMinutes/60;
            var reportedAgeDays = reportedAgeHours/24;
            var reportedAgeMonths = reportedAgeDays/30.41;
            var reportedAgeYears = reportedAgeMonths/12;

            if(isLeapYear(calcDOBDate.getFullYear())) reportedAgeMonths = Math.floor(reportedAgeDays)/30.5;

            if(Math.ceil(reportedAgeDays)<=28){
                reportedAgeNode.value=Math.floor(reportedAgeDays);
                reportedAgeUnitsNode.value="D";
            } else if(Math.ceil(reportedAgeDays)>28 && reportedAgeYears<1)  {
            	if(Math.ceil(reportedAgeDays) > 28 && Math.ceil(reportedAgeDays) < 31)
        			reportedAgeMonths = reportedAgeMonths + 1;
        				
                reportedAgeNode.value=Math.floor(reportedAgeMonths);
                reportedAgeUnitsNode.value="M";
            } else  {
                // get rough estimated year age
                var yearDiff = asOfDate.getFullYear() - calcDOBDate.getFullYear();
                //need to determine whether birthday has happened
                if(asOfDate.getMonth()<calcDOBDate.getMonth())
                    yearDiff = yearDiff-1;
                else if(asOfDate.getMonth()==calcDOBDate.getMonth()){
                    if(asOfDate.getDate()<calcDOBDate.getDate())
                        yearDiff = yearDiff-1;
                }
                reportedAgeNode.value=yearDiff;//Math.floor(reportedAgeYears);
                reportedAgeUnitsNode.value="Y";
            //this is only for leap year, if DOB is 02/29/YYYY and is leap year and is almost one year old, it should be 11 months
                if(calcDOBDate.getMonth() == 1 && calcDOBDate.getDate()==29 && reportedAgeYears > 1 && reportedAgeYears < 1.1 && isLeapYear(calcDOBDate.getFullYear()))
                {
                    currentAgeNode.innerText="11";
                    currentAgeUnitsNode.innerText="Months";
                }
            }

        } else {
            reportedAgeNode.value="";
            reportedAgeUnitsNode.value="";
        }
        autocompTxtValuesForJSP();
    }

    //alert("dobNode.value in calc:" + dobNode.value);
    //alert("calcDOBNode.value in calc:" + calcDOBNode.value);
    //alert("reportedAgeNode.value in calc:" + reportedAgeNode.value);
}

function isLeapYear(varyear)
{
   var leapyear = false;
   if((varyear% 4) == 0) leapyear = true;
   if((varyear% 100) == 0) leapyear = false;
   if((varyear% 400) == 0) leapyear = true;
   return leapyear;
}

function checkMaxLength(sTxtBox)
{
    maxlimit = 2000;
    if (sTxtBox.value.length > maxlimit)
    {
        sTxtBox.value = sTxtBox.value.substring(0, maxlimit);
    }
}
function checkTextAreaLength(sTxtBox, maxLen)
{
    if (sTxtBox.value.length > maxLen)
    {
        sTxtBox.value = sTxtBox.value.substring(0, maxLen);
        var theDateEle = getElementByIdOrByName(sTxtBox.id + "Date");
        var theUserEle = getElementByIdOrByName(sTxtBox.id + "User");
        if (theDateEle != null && theUserEle != null) {
                rollingNoteSetUserDate(sTxtBox.id);
        }
    }
}
function swallowEnter()
{
    if(event.keyCode==13){
        event.keyCode = null;
        return false;
    }
}

function isNumeric(pTextbox)
{
    var varVal = pTextbox.value;
    var y = 0; var s = ""; var c = "";
    y = varVal.length;
    var varKeys = [ 0, 8, 9, 16, 35, 36, 37, 38, 39, 40, 46];
    for(x=0; x<y; x++) {
        c = varVal.substr(x, 1);
        if(isInteger(c)) s += c;
         pTextbox.value = s;
    }
}

function displayInvHistory(oSwitchImage)
{
    var tbodyElts = invHistoryTable.getElementsByTagName("tbody");
    var tbody = tbodyElts[0];
    var trNodes = tbody.getElementsByTagName("tr");

    if(oSwitchImage.src.search(/minus_sign.gif/)==-1){
        oSwitchImage.src = "minus_sign.gif";
    }
    else {
        oSwitchImage.src = "plus_sign.gif";
    }

    for (var i=0; i < trNodes.length; i++)
    {
        if (i != 0)
        {
            if(trNodes[i].style.display == "none") {
                trNodes[i].style.display = "";
            }
            else {
                trNodes[i].style.display = "none";
            }
        }
    }
}

function genProviderAutocomplete(txt_id, div_id)
{
    new Ajax.Autocompleter(txt_id, div_id, "/nbs/getAutocompleterChoices",
            {paramName: "value",parameters : "type=investigator"});
}

function enabledDateAssignedToInvestigation()
{
	if(getElementByIdOrByName("INV110")!=null && getElementByIdOrByName("INV110")!=undefined){
	    getElementByIdOrByName("INV110").disabled=false;
	    getElementByIdOrByName("INV110L").className ="InputFieldLabel";
	    getElementByIdOrByName("INV110L").style.color="#000";
	    $j("#INV110").parent().parent().find("img").attr("disabled", false);
	}
}

function disabledDateAssignedToInvestigation()
{
    // disable the date assigned to investigation field (INV110).
    getElementByIdOrByName("INV110").value="";
    getElementByIdOrByName("INV110").disabled=true;
    $j("#INV110").parent().parent().find("img").attr("disabled", true);
	
    getElementByIdOrByName("INV110L").className = "InputDisabledLabel";
    getElementByIdOrByName("INV110L").style.color="#BBB";
}

function getProvider(identifier)
{
 	//alert('before clear: ' + identifier);
	clearProvider(identifier);
	//alert('After clear: ' + identifier);
	var urlToOpen = "/nbs/Provider.do?method=searchLoad&identifier="+identifier;
	var params="left=100, top=50, width=650, height=500, menubar=no,titlebar=no,toolbar=no,scrollbars=yes,location=no,status=yes,top=150,left=150";
	var pview = getElementByIdOrByName("pageview");
	pview.style.display = "block";
	 var o = new Object();
		    o.opener = self;

			var dialogFeatures =  "dialogWidth: " + 760 + "px;dialogHeight: " +
	            700 + "px;status: no;unadorned: yes;scroll: yes;help: no;" +
		            (true ? "resizable: yes;" : "");
					
					
		//	if(window.showModalDialog==undefined)
			//	ChildWindowHandle = window.open(urlToOpen,"",dialogFeatures);
		//	else
			//	var modWin = window.showModalDialog(urlToOpen,o,dialogFeatures);
			
			var modWin = openWindow(urlToOpen, o,dialogFeatures, pview, "");
			
	//newwindow = window.open(urlToOpen,'Provider', params);
	//newwindow.focus();
}

function clearProvider(identifier)
{
	var code = $(identifier+"Text");
	var zzForm = JPageForm;
    if (identifier.startsWith("CON"))
		zzForm = JCTContactForm;
	code.value = "";
	dwr.util.setValue(identifier, "");
	dwr.util.setValue(identifier+"Error", "");
	getElementByIdOrByName(identifier+"Text").style.visibility="visible";
	getElementByIdOrByName(identifier+"Icon").style.visibility="visible";
	getElementByIdOrByName(identifier+"CodeLookupButton").style.visibility="visible";
	getElementByIdOrByName("clear"+identifier).className="none";
	getElementByIdOrByName(identifier+"SearchControls").className="visible";

    // if the identifier is investigator
    if (identifier == "INV180") {
        disabledDateAssignedToInvestigation();
    }
    if (identifier == "NBS186" && getElementByIdOrByName('NBS187') != null) {  //Inteviewer
    	      pgRequireNotElement("NBS187"); //Date
    } 
    if (getElementByIdOrByName('NBS139') != null)
	  stdUpdateCurrentProvider();
	zzForm.clearDWRInvestigator(identifier);
}
function clearOtherContact(identifier)
{

	dwr.util.setValue(identifier, "");
	dwr.util.setValue(identifier+"Error", "");
	getElementByIdOrByName(identifier+"Icon").style.visibility="visible";
	getElementByIdOrByName("clear"+identifier).className="none";
	getElementByIdOrByName(identifier+"SearchControls").className="visible";
	JCTContactForm.clearDWRContact(identifier);
}
function getDWRProvider(identifier)
{
 dwr.util.setValue(identifier, "");
 var code = $(identifier+"Text");
 var codeValue= code.value;
  var zzForm = JPageForm;
  if (identifier.startsWith("CON"))
 	zzForm = JCTContactForm;
  zzForm.getDwrInvestigatorDetails(codeValue,identifier, function(data) {
       dwr.util.setEscapeHtml(false);
       if(data.indexOf('$$$$$') != -1) {
	         var code = $(identifier+"Text");
	         code.value = "";
	         dwr.util.setValue(identifier, "");
	         dwr.util.setValue(identifier+"Error", "");

	         dwr.util.setValue("investigator.personUid", data.substring(0,data.indexOf('$$$$$')));
	         dwr.util.setValue(identifier, data.substring(data.indexOf('$$$$$')+5));

	        getElementByIdOrByName(identifier+"Text").style.visibility="hidden";
	        getElementByIdOrByName(identifier+"Icon").style.visibility="hidden";
	        getElementByIdOrByName(identifier+"CodeLookupButton").style.visibility="hidden";
	        getElementByIdOrByName("clear"+identifier).className="";
	        getElementByIdOrByName(identifier+"SearchControls").className="none";

	        // enable the date assigned to investigator field
	        if (identifier == "INV180") {
	            enabledDateAssignedToInvestigation();
	        }
	        if (getElementByIdOrByName("attributeMap."+identifier+"Uid") != null) {
	        	getElementByIdOrByName("attributeMap."+identifier+"Uid").value = data.substring(0,data.indexOf('$$$$$'));
	        	if (identifier == "NBS139" || identifier == "NBS145" || identifier == "NBS161" || identifier == "NBS186" || identifier == "NBS197") {
				stdUpdateCurrentProvider(identifier);
			}
			if (identifier == "NBS186") { //Interviewer
				pgRequireElement("NBS187"); //Date
			}
	        }
       } else {
           dwr.util.setValue(identifier+"Error", data);
            getElementByIdOrByName(identifier+"Text").style.visibility="visible";
            getElementByIdOrByName(identifier+"Icon").style.visibility="visible";
            getElementByIdOrByName(identifier+"CodeLookupButton").style.visibility="visible";
            getElementByIdOrByName("clear"+identifier).className="none";
            getElementByIdOrByName(identifier+"SearchControls").className="visible";
       }
    });
}

function cleanupPatientRacesViewDisplay()
{
    var container = getElementByIdOrByName("patientRacesViewContainer");
    if (container != null) {
        var value = container.innerHTML;
        value = jQuery.trim(value);
        var loc = value.lastIndexOf(",");
        if (loc != -1) {
            if (loc + value.substring(loc, value.length).length == value.length) {
                container.innerHTML = value.substring(0, loc);
            }
        }
    }
}

function cleanupPatientRacesViewDisplay2()
{
    var container = getElementByIdOrByName("patientRacesViewContainer2");
    if (container != null) {
        var value = container.innerHTML;
        value = jQuery.trim(value);
        var loc = value.lastIndexOf(",");
        if (loc != -1) {
            if (loc + value.substring(loc, value.length).length == value.length) {
                container.innerHTML = value.substring(0, loc);
            }
        }
    }
}

function genAutocomplete(txt_id, div_id)
{
    new Ajax.Autocompleter(txt_id, div_id, "/nbs/getAutocompleterChoices", {paramName: "value",parameters : "type=city"});
}

function disablePrintLinks() {
	$j("a[href]:not([href^=#])").removeAttr('href');
}

 function genOrganizationAutocomplete(txt_id, div_id){
     new Ajax.Autocompleter(txt_id, div_id, "/nbs/getAutocompleterChoices", {paramName: "value",parameters : "type=organization"});
   }

function getDWROrganization(identifier)
    {
	 dwr.util.setValue(identifier, "");
	 var code = $(identifier+"Text");
     var codeValue= code.value;
     var zzForm = JPageForm;
	 if (identifier.startsWith("CON"))
		zzForm = JCTContactForm;
     zzForm.getDwrOrganizationDetails(codeValue,identifier, function(data) {
           dwr.util.setEscapeHtml(false);
           if(data.indexOf('$$$$$') != -1) {
        	   var code = $(identifier+"Text");
             code.value = "";
             dwr.util.setValue(identifier, "");
             dwr.util.setValue(identifier+"Error", "");
             if (getElementByIdOrByName("attributeMap."+identifier+"Uid") != null) {
 	        	getElementByIdOrByName("attributeMap."+identifier+"Uid").value = data.substring(0,data.indexOf('$$$$$'));
             }
             dwr.util.setValue("organization.organizationUid", data.substring(0,data.indexOf('$$$$$')));
             dwr.util.setValue(identifier, data.substring(data.indexOf('$$$$$')+5));

             getElementByIdOrByName(identifier+"Text").style.visibility="hidden";
             getElementByIdOrByName(identifier+"CodeLookupButton").style.visibility="hidden";
             getElementByIdOrByName("clear"+identifier).className="";
             getElementByIdOrByName(identifier+"SearchControls").className="none";
           } else {

        	   dwr.util.setValue(identifier+"Error", data);
               getElementByIdOrByName(identifier+"Text").style.visibility="visible";
               getElementByIdOrByName(identifier+"CodeLookupButton").style.visibility="visible";
               getElementByIdOrByName("clear"+identifier).className="none";
               getElementByIdOrByName(identifier+"SearchControls").className="visible";
           }
        });
    }

 function getReportingOrg(identifier){
	 clearOrganization(identifier);
	 var urlToOpen = "/nbs/PamOrganization.do?method=searchOrganization&identifier="+identifier;
	 var params="left=100, top=50, width=650, height=500, menubar=no,titlebar=no,toolbar=no,scrollbars=yes,location=no,status=yes,top=150,left=150";
	 var varicella = getElementByIdOrByName("pageview");
	 varicella.style.display = "block";
     var o = new Object();
	 o.opener = self;
	// var modWin = window.showModalDialog(urlToOpen,o, "dialogWidth: " + 760 + "px;dialogHeight: " +
	  //          700 + "px;status: no;unadorned: yes;scroll: yes;help: no;" +
      //      (true ? "resizable: yes;" : ""));
	 
	 var dialogFeatures = "dialogWidth: " + 760 + "px;dialogHeight: " +
	            700 + "px;status: no;unadorned: yes;scroll: yes;help: no;" +
     (true ? "resizable: yes;" : "");
	 var modWin = openWindow(urlToOpen, o,dialogFeatures, varicella, "");
	 
       // newwindow = window.open(urlToOpen,'Organization', params);
       // newwindow.focus();


 }

 function clearOrganization(identifier){
	 var code = $(identifier+"Text");
	   var zzForm = JPageForm;
	   if (identifier.startsWith("CON"))
 			zzForm = JCTContactForm;
	   
	   if (code != null)
		   code.value = "";
		
	   dwr.util.setValue(identifier, "");
		dwr.util.setValue(identifier+"Error", "");
		
		if(getElementByIdOrByName(identifier+"Text")!=null)
			getElementByIdOrByName(identifier+"Text").style.visibility="visible";
		if(getElementByIdOrByName(identifier+"Icon")!=null)
			getElementByIdOrByName(identifier+"Icon").style.visibility="visible";
		if(getElementByIdOrByName(identifier+"CodeLookupButton")!=null)
			getElementByIdOrByName(identifier+"CodeLookupButton").style.visibility="visible";
		if(getElementByIdOrByName("clear"+identifier)!=null)
			getElementByIdOrByName("clear"+identifier).className="none";
		if(getElementByIdOrByName(identifier+"SearchControls")!=null)
			getElementByIdOrByName(identifier+"SearchControls").className="visible";
		
		zzForm.clearDWROrganization(identifier);
}


function updateHospitalInformationFields(dropDownElementId, resultSpanId,admissionDtId, dischargeDtId, durationOfStay)
{

    var dropDownElement = getElementByIdOrByName(dropDownElementId);
    var codeClearButtonId = resultSpanId + "CodeClearButton";
    var searchButtonId =  resultSpanId + "Icon";
    var quickCodesearchTextBoxId = resultSpanId + "Text";
    var quickCodeLookupButtonId = resultSpanId + "CodeLookupButton";
    var searchControlsSpanId = resultSpanId + "SearchControls";
    var clearControlSpanId = "clear" + resultSpanId;
    var hospitalInformationLabel = resultSpanId + "HospitalInformationLabel";
    var hospitalSelectedLabel = resultSpanId + "HospitalSelectedLabel";
    var admissionDtIdLabel = getElementByIdOrByName(admissionDtId+"L");
    var dischargeDtIdLabel = getElementByIdOrByName(dischargeDtId+"L");
    var durationOfStayLabel = getElementByIdOrByName(durationOfStay+"L");

    if (dropDownElement != null) {
        var optionSelected = null;
        if (dropDownElement.selectedIndex >= 0) {
            optionSelected = dropDownElement.options[dropDownElement.selectedIndex].value;
        }
	    if (optionSelected == "Y") {

	        // enable all relevant elements
	        if (getElementByIdOrByName(codeClearButtonId) != null) {
	            getElementByIdOrByName(codeClearButtonId).disabled = false;
	        }
	        if (getElementByIdOrByName(searchButtonId) != null) {
	            getElementByIdOrByName(searchButtonId).disabled = false;
	        }
	        if (getElementByIdOrByName(quickCodesearchTextBoxId) != null) {
	            getElementByIdOrByName(quickCodesearchTextBoxId).disabled = false;
	        }
	        if (getElementByIdOrByName(quickCodeLookupButtonId) != null) {
	            getElementByIdOrByName(quickCodeLookupButtonId).disabled = false;
	        }
	        if (getElementByIdOrByName(hospitalInformationLabel) != null) {
	            getElementByIdOrByName(hospitalInformationLabel).className = "InputFieldLabel";
	        }
	        if (getElementByIdOrByName(hospitalSelectedLabel) != null) {
	            getElementByIdOrByName(hospitalSelectedLabel).className = "InputFieldLabel";
	        }
	        if (admissionDtIdLabel != null) {
	        	admissionDtIdLabel.style.color="#000";

			enableAllBrowsers($j("#" + admissionDtId).parent().parent().find(":input"));
			dischargeDtIdLabel.style.color="#000";

			enableAllBrowsers($j("#" + dischargeDtId).parent().parent().find(":input"));
			durationOfStayLabel.style.color="#000";

			enableAllBrowsers($j("#" + durationOfStay).parent().parent().find(":input"));
		}
	    }
	    else {

	        // disable all relevant elements
	        if (getElementByIdOrByName(codeClearButtonId) != null) {
	            getElementByIdOrByName(codeClearButtonId).disabled = true;
	        }
	        if (getElementByIdOrByName(searchButtonId) != null) {
	            getElementByIdOrByName(searchButtonId).disabled = true;
	        }
	        if (getElementByIdOrByName(quickCodesearchTextBoxId) != null) {
	            getElementByIdOrByName(quickCodesearchTextBoxId).disabled = true;
	        }
	        if (getElementByIdOrByName(quickCodeLookupButtonId) != null) {
	            getElementByIdOrByName(quickCodeLookupButtonId).disabled = true;
	        }
	        if (getElementByIdOrByName(resultSpanId) != null) {
	            getElementByIdOrByName(resultSpanId).innerHTML = "";
	        }
	        if (getElementByIdOrByName(hospitalInformationLabel) != null) {
	            getElementByIdOrByName(hospitalInformationLabel).className = "InputDisabledLabel";
	        }
	        if (getElementByIdOrByName(hospitalSelectedLabel) != null) {
	            getElementByIdOrByName(hospitalSelectedLabel).className = "InputDisabledLabel";
	        }
	        if (admissionDtIdLabel != null) {
	        	admissionDtIdLabel.style.color="#BBB";

			disableAllBrowsers($j("#" + admissionDtId).parent().parent().find(":input"));
				   	   
			$j("#" + admissionDtId).parent().parent().find(":input").val("");
		}
		if (dischargeDtIdLabel != null) {
			dischargeDtIdLabel.style.color="#BBB";

			disableAllBrowsers($j("#" + dischargeDtId).parent().parent().find(":input"));
			
			$j("#" + dischargeDtId).parent().parent().find(":input").val("");
		}
		if (durationOfStayLabel != null) {
			durationOfStayLabel.style.color="#BBB";

			disableAllBrowsers($j("#" + durationOfStay).parent().parent().find(":input"));
			$j("#" + durationOfStay).parent().parent().find(":input").val("");
		}

	        clearOrganization(resultSpanId);
	    }
    }
}


function onLoadIllnessOnsetAgeCalc()
{
    var illnessOnsetAgeNode = getElementByIdOrByName("INV143");
    var illnessOnsetAgeUnitsNode = getElementByIdOrByName("INV144");
    var dobNode = getElementByIdOrByName("DEM115");
    var illnessOnsetDate = getElementByIdOrByName("INV137");
    var defaultAgeAtIllness = "";
    if(illnessOnsetAgeNode.value == "" && illnessOnsetAgeUnitsNode.value == "")
    {
    	calculateIllnessOnsetAge();

    }
}

function pgCalculateIllnessOnsetAge(dobId,onsDtId,onsAgeId,onsAgeUID) {
    //alert("in pgCalculateIllnessOnsetAge()");
    var illnessOnsetAgeNode = getElementByIdOrByName(onsAgeId);
    var illnessOnsetAgeUnitsNode = getElementByIdOrByName(onsAgeUID);

    var dobNode = getElementByIdOrByName(dobId);
    // FIXME : make sure it is DEM115
    var calcDOBNode = getElementByIdOrByName(dobId);
    var illnessOnsetDateNode = getElementByIdOrByName(onsDtId);

    if(dobNode.value!="" && isDate(dobNode.value)){
        calcDOBNode.value = dobNode.value;
    }
    var calcDOBDate = new Date(calcDOBNode.value);
	if((dobNode.value!=null && dobNode.value!= "")&& (illnessOnsetDateNode.value!=null && illnessOnsetDateNode.value!= "") )
	{
        var illnessOnsetDate = new Date(illnessOnsetDateNode.value);

        //alert("should reset reportedAgeNode");
        illnessOnsetAgeNode.value="";
        illnessOnsetAgeUnitsNode.value="";

        var illnessOnsetAgeMilliSec = illnessOnsetDate.getTime() - calcDOBDate.getTime();
        if (illnessOnsetAgeMilliSec < 0) {
	        //alert("Illness onset before birth date?");
	        return;
        }
        if(!window.isNaN(illnessOnsetAgeMilliSec)){
            var illnessOnsetAgeSeconds = illnessOnsetAgeMilliSec/1000;
            var illnessOnsetAgeMinutes = illnessOnsetAgeSeconds/60;
            var illnessOnsetAgeHours = illnessOnsetAgeMinutes/60;
            var illnessOnsetAgeDays = illnessOnsetAgeHours/24;
            var illnessOnsetAgeMonths = illnessOnsetAgeDays/30.41;
            var illnessOnsetAgeYears = illnessOnsetAgeMonths/12;

            if(isLeapYear(calcDOBDate.getFullYear())) illnessOnsetAgeMonths = Math.floor(illnessOnsetAgeDays)/30.5;

            if(Math.ceil(illnessOnsetAgeDays)<=31){
            	illnessOnsetAgeNode.value=Math.ceil(illnessOnsetAgeDays);
            	illnessOnsetAgeUnitsNode.value="D";
            } else if(Math.ceil(illnessOnsetAgeDays)>31 && illnessOnsetAgeYears<1)  {
            	st=calcDOBDate.valueOf();en=illnessOnsetDate.valueOf();diff=en-st-(-0);diffdate=new Date();diffdate.setTime(diff);monthdiff=diffdate.getMonth();

            	illnessOnsetAgeNode.value=monthdiff;
            	illnessOnsetAgeUnitsNode.value="M";
            } else  {
                // get rough estimated year age
                var yearDiff = illnessOnsetDate.getFullYear() - calcDOBDate.getFullYear();
                //need to determine whether birthday has happened
                if(illnessOnsetDate.getMonth()<calcDOBDate.getMonth())
                    yearDiff = yearDiff-1;
                else if(illnessOnsetDate.getMonth()==calcDOBDate.getMonth()){
                    if(illnessOnsetDate.getDate()<calcDOBDate.getDate())
                        yearDiff = yearDiff-1;
                }
                illnessOnsetAgeNode.value=yearDiff;//Math.floor(reportedAgeYears);
                illnessOnsetAgeUnitsNode.value="Y";
            //this is only for leap year, if DOB is 02/29/YYYY and is leap year and is almost one year old, it should be 11 months
                if(calcDOBDate.getMonth() == 1 && calcDOBDate.getDate()==29 && reportedAgeYears > 1 && reportedAgeYears < 1.1 && isLeapYear(calcDOBDate.getFullYear()))
                {
                    currentAgeNode.innerText="11";
                    currentAgeUnitsNode.innerText="Months";
                }
            }

        } else {
        	illnessOnsetAgeNode.value="";
        	illnessOnsetAgeUnitsNode.value="";
        }
        autocompTxtValuesForJSP();
	}
}


function pgCalculateIllnessDuration(onsAgeId,onsAgeU,onsDt,endDt) {

    //alert("in pgCalculateIllnessDuration()");

    var illnessOnsetAgeNode = getElementByIdOrByName(onsAgeId);
    var illnessOnsetAgeUnitsNode = getElementByIdOrByName(onsAgeU);

    var onSetNode = getElementByIdOrByName(onsDt);
    // FIXME : make sure it is INV137
    var calcOnSetNode = getElementByIdOrByName(onsDt);
    var illnessEndDateNode = getElementByIdOrByName(endDt);

    if(onSetNode.value!="" && isDate(onSetNode.value)){
        calcOnSetNode.value = onSetNode.value;
    }
    var calcOnSetDate = new Date(calcOnSetNode.value);
	if((onSetNode.value!=null && onSetNode.value!= "")&& (illnessEndDateNode.value!=null && illnessEndDateNode.value!= "") )
	{
        var illnessEndDate = new Date(illnessEndDateNode.value);

        //alert("should reset reportedAgeNode");
        illnessOnsetAgeNode.value="";
        illnessOnsetAgeUnitsNode.value="";

        var illnessOnsetAgeMilliSec = illnessEndDate.getTime() - calcOnSetDate.getTime();
        if(!window.isNaN(illnessOnsetAgeMilliSec)){
            var illnessOnsetAgeSeconds = illnessOnsetAgeMilliSec/1000;
            var illnessOnsetAgeMinutes = illnessOnsetAgeSeconds/60;
            var illnessOnsetAgeHours = illnessOnsetAgeMinutes/60;
            var illnessOnsetAgeDays = illnessOnsetAgeHours/24;
            var illnessOnsetAgeMonths = illnessOnsetAgeDays/30.41;
            var illnessOnsetAgeYears = illnessOnsetAgeMonths/12;

            if(isLeapYear(calcOnSetDate.getFullYear())) illnessOnsetAgeMonths = Math.floor(illnessOnsetAgeDays)/30.5;


	    //alert('illnessOnsetAgeDays: ' + illnessOnsetAgeDays + ', Math.ceil(illnessOnsetAgeDays): ' + Math.ceil(illnessOnsetAgeDays));

            if(Math.ceil(illnessOnsetAgeDays)<=31){
            	illnessOnsetAgeNode.value=Math.ceil(illnessOnsetAgeDays);
            	illnessOnsetAgeUnitsNode.value="D";
            } else if(Math.ceil(illnessOnsetAgeDays)>31 && illnessOnsetAgeYears<1)  {
           	st=calcOnSetDate.valueOf();en=illnessEndDate.valueOf();diff=en-st-(-0);diffdate=new Date();diffdate.setTime(diff);monthdiff=diffdate.getMonth();

        	illnessOnsetAgeNode.value= monthdiff;
            	illnessOnsetAgeUnitsNode.value="M";
            } else  {
                // get rough estimated year age
                var yearDiff = illnessEndDate.getFullYear() - calcOnSetDate.getFullYear();
                //need to determine whether birthday has happened
                if(illnessEndDate.getMonth()<calcOnSetDate.getMonth())
                    yearDiff = yearDiff-1;
                else if(illnessEndDate.getMonth()==calcOnSetDate.getMonth()){
                    if(illnessEndDate.getDate()<calcOnSetDate.getDate())
                        yearDiff = yearDiff-1;
                }
                illnessOnsetAgeNode.value=yearDiff;//Math.floor(reportedAgeYears);
                illnessOnsetAgeUnitsNode.value="Y";
            //this is only for leap year, if OnSet date is 02/29/YYYY and is leap year and is almost one year old, it should be 11 months
                if(calcOnSetDate.getMonth() == 1 && calcOnSetDate.getDate()==29 && reportedAgeYears > 1 && reportedAgeYears < 1.1 && isLeapYear(calcOnSetDate.getFullYear()))
                {
                    currentAgeNode.innerText="11";
                    currentAgeUnitsNode.innerText="Months";
                }
            }

        } else {
        	illnessOnsetAgeNode.value="";
        	illnessOnsetAgeUnitsNode.value="";
        }
        autocompTxtValuesForJSP();
	}


}


   	 	function reloadInvs(filler){
   	         	//alert("Inside reload :" +filler.value);
   	 	         JPageForm.callChildForm(filler.value, function(data) {
   	 	         });
   	 	         setTimeout("reldPage()", 1000);
   		  }

   		  function reldPage() {
   	 	  	  document.forms[0].action ="/nbs/LoadManageCtAssociation.do?method=manageContactsSubmit";
   			  document.forms[0].submit();
   		  }

   		function transferOwnership(){
    		   document.forms[0].target="";
 			document.forms[0].action ="${PageForm.attributeMap.TransferOwnership}";
 		}
   		function transferPamOwnership()
   		{
   		    var divElt = getElementByIdOrByName("pageview");
   		    divElt.style.display = "block";
   		    var o = new Object();
   		    o.opener = self;
   		    //window.showModalDialog("/nbs/PageAction.do?method=transferOwnershipLoad", o, GetDialogFeatures(600, 350, false));
   		    
   		    
   		    var URL = "/nbs/PageAction.do?method=transferOwnershipLoad";
   		    
   		    var modWin = openWindow(URL, o,GetDialogFeatures(850, 500, false, false), divElt, "");
   		 
   		    return false;
   		}


   		function createPamNotification()
   		{


            var urlToOpen =  "/nbs/PageAction.do?method=createNotification";
            var divElt = getElementByIdOrByName("pageview");
           // alert(divElt );
            if (divElt == null) {
	                   divElt = getElementByIdOrByName("blockparent");
	        }
	              //  alert("divElt :"+divElt.value );
	          divElt.style.display = "block";
	          var o = new Object();
	          o.opener = self;
	          var dlgStyle = "scroll:no;scrollbars:no;status:no;resizable:yes;help:no;dialog Height:700px;dialogWidth:840px;";
	         // window.showModalDialog(urlToOpen,o, dlgStyle);
	          
	          var modWin = openWindow(urlToOpen, o,dlgStyle, divElt, "");
	          
	        return false;
   		}

		function pgChangeCondition()
   		{
   		    var divElt = getElementByIdOrByName("pageview");
   		    divElt.style.display = "block";
   		    var o = new Object();
   		    o.opener = self;
   		 //   window.showModalDialog("/nbs/PageAction.do?method=changeConditionLoad", o, GetDialogFeatures(500, 305, false));

   		    var URL = "/nbs/PageAction.do?method=changeConditionLoad";
   		    
   		 var modWin = openWindow(URL, o,GetDialogFeatures(500, 500, false, false), divElt, "");
   		 
   		  return false;
   		}

		
			



		// Change Condition - perform the change
   		function pgChangeToNewCondition(conditionCd, conditionDesc) {
	    		blockUIDuringFormSubmission()
	    	//alert("in pgChangeToNewCondition(" + conditionCd + ", " + conditionDesc + ", " + userHasProgAreaPermission +")");
   			//alert( "action=" + "/nbs/PageAction.do?method=changeConditionSubmit&INV169=" + conditionCd + ';&INV169Desc=' + conditionDesc);
		   	document.forms[0].action ="/nbs/PageAction.do?method=changeConditionSubmit&INV169=" + conditionCd + '&INV169Desc=' + conditionDesc;
		   	document.forms[0].submit();
		   	return false;
		}


//
// enable or disable field based on value
// fromBoolField - must be Y,N,UNK field
// toTextField - text field to enable or disable
//
function enableOrDisableTarget(fromBoolField,toTextField)
{
	 var fromBoolNode = getElementByIdOrByName(fromBoolField);
	 var toTextBoxLabel = getElementByIdOrByName(toTextField+"L");

	//alert("Selected value:" + fromBoolNode.value);
	if (fromBoolNode==null || toTextBoxLabel==null) {
		return;
	}
	if (fromBoolNode.value=="Y") {
		toTextBoxLabel.style.color="#000";

		enableAllBrowsers($j("#" + toTextField).parent().parent().find(":input"));
		$j("#" + toTextField).parent().parent().find("img").attr("disabled", false);
		return;
	} else {
		//not Yes -> target is disabled
		toTextBoxLabel.style.color="#BBB";

		disableAllBrowsers($j("#" + toTextField).parent().parent().find(":input"));
		$j("#" + toTextField).parent().parent().find(":input").val("");
		$j("#" + toTextField).parent().parent().find("img").attr("disabled", true);
	}
}

//
// enable or disable field based on if value present
//
// toTextField - text field to enable or disable
//
function enableOrDisableIfDwrValue(fromField, toTextField) {

	var toTextBoxLabel = getElementByIdOrByName(toTextField + "L");
	var fromValue = dwr.util.getValue(fromField);

	if (fromValue == null || toTextBoxLabel == null) {
		return;
	}
	if (fromValue != "") {
		toTextBoxLabel.style.color = "#000";
		enableAllBrowsers($j("#" + toTextField).parent().parent().find(":input"));
		$j("#" + toTextField).parent().parent().find("img").attr("disabled", false);
		return;
	} else {
		//not Yes -> target is disabled
		toTextBoxLabel.style.color = "#BBB";
		disableAllBrowsers($j("#" + toTextField).parent().parent().find(":input"));
		$j("#" + toTextField).parent().parent().find(":input").val("");
		$j("#" + toTextField).parent().parent().find("img").attr("disabled", true);
	}
}


//
// enable or disable Imported Fields
//
// dImp -disease imported field
// 					INV152 INV153  INV154 INV155 INV156
function enableOrDisableDiseaseAcquired(disAcq, impCntry, impSt, impCity, impCounty) {
	
	var disAcqNode = getElementByIdOrByName(disAcq);
	if (disAcqNode == null) {
		return;
	}
	var impCntryLabel = getElementByIdOrByName(impCntry + "L");
	var impStLabel = getElementByIdOrByName(impSt + "L");
	var impCityLabel = getElementByIdOrByName(impCity + "L");
	var impCountyLabel = getElementByIdOrByName(impCounty + "L"); 
	
	//disable if cleared or set to Unknown..
	if (disAcqNode.value == "" || disAcqNode.value == "UNK"  ) {
		// disable country fields
		if (impCntryLabel != null) {
			impCntryLabel.style.color="#BBB";

			disableAllBrowsers($j("#" + impCntry).parent().parent().find(":input"));
			$j("#" + impCntry).parent().parent().find(":input").val("");
			$j("#" + impCntry).parent().parent().find("img").attr("disabled", true);
		}
		if (impStLabel != null) {
			impStLabel.style.color="#BBB";

			disableAllBrowsers($j("#" + impSt).parent().parent().find(":input"));
			$j("#" + impSt).parent().parent().find(":input").val("");
			$j("#" + impSt).parent().parent().find("img").attr("disabled", true);
		}
		if (impCityLabel != null) {
		// disable city fields
			impCityLabel.style.color="#BBB";

			disableAllBrowsers($j("#" + impCity));
			$j("#" + impCity).val("");
		}
		if (impCountyLabel != null) {
			// disable county fields
			impCountyLabel.style.color="#BBB";

			disableAllBrowsers($j("#" + impCounty).parent().parent().find(":input"));
			$j("#" + impCounty).parent().parent().find(":input").val("");
			$j("#" + impCounty).parent().parent().find("img").attr("disabled", true);
		}
	} else if (disAcqNode.value == "OOC")  {

		// enable country fields
		if (impCntryLabel != null) {
			impCntryLabel.style.color="#000";

			enableAllBrowsers(	$j("#" + impCntry).parent().parent().find(":input"));
			$j("#" + impCntry).parent().parent().find("img").attr("disabled", false);
		}
		if (impStLabel != null) {
			// enable state fields
			impStLabel.style.color="#BBB";

			disableAllBrowsers($j("#" + impSt).parent().parent().find(":input"));
			$j("#" + impSt).parent().parent().find("img").attr("disabled", true);
		}
		if (impCityLabel != null) {
		// enable city fields
			impCityLabel.style.color="#000";

			enableAllBrowsers($j("#" + impCity));
		}
		if (impCountyLabel != null) {
			// enable county fields
			impCountyLabel.style.color="#BBB";
			
			disableAllBrowsers($j("#" + impCounty).parent().parent().find(":input"));
			$j("#" + impCounty).parent().parent().find("img").attr("disabled", true);
		}

	} else if (disAcqNode.value == "OOS")  {
			// disable country fields
		if (impCntryLabel != null) {
			impCntryLabel.style.color="#BBB";
			
			disableAllBrowsers($j("#" + impCntry).parent().parent().find(":input"));
			$j("#" + impCntry).parent().parent().find("img").attr("disabled", true);
		}
		if (impStLabel != null) {
			// enable state fields
			impStLabel.style.color="#000";
		
			disableAllBrowsers($j("#" + impSt).parent().parent().find(":input"));
			$j("#" + impSt).parent().parent().find("img").attr("disabled", false);
			
		}
		if (impCityLabel != null) {
			// enable city fields
			impCityLabel.style.color="#000";

			enableAllBrowsers($j("#" + impCity));
		}
		if (impCountyLabel != null) {
			// enable county fields
			impCountyLabel.style.color="#000";

			enableAllBrowsers($j("#" + impCounty).parent().parent().find(":input"));
			$j("#" + impCounty).parent().parent().find("img").attr("disabled", false);
		}
	} else if (disAcqNode.value == "OOJ")  {
			// disable country fields
		if (impCntryLabel != null) {
			impCntryLabel.style.color="#BBB";

			disableAllBrowsers($j("#" + impCntry).parent().parent().find(":input"));
			$j("#" + impCntry).parent().parent().find("img").attr("disabled", true);
		}
			// disable state fields
		if (impStLabel != null) {
			impStLabel.style.color="#BBB";

			disableAllBrowsers($j("#" + impSt).parent().parent().find(":input"));
			$j("#" + impSt).parent().parent().find("img").attr("disabled", true);
		}
		if (impCityLabel != null) {
			// enable city fields
			impCityLabel.style.color="#000";

			enableAllBrowsers($j("#" + impCity));
		}
		if (impCountyLabel != null) {
			// enable county fields
			impCountyLabel.style.color="#000";

			enableAllBrowsers($j("#" + impCounty).parent().parent().find(":input"));
			$j("#" + impCounty).parent().parent().find("img").attr("disabled", false);
		}

	}

}

//
// calculate the duration of hospital stay
// based on the discharge date - the admission date
//
function pgCalcDaysInHosp(hospAdmitDateId, hospDischargeDateId, targetDaysStayId) {

	var hospAdmitDateNode = getElementByIdOrByName(hospAdmitDateId);
	var hospDischargeDateNode = getElementByIdOrByName(hospDischargeDateId);
	var durationOfStayNode = getElementByIdOrByName(targetDaysStayId);

	if (hospAdmitDateNode.value == null || hospAdmitDateNode.value == "" || hospDischargeDateNode.value == "") {
		return;
	}

	var hospStartDate = new Date(hospAdmitDateNode.value);
	var hospEndDate = new Date(hospDischargeDateNode.value);

	var hospStayMilliSec = hospEndDate.getTime() - hospStartDate.getTime();
	var staySeconds = hospStayMilliSec / 1000;
	var stayMinutes = staySeconds / 60;
	var stayHours = stayMinutes / 60;
	var stayDays = stayHours / 24;

	//alert('Hosp Stay Days: ' + stayDays + ', Math.ceil(stayDays): ' + Math.ceil(stayDays));
	if (stayDays > 0 && stayDays < 1000) {
		durationOfStayNode.value = Math.ceil(stayDays);
	}
}

//   It is the text field to enable or disable
//
function pgDisableElement(elementId) {
	var labelNode = getElementByIdOrByName(elementId + "L");
	//could be no label
	if (labelNode != null) {
		labelNode.style.color = "#BBB";
	}
	//disable any inputs or images associated with the element

	disableAllBrowsers($j("#" + elementId).parent().parent().find(":input"));
	$j("#" + elementId).parent().parent().find(":input").val("");
	$j("#" + elementId).parent().parent().find("img").attr("disabled", true);
	$j("#" + elementId).parent().parent().find("checkbox").attr("disabled", true);
	//if multiselect is there - get rid of selected values
	var spanSelectedVal = getElementByIdOrByName(elementId + "-selectedValues");
	if (spanSelectedVal != null) {
		spanSelectedVal.innerHTML = " Selected Values: "
	}
}

function pgReadOnlyElement(elementId) {
  $j("#" + elementId).parent().parent().find(":input").attr("readonly", true);
  $j("#" + elementId).parent().parent().find("img").attr("disabled", true);
  $j("#" + elementId).parent().parent().find("checkbox").attr("readonly", true);
}

//   It is the text field to enable or disable
//
function pgEnableElement(elementId) {
	var labelNode = getElementByIdOrByName(elementId + "L");
	//could be no label
	if (labelNode != null) {
		labelNode.style.color = "#000";
	}
	//disable any inputs or images associated with the element

	enableAllBrowsers($j("#" + elementId).parent().parent().find(":input"));
	$j("#" + elementId).parent().parent().find("img").attr("disabled", false);

	enableAllBrowsers($j("#" + elementId).parent().parent().find("checkbox"));
}

//   Disable a Participation Type Element
//   i.e. Investigator, Reporting Provider, Reporting Organization
//
function pgDisableParticipationElement(elementId)
{
  //alert("pgDisable " + elementId);
  var textEl = $(elementId + "Text");
  var iconEl = getElementByIdOrByName(elementId + "Icon");
  var clbEl = getElementByIdOrByName(elementId + "CodeLookupButton");
  var clearEl = getElementByIdOrByName("clear" + elementId);
  var searchEl = getElementByIdOrByName(elementId + "SearchControls")


  dwr.util.setValue(elementId, "");
  dwr.util.setValue(elementId + "Error", "");
  if (textEl != null) {
        textEl.value = "";
  	textEl.style.visibility = "visible";
  } //contact doesn't have QC lookup
  if (iconEl != null) {
    iconEl.style.visibility = "visible";
  }
  if (clbEl != null) {
    clbEl.style.visibility = "visible";
  }
  if (clearEl != null) {
    clearEl.className = "none";
  }
  if (searchEl != null) {
    searchEl.className = "visible";
  }

  disableAllBrowsers( $j("#" + elementId + "SearchControls").parent().parent().find(":input"));
  $j("#" + elementId + "ICON").parent().parent().find("img").attr("disabled", true);
  $j("#" + elementId + "L").css("color", "#BBB");
  $j("#" + elementId + "S").css("color", "#BBB");
}
//   Enable a Participation Type Element
//   i.e. Investigator, Reporting Provider, Reporting Organization
//
function pgEnableParticipationElement(elementId) {
  //alert ("in pgEnableParticipationElement -> " + elementId);
  var inpEl = $(elementId+"Text");
  var iconEl = getElementByIdOrByName(elementId+"Icon");
  var clbEl = getElementByIdOrByName(elementId+"CodeLookupButton");
  var clearEl = getElementByIdOrByName("clear"+elementId);
  var searchEl = getElementByIdOrByName(elementId+"SearchControls");


  var selectedItem = dwr.util.getValue(elementId);
  if (selectedItem != "") { return; }  //don't clear data when loading page if data selected


  enableAllBrowsers($j("#clear" + elementId).parent().parent().find(":input"));
  $j("#clear" + elementId).parent().parent().find("img").attr("disabled", false);

  enableAllBrowsers($j("#" + elementId + "Icon").parent().parent().find(":input"));
  enableAllBrowsers($j("#" + elementId + "CodeLookupButton").parent().parent().find(":input"));
  $j("#" + elementId + "L").css("color", "#000");
  $j("#" + elementId + "S").css("color", "#000");


   dwr.util.setValue(elementId+"Error", "");  //clear any errors
   if (inpEl != null)inpEl.style.visibility="visible";
   if (iconEl != null) { iconEl.style.visibility="visible"; }
   if (clbEl != null) { clbEl.style.visibility="visible"; }
   if (clearEl != null) { clearEl.className="none"; }
   if (searchEl != null) { searchEl.className="visible"; }

    if (getElementByIdOrByName(elementId+"S") != null) {
    	getElementByIdOrByName(elementId+"S").className="InputFieldLabel"; }
    if (getElementByIdOrByName(elementId+"L") != null) {
    	if ($j("#"+elementId+"L").hasClass("requiredInputField") == true) {
  		getElementByIdOrByName(elementId+"L").className="InputFieldLabel requiredInputField";
    	} else {
  		getElementByIdOrByName(elementId+"L").className="InputFieldLabel";
  	}
    }

}

//
// enable or disable other field based on value
// fromSelFieldId - user must have selected Other in order to enable Other field for text entry
//   The toTextField is by convention is fromSelField + "Oth"
//   It is the text field to enable or disable
//
function enableOrDisableOther(fromSelFieldId)
{
  var selectElt = getElementByIdOrByName(fromSelFieldId);
  if (selectElt == null) {
    return;
  }
  var i;
  var foundOther = 0;
  if (selectElt.options != null) {
    for (i = 0; i < selectElt.options.length; i++) {
      if (selectElt.options[i].selected && selectElt.options[i].text != "") {
        //alert(selectElt.options[i].text);
        //probably shoud match with regular expression here..
        if (selectElt.options[i].value == "OTH")
          foundOther++;
      }
    }
  }

  var toTextField = fromSelFieldId + "Oth";
  var toTextBoxLabel = getElementByIdOrByName(toTextField + "L");

  if (foundOther != 0) {
    toTextBoxLabel.style.color = "#000";

	enableAllBrowsers($j("#" + toTextField).parent().parent().find(":input"));
    if (getElementByIdOrByName(toTextField + "L") != null) {
      getElementByIdOrByName(toTextField + "L").disabled = false;
    }

    return;
  } else {
    //not Other -> target is disabled
    toTextBoxLabel.style.color = "#BBB";

	disableAllBrowsers($j("#" + toTextField).parent().parent().find(":input"));
    $j("#" + toTextField).parent().parent().find(":input").val("");
  }
  /*
  //if the parent is hidden, hide the oth child. The attribute display has not been used since the element doesn't have that attribute. The <tr> has the attribute disable.
  if(getElementByIdOrByName(fromSelFieldId).offsetWidth == 0 && getElementByIdOrByName(fromSelFieldId).offsetHeight == 0)
	 pgHideElement(toTextField);
  else
	 pgUnhideElement(toTextField);
	 */
}

//
// To support the Require If rule, add a class to mark an element as required
//
function pgRequireElement(reqNodeId)
{
  var reqNode = $j("#" + reqNodeId);
  if (reqNode == null || reqNode == 'undefined' || (reqNode.length == 0) || (reqNode.length > 1)) {
    return;
  }
  //check if label is disabled
  if (reqNodeId.charAt(reqNodeId.length - 1) == 'L') {
     if (reqNode[0].style.color == "#BBB" || reqNode[0].style.color == "#bbb")
     	return;
  }
  for (var i = 0; i < reqNode.length; i++) {
    var isDisabledFld = $j(reqNode[i]).attr('disabled');
    if (isDisabledFld != null && isDisabledFld)
      	return; //don't require a disabled field
    if ($j(reqNode[i]).hasClass("requiredInputField") == false) {
      $j(reqNode[i]).addClass("requiredInputField");
      var lastChar = reqNodeId.charAt(reqNodeId.length - 1);
      if (lastChar != null && lastChar == "L") {
        $j('<span style="color:#FF0000">* </span>').insertBefore("#" + reqNodeId);
      }
      else {
        $j('<span style="color:#FF0000">* </span>').insertBefore("#" + reqNodeId + "L");
      }
    }
  }
  return;
}
//
//To support the Require If rule, remove the required class from an element
//
function pgRequireNotElement(reqNodeId) {
		var reqNode = $j("#"+reqNodeId);
		if (reqNode == null || reqNode == 'undefined' || (reqNode.length==0) || (reqNode.length>1) ) {
			//alert("Pourquoi reqNotNode(" + reqNodeId + ") is null?");
			return;
		}
		for (var i = 0; i < reqNode.length; i++) {
			if ($j(reqNode[i]).hasClass("requiredInputField") == true) {
				$j(reqNode[i]).removeClass("requiredInputField");
				var reqText = $j(reqNode[i]).parent().children('span:first').text();
				if (reqText == "* ") {
					$j(reqNode[i]).parent().children('span:first').remove();
				} else {
					labelNode = $j("#"+reqNodeId+"L");
					if (labelNode != null) {
						reqText = $j(labelNode).parent().children('span:first').text();
						if (reqText == "* ") {
							$j(labelNode).parent().children('span:first').remove();
						}
					}
				}
			}
		}
		return;
}

//
// check entered structured numeric value
//
function isStructuredNumericCharEntered(pTextbox)
{
    //alert("in checkStructuredNumericVal()");
    var varVal = pTextbox.value;
    var y = 0; var prevStr = ""; var s = ""; var c = "";
	var count=0;
    y = varVal.length;
    var pattern = /^([<>=]|[<][=]|[>][=]|[<][>])?(((((\d){0,10})|((\d){1,10})([\.]{1})((\d){1,5})?)?)|(((((\d){1,10})([\.]{1})((\d){1,5})[+])|(((\d){1,10})[+]))|((((\d){1,10})|(((\d){1,10})([\.]{1})((\d){1,5})))[-:\/](((\d){1,10})|(((\d){1,10})([\.]{1})((\d){1,5})?)?)?)?)?)?$/;

    for(x=0; x<y; x++) {
        c = varVal.substr(x, 1);
        prevStr = s;
        s += c;
    	if ((c == '\b') || (pattern.test(s))) {
         	pTextbox.value = s;
   	 } else {
    		pTextbox.value = prevStr;
    	}
    }
}

function pageCreateLoad(multiselects)
{
    var actionMode = getElementByIdOrByName("actionMode") == null ? "" : getElementByIdOrByName("actionMode").value;
    if(actionMode == 'Preview') return;


    // AutoComplete Stuff
    autocompTxtValuesForJSP();

    // update multi-select results span to display selected options
    var selectEltIdsArray = multiselects.split("|");
    for (var i = 0; i < selectEltIdsArray.length; i++)
    {
        var selectElt = getElementByIdOrByName(selectEltIdsArray[i]);
        if(selectElt != null && !(selectElt.isDisabled) && selectElt.type == 'select-multiple')
        {
            var valuesDisplaySpanId = selectEltIdsArray[i] + "-selectedValues";
            displaySelectedOptions(selectElt, valuesDisplaySpanId);
        }
    }

    // get error tabs
    var errorTabsPresent = false;


	if(actionMode!="Merge"){
		JBaseForm.getErrorTabs(function(data)
		{
			if (data.length > 0)
			{
			   errorTabsPresent = true;
			   handleErrorTabs(data);
			}

			// if there are no error tabs, get the tab is selected in view mode
			// and automatically select it
			if (errorTabsPresent == false)
			{
				JBaseForm.getTabId(function(data) {
					if(data!=null && data!="" && data == '5') {
							data = '0'
					}

					if(data!=null && data!="")
					{
						selectTab(0,pageTabCount(),data,'ongletTextEna','ongletTextDis','ongletTextErr',null,null);
					}
					else
					{
						selectTab(0,pageTabCount(),0,'ongletTextEna','ongletTextDis','ongletTextErr',null,null);
					}
				});
			}
		});
	}

}


function pageCreateLoad2(multiselects)
{
    var actionMode = getElementByIdOrByName("actionMode") == null ? "" : getElementByIdOrByName("actionMode").value;
    if(actionMode == 'Preview') return;


    // AutoComplete Stuff
    autocompTxtValuesForJSP();

    // update multi-select results span to display selected options
    var selectEltIdsArray = multiselects.split("|");
    for (var i = 0; i < selectEltIdsArray.length; i++)
    {
        var selectElt = getElementByIdOrByName(selectEltIdsArray[i]+"_2");
        if(selectElt != null && !(selectElt.isDisabled) && selectElt.type == 'select-multiple')
        {
            var valuesDisplaySpanId = selectEltIdsArray[i] +"_2"+ "-selectedValues";
            displaySelectedOptions(selectElt, valuesDisplaySpanId);
        }
    }

    // get error tabs
    var errorTabsPresent = false;

	if(actionMode!="Merge"){
    JBaseForm.getErrorTabs(function(data)
    {
        if (data.length > 0)
        {
           errorTabsPresent = true;
           handleErrorTabs(data);
        }

        // if there are no error tabs, get the tab is selected in view mode
	    // and automatically select it
	    if (errorTabsPresent == false)
	    {
	        JBaseForm.getTabId(function(data) {
	            if(data!=null && data!="" && data == '5') {
	                    data = '0'
	            }

	            if(data!=null && data!="")
	            {
	                selectTab(0,pageTabCount(),data,'ongletTextEna','ongletTextDis','ongletTextErr',null,null);
	            }
	            else
	            {
	                selectTab(0,pageTabCount(),0,'ongletTextEna','ongletTextDis','ongletTextErr',null,null);
	            }
	        });
	    }
    });
	}
}
////////////////////
//use a regular expression for temperature validation as entered
function isTemperatureCharEntered(pTextbox)
{
  var varVal = pTextbox.value;
  var y = 0;
  var prevStr = "";
  var s = "";
  var c = "";
  var count = 0;
  y = varVal.length;
  var pattern = /^\d{1,3}(\.(\d{1})?)?$/;

  for (x = 0; x < y; x++) {
    c = varVal.substr(x, 1);
    prevStr = s;
    s += c;
    if ((c == '\b') || (pattern.test(s))) {
      pTextbox.value = s;
    } else {
      pTextbox.value = prevStr;
    }
  }
}

////////////////////
//use a regular expression for temperature validation onBlur
function isTemperatureEntered(pTextbox)
{
  var varVal = pTextbox.value;
  if (varVal == "") {
    return;
  }

  var varPattern = /^\d{1,3}(\.\d{1})?$/;
  if (varPattern.test(varVal)) {
    return;
  } else {
    alert("Please enter a valid temperature.");
  }
}

function pgCheckOnloadOtherEntryAllowedFields()
{
  var otherElts = $j(".otherEntryField");
  //alert(otherElts);
  //alert("# of elts with otherEntryField set = " + otherElts.length);
  for (var i = 0; i < otherElts.length; i++) {
    var fieldId = $j(otherElts[i]).attr("id")
      //alert("id = "+fieldId.length-4);
      //alert ("calling enableOrDisableOther("+fieldId+")");
      enableOrDisableOther(fieldId.substr(0, fieldId.length - 4));
  }
}

// Check the min and max values of a numeric field
// Alert the user if the value is not in the range
function pgCheckFieldMinMax(pTextbox, pMinVal, pMaxVal)
{
  if (pTextbox.value == "") {
    return;
  }
  //if decimal point - getting stored as zero
  if (pMinVal == 0 && pMaxVal == 0) {
    return;
  }
  var varVal = pTextbox.value;
  if (varVal < pMinVal) {
    alert("Please enter a value greater than or equal to " + pMinVal);
    pTextbox.focus();
    return;
  }
  if (varVal > pMaxVal) {
    alert("Please enter a value less than or equal to " + pMaxVal);
    pTextbox.focus();
    return;
  }
}

// Check that the year entered is 1875 to the current year
// Alert the user if the value is not in the range
function pgCheckFullYear(pTextbox)
{
  if (pTextbox.value == "") {
    return;
  }

  var varVal = pTextbox.value;
  if (varVal < 1875) {
    alert("Please enter a year greater than 1874.");
    pTextbox.focus();
    return;
  }

  var now = new Date();
  var currentYear = now.getFullYear();
  if (varVal > currentYear) {
    alert("Please enter a year less than or equal to " + currentYear);
    pTextbox.focus();
    return;
  }
}

//after the on change on a selection box, set the next focus
//to the next text, multi-select, singel-select, etc. input that is not hidden,button,image
function pgSelectNextFocus(currentElt)
{
  var fields = $j(currentElt).parents('form:eq(0),body').find(':input').not('[type=hidden]');
  var index = fields.index(currentElt);

  if (index > -1 && (index + 1) < fields.length) {
    for (index = index + 1; index < fields.length; ++index) {
      if (fields.eq(index).attr("disabled") || ((fields.eq(index).attr("type") == "button") || (fields.eq(index).attr("type") == "image"))) {
        continue;
      }
      break;
    }
    if (index < fields.length) {
      fields.eq(index).focus();
    }
  }
}

function pgSubSectionEnabled(elementId)
{
  var subSectionId = "#" + elementId;
  var subSectionBody = $j(subSectionId).find("tbody");
  if (subSectionBody == null)
  	return;
  for (var ii = 0; ii < subSectionBody.length; ii++) {
    var subSectionTBody = $j(subSectionBody.get(ii));

	enableAllBrowsers( $j(subSectionTBody).find("INPUT"));

	enableAllBrowsers( $j(subSectionTBody).find("checkbox"));
    $j(subSectionBody.get(ii)).find("img").attr("disabled", false);

	enableAllBrowsers(  $j(subSectionBody.get(ii)).find(id + "_textbox"));

	enableAllBrowsers( $j(subSectionBody.get(ii)).find("td"));

    var subSectionsInput = subSectionTBody.find("INPUT");
    for (var i = 0; i < subSectionsInput.length; i++) {
      if ($j(subSectionsInput.get(i)).attr("id") != null &&
        $j(subSectionsInput.get(i)).attr("id").length > 0) {
        var id = "#" + $j(subSectionsInput.get(i)).attr("id");
        $j(id + "L").removeAttr("disabled", true).css("color", "#000");
        $j(id).removeAttr("disabled", true).css("color", "#000");
      }
    }

    var subSectionsSelect = $j(subSectionBody.get(ii)).find("SELECT");
    for (var i = 0; i < subSectionsSelect.length; i++) {
      if ($j(subSectionsSelect.get(i)).attr("id") != null &&
        $j(subSectionsSelect.get(i)).attr("id").length > 0) {
        var id = "#" + $j(subSectionsSelect.get(i)).attr("id");
        $j(id + "L").removeAttr("disabled", true).css("color", "#000");
        $j(id).removeAttr("disabled", true).css("color", "#000");
        $j("#" + id).parent().parent().find("img").attr("disabled", false);
      }
    }
  }
  if ($j(subSectionId).hasClass("batchSubSectionDisabled")) {
  	$j(subSectionId).removeClass("batchSubSectionDisabled");
  }
}

function pgSubSectionDisabled(elementId)
{
  var subSectionId = "#" + elementId;
  var subSectionBody = $j(subSectionId).find("tbody");
   if (subSectionBody == null)
  	return;
  for (var ii = 0; ii < subSectionBody.length; ii++) {
    var subSectionTBody = $j(subSectionBody.get(ii));

	disableAllBrowsers( $j(subSectionTBody).find("INPUT"));
    //$j(subSectionTBody).find("INPUT").val("");
    $j(subSectionTBody).find("checkbox").attr("disabled", true);
    $j(subSectionBody.get(ii)).find("img").attr("disabled", true);
	disableAllBrowsers($j(subSectionBody.get(ii)).find("td"));
	


    var subSectionsInput = subSectionTBody.find("INPUT");
    for (var i = 0; i < subSectionsInput.length; i++) {
      if ($j(subSectionsInput.get(i)).attr("id") != null &&
        $j(subSectionsInput.get(i)).attr("id").length > 0) {
        var origId = $j(subSectionsInput.get(i)).attr("id");
        var id = "#" + origId;

		disableAllBrowsers($j(id + "L"));
		
		
        //$j(id).attr("disabled", true);
        var eleType = $j(id).attr("type");
        if (eleType != 'button' && eleType != 'image') {
          $j(id).val("");
          if ($j(id).hasClass("requiredInputField") == true) {
            pgRequireNotElement(origId);
          } //if required          
        } //button
        if ($j(id + "L").hasClass("requiredInputField") == true) {
          pgRequireNotElement(origId + "L");
        } //if required
      }
    }

   
    var subSectionsSelect = $j(subSectionBody.get(ii)).find("SELECT");
       for (var i = 0; i < subSectionsSelect.length; i++) {
         if ($j(subSectionsSelect.get(i)).attr("id") != null &&
           $j(subSectionsSelect.get(i)).attr("id").length > 0) {
           var origId = $j(subSectionsSelect.get(i)).attr("id");
           var id = "#" + origId;

		   disableAllBrowsers($j(id + "L"));
		   disableAllBrowsers($j(id));
			   
           var str = $j(subSectionsSelect.get(i)).attr("id") + "_textbox";
           if (getElementByIdOrByName(str) != null) {
             getElementByIdOrByName($j(subSectionsSelect.get(i)).attr("id")).value = "";
             getElementByIdOrByName(str).value = "";
   
           }
           if ($j(id + "L").hasClass("requiredInputField") == true) {
             pgRequireNotElement(origId + "L");
           } //if required

		  $j("#" + id).parent().parent().find("img").attr("disabled",true);
		 
         }
       }
  }
  //if batch subsection - clear the data in the repeating section
  if ($j(subSectionId).hasClass("batchSubSection")) {
  	if(window.event!=null && window.event.type != 'load') {
		clearRepeatingblk(elementId);
        }		
	$j(subSectionId).addClass("batchSubSectionDisabled");
  }  

}

function sharePamCaseLoad()
{
  var divElt = getElementByIdOrByName("pageview");
  divElt.style.display = "block";
  var o = new Object();
  o.opener = self;
  //window.showModalDialog("/nbs/PageAction.do?method=sharePageCaseLoad", o, GetDialogFeatures(700, 400, false));
  
  var URL = "/nbs/PageAction.do?method=sharePageCaseLoad";
  
  var modWin = openWindow(URL, o,GetDialogFeatures(700, 400, false, false), divElt, "");
  
  return false;
}

function sharePageCaseSubmit(exportFacility, comment, docType)
{
	if (docType == 'undefined' || docType == null)
  		document.forms[0].action = "/nbs/PageAction.do?method=sharePageCaseSubmit&exportFacility=" + exportFacility + '&comment=' + comment;
  	else
  		document.forms[0].action = "/nbs/PageAction.do?method=sharePageCaseSubmit&exportFacility=" + exportFacility + '&comment=' + comment +"&documentType=" +docType;
  document.forms[0].submit();
}

function getDWRPlace(identifier)
{
  dwr.util.setValue(identifier, "");
  var code = $(identifier + "Text");
  JPageForm.getDwrPlaceDetails(code.value, identifier, function(retData) {
    handlePlaceData(retData, identifier);
  });
}

function handlePlaceData(retData, identifier)
{
  dwr.util.setEscapeHtml(false);
  if (retData.indexOf('$$$$$') != -1) {
    var code = $(identifier + "Text");
    code.value = "";
    dwr.util.setValue(identifier, "");
    dwr.util.setValue(identifier + "Error", "");

    var uidElement = getElementByIdOrByName(identifier);var ar = retData.split("$$$$$");
    var uidValue = ar[0];
    uidElement ? uidElement.value = uidValue : "";
    var d = ar[1];
    d += "<br/>" + ar[2];
    dwr.util.setValue(identifier + "Disp", d);
    $j("#clear" + identifier).show();
    $j("#" + identifier + "SearchControls").hide();
  } else {
    dwr.util.setValue(identifier + "Error", retData);
    clearPlace(identifier);
  }
}

function clearPlace(identifier)
{
  $j("#" + identifier + "Text").show();
  $j("#" + identifier + "CodeLookupButton").show();
  $j("#clear" + identifier ).hide();
  $j("#" + identifier + "SearchControls").show();
  $j("#" + identifier + "Disp").html("");
}

function getHangoutPlace(identifier)
{

  var urlToOpen = "/nbs/Place.do?method=find&identifier="+identifier;
  var params="left=100, top=50, width=650, height=500, menubar=no,titlebar=no,toolbar=no,scrollbars=yes,location=no,status=yes,top=150,left=150";
  var pview = getElementByIdOrByName("pageview");
  pview.style.display = "block";
  var o = new Object();
  o.opener = self;
 // var modWin = window.showModalDialog(urlToOpen,o, "dialogWidth: " + 760 + "px;dialogHeight: " +
   //           700 + "px;status: no;unadorned: yes;scroll: yes;help: no;" +
 //           (true ? "resizable: yes;" : ""));
  
  var dialogFeatures = "dialogWidth: " + 760 + "px;dialogHeight: " +
              700 + "px;status: no;unadorned: yes;scroll: yes;help: no;" +
  (true ? "resizable: yes;" : "");
  
  var modWin = openWindow(urlToOpen, o,dialogFeatures, pview, "");
  
  /*var nm= getElementByIdOrByName(identifier).value;        
   if (nm != null && nm != "") {
     var returnedValue = "";
     JBaseForm.getDwrPlaceDetailsByName(nm, function(retData){
   	  returnedValue = retData;
   	   handlePlaceData(returnedValue, identifier);
     });
   }*/
}

function pgEnableElements(destArr)
{
  for(var i = 0; i < destArr.length; i++){
    pgEnableElement( destArr[i] );
  }
}

function pgDisableElements(destArr)
{
  for(var i = 0; i < destArr.length; i++){
    pgDisableElement( destArr[i] );
  }
}

function pgRequireElements(destArr)
{
  for(var i = 0; i < destArr.length; i++){
    pgRequireElement( destArr[i] );
  }
}

function pgSubSectionDisabledGrey(elementId)
{
  var subSectionId = "#" + elementId;
  var subSectionBody = $j(subSectionId).find("tbody");
  for (var ii = 0; ii < subSectionBody.length; ii++) {
    var subSectionTBody = $j(subSectionBody.get(ii));
    var subSectionsInput = subSectionTBody.find("INPUT");
    for (var i = 0; i < subSectionsInput.length; i++) {
      if ($j(subSectionsInput.get(i)).attr("id") != null &&
        $j(subSectionsInput.get(i)).attr("id").length > 0) {
        var origId = $j(subSectionsInput.get(i)).attr("id");
        pgDisableElement(origId);
        pgRequireNotElement(origId);
      }
    }

    var subSectionsSelect = $j(subSectionBody.get(ii)).find("SELECT");
    for (var i = 0; i < subSectionsSelect.length; i++) {
      if ($j(subSectionsSelect.get(i)).attr("id") != null &&
        $j(subSectionsSelect.get(i)).attr("id").length > 0) {
        var origId = $j(subSectionsSelect.get(i)).attr("id");
        pgDisableElement(origId);
        pgRequireNotElement( origId );
      }
    }
  }
  //if batch subsection - clear the data in the repeating section
  if ($j(subSectionId).hasClass("batchSubSection")) {
    if(window.event!=null && window.event.type != 'load') {
    	clearRepeatingblk(elementId);
    }
  }
}

//Hide the element and clear any data
function pgHideElement(elementId) {
	//hide any inputs or images associated with the element
	$j("#"+elementId+"L").parent().parent().hide(); //find the TR and hide it
	//$j("#"+elementId).parent().parent().hide(); //added for the business rule view
	
	pgClearElement(elementId);
}

//Show the element
function pgUnhideElement(elementId) {
	//alert("unhide " + elementId);
	//show any inputs or images associated with the element
	//$j("#"+elementId).parent().parent().show(); //added for the business rule view
	$j("#"+elementId+"L").parent().parent().show(); //find the TR and unhide it
}

//Hide the participation
function pgHideParticipationElement(elementId) {
	//clear the value if present
	//alert("hidePar "  + elementId);
	if ($j("#clear" + elementId).hasClass("none") == false) {
		//alert("click");
		$j("#" + elementId + "CodeClearButton").click();
	}
	//hide the Participation
	$j("#" + elementId + "L").parent().parent('tr').hide();
	$j("#" + elementId + "S").parent().hide();
}

//Show the participation
function pgUnhideParticipationElement(elementId) {
	//Show the Participation
	$j("#" + elementId + "L").parent().parent('tr').show();
	$j("#" + elementId + "S").parent().show();
	// pgEnableParticipationElement(elementId);
}

//Hide the Subsection
function pgSubSectionHidden(elementId) {
	//clear values
	pgSubSectionDisabled(elementId);
	//hide subsection
	if(getElementByIdOrByName(elementId)!=null)//Fatima: added because it's an issue
		getElementByIdOrByName(elementId).style.display = "none";
}
//Show the Subsection
function pgSubSectionShown(elementId) {
	//if currently hidden, unhide
	
	if (getElementByIdOrByName(elementId)!=null){//Fatima: added because it is an issue
	if (getElementByIdOrByName(elementId).style.display == "none") {
		getElementByIdOrByName(elementId).style.display = "";
		pgSubSectionEnabled(elementId);
	}
	}
}

//   Clear any value associated with the input
function pgClearElement(elementId) {
	//clear any inputs associated with the element
	$j("#" + elementId).parent().parent().find(":input").val("");
	//if multiselect is there - get rid of selected values
	var spanSelectedVal = getElementByIdOrByName(elementId + "-selectedValues");
	if (spanSelectedVal != null) {
		spanSelectedVal.innerHTML = " Selected Values: "
	}
}

//	Specific function for other race radio
function getCheckedOtherRace(radioObj) {
	if(!radioObj)
		return "";
	var radioLength = radioObj.length;
	if(radioLength == undefined) {
		if (getElementByIdOrByName("DEM154") != null) {
			if(radioObj.checked) 
				pgUnhideElement("DEM154");
			else
				pgHideElement("DEM154");
		}
	}
	return;
}

//	Specific function for other race radio
function getOnLoadOtherRace() {
		if (getElementByIdOrByName("DEM154") == null) 
			return;
			
		var otherRaceChkd = $j("input[type='checkbox'][name='pageClientVO.otherRace']").attr('checked');
		if (otherRaceChkd != null && otherRaceChkd) {
			pgUnhideElement("DEM154");
			return;
		}
		if($j("#DEM154").val())
			pgUnhideElement("DEM154");
		else
			pgHideElement("DEM154");
	return;
}