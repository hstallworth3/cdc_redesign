package gov.cdc.nedss.act.observation.ejb.dao;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.Writer;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

import oracle.jdbc.driver.OracleConnection;
import oracle.jdbc.pool.OracleDataSource;
import oracle.xdb.XMLType;
import gov.cdc.nedss.act.observation.dt.EDXDocumentDT;
import gov.cdc.nedss.act.sqlscript.WumSqlQuery;
import gov.cdc.nedss.exception.NEDSSDAOSysException;
import gov.cdc.nedss.exception.NEDSSSystemException;
import gov.cdc.nedss.systemservice.exception.ResultSetUtilsException;
import gov.cdc.nedss.util.BMPBase;
import gov.cdc.nedss.util.LogUtils;
import gov.cdc.nedss.util.NEDSSConstants;
import gov.cdc.nedss.util.PropertyUtil;
import gov.cdc.nedss.util.ResultSetUtils;
import gov.cdc.nedss.util.StringUtils;

public class EDXDocumentDAOImpl extends BMPBase{
    static final LogUtils logger = new LogUtils(EDXDocumentDAOImpl.class.getName());
    private static PropertyUtil propertyUtil= PropertyUtil.getInstance();
    public EDXDocumentDAOImpl()
    {
    }
    
    public void insertEDXDocument(EDXDocumentDT dt)
            throws NEDSSSystemException
    {
        Connection dbConnection = null;
        PreparedStatement preparedStmt = null;

        try
        {
            dbConnection = getConnection();
        }
        catch(NEDSSSystemException nsex)
        {
            logger.fatal("Error obtaining db connection " +
                "while inserting into EDX_Document", nsex);
            throw new NEDSSSystemException(nsex.toString());
        }

        try
        {
			preparedStmt = dbConnection
						.prepareStatement(WumSqlQuery.INSERT_EDX_DOCUMENT);

			int i = 1;
			XMLType xml = null;
			logger.debug("EDXDocumentDT = " + dt);
			preparedStmt.setLong(i++, dt.getActUid().longValue());
			if (propertyUtil.getDatabaseServerType() != null
					&& (propertyUtil.getDatabaseServerType()
							.equalsIgnoreCase(NEDSSConstants.ORACLE_ID))) {
				byte[] byteBuffer = dt.getPayload().toString().getBytes();
				InputStream is = new ByteArrayInputStream(byteBuffer);
				
				if (dbConnection instanceof org.jboss.jca.adapters.jdbc.jdk7.WrappedConnectionJDK7) {  
				      org.jboss.jca.adapters.jdbc.jdk7.WrappedConnectionJDK7 wrappedConn = (org.jboss.jca.adapters.jdbc.jdk7.WrappedConnectionJDK7) dbConnection;  
				      OracleConnection oracleConn = (OracleConnection) wrappedConn.getUnderlyingConnection();  
				      xml = XMLType.createXML(oracleConn, is);       
				} 
				
				preparedStmt.setObject(i++, xml);
			} else
				preparedStmt.setString(i++, dt.getPayload());
			preparedStmt.setString(i++, dt.getRecordStatusCd());
			preparedStmt.setTimestamp(i++, dt.getRecordStatusTime());
			preparedStmt.setTimestamp(i++, dt.getAddTime());
			preparedStmt.setString(i++, dt.getDocTypeCd());
			preparedStmt.setLong(i++, dt.getNbsDocumentMetadataUid()
					.longValue());

			preparedStmt.executeUpdate();
			logger.debug("EDXDocument inserted for act_uid " + dt.getActUid());
       }
        catch(SQLException sqlex)
        {
            logger.fatal("SQLException while inserting " +
                        "document into EDX_Document: \n", sqlex);
            throw new NEDSSDAOSysException( sqlex.toString() );
        }
        catch(Exception ex)
        {
            logger.fatal("Error while inserting EDXDocument", ex);
            throw new NEDSSSystemException (ex.toString());
        }
        finally
        {
           // closeResultSet(resultSet);
            closeStatement(preparedStmt);
            releaseConnection(dbConnection);
        }
    }//end of inserting a EDX_Document


