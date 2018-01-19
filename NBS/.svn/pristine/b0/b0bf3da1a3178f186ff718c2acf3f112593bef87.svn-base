package gov.cdc.nedss.proxy.ejb.tasklistproxyejb.dao;

import gov.cdc.nedss.act.publichealthcase.ejb.dao.PublicHealthCaseDAOImpl;
import gov.cdc.nedss.act.sqlscript.WumSqlQuery;
import gov.cdc.nedss.exception.NEDSSSystemException;
import gov.cdc.nedss.proxy.ejb.investigationproxyejb.vo.InvestigationSummaryVO;
import gov.cdc.nedss.proxy.ejb.observationproxyejb.vo.ObservationSummaryVO;
import gov.cdc.nedss.proxy.ejb.queue.dao.MessageLogDAOImpl;
import gov.cdc.nedss.proxy.ejb.queue.dt.MessageLogDT;
import gov.cdc.nedss.proxy.ejb.tasklistproxyejb.vo.TaskListItemVO;
import gov.cdc.nedss.systemservice.ejb.mainsessionejb.bean.MainSessionCommand;
import gov.cdc.nedss.systemservice.nbssecurity.NBSBOLookup;
import gov.cdc.nedss.systemservice.nbssecurity.NBSOperationLookup;
import gov.cdc.nedss.systemservice.nbssecurity.NBSSecurityObj;
import gov.cdc.nedss.systemservice.nbssecurity.ProgramAreaJurisdictionUtil;
import gov.cdc.nedss.systemservice.util.MainSessionHolder;
import gov.cdc.nedss.util.CtrlCdDisplaySummaryVO;
import gov.cdc.nedss.util.DAOBase;
import gov.cdc.nedss.util.DataTables;
import gov.cdc.nedss.util.JNDINames;
import gov.cdc.nedss.util.LogUtils;
import gov.cdc.nedss.util.NEDSSConstants;
import gov.cdc.nedss.util.PropertyUtil;
import gov.cdc.nedss.util.ResultSetUtils;
import gov.cdc.nedss.webapp.nbs.logicsheet.helper.CachedDropDownValues;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.TreeMap;

public class TaskListProxyDAOImpl extends DAOBase
{
    public TaskListProxyDAOImpl()
    {
     
    }

    private static final LogUtils logger = new LogUtils(TaskListProxyDAOImpl.class.getName());
    private static PropertyUtil propertyUtil= PropertyUtil.getInstance();
       
    /**
     * getCount
     * returns the count for task list available
     * @param aQuery String
     * @return Integer
     * @throws Exception
     */
     public Integer getCount(String aQuery) throws Exception
     {
    	 try{
	        logger.debug(aQuery);
	        Integer count = null;
            logger.debug("Begin execution of TaskListProxyDAOImpl.getCount() query :"+aQuery);
            count = ((Integer)preparedStmtMethod(null,null,aQuery,NEDSSConstants.SELECT_COUNT));
            logger.debug("End execution of TaskListProxyDAOImpl.getCount() query :"+aQuery);
            logger.info("getCount - count = " + count);

            return count;
    	 }catch(Exception ex){
    		 logger.fatal("Exception  = "+ex.getMessage(), ex);
    		 throw new Exception(ex.toString());
    	 }
     }

     // called by TaskListProxyEJB.getMyInvestigations method
     @SuppressWarnings("unchecked")
	public Collection<Object>  getInvestigationSummaryVOCollection(NBSSecurityObj nbssecurityObj) throws Exception
     {
        InvestigationSummaryVO investigationSummaryVO = new InvestigationSummaryVO();
        ArrayList<Object>  ar = new ArrayList<Object> ();
        logger.debug("getInvestigationSummaryVOCollection  - ar = " + ar);
        logger.debug("Begin execution of TaskListProxyDAOImpl.getInvestigationSummaryVOCollection  method");
        // Get Security Properties for MyPAInvestigations 
        String myPAInvestigationsSecurity = propertyUtil.getMyProgramAreaSecurity();
        logger.debug("TaskListProxyDAOImpl.getInvestigationSummaryVOCollection(NBSSecurityObj) - myPAInvestigationsSecurity = " + myPAInvestigationsSecurity);
      
        if (myPAInvestigationsSecurity==null)
            throw new NEDSSSystemException("TaskListProxyDAOImpl.getInvestigationSummaryVOCollection  - Security property settings missing for MyPAInvestigations");
       		
        if ( !(myPAInvestigationsSecurity.equals(NBSOperationLookup.VIEW)) &&
           	 !(myPAInvestigationsSecurity.equals(NBSOperationLookup.EDIT)) )
               throw new NEDSSSystemException("TaskListProxyDAOImpl.getInvestigationSummaryVOCollection  - Security property settings for MyPAInvestigations is incorrect");
        
        try
        {
            if(nbssecurityObj.getPermission(NBSBOLookup.INVESTIGATION, myPAInvestigationsSecurity))
            {
                String dataAccessWhereClause = nbssecurityObj.getDataAccessWhereClause(NBSBOLookup.INVESTIGATION, myPAInvestigationsSecurity, DataTables.PUBLIC_HEALTH_CASE_TABLE);
                if(dataAccessWhereClause == null)
                   dataAccessWhereClause = "";
                else
                   dataAccessWhereClause = "AND " + dataAccessWhereClause;

                String aQuery;
                if (propertyUtil.getDatabaseServerType() != null && propertyUtil.getDatabaseServerType().equalsIgnoreCase(NEDSSConstants.ORACLE_ID))
                {
                    logger.info("propertyUtil.getDatabaseServerType() = " + propertyUtil.getDatabaseServerType());
                    aQuery = WumSqlQuery.SELECT_MY_INVESTIGATIONS_ORACLE +
                            "Public_health_case.investigation_status_cd = '" + NEDSSConstants.STATUS_OPEN +
                            "' AND Public_health_case.record_status_cd <> '" + NEDSSConstants.RECORD_STATUS_LOGICAL_DELETE +
                            "' " + dataAccessWhereClause;
                }
                else
                {
                    logger.info("propertyUtil.getDatabaseServerType() = " + propertyUtil.getDatabaseServerType());
                    aQuery = WumSqlQuery.SELECT_MY_INVESTIGATIONS_SQL +
                            "where (Public_health_case.investigation_status_cd = '" + NEDSSConstants.STATUS_OPEN +
                            "' AND Public_health_case.record_status_cd <> '" + NEDSSConstants.RECORD_STATUS_LOGICAL_DELETE +
                            "' " + dataAccessWhereClause+")";
                }
                logger.info("aQuery = " + aQuery);

                logger.info("getInvestigationSummaryVOCollection  - aQuery = " + aQuery);
                ar = (ArrayList<Object> )preparedStmtMethod(investigationSummaryVO, null, aQuery, NEDSSConstants.SELECT);
                CachedDropDownValues cache = new CachedDropDownValues();
                TreeMap<?, ?> mapPhcSts = cache.getCodedValuesAsTreeMap("PHC_IN_STS");
                mapPhcSts = cache.reverseMap(mapPhcSts); // we can add another method that do not do reverse
                TreeMap<Object, Object> mapConditionCode = cache.getConditionCodes();
                TreeMap<Object, Object> mapJurisdiction = cache.getJurisdictionCodedValuesWithAll();
                TreeMap<?, ?> mapPhcClass = cache.getCodedValuesAsTreeMap("PHC_CLASS");
                mapPhcClass = cache.reverseMap(mapPhcClass); // we can add another method that do not do reverse
                String  phcLocal = null;
                for (Iterator<Object> anIterator = ar.iterator(); anIterator.hasNext(); ) {
                	                    logger.debug(
                      "WorkupProxyEJB:getInvestgationSummaryVO anIterator.hasNext()" +
                      anIterator.hasNext());
                  investigationSummaryVO = (InvestigationSummaryVO) anIterator.next();
                  if(phcLocal!=null && phcLocal.equalsIgnoreCase(investigationSummaryVO.getLocalId())){
                	continue;
                  }
                  phcLocal=investigationSummaryVO.getLocalId();
                  if (investigationSummaryVO.getCaseClassCd() != null
                      && investigationSummaryVO.getCaseClassCd().trim().length() != 0) {
                    investigationSummaryVO.setCaseClassCodeTxt( (String) mapPhcClass.get(
                        investigationSummaryVO.getCaseClassCd()));
                  }

                  if (investigationSummaryVO.getInvestigationStatusCd() != null
                      &&
                      investigationSummaryVO.getInvestigationStatusCd().trim().length() != 0) {
                    investigationSummaryVO.setInvestigationStatusDescTxt( (String) mapPhcSts.
                        get(investigationSummaryVO.getInvestigationStatusCd()));
                  }

                  if (investigationSummaryVO.getCd() != null
                      && investigationSummaryVO.getCd().trim().length() != 0) {
                    // map = cache.reverseMap(map); // we can add another method that do not do reverse
                    investigationSummaryVO.setConditionCodeText( (String) mapConditionCode.get(
                        investigationSummaryVO.getCd()));
                  }

                  if (investigationSummaryVO.getJurisdictionCd() != null
                      &&
                      investigationSummaryVO.getJurisdictionCd().trim().length() != 0) {
                    //   map = cache.reverseMap(map); // we can add another method that do not do reverse
                    investigationSummaryVO.setJurisdictionDescTxt( (String) mapJurisdiction.get(
                        investigationSummaryVO.getJurisdictionCd()));
                  }
                }

            }
            else
            {
                throw new NEDSSSystemException("NO PERMISSIONS");
            }
        }
        catch(Exception sex)
        {
            logger.fatal("Error in TaskListProxyDAOImpl getInvestigationSummaryVOCollection  = " +  sex.getMessage(), sex);
            throw new Exception(sex.toString());
        }
        logger.debug("End execution of TaskListProxyDAOImpl.getInvestigationSummaryVOCollection  method");
        logger.info("returning ar - " + ar.toString());
        return ar;
     }

