package gov.cdc.nedss.nnd.ejb.nndmessageprocessorejb.dao;


import gov.cdc.nedss.exception.NEDSSDAOSysException;
import gov.cdc.nedss.exception.NEDSSSystemException;
import gov.cdc.nedss.nnd.helper.MsgOutMessageDT;
import gov.cdc.nedss.nnd.helper.MsgOutUidGeneratorHelper;
import gov.cdc.nedss.nnd.util.NNDConstantUtil;
import gov.cdc.nedss.util.BMPBase;
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


public class MsgOutMessageDAOImpl
    extends BMPBase
{

    static final LogUtils logger = new LogUtils(MsgOutMessageDAOImpl.class.getName());
    private static final String ORACLE_INSERT = "INSERT INTO MsgOut_Message ( " +
                                                "message_uid, " +
                                                "attachment_txt, " +
                                                "add_time, " +
                                                "interaction_id, " +
                                                "msg_id_assign_auth_cd, " +
                                                "msg_id_assign_auth_desc_txt, " +
                                                "msg_id_root_txt, " +
                                                "msg_id_type_cd, " +
                                                "msg_id_type_desc_txt, " +
                                                "sending_facility_entity_uid, " +
                                                "status_cd, " +
                                                "status_time, " +
                                                "processing_cd, " +
                                                "processing_desc_txt, " +
                                                "record_status_cd, " +
                                                "record_status_time, " +
                                                "version_id " +
                                                " ) VALUES ( ?, empty_clob(), ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ? )";
    private static final String SQL_INSERT = "INSERT INTO MsgOut_Message ( " +
                                             "message_uid, " +
                                             "attachment_txt, " +
                                             "add_time, " +
                                             "interaction_id, " +
                                             "msg_id_assign_auth_cd, " +
                                             "msg_id_assign_auth_desc_txt, " +
                                             "msg_id_root_txt, " +
                                             "msg_id_type_cd, " +
                                             "msg_id_type_desc_txt, " +
                                             "sending_facility_entity_uid, " +
                                             "status_cd, " +
                                             "status_time, " +
                                             "processing_cd, " +
                                             "processing_desc_txt, " +
                                             "record_status_cd, " +
                                             "record_status_time, " +
                                             "version_id " +
                                             " ) VALUES ( ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ? )";

    public MsgOutMessageDAOImpl()
                         throws NEDSSDAOSysException, NEDSSSystemException
    {
    }

    public long create(Object obj)
                throws NEDSSDAOSysException, NEDSSSystemException
    {
    	try{
	        MsgOutMessageDT dt = (MsgOutMessageDT)obj;
	
	        if (dt == null)
	            throw new NEDSSSystemException("Error: try to create null MsgOutMessageDT object.");
	
	        long uid = insertMsgOutMessage((MsgOutMessageDT)obj);
	
	        return uid;
    	}catch(NEDSSDAOSysException ex){
    		logger.fatal("NEDSSDAOSysException  = "+ex.getMessage(), ex);
    		throw new NEDSSDAOSysException(ex.toString());
    	}catch(Exception ex){
    		logger.fatal("Exception  = "+ex.getMessage(), ex);
    		throw new NEDSSSystemException(ex.toString());
    	}
    }

    private long insertMsgOutMessage(MsgOutMessageDT dt)
                              throws NEDSSDAOSysException,
                                     NEDSSSystemException
    {

        PropertyUtil propUtil = PropertyUtil.getInstance();

        //handle ORACLE and SQL differently
        if (propUtil.getDatabaseServerType() != null && propUtil.getDatabaseServerType().equalsIgnoreCase(NEDSSConstants.ORACLE_ID))
        {
            Connection dbConnection = null;
            PreparedStatement preparedStmt = null;
            Statement statement = null;
            ResultSet resultSet = null;
            long uid = 0;
            MsgOutUidGeneratorHelper uidGen = null;
            int resultCount = 0;

            try
            {
                dbConnection = getConnection(NEDSSConstants.MSGOUT);

                /**
                * get Uid from UidGeneratorHelper and update table
                */
                uidGen = new MsgOutUidGeneratorHelper();
                uid = uidGen.getMsgOutIDLong(NNDConstantUtil.MSGOUT_MESSAGE).longValue();

                preparedStmt = dbConnection.prepareStatement(ORACLE_INSERT);

                int i = 1;
                preparedStmt.setLong(i++, uid); // set message_uid
                preparedStmt.setTimestamp(i++, dt.getAddTime());
                preparedStmt.setString(i++, dt.getInteractionId());
                preparedStmt.setString(i++, dt.getMsgIdAssignAuthCd());
                preparedStmt.setString(i++, dt.getMsgIdAssignAuthDescTxt());
                preparedStmt.setString(i++, dt.getMsgIdRootTxt());
                preparedStmt.setString(i++, dt.getMsgIdTypeCd());
                preparedStmt.setString(i++, dt.getMsgIdTypeDescTxt());

                if (dt.getSendingFacilityEntityUid() == null)
                    preparedStmt.setNull(i++, Types.BIGINT);
                else
                    preparedStmt.setLong(i++,
                                         dt.getSendingFacilityEntityUid().longValue());

                preparedStmt.setString(i++, dt.getStatusCd());

                if (dt.getStatusTime() == null)
                    preparedStmt.setTimestamp(i++,
                                              new Timestamp(new java.util.Date().getTime()));
                else
                    preparedStmt.setTimestamp(i++, dt.getStatusTime());

                preparedStmt.setString(i++, dt.getProcessingCd());
                preparedStmt.setString(i++, dt.getProcessingDescTxt());
                preparedStmt.setString(i++, dt.getRecordStatusCd());

                if (dt.getRecordStatusTime() == null)
                    preparedStmt.setTimestamp(i++,
                                              new Timestamp(new java.util.Date().getTime()));
                else
                    preparedStmt.setTimestamp(i++, dt.getRecordStatusTime());

                preparedStmt.setString(i++, dt.getVersionId());
                preparedStmt.executeUpdate();


            }

            catch (SQLException sqlex)
            {
            	logger.fatal("SQLException while inserting"+sqlex.getMessage(), sqlex);
            	throw new NEDSSDAOSysException("Insert table failed");
            }
            catch (Exception ex)
           {
            	logger.fatal("Exception while inserting"+ex.getMessage(), ex);
            	throw new NEDSSSystemException("Insert into table Failed");
           }
           finally
           {

        	   closeStatement(preparedStmt);
        	   releaseConnection(dbConnection);
           }

           try {

				//get another dbconnection for "select for update"
				dbConnection = getConnection(NEDSSConstants.MSGOUT);
				boolean autoCommit = dbConnection.getAutoCommit();
				if( autoCommit == true)
				{
				    dbConnection.setAutoCommit(false);
				}
                statement = dbConnection.createStatement();
                resultSet = statement.executeQuery(
                                    "select attachment_txt from " +
                                    "MsgOut_Message WHERE message_uid = " +
                                    uid + " for update nowait");
                resultSet.next();


                //Narendra 03/02/05 - Changes in support for Oracle JDBC 9.2.0.5 drivers
                /*java.sql.Clob clob = (java.sql.Clob)((weblogic.jdbc.wrapper.Clob) resultSet.getClob(1)).getVendorObj();*/
                java.sql.Clob clob = (java.sql.Clob)( resultSet.getClob(1));
                String XMLString = dt.getAttachmentTxt();
                clob.setString(1, XMLString);
                clob.truncate(XMLString.length());

		//dbConnection.setAutoCommit(autoCommit); //set autoCommit to what is used to be
		//dbConnection.commit();


            }
            catch (SQLException sqlex)
            {
                logger.fatal("SQLException while inserting"+sqlex.getMessage(), sqlex);
                throw new NEDSSDAOSysException("Insert table failed 2nd");
            }
            catch (Exception ex)
            {
            	logger.fatal("Exception while inserting 2nd"+ex.getMessage(), ex);
                throw new NEDSSSystemException("Insert into table Failed 2nd");
            }
            finally
            {
                closeResultSet(resultSet);
                closeStatement(statement);
                releaseConnection(dbConnection);
            }
            return uid;
        }
        else //for SQL
        {

            Connection dbConnection = null;
            PreparedStatement preparedStmt = null;
            long uid = 0;
            MsgOutUidGeneratorHelper uidGen = null;

            try
            {
                dbConnection = getConnection(NEDSSConstants.MSGOUT);

                /**
            * get Uid from MsgOutUidGeneratorHelper and update table
            */
                uidGen = new MsgOutUidGeneratorHelper();
                uid = uidGen.getMsgOutIDLong(NNDConstantUtil.MSGOUT_MESSAGE).longValue();
                logger.debug("message uid = " + uid);
                preparedStmt = dbConnection.prepareStatement(SQL_INSERT);

                String XMLString = dt.getAttachmentTxt();
                int i = 1;
                preparedStmt.setLong(i++, uid);
                preparedStmt.setAsciiStream(i++,
                                            new ByteArrayInputStream(XMLString.getBytes()),
                                            XMLString.length() // attachment_txt
                                            );
                preparedStmt.setTimestamp(i++, dt.getAddTime());
                preparedStmt.setString(i++, dt.getInteractionId());
                preparedStmt.setString(i++, dt.getMsgIdAssignAuthCd());
                preparedStmt.setString(i++, dt.getMsgIdAssignAuthDescTxt());
                preparedStmt.setString(i++, dt.getMsgIdRootTxt());
                preparedStmt.setString(i++, dt.getMsgIdTypeCd());
                preparedStmt.setString(i++, dt.getMsgIdTypeDescTxt());

                if (dt.getSendingFacilityEntityUid() == null)
                    preparedStmt.setNull(i++, Types.BIGINT);
                else
                    preparedStmt.setLong(i++,
                                         dt.getSendingFacilityEntityUid().longValue());

                preparedStmt.setString(i++, dt.getStatusCd());

                if (dt.getStatusTime() == null)
                    preparedStmt.setTimestamp(i++,
                                              new Timestamp(new java.util.Date().getTime()));
                else
                    preparedStmt.setTimestamp(i++, dt.getStatusTime());

                preparedStmt.setString(i++, dt.getProcessingCd());
                preparedStmt.setString(i++, dt.getProcessingDescTxt());
                preparedStmt.setString(i++, dt.getRecordStatusCd());

                if (dt.getRecordStatusTime() == null)
                    preparedStmt.setTimestamp(i++,
                                              new Timestamp(new java.util.Date().getTime()));
                else
                    preparedStmt.setTimestamp(i++, dt.getRecordStatusTime());

                preparedStmt.setString(i++, dt.getVersionId());
                preparedStmt.executeUpdate();

                return uid;
            }
            catch (SQLException sqlex)
            {
                logger.fatal("SQLException while inserting"+sqlex.getMessage(), sqlex);
                throw new NEDSSDAOSysException("Insert table failed");
            }
            catch (Exception ex)
            {
            	logger.fatal("Exception  = "+ex.getMessage(), ex);
                throw new NEDSSSystemException("Insert into table Failed");
            }
            finally
            {
                closeStatement(preparedStmt);
                releaseConnection(dbConnection);
            }
        }
    } //end of insert
}
