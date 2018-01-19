package gov.cdc.nedss.webapp.nbs.action.deduplication;

/**
 * Title:        MergeCandidateListSubmit
 * Description:
 * Copyright:    Copyright (c) 2001
 * Company:      CSC
 * @author
 * @version 1.0
 */

import java.io.*;
import java.util.*;
import java.sql.Timestamp;

import javax.servlet.http.*;
import javax.servlet.*;

import org.apache.struts.action.*;

import gov.cdc.nedss.proxy.ejb.entityproxyejb.bean.EntityProxyHome;
import gov.cdc.nedss.systemservice.ejb.mainsessionejb.bean.*;
import gov.cdc.nedss.systemservice.nbscontext.*;
import gov.cdc.nedss.systemservice.nbssecurity.*;
import gov.cdc.nedss.systemservice.util.MainSessionHolder;
import gov.cdc.nedss.systemservice.util.PrepareVOUtils;
import gov.cdc.nedss.util.*;
import gov.cdc.nedss.exception.*;
import gov.cdc.nedss.webapp.nbs.action.person.util.PersonUtil;
import gov.cdc.nedss.webapp.nbs.action.util.ErrorMessageHelper;
import gov.cdc.nedss.entity.person.dt.*;
import gov.cdc.nedss.entity.person.vo.*;
import gov.cdc.nedss.locator.dt.*;
import gov.cdc.nedss.webapp.nbs.form.person.*;
import gov.cdc.nedss.entity.entityid.dt.EntityIdDT;
import gov.cdc.nedss.association.dt.RoleDT;
import gov.cdc.nedss.deduplication.ejb.deduplicationprocessor.bean.DeDuplicationProcessorHome;
import gov.cdc.nedss.deduplication.vo.*;
//for the old way using entity
import gov.cdc.nedss.util.PropertyUtil;
import gov.cdc.nedss.webapp.nbs.logicsheet.helper.CachedDropDownValues;
import gov.cdc.nedss.webapp.nbs.util.DeDuplicationQueueHelper;

public class MergeCandidateListSubmit extends Action
{

	//For logging
	static final LogUtils logger = new LogUtils(MergeCandidateListSubmit.class.getName());

	public MergeCandidateListSubmit()
	{
	}

