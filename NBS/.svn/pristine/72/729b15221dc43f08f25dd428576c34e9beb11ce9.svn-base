package gov.cdc.nedss.systemservice.dao;

import gov.cdc.nedss.act.sqlscript.WumSqlQuery;
import gov.cdc.nedss.exception.NEDSSDAOAppException;
import gov.cdc.nedss.exception.NEDSSDAOSysException;
import gov.cdc.nedss.exception.NEDSSSystemException;
import gov.cdc.nedss.systemservice.dt.NbsInterfaceDT;
import gov.cdc.nedss.systemservice.exception.ResultSetUtilsException;
import gov.cdc.nedss.systemservice.genericXMLParser.MsgXMLMappingDT;
import gov.cdc.nedss.util.DAOBase;
import gov.cdc.nedss.util.LogUtils;
import gov.cdc.nedss.util.NEDSSConstants;
import gov.cdc.nedss.util.PropertyUtil;
import gov.cdc.nedss.util.ResultSetUtils;

import java.io.ByteArrayInputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

import org.w3c.dom.Document;

public class NbsInterfaceDAOImpl  extends DAOBase{
	static final LogUtils logger = new LogUtils(NbsInterfaceDAOImpl.class.getName());
	private static PropertyUtil propertyUtil= PropertyUtil.getInstance();
	public Collection<Object> getSQLUnprocessedInterfaceUIDCollection(String docTypeCd){
		String SELECT_QUEUED_CASES = "SELECT nbs_interface_uid \"nbsInterfaceUid\" "
				+" FROM NBS_interface" 
				+" WHERE record_status_cd in ('QUEUED','"+NEDSSConstants.ORIG_XML_QUEUED+"') and  imp_exp_Ind_cd='I' and doc_type_cd='"+docTypeCd+"'";
		NbsInterfaceDT nbsInterfaceDT  = new NbsInterfaceDT();
		ArrayList<Object>  unprocessedInterfaceUIDCollection = new ArrayList<Object> ();

		//unprocessedInterfaceUIDCollection.add(docTypeCd);
		try
		{
			unprocessedInterfaceUIDCollection = (ArrayList<Object> )preparedStmtMethod(nbsInterfaceDT, unprocessedInterfaceUIDCollection, SELECT_QUEUED_CASES, NEDSSConstants.SELECT,NEDSSConstants.MSGOUT);
		}
		catch (Exception ex) {
			logger.fatal("NbsInterfaceDAOImpl.insertNBSInterface:  Exception in get queuedCollection:  ERROR = " + ex.getMessage() ,ex);
			throw new NEDSSSystemException(ex.toString(), ex);
		}
		return unprocessedInterfaceUIDCollection;
	}
	@SuppressWarnings("unchecked")
	public NbsInterfaceDT getInterfaceDTByUID(Long uid){
		NbsInterfaceDT nbsInterfaceDT  = new NbsInterfaceDT();
		try{
			String SELECT_QUEUED_CASES = 
					"SELECT " 											+
							"	nbs_interface_uid 	\"nbsInterfaceUid\", " 		+
							"	payload 			\"xmlPayLoadContent\", " 	+
							"	imp_exp_ind_cd 		\"impExpIndCd\", " 			+
							"	record_status_cd 	\"recordStatusCd\", " 		+
							"	record_status_time 	\"recordStatusTime\", " 	+
							"	add_time 			\"addTime\", " 				+
							"	system_nm 			\"systemNm\", "				+
							" 	doc_type_cd			\"docTypeCd\", "			+
							//" 	cda_payload			\"cdaPayload\" "		+
							" 	original_payload	\"originalPayload\", "		+
							" 	original_doc_type_cd \"originalDocTypeCd\" "	+
							" FROM " 											+
							"	NBS_interface " 								+	 
							" WHERE " 											+
							"	nbs_interface_uid 	="+uid;


			ArrayList<Object>  unprocessedInterfaceCollection = new ArrayList<Object> ();
			//		unprocessedInterfaceCollection.add(uid);
			try
			{
				unprocessedInterfaceCollection = (ArrayList<Object> )preparedStmtMethod(nbsInterfaceDT, unprocessedInterfaceCollection, SELECT_QUEUED_CASES, NEDSSConstants.SELECT,NEDSSConstants.MSGOUT);
			}
			catch (Exception ex) {
				logger.fatal("NbsInterfaceDAOImpl.insertNBSInterface:  Exception in get queuedCollection:  ERROR = " + ex.getMessage(), ex);
				throw new NEDSSSystemException(ex.toString(), ex);
			}

			Iterator it = unprocessedInterfaceCollection.iterator();

			while(it.hasNext()){
				nbsInterfaceDT = (NbsInterfaceDT)it.next();
			}
		}catch(Exception ex){
			logger.fatal("Exception  = "+ex.getMessage(), ex);
			throw new NEDSSSystemException(ex.toString());
		}
		return nbsInterfaceDT;
	}

