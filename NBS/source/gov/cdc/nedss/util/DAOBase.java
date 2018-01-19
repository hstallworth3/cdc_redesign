/**
 * Name:		    DAOBase
 * Description:	class that contains a method for a generic prepared statement call
 * Copyright:	Copyright (c) 2002
 * Company: 	    Computer Sciences Corporation
 * @author	    NEDSS Development Team
 * @version	    1.0
 */
package gov.cdc.nedss.util;

import gov.cdc.nedss.exception.NEDSSDAOAppException;
import gov.cdc.nedss.exception.NEDSSDAOSysException;
import gov.cdc.nedss.exception.NEDSSSystemException;
import gov.cdc.nedss.systemservice.util.RootDTInterface;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

/**
 * Name: DAOBase Description: class that contains a method for a generic
 * prepared statement call Copyright: Copyright (c) 2002 Company: Computer
 * Sciences Corporation
 * 
 * @author NEDSS Development Team
 * @version 1.0 generic class used to build and run a PreparedStatement
 */
public class DAOBase extends BMPBase {
	public DAOBase() {
	}

	static final LogUtils logger = new LogUtils(DAOBase.class.getName());

	/**
	 * 
	 * @param query
	 *            as String
	 * @return is Long
	 * @throws NEDSSDAOSysException
	 * 
	 *             this method is used to create sequence number. It will get
	 *             Data from table and increment value. It return as Long object
	 */

	public Long getMaxId(String query) throws NEDSSDAOSysException {

		// get a connection
		Connection dbConnection = null;
		PreparedStatement preparedStmt = null;
		ResultSet resultSet = null;
		Long maxId = null;

		try {
			dbConnection = getConnection();

			// sql statement passed in
			preparedStmt = dbConnection.prepareStatement(query);
			resultSet = preparedStmt.executeQuery();
			long temp = 1;
			if (resultSet.next()) {
				temp = resultSet.getLong(1);

				temp = temp + 1;
				maxId = new Long(temp);

			} else
				maxId = new Long(temp);
		} catch (SQLException sex) {
			logger.fatal("query: "+query);
			logger.fatal("SQLException  ERROR = "+sex.getMessage(), sex);
			throw new NEDSSDAOSysException(sex.toString());
		} catch (Exception ex) {
			logger.fatal("query: "+query);
			logger.fatal("Exception  ERROR = "+ex.getMessage(), ex);
			throw new NEDSSSystemException(ex.toString());
		} finally {
			closeResultSet(resultSet);
			closeStatement(preparedStmt);
			releaseConnection(dbConnection);
		}
		logger.debug("in side DAOBase.preparedStmtMethod - count  = "+ maxId);
		return maxId;
	}

	/**
	 * This method loads a PrepareStatement object using the objects in the
	 * ArrayList<Object> (because it is an ordered list) and runs either an
	 * executeQuery method or an executeUpdate depending on the queryType
	 * parameter.
	 * 
	 * If the queryType is NEDSSConstantUtil.SELECT then the executeQuery method
	 * is run, the rootDTInterface passed in is loaded using the
	 * resultSetUtils.mapRsToBean method and a rootDTInterface is returned.
	 * 
	 * If the queryType is not a NEDSSConstantUtil.SELECT it is assumed the
	 * operation is either an insert or an update which requires an
	 * executeUpdate method. The executeUpdate method returns an int that is
	 * converted to a Integer and becomes the return object.
	 * 
	 * @param rootDTInterface
	 * @param arrayList
	 * @param statement
	 * @param queryType
	 * @return Object
	 * @throws NEDSSDAOSysException
	 */
	public Object preparedStmtMethod(RootDTInterface rootDTInterface,
			ArrayList<Object> arrayList, String statement, String queryType)
			throws NEDSSDAOSysException {

		// get a connection
		Connection dbConnection = null;
		PreparedStatement preparedStmt = null;
		ResultSet resultSet = null;

		try {
			dbConnection = getConnection();

			// sql statement passed in
			preparedStmt = dbConnection.prepareStatement(statement);
			// only for debugging purposes
			logger.debug("DAOBase.preparedStmtMethod.statement is :"
					+ statement);
			logger.debug("DAOBase.preparedStmtMethod.queryType is :"
					+ queryType);
			logger.debug("DAOBase.preparedStmtMethod.rootDTInterface is :"
					+ rootDTInterface);

			// array of objects passed in for statement parameters
			if (arrayList != null) {
				Iterator<?> it = arrayList.iterator();
				for (int i = 1; it.hasNext(); i++) {
					Object obj = it.next();
					if (obj == null) {
						// if it is null setNull
						preparedStmt.setNull(i, Types.VARCHAR);
					}
					// if String with "" vlaue set it to null.
					// Struts may be giving blank String instead of null
					// Oracle does it for you, SQL server does not.
					else if (obj.getClass().toString().equals(
							"class java.lang.String")) {
						if (((String) obj).trim().length() == 0)
							preparedStmt.setNull(i, Types.VARCHAR);
						else
							preparedStmt.setObject(i, obj.toString());
					} else {
						// if it is a timestamp treat it special (mainly for
						// oracle)
						String timeStamp = obj.getClass().toString();
						if (obj.getClass().toString().equals(
								"class java.sql.Timestamp")) {
							preparedStmt.setTimestamp(i,
									(java.sql.Timestamp) obj);
						} else {
							// else pass them all as strings driver will convert
							// to proper type
							// preparedStmt.setObject(i, obj.toString());
							String value = obj.toString();
							if (value == null || value.trim().length() == 0)
								preparedStmt.setNull(i, Types.VARCHAR);
							else
								preparedStmt.setObject(i, value);

						}
					}
				}
			}
			// if it is a select statement then you want to use executeQuery to
			// get the resultSet
			if (queryType.equals(NEDSSConstants.SELECT)) {
				resultSet = preparedStmt.executeQuery();

				ResultSetMetaData resultSetMetaData = null;
				resultSetMetaData = resultSet.getMetaData();

				ArrayList<Object> pnList = new ArrayList<Object>();
				ArrayList<Object> reSetList = new ArrayList<Object>();

				ResultSetUtils resultSetUtils = new ResultSetUtils();
				pnList = (ArrayList<Object>) resultSetUtils.mapRsToBeanList(
						resultSet, resultSetMetaData, rootDTInterface
								.getClass(), pnList);

				for (Iterator<Object> anIterator = pnList.iterator(); anIterator
						.hasNext();) {
					RootDTInterface reSetName = (RootDTInterface) anIterator
							.next();
					reSetName.setItNew(false);
					reSetName.setItDirty(false);
					reSetList.add(reSetName);
				}
				// logger.debug("return person name collection");
				return reSetList;
			}
			// else if it is a SELECT COUNT(*) then it needs to return an
			// Integer
			else if (queryType.equals(NEDSSConstants.SELECT_COUNT)) {
				int count = 0;
				logger
						.debug("in side DAOBase.preparedStmtMethod - statement  = "
								+ statement);
				resultSet = preparedStmt.executeQuery();
				if (resultSet.next())
					count = resultSet.getInt(1);
				logger.debug("in side DAOBase.preparedStmtMethod - count  = "
						+ count);
				return new Integer(count);
			}
			// else if it is an insert or update then you want to use
			// executeUpdate to get an int
			else {
				int result = preparedStmt.executeUpdate();
				logger.debug("preparedStmt.executeUpdate() - result = "
						+ result);
				return new Integer(result);
			}

		} catch (SQLException sex) {
			if (logSQLException(statement))
				logger.fatal("SQLException executing: " + statement
						+ " ERROR = " + sex.getMessage(), sex);
			throw new NEDSSDAOSysException(sex.toString());
		} catch (Exception ex) {
			logger.fatal("Exception: executing statement: "+ statement+ " ERROR = " + ex.getMessage(), ex);
			throw new NEDSSSystemException(ex.toString());
		}
		// clean house
		finally {
			closeResultSet(resultSet);
			closeStatement(preparedStmt);
			releaseConnection(dbConnection);
		}
	}

