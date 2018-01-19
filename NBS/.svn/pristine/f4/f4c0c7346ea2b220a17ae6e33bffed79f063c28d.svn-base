package gov.cdc.nedss.act.observation.helper;

/**
 * Title:        ObservationProcessor
 * Description:
 * Copyright:    Copyright (c) 2002
 * Company:      Computer Sciences Corp
 * @author NEDSS development team
 * @version 1.0
 */
import gov.cdc.nedss.act.observation.dt.ObsValueCodedDT;
import gov.cdc.nedss.act.observation.dt.ObservationDT;
import gov.cdc.nedss.act.observation.vo.ObservationVO;
import gov.cdc.nedss.act.sqlscript.WumSqlQuery;
import gov.cdc.nedss.elr.ejb.msginprocessor.helper.ELRConstants;
import gov.cdc.nedss.entity.observation.dt.CodedResultDT;
import gov.cdc.nedss.entity.observation.dt.LoincResultDT;
import gov.cdc.nedss.entity.observation.dt.ObservationNameDT;
import gov.cdc.nedss.entity.observation.util.DisplayObservationList;
import gov.cdc.nedss.entity.observation.vo.LoincSearchResultTmp;
import gov.cdc.nedss.entity.observation.vo.LoincSrchResultVO;
import gov.cdc.nedss.entity.observation.vo.ObservationSearchResultTmp;
import gov.cdc.nedss.entity.observation.vo.ObservationSrchResultVO;
import gov.cdc.nedss.entity.organization.dt.OrganizationNameDT;
import gov.cdc.nedss.entity.organization.ejb.dao.OrganizationNameDAOImpl;
import gov.cdc.nedss.entity.sqlscript.NEDSSSqlQuery;
import gov.cdc.nedss.exception.NEDSSDAOSysException;
import gov.cdc.nedss.exception.NEDSSSystemException;
import gov.cdc.nedss.proxy.ejb.investigationproxyejb.dao.ObservationSummaryDAOImpl;
import gov.cdc.nedss.proxy.ejb.investigationproxyejb.dao.RetrieveSummaryVO;
import gov.cdc.nedss.proxy.ejb.observationproxyejb.vo.LabReportResultedtestSummaryVO;
import gov.cdc.nedss.proxy.ejb.observationproxyejb.vo.LabReportSummaryVO;
import gov.cdc.nedss.proxy.ejb.observationproxyejb.vo.MorbReportConditionSummaryVO;
import gov.cdc.nedss.proxy.ejb.observationproxyejb.vo.MorbReportSummaryVO;
import gov.cdc.nedss.proxy.ejb.observationproxyejb.vo.ObservationPersonInfoVO;
import gov.cdc.nedss.proxy.ejb.observationproxyejb.vo.ProviderDataForPrintVO;
import gov.cdc.nedss.proxy.ejb.observationproxyejb.vo.ReportSummaryInterface;
import gov.cdc.nedss.proxy.ejb.observationproxyejb.vo.ResultedTestSummaryVO;
import gov.cdc.nedss.proxy.util.EntityProxyHelper;
import gov.cdc.nedss.systemservice.ejb.srtmapejb.dao.SRTMapDAOImpl;
import gov.cdc.nedss.systemservice.nbssecurity.NBSBOLookup;
import gov.cdc.nedss.systemservice.nbssecurity.NBSOperationLookup;
import gov.cdc.nedss.systemservice.nbssecurity.NBSSecurityObj;
import gov.cdc.nedss.util.DAOBase;
import gov.cdc.nedss.util.GenericSummaryVO;
import gov.cdc.nedss.util.LogUtils;
import gov.cdc.nedss.util.NEDSSConstants;
import gov.cdc.nedss.util.PropertyUtil;
import gov.cdc.nedss.util.UidSummaryVO;
import gov.cdc.nedss.webapp.nbs.logicsheet.helper.CachedDropDownValues;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.Vector;

