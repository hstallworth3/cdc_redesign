package gov.cdc.nedss.webapp.nbs.action.pagemanagement.rendering.util;

import gov.cdc.nedss.act.publichealthcase.dt.PublicHealthCaseDT;
import gov.cdc.nedss.act.publichealthcase.vo.PublicHealthCaseVO;
import gov.cdc.nedss.association.dt.ActRelationshipDT;
import gov.cdc.nedss.exception.NEDSSAppConcurrentDataException;
import gov.cdc.nedss.exception.NEDSSAppException;
import gov.cdc.nedss.page.ejb.pageproxyejb.dt.NbsAnswerDT;
import gov.cdc.nedss.page.ejb.pageproxyejb.util.AssociatedInvestigationUpdateUtil;
import gov.cdc.nedss.page.ejb.pageproxyejb.util.PageCaseUtil;
import gov.cdc.nedss.page.ejb.pageproxyejb.vo.PageProxyVO;
import gov.cdc.nedss.page.ejb.pageproxyejb.vo.act.PageActProxyVO;
import gov.cdc.nedss.pam.act.NbsCaseAnswerDT;
import gov.cdc.nedss.proxy.ejb.investigationproxyejb.vo.CoinfectionSummaryVO;
import gov.cdc.nedss.proxy.ejb.investigationproxyejb.vo.InvestigationProxyVO;
import gov.cdc.nedss.systemservice.ejb.mainsessionejb.bean.MainSessionCommand;
import gov.cdc.nedss.systemservice.nbscontext.NBSConstantUtil;
import gov.cdc.nedss.systemservice.nbscontext.NBSContext;
import gov.cdc.nedss.systemservice.nbssecurity.NBSSecurityObj;
import gov.cdc.nedss.systemservice.nbssecurity.ProgramAreaVO;
import gov.cdc.nedss.systemservice.util.MainSessionHolder;
import gov.cdc.nedss.systemservice.vo.ParticipationTypeVO;
import gov.cdc.nedss.util.JNDINames;
import gov.cdc.nedss.util.LogUtils;
import gov.cdc.nedss.util.NEDSSConstants;
import gov.cdc.nedss.util.PropertyUtil;
import gov.cdc.nedss.webapp.nbs.action.pagemanagement.util.common.PageManagementCommonActionUtil;
import gov.cdc.nedss.webapp.nbs.action.util.CallProxyEJB;
import gov.cdc.nedss.webapp.nbs.action.util.MessageLogUtil;
import gov.cdc.nedss.webapp.nbs.action.util.NBSPageConstants;
import gov.cdc.nedss.webapp.nbs.form.page.PageForm;
import gov.cdc.nedss.webapp.nbs.helper.CachedDropDowns;
import gov.cdc.nedss.webapp.nbs.logicsheet.helper.CachedDropDownValues;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.StringTokenizer;
import java.util.TreeMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import jdk.nashorn.internal.ir.RuntimeNode.Request;

/**
 * Utility class to construct PageProxyVO out of PageClientVO and delegates to
 * PageProxyEJB for persistence.
 * 
 * @author Narendra Mallela
 *         <p>
 *         Copyright: Copyright (c) 2008
 *         </p>
 *         <p>
 *         Company: Computer Sciences Corporation
 *         </p>
 *         PageStoreUtil.java Jan 17, 2010
 * @version 1.0
 */
public class PageStoreUtil
{

    static final LogUtils             logger = new LogUtils(PageStoreUtil.class.getName());
    static final CachedDropDownValues srtc   = new CachedDropDownValues();

    /**
     * createHandler
     * 
     * @param form
     * @param req
     * @throws Exception
     */
    public static PageProxyVO createHandler(PageForm form, HttpServletRequest req) throws Exception
    {

        PageProxyVO proxyVO = null;
        try
        {
            NBSSecurityObj nbsSecurityObj = (NBSSecurityObj) req.getSession().getAttribute("NBSSecurityObject");
            String userId = nbsSecurityObj.getTheUserProfile().getTheUser().getEntryID();
            proxyVO = PageCreateHelper.create(form, req);
            if (form.getErrorList().size() == 0)
            {

                String sCurrentTask = NBSContext.getCurrentTask(req.getSession());
                int tempID = -1;
                // Need to revisit
                setEntitiesForCreate(proxyVO, form, tempID, userId, req);
                if (sCurrentTask.equals("CreateInvestigation10") || sCurrentTask.equals("CreateInvestigation11") )
                {
                    createActRelationshipForDoc(sCurrentTask, proxyVO, req);
                }
                Long providerUid = nbsSecurityObj.getTheUserProfile().getTheUser().getProviderUid();

                MessageLogUtil.createMessageForUpdatedComments(form, (PageActProxyVO) proxyVO, providerUid);
                // send Proxy to EJB To Persist
                Long phcUid = sendProxyToPageEJB(proxyVO, req, sCurrentTask,
                        (String) form.getAttributeMap().get(NEDSSConstants.PROCESSING_DECISION));
                // Context
                setContextForCreate(proxyVO, phcUid, getProgAreaVO(req.getSession()), req.getSession());
            }
            else
            {
                form.setPageTitle(NBSPageConstants.CREATE_VARICELLA, req);
            }
        }
        catch (Exception e)
        {
            logger.fatal("Exception occured in PageStoreUtil.createHandler: " + ", " + e.getMessage(), e);
            throw new NEDSSAppException(e.getMessage(), e);
        }
        return proxyVO;
    }

