package gov.cdc.nedss.webapp.nbs.action.investigation.crs;
/**
 *
 * <p>Title: CrsEditLoad</p>
 * <p>Description: This is a load action class for Edit Investigation for the Congenital Rubella Syndrome conditions.</p>
 * <p>Copyright: Copyright (c) 2004</p>
 * <p>Company: Computer Science Corporation</p>
 * @author Shailender Rachamalla
 */
import java.io.*;
import java.util.*;
import javax.servlet.*;
import javax.servlet.http.*;

import org.apache.struts.action.*;
import gov.cdc.nedss.proxy.ejb.investigationproxyejb.vo.*;
import gov.cdc.nedss.systemservice.nbscontext.*;
import gov.cdc.nedss.systemservice.nbssecurity.*;
import gov.cdc.nedss.util.*;
import gov.cdc.nedss.webapp.nbs.action.investigation.common.*;
import gov.cdc.nedss.webapp.nbs.action.investigation.util.generic.*;
import gov.cdc.nedss.webapp.nbs.action.investigation.util.nip.crs.*;
import gov.cdc.nedss.webapp.nbs.form.investigation.*;
import gov.cdc.nedss.webapp.nbs.logicsheet.helper.*;


public class CrsEditLoad
    extends BaseEditLoad
{

    //For logging
    static final LogUtils logger = new LogUtils(CrsEditLoad.class.getName());

   /**
    * empty constructor
    */
    public CrsEditLoad()
    {
    }
    /**
     * Process the specified HTTP request, and create the corresponding HTTP response
        * (or forward to another web component that will create it). Return an ActionForward
        * instance describing where and how control should be forwarded, or null if the response
        * has already been completed.
        * @param mapping - The ActionMapping used to select this instance
        * @param form - The ActionForm bean for this request (if any)
        * @param request - The HTTP request we are processing
        * @param response - The HTTP response we are creating
        * @return mapping.findForward("XSP") -- ActionForward instance describing where and how control should be forwarded
        * @throws IOException -- if an input/output error occurs
     * @throws ServletException -- if a servlet exception occurs
     */
    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request,
    		HttpServletResponse response)
    				throws IOException, ServletException
    {

    	InvestigationForm investigationForm = (InvestigationForm)form;
    	HttpSession session = request.getSession(false);
    	String path = "";
    	NBSSecurityObj nbsSecurityObj = (NBSSecurityObj)session.getAttribute("NBSSecurityObject");
    	boolean editInvestigation = nbsSecurityObj.getPermission(NBSBOLookup.INVESTIGATION, NBSOperationLookup.EDIT);
    	if (!editInvestigation)
    	{
    		logger.info("You do not have access to edit Investigation");
    		session.setAttribute("error", "You do not have access to edit Investigation");
    		throw new ServletException("You do not have access to edit Investigation");
    	}
    	try {
    		String sCurrentTask = super.setContextForEdit(request, session);
    		String sContextAction = request.getParameter("ContextAction");

    		TreeMap<Object,Object> obsmap = super.mapObsQA(investigationForm.getOldProxy().getTheObservationVOCollection());
    		investigationForm.setObservationMap(obsmap);

    		String conditionCode = investigationForm.getOldProxy().getPublicHealthCaseVO().getThePublicHealthCaseDT().getCd();
    		CachedDropDownValues cdv = new CachedDropDownValues();
    		String businessObjNm = cdv.getLDFMap(conditionCode);
    		request.setAttribute("showConditionSpecificLDF", new Boolean(false));

    		super.createXSP(businessObjNm, investigationForm.getOldProxy().getPublicHealthCaseVO().getThePublicHealthCaseDT().getPublicHealthCaseUid(), investigationForm.getOldProxy(),investigationForm.getOldProxy().getPublicHealthCaseVO().getThePublicHealthCaseDT().getCd(),request);
    		super.createXSP(NEDSSConstants.PATIENT_LDF, investigationForm.getOldRevision().getThePersonDT().getPersonUid(), investigationForm.getOldRevision(), null, request);
    		
    		request.setAttribute("showConditionSpecificLDF", new Boolean(false));
        //Long mprUid = (Long) NBSContext.retrieve(session, NEDSSConstants.DSPatientPersonUID);
        Long mprUid =  investigationForm.getOldRevision().getThePersonDT().getPersonParentUid();
    		CRSInvestigationUtil crsInvestigationUtil = new CRSInvestigationUtil();
    		crsInvestigationUtil.setGenericRequestForView(investigationForm.getOldProxy(), request);
    		crsInvestigationUtil.getVaccinationSummaryRecords(mprUid,request);


    		//display revision patient tab
    		GenericInvestigationUtil util = new GenericInvestigationUtil();
    		util.displayRevisionPatient(investigationForm.getOldProxy(), request);
    		investigationForm.setOldRevision(util.getMPRevision(NEDSSConstants.PHC_PATIENT, investigationForm.getOldProxy()));

    		super.getNBSSecurityJurisdictions(request,nbsSecurityObj);
    		boolean viewContactTracing = false;

    		String ContactTracingByConditionCd = GenericInvestigationUtil.getConditionTracingEnableInd(conditionCode);

    		if(ContactTracingByConditionCd.equalsIgnoreCase(NEDSSConstants.CONTACT_TRACING_ENABLE_IND))
    			viewContactTracing = true;
    		else
    			viewContactTracing = false;
    		if(viewContactTracing)
    			viewContactTracing = nbsSecurityObj.getPermission(NBSBOLookup.CT_CONTACT,
    					NBSOperationLookup.VIEW);
    		path =  "/error";
    		if(viewContactTracing)
    			path = "/diseaseform/nip/crs/investigation_crs_edit?CurrentTask=" + sCurrentTask +
    			"&ContextAction=" + sContextAction;
    		else 
    			path = "/diseaseform/nip/crs/investigation_crs_edit_No_Contact?CurrentTask=" + sCurrentTask +
    			"&ContextAction=" + sContextAction;
    	}catch (Exception e) {
    		logger.error("Exception in CRS Edit Load: " + e.getMessage());
    		e.printStackTrace();
    		throw new ServletException("Error occurred during CRS Edit Load : "+e.getMessage());
    	}
    	return new ActionForward(path);

   }


}