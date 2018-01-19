package gov.cdc.nedss.systemservice.dao;

import gov.cdc.nedss.exception.NEDSSDAOSysException;
import gov.cdc.nedss.exception.NEDSSSystemException;
import gov.cdc.nedss.pagemanagement.wa.dao.PageManagementDAOImpl;
import gov.cdc.nedss.systemservice.dt.EDXActivityDetailLogDT;
import gov.cdc.nedss.systemservice.dt.EDXActivityLogDT;
import gov.cdc.nedss.util.DAOBase;
import gov.cdc.nedss.util.LogUtils;
import gov.cdc.nedss.util.NEDSSConstants;
import gov.cdc.nedss.util.PropertyUtil;

import java.io.ByteArrayInputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.sql.Types;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;

public class EDXActivityLogDAOImpl extends DAOBase {

	private static PropertyUtil propertyUtil = PropertyUtil.getInstance();
	private static final LogUtils logger = new LogUtils(PageManagementDAOImpl.class.getName());

	public long insertEDXActivityLogDT(EDXActivityLogDT dt) throws NEDSSSystemException {

		String INSERT_EDX_ACTIVITY_LOG_SQL = 
				"INSERT INTO  " 				+
				"	EDX_activity_log " 			+
				"	( " 						+
				"		source_uid, " 			+ // 1
				"		target_uid, " 			+ // 2
				"		doc_type, " 			+ // 3
				"		record_status_cd, " 	+ // 4
				"		record_status_time, " 	+ // 5
				"		exception_txt, " 		+ // 6
				"		imp_exp_ind_cd, " 		+ // 7
				"		source_type_cd, " 		+ // 8
				"		target_type_cd, " 		+ // 9
				"		business_obj_localId, " + // 10
				"		Message_id, " 			+ // 11
				"		Entity_nm, " 			+ // 12
				"		Accession_nbr, " 		+ // 13
				"		doc_nm, " 				+ // 14
				"		source_nm " 			+ // 15
				"	) " +
				//      1  2  3  4  5  6  7  8  9  10 11 12 13 14 15
				"VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?);";
		
		String INSERT_EDX_ACTIVITY_LOG_ORA = 
				"INSERT INTO  " +
				"	EDX_activity_log " +
				"	(" +
				"		source_uid, " +					// o01
				"		target_uid, " +					// o02
				"		doc_type, " +					// o03
				"		record_status_cd, " +			// o04
				"		record_status_time, " +			// o05
				"		exception_txt, " +				// o06
				"		imp_exp_ind_cd, " +				// o07
				"		source_type_cd, " +				// o08
				"		target_type_cd, " +				// o09
				"		business_obj_localId, " +		// o10
				"		Message_id, " +					// o11
				"		Entity_nm, " +					// o12
				"		Accession_nbr, " +				// o13
				"		doc_nm, " 				+ 		// 14
				"		source_nm " 			+ 		// 15
				"	) " +
				//      1  2  3  4  5  6             7  8  9  10 11 12 13 14 15
				"VALUES(?, ?, ?, ?, ?, EMPTY_CLOB(), ?, ?, ?, ?, ?, ?, ?, ?, ?) ";
		
		String GET_EDX_ACTIVITY_LOG_PK_ORA = 
				"SELECT MAX(EDX_ACTIVITY_LOG_UID) FROM EDX_ACTIVITY_LOG";
		Connection dbConnection = null;
		PreparedStatement pStmt = null;
		Statement statement = null;
		Statement stmt2 = null;
		ResultSet resultset = null;
		ResultSet blobRS = null;
		int resultCount = 0;
		int i = 1;
		String sql = "";
		Long activityUid = null;

		try {
			dbConnection = getConnection();
			boolean isOracle = false;
			if (propertyUtil.getDatabaseServerType() != null && propertyUtil.getDatabaseServerType().equalsIgnoreCase(NEDSSConstants.ORACLE_ID)) {
				isOracle = true;
				sql = INSERT_EDX_ACTIVITY_LOG_ORA;
				pStmt = dbConnection.prepareStatement(sql);			
			} else {
				sql = INSERT_EDX_ACTIVITY_LOG_SQL;
				pStmt = dbConnection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
			}

			if (dt.getSourceUid() != null)
				pStmt.setLong(i++, dt.getSourceUid().longValue());		// o01
			else
				pStmt.setLong(i++, Types.BIGINT);
			
			if (dt.getTargetUid() != null)								// o02
				pStmt.setLong(i++, dt.getTargetUid().longValue());
			else
				pStmt.setLong(i++, Types.BIGINT);

			pStmt.setString(i++, dt.getDocType());						// o04
			pStmt.setString(i++, dt.getRecordStatusCd());				// o05
			pStmt.setTimestamp(i++, dt.getRecordStatusTime());			// o06
			if (propertyUtil.getDatabaseServerType() != null && !propertyUtil.getDatabaseServerType().equalsIgnoreCase(NEDSSConstants.ORACLE_ID)) {
				pStmt.setString(i++, dt.getException());
			}
			pStmt.setString(i++, dt.getImpExpIndCd());					// o07
			pStmt.setString(i++, dt.getSourceTypeCd());					// o08
			pStmt.setString(i++, dt.getTargetTypeCd());					// o09
			pStmt.setString(i++, dt.getBusinessObjLocalId());			// o10
			pStmt.setString(i++, dt.getMessageId());					// o11
			pStmt.setString(i++, dt.getEntityNm());						// o11
			pStmt.setString(i++, dt.getAccessionNbr());					// o12
			pStmt.setString(i++, dt.getDocName());						// o14
			pStmt.setString(i++, dt.getSrcName());						// o15
			
			if (isOracle) {
				resultCount	 = pStmt.executeUpdate();
				
				if (resultCount > 0) {
					pStmt = dbConnection.prepareStatement(GET_EDX_ACTIVITY_LOG_PK_ORA);
					ResultSet	rs = pStmt.executeQuery();
					rs.next();
					activityUid = rs.getLong(1);
					if (rs != null)
						closeResultSet(rs);
				}
			} else {
				resultCount = pStmt.executeUpdate();			
				ResultSet	keyRS	= pStmt.getGeneratedKeys();
				if (keyRS.next()) {
			    	activityUid			= keyRS.getLong(1);
				}
				if (keyRS != null)
					closeResultSet(keyRS);
			}

			if (resultCount != 1) {
				throw new NEDSSSystemException("Error: EDXActivityLogDAOImpl: none or more than one entity inserted at a time, resultCount = " + resultCount);
			}
		} catch (SQLException se) {
			logger.fatal("SQLException  = "+se.getMessage(), se);
			throw new NEDSSDAOSysException("Error: EDXActivityLogDAOImpl: SQLException while inserting into Export Algorithm table (1)\n" + se.getMessage(), se);
		} finally {
			
			closeStatement(pStmt);
			releaseConnection(dbConnection);
		} // end of finally
		if (propertyUtil.getDatabaseServerType() != null && propertyUtil.getDatabaseServerType().equalsIgnoreCase(NEDSSConstants.ORACLE_ID)) {

			try {
				if (dt.getException() != null) {
					// get another dbconnection for "select for update"
					dbConnection = getConnection();
					boolean autoCommit = dbConnection.getAutoCommit();
					if (autoCommit == true) {
						dbConnection.setAutoCommit(false);
					}
					/*
					 * statement = dbConnection.createStatement(); resultset =
					 * statement.executeQuery( "select exception_txt from " +
					 * "EDX_Activity_Log WHERE EDX_Activity_Log_uid = " +
					 * activityUid + " for update nowait"); resultset.next();
					 */
					stmt2 = dbConnection.createStatement();
					stmt2.execute("SELECT exception_txt FROM EDX_ACTIVITY_LOG WHERE edx_activity_log_uid =" + activityUid + " FOR UPDATE");
					blobRS = stmt2.getResultSet();
					java.sql.Clob clob = null;
					while (blobRS.next()) {
						clob = (java.sql.Clob) (blobRS.getClob(1));
					}
					String exception = dt.getException();
					clob.setString(1, exception);
					clob.truncate(exception.length());
					// dbConnection.setAutoCommit(autoCommit); //set autoCommit
					// to what is used to be
					// dbConnection.commit();

				}
			} catch (SQLException sqlex) {
				logger.fatal("SQLException  Insert table failed 2nd = "+sqlex.getMessage(), sqlex);
				throw new NEDSSDAOSysException("Insert table failed 2nd", sqlex);
			} catch (Exception ex) {
				logger.fatal("SQLException  Insert into table Failed 2nd = "+ex.getMessage(), ex);

				throw new NEDSSSystemException("Insert into table Failed 2nd", ex);
			} finally {
				closeResultSet(blobRS);
				closeStatement(stmt2);
				closeResultSet(resultset);
				closeStatement(statement);
				releaseConnection(dbConnection);
			}
		}
		return activityUid;

	}