    /**
     * createHandler
     * 
     * @param form
     * @param req
     * @throws Exception
     */
    public static PageProxyVO createGenericHandler(PageForm form, HttpServletRequest req) throws Exception
    {
        PageProxyVO proxyVO = null;
        try
        {
            proxyVO = PageCreateHelper.createGeneric(form, req);
            if (form.getErrorList().size() == 0)
            {
                Long actUid = sendProxyToPageEJB(proxyVO, req, form.getBusinessObjectType());
                if (actUid != null)
                {
                    // store returned UID
                    if (form.getBusinessObjectType().equals(NEDSSConstants.INTERVIEW_BUSINESS_OBJECT_TYPE))
                        proxyVO.getInterviewVO().getTheInterviewDT().setInterviewUid(actUid);
                    else if( NEDSSConstants.VACCINATION_BUSINESS_OBJECT_TYPE.equals(form.getBusinessObjectType()))
                    	proxyVO.getInterventionVO().getTheInterventionDT().setInterventionUid(actUid);
                }
            }
        }
        catch (Exception e)
        {	
        	logger.fatal("Exception occured in PageStoreUtil.createGenericHandler: " + ", " + e.getMessage(), e);
        	throw new NEDSSAppException(e.getMessage(), e);
        }
        return proxyVO;
    }

    public static ProgramAreaVO getProgAreaVO(HttpSession session) throws NEDSSAppException
    {
        try {
			String conditionCd = (String) NBSContext.retrieve(session, NBSConstantUtil.DSInvestigationCondition);
			String programArea = (String) NBSContext.retrieve(session, NBSConstantUtil.DSInvestigationProgramArea);
			ProgramAreaVO programAreaVO = null;

			programAreaVO = srtc.getProgramAreaCondition("('" + programArea + "')", conditionCd);
			return programAreaVO;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			logger.fatal("Exception occured in PageStoreUtil.getProgAreaVO: " + ", " + e.getMessage(), e);
        	throw new NEDSSAppException(e.getMessage(), e);
		}
    }

    public static void setContextForCreate(PageProxyVO proxyVO, Long phcUid, ProgramAreaVO programArea,
            HttpSession session) throws NEDSSAppException
    {
        try {
			// context store
			String investigationJurisdiction = ((PageActProxyVO) proxyVO).getPublicHealthCaseVO()
			        .getThePublicHealthCaseDT().getJurisdictionCd();
			NBSContext.store(session, NBSConstantUtil.DSInvestigationUid, phcUid.toString());
			NBSContext.store(session, NBSConstantUtil.DSInvestigationJurisdiction, investigationJurisdiction);
			String progArea = (String) NBSContext.retrieve(session, NBSConstantUtil.DSInvestigationProgramArea);
			NBSContext.store(session, NBSConstantUtil.DSInvestigationProgramArea, progArea);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			logger.fatal("Exception occured in PageStoreUtil.setContextForCreate: phcUid: " + phcUid + ", " + e.getMessage(), e);
        	throw new NEDSSAppException(e.getMessage(), e);
		}
    }