	   /**
      * Handles the loading of the page for comparing the two records for merging.
      * @J2EE_METHOD  --  execute
      * @param mapping       the ActionMapping
      * @param form     the ActionForm
      * @return request    the  HttpServletRequest
      * @return response    the  HttpServletResponse
      * @throws IOException
      * @throws ServletException
      **/
	public ActionForward execute(ActionMapping mapping,
				 ActionForm form,
				 HttpServletRequest request,
				 HttpServletResponse response)
	throws IOException, ServletException
	{

          HttpSession session = request.getSession(false);
          NBSSecurityObj secObj = (NBSSecurityObj) session.getAttribute(
              "NBSSecurityObject");

          Collection<?>  mergePatientsCollection;
          Collection<?>  retMergeConfirmationVO;


          String contextAction = request.getParameter("ContextAction");
          String contextTaskName = (String)request.getAttribute("ContextTaskName");

           if(contextTaskName == null)
            contextTaskName = (String)NBSContext.getCurrentTask(session);

          if (contextAction == null)
            contextAction = (String) request.getAttribute("ContextAction");

          if(contextTaskName.equalsIgnoreCase("MergeCandidateList2")) {
            Integer groupNumber = getGroupNbr(session);
            // Handle System Identified Group related person merges here...
            if (contextAction.equalsIgnoreCase("Merge") &&
                contextTaskName.equalsIgnoreCase("MergeCandidateList2")) {
              try {
                //mergePatientsCollection  = getPersonVOs(request, session, secObj);
                mergePatientsCollection  = getSimilarGroupToMerge(groupNumber,
                    request.getParameterValues("merge"));
                //is a Survivor Specified? 
                String specifiedSurvivorPatientId = "";
                String[] survivorList = request.getParameterValues("survivor");
                if (survivorList != null && !survivorList[0].isEmpty()) {
               	 specifiedSurvivorPatientId = survivorList[0];
                } else {
                	//per Christi, they always want the oldest by default
                	//they can override with property setting
                	if (PropertyUtil.isMergeCandidateDefaultSurvivorOldest())
                		specifiedSurvivorPatientId = findOldestPatientId(mergePatientsCollection);
                }
                retMergeConfirmationVO = sendProxyToMeregPersons(
                    mergePatientsCollection, specifiedSurvivorPatientId, session, secObj);
                try {
                updateNonSelectedGroups(getNonSelectedGroupMembers(groupNumber,
                    mergePatientsCollection), session, secObj);
                } catch(Exception e) {
                  if (e instanceof NEDSSAppException) {
                    NEDSSAppException nae = (NEDSSAppException) e;
                    String errorNumber = nae.getErrorCd();
                    if(errorNumber.equals("ERR109")) {
                      //do nothing
                      //This was a result of attempting to set the group_nbr to null of
                      //a record that was not selected to be merged, but someone updated it
                      //prior to this attempt to set the group_nbr to null.  So eat the
                      //data concurrent exception and continue on.  Use does not care that someone
                      //else has updated a record the he has not selected to be merged.
                    } else {
                    	throw new ServletException(e);
                    }
                  } else {
                	  throw new ServletException(e);
                    //throw e;
                  }
                }
                //NBSContext.store(session, "DSMergeConfirmation",
                                 //retMergeConfirmationVO);
              NBSContext.store(session,"DSMergeConfirmation",retMergeConfirmationVO);
              }
              catch (Exception e) {
                if (e instanceof NEDSSAppException) {
                  NEDSSAppException nae = (NEDSSAppException) e;
                  String errorNumber = nae.getErrorCd();
                  //if errorCd is 109, dedup needs to clean up statefull information
                  //no one else should do this if not related to deduplication.
                  if (errorNumber.equals("ERR109")) {
                    PersonVO pvo = ((DeduplicationPatientMergeVO)((Collection<?>)DeDuplicationQueueHelper.getDedupTakenQueue().get(groupNumber)).iterator().next()).getMPR();
                    DeDuplicationQueueHelper.getDedupTakenQueue().remove(groupNumber);
                    DeDuplicationQueueHelper.getDedupSessionQueue().remove(session.
                        getId());
                    DeDuplicationQueueHelper.getDedupAvailableQueue().put(groupNumber, pvo.getThePersonDT().getGroupTime());
                    return ErrorMessageHelper.redirectToPage("PS158", errorNumber);
                  }
                  //redirect to generic error page.
                  return ErrorMessageHelper.redirectToPage("PS175", errorNumber);
                } //end of if
                //redirect to generic error page.
                return ErrorMessageHelper.redirectToPage("PS175", null);
              }

              try {
                processPostMergeOrNoMergeForSystemIdentified(groupNumber, session,
                    contextAction, secObj);
              }
              catch (Exception e) {
                if (e instanceof NEDSSAppException) {
                  NEDSSAppException nae = (NEDSSAppException) e;
                  if (nae.getErrorCd().equals("ERR109")) {
                    logger.info("ERROR ,  DataConcurrent exception recieved.", e);
                    //redirect to dataconcurrent message page.
                    return ErrorMessageHelper.redirectToPage("PS158", nae.getErrorCd());
                  }
                  //redirect to generic error page
                  return ErrorMessageHelper.redirectToPage("PS175", nae.getErrorCd());
                }
                return ErrorMessageHelper.redirectToPage("PS175", null);
              }

              return mapping.findForward(contextAction);
            }

            if (contextAction.equalsIgnoreCase("NoMerge") &&
                contextTaskName.equalsIgnoreCase("MergeCandidateList2")) {
              try {
                processPostMergeOrNoMergeForSystemIdentified(groupNumber, session,
                    contextAction, secObj);
              }
              catch (Exception e) {
                if (e instanceof NEDSSAppException) {
                  NEDSSAppException nae = (NEDSSAppException) e;
                  if (nae.getErrorCd().equals("ERR109")) {
                    //We eat the exception and forward to LOAD Action class...
                    DeDuplicationQueueHelper.getDedupTakenQueue().remove(groupNumber);
                    DeDuplicationQueueHelper.getDedupSessionQueue().remove(session.
                        getId());
                    return mapping.findForward(contextAction);
                  }
                  //For All other error codes redirect to generic error page
                  return ErrorMessageHelper.redirectToPage("PS175", nae.getErrorCd());
                }
                return ErrorMessageHelper.redirectToPage("PS175", null);
              }

              return mapping.findForward(contextAction);
            }

            if (contextAction.equalsIgnoreCase("Cancel") &&
                contextTaskName.equalsIgnoreCase("MergeCandidateList2")) {
              //Adjust the HashMap's
              processContextActionCancel(groupNumber, session);
              return mapping.findForward(contextAction);
            }
          }//end of if (contextTaskName.equalsIgnoreCase("MergeCandidateList2")) around line 92


            // get the personVOs for the persons to be manually merged
            if (contextAction.equalsIgnoreCase("Merge")&&
                contextTaskName.equalsIgnoreCase("MergeCandidateList1")) {
            try {
             mergePatientsCollection  = this.getPersonVOs(request,
                session, secObj);
             
             //is a Survivor Specified? 
             String specifiedSurvivorPatientId = "";
             String[] survivorList = request.getParameterValues("survivor");
             if (survivorList != null && !survivorList[0].isEmpty()) {
            	 specifiedSurvivorPatientId = survivorList[0];
             } else {
            	 //per Christi, they always want the oldest by default, they can override with property setting
            	 //if it is not there, it defaults to oldest.
            	 if (PropertyUtil.isMergeCandidateDefaultSurvivorOldest())
            		 specifiedSurvivorPatientId = findOldestPatientId(mergePatientsCollection);
             }
            /*
             * call the method on proxy merge the selected person and get back the collection of survivor and merged persons
             */
               retMergeConfirmationVO = this.sendProxyToMeregPersons(mergePatientsCollection, specifiedSurvivorPatientId, session, secObj);
               //NBSContext.store(session,"DSMergeConfirmation",this.UpdateVOWithNameCdDesc(retMergeConfirmationVO));
              NBSContext.store(session,"DSMergeConfirmation",retMergeConfirmationVO);
             }
            catch (Exception e) {

              if(e instanceof NEDSSAppException) {
                NEDSSAppException nae = (NEDSSAppException)e;
                if (nae.getErrorCd() != null && nae.getErrorCd().equals("ERR109")) {
                  logger.fatal(
                      "ERROR , The data has been modified by another user, please recheck! ",
                      nae);

                  return ErrorMessageHelper.redirectToPage("PS158", nae.getErrorCd());
                }
                return ErrorMessageHelper.redirectToPage("PS175", nae.getErrorCd());
              } else {
                return ErrorMessageHelper.redirectToPage("PS175", null);
              }
            }//end of catch
            return mapping.findForward(contextAction);
          }
          else if (contextAction.equals("NewSearch")) {
            return mapping.findForward(contextAction);
          }
          else if (contextAction.equals("RefineSearch")) {
            return mapping.findForward(contextAction);
          }

          else if (contextAction.equals("Cancel")&&
                contextTaskName.equalsIgnoreCase("MergeCandidateList1")) {
            return mapping.findForward(contextAction);
          }
          return null;
        }