	@SuppressWarnings("unchecked")
	public Collection getUnprocessedLabsCollection(){
		String SELECT_QUEUED_CASES = "SELECT nbs_interface_uid \"nbsInterfaceUid\", "
				+" FROM NBS_interface" 
				+" WHERE record_status_cd='QUEUED' and  imp_exp_Ind_cd='I'";
			NbsInterfaceDT nbsInterfaceDT  = new NbsInterfaceDT();
			ArrayList<Object>  unprocessedInterfaceUIDCollection = new ArrayList<Object> ();
			try
			{
				unprocessedInterfaceUIDCollection = (ArrayList<Object> )preparedStmtMethod(nbsInterfaceDT, unprocessedInterfaceUIDCollection, SELECT_QUEUED_CASES, NEDSSConstants.SELECT,NEDSSConstants.MSGOUT);
			}
			catch (Exception ex) {
				logger.fatal("NbsInterfaceDAOImpl.insertNBSInterface:  Exception in get queuedCollection:  ERROR = " + ex.getMessage(), ex);
				throw new NEDSSSystemException(ex.toString(), ex);
			}
			return unprocessedInterfaceUIDCollection;
	}


	public Long insertNBSInterface(NbsInterfaceDT dt)throws NEDSSDAOSysException, NEDSSDAOAppException, NEDSSSystemException	
	{
		String dbType = PropertyUtil.getInstance().getDatabaseServerType();
		Long interfaceUid = null;
		try{
			if(dbType.equalsIgnoreCase(NEDSSConstants.ORACLE_ID))
				interfaceUid=  insertNBSInterfaceOracle(dt);
			else if(!dbType.equalsIgnoreCase(NEDSSConstants.ORACLE_ID))
				interfaceUid =  insertNBSInterfaceSQL(dt);
		}catch(Exception e){
			logger.fatal("Exception  Error inserting record for NBS_interface = "+e.getMessage(), e);
			throw new NEDSSSystemException("Error inserting record for NBS_interface", e);

		}
		return interfaceUid;
	}
	private Long insertNBSInterfaceSQL(NbsInterfaceDT dt) throws NEDSSDAOSysException, NEDSSDAOAppException, NEDSSSystemException{

		Connection dbConnection = null;
		PreparedStatement pStmt = null;
		ResultSet resultSet = null;
		int resultCount = 0;
		int i = 1;
		String sql="";
		Long interfaceUid = null;
		ResultSet rs = null;
		sql = WumSqlQuery.INSERT_NBS_INTERFACE_SQL;

		try
		{

			dbConnection = getConnection(NEDSSConstants.MSGOUT);


			pStmt = dbConnection.prepareStatement(sql);   

			pStmt.setString(i++, dt.getXmlPayLoadContent());
			pStmt.setString(i++, dt.getImpExpIndCd());
			pStmt.setString(i++, dt.getRecordStatusCd());
			pStmt.setTimestamp(i++, dt.getRecordStatusTime());
			pStmt.setTimestamp(i++, dt.getAddTime());
			pStmt.setString(i++, dt.getSystemNm());
			pStmt.setString(i++, dt.getDocTypeCd());
			resultCount = pStmt.executeUpdate();
			if (resultCount != 1)
			{
				throw new NEDSSSystemException("NbsInterfaceDAOImpl.insertNBSInterface:  none or more than one records inserted at a time, resultCount = " +
						resultCount);
			}
			//For SQL, get generated key and return; For Oracle, we'll query for max(uid) to get inserted row's UID



			try {
				rs = pStmt.getGeneratedKeys();

				if (rs.next()){
					// return NBS_interface.nbs_interface_uid
					interfaceUid = new Long(rs.getLong(1));
					rs.close();
					return interfaceUid;
				}
				else {
					rs.close();
					throw new NEDSSDAOAppException("NbsInterfaceDAOImpl.insertNBSInterface:  failed to retrieve nbs_interface_uid as a generated key."); 
				}
			} catch (Exception e) {
				logger.fatal("Exception  NbsInterfaceDAOImpl.insertNBSInterface:  failed to retrieve nbs_interface_uid as a generated key. "+e.getMessage(), e);
				throw new NEDSSDAOAppException("NbsInterfaceDAOImpl.insertNBSInterface:  failed to retrieve nbs_interface_uid as a generated key.", e); 
			}	

		}	
		catch(SQLException sqlex)
		{
			String errorMsg = "NbsInterfaceDAOImpl.insertNBSInterface:  SQLException while inserting BLOB into NBS_Interface:" + 
					dt.toString() + "\n";
			logger.fatal(errorMsg, sqlex);
			throw new NEDSSDAOSysException( sqlex.toString(), sqlex );
		}
		catch(Exception ex)
		{
			String errorMsg = "NbsInterfaceDAOImpl.insertNBSInterface:  SQLException while inserting BLOB into NBS_Interface:" + 
					dt.toString() + "\n";
			logger.fatal(errorMsg, ex);
			throw new NEDSSSystemException(errorMsg + ex.toString(), ex);
		}			

		finally{
			closeResultSet(rs);
			closeResultSet(resultSet);
			closeStatement(pStmt);
			releaseConnection(dbConnection);
		}



	}

