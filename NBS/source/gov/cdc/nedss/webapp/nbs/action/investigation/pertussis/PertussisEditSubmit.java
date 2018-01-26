package gov.cdc.nedss.webapp.nbs.action.investigation.pertussis;
/**
 * <p>Title: PertussisEditSubmit</p>
 * <p>Description: This is a Submit action class for edit investigation for all the Pertussis conditions.</p>
 * <p>Copyright: Copyright (c) 2004</p>
 * <p>Company: Computer Science Corporation</p>
 * @author Shailender Rachamalla
 */
import java.io.*;
import java.util.*;

import javax.servlet.*;
import javax.servlet.http.*;

import org.apache.struts.action.*;
import gov.cdc.nedss.act.observation.vo.*;
import gov.cdc.nedss.act.publichealthcase.vo.*;
import gov.cdc.nedss.association.dt.*;
import gov.cdc.nedss.entity.person.vo.*;
import gov.cdc.nedss.exception.*;
import gov.cdc.nedss.ldf.dt.*;
import gov.cdc.nedss.proxy.ejb.investigationproxyejb.vo.*;
import gov.cdc.nedss.systemservice.nbscontext.*;
import gov.cdc.nedss.systemservice.nbssecurity.*;
import gov.cdc.nedss.util.*;
import gov.cdc.nedss.webapp.nbs.action.investigation.common.*;
import gov.cdc.nedss.webapp.nbs.action.person.util.*;
import gov.cdc.nedss.webapp.nbs.form.investigation.*;
import gov.cdc.nedss.webapp.nbs.util.*;

public class PertussisEditSubmit extends BaseEditSubmit
{


  //For logging
  static final LogUtils logger = new LogUtils(PertussisEditSubmit.class.getName());
  static String strLock = "lock";

   public PertussisEditSubmit()
   {
   }

   /**
   * Process the specified HTTP request, and create the corresponding HTTP response
   * (or forward to another web component that will create it). Return an ActionForward
   * instance describing where and how control should be forwarded, or null if the response
   * has already been completed.
   * @param mapping - The ActionMapping used to select this instance
   * @param form - The optional ActionForm bean for this request (if any)
   * @param request - The HTTP request we are processing
   * @param response - The HTTP response we are creating
   * @return mapping.findForward("XSP") -- ActionForward instance describing where and how control should be forwarded
   * @throws IOException -- if an input/output error occurs
   * @throws ServletException -- if a servlet exception occurs
   */
   public ActionForward execute(ActionMapping mapping,
				 ActionForm form,
				 HttpServletRequest request,
				 HttpServletResponse response)
   throws IOException, ServletException {

      logger.debug("inside InvestigationSubmit");
      HttpSession session = request.getSession();
      if (session == null)
      {
	logger.debug("error no session in InvestigationSubmit");
	return mapping.findForward("login");
      }
      Long investigationStartTime = new Long((new Date()).getTime());
      session.setAttribute("investigationStartTime", investigationStartTime);

      NBSSecurityObj nbsSecurityObj = (NBSSecurityObj) session.getAttribute("NBSSecurityObject");
      // Context
      // are we edit or create?
      String sContextAction = request.getParameter("ContextAction");
      String sCurrentTask = NBSContext.getCurrentTask(session);

      if(sContextAction == null)
      {
	session.setAttribute("error", "contextAction is " +  sContextAction +" sCurrentTask " + sCurrentTask);
	throw new ServletException("contextAction is null");
      }

	if(sContextAction.equals("Submit"))
	{
	  try
	  {
	    logger.debug("Handling edit action in InvestigationEditSubmit");
	    this.editHandler(mapping, form, request, session, nbsSecurityObj, sCurrentTask);

	  }
	  catch (NEDSSAppConcurrentDataException e)
	  {
	    logger.fatal("ERROR - NEDSSAppConcurrentDataException, The data has been modified by another user, please recheck! ", e);
	    return mapping.findForward("dataerror");
	  }
	}
	return mapping.findForward(sContextAction);

  }