		private Integer getGroupNbr(HttpSession session) {
          DeDuplicationQueueHelper dh = (DeDuplicationQueueHelper)session.getAttribute("DQH");
          if(dh!=null)
          {
            return (Integer) dh.getDedupSessionQueue().get(session.getId());
          }
          else
          {
            return null;
          }
        }


        private void updateNonSelectedGroups(Collection<?> nonSelectedPvo, HttpSession session, NBSSecurityObj secObj) throws Exception {
          if(nonSelectedPvo.size() > 0) {

              MainSessionCommand msCommand = null;
              String sBeanJndiName = JNDINames.EntityControllerEJB;
              String sMethod = "setMPR";

              MainSessionHolder holder = new MainSessionHolder();
              msCommand = holder.getMainSessionCommand(session);

              //Collection<Object>  collection =  (Collection)DeDuplicationQueueHelper.getDedupTakenQueue().get(groupNumber);

             Iterator<?>  iterator = nonSelectedPvo.iterator();

              while (iterator.hasNext()) {
                PersonVO personVO = (PersonVO) iterator.next();
                personVO.setItDirty(true);
                personVO.getThePersonDT().setItDirty(true);
                personVO.getThePersonDT().setGroupNbr(null);
                personVO.getThePersonDT().setGroupTime(null);

                Object[] oParams = new Object[] {
                    personVO, "PAT_NO_MERGE"};

                msCommand.processRequest(sBeanJndiName, sMethod, oParams);
              }//end of while

          }//end of if
        }