    /**
     * 
     * @param proxyVO
     * @param request
     * @param sCurrentTask
     * @return
     * @throws NEDSSAppConcurrentDataException
     * @throws Exception
     */
    public static Long sendProxyToPageEJB(PageProxyVO proxyVO, HttpServletRequest request, String sCurrentTask,
            String processingDecision) throws NEDSSAppConcurrentDataException, Exception
    {

        try {
			HttpSession session = request.getSession();
			MainSessionCommand msCommand = null;
			Long publicHealthCaseUID = null;
			String sBeanJndiName = JNDINames.PAGE_PROXY_EJB;
			MainSessionHolder holder = new MainSessionHolder();
			msCommand = holder.getMainSessionCommand(session);
			ArrayList<?> resultUIDArr = new ArrayList<Object>();
			translateAnswerDTsToCaseAnswerDTs(proxyVO);
			if (sCurrentTask != null
			        && (sCurrentTask.equals("CreateInvestigation2") || sCurrentTask.equals("CreateInvestigation3")
			                || sCurrentTask.equals("CreateInvestigation4") || sCurrentTask.equals("CreateInvestigation5")
			                || sCurrentTask.equals("CreateInvestigation6") || sCurrentTask.equals("CreateInvestigation7") || sCurrentTask
		                    .equals("CreateInvestigation9")|| sCurrentTask
		                    .equals("CreateInvestigation8")))
			{

			    String sMethod = "setPageProxyWithAutoAssoc";

			    Object sObservationUID = NBSContext.retrieve(session, NBSConstantUtil.DSObservationUID);
			    Object observationTypeCd = NBSContext.retrieve(session, NBSConstantUtil.DSObservationTypeCd);
			    Long DSObservationUID = new Long(sObservationUID.toString());
			    Object[] oParams = { NEDSSConstants.CASE, proxyVO, DSObservationUID, observationTypeCd.toString(),
			            processingDecision };
			    resultUIDArr = msCommand.processRequest(sBeanJndiName, sMethod, oParams);
			    publicHealthCaseUID = (Long) resultUIDArr.get(0);
			}
			else
			{
			    Object[] oParams = { NEDSSConstants.CASE, proxyVO };
			    String sMethod = "setPageProxyVO";
			    resultUIDArr = msCommand.processRequest(sBeanJndiName, sMethod, oParams);

			    if ((resultUIDArr != null) && (resultUIDArr.size() > 0))
			    {
			        publicHealthCaseUID = (Long) resultUIDArr.get(0);
			    }

			}
			String changeCondition=(String)request.getAttribute("changeCondition");
			if(changeCondition!=null){
				if(changeCondition.equals("true")){
				//update CTContact table
				String sMethod = "changeCondition";
				Long phcUid = (Long) proxyVO.getPublicHealthCaseVO().getThePublicHealthCaseDT().getPublicHealthCaseUid();
				Object[] oParams = { NEDSSConstants.CASE, phcUid,
						proxyVO};
				msCommand.processRequest(sBeanJndiName, sMethod, oParams);
				}
			}
			return publicHealthCaseUID;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			logger.fatal("Exception occured in PageStoreUtil.sendProxyToPageEJB: processingDecision: " + processingDecision + ", " + e.getMessage(), e);
        	throw new NEDSSAppException(e.getMessage(), e);
		}
    }

    
    public static Long sendProxyToPageEJBMerge(PageProxyVO proxyVO,PageProxyVO supersededProxyVO,PageProxyVO survivorProxyVOOld, String localIdSurvivor, String localIdSuperseded, HttpServletRequest request) 
    		throws NEDSSAppConcurrentDataException, Exception
    {

        try {
			HttpSession session = request.getSession();
			MainSessionCommand msCommand = null;
			Long publicHealthCaseUID = null;
			String sBeanJndiName = JNDINames.PAGE_PROXY_EJB;
			MainSessionHolder holder = new MainSessionHolder();
			msCommand = holder.getMainSessionCommand(session);
			ArrayList<?> resultUIDArr = new ArrayList<Object>();
			translateAnswerDTsToCaseAnswerDTs(proxyVO);
			Object[] oParams = { NEDSSConstants.CASE, proxyVO, supersededProxyVO,survivorProxyVOOld, localIdSurvivor, localIdSuperseded};
			
		    String sMethod = "setPageProxyVOMerge";
		    resultUIDArr = msCommand.processRequest(sBeanJndiName, sMethod, oParams);

		    if ((resultUIDArr != null) && (resultUIDArr.size() > 0))
		    {
		        publicHealthCaseUID = (Long) resultUIDArr.get(0);
		    }

			return publicHealthCaseUID;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			logger.fatal("Exception occured in PageStoreUtil.sendProxyToPageEJBMerge: : " + e.getMessage(), e);
        	throw new NEDSSAppException(e.getMessage(), e);
		}
    }
    
    
    /**
     * 
     * @param proxyVO
     * @param request
     * @param businessObjectType
     *            i.e. IXS
     * @return
     * @throws NEDSSAppConcurrentDataException
     * @throws Exception
     */
    public static Long sendProxyToPageEJB(PageProxyVO proxyVO, HttpServletRequest request, String businessObjectType)
            throws NEDSSAppConcurrentDataException, Exception
    {
        try {
			HttpSession session = request.getSession();
			MainSessionCommand msCommand = null;
			Long actUID = null;
			String sBeanJndiName = JNDINames.PAGE_PROXY_EJB;
			MainSessionHolder holder = new MainSessionHolder();
			msCommand = holder.getMainSessionCommand(session);
			ArrayList<?> resultUIDArr = new ArrayList<Object>();

			Object[] oParams = { businessObjectType, proxyVO };
			String sMethod = "setPageProxyVO";
			resultUIDArr = msCommand.processRequest(sBeanJndiName, sMethod, oParams);

			if ((resultUIDArr != null) && (resultUIDArr.size() > 0))
			{
			    actUID = (Long) resultUIDArr.get(0);
			}
			return actUID;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			logger.fatal("Exception occured in PageStoreUtil.sendProxyToPageEJB: businessObjectType: " + businessObjectType + ", PublicHealthCaseUid: " + proxyVO.getPublicHealthCaseVO().getThePublicHealthCaseDT().getPublicHealthCaseUid() + ", " + e.getMessage(), e);
        	throw new NEDSSAppException(e.getMessage(), e);
		}
    }