    public Collection<Object> selectEDXDocumentCollection (long actUid) throws NEDSSSystemException
    {

        Connection dbConnection = null;
        PreparedStatement preparedStmt = null;
        ResultSet resultSet = null;
        ResultSetMetaData resultSetMetaData = null;
        ResultSetUtils resultSetUtils = new ResultSetUtils();
        EDXDocumentDT eDXDocumentDT = new EDXDocumentDT();

        try
        {
            dbConnection = getConnection();
        }
        catch(NEDSSSystemException nsex)
        {
            logger.fatal("SQLException while obtaining database connection " +
                            "for selectEDXDocumentCollection " , nsex);
            throw new NEDSSSystemException( nsex.getMessage());
        }

        /**
         * Selects selectEDXDocuments
         */
        try
        {
            preparedStmt = dbConnection.prepareStatement(WumSqlQuery.SELECT_EDX_DOCUMENT_COLLECTION);
            preparedStmt.setLong(1, actUid);
            resultSet = preparedStmt.executeQuery();
            resultSetMetaData = resultSet.getMetaData();
            ArrayList<Object>  reSetList = new ArrayList<Object> ();

            reSetList = (ArrayList<Object> )resultSetUtils.mapRsToBeanList(resultSet, resultSetMetaData, eDXDocumentDT.getClass(), reSetList);
            logger.debug("return EDX_Document collection");
            return reSetList;
        }
        catch(SQLException se)
        {
            throw new NEDSSDAOSysException("SQLException while selecting " +
                            "EDX_Document collection; id = " + actUid + " :\n" + se.getMessage());
        }
        catch(ResultSetUtilsException reuex)
        {
            logger.fatal("Error in result set handling while selecting selectEDXDocumentCollection.", reuex);
            throw new NEDSSDAOSysException(reuex.toString());
        }
        finally
        {
            closeResultSet(resultSet);
            closeStatement(preparedStmt);
            releaseConnection(dbConnection);
        }
    }//end of selecting EDXDocuments
    
