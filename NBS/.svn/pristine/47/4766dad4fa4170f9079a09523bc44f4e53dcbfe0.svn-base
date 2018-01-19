/**
 * Name:        BMPBase.java file
 * Description:    The is a base class for bean-managed persistent (BMP)
 *               entity beans in NEDSS system.
 * Copyright:    Copyright (c) 2001
 * Company:     Computer Sciences Corporation
 * @author    Brent Chen & NEDSS Development Team
 * @version    1.0
 **/
package gov.cdc.nedss.util;

// import gov.cdc.nedss.cdm.ejb.utils.*;
import gov.cdc.nedss.association.dt.RoleDT;
import gov.cdc.nedss.exception.*;
import gov.cdc.nedss.util.*;

import java.rmi.*;
import java.sql.*;
import java.util.*;

import javax.ejb.*;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.rmi.PortableRemoteObject;
import javax.sql.*;

// import gov.cdc.nedss.dao.*;
// import gov.cdc.nedss.dao.exceptions.*;
public class BMPBase {

	// protected static boolean DEBUG_MODE = false;
	// Used for debugging
	private NedssUtils nu;

	static final LogUtils logger = new LogUtils((BMPBase.class).getName()); // Used
																			// for
																			// logging

	protected EntityContext cntx;

	protected static DataSource dataSource = null;

	protected static DataSource dataSource2 = null;

	// protected Connection dbConnection = null;
	private Context jndiCntx;

	// private InitialContext initCntx;
	// public static final String DATA_SOURCE_REFERENCE =
	// "java:jboss/datasources/NedssDataSource";

	/**
	 * Creates a new BMPBase object.
	 */
	public BMPBase() {

		/*
		 * Connection dbConnection = null; nu = DEBUG_MODE ? (new NedssUtils()) :
		 * null; if (DEBUG_MODE) { System.out.print("\nBMPBase is in debug
		 * mode"); if (nu == null) System.out.print("\nCouldn't get an instance
		 * of NEDSS Utils"); }
		 * 
		 * dbConnection = DEBUG_MODE ? nu.getTestConnection() : null;
		 */
	}

	/**
	 * A base method to be overriden for inserting a data record into database
	 * 
	 * @param obj
	 *            a Object object to be inserted into database
	 * @return a long uid
	 * @throws NEDSSDAOSysException
	 * @throws NEDSSSystemException
	 */
	public long create(Object obj) throws NEDSSDAOSysException,
			NEDSSSystemException {

		return 0;
	};

	/**
	 * A base method to be overriden for inserting data records into database
	 * 
	 * @param coll
	 *            a Collection<Object>  of data records to be inserted into database
	 * @return a Long object representing a uid
	 * @throws NEDSSDAOSysException
	 * @throws NEDSSSystemException
	 */
	/*
	 * public long create(Collection<Object> coll) throws NEDSSDAOSysException,
	 * NEDSSSystemException {
	 * 
	 * return 0; }
	 */

	/**
	 * A base method to be overriden for inserting a data record into database
	 * 
	 * @param aUID
	 *            a long uid
	 * @param obj
	 *            a Object object to be inserted into database
	 * @return a long uid
	 * @throws NEDSSDAOSysException
	 * @throws NEDSSSystemException
	 */
	public long create(long aUID, Object obj) throws NEDSSDAOSysException,
			NEDSSSystemException {

		return 0;
	};

	/**
	 * A base method to be overriden for inserting data records into database
	 * 
	 * @param aUID
	 *            a long object representing a uid
	 * @param coll
	 *            a Collection<Object>  objects to be inserted into database
	 * @return a long uid object
	 * @throws NEDSSDAOSysException
	 * @throws NEDSSSystemException
	 */
	public long create(long aUID, Collection<Object> coll) throws NEDSSDAOSysException,
			NEDSSSystemException {

		return 0;
	};