    /**
     * editHandler
     * 
     * @param form
     * @param req
     * @throws Exception
     */
    public static PageProxyVO editHandler(PageForm form, HttpServletRequest req) throws Exception
    {
        try {
			PageProxyVO proxyVO = null;
			NBSSecurityObj nbsSecurityObj = (NBSSecurityObj) req.getSession().getAttribute("NBSSecurityObject");
			String userId = nbsSecurityObj.getTheUserProfile().getTheUser().getEntryID();
			proxyVO = PageCreateHelper.editHandler(form, req);
			if (form.getErrorList().size() == 0)
			{

			    PublicHealthCaseVO phcVO = ((PageActProxyVO) form.getPageClientVO().getOldPageProxyVO())
			            .getPublicHealthCaseVO();

			    setEntitiesForEdit((PageActProxyVO) proxyVO, form, phcVO, userId, req);
			    Long providerUid = nbsSecurityObj.getTheUserProfile().getTheUser().getProviderUid();
			    MessageLogUtil.createMessageForUpdatedComments(form, (PageActProxyVO) proxyVO, providerUid);
			    sendProxyToPageEJB(proxyVO, req, null, null);
			}
			else
			{
			    form.setPageTitle(NBSPageConstants.EDIT_VARICELLA, req);
			}
			return proxyVO;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			logger.fatal("Exception occured in PageStoreUtil.sendProxyToPageEJB: PublicHealthCaseUid: " + form.getPageClientVO().getOldPageProxyVO().getPublicHealthCaseVO().getThePublicHealthCaseDT().getPublicHealthCaseUid() + ", " + e.getMessage(), e);
        	throw new NEDSSAppException(e.getMessage(), e);
		}
    }

    public static InvestigationProxyVO getInvestigationProxy(Long investigationUid, HttpServletRequest request)
            throws NEDSSAppConcurrentDataException, Exception
    {
        HttpSession session = request.getSession();
        MainSessionCommand msCommand = null;
        InvestigationProxyVO proxyVO = null;
        
        try {
        String sBeanJndiName = JNDINames.INVESTIGATION_PROXY_EJB;
        MainSessionHolder holder = new MainSessionHolder();
        msCommand = holder.getMainSessionCommand(session);
        ArrayList<?> resultUIDArr = new ArrayList<Object>();

        Object[] oParams = { investigationUid };
        String sMethod = "getInvestigationProxy";
        resultUIDArr = msCommand.processRequest(sBeanJndiName, sMethod, oParams);

        if ((resultUIDArr != null) && (resultUIDArr.size() > 0))
        {
        	proxyVO = (InvestigationProxyVO) resultUIDArr.get(0);
        }
     	}catch (Exception e) {
     		logger.fatal("Exception occured in PageStoreUtil.getInvestigationProxy: investigationUid: " + investigationUid + ", " + e.getMessage(), e);
        	throw new NEDSSAppException(e.getMessage(), e);
    	}        
        return proxyVO;
    }
	/**
     * editGenericHandler - called from PageAction to process Interviews and
     * other generic pages.
     * 
     * @param form
     * @param req
     * @throws Exception
     */
    public static PageProxyVO editGenericHandler(PageForm form, HttpServletRequest req) throws Exception
    {
        try {
			PageProxyVO proxyVO = null;
			proxyVO = PageCreateHelper.editGenericHandler(form, req);
			if (form.getErrorList().size() == 0)
			{
			    Long actUid = sendProxyToPageEJB(proxyVO, req, form.getBusinessObjectType());
			    if (actUid != null)
			        req.setAttribute("actUid", actUid.toString());
			}
			return proxyVO;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			logger.fatal("Exception occured in PageStoreUtil.editGenericHandler: " + e.getMessage(), e);
        	throw new NEDSSAppException(e.getMessage(), e);
		}
    }

    /**
     * viewHandler
     * 
     * @param form
     * @param req
     * @throws Exception
     */
    public static void viewHandler(PageForm form, HttpServletRequest req) throws Exception
    {
        // PageViewHelper.viewHandler(form, req);
    }

