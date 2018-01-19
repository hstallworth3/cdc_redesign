function validatePartnerServicesFileRequest()
{
    $j(".infoBox").hide();
    
    var errors = new Array();
    var index = 0;
	var isError = false;

	var theReportMonth = $j('#RptMon :selected').val();
	if(theReportMonth == null || theReportMonth == "") {
        	errors[index++] = "Reporting Month is required. Please select March or September.";
        	getElementByIdOrByName("RptMonL").style.color="990000";
		isError = true;
	} else{
		getElementByIdOrByName("RptMonL").style.color="black";
	}
	var theReportYear = $j('#RptYr :selected').val();
	if(theReportYear == null || theReportYear == "") {
        	errors[index++] = "Reporting Year is required. Please select the Reporting Year.";
        	getElementByIdOrByName("RptYrL").style.color="990000";
		isError = true;
	} else{
		getElementByIdOrByName("RptYrL").style.color="black";
	}
	var theContactPerson = $j('#ConPer').val();
	if (theContactPerson != null)
		theContactPerson =  jQuery.trim(theContactPerson); 
	if(theContactPerson == null || theContactPerson == "") {
        	errors[index++] = "Contact Person is a required field. Please enter Contact Person.";
        	getElementByIdOrByName("ConPerL").style.color="990000";
		isError = true;
	} else{
		getElementByIdOrByName("ConPerL").style.color="black";
	}

    	if(isError) {
        	displayErrors("hivPartnerServicesFormEntryErrors", errors);
    	}
    return isError;
}
