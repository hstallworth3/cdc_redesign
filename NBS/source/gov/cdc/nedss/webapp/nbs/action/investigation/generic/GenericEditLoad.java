package gov.cdc.nedss.webapp.nbs.action.investigation.generic;
/**
 *
 * <p>Title: GenericEditLoad</p>
 * <p>Description: This is a Load action class for create investigation for all the Generic conditions.</p>
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
import gov.cdc.nedss.webapp.nbs.form.investigation.*;
import gov.cdc.nedss.webapp.nbs.logicsheet.helper.*;


public class GenericEditLoad
    extends BaseEditLoad
{

    //For logging
    static final LogUtils logger = new LogUtils(GenericEditLoad.class.getName());

    /**
     * empty constructor
     */
    public GenericEditLoad()
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

	NBSSecurityObj nbsSecurityObj = (NBSSecurityObj)session.getAttribute("NBSSecurityObject");
	String conditionCd = investigationForm.getOldProxy().getPublicHealthCaseVO().getThePublicHealthCaseDT().getCd();
	boolean editInvestigation = nbsSecurityObj.getPermission(NBSBOLookup.INVESTIGATION, NBSOperationLookup.EDIT);
	if (!editInvestigation)
	{
	    session.setAttribute("error", "You do not have access to edit Investigation");
	    throw new ServletException("You do not have access to edit Investigation");
	}
	boolean viewContactTracing = false;
    String ContactTracingByConditionCd = GenericInvestigationUtil.getConditionTracingEnableInd(conditionCd);
	
	if(ContactTracingByConditionCd.equalsIgnoreCase(NEDSSConstants.CONTACT_TRACING_ENABLE_IND))
		viewContactTracing = true;
	else
		viewContactTracing = false;
	
	if(viewContactTracing)
	viewContactTracing = nbsSecurityObj.getPermission(NBSBOLookup.CT_CONTACT,
            NBSOperationLookup.VIEW);
 

	String sCurrentTask = super.setContextForEdit(request, session);
	String sContextAction = request.getParameter("ContextAction");

        TreeMap<Object,Object> obsmap = super.mapObsQA(investigationForm.getOldProxy().getTheObservationVOCollection());
        investigationForm.setObservationMap(obsmap);
       
        CachedDropDownValues cdv = new CachedDropDownValues();
        String businessObjNm = cdv.getLDFMap(conditionCd);
        request.setAttribute("showConditionSpecificLDF", new Boolean(false));

        super.createXSP(businessObjNm,investigationForm.getOldProxy().getPublicHealthCaseVO().getThePublicHealthCaseDT().getPublicHealthCaseUid(), investigationForm.getOldProxy(),investigationForm.getOldProxy().getPublicHealthCaseVO().getThePublicHealthCaseDT().getCd(),request);
        super.createXSP(NEDSSConstants.PATIENT_LDF, investigationForm.getOldRevision().getThePersonDT().getPersonUid(), investigationForm.getOldRevision(), null, request);

        this.convertProxyToRequestObj(form, request, session);
        request.setAttribute("showConditionSpecificLDF", new Boolean(false));

         //display revision patient tab
        GenericInvestigationUtil util = new GenericInvestigationUtil();
        util.displayRevisionPatient(investigationForm.getOldProxy(), request);
        investigationForm.setOldRevision(util.getMPRevision(NEDSSConstants.PHC_PATIENT, investigationForm.getOldProxy()));

        super.getNBSSecurityJurisdictions(request,nbsSecurityObj);


	return (this.getForwardPage(sCurrentTask, sContextAction, viewContactTracing));

    }

    /**
     * s
     * @param form
     * @param request
     * @param session
     * @throws ServletException
     */

    private void convertProxyToRequestObj(ActionForm form, HttpServletRequest request, HttpSession session) throws ServletException
    {

	InvestigationForm investigationForm = (InvestigationForm)form;
	InvestigationProxyVO investigationProxyVO = investigationForm.getOldProxy();

      //  Long mprUid = (Long) NBSContext.retrieve(session, NEDSSConstants.DSPatientPersonUID);

        GenericInvestigationUtil genericInvestigationUtil = new GenericInvestigationUtil();
        genericInvestigationUtil.setGenericRequestForView(investigationProxyVO, request);
    }


    /**
     *
     * @param sCurrentTask
     * @param sContextAction
     * @return
     */
    private ActionForward getForwardPage(String sCurrentTask,
					 String sContextAction, boolean viewContactTracing)
    {

    	String path = "/error";
    	if(viewContactTracing)
    	path = "/diseaseform/generic/investigation_generic_edit?CurrentTask=" + sCurrentTask +
		   "&ContextAction=" + sContextAction;
    	else
    		path = "/diseaseform/generic/investigation_generic_edit_No_Contact?CurrentTask=" + sCurrentTask +
 		   "&ContextAction=" + sContextAction;

	logger.debug(" path: " + path);

	return new ActionForward(path);
    }



}