     // called by TaskListProxyEJB.getObservationsNeedingReview method
     public Collection<Object>  getObservationsNeedingReview (NBSSecurityObj nbssecurityObj) throws Exception
     {
        Connection dbConnection = null;
        ObservationSummaryVO observationSummaryVO = new ObservationSummaryVO();
        PreparedStatement preparedStmt = null;
        ResultSet resultSet = null;
        ResultSetMetaData resultSetMetaData = null;
        ResultSetUtils resultSetUtils = new ResultSetUtils();
        ArrayList<Object> ar = new ArrayList<Object> ();
        logger.debug("Begin execution of TaskListProxyDAOImpl.getObservationsNeedingReview method");
        // Get Security Properties for ObsNeedingReview
        String obsNeedingReviewSecurity = propertyUtil.getObservationsNeedingReviewSecurity();
        logger.debug("TaskListProxyDAOImpl.getObservationsNeedingReview - obsNeedingReviewSecurity = " + obsNeedingReviewSecurity);
      
        if (obsNeedingReviewSecurity==null) 
            throw new NEDSSSystemException("TaskListProxyDAOImpl.getTaskListItems - Security property settings missing for ObsNeedingReview");

        if ( !(obsNeedingReviewSecurity.equals(NBSOperationLookup.VIEW)) &&
        	 !(obsNeedingReviewSecurity.equals(NBSOperationLookup.EDIT)) )
            throw new NEDSSSystemException("TaskListProxyDAOImpl.getTaskListItems - Security property settings for ObsNeedingReview is incorrect");
                
        try
        {
            dbConnection = getConnection();
        }
        catch(NEDSSSystemException nsex)
        {
            logger.fatal("Error obtaining dbConnection in getObservationsNeedingReview() = " + nsex);
            nsex.printStackTrace();
            throw new Exception(nsex.toString());
        }

        try
        {
            if(nbssecurityObj.getPermission(NBSBOLookup.OBSERVATIONLABREPORT, obsNeedingReviewSecurity))
            {
                String dataAccessWhereClause = nbssecurityObj.getDataAccessWhereClause(NBSBOLookup.OBSERVATIONLABREPORT, obsNeedingReviewSecurity, DataTables.OBSERVATION_TABLE);
                if(dataAccessWhereClause == null)
                   dataAccessWhereClause = "";
                else
                   dataAccessWhereClause = "AND " + dataAccessWhereClause;

                String aQuery = null;
                if(propertyUtil.getDatabaseServerType() != null && propertyUtil.getDatabaseServerType().equalsIgnoreCase(NEDSSConstants.ORACLE_ID)){
                    aQuery = "SELECT "+
                        "observation.add_time \"addTime\", "+
                        "p.local_id \"localId\", "+
                        "p.person_uid \"subjectUid\", "+
                        "labTest.nbs_test_desc_txt \"cdDescTxt\", "+
                        "observation.observation_uid \"observationUid\", "+
                        "observation.local_id \"observationLocalId\", "+
                        "observation.cd \"cd\", "+
                        "observation.cd_desc_txt \"cdDescTxt\", "+
                        "observation.record_status_cd \"recordStatusCd\", "+
                        "observation.activity_from_time \"activityFromTime\", "+
                        "observation.effective_from_time \"effectiveFromTime\" "+
                        "from observation observation, participation part, person p, " + NEDSSConstants.SYSTEM_REFERENCE_TABLE + ".nbs_lab_test labTest "+
                        "where labTest.nbs_test_code(+) = observation.cd "+
                        "and part.act_uid = observation.observation_uid "+
                        "and part.subject_entity_uid = p.person_uid "+
                        "and upper(p.record_status_cd) != 'LOG_DEL' "+
                        "and observation.record_status_cd = '" + NEDSSConstants.OBSERVATION_RECORD_STATUS_CD + "' "+
                        "and observation.ctrl_cd_display_form = '" + NEDSSConstants.LAB_REPORT + "' "+
                        "and part.type_cd = 'PATSBJ' "+
                        "and part.subject_class_cd = 'PSN' "+
                        "and part.act_class_cd = 'OBS' "+
                        dataAccessWhereClause;
                } else {
                    aQuery = "select " +
                    DataTables.OBSERVATION_TABLE + ".add_time addTime, " +
                    DataTables.PERSON_TABLE + ".local_id localId, " +
                    DataTables.PERSON_TABLE + ".person_uid subjectUid, " +
    //                DataTables.OBSERVATION_TABLE + ".obs_domain_cd obsDomainCd, " +
                    "labTest.nbs_test_desc_txt cdDescTxt, " +
                    DataTables.OBSERVATION_TABLE + ".observation_uid observationUid, " +
                    DataTables.OBSERVATION_TABLE + ".local_id observationLocalId, " +
                    DataTables.OBSERVATION_TABLE + ".cd cd, " +
                    DataTables.OBSERVATION_TABLE + ".cd_desc_txt cdDescTxt, " +
    //                DataTables.OBSERVATION_TABLE + ".status_cd statusCd, " +
                    DataTables.OBSERVATION_TABLE + ".record_status_cd recordStatusCd, " +
                    DataTables.OBSERVATION_TABLE + ".activity_from_time activityFromTime, " +
                    DataTables.OBSERVATION_TABLE + ".effective_from_time effectiveFromTime " +
                    "from " + DataTables.OBSERVATION_TABLE + " " +
                    DataTables.OBSERVATION_TABLE + " " +
                    "inner join " + DataTables.PARTICIPATION_TABLE + " " +  DataTables.PARTICIPATION_TABLE + " " +
                    "on " + DataTables.PARTICIPATION_TABLE + ".act_uid = " + DataTables.OBSERVATION_TABLE + ".observation_uid " +
                    "inner join " + DataTables.PERSON_TABLE + " " + DataTables.PERSON_TABLE + " " +
                    "on " + DataTables.PARTICIPATION_TABLE + ".subject_entity_uid = " + DataTables.PERSON_TABLE + ".person_uid " +
                    "and upper(" + DataTables.PERSON_TABLE + ".record_status_cd) != 'LOG_DEL' " +
                    "left outer join " + NEDSSConstants.SYSTEM_REFERENCE_TABLE + "..nbs_lab_test labTest " +
                    "on " + DataTables.OBSERVATION_TABLE + ".cd = labTest.nbs_test_code " +
                    "where " + DataTables.OBSERVATION_TABLE + ".record_status_cd = '" + NEDSSConstants.OBSERVATION_RECORD_STATUS_CD + "' " +
                    "and " + DataTables.OBSERVATION_TABLE + ".ctrl_cd_display_form = '" + NEDSSConstants.LAB_REPORT + "' " +
                    "and " + DataTables.PARTICIPATION_TABLE + ".type_cd = 'PATSBJ' " +
                    "and " + DataTables.PARTICIPATION_TABLE + ".subject_class_cd = '" + NEDSSConstants.CLASS_CD_PSN + "' " +
                    "and " + DataTables.PARTICIPATION_TABLE + ".act_class_cd = '" + NEDSSConstants.CLASS_CD_OBS + "' " +
                    dataAccessWhereClause;
                }

				// put this back in and use it wehn file gets updated
				// to use ORACLE  <<<<<<<<<<------------------------
				/*                if (propertyUtil.getDatabaseServerType() != null && propertyUtil.getDatabaseServerType().equalsIgnoreCase(NEDSSConstants.ORACLE_ID))
				                {
				                    logger.info("propertyUtil.getDatabaseServerType() = " + propertyUtil.getDatabaseServerType());
				                    aQuery = WumSqlQuery.SELECT_MY_INVESTIGATIONS_ORACLE +
				                            "investigation_status_cd = " + NEDSSConstants.STATUS_OPEN +
				                            " " + dataAccessWhereClause;
				                }
				                else
				                {
				                    logger.info("propertyUtil.getDatabaseServerType() = " + propertyUtil.getDatabaseServerType());
				                    aQuery = WumSqlQuery.SELECT_MY_INVESTIGATIONS_SQL +
				                            "where (investigation_status_cd = " + NEDSSConstants.STATUS_OPEN +
				                            " " + dataAccessWhereClause + ")";
				                }
				*/
                logger.debug("getObservationsNeedingReview - aQuery = " + aQuery);
                preparedStmt = dbConnection.prepareStatement(aQuery);
                resultSet = preparedStmt.executeQuery();
                resultSetMetaData = resultSet.getMetaData();
                String rows = "rows = " + resultSet.getRow();
                ar = (ArrayList<Object> )resultSetUtils.mapRsToBeanList(resultSet, resultSetMetaData, observationSummaryVO.getClass(), ar);
            }
            else
            {
                throw new NEDSSSystemException("NO PERMISSIONS");
            }
        }
        catch(Exception sex)
        {
            logger.fatal("Error in TaskListProxyDAOImpl getObservationsNeedingReview = " +  sex.getMessage(), sex);
            throw new Exception(sex.toString());
        }
        finally
        {
            closeResultSet(resultSet);
            closeStatement(preparedStmt);
            releaseConnection(dbConnection);
        }
        logger.debug("End execution of TaskListProxyDAOImpl.getObservationsNeedingReview method");
        logger.info("returning ar - " + ar.toString());
        return ar;
     }// end getObservationsNeedingReview