	/**
	 * creates an NBS UiMetadata
	 * 
	 * @param dt
	 * @throws NEDSSDAOSysException
	 * @throws NEDSSSystemException
	 */
	public Long insertActivityLogUid(String templateNm) throws NEDSSDAOSysException, NEDSSSystemException {
		String INSERT_EDX_ACTIVITY_LOG_SQL = 
				"INSERT INTO  " +
				"	EDX_activity_log " +
				"	( " +
				"		source_uid, " +
				"		target_uid, " +
				"		doc_type, " +
				"		doc_nm, " +
				"		record_status_cd, " +
				"		record_status_time, " +
				"		exception_txt, " +
				"		imp_exp_ind_cd, " +
				"		source_type_cd," +
				"		source_nm, " +
				"		target_type_cd, " +
				"		business_obj_localId " +
				"	) " +
				"VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?);";

		if (propertyUtil.getDatabaseServerType() != null && propertyUtil.getDatabaseServerType().equalsIgnoreCase(NEDSSConstants.ORACLE_ID))
			return insertOracleActivityLogUid(templateNm);
		else {
			// return insertSQLNBSDocument(nbsDocumentDT);

			Long waQuestionUid = null;

			ArrayList<Object> paramList = new ArrayList<Object>();
			//EDXActivityLogDT dt = new EDXActivityLogDT();
			Timestamp currentTime = new Timestamp(new Date().getTime());
			// paramList.add(new Long(waQuestionUid));
			paramList.add(null);
			paramList.add(null);
			paramList.add("Template");
			paramList.add(templateNm);
			paramList.add("In Progress");
			paramList.add(currentTime);
			paramList.add(null);
			paramList.add("I");
			paramList.add(null);
			paramList.add(propertyUtil.getMsgSendingFacility());
			paramList.add("Template");
			paramList.add(null);

			try {

				waQuestionUid = ((Long) preparedStmtMethodRetUid(null, paramList, INSERT_EDX_ACTIVITY_LOG_SQL + "  SELECT @@IDENTITY AS 'Identity'", NEDSSConstants.UPDATE));
				if (waQuestionUid == null) {
					logger.error("Exception in createWaQuestion: , " + "waQuestionUid = " + waQuestionUid);
					throw new NEDSSSystemException("Exception in createWaQuestion:");
				}

			} catch (Exception ex) {
				logger.fatal("templateNm: "+templateNm+" Exception in createWaQuestion: ERROR = " + ex.getMessage(), ex);
				throw new NEDSSSystemException(ex.toString(), ex);
			}
			return waQuestionUid;
		}
	}