   private void editHandler(ActionMapping mapping,
					       ActionForm form,
					       HttpServletRequest request,
					       HttpSession session,
					       NBSSecurityObj nbsSecurityObj,
					       String sCurrentTask) throws ServletException,
										     NEDSSAppConcurrentDataException
    {
      boolean investigationProxyVODirty = false;

      InvestigationForm investigationForm = (InvestigationForm)form;
      InvestigationProxyVO newInvestigationProxyVO = investigationForm.getProxy();
      InvestigationProxyVO oldInvestigationProxyVO = investigationForm.getOldProxy();

	  // Check if the Case Status has changed
	  boolean caseStatusChanged = caseStatusChanged(oldInvestigationProxyVO, newInvestigationProxyVO);

      PersonVO revision = investigationForm.getPatient();
      PersonVO oldRevision  = investigationForm.getOldRevision();
      // set up the ldf collection
      if(investigationForm.getLdfCollection()!= null)
      {
        HashMap<Object,Object> map = new HashMap<Object,Object>();
        HashMap<Object,Object> invLdfMap = new HashMap<Object,Object>();
        String businessObjName = null;

        // use the new API to retrieve custom field collection
        // to handle multiselect fields (xz 01/11/2005)
        Collection<Object>  coll = extractLdfDataCollection(investigationForm, request);
       Iterator<Object>  it = coll.iterator();
        while (it.hasNext())
        {
          StateDefinedFieldDataDT stateDT = (StateDefinedFieldDataDT) it.next();
          if (stateDT != null && stateDT.getBusinessObjNm() != null)
          {
            if (stateDT.getBusinessObjNm().equalsIgnoreCase(NEDSSConstants.INVESTIGATION_NIP_LDF))
            {
              stateDT.setItDirty(true);
              businessObjName = stateDT.getBusinessObjNm();
              if(stateDT.getLdfUid()!=null)
              invLdfMap.put(stateDT.getLdfUid(), stateDT);
            }

            if (stateDT.getBusinessObjNm().equalsIgnoreCase(NEDSSConstants.PATIENT_LDF))
            {
              stateDT.setItDirty(true);
              stateDT.setItDirty(true);
              if(stateDT.getLdfUid()!=null)
              map.put(stateDT.getLdfUid(), stateDT);

            }

           }
          }

           if(invLdfMap.values().size()>0)
           newInvestigationProxyVO.setTheStateDefinedFieldDataDTCollection(new ArrayList<Object>(invLdfMap.values()));

           if(map.values().size()>0)
           revision.setTheStateDefinedFieldDataDTCollection(map.values());

           newInvestigationProxyVO.setBusinessObjNm(businessObjName);
           oldInvestigationProxyVO.setBusinessObjNm(businessObjName);

        }


      Collection<Object>  personColl = new ArrayList<Object> ();
      revision.setItDirty(true);
      revision.setItNew(false);
      revision.getThePersonDT().setItDirty(true);
      revision.getThePersonDT().setItNew(false);
      PersonUtil.setPatientForEventEdit(revision, request);
      personColl.add(revision);
      newInvestigationProxyVO.setThePersonVOCollection(personColl);

      //set PublicHealthCaseVO stuff, confirmationMethods, ActIdDT
      PublicHealthCaseVO phcVO = newInvestigationProxyVO.getPublicHealthCaseVO();
      PublicHealthCaseVO oldPhcVO = oldInvestigationProxyVO.getPublicHealthCaseVO();

      if(request.getParameter("proxy.publicHealthCaseVO_s.thePublicHealthCaseDT.sharedInd") == null)
      phcVO.getThePublicHealthCaseDT().setSharedInd("F");

      super.setConfirmationMethods(request, oldPhcVO);
      super.setActIdForPublicHealthCase(phcVO, oldPhcVO);

      //set the dateAssignedToInvestigation
      Collection<Object>  oldParticipationDTCollection  = oldInvestigationProxyVO.getPublicHealthCaseVO().getTheParticipationDTCollection();
      super.setParticipationsForEdit(form, request, investigationProxyVODirty);
      super.setDateAssignToInvestigation(oldParticipationDTCollection, request);
      super.setDateAssignToInvestigation(newInvestigationProxyVO.getTheParticipationDTCollection(), request);

      Collection<ObservationVO>  obsColl = newInvestigationProxyVO.getTheObservationVOCollection();
      Collection<ObservationVO>  oldObsColl = oldInvestigationProxyVO.getTheObservationVOCollection();

      //get the parent Observation
     Iterator<ObservationVO>  itObs = oldObsColl.iterator();
      Long parentObservationUID = null;
      Collection<Object>  actrelationships = oldInvestigationProxyVO.getPublicHealthCaseVO().getTheActRelationshipDTCollection();
      if(actrelationships != null)
      {
       Iterator<Object>  actIt = actrelationships.iterator();
        while(actIt.hasNext())
        {
          ActRelationshipDT actDT = (ActRelationshipDT)actIt.next();
          Long publicHealthCaseUID = oldInvestigationProxyVO.getPublicHealthCaseVO().getThePublicHealthCaseDT().getPublicHealthCaseUid();
          if(actDT.getTypeCd().equals(NEDSSConstants.PHC_INV_FORM))
          {
            parentObservationUID = actDT.getSourceActUid();
            break;
          }
        }
      }
      int tempID = -1;
      ObservationVO parentObservationVO = ObservationUtils.findObservationByUid(oldObsColl, parentObservationUID);

      TreeMap<Object,Object> observationMap = investigationForm.getObservationMap();
      TreeMap<Object,Object> obsActTreeMap = super.setObservations(obsColl, observationMap, "T",parentObservationVO.getTheActRelationshipDTCollection(), parentObservationUID, "InvFrmQ", request, tempID);
      Collection<ObservationVO>  newObservationCollection  = (ArrayList<ObservationVO> )obsActTreeMap.get("observations");
      Collection<Object>  newActRelationshipCollection  = (ArrayList<Object> )obsActTreeMap.get("actrelations");
      tempID = ((Integer)obsActTreeMap.get("tempID")).intValue();

      newObservationCollection.add(parentObservationVO);
      this.copyPublicHealthCaseDTValues(newInvestigationProxyVO, oldInvestigationProxyVO);

      newInvestigationProxyVO.setTheObservationVOCollection(newObservationCollection);
      newInvestigationProxyVO.setTheActRelationshipDTCollection(newActRelationshipCollection);
      newInvestigationProxyVO.setPublicHealthCaseVO(oldInvestigationProxyVO.getPublicHealthCaseVO());
      newInvestigationProxyVO.setTheNotificationSummaryVOCollection(oldInvestigationProxyVO.getTheNotificationSummaryVOCollection());

      //batch-entry
      this.setBatchEntryObseravationsForEdit(investigationForm, parentObservationVO, request, tempID);

      newInvestigationProxyVO.setItDirty(true);
      newInvestigationProxyVO.setItNew(false);
      newInvestigationProxyVO.getPublicHealthCaseVO().getThePublicHealthCaseDT().setItDirty(true);
      newInvestigationProxyVO.getPublicHealthCaseVO().setItDirty(true);

	  // set the case status dirty flag
	  if((newInvestigationProxyVO != null) &&
		(newInvestigationProxyVO.getPublicHealthCaseVO() != null) &&
		(newInvestigationProxyVO.getPublicHealthCaseVO().getThePublicHealthCaseDT() != null)){
			newInvestigationProxyVO.
				getPublicHealthCaseVO().
				getThePublicHealthCaseDT().
				setCaseStatusDirty(caseStatusChanged);

	  }

      Long publicHealthCaseUID = null;
      try
      {

	  publicHealthCaseUID =  sendProxyToInvestigationEJB(investigationForm, session,request);
      }
      catch (NEDSSAppConcurrentDataException e)
      {
	logger.fatal("ERROR - NEDSSAppConcurrentDataException, The data has been modified by another user, please recheck! ", e);
	throw new NEDSSAppConcurrentDataException("Concurrent access occurred in InterventionEditSubmit : " + e.toString());
      }
      catch(Exception e)
      {
    	  logger.error("Exception in Pertussis Edit Submit: " + e.getMessage());
    	  e.printStackTrace();
	  throw new ServletException(e.toString());
      }
      logger.info("Done storing for Edit investigation. publicHealthCaseUID= " + publicHealthCaseUID);
   }