public class ObservationProcessor
    extends DAOBase {

  private static final LogUtils logger = new LogUtils(ObservationProcessor.class.
      getName());
  private final String NEXT = "NEXT";
  private static PropertyUtil propertyUtil= PropertyUtil.getInstance();

  private boolean programAreaDerivationExcludeFlag = false;  
  
  public ObservationProcessor() {
	//	 propertyUtil propertyUtil = propertyUtil.getInstance();
  }
//This method is added in place of old method retrieveMorbReportSummary() to improve the performance of the page
  
  @SuppressWarnings("unchecked")
public HashMap<Object, Object> retrieveMorbReportSummaryRevisited(Collection<Object> uidList, boolean isCDCPrintForm,
          NBSSecurityObj nbsSecurityObj, String uidType){
	  	Long providerUid = null;
	    ArrayList<Object>  labReportUids = new ArrayList<Object> ();
	    ArrayList<Object>  treatmentUids = new ArrayList<Object> ();
	    ArrayList<Object>  morbReportSummaryVOColl = new ArrayList<Object> ();
	    ArrayList<Object>  morbReportEventVOColl = new ArrayList<Object> ();
	    MorbReportSummaryVO morbVO = new MorbReportSummaryVO();
	    ObservationSummaryDAOImpl osd = new ObservationSummaryDAOImpl();
	    RetrieveSummaryVO rSummaryVO = new RetrieveSummaryVO();
	    
	    MorbReportSummaryVO morbReportVO = new MorbReportSummaryVO();
	    ArrayList<Object>  morbArray = new ArrayList<Object> ();
	    ArrayList<Object>  argList = new ArrayList<Object> ();
	    Long uid = null;


//	    if (!nbsSecurityObj.getPermission(NBSBOLookup.OBSERVATIONMORBIDITYREPORT,
//	                                      NBSOperationLookup.VIEW)) {
//	      throw new NEDSSSystemException(
//	          "no permissions to view a Morbidity report");
//	    }
	    //This is the where Clause
	    String dataAccessWhereClause = nbsSecurityObj.getDataAccessWhereClause(
	            NBSBOLookup.OBSERVATIONMORBIDITYREPORT, NBSOperationLookup.VIEW);
        if (dataAccessWhereClause == null) {
          dataAccessWhereClause = "";
        }
        else {
          dataAccessWhereClause = " AND " + dataAccessWhereClause;

        }
	    	    
	    try {
	      Iterator<Object> iterator = uidList.iterator();
	      Timestamp fromTime = null;
	      Long MorbUidAsSourceForInvestigation = null;
	      while (iterator.hasNext()) {
	    	  if(uidType.equals("PERSON_PARENT_UID")){

	       // Long morbidityUid = ( (UidSummaryVO) morbIterator.next()).getUid();
	    	uid = (Long) iterator.next();
	        //ArrayList<Object> argList = new ArrayList<Object> ();
	    	 argList.clear();
	        argList.add(uid);
	        //ArrayList<Object> morbArray = new ArrayList<Object> ();
	        String selQuery = WumSqlQuery.SELECT_MORBSUMMARY_FORWORKUPNEW+ dataAccessWhereClause;
	        morbArray = (ArrayList<Object> ) preparedStmtMethod(morbVO, argList,
	        		selQuery, NEDSSConstants.SELECT);
	    	  }else if(uidType.equals("MORBIDITY_UID"))
		    	 {
	    		  UidSummaryVO uidVO = (UidSummaryVO) iterator.next();
	    		  Long morbidityUid = uidVO.getUid();
		    	  fromTime = uidVO.getAddTime();
		    	  if(uidVO.getStatusTime()!=null && fromTime != null && fromTime.equals(uidVO.getStatusTime())){
		    		  MorbUidAsSourceForInvestigation=morbidityUid;
		    	  }
		    	 	argList.clear();
	    	        argList.add(morbidityUid);
	    	        //ArrayList<Object> morbArray = new ArrayList<Object> ();
	    	        morbArray = (ArrayList<Object> ) preparedStmtMethod(morbVO, argList,
	    	            WumSqlQuery.SELECT_MORBSUMMARY_FORWORKUP, NEDSSConstants.SELECT);
		    	 }


	        if (morbArray != null) {
	          Iterator<Object> morbReportSummaryIter = morbArray.iterator();
	          while (morbReportSummaryIter.hasNext()) {
	        	  morbReportVO = (MorbReportSummaryVO) morbReportSummaryIter.
	                next();
	        	morbReportVO.setActivityFromTime(fromTime);
	            Long morbidityUid = morbReportVO.getObservationUid();
	            ArrayList<Object>  rptArgs = new ArrayList<Object> ();
	            ArrayList<Object>  rpt = new ArrayList<Object> ();
	            String setValue = "";
	            MorbReportSummaryVO morbReportSummaryVO = null;
	    	    MorbReportSummaryVO morbReportEventVO = null;
	            MorbReportSummaryVO rptValue = new MorbReportSummaryVO();
	            rptArgs.add(morbidityUid);
	            ArrayList<Object>  providerDetails = osd.getProviderInfo(morbidityUid,"PhysicianOfMorb");
	            
	            Map<Object,Object> associationsMap = rSummaryVO.getAssociatedInvList(morbidityUid,nbsSecurityObj, "OBS");
	            morbReportVO.setAssociationsMap(associationsMap);
	            
	            
				getProviderInformation(providerDetails, morbReportVO);
				
				ArrayList<Object>  facilityDetailsDRRQ = osd.getReportingFacilityInfo(morbidityUid,NEDSSConstants.MOB_REPORTER_OF_MORB_REPORT);
				 if(facilityDetailsDRRQ!=null && facilityDetailsDRRQ.size()>0 && facilityDetailsDRRQ.get(0)!=null && morbReportVO != null){
					 morbReportVO.setReportingFacility(facilityDetailsDRRQ.get(0).toString());
				}
				
	              if(isCDCPrintForm){
	            	  ProviderDataForPrintVO providerDataForPrintVO  = new ProviderDataForPrintVO();
	            	  morbReportVO.setProviderDataForPrintVO(providerDataForPrintVO);
	            	  if(providerUid!=null && MorbUidAsSourceForInvestigation !=null){
	            		  osd.getOrderingPersonAddress(providerDataForPrintVO, providerUid);
                    	  osd.getOrderingPersonPhone(providerDataForPrintVO, providerUid);
	            	  }
	            	  if(MorbUidAsSourceForInvestigation!=null){
		            	  Long reportingFacilityUid=null;
		            	  ArrayList<Object>  facilityDetails = osd.getReportingFacilityInfo(morbidityUid,NEDSSConstants.MOB_REPORTER_OF_MORB_REPORT);
		            	  if(facilityDetails!=null && facilityDetails.size()>0 && morbReportVO != null){
		            		  Object[] facility = facilityDetails.toArray();
		            		  if (facility[0] != null) {
			                	  morbReportVO.getProviderDataForPrintVO().setFacility((String) facility[0]);
			                    logger.debug("FacilityName: " + (String) facility[0]);
			                  }
			                  if (facility[1] != null)
			                	  reportingFacilityUid =((Long) facility[1]);
			                  logger.debug("FacilityUid: " + (Long) facility[1]);
			                  
			                  if(reportingFacilityUid!=null){
			                	  osd.getOrderingFacilityAddress(providerDataForPrintVO, reportingFacilityUid);
			                	  osd.getOrderingFacilityPhone(providerDataForPrintVO, reportingFacilityUid);			                	  
			                  }
		            	  }
	            	  }
	            	  
		            }
	            // separate the event Morb and the summary morb.
	            if(uidType.equals("PERSON_PARENT_UID"))
	            {
		            if (morbReportVO.getRecordStatusCd()!=null && (morbReportVO.getRecordStatusCd().equals("UNPROCESSED")||morbReportVO.getRecordStatusCd().equals("UNPROCESSED_PREV_D"))) {
		            	
		            	morbReportSummaryVO = morbReportVO ;
	        			morbReportSummaryVO.setMPRUid(uid);
	        		}
	        		if(morbReportVO.getRecordStatusCd()!=null && !morbReportVO.getRecordStatusCd().equals("LOG_DEL")){
	        			
	        			morbReportEventVO = morbReportVO ;
	        			morbReportEventVO.setMPRUid(uid);
	        		}
	            }else if(uidType.equals("MORBIDITY_UID"))
	            {
	            	ArrayList<Object>  mprUidList = osd.getPatientPersonUid(NEDSSConstants.MOB_SUBJECT_OF_MORB_REPORT,morbidityUid);
	                 if(mprUidList != null && mprUidList.size()>0)
	                 {
	                	 Object[] vals = mprUidList.toArray();
	                	 if(morbReportVO.getRecordStatusCd()!=null && (morbReportVO.getRecordStatusCd().equals("UNPROCESSED")||morbReportVO.getRecordStatusCd().equals("UNPROCESSED_PREV_D")))
	                	 {
	                		 morbReportSummaryVO = null;
	                		 morbReportSummaryVO = morbReportVO ;
	                		 morbReportSummaryVO.setMPRUid((Long)vals[0]);
	                	 }
	                	 if(morbReportVO.getRecordStatusCd()!=null && !morbReportVO.getRecordStatusCd().equals("LOG_DEL")){
	                		morbReportEventVO = null; 
	 	        			morbReportEventVO = morbReportVO ;
	 	        			morbReportEventVO.setMPRUid((Long)vals[0]);
	 	        		}	 
	                    
	                 }
	            }

	            
	            
	            rpt = (ArrayList<Object> ) preparedStmtMethod(rptValue, rptArgs,
	                                                 WumSqlQuery.SELECT_REPORT_TYPE,
	                                                 NEDSSConstants.SELECT);

	            if (rpt != null) {
	              Iterator<Object> rptValueIter = rpt.iterator();
	              while (rptValueIter.hasNext()) {
	                rptValue = (MorbReportSummaryVO) rptValueIter.next();
	                setValue = rptValue.getReportType();
	              }
	            }
	           if(morbReportSummaryVO != null) 
	        	   morbReportSummaryVO.setReportType(setValue);
	           if(morbReportEventVO != null) 
	        	   morbReportEventVO.setReportType(setValue);

	            
	            //ArrayList<Object> mprUidList = osd.getPatientPersonUid(NEDSSConstants.MOB_SUBJECT_OF_MORB_REPORT,morbidityUid);
	           // ArrayList<Object> mprUidList = argList ;
	          //  if(mprUidList != null && mprUidList.size()>0)
	          //  {
	          //     Object[] vals = mprUidList.toArray();
	         //      morbReportSummaryVO.setMPRUid((Long)vals[0]);
	         //   }
	            ArrayList<Object>  valList = osd.getPatientPersonInfoMorb(morbidityUid);
	            

	           /* if (valList!= null && valList.size() > 0) {
	              Object[] vals = valList.toArray();
	              if (vals[0] != null)
	            	  morbReportEventVO.setPatientFirstName( (String) vals[0]);
	              if (vals[1] != null)
	            	  morbReportEventVO.setPatientLastName( (String) vals[1]);
	              if (vals[2] != null)
	            	  morbReportEventVO.setType( ( (String) vals[2]));
	            //  if (vals[3] != null)
	            //    morbReportSummaryVO.setMPRUid( (Long) vals[3]); 
	            }*/
	            if(morbReportSummaryVO !=null)
	            {
	            	morbReportSummaryVOColl.add(morbReportSummaryVO);
	            }
	            if(morbReportEventVO !=null)
	            	morbReportEventVOColl.add(morbReportEventVO);

	            /****** TREATMENT SUMMARY VO'S ******/

	            UidSummaryVO treatmentUid = new UidSummaryVO();
	            argList.clear();
	            argList.add(morbidityUid);
	            treatmentUids = (ArrayList<Object> ) preparedStmtMethod(treatmentUid,
	                argList, WumSqlQuery.SELECT_TREATMENT_TARGETS,
	                NEDSSConstants.SELECT);

	            Iterator<Object> iter = treatmentUids.iterator();
	            ArrayList<Object>  treatmentColl = new ArrayList<Object> ();
	            while (iter.hasNext()) {
	              UidSummaryVO vo = (UidSummaryVO) iter.next();
	              Long obsUid = vo.getUid();
	              treatmentColl.add(obsUid);

	            }
	            RetrieveSummaryVO retrieveSummaryVO = new RetrieveSummaryVO();
	            Collection<Object> treatmentSummaryVOs = new ArrayList<Object> (retrieveSummaryVO.
	                retrieveTreatmentSummaryVOList(treatmentColl, nbsSecurityObj));
	            if(treatmentSummaryVOs != null && treatmentSummaryVOs.size()>0 && morbReportSummaryVO != null && (morbReportSummaryVO.getObservationUid() == morbidityUid))
	            	morbReportSummaryVO.setTheTreatmentSummaryVOColl(treatmentSummaryVOs);
	            if(treatmentSummaryVOs != null && treatmentSummaryVOs.size()>0 && morbReportEventVO != null && (morbReportEventVO.getObservationUid() == morbidityUid))
	            	morbReportEventVO.setTheTreatmentSummaryVOColl(treatmentSummaryVOs);

	            /*
	                 treatmentPreparedStmt.setLong(1, morbReportSummaryVO.getObservationUid()) ;
	                   treatmentResultSet = treatmentPreparedStmt.executeQuery();
	                   while (labResultSet.next()){
	             treatmentUids.add(treatmentResultSet.getLong("source_act_uid"));
	                   }
	                 RetrieveSummaryVO retrieveSummaryVO = new RetrieveSummaryVO();
	                   Collection<Object>  treatmentSummaryVOs = retrieveSummaryVO.retrieveTreatmentSummaryVOList(treatmentUids);
	                 morbReportSummaryVO.setTheTreatmentSummaryVOColl(treatmentSummaryVOs);
	             */

	            /** END OF TREATMENT SUMMARY VO'S **/

	            /** LAB REPORT SUMMARY VO'S **/
	            UidSummaryVO labReportUid = new UidSummaryVO();
	            argList.clear();
	            argList.add(morbidityUid);
	            labReportUids = (ArrayList<Object> ) preparedStmtMethod(labReportUid,
	                argList, WumSqlQuery.SELECT_LAB_TARGETS, NEDSSConstants.SELECT);
	            LabReportSummaryVO labReportSummaryVOs = new LabReportSummaryVO();
	            Collection<Object> newLabReportSummaryVOCollection  = new ArrayList<Object> ();
	            Collection<Object> labReportSummaryVOCollection  = new ArrayList<Object> ();
	            Collection<Object> labColl = new ArrayList<Object> ();
	            HashMap<Object, Object> labReportMap = new HashMap<Object, Object>();
	            /*If the user has permission to see the Lab */
	            String labUidType = "LABORATORY_UID";
	            if(nbsSecurityObj.getPermission(NBSBOLookup.OBSERVATIONLABREPORT,NBSOperationLookup.VIEW))
	            { labReportMap = retrieveLabReportSummaryRevisited(labReportUids, false, nbsSecurityObj, labUidType);

	           
	            }
	            
	            
	            if(!labReportMap.isEmpty())
	              {
	            	  if(labReportMap.containsKey("labSummList") && morbReportSummaryVO != null)
	            	  {
	            		  newLabReportSummaryVOCollection  = (ArrayList<Object> )labReportMap.get("labSummList");
	            		  Iterator<Object> ite = newLabReportSummaryVOCollection.iterator();
	            		  while( ite.hasNext())
	            		  {
	            			 labReportSummaryVOs = (LabReportSummaryVO) ite. next();
	            			 labColl.add(labReportSummaryVOs);
	            			 morbReportSummaryVO.setTheLabReportSummaryVOColl(labColl);
	            		  }
	            	  }
	            	  if(labReportMap.containsKey("labEventList") && morbReportEventVO != null)
	            	  {
	            		  labReportSummaryVOCollection  =(ArrayList<Object> )labReportMap.get("labEventList");
	            		  Iterator<Object> ite = labReportSummaryVOCollection.iterator();
	            		  while( ite.hasNext())
	            		  {
	            			  labReportSummaryVOs = (LabReportSummaryVO) ite.next();
	            			  labColl.add(labReportSummaryVOs);
	            			  morbReportEventVO.setTheLabReportSummaryVOColl(labColl);
	            			  
	            		  }
	            	  }
	              }
	           
	            /****** END OF LAB REPORT SUMMAY VO'S *****/
	          }
	        }
	      }
	    }
	    catch (Exception ex) {
	      logger.fatal(
	          "Error while getting observations(morb) for given set of UIDS in workup",
	          ex);
	      throw new NEDSSSystemException(ex.toString());
	    }
	    this.populateDescTxtFromCachedValues(morbReportEventVOColl);
	    this.populateDescTxtFromCachedValues(morbReportSummaryVOColl);
	    HashMap<Object, Object> returnMap = new HashMap<Object, Object>();
	    returnMap.put("MorbSummColl", morbReportSummaryVOColl);
	    returnMap.put("MorbEventColl", morbReportEventVOColl);
	    return returnMap; 	  
  }
  

  
  
  /**
   * Goes to the database to obtain the Program Area Codes based on the specified parameters.
   * @param codeVector : Vector
   * @param reportingLabCLIA : String
   * @param nextLookUp : String
   * @param type : String - LN for Loinc, SNM for Snomed, LT for Local Test and LR for local result.
   * @return String
   *
   */
  // AK 7/25/04
  private String getProgAreaCd(Vector<Object> codeVector, String reportingLabCLIA,
                               String nextLookUp, String type) {

    if (codeVector == null || codeVector.size() == 0)
      return null;

    ArrayList<Object>  progAreaCdList = null;
    Vector<Object> toReturn = new Vector<Object>();
    SRTMapDAOImpl dao = new SRTMapDAOImpl();
    String lastPACode = null;

    try {
      for (int k = 0; k < codeVector.size(); k++) {
        progAreaCdList = (ArrayList<Object> ) dao.getProgAreaCd( (String) codeVector.
            elementAt(k), type, reportingLabCLIA);

        // The above method returns the count of PAs found at
        // index 1 and program area at index 0

        // Return null if we got more than one PA
        int count = ( (Integer) progAreaCdList.get(1)).intValue();
        if (count != 1)
          return null;

        String currentPAcode = progAreaCdList.get(0).toString();

        // Compare with previously retrieved PA and return null if they are different.
        if (lastPACode == null)
          lastPACode = currentPAcode;
        else if (!currentPAcode.equals(lastPACode))
          return null;
      } //end of for
    }
    catch (Exception e) {
      e.printStackTrace();
      return null; //break out
    } //end of catch
    return lastPACode;
  } //end of getProgAreaCd()

  /**
   * Returns the code that will be used to help resolve the program area cd
   * @param obsDt : ObservationDT
   * @return code : String
   */
  private String getLocalTestCode(ObservationDT obsDt)
  {
    String code = null;
    if (obsDt != null)
    {
      if (obsDt.getCdSystemCd() != null)
      {
        if (obsDt.getCd() != null && !obsDt.getCd().equals("") &&
            !obsDt.getCd().equals(" "))
        {
          code = obsDt.getCd();
        }
      } //end of if
    } //end of if
    return code;
  } //end of getLocalTestColl

  /**
   * Attempts to resolve the Program Area Cd based on the Local Default Cd.  If
   * more than one type of Program Area Cd is resolved, then return null.  If no
   * Program Area Cd is resolved, return nextLookup value.
   * @param codeVector : Vector
   * @param reportingLabCLIA : String
   * @param nextLookup : String
   * @param sql : String
   * @return String
   */
  // AK 7/25/03
  private String getProgAreaCdLocalDefault(Vector<Object> codeVector,
                                           String reportingLabCLIA,
                                           String nextLookup, String sql) {
    Vector<Object> toReturn = new Vector<Object>();
    SRTMapDAOImpl dao = new SRTMapDAOImpl();
    String lastPACode = null;
    try {
      for (int k = 0; k < codeVector.size(); k++) {
        Collection<Object> defaultPACColl = dao.getProgAreaCdLocalDefault(
            codeVector.elementAt(k).toString(),
            reportingLabCLIA, sql);
        if (defaultPACColl.size() == 1) {
          String currentPACode = defaultPACColl.iterator().next().toString();
          // Compare with previously retrieved PA and return null if they are different.
          if (lastPACode == null)
            lastPACode = currentPACode;
          else if (!currentPACode.equals(lastPACode))
            return null;
        }
      }
    }
    catch (Exception e) {
      e.printStackTrace();
      return null; //????leave observation.progAreaCd == null?????
    } //end of catch
    return lastPACode;
  } //end of getProgAreaCdLocalDefault(...)

  public String getTestCode(String TestNameCollection, String labCLIA,
                            Long labUid,
                            String programAreaCd) throws NEDSSSystemException {
    String code = null;
    return code;
  }


  public boolean getOrganismReqdIndicatorForResultedTest(String
      testNameCondition,
      String labClia,
      Long labUid,
      String programAreaCd) {
    //String rtTestCode = getTestCodeForResultedTest(testNameCondition,
    //    labClia,
    //    labUid,
    //    programAreaCd);

    if (testNameCondition == null || testNameCondition.trim().length() == 0)
      return false;

    String indicator = new SRTMapDAOImpl().
        getOrganismReqdIndicatorForResultedTest(testNameCondition);

    if (indicator != null && indicator.equalsIgnoreCase(NEDSSConstants.YES)) {
      return true;
    }
    else {
      return false;
    }
  }

  public ArrayList<Object>  findOrderedTestName(String clia, Long labId,
                                       String searchString, String testType,
                                       int cacheNumber, int fromIndex) {
    ArrayList<Object>  resultList = new ArrayList<Object> ();
    StringBuffer query = new StringBuffer();
    EntityProxyHelper entityProxyHelper = EntityProxyHelper.getInstance();

    if ( labId != null && (clia == null || clia.equals("")) ) {
      clia = entityProxyHelper.organizationCLIALookup(labId);
    }
    if (clia == null || clia.equals(""))
      clia = NEDSSConstants.DEFAULT;

    String databaseType = propertyUtil.getDatabaseServerType();
    if (testType.equals(NEDSSConstants.TEST_TYPE_LOCAL)) {

      if(databaseType != null && databaseType.equalsIgnoreCase(NEDSSConstants.ORACLE_ID)){
        String newQuery = NEDSSSqlQuery.SELECT_ORDERED_TEST_SEARCH_ORACLE;
        String where = " and ( upper(LAB_TEST_DESC_TXT)  like '%" +
            searchString.toUpperCase() +
            "%') and LABORATORY_ID = '" + clia + "' order by LAB_TEST_DESC_TXT";
        query.append(newQuery);
        query.append(where);

        resultList = findObservationByName(cacheNumber, fromIndex, query,
                                           "ordered");
      }
      else{
        String newQuery = NEDSSSqlQuery.SELECT_ORDERED_TEST_SEARCH_SQL;
        String where = " and ( upper(LAB_TEST_DESC_TXT)  like '%" +
            searchString.toUpperCase() +
            "%') and LABORATORY_ID = '" + clia + "' order by LAB_TEST_DESC_TXT";
        query.append(newQuery);
        query.append(where);

        resultList = findObservationByName(cacheNumber, fromIndex, query,
                                           "ordered");
      }
    }
    else if (testType.equals(NEDSSConstants.TEST_TYPE_LOINC)) {
       if(databaseType != null && databaseType.equalsIgnoreCase(NEDSSConstants.ORACLE_ID)){
         String newQuery = NEDSSSqlQuery.SELECT_RESULTED_ORDERED_LOINC_TEST_SEARCH_ORACLE;
         String where = "and ( upper( Loinc_code.component_name)  like '%" +
             searchString.toUpperCase() +
             "%') order by Loinc_code.component_name";
         query.append(newQuery);
         query.append(where);
        // System.out.println(query.toString());
         resultList = findLoincObservationByName(cacheNumber, fromIndex, query,
                                                 "ordered");
       }
       else{
         String newQuery = NEDSSSqlQuery.SELECT_RESULTED_ORDERED_LOINC_TEST_SEARCH_SQL;
         String where = "and ( upper( Loinc_code.component_name)  like '%" +
             searchString.toUpperCase() +
             "%') order by Loinc_code.component_name";
         query.append(newQuery);
         query.append(where);
         //System.out.println(query.toString());
         resultList = findLoincObservationByName(cacheNumber, fromIndex, query,
                                                 "ordered");

       }
    }

    return resultList;
  }

  public ArrayList<Object>  findResultedTestName(String clia, Long labId,
                                        String searchString, String testType,
                                        int cacheNumber, int fromIndex) {
    ArrayList<Object>  resultList = new ArrayList<Object> ();
    StringBuffer query = new StringBuffer();
    EntityProxyHelper entityProxyHelper = EntityProxyHelper.getInstance();
    String databaseType = propertyUtil.getDatabaseServerType();

    //BB - 12/14/04 - civil00012111 - Add check for null labId and else condition
    //to set clia to DEFAULT
    if ( labId != null && (clia == null || clia.equals("")) ) {
      clia = entityProxyHelper.organizationCLIALookup(labId);
    }

    if (clia == null || clia.equals(""))
      clia = NEDSSConstants.DEFAULT;

    if (testType.equals(NEDSSConstants.TEST_TYPE_LOCAL)) {
      if(databaseType != null && databaseType.equalsIgnoreCase(NEDSSConstants.ORACLE_ID)){
        String newQuery = NEDSSSqlQuery.SELECT_RESULTED_TEST_SEARCH_ORACLE;
        String where = " and ( upper(LAB_TEST_DESC_TXT)  like '%" +
            searchString.toUpperCase() +
            "%') and LABORATORY_ID = '" + clia + "' order by LAB_TEST_DESC_TXT";
        query.append(newQuery);
        query.append(where);
        resultList = findObservationByName(cacheNumber, fromIndex, query,
                                           "resulted");
      }
      else{
        String newQuery = NEDSSSqlQuery.SELECT_RESULTED_TEST_SEARCH_SQL;
        String where = " and ( upper(LAB_TEST_DESC_TXT)  like '%" +
            searchString.toUpperCase() +
            "%') and LABORATORY_ID = '" + clia + "' order by LAB_TEST_DESC_TXT";
        query.append(newQuery);
        query.append(where);
        resultList = findObservationByName(cacheNumber, fromIndex, query,"resulted");

      }
    }
    else if (testType.equals(NEDSSConstants.TEST_TYPE_LOINC)) {
      if(databaseType != null && databaseType.equalsIgnoreCase(NEDSSConstants.ORACLE_ID)){
        String newQuery = NEDSSSqlQuery.SELECT_RESULTED_ORDERED_LOINC_TEST_SEARCH_ORACLE;
        String where = "and ( upper( loinc_code.component_name)  like '%" +
            searchString.toUpperCase() +
            "%') order by Loinc_code.component_name";
        query.append(newQuery);
        query.append(where);
        resultList = findLoincObservationByName(cacheNumber, fromIndex, query,
                                                "resulted");
      }else{
        String newQuery = NEDSSSqlQuery.SELECT_RESULTED_ORDERED_LOINC_TEST_SEARCH_SQL;
        String where = "and ( upper( Loinc_code.component_name)  like '%" +
            searchString.toUpperCase() +
            "%') order by Loinc_code.component_name";
        query.append(newQuery);
        query.append(where);
        resultList = findLoincObservationByName(cacheNumber, fromIndex, query,
                                                "resulted");

      }
    }

    return resultList;
  }
  
	public ArrayList<Object> findLabResultedTestName(String clia, Long labId,
			String searchString, String testType) {
		ArrayList<Object> resultList = new ArrayList<Object>();
		StringBuffer query = new StringBuffer();
		EntityProxyHelper entityProxyHelper = EntityProxyHelper.getInstance();
		String databaseType = propertyUtil.getDatabaseServerType();

		if (labId != null && (clia == null || clia.equals(""))) {
			clia = entityProxyHelper.organizationCLIALookup(labId);
		}

		if (clia == null || clia.equals(""))
			clia = NEDSSConstants.DEFAULT;

		if (testType.equals(NEDSSConstants.TEST_TYPE_LOCAL)) {
			if (databaseType != null
					&& databaseType.equalsIgnoreCase(NEDSSConstants.ORACLE_ID)) {
				String newQuery = NEDSSSqlQuery.SELECT_RESULTED_TEST_SEARCH_ORACLE;
				String where = " and ( upper(LAB_TEST_DESC_TXT)  like '%"
						+ searchString.toUpperCase()
						+ "%' OR upper(LAB_TEST_CD) LIKE  '%"
						+ searchString.toUpperCase()
						+ "%') and LABORATORY_ID = '" + clia
						+ "' order by LAB_TEST_DESC_TXT";
				query.append(newQuery);
				query.append(where);
				resultList = findObservationByName(query, "resulted");
			} else {
				String newQuery = NEDSSSqlQuery.SELECT_RESULTED_TEST_SEARCH_SQL;
				String where = " and ( upper(LAB_TEST_DESC_TXT)  like '%"
						+ searchString.toUpperCase()
						+ "%'  OR upper(LAB_TEST_CD) LIKE  '%"
						+ searchString.toUpperCase()
						+ "%') and LABORATORY_ID = '" + clia
						+ "' order by LAB_TEST_DESC_TXT";
				query.append(newQuery);
				query.append(where);
				resultList = findObservationByName(query, "resulted");

			}
		} else if (testType.equals(NEDSSConstants.TEST_TYPE_LOINC)) {
			if (databaseType != null
					&& databaseType.equalsIgnoreCase(NEDSSConstants.ORACLE_ID)) {
				String newQuery = NEDSSSqlQuery.SELECT_RESULTED_ORDERED_LOINC_TEST_SEARCH_ORACLE;
				String where = "and ( upper( loinc_code.component_name)  like '%"
						+ searchString.toUpperCase()
						+ "%' OR  upper(loinc_code.loinc_cd) like '%"
						+ searchString.toUpperCase()
						+ "%') order by Loinc_code.component_name";
				query.append(newQuery);
				query.append(where);
				resultList = findLoincObservationByName(query, "resulted");
			} else {
				String newQuery = NEDSSSqlQuery.SELECT_RESULTED_ORDERED_LOINC_TEST_SEARCH_SQL;
				String where = "and ( upper( Loinc_code.component_name)  like '%"
						+ searchString.toUpperCase()
						+ "%' OR  upper(loinc_code.loinc_cd) like '%"
						+ searchString.toUpperCase()
						+ "%') order by Loinc_code.component_name";
				query.append(newQuery);
				query.append(where);
				resultList = findLoincObservationByName(query, "resulted");

			}
		}

		return resultList;
	}
	
	public ArrayList<Object> findLabCodedResult(String clia, Long labId,
			String searchString, String testType) {
		ArrayList<Object> resultList = new ArrayList<Object>();
		StringBuffer query = new StringBuffer();
		EntityProxyHelper entityProxyHelper = EntityProxyHelper.getInstance();
		String databaseType = propertyUtil.getDatabaseServerType();

		if (labId != null && (clia == null || clia.equals(""))) {
			clia = entityProxyHelper.organizationCLIALookup(labId);
		}

		if (clia == null || clia.equals(""))
			clia = NEDSSConstants.DEFAULT;

		if (testType.equals(NEDSSConstants.RESULT_TYPE_LOCAL)) {
			if (databaseType != null
					&& databaseType.equalsIgnoreCase(NEDSSConstants.ORACLE_ID)) {
				String newQuery = NEDSSSqlQuery.SELECT_CODED_RESULT_SEARCH_ORACLE;
				String where = " and ( upper(LAB_RESULT_DESC_TXT)  like '%"
						+ searchString.toUpperCase()
						+ "%') and LABORATORY_ID = '" + clia
						+ "' order by LAB_RESULT_DESC_TXT";
				query.append(newQuery);
				query.append(where);
				resultList = findCodedResultByName(query, "resulted");
			} else {
				String newQuery = NEDSSSqlQuery.SELECT_CODED_RESULT_SEARCH_SQL;
				String where = " and ( upper(LAB_RESULT_DESC_TXT)  like '%"
						+ searchString.toUpperCase()
						+ "%') and LABORATORY_ID = '" + clia
						+ "' order by LAB_RESULT_DESC_TXT";
				query.append(newQuery);
				query.append(where);
				resultList = findCodedResultByName(query, "resulted");

			}
		} else if (testType.equals(NEDSSConstants.RESULT_TYPE_SNOMED)) {
			if (databaseType != null
					&& databaseType.equalsIgnoreCase(NEDSSConstants.ORACLE_ID)) {
				String newQuery = NEDSSSqlQuery.SELECT_CODED_RESULT_SNOMED_SEARCH_ORACLE;
				String where = " ( upper( snomed_desc_txt)  like '%"
						+ searchString.toUpperCase()
						+ "%') order by snomed_desc_txt";
				query.append(newQuery);
				query.append(where);
				resultList = findCodedResultByName(query, "resulted");
			} else {
				String newQuery = NEDSSSqlQuery.SELECT_CODED_RESULT_SNOMED_SEARCH_SQL;
				String where = " ( upper( snomed_desc_txt)  like '%"
						+ searchString.toUpperCase()
						+ "%') order by snomed_desc_txt";
				query.append(newQuery);
				query.append(where);
				resultList = findCodedResultByName(query, "resulted");

			}
		}

		return resultList;
	}
	
	public ArrayList<Object> findLabCodedResultByCode(String clia, Long labId,
			String searchCode, String testType) {
		ArrayList<Object> resultList = new ArrayList<Object>();
		StringBuffer query = new StringBuffer();
		EntityProxyHelper entityProxyHelper = EntityProxyHelper.getInstance();
		String databaseType = propertyUtil.getDatabaseServerType();

		if (labId != null && (clia == null || clia.equals(""))) {
			clia = entityProxyHelper.organizationCLIALookup(labId);
		}

		if (clia == null || clia.equals(""))
			clia = NEDSSConstants.DEFAULT;

		if (testType == null || testType.equals(NEDSSConstants.RESULT_TYPE_LOCAL)) {
			if (databaseType != null
					&& databaseType.equalsIgnoreCase(NEDSSConstants.ORACLE_ID)) {
				String newQuery = NEDSSSqlQuery.SELECT_CODED_RESULT_SEARCH_ORACLE;
				String where = " and ( lab_result_cd  = '"
						+ searchCode
						+ "') and LABORATORY_ID = '" + clia
						+ "' order by LAB_RESULT_DESC_TXT";
				query.append(newQuery);
				query.append(where);
				resultList.addAll(findCodedResultByName(query, "resulted"));
			} else {
				String newQuery = NEDSSSqlQuery.SELECT_CODED_RESULT_SEARCH_SQL;
				String where = " and ( lab_result_cd = '"
						+ searchCode
						+ "') and LABORATORY_ID = '" + clia
						+ "' order by LAB_RESULT_DESC_TXT";
				query.append(newQuery);
				query.append(where);
				resultList.addAll(findCodedResultByName(query, "resulted"));

			}
		} 
		if (testType == null || testType.equals(NEDSSConstants.RESULT_TYPE_SNOMED)) {
			query = new StringBuffer();
			if (databaseType != null
					&& databaseType.equalsIgnoreCase(NEDSSConstants.ORACLE_ID)) {
				String newQuery = NEDSSSqlQuery.SELECT_CODED_RESULT_SNOMED_SEARCH_ORACLE;
				String where = " ( snomed_cd = '"
						+ searchCode
						+ "') order by snomed_desc_txt";
				query.append(newQuery);
				query.append(where);
				resultList.addAll(findCodedResultByName(query, "resulted"));
			} else {
				String newQuery = NEDSSSqlQuery.SELECT_CODED_RESULT_SNOMED_SEARCH_SQL;
				String where = " ( snomed_cd = '"
						+ searchCode
						+ "') order by snomed_desc_txt";
				query.append(newQuery);
				query.append(where);
				resultList.addAll(findCodedResultByName(query, "resulted"));

			}
		}

		return resultList;
	}
  
	public ArrayList<Object> findLabResultedTestByCode(String clia, Long labId,
			String searchCode, String testType) {
		ArrayList<Object> resultList = new ArrayList<Object>();
		StringBuffer query = new StringBuffer();
		EntityProxyHelper entityProxyHelper = EntityProxyHelper.getInstance();
		String databaseType = propertyUtil.getDatabaseServerType();

		if (labId != null && (clia == null || clia.equals(""))) {
			clia = entityProxyHelper.organizationCLIALookup(labId);
		}

		if (clia == null || clia.equals(""))
			clia = NEDSSConstants.DEFAULT;

		if (testType == null || testType.equals(NEDSSConstants.TEST_TYPE_LOCAL)) {
			if (databaseType != null
					&& databaseType.equalsIgnoreCase(NEDSSConstants.ORACLE_ID)) {
				String newQuery = NEDSSSqlQuery.SELECT_RESULTED_TEST_SEARCH_ORACLE;
				String where = " and ( LAB_TEST_CD  = '"
						+ searchCode
						+ "') and LABORATORY_ID = '" + clia
						+ "' order by LAB_TEST_DESC_TXT";
				query.append(newQuery);
				query.append(where);
				resultList.addAll(findObservationByName(query, "resulted"));
			} else {
				String newQuery = NEDSSSqlQuery.SELECT_RESULTED_TEST_SEARCH_SQL;
				String where = " and ( LAB_TEST_CD  = '"
						+ searchCode
						+ "') and LABORATORY_ID = '" + clia
						+ "' order by LAB_TEST_DESC_TXT";
				query.append(newQuery);
				query.append(where);
				resultList.addAll(findObservationByName(query, "resulted"));

			}
		} 
		if (testType == null || testType.equals(NEDSSConstants.TEST_TYPE_LOINC)) {
			query = new StringBuffer();
			if (databaseType != null
					&& databaseType.equalsIgnoreCase(NEDSSConstants.ORACLE_ID)) {
				String newQuery = NEDSSSqlQuery.SELECT_RESULTED_ORDERED_LOINC_TEST_SEARCH_ORACLE;
				String where = "and ( loinc_code.loinc_cd  = '"
						+ searchCode
						+ "') order by Loinc_code.component_name";
				query.append(newQuery);
				query.append(where);
				resultList.addAll(findLoincObservationByName(query, "resulted"));
			} else {
				String newQuery = NEDSSSqlQuery.SELECT_RESULTED_ORDERED_LOINC_TEST_SEARCH_SQL;
				String where = "and ( Loinc_code.loinc_cd  = '"
						+ searchCode
						+ "') order by Loinc_code.component_name";
				query.append(newQuery);
				query.append(where);
				resultList.addAll(findLoincObservationByName(query, "resulted"));

			}
		}

		return resultList;
	}

  public ArrayList<Object>  findDrugsByName(String clia, Long labId, String searchString,
                                   String testType, String method,
                                   int cacheNumber, int fromIndex) {

    ArrayList<Object>  resultList = new ArrayList<Object> ();
    StringBuffer query = new StringBuffer();
    EntityProxyHelper entityProxyHelper = EntityProxyHelper.getInstance();

    if (clia.equals("") || clia == null) {
      clia = entityProxyHelper.organizationCLIALookup(labId);
    }

    String databaseType = propertyUtil.getDatabaseServerType();
    if (testType.equals(NEDSSConstants.TEST_TYPE_LOCAL)) {
      if(databaseType != null && databaseType.equalsIgnoreCase(NEDSSConstants.ORACLE_ID)){
        String newQuery = NEDSSSqlQuery.SELECT_DRUG_SEARCH_ORACLE;
        String where = " and ( upper( lab_test.LAB_TEST_DESC_TXT)  like '%" +
            searchString.toUpperCase() +
            "%')" + //removed 10/11/04, BB:  and lab_coding_system.laboratory_id = '" + clia +"'"+
            " order by LAB_TEST_DESC_TXT";
        query.append(newQuery);
        query.append(where);
        resultList = findObservationByName(cacheNumber, fromIndex, query,
                                           "drug");
      }else{
        String newQuery = NEDSSSqlQuery.SELECT_DRUG_SEARCH_SQL;
        String where = " and ( upper( lab_test.LAB_TEST_DESC_TXT)  like '%" +
            searchString.toUpperCase() +
            "%')" + //removed 10/11/04, BB:and Lab_coding_system.laboratory_id = '" + clia +"'"+
            " order by LAB_TEST_DESC_TXT";
        query.append(newQuery);
        query.append(where);
        resultList = findObservationByName(cacheNumber, fromIndex, query,
                                           "drug");

      }
    }
    else if (testType.equals(NEDSSConstants.TEST_TYPE_LOINC)) {
      String meth = "";
      String newQuery;
      if(databaseType != null && databaseType.equalsIgnoreCase(NEDSSConstants.ORACLE_ID)){
        newQuery = NEDSSSqlQuery.SELECT_DRUG_LOINC_SEARCH_ORACLE;
      }
      else{
        newQuery = NEDSSSqlQuery.SELECT_DRUG_LOINC_SEARCH_SQL;
      }

      String where = "and ( upper( Loinc_code.component_name)  like '%" +
          searchString.toUpperCase() + "%') ";
      query.append(newQuery);
      query.append(where);
      method = method.trim();

      if ( (! (method.equals("")))) {
        meth = " and Loinc_code.method_type='" + method.toUpperCase() + "'  order by Loinc_code.component_name";
      }
      else {
        meth = " and Loinc_code.method_type IS NULL order by Loinc_code.component_name";
      }
      query.append(meth);
      resultList = findLoincObservationByName(cacheNumber, fromIndex, query,
                                              "drug");
    }
//System.out.println("query-->" + query.toString());
    return resultList;
  }

  public ArrayList<Object>  findOrganismsByName(String clia, Long labId,
                                       String searchString, String testType,
                                       int cacheNumber, int fromIndex) {
    ArrayList<Object>  resultList = new ArrayList<Object> ();
    StringBuffer query = new StringBuffer();
    EntityProxyHelper entityProxyHelper = EntityProxyHelper.getInstance();
    String databaseType = propertyUtil.getDatabaseServerType();
    if (clia.equals("") || clia == null) {
      clia = entityProxyHelper.organizationCLIALookup(labId);
    }
    if(databaseType != null && databaseType.equalsIgnoreCase(NEDSSConstants.ORACLE_ID)){
			String newQuery = NEDSSSqlQuery.SELECT_ORGANISM_SEARCH_ORACLE;
			String where = " (upper(SNOMED_DESC_TXT)  like '%" +
					searchString.toUpperCase() +
					"%') order by SNOMED_DESC_TXT";
			query.append(newQuery);
			query.append(where);

			resultList = findObservationByName(cacheNumber, fromIndex, query,
                                       "organism");
    }
    else{
      String newQuery = NEDSSSqlQuery.SELECT_ORGANISM_SEARCH_SQL;
      String where = " (upper(SNOMED_DESC_TXT)  like '%" +
					searchString.toUpperCase() +
					"%') order by SNOMED_DESC_TXT";
      query.append(newQuery);
      query.append(where);

      resultList = findObservationByName(cacheNumber, fromIndex, query,
                                      "organism");

    }
    return resultList;
  }

  @SuppressWarnings("unchecked")
private ArrayList<Object>  findObservationByName(int cacheNumber, int fromIndex,
                                          StringBuffer query, String type) throws
      NEDSSSystemException {
    ArrayList<Object>  searchResult = new ArrayList<Object> ();
    int totalCount = 0;
    int listCount = 0;
    DisplayObservationList displayObservationList = null;
    ObservationSearchResultTmp searchResultName = new
        ObservationSearchResultTmp();

    //@@ System.out.println("query-----> \n" + query.toString());
    DAOBase dao = new DAOBase();
    ArrayList<Object>  resultList = (ArrayList<Object> ) dao.preparedStmtMethod(searchResultName, null,
        query.toString(), NEDSSConstants.SELECT);

    Iterator<Object> nameItr1 = resultList.iterator();
    CachedDropDownValues cache = new CachedDropDownValues();

    while (nameItr1.hasNext()) {
      ArrayList<Object>  nameList = new ArrayList<Object> ();
      ObservationSrchResultVO srchResultVO = new ObservationSrchResultVO();
      ObservationSearchResultTmp tmp = (ObservationSearchResultTmp) nameItr1.
          next();

			ObservationNameDT nameDT = new ObservationNameDT();

      if (! (tmp.getLaboratoryId() == null)) {

        nameDT.setLaboratoryId(tmp.getLaboratoryId());
        nameDT.setLabTestCd(tmp.getLabTestCd());
        nameDT.setLabTestDescription(tmp.getLabTestDescription());
        nameDT.setLabConditionCd(tmp.getLabConditionCd());
        if (tmp.getLabOrganismIndicator() != null) {
          nameDT.setLabOrganismIndicator(tmp.getLabOrganismIndicator());
        }

        nameList.add(nameDT);
        srchResultVO.setLaboratoryID(nameDT.getLabTestCd());
      }else{
				nameDT.setLaboratoryId("SNM");
				nameDT.setLabTestCd(tmp.getLabTestCd());
				nameDT.setLabTestDescription(tmp.getLabTestDescription());
  			 nameList.add(nameDT);
        srchResultVO.setLaboratoryID(nameDT.getLabTestCd());

			}

      totalCount++;
      srchResultVO.setLabName(type);
      srchResultVO.setPersonNameColl(nameList);
      searchResult.add(srchResultVO);
    } // while (nameItr.hasNext())

    ArrayList<Object>  cacheList = new ArrayList<Object> ();
    for (int j = 0; j < searchResult.size(); j++) {
      ObservationSrchResultVO psvo = new ObservationSrchResultVO();
      psvo = (ObservationSrchResultVO) searchResult.get(j);
      if (fromIndex > searchResult.size()) {
        break;
      }
      if (cacheNumber == listCount) {
        break;
      }
      cacheList.add(searchResult.get(j));
      listCount++;
    }

    ArrayList<Object>  displayList = new ArrayList<Object> ();
		//System.out.println("size-->" + cacheList.size());
    displayObservationList = new DisplayObservationList(totalCount, cacheList,
        fromIndex, listCount);
    displayList.add(displayObservationList);

    return displayList;

  }
  
  @SuppressWarnings("unchecked")
  private ArrayList<Object>  findObservationByName(StringBuffer query, String type) throws
        NEDSSSystemException {
      ArrayList<Object>  searchResult = new ArrayList<Object> ();
      int listCount = 0;
      DisplayObservationList displayObservationList = null;
      ObservationSearchResultTmp searchResultName = new
          ObservationSearchResultTmp();

      //@@ System.out.println("query-----> \n" + query.toString());
      DAOBase dao = new DAOBase();
      ArrayList<Object>  resultList = (ArrayList<Object> ) dao.preparedStmtMethod(searchResultName, null,
          query.toString(), NEDSSConstants.SELECT);

      Iterator<Object> nameItr1 = resultList.iterator();
      ArrayList<Object>  nameList = new ArrayList<Object> ();
      
      while (nameItr1.hasNext()) {
        
        ObservationSearchResultTmp tmp = (ObservationSearchResultTmp) nameItr1.
            next();

  			ObservationNameDT nameDT = new ObservationNameDT();

        if (! (tmp.getLaboratoryId() == null)) {

          nameDT.setLaboratoryId(tmp.getLaboratoryId());
          nameDT.setLabTestCd(tmp.getLabTestCd());
          nameDT.setLabTestDescription(tmp.getLabTestDescription());
          nameDT.setLabConditionCd(tmp.getLabConditionCd());
          if (tmp.getLabOrganismIndicator() != null) {
            nameDT.setLabOrganismIndicator(tmp.getLabOrganismIndicator());
          }

          nameList.add(nameDT);
        }else{
  				nameDT.setLaboratoryId("SNM");
  				nameDT.setLabTestCd(tmp.getLabTestCd());
  				nameDT.setLabTestDescription(tmp.getLabTestDescription());
    			 nameList.add(nameDT);

  			}
      } // while (nameItr.hasNext())

      return nameList;

    }
  
  @SuppressWarnings("unchecked")
  private ArrayList<Object>  findCodedResultByName(StringBuffer query, String type) throws
        NEDSSSystemException {
      ArrayList<Object>  searchResult = new ArrayList<Object> ();
      CodedResultDT codedResultDT = new
    		  CodedResultDT();

      DAOBase dao = new DAOBase();
      searchResult = (ArrayList<Object> ) dao.preparedStmtMethod(codedResultDT, null,
          query.toString(), NEDSSConstants.SELECT);
      
      return searchResult;

      

    }

  @SuppressWarnings("unchecked")
private ArrayList<Object>  findLoincObservationByName(int cacheNumber, int fromIndex,
                                               StringBuffer query, String type) throws
      NEDSSSystemException {
    DAOBase dao = new DAOBase();
    int totalCount = 0;
    int listCount = 0;
    DisplayObservationList displayObservationList = null;
    LoincSearchResultTmp loincResult = new LoincSearchResultTmp();
    //@@ System.out.println("query........." + query);

    ArrayList<Object>  resultList = (ArrayList<Object> ) dao.preparedStmtMethod(loincResult, null,
        query.toString(), NEDSSConstants.SELECT);
    ArrayList<Object>  searchResult = new ArrayList<Object> ();
    Iterator<Object> nameItr1 = resultList.iterator();

    while (nameItr1.hasNext()) {
      ArrayList<Object>  list = new ArrayList<Object> ();
      LoincSearchResultTmp tmp = (LoincSearchResultTmp) nameItr1.next();
      LoincSrchResultVO srchResultVO = new LoincSrchResultVO();
      if (! (tmp.getLoincCd() == null)) {
        LoincResultDT loincDT = new LoincResultDT();

        loincDT.setLoincCd(tmp.getLoincCd());
        loincDT.setLoincComponentName(tmp.getLoincComponentName());
        loincDT.setLoincMethod(tmp.getLoincMethod());
        loincDT.setLoincSystem(tmp.getLoincSystem());
        loincDT.setLoincProperty(tmp.getLoincProperty());
        loincDT.setRelatedClassCd(tmp.getRelatedClassCd());

        if(tmp.getLoincProperty() != null &&
           tmp.getLoincProperty().equalsIgnoreCase("PRID") &&
           tmp.getRelatedClassCd() != null &&
           tmp.getRelatedClassCd().equalsIgnoreCase("MICRO") &&
           tmp.getLoincMethod()!= null){

          String s = tmp.getLoincMethod();
          int length = s.length();
          for(int i=0; i<length;i++)
          {
            char c1 = s.charAt(i);
            if (c1 == 'C') {
              int temp = i +1;
              if( temp != length){
                char c2 = s.charAt(temp);
                if (c2 == 'U'){
                  loincDT.setLabOrganismIndicator("Y");
                  break;
                }
              }

            }
           }
        }
        list.add(loincDT);
        srchResultVO.setLoincCd(tmp.getLoincCd());
      }

      totalCount++;

      srchResultVO.setLabName(type);
      srchResultVO.setLoincColl(list);
      searchResult.add(srchResultVO);
    }

    ArrayList<Object>  cacheList = new ArrayList<Object> ();
    for (int j = 0; j < searchResult.size(); j++) {
      LoincSrchResultVO psvo = new LoincSrchResultVO();
      psvo = (LoincSrchResultVO) searchResult.get(j);
      if (fromIndex > searchResult.size()) {
        break;
      }
      if (cacheNumber == listCount) {
        break;
      }
      cacheList.add(searchResult.get(j));
      listCount++;
    }

    ArrayList<Object>  displayList = new ArrayList<Object> ();
    displayObservationList = new DisplayObservationList(totalCount, cacheList,
        fromIndex, listCount);

    displayList.add(displayObservationList);

    return displayList;
  }
  
  @SuppressWarnings("unchecked")
  private ArrayList<Object>  findLoincObservationByName(StringBuffer query, String type) throws
        NEDSSSystemException {
      DAOBase dao = new DAOBase();
      LoincSearchResultTmp loincResult = new LoincSearchResultTmp();
      //@@ System.out.println("query........." + query);

      ArrayList<Object>  resultList = (ArrayList<Object> ) dao.preparedStmtMethod(loincResult, null,
          query.toString(), NEDSSConstants.SELECT);
      Iterator<Object> nameItr1 = resultList.iterator();

      ArrayList<Object>  list = new ArrayList<Object> ();
      while (nameItr1.hasNext()) {
        
        LoincSearchResultTmp tmp = (LoincSearchResultTmp) nameItr1.next();
        if (! (tmp.getLoincCd() == null)) {
          LoincResultDT loincDT = new LoincResultDT();

          loincDT.setLoincCd(tmp.getLoincCd());
          loincDT.setLoincComponentName(tmp.getLoincComponentName());
          loincDT.setLoincMethod(tmp.getLoincMethod());
          loincDT.setLoincSystem(tmp.getLoincSystem());
          loincDT.setLoincProperty(tmp.getLoincProperty());
          loincDT.setRelatedClassCd(tmp.getRelatedClassCd());

          if(tmp.getLoincProperty() != null &&
             tmp.getLoincProperty().equalsIgnoreCase("PRID") &&
             tmp.getRelatedClassCd() != null &&
             tmp.getRelatedClassCd().equalsIgnoreCase("MICRO") &&
             tmp.getLoincMethod()!= null){

            String s = tmp.getLoincMethod();
            int length = s.length();
            for(int i=0; i<length;i++)
            {
              char c1 = s.charAt(i);
              if (c1 == 'C') {
                int temp = i +1;
                if( temp != length){
                  char c2 = s.charAt(temp);
                  if (c2 == 'U'){
                    loincDT.setLabOrganismIndicator("Y");
                    break;
                  }
                }

              }
             }
          }
          list.add(loincDT);
        }
      }

     

     

      return list;
    }

  
@SuppressWarnings("unchecked")
private Collection<Object> getLabReportSummaryListforSecurity(NBSSecurityObj nbsSecurityObj)
{
 ArrayList<Object>  labReportSummaryList = new ArrayList<Object> ();
 int labCountFix = propertyUtil.getLabCount();
 String labQuery = "";
 try{

 if(nbsSecurityObj.getPermission(NBSBOLookup.OBSERVATIONLABREPORT,NBSOperationLookup.ASSIGNSECURITY))
 {
   if (propertyUtil.getDatabaseServerType() != null &&
     propertyUtil.
     getDatabaseServerType().equalsIgnoreCase(NEDSSConstants.ORACLE_ID)) {

     labQuery = " SELECT  /*+ FIRST_ROWS */ obs.observation_uid \"ObservationUid\", "
       + " obs.local_id \"LocalId\", "
       + " obs.electronic_ind \"electronicInd\", "
       + " obs.jurisdiction_cd \"Jurisdiction\", "
       + " obs.prog_area_cd \"ProgramArea\", "
       + " obs.rpt_to_state_time \"DateReceived\", "
       + " obs.version_ctrl_nbr \"versionCtrlNbr\", "
       + " obs.ctrl_cd_display_form \"ctrlCdDisplayForm\", "
       + " obs.status_cd \"Status\", "
       + " obs1.cd_desc_txt \"resultedTest\","
       + " obs1.cd \"resultedTestCd\", "
       + " obs1.cd_system_cd \"resultedTestCdSystemCd\" "
       + " FROM observation obs,observation obs1,"
       + " act_relationship act "
       + " WHERE "
       + " 'Result' = obs1.obs_domain_cd_st_1"
       + " AND obs.observation_uid = act.target_act_uid"
       + " AND obs1.observation_uid = act.source_act_uid"
       + " AND 'OBS' = act.target_class_cd"
       + " AND 'COMP' = act.type_cd"
       + " AND 'OBS' = act.source_class_cd"
       + " AND 'ACTIVE' = act.record_status_cd"
       + " and obs.observation_uid in ( "
       + "  select * from "
       + " (select /*+ INDEX_JOIN(OBSV) */ observation_uid "
       + " from observation obsv "
       + " where (obsv.prog_area_cd IS NULL "
       + " OR obsv.jurisdiction_cd IS NULL) "
       + " and obsv.ctrl_cd_display_form = 'LabReport' "
       + " and obsv.obs_domain_cd_st_1 = 'Order'  order by obsv.rpt_to_state_time,obsv.OBSERVATION_UID)"
       + " where rownum <= "+labCountFix+")  ";
 }
 else
   labQuery =  " SELECT  obs.observation_uid \"ObservationUid\", "
               + " obs.local_id \"LocalId\", "
               + " obs.electronic_ind \"electronicInd\", "
               + " obs.jurisdiction_cd \"Jurisdiction\", "
               + " obs.prog_area_cd \"ProgramArea\", "
               + " obs.rpt_to_state_time \"DateReceived\", "
               + " obs.version_ctrl_nbr \"versionCtrlNbr\", "
               + " obs.ctrl_cd_display_form \"ctrlCdDisplayForm\", "
               + " obs.status_cd \"Status\", "
               + " obs1.cd_desc_txt \"resultedTest\","
               + " obs1.cd \"resultedTestCd\", "
               + " obs1.cd_system_cd \"resultedTestCdSystemCd\" "
               + " FROM observation obs,observation obs1,"
               + " act_relationship act "
               + " WHERE "
               +"obs1.obs_domain_cd_st_1 = 'Result' and"
               + " act.target_act_uid = obs.observation_uid "
               + " AND (act.source_act_uid = obs1.observation_uid)"
               + " AND (act.target_class_cd = 'OBS')"
               + " AND (act.type_cd = 'COMP') "
               + " AND (act.source_class_cd = 'OBS')"
               + " AND (act.record_status_cd = 'ACTIVE')"
               + " and act.target_act_uid in ( "
               + " select top "+labCountFix+" observation_uid from observation obsv "
               + " where (obsv.prog_area_cd IS NULL "
               + " OR obsv.jurisdiction_cd IS NULL) "
                   + " and obsv.ctrl_cd_display_form = 'LabReport'"
                   + " and obsv.obs_domain_cd_st_1 = 'Order'"
                   + " order by obsv.rpt_to_state_time,obsv.OBSERVATION_UID)";
   long timebegin = 0; long timeend = 0;
   timebegin=System.currentTimeMillis();
   logger.debug("\n timebegin  for LabReportSummaryListforSecurity " + timebegin);
   logger.debug("\n labQuery  for Security = "+labQuery);
               ArrayList<Object>  argList = new ArrayList<Object> ();
               argList.add(new Integer(labCountFix));
               LabReportResultedtestSummaryVO labReportSummaryVO = new LabReportResultedtestSummaryVO();
               labReportSummaryList = (ArrayList<Object> )preparedStmtMethod(labReportSummaryVO, null,labQuery, NEDSSConstants.SELECT);
    timeend = System.currentTimeMillis();
    logger.debug("\n timeend for LabReportSummaryListforSecurity " + timeend);


   }

 }
 catch(Exception e)
 {

 }

 return labReportSummaryList;
}

  @SuppressWarnings("unchecked")
private ArrayList<Object>  getLabReportPersonInfoforSecurity(NBSSecurityObj nbsSecurityObj)
  {
    ArrayList<Object>  labReportPersonInfoList = new ArrayList<Object> ();
    int labCountFix = propertyUtil.getLabCount();
    String labPersonInfoQuery = "";
   try{

   if(nbsSecurityObj.getPermission(NBSBOLookup.OBSERVATIONLABREPORT,NBSOperationLookup.ASSIGNSECURITY))
   {
     String labDataAccessWhereClause = nbsSecurityObj.getDataAccessWhereClause(NBSBOLookup.OBSERVATIONLABREPORT,NBSOperationLookup.ASSIGNSECURITY);
     labDataAccessWhereClause = labDataAccessWhereClause != null ? "AND " + labDataAccessWhereClause : "";
     if (propertyUtil.getDatabaseServerType() != null &&
       propertyUtil.getDatabaseServerType().equalsIgnoreCase(NEDSSConstants.ORACLE_ID)) {
     labPersonInfoQuery =  " SELECT /*+ FIRST_ROWS */ observation.OBSERVATION_UID \"observationUid\", "
    	 + " NVL(person_name.last_nm, 'No Last') \"lastNm\","
         + " NVL(person_name.first_nm, 'No First') \"firstNm\","
         + " person.curr_sex_cd \"currSexCd\", "
         + " person.birth_time \"birthTime\" ,"
         + " person.local_id \"personLocalId\","
         + " person.person_parent_uid \"personParentUid\" "
         + " FROM person,  participation,person_name, "
         +"  (select * from "
		         + " (select /*+ USE_CONCAT */ observation_uid "
		         + " from observation obsv "
		         + " where obsv.ctrl_cd_display_form = 'LabReport' "
                 + " and  obsv.obs_domain_cd_st_1 = 'Order'"
                 + " and (obsv.prog_area_cd IS NULL "
                 + " OR obsv.jurisdiction_cd IS NULL) "
                 + " order by obsv.rpt_to_state_time,obsv.OBSERVATION_UID) where rownum <= "+labCountFix+") observation"
         + " WHERE (observation.observation_uid = participation.act_uid) and person.person_uid = participation.subject_entity_uid "
         + " AND (person_name.person_uid(+) = person.person_uid) "
         + " AND (person_name.nm_use_cd (+)= 'L') "
         + " AND participation.type_cd = 'PATSBJ' ";    
     
     
    }
    else
      labPersonInfoQuery =  " SELECT top "+labCountFix+" observation.OBSERVATION_UID \"observationUid\", "
    	 + " ISNULL(pnm.last_nm, 'No Last') \"lastNm\","
         + " ISNULL(pnm.first_nm, 'No First') \"firstNm\","
         + " p.curr_sex_cd \"currSexCd\", "
         + " p.birth_time \"birthTime\", "
         + " p.local_id \"personLocalId\","
         + " p.person_parent_uid \"personParentUid\" "
         + " FROM person p Left Outer join person_name pnm on p.person_uid=pnm.person_uid,  participation,observation "
		 + " WHERE (observation.observation_uid = participation.act_uid) and p.person_uid = participation.subject_entity_uid "
         + " AND (pnm.nm_use_cd = 'L' or pnm.nm_use_cd IS NULL )"
         + " AND participation.type_cd = 'PATSBJ' and "
         + " (observation.prog_area_cd IS NULL "
         + " OR observation.jurisdiction_cd IS NULL) "
         + " and observation.ctrl_cd_display_form = 'LabReport' "
         + " and observation.obs_domain_cd_st_1 = 'Order'"
         + " order by observation.rpt_to_state_time,observation.OBSERVATION_UID";
     
          long timebegin = 0; long timeend = 0;
          timebegin=System.currentTimeMillis();
          logger.debug("\n timebegin  for LabReportSummaryListforReview(PersonInfo) " + timebegin);


               ObservationPersonInfoVO observationPersonInfoVO = new ObservationPersonInfoVO();
               labReportPersonInfoList = (ArrayList<Object> )preparedStmtMethod(observationPersonInfoVO, null,labPersonInfoQuery, NEDSSConstants.SELECT);
          timeend = System.currentTimeMillis();
          logger.debug("\n timeend  for LabReportSummaryListforReview(PersonInfo) " + timeend);
          logger.debug("\n total time for LabReportSummaryListforReview(PersonInfo) " + new Long(timeend-timebegin));


     }

   }
   catch(Exception e)
   {

   }

   return labReportPersonInfoList;
  }



  @SuppressWarnings("unchecked")
private Collection<Object> getMorbReportSummaryListforSecurity(NBSSecurityObj nbsSecurityObj)
   {
     ArrayList<Object>  morbReportSummaryList = new ArrayList<Object> ();
     int morbCountFix = propertyUtil.getMorbCount();
     String morbQuery = "";
    try{

    if(nbsSecurityObj.getPermission(NBSBOLookup.OBSERVATIONMORBIDITYREPORT,NBSOperationLookup.ASSIGNSECURITY))
    {
        if (propertyUtil.getDatabaseServerType() != null &&
        propertyUtil.getDatabaseServerType().equalsIgnoreCase(NEDSSConstants.ORACLE_ID)) {

        morbQuery = " SELECT obs.local_id \"localId\","
          + " obs.observation_uid \"observationUid\", "
          + " obs.cd \"condition\", "
          + " obs.rpt_to_state_time \"dateReceived\", "
          + " obs.prog_area_cd \"programArea\", "
          + " obs.jurisdiction_cd \"jurisdiction\","
          + " obs.record_status_cd \"recordStatusCd\","
          + " obs.ctrl_cd_display_form \"ctrlCdDisplayForm\","
          + " obsvc.CODE \"reportType\" "
          + " from observation obs,"
          + " act_relationship act,"
          + " observation obs1, "
          + " obs_value_coded obsvc"
          + " WHERE"
          + " act.target_act_uid = obs.OBSERVATION_UID"
          + " and  obs1.observation_uid = act.source_act_uid"
          + " and  obsvc.observation_uid = obs1.observation_uid"
          + " and  obs1.CD = 'MRB100'"
          + " and  act.target_act_uid in ("
          + " select * from "
          + "(select observation_uid "
          + " from observation obsv "
          + " where (obsv.prog_area_cd IS NULL"
          + " OR obsv.jurisdiction_cd IS NULL)"
          + " and obsv.ctrl_cd_display_form = 'MorbReport' ) "
          + " where rownum <= " +morbCountFix+ " )"
          + " order by obs.rpt_to_state_time,obs.observation_uid";
    }
    else
      morbQuery = " SELECT obs.local_id \"localId\","
          + " obs.observation_uid \"observationUid\", "
          + " obs.cd \"condition\", "
          + " obs.rpt_to_state_time \"dateReceived\", "
          + " obs.prog_area_cd \"programArea\", "
          + " obs.jurisdiction_cd \"jurisdiction\","
          + " obs.record_status_cd \"recordStatusCd\","
          + " obs.ctrl_cd_display_form \"ctrlCdDisplayForm\","
          + " obsvc.CODE \"reportType\" "
          + " from observation obs,"
          + " act_relationship act,"
          + " observation obs1, "
          + " obs_value_coded obsvc"
          + " WHERE"
          + " act.target_act_uid = obs.OBSERVATION_UID"
          + " and  obs1.observation_uid = act.source_act_uid"
          + " and  obsvc.observation_uid = obs1.observation_uid"
          + " and  obs1.CD = 'MRB100'"
          + " and  act.target_act_uid in ("
             + " select top " +morbCountFix+ " observation_uid from observation obsv "
             + " where (obsv.prog_area_cd IS NULL "
             + " OR obsv.jurisdiction_cd IS NULL) "
             + " and obsv.ctrl_cd_display_form = 'MorbReport'"
             + " order by obsv.rpt_to_state_time,obsv.OBSERVATION_UID)";
              long timebegin = 0; long timeend = 0;
              timebegin=System.currentTimeMillis();
              logger.debug("\n timebegin  for MorbbReportSummaryListforSecurity " + timebegin);
              logger.debug("\n The Morbquery for security = " +morbQuery);
                  MorbReportConditionSummaryVO morbReportConditionSummaryVO = new MorbReportConditionSummaryVO();
                  morbReportSummaryList = (ArrayList<Object> )preparedStmtMethod(morbReportConditionSummaryVO, null,morbQuery, NEDSSConstants.SELECT);
              timeend = System.currentTimeMillis();
              logger.debug("\n timeend  for MorbbReportSummaryListforSecurity " + timeend);

      }

    }
    catch(Exception e)
    {

    }

    return morbReportSummaryList;
   }

   @SuppressWarnings("unchecked")
private Collection<Object> getMorbReportPersonInfoforSecurity(NBSSecurityObj nbsSecurityObj)
 {
  ArrayList<Object>  morbReportPersonInfoList = new ArrayList<Object> ();
  int morbCountFix = propertyUtil.getMorbCount();
  String morbPersonInfoQuery = "";
  try{

  if(nbsSecurityObj.getPermission(NBSBOLookup.OBSERVATIONMORBIDITYREPORT,NBSOperationLookup.ASSIGNSECURITY))
  {
    if (propertyUtil.getDatabaseServerType() != null &&
    propertyUtil.getDatabaseServerType().equalsIgnoreCase(NEDSSConstants.ORACLE_ID)) {
    	 /*morbPersonInfoQuery = " SELECT  observation.OBSERVATION_UID \"observationUid\", "
    	      + " person.person_parent_uid \"personParentUid\" "
    	      + " FROM person, observation, participation"
    	      + " WHERE person.person_uid = participation.subject_entity_uid "
    	      + " AND participation.act_uid = observation.observation_uid "
    	      + " AND participation.type_cd = '" +
    	      NEDSSConstants.MOB_SUBJECT_OF_MORB_REPORT + "'"
    	      + " AND observation.observation_uid in( "
    	        + " select * from "
    	        + " (select observation_uid "
    	        + " from observation obsv "
    	        + " where (obsv.prog_area_cd IS NULL"
    	        + " OR obsv.jurisdiction_cd IS NULL)"
    	        + " and obsv.ctrl_cd_display_form = 'MorbReport' ) "
    	        + " where rownum <= " +morbCountFix+ " )"
    	        + " order by observation.rpt_to_state_time,observation.observation_uid";*/
    	 
    morbPersonInfoQuery = " SELECT  /*+ FIRST_ROWS */ observation.OBSERVATION_UID \"observationUid\", "
      + " NVL(person_name.last_nm, 'No Last') \"lastNm\","
      + " NVL(person_name.first_nm, 'No First') \"firstNm\","
      + " person.curr_sex_cd \"currSexCd\", "
      + " person.birth_time \"birthTime\" ,"
      + " person.local_id \"personLocalId\","
      + " person.person_parent_uid \"personParentUid\" "
      + " FROM person, participation, person_name,"      
      + " (select /*+ USE_CONCAT */ observation_uid "
      + " from observation obsv "
      + " where (obsv.prog_area_cd IS NULL"
      + " OR obsv.jurisdiction_cd IS NULL)"
      + " and obsv.ctrl_cd_display_form = 'MorbReport'  "
      + " and  rownum <= "+morbCountFix+"  order by obsv.rpt_to_state_time,obsv.observation_uid)observation " 
      + " WHERE (observation.observation_uid = participation.act_uid) and person.person_uid = participation.subject_entity_uid "
     + " AND (person_name.person_uid(+) = person.person_uid) "
     + " AND (person_name.nm_use_cd (+)= 'L') ";    
    
}
else
   
	/* morbPersonInfoQuery =   " SELECT  observation.OBSERVATION_UID \"observationUid\", "
	        + " person.person_parent_uid \"personParentUid\" "
	        + " FROM person, observation, participation"
	        + " WHERE person.person_uid = participation.subject_entity_uid "
	        + " AND participation.act_uid = observation.observation_uid "
	        + " AND participation.type_cd = '" +
	        NEDSSConstants.MOB_SUBJECT_OF_MORB_REPORT + "'"
	        + " AND observation.observation_uid in( "
	        + " select top "+morbCountFix+ " observation_uid from observation obsv "
	        + " where (obsv.prog_area_cd IS NULL "
	        + " OR obsv.jurisdiction_cd IS NULL) "
	        + " and obsv.ctrl_cd_display_form = 'MorbReport'"
	        + " order by obsv.rpt_to_state_time,obsv.OBSERVATION_UID)";*/
	
	morbPersonInfoQuery =  " SELECT top "+morbCountFix+" observation.OBSERVATION_UID \"observationUid\", "
	 + " ISNULL(pnm.last_nm, 'No Last') \"lastNm\","
     + " ISNULL(pnm.first_nm, 'No First') \"firstNm\","
     + " p.curr_sex_cd \"currSexCd\", "
     + " p.birth_time \"birthTime\" ,"
     + " p.local_id \"personLocalId\","
     + " p.person_parent_uid \"personParentUid\" "
     + " FROM person p Left Outer join person_name pnm on p.person_uid=pnm.person_uid,  participation,observation "
	 + " WHERE (observation.observation_uid = participation.act_uid) and p.person_uid = participation.subject_entity_uid "
     + " AND (pnm.nm_use_cd = 'L' or pnm.nm_use_cd IS NULL )"
     + " and (observation.prog_area_cd IS NULL "
     + " OR observation.jurisdiction_cd IS NULL) "
     + " and observation.ctrl_cd_display_form = 'MorbReport' "
     + " order by observation.OBSERVATION_UID ";
	
	/*morbPersonInfoQuery =   " SELECT  observation.OBSERVATION_UID \"observationUid\", "
    	+ " person_name.last_nm \"lastNm\","
        + " person_name.first_nm \"firstNm\","
        + " person.person_parent_uid \"personParentUid\" "
        + " FROM person, observation, participation, person_name"
        + " WHERE person.person_uid = participation.subject_entity_uid "
        + " AND participation.act_uid = observation.observation_uid "
        + " AND participation.type_cd = '" +
        NEDSSConstants.MOB_SUBJECT_OF_MORB_REPORT + "'"
        + " AND observation.observation_uid in( "
        + " select top "+morbCountFix+ " observation_uid from observation obsv "
        + " where (obsv.prog_area_cd IS NULL "
        + " OR obsv.jurisdiction_cd IS NULL) "
        + " and obsv.ctrl_cd_display_form = 'MorbReport'"
        + " AND (person_name.person_uid(+) = person.person_uid) "
        + " AND (person_name.nm_use_cd (+)= 'L') "
        + " order by obsv.rpt_to_state_time,obsv.OBSERVATION_UID)";*/

      long timebegin = 0; long timeend = 0;
      timebegin=System.currentTimeMillis();
      logger.debug("\n timebegin  for MorbReportSummaryListforReview(PersonInfo) " + timebegin);

               ObservationPersonInfoVO observationPersonInfoVO = new ObservationPersonInfoVO();
               morbReportPersonInfoList = (ArrayList<Object> )preparedStmtMethod(observationPersonInfoVO, null,morbPersonInfoQuery, NEDSSConstants.SELECT);
      timeend = System.currentTimeMillis();
      logger.debug("\n timeend  for MorbReportSummaryListforReview(PersonInfo) " + timeend);
      logger.debug("\n total time for MorbReportSummaryListforReview(PersonInfo) " + new Long(timeend-timebegin));

    }

  }
  catch(Exception e)
  {

  }

  return morbReportPersonInfoList;
 }




   public Collection<Object> getLabReportSummaryVOCollForSecurity(NBSSecurityObj nbsSecurityObj)
    {
	  logger.debug("in getLabReportSummaryVOCollForSecurity()");
      Collection<Object> reportSummaryColl = new ArrayList<Object> ();
      Collection<Object> personList = null;
      Collection<Object> labList = null;
      ArrayList<Object>  labSummList = new ArrayList<Object> ();
      CachedDropDownValues cdv = new CachedDropDownValues();
      String tempStr = "";
      try
     {
    	 labList = getLabReportSummaryListforSecurity(nbsSecurityObj);
    	 if(labList!=null && labList.size()==0)
    		 labList=null;
         personList = getLabReportPersonInfoforSecurity(nbsSecurityObj);
       Collection<Object> resultedColl = new ArrayList<Object> ();
       HashMap<Long, Object> mapForLab = new HashMap<Long, Object>();
       Long observationUid = null;
       if (labList != null) {
         Iterator<Object> labIt = labList.iterator();
         while (labIt.hasNext()) {
           LabReportResultedtestSummaryVO labReportResultedSummaryVO = (
           LabReportResultedtestSummaryVO) labIt.next();
           LabReportSummaryVO labReportSummaryVO = new LabReportSummaryVO();
           observationUid=labReportResultedSummaryVO.
                   getObservationUid();
           labReportSummaryVO.setObservationUid(observationUid);
           labReportSummaryVO.setDateReceived(labReportResultedSummaryVO.getDateReceived());
           
           labReportSummaryVO.setVersionCtrlNbr(labReportResultedSummaryVO.getVersionCtrlNbr());
                 
           labReportSummaryVO.setLocalId(labReportResultedSummaryVO.getLocalId());
           labReportSummaryVO.setElectronicInd(labReportResultedSummaryVO.getElectronicInd());
           
           /* Setting it as "Lab Report" when the cntrl_display_form_cd is "LapReport" */
           labReportSummaryVO.setType(NEDSSConstants.LAB_REPORT_DESC);
           labReportSummaryVO.setProgramArea(labReportResultedSummaryVO.getProgramArea());
           labReportSummaryVO.setProgAreaCd(labReportResultedSummaryVO.getProgramArea());
           if (labReportSummaryVO.getProgramArea() != null) {
             tempStr = cdv.getProgramAreaDesc(labReportSummaryVO.getProgramArea());
             if(tempStr != null){
				labReportSummaryVO.setProgramArea(tempStr);
             }
           }
           labReportSummaryVO.setJurisdiction(labReportResultedSummaryVO.getJurisdiction());
           labReportSummaryVO.setJurisdictionCd(labReportResultedSummaryVO.getJurisdiction());
           
           if (labReportSummaryVO.getJurisdiction() != null) {
            tempStr = cdv.getJurisdictionDesc(labReportSummaryVO.getJurisdiction());
            if (tempStr != null )
				 labReportSummaryVO.setJurisdiction(tempStr);
           }
           
           //Get provider
           ObservationSummaryDAOImpl osd = new ObservationSummaryDAOImpl();
           Long summaryLong = labReportSummaryVO.getObservationUid();
           if(summaryLong!=null){
	           ArrayList<Object>  providerDetails = osd.getProviderInfo(summaryLong,"ORD");
	           getProviderInformation(providerDetails, labReportSummaryVO);
           }
           
			 //Get the Reporting Facility
			 Map<Object,Object> uidMap = osd.getLabParticipations(observationUid);
            if (uidMap != null && uidMap.containsKey(NEDSSConstants.PAR111_TYP_CD) && labReportSummaryVO != null) {
          	  labReportSummaryVO.setReportingFacility(osd.getReportingFacilityName((Long)uidMap.get(NEDSSConstants.PAR111_TYP_CD)));  
            }
            
           
           if( personList != null)
           {
             Iterator<Object> it = personList.iterator();
             while (it.hasNext()) {
               ObservationPersonInfoVO personInfo = (ObservationPersonInfoVO) it.
                   next();
               if (personInfo.getObservationUid().compareTo(
                   labReportResultedSummaryVO.getObservationUid()) == 0) {
                    labReportSummaryVO.setMPRUid(personInfo.getPersonParentUid());
                    if(personInfo.getFirstNm()!=null)
                    labReportSummaryVO.setPatientFirstName(personInfo.getFirstNm());
                    if(personInfo.getLastNm() != null)
                    labReportSummaryVO.setPatientLastName(personInfo.getLastNm());
                    if(personInfo.getPersonLocalId() != null)
                        labReportSummaryVO.setPersonLocalId(personInfo.getPersonLocalId());
                    if(personInfo.getCurrSexCd() != null)
                        labReportSummaryVO.setCurrSexCd(personInfo.getCurrSexCd());
                    if(personInfo.getBirthTime() != null)
                        labReportSummaryVO.setBirthTime(personInfo.getBirthTime());
                    
                    //+ " person.local_id \"personLocalId\","
               }
             }
          }
/*
           
           
           ResultedTestSummaryVO resultedTestSummVO = new ResultedTestSummaryVO();
           resultedTestSummVO.setObservationUid(labReportResultedSummaryVO.
                                                getObservationUid());
           resultedTestSummVO.setResultedTest(labReportResultedSummaryVO.
                                              getResultedTest());
           resultedTestSummVO.setResultedTestCd(labReportResultedSummaryVO.
                                                getResultedTestCd());
           resultedTestSummVO.setCdSystemCd(labReportResultedSummaryVO.
                                           getResultedTestCdSystemCd());
           if (observationUid == null)//should happen for the first entry
           {

             resultedColl.add(resultedTestSummVO);
             LabReportSummaryVO labReportSummVO = (LabReportSummaryVO) mapForLab.
                  get(observationUid);
             labReportSummaryVO.setTheResultedSummaryTestVOCollection(
                   resultedColl);
         }
         else if(observationUid!= null && labReportResultedSummaryVO.
               getObservationUid().compareTo(observationUid) == 0) {
             resultedColl.add(resultedTestSummVO);
             LabReportSummaryVO labReportSummVO = (LabReportSummaryVO) mapForLab.
                  get(observationUid);
             labReportSummaryVO.setTheResultedSummaryTestVOCollection(
                   resultedColl);

           }

           /*if( observationUid == null &&
                 labReportResultedSummaryVO.
                 getObservationUid().compareTo(observationUid)!= 0)
           {
             labReportSummaryVO.setTheResultedSummaryTestVOCollection(
                 resultedColl);
             resultedColl = new ArrayList<Object> ();
             resultedColl.add(resultedTestSummVO);
           }
            if( observationUid != null &&
                 labReportResultedSummaryVO.
                 getObservationUid().compareTo(observationUid)!= 0) */
          /* else
            {
              resultedColl = new ArrayList<Object> ();
              resultedColl.add(resultedTestSummVO);
              LabReportSummaryVO labReportSummVO = (LabReportSummaryVO) mapForLab.
                 get(observationUid);
             labReportSummaryVO.setTheResultedSummaryTestVOCollection(
                 resultedColl);
             //mapForLab.put(observationUid, labReportSummaryVO);
             //resultedColl = new ArrayList<Object> ();
            // resultedColl.add(resultedTestSummVO);
           }
           mapForLab.put(labReportResultedSummaryVO.getObservationUid(),
                         labReportSummaryVO);
           observationUid = labReportResultedSummaryVO.getObservationUid();

           if (!labIt.hasNext()) {
             LabReportSummaryVO labReportSummVO = (LabReportSummaryVO) mapForLab.
                 get(observationUid);
             labReportSummaryVO.setTheResultedSummaryTestVOCollection(
                 resultedColl);
           }*/
           
           
           mapForLab.put(observationUid, labReportSummaryVO);
           
           
       	//Get tests and susceptibilities
           ArrayList<Object>  argList = new ArrayList<Object> ();
           argList.clear();
           argList.add("COMP"); // It should be constant
           argList.add(observationUid);
           
           getTestAndSusceptibilitiesDRRQ(argList, labReportSummaryVO, null); 
           
           labSummList.add(labReportSummaryVO);
           
         }
       }
       reportSummaryColl.addAll(mapForLab.values());
       this.populateDescTxtFromCachedValuesDRRQ(labSummList);
     }
     catch(Exception e)
     {
      logger.error(
              "Error while getting observations for Security",
              e);

     }
      logger.debug("leaving getLabReportSummaryVOCollForSecurity()");
      return reportSummaryColl;
    }
    public Collection<Object> getMorbReportSummaryVOCollForSecurity(NBSSecurityObj nbsSecurityObj)
     {
       Collection<Object> reportSummaryColl = new ArrayList<Object> ();
       Collection<Object> morbList = null;
       Collection<Object> personList = null;
       ArrayList<Object>  labReportUids = new ArrayList<Object> ();
   String tempStr = "";
       CachedDropDownValues cdv = new CachedDropDownValues();

      try
      {
    	  morbList = getMorbReportSummaryListforSecurity(nbsSecurityObj);
    	  if(morbList!=null && morbList.size()==0)
    		  morbList = null;

    	 personList = getMorbReportPersonInfoforSecurity(nbsSecurityObj);
    	 if(personList!=null && personList.size()==0)
   		  personList = null;

        HashMap<Long, Object> mapForMorb = new HashMap<Long, Object>();
        Long observationUid = null;
        if (morbList != null) {
          Iterator<Object> morbIt = morbList.iterator();
          while (morbIt.hasNext()) {
            MorbReportConditionSummaryVO morbReportConditionSummaryVO = (
            MorbReportConditionSummaryVO) morbIt.next();
            MorbReportSummaryVO morbReportSummaryVO = new MorbReportSummaryVO();
            morbReportSummaryVO.setObservationUid(morbReportConditionSummaryVO.
                                                 getObservationUid());
            morbReportSummaryVO.setDateReceived(morbReportConditionSummaryVO.getDateReceived());
            morbReportSummaryVO.setLocalId(morbReportConditionSummaryVO.getLocalId());
            /* Setting it as "Lab Report" when the cntrl_display_form_cd is "LapReport" */
            morbReportSummaryVO.setType(NEDSSConstants.MORB_REPORT_DESC);
			morbReportSummaryVO.setProgramArea(morbReportConditionSummaryVO.getProgramArea());
			morbReportSummaryVO.setProgAreaCd(morbReportConditionSummaryVO.getProgramArea());
            if (morbReportSummaryVO.getProgramArea() != null) {
               tempStr = cdv.getProgramAreaDesc(morbReportSummaryVO.getProgramArea());
				if(tempStr != null)
					morbReportSummaryVO.setProgramArea(tempStr);
			   }
            morbReportSummaryVO.setJurisdiction(morbReportConditionSummaryVO.getJurisdiction());
            morbReportSummaryVO.setJurisdictionCd(morbReportConditionSummaryVO.getJurisdiction());
			   
			   if(morbReportSummaryVO.getJurisdiction() != null){
				  tempStr = cdv.getJurisdictionDesc(morbReportSummaryVO.getJurisdiction());
				  if(tempStr != null)
					morbReportSummaryVO.setJurisdiction(tempStr);
			   }
			   morbReportSummaryVO.setCondition(morbReportConditionSummaryVO.getCondition());
			   if (morbReportSummaryVO.getCondition() != null) {
					 tempStr = cdv.getConditionDesc(morbReportSummaryVO.getCondition());
				  if(tempStr != null)
					morbReportSummaryVO.setConditionDescTxt(tempStr);

             }
			   
	           //Get provider
	           ObservationSummaryDAOImpl osd = new ObservationSummaryDAOImpl();
	           Long summaryLong = morbReportSummaryVO.getObservationUid();
	           if(summaryLong!=null){
		           ArrayList<Object>  providerDetails = osd.getProviderInfo(summaryLong,"PhysicianOfMorb");
		           getProviderInformation(providerDetails, morbReportSummaryVO);
	           }
	           //Get the reporting facility
				 Map<Object,Object> uidMap = osd.getLabParticipations(summaryLong);
				 ArrayList<Object>  facilityDetails = osd.getReportingFacilityInfo(summaryLong,NEDSSConstants.MOB_REPORTER_OF_MORB_REPORT);
				 
				 if(facilityDetails!=null && facilityDetails.size()>0 && morbReportSummaryVO != null){
         		  Object[] facility = facilityDetails.toArray();
         		  if (facility[0] != null) {
         			morbReportSummaryVO.setReportingFacility((String) facility[0]);
	                    logger.debug("FacilityName: " + (String) facility[0]);
	                  }
         		  
	              }
				 
	           
	           
             if( personList != null)
             {
              Iterator<Object>  it = personList.iterator();
               while (it.hasNext()) {
                 ObservationPersonInfoVO personInfo = (ObservationPersonInfoVO) it.
                     next();
                 if (personInfo.getObservationUid().compareTo(
                     morbReportConditionSummaryVO.getObservationUid()) == 0) {
                     morbReportSummaryVO.setMPRUid(personInfo.getPersonParentUid());
                     if(personInfo.getFirstNm()!=null)
                     morbReportSummaryVO.setPatientFirstName(personInfo.getFirstNm());
                     if(personInfo.getLastNm() != null)
                     morbReportSummaryVO.setPatientLastName(personInfo.getLastNm());
                     if(personInfo.getPersonLocalId() != null)
                         morbReportSummaryVO.setPersonLocalId(personInfo.getPersonLocalId());
                     if(personInfo.getCurrSexCd() != null)
                    	 morbReportSummaryVO.setCurrSexCd(personInfo.getCurrSexCd());
                     if(personInfo.getBirthTime() != null)
                    	 morbReportSummaryVO.setBirthTime(personInfo.getBirthTime());
                 }
               }
             }


             /*  if (observationUid == null ||
                   morbReportConditionSummaryVO.
                   getObservationUid().compareTo(observationUid) == 0) {

               }
               else { */
                 MorbReportSummaryVO morbReportSummVO = (MorbReportSummaryVO) mapForMorb.
                     get(observationUid);
              //                  }
               mapForMorb.put(morbReportConditionSummaryVO.getObservationUid(),
                             morbReportSummaryVO);
               observationUid = morbReportConditionSummaryVO.getObservationUid();

               if (!morbIt.hasNext()) {
                 MorbReportSummaryVO mobbReportSummVO = (MorbReportSummaryVO) mapForMorb.
                     get(observationUid);

               }
               //Get labs from morbidity
               getLabReportsFromMorbidity( morbReportSummaryVO, labReportUids, nbsSecurityObj);
               
             }
           }
        reportSummaryColl.addAll(mapForMorb.values());
      }
      catch(Exception e)
      {
       logger.error(
               "Error while getting Morb observations for security",
               e);

      }

       return reportSummaryColl;
     }
	/**
	 * getLabReportsFromMorbidity: common method used from DRRQ and DRSAQ to get the morb's labs.
	 * @param morbReportSummaryVO
	 * @param labReportUids
	 * @param nbsSecurityObj
	 */

    
    private void getLabReportsFromMorbidity(MorbReportSummaryVO morbReportSummaryVO, ArrayList<Object>  labReportUids, NBSSecurityObj nbsSecurityObj){
    	
    	

        /** LAB REPORT SUMMARY VO'S **/
        ArrayList<Object>  argList = new ArrayList<Object> ();
        
        UidSummaryVO labReportUid = new UidSummaryVO();
        argList.clear();
        argList.add(morbReportSummaryVO.getObservationUid());
        labReportUids = (ArrayList<Object> ) preparedStmtMethod(labReportUid,
            argList, WumSqlQuery.SELECT_LAB_TARGETS, NEDSSConstants.SELECT);
        
        
        LabReportSummaryVO labReportSummaryVOs = new LabReportSummaryVO();
        Collection<Object> labReportSummaryVOCollection  = new ArrayList<Object> ();
        Collection<Object> labColl = new ArrayList<Object> ();
       // ArrayList<Object> labReportMap = new ArrayList<Object>();
        HashMap<Object, Object> labReportMap = new HashMap<Object, Object>();
        
        /*If the user has permission to see the Lab */
        String labUidType = "LABORATORY_UID";
        if(nbsSecurityObj.getPermission(NBSBOLookup.OBSERVATIONLABREPORT,NBSOperationLookup.VIEW))
        { 
     	   
     	  // labReportMap  = getLabsFromUid(labReportUids, nbsSecurityObj, labUidType);
     	  // labReportSummaryVOCollection = getLabReportSummaryVOColl(nbsSecurityObj, labReportMap);
     	   labReportMap = retrieveLabReportSummaryRevisitedFromMorbDRRQ(labReportUids, false, nbsSecurityObj, labUidType);
        }

        if(!labReportMap.isEmpty())
          {

        	  if(morbReportSummaryVO != null)
        	  {
        		  labReportSummaryVOCollection  =(ArrayList<Object> )labReportMap.get("labEventList");
        		  Iterator<Object> ite = labReportSummaryVOCollection.iterator();
        		  while( ite.hasNext()){
        			  labReportSummaryVOs = (LabReportSummaryVO) ite.next();
        			  labColl.add(labReportSummaryVOs);
        			  morbReportSummaryVO.setTheLabReportSummaryVOColl(labColl);
        		  }
        	  }
          }
        
        
    	
    	
    	
    }
@SuppressWarnings("unchecked")
private Collection<Object> getLabReportSummaryListforReview(NBSSecurityObj nbsSecurityObj)
 {
  ArrayList<Object>  labReportSummaryList = new ArrayList<Object> ();
  int labCountFix = propertyUtil.getLabCount();

  // Get Security Properties for ObsNeedingReview
  String obsNeedingReviewSecurity = propertyUtil.getObservationsNeedingReviewSecurity();

  String labQuery = "";
  try{

  if(nbsSecurityObj.getPermission(NBSBOLookup.OBSERVATIONLABREPORT,obsNeedingReviewSecurity))
  {
    String labDataAccessWhereClause = nbsSecurityObj.
        getDataAccessWhereClause(NBSBOLookup.OBSERVATIONLABREPORT,
        						 obsNeedingReviewSecurity);

    labDataAccessWhereClause = labDataAccessWhereClause != null ? "AND " + labDataAccessWhereClause : "";
    labDataAccessWhereClause = labDataAccessWhereClause.replaceAll("program_jurisdiction_oid", "obs.program_jurisdiction_oid");
    labDataAccessWhereClause = labDataAccessWhereClause.replaceAll("shared_ind", "obs.shared_ind");
    logger.debug(" \n labDataAccessWhereClause "+ labDataAccessWhereClause);
    if (propertyUtil.getDatabaseServerType() != null &&
    propertyUtil.
    getDatabaseServerType().equalsIgnoreCase(NEDSSConstants.ORACLE_ID)) {

    labQuery =   " SELECT /*+ FIRST_ROWS */ obs.observation_uid \"ObservationUid\", "
            		+ " obs.local_id \"LocalId\", "
     		        + " obs.shared_ind \"sharedInd\", "
            		+ " OBS.electronic_ind \"electronicInd\", "
                    + " obs.jurisdiction_cd \"JurisdictionCd\", "
                    + " obs.prog_area_cd \"ProgramArea\", "
                    + " obs.rpt_to_state_time \"DateReceived\", "
                    + " obs.version_ctrl_nbr \"versionCtrlNbr\", "
                    + " obs.ctrl_cd_display_form \"ctrlCdDisplayForm\", "
                    + " obs.status_cd \"Status\", "
                    + " obs1.cd_desc_txt \"resultedTest\","
                    + " obs1.cd \"resultedTestCd\", "
                    + " obs1.cd_system_cd \"resultedTestCdSystemCd\" "
                    + " FROM observation obs, act_relationship act, observation obs1"
                    + " WHERE "
                    + " act.target_act_uid = obs.observation_uid + 0 "
                    + " AND act.record_status_cd = 'ACTIVE'"
                    + " and obs.record_status_cd in ('UNPROCESSED', 'UNPROCESSED_PREV_D')" 
                    + " AND act.target_class_cd = 'OBS'"
                    + " AND act.type_cd = 'COMP' "
                    + " AND act.source_class_cd = 'OBS'"
                    + " and obs.ctrl_cd_display_form = 'LabReport' "
                    + " and obs1.obs_domain_cd_st_1 || \'\' = 'Result'"
                    + " AND obs1.observation_uid = act.source_act_uid + 0 "
                    + labDataAccessWhereClause 
                    + " AND rownum <= "+labCountFix+" order by obs.rpt_to_state_time, act.target_act_uid";
    	          /*
    	           * Newer query in may 2009 

          " SELECT  obs2.observation_uid   \"ObservationUid\"  ,"  
        + " obs2.local_id   \"LocalId\"  ,"  
        + " obs2.jurisdiction_cd   \"JurisdictionCd\"  , "  
        + " obs2.prog_area_cd   \"ProgramArea\"  ,"  
        + " obs2.rpt_to_state_time   \"DateReceived\"  ,"  
        + " obs2.ctrl_cd_display_form   ctrlCdDisplayForm  ,"  
        + " obs2.status_cd   \"Status\"  ,"  
        + " obs1.cd_desc_txt   \"resultedTest\"  ," 
        + " obs1.cd   \"resultedTestCd\"  ,"  
        + " obs1.cd_system_cd   \"resultedTestCdSystemCd\" "    
        + " FROM observation obs1," 
        + " act_relationship act, observation obs2"     
               
        + " WHERE "  
        + " act.target_act_uid = obs2.observation_uid"  
        + " and obs1.obs_domain_cd_st_1 = 'Result'" 
        + " AND (act.source_act_uid = obs1.observation_uid) " 
        + " AND (act.target_class_cd = 'OBS')" 
        + " AND (act.type_cd = 'COMP')"  
        + " AND (act.source_class_cd = 'OBS')" 
        + " AND (act.record_status_cd = 'ACTIVE')" 
        + " AND obs2.observation_uid in ( select observation_uid from (select observation_uid from observation  obs "   
        
        + " where (obs.record_status_cd =  'UNPROCESSED'"  
        + " OR record_status_cd = 'UNPROCESSED_PREV_D' )"  
        + " and (obs.ctrl_cd_display_form = 'LabReport' )" 
        + labDataAccessWhereClause
        + " order by  obs.rpt_to_state_time, obs.observation_uid)"
        + " where rownum <= "+ labCountFix       
        + ") ";   
            	           */
    
    }
    
    
    else
      labQuery = " SELECT  obs.observation_uid \"ObservationUid\", "
    		       + " obs.local_id \"LocalId\", "
    		       + " obs.shared_ind \"sharedInd\", "
                   + " OBS.electronic_ind \"electronicInd\", "
                   + " obs.jurisdiction_cd \"JurisdictionCd\", "
                   + " obs.prog_area_cd \"ProgramArea\", "
                   + " obs.rpt_to_state_time \"DateReceived\", "
                   + " obs.version_ctrl_nbr \"versionCtrlNbr\", "
                   + " obs.ctrl_cd_display_form \"ctrlCdDisplayForm\", "
                   + " obs.status_cd \"Status\", "
                   + " obs1.cd_desc_txt \"resultedTest\","
                   + " obs1.cd \"resultedTestCd\", "
                   + " obs1.cd_system_cd \"resultedTestCdSystemCd\" "
                   + " FROM observation obs,observation obs1,"
                   + " act_relationship act "
                   + " WHERE "
                   + " act.target_act_uid = obs.observation_uid "
                   + " AND obs1.obs_domain_cd_st_1 = 'Result'"
                   + " AND (act.source_act_uid = obs1.observation_uid)"
                   + " AND (act.target_class_cd = 'OBS')"
                   + " AND (act.type_cd = 'COMP') "
                   + " AND (act.source_class_cd = 'OBS')"
                   + " AND (act.record_status_cd = 'ACTIVE')"
                   + " and (obs.record_status_cd = 'UNPROCESSED'"  
                   + "	OR obs.record_status_cd = 'UNPROCESSED_PREV_D' )" 
                   + " and obs.ctrl_cd_display_form = 'LabReport' "
                   + labDataAccessWhereClause
                   + " order by obs.rpt_to_state_time, obs.observation_uid";
           logger.info("LabReport Summary  List for Review Query - "+labQuery);
           long timebegin = 0; long timeend = 0;
           timebegin=System.currentTimeMillis();
           logger.debug("\n timebegin  for LabReportSummaryListforReview " + timebegin);

                LabReportResultedtestSummaryVO labReportSummaryVO = new LabReportResultedtestSummaryVO();
                labReportSummaryList = (ArrayList<Object> )preparedStmtMethod(labReportSummaryVO, null,labQuery, NEDSSConstants.SELECT);
           timeend = System.currentTimeMillis();
           logger.debug("\n timeend  for LabReportSummaryListforReview " + timeend);
           logger.debug("\n total time for LabReportSummaryListforReview " + new Long(timeend-timebegin));


    }

  }
  catch(Exception e)
  {
	  
	  logger.error("Error in fetching LabReportSummaryList for Review "); 
      throw new NEDSSSystemException(e.toString());
      

  }

  return labReportSummaryList;
 }


  @SuppressWarnings("unchecked")
private Map<Long, Object> getLabReportPersonInfoforReview(NBSSecurityObj nbsSecurityObj)
 {
   HashMap<Long, Object> observationPersonInfoVOMap = new HashMap<Long, Object>();
   int labCountFix = propertyUtil.getLabCount();
   String labPersonInfoQuery = "";
  try{
	  String obsNeedingReviewSecurity = propertyUtil.getObservationsNeedingReviewSecurity();
	  if(nbsSecurityObj.getPermission(NBSBOLookup.OBSERVATIONLABREPORT,obsNeedingReviewSecurity))
	  {
	    String labDataAccessWhereClause = nbsSecurityObj.getDataAccessWhereClause(NBSBOLookup.OBSERVATIONLABREPORT,obsNeedingReviewSecurity);
	    labDataAccessWhereClause = labDataAccessWhereClause != null ? "AND " + labDataAccessWhereClause : "";
	    if (propertyUtil.getDatabaseServerType() != null &&
      propertyUtil.getDatabaseServerType().equalsIgnoreCase(NEDSSConstants.ORACLE_ID)) {
    labPersonInfoQuery =  /*" SELECT  observation.OBSERVATION_UID \"observationUid\", "
        + " person_name.last_nm \"lastNm\","
        + " person_name.first_nm \"firstNm\","
        + " person.person_parent_uid \"personParentUid\" "
        + " FROM person, person_name, observation, participation"
        + " WHERE person.person_uid = participation.subject_entity_uid "
        + " AND participation.act_uid = observation.observation_uid "
        + " AND participation.type_cd = 'PATSBJ' "
        + " OR ( person.person_uid = person_name.person_uid "
        + " AND person_name.nm_use_cd = 'L' )"
        + " AND observation.observation_uid in( "
        + " select * from "
        + " (select observation_uid "
        + " from observation obsv "
        + " where(obsv.record_status_cd = 'UNPROCESSED' "
        + " OR obsv.record_status_cd = 'UNPROCESSED_PREV_D') "
        + " and obsv.ctrl_cd_display_form = 'LabReport' "
        + labDataAccessWhereClause + ")"
        + " where rownum <= "+labCountFix+" ) order by  observation.rpt_to_state_time,observation.observation_uid ";
     */
    //System.out.println("TESING --> By Pradeep");

        " SELECT  /*+ USE_NL(PARTICIPATION,OBSERVATION, PERSON_NAME) */ observation.OBSERVATION_UID \"observationUid\", "
        + " NVL(person_name.last_nm,'No Last') \"lastNm\","
        + " NVL(person_name.first_nm,'No First') \"firstNm\","
        + " person.local_id \"personLocalId\","
        + " person.person_parent_uid \"personParentUid\", "
        + " person.curr_sex_cd \"currSexCd\", "
        + " person.birth_time \"birthTime\" "
        + " FROM person, person_name, observation, participation"
        + " WHERE ((observation.observation_uid = participation.act_uid)  "
        + " AND (participation.type_cd = 'PATSBJ') "
        + " AND (person.person_uid = participation.subject_entity_uid)  "
        + " AND (person_name.person_uid(+) = person.person_uid)  "
        + " AND(person_name.nm_use_cd (+)= 'L') "
        + " AND (observation.record_status_cd = 'UNPROCESSED' "
        + " OR observation.record_status_cd = 'UNPROCESSED_PREV_D') "
        + " and observation.ctrl_cd_display_form = 'LabReport' "
        + labDataAccessWhereClause + ")"
        + " order by observation.observation_uid ";
    	
    //personLocalId
/*  New query in May 2009
labPersonInfoQuery =   " SELECT  observation.OBSERVATION_UID   \"observationUid\"  ,"  
           +" person_name.last_nm   \"lastNm\"  ," 
           +" person_name.first_nm   \"firstNm\"  , " 
           +" person.person_parent_uid   \"personParentUid\" "    
           +" FROM person, person_name,"
           +" (select observation_uid   from observation obsv "
           +" where (obsv.record_status_cd = 'UNPROCESSED'"
           +" OR record_status_cd = 'UNPROCESSED_PREV_D')" 
           +" and obsv.ctrl_cd_display_form = 'LabReport' "  
           +  labDataAccessWhereClause 
           +" and rownum <= "+labCountFix
           +" order by obsv.rpt_to_state_time,obsv.observation_uid) observation," 
           +" participation " 
           +" WHERE (observation.observation_uid = participation.act_uid) "   
           +" AND (participation.type_cd = 'PATSBJ')"  
           +" AND (person.person_uid = participation.subject_entity_uid)"   
           +" AND (person_name.person_uid(+) = person.person_uid)"   
           +" AND(person_name.nm_use_cd (+)= 'L')";     	
*/
   }
   else
     labPersonInfoQuery =  /*" SELECT  observation.OBSERVATION_UID \"observationUid\", "
        + " person_name.last_nm \"lastNm\","
        + " person_name.first_nm \"firstNm\","
        + " person.person_parent_uid \"personParentUid\" "
        + " FROM person, person_name, observation, participation"
        + " WHERE person.person_uid = participation.subject_entity_uid "
        + " AND participation.act_uid = observation.observation_uid "
        + " AND participation.type_cd = 'PATSBJ' "
        + " AND person.person_uid = person_name.person_uid "
        + " AND person_name.nm_use_cd = 'L' "
        + " AND observation.observation_uid in( "
        + " select top "+labCountFix+" observation_uid from observation obsv "
        + " where (obsv.record_status_cd = 'UNPROCESSED' "
        + " OR obsv.record_status_cd = 'UNPROCESSED_PREV_D' )"
        + " and obsv.ctrl_cd_display_form = 'LabReport'"
        + labDataAccessWhereClause
        + " order by obsv.rpt_to_state_time DESC,obsv.observation_uid)";
		*/    	   	     	 
		" SELECT  observation.OBSERVATION_UID \"observationUid\", "
        + " ISNULL(pnm.last_nm,'No Last') \"lastNm\","
        + " ISNULL(pnm.first_nm,'No First') \"firstNm\","
        + " p.local_id \"personLocalId\","
        + " p.person_parent_uid \"personParentUid\"," 
        + " p.curr_sex_cd \"currSexCd\", "
        + " p.birth_time \"birthTime\" " 
        + " FROM Person p Left outer join Person_name pnm on p.person_uid=pnm.person_uid, observation, participation"
        + " WHERE ((observation.observation_uid = participation.act_uid) "
        + " AND (participation.type_cd = 'PATSBJ')  "
        + " AND (p.person_uid = participation.subject_entity_uid) "
        + " AND (pnm.nm_use_cd = 'L' or pnm.nm_use_cd IS NULL) "
        + " AND (observation.record_status_cd = 'UNPROCESSED' "
        + " OR observation.record_status_cd = 'UNPROCESSED_PREV_D') " 
        + " and observation.ctrl_cd_display_form = 'LabReport' "
        + labDataAccessWhereClause +")"
        + " order by observation.observation_uid";
	    logger.info("LabReport Person Info for Review Query - "+labPersonInfoQuery);
         long timebegin = 0; long timeend = 0;
         timebegin=System.currentTimeMillis();
         logger.debug("\n timebegin  for LabReportSummaryListforReview(PersonInfo) " + timebegin);


                ObservationPersonInfoVO observationPersonInfoVO = new ObservationPersonInfoVO();
                observationPersonInfoVOMap =(HashMap)preparedStmtMethodForMap(observationPersonInfoVO, null,
                                            labPersonInfoQuery, NEDSSConstants.SELECT, "getObservationUid");
         timeend = System.currentTimeMillis();
         logger.debug("\n timeend  for LabReportSummaryListforReview(PersonInfo) " + timeend);
         logger.debug("\n total time for LabReportSummaryListforReview(PersonInfo) " + new Long(timeend-timebegin));


    }

  }
  catch(Exception e)
  {
	  logger.error("Error in fetching person Information data for Lab Report(Observations) for Review "); 
      throw new NEDSSSystemException(e.toString());
  }

  return observationPersonInfoVOMap;
 }


public Collection<Object> getLabReportSummaryVOCollOld(NBSSecurityObj nbsSecurityObj)
 {
   Collection<Object> reportSummaryColl = new ArrayList<Object> ();
   HashMap<Long, Object> personListMap = null;
   ArrayList<Object>  labList = null;
   CachedDropDownValues cdv = new CachedDropDownValues();
   ArrayList<Object>  labSummList = new ArrayList<Object> ();
  try
  {
    personListMap = (HashMap<Long, Object>) getLabReportPersonInfoforReview(
          nbsSecurityObj);
    logger.debug("\n hashMap size "+ personListMap.size());
    labList = (ArrayList<Object> ) getLabReportSummaryListforReview(nbsSecurityObj);
    Collection<Object> resultedColl = new ArrayList<Object> ();
    HashMap<Long, Object> mapForLab = new HashMap<Long, Object>();
    Long observationUid = null;
    if (personListMap != null && labList != null) {
      Iterator<Object> labIt = labList.iterator();
      while (labIt.hasNext()) {
    	  
    	  LabReportResultedtestSummaryVO labReportResultedSummaryVO = (
    		        LabReportResultedtestSummaryVO) labIt.next();
    	  LabReportSummaryVO labReportSummaryVO = new LabReportSummaryVO();      
        
        labReportSummaryVO.setObservationUid(labReportResultedSummaryVO.
                                             getObservationUid());
       
        labReportSummaryVO.setVersionCtrlNbr(labReportResultedSummaryVO.getVersionCtrlNbr());
//        String startDate = labReportResultedSummaryVO.getDateReceived()==null?"No Date":
//			StringUtils.formatDate(labReportResultedSummaryVO.getDateReceived());
//        startDate = startDate+"<br>"+StringUtils.formatDatewithHrMin(labReportResultedSummaryVO.getDateReceived());
//       
       
        labReportSummaryVO.setDateReceived(labReportResultedSummaryVO.getDateReceived());
     //   labReportSummaryVO.setDateReceived(startDate);
        labReportSummaryVO.setLocalId(labReportResultedSummaryVO.getLocalId());
        /* Setting it as "Lab Report" when the cntrl_display_form_cd is "LapReport" */
        labReportSummaryVO.setType(NEDSSConstants.LAB_REPORT_DESC);
        labReportSummaryVO.setStatus(labReportResultedSummaryVO.getStatus());
        labReportSummaryVO.setElectronicInd(labReportResultedSummaryVO.getElectronicInd());
        
        if (labReportSummaryVO.getStatus() != null) {
         String tempStr = cdv.getDescForCode("ACT_OBJ_ST", labReportSummaryVO.getStatus());
         if (tempStr != null)
        labReportSummaryVO.setStatus(tempStr);
          }
        if (labReportResultedSummaryVO.getJurisdictionCd() != null) {
            String tempString = cdv.getJurisdictionDesc(labReportResultedSummaryVO.getJurisdictionCd());
            labReportSummaryVO.setJurisdiction(tempString);
          }
   
        
		/*
        ResultedTestSummaryVO resultedTestSummVO = new ResultedTestSummaryVO();
        resultedTestSummVO.setObservationUid(labReportResultedSummaryVO.
                                             getObservationUid());
        resultedTestSummVO.setResultedTest(labReportResultedSummaryVO.
                                           getResultedTest());
        resultedTestSummVO.setResultedTestCd(labReportResultedSummaryVO.
                                             getResultedTestCd());
        resultedTestSummVO.setCdSystemCd(labReportResultedSummaryVO.
                getResultedTestCdSystemCd());
*/
       // resultedTestSummVO.setTextResultValue("POSITIVE TESTING");


        //Get the Provider
        
        
        
        
        ObservationSummaryDAOImpl osd = new ObservationSummaryDAOImpl();
		if(labReportSummaryVO.getObservationUid()!=null){
			Long summaryLong = labReportSummaryVO.getObservationUid();
			ArrayList<Object>  providerDetails = osd.getProviderInfo(summaryLong,"ORD");
		
			getProviderInformation(providerDetails, labReportSummaryVO);
			/*
			 if (providerDetails != null && providerDetails.size() > 0) {
                  Object[] orderProvider = providerDetails.toArray();

                  if (orderProvider[0] != null) {
                	  labReportSummaryVO.setProviderLastName((String) orderProvider[0]);
                      logger.debug("ProviderLastName: " + (String) orderProvider[0]);
                  }
                  if (orderProvider[1] != null){
                	  labReportSummaryVO.setProviderFirstName((String) orderProvider[1]);
                	  logger.debug("ProviderFirstName: " + (String) orderProvider[1]);
                  }
                  if (orderProvider[2] != null){
                	  labReportSummaryVO.setProviderPrefix((String) orderProvider[2]);
                   logger.debug("ProviderPrefix: " + (String) orderProvider[2]);
                  }
                  if (orderProvider[3] != null){
                	  labReportSummaryVO.setProviderSuffix(( String)orderProvider[3]);
                  	logger.debug("ProviderSuffix: " + (String) orderProvider[3]);
                  }
              	if (orderProvider[4] != null){
              		labReportSummaryVO.setProviderDegree(( String)orderProvider[4]);
                 	logger.debug("ProviderDegree: " + (String) orderProvider[4]);
              	 }
             	if (orderProvider[5] != null){
             		labReportSummaryVO.setProviderUid((String)(orderProvider[5]+""));
                 	logger.debug("orderProviderUid: " + (Long) orderProvider[5]);
             	 }
                }
			 */
			 //Get the Reporting Facility
			 Map<Object,Object> uidMap = osd.getLabParticipations(summaryLong);
              if (uidMap != null && uidMap.containsKey(NEDSSConstants.PAR111_TYP_CD) && labReportSummaryVO != null) {
            	  labReportSummaryVO.setReportingFacility(osd.getReportingFacilityName((Long)uidMap.get(NEDSSConstants.PAR111_TYP_CD)));  
              }
              }
		
	
		
        if(personListMap != null)
        {
          Collection<Object> personList = personListMap.values();
          if (personList != null) {
            Iterator<Object> it = personList.iterator();
            while (it.hasNext()) {
              ObservationPersonInfoVO personInfo = (ObservationPersonInfoVO)
                  it.next();
              if (personInfo.getObservationUid().compareTo(labReportSummaryVO.
                  getObservationUid()) == 0) {
                labReportSummaryVO.setMPRUid(personInfo.getPersonParentUid());
                labReportSummaryVO.setPatientFirstName(personInfo.getFirstNm());
                labReportSummaryVO.setPatientLastName(personInfo.getLastNm());
                labReportSummaryVO.setBirthTime(personInfo.getBirthTime());
                labReportSummaryVO.setCurrSexCd(personInfo.getCurrSexCd());
              
                labReportSummaryVO.setPersonLocalId(personInfo.getPersonLocalId());
                
                
              }
            }
          }
          
     
		  

          if (personListMap.containsKey(labReportSummaryVO.getObservationUid())) {
            ObservationPersonInfoVO observationPersonInfoVO = (
                ObservationPersonInfoVO) personListMap.get(
                labReportResultedSummaryVO.getObservationUid());
            labReportSummaryVO.setMPRUid(observationPersonInfoVO.
                                         getPersonParentUid());
            labReportSummaryVO.setPatientFirstName(observationPersonInfoVO.
                getFirstNm());
            labReportSummaryVO.setPatientLastName(observationPersonInfoVO.
                getLastNm());
            labReportSummaryVO.setBirthTime(observationPersonInfoVO.getBirthTime());
            labReportSummaryVO.setCurrSexCd(observationPersonInfoVO.getCurrSexCd());
           
            labReportSummaryVO.setPersonLocalId(observationPersonInfoVO.getPersonLocalId()); 
    			
          }
        }


		  
		  ArrayList<Object> uidList = new ArrayList<Object> ();
	       uidList.add(labReportSummaryVO.getMPRUid());
	       
	      //Get the Associated investigation(s)
		  RetrieveSummaryVO retrieveSummaryVO = new RetrieveSummaryVO();
			  
		  Collection<Object> invSummaryVOs = retrieveSummaryVO.retrieveInvestgationSummaryVO(uidList, nbsSecurityObj);
		  labReportSummaryVO.setInvSummaryVOs(invSummaryVOs);
      /*
		  if (observationUid == null)//should happen for the first entry
        {

          resultedColl.add(resultedTestSummVO);
          LabReportSummaryVO labReportSummVO = (LabReportSummaryVO) mapForLab.
               get(observationUid);
          labReportSummaryVO.setTheResultedSummaryTestVOCollection(
                resultedColl);
        }
        else if(observationUid!= null && labReportResultedSummaryVO.
            getObservationUid().compareTo(observationUid) == 0) {
          resultedColl.add(resultedTestSummVO);
          LabReportSummaryVO labReportSummVO = (LabReportSummaryVO) mapForLab.
               get(observationUid);
          labReportSummaryVO.setTheResultedSummaryTestVOCollection(
                resultedColl);

       }
       else
       {
           resultedColl = new ArrayList<Object> ();
           resultedColl.add(resultedTestSummVO);
           LabReportSummaryVO labReportSummVO = (LabReportSummaryVO) mapForLab.
              get(observationUid);
          labReportSummaryVO.setTheResultedSummaryTestVOCollection(
              resultedColl);
          //mapForLab.put(observationUid, labReportSummaryVO);
          //resultedColl = new ArrayList<Object> ();
         // resultedColl.add(resultedTestSummVO);
        }
*/
        
        
    


       /* if (observationUid == null ||
            labReportResultedSummaryVO.
            getObservationUid().compareTo(observationUid) == 0) {
          resultedColl.add(resultedTestSummVO);
        }
        else {
          LabReportSummaryVO labReportSummVO = (LabReportSummaryVO) mapForLab.
              get(observationUid);
          labReportSummaryVO.setTheResultedSummaryTestVOCollection(
              resultedColl);
          //mapForLab.put(observationUid, labReportSummaryVO);
          resultedColl = new ArrayList<Object> ();
          resultedColl.add(resultedTestSummVO);
        }  */
        mapForLab.put(labReportResultedSummaryVO.getObservationUid(),
                      labReportSummaryVO);
        observationUid = labReportResultedSummaryVO.getObservationUid();

       /* if (!labIt.hasNext()) {
          LabReportSummaryVO labReportSummVO = (LabReportSummaryVO) mapForLab.
              get(observationUid);
          labReportSummaryVO.setTheResultedSummaryTestVOCollection(
              resultedColl);
        }*/
        
    	//Get tests and susceptibilities
        Long ObservationUID = labReportResultedSummaryVO.getObservationUid();
        ArrayList<Object>  argList = new ArrayList<Object> ();
        argList.clear();
        argList.add("COMP"); // It should be constant
        argList.add(ObservationUID);
        
        getTestAndSusceptibilitiesDRRQ(argList, labReportSummaryVO, null); 
        
        labSummList.add(labReportSummaryVO);
        
      }
    }
    reportSummaryColl.addAll(mapForLab.values());
    this.populateDescTxtFromCachedValuesDRRQ(labSummList);
    
  }
  catch(Exception e)
  {
   logger.error(
           "Error while getting Lab observations for Review",
           e);
   throw new NEDSSSystemException(e.toString());

  }
  
   return reportSummaryColl;
 }




public Collection<Object> getLabReportSummaryVOColl(NBSSecurityObj nbsSecurityObj)
 {
   Collection<Object> reportSummaryColl = new ArrayList<Object> ();
   HashMap<Long, Object> personListMap = null;
   HashMap<Long, ObservationPersonInfoVO> personListObsMap = new HashMap(); //for DRR performance
   ArrayList<Object>  labList = null;
   CachedDropDownValues cdv = new CachedDropDownValues();
   ArrayList<Object>  labSummList = new ArrayList<Object> ();
   
  try
  {
    personListMap = (HashMap<Long, Object>) getLabReportPersonInfoforReview(
          nbsSecurityObj);
    logger.debug("\n hashMap size "+ personListMap.size());

    labList = (ArrayList<Object> ) getLabReportSummaryListforReview(nbsSecurityObj);

     //for DRR performance  	
    if(personListMap != null) {
          Collection<Object> personList = personListMap.values();
          if (personList != null) {
            Iterator<Object> it = personList.iterator();
            while (it.hasNext()) {
              ObservationPersonInfoVO personInfo = (ObservationPersonInfoVO) it.next();
              if (personInfo.getObservationUid() != null)
              	personListObsMap.put(personInfo.getObservationUid(),personInfo);
            }
          }
    }    	
    	
    HashMap<Long, Object> mapForLab = new HashMap<Long, Object>();
    Long observationUid = null;
    if (personListMap != null && labList != null) {
      Iterator<Object> labIt = labList.iterator();
      while (labIt.hasNext()) {
    	  
    	LabReportResultedtestSummaryVO labReportResultedSummaryVO = (LabReportResultedtestSummaryVO) labIt.next();
    	LabReportSummaryVO labReportSummaryVO = new LabReportSummaryVO();      
        
    	observationUid=labReportResultedSummaryVO.getObservationUid();
        labReportSummaryVO.setObservationUid(observationUid);
       
        labReportSummaryVO.setVersionCtrlNbr(labReportResultedSummaryVO.getVersionCtrlNbr());

        labReportSummaryVO.setDateReceived(labReportResultedSummaryVO.getDateReceived());
     //   labReportSummaryVO.setDateReceived(startDate);
        labReportSummaryVO.setLocalId(labReportResultedSummaryVO.getLocalId());
        /* Setting it as "Lab Report" when the cntrl_display_form_cd is "LapReport" */
        labReportSummaryVO.setType(NEDSSConstants.LAB_REPORT_DESC);
        labReportSummaryVO.setStatus(labReportResultedSummaryVO.getStatus());
        labReportSummaryVO.setElectronicInd(labReportResultedSummaryVO.getElectronicInd());
        
        if (labReportSummaryVO.getStatus() != null) {
         String tempStr = cdv.getDescForCode("ACT_OBJ_ST", labReportSummaryVO.getStatus());
         if (tempStr != null)
        labReportSummaryVO.setStatus(tempStr);
          }
        String jurisdictionCd = labReportResultedSummaryVO.getJurisdictionCd();
        if ( jurisdictionCd!= null) {
            String tempString = cdv.getJurisdictionDesc(jurisdictionCd);
            labReportSummaryVO.setJurisdictionCd(jurisdictionCd);
            labReportSummaryVO.setJurisdiction(tempString);
          }
        labReportSummaryVO.setProgramArea(labReportResultedSummaryVO.getProgramArea());
        labReportSummaryVO.setSharedInd(labReportResultedSummaryVO.getSharedInd());
        
        if (labReportSummaryVO.getProgramArea() != null) {
         String tempStr = cdv.getProgramAreaDesc(labReportSummaryVO.getProgramArea());
          if(tempStr != null){
        	  labReportSummaryVO.setProgAreaCd(labReportSummaryVO.getProgramArea());
			  labReportSummaryVO.setProgramArea(tempStr);
          }
        }
        ObservationSummaryDAOImpl osd = new ObservationSummaryDAOImpl();
		if(observationUid!=null){

			String DRR_Skip_Provider_Info = propertyUtil.getProperty("DRR_SKIP_PROVIDER_INFO", "F");
			ArrayList<Object>  providerDetails = new ArrayList<Object>();
			if (DRR_Skip_Provider_Info.equals("F")) {
				providerDetails = osd.getProviderInfo(observationUid,"ORD");
			}
		
			getProviderInformation(providerDetails, labReportSummaryVO);
			
			 //Get the Reporting Facility
			 Map<Object,Object> uidMap = osd.getLabParticipations(observationUid);
              if (uidMap != null && uidMap.containsKey(NEDSSConstants.PAR111_TYP_CD) && labReportSummaryVO != null) {
     			  String DRR_Skip_Rpt_Facil = propertyUtil.getProperty("DRR_SKIP_RPT_FACIL", "F");
    			  if (DRR_Skip_Rpt_Facil.equals("F"))
    				  labReportSummaryVO.setReportingFacility(osd.getReportingFacilityName((Long)uidMap.get(NEDSSConstants.PAR111_TYP_CD)));      	    
              }
          }

        if (labReportSummaryVO.getObservationUid() != null && personListObsMap.containsKey(labReportSummaryVO.getObservationUid())) {
        	ObservationPersonInfoVO personInfo = personListObsMap.get(labReportSummaryVO.getObservationUid());
            labReportSummaryVO.setMPRUid(personInfo.getPersonParentUid());
            labReportSummaryVO.setPatientFirstName(personInfo.getFirstNm());
            labReportSummaryVO.setPatientLastName(personInfo.getLastNm());
            labReportSummaryVO.setBirthTime(personInfo.getBirthTime());
            labReportSummaryVO.setCurrSexCd(personInfo.getCurrSexCd());
            labReportSummaryVO.setPersonLocalId(personInfo.getPersonLocalId());
        }
          
          if (observationUid != null && personListMap.containsKey(observationUid)) {
            ObservationPersonInfoVO observationPersonInfoVO = (ObservationPersonInfoVO) personListMap.get(observationUid);
            labReportSummaryVO.setMPRUid(observationPersonInfoVO.getPersonParentUid());
            labReportSummaryVO.setPatientFirstName(observationPersonInfoVO.getFirstNm());
            labReportSummaryVO.setPatientLastName(observationPersonInfoVO.getLastNm());
            labReportSummaryVO.setBirthTime(observationPersonInfoVO.getBirthTime());
            labReportSummaryVO.setCurrSexCd(observationPersonInfoVO.getCurrSexCd());
            labReportSummaryVO.setPersonLocalId(observationPersonInfoVO.getPersonLocalId()); 
          }
        


		  
		  ArrayList<Object> uidList = new ArrayList<Object> ();
	       uidList.add(labReportSummaryVO.getMPRUid());
	       
	      //Get the Associated investigation(s)
		/*  RetrieveSummaryVO retrieveSummaryVO = new RetrieveSummaryVO();
		  Collection<Object> invSummaryVOs = retrieveSummaryVO.retrieveInvestgationSummaryVO(uidList, nbsSecurityObj);
		  labReportSummaryVO.setInvSummaryVOs(invSummaryVOs);*/
    

			
		    String DRR_Skip_Assoc_Inv = propertyUtil.getProperty("DRR_SKIP_ASSOC_INV", "F");
		    if (DRR_Skip_Assoc_Inv.equals("F")) {
		    	RetrieveSummaryVO rsvo = new RetrieveSummaryVO();
		    	labReportSummaryVO.setInvSummaryVOs(
		    			rsvo.getAssociatedInvListVersion2(observationUid, nbsSecurityObj, NEDSSConstants.OBSERVATION_CLASS_CODE));
		    } else {
		    	//skipping investigations for performance reasons
		    	labReportSummaryVO.setInvSummaryVOs(new ArrayList<Object>());
		    }
			

        mapForLab.put(observationUid, labReportSummaryVO);
    
        
    	//Get tests and susceptibilities
        ArrayList<Object>  argList = new ArrayList<Object> ();
        argList.clear();
        argList.add("COMP"); // It should be constant
        argList.add(observationUid);
        
        getTestAndSusceptibilitiesDRRQ(argList, labReportSummaryVO, null); 
        
        labSummList.add(labReportSummaryVO);
        
      }
    }
    reportSummaryColl.addAll(mapForLab.values());
    this.populateDescTxtFromCachedValuesDRRQ(labSummList);
    
  }
  catch(Exception e)
  {
   logger.error(
           "Error while getting Lab observations for Review",
           e);
   throw new NEDSSSystemException(e.toString());

  }
  
   return reportSummaryColl;
 }

 @SuppressWarnings("unchecked")
private Collection<Object> getMorbReportSummaryListforReview(NBSSecurityObj nbsSecurityObj)
  {
    ArrayList<Object>  morbReportSummaryList = new ArrayList<Object> ();
    int morbCountFix = propertyUtil.getMorbCount();

    // Get Security Properties for ObsNeedingReview
    String obsNeedingReviewSecurity = propertyUtil.getObservationsNeedingReviewSecurity();

    String morbQuery = "";
   try{

   if(nbsSecurityObj.getPermission(NBSBOLookup.OBSERVATIONMORBIDITYREPORT,obsNeedingReviewSecurity))
   {
     String morbDataAccessWhereClause = nbsSecurityObj.getDataAccessWhereClause(NBSBOLookup.OBSERVATIONMORBIDITYREPORT,obsNeedingReviewSecurity);
     morbDataAccessWhereClause = morbDataAccessWhereClause != null ? "AND " + morbDataAccessWhereClause : "";
     //This is just to keep the alias name of the observation table so that you can remove the sub-query  
     morbDataAccessWhereClause = morbDataAccessWhereClause.replaceAll("program_jurisdiction_oid", "obs.program_jurisdiction_oid");
     morbDataAccessWhereClause = morbDataAccessWhereClause.replaceAll("shared_ind", "obs.shared_ind");
     logger.debug(" \n morbDataAccessWhereClause "+ morbDataAccessWhereClause);
     if (propertyUtil.getDatabaseServerType() != null &&
      propertyUtil.getDatabaseServerType().equalsIgnoreCase(NEDSSConstants.ORACLE_ID)) {
    	 
     	 
      /*   New query in May 2009 
         " SELECT obs.local_id \"localId\","
        + " obs.observation_uid \"observationUid\", "
        + " obs.cd \"condition\", "
        + " obs.rpt_to_state_time \"dateReceived\", "
        + " obs.prog_area_cd \"programArea\", "
        + " obs.jurisdiction_cd \"JurisdictionCd\","
        + " obs.record_status_cd \"recordStatusCd\","
        + " obs.ctrl_cd_display_form \"ctrlCdDisplayForm\","
        + " obsvc.CODE \"reportType\" "
        + " from observation obs,"
        + " act_relationship act,"
        + " observation obs1, "
        + " obs_value_coded obsvc"
        + " WHERE"
        + " act.target_act_uid = obs.OBSERVATION_UID"
        + " and  obs1.observation_uid = act.source_act_uid"
        + " and  obsvc.observation_uid = obs1.observation_uid"
        + " and  obs1.CD = 'MRB100'"
        + " and (obs.record_status_cd = 'UNPROCESSED'  OR obs.record_status_cd = 'UNPROCESSED_PREV_D' )"  
        + " and (obs.ctrl_cd_display_form = 'MorbReport' )"
        + morbDataAccessWhereClause 
        + " AND rownum <= " + morbCountFix 
        + "  order by obs.rpt_to_state_time,obs.observation_uid  "; */
    	 /**
    	  * Newer again in May 2009
    	  */
  morbQuery =   " SELECT obs2.local_id \"localId\","
		        + " obs2.shared_ind \"sharedInd\", "
    	        + " obs2.observation_uid \"observationUid\", "
    	        + " obs2.cd \"condition\", "
    	        + " obs2.rpt_to_state_time \"dateReceived\", "
    	        + " obs2.prog_area_cd \"programArea\", "
    	        + " obs2.jurisdiction_cd \"JurisdictionCd\","
    	        + " obs2.record_status_cd \"recordStatusCd\","
    	        + " obs2.ctrl_cd_display_form \"ctrlCdDisplayForm\","
    	        + " obsvc.CODE \"reportType\" "
    	        + " FROM "
    	        + " act_relationship act,"
    	        + " obs_value_coded obsvc, "
                		+ " (select /*+ FIRST_ROWS */" 
                		+ "  obs.local_id ," 
                		+ "  obs.shared_ind ," 
                		+ "  obs.observation_uid    ,"  
                		+ "  obs.cd ,"  
                		+ " obs.rpt_to_state_time, "  
                        + " obs.prog_area_cd,"  
                        + " obs.jurisdiction_cd,"  
                        + " obs.record_status_cd   ," 
                        + " obs.ctrl_cd_display_form "  
                        + " from observation obs "
                        + " where (obs.record_status_cd =  'UNPROCESSED'" 
                        + " OR record_status_cd = 'UNPROCESSED_PREV_D' )" 
                        + " and (obs.ctrl_cd_display_form = 'MorbReport' )"
                        + morbDataAccessWhereClause
                        +" and rownum <= "+morbCountFix +"  order by  obs.rpt_to_state_time, obs.observation_uid) obs2,"
                + " observation obs1 "  
    	        + " WHERE"
    	        + " act.target_act_uid = obs2.OBSERVATION_UID"
    	        + " and  obs1.observation_uid = act.source_act_uid"
    	        + " and  obsvc.observation_uid = obs1.observation_uid"
    	        + " and  obs1.CD = 'MRB100'";       
       }
       else
         morbQuery =  " SELECT top "+morbCountFix+" obs.local_id \"localId\","
         + " obs.observation_uid \"observationUid\", "
         + " obs.shared_ind \"sharedInd\", "
         + " obs.cd \"condition\", "
         + " obs.rpt_to_state_time \"dateReceived\", "
         + " obs.prog_area_cd \"programArea\", "
         + " obs.jurisdiction_cd \"JurisdictionCd\","
         + " obs.record_status_cd \"recordStatusCd\","
         + " obs.ctrl_cd_display_form \"ctrlCdDisplayForm\","
         + " obsvc.CODE \"reportType\" "
         + " from observation obs,"
         + " act_relationship act,"
         + " observation obs1, "
         + " obs_value_coded obsvc"
         + " WHERE"
         + " act.target_act_uid = obs.OBSERVATION_UID"
         + " and  obs1.observation_uid = act.source_act_uid"
         + " and  obsvc.observation_uid = obs1.observation_uid"
         + " and  obs1.CD = 'MRB100'"
         + " and (obs.record_status_cd = 'UNPROCESSED'  OR obs.record_status_cd = 'UNPROCESSED_PREV_D' )"  
         + " and obs.ctrl_cd_display_form = 'MorbReport' "
         + morbDataAccessWhereClause
         + "order by obs.rpt_to_state_time,obs.observation_uid  ";

         logger.info("MorbReport Summary List for Review Query - "+morbQuery);

           long timebegin = 0; long timeend = 0;
           timebegin=System.currentTimeMillis();
           logger.debug("\n timebegin  for MorbReportSummaryListforReview " + timebegin);

                 MorbReportConditionSummaryVO morbReportConditionSummaryVO = new MorbReportConditionSummaryVO();
                 morbReportSummaryList = (ArrayList<Object> )preparedStmtMethod(morbReportConditionSummaryVO, null,morbQuery, NEDSSConstants.SELECT);
           timeend = System.currentTimeMillis();
           logger.debug("\n timeend  for MorbReportSummaryListforReview " + timeend);
           logger.debug("\n total time for MorbReportSummaryListforReview " + new Long(timeend-timebegin));


     }

   }
   catch(Exception e)
   {
	   logger.error("Error in fetching Morbidity Reports for Review ");
	   throw new NEDSSSystemException(e.toString());

   }

   return morbReportSummaryList;
  }
   @SuppressWarnings("unchecked")
private Collection<Object> getMorbReportPersonInfoforReview(NBSSecurityObj nbsSecurityObj)
   {
    ArrayList<Object>  morbReportPersonInfoList = new ArrayList<Object> ();
    int morbCountFix = propertyUtil.getMorbCount();
    String morbPersonInfoQuery = "";
    try{
    	String obsNeedingReviewSecurity = propertyUtil.getObservationsNeedingReviewSecurity();
	    if(nbsSecurityObj.getPermission(NBSBOLookup.OBSERVATIONMORBIDITYREPORT,obsNeedingReviewSecurity))
	    {
	      String morbDataAccessWhereClause = nbsSecurityObj.getDataAccessWhereClause(NBSBOLookup.OBSERVATIONMORBIDITYREPORT,obsNeedingReviewSecurity);
	      morbDataAccessWhereClause = morbDataAccessWhereClause != null ? "AND " + morbDataAccessWhereClause : "";
	      logger.debug(" \n morbDataAccessWhereClause "+ morbDataAccessWhereClause);
	      if (propertyUtil.getDatabaseServerType() != null &&
	      propertyUtil.getDatabaseServerType().equalsIgnoreCase(NEDSSConstants.ORACLE_ID)) {
      morbPersonInfoQuery =
        /*" SELECT  observation.OBSERVATION_UID \"observationUid\", "
        + " person_name.last_nm \"lastNm\","
        + " person_name.first_nm \"firstNm\","
        + " person.person_parent_uid \"personParentUid\" "
        + " FROM person, person_name, observation, participation"
        + " WHERE person.person_uid = participation.subject_entity_uid "
        + " AND participation.act_uid = observation.observation_uid "
        + " AND participation.type_cd = '" +
        NEDSSConstants.MOB_SUBJECT_OF_MORB_REPORT + "'"
        + " AND person.person_uid = person_name.person_uid "
        + " AND person_name.nm_use_cd = 'L' "
        + " AND observation.observation_uid in( "
        + " select * from "
        + " (select observation_uid   from observation obsv"
        + " where (obsv.record_status_cd = '" +
        NEDSSConstants.OBS_UNPROCESSED + "'"
        + " OR record_status_cd = '" +
        NEDSSConstants.OBS_UNPROCESSED_PREV_D + "') "
        + " and (ctrl_cd_display_form = '" +
        NEDSSConstants.MOB_CTRLCD_DISPLAY + "') "
        + morbDataAccessWhereClause + " ) "
        + " where rownum <= "+morbCountFix+" )  order by observation.rpt_to_state_time,observation.OBSERVATION_UID ";
*/
    /*  New query tuned in May 2009- 
     *     " SELECT  observation.OBSERVATION_UID \"observationUid\", "
        + " person_name.last_nm \"lastNm\","
        + " person_name.first_nm \"firstNm\","
        + " person.person_parent_uid \"personParentUid\" "
        + " FROM person, person_name, observation, participation"
        + " WHERE ((observation.observation_uid = participation.act_uid) "
        + " AND (participation.type_cd = '" + NEDSSConstants.MOB_SUBJECT_OF_MORB_REPORT + "')"
        + " AND (person.person_uid = participation.subject_entity_uid) "
        + " AND (person_name.person_uid(+) = person.person_uid) "
        + " AND (person_name.nm_use_cd (+)= 'L') "
   	 	+ " AND (observation.record_status_cd = 'UNPROCESSED'"  
   	 	+ " OR observation.record_status_cd = 'UNPROCESSED_PREV_D')" 
   	 	+ " and observation.ctrl_cd_display_form = 'MorbReport' "
   	 	+ morbDataAccessWhereClause +")"
   	 	+ " order by rpt_to_state_time, observation_uid "
   	 	+ " AND  rownum <= "+morbCountFix; */
      
      /** 
       * Newer query tuned with sub query again
       */
      " SELECT  /*+ USE_NL(PARTICIPATION,OBSERVATION, PERSON_NAME) */ observation.OBSERVATION_UID \"observationUid\", "
      + " NVL(person_name.last_nm,'No Last') \"lastNm\","
      + " NVL(person_name.first_nm,'No First') \"firstNm\","
      + " person.local_id \"personLocalId\","
      + " person.person_parent_uid \"personParentUid\", "
      + " person.curr_sex_cd \"currSexCd\", "
      + " person.birth_time \"birthTime\" "
      + " FROM person, person_name, participation ,"
      + " (select /*+ FIRST_ROWS */ observation_uid   from observation obsv "
      + " where (obsv.record_status_cd = 'UNPROCESSED' "
      + " OR record_status_cd = 'UNPROCESSED_PREV_D') " 
      + " and (ctrl_cd_display_form = 'MorbReport') " 
      +  morbDataAccessWhereClause 
      + " and rownum <=" + morbCountFix
      + " order by obsv.rpt_to_state_time,obsv.observation_uid)  observation"
      + " WHERE (observation.observation_uid = participation.act_uid) "
      + " AND (participation.type_cd = '" + NEDSSConstants.MOB_SUBJECT_OF_MORB_REPORT + "')"
      + " AND (person.person_uid = participation.subject_entity_uid) "
      + " AND (person_name.person_uid(+) = person.person_uid) "
      + " AND (person_name.nm_use_cd (+)= 'L') ";
 
  }
  else
    /*morbPersonInfoQuery =         " SELECT  observation.OBSERVATION_UID \"observationUid\", "
      + " person_name.last_nm \"lastNm\","
      + " person_name.first_nm \"firstNm\","
      + " person.person_parent_uid \"personParentUid\" "
      + " FROM person, person_name, observation, participation"
      + " WHERE person.person_uid = participation.subject_entity_uid "
      + " AND participation.act_uid = observation.observation_uid "
      + " AND participation.type_cd = '" +
      NEDSSConstants.MOB_SUBJECT_OF_MORB_REPORT + "'"
      + " AND person.person_uid = person_name.person_uid "
      + " AND person_name.nm_use_cd = 'L' "
      + " AND observation.observation_uid in( "
         + " select top "+morbCountFix+" observation_uid from observation obsv "
         + " where (obsv.record_status_cd = 'UNPROCESSED' "
         + " OR obsv.record_status_cd = 'UNPROCESSED_PREV_D' ) "
         + " and obsv.ctrl_cd_display_form = 'MorbReport' "
         + morbDataAccessWhereClause
         + " order by obsv.rpt_to_state_time DESC,obsv.observation_uid) ";
		*/  
	morbPersonInfoQuery =  " SELECT top "+morbCountFix+" observation.OBSERVATION_UID \"observationUid\", "
	     + " ISNULL(pnm.last_nm,'No Last') \"lastNm\","
	     + " ISNULL(pnm.first_nm,'No First') \"firstNm\","
		 + " p.local_id \"personLocalId\","
		 + " p.person_parent_uid \"personParentUid\", "
         + " p.curr_sex_cd \"currSexCd\", "
         + " p.birth_time \"birthTime\" "
		 + " FROM person p Left Outer join person_name pnm on p.person_uid=pnm.person_uid, observation, participation"
		 + " WHERE ((observation.observation_uid = participation.act_uid)  "
		 + " AND (participation.type_cd = '" +
		  NEDSSConstants.MOB_SUBJECT_OF_MORB_REPORT + "')"
		 + " AND (p.person_uid = participation.subject_entity_uid) "
		 + " AND (pnm.nm_use_cd = 'L' or pnm.nm_use_cd IS NULL )"
		 + " AND (observation.record_status_cd = 'UNPROCESSED'"  
		 + " OR observation.record_status_cd = 'UNPROCESSED_PREV_D')" 
		 + " and observation.ctrl_cd_display_form = 'MorbReport' "
		 + morbDataAccessWhereClause +")"
		 + " order by rpt_to_state_time, observation_uid ";
	    logger.info("MorbReport Person Info for Review Query - "+morbPersonInfoQuery);
        long timebegin = 0; long timeend = 0;
        timebegin=System.currentTimeMillis();
        logger.debug("\n timebegin  for MorbReportSummaryListforReview(PersonInfo) " + timebegin);

                 ObservationPersonInfoVO observationPersonInfoVO = new ObservationPersonInfoVO();
                 morbReportPersonInfoList = (ArrayList<Object> )preparedStmtMethod(observationPersonInfoVO, null,morbPersonInfoQuery, NEDSSConstants.SELECT);
        timeend = System.currentTimeMillis();
        logger.debug("\n timeend  for MorbReportSummaryListforReview(PersonInfo) " + timeend);
        logger.debug("\n total time for MorbReportSummaryListforReview(PersonInfo) " + new Long(timeend-timebegin));

      }

    }
    catch(Exception e)
    {
    	logger.error("Error in getting the person information for Morb Reports");
    	throw new NEDSSSystemException(e.toString()); 			

    }

    return morbReportPersonInfoList;
   }

   public Collection<Object> getMorbReportSummaryVOColl(NBSSecurityObj nbsSecurityObj)
    {
      Collection<Object> reportSummaryColl = new ArrayList<Object> ();
      ArrayList<Object>  morbList = null;
      ArrayList<Object>  personList = null;
      ArrayList<Object>  labReportUids = new ArrayList<Object> ();
      
      CachedDropDownValues cdv = new CachedDropDownValues();
     try
     {
       personList = (ArrayList<Object> )getMorbReportPersonInfoforReview(
             nbsSecurityObj);
       morbList = (ArrayList<Object> ) getMorbReportSummaryListforReview(nbsSecurityObj);
       HashMap<Long, Object> mapForMorb = new HashMap<Long, Object>();
       Long observationUid = null;
       if (morbList != null) {
         Iterator<Object> morbIt = morbList.iterator();
         while (morbIt.hasNext()) {
           MorbReportConditionSummaryVO morbReportConditionSummaryVO = (
           MorbReportConditionSummaryVO) morbIt.next();
           MorbReportSummaryVO morbReportSummaryVO = new MorbReportSummaryVO();
           morbReportSummaryVO.setObservationUid(morbReportConditionSummaryVO.
                                                getObservationUid());
          /* String startDate = morbReportConditionSummaryVO.getDateReceived()==null?"No Date":StringUtils.formatDate(morbReportConditionSummaryVO.getDateReceived());
           startDate = startDate+"<br>"+StringUtils.formatDatewithHrMin(morbReportConditionSummaryVO.getDateReceived());*/
           
           morbReportSummaryVO.setDateReceived(morbReportConditionSummaryVO.getDateReceived());
       //    morbReportSummaryVO.setDateReceived(startDate);
           morbReportSummaryVO.setLocalId(morbReportConditionSummaryVO.getLocalId());
           /* Setting it as "Morbidity Report" when the cntrl_display_form_cd is MorbReport" */
           morbReportSummaryVO.setType(NEDSSConstants.MORB_REPORT_DESC);
           morbReportSummaryVO.setStatus(morbReportConditionSummaryVO.getStatus());
           if (morbReportSummaryVO.getStatus() != null) {
             String tempStr = cdv.getDescForCode("ACT_OBJ_ST",
                                                 morbReportSummaryVO.getStatus());
             if (tempStr != null)
                morbReportSummaryVO.setStatus(tempStr);
           }
           if (morbReportConditionSummaryVO.getJurisdictionCd() != null) {
               String tempString = cdv.getJurisdictionDesc(morbReportConditionSummaryVO.getJurisdictionCd());
               morbReportSummaryVO.setJurisdictionCd(morbReportConditionSummaryVO.getJurisdictionCd());
               morbReportSummaryVO.setJurisdiction(tempString);
             }
           morbReportSummaryVO.setProgramArea(morbReportConditionSummaryVO.getProgramArea());
           if (morbReportSummaryVO.getProgramArea() != null) {
              String tempStr = cdv.getProgramAreaDesc(morbReportSummaryVO.getProgramArea());
				if(tempStr != null){
					morbReportSummaryVO.setProgAreaCd(morbReportSummaryVO.getProgramArea());
					morbReportSummaryVO.setProgramArea(tempStr);
				}
			   }
           morbReportSummaryVO.setSharedInd(morbReportConditionSummaryVO.getSharedInd());
           morbReportSummaryVO.setReportType(morbReportConditionSummaryVO.getReportType());
           if (morbReportSummaryVO.getReportType() != null) {
             String tempStr = cdv.getDescForCode("MORB_RPT_TYPE", morbReportSummaryVO.getReportType());
             morbReportSummaryVO.setReportTypeDescTxt(tempStr);
           }
           morbReportSummaryVO.setCondition(morbReportConditionSummaryVO.getCondition());
           if (morbReportSummaryVO.getCondition() != null) {
             String tempStr = cdv.getConditionDesc(morbReportSummaryVO.getCondition());
             morbReportSummaryVO.setConditionDescTxt(tempStr);
            }
           if( personList != null)
             {
               Iterator<Object> it = personList.iterator();
               while(it.hasNext())
               {
                 ObservationPersonInfoVO personInfo = (ObservationPersonInfoVO)it.next();
                 if(personInfo.getObservationUid().compareTo(morbReportConditionSummaryVO.getObservationUid())==0)
                 {
                   morbReportSummaryVO.setMPRUid(personInfo.getPersonParentUid());
                   morbReportSummaryVO.setPatientFirstName(personInfo.getFirstNm());
                   morbReportSummaryVO.setPatientLastName(personInfo.getLastNm());
                   morbReportSummaryVO.setCurrSexCd(personInfo.getCurrSexCd());
                   morbReportSummaryVO.setBirthTime(personInfo.getBirthTime());
                   morbReportSummaryVO.setPersonLocalId(personInfo.getPersonLocalId());//Pruthvi: TO DO Change
                 }
               }
             }

           
           
       	ObservationSummaryDAOImpl osd = new ObservationSummaryDAOImpl();
			if(morbReportSummaryVO.getObservationUid()!=null){
				Long summaryLong = morbReportSummaryVO.getObservationUid();
				ArrayList<Object>  providerDetails = osd.getProviderInfo(summaryLong,"PhysicianOfMorb");
			
				getProviderInformation(providerDetails, morbReportSummaryVO);
				/*
	                  Object[] orderProvider = providerDetails.toArray();
	                  if (providerDetails != null && providerDetails.size() > 0) {

	                  if (orderProvider[0] != null) {
	                	  morbReportSummaryVO.setProviderLastName((String) orderProvider[0]);
	                      logger.debug("ProviderLastName: " + (String) orderProvider[0]);
	                  }
	                  if (orderProvider[1] != null){
	                	  morbReportSummaryVO.setProviderFirstName((String) orderProvider[1]);
	                	  logger.debug("ProviderFirstName: " + (String) orderProvider[1]);
	                  }
	                  if (orderProvider[2] != null){
	                	  morbReportSummaryVO.setProviderPrefix((String) orderProvider[2]);
	                   logger.debug("ProviderPrefix: " + (String) orderProvider[2]);
	                  }
	                  if (orderProvider[3] != null){
	                	  morbReportSummaryVO.setProviderSuffix(( String)orderProvider[3]);
	                  	logger.debug("ProviderSuffix: " + (String) orderProvider[3]);
	                  }
	              	if (orderProvider[4] != null){
	              		morbReportSummaryVO.setProviderDegree(( String)orderProvider[4]);
	                 	logger.debug("ProviderDegree: " + (String) orderProvider[4]);
	              	 }
	             	if (orderProvider[5] != null){
	             		morbReportSummaryVO.setProviderUid((String)(orderProvider[5]+""));//TODO: fatima. Change provider Uid to long
	                 	logger.debug("orderProviderUid: " + (Long) orderProvider[5]);
	             	 }
	                }*/
				 Map<Object,Object> uidMap = osd.getLabParticipations(summaryLong);
				 ArrayList<Object>  facilityDetails = osd.getReportingFacilityInfo(summaryLong,NEDSSConstants.MOB_REPORTER_OF_MORB_REPORT);
				 
				 if(facilityDetails!=null && facilityDetails.size()>0 && morbReportSummaryVO != null){
           		  Object[] facility = facilityDetails.toArray();
           		  if (facility[0] != null) {
           			morbReportSummaryVO.setReportingFacility((String) facility[0]);
	                    logger.debug("FacilityName: " + (String) facility[0]);
	                  }
           		  
	              }
			}
			
          /* if (personListMap.containsKey(labReportSummaryVO.getObservationUid())) {
             ObservationPersonInfoVO observationPersonInfoVO = (
                 ObservationPersonInfoVO) personListMap.get(
                 labReportResultedSummaryVO.getObservationUid());
             labReportSummaryVO.setMPRUid(observationPersonInfoVO.
                                          getPersonParentUid());
             labReportSummaryVO.setPatientFirstName(observationPersonInfoVO.
                 getFirstNm());
             labReportSummaryVO.setPatientLastName(observationPersonInfoVO.
                                                   getLastNm());

           } */

           if (observationUid == null ||
               morbReportConditionSummaryVO.
               getObservationUid().compareTo(observationUid) == 0) {

           }
           else {
             MorbReportSummaryVO morbReportSummVO = (MorbReportSummaryVO) mapForMorb.
                 get(observationUid);
             //mapForLab.put(observationUid, labReportSummaryVO);
                       }
           mapForMorb.put(morbReportConditionSummaryVO.getObservationUid(),
                         morbReportSummaryVO);
           observationUid = morbReportConditionSummaryVO.getObservationUid();

           if (!morbIt.hasNext()) {
             MorbReportSummaryVO mobbReportSummVO = (MorbReportSummaryVO) mapForMorb.
                 get(observationUid);

           }
           
           //Get labs from morbidity
           getLabReportsFromMorbidity( morbReportSummaryVO, labReportUids, nbsSecurityObj);
           
           /*
       	//Get tests and susceptibilities
           ArrayList<Object>  argList = new ArrayList<Object> ();
           argList.clear();
           argList.add("COMP"); // It should be constant
           argList.add(observationUid);
           
           //getTestAndSusceptibilitiesDRRQ(argList, morbReportSummaryVO, null); 
           
         //  labSummList.add(morbReportSummaryVO);
*/

         }
       }
       
       reportSummaryColl.addAll(mapForMorb.values());
     }
     catch(Exception e)
     {
      logger.error(
              "Error while getting Morb observations for Review",
              e);
      throw new NEDSSSystemException(e.toString());

     }

      return reportSummaryColl;
    }


// This method is added in place of old method retrieveLabReportSummary() to improve the performance of the page
   
   public HashMap<Object, Object> retrieveLabReportSummaryRevisited(Collection<Object> labReportUids, boolean isCDCFormPrintCase,
                                             NBSSecurityObj nbsSecurityObj, String uidType) {
    //labReportUids = getLongArrayList(labReportUids);
	HashMap<Object, Object> labReportSummarMap = getObservationSummaryListForWorkupRevisited(
        labReportUids, isCDCFormPrintCase, nbsSecurityObj, uidType);

    return labReportSummarMap;
  }
   
   /**
    * retrieveLabReportSummaryRevisitedFromMorbDRRQ: method used for retrieving the morb's labs
    * @param labReportUids
    * @param isCDCFormPrintCase
    * @param nbsSecurityObj
    * @param uidType
    * @return
    */
   public HashMap<Object, Object> retrieveLabReportSummaryRevisitedFromMorbDRRQ(Collection<Object> labReportUids, boolean isCDCFormPrintCase,
           NBSSecurityObj nbsSecurityObj, String uidType) {
	//labReportUids = getLongArrayList(labReportUids);
	HashMap<Object, Object> labReportSummarMap = getObservationSummaryListForWorkupRevisitedFromMorbDRRQ(
	labReportUids, isCDCFormPrintCase, nbsSecurityObj, uidType);
	
	return labReportSummarMap;
   }
   
   ArrayList<Object> getLabsFromUid(Collection<Object> uidList, NBSSecurityObj nbsSecurityObj, String uidType){
	   

	   
		    int count = 0;
		    ArrayList<Object>  labList = new ArrayList<Object> ();
		    
		    if (uidList != null) {

		      //This is the where Clause
		      String dataAccessWhereClause = nbsSecurityObj.getDataAccessWhereClause(
		              NBSBOLookup.OBSERVATIONLABREPORT, NBSOperationLookup.VIEW);
		      if (dataAccessWhereClause == null) {
		        dataAccessWhereClause = "";
		      }
		      else {
		        dataAccessWhereClause = " AND " + dataAccessWhereClause;
		      }

		      LabReportSummaryVO labVO = new LabReportSummaryVO();
		      
		      ArrayList<Object>  argList = new ArrayList<Object> ();
		      Long LabAsSourceForInvestigation = null;
		      try {
		        
		        Timestamp fromTime = null;
		        //   uidList = (ArrayList<Object> )getUidSummaryVOArrayList(uidList);
		        Iterator<Object> itLabId = uidList.iterator();
		        while (itLabId.hasNext()) {
		        	if(uidType.equals("PERSON_PARENT_UID")){
			          Long uid = (Long) itLabId.next();
			          //Long observationUid = vo.getUid();
			          argList.clear();
			          argList.add(uid);
			          String selQuery = WumSqlQuery.SELECT_LABSUMMARY_FORWORKUPNEW+ dataAccessWhereClause;
			          labList = (ArrayList<Object> ) preparedStmtMethod(labVO, argList, selQuery, NEDSSConstants.SELECT);
			          count = count + 1;
		        	}else if(uidType.equals("LABORATORY_UID"))
		        	{
		        	  UidSummaryVO vo = (UidSummaryVO) itLabId.next();	
		        	  Long observationUid = vo.getUid();
		        	  fromTime = vo.getAddTime();
//		        	  DateFormat format =  DateFormat.getDateInstance(DateFormat.MEDIUM);
//		        	  if(vo.getStatusTime()!=null && format.format(vo.getStatusTime()).equals(format.format(fromTime))){
		        	 if(vo.getStatusTime()!=null && vo.getStatusTime().compareTo(fromTime)==0){
		        		  LabAsSourceForInvestigation=vo.getUid();
		        	  }
		        	  argList.clear();
		  	          argList.add(observationUid);
		  	          labList = (ArrayList<Object> ) preparedStmtMethod(labVO, argList,
		  	              WumSqlQuery.SELECT_LABSUMMARY_FORWORKUP, NEDSSConstants.SELECT);
		  	          count = count + 1;
		        	}
		        }
		      }
		      catch (Exception ex) {
		          logger.error(
		              "Error while getting observations for given set of UIDS in workup",
		              ex);
		          throw new NEDSSSystemException(ex.toString());
		        }	
		    }

		    return labList;
   }

  @SuppressWarnings("unchecked") 
protected HashMap<Object, Object> getObservationSummaryListForWorkupRevisited(Collection<Object> uidList,boolean isCDCFormPrintCase,
      NBSSecurityObj nbsSecurityObj, String uidType) throws NEDSSSystemException {
    ArrayList<Object>  labSummList = new ArrayList<Object> ();
    ArrayList<Object>  labEventList = new ArrayList<Object> ();
    int count = 0;
    
   
    Long providerUid=null;
    RetrieveSummaryVO rSummaryVO = new RetrieveSummaryVO();
    /*
         long t1begin = 0; long t2begin = 0; long t3begin = 0; long t4begin = 0;
         long t1end = 0; long t2end = 0; long t3end = 0; long t4end = 0;
         long beforeFindPerson = 0; long afterFindPerson = 0; long totalFindPerson = 0;
         long beforeReflex = 0; long afterReflex = 0; long totalReflex = 0;
         long beforeSus = 0; long afterSus = 0; long totalSus = 0;
         long beforeReflex2 = 0; long afterReflex2 = 0; long totalReflex2 = 0;
     */
    //timing
    //t1begin=System.currentTimeMillis();
    //System.out.println("Begin getObservation Loop: " + t1begin);
    //System.out.println("lab uid list ==>" + uidList);
    if (uidList != null) {
//      if (!nbsSecurityObj.getPermission(NBSBOLookup.OBSERVATIONLABREPORT,
//                                        NBSOperationLookup.VIEW)) {
//               logger.info("Permission for retrieveObservationSummaryListForWorkup() = " + nbsSecurityObj.getPermission(NBSBOLookup.OBSERVATIONLABREPORT));
//        throw new NEDSSSystemException("no permissions to view an observation");
 //     }
      
      //This is the where Clause
      String dataAccessWhereClause = nbsSecurityObj.getDataAccessWhereClause(
              NBSBOLookup.OBSERVATIONLABREPORT, NBSOperationLookup.VIEW);
      if (dataAccessWhereClause == null) {
        dataAccessWhereClause = "";
      }
      else {
        dataAccessWhereClause = " AND " + dataAccessWhereClause;
      }
      
      
     
      LabReportSummaryVO labVO = new LabReportSummaryVO();
      
     
      ArrayList<Object>  labList = new ArrayList<Object> ();
      ArrayList<Object>  argList = new ArrayList<Object> ();
      Long LabAsSourceForInvestigation = null;
      try {
        
        Timestamp fromTime = null;
        //   uidList = (ArrayList<Object> )getUidSummaryVOArrayList(uidList);
        Iterator<Object> itLabId = uidList.iterator();
        while (itLabId.hasNext()) {
        	if(uidType.equals("PERSON_PARENT_UID")){
	          Long uid = (Long) itLabId.next();
	          //Long observationUid = vo.getUid();
	          argList.clear();
	          argList.add(uid);
	          String selQuery = WumSqlQuery.SELECT_LABSUMMARY_FORWORKUPNEW+ dataAccessWhereClause;
	          labList = (ArrayList<Object> ) preparedStmtMethod(labVO, argList, selQuery, NEDSSConstants.SELECT);
	          count = count + 1;
        	}else if(uidType.equals("LABORATORY_UID"))
        	{
        	  UidSummaryVO vo = (UidSummaryVO) itLabId.next();	
        	  Long observationUid = vo.getUid();
        	  fromTime = vo.getAddTime();
//        	  DateFormat format =  DateFormat.getDateInstance(DateFormat.MEDIUM);
//        	  if(vo.getStatusTime()!=null && format.format(vo.getStatusTime()).equals(format.format(fromTime))){
        	 if(vo.getStatusTime()!=null && vo.getStatusTime().compareTo(fromTime)==0){
        		  LabAsSourceForInvestigation=vo.getUid();
        	  }
        	  argList.clear();
  	          argList.add(observationUid);
  	          labList = (ArrayList<Object> ) preparedStmtMethod(labVO, argList,
  	              WumSqlQuery.SELECT_LABSUMMARY_FORWORKUP, NEDSSConstants.SELECT);
  	          count = count + 1;
        	}
           if(labList != null) {
            //Timing
            //t2begin = System.currentTimeMillis();
            Iterator<Object> labIt = labList.iterator();
            while (labIt.hasNext()) {
              LabReportSummaryVO labRepVO = (LabReportSummaryVO) labIt.next();
              labRepVO.setActivityFromTime(fromTime);
              LabReportSummaryVO labRepSumm = null;
              LabReportSummaryVO labRepEvent = null;
              //Populate name properties and report type cd for lab report summary vo, labVO
              //Timing
              //beforeFindPerson = System.currentTimeMillis();
              //Object[] vals = findPatientLegalName(observationUid, nbsSecurityObj);

              //ArrayList<Object> patientPersonInfo = (ArrayList<Object> )preparedStmtMethod(labVO, argList, QUICK_FIND_PATIENT, NEDSSConstants.SELECT);
              ObservationSummaryDAOImpl osd = new ObservationSummaryDAOImpl();
              OrganizationNameDAOImpl organizationDao = new OrganizationNameDAOImpl();
              Map<Object,Object> uidMap = osd.getLabParticipations(labRepVO.getObservationUid());
              if (uidMap != null && uidMap.containsKey(NEDSSConstants.PAR110_TYP_CD) ){
            		if (labRepVO.getRecordStatusCd()!=null && (labRepVO.getRecordStatusCd().equals("UNPROCESSED")||labRepVO.getRecordStatusCd().equals("UNPROCESSED_PREV_D"))) {
            			labRepSumm = labRepVO ;
            			labRepSumm.setMPRUid((Long)uidMap.get(NEDSSConstants.PAR110_TYP_CD));
            		}
            		if(labRepVO.getRecordStatusCd()!=null && !labRepVO.getRecordStatusCd().equals("LOG_DEL")){
            			labRepEvent = labRepVO;
            			labRepEvent.setMPRUid((Long)uidMap.get(NEDSSConstants.PAR110_TYP_CD));
            		}
              }
            	  
              ArrayList<Object>  valList = osd.getPatientPersonInfo(labRepVO.getObservationUid());
              ArrayList<Object>  providerDetails = osd.getProviderInfo(labRepVO.getObservationUid(),"ORD");
              ArrayList<Object>  actIdDetails = osd.getActIdDetails(labRepVO.getObservationUid());
              Map<Object,Object> associationsMap = rSummaryVO.getAssociatedInvList(labRepVO.getObservationUid(),nbsSecurityObj, "OBS");

              //Timing
              //afterFindPerson = System.currentTimeMillis();
              //totalFindPerson += (afterFindPerson - beforeFindPerson);

              /*if(vals !=null)
                      {
                      if(vals[0] !=null)
                         labrepSumm.setPatientFirstName((String)vals[0]);
                      if(vals[1] != null)
                        labrepSumm.setPatientLastName((String)vals[1]);
                      if(vals[2] != null)
                        labrepSumm.setType(((ObservationVO)vals[2]).getTheObservationDT().getCtrlCdDisplayForm());
                      labrepSumm.setMPRUid((Long)vals[3]);
                      }
               */
              //Iterator patientPersonInfoIt = patientPersonInfo.iterator();
              /*if (valList != null && valList.size() > 0) {
                Object[] vals = valList.toArray();

                if (vals[0] != null) {
                  labrepSumm.setPatientFirstName( (String) vals[0]);
                  logger.debug("FirstName: " + (String) vals[0]);
                }
                if (vals[1] != null)
                  labrepSumm.setPatientLastName( (String) vals[1]);
                if (vals[2] != null)
                  labrepSumm.setType( ( (String) vals[2]));
               // labrepSumm.setMPRUid( (Long) vals[3]);
              }*/
              if(labRepEvent!=null){
            	  labRepEvent.setAssociationsMap(associationsMap);
              }
              if(labRepSumm!=null){
            	  labRepSumm.setAssociationsMap(associationsMap);
              }
              if (uidMap != null && uidMap.containsKey(NEDSSConstants.PAR111_TYP_CD) && labRepEvent != null) {
            	  labRepEvent.setReportingFacility(osd.getReportingFacilityName((Long)uidMap.get(NEDSSConstants.PAR111_TYP_CD)));
               }
              if (uidMap != null && uidMap.containsKey(NEDSSConstants.PAR111_TYP_CD) && labRepSumm != null) {
            	  labRepSumm.setReportingFacility(osd.getReportingFacilityName((Long)uidMap.get(NEDSSConstants.PAR111_TYP_CD)));
               }
              if (uidMap != null && uidMap.containsKey(NEDSSConstants.PAR104_TYP_CD) && labRepEvent != null) {
            	  CachedDropDownValues cddv = new CachedDropDownValues();
            	  labRepEvent.setSpecimenSource(cddv.getDescForCode("SPECMN_SRC",osd.getSpecimanSource((Long)uidMap.get(NEDSSConstants.PAR104_TYP_CD))));
               }
              if (uidMap != null && uidMap.containsKey(NEDSSConstants.PAR104_TYP_CD) && labRepSumm != null) {
            	  CachedDropDownValues cddv = new CachedDropDownValues();
            	  labRepSumm.setSpecimenSource(cddv.getDescForCode("SPECMN_SRC",osd.getSpecimanSource((Long)uidMap.get(NEDSSConstants.PAR104_TYP_CD))));
               }
              
              providerUid = getProviderInformation(providerDetails, labRepEvent);
              
              /*
              if (providerDetails != null && providerDetails.size() > 0 && labRepEvent != null) {
                  Object[] orderProvider = providerDetails.toArray();

                  if (orderProvider[0] != null) {
                	  labRepEvent.setProviderLastName((String) orderProvider[0]);
                    logger.debug("ProviderLastName: " + (String) orderProvider[0]);
                  }
                  if (orderProvider[1] != null)
                	  labRepEvent.setProviderFirstName((String) orderProvider[1]);
                  logger.debug("ProviderFirstName: " + (String) orderProvider[1]);
                  if (orderProvider[2] != null)
                	  labRepEvent.setProviderPrefix((String) orderProvider[2]);
                   logger.debug("ProviderPrefix: " + (String) orderProvider[2]);
                  if (orderProvider[3] != null)
                	  labRepEvent.setProviderSuffix(( String)orderProvider[3]);
                  	logger.debug("ProviderSuffix: " + (String) orderProvider[3]);

              	if (orderProvider[4] != null)
              		labRepEvent.setDegree(( String)orderProvider[4]);
                 	logger.debug("ProviderDegree: " + (String) orderProvider[4]);
             	if (orderProvider[5] != null)
             		providerUid= (Long)orderProvider[5];
                 	logger.debug("orderProviderUid: " + (Long) orderProvider[5]);

                }*/
              if(isCDCFormPrintCase && providerUid!=null && LabAsSourceForInvestigation!=null){
            	  ProviderDataForPrintVO providerDataForPrintVO =null;
            	  if(labRepEvent.getProviderDataForPrintVO()==null){
            		  providerDataForPrintVO =new ProviderDataForPrintVO();
            		  labRepEvent.setProviderDataForPrintVO(providerDataForPrintVO);
               	  }           	  
                  Long orderingFacilityUid = null;
                  if(uidMap.get(NEDSSConstants.PAR101_TYP_CD)!=null){
                	  orderingFacilityUid=(Long)uidMap.get(NEDSSConstants.PAR101_TYP_CD);
                  }
                  if(orderingFacilityUid!=null){
                	  ArrayList list = (ArrayList)organizationDao.load(orderingFacilityUid);
                	 if( !list.isEmpty()) {
                		 OrganizationNameDT dt = (OrganizationNameDT)list.get(0);
                	 
                		  providerDataForPrintVO.setFacilityName(dt.getNmTxt());
                	 }
                	  osd.getOrderingFacilityAddress(providerDataForPrintVO, orderingFacilityUid);
                      osd.getOrderingFacilityPhone(providerDataForPrintVO, orderingFacilityUid);
                  }
                  if(providerUid!=null){
                	  osd.getOrderingPersonAddress(providerDataForPrintVO, providerUid);
                      osd.getOrderingPersonPhone(providerDataForPrintVO, providerUid);
                  }
              }
              
              getProviderInformation(providerDetails, labRepSumm);
              
              /*
              if (providerDetails != null && providerDetails.size() > 0 && labRepSumm != null) {
                  Object[] orderProvider = providerDetails.toArray();

                  if (orderProvider[0] != null) {
                	  labRepSumm.setProviderLastName((String) orderProvider[0]);
                    logger.debug("ProviderLastName: " + (String) orderProvider[0]);
                  }
                  if (orderProvider[1] != null)
                	  labRepSumm.setProviderFirstName((String) orderProvider[1]);
                  logger.debug("ProviderFirstName: " + (String) orderProvider[1]);
                  if (orderProvider[2] != null)
                	  labRepSumm.setProviderPrefix((String) orderProvider[2]);
                   logger.debug("ProviderPrefix: " + (String) orderProvider[2]);
                  if (orderProvider[3] != null)
                	  labRepSumm.setProviderSuffix(( String)orderProvider[3]);
                  	logger.debug("ProviderSuffix: " + (String) orderProvider[3]);

                 if (orderProvider[4] != null)
              		labRepSumm.setDegree(( String)orderProvider[4]);
                 	logger.debug("ProviderDegree: " + (String) orderProvider[4]);

                }*/
              
              if (actIdDetails != null && actIdDetails.size() > 0 && labRepEvent != null) {
                  Object[] accessionNumber = actIdDetails.toArray();
                  if (accessionNumber[0] != null) {
                	  labRepEvent.setAccessionNumber((String) accessionNumber[0]);
                    logger.debug("AccessionNumber: " + (String) accessionNumber[0]);
                  }
              }
              if (actIdDetails != null && actIdDetails.size() > 0 && labRepSumm != null) {
                  Object[] accessionNumber = actIdDetails.toArray();
                  if (accessionNumber[0] != null) {
                	  labRepSumm.setAccessionNumber((String) accessionNumber[0]);
                    logger.debug("AccessionNumber: " + (String) accessionNumber[0]);
                  }
              }

              if(labRepEvent!= null) 
            	  labEventList.add(labRepEvent);
              if(labRepSumm !=null)
            	  labSummList.add(labRepSumm);
              
              Long ObservationUID = labRepVO.getObservationUid();
              argList.clear();
              argList.add("COMP"); // It should be constant
              argList.add(ObservationUID);
              //beforeReflex = System.currentTimeMillis();
//System.out.println("id        ......." +  ObservationUID);
//System.out.println("lab querty......." +  WumSqlQuery.SELECT_LABRESULTED_REFLEXTEST_SUMMARY_FORWORKUP);
			getTestAndSusceptibilities(argList, labRepEvent, labRepSumm);
        }

           }
        }
        }
      catch (Exception ex) {
        logger.error(
            "Error while getting observations for given set of UIDS in workup",
            ex);
        throw new NEDSSSystemException(ex.toString());
      }
  }
    
  //t1end = System.currentTimeMillis();

  /*System.out.println("T1: " + new Long(t1end-t1begin));
   System.out.println("T2: " + new Long(t2end-t2begin));
   System.out.println("T3: " + new Long(t3end-t3begin));
   System.out.println("T4: " + new Long(t4end-t4begin));
   System.out.println("findPerson: " + new Long(totalFindPerson));
   System.out.println("Reflex: " + new Long(totalReflex));
   System.out.println("Sus: " + new Long(totalSus));
   System.out.println("Reflex2: " + new Long(totalReflex2));
   */
//System.out.println("lab summ list size hello ===>" + labSummList.size());
  //System.out.println( "\nCount , countResult , countSus  "+  count + " "+ countResult+ " " +countSus );
  this.populateDescTxtFromCachedValues(labSummList);
  this.populateDescTxtFromCachedValues(labEventList);
  HashMap<Object, Object> returnMap = new HashMap<Object, Object>();
  returnMap.put("labSummList", labSummList);
  returnMap.put("labEventList", labEventList);
  return returnMap;
} //end of getObservationSummaryVOCollectionForWorkup()

    
/**
 * getObservationSummaryListForWorkupRevisitedFromMorbDRRQ
 * @param uidList
 * @param isCDCFormPrintCase
 * @param nbsSecurityObj
 * @param uidType
 * @return
 * @throws NEDSSSystemException
 */
    @SuppressWarnings("unchecked") 
  protected HashMap<Object, Object> getObservationSummaryListForWorkupRevisitedFromMorbDRRQ(Collection<Object> uidList,boolean isCDCFormPrintCase,
        NBSSecurityObj nbsSecurityObj, String uidType) throws NEDSSSystemException {
      ArrayList<Object>  labEventList = new ArrayList<Object> ();
      int count = 0;

      Long providerUid=null;
      RetrieveSummaryVO rSummaryVO = new RetrieveSummaryVO();

      if (uidList != null) {

        //This is the where Clause
        String dataAccessWhereClause = nbsSecurityObj.getDataAccessWhereClause(
                NBSBOLookup.OBSERVATIONLABREPORT, NBSOperationLookup.VIEW);
        if (dataAccessWhereClause == null) {
          dataAccessWhereClause = "";
        }
        else {
          dataAccessWhereClause = " AND " + dataAccessWhereClause;
        }
        
        
       
        LabReportSummaryVO labVO = new LabReportSummaryVO();
        
       
        ArrayList<Object>  labList = new ArrayList<Object> ();
        ArrayList<Object>  argList = new ArrayList<Object> ();
        Long LabAsSourceForInvestigation = null;
        try {
          
          Timestamp fromTime = null;
          //   uidList = (ArrayList<Object> )getUidSummaryVOArrayList(uidList);
          Iterator<Object> itLabId = uidList.iterator();
          while (itLabId.hasNext()) {
          	if(uidType.equals("PERSON_PARENT_UID")){
  	          Long uid = (Long) itLabId.next();
  	          //Long observationUid = vo.getUid();
  	          argList.clear();
  	          argList.add(uid);
  	          String selQuery = WumSqlQuery.SELECT_LABSUMMARY_FORWORKUPNEW+ dataAccessWhereClause;
  	          labList = (ArrayList<Object> ) preparedStmtMethod(labVO, argList, selQuery, NEDSSConstants.SELECT);
  	          count = count + 1;
          	}else if(uidType.equals("LABORATORY_UID"))
          	{
          	  UidSummaryVO vo = (UidSummaryVO) itLabId.next();	
          	  Long observationUid = vo.getUid();
          	  fromTime = vo.getAddTime();
//          	  DateFormat format =  DateFormat.getDateInstance(DateFormat.MEDIUM);
//          	  if(vo.getStatusTime()!=null && format.format(vo.getStatusTime()).equals(format.format(fromTime))){
          	 if(vo.getStatusTime()!=null && vo.getStatusTime().compareTo(fromTime)==0){
          		  LabAsSourceForInvestigation=vo.getUid();
          	  }
          	  argList.clear();
    	          argList.add(observationUid);
    	          labList = (ArrayList<Object> ) preparedStmtMethod(labVO, argList,
    	              WumSqlQuery.SELECT_LABSUMMARY_FORWORKUP, NEDSSConstants.SELECT);
    	          count = count + 1;
          	}
             if(labList != null) {
              //Timing
              //t2begin = System.currentTimeMillis();
              Iterator<Object> labIt = labList.iterator();
              while (labIt.hasNext()) {
                LabReportSummaryVO labRepVO = (LabReportSummaryVO) labIt.next();
                labRepVO.setActivityFromTime(fromTime);
                LabReportSummaryVO labRepSumm = null;
                LabReportSummaryVO labRepEvent = null;
                //Populate name properties and report type cd for lab report summary vo, labVO
                //Timing
                //beforeFindPerson = System.currentTimeMillis();
                //Object[] vals = findPatientLegalName(observationUid, nbsSecurityObj);

                //ArrayList<Object> patientPersonInfo = (ArrayList<Object> )preparedStmtMethod(labVO, argList, QUICK_FIND_PATIENT, NEDSSConstants.SELECT);
                ObservationSummaryDAOImpl osd = new ObservationSummaryDAOImpl();
                OrganizationNameDAOImpl organizationDao = new OrganizationNameDAOImpl();
                Map<Object,Object> uidMap = osd.getLabParticipations(labRepVO.getObservationUid());
                if (uidMap != null && uidMap.containsKey(NEDSSConstants.PAR110_TYP_CD) ){
              		if (labRepVO.getRecordStatusCd()!=null && (labRepVO.getRecordStatusCd().equals("UNPROCESSED")||labRepVO.getRecordStatusCd().equals("UNPROCESSED_PREV_D"))) {
              			labRepSumm = labRepVO ;
              			labRepSumm.setMPRUid((Long)uidMap.get(NEDSSConstants.PAR110_TYP_CD));
              		}
              		if(labRepVO.getRecordStatusCd()!=null && !labRepVO.getRecordStatusCd().equals("LOG_DEL")){
              			labRepEvent = labRepVO;
              			labRepEvent.setMPRUid((Long)uidMap.get(NEDSSConstants.PAR110_TYP_CD));
              		}
                }
              	  
                ArrayList<Object>  providerDetails = osd.getProviderInfo(labRepVO.getObservationUid(),"ORD");
                ArrayList<Object>  actIdDetails = osd.getActIdDetails(labRepVO.getObservationUid());
                Map<Object,Object> associationsMap = rSummaryVO.getAssociatedInvList(labRepVO.getObservationUid(),nbsSecurityObj, "OBS");

                if(labRepEvent!=null){
              	  labRepEvent.setAssociationsMap(associationsMap);
                }
                if(labRepSumm!=null){
              	  labRepSumm.setAssociationsMap(associationsMap);
                }
                if (uidMap != null && uidMap.containsKey(NEDSSConstants.PAR111_TYP_CD) && labRepEvent != null) {
              	  labRepEvent.setReportingFacility(osd.getReportingFacilityName((Long)uidMap.get(NEDSSConstants.PAR111_TYP_CD)));
                 }
                if (uidMap != null && uidMap.containsKey(NEDSSConstants.PAR111_TYP_CD) && labRepSumm != null) {
              	  labRepSumm.setReportingFacility(osd.getReportingFacilityName((Long)uidMap.get(NEDSSConstants.PAR111_TYP_CD)));
                 }
                if (uidMap != null && uidMap.containsKey(NEDSSConstants.PAR104_TYP_CD) && labRepEvent != null) {
              	  CachedDropDownValues cddv = new CachedDropDownValues();
              	  labRepEvent.setSpecimenSource(cddv.getDescForCode("SPECMN_SRC",osd.getSpecimanSource((Long)uidMap.get(NEDSSConstants.PAR104_TYP_CD))));
                 }
                if (uidMap != null && uidMap.containsKey(NEDSSConstants.PAR104_TYP_CD) && labRepSumm != null) {
              	  CachedDropDownValues cddv = new CachedDropDownValues();
              	  labRepSumm.setSpecimenSource(cddv.getDescForCode("SPECMN_SRC",osd.getSpecimanSource((Long)uidMap.get(NEDSSConstants.PAR104_TYP_CD))));
                 }
                
                providerUid = getProviderInformation(providerDetails, labRepEvent);
                
                if(isCDCFormPrintCase && providerUid!=null && LabAsSourceForInvestigation!=null){
              	  ProviderDataForPrintVO providerDataForPrintVO =null;
              	  if(labRepEvent.getProviderDataForPrintVO()==null){
              		  providerDataForPrintVO =new ProviderDataForPrintVO();
              		  labRepEvent.setProviderDataForPrintVO(providerDataForPrintVO);
                 	  }           	  
                    Long orderingFacilityUid = null;
                    if(uidMap.get(NEDSSConstants.PAR101_TYP_CD)!=null){
                  	  orderingFacilityUid=(Long)uidMap.get(NEDSSConstants.PAR101_TYP_CD);
                    }
                    if(orderingFacilityUid!=null){
                  	  ArrayList list = (ArrayList)organizationDao.load(orderingFacilityUid);
                  	 if( !list.isEmpty()) {
                  		 OrganizationNameDT dt = (OrganizationNameDT)list.get(0);
                  	 
                  		  providerDataForPrintVO.setFacilityName(dt.getNmTxt());
                  	 }
                  	  osd.getOrderingFacilityAddress(providerDataForPrintVO, orderingFacilityUid);
                        osd.getOrderingFacilityPhone(providerDataForPrintVO, orderingFacilityUid);
                    }
                    if(providerUid!=null){
                  	  osd.getOrderingPersonAddress(providerDataForPrintVO, providerUid);
                        osd.getOrderingPersonPhone(providerDataForPrintVO, providerUid);
                    }
                }
                
                getProviderInformation(providerDetails, labRepSumm);
               
                if (actIdDetails != null && actIdDetails.size() > 0 && labRepEvent != null) {
                    Object[] accessionNumber = actIdDetails.toArray();
                    if (accessionNumber[0] != null) {
                  	  labRepEvent.setAccessionNumber((String) accessionNumber[0]);
                      logger.debug("AccessionNumber: " + (String) accessionNumber[0]);
                    }
                }
                if (actIdDetails != null && actIdDetails.size() > 0 && labRepSumm != null) {
                    Object[] accessionNumber = actIdDetails.toArray();
                    if (accessionNumber[0] != null) {
                  	  labRepSumm.setAccessionNumber((String) accessionNumber[0]);
                      logger.debug("AccessionNumber: " + (String) accessionNumber[0]);
                    }
                }

                if(labRepEvent!= null) 
              	  labEventList.add(labRepEvent);
                
                Long ObservationUID = labRepVO.getObservationUid();
                argList.clear();
                argList.add("COMP"); // It should be constant
                argList.add(ObservationUID);

  			getTestAndSusceptibilitiesDRRQ(argList, labRepEvent, labRepSumm);
          }

             }
          }
          }
        catch (Exception ex) {
          logger.error(
              "Error while getting observations for given set of UIDS in workup",
              ex);
          throw new NEDSSSystemException(ex.toString());
        }
      }
   
  //  this.populateDescTxtFromCachedValues(labSummList);
    this.populateDescTxtFromCachedValuesDRRQ(labEventList);
    HashMap<Object, Object> returnMap = new HashMap<Object, Object>();
    //returnMap.put("labSummList", labSummList);
    returnMap.put("labEventList", labEventList);
    return returnMap;
  } //end of getObservationSummaryVOCollectionForWorkup()
  
  
  /**
   * getProviderInformation(): common method for getting the provider information from DRRQ and Patient file (Summary and Event tab).
   * @param providerDetails
   * @param labRep
   * @return
   */
  private Long getProviderInformation (ArrayList<Object>  providerDetails, LabReportSummaryVO labRep){  

	  Long providerUid = null;
	  
      if (providerDetails != null && providerDetails.size() > 0 && labRep != null) {
          Object[] orderProvider = providerDetails.toArray();

          if (orderProvider[0] != null) {
        	  labRep.setProviderLastName((String) orderProvider[0]);
              logger.debug("ProviderLastName: " + (String) orderProvider[0]);
          }
          if (orderProvider[1] != null){
        	  labRep.setProviderFirstName((String) orderProvider[1]);
          	  logger.debug("ProviderFirstName: " + (String) orderProvider[1]);
          }
          if (orderProvider[2] != null){
        	  labRep.setProviderPrefix((String) orderProvider[2]);
           	  logger.debug("ProviderPrefix: " + (String) orderProvider[2]);
          }
          if (orderProvider[3] != null){
        	  labRep.setProviderSuffix(( String)orderProvider[3]);
          	  logger.debug("ProviderSuffix: " + (String) orderProvider[3]);
          }
      	  if (orderProvider[4] != null){
      		labRep.setDegree(( String)orderProvider[4]);
         	logger.debug("ProviderDegree: " + (String) orderProvider[4]);
      	  }
     	  if (orderProvider[5] != null){
     		providerUid= (Long)orderProvider[5];
     	    labRep.setProviderUid((String)(orderProvider[5]+""));
         	logger.debug("orderProviderUid: " + (Long) orderProvider[5]);
     	  }
        }
      
      return providerUid;
      
  }
  
  /**
   * getProviderInformation(): common method for getting the provider information from DRRQ and Patient file (Summary and Event tab).
   * @param providerDetails
   * @param labRep
   * @return
   */
  
  private Long getProviderInformation (ArrayList<Object>  providerDetails, MorbReportSummaryVO morbRep){  

	  Long providerUid = null;
	  
      if (providerDetails != null && providerDetails.size() > 0 && morbRep != null) {
          Object[] orderProvider = providerDetails.toArray();

          if (orderProvider[0] != null) {
        	  morbRep.setProviderLastName((String) orderProvider[0]);
              logger.debug("ProviderLastName: " + (String) orderProvider[0]);
          }
          if (orderProvider[1] != null){
        	  morbRep.setProviderFirstName((String) orderProvider[1]);
          	  logger.debug("ProviderFirstName: " + (String) orderProvider[1]);
          }
          if (orderProvider[2] != null){
        	  morbRep.setProviderPrefix((String) orderProvider[2]);
           	  logger.debug("ProviderPrefix: " + (String) orderProvider[2]);
          }
          if (orderProvider[3] != null){
        	  morbRep.setProviderSuffix(( String)orderProvider[3]);
          	  logger.debug("ProviderSuffix: " + (String) orderProvider[3]);
          }
      	  if (orderProvider[4] != null){
      		morbRep.setDegree(( String)orderProvider[4]);
         	logger.debug("ProviderDegree: " + (String) orderProvider[4]);
      	  }
     	  if (orderProvider[5] != null){
     		providerUid= (Long)orderProvider[5];
     		morbRep.setProviderUid((String)(orderProvider[5]+""));
         	logger.debug("orderProviderUid: " + (Long) orderProvider[5]);
     	  }
        }
      
      return providerUid;
      
  }
  
  /**
   * getTestAndSusceptibilities: common method for getting the test and susceptibilities from Patient File and DRRQ
   * @param argList
   * @param labRepEvent
   * @param labRepSumm
   */
      private void getTestAndSusceptibilities(ArrayList<Object> argList, LabReportSummaryVO labRepEvent, LabReportSummaryVO labRepSumm){
    	  String query = "";
    	  
    	  
    	  
    	  ResultedTestSummaryVO testVO = new ResultedTestSummaryVO();
    	  ArrayList<Object>  testList = null;
    	  
			 if (propertyUtil.getDatabaseServerType() != null && propertyUtil.getDatabaseServerType().equalsIgnoreCase(NEDSSConstants.ORACLE_ID))
               query = WumSqlQuery.SELECT_LABRESULTED_REFLEXTEST_SUMMARY_FORWORKUP_ORA;
          else
               query = WumSqlQuery.SELECT_LABRESULTED_REFLEXTEST_SUMMARY_FORWORKUP_SQL;

				testList = (ArrayList<Object> ) preparedStmtMethod(testVO, argList,query, NEDSSConstants.SELECT);
           //afterReflex = System.currentTimeMillis();
           //totalReflex += (afterReflex - beforeReflex);
				
		    	  if (testList != null) {

		             	if(labRepEvent != null)
		             	  labRepEvent.setTheResultedSummaryTestVOCollection(testList);
		             	if(labRepSumm != null)
		             		labRepSumm.setTheResultedSummaryTestVOCollection(testList);
		             	
		                 Iterator<Object> it = testList.iterator();
		                 
		                 //timing
		                 //t3begin = System.currentTimeMillis();
		                 while (it.hasNext()) {
		                   ResultedTestSummaryVO RVO = (ResultedTestSummaryVO) it.next();
		                   
		                   
		                   
		                   	setSusceptibility(RVO, labRepEvent, labRepSumm, argList);
           
		                 }
		    	  }
         }
      
      
      private void getTestAndSusceptibilitiesDRRQ(ArrayList<Object> argList, LabReportSummaryVO labRepEvent, LabReportSummaryVO labRepSumm){
    	  String query = "";
    	  
    	  
    	  
    	  ResultedTestSummaryVO testVO = new ResultedTestSummaryVO();
    	  ArrayList<Object>  testList = null;
    	  
			 if (propertyUtil.getDatabaseServerType() != null && propertyUtil.getDatabaseServerType().equalsIgnoreCase(NEDSSConstants.ORACLE_ID))
               query = WumSqlQuery.SELECT_LABRESULTED_REFLEXTEST_SUMMARY_FORWORKUP_ORA;
          else
               query = WumSqlQuery.SELECT_LABRESULTED_REFLEXTEST_SUMMARY_FORWORKUP_SQL;

				testList = (ArrayList<Object> ) preparedStmtMethod(testVO, argList,query, NEDSSConstants.SELECT);
           //afterReflex = System.currentTimeMillis();
           //totalReflex += (afterReflex - beforeReflex);
				
		    	  if (testList != null) {

		             	if(labRepEvent != null)
		             	  labRepEvent.setTheResultedSummaryTestVOCollection(testList);
		             	if(labRepSumm != null)
		             		labRepSumm.setTheResultedSummaryTestVOCollection(testList);
		             	
		                 Iterator<Object> it = testList.iterator();
		                 
		                 //timing
		                 //t3begin = System.currentTimeMillis();
		                 String DRR_Skip_Suscept = propertyUtil.getProperty("DRR_SKIP_SUSCEPT", "F");
		                 while (it.hasNext()) {
		                   ResultedTestSummaryVO RVO = (ResultedTestSummaryVO) it.next();
		                   
			    			  if (DRR_Skip_Suscept.equals("F"))
			    				  setSusceptibilityDRRQ(RVO, labRepEvent, labRepSumm, argList);
           
		                 } //while
		    	  } //testList != null
      }
      
       

  
      private void setSusceptibility(ResultedTestSummaryVO RVO, LabReportSummaryVO labRepEvent, LabReportSummaryVO labRepSumm, ArrayList<Object> argList){
    	  
    	  
    	  ResultedTestSummaryVO susVO = new ResultedTestSummaryVO();
    	  int countResult = 0;
    	  int countSus = 0;
    	  UidSummaryVO sourceActUidVO = new UidSummaryVO();
    	  ArrayList<Object>  susList = new ArrayList<Object> ();

                 Long sourceActUid = RVO.getSourceActUid();
                 argList.clear();
                 argList.add("REFR"); // It should be constant
                 argList.add(sourceActUid);
                 
                // int num = setSusceptability2(argList);
                 
                 countResult = countResult + 1;
                 //beforeSus = System.currentTimeMillis();
                 String theQuery = "";
       		      if (propertyUtil.getDatabaseServerType() != null && propertyUtil.getDatabaseServerType().equalsIgnoreCase(NEDSSConstants.ORACLE_ID))
       		    	  theQuery = WumSqlQuery.GET_SOURCE_ACT_UID_FOR_SUSCEPTIBILITES_ORA;
       			  else
       				  theQuery = WumSqlQuery.GET_SOURCE_ACT_UID_FOR_SUSCEPTIBILITES_SQL;
                  
                 
                   
                 susList = (ArrayList<Object> ) preparedStmtMethod(sourceActUidVO,argList,
                     theQuery, NEDSSConstants.SELECT);
                 //afterSus = System.currentTimeMillis();
                 //totalSus += (afterSus - beforeSus);
                 if (susList != null) {
                   Iterator<Object> susIter = susList.iterator();
                   ArrayList<Object>  susListFinal = new ArrayList<Object> ();
                   //timing
                   //t4begin = System.currentTimeMillis();

  	           ArrayList<Object>  multipleSusceptArray = new ArrayList<Object> ();

                   while (susIter.hasNext()) {
                     UidSummaryVO uidVO = (UidSummaryVO) susIter.next();
                     Long sourceAct = uidVO.getUid();
                     argList.clear();
                     argList.add("COMP"); // It should be constant
  //System.out.println("source act -->" + sourceAct.toString());
                     argList.add(sourceAct);

                     countSus = countSus + 1;
                     //beforeReflex2 = System.currentTimeMillis();
                     if (propertyUtil.getDatabaseServerType() != null &&
                         propertyUtil.getDatabaseServerType().equalsIgnoreCase(NEDSSConstants.ORACLE_ID)) {

                     susListFinal = (ArrayList<Object> ) preparedStmtMethod(susVO, argList,
                         WumSqlQuery.SELECT_LABSUSCEPTIBILITES_REFLEXTEST_SUMMARY_FORWORKUP_ORACLE,
                         NEDSSConstants.SELECT);
                     }
                     else
                     {
                       susListFinal = (ArrayList<Object> ) preparedStmtMethod(susVO, argList,
                           WumSqlQuery.SELECT_LABSUSCEPTIBILITES_REFLEXTEST_SUMMARY_FORWORKUP_SQLSERVER,
                           NEDSSConstants.SELECT);
                     }
                     //afterReflex2 = System.currentTimeMillis();
                     //totalReflex2 += (afterReflex2 - beforeReflex2);

  					Iterator<Object> multSuscepts = susListFinal.iterator();
  					while (multSuscepts.hasNext())
  					{
  							ResultedTestSummaryVO rtsVO = (ResultedTestSummaryVO)multSuscepts.next();
  							multipleSusceptArray.add(rtsVO);
  					}
  											//multipleSuscept.add(susListFinal);
                   }

  					if (multipleSusceptArray != null) {
  						RVO.setTheSusTestSummaryVOColl(multipleSusceptArray);
  					}

  										//if (multipleSuscept != null) {
  										//	RVO.setTheSusTestSummaryVOColl(multipleSuscept);
  										//}
                   //t4end = System.currentTimeMillis();
                 }
               
               //t3end = System.currentTimeMillis();
             
    	  
    	  
      }
  
      

      private void setSusceptibilityDRRQ(ResultedTestSummaryVO RVO, LabReportSummaryVO labRepEvent, LabReportSummaryVO labRepSumm, ArrayList<Object> argList){
    	  
    	  
    	  ResultedTestSummaryVO susVO = new ResultedTestSummaryVO();
    	  Long sourceActUid = RVO.getSourceActUid();

	      ArrayList<Object>  susListFinal = new ArrayList<Object> ();
	
	
	      ArrayList<Object>  multipleSusceptArray = new ArrayList<Object> ();
	
          argList.clear();
          argList.add("COMP"); // It should be constant
          argList.add("REFR"); 
          argList.add(sourceActUid);
	                 
	
         if (propertyUtil.getDatabaseServerType() != null &&
             propertyUtil.getDatabaseServerType().equalsIgnoreCase(NEDSSConstants.ORACLE_ID)) {

         susListFinal = (ArrayList<Object> ) preparedStmtMethod(susVO, argList,
             WumSqlQuery.SELECT_LABSUSCEPTIBILITES_REFLEXTEST_SUMMARY_FORWORKUP_ORACLE_DRRQ,
             NEDSSConstants.SELECT);
         }
         else{
           susListFinal = (ArrayList<Object> ) preparedStmtMethod(susVO, argList,
               WumSqlQuery.SELECT_LABSUSCEPTIBILITES_REFLEXTEST_SUMMARY_FORWORKUP_SQLSERVER_DRRQ,
               NEDSSConstants.SELECT);
         }
	
	
				Iterator<Object> multSuscepts = susListFinal.iterator();
				while (multSuscepts.hasNext()){
						ResultedTestSummaryVO rtsVO = (ResultedTestSummaryVO)multSuscepts.next();
						multipleSusceptArray.add(rtsVO);
				}
			
   

				if (multipleSusceptArray != null) {
					RVO.setTheSusTestSummaryVOColl(multipleSusceptArray);
				}

 
      }
  
      
      
  private String prepareSQLForContainsIn(Collection<Object> uidList, String query,
                                         String whereClause) {
    Long uid = null;
    StringBuffer sf = new StringBuffer();
    sf.append("('");
    Iterator<Object> itor = uidList.iterator();
    while (itor.hasNext()) {
      uid = (Long) itor.next();
      sf.append(uid.toString());
      if (itor.hasNext()) {
        sf.append("', '");
      }
    }
    sf.append("')");

    logger.debug(query + sf.toString() + whereClause);

    return query + sf.toString() + whereClause;
  }

  public ArrayList<Object>  getUidSummaryVOArrayList(Collection<Object> uidLongCollection) {
    ArrayList<Object>  uidSummaryVOColl = new ArrayList<Object> ();
    Iterator<Object> itor = uidLongCollection.iterator();
    while (itor.hasNext()) {

      //System.out.println("\n\n\n\n\n  " + itor.next().getClass().toString());
      Long uidLong = (Long) itor.next();
      UidSummaryVO uidSummaryVO = new UidSummaryVO();
      uidSummaryVO.setUid(uidLong);
      uidSummaryVOColl.add(uidSummaryVO);

    }
    return uidSummaryVOColl;
  }

  private void populateDescTxtFromCachedValues(Collection<Object>
                                               reportSummaryVOCollection) {
    ReportSummaryInterface sumVO = null;
    LabReportSummaryVO labVO = null;
    LabReportSummaryVO labMorbVO = null;
    MorbReportSummaryVO morbVO = null;
    ResultedTestSummaryVO resVO = null;
    Iterator<Object> resItor = null;
    Iterator<Object> labMorbItor = null;
    ResultedTestSummaryVO susVO = null;
    Iterator<Object> susItor = null;
    Collection<Object> susColl = null;
    Collection<Object> labMorbColl = null;
    CachedDropDownValues cdv = new CachedDropDownValues();
    String tempStr = null;

   Iterator<Object>  itor = reportSummaryVOCollection.iterator();
    while (itor.hasNext()) {
      cdv = new CachedDropDownValues();
      sumVO = (ReportSummaryInterface) itor.next();
      if (sumVO instanceof LabReportSummaryVO) {
        labVO = (LabReportSummaryVO) sumVO;
        labVO.setType(NEDSSConstants.LAB_REPORT_DESC);
        if (labVO.getProgramArea() != null) {
          tempStr = cdv.getProgramAreaDesc(labVO.getProgramArea());
          labVO.setProgramArea(tempStr);
        }
        if (labVO.getJurisdiction() != null) {
          tempStr = cdv.getJurisdictionDesc(labVO.getJurisdiction());
          if(!tempStr.isEmpty())
        	  labVO.setJurisdiction(tempStr);
        }
        if (labVO.getStatus() != null) {
          tempStr = cdv.getDescForCode("ACT_OBJ_ST", labVO.getStatus());
          if (tempStr != null)
            labVO.setStatus(tempStr);
        }
        if (labVO.getTheResultedTestSummaryVOCollection() != null &&
            labVO.getTheResultedTestSummaryVOCollection().size() > 0) {
          resItor = labVO.getTheResultedTestSummaryVOCollection().iterator();
          while (resItor.hasNext()) {
            resVO = (ResultedTestSummaryVO) resItor.next();


						if (resVO.getCtrlCdUserDefined1() != null)
            {
							if (resVO.getCtrlCdUserDefined1() != null && resVO.getCtrlCdUserDefined1().equals("N"))
							{
								if (resVO.getCodedResultValue() != null &&
										!resVO.getCodedResultValue().equals("")) {
									tempStr = cdv.getCodedResultDesc(resVO.getCodedResultValue());
									resVO.setCodedResultValue(tempStr);
								}
							}
							else if (resVO.getCtrlCdUserDefined1() == null || resVO.getCtrlCdUserDefined1().equals("Y"))
							{
								if (resVO.getOrganismName() != null && resVO.getOrganismCodeSystemCd()!=null ) {
									if (resVO.getOrganismCodeSystemCd() != null && resVO.getOrganismCodeSystemCd().equals("SNM")) {
										tempStr = cdv.getOrganismListDescSNM(resVO.getCodedResultValue());
										resVO.setOrganismName(tempStr);
									}
									else {
										tempStr = cdv.getOrganismListDesc(resVO.getCodedResultValue());
										resVO.setOrganismName(tempStr);
									}
								}
							}
						}else if (resVO.getCtrlCdUserDefined1() == null ){
							if (resVO.getOrganismName() != null){
//System.out.println("got in with an org for elr");
								if (resVO.getOrganismCodeSystemCd()!=null ) {
									if (resVO.getOrganismCodeSystemCd().equals("SNM")) {
										tempStr = cdv.getOrganismListDescSNM(resVO.getCodedResultValue());
										if (tempStr == null)
										{
											resVO.setOrganismName(resVO.getOrganismName());
										}
										else
										{
											resVO.setOrganismName(tempStr);
										}
									}
									else
									{
										tempStr = cdv.getOrganismListDesc(resVO.getCodedResultValue());
										if (tempStr == null)
										{
											resVO.setOrganismName(resVO.getOrganismName());
										}
										else
										{
											resVO.setOrganismName(tempStr);
										}

									}
								}
								else
								{
									resVO.setOrganismName(resVO.getOrganismName());
								}
							}
							else
							{
								tempStr = cdv.getOrganismListDesc(resVO.getCodedResultValue());
								if (tempStr == null)
								{
									resVO.setOrganismName(resVO.getCodedResultValue());
								}
								else
								{
									resVO.setOrganismName(tempStr);
								}
							}
						}

            if ( (resVO.getCdSystemCd() != null) &&
                (! (resVO.getCdSystemCd().equals("")))) {
              if (resVO.getCdSystemCd().equals("LN")) {
                if (resVO.getResultedTestCd() != null &&
                    !resVO.getResultedTestCd().equals("")) {
                  tempStr = cdv.getResultedTestDesc(resVO.getResultedTestCd());
                  // System.out.println("\n The temStr for resVO" + tempStr);
                  if (tempStr != null && !tempStr.equals(""))
                    resVO.setResultedTest(tempStr);
                }
              }
              else if (!resVO.getCdSystemCd().equals("LN")) {
                if (resVO.getResultedTestCd() != null &&
                    !resVO.getResultedTestCd().equals("")) {
                  tempStr = cdv.getResultedTestDescLab(resVO.getCdSystemCd(),
                      resVO.getResultedTestCd());
                  if (tempStr != null && !tempStr.equals(""))
                    resVO.setResultedTest(tempStr);

                }
              }
            }
         // Added this for ER16368
            if ((resVO.getResultedTestStatusCd() != null) &&(! (resVO.getResultedTestStatusCd().equals("")))){
            	tempStr = cdv.getDescForCode("ACT_OBJ_ST",resVO.getResultedTestStatusCd());
            	 if (tempStr != null && !tempStr.equals(""))
                   	resVO.setResultedTestStatus(tempStr);
            }
         // End  ER16368
            susColl = resVO.getTheSusTestSummaryVOColl();
            if (susColl != null && susColl.size() > 0) {
              susItor = susColl.iterator();
              while (susItor.hasNext()) {
                susVO = (ResultedTestSummaryVO) susItor.next();

                if (susVO.getCodedResultValue() != null &&
                    !susVO.getCodedResultValue().equals("")) {
                  tempStr = cdv.getCodedResultDesc(susVO.getCodedResultValue());
                  if (tempStr != null && !tempStr.equals(""))
                	  susVO.setCodedResultValue(tempStr);
                }
                if (susVO.getCdSystemCd() != null &&
                    !susVO.getCdSystemCd().equals("")) {
                  if (susVO.getCdSystemCd().equals("LN")) {
                    if (susVO.getResultedTestCd() != null &&
                        !susVO.getResultedTestCd().equals("")) {
                      tempStr = cdv.getResultedTestDesc(susVO.getResultedTestCd());

                      if (tempStr != null && !tempStr.equals("")) {
                        susVO.setResultedTest(tempStr);
                      }
                    }
                  }
                  else if (!susVO.getCdSystemCd().equals("LN")) {
                    if (susVO.getResultedTestCd() != null &&
                        !susVO.getResultedTestCd().equals("")) {
                      tempStr = cdv.getResultedTestDescLab(susVO.getCdSystemCd(),
                          susVO.getResultedTestCd());
                      if (tempStr != null && !tempStr.equals("")) {
                        susVO.setResultedTest(tempStr);
                      }
                    }
                  }
                }

              } // inner while
            }
          } //outer while
        } //if
      }
      else if (sumVO instanceof MorbReportSummaryVO) {
        morbVO = (MorbReportSummaryVO) sumVO;
        if (morbVO.getCondition() != null) {
          tempStr = cdv.getConditionDesc(morbVO.getCondition());
          morbVO.setConditionDescTxt(tempStr);
        }
        if (morbVO.getProgramArea() != null) {
          tempStr = cdv.getProgramAreaDesc(morbVO.getProgramArea());
          morbVO.setProgramArea(tempStr);
        }
        if (morbVO.getJurisdiction() != null) {
          tempStr = cdv.getJurisdictionDesc(morbVO.getJurisdiction());
          morbVO.setJurisdiction(tempStr);
        }
        morbVO.setType(NEDSSConstants.MORB_REPORT_DESC);
        if (morbVO.getReportType() != null) {
          tempStr = cdv.getDescForCode("MORB_RPT_TYPE", morbVO.getReportType());
          morbVO.setReportTypeDescTxt(tempStr);
        }
        labMorbColl = morbVO.getTheLabReportSummaryVOColl();
        if (labMorbColl != null) {
          //morb has collection of labsumvo
          labMorbItor = labMorbColl.iterator();
          while (labMorbItor.hasNext()) {
            labMorbVO = (LabReportSummaryVO) labMorbItor.next();
            if (labMorbVO.getTheResultedTestSummaryVOCollection() != null) {

              //lab has collection of ResultedTestSummaryVO
              resItor = labMorbVO.getTheResultedTestSummaryVOCollection().
                  iterator();
              while (resItor.hasNext()) {
                resVO = (ResultedTestSummaryVO) resItor.next();
                if (resVO.getCodedResultValue() != null &&
                    !resVO.getCodedResultValue().equals("")) {
                  //tempStr = cdv.getCodedResultDesc(resVO.getCodedResultValue());
                  //resVO.setCodedResultValue(tempStr);
                }
                if (resVO.getResultedTest() != null &&
                    !resVO.getResultedTest().equals("")) {
                  //tempStr = cdv.getResultedTestDesc(resVO.getResultedTestCd());
                  //resVO.setResultedTest(tempStr);

                }

                susColl = resVO.getTheSusTestSummaryVOColl();
                if (susColl != null && susColl.size() > 0) {
                  //ResultedTestSummaryVO has collection of SusTestSummaryVO
                  susItor = susColl.iterator();
                  while (susItor.hasNext()) {
                    susVO = (ResultedTestSummaryVO) susItor.next();
                    if (susVO.getCodedResultValue() != null &&
                        !susVO.getCodedResultValue().equals("")) {
                      tempStr = cdv.getCodedResultDesc(susVO.
                          getCodedResultValue());
                      susVO.setCodedResultValue(tempStr);
                    }

                  } // inner while
                }
              } //outer while
            } //if lab
          } // while labmorb
        } //if labmorbcoll

      }
    }
  }

  private void populateDescTxtFromCachedValuesDRRQ(Collection<Object>
                                               reportSummaryVOCollection) {
    ReportSummaryInterface sumVO = null;
    LabReportSummaryVO labVO = null;
    ResultedTestSummaryVO resVO = null;
    Iterator<Object> resItor = null;
    CachedDropDownValues cdv = new CachedDropDownValues();
    String tempStr = null;

   Iterator<Object>  itor = reportSummaryVOCollection.iterator();
    while (itor.hasNext()) {
      cdv = new CachedDropDownValues();
      sumVO = (ReportSummaryInterface) itor.next();
      if (sumVO instanceof LabReportSummaryVO) {
        labVO = (LabReportSummaryVO) sumVO;
        labVO.setType(NEDSSConstants.LAB_REPORT_DESC);

        if (labVO.getTheResultedTestSummaryVOCollection() != null &&
            labVO.getTheResultedTestSummaryVOCollection().size() > 0) {
          resItor = labVO.getTheResultedTestSummaryVOCollection().iterator();
          while (resItor.hasNext()) {
            resVO = (ResultedTestSummaryVO) resItor.next();

         // Added this for ER16368
            if ((resVO.getResultedTestStatusCd() != null) &&(! (resVO.getResultedTestStatusCd().equals("")))){
            	tempStr = cdv.getDescForCode("ACT_OBJ_ST",resVO.getResultedTestStatusCd());
            	 if (tempStr != null && !tempStr.equals(""))
                   	resVO.setResultedTestStatus(tempStr);
            }
         // End  ER16368
            
            if (resVO.getCtrlCdUserDefined1() != null && resVO.getCtrlCdUserDefined1().equals("N"))
			{
				if (resVO.getCodedResultValue() != null &&
						!resVO.getCodedResultValue().equals("")) {
					tempStr = cdv.getCodedResultDesc(resVO.getCodedResultValue());
					resVO.setCodedResultValue(tempStr);
				}
			}
         
          } //outer while
        } //if
      }
    }
  }

  
  @SuppressWarnings("unchecked")
public String getLaboratorySystemDescTxt(String laboratory_id) {
    String codeSql;
    String codeValue = null;
    //  logic to check if code has seperate table
    if (propertyUtil.getDatabaseServerType() != null &&
        propertyUtil.
        getDatabaseServerType().equalsIgnoreCase(NEDSSConstants.ORACLE_ID)) {
      codeSql = "select Laboratory_system_desc_txt \"str1\" from  " +
          NEDSSConstants.SYSTEM_REFERENCE_TABLE +
          ".lab_coding_system where laboratory_id = ?";
    }
    else {
      codeSql = "select Laboratory_system_desc_txt str1 from  " +
          NEDSSConstants.SYSTEM_REFERENCE_TABLE +
          "..lab_coding_system where laboratory_id = ?";
    }
    logger.debug("codeSQL is" + codeSql);
    if (laboratory_id == null) {
      codeSql = null; //ent table
      logger.error(
          "ObservationProcessor:getLaboratorySystemDescTxt: Input code == null");
    }
    else {
      // else, code is in common table

      ArrayList<Object>  arrayList = new ArrayList<Object> ();
      GenericSummaryVO summaryResult = new GenericSummaryVO();
      arrayList.add(laboratory_id);
      ArrayList<Object>  list = (ArrayList<Object> ) preparedStmtMethod(summaryResult,
          arrayList, codeSql, NEDSSConstants.SELECT);
      summaryResult = (GenericSummaryVO) list.get(0);
      codeValue = summaryResult.getStr1();

    }
    return codeValue;

  }

   public HashMap<Object, Object> getProgramArea(String reportingLabCLIA,
                                 Collection<Object> observationVOCollection,
                                 String electronicInd)
   {

     HashMap<Object, Object> returnMap = new HashMap<Object, Object>();
     if (reportingLabCLIA == null)
     {
       returnMap.put(NEDSSConstants.ERROR,
                     NEDSSConstants.REPORTING_LAB_CLIA_NULL);
       return returnMap;
     }

     Iterator<Object> obsIt = observationVOCollection.iterator();
     Hashtable<Object, Object> paHTBL = new Hashtable<Object, Object>();

     //iterator through each resultTest
     while (obsIt.hasNext())
     {
       ObservationVO obsVO = (ObservationVO) obsIt.next();
       ObservationDT obsDt = obsVO.getTheObservationDT();

       String obsDomainCdSt1 = obsDt.getObsDomainCdSt1();
       String obsDTCode = obsDt.getCd();
       boolean found = false;
       
       //Set exclude flag to false - if any of the components - Lab Result (SNOMED or Local) or Lab Test (LOINC or
       //Local) is excluded, this flag will be set so as not to fail the derivation for this resulted test.
       programAreaDerivationExcludeFlag = false;

       // make sure you are dealing with a resulted test here.
       if ( (obsDomainCdSt1 != null) &&
           obsDomainCdSt1.equals(ELRConstants.ELR_OBSERVATION_RESULT) &&
           (obsDTCode != null) &&
           (!obsDTCode.equals(NEDSSConstants.ACT114_TYP_CD)))
       {
         // Retrieve PAs using Lab Result --> SNOMED code mapping
         // If ELR, use actual CLIA - if manual use "DEFAULT" as CLIA
         String progAreaCd;
         if ( electronicInd.equals(NEDSSConstants.ELECTRONIC_IND_ELR) )
           progAreaCd = getPAFromSNOMEDCodes(reportingLabCLIA, obsVO.getTheObsValueCodedDTCollection());
         else
           progAreaCd = getPAFromSNOMEDCodes(NEDSSConstants.DEFAULT, obsVO.getTheObsValueCodedDTCollection());


         // If PA returned, check to see if it is the same one as before.
         if (progAreaCd != null)
         {
           found = true;
           paHTBL.put(progAreaCd.trim(), progAreaCd.trim());
           if (paHTBL.size() != 1)
           {
             break;
           }

         }

         // Retrieve PAs using Resulted Test --> LOINC mapping
         if (!found)
         {
           progAreaCd = getPAFromLOINCCode(reportingLabCLIA, obsVO);
           // If PA returned, check to see if it is the same one as before.
           if (progAreaCd != null)
           {
             found = true;
             paHTBL.put(progAreaCd.trim(), progAreaCd.trim());
             if (paHTBL.size() != 1)
             {
               break;
             }
           }
         }

         // Retrieve PAs using Local Result Code to PA mapping
         if (!found)
         {
           progAreaCd = getPAFromLocalResultCode(reportingLabCLIA,
                                                 obsVO.
                                                 getTheObsValueCodedDTCollection());
           // If PA returned, check to see if it is the same one as before.
           if (progAreaCd != null)
           {
             found = true;
             //System.out.println("Found!" + progAreaCd);
             paHTBL.put(progAreaCd.trim(), progAreaCd.trim());
             if (paHTBL.size() != 1)
             {
               break;
             }
           }
         }

         // Retrieve PAs using Local Result Code to PA mapping
         if (!found)
         {
           progAreaCd = getPAFromLocalTestCode(reportingLabCLIA, obsVO);
           // If PA returned, check to see if it is the same one as before.
           if (progAreaCd != null)
           {
             found = true;
             paHTBL.put(progAreaCd.trim(), progAreaCd.trim());
             if (paHTBL.size() != 1)
             {
               break;
             }
           }
         }
         
         //If we haven't found a PA and the no components were excluded based on the exclude flag,
         //clear the PA hashtable which will fail the derivation
         if (!found && !programAreaDerivationExcludeFlag) 
         {
           paHTBL.clear();
           break;
         }
       }
     } //end of while

     if(paHTBL.size() == 0)
     {
       returnMap.put(NEDSSConstants.ERROR, ELRConstants.PROGRAM_ASSIGN_2);
     }
     else if (paHTBL.size() == 1)
     {
       returnMap.put(ELRConstants.PROGRAM_AREA_HASHMAP_KEY, paHTBL.keys().nextElement().toString());
     }
     else
     {
       returnMap.put(NEDSSConstants.ERROR, ELRConstants.PROGRAM_ASSIGN_1);
     }
     return returnMap;
   } //end of getProgramArea





  /**
   * Returns a collection of Snomed codes to be used to resolve the program area code.
   * If more than one type of snomed is resolved, return null.
   * @param ObservationVO resultTestVO
   * @param reportingLabCLIA : String
   * @return Vector
   */
   // AK - 7/25/04
   private String getPAFromSNOMEDCodes(String reportingLabCLIA,
		   Collection<Object> obsValueCodedDtColl) {

	   Vector<Object> snomedVector = new Vector<Object>();
	   SRTMapDAOImpl dao = new SRTMapDAOImpl();

	   if (reportingLabCLIA == null)
		   return null;

	   if (obsValueCodedDtColl != null) {
		   Iterator<Object> codedDtIt = obsValueCodedDtColl.iterator();
		   while (codedDtIt.hasNext()) {

			   ObsValueCodedDT codedDt = (ObsValueCodedDT) codedDtIt.next();
			   String codeSystemCd = codedDt.getCodeSystemCd();

			   if (codeSystemCd == null || codeSystemCd.trim().equals(""))
				   continue;

			   String obsCode = codedDt.getCode();
			   if (obsCode == null || obsCode.trim().equals(""))
				   continue;

			   /*
			    * 	Check if ObsValueCodedDT.codeSystemCd='L' and CLIA for Reporting Lab is available,
			    *  find Snomed For ObsValueCodedDT.code(Local Result to Snomed lookup)
			    */


			   if (!codeSystemCd.equals(ELRConstants.ELR_SNOMED_CD)){
				   // If local code and it is not excluded from PA Derivation, attempt to retrieve corresponding SNOMED code
				   if (!dao.removePADerivationExcludedLabResultCodes(obsCode, reportingLabCLIA)) {
					   ArrayList<Object>  snomedList = (ArrayList<Object> ) dao.getSnomed(codedDt.getCode(),
							   // ELRConstants.TYPE = "LR"
							   ELRConstants.TYPE,
							   reportingLabCLIA);
					   if ( ( (Integer) snomedList.get(1)).intValue() == 1)
						   snomedVector.addElement(snomedList.get(0));
				   } else {
					   //If so, set exclude flag so we won't fail this resulted test if no PA is derived for it
					   programAreaDerivationExcludeFlag = true;
				   }
			   }
		   
		   /*  If already coded using SNOMED code, just add it to the return array.
		    *  check if ObsValueCodedDT.codeSystemCd="SNM", use ObsValueCodedDT.code for Snomed
     	    *  Need to check SNOMED codes for Program Area Derivation Exclusion flag - don't include codes with this set
		    */
		   else if (codeSystemCd.equals("SNM")) {
			   // If snomed code and it is not excluded from PA Derivation, add it to the SNOMED Vector
			   if (!dao.removePADerivationExcludedSnomedCodes(obsCode)) 
				   snomedVector.addElement(obsCode);
			   else
				   //Otherwise don't add it and set the exclude flag so we won't fail this resulted test if no PA is derived for it
				   programAreaDerivationExcludeFlag = true;
		   }
	   } //end of while


	   // Now that we have resolved all the SNOMED codes, try to derive the PA
	   if (snomedVector != null &&
			   snomedVector.size() == obsValueCodedDtColl.size())
		   return getProgAreaCd(snomedVector, reportingLabCLIA,
				   NEXT, ELRConstants.ELR_SNOMED_CD);

   } //end of if

   return null;
} //end of getPAFromSNOMED(...)

  /**
   * Attempts to resolve a ProgramAreaCd based on Loinc.
   * @param reportingLabCLIA : String
   * @param obsDt : ObservationDT
   * @return loincVector : Vector
   * @throws NEDSSDAOSysException
   */
  // AK - 7/25/04
  private String getPAFromLOINCCode(String reportingLabCLIA,
                                    ObservationVO resultTestVO) throws
      NEDSSDAOSysException {

    ObservationDT obsDt = resultTestVO.getTheObservationDT();
    if (obsDt == null || reportingLabCLIA == null)
      return null;

    String cdSystemCd = obsDt.getCdSystemCd();
    if (cdSystemCd == null || cdSystemCd.trim().equals(""))
      return null;

    String obsCode = obsDt.getCd();
    if (obsCode == null || obsCode.trim().equals(""))
      return null;

    SRTMapDAOImpl dao = new SRTMapDAOImpl();
    Vector<Object> loincVector = new Vector<Object>();

    if(cdSystemCd.equals(ELRConstants.ELR_OBSERVATION_LOINC))
    {
      //Check if this loinc code should be excluded from Program Area derivation
      //If so, set exclude flag so we won't fail this resulted test if no PA is derived for it
      if (dao.removePADerivationExcludedLoincCodes(obsCode)){
      	programAreaDerivationExcludeFlag = true;
        return null;
      }
      
      loincVector.addElement(obsCode);
    }
    else
    {
        //Check if this local test code should be excluded from Program Area derivation
        //If so, set exclude flag so we won't fail this resulted test if no PA is derived for it
        if (dao.removePADerivationExcludedLabTestCodes(obsCode, reportingLabCLIA)) {
        	programAreaDerivationExcludeFlag = true;
            return null;
        }
    	
    	ArrayList<Object>  loincList = (ArrayList<Object> ) dao.getMsgProgAreaLoincSnomed(obsCode,
         "LT",
         reportingLabCLIA);
     if ( ( (Integer) loincList.get(1)).intValue() == 1)
       loincVector.addElement(loincList.get(0));
    }

   
/*
    if (!cdSystemCd.equals(ELRConstants.ELR_OBSERVATION_LOINC)) {
      ArrayList<Object> loincList = (ArrayList<Object> ) dao.getMsgProgAreaLoincSnomed(obsCode,
          "LT",
          reportingLabCLIA);
      if ( ( (Integer) loincList.get(1)).intValue() == 1)
        loincVector.addElement(loincList.get(0));
    }
    else if (cdSystemCd.equals(ELRConstants.ELR_OBSERVATION_LOINC))
      loincVector.addElement(obsCode);
*/
      // Now that we have resolved all the LOINC codes, try to derive the PA
    return getProgAreaCd(loincVector, reportingLabCLIA, NEXT,
                         ELRConstants.ELR_OBSERVATION_LOINC);

  } //end of getLoincColl(...)

  /**
   * Attempts to resolve a program area cd based on Local Result code.
   * @param codedColl : Collection
   * @param reportingLabCLIA : String
   * @param ObservationVO : ObservationVO
   * @return progrAreaCd : String
   */
  // AK - 7/25/04
  public String getPAFromLocalResultCode(String reportingLabCLIA,
		  Collection<Object> obsValueCodedDtColl) {
	  String lastProgAreaCd = null;
	  String progAreaCd = null;
	  SRTMapDAOImpl dao = new SRTMapDAOImpl();

	  if (obsValueCodedDtColl == null || reportingLabCLIA == null)
		  return null;

	  Vector<Object> codeVector = new Vector<Object>();
	  Iterator<Object> codedDtIt = obsValueCodedDtColl.iterator();

	  while (codedDtIt.hasNext()) {
		  ObsValueCodedDT obsValueCodedDT = (ObsValueCodedDT) codedDtIt.next();
		  String code = obsValueCodedDT.getCode();
		  String codeSystemCd = obsValueCodedDT.getCodeSystemCd();
		  if (code != null &&  !codeSystemCd.equals(ELRConstants.ELR_SNOMED_CD)) {
		      //Check if this local result code should be excluded from Program Area derivation
			  if (!dao.removePADerivationExcludedLabResultCodes(code, reportingLabCLIA)){
				  codeVector.addElement(code);
			  } else {
			      //If so, set exclude flag so we won't fail this resulted test if no PA is derived for it
				  programAreaDerivationExcludeFlag = true;

			  }

		  }

		  String codeSql = null;
		  String dbServerType = propertyUtil.getDatabaseServerType();

		  // Use default condition code to PA mapping
		  if (dbServerType != null &&
				  dbServerType.equalsIgnoreCase(NEDSSConstants.ORACLE_ID))
			  codeSql = ELRConstants.
			  SELECT_LOCAL_RESULT_DEFAULT_CONDITION_PROGRAM_AREA_CD_ORACLE;
		  else
			  codeSql = ELRConstants.
			  SELECT_LOCAL_RESULT_DEFAULT_CONDITION_PROGRAM_AREA_CD_SQL;

		  progAreaCd = getProgAreaCdLocalDefault(codeVector, reportingLabCLIA,
				  NEXT, codeSql);

		  if (progAreaCd == null) {

			  // Use default PA code
			  if (dbServerType != null &&
					  dbServerType.equalsIgnoreCase(NEDSSConstants.ORACLE_ID))
				  codeSql = ELRConstants.
				  SELECT_LOCAL_RESULT_DEFAULT_PROGRAM_AREA_CD_ORACLE;
			  else
				  codeSql = ELRConstants.
				  SELECT_LOCAL_RESULT_DEFAULT_PROGRAM_AREA_CD_SQL;

			  progAreaCd = getProgAreaCdLocalDefault(codeVector, reportingLabCLIA,
					  NEXT, codeSql);
		  }

		  if (lastProgAreaCd == null)
			  lastProgAreaCd = progAreaCd;
		  else
			  if (!lastProgAreaCd.equals(progAreaCd))

				  // Different PAs returned - error.
				  return null;

	  }
	  return lastProgAreaCd;

  } //end of method

  /**
   * Attempts to resolve a program area cd based on LocalTestDefault cd.
   * @param reportingLabCLIA : String
   * @param obsDt : ObservationVO
   * @return progAreaCd : String
   */
  // AK - 7/25/04
  public String getPAFromLocalTestCode(String reportingLabCLIA,
                                       ObservationVO resultTestVO) {

    ObservationDT obsDt = resultTestVO.getTheObservationDT();
    
    //If this test has a LOINC, we should return and not treat it as a local test 
    if (obsDt.equals(ELRConstants.ELR_OBSERVATION_LOINC))
    	return null;
    
    String code = getLocalTestCode(obsDt);
    SRTMapDAOImpl dao = new SRTMapDAOImpl();

    if (reportingLabCLIA == null || code == null || code.trim().equals(""))
      return null;

    //Check if this code should be excluded from Program Area derivation
    if (dao.removePADerivationExcludedLabTestCodes(code, reportingLabCLIA))
      return null;
        
    String progAreaCd = null;

    Vector<Object> codeVector = new Vector<Object>();
    codeVector.addElement(code);

    String codeSql = null;
    String dbServerType = propertyUtil.getDatabaseServerType();

    // Use default condition code to PA mapping
    if (dbServerType != null &&
        dbServerType.equalsIgnoreCase(NEDSSConstants.ORACLE_ID))
      codeSql = ELRConstants.
          SELECT_LOCAL_TEST_DEFAULT_CONDITION_PROGRAM_AREA_CD_ORACLE;
    else
      codeSql = ELRConstants.
          SELECT_LOCAL_TEST_DEFAULT_CONDITION_PROGRAM_AREA_CD_SQL;

    progAreaCd = getProgAreaCdLocalDefault(codeVector, reportingLabCLIA,
                                           NEXT, codeSql);

    if (progAreaCd == null) {

      if (dbServerType != null &&
          dbServerType.equalsIgnoreCase(NEDSSConstants.ORACLE_ID))
        codeSql = ELRConstants.SELECT_LOCAL_TEST_DEFAULT_PROGRAM_AREA_CD_ORACLE;
      else
        codeSql = ELRConstants.SELECT_LOCAL_TEST_DEFAULT_PROGRAM_AREA_CD_SQL;

      progAreaCd = getProgAreaCdLocalDefault(codeVector, reportingLabCLIA,
                                             NEXT, codeSql);
    }
    return progAreaCd;

  } //end of method

  public ObservationVO labLoincSnomedLookup(ObservationVO obsVO, String labClia) {
  	/*
  	 * Ajith 7/15/05
  	 * When a reporting facility without a clia number is selected by the user, the droplist
  	 * of lab tests presented comes from the "DEFAULT" group. Furthermore, even "DEFAULT" lab tests
  	 * and results can be mapped to LOINC and SNOMEDs( respectively ) just as the lab specific tests
  	 * and results using the labtest_loinc and labresult_snomed DWYER tables.
  	 * Because of these reasons, we must use the clia number "DEFAULT" when no clia is available
  	 * for the selected facility. Otherwise alternate codes coming out of LOINC and SNOMED associations
  	 * will not be set on the OT and RT.
  	 *
  	 */
    if (labClia == null && obsVO.getTheObservationDT().getCdSystemCd().equals(NEDSSConstants.DEFAULT))
    	labClia = NEDSSConstants.DEFAULT ;

    if (labClia == null )
      return obsVO;


//System.out.println("Before obsVO.getTheObservationDT().getAltCdSystemCd(): " + obsVO.getTheObservationDT().getAltCdSystemCd());
//System.out.println("Before obsVO.getTheObservationDT().getAltCd(): " + obsVO.getTheObservationDT().getAltCd());
//System.out.println("Before obsVO.getTheObservationDT().getCdDerivedInd(): " + obsVO.getTheObservationDT().getCdDerivedInd());
    //Do lookup for the observation dt
    doLoincCdLookupForObservationDT(obsVO.getTheObservationDT(), labClia);

    //Do lookup for all the obsValueCodedDT
    doSnomedCdLookupForObsValueCodedDTs(obsVO.getTheObsValueCodedDTCollection(),
                                        labClia);

    // System.out.println("/n After obsVO.getTheObservationDT().getAltCdSystemCd(): " + obsVO.getTheObservationDT().getAltCdSystemCd());
    //System.out.println("After obsVO.getTheObservationDT().getAltCd(): " + obsVO.getTheObservationDT().getAltCd());
    // System.out.println("After obsVO.getTheObservationDT().getCdDerivedInd(): " + obsVO.getTheObservationDT().getCdDerivedInd());

    return obsVO;
  }

  private void doLoincCdLookupForObservationDT(ObservationDT obsDT,
                                               String labClia) {
    String cdSystemCd = obsDT.getCdSystemCd();
    String altCdSystemCd = obsDT.getAltCdSystemCd();

    if (cdSystemCd != null && !cdSystemCd.equals("LN") && altCdSystemCd == null) {
      //System.out.println("/n labClia: " + labClia);
      // System.out.println("obsDT.getCd(): " + obsDT.getCd());
      List<Object> loincCdList = new SRTMapDAOImpl().findLoincCds(labClia, obsDT.getCd());
      //If only one loinc cd found, use it, otherwise discard
      if (loincCdList != null && loincCdList.size() == 1) {
        obsDT.setAltCdSystemCd("LN");
        obsDT.setAltCd( (String) loincCdList.get(0));
        obsDT.setCdDerivedInd("Y");
      }
    }
  }

  private void doSnomedCdLookupForObsValueCodedDTs(Collection<Object> obsValueCodedDTs,
      String labClia) {
    if (obsValueCodedDTs == null || obsValueCodedDTs.isEmpty())
      return;

    for (Iterator<Object> it = obsValueCodedDTs.iterator(); it.hasNext(); ) {
      ObsValueCodedDT obsValueCodedDT = (ObsValueCodedDT) it.next();
      if (obsValueCodedDT == null)
        continue;
      // System.out.println("/n Before obsValueCodedDT.getAltCdSystemCd(): " + obsValueCodedDT.getAltCdSystemCd());
      // System.out.println("Before obsValueCodedDT.getAltCd(): " + obsValueCodedDT.getAltCd());
      // System.out.println("Before obsValueCodedDT.getCodeDerivedInd()): " + obsValueCodedDT.getCodeDerivedInd());

      String cdSystemCd = obsValueCodedDT.getCodeSystemCd();
      String altCdSystemCd = obsValueCodedDT.getAltCdSystemCd();

      if (cdSystemCd != null && !cdSystemCd.equals("SNM") && altCdSystemCd == null) {
        // System.out.println("/n labClia: " + labClia);
        //  System.out.println("obsValueCodedDT.getCode(): " + obsValueCodedDT.getCode());
        List<Object> snomedCdList = new SRTMapDAOImpl().findSnomedCds(labClia,
            obsValueCodedDT.getCode());
        //If only one snomed cd found, use it, otherwise discard
        if (snomedCdList != null && snomedCdList.size() == 1) {
          obsValueCodedDT.setAltCdSystemCd("SNM");
          obsValueCodedDT.setAltCd( (String) snomedCdList.get(0));
          obsValueCodedDT.setCodeDerivedInd("Y");
        }
      }
      // System.out.println("After obsValueCodedDT.getAltCdSystemCd(): " + obsValueCodedDT.getAltCdSystemCd());
      // System.out.println("After obsValueCodedDT.getAltCd(): " + obsValueCodedDT.getAltCd());
      //System.out.println("After obsValueCodedDT.getCodeDerivedInd()): " + obsValueCodedDT.getCodeDerivedInd());

    }
  }

  @SuppressWarnings("unchecked")
public TreeMap<Object, Object> getCodeSystemDescription(String laboratory_id)
  {
    String codeSql = null;
    TreeMap<Object, Object> codeMap = null;
    String dbServerType = propertyUtil.getDatabaseServerType();
    // Use default condition code to PA mapping
    if (dbServerType != null &&
        dbServerType.equalsIgnoreCase(NEDSSConstants.ORACLE_ID))
      codeSql = "select a.laboratory_id \"str1\", a.laboratory_system_desc_txt \"str2\" from "+
          NEDSSConstants.SYSTEM_REFERENCE_TABLE +".lab_coding_system a where laboratory_id = '" + laboratory_id + "'";

    else
      codeSql = "select a.laboratory_id str1, a.laboratory_system_desc_txt str2 from "+
          NEDSSConstants.SYSTEM_REFERENCE_TABLE +"..lab_coding_system a where laboratory_id = '" + laboratory_id + "'";

    GenericSummaryVO summaryVO = new GenericSummaryVO();
    ArrayList<Object>  list = (ArrayList<Object> ) preparedStmtMethod(summaryVO, null,
        codeSql, NEDSSConstants.SELECT);
    codeMap = new TreeMap<Object, Object>();
    for (int count = 0; count < list.size(); count++) {
      GenericSummaryVO genSumm = (GenericSummaryVO) list.get(count);
      codeMap.put(NEDSSConstants.KEY_CODINGSYSTEMCD, genSumm.getStr1());
      codeMap.put(NEDSSConstants.KEY_CODESYSTEMDESCTXT , genSumm.getStr2());
    }
    return codeMap;
  }
/*
 *   getDerivedConditionList - get the list of conditions for the set of resulted tests
 *   @param reportingLabId (clia)
 *   @param observationVOCollection - the Result Tests
 *   @param electronicInd
 *   @return arrayList of condition codes
 */
public ArrayList<String> getDerivedConditionList(String reportingLabCLIA,
			Collection<Object> observationVOCollection, String electronicInd) {
		int noConditionFoundForResultedTestCount = 0;
		ArrayList<String> returnList =  new ArrayList<String> ();

		Iterator<Object> obsIt = observationVOCollection.iterator();
		// iterator through each resultTest
		while (obsIt.hasNext()) {
			ArrayList<String> resultedTestConditionList =  new ArrayList<String> ();
			ObservationVO obsVO = (ObservationVO) obsIt.next();
			ObservationDT obsDt = obsVO.getTheObservationDT();

			String obsDomainCdSt1 = obsDt.getObsDomainCdSt1();
			String obsDTCode = obsDt.getCd();

			// make sure you are dealing with a resulted test here.
			if ((obsDomainCdSt1 != null)
					&& obsDomainCdSt1
							.equals(ELRConstants.ELR_OBSERVATION_RESULT)
					&& (obsDTCode != null)
					&& (!obsDTCode.equals(NEDSSConstants.ACT114_TYP_CD))) {
				
				// Retrieve Condition List using SNM Lab Result --> SNOMED code mapping
				// If ELR, use actual CLIA - if manual use "DEFAULT" as CLIA
				if (electronicInd.equals(NEDSSConstants.ELECTRONIC_IND_ELR))
					resultedTestConditionList = getConditionsFromSNOMEDCodes(reportingLabCLIA,
							obsVO.getTheObsValueCodedDTCollection());
				else
					resultedTestConditionList = getConditionsFromSNOMEDCodes(NEDSSConstants.DEFAULT,
							obsVO.getTheObsValueCodedDTCollection());

				// if no conditions found - try LN to retrieve Condition using Resulted Test --> LOINC mapping
				if (resultedTestConditionList.isEmpty()) {
					String loincCondition = getConditionForLOINCCode(reportingLabCLIA, obsVO);
					if (loincCondition!= null && !loincCondition.isEmpty())
						resultedTestConditionList.add(loincCondition);
				}

				// none - try LR to retrieve default Condition using Local Result Code to condition mapping
				if (resultedTestConditionList.isEmpty()) {
					String localResultDefaultConditionCd = getConditionCodeForLocalResultCode(reportingLabCLIA,
							obsVO.getTheObsValueCodedDTCollection());
					if (localResultDefaultConditionCd != null && !localResultDefaultConditionCd.isEmpty())
						resultedTestConditionList.add(localResultDefaultConditionCd);
				}
				// none - try LT to retrieve default Condition using Local Test Code to condition mapping
				if (resultedTestConditionList.isEmpty()) {
					String localTestDefaultConditionCd = getConditionCodeForLocalTestCode(reportingLabCLIA,
							obsVO);
					if (localTestDefaultConditionCd != null && !localTestDefaultConditionCd.isEmpty())
						resultedTestConditionList.add(localTestDefaultConditionCd);
				}
				// none - see if default condition code exists for the resulted lab test
				if (resultedTestConditionList.isEmpty()) {
					String defaultLabTestConditionCd = getDefaultConditionForLabTestCode(obsDTCode, reportingLabCLIA);
					if (defaultLabTestConditionCd != null && !defaultLabTestConditionCd.isEmpty())
						resultedTestConditionList.add(defaultLabTestConditionCd);
				}
				if (resultedTestConditionList.isEmpty()) {
					noConditionFoundForResultedTestCount = noConditionFoundForResultedTestCount + 1;
				}
				//if we found conditions add them to the return list
				if (!resultedTestConditionList.isEmpty()) {
					Set<String> hashset = new HashSet<String>();
					hashset.addAll(returnList);
					hashset.addAll(resultedTestConditionList);
					//get rid of dups..
					returnList = new ArrayList<String>(hashset);
				} //resulted test condition list not empty
			} //end of if valid resulted test
		} // end of while more resulted tests
		//if we couldn't derive a condition for a test, return no conditions
		if (noConditionFoundForResultedTestCount > 0)
			returnList.clear(); //incomplete list - return empty list

		return returnList;
} // end of ConditionList

/**
 * Returns a List of Condition Codes associated with the passed Snomed codes.
 * 
 * @param reportingLabCLIA : String
 * @param obsValueCodedDtColl : Collection
 * @return ArrayList<string>
 */
 // AK - 7/25/04
	private ArrayList<String> getConditionsFromSNOMEDCodes(
			String reportingLabCLIA, Collection<Object> obsValueCodedDtColl) {

		ArrayList<String> snomedConditionList = new ArrayList<String>();
		SRTMapDAOImpl dao = new SRTMapDAOImpl();

		if (obsValueCodedDtColl != null) {
			Iterator<Object> codedDtIt = obsValueCodedDtColl.iterator();
			while (codedDtIt.hasNext()) {
				String snomedCd = "";
				String conditionCd = "";
				ObsValueCodedDT codedDt = (ObsValueCodedDT) codedDtIt.next();
				String codeSystemCd = codedDt.getCodeSystemCd();

				if (codeSystemCd == null || codeSystemCd.trim().equals(""))
					continue;

				String obsCode = codedDt.getCode();
				if (obsCode == null || obsCode.trim().equals(""))
					continue;

				/* If the code is not a Snomed code, try to get the snomed code.
				 * Check if ObsValueCodedDT.codeSystemCd='L' and CLIA for
				 * Reporting Lab is available, find the Snomed code for
				 * ObsValueCodedDT.code(Local Result to Snomed lookup)
				 */
				if (!codeSystemCd.equals(ELRConstants.ELR_SNOMED_CD)) {
					ArrayList<Object> snomedList = (ArrayList<Object>) dao
							.getSnomed(codedDt.getCode(),
							// ELRConstants.TYPE = "LR"
									ELRConstants.TYPE, reportingLabCLIA);
					if (((Integer) snomedList.get(1)).intValue() == 1)
						snomedCd = (String) snomedList.get(0);
					else continue;
				}

				/*
				 * If already coded using SNOMED code, just add it to the return
				 * array. check if ObsValueCodedDT.codeSystemCd="SNM", use
				 * ObsValueCodedDT.code for Snomed
				 */
				else if (codeSystemCd.equals("SNM")) {
					snomedCd = obsCode;
				}
				
				//if these is a Snomed code, see if we can get a corresponding condition for it
				try {
					conditionCd = dao.getConditionForSnomedCode(snomedCd);
				} catch (Exception e) {
					e.printStackTrace();
				}
				if (conditionCd != null && !conditionCd.isEmpty())
					snomedConditionList.add(conditionCd);
			} // end of while has next
		} // end if collection not null
		return snomedConditionList;
	} //getConditionsFromSNOMEDCodes()
	
	private String getConditionForLOINCCode(String reportingLabCLIA,
			ObservationVO resultTestVO) throws NEDSSDAOSysException {

		String loincCd = "";
		ObservationDT obsDt = resultTestVO.getTheObservationDT();
		if (obsDt == null || reportingLabCLIA == null)
			return null;

		String cdSystemCd = obsDt.getCdSystemCd();
		if (cdSystemCd == null || cdSystemCd.trim().equals(""))
			return null;

		String obsCode = obsDt.getCd();
		if (obsCode == null || obsCode.trim().equals(""))
			return null;

		SRTMapDAOImpl dao = new SRTMapDAOImpl();
		if (cdSystemCd.equals(ELRConstants.ELR_OBSERVATION_LOINC)) {
			loincCd = obsCode;
		} else {
			ArrayList<Object> loincList = (ArrayList<Object>) dao
					.getMsgProgAreaLoincSnomed(obsCode, "LT", reportingLabCLIA);
			if (((Integer) loincList.get(1)).intValue() == 1)
				loincCd = (String) loincList.get(0);
		}

		// If we have resolved the LOINC code, try to derive the condition
		if (loincCd == null || loincCd.isEmpty())
			return loincCd;

		String conditionCd = "";
		try {
			conditionCd = dao.getConditionForLoincCode(loincCd);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return (conditionCd);

	} //end of getConditionForLoincCode()
	
	 /**
	   * Gets the default condition for a Local Result code.
	   * If we find that it maps to more than one condition code, return nothing.
	   * @param reportingLabCLIA : String
	   * @param obsValueCodedDtColl: Collection
	   * @return conditionCd : String
	   */
	  public String getConditionCodeForLocalResultCode(String reportingLabCLIA,
			  Collection<Object> obsValueCodedDtColl) {
		  String conditionCd = "";
		  HashMap<String, String> conditionMap = new HashMap<String,String>();
		  if (obsValueCodedDtColl == null || reportingLabCLIA == null)
			  return null;

		SRTMapDAOImpl dao = new SRTMapDAOImpl();
	  	Iterator<Object> codedDtIt = obsValueCodedDtColl.iterator();
		  while (codedDtIt.hasNext()) {
			  ObsValueCodedDT obsValueCodedDT = (ObsValueCodedDT) codedDtIt.next();
			  String code = obsValueCodedDT.getCode();
			  //String codeSystemCd = obsValueCodedDT.getCodeSystemCd();
			  if (code != null) {
					String defaultCondition = dao.getDefaultConditionForLocalResultCode(code, reportingLabCLIA);
					if (defaultCondition != null && !defaultCondition.isEmpty()) {
						conditionCd = defaultCondition;
						conditionMap.put(defaultCondition, code);
					}
			  } 
		}
		if (conditionMap.size() > 1 || conditionMap.isEmpty())
			return("");
		else return(conditionCd);
	  }	
	  
		/**
	   * Gets the default condition for the Local Test code.
	   * @param resultTestVO : Collection
	   * @param reportingLabCLIA : String
	   * @return conditionCd : String
	   */
	  public String getConditionCodeForLocalTestCode(String reportingLabCLIA,
              ObservationVO resultTestVO) {
	  
		  //edit checks
		  if (reportingLabCLIA == null || resultTestVO == null)
			  return null;
		  ObservationDT obsDt = resultTestVO.getTheObservationDT();
	      if (obsDt.getCd() == null || obsDt.getCd().equals("") ||
		            obsDt.getCd().equals(" ") || obsDt.getCdSystemCd() == null)  
	    	  return null;
	      
	      String testCd = obsDt.getCd();

		  SRTMapDAOImpl dao = new SRTMapDAOImpl();

		  String conditionCd = dao.getDefaultConditionForLocalResultCode(testCd, reportingLabCLIA);
		  return(conditionCd);
	  } //getConditionCodeForLocalTestCode()	
	  
		/**
	   * Gets the default condition for the Lab Test code.
	   * @param lab_test_cd
	   * @return conditionCd : String
	   */
	  public String getDefaultConditionForLabTestCode(String labTestCd, String reportingLabCLIA) {
	  
		  SRTMapDAOImpl dao = new SRTMapDAOImpl();

		  String conditionCd = dao.getDefaultConditionForLabTest(labTestCd, reportingLabCLIA );
		  //see if the DEFAULT is set for the lab test if still not found..
		  if ((conditionCd == null || conditionCd.isEmpty()) && !reportingLabCLIA.equals(NEDSSConstants.DEFAULT))
			   conditionCd = dao.getDefaultConditionForLabTest(labTestCd, NEDSSConstants.DEFAULT);
		  
		  return(conditionCd);
	  } 		  
	  
}