	/**
	 * 
	 * @param rootDTInterface
	 * @param arrayList
	 * @param statement
	 * @param queryType
	 * @param dataSource
	 * @return
	 * @throws NEDSSDAOSysException
	 */
	public Object preparedStmtMethod(RootDTInterface rootDTInterface,
			ArrayList<Object> arrayList, String statement, String queryType,
			String odbcName) throws NEDSSDAOSysException {

		// get a connection
		Connection dbConnection = null;
		PreparedStatement preparedStmt = null;
		ResultSet resultSet = null;

		try {
			dbConnection = getConnection(odbcName);

			// sql statement passed in
			preparedStmt = dbConnection.prepareStatement(statement);
			// only for debugging purposes
			logger.debug("DAOBase.preparedStmtMethod.statement is :"
					+ statement);
			logger.debug("DAOBase.preparedStmtMethod.queryType is :"
					+ queryType);
			logger.debug("DAOBase.preparedStmtMethod.rootDTInterface is :"
					+ rootDTInterface);

			// array of objects passed in for statement parameters
			if (arrayList != null) {
				Iterator<Object> it = arrayList.iterator();
				for (int i = 1; it.hasNext(); i++) {
					Object obj = it.next();
					if (obj == null) {
						// if it is null setNull
						preparedStmt.setNull(i, Types.VARCHAR);
					}
					// if String with "" vlaue set it to null.
					// Struts may be giving blank String instead of null
					// Oracle does it for you, SQL server does not.
					else if (obj.getClass().toString().equals(
							"class java.lang.String")) {
						if (((String) obj).trim().length() == 0)
							preparedStmt.setNull(i, Types.VARCHAR);
						else
							preparedStmt.setObject(i, obj.toString());
					} else {
						// if it is a timestamp treat it special (mainly for
						// oracle)
						String timeStamp = obj.getClass().toString();
						if (obj.getClass().toString().equals(
								"class java.sql.Timestamp")) {
							preparedStmt.setTimestamp(i,
									(java.sql.Timestamp) obj);
						} else {
							// else pass them all as strings driver will convert
							// to proper type
							// preparedStmt.setObject(i, obj.toString());
							String value = obj.toString();
							if (value == null || value.trim().length() == 0)
								preparedStmt.setNull(i, Types.VARCHAR);
							else
								preparedStmt.setObject(i, value);

						}
					}
				}
			}
			// if it is a select statement then you want to use executeQuery to
			// get the resultSet
			if (queryType.equals(NEDSSConstants.SELECT)) {
				resultSet = preparedStmt.executeQuery();

				ResultSetMetaData resultSetMetaData = null;
				resultSetMetaData = resultSet.getMetaData();

				ArrayList<Object> pnList = new ArrayList<Object>();
				ArrayList<Object> reSetList = new ArrayList<Object>();

				ResultSetUtils resultSetUtils = new ResultSetUtils();
				pnList = (ArrayList<Object>) resultSetUtils.mapRsToBeanList(
						resultSet, resultSetMetaData, rootDTInterface
								.getClass(), pnList);

				for (Iterator<Object> anIterator = pnList.iterator(); anIterator
						.hasNext();) {
					RootDTInterface reSetName = (RootDTInterface) anIterator
							.next();
					reSetName.setItNew(false);
					reSetName.setItDirty(false);
					reSetList.add(reSetName);
				}
				// logger.debug("return person name collection");
				return reSetList;
			}
			// else if it is a SELECT COUNT(*) then it needs to return an
			// Integer
			else if (queryType.equals(NEDSSConstants.SELECT_COUNT)) {
				int count = 0;
				logger
						.debug("in side DAOBase.preparedStmtMethod - statement  = "
								+ statement);
				resultSet = preparedStmt.executeQuery();
				if (resultSet.next())
					count = resultSet.getInt(1);
				logger.debug("in side DAOBase.preparedStmtMethod - count  = "
						+ count);
				return new Integer(count);
			}
			// else if it is an insert or update then you want to use
			// executeUpdate to get an int
			else {
				int result = preparedStmt.executeUpdate();
				logger.debug("preparedStmt.executeUpdate() - result = "
						+ result);
				return new Integer(result);
			}

		} catch (SQLException sex) {
			if (logSQLException(statement))
				logger.fatal("SQLException executing: " + statement
						+ " ERROR = " + sex.getMessage(), sex);
			throw new NEDSSDAOSysException(sex.toString());
		} catch (Exception ex) {
			logger.fatal("Exception executing: " + statement+ " ERROR = " + ex.getMessage(), ex);
			throw new NEDSSSystemException(ex.toString());
		}
		// clean house
		finally {
			closeResultSet(resultSet);
			closeStatement(preparedStmt);
			releaseConnection(dbConnection);
		}
	}