	/**
	 * A base method to be overriden for inserting data records into database
	 * 
	 * @param aUID
	 *            a long uid
	 * @param seqNum
	 *            an int data record sequence number
	 * @param coll
	 *            a Collection<Object>  data records to be inserted into database
	 * @return a long uid
	 * @throws NEDSSDAOSysException
	 * @throws NEDSSSystemException
	 */
	public long create(long aUID, int seqNum, Collection<Object> coll)
			throws NEDSSDAOSysException, NEDSSSystemException {

		return 0;
	};

	/**
	 * A base method to be overriden for retrieving a data record from database
	 * 
	 * @param aUID
	 *            a long representing a uid
	 * @return Object object
	 * @throws NEDSSDAOSysException
	 * @throws NEDSSSystemException
	 */
	public Object loadObject(long aUID) throws NEDSSDAOSysException,
			NEDSSSystemException {

		return null;
	};

	/**
	 * A base method to be overriden for retrieving data records from database
	 * 
	 * @param aUID
	 *            a long representing a uid
	 * @return a Collection<Object>  of data record objects
	 * @throws NEDSSDAOSysException
	 * @throws NEDSSSystemException
	 */
	public Collection<Object> load(long aUID) throws NEDSSDAOSysException,
			NEDSSSystemException {

		return null;
	};

	// public void store(Object obj) throws NEDSSDAOSysException,
	// NEDSSSystemException, NEDSSConcurrentDataException {};

	/**
	 * A base method to be overriden for updating data records to database
	 * 
	 * @param coll
	 *            a Collection<Object>  of data records to be updated
	 * @throws NEDSSDAOSysException
	 * @throws NEDSSSystemException
	 */
	public void store(Collection<Object> coll) throws NEDSSDAOSysException,
			NEDSSSystemException {
	};

	/**
	 * A base method to be overriden for updating a data record to database
	 * 
	 * @param aUID
	 *            a long representing an uid
	 * @param obj
	 *            an Object object to be updated
	 * @throws NEDSSDAOSysException
	 * @throws NEDSSSystemException
	 */
	public void store(long aUID, Object obj) throws NEDSSDAOSysException,
			NEDSSSystemException {
	};

	/**
	 * A base method to be overriden for updating data records to database
	 * 
	 * @param aUID
	 *            a long representing an uid
	 * @param coll
	 *            a Collection<Object>  of data records to be updated
	 * @throws NEDSSDAOSysException
	 * @throws NEDSSSystemException
	 */
	public void store(long aUID, Collection<Object> coll) throws NEDSSDAOSysException,
			NEDSSSystemException {
	};

	/**
	 * A base method to be overriden for updating data records to database
	 * 
	 * @param aUID
	 *            a long number representing an uid
	 * @param seqNum
	 *            an int number representing a data record's sequence number
	 * @param coll
	 *            a Collection<Object>  of data records to be updated
	 * @throws NEDSSDAOSysException
	 * @throws NEDSSSystemException
	 */
	public void store(long aUID, int seqNum, Collection<Object> coll)
			throws NEDSSDAOSysException, NEDSSSystemException {
	};

	/**
	 * A base method to be overriden for deleting data records from database
	 * 
	 * @param aUID
	 *            a long representing a uid
	 * @throws NEDSSDAOSysException
	 * @throws NEDSSSystemException
	 */
	public void remove(long aUID) throws NEDSSDAOSysException,
			NEDSSSystemException {
	};

	// Abstract Method for Association DAOs

	/**
	 * A base method to be overriden for retrieving data records from database
	 * 
	 * @param scopingEntityUid
	 *            a long representing a uid
	 * @return Collection<Object>  of data record objects
	 * @throws NEDSSDAOSysException
	 * @throws NEDSSSystemException
	 */
	public Collection<Object> loadScoping(long scopingEntityUid)
			throws NEDSSDAOSysException, NEDSSSystemException {

		return null;
	};

