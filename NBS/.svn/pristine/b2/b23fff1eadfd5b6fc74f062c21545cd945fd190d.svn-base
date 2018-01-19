package gov.cdc.nedss.webapp.nbs.action.deduplication;


import gov.cdc.nedss.systemservice.ejb.mainsessionejb.bean.*;
import gov.cdc.nedss.systemservice.nbscontext.*;
import gov.cdc.nedss.systemservice.nbssecurity.*;
import gov.cdc.nedss.systemservice.util.MainSessionHolder;
import gov.cdc.nedss.util.*;
import gov.cdc.nedss.webapp.nbs.form.person.*;
import gov.cdc.nedss.entity.person.util.DisplayPersonList;
import gov.cdc.nedss.entity.person.vo.*;
import gov.cdc.nedss.entity.person.dt.*;
import gov.cdc.nedss.entity.entityid.dt.*;

import java.text.SimpleDateFormat;

import gov.cdc.nedss.locator.dt.*;
//for the old way using entity
import gov.cdc.nedss.util.PropertyUtil;
import gov.cdc.nedss.webapp.nbs.logicsheet.helper.CachedDropDownValues;
import gov.cdc.nedss.webapp.nbs.util.DeDuplicationQueueHelper;
import gov.cdc.nedss.deduplication.vo.DeduplicationPatientMergeVO;
import gov.cdc.nedss.exception.NEDSSAppException;
import gov.cdc.nedss.deduplication.exception.NEDSSDeduplicationException;
import gov.cdc.nedss.webapp.nbs.action.util.ErrorMessageHelper;







import java.io.*;
import java.util.*;
import java.sql.Timestamp;

import javax.servlet.*;
import javax.servlet.http.*;

import org.apache.struts.action.*;

import gov.cdc.nedss.webapp.nbs.action.person.util.PersonUtil;


/**
 * This class is for preparing the Person Search Results page
 * for display of search results.
 */