    /**
     * setEntitiesForEdit creates Participations' and NBSActEntities' with types
     * of PRVs and ORGs associated with Varicella
     * 
     * @param proxyVO
     * @param form
     * @param revisionPatientUID
     * @param userId
     * @param request
     * @throws NEDSSAppException
     */
    private static void setEntitiesForCreate(PageProxyVO proxyVO, PageForm form, int revisionPatientUID, String userId,
            HttpServletRequest request) throws NEDSSAppException
    {

        String stdFieldFollowupUid = form.getAttributeMap().get(NEDSSConstants.FOLLOWUP_INVESTIGATOR + "Uid") == null ? ""
                : (String) form.getAttributeMap().get(NEDSSConstants.FOLLOWUP_INVESTIGATOR + "Uid");
        String stdInterviewerUid = form.getAttributeMap().get(NEDSSConstants.INTERVIEW_INVESTIGATOR + "Uid") == null ? ""
                : (String) form.getAttributeMap().get(NEDSSConstants.INTERVIEW_INVESTIGATOR + "Uid");

        try
        {
            // ///////////////////////////////////////////////////////////////////////////////
            // Iterate through the Case Participation Types to see if they are
            // present
            // and if they are put them in the attribute map
            // /////////////////////////////////////////////////////////////////////////////// 
            
            TreeMap<Object, Object> participationTypeCaseMap = CachedDropDowns.getParticipationTypeList();

            Iterator parTypeIt = participationTypeCaseMap.values().iterator();
            while (parTypeIt.hasNext())
            {
                ParticipationTypeVO parTypeVO = (ParticipationTypeVO) parTypeIt.next();
                String entityUid = form.getAttributeMap().get(parTypeVO.getQuestionIdentifier() + "Uid") == null ? ""
                        : (String) form.getAttributeMap().get(parTypeVO.getQuestionIdentifier() + "Uid");

                // check couple of exceptions for STD setting initial
                // investigators..
                if (parTypeVO.getQuestionIdentifier() != null
                        && parTypeVO.getQuestionIdentifier().equalsIgnoreCase(
                                NEDSSConstants.INITIAL_FOLLOWUP_INVESTIGATOR)
                        && (entityUid == null || entityUid.isEmpty()) && !stdFieldFollowupUid.isEmpty())
                    entityUid = stdFieldFollowupUid;
                if (parTypeVO.getQuestionIdentifier() != null
                        && parTypeVO.getQuestionIdentifier().equalsIgnoreCase(
                                NEDSSConstants.INITIAL_INTERVIEW_INVESTIGATOR)
                        && (entityUid == null || entityUid.isEmpty()) && !stdInterviewerUid.isEmpty())
                    entityUid = stdInterviewerUid;

                PageCreateHelper._setEntitiesForCreate(proxyVO, form, revisionPatientUID, userId, request, entityUid,
                        parTypeVO.getTypeCd(), parTypeVO.getSubjectClassCd());
            }
        }
        catch (NEDSSAppException e)
        {
            e.printStackTrace();
            logger.fatal("Exception occured in PageStoreUtil.setEntitiesForCreate: userId: " + userId + ", revisionPatientUID: " + revisionPatientUID + ", PublicHealthCaseUid: " + form.getPageClientVO().getOldPageProxyVO().getPublicHealthCaseVO().getThePublicHealthCaseDT().getPublicHealthCaseUid() + ", " + e.getMessage(), e);
        	throw new NEDSSAppException(e.getMessage(), e);
        }

    }