	public Long insertOracleActivityLogUid(String templateNm) throws NEDSSDAOSysException, NEDSSSystemException {
		String INSERT_EDX_ACTIVITY_LOG_ORA = 
				"INSERT INTO  " +
				"	EDX_activity_log " +
				"	(" +
				"		source_uid, " +
				"		target_uid, " +
				"		doc_type, " +
				"		doc_nm, " +
				"		record_status_cd, " +
				"		record_status_time, " +
				"		exception_txt, " +
				"		imp_exp_ind_cd, " +
				"		source_type_cd, " +
				"		source_nm, " +
				"		target_type_cd, " +
				"		business_obj_localId " +
				"	) " +
				"	VALUES(?, ?, ?, ?, ?, ?, EMPTY_CLOB(), ?, ?, ?, ?, ?)";
		
		Long waQuestionUid = null;

		Connection dbConnection = null;
		Statement statement = null;
		ResultSet resultSet = null;
		Timestamp currentTime = new Timestamp(new Date().getTime());

		ArrayList<Object> paramList = new ArrayList<Object>();
		paramList.add(null);
		paramList.add(null);
		paramList.add("Template");
		paramList.add(templateNm);
		paramList.add("In Progress");
		paramList.add(currentTime);
		// paramList.add(null);
		paramList.add("I");
		paramList.add(null);
		paramList.add(propertyUtil.getMsgSendingFacility());
		paramList.add("Template");
		paramList.add(null);
		try {

			preparedStmtMethod(null, paramList, INSERT_EDX_ACTIVITY_LOG_ORA, NEDSSConstants.UPDATE);
			dbConnection = getConnection();
			statement = dbConnection.createStatement();
			statement.execute("select max(edx_activity_log_uid) from EDX_activity_log");
			resultSet = statement.getResultSet();
			while (resultSet.next()) {
				waQuestionUid = new Long(resultSet.getLong(1));
			}
			releaseConnection(dbConnection);
			statement.close();
			resultSet.close();

		} catch (Exception ex) {
			logger.fatal("templateNm: "+templateNm+" Exception in insertOracleWaQuestion: ERROR = " + ex.getMessage(), ex);
			throw new NEDSSSystemException(ex.toString(), ex);
		}

		return waQuestionUid;
	}