        private Collection<Object>  getNonSelectedGroupMembers(Integer groupNbr, Collection<?>  selectedColl) {
          if(selectedColl == null) return null;


          List<?> totalGroupCollection  = (List<?>)DeDuplicationQueueHelper.getDedupTakenQueue().get(groupNbr);
         Iterator<?>  selectedColliterator = selectedColl.iterator();

          List<Object> personUIDsInTotalGroupList = new ArrayList<Object> ();
          List<Object> personUIDsInSelectedList = new ArrayList<Object> ();
          Collection<Object>  nonSelectedCollection  = new ArrayList<Object> ();

          int totalGroupCollectionSize = totalGroupCollection.size();
          int counter = 0;

          while(counter < totalGroupCollectionSize)
          {
            personUIDsInTotalGroupList.add(((DeduplicationPatientMergeVO) totalGroupCollection.get(counter)).getMPR().getThePersonDT().getPersonUid());
            counter++;
          }

          while(selectedColliterator.hasNext())
          {
            personUIDsInSelectedList.add(((PersonVO) selectedColliterator.next()).getThePersonDT().getPersonUid());
          }

          totalGroupCollectionSize = totalGroupCollection.size();

          while(totalGroupCollectionSize-- > 0)
          {
            if(personUIDsInSelectedList.contains(personUIDsInTotalGroupList.get(totalGroupCollectionSize)) == false)
          {
            nonSelectedCollection.add(((DeduplicationPatientMergeVO) totalGroupCollection.get(totalGroupCollectionSize)).getMPR());
          }
        }

        return nonSelectedCollection;


        }

        private Collection<Object>  getSimilarGroupToMerge(Integer groupNbr, String[] mergeCandidateList) {
        	ArrayList<Object> list = new ArrayList<Object> ();
        	Collection<Object>  retPerVOColl = null;
        	Collection<Object>  sysIdCollection  = new ArrayList<Object> ();

        	try {
        		Collection<?>  pvoGroup = (Collection<?>)DeDuplicationQueueHelper.getDedupTakenQueue().get(groupNbr);

        		for(int i =0; i<mergeCandidateList.length; i++)
        		{
        			Long selectedId = new Long(mergeCandidateList[i]);
        			Iterator<?>  it = pvoGroup.iterator();
        			while(it.hasNext()) {
        				PersonVO pvo = ((DeduplicationPatientMergeVO)it.next()).getMPR();
        				if(pvo.getThePersonDT().getPersonUid().longValue() == selectedId.longValue()) {
        					list.add(pvo);
        					break;
        				}//end of if
        			}//end of while
        		}//end of while
        	} catch (Exception ex) {
        		logger.error("Exception encountered in CompareMergeLoad.getSimilarGroupToMerge() " + ex.getMessage());
        		ex.printStackTrace();
        	}
        	return list;
        }

