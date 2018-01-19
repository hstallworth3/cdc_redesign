<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/struts-layout.tld" prefix="layout"%>
<%@ taglib uri="/WEB-INF/displaytag.tld" prefix="display"%>
<%@ taglib uri="/WEB-INF/nedss.tld" prefix="nedss"%>
<%@ page isELIgnored ="false" %>
<%@ page import = "gov.cdc.nedss.webapp.nbs.diseaseform.util.InvestigationFieldConstants"%>
<%@ page import="java.util.*" %>
<%@ page import="gov.cdc.nedss.util.NEDSSConstants" %>

<!-- Basic information subsection for questions -->
<logic:equal name="manageQuestionsForm" property="actionMode" value="Create">   
    <!-- State Defined or PHIN Standard -->
    <tr>
        <td class="fieldName">
            <span class="boldRed">*</span> 
            <span id="questionTypeL" title="Question Type"> Question Type:</span>
        </td>
        <td>
            <html:select property="selection.questionType" styleId="questionType" onchange="displayUniqueId(this);" >
                <html:optionsCollection property="codedValue(NBS_QUESTION_TYPE)" value="key" label="value"/>
            </html:select>
        </td>
    </tr>  
</logic:equal>

<tr>
    <td class="fieldName"> 
        <span class="boldRed">*</span>
        <span title="Unique ID" id="questionIdentifierL">Unique ID:</span>
    </td>
    <td id ="questionIdcr">
        <html:text property="selection.questionIdentifier" size="25" maxlength="50" styleId="questionIdentifier"  onkeyup="isSpecialCharEnteredForCode(this)"/>      
    </td>
    <td id ="questionIdvw"> 
        <span id="qId"> </span> <nedss:view  name="manageQuestionsForm" property="selection.questionIdentifier"/>                          
    </td>
</tr>

<tr>
    <td class="fieldName"> 
        <span class="boldRed">*</span>
        <span title="Unique Name" id="questionNmL">Unique Name:</span>
    </td>
    <td>
        <html:text property="selection.questionNm" size="50" maxlength="50" style="width:500px;" styleId="questionNm" />      
    </td>
</tr>

 <%-- <tr>
    <td class="fieldName" id="groupL">
        <span class="boldRed">*</span> 
        <span title="group">Group:</span>
    </td>
    <td>
        <html:select property="selection.groupNm" styleId = "group" onchange="updateRDBTableNames()">
            <html:optionsCollection property="codedValue(NBS_QUES_GROUP)" value="key" label="value"/>
        </html:select>
    </td>
</tr> --%>
 
<tr>
    <td class="fieldName" id="subGroupL">
        <span class="boldRed">*</span> 
        <span title="Subgroup">Subgroup:</span>
    </td>
    <td>
        <html:select property="selection.subGroupNm" styleId = "subGroup" onchange="updateRDBTableNames()">
            <html:optionsCollection property="codedValue(NBS_QUES_SUBGROUP)" value="key" label="value"/>
        </html:select>
    </td>
</tr>
      
<tr>
    <td class="fieldName">
        <span class="boldRed">*</span> 
        <span title="Description" id="descL">Description:</span>
    </td>
    <td>
        <html:textarea style="width:500px; height:100px;" property="selection.description" styleId="desc" />      
    </td>
</tr>

<tr>      
    <td class="fieldName" id="dataTypeL">
        <span class="boldRed">*</span> 
        <span title="Data Type">Data Type:</span>
    </td>
    <logic:equal name="manageQuestionsForm" property="actionMode" value="Create">   
        <td>
            <html:select property="selection.dataType" styleId = "dataType"  onchange="showDataTypeSpecificFields(true);initializeDataTypeSpecificFields();updateDisplayControlsForDataType(this);">
                <html:optionsCollection property="codedValue(NBS_DATA_TYPE)" value="key" label="value"/>
            </html:select>
            <html:hidden  name="manageQuestionsForm" property="selection.dataType"  styleId="dataTypehid"/>
        </td>
    </logic:equal>
</tr>
 