     @SuppressWarnings("unchecked")
	public List<?>[] findObservationsNeedingReview (NBSSecurityObj nbssecurityObj)
     {
        String SELECT_OBSUIDS;
        ArrayList<Object> labUidList = new ArrayList<Object> ();
        String aQuery = "";

        // Get Security Properties for ObsNeedingReview
        String obsNeedingReviewSecurity = propertyUtil.getObservationsNeedingReviewSecurity();
        logger.debug("TaskListProxyDAOImpl.findObservationsNeedingReview - obsNeedingReviewSecurity = " + obsNeedingReviewSecurity);
      
        if (obsNeedingReviewSecurity==null) 
            throw new NEDSSSystemException("TaskListProxyDAOImpl.getTaskListItems - Security property settings missing for ObsNeedingReview");

        if ( !(obsNeedingReviewSecurity.equals(NBSOperationLookup.VIEW)) &&
        	 !(obsNeedingReviewSecurity.equals(NBSOperationLookup.EDIT)) )
            throw new NEDSSSystemException("TaskListProxyDAOImpl.getTaskListItems - Security property settings for ObsNeedingReview is incorrect");
        
        try {
          String labDataAccessWhereClause = nbssecurityObj.getDataAccessWhereClause(NBSBOLookup.OBSERVATIONLABREPORT,obsNeedingReviewSecurity);
          labDataAccessWhereClause = labDataAccessWhereClause != null ? "AND " + labDataAccessWhereClause : "";
          String morbDataAccessWhereClause = nbssecurityObj.getDataAccessWhereClause(NBSBOLookup.OBSERVATIONMORBIDITYREPORT,obsNeedingReviewSecurity);
          morbDataAccessWhereClause = morbDataAccessWhereClause != null ? "AND " + morbDataAccessWhereClause : "";
          if(nbssecurityObj.getPermission(NBSBOLookup.OBSERVATIONLABREPORT,obsNeedingReviewSecurity)
           && nbssecurityObj.getPermission(NBSBOLookup.OBSERVATIONMORBIDITYREPORT,obsNeedingReviewSecurity))
          { aQuery = "SELECT "+
                "observation_uid \"uid\", " +
                "ctrl_cd_display_form \"ctrlCdDisplayForm\"" +
                "FROM observation  " +
                "WHERE (record_status_cd = '" + NEDSSConstants.OBS_UNPROCESSED +
                "' " +
                "OR record_status_cd = '" +
                NEDSSConstants.OBS_UNPROCESSED_PREV_D + "') " +
                "AND ((ctrl_cd_display_form = '" + NEDSSConstants.MOB_CTRLCD_DISPLAY +
                "' " + morbDataAccessWhereClause + ") " +
                "OR (ctrl_cd_display_form = '" + NEDSSConstants.LAB_CTRLCD_DISPLAY + "' " +
                labDataAccessWhereClause + "))";
          }
          else if((nbssecurityObj.getPermission(NBSBOLookup.OBSERVATIONLABREPORT,obsNeedingReviewSecurity))
           && !(nbssecurityObj.getPermission(NBSBOLookup.OBSERVATIONMORBIDITYREPORT,obsNeedingReviewSecurity)))
          {
            aQuery = "SELECT "+
                  "observation_uid \"uid\", " +
                  "ctrl_cd_display_form \"ctrlCdDisplayForm\"" +
                  "FROM observation  " +
                  "WHERE (record_status_cd = '" + NEDSSConstants.OBS_UNPROCESSED +
                  "' " +
                  "OR record_status_cd = '" +
                  NEDSSConstants.OBS_UNPROCESSED_PREV_D + "') " +
                  "AND "  +
                  " (ctrl_cd_display_form = '" + NEDSSConstants.LAB_CTRLCD_DISPLAY + "' " +
                  labDataAccessWhereClause + ")";

          }
          else if(!(nbssecurityObj.getPermission(NBSBOLookup.OBSERVATIONLABREPORT,obsNeedingReviewSecurity))
          && nbssecurityObj.getPermission(NBSBOLookup.OBSERVATIONMORBIDITYREPORT,obsNeedingReviewSecurity))
         {
           aQuery = "SELECT "+
                        "observation_uid \"uid\", " +
                        "ctrl_cd_display_form \"ctrlCdDisplayForm\"" +
                        "FROM observation  " +
                        "WHERE (record_status_cd = '" + NEDSSConstants.OBS_UNPROCESSED +
                        "' " +
                        "OR record_status_cd = '" +
                        NEDSSConstants.OBS_UNPROCESSED_PREV_D+ "') " +
                        "AND "  +
                        " (ctrl_cd_display_form = '" + NEDSSConstants.MOB_CTRLCD_DISPLAY + "' " +
                        morbDataAccessWhereClause + ")";


         }


         CtrlCdDisplaySummaryVO ctrlCdDisplaySummaryVO = new CtrlCdDisplaySummaryVO();
         aQuery = aQuery + " order by rpt_to_state_time ";
        // System.out.println("\n\n aQuery " +aQuery );
         labUidList = (ArrayList<Object> ) preparedStmtMethod(ctrlCdDisplaySummaryVO, null,
                                                  aQuery,
                                                  NEDSSConstants.SELECT);
        List<?>[] labList = null;
        labList = this.buildObservationUidList(labUidList);
          return labList;
        }
        catch (Exception ex) {
	        logger.fatal("Error while getting observations for Review"+ex.getMessage(), ex);
	        throw new NEDSSSystemException(ex.toString());
        }
     }// end findObservationsNeedingReview

     private List<?>[] buildObservationUidList(ResultSet resultSet) throws SQLException, NEDSSSystemException
     {
       List<Object> labReportUidList = null;
       List<Object> morbReportUidList = null;
       try{
              while(resultSet.next())
              {
                long observationUid =  resultSet.getLong(1);
                String ctrlCdDisplayForm = resultSet.getString(2);

                if(ctrlCdDisplayForm != null && ctrlCdDisplayForm.equals(NEDSSConstants.LAB_REPORT))
                {
                  if(labReportUidList == null) labReportUidList = new ArrayList<Object> ();
                  labReportUidList.add(new Long(observationUid));
                }
                else if(ctrlCdDisplayForm != null && ctrlCdDisplayForm.equals(NEDSSConstants.MORBIDITY_REPORT))
                {
                  if (morbReportUidList == null) morbReportUidList = new ArrayList<Object> ();
                  morbReportUidList.add(new Long(observationUid));
                }
              }

              List<?>[] returnObsUids = {labReportUidList, morbReportUidList};

              return returnObsUids;
       }catch(SQLException ex){
    	   logger.fatal("SQLException  = "+ex.getMessage(), ex);
    	   throw new SQLException(ex.toString());
       }catch(Exception ex){
    	   logger.fatal("Exception  = "+ex.getMessage(), ex);
    	   throw new NEDSSSystemException(ex.toString());
       }
     }

  private List<?>[] buildObservationUidList(ArrayList<Object>  ctrlCdDisplaySummaryVOColl) throws SQLException, NEDSSSystemException
 {
	   List<Object> labReportUidList = null;
	   List<Object> morbReportUidList = null;
	   try{
		   if(ctrlCdDisplaySummaryVOColl!=null)
		   {
		    Iterator<Object>  itor = ctrlCdDisplaySummaryVOColl.iterator();
		     while (itor.hasNext()) {
		       CtrlCdDisplaySummaryVO ctrlCdDisplaySummaryVO = (CtrlCdDisplaySummaryVO)itor.next();
		       Long observationUid = ctrlCdDisplaySummaryVO.getUid();
		       String ctrlCdDisplayForm = ctrlCdDisplaySummaryVO.getCtrlCdDisplayForm();
		
		       if (ctrlCdDisplayForm != null &&
		           ctrlCdDisplayForm.equals(NEDSSConstants.LAB_REPORT)) {
		         if (labReportUidList == null)
		           labReportUidList = new ArrayList<Object> ();
		         labReportUidList.add(observationUid);
		       }
		       else if (ctrlCdDisplayForm != null &&
		                ctrlCdDisplayForm.equals(NEDSSConstants.MORBIDITY_REPORT)) {
		         if (morbReportUidList == null)
		           morbReportUidList = new ArrayList<Object> ();
		         morbReportUidList.add(observationUid);
		       }
		     }
		   }
	
	      List<?>[] returnObsUids = {labReportUidList, morbReportUidList};
	
          return returnObsUids;
	   }catch(Exception ex){
		   logger.fatal("Exception  = "+ex.getMessage(), ex);
		   throw new NEDSSSystemException(ex.toString());
	   }
 }