   /**
    * set batch entry observation value edit from InvestigationForm object with code
    * @param InvestigationForm the investigationForm
    * @param String the investigationFormCd
    */
      private int setBatchEntryObseravationsForEdit(InvestigationForm investigationForm, ObservationVO formObservationVO, HttpServletRequest request, int tempID)
      {

           Collection<Object>  itemActRelationships = null;
           ActRelationshipDT actRelationshipDT = null;

           Collection<ObservationVO>  observationVOCollection  = investigationForm.getProxy().getTheObservationVOCollection();
           Collection<Object>  actRelationshipDTCollection  = investigationForm.getProxy().getTheActRelationshipDTCollection();
           if(actRelationshipDTCollection  == null)
           actRelationshipDTCollection  = new ArrayList<Object> ();
           investigationForm.getProxy().setTheActRelationshipDTCollection(actRelationshipDTCollection);
           {
             logger.info(" .main edit ObservationVOCollection  size: " + ((observationVOCollection  == null) ? "0" : String.valueOf(observationVOCollection.size())) + " .main actRelationshipDTCollection  size: " + ((actRelationshipDTCollection  == null) ? "0" : String.valueOf(actRelationshipDTCollection.size())));
             Collection<Object>  antiBatchEntryCollection  = investigationForm.getAntibioticBatchEntryCollection();
             logger.info(" edit ..AntibioticBatchEntryCollection  size: " + ((antiBatchEntryCollection  == null) ? "0" : String.valueOf(antiBatchEntryCollection.size())));
             if(antiBatchEntryCollection  != null)
             {
                TreeMap<Object,Object> treeMap = null;
               Iterator<Object>  anIterator = antiBatchEntryCollection.iterator();
                 while(anIterator.hasNext())
                 {
                     BatchEntryHelper aBatch = (BatchEntryHelper)anIterator.next();
                     treeMap = super.setBatchObservations(aBatch, formObservationVO,NEDSSConstants.ANTIBIOTIC_CONTAIN_ROW, request, tempID);
                     observationVOCollection.addAll((ArrayList<ObservationVO> )(treeMap.get("observations")));
                     actRelationshipDTCollection.addAll((ArrayList<Object> )(treeMap.get("actrelations")));
                     tempID = ((Integer)treeMap.get("tempID")).intValue();
                 }
             }
           }
           logger.debug(" .main ObservationVOCollection  after adding Antibiotic batchEntry size: " + ((observationVOCollection  == null) ? "0" : String.valueOf( observationVOCollection.size())) +  " after adding item actRelationshipDTCollection  size: " + ((actRelationshipDTCollection  == null) ? "0" : String.valueOf(actRelationshipDTCollection.size())));

           Collection<Object>  SourcebatchEntryCollection  = investigationForm.getSourceBatchEntryCollection();
           logger.debug(" edit ..SourcebatchEntryCollection  size: " + ((SourcebatchEntryCollection  == null) ? "0" : String.valueOf(SourcebatchEntryCollection.size())));
           if(SourcebatchEntryCollection  != null)
           {
               TreeMap<Object,Object> sTreeMap = null;
              Iterator<Object>  anIterator = SourcebatchEntryCollection.iterator();
               while(anIterator.hasNext())
               {
                   ObservationVO rowObservationVO = null;
                   BatchEntryHelper sourceBatchEntryHelper = (BatchEntryHelper)anIterator.next();
                   sTreeMap = super.setBatchObservations(sourceBatchEntryHelper, formObservationVO,NEDSSConstants.SOURCE_CONTAIN_ROW, request, tempID);
                   observationVOCollection.addAll((ArrayList<ObservationVO> )(sTreeMap.get("observations")));
                   actRelationshipDTCollection.addAll((ArrayList<Object> )(sTreeMap.get("actrelations")));
                   tempID = ((Integer)sTreeMap.get("tempID")).intValue();

               }
           }
           logger.info(" .main ObservationVOCollection  after adding Antibiotic batchEntry size: " + ((observationVOCollection  == null) ? "0" : String.valueOf( observationVOCollection.size())) +  " after adding item actRelationshipDTCollection  size: " + ((actRelationshipDTCollection  == null) ? "0" : String.valueOf(actRelationshipDTCollection.size())));
 
          Iterator<Object>  actIterator = actRelationshipDTCollection.iterator();
           ActRelationshipDT itorActRelationshipDT = null;
           while(actIterator.hasNext())
           {
               itorActRelationshipDT = (ActRelationshipDT)actIterator.next();
           }

        return tempID;
      }

}//InvestigationEditSubmit