	public void insertEDXActivityLog(EDXActivityLogDT dt) throws NEDSSSystemException {

		String UPDATE_EDX_ACTIVITY_LOG_SQL = 
				"update  " 						+
				"	EDX_activity_log  " 		+
				"set " 							+
				"	source_uid=?, " 			+	// 01
				"	target_uid=?, " 			+	// 02
				"	doc_type=?, " 				+	// 03
				"	doc_nm=?, " 				+	// 04
				"	record_status_cd=?, " 		+	// 05
				"	record_status_time=?, " 	+	// 06
				"	exception_txt=?, " 			+	// 07
				"	imp_exp_ind_cd=?, " 		+	// 08
				"	source_type_cd=?, " 		+	// 09
				"	source_nm=?, " 				+	// 10
				"	target_type_cd=?, " 		+	// 11
				"	business_obj_localId=?, " 	+	// 12
				"	algorithm_action=?, " 		+	// 13
				"	algorithm_name=?, " 		+	// 14
				"	Message_id=?, " 			+	// 15
				"	Entity_nm=?, " 				+	// 16
				"	Accession_nbr=? " 			+	// 17
				"where " 						+
				"	EDX_activity_log_uid =? ";		// 18
		
		String UPDATE_EDX_ACTIVITY_LOG_ORA = 
				"update  " 						+
				"	EDX_activity_log  " 		+	
				"set " 							+
				"	source_uid=?, " 			+	// 01
				"	target_uid=?, " 			+	// 02
				"	doc_type=?, " 				+	// 03
				"	doc_nm=?, " 				+	// 04
				"	record_status_cd=?, " 		+	// 05
				"	record_status_time=?, " 	+	// 06
				"	imp_exp_ind_cd=?, " 		+	// 07
				"	source_type_cd=?, " 		+	// 08
				"	source_nm=?, " 				+	// 09
				"	target_type_cd=?, " 		+	// 10
				"	business_obj_localId=?, " 	+	// 11
				"	algorithm_action=?, " 		+	// 12
				"	algorithm_name=?, " 		+	// 13
				"	Message_id=?, " 			+	// 14
				"	Entity_nm=?, " 				+	// 15
				"	Accession_nbr=? " 			+	// 16
				"where " 						+
				"	EDX_activity_log_uid =? ";		// 17
		
		String INSERT_EDX_ACTIVITY_LOG_DETAILS_SQL = 
				"INSERT INTO  " 				+
				"	EDX_activity_detail_log " 	+
				"	( " 						+
				"		EDX_activity_log_uid, " +
				"		record_id, " 			+
				"		record_type, " 			+
				"		record_nm, " 			+
				"		log_type, " 			+
				"		log_comment " 			+		 
				" 	) " 						+
				"VALUES(?,?,?,?,?,?);";
		
		String INSERT_EDX_ACTIVITY_LOG_DETAILS_ORA = 
				"INSERT INTO  " 				+
				"	EDX_activity_detail_log " 	+
				"	( " 						+
				"		EDX_activity_log_uid, " +
				"		record_id, " 			+
				"		record_type, " 			+
				"		record_nm, " 			+
				"		log_type, " 			+
				"		log_comment " 			+
				"	) " 						+
				"VALUES(?,?,?,?,?,?)";

		Connection dbConnection = null;
		PreparedStatement pStmt = null;
		Statement statement = null;
		Statement stmt2 = null;
		ResultSet resultset = null;
		ResultSet blobRS = null;
		int resultCount = 0;
		int i = 1;
		String edxActivityLogUpdateSql = "";
		String edxActivityLogDTDetailsSql = "";
		//Long activityUid = null;

		if (propertyUtil.getDatabaseServerType() != null && propertyUtil.getDatabaseServerType().equalsIgnoreCase(NEDSSConstants.ORACLE_ID)) {
			edxActivityLogUpdateSql = UPDATE_EDX_ACTIVITY_LOG_ORA;
			edxActivityLogDTDetailsSql = INSERT_EDX_ACTIVITY_LOG_DETAILS_ORA;
		} else {
			edxActivityLogUpdateSql = UPDATE_EDX_ACTIVITY_LOG_SQL;
			edxActivityLogDTDetailsSql = INSERT_EDX_ACTIVITY_LOG_DETAILS_SQL;
		}
		try {
			dbConnection = getConnection();
			if (dt.getRecordStatusCd().equalsIgnoreCase("Success") || dt.isLogDetailAllStatus()) {
				// insertion to EdxActivityLogDTDetails starts here
				Collection<Object> edxActivityLogDTDetails = new ArrayList<Object>();
				edxActivityLogDTDetails = dt.getEDXActivityLogDTWithVocabDetails();
				if (edxActivityLogDTDetails != null) {
					Iterator<Object> it = edxActivityLogDTDetails.iterator();
					while (it.hasNext()) {
						EDXActivityDetailLogDT ddt = (EDXActivityDetailLogDT) it.next();
						pStmt = dbConnection.prepareStatement(edxActivityLogDTDetailsSql);

						if (dt.getEdxActivityLogUid() != null && dt.getEdxActivityLogUid() > 0) {
							pStmt.setLong(i++, dt.getEdxActivityLogUid().longValue());			// 01
						} else {
							pStmt.setLong(i++, ddt.getEdxActivityLogUid().longValue());			// 01
						}
						pStmt.setString(i++, ddt.getRecordId());								// 02
						pStmt.setString(i++, ddt.getRecordType());								// 03
						pStmt.setString(i++, ddt.getRecordName());								// 04
						pStmt.setString(i++, ddt.getLogType());									// 05
						pStmt.setString(i++, ddt.getComment());									// 06
						try {
							pStmt.executeUpdate();
							closeStatement(pStmt);
							i = 1;
						} catch (SQLException se) {
							se.printStackTrace();
							throw new NEDSSDAOSysException("Error: EDXActivityLogDAOImpl: SQLException while inserting into Export Algorithm table (2)\n" + se.getMessage(), se);
						}
					}
				}// insertion to EdxActivityLogDTDetails ends here

				edxActivityLogDTDetails = dt.getEDXActivityLogDTWithQuesDetails();
				if (edxActivityLogDTDetails != null) {
					Iterator<Object> it = edxActivityLogDTDetails.iterator();
					while (it.hasNext()) {
						EDXActivityDetailLogDT ddt = (EDXActivityDetailLogDT) it.next();
						pStmt = dbConnection.prepareStatement(edxActivityLogDTDetailsSql);

						pStmt.setLong(i++, ddt.getEdxActivityLogUid().longValue());
						pStmt.setString(i++, ddt.getRecordId());
						pStmt.setString(i++, ddt.getRecordType());
						pStmt.setString(i++, ddt.getRecordName());
						pStmt.setString(i++, ddt.getLogType());
						pStmt.setString(i++, ddt.getComment());
						try {
							pStmt.executeUpdate();
							closeStatement(pStmt);
							i = 1;
						} catch (SQLException se) {
							se.printStackTrace();
							throw new NEDSSDAOSysException("Error: EDXActivityLogDAOImpl: SQLException while inserting into Export Algorithm table (3)\n" + se.getMessage(), se);
						}
					}
				}// insertion to EdxActivityLogDTDetails ends here
			}

			if (dt.getEdxActivityLogUid() != null) {
				pStmt = dbConnection.prepareStatement(edxActivityLogUpdateSql);
				if (dt.getSourceUid() != null)
					pStmt.setLong(i++, dt.getSourceUid().longValue());		// 01
				else
					pStmt.setLong(i++, Types.BIGINT);						// 01
				if (dt.getTargetUid() != null)
					pStmt.setLong(i++, dt.getTargetUid().longValue());		// 02
				else
					pStmt.setLong(i++, Types.BIGINT);						// 02

				pStmt.setString(i++, dt.getDocType());						// 03
				pStmt.setString(i++, dt.getDocName());						// 04
				pStmt.setString(i++, dt.getRecordStatusCd());				// 05
				pStmt.setTimestamp(i++, dt.getRecordStatusTime());			// 06
				if (propertyUtil.getDatabaseServerType() != null 
						&& 
						!propertyUtil.getDatabaseServerType().equalsIgnoreCase(NEDSSConstants.ORACLE_ID)) {
					pStmt.setString(i++, dt.getException());				// 07
				}
				pStmt.setString(i++, dt.getImpExpIndCd());					// 08
				pStmt.setString(i++, dt.getSourceTypeCd());					// 09
				pStmt.setString(i++, dt.getSrcName());						// 10
				pStmt.setString(i++, dt.getTargetTypeCd());					// 11
				pStmt.setString(i++, dt.getBusinessObjLocalId());			// 12
				pStmt.setString(i++, dt.getAlgorithmAction());				// 13
				pStmt.setString(i++, dt.getAlgorithmName());				// 14
				pStmt.setString(i++, dt.getMessageId());					// 15
				pStmt.setString(i++, dt.getEntityNm());						// 16
				pStmt.setString(i++, dt.getAccessionNbr());					// 17
				pStmt.setLong(i++, dt.getEdxActivityLogUid().longValue());	// 18

				resultCount = pStmt.executeUpdate();

				if (resultCount != 1) {
					throw new NEDSSSystemException("Error: EDXActivityLogDAOImpl: none or more than one entity inserted at a time, resultCount = " + resultCount);
				}
			}
		} catch (SQLException se) {
			logger.fatal("SQLException  = "+se.getMessage(), se);
			throw new NEDSSDAOSysException("Error: EDXActivityLogDAOImpl: SQLException while inserting into Export Algorithm table (4)\n" + se.getMessage(), se);
		} finally {
			closeStatement(pStmt);
			releaseConnection(dbConnection);
		} // end of finally
		if (propertyUtil.getDatabaseServerType() != null && propertyUtil.getDatabaseServerType().equalsIgnoreCase(NEDSSConstants.ORACLE_ID)) {

			try {
				if (dt.getException() != null) {
					// get another dbconnection for "select for update"
					dbConnection = getConnection();
					boolean autoCommit = dbConnection.getAutoCommit();
					if (autoCommit == true) {
						dbConnection.setAutoCommit(false);
					}
					/*
					 * statement = dbConnection.createStatement(); resultset =
					 * statement.executeQuery( "select exception_txt from " +
					 * "EDX_Activity_Log WHERE EDX_Activity_Log_uid = " +
					 * activityUid + " for update nowait"); resultset.next();
					 */
					stmt2 = dbConnection.createStatement();
					stmt2.execute("SELECT exception_txt FROM EDX_ACTIVITY_LOG WHERE EDX_activity_log_uid =" + dt.getEdxActivityLogUid().longValue() + " FOR UPDATE");
					blobRS = stmt2.getResultSet();
					java.sql.Clob clob = null;
					while (blobRS.next()) {
						clob = (java.sql.Clob) (blobRS.getClob(1));
					}
					String exception = dt.getException();
					clob.setString(1, exception);
					clob.truncate(exception.length());
					// dbConnection.setAutoCommit(autoCommit); //set autoCommit
					// to what is used to be
					// dbConnection.commit();

				}
			} catch (SQLException sqlex) {
				logger.fatal("SQLException  Insert table failed 2nd = "+sqlex.getMessage(), sqlex);
				throw new NEDSSDAOSysException("Insert table failed 2nd", sqlex);
			} catch (Exception ex) {
				logger.fatal("SQLException  Insert into table Failed 2nd = "+ex.getMessage(), ex);
				throw new NEDSSSystemException("Insert into table Failed 2nd", ex);
			} finally {
				closeResultSet(blobRS);
				closeStatement(stmt2);
				closeResultSet(resultset);
				closeStatement(statement);
				releaseConnection(dbConnection);
			}
		}

	}