	/**
	 * @param statement
	 * @return
	 */
	private boolean logSQLException(String statement) {
		logger.debug("DAOBase.logSQLException.statement is :" + statement);

		return (!statement.startsWith("insert into df_sf_metadata_group") && !statement
				.startsWith("insert into bus_obj_df_sf_mdata_group"));
	}

	/**
	 * This method loads a PrepareStatement object using the objects in the
	 * ArrayList<Object> (because it is an ordered list) and runs either an
	 * executeQuery method or an executeUpdate depending on the queryType
	 * parameter.
	 * 
	 * If the queryType is NEDSSConstantUtil.SELECT then the executeQuery method
	 * is run, the rootDTInterface passed in is loaded using the
	 * resultSetUtils.mapRsToBean method and a rootDTInterface is returned.
	 * 
	 * If the queryType is not a NEDSSConstantUtil.SELECT it is assumed the
	 * operation is either an insert or an update which requires an
	 * executeUpdate method. The executeUpdate method returns an int that is
	 * converted to a Integer and becomes the return object.
	 * 
	 * @param rootDTInterface
	 * @param arrayList
	 * @param statement
	 * @param queryType
	 * @return Object
	 * @throws NEDSSDAOSysException
	 */
	public Object preparedStmtMethodForMap(RootDTInterface rootDTInterface,
			ArrayList<Object> arrayList, String statement, String queryType,
			String pk) throws NEDSSDAOSysException {

		// get a connection
		Connection dbConnection = null;
		PreparedStatement preparedStmt = null;
		ResultSet resultSet = null;

		try {
			dbConnection = getConnection();
			// sql statement passed in
			preparedStmt = dbConnection.prepareStatement(statement);
			logger.debug("DAOBase.preparedStmtMethodForMap.statement is :"
					+ statement);
			logger.debug("DAOBase.preparedStmtMethodForMap.queryType is :"
					+ queryType);
			logger
					.debug("DAOBase.preparedStmtMethodForMap.rootDTInterface is :"
							+ rootDTInterface);

			// array of objects passed in for statement parameters
			if (arrayList != null) {
				Iterator<Object> it = arrayList.iterator();
				for (int i = 1; it.hasNext(); i++) {
					Object obj = it.next();
					if (obj == null) {
						// if it is null setNull
						preparedStmt.setNull(i, Types.VARCHAR);
					} else {
						// if it is a timestamp treat it special (mainly for
						// oracle)
						String timeStamp = obj.getClass().toString();
						if (obj.getClass().toString().equals(
								"class java.sql.Timestamp")) {
							preparedStmt.setTimestamp(i,
									(java.sql.Timestamp) obj);
						} else {
							// else pass them all as strings driver will convert
							// to proper type
							preparedStmt.setObject(i, obj.toString());
						}
					}
				}
			}
			// if it is a select statement then you want to use executeQuery to
			// get the resultSet
			if (queryType.equals(NEDSSConstants.SELECT)) {
				resultSet = preparedStmt.executeQuery();

				ResultSetMetaData resultSetMetaData = null;
				resultSetMetaData = resultSet.getMetaData();

				HashMap<Object, Object> pnMap = new HashMap<Object, Object>();
				// HashMap reSetMap = new HashMap<Object,Object>();

				ResultSetUtils resultSetUtils = new ResultSetUtils();
				pnMap = (HashMap<Object, Object>) resultSetUtils
						.mapRsToBeanMap(resultSet, resultSetMetaData,
								rootDTInterface.getClass(), pk, pnMap);
				/*
				 * for(Iterator<Object> anIterator = pnMap.values().iterator();
				 * anIterator.hasNext(); ) { RootDTInterface reSetName =
				 * (RootDTInterface)anIterator.next();
				 * reSetName.setItNew(false); reSetName.setItDirty(false);
				 * reSetList.add(reSetName); }
				 */// logger.debug("return person name collection");
				return pnMap;
			}
			// else if it is a SELECT COUNT(*) then it needs to return an
			// Integer
			else if (queryType.equals(NEDSSConstants.SELECT_COUNT)) {
				int count = 0;
				logger
						.debug("in side DAOBase.preparedStmtMethod - statement  = "
								+ statement);
				resultSet = preparedStmt.executeQuery();
				if (resultSet.next())
					count = resultSet.getInt(1);
				logger.debug("in side DAOBase.preparedStmtMethod - count  = "
						+ count);
				return new Integer(count);
			}
			// else if it is an insert or update then you want to use
			// executeUpdate to get an int
			else {
				int result = preparedStmt.executeUpdate();
				logger.debug("preparedStmt.executeUpdate() - result = "
						+ result);
				return new Integer(result);
			}

		} catch (SQLException sex) {
			logger.fatal("SQLException executing: " + statement + " ERROR = "
					+ sex.getMessage(), sex);
			throw new NEDSSDAOSysException(sex.toString());
		} catch (Exception ex) {
			logger.fatal("Exception  executing: " + statement + " ERROR = "
					+ ex.getMessage(), ex);
			throw new NEDSSSystemException(ex.toString());
		}
		// clean house
		finally {
			closeResultSet(resultSet);
			closeStatement(preparedStmt);
			releaseConnection(dbConnection);
		}
	}