<!-- Fields for 'coded' question type -->
<tr class="questionTypeRelatedField codedQuestionType">
    <td class="fieldName"> 
        <span class="boldRed">*</span>
        <span title="Value Set" id="valSetL">Value Set:</span>
    </td>
    <td>
        <html:select property="selection.codeSetGroupId" styleId="valSet" 
                onchange="javascript:updateDefaultValuesForValueSet(this.options[this.selectedIndex].value, 'defaultValueCoded');">
            <html:optionsCollection property="valueSets" value="key" label="value"/>
        </html:select>
    </td>
</tr>
<tr class="questionTypeRelatedField codedQuestionType">
    <td class="fieldName">
        <html:hidden  name="manageQuestionsForm" property="defaultValue"  styleId="defaultValueHidden"/>
        <span title="Default Value" id="defaultValueCodedL">Default Value:</span>
    </td>
    <td>
        <html:select property="selection.defaultValue" styleId="defaultValueCoded">
            <html:optionsCollection property="defaultValueUnitColl" value="key" label="value"/>
        </html:select>
    </td>
</tr>
<tr class="questionTypeRelatedField defaultType">
    <td class="fieldName">
        <span class="boldRed">*</span>
        <span title="Allow for Entry of Other Value" id="AllowEntryL">Allow for Entry of Other Value:</span>
    </td>
    <td>
         <html:radio styleId="yesAllowEntry" name="manageQuestionsForm" property="selection.otherValIndCd"  value="<%= NEDSSConstants.TRUE %>"/> Yes
        &nbsp;<html:radio styleId="noAllowEntry" name="manageQuestionsForm" property="selection.otherValIndCd"  value="<%= NEDSSConstants.FALSE %>"/> No
   
    </td>
</tr>

<!-- Fields for 'date' question type -->
<tr class="questionTypeRelatedField dateQuestionType">
    <td class="fieldName"> 
        <span class="boldRed">*</span>
        <span title="Mask" id="maskDL"> Mask:</span>
    </td>
    <td>
        <html:select property="selection.mask" styleId="maskD">
            <html:optionsCollection property="maskforDate(NBS_MASK_TYPE)" value="key" label="value"/>
        </html:select>
    </td>
</tr>
<tr class="questionTypeRelatedField dateQuestionType">
    <td class="fieldName"> 
        <span class="boldRed">*</span>
        <span title="Mask" id="allowFutureDatesL"> Allow for Future Dates:</span>
    </td>
    <td>
        <html:radio styleId="yesFutureEntry" name="manageQuestionsForm" property="selection.futureDateIndCd"  value="<%= NEDSSConstants.TRUE %>"/> Yes
        &nbsp;<html:radio styleId="noFutureEntry" name="manageQuestionsForm" property="selection.futureDateIndCd"  value="<%= NEDSSConstants.FALSE %>"/> No
    </td>                          
</tr>

<!-- Fields for 'date/time' question type -->
<tr class="questionTypeRelatedField datetimeQuestionType">
    <td class="fieldName">
        <span class="boldRed">*</span> 
        <span title="Mask" id="maskDTL">Mask:</span>
    </td>
    <td>
        <html:select property="selection.mask" styleId="maskDT">
            <html:optionsCollection property="maskforDateTime(NBS_MASK_TYPE)" value="key" label="value"/>
        </html:select>
    </td>
</tr>   
            
<!-- Fields for 'numeric' question type -->            
<tr class="questionTypeRelatedField numericQuestionType">
    <td class="fieldName"> 
        <span class="boldRed">*</span>
        <span title="Mask" id="maskNL">Mask:</span>
    </td>
    <td>
        <html:select property="selection.mask" styleId="maskN" onchange="handleNumericMaskChange(this)">
            <html:optionsCollection property="maskforNumeric(NBS_MASK_TYPE)" value="key" label="value"/>
        </html:select>
    </td>
</tr>       
<tr class="questionTypeRelatedField numericQuestionType">
    <td class="fieldName"> 
        <span class="boldRed">*</span>
        <span title="Field Length" id="fieldLenNumL">Field Length:</span>
    </td>
    <td id ="questionIdcr">
        <html:text property="selection.fieldLength" size="10" maxlength="10" styleId="fieldLenNum"  onkeyup="isNumericCharacterEntered(this);"/>      
    </td>