     @SuppressWarnings("unchecked")
	public List<?>[] findObservationsNeedingSecurityAssignment (NBSSecurityObj securityObj)
     {
        ArrayList<Object> labUidList = new ArrayList<Object> ();
        String aQuery = "";
        try
        {
	         if(securityObj.getPermission(NBSBOLookup.OBSERVATIONLABREPORT,NBSOperationLookup.ASSIGNSECURITY)
	         && securityObj.getPermission(NBSBOLookup.OBSERVATIONMORBIDITYREPORT,NBSOperationLookup.ASSIGNSECURITY))
	         {aQuery = "SELECT "+
	               "observation_uid \"uid\", " +
	               "ctrl_cd_display_form \"ctrlCdDisplayForm\"" +
	               "FROM observation  " +
	               "WHERE (prog_area_cd IS NULL " +
	               "OR jurisdiction_cd IS NULL) " +
	               "AND (ctrl_cd_display_form = '" + NEDSSConstants.MOB_CTRLCD_DISPLAY +
	               "' " +
	               "OR ( ctrl_cd_display_form = '" + NEDSSConstants.LAB_REPORT + "' and "+
						     " obs_domain_cd_st_1 = 'Order' ))";
	         }
	         if(securityObj.getPermission(NBSBOLookup.OBSERVATIONLABREPORT,NBSOperationLookup.ASSIGNSECURITY)
	         && !securityObj.getPermission(NBSBOLookup.OBSERVATIONMORBIDITYREPORT,NBSOperationLookup.ASSIGNSECURITY))
	        {aQuery = "SELECT "+
	          "observation_uid \"uid\", " +
	          "ctrl_cd_display_form \"ctrlCdDisplayForm\"" +
	          "FROM observation  " +
	          "WHERE (prog_area_cd IS NULL " +
	          "OR jurisdiction_cd IS NULL) " +
	          "AND (( ctrl_cd_display_form = '" + NEDSSConstants.LAB_REPORT + "'"+
						 " and obs_domain_cd_st_1 = 'Order'))";
	       }
	       if (!securityObj.getPermission(NBSBOLookup.OBSERVATIONLABREPORT,
	                                     NBSOperationLookup.ASSIGNSECURITY)
	           &&
	           securityObj.getPermission(NBSBOLookup.OBSERVATIONMORBIDITYREPORT,
	                                      NBSOperationLookup.ASSIGNSECURITY)) {
	         aQuery = "SELECT " +
             "observation_uid \"uid\", " +
             "ctrl_cd_display_form \"ctrlCdDisplayForm\"" +
             "FROM observation  " +
             "WHERE (prog_area_cd IS NULL " +
             "OR jurisdiction_cd IS NULL) " +
             "AND (ctrl_cd_display_form = '" + NEDSSConstants.MOB_CTRLCD_DISPLAY+ "')";
       		}



           aQuery = aQuery + " order by rpt_to_state_time ";
           logger.debug("aQuery:  " + aQuery);

           CtrlCdDisplaySummaryVO ctrlCdDisplaySummaryVO = new CtrlCdDisplaySummaryVO();
           labUidList = (ArrayList<Object> ) preparedStmtMethod(ctrlCdDisplaySummaryVO, null,
                                                     aQuery,
                                                     NEDSSConstants.SELECT);
           List<?>[] labList = null;
           labList = this.buildObservationUidList(labUidList);
           return labList;

        }
        catch(SQLException sex)
        {
            logger.fatal("Error in TaskListProxyDAOImpl findObservationsNeedingReview = " +  sex.getMessage(), sex);
            throw new NEDSSSystemException(sex.toString());
        }
     }// end findObservationsNeedingSecurityAssignment



     // called by TaskListProxyEJB.getObservationsNeedingSecurityAssignment method
     public Collection<Object>  getObservationsNeedingSecurityAssignment (NBSSecurityObj nbssecurityObj) throws Exception
     {
        logger.debug("getObservationsNeedingSecurityAssignment");
        Connection dbConnection = null;
        ObservationSummaryVO observationSummaryVO = new ObservationSummaryVO();
        PreparedStatement preparedStmt = null;
        ResultSet resultSet = null;
        ResultSetMetaData resultSetMetaData = null;
        ResultSetUtils resultSetUtils = new ResultSetUtils();
        ArrayList<Object> ar = new ArrayList<Object> ();
        logger.debug("Begin execution of TaskListProxyDAOImpl.getObservationsNeedingSecurityAssignment method");
        try
        {
            dbConnection = getConnection();
        }
        catch(NEDSSSystemException nsex)
        {
            logger.fatal("Error obtaining dbConnection in getObservationsNeedingSecurityAssignment() = " + nsex.getMessage(), nsex);
            throw new Exception(nsex.toString());
        }

        try
        {
            if(nbssecurityObj.getPermission(NBSBOLookup.OBSERVATIONLABREPORT, NBSOperationLookup.ASSIGNSECURITY))
            {

                String dataAccessWhereClause = nbssecurityObj.getDataAccessWhereClause(
                            NBSBOLookup.OBSERVATIONLABREPORT,
                            NBSOperationLookup.VIEW,
                            DataTables.OBSERVATION_TABLE);
                if(dataAccessWhereClause == null)
                   dataAccessWhereClause = "";
                else
                   dataAccessWhereClause = "AND " + dataAccessWhereClause;

                logger.debug("dataAccessWhereClause = " + dataAccessWhereClause);

                  String aQuery = null;
                  if(propertyUtil.getDatabaseServerType() != null && propertyUtil.getDatabaseServerType().equalsIgnoreCase(NEDSSConstants.ORACLE_ID)){
                      aQuery = "select "+
                          "Observation.add_time \"addTime\", "+
                          "Observation.obs_domain_cd \"obsDomainCd\", "+
                          "labTest.nbs_test_desc_txt \"cdDescTxt\", "+
                          "Observation.observation_uid \"observationUid\", "+
                          "Observation.local_id \"observationLocalId\", "+
                          "Observation.cd \"cd\", "+
                          "Observation.cd_desc_txt \"cdDescTxt\", "+
                          "Observation.status_cd \"statusCd\", "+
                          "Observation.record_status_cd \"recordStatusCd\", "+
                          "Observation.activity_from_time \"activityFromTime\", "+
                          "Observation.effective_from_time \"effectiveFromTime\" "+
                        "from "+
                          "Observation Observation, " +  NEDSSConstants.SYSTEM_REFERENCE_TABLE + ".nbs_lab_test labTest "+
                        "where "+
                          "Observation.cd = labTest.nbs_test_code(+) "+
                          "and Observation.electronic_ind = 'Y' "+
                          "and Observation.ctrl_cd_display_form = 'LabReport' "+
                          "and (( ( Observation.prog_area_cd is null) "+
                        "or ( Observation.prog_area_cd = '')) "+
                        "or (( Observation.jurisdiction_cd is null) "+
                        "or ( Observation.jurisdiction_cd = '')))";
                  } else {
                      aQuery = "select " +
                      DataTables.OBSERVATION_TABLE + ".add_time addTime, " +
                      DataTables.OBSERVATION_TABLE + ".obs_domain_cd obsDomainCd, " +
                      "labTest.nbs_test_desc_txt cdDescTxt, " +
                      DataTables.OBSERVATION_TABLE + ".observation_uid observationUid, " +
                      DataTables.OBSERVATION_TABLE + ".local_id observationLocalId, " +
                      DataTables.OBSERVATION_TABLE + ".cd cd, " +
                      DataTables.OBSERVATION_TABLE + ".cd_desc_txt cdDescTxt, " +
                      DataTables.OBSERVATION_TABLE + ".status_cd statusCd, " +
                      DataTables.OBSERVATION_TABLE + ".record_status_cd recordStatusCd, " +
                      DataTables.OBSERVATION_TABLE + ".activity_from_time activityFromTime, " +
                      DataTables.OBSERVATION_TABLE + ".effective_from_time effectiveFromTime " +
                      "from " + DataTables.OBSERVATION_TABLE + " " +
                      DataTables.OBSERVATION_TABLE + " " +
                      "left outer join " + NEDSSConstants.SYSTEM_REFERENCE_TABLE + "..nbs_lab_test labTest " +
                      "on " + DataTables.OBSERVATION_TABLE + ".cd = labTest.nbs_test_code " +
                      "where " + DataTables.OBSERVATION_TABLE + ".electronic_ind = 'Y' " +

                      "and " + DataTables.OBSERVATION_TABLE + ".ctrl_cd_display_form = '" + NEDSSConstants.LAB_REPORT + "'" +
                      "and (( ( " + DataTables.OBSERVATION_TABLE + ".prog_area_cd is null) " +
                      "or ( " + DataTables.OBSERVATION_TABLE + ".prog_area_cd = '')) " +
                      "or (( " + DataTables.OBSERVATION_TABLE + ".jurisdiction_cd is null) " +
                      "or ( " + DataTables.OBSERVATION_TABLE + ".jurisdiction_cd = ''))) ";
                  }

                logger.info("getObservationsNeedingSecurityAssignment - aQuery = " + aQuery);
                preparedStmt = dbConnection.prepareStatement(aQuery);
                resultSet = preparedStmt.executeQuery();
                resultSetMetaData = resultSet.getMetaData();
                String rows = "rows = " + resultSet.getRow();
                ar = (ArrayList<Object> )resultSetUtils.mapRsToBeanList(resultSet, resultSetMetaData, observationSummaryVO.getClass(), ar);



            }
            else
            {
                throw new NEDSSSystemException("NO PERMISSIONS");
            }
        }
        catch(Exception sex)
        {
            logger.fatal("Error in TaskListProxyDAOImpl getObservationsNeedingSecurityAssignment = " +  sex.getMessage(), sex);
            throw new Exception(sex.toString());
        }
        finally
        {
            closeResultSet(resultSet);
            closeStatement(preparedStmt);
            releaseConnection(dbConnection);
        }
        logger.debug("End execution of TaskListProxyDAOImpl.getObservationsNeedingSecurityAssignment method");
        return ar;
     }// end getObservationsNeedingSecurityAssignment