	/**
	 * A finder method to be overriden for finding data records based on a
	 * primary key
	 * 
	 * @param pk
	 *            a long number representing a primary key
	 * @return a Long object representing the primary key for the found data
	 *         record
	 * @throws NEDSSDAOSysException
	 * @throws NEDSSSystemException
	 */
	public Long findByPrimaryKey(long pk) throws NEDSSDAOSysException,
			NEDSSSystemException {

		return null;
	};

	/**
	 * A finder method to be overriden for finding data records based on some
	 * key words
	 * 
	 * @param obj
	 *            an Object object containing the serach criteria (key words)
	 * @param cacheNumber
	 *            an int number defining the number of records returned
	 * @param fromIndex
	 *            an int number defined the starting index of the returned data
	 *            records
	 * @return a List<Object> object containing the data record returned
	 * @throws NEDSSDAOSysException
	 * @throws NEDSSSystemException
	 */
	public DisplayList findByKeyWords(Object obj, int cacheNumber, int fromIndex)
			throws NEDSSDAOSysException, NEDSSSystemException {

		return null;
	};

	/**
	 * A method used to set an EntityContext object
	 * 
	 * @param context
	 *            an EntityContext object to be set
	 * @throws RemoteException
	 */
	public void setContext(EntityContext context) throws RemoteException {
		cntx = context;
	}

	/**
	 * A method used to de-reference an EJBContext object
	 * 
	 * @throws RemoteException
	 */
	public void unsetContext() throws RemoteException {
		cntx = null;
	}

	/**
	 * A method used to obtain an EntityContext object
	 * 
	 * @return an EntityContext object
	 */
	public EntityContext getContext() {

		return cntx;
	}

	/**
	 * A method used to grab a db connection from the nbs_ods database
	 * connection pool
	 * 
	 * @return a Connection object
	 * @throws NEDSSSystemException
	 */
	public Connection getConnection() throws NEDSSSystemException {

		Connection dbConnection = null;
		InitialContext initCntx = null;
		logger.debug("STEP 1:-inside the create connection method");
		/*
		 * if (DEBUG_MODE) { changing getConnection for testing purposes
		 * 
		 * //logger.debug("getting a connection - DEBUG_MODE"); // if (nu ==
		 * null) // logger.debug("\nCouldn't get an instance of NEDSS Utils - to
		 * use it"); return nu.getTestConnection(); } else {
		 */
		try {
			logger.debug("STEP 2:-inside the create connection method and dataSource:-"+ dataSource);
			
			if (dataSource == null) {
				logger.debug("STEP 2:-inside the create connection method and jndiCntx:-"+ jndiCntx);
				
				if (jndiCntx == null) {

					logger.debug("STEP 3:-inside the create connection method and initCntx:-"+ initCntx);
					
					if (initCntx == null) {
                        //Properties ht = new Properties();
                        //ht.put(Context.INITIAL_CONTEXT_FACTORY, "org.jboss.as.naming.InitialContextFactory");
                        //ht.put(Context.PROVIDER_URL, "remote://localhost:4447");
                        //ht.put(Context.URL_PKG_PREFIXES, "org.jboss.ejb.client.naming");
                        //initCntx = new InitialContext(ht);
						initCntx = new InitialContext();
					}
					logger.debug("STEP 4:-inside the create connection method and before lookup:-");

					//jndiCntx = (Context) initCntx.lookup("java:jboss/datasources/NedssDataSource");
					dataSource = (DataSource)initCntx.lookup("java:jboss/datasources/NedssDataSource");
					logger.debug("STEP 5:-inside the create connection method and after lookup jndiCntx:-"+ jndiCntx);
				}

				logger.debug("STEP 6:-inside the create connection method and before datasource lookup ");
				//dataSource = (DataSource) jndiCntx
				//		.lookup(JNDINames.NEDSS_DATASOURCE);
				logger.debug("STEP 7:-inside the create connection method and after datasource lookup");
			}

			// Grab a database connection
			dbConnection = dataSource.getConnection();
			/*
			 * if ( dbConnection != null ) { DatabaseMetaData dm =
			 * dbConnection.getMetaData(); StringBuffer message = new
			 * StringBuffer("------------------------------------------- \n JDBC
			 * driver details "); message.append("\n Database product name : " +
			 * dm.getDatabaseProductName() ); message.append("\n Database
			 * product version : " + dm.getDatabaseProductVersion() );
			 * message.append("\n Major Version : " + dm.getDriverMajorVersion() );
			 * message.append("\n MinorVersion : " + dm.getDriverMinorVersion() );
			 * message.append("\n Driver Name : " + dm.getDriverName() );
			 * message.append("\n Driver Version : " + dm.getDriverVersion());
			 * logger.debug(message); }
			 */
			// logger.debug("Got connection in bmpbase: " + dbConnection);
			return dbConnection;
		} catch (SQLException sex) {
			sex.printStackTrace();
			throw new NEDSSSystemException(
					"Exception while getConnection(): \n" + sex.getMessage());
		} catch (NamingException nex) {
			nex.printStackTrace();
			throw new NEDSSSystemException(
					"Error while looking up data source: \n" + nex.getMessage());
		} finally {
			closeContext(initCntx);

			if (jndiCntx != null)

				try {
					jndiCntx.close();
				} catch (Exception ex) {
				}
		}

		// }
	} // end of getConnection()

