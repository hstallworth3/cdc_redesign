/**
 *  avr.js.
 *  Common JavaScript code for avr.*.xsp.
 *  Calls functions that are in sniffer.js and nedss.js.
 *  @author Ed Jenkins
 */

/**
 *  Runs a report.
 */
/*
function RunReport()
{
    //  IE can do a double-submit.
    if(is_ie)
    {
        //  Run the report in a new window.
        document.frm.id_mode.value = "edit";
        document.frm.id_ObjectType.value = REPORT;
        document.frm.id_OperationType.value = RUN_REPORT;
        post(null, "_blank");
        //  Go to the run page in the current window.
        document.frm.id_OperationType.value = RUN_PAGE;
        post();
        return;
    }
    //  But NN can't.
    //  So, for NN, we'll create a new form,
    //  copy all data to the new form and submit it first.
    CreateAgent("rpt");
    ImportFormElements("frm", "rpt");
//  DebugNewForm();
//  return;
    var f = null;
    //  Run the report in a new window, using the new form.
    f = document.forms[1];
    f.rpt_id_mode.value = "edit";
    f.rpt_id_ObjectType.value = REPORT;
    f.rpt_id_OperationType.value = RUN_REPORT;
    f.action = "/nedss/nfc";
    f.method = "POST";
    f.target = "_blank";
    f.submit();
    //  Submit the original form in the current window.
    f = document.forms[0];
    f.id_mode.value = "edit";
    f.id_ObjectType.value = REPORT;
    f.id_OperationType.value = RUN_PAGE;
    f.action = "/nedss/nfc";
    f.method = "POST";
    f.target = "_self";
    f.submit();
}

function DebugNewForm()
{
    var varSize = null;
    var varMulti = null;
    var varCount = 0;
    var varDisease = getElementByIdOrByName("rpt_id_C_D01");
    varSize = varDisease.getAttribute("size");
    varMulti = varDisease.getAttribute("multiple");
    varDisease.options[1].selected = true;
    varDisease.options[2].selected = true;
    varDisease.options[3].selected = true;
    var x = 0;
    var y = varDisease.options.length;
    for(x=0; x<y; x++)
    {
        if(varDisease.options[x].selected == true)
        {
            window.alert(varDisease.options[x].text);
        }
    }
    varCount = GetSelectedCount(varDisease);
    window.alert("rpt_id_C_D01.size=" + varSize);
    window.alert("rpt_id_C_D01.Multi=" + varMulti);
    window.alert("rpt_id_C_D01.selected.count=" + varCount);
    var d = getElementByIdOrByName("id_C_D01");
    var c = GetSelectedCount(d);
    window.alert("id_C_D01.selected.count=" + c);
}
*/