	private Long insertNBSInterfaceOracle( NbsInterfaceDT dt) throws NEDSSDAOSysException, NEDSSDAOAppException, NEDSSSystemException
	{	

		Connection dbConnection = null;
		PreparedStatement pStmt = null;
		ResultSet rs = null;
		Statement stmt2 = null;
		int resultCount = 0;
		int i = 1;
		String sql="";
		Long interfaceUid = null;


		sql = WumSqlQuery.INSERT_NBS_INTERFACE_ORA;


		try
		{

			dbConnection = getConnection(NEDSSConstants.MSGOUT);

			pStmt = dbConnection.prepareStatement(sql);   

			pStmt.setString(i++, dt.getImpExpIndCd());
			pStmt.setString(i++, dt.getRecordStatusCd());
			pStmt.setTimestamp(i++, dt.getRecordStatusTime());
			pStmt.setTimestamp(i++, dt.getAddTime());
			pStmt.setString(i++, dt.getSystemNm()); 
			pStmt.setString(i++, dt.getDocTypeCd());
			resultCount = pStmt.executeUpdate();
			if (resultCount != 1)
			{
				throw new NEDSSSystemException("NbsInterfaceDAOImpl.insertNBSInterface:  none or more than one records inserted at a time, resultCount = " +
						resultCount);
			}
			releaseConnection(dbConnection);
			pStmt.close();

			dbConnection = getConnection(NEDSSConstants.MSGOUT);
			stmt2 = dbConnection.createStatement();
			stmt2.execute("select max(nbs_interface_uid) from nbs_interface where imp_exp_ind_cd='E'");
			rs = stmt2.getResultSet();
			while(rs.next()){
				interfaceUid = new Long(rs.getLong(1));
			}
			releaseConnection(dbConnection);
			pStmt.close();
			rs.close();
			
		       dbConnection = getConnection(NEDSSConstants.MSGOUT);
		        boolean autoCommit = dbConnection.getAutoCommit();
				if (autoCommit == true) {
					dbConnection.setAutoCommit(false);
				}
				//TODO:  Get and return max(nbs_interface_uid) as generated key for this insert
				stmt2 = dbConnection.createStatement();
				//pStmt = dbConnection.prepareStatement("SELECT payload FROM NBS_Interface WHERE nbs_interface_uid = ? FOR UPDATE");
				//pStmt.setLong(1, interfaceUid.longValue());
				//resultSet = pStmt.executeQuery();
				//resultSet.next();
				stmt2.execute("SELECT payload FROM NBS_Interface WHERE nbs_interface_uid ="+interfaceUid.longValue()+" FOR UPDATE");
				  rs = stmt2.getResultSet();
				  java.sql.Blob blob = null;
			      while(rs.next()){
			    	  blob = (java.sql.Blob)(rs.getBlob("PAYLOAD"));
			      }
				writeBinaryDataToBlob(blob, dt.getXmlPayLoadContent());

		}
		catch(SQLException sqlex)
		{
			String errorMsg = "NbsInterfaceDAOImpl.insertNBSInterface:  SQLException while inserting BLOB into NBS_Interface:" + 
					dt.toString() + "\n";
			logger.fatal(errorMsg, sqlex);
			throw new NEDSSDAOSysException( sqlex.toString(), sqlex );
		}
		catch(Exception ex)
		{
			String errorMsg = "NbsInterfaceDAOImpl.insertNBSInterface:  SQLException while inserting BLOB into NBS_Interface:" + 
					dt.toString() + "\n";
			logger.fatal(errorMsg, ex);
			throw new NEDSSSystemException(errorMsg + ex.toString(), ex);
		}
		finally{
			try{
				closeResultSet(rs);
				closeStatement(stmt2);
				closeStatement(pStmt);
				releaseConnection(dbConnection);
			}
			catch (Exception ex){}

		}
		return interfaceUid;  //TODO:  Return interface_uid
	}