	/**
	 * @param queryType
	 * @param arrayList
	 *            in inArrayList
	 * @param arrayList
	 *            in outArrayList
	 * @return ArrayList
	 * @throws NEDSSDAOSysException
	 * 
	 *             This is call store procedure and return ArryList object
	 */
	public ArrayList<Object> callStoredProcedureMethod(String sQuery,
			ArrayList<Object> inArrayList, ArrayList<Object> outArrayList)
			throws NEDSSDAOSysException {
		Connection dbConnection = null;
		CallableStatement sProc = null;
		ArrayList<Object> arrayList = new ArrayList<Object>();

		try {
			dbConnection = getConnection();
			logger.debug("DAOBase.callStoredProcedureMethod.sQuery is :"
					+ sQuery);

			sProc = dbConnection.prepareCall(sQuery);
			int tempInt = inArrayList.size();
			int tempOut = outArrayList.size() + inArrayList.size();
			int i = 1;
			if (inArrayList != null) {
				Iterator<Object> it = inArrayList.iterator();
				while (it.hasNext()) {
					Object obj = it.next();
					if (obj != null)
						sProc.setObject(i, obj.toString());
					i++;
				}
			}

			if (outArrayList != null) {
				Iterator<Object> it = outArrayList.iterator();
				while (it.hasNext()) {
					Object obj = it.next();
					if (obj != null)
						sProc.registerOutParameter(i, Integer.parseInt(obj
								.toString()));
					i++;
				}
			}

			sProc.execute();

			while (tempInt < tempOut)
				arrayList.add(sProc.getObject(++tempInt));

		} catch (SQLException sex) {
			logger.fatal("SQLException executing:"+sQuery+"  ERROR = " + sex.getMessage(), sex);
			throw new NEDSSDAOSysException(sex.toString());
		} catch (Exception ex) {
			logger.fatal("Exception  executing:"+sQuery+" ERROR = " + ex.getMessage(), ex);
			throw new NEDSSSystemException(ex.toString());
		}
		// clean house
		finally {
			closeStatement(sProc);
			releaseConnection(dbConnection);
		}
		// System.out.println("\narrayList.size() = " + arrayList.size() +
		// "\n");
		return arrayList;
	}