    // getTaskListItems -------(The queries in this method are modified for better performance civil00017522)-------------------------
    public Collection<Object>  getTaskListItems (NBSSecurityObj nbssecurityObj) throws javax.ejb.EJBException, Exception
    {
    	logger.debug("Begin execution of TaskListProxyDAOImpl.getTaskListItems method");
        TaskListProxyDAOImpl taskListProxyDAOImpl = new TaskListProxyDAOImpl();
        ArrayList<Object> ar = new ArrayList<Object> ();
        
        // Get Security Properties for ObsNeedingReview
        String obsNeedingReviewSecurity = propertyUtil.getObservationsNeedingReviewSecurity();
        logger.debug("TaskListProxyDAOImpl.getTaskListItems - obsNeedingReviewSecurity = " + obsNeedingReviewSecurity);
      
        if (obsNeedingReviewSecurity==null) 
            throw new NEDSSSystemException("TaskListProxyDAOImpl.getTaskListItems - Security property settings missing for ObsNeedingReview");
        
        if ( (!(obsNeedingReviewSecurity.equals(NBSOperationLookup.VIEW))) &&
        	 (!(obsNeedingReviewSecurity.equals(NBSOperationLookup.EDIT))) )
            throw new NEDSSSystemException("TaskListProxyDAOImpl.getTaskListItems - Security property settings for ObsNeedingReview is incorrect");
                
        try
        {
        	
           // OPEN INVESTIGATIONS
			String myPAInvestigationsSecurity = propertyUtil
					.getMyProgramAreaSecurity();
			logger.debug("TaskListProxyDAOImpl.getInvestigationSummaryVOCollection(NBSSecurityObj) - myPAInvestigationsSecurity = "
					+ myPAInvestigationsSecurity);

			if (myPAInvestigationsSecurity != null
					&& nbssecurityObj.getPermission(NBSBOLookup.INVESTIGATION,
							myPAInvestigationsSecurity)) {

				TaskListItemVO taskListItemVOOpenInvestigations = new TaskListItemVO();
				taskListItemVOOpenInvestigations
						.setTaskListItemName(NEDSSConstants.OPEN_INVESTIGATIONS);
				try {
					ArrayList<Object> myInvestigations = (ArrayList<Object>) taskListProxyDAOImpl
							.getInvestigationSummaryVOCollection(nbssecurityObj);
					taskListItemVOOpenInvestigations
							.setTaskListItemCount(myInvestigations.size());
				} catch (NEDSSSystemException e) {
					logger.fatal(
							"TaskListProxyEJB.getTaskListItems: errro while getting open investigations count: "
									+ e.getMessage(), e);
					// throw new NEDSSSystemException(e.getMessage(),e);
				}
				ar.add(taskListItemVOOpenInvestigations);
			}
          //  }
            
            
            // NND_NOTIFICATIONS_FOR_APPROVAL
        	logger.debug("Begin execution of TaskListProxyDAOImpl.getTaskListItems NND_NOTIFICATIONS_FOR_APPROVAL");
            if(nbssecurityObj.getPermission(NBSBOLookup.NOTIFICATION, NBSOperationLookup.REVIEW))
            {
                logger.debug("NND_NOTIFICATIONS_FOR_APPROVAL - getPermission is true --- ");
                String dataAccessWhereClause = nbssecurityObj.getDataAccessWhereClause(NBSBOLookup.NOTIFICATION, NBSOperationLookup.REVIEW, DataTables.NOTIFICATION_TABLE);
                if(dataAccessWhereClause == null)
                   dataAccessWhereClause = "";
                else
                   dataAccessWhereClause = "AND " + dataAccessWhereClause;

                logger.debug("dataAccessWhereClause = " + dataAccessWhereClause);

                String aQuery;
                logger.debug("propertyUtil.getDatabaseServerType() = " + propertyUtil.getDatabaseServerType());
                logger.debug("NEDSSConstants.ORACLE_ID) = " + NEDSSConstants.ORACLE_ID);
                if (propertyUtil.getDatabaseServerType() != null && propertyUtil.getDatabaseServerType().trim().equalsIgnoreCase(NEDSSConstants.ORACLE_ID))
                {
                    logger.debug("IN THE ORACLE SELECTION = " + propertyUtil.getDatabaseServerType());
                    aQuery =  WumSqlQuery.SELECT_COUNT_OF_NOTIFICATIONS_FOR_APPROVAL_ORACLE_REVISITED + dataAccessWhereClause;
                }
                else
                {
                    logger.debug("IN THE SQL SELECTION = " + propertyUtil.getDatabaseServerType());
                    aQuery =  WumSqlQuery.SELECT_COUNT_OF_NOTIFICATIONS_FOR_APPROVAL_SQL_REVISITED + dataAccessWhereClause;
                }
                logger.debug("NND_NOTIFICATIONS_FOR_APPROVAL Query = " + aQuery);
                TaskListItemVO taskListItemVO = new TaskListItemVO();
				try {
					Integer count = taskListProxyDAOImpl.getCount(aQuery);
					logger.info("count = " + count);

					taskListItemVO
							.setTaskListItemName(NEDSSConstants.NND_NOTIFICATIONS_FOR_APPROVAL);
					if (count == null)
						taskListItemVO.setTaskListItemCount(new Integer(0));
					else
						taskListItemVO.setTaskListItemCount(count);
				} catch (NEDSSSystemException e) {
					logger.fatal(
							"TaskListProxyEJB.getTaskListItems: errro while getting NND_NOTIFICATIONS_FOR_APPROVAL count: "
									+ e.getMessage(), e);
				}

                ar.add(taskListItemVO);
                logger.info("ar = " + ar.toString());
            }// end of ---- NND_NOTIFICATIONS_FOR_APPROVAL
            logger.debug("End execution of TaskListProxyDAOImpl.getTaskListItems NND_NOTIFICATIONS_FOR_APPROVAL");

            logger.debug("Begin execution of TaskListProxyDAOImpl.getTaskListItems NND_UPDATED_NOTIFICATIONS_FOR_APPROVAL");
			// NND_UPDATED_NOTIFICATIONS_FOR_APPROVAL
			if(nbssecurityObj.getPermission(NBSBOLookup.NOTIFICATION, NBSOperationLookup.REVIEW))
			{
				logger.debug("NND_UPDATED_NOTIFICATIONS_FOR_APPROVAL - getPermission is true --- ");
				String dataAccessWhereClause = nbssecurityObj.getDataAccessWhereClause(NBSBOLookup.NOTIFICATION, NBSOperationLookup.REVIEW, DataTables.NOTIFICATION_TABLE);
				if(dataAccessWhereClause == null)
				   dataAccessWhereClause = "";
				else
				   dataAccessWhereClause = "AND " + dataAccessWhereClause;

				logger.debug("dataAccessWhereClause = " + dataAccessWhereClause);

				String aQuery;
				logger.debug("propertyUtil.getDatabaseServerType() = " + propertyUtil.getDatabaseServerType());
				logger.debug("NEDSSConstants.ORACLE_ID) = " + NEDSSConstants.ORACLE_ID);
				if (propertyUtil.getDatabaseServerType() != null && propertyUtil.getDatabaseServerType().trim().equalsIgnoreCase(NEDSSConstants.ORACLE_ID))
				{
					logger.debug("IN THE ORACLE SELECTION = " + propertyUtil.getDatabaseServerType());
					aQuery =  WumSqlQuery.SELECT_COUNT_OF_UPDATED_NOTIFICATIONS_FOR_APPROVAL_ORACLE_REVISITED + dataAccessWhereClause;
				}
				else
				{
					logger.debug("IN THE SQL SELECTION = " + propertyUtil.getDatabaseServerType());
					aQuery =  WumSqlQuery.SELECT_COUNT_OF_UPDATED_NOTIFICATIONS_FOR_APPROVAL_SQL_REVISITED+ dataAccessWhereClause;
				}
				 logger.debug("NND_UPDATED_NOTIFICATIONS_FOR_APPROVAL Query = " + aQuery);
				 TaskListItemVO taskListItemVO = new TaskListItemVO();
				try {
					Integer count = taskListProxyDAOImpl.getCount(aQuery);
					logger.info("count = " + count);

					taskListItemVO
							.setTaskListItemName(NEDSSConstants.NND_UPDATED_NOTIFICATIONS_FOR_APPROVAL);
					if (count == null)
						taskListItemVO.setTaskListItemCount(new Integer(0));
					else
						taskListItemVO.setTaskListItemCount(count);
				} catch (NEDSSSystemException e) {
					logger.fatal(
							"TaskListProxyEJB.getTaskListItems: errro while getting UPDATED_NOTIFICATIONS count: "
									+ e.getMessage(), e);
				}

				ar.add(taskListItemVO);
				logger.info("ar = " + ar.toString());
			}// end of ---- NND_UPDATED_NOTIFICATIONS_FOR_APPROVAL
			logger.debug("End execution of TaskListProxyDAOImpl.getTaskListItems NND_UPDATED_NOTIFICATIONS_FOR_APPROVAL");
			// Rejected Notifications
			
			logger.debug("Begin execution of TaskListProxyDAOImpl.getTaskListItems REJECTED_NOTIFICATIONS");
			if((nbssecurityObj.getPermission(NBSBOLookup.NOTIFICATION, NBSOperationLookup.CREATE)
					|| (nbssecurityObj.getPermission(NBSBOLookup.NOTIFICATION, NBSOperationLookup.CREATENEEDSAPPROVAL))))
			{
				logger.debug("REJECTED NOTIFICATIONS - getPermission is true --- ");
				boolean check1 = nbssecurityObj.getPermission(NBSBOLookup.NOTIFICATION, NBSOperationLookup.CREATE,
						ProgramAreaJurisdictionUtil.ANY_PROGRAM_AREA, ProgramAreaJurisdictionUtil.ANY_JURISDICTION);
				boolean check2 = nbssecurityObj.getPermission(NBSBOLookup.NOTIFICATION, NBSOperationLookup.CREATENEEDSAPPROVAL,
						ProgramAreaJurisdictionUtil.ANY_PROGRAM_AREA, ProgramAreaJurisdictionUtil.ANY_JURISDICTION);
				String dataAccessWhereClause = "";
				String dataAccessWhereClause1 = null;
				String dataAccessWhereClause2 = null;
				if(check1)
					dataAccessWhereClause1 = nbssecurityObj.getDataAccessWhereClause(NBSBOLookup.NOTIFICATION,
						NBSOperationLookup.CREATE, "Notification");
				if(check2)
					dataAccessWhereClause2 = nbssecurityObj.getDataAccessWhereClause(NBSBOLookup.NOTIFICATION,
						NBSOperationLookup.CREATENEEDSAPPROVAL, "Notification");
				//Need to get the where clause for both the permissions as user might have Create and Create Needs Approval in different PA Jurisdictions
				if(dataAccessWhereClause1 == null && dataAccessWhereClause2 == null)
				   dataAccessWhereClause = "";
				else if(dataAccessWhereClause1 == null && dataAccessWhereClause2 != null && dataAccessWhereClause2.trim().length()>0)
					dataAccessWhereClause = " AND " + dataAccessWhereClause2;
				else if(dataAccessWhereClause1 != null && dataAccessWhereClause1.trim().length()>0 && dataAccessWhereClause2 == null)
					dataAccessWhereClause = " AND " + dataAccessWhereClause1;
				else if(dataAccessWhereClause1 != null && dataAccessWhereClause1.trim().length()>0 && dataAccessWhereClause2 != null && dataAccessWhereClause2.trim().length()>0)
					dataAccessWhereClause = " AND (" + dataAccessWhereClause1 +" or "+ dataAccessWhereClause2+")";

				logger.debug("dataAccessWhereClause = " + dataAccessWhereClause);

				String aQuery;
				logger.debug("propertyUtil.getDatabaseServerType() = " + propertyUtil.getDatabaseServerType());
				logger.debug("NEDSSConstants.ORACLE_ID) = " + NEDSSConstants.ORACLE_ID);
				if (propertyUtil.getDatabaseServerType() != null && propertyUtil.getDatabaseServerType().trim().equalsIgnoreCase(NEDSSConstants.ORACLE_ID))
				{
					logger.debug("IN THE ORACLE SELECTION = " + propertyUtil.getDatabaseServerType());
					aQuery =  WumSqlQuery.SELECT_COUNT_OF_REJECTED_NOTIFICATIONS_FOR_APPROVAL_ORACLE_REVISITED 
					+ PropertyUtil.getInstance().getRejectedNotificationDaysLimit()+ dataAccessWhereClause;
				}
				else
				{
					logger.debug("IN THE SQL SELECTION = " + propertyUtil.getDatabaseServerType());
					aQuery =  WumSqlQuery.SELECT_COUNT_OF_REJECTED_NOTIFICATIONS_FOR_APPROVAL_SQL_REVISITED
					+ PropertyUtil.getInstance().getRejectedNotificationDaysLimit()+ dataAccessWhereClause;
				}
				 logger.debug("REJECTED_NOTIFICATIONS Query = " + aQuery);
				TaskListItemVO taskListItemVO = new TaskListItemVO();
				try {
					Integer count = taskListProxyDAOImpl.getCount(aQuery);
					logger.info("count = " + count);
					taskListItemVO
							.setTaskListItemName(NEDSSConstants.NND_REJECTED_NOTIFICATIONS_FOR_APPROVAL);
					if (count == null)
						taskListItemVO.setTaskListItemCount(new Integer(0));
					else
						taskListItemVO.setTaskListItemCount(count);
				} catch (NEDSSSystemException e) {
					logger.fatal(
							"TaskListProxyEJB.getTaskListItems: errro while getting REJECTED_NOTIFICATIONS count: "
									+ e.getMessage(), e);
				}

				ar.add(taskListItemVO);
				logger.info("ar = " + ar.toString());
			}// end of ---- REJECTED_NOTIFICATIONS_FOR_APPROVAL
			logger.debug("End execution of TaskListProxyDAOImpl.getTaskListItems REJECTED_NOTIFICATIONS");

            // Observations Needing Program or Jurisdiction Assignment
			logger.debug("Begin execution of TaskListProxyDAOImpl.getTaskListItems OBS_NEEDING_PROG_JUR_ASSIGNMENT");
            if(nbssecurityObj.getPermission(NBSBOLookup.OBSERVATIONLABREPORT, NBSOperationLookup.ASSIGNSECURITY,ProgramAreaJurisdictionUtil.ANY_PROGRAM_AREA,ProgramAreaJurisdictionUtil.ANY_JURISDICTION)||
              nbssecurityObj.getPermission(NBSBOLookup.OBSERVATIONMORBIDITYREPORT, NBSOperationLookup.ASSIGNSECURITY,ProgramAreaJurisdictionUtil.ANY_PROGRAM_AREA,ProgramAreaJurisdictionUtil.ANY_JURISDICTION ))
            {
            boolean flag1 = false;
            boolean flag2 = false;
            boolean flag3 = false;
            String aQuery="";
            String aQueryDoc="";
            flag1 = nbssecurityObj.getPermission(NBSBOLookup.OBSERVATIONLABREPORT, NBSOperationLookup.ASSIGNSECURITY,ProgramAreaJurisdictionUtil.ANY_PROGRAM_AREA,ProgramAreaJurisdictionUtil.ANY_JURISDICTION);
            flag2 = nbssecurityObj.getPermission(NBSBOLookup.OBSERVATIONMORBIDITYREPORT, NBSOperationLookup.ASSIGNSECURITY,ProgramAreaJurisdictionUtil.ANY_PROGRAM_AREA,ProgramAreaJurisdictionUtil.ANY_JURISDICTION);
            flag3 = nbssecurityObj.getPermission(NBSBOLookup.DOCUMENT, NBSOperationLookup.ASSIGNSECURITY,ProgramAreaJurisdictionUtil.ANY_PROGRAM_AREA,ProgramAreaJurisdictionUtil.ANY_JURISDICTION);
            if(flag1 && flag2)
            {
            	if (propertyUtil.getDatabaseServerType() != null && propertyUtil.getDatabaseServerType().trim().equalsIgnoreCase(NEDSSConstants.ORACLE_ID)){
            		aQuery = "select /*+ USE_CONCAT */ count(*) "+
            		"from Observation  "+
            		"where ((Observation.ctrl_cd_display_form = '" +NEDSSConstants.LAB_REPORT + "' "+
            		"and Observation.obs_domain_cd_st_1 = 'Order') "+
            		"or Observation.ctrl_cd_display_form = '"+NEDSSConstants.MOB_CTRLCD_DISPLAY + "') "+
            		"and (((Observation.prog_area_cd is null) "+
            		"or (Observation.prog_area_cd = '')) "+
            		"or ((Observation.jurisdiction_cd is null) "+
            		"or (Observation.jurisdiction_cd = ''))) ";
				}
            	else{
	            	aQuery = "select " +
	                  "count(*) " +
	                  "from " + DataTables.OBSERVATION_TABLE + " " +
	                  "where " +
	                  "((" + DataTables.OBSERVATION_TABLE + ".ctrl_cd_display_form = '" +
	                  NEDSSConstants.LAB_REPORT + "' and "+DataTables.OBSERVATION_TABLE+".obs_domain_cd_st_1 = 'Order')" +
	                  "or " + DataTables.OBSERVATION_TABLE + ".ctrl_cd_display_form = '" +
	                  NEDSSConstants.MOB_CTRLCD_DISPLAY + "') " +
	                  "and (( ( " + DataTables.OBSERVATION_TABLE +
	                  ".prog_area_cd is null) " +
	                  "or ( " + DataTables.OBSERVATION_TABLE + ".prog_area_cd = '')) " +
	                  "or (( " + DataTables.OBSERVATION_TABLE +
	                  ".jurisdiction_cd is null) " +
	                  "or ( " + DataTables.OBSERVATION_TABLE +
	                  ".jurisdiction_cd = ''))) ";
            	}


            }
            else if(flag1 && !flag2)
            	if (propertyUtil.getDatabaseServerType() != null && propertyUtil.getDatabaseServerType().trim().equalsIgnoreCase(NEDSSConstants.ORACLE_ID)){
            		
            		aQuery = "select /*+ USE_CONCAT */ count(*) "+
            		"from Observation  "+
            		"where ((Observation.ctrl_cd_display_form = '" +NEDSSConstants.LAB_REPORT + "' "+
            		"and Observation.obs_domain_cd_st_1 = 'Order')) "+
            		"and (((Observation.prog_area_cd is null) "+
            		"or (Observation.prog_area_cd = '')) "+
            		"or ((Observation.jurisdiction_cd is null) "+
            		"or (Observation.jurisdiction_cd = ''))) ";

            	}
            	else{
					  aQuery = "select " +
					  "count(*) " +
					  "from " + DataTables.OBSERVATION_TABLE + " " +
					  "where " +
					  "("+ DataTables.OBSERVATION_TABLE + ".ctrl_cd_display_form = '" +
					   NEDSSConstants.LAB_REPORT + "' and "+DataTables.OBSERVATION_TABLE+".obs_domain_cd_st_1 = 'Order')" + 
					  "and (( ( " + DataTables.OBSERVATION_TABLE +
					  ".prog_area_cd is null) " +
					  "or ( " + DataTables.OBSERVATION_TABLE + ".prog_area_cd = '')) " +
					  "or (( " + DataTables.OBSERVATION_TABLE +
					  ".jurisdiction_cd is null) " +
					  "or ( " + DataTables.OBSERVATION_TABLE + ".jurisdiction_cd = ''))) ";
            	}
           else if(!flag1 && flag2)
           	if (propertyUtil.getDatabaseServerType() != null && propertyUtil.getDatabaseServerType().trim().equalsIgnoreCase(NEDSSConstants.ORACLE_ID)){
        		aQuery = "select /*+ USE_CONCAT */ count(*) "+
        		"from Observation  "+
        		"where (Observation.ctrl_cd_display_form = '"+NEDSSConstants.MOB_CTRLCD_DISPLAY + "') "+
        		"and (((Observation.prog_area_cd is null) "+
        		"or (Observation.prog_area_cd = '')) "+
        		"or ((Observation.jurisdiction_cd is null) "+
        		"or (Observation.jurisdiction_cd = ''))) ";
			}
           	else{
				  aQuery = "select " +
				  "count(*) " +
				  "from " + DataTables.OBSERVATION_TABLE + " " +
				  "where " +
				  "("+ DataTables.OBSERVATION_TABLE + ".ctrl_cd_display_form = '" +
				   NEDSSConstants.MOB_CTRLCD_DISPLAY + "' and "+DataTables.OBSERVATION_TABLE+".obs_domain_cd_st_1 = 'Order')" + 
				  "and (( ( " + DataTables.OBSERVATION_TABLE +
				  ".prog_area_cd is null) " +
				  "or ( " + DataTables.OBSERVATION_TABLE + ".prog_area_cd = '')) " +
				  "or (( " + DataTables.OBSERVATION_TABLE +
				  ".jurisdiction_cd is null) " +
				  "or ( " + DataTables.OBSERVATION_TABLE + ".jurisdiction_cd = ''))) ";
      	}
            // For PHCR Documents needing security Assignment
           	if(flag3)
           {
               	if (propertyUtil.getDatabaseServerType() != null && propertyUtil.getDatabaseServerType().trim().equalsIgnoreCase(NEDSSConstants.ORACLE_ID)){
                    aQueryDoc = 	"select /*+ USE_CONCAT */ count(*) from nbs_document where " + 
              		"(( ( nbs_document.prog_area_cd is null) or ( nbs_document.prog_area_cd = '')) or " +
              		" (( nbs_document.jurisdiction_cd is null) or ( nbs_document.jurisdiction_cd = ''))) and nbs_document.doc_type_cd ='"+ NEDSSConstants.PHC_236 +"'"+
              		" and nbs_document.record_status_cd='UNPROCESSED'";
               	}
               	else{
               		aQueryDoc = 	"select count(*) from nbs_document where " + 
              		"(( ( nbs_document.prog_area_cd is null) or ( nbs_document.prog_area_cd = '')) or " +
              		" (( nbs_document.jurisdiction_cd is null) or ( nbs_document.jurisdiction_cd = ''))) and nbs_document.doc_type_cd ='"+ NEDSSConstants.PHC_236 +"'"+
              		" and nbs_document.record_status_cd='UNPROCESSED'";

               	}
               	
           }

            /*else if(!flag1 && !flag2 && !flag3)
            {
               throw new SecurityException("Expected the user to have view lab, morb report and View Document permissions.");
            }*/
            logger.debug("OBS_NEEDING_PROG_JUR_ASSIGNMENT Query = " + aQuery);
            Integer count = new Integer(0);
            Integer countObs = new Integer(0);
            Integer countDoc = new Integer(0);
            TaskListItemVO taskListItemVO = new TaskListItemVO();
            try{
					if (!flag3 && aQuery != null && !aQuery.isEmpty())
						count = taskListProxyDAOImpl.getCount(aQuery);
					else {
						if (aQueryDoc != null && !aQueryDoc.isEmpty())
							countDoc = taskListProxyDAOImpl.getCount(aQueryDoc);
						if (aQuery != null && !aQuery.isEmpty())
							countObs = taskListProxyDAOImpl.getCount(aQuery);

						count = new Integer(countObs.intValue()
								+ countDoc.intValue());
					}
              taskListItemVO.setTaskListItemName(NEDSSConstants.
                  ELRS_NEEDING_PROGRAM_OR_JURISDICTION_ASSIGNMENT);
              if (count == null)
                taskListItemVO.setTaskListItemCount(new Integer(0));
              else
                taskListItemVO.setTaskListItemCount(count);
            } catch (NEDSSSystemException e) {
				logger.fatal(
						"TaskListProxyEJB.getTaskListItems: errro while getting DOC_NEEDING_PROG_JUR_ASSIGNMENT count: "
								+ e.getMessage(), e);
			}
              ar.add(taskListItemVO);
          } //end of Observation Needing Assignment
            logger.debug("End execution of TaskListProxyDAOImpl.getTaskListItems OBS_NEEDING_PROG_JUR_ASSIGNMENT");
            // Observation Needing Review starts 
            logger.debug("Begin execution of TaskListProxyDAOImpl.getTaskListItems OBS_NEEDING_REVIEW");
            if(nbssecurityObj.getPermission(NBSBOLookup.OBSERVATIONLABREPORT, obsNeedingReviewSecurity)
                || (nbssecurityObj.getPermission(NBSBOLookup.OBSERVATIONMORBIDITYREPORT, obsNeedingReviewSecurity)
                )||(nbssecurityObj.getPermission(NBSBOLookup.DOCUMENT,"VIEW")))
            {
             	 
              boolean flag1;
              boolean flag2;
              boolean flag3;
               flag1 = nbssecurityObj.getPermission(NBSBOLookup.OBSERVATIONLABREPORT,obsNeedingReviewSecurity);
               flag2 = nbssecurityObj.getPermission(NBSBOLookup.OBSERVATIONMORBIDITYREPORT,obsNeedingReviewSecurity);
               flag3= nbssecurityObj.getPermission(NBSBOLookup.DOCUMENT,"VIEW"); 
              String aQuery = "";
             
              String dataAccessWhereClauseLab = nbssecurityObj.
                  getDataAccessWhereClause(NBSBOLookup.OBSERVATIONLABREPORT,
                		  					obsNeedingReviewSecurity,
                		  					DataTables.OBSERVATION_TABLE);
              if (dataAccessWhereClauseLab == null)
                dataAccessWhereClauseLab = "";
              else
                dataAccessWhereClauseLab = " AND " + dataAccessWhereClauseLab;
              String dataAccessWhereClauseMorb = nbssecurityObj.
                  getDataAccessWhereClause(NBSBOLookup.OBSERVATIONMORBIDITYREPORT,
                		  					obsNeedingReviewSecurity,
                		  					DataTables.OBSERVATION_TABLE);
              if (dataAccessWhereClauseMorb == null)
                dataAccessWhereClauseMorb = "";
              else
                dataAccessWhereClauseMorb = " AND " + dataAccessWhereClauseMorb;
              String dataAccessWhereClauseDoc = nbssecurityObj.
              getDataAccessWhereClause(NBSBOLookup.DOCUMENT,
            		  					"VIEW",
            		  					DataTables.NBS_DOCUMENT_TABLE);
            		  					
              if (dataAccessWhereClauseDoc == null)
            	  dataAccessWhereClauseDoc = "";
              else
            	  dataAccessWhereClauseDoc = " AND " + dataAccessWhereClauseDoc;
           
              String aQueryDoc="";
              
              if (flag1 && !flag2) {
                 	if (propertyUtil.getDatabaseServerType() != null && propertyUtil.getDatabaseServerType().trim().equalsIgnoreCase(NEDSSConstants.ORACLE_ID)){
                 		aQuery = "select /*+ INDEX(OBSERVATION) */ count(*) "+
                 		"from Observation observation  "+
                 		"where (observation.record_status_cd = '"+NEDSSConstants.OBSERVATION_RECORD_STATUS_CD + "' " +
                 		"OR record_status_cd = '"+NEDSSConstants.OBS_UNPROCESSED_PREV_D + "') " +
                 		"and observation.ctrl_cd_display_form = '"+NEDSSConstants.LAB_CTRLCD_DISPLAY + "' " +
                 		dataAccessWhereClauseLab;
                 	}
                 	else{
		                aQuery = "select " +
	                    "count(*) " +
	                    "from " + DataTables.OBSERVATION_TABLE + " observation " +
	                    "where " +
	                    "(observation.record_status_cd = '" +
	                    NEDSSConstants.OBSERVATION_RECORD_STATUS_CD + "'" +
	                    "OR record_status_cd = '" +
	                    NEDSSConstants.OBS_UNPROCESSED_PREV_D + "') " +
	                    "and observation.ctrl_cd_display_form = '" +
	                    NEDSSConstants.LAB_CTRLCD_DISPLAY + "' " +
	                    dataAccessWhereClauseLab;
                 	}
                
              }
              else if (!flag1 && flag2) {
            	  if (propertyUtil.getDatabaseServerType() != null && propertyUtil.getDatabaseServerType().trim().equalsIgnoreCase(NEDSSConstants.ORACLE_ID)){
               		aQuery = "select /*+ INDEX(OBSERVATION) */ count(*) "+
               		"from Observation observation  "+
               		"where (observation.record_status_cd = '"+NEDSSConstants.OBSERVATION_RECORD_STATUS_CD + "' " +
               		"OR record_status_cd = '"+NEDSSConstants.OBS_UNPROCESSED_PREV_D + "') " +
               		"and observation.ctrl_cd_display_form = '"+NEDSSConstants.MOB_CTRLCD_DISPLAY + "' " +
               		dataAccessWhereClauseMorb;
               	}
               	else{
                aQuery = "select " +
                    "count(*) " +
                    "from " + DataTables.OBSERVATION_TABLE + " observation " +
                    "where " +
                    "(observation.record_status_cd = '" +
                    NEDSSConstants.OBSERVATION_RECORD_STATUS_CD + "'" +
                    "OR record_status_cd = '" +
                    NEDSSConstants.OBS_UNPROCESSED_PREV_D + "') " +
                    "and observation.ctrl_cd_display_form = '" +
                    NEDSSConstants.MOB_CTRLCD_DISPLAY + "' " +
                    dataAccessWhereClauseMorb;
               	}

              }
              else if (flag1 && flag2) {
            	  if (propertyUtil.getDatabaseServerType() != null && propertyUtil.getDatabaseServerType().trim().equalsIgnoreCase(NEDSSConstants.ORACLE_ID)){
                 		aQuery = "select /*+ INDEX(OBSERVATION) */ count(*) "+
                 		"from Observation observation  "+
                 		"where (observation.record_status_cd = '"+NEDSSConstants.OBSERVATION_RECORD_STATUS_CD + "' " +
                 		"OR record_status_cd = '"+NEDSSConstants.OBS_UNPROCESSED_PREV_D + "') " +
                 		"and ((observation.ctrl_cd_display_form = '"+NEDSSConstants.LAB_CTRLCD_DISPLAY + "' " +
                 		dataAccessWhereClauseLab + ") "+
                 		"or (observation.ctrl_cd_display_form = '"+NEDSSConstants.MOB_CTRLCD_DISPLAY + "' " +
                 		dataAccessWhereClauseMorb + "))";
                 	}
                 	else{
                 		aQuery = "select " +
	                    "count(*) " +
	                    "from " + DataTables.OBSERVATION_TABLE + " observation " +
	                    "where " +
	                    "(observation.record_status_cd = '" +
	                    NEDSSConstants.OBSERVATION_RECORD_STATUS_CD + "'" +
	                    "OR record_status_cd = '" +
	                    NEDSSConstants.OBS_UNPROCESSED_PREV_D + "') " + " and " +
	                    "((observation.ctrl_cd_display_form = '" +
	                    NEDSSConstants.LAB_CTRLCD_DISPLAY + "'" +
	                    dataAccessWhereClauseLab + ")" +
	                    " or " +
	                    "(observation.ctrl_cd_display_form = '" +
	                    NEDSSConstants.MOB_CTRLCD_DISPLAY + "'" +
	                    dataAccessWhereClauseMorb + "))";
                 	}
                
                    

              }
              // Include Document Query
              if(flag3) {
            	  if (propertyUtil.getDatabaseServerType() != null &&
    				      propertyUtil.getDatabaseServerType().equalsIgnoreCase(NEDSSConstants.ORACLE_ID)){
		            	  aQueryDoc ="select /*+ INDEX(NBS_DOCUMENT) */ count(*) from nbs_document WHERE "+  
		            	  " (nbs_document.record_status_cd = 'UNPROCESSED'  OR nbs_document.record_status_cd = '" + NEDSSConstants.OBS_UNPROCESSED_PREV_D + "' " +") "+ 
		            	  " and nbs_document.doc_type_cd ='"+ NEDSSConstants.PHC_236 +"' "+ dataAccessWhereClauseDoc;
            	  }else 
            	  {
                	  aQueryDoc ="select count(*) from nbs_document WHERE "+  
	            	  " (nbs_document.record_status_cd = 'UNPROCESSED'  OR nbs_document.record_status_cd = '" + NEDSSConstants.OBS_UNPROCESSED_PREV_D + "' " +") "+ 
	            	  " and nbs_document.doc_type_cd ='"+ NEDSSConstants.PHC_236 +"' "+ dataAccessWhereClauseDoc;
            	  }

              }
           
/*              else if (!flag1 && !flag2 && !flag3)
              {
                throw new SecurityException("Expected the user to have view lab Report, morb report and Document permissions.");
              }*/
              Integer countDoc= new Integer(0);
              Integer countObs=new Integer(0);
              Integer count=new Integer(0);
              TaskListItemVO taskListItemVO = new TaskListItemVO();
				try {
					if (propertyUtil.getDatabaseServerType() != null) {
						logger.debug("OBS_NEEDING_REVIEW Query = " + aQuery);
						if (!flag3 && aQuery != null && !aQuery.isEmpty())
							count = taskListProxyDAOImpl.getCount(aQuery);
						else {
							if (aQueryDoc != null && !aQueryDoc.isEmpty())
								countDoc = taskListProxyDAOImpl
										.getCount(aQueryDoc);
							if (aQuery != null && !aQuery.isEmpty())
								countObs = taskListProxyDAOImpl
										.getCount(aQuery);

							count = new Integer(countObs.intValue()
									+ countDoc.intValue());
						}
						logger.info("count = " + count);
						taskListItemVO
								.setTaskListItemName(NEDSSConstants.NEW_LAB_REPORTS_FOR_REVIEW);
						if (count == null)
							taskListItemVO.setTaskListItemCount(new Integer(0));
						else
							taskListItemVO.setTaskListItemCount(count);
					}
				} catch (NEDSSSystemException e) {
					logger.fatal(
							"TaskListProxyEJB.getTaskListItems: errro while getting DOC_NEEDING_REVIEW count: "
									+ e.getMessage(), e);
				}

                ar.add(taskListItemVO);
                logger.info("ar = " + ar.toString());
            }// end of --- New Lab Reports For Review
            logger.debug("End execution of TaskListProxyDAOImpl.getTaskListItems OBS_NEEDING_REVIEW");

            if (nbssecurityObj.getPermission(NBSBOLookup.QUEUES, NBSOperationLookup.MESSAGE))
            { 
                // Message_Log Queue.
                MessageLogDAOImpl messageLogDao = new MessageLogDAOImpl();
                MessageLogDT messageLogDt = new MessageLogDT();
                Long id = nbssecurityObj.getTheUserProfile().getTheUser().getProviderUid(); 
                messageLogDt.setAssignedToUid(id);
                TaskListItemVO taskListItemVO = new TaskListItemVO();
                taskListItemVO.setTaskListItemName(NEDSSConstants.MESSAGE_QUEUE);
                taskListItemVO.setTaskListItemCount(messageLogDao.getCount(messageLogDt, nbssecurityObj));
                ar.add(taskListItemVO);
            }
            
            if (nbssecurityObj.getPermission(NBSBOLookup.QUEUES, NBSOperationLookup.SUPERVISORREVIEW))
            {
                // Supervisor Review Queue 
                TaskListItemVO supervisor = new TaskListItemVO();
                supervisor.setTaskListItemName(NEDSSConstants.SUPERVISOR_REVIEW_QUEUE);
                PublicHealthCaseDAOImpl dao = new PublicHealthCaseDAOImpl();
                supervisor.setTaskListItemCount(dao.getPublicHealthCasesBySupervisorCount(nbssecurityObj) );
                ar.add(supervisor); 
            }
        }
        catch(NEDSSSystemException nsex)
        {
            logger.fatal("Error in getTaskListItems "  +  nsex.getMessage(), nsex);
            throw new Exception(nsex.toString());
        }
        logger.info("Returning ar = " + ar.size() );
        logger.debug("End execution of TaskListProxyDAOImpl.getTaskListItems method");
        return ar;
    } // end getTaskListItems ---------------------------------
}