	public Collection<Object> retrieveNBSInterface()throws NEDSSSystemException{

		Connection dbConnection = null;
		PreparedStatement preparedStmt = null;
		ResultSet resultSet = null;
		ResultSetMetaData resultSetMetaData = null;
		ResultSetUtils resultSetUtils = new ResultSetUtils();
		String sqlQuery = null;

		try {
			dbConnection = getConnection(NEDSSConstants.MSGOUT);
		} catch (NEDSSSystemException nsex) {
			logger.fatal("NbsInterfaceDAOImpl.retrieveNBSInterface:  SQLException while obtaining database connection "
					+ "for case Notification: retrieveNBSInterface()  in CaseNotificationDAO", nsex);
			throw new NEDSSSystemException(nsex.getMessage(), nsex);
		}
		//  logic to check if code has separate table
		if (propertyUtil.getDatabaseServerType() != null &&
				propertyUtil.getDatabaseServerType().equalsIgnoreCase(NEDSSConstants.ORACLE_ID)){
			sqlQuery = WumSqlQuery.GET_NBS_INTERFACE_FIELDS_ORA;
		}
		else{
			sqlQuery = WumSqlQuery.GET_NBS_INTERFACE_FIELDS_SQL;
		}
		try {
			preparedStmt = dbConnection
					.prepareStatement(sqlQuery);
			resultSet = preparedStmt.executeQuery();
			ArrayList<Object>  nBSInterfaceList = new ArrayList<Object> ();
			if(resultSet!=null){
				resultSetMetaData = resultSet.getMetaData();


				nBSInterfaceList = (ArrayList<Object> ) resultSetUtils.mapRsToBeanList(resultSet,
						resultSetMetaData, NbsInterfaceDT.class, nBSInterfaceList);

				logger.debug("NbsInterfaceDAOImpl.retrieveNBSInterface:  Returned Trigger Fields list");
			}
			return nBSInterfaceList;
		} catch (SQLException se) {
			logger.fatal("SQLException NbsInterfaceDAOImpl.retrieveNBSInterface:  SQLException while selecting  NBS Interface Field List: CaseNotificationDAOImpl "+se.getMessage(), se);
			throw new NEDSSDAOSysException("NbsInterfaceDAOImpl.retrieveNBSInterface:  SQLException while selecting "
					+ "NBS Interface Field List: CaseNotificationDAOImpl " +se.getMessage(), se);
		} catch (ResultSetUtilsException reuex) {
			logger.fatal("NbsInterfaceDAOImpl.retrieveNBSInterface:  Error in result set handling while selecting NBS Interface Fields: CaseNotificationDAOImpl.",
					reuex);
			throw new NEDSSDAOSysException(reuex.toString(), reuex);
		} finally {
			closeResultSet(resultSet);
			closeStatement(preparedStmt);
			releaseConnection(dbConnection);
		}
	}


