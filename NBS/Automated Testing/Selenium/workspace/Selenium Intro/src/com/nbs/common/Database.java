package com.nbs.common;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
//import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

//import com.gargoylesoftware.htmlunit.javascript.host.Console;
import com.rtts.utilities.Log;

public class Database {

	String dbDriver = "com.microsoft.sqlserver.jdbc.SQLServerDriver";
	// String dbDriver = "com.microsoft.sqlserver.jdbc.SQLServerDriver";
	String url = "";// "jdbc:odbc:nedss-dbsql\\tst";
	Connection con = null;
	Statement stmt = null;
	ResultSet rs = null;

	public String getXMLLabReport(String database, String labID) {
		String xml = "";

		try {
			// createConnection();
			Class.forName(dbDriver);
			// try{
			// InitialContext initCntx = new InitialContext();
			url = "jdbc:sqlserver:" + database + ";SelectMethod=cursor;DatabaseName=nbs_odse";
			// DataSource dataSource =
			// (DataSource)initCntx.lookup("java:comp/env/jdbc/NedssDataSource");
			// Connection dbConnection = dataSource.getConnection();
			// }catch(NamingException e){}

			con = DriverManager.getConnection(url, "nbs_ods", "ods");
			TimeUnit.SECONDS.sleep(10);
			stmt = con.createStatement();

			rs = stmt.executeQuery("select payload from EDX_Document where act_uid in "
					+ "(select observation_uid from observation where local_id='" + labID + "')");

			while (rs.next()) {

				xml = rs.getString(1);
			}

		} catch (Exception e) {

			System.out.println("Error: SQL Exception 1 " + e.getMessage());
		}

		return xml;
	}

	public boolean jurisdictionDerivated(String database, String zipCode) {
		String jurisdiction = "";
		boolean found = false;

		try {

			Class.forName(dbDriver);

			url = "jdbc:sqlserver:" + database + ";SelectMethod=cursor;DatabaseName=nbs_srte";
			con = DriverManager.getConnection(url, "nbs_ods", "ods");
			TimeUnit.SECONDS.sleep(3);
			stmt = con.createStatement();
			TimeUnit.SECONDS.sleep(3);
			rs = stmt.executeQuery("select * from jurisdiction_participation where fips_cd ='" + zipCode + "'");

			while (rs.next()) {
				jurisdiction = rs.getString(1);
			}

			if (jurisdiction != null && jurisdiction.length() > 0)
				found = true;
		} catch (Exception e) {
			System.out.println("Error: SQL Exception 2" + e.getMessage());
		}
		return found;
	}

	public boolean loincExists(String database, String loinc) {
		String result = "";
		boolean found = false;

		try {

			Class.forName(dbDriver);

			url = "jdbc:sqlserver:" + database + ";SelectMethod=cursor;DatabaseName=nbs_srte";
			con = DriverManager.getConnection(url, "nbs_ods", "ods");
			stmt = con.createStatement();
			rs = stmt.executeQuery("select * from loinc_code where loinc_cd='" + loinc + "'");

			while (rs.next()) {
				result = rs.getString(1);
			}

			if (result != null && result.length() > 0)
				found = true;
		} catch (Exception e) {
			System.out.println("Error: SQL Exception 3" + e.getMessage());
		}
		return found;
	}

	public int getNbsInterfaceQueued(String database) {
		String result = "";
		// boolean found=false;
		int resultInt = 0;

		try {

			Class.forName(dbDriver);

			url = "jdbc:sqlserver:" + database + ";SelectMethod=cursor;DatabaseName=nbs_odse";
			con = DriverManager.getConnection(url, "nbs_ods", "ods");
			stmt = con.createStatement();
			rs = stmt.executeQuery(
					"select count(*) from nbs_interface where record_status_cd='QUEUED' and imp_exp_ind_cd ='I'");

			if (rs != null && rs.next()) {
				result = rs.getString(1);
				resultInt = Integer.parseInt(result);
			}

		} catch (Exception e) {
			System.out.println("Error: SQL Exception 4 " + e.getMessage());
		}

		return resultInt;
	}

	public String getConditionLinkedToLoinc(String database, String loinc) {
		String result = "";
		boolean found = false;

		try {

			Class.forName(dbDriver);

			url = "jdbc:sqlserver:" + database + ";SelectMethod=cursor;DatabaseName=nbs_srte";
			con = DriverManager.getConnection(url, "nbs_ods", "ods");
			stmt = con.createStatement();
			rs = stmt.executeQuery("select disease_nm from loinc_condition where loinc_cd='" + loinc + "'");

			while (rs.next() && result.length() > 0) {// TODO: review
														// result.length()>0
														// does it make any
														// sense??
				result = rs.getString(1);
			}

			if (result != null) {
				found = true;
			}

		} catch (Exception e) {
			System.out.println("Error: SQL Exception 5" + e.getMessage());
		}
		return result;
	}

	/**
	 * getHL7MessageFromMsgOutProcessor():
	 * 
	 * @param loinc
	 * @return
	 */