        /**
           * Retrieves the personVOs of the persons to be merged.
           * @J2EE_METHOD  --  getPersonVOs
           * @param request       the HttpServletRequest
           * @return session    the  HttpSession
           * @return secObj    the  NBSSecurityObj
           **/
             private Collection<?>  getPersonVOs (HttpServletRequest request, HttpSession session,
                     NBSSecurityObj secObj) throws Exception
             {


                     Collection<?>  retPerVOColl = null;
                     Collection<Object>  sysIdCollection  = null;
                     String[] mergeCandidateList = request.getParameterValues("merge");
                     sysIdCollection  = new ArrayList<Object> ();

                     // Get the Uids for the patients to be merged and add it to the collection

                     for(int i =0; i<mergeCandidateList.length; i++)
                     {
                        sysIdCollection.add(new Long(mergeCandidateList[i]));
                     }
                     // call proxy to get the collection of the personVOs to be merged
                     if(sysIdCollection!=null)
                            retPerVOColl = sendProxyToGetPersons(sysIdCollection, session, secObj);

                    // Make sure that the PersonVO's returned as part of the above collection
                    // have a record status code of 'ACTIVE', otherwise raise DataConc Exception and
                    // get out...

                   Iterator<?>  iterator = retPerVOColl.iterator();

                    while(iterator.hasNext())
                    {
                        PersonVO personVO = (PersonVO)iterator.next();
                        if(!personVO.getThePersonDT().getRecordStatusCd().equals("ACTIVE"))
                        {
                            throw new NEDSSConcurrentDataException("ERR109");
                        }
                    }

                    return retPerVOColl;

             }
              /**
                * Retrieves the personVOs of the persons to be merged.
                 * @J2EE_METHOD  --  getPersonVOs
                 * @param sysIdCollection        the Collection
                 * @return session    the  HttpSession
                 * @return secObj    the  NBSSecurityObj
                 * @throws NEDSSAppConcurrentDataException
                **/
                private Collection<?>  sendProxyToGetPersons(Collection<Object> sysIdCollection, HttpSession session,
                      NBSSecurityObj secObj) throws Exception
               {
               /**
               * Call the mainsessioncommand
               */

               MainSessionCommand msCommand = null;
               try
               {
                      String sBeanJndiName = JNDINames.ENTITY_PROXY_EJB;
                      String sMethod = "getPersons";

                      //##!! System.out.println("Number of elements in UID collection that is passed to EntityProxy.getPersons is " +	sysIdCollection.size());

                      Object[] oParams = new Object[]{sysIdCollection};

                      MainSessionHolder holder = new MainSessionHolder();
                      msCommand = holder.getMainSessionCommand(session);

                      //##!! System.out.println("Jndi name= " + sBeanJndiName + " Method= " + sMethod);

                      Collection<?>  resultPerVOs = (ArrayList<?> )msCommand.processRequest(sBeanJndiName, sMethod, oParams).get(0);

                      if ((resultPerVOs != null)) //&& (resultPerVOs.size() > 0))
                      {
                              logger.debug("Found PersonVOs");
                              return resultPerVOs;
                      }
                      else
                      {
                              return null;
                      }
              }

              finally
              {
                      msCommand = null;

              }

      }
      private Collection<Object>  sendProxyToMeregPersons(Collection<?> pVOCollection, String specifiedSurvivorPatientId, HttpSession session,
                    NBSSecurityObj secObj) throws Exception
             {
             /**
             * Call the mainsessioncommand
             */

             MainSessionCommand msCommand = null;
             try
             {
                    String sBeanJndiName = JNDINames.DEDUPLICATION_PROCESSOR_EJB;
                    String sMethod = "mergeMPR";


                    //##!! System.out.println("Number of elements in UID collection that is passed to EntityProxy.getPersons is " +	sysIdCollection.size());

                    Object[] oParams = new Object[]{pVOCollection, specifiedSurvivorPatientId};

                    MainSessionHolder holder = new MainSessionHolder();
                    msCommand = holder.getMainSessionCommand(session);

                    //##!! System.out.println("Jndi name= " + sBeanJndiName + " Method= " + sMethod);

                    DeduplicationPatientMergeVO dedupPatientMergeVO = (DeduplicationPatientMergeVO)msCommand.processRequest(sBeanJndiName, sMethod, oParams).get(0);

                    if ((dedupPatientMergeVO.getTheMergeConfirmationVO() != null)) //&& (resultPerVOs.size() > 0))
                    {
                            logger.debug("Found PersonVOs");
                            Object[] oParam = new Object[]{dedupPatientMergeVO.getThePersonMergeDT()};
                            String method = "createPersonMergeDt";
                            msCommand.processRequest(sBeanJndiName, method, oParam);
                            return dedupPatientMergeVO.getTheMergeConfirmationVO();
                    }
                    else
                    {
                            return null;
                    }
            }
            finally
            {
                    msCommand = null;
            }

    }
    /**
     * Processes user actions Merge and NoMerge
     * @param groupNumber
     * @param session
     * @param contextAction
     * @param secObj
     * @throws NEDSSAppException
     */
    private void processPostMergeOrNoMergeForSystemIdentified(Integer groupNumber, HttpSession session, String contextAction, NBSSecurityObj secObj) throws Exception
    {

        synchronized(this)
        {

            if (contextAction.equalsIgnoreCase("Merge")) {
              HashMap<Object,Object> hashMap = DeDuplicationQueueHelper.getDedupTakenQueue();
              hashMap.remove(groupNumber);

              hashMap = DeDuplicationQueueHelper.getDedupSessionQueue();
              hashMap.remove(session.getId());
              return;
            }

            if (contextAction.equalsIgnoreCase("NoMerge")) {

              // Go to the DataBase and null out the groupNumber field and groupTime field associated
              // with the PVO's of this groupNumber...
              MainSessionCommand msCommand = null;
              String sBeanJndiName = JNDINames.EntityControllerEJB;
              String sMethod = "setMPR";

              MainSessionHolder holder = new MainSessionHolder();
              msCommand = holder.getMainSessionCommand(session);

              Collection<?>  collection = (Collection<?>) DeDuplicationQueueHelper.
                  getDedupTakenQueue().get(groupNumber);

                //Remove the group number
                HashMap<Object,Object> hashMap = DeDuplicationQueueHelper.getDedupTakenQueue();
                hashMap.remove(groupNumber);

                hashMap = DeDuplicationQueueHelper.getDedupSessionQueue();
                hashMap.remove(session.getId());

             Iterator<?>  iterator = collection.iterator();

              //while (iterator.hasNext()) {
                //ArrayList<Object> deDupMergeArrayList = (ArrayList<Object> ) iterator.next();
                //Iterator ite = deDupMergeArrayList.iterator();
                while(iterator.hasNext())
                {
                  DeduplicationPatientMergeVO dedupPatientMergeVO = (DeduplicationPatientMergeVO)iterator.next();
                  PersonVO personVO = (PersonVO)dedupPatientMergeVO.getMPR();
                  personVO.setItDirty(true);
                  personVO.setItNew(false);
                  personVO.getThePersonDT().setItDirty(true);
                  personVO.getThePersonDT().setItNew(false);
                  personVO.getThePersonDT().setGroupNbr(null);
                  personVO.getThePersonDT().setGroupTime(null);

                  Object[] oParams = new Object[]
                      {personVO, "PAT_NO_MERGE"};
                  msCommand.processRequest(sBeanJndiName, sMethod, oParams);
                }
              }
              return;

        }//end of sync
    }

