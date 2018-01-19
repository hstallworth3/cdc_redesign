package gov.cdc.nedss.proxy.ejb.investigationproxyejb.dao;

import gov.cdc.nedss.exception.NEDSSSystemException;
import gov.cdc.nedss.proxy.ejb.observationproxyejb.vo.ProviderDataForPrintVO;
import gov.cdc.nedss.proxy.util.EntityProxyHelper;
import gov.cdc.nedss.util.DAOBase;
import gov.cdc.nedss.util.LogUtils;
import gov.cdc.nedss.util.NEDSSConstants;
import gov.cdc.nedss.util.PropertyUtil;
import gov.cdc.nedss.util.UidSummaryVO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class ObservationSummaryDAOImpl
    extends DAOBase {

  public ObservationSummaryDAOImpl() {
  }

  private static final LogUtils logger = new LogUtils(ObservationSummaryDAOImpl.class.getName());
  private static PropertyUtil propertyUtil= PropertyUtil.getInstance();


  private String prepareSQLForContainsIn(Collection<Object> uidList, String query,
                                         String whereClause) {
    Long uid = null;
    StringBuffer sf = new StringBuffer();
    try{
	    sf.append("('");
	    Iterator<Object>  itor = uidList.iterator();
	    while (itor.hasNext()) {
	      uid = (Long) itor.next();
	      sf.append(uid.toString());
	      if (itor.hasNext()) {
	        sf.append("', '");
	      }
	    }
	    sf.append("')");
	
	    logger.debug(query + sf.toString() + whereClause);
    }catch(Exception ex){
    	logger.error("Exception ="+ex.getMessage(), ex);
    	throw new NEDSSSystemException(ex.toString(), ex);
    }
    return query + sf.toString() + whereClause;
  }


  @SuppressWarnings("unchecked")
public Collection<Object>  findAllLabReportUidListForManage(Long subjectUID, String LABREPORT_WHERECLAUSE)
  {
    Collection<Object>  uidSummaryVOCollection  = new ArrayList<Object> ();
    UidSummaryVO uidSummaryVO = new UidSummaryVO();
    try{
      EntityProxyHelper entityProxyHelper = EntityProxyHelper.getInstance();
      Collection<Object>  uidList = entityProxyHelper.findActivePatientUidsByParentUid(subjectUID);

      String aQuery = "SELECT "+
                "part.act_uid  \"uid\" "+
                "FROM Participation part, Observation obs "+
                "WHERE part.act_class_cd = '" + NEDSSConstants.OBSERVATION_CLASS_CODE + "' " +

                "AND part.subject_class_cd = '" + NEDSSConstants.PERSON_CLASS_CODE + "' " +
                "AND part.type_cd = '" + NEDSSConstants.PAR110_TYP_CD + "' " +
                "AND part.record_status_cd = '" + NEDSSConstants.RECORD_STATUS_ACTIVE + "' " +
                "AND part.act_uid = obs.observation_uid " +
                "AND obs.record_status_cd IN ('" + NEDSSConstants.OBS_PROCESSED + "', '" + NEDSSConstants.OBS_UNPROCESSED + "')" +
                "AND part.subject_entity_uid IN ";
      aQuery = this.prepareSQLForContainsIn(uidList, aQuery, LABREPORT_WHERECLAUSE);
      logger.debug("aQuery:  " + aQuery);

      uidSummaryVOCollection  = (ArrayList<Object> )super.preparedStmtMethod(uidSummaryVO, null, aQuery, NEDSSConstants.SELECT);
    }catch(Exception ex){
    	logger.error("Exception ="+ex.getMessage(),ex);
    	throw new NEDSSSystemException(ex.toString(), ex);
    }
      return uidSummaryVOCollection;

  }
  
  
	@SuppressWarnings("unchecked")
	public Map<Object, Object> findObservationAssociationWithPHC(Long phcUID, String actTypeCd) {
		Map<Object,Object> uidSummaryVOMap = new HashMap<Object, Object>();
		UidSummaryVO uidSummaryVO = new UidSummaryVO();

		try{
			ArrayList<Object> params = new ArrayList<Object>();
			params.add(phcUID);
			String aQuery = "SELECT act.source_act_uid  \"uid\" "
					+ "FROM act_relationship act "
					+"WHERE act.source_class_cd = '"
					+ NEDSSConstants.OBSERVATION_CLASS_CODE + "' " 
					+"AND act.target_class_cd = '" + NEDSSConstants.CASE + "' "
					+"AND act.type_cd = '" + actTypeCd + "' "
					+"AND target_act_uid = ? ";
			logger.debug("aQuery:  " + aQuery);
	
			uidSummaryVOMap = (Map<Object, Object>) super.preparedStmtMethodForMap(
					uidSummaryVO, params, aQuery, NEDSSConstants.SELECT,"getUid");
		}catch(Exception ex){
			logger.error("Exception ="+ex.getMessage(),ex);
			throw new NEDSSSystemException(ex.toString(), ex);
		}
		return uidSummaryVOMap;

	}

  @SuppressWarnings("unchecked")
public Collection<Object>  findAllActiveLabReportUidListForManage(Long investigationUid, String whereClause)
  {

    UidSummaryVO uidSummaryVO = new UidSummaryVO();
    Collection<Object>  uidSummaryVOCollection  = new ArrayList<Object> ();
    try{
		String NVL_FUNC;
		if (propertyUtil.getDatabaseServerType() != null &&
		propertyUtil.getDatabaseServerType().equalsIgnoreCase(NEDSSConstants.ORACLE_ID)){
			NVL_FUNC = "NVL";
		}else{
			NVL_FUNC = "ISNULL";
		}
		String aQuery = "SELECT "+
                "ar.source_act_uid \"uid\", "+
                NVL_FUNC +
                "(ar.from_time,obs.add_time ) \"addTime\", "+//Using addTime attribute from UidSummaryVO to collect the act_relationship.from_time
                "ar.add_reason_cd \"addReasonCd\" "+
                "FROM observation obs, Act_Relationship ar "+
                "WHERE ar.target_class_cd = '" + NEDSSConstants.PUBLIC_HEALTH_CASE_CLASS_CODE + "' " +
                "AND ar.source_class_cd = '" + NEDSSConstants.OBSERVATION_CLASS_CODE + "' " +
                "AND ar.type_cd = '" + NEDSSConstants.LAB_REPORT + "' " +
                "AND ar.record_status_cd = '" + NEDSSConstants.RECORD_STATUS_ACTIVE + "' " +
                "AND ar.target_act_uid = " + investigationUid + " " +
                "AND ar.source_act_uid = obs.observation_uid " + whereClause;

		logger.debug("aQuery:  " + aQuery);

		uidSummaryVOCollection  = (ArrayList<Object> )super.preparedStmtMethod(uidSummaryVO, null, aQuery, NEDSSConstants.SELECT);
    }catch(Exception ex){
    	logger.error("Exception ="+ex.getMessage(),ex);
    	throw new NEDSSSystemException(ex.toString(), ex);
    }
      return uidSummaryVOCollection;
  }


  @SuppressWarnings("unchecked")
public Collection<Object>  findAllMorbReportUidListForManage(Long subjectUID, String MORBREPORT_WHERECLAUSE)
  {
    UidSummaryVO uidSummaryVO = new UidSummaryVO();
    Collection<Object>  uidSummaryVOCollection  = new ArrayList<Object> ();

    try{
    EntityProxyHelper entityProxyHelper = EntityProxyHelper.getInstance();
    Collection<Object>  uidList = entityProxyHelper.findActivePatientUidsByParentUid(subjectUID);

      String aQuery = "SELECT "+
                "part.act_uid \"uid\" "+
                "FROM Participation part, Observation obs "+
                "WHERE part.act_class_cd = '" + NEDSSConstants.OBSERVATION_CLASS_CODE + "' " +
                "AND part.subject_class_cd = '" + NEDSSConstants.PERSON_CLASS_CODE + "' " +
                "AND part.type_cd = '" + NEDSSConstants.MOB_SUBJECT_OF_MORB_REPORT + "' " +
                "AND part.record_status_cd = '" + NEDSSConstants.RECORD_STATUS_ACTIVE + "' " +
                "AND part.act_uid = obs.observation_uid " +
                "AND obs.record_status_cd IN ('" + NEDSSConstants.OBS_PROCESSED + "' " + ",'" + NEDSSConstants.OBS_UNPROCESSED + "') " +
                "AND part.subject_entity_uid IN ";
      aQuery = this.prepareSQLForContainsIn(uidList, aQuery, MORBREPORT_WHERECLAUSE);
      logger.debug("aQuery:  " + aQuery);
      uidSummaryVOCollection  =  (ArrayList<Object> )super.preparedStmtMethod(uidSummaryVO, null, aQuery, NEDSSConstants.SELECT);
    }catch(Exception ex){
    	logger.error("Exception ="+ex.getMessage(),ex);
    	throw new NEDSSSystemException(ex.toString(), ex);
    }
      return uidSummaryVOCollection;

  }

  @SuppressWarnings("unchecked")
public Collection<Object>  assocMorbReportUidListForManage(Long observationUID,  Long investigationUID,  String whereClause)
  {

    Collection<Object>  uidSummaryVOCollection  = new ArrayList<Object> ();
    UidSummaryVO uidSummaryVO = new UidSummaryVO();

    try{
      String aQuery = "SELECT "+
                "ar.source_act_uid \"uid\", " +
                "ar.from_time \"addTime\", "+//Using addTime attribute from UidSummaryVO to collect the act_relationship.from_time
                "ar.add_reason_cd \"addReasonCd\" "+
                "FROM Act_Relationship ar, Observation obs  "+
                "WHERE ar.target_class_cd = '" + NEDSSConstants.PUBLIC_HEALTH_CASE_CLASS_CODE + "' " +
                "AND ar.source_class_cd = '" + NEDSSConstants.OBSERVATION_CLASS_CODE + "' " +
                "AND ar.type_cd = '" + NEDSSConstants.MORBIDITY_REPORT + "' " +
                "AND ar.record_status_cd = '" + NEDSSConstants.RECORD_STATUS_ACTIVE + "' " +
                "AND ar.target_act_uid != " + investigationUID + " " +
                "AND ar.source_act_uid = " + observationUID + " " +
                "AND ar.source_act_uid = obs.observation_uid " + whereClause;

      logger.debug("aQuery:  " + aQuery);

      uidSummaryVOCollection  = (ArrayList<Object> )super.preparedStmtMethod(uidSummaryVO, null, aQuery, NEDSSConstants.SELECT);
    }catch(Exception ex){
    	logger.error("Exception ="+ex.getMessage(),ex);
    	throw new NEDSSSystemException(ex.toString(), ex);
    }
      return uidSummaryVOCollection;
  }

  @SuppressWarnings("unchecked")
public Collection<Object>  findAllActiveMorbReportUidListForManage(Long investigationUID, String whereClause)
  {

    Collection<Object>  uidSummaryVOCollection  = new ArrayList<Object> ();
    UidSummaryVO uidSummaryVO = new UidSummaryVO();

    try{
      String aQuery = "SELECT "+
                "ar.source_act_uid \"uid\", " +
                "ar.from_time \"addTime\", "+//Using addTime attribute from UidSummaryVO to collect the act_relationship.from_time
                "ar.add_reason_cd \"addReasonCd\" "+
                "FROM Act_Relationship ar, Observation obs  "+
                "WHERE ar.target_class_cd = '" + NEDSSConstants.PUBLIC_HEALTH_CASE_CLASS_CODE + "' " +
                "AND ar.source_class_cd = '" + NEDSSConstants.OBSERVATION_CLASS_CODE + "' " +
                "AND ar.type_cd = '" + NEDSSConstants.MORBIDITY_REPORT + "' " +
                "AND ar.record_status_cd = '" + NEDSSConstants.RECORD_STATUS_ACTIVE + "' " +
                "AND ar.target_act_uid = " + investigationUID + " " +
                "AND ar.source_act_uid = obs.observation_uid " + whereClause;

      logger.debug("aQuery:  " + aQuery);

      uidSummaryVOCollection  = (ArrayList<Object> )super.preparedStmtMethod(uidSummaryVO, null, aQuery, NEDSSConstants.SELECT);
    }catch(Exception ex){
    	logger.error("Exception ="+ex.getMessage(),ex);
    	throw new NEDSSSystemException(ex.toString(), ex);
    }
      return uidSummaryVOCollection;
  }

  public ArrayList<Object>  getPatientPersonInfo(Long observationUID) throws NEDSSSystemException
  {

    ArrayList<Object> vals= new ArrayList<Object> ();
    String QUICK_FIND_PATIENT = "SELECT person_name.last_nm \"lastNm\", " +
    "person_name.first_nm \"firstNm\", observation.ctrl_cd_display_form \"ctrlCdDisplayForm\", " +
    "person.person_parent_uid \"personParentUid\" " +
    "FROM person, person_name, observation, participation " +
    "WHERE person.person_uid = participation.subject_entity_uid " +
    "AND participation.act_uid = observation.observation_uid " +
    "AND participation.type_cd = \'PATSBJ\' " +
    "AND person.person_uid = person_name.person_uid " +
    "AND person_name.nm_use_cd = \'L\' " +
    "AND observation.observation_uid = ? " ;

             /**
             * Get the patientInfo
             */
            Connection dbConnection = null;
            PreparedStatement preparedStmt = null;
            ResultSet resultSet = null;

            try
            {
                dbConnection = getConnection();
                preparedStmt = dbConnection.prepareStatement(QUICK_FIND_PATIENT);
                preparedStmt.setLong(1, observationUID.longValue());
                resultSet = preparedStmt.executeQuery();
                logger.debug("get resultSet " + resultSet.toString());
                while(resultSet.next()){
                  vals.add(0, resultSet.getString("firstNm"));
                  vals.add(1, resultSet.getString("lastNm"));
                  vals.add(2, resultSet.getString("ctrlCdDisplayForm"));
                  vals.add(3, new Long(resultSet.getString("personParentUid")));
                }
              }
              catch(SQLException se)
              {
                logger.fatal("Error: SQLException while getting observations for a person in workup", se);
                throw new NEDSSSystemException( se.getMessage());
              }
              catch(Exception ex)
              {
                  logger.fatal("Error while getting observations for a person in workup", ex);
                  throw new NEDSSSystemException(ex.toString());
              }
              finally
              {
                  closeResultSet(resultSet);
                  closeStatement(preparedStmt);
                  releaseConnection(dbConnection);
              }

      return vals;
  }
  /*
   * This method will return the Ordering Provider details like lastName,FirstName
   * @Param = ObservationUID
   * @returns ArrayList
   */
  public ArrayList<Object>  getProviderInfo(Long observationUID,String partTypeCd) throws NEDSSSystemException
  {
	  ArrayList<Object> orderProviderInfo= new ArrayList<Object> ();
	  //per Pete Varnell - use optimizer hint on select
	  String ORD_PROVIDER_ORACLE = "SELECT /*+ USE_NL(PARTICIPATION,OBSERVATION,PERSON_NAME) */ ";
	  String ORD_PROVIDER_MSQL = "SELECT ";
	  String ORD_PROVIDER = "person_name.person_uid \"providerUid\",person_name.last_nm \"lastNm\", person_name.nm_degree \"degree\", " +
	  		"person_name.first_nm \"firstNm\" , person_name.nm_prefix \"prefix\" ,  person_name.nm_suffix \"suffix\" " +
	  		"FROM person_name, observation, " +
	  		"participation WHERE person_name.person_uid = participation.subject_entity_uid " +
	  		"AND participation.act_uid = observation.observation_uid " +
	  		"AND participation.type_cd = ? AND participation.subject_class_cd='PSN' " +
	  		"AND observation.observation_uid = ? " ;

             /**
             * Get the OrderingProviderInfo
             */
            Connection dbConnection = null;
            PreparedStatement preparedStmt = null;
            ResultSet resultSet = null;
            String theSelect = "";
            try
            {
    			if (propertyUtil.getDatabaseServerType() != null &&
    					propertyUtil.getDatabaseServerType().equalsIgnoreCase(NEDSSConstants.ORACLE_ID)){
    						theSelect= ORD_PROVIDER_ORACLE+ORD_PROVIDER;
    					}else{
    						theSelect= ORD_PROVIDER_MSQL+ORD_PROVIDER;
    					}
                dbConnection = getConnection();
                preparedStmt = dbConnection.prepareStatement(theSelect);
                preparedStmt.setString(1, partTypeCd);
                preparedStmt.setLong(2, observationUID.longValue());
                resultSet = preparedStmt.executeQuery();
                logger.debug("get resultSet " + resultSet.toString());
                while(resultSet.next()){
                	orderProviderInfo.add(0, resultSet.getString("lastNm"));
                	orderProviderInfo.add(1, resultSet.getString("firstNm"));
                	orderProviderInfo.add(2, resultSet.getString("prefix"));
                	orderProviderInfo.add(3, resultSet.getString("suffix"));
                	orderProviderInfo.add(4, resultSet.getString("degree"));
                	orderProviderInfo.add(5, resultSet.getLong("providerUid"));
                }
              }
              catch(SQLException se)
              {
                logger.fatal("Error: SQLException while getting observations for a person(Provider) in workup", se);
                logger.fatal("Select="+theSelect);
                throw new NEDSSSystemException( se.getMessage());
              }
              catch(Exception ex)
              {
                  logger.fatal("Error while getting observations for a person(Provider)in workup", ex);
                  throw new NEDSSSystemException(ex.toString());
              }
              finally
              {
                  closeResultSet(resultSet);
                  closeStatement(preparedStmt);
                  releaseConnection(dbConnection);
              }

      return orderProviderInfo;
  }
  
  /*
   * This method will return the RootExtensionText
   * @Param = ObservationUID
   * @returns ArrayList
   * In the Future this method will be used to get more data from the Act_id
   */
   
  public ArrayList<Object>  getActIdDetails(Long observationUID) throws NEDSSSystemException
  {
	  ArrayList<Object> actIdDetails= new ArrayList<Object> ();

	  String ACCESSION_NUMBER = "SELECT Act_id.root_extension_txt \"rootExtTxt\" " +
	  		"FROM Act_id " +
	  		"WHERE Act_id.Act_uid = ? " ;
             /**
             * Get the RootExtensionText for now
             */
            Connection dbConnection = null;
            PreparedStatement preparedStmt = null;
            ResultSet resultSet = null;
            try
            {
                dbConnection = getConnection();
                preparedStmt = dbConnection.prepareStatement(ACCESSION_NUMBER);
                preparedStmt.setLong(1, observationUID.longValue());
                resultSet = preparedStmt.executeQuery();
                logger.debug("get resultSet " + resultSet.toString());
               // Processed the record same way for the future enhancements
                while(resultSet.next()){
                	actIdDetails.add(0, resultSet.getString("rootExtTxt"));
                }
              }
              catch(SQLException se)
              {
                logger.fatal("Error: SQLException while getting observations for a person(Provider) in workup", se);
                throw new NEDSSSystemException( se.getMessage());
              }
              catch(Exception ex)
              {
                  logger.fatal("Error while getting observations for a person(Provider)in workup", ex);
                  throw new NEDSSSystemException(ex.toString());
              }
              finally
              {
                  closeResultSet(resultSet);
                  closeStatement(preparedStmt);
                  releaseConnection(dbConnection);
              }

      return actIdDetails;
  }

  public String getReportingFacilityName(Long organizationUid) throws NEDSSSystemException{

		String orgName = null;
		String ORG_NAME_QUERY = "SELECT organization_name.nm_txt \"nmTxt\" "
				+ "FROM organization_name" + " WHERE organization_uid = ? ";
		/**
		 * Get the patientInfo
		 */
		Connection dbConnection = null;
		PreparedStatement preparedStmt = null;
		ResultSet resultSet = null;

		try {
			dbConnection = getConnection();
			preparedStmt = dbConnection.prepareStatement(ORG_NAME_QUERY);
			preparedStmt.setLong(1, organizationUid.longValue());
			resultSet = preparedStmt.executeQuery();
			logger.debug("get resultSet " + resultSet.toString());
			while (resultSet.next()) {
				orgName = resultSet.getString("nmTxt");
			}
		} catch (SQLException se) {
			logger
					.fatal(
							"Error: SQLException while getting reporting facility name in workup",
							se);
			throw new NEDSSSystemException(se.getMessage());
		} catch (Exception ex) {
			logger.fatal(
					"Error while getting observations for a person in workup",
					ex);
			throw new NEDSSSystemException(ex.toString());
		} finally {
			closeResultSet(resultSet);
			closeStatement(preparedStmt);
			releaseConnection(dbConnection);
		}
		return orgName;
	}

  public ProviderDataForPrintVO getOrderingFacilityAddress(ProviderDataForPrintVO providerDataForPrintVO, Long organizationUid) throws NEDSSSystemException{

		String ORG_NAME_QUERY = "select street_addr1 \"streetAddr1\", street_addr2 \"streetAddr2\", city_desc_txt \"cityDescTxt\", state_cd \"stateCd\", zip_cd \"zipCd\" from Postal_locator where postal_locator_uid in ("
				+ "select locator_uid from Entity_locator_participation where entity_uid in (?)and cd='O' and class_cd='PST')";
		/**
		 * Get the OrgAddress
		 */
		Connection dbConnection = null;
		PreparedStatement preparedStmt = null;
		ResultSet resultSet = null;

		try {
			dbConnection = getConnection();
			preparedStmt = dbConnection.prepareStatement(ORG_NAME_QUERY);
			preparedStmt.setLong(1, organizationUid.longValue());
			resultSet = preparedStmt.executeQuery();
			logger.debug("get resultSet " + resultSet.toString());
			while (resultSet.next()) {
				providerDataForPrintVO.setFacilityAddress1(resultSet.getString("streetAddr1"));
				providerDataForPrintVO.setFacilityAddress2(resultSet.getString("streetAddr2"));
				providerDataForPrintVO.setFacilityCity(resultSet.getString("cityDescTxt"));
				providerDataForPrintVO.setFacilityState(resultSet.getString("stateCd"));
				providerDataForPrintVO.setFacilityZip(resultSet.getString("zipCd"));
				
				break;  //We will only take the first one
			}
		} catch (SQLException se) {
			logger
					.fatal(
							"Error: SQLException while getting getOrderingFacilityAddress:",
							se);
			throw new NEDSSSystemException(se.getMessage());
		} catch (Exception ex) {
			logger.fatal(
					"Error while getting getOrderingFacilityAddress:",ex);
			throw new NEDSSSystemException(ex.toString());
		} finally {
			closeResultSet(resultSet);
			closeStatement(preparedStmt);
			releaseConnection(dbConnection);
		}
		return providerDataForPrintVO;
	}
  
  public ProviderDataForPrintVO getOrderingFacilityPhone(ProviderDataForPrintVO providerDataForPrintVO, Long organizationUid) throws NEDSSSystemException{

		String ORG_NAME_QUERY = "select phone_nbr_txt \"phoneNbrTxt\", extension_txt \"extensionTxt\" from TELE_locator where TELE_locator_uid in ("
				+" select locator_uid from Entity_locator_participation where entity_uid= ? and cd='PH' and class_cd='TELE')  ";
		/**
		 * Get the OrgPhone
		 */
		Connection dbConnection = null;
		PreparedStatement preparedStmt = null;
		ResultSet resultSet = null;

		try {
			dbConnection = getConnection();
			preparedStmt = dbConnection.prepareStatement(ORG_NAME_QUERY);
			preparedStmt.setLong(1, organizationUid.longValue());
			resultSet = preparedStmt.executeQuery();
			logger.debug("get resultSet " + resultSet.toString());
			while (resultSet.next()) {
				providerDataForPrintVO.setFacilityPhone(resultSet.getString("phoneNbrTxt"));
				providerDataForPrintVO.setFacilityPhoneExtension(resultSet.getString("extensionTxt"));
				break; //We will only take the first one
		}
		} catch (SQLException se) {
			logger
					.fatal(
							"Error: SQLException while getting getOrderingFacilityPhone:",
							se);
			throw new NEDSSSystemException(se.getMessage());
		} catch (Exception ex) {
			logger.fatal(
					"Error while getting getOrderingFacilityPhone:",
					ex);
			throw new NEDSSSystemException(ex.toString());
		} finally {
			closeResultSet(resultSet);
			closeStatement(preparedStmt);
			releaseConnection(dbConnection);
		}
		return providerDataForPrintVO;
	}

  public ProviderDataForPrintVO getOrderingPersonAddress(ProviderDataForPrintVO providerDataForPrintVO, Long organizationUid) throws NEDSSSystemException{

		String ORG_NAME_QUERY = "select street_addr1 \"streetAddr1\", city_desc_txt \"cityDescTxt\", state_cd \"stateCd\", zip_cd \"zipCd\" from Postal_locator where postal_locator_uid in ("
				+ "select locator_uid from Entity_locator_participation where entity_uid in (?)and cd='O' and class_cd='PST')";
		/**
		 * Get the OrgAddress
		 */
		Connection dbConnection = null;
		PreparedStatement preparedStmt = null;
		ResultSet resultSet = null;

		try {
			dbConnection = getConnection();
			preparedStmt = dbConnection.prepareStatement(ORG_NAME_QUERY);
			preparedStmt.setLong(1, organizationUid.longValue());
			resultSet = preparedStmt.executeQuery();
			logger.debug("get resultSet " + resultSet.toString());
			while (resultSet.next()) {
				providerDataForPrintVO.setProviderStreetAddress1(resultSet.getString("streetAddr1"));
				providerDataForPrintVO.setProviderCity(resultSet.getString("cityDescTxt"));
				providerDataForPrintVO.setProviderState(resultSet.getString("stateCd"));
				providerDataForPrintVO.setProviderZip(resultSet.getString("zipCd"));
				break;  //We will only take the first one
			}
		} catch (SQLException se) {
			logger
					.fatal(
							"Error: SQLException while getting getOrderingFacilityAddress:",
							se);
			throw new NEDSSSystemException(se.getMessage());
		} catch (Exception ex) {
			logger.fatal(
					"Error while getting getOrderingFacilityAddress:",ex);
			throw new NEDSSSystemException(ex.toString());
		} finally {
			closeResultSet(resultSet);
			closeStatement(preparedStmt);
			releaseConnection(dbConnection);
		}
		return providerDataForPrintVO;
	}

public ProviderDataForPrintVO getOrderingPersonPhone(ProviderDataForPrintVO providerDataForPrintVO, Long organizationUid) throws NEDSSSystemException{

		String ORG_NAME_QUERY = "select phone_nbr_txt \"phoneNbrTxt\", extension_txt \"extensionTxt\" from TELE_locator where TELE_locator_uid in ("
				+" select locator_uid from Entity_locator_participation where entity_uid= ? and cd='O' and class_cd='TELE')  ";
		/**
		 * Get the OrgPhone
		 */
		Connection dbConnection = null;
		PreparedStatement preparedStmt = null;
		ResultSet resultSet = null;

		try {
			dbConnection = getConnection();
			preparedStmt = dbConnection.prepareStatement(ORG_NAME_QUERY);
			preparedStmt.setLong(1, organizationUid.longValue());
			resultSet = preparedStmt.executeQuery();
			logger.debug("get resultSet " + resultSet.toString());
			while (resultSet.next()) {
				providerDataForPrintVO.setProviderPhone(resultSet.getString("phoneNbrTxt"));
				providerDataForPrintVO.setProviderPhoneExtension(resultSet.getString("extensionTxt"));
				break; //We will only take the first one
		}
		} catch (SQLException se) {
			logger
					.fatal(
							"Error: SQLException while getting getOrderingFacilityPhone:",
							se);
			throw new NEDSSSystemException(se.getMessage());
		} catch (Exception ex) {
			logger.fatal(
					"Error while getting getOrderingFacilityPhone:",
					ex);
			throw new NEDSSSystemException(ex.toString());
		} finally {
			closeResultSet(resultSet);
			closeStatement(preparedStmt);
			releaseConnection(dbConnection);
		}
		return providerDataForPrintVO;
	}

  
  public String getSpecimanSource(Long materialUid) throws NEDSSSystemException{

		String specSource = null;
		String SPECIMEN_SOURCE_QUERY = "SELECT cd \"specimenSource\" "
				+ "FROM material" + " WHERE material_uid = ? ";
		/**
		 * Get the patientInfo
		 */
		Connection dbConnection = null;
		PreparedStatement preparedStmt = null;
		ResultSet resultSet = null;

		try {
			dbConnection = getConnection();
			preparedStmt = dbConnection.prepareStatement(SPECIMEN_SOURCE_QUERY);
			preparedStmt.setLong(1, materialUid.longValue());
			resultSet = preparedStmt.executeQuery();
			logger.debug("get resultSet " + resultSet.toString());
			while (resultSet.next()) {
				specSource = resultSet.getString("specimenSource");
			}
		} catch (SQLException se) {
			logger
					.fatal(
							"Error: SQLException while getting specimen source for lab in workup",
							se);
			throw new NEDSSSystemException(se.getMessage());
		} catch (Exception ex) {
			logger.fatal(
					"Error while getting observations for a person in workup",
					ex);
			throw new NEDSSSystemException(ex.toString());
		} finally {
			closeResultSet(resultSet);
			closeStatement(preparedStmt);
			releaseConnection(dbConnection);
		}
		return specSource;
	}

  public ArrayList<Object>  getPatientPersonUid(String participationTypeCd,Long observationUID) throws NEDSSSystemException
{

  ArrayList<Object>  vals= new ArrayList<Object> ();
  String QUICK_FIND_PATIENT = "SELECT " +
  "person.person_parent_uid \"personParentUid\" " +
  "FROM person, observation, participation " +
  "WHERE person.person_uid = participation.subject_entity_uid " +
  "AND participation.act_uid = observation.observation_uid " +
  "AND participation.type_cd = ? " +
  "AND observation.observation_uid = ? " ;

           /**
           * Get the patientUid
           */
          Connection dbConnection = null;
          PreparedStatement preparedStmt = null;
          ResultSet resultSet = null;

          try
          {
              dbConnection = getConnection();
              preparedStmt = dbConnection.prepareStatement(QUICK_FIND_PATIENT);
              preparedStmt.setString(1,participationTypeCd.toString());
              preparedStmt.setLong(2, observationUID.longValue());
              resultSet = preparedStmt.executeQuery();
              logger.debug("get resultSet " + resultSet.toString());
              while(resultSet.next()){
                vals.add(0,  new Long(resultSet.getString("personParentUid")));
              }
            }
            catch(SQLException se)
            {
              logger.fatal("Error: SQLException while getting observations for a person in workup", se);
              throw new NEDSSSystemException( se.getMessage());
            }
            catch(Exception ex)
            {
                logger.fatal("Error while getting observations for a person in workup", ex);
                throw new NEDSSSystemException(ex.toString());
            }
            finally
            {
                closeResultSet(resultSet);
                closeStatement(preparedStmt);
                releaseConnection(dbConnection);
            }

    return vals;
}

  public Map<Object,Object> getLabParticipations(Long observationUID) throws NEDSSSystemException{
		Map<Object,Object> vals = new HashMap<Object,Object>();
		  //per Pete Varnell - use optimizer hint on select
		String QUICK_FIND_PATIENT_ORACLE = "SELECT /*+ USE_NL(PARTICIPATION,OBSERVATION) */  ";
		String QUICK_FIND_PATIENT_MSQL = "SELECT ";
		String QUICK_FIND_PATIENT =  "participation.subject_class_cd \"classCd\", "
				+ "participation.type_cd \"typeCd\", "
				+ "participation.subject_entity_uid \"subjectEntityUid\" "
				+ "from observation, participation "
				+ "WHERE participation.act_uid = observation.observation_uid "
				+ "AND participation.type_cd in(\'"
				+ NEDSSConstants.PAR111_TYP_CD + "\',\'"
				+ NEDSSConstants.PAR104_TYP_CD + "\',\'"
				+ NEDSSConstants.PAR110_TYP_CD + "\',\'"
				+ NEDSSConstants.PAR101_TYP_CD + "\')"
				+ "AND observation.observation_uid = ? ";
		
		/**
		 * Get the patientUid
		 */
		Connection dbConnection = null;
		PreparedStatement preparedStmt = null;
		ResultSet resultSet = null;
		String theSelect="";
		try {
			
			if (propertyUtil.getDatabaseServerType() != null &&
			propertyUtil.getDatabaseServerType().equalsIgnoreCase(NEDSSConstants.ORACLE_ID)){
				theSelect= QUICK_FIND_PATIENT_ORACLE+QUICK_FIND_PATIENT;
			}else{
				theSelect= QUICK_FIND_PATIENT_MSQL+QUICK_FIND_PATIENT;
			}
			dbConnection = getConnection();
			preparedStmt = dbConnection.prepareStatement(theSelect);
			preparedStmt.setLong(1, observationUID.longValue());
			resultSet = preparedStmt.executeQuery();
			logger.debug("get resultSet " + resultSet.toString());
			while (resultSet.next()) {
				String classCd = resultSet.getString("classCd");
				String typeCd = resultSet.getString("typeCd");
				Long subjectEntityUid = new Long(resultSet.getString("subjectEntityUid"));
				if(classCd.equalsIgnoreCase(NEDSSConstants.CLASS_CD_PSN) && typeCd.equalsIgnoreCase( NEDSSConstants.PAR101_TYP_CD))
					continue;
				vals.put(typeCd, subjectEntityUid);
			}
		} catch (SQLException se) {
			logger
					.fatal(
							"Error: SQLException while getting observations for a person in workup",
							se);
			logger.fatal("Select="+theSelect);
			throw new NEDSSSystemException(se.getMessage());
		} catch (Exception ex) {
			logger.fatal(
					"Error while getting observations for a person in workup",
					ex);
			throw new NEDSSSystemException(ex.toString());
		} finally {
			closeResultSet(resultSet);
			closeStatement(preparedStmt);
			releaseConnection(dbConnection);
		}
		return vals;
	}



  public ArrayList<Object>  getPatientPersonInfoMorb(Long observationUID) throws NEDSSSystemException
  {

    ArrayList<Object> vals= new ArrayList<Object> ();
    String QUICK_FIND_PATIENT = "SELECT person_name.last_nm \"lastNm\", " +
    "person_name.first_nm \"firstNm\", observation.ctrl_cd_display_form \"ctrlCdDisplayForm\", " +
    "person.person_parent_uid \"personParentUid\" " +
    "FROM person, person_name, observation, participation " +
    "WHERE person.person_uid = participation.subject_entity_uid " +
    "AND participation.act_uid = observation.observation_uid " +
    "AND participation.type_cd = \'SubjOfMorbReport\' " +
    "AND person.person_uid = person_name.person_uid " +
    "AND person_name.nm_use_cd = \'L\' " +
    "AND observation.observation_uid = ? " ;

             /**
             * Get the patientInfo
             */
            Connection dbConnection = null;
            PreparedStatement preparedStmt = null;
            ResultSet resultSet = null;

            try
            {
                dbConnection = getConnection();
                preparedStmt = dbConnection.prepareStatement(QUICK_FIND_PATIENT);
                preparedStmt.setLong(1, observationUID.longValue());
                resultSet = preparedStmt.executeQuery();
                logger.debug("get resultSet " + resultSet.toString());
                while(resultSet.next()){
                  vals.add(0, resultSet.getString("firstNm"));
                  vals.add(1, resultSet.getString("lastNm"));
                  vals.add(2, resultSet.getString("ctrlCdDisplayForm"));
                  vals.add(3, new Long(resultSet.getString("personParentUid")));
                }
              }
              catch(SQLException se)
              {
                logger.fatal("Error: SQLException while getting observations for a person in workup", se);
                throw new NEDSSSystemException( se.getMessage());
              }
              catch(Exception ex)
              {
                  logger.fatal("Error while getting observations for a person in workup", ex);
                  throw new NEDSSSystemException(ex.toString());
              }
              finally
              {
                  closeResultSet(resultSet);
                  closeStatement(preparedStmt);
                  releaseConnection(dbConnection);
              }

      return vals;
  }
  
  public ArrayList<Object>  getReportingFacilityInfo(Long observationUID, String partTypeCd) throws NEDSSSystemException
  {
	  ArrayList<Object> facilityInfo= new ArrayList<Object> ();

	  String ORD_PROVIDER = " SELECT  organization_name.organization_uid \"organizationUid\", organization_name.nm_txt \"nmTxt\" "	  
			  	+" FROM organization_name, observation, "
			  	+" participation WHERE organization_name.organization_uid = participation.subject_entity_uid "
		  		+" AND participation.act_uid = observation.observation_uid  "
		  		+" AND participation.type_cd = ? AND participation.subject_class_cd='ORG'  "
		  		+" AND observation.observation_uid = ? ";

             /**
             * Get the OrderingProviderInfo
             */
            Connection dbConnection = null;
            PreparedStatement preparedStmt = null;
            ResultSet resultSet = null;

            try
            {
                dbConnection = getConnection();
                preparedStmt = dbConnection.prepareStatement(ORD_PROVIDER);
                preparedStmt.setString(1, partTypeCd);
                preparedStmt.setLong(2, observationUID.longValue());
                resultSet = preparedStmt.executeQuery();
                logger.debug("get resultSet " + resultSet.toString());
                while(resultSet.next()){
                	facilityInfo.add(0, resultSet.getString("nmTxt"));
                	facilityInfo.add(1, resultSet.getLong("organizationUid"));
                }
              }
              catch(SQLException se)
              {
                logger.fatal("Error: SQLException while getting observations for a person(Provider) in workup", se);
                throw new NEDSSSystemException( se.getMessage());
              }
              catch(Exception ex)
              {
                  logger.fatal("Error while getting observations for a person(Provider)in workup", ex);
                  throw new NEDSSSystemException(ex.toString());
              }
              finally
              {
                  closeResultSet(resultSet);
                  closeStatement(preparedStmt);
                  releaseConnection(dbConnection);
              }

      return facilityInfo;
  }
}