	/**
	 * A method used to grab a db connection from the named database connection
	 * pool
	 * 
	 * @param odbcName
	 *            a String object representing the name of the data source
	 * @return a Connection object
	 * @throws NEDSSSystemException
	 */
	public Connection getConnection(String odbcName)
			throws NEDSSSystemException {

		Connection dbConnection = null;
		InitialContext initCntx = null;

		/*
		 * if (DEBUG_MODE) { changing getConnection for testing purposes
		 * 
		 * //logger.debug("getting a connection - DEBUG_MODE"); if (nu == null)
		 * logger.debug("\nCouldn't get an instance of NEDSS Utils - to use
		 * it"); return nu.getTestConnection(); } else {
		 */
		try {
			{

				if (jndiCntx == null) {

					if (initCntx == null) {
						initCntx = new InitialContext();
					}

					//jndiCntx = (Context) initCntx.lookup("java:comp/env/jdbc/");
				}

				if (odbcName.compareToIgnoreCase(NEDSSConstants.MSGIN) == 0) {
					
					dataSource2 = (DataSource)initCntx.lookup("java:jboss/datasources/MsgInDataSource");
					
					//dataSource2 = (DataSource) jndiCntx.lookup(JNDINames.MSGIN_DATASOURCE);
					
					dbConnection = dataSource2.getConnection();
				} else if (odbcName.compareToIgnoreCase(NEDSSConstants.MSGOUT) == 0) {

					dataSource2 = (DataSource)initCntx.lookup("java:jboss/datasources/MsgOutDataSource");
					//dataSource2 = (DataSource) jndiCntx.lookup(JNDINames.MSGOUT_DATASOURCE);
					dbConnection = dataSource2.getConnection();
				} else if (odbcName.compareToIgnoreCase(NEDSSConstants.ODS) == 0) {
					
					dataSource2 = (DataSource)initCntx.lookup("java:jboss/datasources/NedssDataSource");
					//dataSource2 = (DataSource) jndiCntx.lookup(JNDINames.NEDSS_DATASOURCE);
					dbConnection = dataSource2.getConnection();
				} else if (odbcName.compareToIgnoreCase(NEDSSConstants.ELRXREF) == 0) {
					
					dataSource2 = (DataSource)initCntx.lookup("java:jboss/datasources/ElrXrefDataSource");
					//dataSource2 = (DataSource) jndiCntx.lookup(JNDINames.ELRXREF_DATASOURCE);
					dbConnection = dataSource2.getConnection();
					
				} else if (odbcName.compareToIgnoreCase(NEDSSConstants.RDB) == 0) {
					
					dataSource2 = (DataSource)initCntx.lookup("java:jboss/datasources/RdbDataSource");
					//dataSource2 = (DataSource) jndiCntx.lookup(JNDINames.ELRXREF_DATASOURCE);
					dbConnection = dataSource2.getConnection();
				} else if (odbcName.compareToIgnoreCase(NEDSSConstants.SRT) == 0) {
					
					dataSource2 = (DataSource)initCntx.lookup("java:jboss/datasources/SrtDataSource");
					//dataSource2 = (DataSource) jndiCntx.lookup(JNDINames.ELRXREF_DATASOURCE);
					dbConnection = dataSource2.getConnection();
				}
			}

			// logger.debug("Got connection in bmpbase for "+ odbcName +": " +
			// dbConnection);
			return dbConnection;
		} catch (SQLException sex) {
			sex.printStackTrace();
			throw new NEDSSSystemException(
					"Exception while getConnection(): \n" + sex.getMessage());
		} catch (NamingException nex) {
			nex.printStackTrace();
			throw new NEDSSSystemException(
					"Error while looking up data source: \n" + nex.getMessage());
		} finally {
			closeContext(initCntx);

			if (jndiCntx != null)

				try {
					jndiCntx.close();
				} catch (Exception ex) {
				}
		}

		// }
	}