    /**
     * Given a groupNumber and on cancel action from the user the method sets the HashMap's appropriately
     * @param inGroupNumber
     * @param inSession
     */
    private void processContextActionCancel(Integer inGroupNumber, HttpSession inSession)
    {
        synchronized(this)
        {
            HashMap<Object,Object> deduplicationAvailableQueue = DeDuplicationQueueHelper.getDedupAvailableQueue();
            HashMap<Object,Object> dedupTakenQueue = DeDuplicationQueueHelper.getDedupTakenQueue();

            //Since the value of the object in the DeDupTakenQueue is a collection of PVO's
            //we need to grab the first one and get a PersonDT out of it and then get the groupTime on it
            //to set it on the AvailableQueue
            if(dedupTakenQueue!=null)
            {
              Collection<?>  collection = (Collection<?>) dedupTakenQueue.get(inGroupNumber);
              if(collection != null)
              {
               Iterator<?>  iterator = collection.iterator();

                if (iterator.hasNext())
                {
                  DeduplicationPatientMergeVO dedupPatientMergeVO = (DeduplicationPatientMergeVO) iterator.next();
                  deduplicationAvailableQueue.put(inGroupNumber,
                                                  dedupPatientMergeVO.getMPR().
                                                  getThePersonDT().getGroupTime());
                }
              }

              dedupTakenQueue.remove(inGroupNumber);

              if(DeDuplicationQueueHelper.getDedupSessionQueue()!=null)DeDuplicationQueueHelper.getDedupSessionQueue().remove(inSession.getId());
            }
        }
    }
    /**
     * findOldestPatientId - find the patient id that is less than all the others
     * Per christi the users want to default to the oldest patient id
     * @param personVOCollection
     * @return
     */
    private String findOldestPatientId(Collection<?> personVOCollection) {
    	Iterator personIter = personVOCollection.iterator();
    	String oldestPersonId = null;
    	while (personIter.hasNext()) {
    		PersonVO personVO = (PersonVO) personIter.next();
    		String personID = PersonUtil.getDisplayLocalID(personVO.getThePersonDT().getLocalId());
    		if (oldestPersonId == null)
    			oldestPersonId = personID;
    		else {
    			Long currentOldestPersonId = new Long(oldestPersonId);
    			Long thisPersonId = new Long(personID);
    			if (thisPersonId.compareTo(currentOldestPersonId) < 0)
    				oldestPersonId = personID;
    		}
    		
    	} //while hasNext
    	
	return oldestPersonId;
}


}