	public void updateNbsInterfaceDT (NbsInterfaceDT nbsInterfaceDT)throws  NEDSSSystemException
	{
		String UPDATE_NBS_INTERFACE="UPDATE NBS_interface SET record_status_cd =?, record_status_time =? WHERE nbs_interface_uid=?";
		Connection dbConnection = null;
		PreparedStatement preparedStmt = null;
		int resultCount = 0;
		logger.debug("NbsInterfaceDAOImpl.updateNbsInterfaceDT:  UPDATE_NBS_INTERFACE="+ UPDATE_NBS_INTERFACE);
		try{
			dbConnection = getConnection(NEDSSConstants.MSGOUT);
			preparedStmt = dbConnection.prepareStatement( UPDATE_NBS_INTERFACE);
			int i = 1;
			preparedStmt.setString(i++, nbsInterfaceDT.getRecordStatusCd()); //1
			preparedStmt.setTimestamp(i++, nbsInterfaceDT.getRecordStatusTime()); //2
			preparedStmt.setLong(i++, nbsInterfaceDT.getNbsInterfaceUid().longValue());//3
			resultCount = preparedStmt.executeUpdate();
			logger.debug("resultCount is " + resultCount);
		}
		catch(SQLException sqlex)
		{
			logger.fatal("NbsInterfaceDAOImpl.updateNbsInterfaceDT:  SQLException while updateNbsInterfaceDT " + "updateNbsInterfaceDT into Nbs_Interface:"+nbsInterfaceDT.toString(), sqlex);
			throw new NEDSSDAOSysException( sqlex.toString(), sqlex );
		}
		catch(Exception ex)
		{
			logger.fatal("NbsInterfaceDAOImpl.updateNbsInterfaceDT:  Error while update into updateNbsInterfaceDT, updateNbsInterfaceDT="+ nbsInterfaceDT.toString(), ex);
			throw new NEDSSSystemException(ex.toString(), ex);
		}
		finally
		{
			closeStatement(preparedStmt);
			releaseConnection(dbConnection);
		}

	}//end of update method

	private void writeBinaryDataToBlob(java.sql.Blob messageBLOB, String blobData) throws Exception {
		try{
			messageBLOB.setBytes(1, blobData.getBytes());
		}catch(Exception ex){
			logger.fatal("Exception  = "+ex.getMessage(), ex);
			throw new NEDSSSystemException(ex.toString());
		}
	}

	public  int getSQLUnprocessedInterfaceCount(String cd){
		Connection dbConnection = null;
		PreparedStatement preparedStmt = null;
		int numberOfCasesNeedsToBeImported = 0;
		int resultCount = 0;
		ResultSet resultSet=null;
		String SELECT_QUEUED_ELR_CASES = "SELECT count(*) "
				+" FROM NBS_interface" 
				+" WHERE record_status_cd='QUEUED' and  imp_exp_Ind_cd='I'  and doc_type_cd =?";
		logger.debug(" COUNT_PHC_CASES="+ SELECT_QUEUED_ELR_CASES);
		try{
			dbConnection = getConnection(NEDSSConstants.MSGOUT);
		}catch(NEDSSSystemException nsex){
			logger.fatal("SQLException while obtaining database connection for getSQLUnprocessedInterfaceCount ", nsex);
			throw new NEDSSSystemException(nsex.toString());
		}
		try{
			preparedStmt = dbConnection.prepareStatement( SELECT_QUEUED_ELR_CASES);
			int i = 1;
			preparedStmt.setString(i++, cd); 

			resultSet = preparedStmt.executeQuery();
			if (resultSet.next())
			{
				numberOfCasesNeedsToBeImported = resultSet.getInt(1);
			}
			logger.debug("resultCount is " +numberOfCasesNeedsToBeImported);
		}
		catch(SQLException sqlex)
		{
			logger.fatal("SQLException while NbsInterfaceDAOImpl.getSQLUnprocessedInterfaceCount: cd :"+cd, sqlex);
			throw new NEDSSDAOSysException( sqlex.toString(), sqlex );
		}
		catch(Exception ex)
		{
			logger.fatal("NbsInterfaceDAOImpl.Exception while getSQLUnprocessedInterfaceCount: cd :"+cd+ ex.getMessage(), ex);
			throw new NEDSSSystemException(ex.toString(), ex);
		}
		finally
		{
			closeResultSet(resultSet);
			closeStatement(preparedStmt);
			releaseConnection(dbConnection);
		}
		return numberOfCasesNeedsToBeImported;
	}//end


