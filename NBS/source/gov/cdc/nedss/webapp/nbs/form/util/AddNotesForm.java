package gov.cdc.nedss.webapp.nbs.form.util;

import org.apache.struts.action.ActionForm;
import gov.cdc.nedss.util.NEDSSConstants;

public class AddNotesForm extends ActionForm 
{
	private String notes;
	private String accessModifier;

	public String getNotes() {
		return notes;
	}

	public void setNotes(String notes) {
		this.notes = notes;
	}

	public String getAccessModifier() {
		return accessModifier;
	}

	public void setAccessModifier(String accessModifier) {
		this.accessModifier = accessModifier;
	}

	public void reset() {
		notes = "";
		accessModifier = NEDSSConstants.PUBLIC;
	}
}