	public void insertExportEDXActivityLog(EDXActivityLogDT dt) throws NEDSSSystemException {

		String INSERT_EDX_ACTIVITY_LOG_SQL = "INSERT INTO  EDX_activity_log ( source_uid, target_uid, doc_type,doc_nm, record_status_cd, record_status_time, exception_txt, imp_exp_ind_cd, source_type_cd,source_nm,"
				+ " target_type_cd, business_obj_localId )VALUES(?,?,?,?,?,?,?,?,?,?,?,?);";
		String INSERT_EDX_ACTIVITY_LOG_ORA = "INSERT INTO  EDX_activity_log (source_uid, target_uid, doc_type,doc_nm, record_status_cd, record_status_time, exception_txt, imp_exp_ind_cd, source_type_cd,source_nm,"
				+ " target_type_cd, business_obj_localId )VALUES(?,?,?,?,?,?,EMPTY_CLOB(),?,?,?,?,?)";
		Connection dbConnection = null;
		PreparedStatement pStmt = null;
		Statement statement = null;
		PreparedStatement pStmt1 = null;
		ResultSet resultset = null;
		ResultSet blobRS = null;
		int resultCount = 0;
		int i = 1;
		String sql = "";
		Long activityUid = null;

		if (propertyUtil.getDatabaseServerType() != null && propertyUtil.getDatabaseServerType().equalsIgnoreCase(NEDSSConstants.ORACLE_ID))
			sql = INSERT_EDX_ACTIVITY_LOG_ORA;
		else
			sql = INSERT_EDX_ACTIVITY_LOG_SQL;
		try {
			dbConnection = getConnection();

			pStmt = dbConnection.prepareStatement(sql);
			if (dt.getSourceUid() != null)
				pStmt.setLong(i++, dt.getSourceUid().longValue());
			else
				pStmt.setLong(i++, Types.BIGINT);
			if (dt.getTargetUid() != null)
				pStmt.setLong(i++, dt.getTargetUid().longValue());
			else
				pStmt.setLong(i++, Types.BIGINT);

			pStmt.setString(i++, dt.getDocType());
			pStmt.setString(i++, dt.getDocName());
			pStmt.setString(i++, dt.getRecordStatusCd());
			pStmt.setTimestamp(i++, dt.getRecordStatusTime());
			if (propertyUtil.getDatabaseServerType() != null && !propertyUtil.getDatabaseServerType().equalsIgnoreCase(NEDSSConstants.ORACLE_ID)) {
				pStmt.setString(i++, dt.getException());
			}
			pStmt.setString(i++, dt.getImpExpIndCd());
			pStmt.setString(i++, dt.getSourceTypeCd());
			pStmt.setString(i++, dt.getSrcName());
			pStmt.setString(i++, dt.getTargetTypeCd());
			pStmt.setString(i++, dt.getBusinessObjLocalId());

			resultCount = pStmt.executeUpdate();

			if (resultCount != 1) {
				throw new NEDSSSystemException("Error: EDXActivityLogDAOImpl: none or more than one entity inserted at a time, resultCount = " + resultCount);
			}
		} catch (SQLException se) {
			logger.fatal("SQLException  = "+se.getMessage(), se);
			throw new NEDSSDAOSysException("Error: EDXActivityLogDAOImpl: SQLException while inserting into Export Algorithm table (5)\n" + se.getMessage(), se);
		} finally {
			closeStatement(pStmt);
			releaseConnection(dbConnection);
		} // end of finally
		if (propertyUtil.getDatabaseServerType() != null && propertyUtil.getDatabaseServerType().equalsIgnoreCase(NEDSSConstants.ORACLE_ID)) {

			try {
				if (dt.getException() != null) {
					// get another dbconnection for "select for update"
					dbConnection = getConnection();
					boolean autoCommit = dbConnection.getAutoCommit();
					if (autoCommit == true) {
						dbConnection.setAutoCommit(false);
					}
					/*
					 * statement = dbConnection.createStatement(); resultset =
					 * statement.executeQuery( "select exception_txt from " +
					 * "EDX_Activity_Log WHERE EDX_Activity_Log_uid = " +
					 * activityUid + " for update nowait"); resultset.next();
					 */
					pStmt1 = dbConnection.prepareStatement("SELECT exception_txt FROM EDX_ACTIVITY_LOG WHERE source_uid = ? and target_uid = ? FOR UPDATE");
					pStmt1.setLong(1, dt.getSourceUid().longValue());
					pStmt1.setLong(2, dt.getTargetUid().longValue());
					
					blobRS = pStmt1.executeQuery();
					java.sql.Clob clob = null;
					while (blobRS.next()) {
						clob = (java.sql.Clob) (blobRS.getClob(1));
					}
					String exception = dt.getException();
					clob.setString(1, exception);
					clob.truncate(exception.length());
					// dbConnection.setAutoCommit(autoCommit); //set autoCommit
					// to what is used to be
					// dbConnection.commit();

				}
			} catch (SQLException sqlex) {
				logger.fatal("SQLException  Insert table failed 2nd = "+sqlex.getMessage(), sqlex);
				throw new NEDSSDAOSysException("Insert table failed 2nd", sqlex);
			} catch (Exception ex) {
				logger.fatal("SQLException  Insert into table Failed 2nd = "+ex.getMessage(), ex);
				throw new NEDSSSystemException("Insert into table Failed 2nd", ex);
			} finally {
				closeResultSet(blobRS);
				closeStatement(pStmt1);
				closeResultSet(resultset);
				closeStatement(statement);
				releaseConnection(dbConnection);
			}
		}

	}

	private void writeDataToBlob(java.sql.Blob messageBLOB, String blobData) throws Exception {
		try{
			messageBLOB.setBytes(1, blobData.getBytes());
		}catch(Exception ex){
			logger.fatal("Exception  = "+ex.getMessage(), ex);
			throw new Exception(ex.toString());
		}
	}
}