	public NbsInterfaceDT getUnprocessedInterfaceDT( String docTypeCd){
		String SELECT_QUEUED_CASES = 
				"	nbs_interface_uid 	\"nbsInterfaceUid\", " 		+
						"	payload 			\"xmlPayLoadContent\", " 	+
						"	imp_exp_ind_cd 		\"impExpIndCd\", " 			+
						"	record_status_cd 	\"recordStatusCd\", " 		+
						"	record_status_time 	\"recordStatusTime\", " 	+
						"	add_time 			\"addTime\", " 				+
						"	system_nm 			\"systemNm\", "				+
						" 	doc_type_cd			\"docTypeCd\" "				+
						" FROM " 											+
						"	NBS_interface " 								+	 
						" WHERE " 											+
						"	record_status_cd	='QUEUED' " 				+
						"AND  " 											+
						"	imp_exp_Ind_cd		='I' ";

		String query=null;
		String SELECT_FROM_PHC_TABLE_SQL = "Select TOP 1  "+SELECT_QUEUED_CASES+ " and doc_type_cd=? ";
		String SELECT_FROM_PHC_TABLE_ORACLE = "Select  "+SELECT_QUEUED_CASES+ "  and doc_type_cd=?  and  ROWNUM <= 1 ";
		PropertyUtil propertyUtil = PropertyUtil.getInstance();
		String databaseType = propertyUtil.getDatabaseServerType();
		if(databaseType != null && databaseType.equalsIgnoreCase(NEDSSConstants.ORACLE_ID)){
			query=SELECT_FROM_PHC_TABLE_ORACLE;
		}
		else
		{
			query=SELECT_FROM_PHC_TABLE_SQL;
		}			
		NbsInterfaceDT nbsInterfaceDT  = new NbsInterfaceDT();
		ArrayList<Object>  unprocessedInterfaceCollection = new ArrayList<Object> ();
		unprocessedInterfaceCollection.add(docTypeCd);
		try
		{
			unprocessedInterfaceCollection = (ArrayList<Object> )preparedStmtMethod(nbsInterfaceDT, unprocessedInterfaceCollection, query, NEDSSConstants.SELECT,NEDSSConstants.MSGOUT);
		}
		catch (Exception ex) {
			logger.fatal("NbsInterfaceDAOImpl.getUnprocessedInterfaceDT:  Exception :  ERROR = " + ex.getMessage(), ex);
			throw new NEDSSSystemException(ex.toString(), ex);
		}

		Iterator it = unprocessedInterfaceCollection.iterator();

		while(it.hasNext()){
			nbsInterfaceDT = (NbsInterfaceDT)it.next();
		}
		return nbsInterfaceDT;
	}
	
	@SuppressWarnings("unchecked")
	public ArrayList<MsgXMLMappingDT> getOriginalDocumentXMLMapping(
			String origicalDocumentType) {

		MsgXMLMappingDT mappingDT = new MsgXMLMappingDT();
		ArrayList<Object> parameters = new ArrayList<Object>();
		parameters.add(origicalDocumentType);
		// Get the information from the XML_Mapping_table
		String xmlMappingTableQuery = "Select xml_path \"xmlPath\", xml_tag \"xmlTag\", translation_table_nm \"translationTableNm\","
				+ " question_identifier \"questionIdentifier\", QUES_CODE_SYSTEM_CD \"quesCodeSystemCd\", QUES_CODE_SYSTEM_DESC_TXT \"quesCodeSystemDescTxt\", QUES_DISPLAY_TXT \"quesDisplayTxt\", "
				+ " repeat_group_seq_nbr \"repeatGroupSeqNbr\", question_data_type \"questionDataType\", column_nm \"columnNm\", part_type_cd \"partTypeCd\""
				+ " from MSG_XML_mapping where doc_type_cd =? order by TRANSLATION_TABLE_NM desc";

		ArrayList<MsgXMLMappingDT> eICRMappingTable = (ArrayList<MsgXMLMappingDT>) preparedStmtMethod(
				mappingDT, parameters, xmlMappingTableQuery, NEDSSConstants.SELECT,
				NEDSSConstants.MSGOUT);
		return eICRMappingTable;
	}
}