	// Releases the connection back to the data source
	protected void releaseConnection(Connection connection)
			throws NEDSSSystemException {

		try {

			if (connection != null && !connection.isClosed()) {
				connection.close();

				// logger.debug("Close connection in bmpbase: " + connection);
				// logger.debug("");
				// connection = null;
			}
		} catch (SQLException sex) {
			sex.printStackTrace();

			// throw new NEDSSSystemException("Exception while
			// releaseConnection(): \n" +
			// sex.getMessage());
		}
	} // end of releaseConnection()

	// Close the resultSet
	protected void closeResultSet(ResultSet resultSet)
			throws NEDSSSystemException {

		try {

			if (resultSet != null) {
				resultSet.close();

				// //logger.error("Close resultset in bmpbase");
			}
		} catch (SQLException sex) {
			sex.printStackTrace();

			// throw new NEDSSSystemException("Exception while closeResultSet():
			// \n" +
			// sex.getMessage());
		}
	} // end of closeResultSet()

	// Close the statement
	protected void closeStatement(Statement stmt) throws NEDSSSystemException {

		try {

			if (stmt != null) {
				stmt.close();

				// //logger.error("Close statement in bmpbase");
			}
		} catch (SQLException sex) {
			sex.printStackTrace();

			// throw new NEDSSSystemException("Exception while closeStatement():
			// \n" +
			// sex.getMessage());
		}
	} // end of closeStatement()

	protected void closeCallableStatement(CallableStatement stmt)
			throws NEDSSSystemException {

		try {

			if (stmt != null) {
				stmt.close();
			}
		} catch (SQLException sex) {
			sex.printStackTrace();

			// throw new NEDSSSystemException("Exception while closeStatement():
			// \n" +
			// sex.getMessage());
		}
	} // end of closeStatement()
	
    protected PreparedStatement setLong(PreparedStatement ps, int i, Long l) throws SQLException
    {
        if (l == null)
            ps.setNull(i, Types.INTEGER);
        else
            ps.setLong(i, l);

        return ps;
    }

    protected PreparedStatement setTimestamp(PreparedStatement ps, int i, Timestamp t) throws SQLException
    {
        if (t == null)
            ps.setNull(i, Types.TIMESTAMP);
        else
            ps.setTimestamp(i, t);

        return ps;
    }

	private void closeContext(InitialContext ctx) {

		try {

			if (ctx != null)
				ctx.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
} // end of DBConnection class