	public String getHL7MessageFromMsgOutProcessor(String database, Log log) {
		String resultPublicHealthCase = "";
		String resultCnTransportQOutUid = "";
		String hl7 = "";

		boolean found = false;

		try {

			Class.forName(dbDriver);

			url = "jdbc:sqlserver:" + database + ";SelectMethod=cursor;DatabaseName=nbs_odse";
			con = DriverManager.getConnection(url, "nbs_ods", "ods");
			stmt = con.createStatement();

			rs = stmt.executeQuery(
					"select public_health_case_local_id, cn_transportq_out_uid, * from nbs_odse..cn_transportq_out order by add_time desc");

			while (rs.next() && !found) {
				resultPublicHealthCase = rs.getString(1);
				resultCnTransportQOutUid = rs.getString(2);
				found = true;
			}
			stmt.close();

			log.write("The public health case is: " + resultPublicHealthCase + ". The cnTransportQOutUid is: "
					+ resultCnTransportQOutUid);
			hl7 = getHL7message(database, resultPublicHealthCase, resultCnTransportQOutUid);
			log.write("The HL7 message is: " + hl7);

		} catch (Exception e) {
			System.out.println("Error: SQL Exception 6" + e.getMessage());
		}
		return hl7;
	}

	public String getHL7message(String database, String resultPublicHealthCase, String resultCnTransportQOutUid) {

		String hl7 = null;
		try {

			Class.forName(dbDriver);

			url = "jdbc:sqlserver:" + database + ";SelectMethod=cursor;DatabaseName=nbs_odse";
			con = DriverManager.getConnection(url, "nbs_ods", "ods");
			stmt = con.createStatement();

			while (hl7 == null) {
				rs = stmt.executeQuery(
						"select public_health_case.local_id, public_health_case.public_health_case_uid, act_relationship.source_act_uid, notification.notification_uid,"
								+ "notification.local_id, cn_transportq_out.last_chg_time,cn_transportq_out.cn_transportq_out_uid, notification.record_status_time, convert(varchar(max), convert(varbinary(max), nbs_msgoute..transportq_out.payloadcontent)) "
								+ "as HL7_Message " + "FROM nbs_odse..public_health_case "
								+ "left JOIN nbs_odse..act_relationship on public_health_case.public_health_case_uid = act_relationship.target_act_uid "
								+ "left join nbs_odse..Notification on act_relationship.source_act_uid = notification.notification_uid "
								+ "left join nbs_msgoute..transportq_out on notification.local_id=nbs_msgoute..transportq_out.messageId "
								+ "left join nbs_odse..CN_transportq_out on notification.notification_uid=cn_transportq_out.notification_uid "
								+ "where public_health_case.local_id = '" + resultPublicHealthCase
								+ "' and cn_transportq_out.cn_transportq_out_uid='" + resultCnTransportQOutUid + "'");

				while (rs.next()) {
					hl7 = rs.getString(9);
				}

			}
		} catch (Exception e) {
			System.out.println("Error: SQL Exception 7" + e.getMessage());
		}
		return hl7;

	}

	public String getNotificationIdFromInvestigationId(String database, String investigationId) {

		String notificationId = "";
		// boolean found=false;

		try {

			Class.forName(dbDriver);
			System.out.println("database " + database); // added by Deepthi
			url = "jdbc:sqlserver:" + database + ";SelectMethod=cursor;DatabaseName=nbs_odse";
			con = DriverManager.getConnection(url, "nbs_ods", "ods");
			stmt = con.createStatement();
			// rs=stmt.executeQuery("select local_id from nbs_odse..Notification
			// where notification_uid ="+
			// "(select source_act_uid from nbs_odse..Act_relationship where
			// target_act_uid ="+
			// "(select public_health_case_uid from nbs_odse..Public_health_case
			// where local_id='"+investigationId+"') and type_cd =
			// 'Notification')");

			rs = stmt.executeQuery("select local_id from nbs_odse..Notification where notification_uid ="
					+ "(select source_act_uid from nbs_odse..Act_relationship where target_act_uid ="
					+ "(select public_health_case_uid from nbs_odse..Public_health_case where local_id='"
					+ investigationId + "') and type_cd = 'Notification')");

			while (rs.next()) {
				notificationId = rs.getString(1);
			}

		} catch (Exception e) {
			System.out.println("Error: SQL Exception 8 " + e.getMessage());
		}
		return notificationId;

	}

	static public Connection createConnection() throws Exception {
		Connection con = null;
		try {
			Context initCtx = new InitialContext();
			// Context envCtx = (Context)
			// initCtx.lookup("java:comp/env");
			DataSource ds = (DataSource) initCtx.lookup("java:comp/env/jdbc/NedssDataSource");
			con = ds.getConnection();
		} catch (SQLException ex) {
			throw new Exception("Unable to open MSG_OUT connection: " + ex.getMessage());
		} catch (NamingException n) {
			throw new Exception("Invalid JNDI name: /jdbc/MsgOutDataSource" + n.getMessage());
		}
		return con;
	}

}