    public EDXDocumentDT selectIndividualEDXDocument (long eDXDocumentUid) throws NEDSSSystemException
    {

        Connection dbConnection = null;
        PreparedStatement preparedStmt = null;
        ResultSet resultSet = null;
        ResultSetMetaData resultSetMetaData = null;
        ResultSetUtils resultSetUtils = new ResultSetUtils();
        EDXDocumentDT eDXDocumentDT = new EDXDocumentDT();

        try
        {
            dbConnection = getConnection();
        }
        catch(NEDSSSystemException nsex)
        {
            logger.fatal("SQLException while obtaining database connection " +
                            "for selectIndividualEDXDocument " , nsex);
            throw new NEDSSSystemException( nsex.getMessage());
        }

        /**
         * Selects selectEDXDocuments
         * "SELECT EDX_Document.EDX_Document_uid \"eDXDocumentUid\", EDX_Document.act_uid \"actUid\", "
			+ " EDX_Document.payload \"payload\" , "
			+ " EDX_Document.record_status_cd \"recordStatusCd\" , "
			+ " EDX_Document.record_status_time \"recordStatusTime\" , "
			+ " EDX_Document.add_time \"addTime\" , "
			+ " EDX_Document.doc_type_cd \"docTypeCd\" , "
			+ " NBS_document_metadata.document_view_xsl \"documentViewXsl\" , "
			+ " NBS_document_metadata.xml_schema_location \"xmlSchemaLocation\"  "
			+ " FROM EDX_Document, NBS_document_metadata "
			+ " WHERE EDX_Document.nbs_document_metadata_uid=NBS_document_metadata.nbs_document_metadata_uid "
			+ " and EDX_Document.EDX_Document_Uid  = ?"
         */
        try
        {
        	if(propertyUtil.getDatabaseServerType() != null
					&& (propertyUtil.getDatabaseServerType()
							.equalsIgnoreCase(NEDSSConstants.ORACLE_ID)))
        		preparedStmt = dbConnection.prepareStatement(WumSqlQuery.SELECT_INDIVIDUAL_EDX_DOCUMENT_ORA);
        	else
        		preparedStmt = dbConnection.prepareStatement(WumSqlQuery.SELECT_INDIVIDUAL_EDX_DOCUMENT);
            preparedStmt.setLong(1, eDXDocumentUid);
            resultSet = preparedStmt.executeQuery();
            resultSetMetaData = resultSet.getMetaData();
            ArrayList<Object>  otList = new ArrayList<Object> ();
            if(resultSet!=null && propertyUtil.getDatabaseServerType() != null
					&& (propertyUtil.getDatabaseServerType()
							.equalsIgnoreCase(NEDSSConstants.ORACLE_ID))){
            	while (resultSet.next()){
            	eDXDocumentDT.setEDXDocumentUid(resultSet.getLong(1));
            	eDXDocumentDT.setActUid(resultSet.getLong(2));
            	eDXDocumentDT.setPayload(StringUtils.clobToString(resultSet.getClob(3)));
            	eDXDocumentDT.setRecordStatusCd(resultSet.getString(4));
            	eDXDocumentDT.setRecordStatusTime(resultSet.getTimestamp(5));
            	eDXDocumentDT.setAddTime(resultSet.getTimestamp(6));
            	eDXDocumentDT.setDocTypeCd(resultSet.getString(7));
            	eDXDocumentDT.setDocumentViewXsl(StringUtils.blobToString(resultSet.getBlob(8)));
            	eDXDocumentDT.setXmlSchemaLocation(resultSet.getString(9));
            	otList.add(eDXDocumentDT);
            	}
            }
            else
            	otList = (ArrayList<Object> )resultSetUtils.mapRsToBeanList(resultSet, resultSetMetaData, eDXDocumentDT.getClass(), otList);
            logger.debug("return EDXDocument collection");
            return (EDXDocumentDT)otList.get(0);
        }
        catch(SQLException se)
        {
            throw new NEDSSDAOSysException("SQLException while selecting " +
                            "EDX_Document ; eDXDocumentUid = " + eDXDocumentUid + " :\n" + se.getMessage());
        }
        catch(ResultSetUtilsException reuex)
        {
            logger.fatal("Error in result set handling while selecting selectIndividualEDXDocument.", reuex);
            throw new NEDSSDAOSysException(reuex.toString());
        }
        finally
        {
            closeResultSet(resultSet);
            closeStatement(preparedStmt);
            releaseConnection(dbConnection);
        }
    }//end of selecting EDXDocument
    
/*public static void main(String args[])
    {
       logger.debug("EDXDocument - Doing the main thing");
     try
       {
    	 EDXDocumentDT dt = new EDXDocumentDT();
         Long uid = new Long(12);
         dt.setActUid(new Long(10180005));
         dt.setPayload("XML");
         dt.setRecordStatusCd("ACTIVE");
         dt.setRecordStatusTime(new Timestamp(new Date().getTime()));
         dt.setDocTypeCd("Lab");
         dt.setAddTime(new Timestamp(new Date().getTime()));
         dt.setNbsDocumentMetadataUid(new Long(1005));
         EDXDocumentDAOImpl dao = new EDXDocumentDAOImpl();
         dao.insertEDXDocument(dt);
         logger.debug("Executed insertEDXDocument: " + dt);
       }
       catch(Exception e)
       {
         logger.debug("\n\nObsValueCodedDAOImpl ERROR : turkey no worky = \n" + e);

       }
     }
*/
}