    /**
     * setEntitiesForEdit creates Participations' and NBSActEntities' with types
     * of PRVs and ORGs associated with Varicella
     * 
     * @param proxyVO
     * @param form
     * @param phcVO
     * @param userId
     * @param request
     */
    private static void setEntitiesForEdit(PageActProxyVO proxyVO, PageForm form, PublicHealthCaseVO phcVO,
            String userId, HttpServletRequest request) throws NEDSSAppException
    {
        try {
			String stdFieldFollowupUid = form.getAttributeMap().get(NEDSSConstants.FOLLOWUP_INVESTIGATOR + "Uid") == null ? ""
			        : (String) form.getAttributeMap().get(NEDSSConstants.FOLLOWUP_INVESTIGATOR + "Uid");
			String stdInterviewerUid = form.getAttributeMap().get(NEDSSConstants.INTERVIEW_INVESTIGATOR + "Uid") == null ? ""
			        : (String) form.getAttributeMap().get(NEDSSConstants.INTERVIEW_INVESTIGATOR + "Uid");

			String programArea = proxyVO.getPublicHealthCaseVO().getThePublicHealthCaseDT().getProgAreaCd();
			NBSSecurityObj nbsSecurityObj = (NBSSecurityObj) request.getSession().getAttribute("NBSSecurityObject");
			Long providerUid = nbsSecurityObj.getTheUserProfile().getTheUser().getProviderUid();

			PropertyUtil properties = PropertyUtil.getInstance();
			StringTokenizer st2 = new StringTokenizer(properties.getSTDProgramAreas(), ",");
			if (st2 != null)
			{
			    while (st2.hasMoreElements())
			    {
			        if (st2.nextElement().equals(programArea))
			        {
			            proxyVO.setSTDProgramArea(true);
			            break;
			        }
			    }
			}
			// ///////////////////////////////////////////////////////////////////////////////
			// Iterate through the Case Participation Types to see if they are
			// present
			// and if they are put them in the attribute map
			// ///////////////////////////////////////////////////////////////////////////////
			 
			TreeMap<Object, Object> participationTypeCaseMap = CachedDropDowns.getParticipationTypeList();
			Iterator parTypeIt = participationTypeCaseMap.values().iterator();
			while (parTypeIt.hasNext())
			{
			    ParticipationTypeVO parTypeVO = (ParticipationTypeVO) parTypeIt.next();
			    String entityUid = (form.getAttributeMap().get(parTypeVO.getQuestionIdentifier() + "Uid") == null || form.getAttributeMap().get(parTypeVO.getQuestionIdentifier() + "Uid").equals("")) ? null
			            : (String) form.getAttributeMap().get(parTypeVO.getQuestionIdentifier() + "Uid");

			    // check couple of exceptions for STD setting initial
			    // investigators..
			    if (parTypeVO.getQuestionIdentifier() != null
			            && parTypeVO.getQuestionIdentifier().equalsIgnoreCase(NEDSSConstants.INITIAL_FOLLOWUP_INVESTIGATOR)
			            && (entityUid == null || entityUid.isEmpty()) && !stdFieldFollowupUid.isEmpty())
			        entityUid = stdFieldFollowupUid;
			    if (parTypeVO.getQuestionIdentifier() != null
			            && parTypeVO.getQuestionIdentifier()
			                    .equalsIgnoreCase(NEDSSConstants.INITIAL_INTERVIEW_INVESTIGATOR)
			            && (entityUid == null || entityUid.isEmpty()) && !stdInterviewerUid.isEmpty())
			        entityUid = stdInterviewerUid;

			    PageCreateHelper.createOrDeleteParticipation(entityUid, form, proxyVO, parTypeVO.getTypeCd(),
			            parTypeVO.getSubjectClassCd(), userId, providerUid);
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			logger.fatal("Exception occured in PageStoreUtil.setEntitiesForEdit: userId: " + userId + ", PublicHealthCaseUid: " + form.getPageClientVO().getOldPageProxyVO().getPublicHealthCaseVO().getThePublicHealthCaseDT().getPublicHealthCaseUid() + ", " + e.getMessage(), e);
        	throw new NEDSSAppException(e.getMessage(), e);
		}

    }

    public static void createActRelationshipForDoc(String sCurrentTask, PageProxyVO proxyVO, HttpServletRequest request) throws NEDSSAppException
    {
        try {
			HttpSession session = request.getSession();
			Object DSDocumentUID = NBSContext.retrieve(session, NBSConstantUtil.DSDocumentUID);
			ActRelationshipDT actDoc = new ActRelationshipDT();
			actDoc.setItNew(true);
			actDoc.setSourceActUid(new Long(DSDocumentUID.toString()));
			actDoc.setSourceClassCd(NEDSSConstants.ACT_CLASS_CD_FOR_DOC);
			actDoc.setStatusCd(NEDSSConstants.STATUS_ACTIVE);
			actDoc.setTargetActUid(((PageActProxyVO) proxyVO).getPublicHealthCaseVO().getThePublicHealthCaseDT()
			        .getPublicHealthCaseUid());
			actDoc.setTargetClassCd(NEDSSConstants.CASE);
			actDoc.setRecordStatusCd(NEDSSConstants.ACTIVE);
			actDoc.setTypeCd(NEDSSConstants.DocToPHC);
			if (((PageActProxyVO) proxyVO).getPublicHealthCaseVO().getTheActRelationshipDTCollection() == null)
			{
			    Collection<Object> coll = new ArrayList<Object>();
			    coll.add(actDoc);
			    ((PageActProxyVO) proxyVO).getPublicHealthCaseVO().setTheActRelationshipDTCollection(coll);
			}
			else
			    ((PageActProxyVO) proxyVO).getPublicHealthCaseVO().getTheActRelationshipDTCollection().add(actDoc);
		} catch (NumberFormatException e) {
			// TODO Auto-generated catch block
			logger.fatal("Exception occured in PageStoreUtil.createActRelationshipForDoc: sCurrentTask: " + sCurrentTask + ", PublicHealthCaseUid: " + proxyVO.getPublicHealthCaseVO().getThePublicHealthCaseDT().getPublicHealthCaseUid() + ", " + e.getMessage(), e);
        	throw new NEDSSAppException(e.getMessage(), e);
		}
    }

    public static void translateAnswerDTsToCaseAnswerDTs(PageProxyVO pageProxyVO) throws NEDSSAppException
    {
        PageActProxyVO proxyVO = (PageActProxyVO) pageProxyVO;
        Map<Object, Object> answerMap = proxyVO.getPageVO().getAnswerDTMap();
        Map<Object, Object> pamAnswerMap = proxyVO.getPageVO().getPamAnswerDTMap();
        Map<Object, Object> repeatingAnswerMap = proxyVO.getPageVO().getPageRepeatingAnswerDTMap();
        try
        {
            if (answerMap != null && answerMap.keySet().size() > 0)
            {
                Iterator<Object> ite = answerMap.keySet().iterator();
                while (ite.hasNext())
                {
                    Object key = ite.next();
                    // single entry fields
                    if (answerMap.get(key) instanceof NbsAnswerDT)
                    {
                        answerMap.put(key, new NbsCaseAnswerDT((NbsAnswerDT) answerMap.get(key)));
                    }
                    // Multi-selects
                    else if (answerMap.get(key) instanceof ArrayList<?>)
                    {
                        ArrayList<?> aDTList = (ArrayList<?>) answerMap.get(key);
                        Collection<NbsCaseAnswerDT> caDTList = new ArrayList<NbsCaseAnswerDT>();
                        for (Object ansDT : aDTList)
                        {
                            if (ansDT instanceof NbsAnswerDT)
                                caDTList.add(new NbsCaseAnswerDT((NbsAnswerDT) ansDT));
                            else
                            {
                                logger.debug("answerMap: Answer is not AnswerDT Instance");
                            }
                        }
                        answerMap.put(key, caDTList);
                    }
                    else
                    {
                        logger.debug("answerMap: Answer is not AnswerDT or ArrayList<?> Instance");
                    }
                }
            }
            if (pamAnswerMap != null && pamAnswerMap.keySet().size() > 0)
            {
                Iterator<Object> ite = pamAnswerMap.keySet().iterator();
                while (ite.hasNext())
                {
                    Object key = ite.next();
                    // single entry fields
                    if (pamAnswerMap.get(key) instanceof NbsAnswerDT)
                    {
                        pamAnswerMap.put(key, new NbsCaseAnswerDT((NbsAnswerDT) pamAnswerMap.get(key)));
                    }
                    // Multi-selects
                    else if (pamAnswerMap.get(key) instanceof ArrayList<?>)
                    {
                        ArrayList<?> aDTList = (ArrayList<?>) pamAnswerMap.get(key);
                        Collection<NbsCaseAnswerDT> caDTList = new ArrayList<NbsCaseAnswerDT>();
                        for (Object ansDT : aDTList)
                        {
                            if (ansDT instanceof NbsAnswerDT)
                                caDTList.add(new NbsCaseAnswerDT((NbsAnswerDT) ansDT));
                            else
                            {
                                logger.debug("pamAnswerMap: Answer is not AnswerDT Instance");
                            }
                        }
                        pamAnswerMap.put(key, caDTList);
                    }
                    else
                    {
                        logger.debug("pamAnswerMap: Answer is not AnswerDT or ArrayList<?> Instance");
                    }
                }
            }
            if (repeatingAnswerMap != null && repeatingAnswerMap.keySet().size() > 0)
            {
                Iterator<Object> ite = repeatingAnswerMap.keySet().iterator();
                Map<Object, Object> returnedMap = new HashMap<Object, Object>();
                while (ite.hasNext())
                {
                    Object key = ite.next();
                    if (repeatingAnswerMap.get(key) instanceof ArrayList<?>)
                    {
                        ArrayList<?> aDTList = (ArrayList<?>) repeatingAnswerMap.get(key);
                        Collection<NbsCaseAnswerDT> caDTList = new ArrayList<NbsCaseAnswerDT>();
                        for (Object ansDT : aDTList)
                        {
                            if (ansDT instanceof NbsAnswerDT)
                                caDTList.add(new NbsCaseAnswerDT((NbsAnswerDT) ansDT));
                        }
                        returnedMap.put(key, caDTList);
                    }
                    else if (repeatingAnswerMap.get(key) instanceof NbsAnswerDT)
                    {
                        returnedMap.put(key, new NbsCaseAnswerDT((NbsAnswerDT) repeatingAnswerMap.get(key)));
                    }
                    else
                    {
                        logger.debug("repeatingAnswerMap: Answer is not AnswerDT or ArrayList<?> Instance");
                    }
                }
                proxyVO.getPageVO().setPageRepeatingAnswerDTMap(returnedMap);

            }
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
            logger.fatal("Exception occured in PageStoreUtil.translateAnswerDTsToCaseAnswerDTs: PublicHealthCaseUid: " + proxyVO.getPublicHealthCaseVO().getThePublicHealthCaseDT().getPublicHealthCaseUid() + ", " + ex.getMessage(), ex);
        	throw new NEDSSAppException(ex.getMessage(), ex);
        }

    }

    /**
     * @param form
     * @param req
     * @return
     * @throws Exception
     */
    public static PageProxyVO mergeHandler(PageForm form, HttpServletRequest req) throws Exception
    {
        try {
			PageProxyVO proxyVO = null;
			NBSSecurityObj nbsSecurityObj = (NBSSecurityObj) req.getSession().getAttribute("NBSSecurityObject");
			String userId = nbsSecurityObj.getTheUserProfile().getTheUser().getEntryID();
			proxyVO = PageCreateHelper.editHandler(form, req);
			
			if (form.getErrorList().size() == 0)
			{
				 PageActProxyVO survivorProxyVOOld = ((PageActProxyVO) form.getPageClientVO().getOldPageProxyVO());
				 PageActProxyVO supersededProxyVO = (PageActProxyVO) form.getPageClientVO2().getOldPageProxyVO();

			    PageActProxyVO survivorProxyVO = (PageActProxyVO) proxyVO;
			    
				PublicHealthCaseVO survivorVO = ((PageActProxyVO) survivorProxyVO)
	                    .getPublicHealthCaseVO();
				
			    setEntitiesForEdit((PageActProxyVO) proxyVO, form, survivorVO, userId, req);
			    Long providerUid = nbsSecurityObj.getTheUserProfile().getTheUser().getProviderUid();
			    MessageLogUtil.createMessageForUpdatedComments(form, (PageActProxyVO) proxyVO, providerUid);
			    setActRelationshipForMerge((PageActProxyVO)proxyVO, form, req);

				String localIdSuperseded=(String)form.getAttributeMap2().get("caseLocalId");
				String localIdSurvivor=(String)form.getAttributeMap().get("caseLocalId");
				
				survivorVO.getThePublicHealthCaseDT().setItDirty(true); 
				survivorProxyVOOld.setItDirty(true);
				/*
				 * This is the code that sets the proxyVO as merge case so that same record is not updated multiple times
				 */
				((PageActProxyVO) proxyVO).setMergeCase(true);
				supersededProxyVO.setMergeCase(true);
				
			    sendProxyToPageEJBMerge(proxyVO,supersededProxyVO,survivorProxyVOOld, localIdSurvivor,localIdSuperseded, req);

			}
			return proxyVO;
		} catch (Exception ex) {
			// TODO Auto-generated catch block
			logger.fatal("Exception occured in PageStoreUtil.mergeHandler: PublicHealthCaseUid: " + form.getPageClientVO().getOldPageProxyVO().getPublicHealthCaseVO().getThePublicHealthCaseDT().getPublicHealthCaseUid() + ", " + ex.getMessage(), ex);
        	throw new NEDSSAppException(ex.getMessage(), ex);
		}
    }
    
    private static void setActRelationshipForMerge(PageActProxyVO survivorProxyVO, PageForm form, HttpServletRequest req) throws NEDSSAppException{
    	try{
    		PageActProxyVO supersededProxyVO = (PageActProxyVO) form.getPageClientVO2().getOldPageProxyVO();
    		
    		for (Iterator<Object> anIterator = supersededProxyVO.getPublicHealthCaseVO().getTheActRelationshipDTCollection().iterator(); anIterator.hasNext();) {
				ActRelationshipDT actRelationshipDT = (ActRelationshipDT) anIterator.next();
				boolean found = false;
				if(actRelationshipDT!=null && !NEDSSConstants.ACT106_TYP_CD.equals(actRelationshipDT.getTypeCd())){
					
					for (Iterator<Object> anIteratorNew = survivorProxyVO.getPublicHealthCaseVO()
							.getTheActRelationshipDTCollection().iterator(); anIteratorNew.hasNext();) {
						
						ActRelationshipDT actRelationshipDTNew = (ActRelationshipDT) anIteratorNew.next();
					if(actRelationshipDT.getSourceActUid().equals(actRelationshipDTNew.getSourceActUid()) && actRelationshipDT.getSourceClassCd().equals(actRelationshipDTNew.getSourceClassCd())){
						 found = true;
						 break;
						}
					}
					if(!found){
						actRelationshipDT.setTargetActUid(survivorProxyVO.getPublicHealthCaseVO().getThePublicHealthCaseDT().getPublicHealthCaseUid());
						survivorProxyVO.getPublicHealthCaseVO().getTheActRelationshipDTCollection().add(actRelationshipDT);
						actRelationshipDT.setItNew(true);
						actRelationshipDT.setItDirty(false);
						actRelationshipDT.setItDelete(false);
					}
				
				}
    		}
    		survivorProxyVO.getPublicHealthCaseVO().setItDirty(true);

    	}catch(Exception ex){
    		logger.fatal("Exception occured in PageStoreUtil.setActRelationshipForMerge: PublicHealthCaseUid: " + form.getPageClientVO().getOldPageProxyVO().getPublicHealthCaseVO().getThePublicHealthCaseDT().getPublicHealthCaseUid() + ", " + ex.getMessage(), ex);
        	throw new NEDSSAppException(ex.getMessage(), ex);
    	}
    }
    
}