	/*
	 * // for testing public static void main(String[] args) {
	 * 
	 * PersonDT personDT = new PersonDT(); DAOBase daoBase = new DAOBase();
	 * 
	 * String SELECT_PERSON =
	 * "SELECT person_uid \"personUid\", administrative_gender_cd \"administrativeGenderCd\", add_reason_cd \"addReasonCd\", add_time \"addTime\", add_user_id \"addUserId\", age_calc \"ageCalc\", age_calc_time \"ageCalcTime\", age_calc_unit_cd \"ageCalcUnitCd\", age_category_cd \"ageCategoryCd\", age_reported \"ageReported\", age_reported_time \"ageReportedTime\", age_reported_unit_cd \"ageReportedUnitCd\", birth_gender_cd \"birthGenderCd\", birth_order_nbr \"birthOrderNbr\", birth_time \"birthTime\", birth_time_calc \"birthTimeCalc\", cd \"cd\", cd_desc_txt \"cdDescTxt\", curr_sex_cd \"currSexCd\", deceased_ind_cd \"deceasedIndCd\", deceased_time \"deceasedTime\", description \"description\", education_level_cd \"educationLevelCd\", education_level_desc_txt \"educationLevelDescTxt\", ethnic_group_ind \"ethnicGroupInd\", last_chg_reason_cd \"lastChgReasonCd\", last_chg_time \"lastChgTime\", last_chg_user_id \"lastChgUserId\", marital_status_cd \"maritalStatusCd\", "
	 * +
	 * " marital_status_desc_txt \"maritalStatusDescTxt\", mothers_maiden_nm \"mothersMaidenNm\", multiple_birth_ind \"multipleBirthInd\", "
	 * +
	 * "occupation_cd \"occupationCd\", preferred_gender_cd \"preferredGenderCd\", prim_lang_cd \"primLangCd\", prim_lang_desc_txt \"primLangDescTxt\", record_status_cd \"recordStatusCd\", record_status_time \"recordStatusTime\", status_cd \"statusCd\", status_time \"statusTime\", survived_ind_cd \"survivedIndCd\", user_affiliation_txt \"userAffiliationTxt\", "
	 * +
	 * "first_nm \"firstNm\", last_nm \"lastNm\", middle_nm \"middleNm\", nm_prefix \"nmPrefix\", nm_suffix \"nmSuffix\", preferred_nm \"preferredNm\", hm_street_addr1 \"hmStreetAddr1\", hm_street_addr2 \"hmStreetAddr2\", hm_city_cd \"hmCityCd\", hm_city_desc_txt \"hmCityDescTxt\", hm_state_cd \"hmStateCd\", hm_zip_cd \"hmZipCd\", hm_cnty_cd \"hmCntyCd\", hm_cntry_cd \"hmCntryCd\", hm_phone_nbr \"hmPhoneNbr\", hm_phone_cntry_cd \"hmPhoneCntryCd\", hm_email_addr \"hmEmailAddr\", cell_phone_nbr \"cellPhoneNbr\", wk_street_addr1 \"wkStreetAddr1\", wk_street_addr2 \"wkStreetAddr2\", wk_city_cd \"wkCityCd\", wk_city_desc_txt \"wkCityDescTxt\", wk_state_cd \"wkStateCd\", wk_zip_cd \"wkZipCd\", wk_cnty_cd \"wkCntyCd\", wk_cntry_cd \"wkCntryCd\", wk_phone_nbr \"wkPhoneNbr\", wk_phone_cntry_cd \"wkPhoneCntryCd\", wk_email_addr \"wkEmailAddr\", SSN \"SSN\", medicaid_num \"medicaidNum\", dl_num \"dlNum\", dl_state_cd \"dlStateCd\", race_cd \"raceCd\", race_seq_nbr \"raceSeqNbr\","
	 * +
	 * " race_category_cd \"raceCategoryCd\", ethnicity_group_cd \"ethnicityGroupCd\", ethnic_group_seq_nbr \"ethnicGroupSeqNbr\", adults_in_house_nbr \"adultsInHouseNbr\", "
	 * +
	 * "children_in_house_nbr \"childrenInHouseNbr\", birth_city_cd \"birthCityCd\", birth_city_desc_txt \"birthCityDescTxt\", birth_cntry_cd \"birthCntryCd\", birth_state_cd \"birthStateCd\", local_id \"localId\", version_ctrl_nbr \"versionCtrlNbr\" FROM "
	 * + DataTables.PERSON_TABLE + " WHERE person_uid = ?";
	 * 
	 * ArrayList<Object> arrayList = new ArrayList<Object> (); //**** sql server
	 * personuid Long personUidLong = new Long(470160908); //**** oracle server
	 * personuid //Long personUidLong = new Long(310326053);
	 * arrayList.add(personUidLong);
	 * 
	 * 
	 * try {
	 * 
	 * personDT = (PersonDT)daoBase.preparedStmtMethod(personDT, arrayList,
	 * SELECT_PERSON, "SELECT");
	 * 
	 * System.out.println("personDT.getPersonUid() = " +
	 * personDT.getPersonUid()); System.out.println("personDT.getFirstNm() = " +
	 * personDT.getFirstNm()); System.out.println("personDT.getLastNm() = " +
	 * personDT.getLastNm()); System.out.println("personDT.getAddTime() = " +
	 * personDT.getAddTime());
	 * System.out.println("personDT.getRecordStatusTime() = " +
	 * personDT.getRecordStatusTime());
	 * System.out.println("personDT.getLocalId() = " + personDT.getLocalId());
	 * 
	 * } catch (Exception ex) { logger.fatal("Exception in main:  ERROR = " +
	 * ex); throw new NEDSSSystemException(ex.toString()); } arrayList.clear();
	 * 
	 * String UPDATE_PERSON = "UPDATE " + DataTables.PERSON_TABLE + " set " +
	 * //1 "administrative_gender_cd = ?, " + "add_reason_cd = ?, " +
	 * "add_time = ?,  " + "add_user_id = ?,  " + "age_calc = ?,  " +
	 * "age_calc_time = ?,  " + "age_calc_unit_cd = ?,  " +
	 * "age_category_cd = ?,  " + "age_reported = ?,  " + //10
	 * "age_reported_time = ?,  " + "age_reported_unit_cd = ?,  " +
	 * "birth_gender_cd = ?,  " + "birth_order_nbr = ?,  " + "birth_time = ?,  "
	 * + "birth_time_calc = ?,  " + "cd = ?,  " + "cd_desc_txt = ?,  " +
	 * "curr_sex_cd = ?,  " + "deceased_ind_cd = ?,  " + //20
	 * "deceased_time = ?,  " + "description = ?,  " +
	 * "education_level_cd = ?,  " + "education_level_desc_txt = ?,  " +
	 * "ethnic_group_ind = ?,  " + "last_chg_reason_cd = ?,  " +
	 * "last_chg_time = ?,  " + "last_chg_user_id = ?,  " +
	 * "marital_status_cd = ?,  " + "marital_status_desc_txt = ?,  " + //30
	 * "mothers_maiden_nm = ?,  " + "multiple_birth_ind = ?,  " +
	 * "occupation_cd = ?,  " + "preferred_gender_cd = ?,  " +
	 * "prim_lang_cd = ?,  " + "prim_lang_desc_txt = ?,  " +
	 * "record_status_cd = ?,  " + "record_status_time = ?,  " +
	 * "status_cd = ?,  " + "status_time = ?,  " + //40 "survived_ind_cd = ?,  "
	 * + "user_affiliation_txt = ?, " + "first_nm = ?,  " + "last_nm = ?,  " +
	 * "middle_nm = ?,  " + "nm_prefix = ?,  " + "nm_suffix = ?,  " +
	 * "preferred_nm = ?,  " + "hm_street_addr1 = ?,  " +
	 * "hm_street_addr2 = ?,  " + //50 "hm_city_cd = ?,  " +
	 * "hm_city_desc_txt = ?,  " + "hm_state_cd = ?,  " + "hm_zip_cd = ?,  " +
	 * "hm_cnty_cd = ?,  " + "hm_cntry_cd = ?,  " + "hm_phone_nbr = ?,  " +
	 * "hm_phone_cntry_cd = ?,  " + "hm_email_addr = ?,  " +
	 * "cell_phone_nbr = ?,  " + //60 "wk_street_addr1 = ?,  " +
	 * "wk_street_addr2 = ?,  " + "wk_city_cd = ?,  " +
	 * "wk_city_desc_txt = ?,  " + "wk_state_cd = ?,  " + "wk_zip_cd = ?,  " +
	 * "wk_cnty_cd = ?,  " + "wk_cntry_cd = ?,  " + "wk_phone_nbr = ?,  " +
	 * "wk_phone_cntry_cd = ?,  " + //70 "wk_email_addr = ?,  " + "SSN = ?,  " +
	 * "medicaid_num = ?,  " + "dl_num = ?,  " + "dl_state_cd = ?,  " +
	 * "race_cd = ?,  " + "race_seq_nbr = ?,  " + "race_category_cd = ?,  " +
	 * "ethnicity_group_cd = ?,  " + "ethnic_group_seq_nbr = ?,  " + //80
	 * "adults_in_house_nbr = ?,  " + "children_in_house_nbr = ?,  " +
	 * "birth_city_cd = ?,  " + "birth_city_desc_txt = ?,  " +
	 * "birth_cntry_cd = ?,  " + "birth_state_cd = ?,  " +
	 * "race_desc_txt = ?,  " + "ethnic_group_desc_txt = ?,  " +
	 * "version_ctrl_nbr = ?  " + "WHERE person_uid = ?  " + //90
	 * "AND version_ctrl_nbr = ?";
	 * 
	 * try { //Updates person table if (personDT != null) {
	 * System.out.println("Updating personDT: UID = " +
	 * personDT.getPersonUid().longValue());
	 * //************************************************************ //int i =
	 * 1; //preparedStmt.setString(i++, personDT.getAdministrativeGenderCd());
	 * System.out.println("personDT.getAdministrativeGenderCd() = " +
	 * personDT.getAdministrativeGenderCd());
	 * arrayList.add(personDT.getAdministrativeGenderCd());
	 * //preparedStmt.setString(i++, personDT.getAddReasonCd());
	 * arrayList.add(personDT.getAddReasonCd());
	 * 
	 * if (personDT.getAddTime() == null) //preparedStmt.setNull(i++,
	 * Types.TIMESTAMP); arrayList.add(personDT.getAddTime()); else
	 * //preparedStmt.setTimestamp(i++, personDT.getAddTime());
	 * arrayList.add(personDT.getAddTime());
	 * 
	 * if (personDT.getAddUserId() == null) //preparedStmt.setNull(i++,
	 * Types.INTEGER); arrayList.add(personDT.getAddUserId()); else
	 * //preparedStmt.setLong(i++, (personDT.getAddUserId()).longValue());
	 * arrayList.add(personDT.getAddUserId());
	 * 
	 * if (personDT.getAgeCalc() == null) //preparedStmt.setNull(i++,
	 * Types.INTEGER); arrayList.add(personDT.getAgeCalc()); else
	 * //preparedStmt.setInt(i++,(personDT.getAgeCalc()).intValue());
	 * arrayList.add(personDT.getAgeCalc());
	 * 
	 * if (personDT.getAgeCalcTime() == null) //preparedStmt.setNull(i++,
	 * Types.TIMESTAMP); arrayList.add(personDT.getAgeCalcTime()); else
	 * //preparedStmt.setTimestamp(i++, personDT.getAgeCalcTime());
	 * arrayList.add(personDT.getAgeCalcTime());
	 * 
	 * //preparedStmt.setString(i++, personDT.getAgeCalcUnitCd());
	 * arrayList.add(personDT.getAgeCalcUnitCd()); //preparedStmt.setString(i++,
	 * personDT.getAgeCategoryCd()); arrayList.add(personDT.getAgeCategoryCd());
	 * //preparedStmt.setString(i++, personDT.getAgeReported());
	 * arrayList.add(personDT.getAgeReported());
	 * System.out.println("Updating person part 1" +
	 * (personDT.getPersonUid()).longValue());
	 * 
	 * if (personDT.getAgeReportedTime() == null) //preparedStmt.setNull(i++,
	 * Types.TIMESTAMP); arrayList.add(personDT.getAgeReportedTime()); else
	 * //preparedStmt.setTimestamp(i++, personDT.getAgeReportedTime());
	 * arrayList.add(personDT.getAgeReportedTime());
	 * 
	 * //preparedStmt.setString(i++, personDT.getAgeReportedUnitCd());
	 * arrayList.add(personDT.getAgeReportedUnitCd());
	 * //preparedStmt.setString(i++, personDT.getBirthGenderCd());
	 * arrayList.add(personDT.getBirthGenderCd());
	 * 
	 * if (personDT.getBirthOrderNbr() == null) //preparedStmt.setNull(i++,
	 * Types.INTEGER); arrayList.add(personDT.getBirthOrderNbr()); else
	 * //preparedStmt.setInt(i++, (personDT.getBirthOrderNbr()).intValue());
	 * arrayList.add(personDT.getBirthOrderNbr());
	 * 
	 * //preparedStmt.setTimestamp(i++, personDT.getBirthTime());
	 * arrayList.add(personDT.getBirthTime()); //preparedStmt.setTimestamp(i++,
	 * personDT.getBirthTimeCalc()); arrayList.add(personDT.getBirthTimeCalc());
	 * //preparedStmt.setString(i++, personDT.getCd());
	 * arrayList.add(personDT.getCd()); //preparedStmt.setString(i++,
	 * personDT.getCdDescTxt()); arrayList.add(personDT.getCdDescTxt());
	 * //preparedStmt.setString(i++, personDT.getCurrSexCd());
	 * arrayList.add(personDT.getCurrSexCd()); //preparedStmt.setString(i++,
	 * personDT.getDeceasedIndCd()); arrayList.add(personDT.getDeceasedIndCd());
	 * //preparedStmt.setTimestamp(i++, personDT.getDeceasedTime());
	 * arrayList.add(personDT.getDeceasedTime()); //preparedStmt.setString(i++,
	 * personDT.getDescription()); arrayList.add(personDT.getDescription());
	 * //preparedStmt.setString(i++, personDT.getEducationLevelCd());
	 * arrayList.add(personDT.getEducationLevelCd());
	 * //preparedStmt.setString(i++, personDT.getEducationLevelDescTxt());
	 * arrayList.add(personDT.getEducationLevelDescTxt());
	 * //preparedStmt.setString(i++, personDT.getEthnicGroupInd());
	 * arrayList.add(personDT.getEthnicGroupInd());
	 * //preparedStmt.setString(i++, personDT.getLastChgReasonCd());
	 * arrayList.add(personDT.getLastChgReasonCd());
	 * //preparedStmt.setTimestamp(i++, personDT.getLastChgTime());
	 * arrayList.add(personDT.getLastChgTime());
	 * 
	 * if (personDT.getLastChgUserId() == null) //preparedStmt.setNull(i++,
	 * Types.INTEGER); arrayList.add(personDT.getLastChgUserId()); else
	 * //preparedStmt.setLong(i++, (personDT.getLastChgUserId()).longValue());
	 * arrayList.add(personDT.getLastChgUserId());
	 * 
	 * //preparedStmt.setString(i++, personDT.getMaritalStatusCd());
	 * arrayList.add(personDT.getMaritalStatusCd());
	 * //preparedStmt.setString(i++, personDT.getMaritalStatusDescTxt());
	 * arrayList.add(personDT.getMaritalStatusDescTxt());
	 * //preparedStmt.setString(i++, personDT.getMothersMaidenNm());
	 * arrayList.add(personDT.getMothersMaidenNm());
	 * //preparedStmt.setString(i++, personDT.getMultipleBirthInd());
	 * arrayList.add(personDT.getMultipleBirthInd());
	 * //preparedStmt.setString(i++, personDT.getOccupationCd());
	 * arrayList.add(personDT.getOccupationCd());
	 * 
	 * // //preparedStmt.setString(i++, personDT.getOrgAccesPermis());
	 * //preparedStmt.setString(i++, personDT.()getPreferredGenderCd());
	 * arrayList.add(personDT.getPreferredGenderCd());
	 * //preparedStmt.setString(i++, personDT.getPrimLangCd());
	 * arrayList.add(personDT.getPrimLangCd()); //preparedStmt.setString(i++,
	 * personDT.getPrimLangDescTxt());
	 * arrayList.add(personDT.getPrimLangDescTxt());
	 * 
	 * // //preparedStmt.setString(i++, personDT.getProgAreaAccessPermis());
	 * //preparedStmt.setString(i++, personDT.getRecordStatusCd());
	 * arrayList.add(personDT.getRecordStatusCd());
	 * //preparedStmt.setTimestamp(i++, personDT.getRecordStatusTime());
	 * arrayList.add(personDT.getRecordStatusTime());
	 * //preparedStmt.setString(i++, personDT.getStatusCd());
	 * arrayList.add(personDT.getStatusCd());
	 * 
	 * if (personDT.getStatusTime() == null) //preparedStmt.setNull(i++,
	 * Types.TIMESTAMP); arrayList.add(personDT.getStatusTime()); else
	 * //preparedStmt.setTimestamp(i++, personDT.getStatusTime());
	 * arrayList.add(personDT.getStatusTime());
	 * 
	 * //preparedStmt.setString(i++, personDT.getSurvivedIndCd());
	 * arrayList.add(personDT.getSurvivedIndCd()); //preparedStmt.setString(i++,
	 * personDT.getUserAffiliationTxt());
	 * arrayList.add(personDT.getUserAffiliationTxt());
	 * //preparedStmt.setString(i++, personDT.getFirstNm());
	 * personDT.setFirstNm("Tom"); arrayList.add(personDT.getFirstNm());
	 * System.out.println("Updating person part 2" +
	 * (personDT.getPersonUid()).longValue()); //preparedStmt.setString(i++,
	 * personDT.getLastNm()); personDT.setLastNm("Jones");
	 * arrayList.add(personDT.getLastNm()); //preparedStmt.setString(i++,
	 * personDT.getMiddleNm()); arrayList.add(personDT.getMiddleNm());
	 * //preparedStmt.setString(i++, personDT.getNmPrefix());
	 * arrayList.add(personDT.getNmPrefix()); //preparedStmt.setString(i++,
	 * personDT.getNmSuffix()); arrayList.add(personDT.getNmSuffix());
	 * //preparedStmt.setString(i++, personDT.getPreferredNm());
	 * arrayList.add(personDT.getPreferredNm()); //preparedStmt.setString(i++,
	 * personDT.getHmStreetAddr1()); arrayList.add(personDT.getHmStreetAddr1());
	 * //preparedStmt.setString(i++, personDT.getHmStreetAddr2());
	 * arrayList.add(personDT.getHmStreetAddr2()); //preparedStmt.setString(i++,
	 * personDT.getHmCityCd()); arrayList.add(personDT.getHmCityCd());
	 * //preparedStmt.setString(i++, personDT.getHmCityDescTxt());
	 * arrayList.add(personDT.getHmCityDescTxt()); //preparedStmt.setString(i++,
	 * personDT.getHmStateCd()); arrayList.add(personDT.getHmStateCd());
	 * //preparedStmt.setString(i++, personDT.getHmZipCd());
	 * arrayList.add(personDT.getHmZipCd()); //preparedStmt.setString(i++,
	 * personDT.getHmCntyCd()); arrayList.add(personDT.getHmCntyCd());
	 * //preparedStmt.setString(i++, personDT.getHmCntryCd());
	 * arrayList.add(personDT.getHmCntryCd()); //preparedStmt.setString(i++,
	 * personDT.getHmPhoneNbr()); arrayList.add(personDT.getHmPhoneNbr());
	 * //preparedStmt.setString(i++, personDT.getHmPhoneCntryCd());
	 * arrayList.add(personDT.getHmPhoneCntryCd());
	 * //preparedStmt.setString(i++, personDT.getHmEmailAddr());
	 * arrayList.add(personDT.getHmEmailAddr()); //preparedStmt.setString(i++,
	 * personDT.getCellPhoneNbr()); arrayList.add(personDT.getCellPhoneNbr());
	 * //preparedStmt.setString(i++, personDT.getWkStreetAddr1());
	 * arrayList.add(personDT.getWkStreetAddr1()); //preparedStmt.setString(i++,
	 * personDT.getWkStreetAddr2()); arrayList.add(personDT.getWkStreetAddr2());
	 * //preparedStmt.setString(i++, personDT.getWkCityCd());
	 * arrayList.add(personDT.getWkCityCd()); //preparedStmt.setString(i++,
	 * personDT.getWkCityDescTxt()); arrayList.add(personDT.getWkCityDescTxt());
	 * //preparedStmt.setString(i++, personDT.getWkStateCd());
	 * arrayList.add(personDT.getWkStateCd()); //preparedStmt.setString(i++,
	 * personDT.getWkZipCd()); arrayList.add(personDT.getWkZipCd());
	 * //preparedStmt.setString(i++, personDT.getWkCntyCd());
	 * arrayList.add(personDT.getWkCntyCd()); //preparedStmt.setString(i++,
	 * personDT.getWkCntryCd()); arrayList.add(personDT.getWkCntryCd());
	 * //preparedStmt.setString(i++, personDT.getWkPhoneNbr());
	 * arrayList.add(personDT.getWkPhoneNbr()); //preparedStmt.setString(i++,
	 * personDT.getWkPhoneCntryCd());
	 * arrayList.add(personDT.getWkPhoneCntryCd());
	 * //preparedStmt.setString(i++, personDT.getWkEmailAddr());
	 * arrayList.add(personDT.getWkEmailAddr()); //preparedStmt.setString(i++,
	 * personDT.getSSN()); arrayList.add(personDT.getSSN());
	 * //preparedStmt.setString(i++, personDT.getMedicaidNum());
	 * arrayList.add(personDT.getMedicaidNum());
	 * System.out.println("Updating person part 3" +
	 * (personDT.getPersonUid()).longValue()); //preparedStmt.setString(i++,
	 * personDT.getDlNum()); arrayList.add(personDT.getDlNum());
	 * //preparedStmt.setString(i++, personDT.getDlStateCd());
	 * arrayList.add(personDT.getDlStateCd()); //preparedStmt.setString(i++,
	 * personDT.getRaceCd()); arrayList.add(personDT.getRaceCd());
	 * 
	 * if (personDT.getRaceSeqNbr() == null) //preparedStmt.setNull(i++,
	 * Types.INTEGER); arrayList.add(personDT.getRaceSeqNbr()); else
	 * //preparedStmt.setInt(i++, (personDT.getRaceSeqNbr()).intValue());
	 * arrayList.add(personDT.getRaceSeqNbr());
	 * 
	 * //preparedStmt.setString(i++, personDT.getRaceCategoryCd());
	 * arrayList.add(personDT.getRaceCategoryCd());
	 * //preparedStmt.setString(i++, personDT.getEthnicityGroupCd());
	 * arrayList.add(personDT.getEthnicityGroupCd());
	 * 
	 * if (personDT.getEthnicGroupSeqNbr() == null) //preparedStmt.setNull(i++,
	 * Types.INTEGER); arrayList.add(personDT.getEthnicGroupSeqNbr()); else
	 * //preparedStmt.setInt(i++, (personDT.getEthnicGroupSeqNbr()).intValue());
	 * arrayList.add(personDT.getEthnicGroupSeqNbr());
	 * 
	 * if (personDT.getAdultsInHouseNbr() == null) //preparedStmt.setNull(i++,
	 * Types.INTEGER); arrayList.add(personDT.getAdultsInHouseNbr()); else
	 * //preparedStmt.setInt(i++, (personDT.getAdultsInHouseNbr()).intValue());
	 * arrayList.add(personDT.getAdultsInHouseNbr());
	 * 
	 * if (personDT.getChildrenInHouseNbr() == null) //preparedStmt.setNull(i++,
	 * Types.INTEGER); arrayList.add(personDT.getChildrenInHouseNbr()); else
	 * //preparedStmt.setInt(i++,(personDT.getChildrenInHouseNbr()).intValue());
	 * arrayList.add(personDT.getChildrenInHouseNbr());
	 * 
	 * //preparedStmt.setString(i++, personDT.getBirthCityCd());
	 * arrayList.add(personDT.getBirthCityCd()); //preparedStmt.setString(i++,
	 * personDT.getBirthCityDescTxt());
	 * arrayList.add(personDT.getBirthCityDescTxt());
	 * //preparedStmt.setString(i++, personDT.getBirthCntryCd());
	 * arrayList.add(personDT.getBirthCntryCd()); //preparedStmt.setString(i++,
	 * personDT.getBirthStateCd()); arrayList.add(personDT.getBirthStateCd());
	 * //preparedStmt.setString(i++, personDT.getRaceDescTxt());
	 * arrayList.add(personDT.getRaceDescTxt()); //preparedStmt.setString(i++,
	 * personDT.getEthnicGroupDescTxt());
	 * arrayList.add(personDT.getEthnicGroupDescTxt());
	 * //preparedStmt.setInt(i++,(personDT.getVersionCtrlNbr().intValue()));
	 * arrayList.add(personDT.getVersionCtrlNbr()); if (personDT.getPersonUid()
	 * == null) //preparedStmt.setNull(i++, Types.INTEGER);
	 * arrayList.add(personDT.getPersonUid()); else
	 * //preparedStmt.setLong(i++,(personDT.getPersonUid()).longValue());
	 * arrayList.add(personDT.getPersonUid());
	 * 
	 * //preparedStmt.setInt(i++, personDT.getVersionCtrlNbr().intValue()-1);
	 * arrayList.add(personDT.getVersionCtrlNbr());
	 * 
	 * System.out.println(
	 * "loaded the dt - passing everybody to preparedStmtMethod ---<-<-<->->---"
	 * ); int resultCount = ((Integer)daoBase.preparedStmtMethod(personDT,
	 * arrayList, UPDATE_PERSON, "UPDATE")).intValue();
	 * 
	 * if (resultCount != 1) {
	 * logger.error("Error: none or more than one person updated at a time, " +
	 * "resultCount = " + resultCount); throw newNEDSSConcurrentDataException(
	 * "NEDSSConcurrentDataException: The data has been modified by other user, please verify!"
	 * ); } } } catch (Exception ex) {
	 * logger.fatal("Exception in main:  ERROR = " + ex); throw new
	 * NEDSSSystemException(ex.toString()); } }
	 */
	public Object preparedStmtMethodRetUid(RootDTInterface rootDTInterface,
			ArrayList<Object> arrayList, String statement, String queryType)
			throws NEDSSDAOSysException {

		// get a connection
		Connection dbConnection = null;
		PreparedStatement preparedStmt = null;
		ResultSet resultSet = null;

		try {
			dbConnection = getConnection();

			// sql statement passed in
			preparedStmt = dbConnection.prepareStatement(statement);
			// only for debugging purposes
			logger.debug("DAOBase.preparedStmtMethod.statement is :"
					+ statement);
			logger.debug("DAOBase.preparedStmtMethod.queryType is :"
					+ queryType);
			logger.debug("DAOBase.preparedStmtMethod.rootDTInterface is :"
					+ rootDTInterface);

			// array of objects passed in for statement parameters
			if (arrayList != null) {
				Iterator<?> it = arrayList.iterator();
				for (int i = 1; it.hasNext(); i++) {
					Object obj = it.next();
					if (obj == null) {
						// if it is null setNull
						preparedStmt.setNull(i, Types.VARCHAR);
					}
					// if String with "" vlaue set it to null.
					// Struts may be giving blank String instead of null
					// Oracle does it for you, SQL server does not.
					else if (obj.getClass().toString().equals(
							"class java.lang.String")) {
						if (((String) obj).trim().length() == 0)
							preparedStmt.setNull(i, Types.VARCHAR);
						else
							preparedStmt.setObject(i, obj.toString());
					} else {
						// if it is a timestamp treat it special (mainly for
						// oracle)
						String timeStamp = obj.getClass().toString();
						if (obj.getClass().toString().equals(
								"class java.sql.Timestamp")) {
							preparedStmt.setTimestamp(i,
									(java.sql.Timestamp) obj);
						} else {
							// else pass them all as strings driver will convert
							// to proper type
							// preparedStmt.setObject(i, obj.toString());
							String value = obj.toString();
							if (value == null || value.trim().length() == 0)
								preparedStmt.setNull(i, Types.VARCHAR);
							else
								preparedStmt.setObject(i, value);

						}
					}
				}
			}

			// else if it is an insert or update then you want to use
			// executeUpdate to get an int

				int result = preparedStmt.executeUpdate();
				logger.debug("preparedStmt.executeUpdate() - result = "
						+ result);
				  try {
					  resultSet = preparedStmt.getGeneratedKeys();

						if (resultSet.next()) {
							// return CT_contact_attachment.ct_contact_attachment_uid
							Long insertedRecordUid = new Long(resultSet.getLong(1));
							resultSet.close();
							return insertedRecordUid;
						} else {
							resultSet.close();
							throw new NEDSSDAOAppException("Inserting to the table :  failed to retrieve the generated key."); 
						}
					} catch (Exception e) {
						throw new NEDSSDAOAppException("Inserting to the table:  failed to retrieve the generated key."); 
					}	

				


		} catch (SQLException sex) {
			if (logSQLException(statement))
				logger.fatal("SQLException executing: " + statement
						+ " ERROR = " + sex.getMessage(), sex);
			throw new NEDSSDAOSysException(sex.toString());
		} catch (Exception ex) {
			logger.fatal("Exception  executing: " + statement
						+ "Error ="+ ex.getMessage(), ex);
			throw new NEDSSSystemException(ex.toString());
		}
		// clean house
		finally {
			closeResultSet(resultSet);
			closeStatement(preparedStmt);
			releaseConnection(dbConnection);
		}
	}

}