</tr>  
<tr class="questionTypeRelatedField numericQuestionType">
    <td class="fieldName"> 
        <span title="Default Value" id="defaultValueL">Default Value:</span>
    </td>
    <td>
        <html:text property="selection.defaultValue" size="25" maxlength="50" styleId="defaultValueNumeric" />      
    </td>
</tr>
<tr class="questionTypeRelatedField numericQuestionType">
    <td class="fieldName"> 
        <span title="Minimum Value" id="minValueL">Minimum Value:</span>
    </td>
    <td>
        <html:text property="selection.minValue" size="10" maxlength="10" styleId="minValue" />      
    </td>
</tr>
<tr class="questionTypeRelatedField numericQuestionType">
    <td class="fieldName"> 
        <span title="Maximum Value" id="maxValueL">Maximum Value:</span>
    </td>
    <td>
        <html:text property="selection.maxValue" size="10" maxlength="10" styleId="maxValue" />      
    </td>
</tr>
<tr class="questionTypeRelatedField numericQuestionType">
    <td class="fieldName"> 
        <span class="boldRed">*</span>
        <span title="Related Units">Related Units:</span>
    </td>
    <td>
        <html:radio styleId="relUnitY" onclick="displayRelatedUnit(true)"  name="manageQuestionsForm"  property="selection.relatedUnitInd"  value="<%= NEDSSConstants.TRUE %>"/> Yes
        &nbsp;<html:radio styleId="relUnitN" onclick="displayRelatedUnit(false)"  name="manageQuestionsForm" property="selection.relatedUnitInd"  value="<%= NEDSSConstants.FALSE %>"/> No
    </td>                          
</tr>
<tr class="questionTypeRelatedField relatedUnitsQuestionType">
    <td class="fieldName"> 
        <span class="boldRed">*</span>
        <span title="Units Type" id="unitsTypeL">Units Type:</span>
    </td>
    <td>
        <html:select property="selection.unitTypeCd" styleId="unitsTypeN" onchange="showUnitTypeSpecificFields(this);">
            <html:optionsCollection property="unitsType" value="key" label="value"/>
        </html:select>
    </td>
</tr>
<tr class="questionTypeRelatedField codedUnitType">
    <td class="fieldName"> 
        <span class="boldRed">*</span>
        <span title="Related Units Value Set" id="relatedUnitsVL">Related Units Value Set:</span>
    </td>
    <td>
        <html:select property="selection.unitValue" styleId="relatedUnitsV">
             <html:optionsCollection property="valueSets" value="key" label="value"/>
        </html:select>
    </td>
</tr>
<tr class="questionTypeRelatedField literalUnitType">
    <td class="fieldName"> 
        <span class="boldRed">*</span>
        <span title="Literal Units Value" id="literalUnitsL">Literal Units Value:</span>
    </td>
    <td>
       <html:text property="selection.unitValue" size="25" maxlength="50" styleId="literalUnits" />  
    </td>
</tr>

<!-- Fields for 'text' question type -->   
<tr class="questionTypeRelatedField textQuestionType">
    <td class="fieldName"> 
        <span class="boldRed">*</span>
        <span title="Mask" id="maskTL">Mask:</span>
    </td>
    <td>
        <html:select property="selection.mask" styleId="maskT" onchange="handleTextMaskChange(this)">
            <html:optionsCollection property="maskforText(NBS_MASK_TYPE)" value="key" label="value"/>
        </html:select>
    </td>
</tr>
<tr class="questionTypeRelatedField textQuestionType">
    <td class="fieldName"> 
        <span class="boldRed">*</span>
        <span title="Field Length" id="fieldLenTxtL">Field Length:</span>
    </td>
    <td id ="questionIdcr">
        <html:text property="selection.fieldLength" size="10" maxlength="10" styleId="fieldLenTxt" />      
    </td>
</tr>
<tr class="questionTypeRelatedField textQuestionType">
    <td class="fieldName"> 
        <span title="Default Value" id="defaultValueL">Default Value:</span>
    </td>
    <td>
        <html:text property="selection.defaultValue" size="25" maxlength="50" styleId="defaultValueText" />      
    </td>
</tr>