public class MergeCandidateListLoad
    extends Action
{

    static final LogUtils logger = new LogUtils(MergeCandidateListLoad.class.getName());
    /**
     * This is the constructor for the PersonSearchResultsLoad
     * class
     */
    public MergeCandidateListLoad()
    {
    }

    /**
     * This method is controls the execution of the
     * PersonSearchResultsLoad logic, and dictates
     * the navigation.
     *
     * @param mapping ActionMapping
     * @param form ActionForm
     * @param request HttpServletRequest
     * @param response HttpServletResponse
     * @exception IOException
     * @exception ServletException
     * @return ActionForward
     */
    public ActionForward execute(ActionMapping mapping, ActionForm form,
    		HttpServletRequest request,
    		HttpServletResponse response)
    				throws IOException, ServletException
    				{

    	ArrayList<?> patientSrchResultVO = new ArrayList<Object> ();
    	HttpSession session = request.getSession(false);
    	NBSSecurityObj secObj = (NBSSecurityObj)session.getAttribute(
    			"NBSSecurityObject");
    	String contextAction = request.getParameter("ContextAction");

    	if (contextAction == null)
    		contextAction = (String)request.getAttribute("ContextAction");
    	ErrorMessageHelper.setErrMsgToRequest(request, "PS209");

    	if (contextAction.equalsIgnoreCase("Submit") ||
    			contextAction.equalsIgnoreCase("ReturnToMergeCandidateList")|| contextAction.equalsIgnoreCase("GlobalMP_SystemIndentified")||contextAction.equalsIgnoreCase("NoMerge"))
    	{

    		TreeMap<Object,Object> tm = NBSContext.getPageContext(session, "PS209",
    				contextAction);
    		String sCurrTask = NBSContext.getCurrentTask(session);
    		NBSContext.lookInsideTreeMap(tm);
    		request.setAttribute("ContextTaskName", sCurrTask);
    		// add button security
    		boolean meregeButton = secObj.getPermission(NBSBOLookup.PATIENT,
    				NBSOperationLookup.MERGE);
    		request.setAttribute("mergeButton", String.valueOf(meregeButton));


    		if(sCurrTask.equals("MergeCandidateList1"))
    		{
    			request.setAttribute("refineSearchHref",
    					"/nbs/" + sCurrTask +
    					".do?ContextAction=" +
    					tm.get("RefineSearch")+"&Mode1=ManualMerge");
    			request.setAttribute("formHref", "/nbs/" + sCurrTask + ".do");
    			request.setAttribute("newSearchHref",
    					"/nbs/" + sCurrTask +
    					".do?ContextAction=" + tm.get("NewSearch")+"&Mode1=ManualMerge");


    			request.setAttribute("Cancel", "/nbs/" + sCurrTask +".do?ContextAction="+tm.get("Cancel"));
    			ArrayList<?> personList = (ArrayList<?>) NBSContext.retrieve(session, "DSSearchResults");
    			if (personList != null) {
    				DisplayPersonList displayPersonList = (DisplayPersonList)personList.get(0); 
    				if (displayPersonList.getList().size() > 1)
    					personList = sortPersonListOnPersonLocalId(personList);
    			}
    			request.setAttribute("DSSearchResults", NBSContext.retrieve(session, "DSSearchResults"));
    			request.setAttribute("NoOfRows",new Integer(PropertyUtil.getInstance().getNumberOfRows()).toString());
    			try {

    				PatientSearchVO psVO = (PatientSearchVO) NBSContext.retrieve(session,
    						"DSSearchCriteria");
    				String scString = this.buildSearchCriteriaString(psVO);
    				request.setAttribute("DSSearchCriteriaString", scString);
    			}
    			catch (Exception e)
    			{
    				session.setAttribute("error",
    						"DSSearchCriteria not available in Object Store");
    				e.printStackTrace();
    			}

    			//request.setAttribute("DSSearchResults",session.getAttribute("PerMerge"));
    			return mapping.findForward("XSP");
    		}

    		if(sCurrTask.equals("MergeCandidateList2"))
    		{

    			request.setAttribute("formHref", "/nbs/" + sCurrTask + ".do");
    			request.setAttribute("Cancel", "/nbs/" + sCurrTask +".do?ContextAction="+tm.get("Cancel"));
    			HashMap<Object,Object> dedupAvailableQueue = DeDuplicationQueueHelper.
    					getDedupAvailableQueue();
    			if (dedupAvailableQueue == null)
    			{
    				logger.fatal("The dedupAvailableQueue is null");
    				request.setAttribute("groupsAvailableToMerge", new Integer(0));
    				return mapping.findForward("XSP");

    			}
    			else
    			{
    				if (dedupAvailableQueue.isEmpty())
    				{
    					//No groups to merge, we need to get out of here...
    					logger.fatal("The dedupAvailableQueue is empty");
    					request.setAttribute("groupsAvailableToMerge", new Integer(0));
    					return mapping.findForward("XSP");
    				}
    				//Pop off the top one because it is being taken...
    				Collection<Object>  keySetCollection  = new ArrayList<Object>(dedupAvailableQueue.keySet());

    				Iterator<Object>  iterator = null;
    				Object key = null;
    				Object value = null;
    				Collection<?>  collection = null;

    				//Use sync. since we are using HashMap's...
    				synchronized (this)
    				{
    					iterator = keySetCollection.iterator();

    					//before removing it from DeDupAvailableQueue get the PersonVOCollection  belonging to this
    					//groupNumber so that we can put the same in DeDupTakenQueue...

    					while (collection == null && iterator.hasNext())
    					{
    						key = iterator.next();
    						try
    						{
    							collection = getPersonVOsGroupedSimilar(session,
    									Long.valueOf(key.toString()));
    						}
    						catch (NEDSSAppException nae)
    						{
    							if (nae.getErrorCd().equals("ERR109"))
    							{
    								logger.fatal("ERROR ,  ", nae);
    								//send to DataConcurrent error message page
    								return ErrorMessageHelper.redirectToPage("PS158",
    										nae.getErrorCd());
    							}
    							//send to generic error page
    							return ErrorMessageHelper.redirectToPage("PS175", null);

    						}
    						catch (Exception e)
    						{
    							//send to generic error page
    							return ErrorMessageHelper.redirectToPage("PS175", null);
    						}
    						//this will occur when DeDuplicationException is caught in getPersonVosGroupedSimilar(...)
    						if (collection == null)
    							dedupAvailableQueue.remove(key);

    					}

    					//If either one condition is met we need to get out...
    					if (collection == null && iterator.hasNext() == false)
    					{
    						logger.fatal("The dedupAvailableQueue is empty");
    						request.setAttribute("groupsAvailableToMerge", new Integer(0));
    						return mapping.findForward("XSP");
    					}
    					patientSrchResultVO = buildPersonSummaryVOCollection(collection,request);

    					dedupAvailableQueue.remove(key);

    					DeDuplicationQueueHelper.getDedupTakenQueue().put(key, ((ArrayList<?> )collection).get(0));
    					DeDuplicationQueueHelper.getDedupSessionQueue().put(session.getId(),
    							key);
    				}

    				//                    request.setAttribute("candidateQueueCreationDate",
    				//                                        Timestamp.valueOf(value.toString()));
    				request.setAttribute("groupsAvailableToMerge",
    						new Integer(dedupAvailableQueue.size()));
    				if (session.getAttribute("DQH") == null)
    				{
    					session.setAttribute("DQH", new DeDuplicationQueueHelper());
    					DeduplicationPatientMergeVO dedupPatientMergeVO = (DeduplicationPatientMergeVO)((ArrayList<?> )((ArrayList<?> )collection).get(0)).get(0);
    					session.setAttribute("groupDate", dedupPatientMergeVO.getMPR().getThePersonDT().getGroupTime());
    				}
    				NBSContext.store(session, "DSSearchResults", patientSrchResultVO);
    				request.setAttribute("DSSearchResults",
    						NBSContext.retrieve(session,
    								"DSSearchResults"));
    				return mapping.findForward("XSP");
    			}
    		}
    	}

    	return mapping.findForward("XSP");
    				}
	/**
     * Given a group number as parameter returns a Collection<Object>  of PersonVOs Associated with that group number.
     * @param session
     * @param groupNumber
     * @return Collection
     */
    private Collection<?>  getPersonVOsGroupedSimilar(HttpSession session, Long groupNumber) throws Exception
    {
        //Collection<Object>  groupNumberCollection  = new ArrayList<Object> ();

        //groupNumberCollection.add(groupNumber);

        return useProxyToGetSimilarlyGroupedPersons(groupNumber, session);

    }

    /**
      * Retrieves the personVOs of the persons to be merged.
       * @J2EE_METHOD  --  getPersonVOs
       * @param groupNumberCollection        the Collection
       * @return session    the  HttpSession
       * @return secObj    the  NBSSecurityObj
      **/
      private Collection<?>  useProxyToGetSimilarlyGroupedPersons(Long groupNumber, HttpSession session) throws Exception
      {
    	  /**
    	   * Call the mainsessioncommand
    	   */
    	  MainSessionCommand msCommand = null;
    	  try
    	  {
    		  String sBeanJndiName = JNDINames.DEDUPLICATION_PROCESSOR_EJB;
    		  String sMethod = "getSimilarGroup";

    		  //##!! System.out.println("Number of elements in UID collection that is passed to EntityProxy.getPersons is " +	sysIdCollection.size());

    		  Object[] oParams = new Object[]{groupNumber};

    		  MainSessionHolder holder = new MainSessionHolder();
    		  msCommand = holder.getMainSessionCommand(session);

    		  return msCommand.processRequest(sBeanJndiName, sMethod, oParams);
    	  }
    	  catch (Exception e)
    	  {
    		  if(e instanceof NEDSSDeduplicationException)
    		  {
    			  return null;
    		  }
    		  if(e.toString().indexOf("NEDSSDeduplicationException") != -1)
    		  {
    			  return null;
    		  }
    		  throw e;
    	  }
    	  finally
    	  {
    		  msCommand = null;
    	  }

      }

    /**
     * Builds a Collection<Object>  of PersonSummaryVO's given a PersonVO Collection
     * @param collection
     */
    private ArrayList<Object> buildPersonSummaryVOCollection(Collection<?> collection,HttpServletRequest request)
    {
        ArrayList<?> arrayList = (ArrayList<?> )collection;
        PersonVO personVO;

        ArrayList<Object> similarGroupArrayList = new ArrayList<Object>((ArrayList<?> )arrayList.get(0));

        for(int i=0; i < similarGroupArrayList.size(); i++)
        {
            DeduplicationPatientMergeVO deduplicationPatientMergeVO = (DeduplicationPatientMergeVO)similarGroupArrayList.get(i);
            similarGroupArrayList.set(i, buildPersonSerachResultVO(deduplicationPatientMergeVO,request));
        }

        return sortPatientSrchResultVOList(similarGroupArrayList); //sort on Patient ID ascending - oldest first
    }
    /**
     * Builds a PersonSummaryVO given a PersonVO
     * @param inPersonVO
     * @return PersonSummaryVO
     */
    private PatientSrchResultVO buildPersonSerachResultVO(DeduplicationPatientMergeVO inDeduplicationPatientMergeVO,HttpServletRequest request)
    {
    	PersonVO inPersonVO = (PersonVO)inDeduplicationPatientMergeVO.getMPR();

    	PatientSrchResultVO patientSrchResultVO = new PatientSrchResultVO();

    	try {
    		Collection<Object>  col = inDeduplicationPatientMergeVO.getRevisionCollection();
    		CachedDropDownValues cache = new CachedDropDownValues();
    		TreeMap<?,?> tMap = cache.getCodedValuesAsTreeMap("SEX");
    		tMap = cache.reverseMap(tMap);
    		Collection<Object>  revCollection  = new ArrayList<Object> ();
    		if(DeDuplicationQueueHelper.getLastBatchProcessTime() != null)
    			request.setAttribute("candidateQueueCreationDate",this.formatDate(DeDuplicationQueueHelper.getLastBatchProcessTime()));
    		else
    			request.setAttribute("candidateQueueCreationDate", "");
    		if(col!=null)
    		{
    			Iterator<Object>  ite = col.iterator();
    			while(ite.hasNext())
    			{
    				PersonVO revision = (PersonVO) ite.next();
    				PatientRevisionSrchResultVO patientRevisionSrchResultVO = new
    						PatientRevisionSrchResultVO();
    				patientRevisionSrchResultVO.setPersonNameColl(this.updateNmCdWithDescription(revision.
    						getThePersonNameDTCollection()));
    				patientRevisionSrchResultVO.setPersonIdColl(this.updateTypeCdWithDescription(revision.
    						getTheEntityIdDTCollection()));
    				patientRevisionSrchResultVO.setPersonLocatorsColl(this.updateLocUseCdWithDescription(revision.
    						getTheEntityLocatorParticipationDTCollection()));
    				if(revision.getThePersonDT().getCurrSexCd()!=null)
    					patientRevisionSrchResultVO.setCurrentSex((String) tMap.get	(revision.getThePersonDT().
    							getCurrSexCd()));
    				patientRevisionSrchResultVO.setPersonDOB(formatDate(revision.
    						getThePersonDT().getBirthTime()));
    				revCollection.add(patientRevisionSrchResultVO);
    			}
    		}
    		PersonDT personDT = inPersonVO.getThePersonDT();
    		patientSrchResultVO.setRevisionColl(revCollection);
    		patientSrchResultVO.setPersonNameColl(this.updateNmCdWithDescription(inPersonVO.getThePersonNameDTCollection()));
    		patientSrchResultVO.setPersonLocatorsColl(this.updateLocUseCdWithDescription(inPersonVO.getTheEntityLocatorParticipationDTCollection()));
    		patientSrchResultVO.setPersonIdColl(this.updateTypeCdWithDescription(inPersonVO.getTheEntityIdDTCollection()));
    		if(personDT.getCurrSexCd() != null)  //added bcoz the merge similar patient algo. got change and now the patient current sex might be null
    		patientSrchResultVO.setCurrentSex((String) tMap.get(personDT.getCurrSexCd()));
    		patientSrchResultVO.setPersonDOB(formatDate(personDT.getBirthTime()));
    		patientSrchResultVO.setPersonUID(personDT.getPersonUid());
    		patientSrchResultVO.setPersonLocalID(personDT.getLocalId());
    	} catch (Exception ex) {
    		logger.error("Exception encountered in CompareMergeLoad.buildPersonSearchResultVO() " + ex.getMessage());
    	}
    	return patientSrchResultVO;
    }
    
     private Collection<Object>  updateNmCdWithDescription(Collection<Object> col)
     {
    	 Collection<Object>  nameCollection  = new ArrayList<Object> ();
    	 if(col!=null)
    	 {
    		 Iterator<Object>  ite = col.iterator();

    		 while(ite.hasNext())
    		 {
    			 CachedDropDownValues cache = new CachedDropDownValues();
    			 TreeMap<?,?> map = cache.getCodedValuesAsTreeMap("P_NM_USE");
    			 try{
    				 PersonNameDT  pnDT = (PersonNameDT)((PersonNameDT)ite.next()).deepCopy();
    				 map = cache.reverseMap(map);
    				 pnDT.setNmUseCd((String) map.get(pnDT.getNmUseCd()));
    				 nameCollection.add(pnDT);
    			 }
    			 catch(Exception e)
    			 {
    				 logger.info(e.toString());
    			 }

    		 }
    	 }
    	 return nameCollection;
     }
     private Collection<Object>  updateLocUseCdWithDescription(Collection<Object> col)
     {
       Collection<Object>  lucCollection  = new ArrayList<Object> ();
       if(col!=null)
       {
        Iterator<Object>  ite = col.iterator();

         while(ite.hasNext())
         {
           CachedDropDownValues cache = new CachedDropDownValues();
           TreeMap<?,?> map = cache.getCodedValuesAsTreeMap("EL_TYPE");
           TreeMap<?,?> map1 = cache.getCodedValuesAsTreeMap("EL_USE");
             try
             {
                   EntityLocatorParticipationDT  elpDT = (EntityLocatorParticipationDT)((EntityLocatorParticipationDT)ite.next()).deepCopy();
                   map = cache.reverseMap(map);
                   map1 = cache.reverseMap(map1);
                   elpDT.setUseCd( (String) map1.get(elpDT.getUseCd()));
                   elpDT.setCdDescTxt((String) map.get(elpDT.getCd()));
                   if(elpDT.getClassCd().equals(NEDSSConstants.POSTAL))
                   {
                       elpDT.getThePostalLocatorDT().setCityCd(elpDT.getThePostalLocatorDT().getCityDescTxt());
                   }
                   lucCollection.add(elpDT);
             }
             catch(Exception e)
             {
                 logger.info(e.toString());
             }

        }
       }
       return lucCollection;
     }

     private Collection<Object>  updateTypeCdWithDescription(Collection<Object> col)
     {
       Collection<Object>  idCollection  = new ArrayList<Object> ();
       if(col!=null)
       {
        Iterator<Object>  ite = col.iterator();

         while(ite.hasNext())
         {
           CachedDropDownValues cache = new CachedDropDownValues();
           TreeMap<?,?> map = cache.getCodedValuesAsTreeMap("EI_TYPE_PAT");
             try
             {
                EntityIdDT  eiDT = (EntityIdDT)((EntityIdDT)ite.next()).deepCopy();
                map = cache.reverseMap(map);
                eiDT.setTypeCd( (String) map.get(eiDT.getTypeCd()));
                idCollection.add(eiDT);
             }

             catch(Exception e)
             {
                 logger.info(e.toString());
             }
        }
       }
       return idCollection;
     }

    /**
     * This private method is used to convert Timestamp object to String object
     * @param timestamp Timestamp object
     * @return String object
     */
    private String formatDate(java.sql.Timestamp timestamp) {
      java.util.Date date = null;
      SimpleDateFormat formatter = new SimpleDateFormat("MM/dd/yyyy");
      if (timestamp != null) {
        date = new java.util.Date(timestamp.getTime());
      }
      if (date == null) {
        return null;
      }
      else {
        return formatter.format(date);
      }
    }
    private String buildSearchCriteriaString(PersonSearchVO psVO) {

    	//  build the criteria string
    	StringBuffer sQuery = new StringBuffer("");
    	CachedDropDownValues cache = new CachedDropDownValues();

    	try {

    		if (psVO.getLastName() != null &&
    				!psVO.getLastName().equals(""))
    			sQuery.append("Last Name").append(
    					" " +
    							cache.getDescForCode("SEARCH_SNDX",
    									psVO.getLastNameOperator()) +
    					" ").append("'" + psVO.getLastName() + "'").append(
    							", ");

    		if (psVO.getFirstName() != null &&
    				!psVO.getFirstName().equals(""))
    			sQuery.append("First Name").append(
    					" " +
    							cache.getDescForCode("SEARCH_SNDX",
    									psVO.getFirstNameOperator()) +
    					" ").append("'" + psVO.getFirstName() + "'").append(
    							", ");

    		if ( (psVO.getBirthTimeMonth() != null &&
    				!psVO.getBirthTimeMonth().equals("")) ||
    				(psVO.getBirthTimeDay() != null && !psVO.getBirthTimeDay().equals("")) ||
    				(psVO.getBirthTimeYear() != null && !psVO.getBirthTimeYear().equals("")))
    		{
    			sQuery.append("DOB").append(" " +
    					cache.getDescForCode("SEARCH_NUM",
    							psVO.getBirthTimeOperator()) +
    					" ").append("'" +
    							PersonUtil.getBirthDate(psVO.
    									getBirthTimeMonth(),
    									psVO.getBirthTimeDay(), psVO.getBirthTimeYear()) + "'").append(", ");

    		}
    		else if ( (psVO.getAfterBirthTime() != null &&
    				psVO.getAfterBirthTime().trim().length() != 0) ||
    				(psVO.getBeforeBirthTime() != null &&
    				psVO.getBeforeBirthTime().trim().length() != 0))
    		{
    			sQuery.append("DOB").append(" Between ").append("'" +
    					psVO.getBeforeBirthTime() + "' and '").append(
    							psVO.getAfterBirthTime() + "'").append(", ");

    		}


    		if (psVO.getCurrentSex() != null &&
    				!psVO.getCurrentSex().equals(""))
    			sQuery.append("Current Sex Equal ").append(
    					"'" +
    							cache.getDescForCode("SEX", psVO.getCurrentSex()) +
    					"'").append(", ");

    		if (psVO.getStreetAddr1() != null &&
    				!psVO.getStreetAddr1().equals(""))
    			sQuery.append("Street Address").append(
    					" " +
    							cache.getDescForCode("SEARCH_SNDX",
    									psVO.getStreetAddr1Operator()) +
    					" ").append("'" + psVO.getStreetAddr1() + "'").append(
    							", ");

    		if (psVO.getCityDescTxt() != null &&
    				!psVO.getCityDescTxt().equals(""))
    			sQuery.append("City").append(
    					" " +
    							cache.getDescForCode("SEARCH_SNDX",
    									psVO.getCityDescTxtOperator()) +
    					" ").append("'" + psVO.getCityDescTxt() + "'").append(
    							", ");

    		if (psVO.getState() != null && !psVO.getState().equals(""))
    			sQuery.append("State Equal ").append(
    					"'" + getStateDescTxt(psVO.getState()) + "'").append(
    							", ");

    		if (psVO.getZipCd() != null && !psVO.getZipCd().equals(""))
    			sQuery.append("Zip").append(" Equal ").append("'" + psVO.getZipCd() + "' ").append(",");

    		if (psVO.getLocalID() != null && !psVO.getLocalID().equals("")) {
    			if (psVO.getPatientIDOperator().equals("IN")) {
    				sQuery.append("Patient ID Equals ")
    				.append("'" + psVO.getLocalID() + "'").append(", ");
    			} else {
    				sQuery.append("Patient ID Equal ")
    				.append("'" + psVO.getLocalID() + "'").append(", ");
    			}
    		}

    		if (psVO.getRootExtensionTxt() != null &&
    				!psVO.getRootExtensionTxt().equals(""))
    		{

    			if (psVO.getTypeCd() != null &&
    					!psVO.getTypeCd().equals(""))
    				sQuery.append(cache.getDescForCode("EI_TYPE_PAT",psVO.getTypeCd())).append(
    						" Equal ").append("'" + psVO.getRootExtensionTxt() +
    								"'").append(", ");
    		}


    		if (psVO.getSsn()!= null &&
    				!psVO.getSsn().trim().equals(""))
    			sQuery.append("SSN").append(" Equal ").append("'" + psVO.getSsn() + "' ").append(",");


    		if (psVO.getPhoneNbrTxt() != null && !psVO.getPhoneNbrTxt().equals("") && psVO.getPhoneNbrTxt().trim().length() == 12)
    			sQuery.append("Telephone").append(" Equal ").append("'" + psVO.getPhoneNbrTxt() + "' ").append(",");
    		else if (psVO.getPhoneNbrTxt() != null && !psVO.getPhoneNbrTxt().equals("") && psVO.getPhoneNbrTxt().trim().length() < 12)
    			sQuery.append("Telephone").append(" Contains ").append("'" + psVO.getPhoneNbrTxt() + "' ").append(",");

    		String findRace = getRaceDescTxt(psVO.getRaceCd(),cache.getRaceCodes("ROOT"));

    		if (psVO.getRaceCd() != null && !psVO.getRaceCd().equals(""))
    			sQuery.append("Race Equal ").append(
    					"'" +
    							cache.getDescForCode("ROOT", psVO.getRaceCd()) +
    					"'").append(", ");

    		if (psVO.getEthnicGroupInd() != null &&
    				!psVO.getEthnicGroupInd().equals(""))
    			sQuery.append("Ethnicity Equal ").append(
    					"'" +
    							cache.getDescForCode("P_ETHN_GRP",
    									psVO.getEthnicGroupInd()) +
    					"'").append(", ");

    		String findRole = getRoleDescTxt(psVO.getRole(),cache.getCodedValues("RL_TYPE_PAT"));

    		if (psVO.getRole() != null && !psVO.getRole().equals(""))
    			sQuery.append("Role Equal ").append(
    					"'" +
    							cache.getDescForCode("RL_TYPE_PAT", psVO.getRole()) +
    					"'").append(", ");

    		//System.out.println("Search String: " + sQuery.toString());
    	} catch (Exception ex) {
    		logger.error("Exception encountered in CompareMergeLoad.buildSearchCriteriaString() " + ex.getMessage());
    	}
    	return sQuery.toString();

    }//buildSearchCriteriaString
    private String getStateDescTxt(String sStateCd) {

    	CachedDropDownValues srtValues = new CachedDropDownValues();
    	TreeMap<Object,Object> treemap = srtValues.getStateCodes2("USA");
    	String desc = "";

    	if (sStateCd != null && treemap.get(sStateCd) != null)
    		desc = (String) treemap.get(sStateCd);

    	return desc;
    }
    private String getRaceDescTxt(String raceCode, String descTxt){

    	String returnStr = "";
    	if (raceCode != null) {
    		if (!raceCode.equals("") && descTxt != null) {
    			String findRace = descTxt.substring(descTxt.indexOf(raceCode));
    			String s = findRace.substring(findRace.indexOf("$") + 1);
    			return s.substring(0, s.indexOf("|") + 1);
    		}
    	}

    	return returnStr;
    }

    private String getRoleDescTxt(String role , String descTxt) {

    	String returnStr = "";
    	if (role != null) {
    		if (!role.equals("") && descTxt != null) {
    			String findRole = descTxt.substring(descTxt.indexOf(role));
    			String s = findRole.substring(findRole.indexOf("$") + 1);
    			return s.substring(0, s.indexOf("|") + 1);
    		}
    	}
    	return returnStr;
    }
    /**
     * This list is from the Manual Merge list
     * @param personList
     * @return
     */
    private ArrayList<?> sortPersonListOnPersonLocalId(ArrayList<?> personList) {
    	logger.debug("entering sortPersonListOnPersonLocalId()");
    	DisplayPersonList displayPersonList = (DisplayPersonList)personList.get(0);
    	ArrayList<Object> patientObjList = displayPersonList.getList();
    	Collections.sort( patientObjList, new Comparator()
		{
			public int compare( Object a, Object b )
			{
				return((String) ((PatientSrchResultVO)a).getPersonLocalID()).compareToIgnoreCase((String) ((PatientSrchResultVO) b).getPersonLocalID());
			}
		} );
    	logger.debug("leaving sortPersonListOnPersonLocalId()");
    	return patientObjList;
	}
    /**
     * This list if from the System Identified Merge Candidate List
     * @param personList
     * @return
     */
    private ArrayList<Object> sortPatientSrchResultVOList(ArrayList<Object> personList) {
    	if (personList == null || personList.size() < 2)
    		return personList;
    	logger.debug("entering sortPatientSrchResultVOList()");
    	Collections.sort( personList, new Comparator()
		{
			public int compare( Object a, Object b )
			{
				return((String) ((PatientSrchResultVO)a).getPersonLocalID()).compareToIgnoreCase((String) ((PatientSrchResultVO) b).getPersonLocalID());
			}
		} );
    	logger.debug("leaving sortPatientSrchResultVOList()");
    	return personList;
	}
}

