package gov.cdc.nedss.systemservice.ejb.nbsdocumentejb.dao;

import gov.cdc.nedss.act.sqlscript.WumSqlQuery;
import gov.cdc.nedss.entity.person.dt.PersonDT;
import gov.cdc.nedss.entity.person.vo.PersonVO;
import gov.cdc.nedss.exception.NEDSSDAOSysException;
import gov.cdc.nedss.exception.NEDSSSystemException;
import gov.cdc.nedss.proxy.ejb.investigationproxyejb.dao.ObservationSummaryDAOImpl;
import gov.cdc.nedss.proxy.ejb.investigationproxyejb.dao.RetrieveSummaryVO;
import gov.cdc.nedss.systemservice.ejb.nbsdocumentejb.dt.EDXEventProcessCaseSummaryDT;
import gov.cdc.nedss.systemservice.ejb.nbsdocumentejb.dt.EDXEventProcessDT;
import gov.cdc.nedss.systemservice.ejb.nbsdocumentejb.dt.NBSDocumentDT;
import gov.cdc.nedss.systemservice.ejb.nbsdocumentejb.dt.SummaryDT;
import gov.cdc.nedss.systemservice.ejb.nbsdocumentejb.vo.NBSDocumentVO;
import gov.cdc.nedss.systemservice.nbssecurity.NBSBOLookup;
import gov.cdc.nedss.systemservice.nbssecurity.NBSOperationLookup;
import gov.cdc.nedss.systemservice.nbssecurity.NBSSecurityObj;
import gov.cdc.nedss.systemservice.uidgenerator.UidClassCodes;
import gov.cdc.nedss.systemservice.uidgenerator.UidGeneratorHelper;
import gov.cdc.nedss.util.DAOBase;
import gov.cdc.nedss.util.LogUtils;
import gov.cdc.nedss.util.NEDSSConstants;
import gov.cdc.nedss.util.PropertyUtil;
import gov.cdc.nedss.util.ResultSetUtils;
import gov.cdc.nedss.webapp.nbs.action.observation.review.util.ObservationReviewQueueUtil;
import gov.cdc.nedss.webapp.nbs.logicsheet.helper.CachedDropDownValues;

import java.io.ByteArrayInputStream;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.sql.Types;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;


public class NbsDocumentDAOImpl  extends DAOBase {
		  private static final LogUtils logger = new LogUtils(NbsDocumentDAOImpl.class.
		      getName());
		  private final String NEXT = "NEXT";
		  private static PropertyUtil propertyUtil= PropertyUtil.getInstance();
		  private static CachedDropDownValues cdv = new CachedDropDownValues();
		  public NbsDocumentDAOImpl() 
		  {
			  
		  }

		  public Collection<Object> getDocReportSummaryVOCollForSecurity(NBSSecurityObj nbsSecurityObj)
		    {
			  ObservationReviewQueueUtil util = new ObservationReviewQueueUtil();
			  
		    
			  Collection<Object> reportSummaryColl = new ArrayList<Object> ();
		      Collection<Object> docList = null;
		      String tempStr = "";
		      try
		      {
		    	 docList = getDocReportSummaryListforSecurity(nbsSecurityObj);
		     	 if(docList!=null && docList.size()==0)
		     		docList=null;
		     	//Collection<Object> resultedColl = new ArrayList<Object> ();
		     	HashMap<Object, Object> mapForDoc = new HashMap<Object, Object>();
		       // Long nbsDocUid = null;
		     	if (docList != null) {
		            Iterator<Object> docIt = docList.iterator();
		            while (docIt.hasNext()) {
	            	SummaryDT docResultedSummaryVO = (SummaryDT) docIt.next();
	            	
	            			SummaryDT docSummaryVO = new SummaryDT();
	            			docSummaryVO.setNbsDocumentUid(docResultedSummaryVO.getNbsDocumentUid());
	            			docSummaryVO.setLocalId(docResultedSummaryVO.getLocalId());
	            			docSummaryVO.setMPRUid(docResultedSummaryVO.getMPRUid());
	            			docSummaryVO.setJurisdiction(docResultedSummaryVO.getJurisdiction());
	            			docSummaryVO.setJurisdictionCd(docResultedSummaryVO.getJurisdiction());
	            			docSummaryVO.setProgramArea(docResultedSummaryVO.getProgramArea());
	            			docSummaryVO.setProgAreaCd(docResultedSummaryVO.getProgramArea());
	            			docSummaryVO.setAddTime(docResultedSummaryVO.getAddTime());
	            			docSummaryVO.setCd(docResultedSummaryVO.getCd());
	            			docSummaryVO.setCdDescTxt(docResultedSummaryVO.getCdDescTxt());
	            			docSummaryVO.setType(cdv.getCodeShortDescTxt(docResultedSummaryVO.getDocType(), "PUBLIC_HEALTH_EVENT"));
	            			docSummaryVO.setExternalVersionCtrlNbr(docResultedSummaryVO.getExternalVersionCtrlNbr());
	            			docSummaryVO.setPayloadViewIndCd(docResultedSummaryVO.getPayloadViewIndCd());
	            			docSummaryVO.setSendingFacilityNm(docResultedSummaryVO.getSendingFacilityNm()); 
	            			
	            			ObservationSummaryDAOImpl osd = new ObservationSummaryDAOImpl();
							if(docSummaryVO.getNbsDocumentUid()!=null){
								Long summaryLong = docSummaryVO.getNbsDocumentUid();
								ArrayList<Object>  providerDetails = osd.getProviderInfo(summaryLong,"ORD");
							
								util.getProviderInformationCaseReport(providerDetails, docSummaryVO);
							}
	            			if (docSummaryVO.getProgramArea() != null) {
	            	             tempStr = cdv.getProgramAreaDesc(docSummaryVO.getProgramArea());
	            	             if(tempStr != null)
	            	            	 docSummaryVO.setProgramArea(tempStr);
	            	           }
	            			docSummaryVO.setJurisdiction(docResultedSummaryVO.getJurisdiction());
            	            if (docSummaryVO.getJurisdiction() != null) {
            	            	tempStr = cdv.getJurisdictionDesc(docSummaryVO.getJurisdiction());
            	            	if (tempStr != null )
            	            	docSummaryVO.setJurisdiction(tempStr);
            	            }
            	            if(docResultedSummaryVO.getFirstName() != null)
            	            	docSummaryVO.setFirstName(docResultedSummaryVO.getFirstName());
            	            if(docResultedSummaryVO.getLastName() != null)
            	            	docSummaryVO.setLastName(docResultedSummaryVO.getLastName());      
            	            if(docResultedSummaryVO.getCurrSexCd() != null)
            	            	docSummaryVO.setCurrSexCd(docResultedSummaryVO.getCurrSexCd());      
            	            if(docResultedSummaryVO.getBirthTime() != null)
            	            	docSummaryVO.setBirthTime(docResultedSummaryVO.getBirthTime());      
            	            if(docResultedSummaryVO.getPersonLocalId() != null)
            	            	docSummaryVO.setPersonLocalId(docResultedSummaryVO.getPersonLocalId());
            	            
            	            mapForDoc.put(docResultedSummaryVO.getNbsDocumentUid(),
            	        		   docSummaryVO);
            	          //  nbsDocUid = docResultedSummaryVO.getNbsDocumentUid();
                       
	                   }
	                 }
		     	reportSummaryColl.addAll(mapForDoc.values());
		      }catch(Exception e)
		      {
		          logger.error("Error while getting document for Security"+e.getMessage(),e);
		      }
		      return reportSummaryColl;
		    }
		  
		  @SuppressWarnings("unchecked")
		private Collection<Object> getDocReportSummaryListforSecurity(NBSSecurityObj nbsSecurityObj)
		  {

			  ArrayList<Object>  docReportSummaryList = new ArrayList<Object> ();
			  int docCountFix = propertyUtil.getDocCount();
			  String docQuery = "";
			  try{

			  if(nbsSecurityObj.getPermission(NBSBOLookup.DOCUMENT,NBSOperationLookup.ASSIGNSECURITY))
			  {
			    if (propertyUtil.getDatabaseServerType() != null &&
			      propertyUtil.
			      getDatabaseServerType().equalsIgnoreCase(NEDSSConstants.ORACLE_ID)) {

			    	/*docQuery = 
			    		" SELECT  nd.nbs_document_uid  \"nbsDocumentUid\",  nd.local_id  \"localId\", per.person_parent_uid \"MPRUid\"," +   
			    		 " pername.first_nm  \"firstName\", pername.last_nm \"lastName\" " +
						 " nd.jurisdiction_cd \"jurisdiction\",  nd.prog_area_cd \"programArea\" ,  nd.add_time \"dateReceived\" , nd.cd \"cd\" , nd.cd_desc_txt \"cdDescTxt\" , nd.doc_type_cd \"docType\" " +   
				    	" FROM nbs_document nd, participation par, person per, Person_name pername  WHERE " +
				    	" nd.nbs_document_uid in "+
				    	" ( select nbs_document_uid from nbs_document nbd  where (nbd.record_status_cd = 'UNPROCESSED') and " + 
				    	" ((nbd.prog_area_cd IS NULL OR nbd.prog_area_cd = '') OR (nbd.jurisdiction_cd IS NULL OR nbd.jurisdiction_cd = ''))  and nbd.doc_type_cd = '"+ NEDSSConstants.PHC_236 +"'"+
				    	" and rownum <= "+docCountFix+ ")" + "and per.person_uid (+)= pername.person_uid " +
				    	" and par.act_uid = nd.nbs_document_uid and par.subject_entity_uid = per.person_uid and par.type_cd='"+NEDSSConstants.SUBJECT_OF_DOC+"' order by nd.nbs_document_uid"  ;*/
			    	
			    	docQuery =  " SELECT  nd.sending_facility_nm  \"sendingFacilityNm\", nd.nbs_document_uid  \"nbsDocumentUid\",  nd.local_id  \"localId\", nd.external_version_ctrl_nbr \"externalVersionCtrlNbr\", per.person_parent_uid \"MPRUid\"," +
			    	            " NVL(pername.first_nm, 'No First')  \"firstName\", NVL(pername.last_nm, 'No Last') \"lastName\", per.local_id \"personLocalId\" , " +
			    	            " per.curr_sex_cd \"currSexCd\", " +
								 " per.birth_time \"birthTime\", " +
					             " nd.jurisdiction_cd \"jurisdiction\",  nd.prog_area_cd \"programArea\" ,  nd.add_time \"addTime\" , nd.cd \"cd\" , nd.cd_desc_txt \"cdDescTxt\" , nd.doc_type_cd \"docType\", nd.payload_view_ind_cd \"payloadViewIndCd\" " +   
        			             " FROM participation par, person per, Person_name pername, " +
					             " ( select * from nbs_document nbd  where (nbd.record_status_cd = 'UNPROCESSED') and " + 
							     " ((nbd.prog_area_cd IS NULL OR nbd.prog_area_cd = '') OR (nbd.jurisdiction_cd IS NULL OR nbd.jurisdiction_cd = ''))"
					            //+ " and nbd.doc_type_cd = '"+ NEDSSConstants.PHC_236 +"'"
			                    + " and  rownum <= "+docCountFix+"  order by nbs_document_uid) nd " 			                    
			                    + " WHERE  par.act_uid = nd.nbs_document_uid and par.subject_entity_uid = per.person_uid and par.type_cd='"+NEDSSConstants.SUBJECT_OF_DOC+"'  and per.person_uid (+)= pername.person_uid " ;  


				  }
				  else
					  /*docQuery =  
						  "SELECT  nd.nbs_document_uid  \"nbsDocumentUid\",  nd.local_id  \"localId\", per.person_parent_uid \"MPRUid\"," +  
						  " pername.first_nm  \"firstName\", pername.last_nm \"lastName\" " +
				    	  " nd.jurisdiction_cd \"jurisdiction\",  nd.prog_area_cd \"programArea\",  nd.add_time \"dateReceived\" , nd.cd \"cd\" , nd.cd_desc_txt \"cdDescTxt\" , nd.doc_type_cd \"docType\" " + 
				    	  " FROM nbs_document nd, participation par, person per, Person_name pername WHERE " + 
				    	  " nd.nbs_document_uid in (  select top "+docCountFix+ " nbs_document_uid from nbs_document nbd  where (nbd.record_status_cd = 'UNPROCESSED') and" + 
				    	  "((nbd.prog_area_cd IS NULL OR nbd.prog_area_cd = '') OR (nbd.jurisdiction_cd IS NULL OR nbd.jurisdiction_cd = ''))  and nbd.doc_type_cd = '"+ NEDSSConstants.PHC_236 +"'"+
				    	   " and per.person_uid (+)= pername.person_uid " +
				    	  " order by " + 
				    	  " nbd.nbs_document_uid) and par.act_uid = nd.nbs_document_uid and par.subject_entity_uid = per.person_uid and par.type_cd='"+NEDSSConstants.SUBJECT_OF_DOC+"' " ;*/
			    
			    docQuery =  " SELECT top "+docCountFix+ " nd.sending_facility_nm  \"sendingFacilityNm\", nd.nbs_document_uid  \"nbsDocumentUid\",  nd.local_id  \"localId\", nd.external_version_ctrl_nbr \"externalVersionCtrlNbr\", p.person_parent_uid \"MPRUid\"," +
			    	            " ISNULL(pnm.first_nm, 'No First') \"firstName\", ISNULL(pnm.last_nm, 'No Last') \"lastName\", p.local_id \"personLocalId\" ," +
			    	             " p.curr_sex_cd \"currSexCd\", " +
								  " p.birth_time \"birthTime\", "+
					             " nd.jurisdiction_cd \"jurisdiction\",  nd.prog_area_cd \"programArea\" ,  nd.add_time \"addTime\" , nd.cd \"cd\" , nd.cd_desc_txt \"cdDescTxt\" , nd.doc_type_cd \"docType\", nd.payload_view_ind_cd \"payloadViewIndCd\" " +
		                      " FROM person p Left Outer join person_name pnm on p.person_uid=pnm.person_uid,  participation,nbs_document nd "
				              + " WHERE (participation.act_uid = nd.nbs_document_uid) and p.person_uid = participation.subject_entity_uid "
		                      + " AND (pnm.nm_use_cd = 'L' or pnm.nm_use_cd IS NULL )"
		                      + " and nd.record_status_cd = 'UNPROCESSED' and"
		                      + " ((nd.prog_area_cd IS NULL OR nd.prog_area_cd = '') OR (nd.jurisdiction_cd IS NULL OR nd.jurisdiction_cd = ''))  " 
		                      //+	"and nd.doc_type_cd = '"+ NEDSSConstants.PHC_236 +"'"
		                      + " order by nd.nbs_document_uid ";

			    long timebegin = 0; long timeend = 0;
			    timebegin=System.currentTimeMillis();
			    logger.debug("\n timebegin  for DocReportSummaryListforSecurity " + timebegin);
			    logger.debug("\n docQuery  for Security = "+docQuery);
			                ArrayList<Object>  argList = new ArrayList<Object> ();
			                argList.add(new Integer(docCountFix));
			                SummaryDT docSummaryVO = new SummaryDT();
			                docReportSummaryList = (ArrayList<Object> )preparedStmtMethod(docSummaryVO, null,docQuery, NEDSSConstants.SELECT);
			     timeend = System.currentTimeMillis();
			     logger.debug("\n timeend for DocReportSummaryListforSecurity " + timeend);

			    }

			  }
			  catch(Exception e){
		          logger.fatal("Error in fetching getDocReportSummaryListforSecurity() "+e.getMessage(), e); 
			      throw new NEDSSSystemException(e.toString(), e);
		         }

			  return docReportSummaryList;

		  }
		  
		  
		  public Collection<Object> getDocSummaryVOCollForReview(NBSSecurityObj nbsSecurityObj)
		  {
			  ObservationReviewQueueUtil util = new ObservationReviewQueueUtil();
			  
			  Collection<Object> docList = null;
		      String tempStr = "";
		      try
		      {
		    	 docList = getDocSummaryListforReview(nbsSecurityObj);
		    	 if (docList != null) {
			            Iterator<Object> docIt = docList.iterator();
			            while (docIt.hasNext()) {
			            	SummaryDT docSummaryVO  = (SummaryDT)docIt.next();
			            	
			            	if (docSummaryVO.getProgramArea() != null) {
	            	             tempStr = cdv.getProgramAreaDesc(docSummaryVO.getProgramArea());
	            	             if(tempStr != null){
	            	            	 docSummaryVO.setProgAreaCd(docSummaryVO.getProgramArea());
	            	            	 docSummaryVO.setProgramArea(tempStr);
	            	             }
	            	         }
			            	if (docSummaryVO.getJurisdiction() != null) {
            	            	tempStr = cdv.getJurisdictionDesc(docSummaryVO.getJurisdiction());
            	            	if (tempStr != null ){
            	            		docSummaryVO.setJurisdictionCd(docSummaryVO.getJurisdiction());
            	            	docSummaryVO.setJurisdiction(tempStr);
            	            	}
            	            }
			            	/*if (docSummaryVO.getCd() != null) {
            	            	tempStr = cdv.getConditionDesc(docSummaryVO.getCd());
            	            	if (tempStr != null )
            	            	docSummaryVO.setCdDescTxt(tempStr);
            	            }*/
			            	if (docSummaryVO.getDocType() != null && docSummaryVO.getDocType().equals(NEDSSConstants.PHC_236)) {
            	            	tempStr = cdv.getCodeShortDescTxt(docSummaryVO.getDocType(),"PUBLIC_HEALTH_EVENT");
            	            	if (tempStr != null )
            	            	docSummaryVO.setDocType(tempStr);
            	            }
			            	/*
			      	      //Get the Associated investigation(s)
			      		  RetrieveSummaryVO retrieveSummaryVO = new RetrieveSummaryVO();
			      		  
			      		 ArrayList<Object> uidList = new ArrayList<Object> ();
			  	       uidList.add(docSummaryVO.getMPRUid());
			            	Collection<Object> invSummaryVOs = retrieveSummaryVO.retrieveInvestgationSummaryVO(uidList, nbsSecurityObj);
			            	docSummaryVO.setInvSummaryVOs(invSummaryVOs);
			            	
			            	*/
			            	//Check for Investigation associations
			            		 /*       RetrieveSummaryVO rsvo = new RetrieveSummaryVO();
			            		        docSummaryVO.setInvSummaryVOs(
			            						rsvo.getAssociatedInvListVersion2(docSummaryVO.getMPRUid(), nbsSecurityObj, NEDSSConstants.ACT_CLASS_CD_FOR_DOC)
			            				);
			            				*/
			            				
			            	ObservationSummaryDAOImpl osd = new ObservationSummaryDAOImpl();
							if(docSummaryVO.getNbsDocumentUid()!=null){
								Long summaryLong = docSummaryVO.getNbsDocumentUid();
								ArrayList<Object>  providerDetails = osd.getProviderInfo(summaryLong,"ORD");
							
								util.getProviderInformationCaseReport(providerDetails, docSummaryVO);
								/*
								 if (providerDetails != null && providerDetails.size() > 0) {
					                  Object[] orderProvider = providerDetails.toArray();

					                  if (orderProvider[0] != null) {
					                	  docSummaryVO.setProviderLastName((String) orderProvider[0]);
					                      logger.debug("ProviderLastName: " + (String) orderProvider[0]);
					                  }
					                  if (orderProvider[1] != null){
					                	  docSummaryVO.setProviderFirstName((String) orderProvider[1]);
					                	  logger.debug("ProviderFirstName: " + (String) orderProvider[1]);
					                  }
					                  if (orderProvider[2] != null){
					                	  docSummaryVO.setProviderPrefix((String) orderProvider[2]);
					                   logger.debug("ProviderPrefix: " + (String) orderProvider[2]);
					                  }
					                  if (orderProvider[3] != null){
					                	  docSummaryVO.setProviderSuffix(( String)orderProvider[3]);
					                  	logger.debug("ProviderSuffix: " + (String) orderProvider[3]);
					                  }
					              	if (orderProvider[4] != null){
					              		docSummaryVO.setProviderDegree(( String)orderProvider[4]);
					                 	logger.debug("ProviderDegree: " + (String) orderProvider[4]);
					              	 }
					             	if (orderProvider[5] != null){
					             		docSummaryVO.setProviderUid((String)orderProvider[5]);
					                 	logger.debug("orderProviderUid: " + (Long) orderProvider[5]);
					             	 }
					                }*/
								 //osd.getReportingFacilityInfo(summaryLong,NEDSSConstants.MOB_REPORTER_OF_MORB_REPORT);
								 //Map<Object,Object> uidMap = osd.getLabParticipations(summaryLong);
					            //  if (uidMap != null && uidMap.containsKey(NEDSSConstants.PAR111_TYP_CD) && docSummaryVO != null) {
					            	//  docSummaryVO.setReportingFacility(osd.getReportingFacilityName((Long)uidMap.get(NEDSSConstants.PAR111_TYP_CD)));  
					             // }
								 
								 docSummaryVO.setSendingFacilityNm(docSummaryVO.getSendingFacilityNm());  
					              }

			            	
			            	
			            	
			            	
			            	
			            	
			            	
			            }
		    	 }
		    	 
		    	 

					//Get Provider information
					
							
							
	
		      }catch(Exception e){
		          logger.fatal("Error in fetching Document Summary List<Object> for Review "+e.getMessage(), e); 
			      throw new NEDSSSystemException(e.toString(), e);
		         }
		      return docList;
		    }
		  
		  @SuppressWarnings("unchecked")
		private Collection<Object> getDocSummaryListforReview(NBSSecurityObj nbsSecurityObj)
		  {
		      ArrayList<Object>  docSummaryList = new ArrayList<Object> ();
			  int docCountFix = propertyUtil.getDocCount();
			  String docQuery = "";
			  String docDataAccessWhereClause=null;
			  // This will be "VIEW" for Document
			  String docsNeedingReviewSecurity = "VIEW";
			  if(nbsSecurityObj.getPermission(NBSBOLookup.DOCUMENT,docsNeedingReviewSecurity))
			  {
			        docDataAccessWhereClause = nbsSecurityObj.
			        getDataAccessWhereClause(NBSBOLookup.DOCUMENT,
			        		docsNeedingReviewSecurity);
			        
			        docDataAccessWhereClause = docDataAccessWhereClause != null ? " AND " + docDataAccessWhereClause : "";			        
			  
			  try{
		
			    if (propertyUtil.getDatabaseServerType() != null &&
				      propertyUtil.
				      getDatabaseServerType().equalsIgnoreCase(NEDSSConstants.ORACLE_ID)){
			    	//Removed this query as it was giving duplicate documents if person has alias and legal both names. 
			    	 /*docQuery =  " SELECT"
			    			 + " nbsdoc.nbs_document_uid  \"nbsDocumentUid\", "
			    			 + " nbsdoc.shared_ind  \"sharedInd\", "
			    			 + " nbsdoc.sending_facility_nm  \"sendingFacilityNm\", "
						  + " nbsdoc.local_id  \"localId\", " 
						  + " nbsdoc.doc_type_cd  \"docType\", "
						  + " nbsdoc.external_version_ctrl_nbr \"externalVersionCtrlNbr\", "
						  + " nbsdoc.payload_view_ind_cd \"payloadViewIndCd\", "
						  + " per.person_parent_uid \"MPRUid\", "
						  + " per.curr_sex_cd \"currSexCd\", "
						  + " per.birth_time \"birthTime\", "
						  + " nbsdoc.jurisdiction_cd \"jurisdiction\", "
						  + " nbsdoc.prog_area_cd  \"programArea\", "
						  + " NVL(pername.first_nm, 'No First') \"firstName\", "
						  + " NVL(pername.last_nm, 'No Last') \"lastName\", "
						  + " per.local_id \"personLocalId\", "
						  + " nbsdoc.doc_status_cd \"statusCd\", "
						  + " nbsdoc.add_time \"addTime\", "
						  + " nbsdoc.cd \"cd\", "
						  + " nbsdoc.cd_desc_txt \"cdDescTxt\" "
						  + " FROM nbs_document nbsdoc, participation particip, person per, Person_name pername "
						  + " WHERE particip.act_uid = nbsdoc.nbs_document_uid  and particip.subject_entity_uid = per.person_uid  and "
				    	  + "particip.type_cd='" +NEDSSConstants.SUBJECT_OF_DOC+"'  and per.person_uid = pername.person_uid "
				    	  + "and nbsdoc.record_status_cd = 'UNPROCESSED' "
				    	  + "and rownum <="+docCountFix+" "
				    	  + docDataAccessWhereClause 
				    	  + " order by nbsdoc.nbs_document_uid ";*/
			    	docQuery =  " SELECT"
			    			 + " nbsdoc.nbs_document_uid  \"nbsDocumentUid\", "
			    			 + " nbsdoc.shared_ind  \"sharedInd\", "
			    			 + " nbsdoc.sending_facility_nm  \"sendingFacilityNm\", "
						  + " nbsdoc.local_id  \"localId\", " 
						  + " nbsdoc.doc_type_cd  \"docType\", "
						  + " nbsdoc.external_version_ctrl_nbr \"externalVersionCtrlNbr\", "
						  + " nbsdoc.payload_view_ind_cd \"payloadViewIndCd\", "
						  + " per.person_parent_uid \"MPRUid\", "
						  + " per.curr_sex_cd \"currSexCd\", "
						  + " per.birth_time \"birthTime\", "
						  + " nbsdoc.jurisdiction_cd \"jurisdiction\", "
						  + " nbsdoc.prog_area_cd  \"programArea\", "
						  + " NVL(pername.first_nm, 'No First') \"firstName\", "
						  + " NVL(pername.last_nm, 'No Last') \"lastName\", "
						  + " per.local_id \"personLocalId\", "
						  + " nbsdoc.doc_status_cd \"statusCd\", "
						  + " nbsdoc.add_time \"addTime\", "
						  + " nbsdoc.cd \"cd\", "
						  + " nbsdoc.cd_desc_txt \"cdDescTxt\" "
						  + " FROM nbs_document nbsdoc INNER JOIN participation particip "
						  + " ON particip.act_uid = nbsdoc.nbs_document_uid "
						  + " INNER JOIN person per "
						  + " ON particip.subject_entity_uid = per.person_uid  and "
				    	  + " particip.type_cd='" +NEDSSConstants.SUBJECT_OF_DOC+"' "
				    	  + " LEFT OUTER JOIN Person_name pername "
				    	  + " ON per.person_uid = pername.person_uid AND pername.nm_use_cd = 'L' "
				    	  + "where nbsdoc.record_status_cd = 'UNPROCESSED' "
				    	  + "and rownum <="+docCountFix+" "
				    	  + docDataAccessWhereClause 
				    	  + " order by nbsdoc.nbs_document_uid ";
			    	 
			    }
			    else
			    	//Removed this query as it was giving duplicate documents if person has alias and legal both names. 
			    	/*docQuery =  " SELECT top "+docCountFix+" "
					  		 + " nbsdoc.nbs_document_uid nbsDocumentUid, "
					  		 + " nbsdoc.shared_ind  \"sharedInd\", "
			    			 + " nbsdoc.sending_facility_nm  \"sendingFacilityNm\", "
					  		 + " nbsdoc.local_id localId, " 
					  		 + " nbsdoc.doc_type_cd docType, "
							 + " nbsdoc.payload_view_ind_cd payloadViewIndCd, "
					  		 + " nbsdoc.external_version_ctrl_nbr externalVersionCtrlNbr, "
					  		 + " per.person_parent_uid MPRUid, "
					         + " per.curr_sex_cd \"currSexCd\", "
					         + " per.birth_time \"birthTime\", "
					  		 + " nbsdoc.jurisdiction_cd jurisdiction, "
					  		 + " nbsdoc.prog_area_cd programArea, "
					  		 + " ISNULL(pername.first_nm, 'No First') firstName, "
					  		 + " ISNULL(pername.last_nm, 'No Last') lastName, "
							 + " per.local_id personLocalId, "
					  		 + " nbsdoc.doc_status_cd statusCd, "
					  		 + " nbsdoc.add_time addTime, "
					  		 + " nbsdoc.cd cd, "
					  		 + " nbsdoc.cd_desc_txt cdDescTxt "
					  		 + " FROM nbs_document nbsdoc, "
					  		 + " participation particip, " 
					  		 + " person per, "  
					  		 + " Person_name pername "
					  		 + " WHERE particip.act_uid = nbsdoc.nbs_document_uid "
					  		 + " AND particip.subject_entity_uid = per.person_uid "
					  		 + " AND particip.type_cd='"+NEDSSConstants.SUBJECT_OF_DOC+"' "
					  		 + " AND per.person_uid = pername.person_uid"
					  		 + " AND nbsdoc.record_status_cd = 'UNPROCESSED' "
					  		 + docDataAccessWhereClause 
					  		 + " order by nbsdoc.nbs_document_uid ";*/
			    	
				  	 docQuery =  " SELECT top "+docCountFix+" "
				  		 + " nbsdoc.nbs_document_uid nbsDocumentUid, "
				  		 + " nbsdoc.shared_ind  \"sharedInd\", "
		    			 + " nbsdoc.sending_facility_nm  \"sendingFacilityNm\", "
				  		 + " nbsdoc.local_id localId, " 
				  		 + " nbsdoc.doc_type_cd docType, "
						 + " nbsdoc.payload_view_ind_cd payloadViewIndCd, "
				  		 + " nbsdoc.external_version_ctrl_nbr externalVersionCtrlNbr, "
				  		 + " per.person_parent_uid MPRUid, "
				         + " per.curr_sex_cd \"currSexCd\", "
				         + " per.birth_time \"birthTime\", "
				  		 + " nbsdoc.jurisdiction_cd jurisdiction, "
				  		 + " nbsdoc.prog_area_cd programArea, "
				  		 + " ISNULL(pername.first_nm, 'No First') firstName, "
				  		 + " ISNULL(pername.last_nm, 'No Last') lastName, "
						 + " per.local_id personLocalId, "
				  		 + " nbsdoc.doc_status_cd statusCd, "
				  		 + " nbsdoc.add_time addTime, "
				  		 + " nbsdoc.cd cd, "
				  		 + " nbsdoc.cd_desc_txt cdDescTxt "
				  		 + " FROM nbs_document nbsdoc INNER JOIN "
				  		 + " participation particip ON " 
				  		 + " particip.act_uid = nbsdoc.nbs_document_uid "
				  		 + " INNER JOIN person per ON "  
				  		 + " particip.subject_entity_uid = per.person_uid "
				  		 + " AND particip.type_cd='"+NEDSSConstants.SUBJECT_OF_DOC+"' "
				  		 + " LEFT OUTER JOIN Person_name pername ON"
				  		 + " per.person_uid = pername.person_uid"
				  		 + " AND pername.nm_use_cd = 'L' "
				  		 + " WHERE nbsdoc.record_status_cd = 'UNPROCESSED' "
				  		 + docDataAccessWhereClause 
				  		 + " order by nbsdoc.nbs_document_uid ";
					  
			    long timebegin = 0; long timeend = 0;
			    timebegin=System.currentTimeMillis();
			    logger.debug("\n timebegin  for getDocSummaryListforReview " + timebegin);
			    logger.debug("\n docQuery  for Security = "+docQuery);
			                ArrayList<Object>  argList = new ArrayList<Object> ();
			                argList.add(new Integer(docCountFix));
			                SummaryDT docSummaryVO = new SummaryDT();
			                docSummaryList = (ArrayList<Object> )preparedStmtMethod(docSummaryVO, null,docQuery, NEDSSConstants.SELECT);
			     timeend = System.currentTimeMillis();
			     logger.debug("\n timeend for getDocSummaryListforReview " + timeend);
		
			  }			 
			  catch(Exception e)
			  {
				  logger.fatal("Error in fetching Document Summary List<Object> for Review "+e.getMessage(), e); 
			      throw new NEDSSSystemException(e.toString(), e);
		
			  }
		  }
		
			  return docSummaryList;
		
		  }

		public Long updateNbsDocument(NBSDocumentDT dt){
			  Connection dbConnection = null;
			  PreparedStatement pStmt = null;
			  int resultCount = 0;
			  int i = 1;
			  try {
				  dbConnection = getConnection();
				  pStmt = dbConnection.prepareStatement(WumSqlQuery.UPDATE_NBS_DOCUMENT);
				  pStmt.setTimestamp(i++, dt.getLastChgTime()); //3
				  pStmt.setLong(i++, dt.getLastChgUserId().longValue());//4  

				  pStmt.setString(i++, dt.getRecordStatusCd()); //5
				  pStmt.setTimestamp(i++, (dt.getRecordStatusTime()));//6
				  pStmt.setString(i++, dt.getProgAreaCd()); //7
				  pStmt.setString(i++, dt.getJurisdictionCd()); //8
				  if(dt.getProgramJurisdictionOid()!=null)
					  pStmt.setLong(i++, dt.getProgramJurisdictionOid().longValue()); //8
				  else
					  pStmt.setNull(i++,Types.INTEGER);	
				  pStmt.setString(i++, dt.getTxt()); //9
				  pStmt.setInt(i++, dt.getVersionCtrlNbr().intValue()+1);//10
				  pStmt.setString(i++, dt.getCd()); //11
				  pStmt.setString(i++, dt.getDocPurposeCd()); //12
				  pStmt.setString(i++, dt.getDocStatusCd()); //13
				  pStmt.setString(i++, dt.getCdDescTxt()); //14
				  pStmt.setString(i++,dt.getSendingFacilityNm()); //15
				  pStmt.setString(i++,dt.getProcessingDecisionCd()); //16
				  pStmt.setString(i++, dt.getProcessingDecisiontxt()); //17
				  pStmt.setLong(i++,dt.getNbsDocumentUid().longValue()); //18
				

				  resultCount = pStmt.executeUpdate();

				  if (resultCount != 1)
				  {
					  throw new NEDSSSystemException("Error: none or more than one entity inserted at a time, resultCount = " +
							  resultCount);
				  }
			  }
			  catch (SQLException se)
			  {
				  logger.fatal("SQLException ="+se.getMessage(), se);
				  throw new NEDSSDAOSysException("Error: SQLException while updating NBS_DOCUMENT table\n" +
						  se.getMessage(), se);
			  }
			  finally
			  {
				  closeStatement(pStmt);
				  releaseConnection(dbConnection);
			  }//end of finally

			  return dt.getNbsDocumentUid();

		  }
		  
		  public NBSDocumentVO getNBSDocument(Long nbsDocUid)
		  {
			    Connection dbConnection = null;
				PreparedStatement preparedStmt = null;
				ResultSet resultSet = null;
			    String docQuery = WumSqlQuery.GET_NBS_DOCUMENT	;
			    int i = 1;
			    
			    NBSDocumentVO nbsDocumentVO = new NBSDocumentVO();
			    try{

			    dbConnection = getConnection();
			    NBSDocumentDT nbsDocumentDT = new NBSDocumentDT();
               	preparedStmt = dbConnection.prepareStatement(docQuery);
        		preparedStmt.setLong(i++, nbsDocUid.longValue());    				
        		resultSet = preparedStmt.executeQuery();
        		while(resultSet.next()) {
        			nbsDocumentDT.setNbsDocumentUid(new Long(resultSet.getLong("nbsDocumentUid")));
        			nbsDocumentDT.setLocalId(resultSet.getString("localId"));
        			nbsDocumentDT.setDocTypeCd(resultSet.getString("docTypeCd")); 
        			nbsDocumentDT.setJurisdictionCd(resultSet.getString("jurisdictionCd")); 
        			nbsDocumentDT.setProgAreaCd(resultSet.getString("progAreaCd")); 
        			nbsDocumentDT.setDocStatusCd(resultSet.getString("docStatusCd")); 
        			nbsDocumentDT.setAddTime(resultSet.getTimestamp("addTime")); 
        			nbsDocumentDT.setTxt(resultSet.getString("txt")); 
        			nbsDocumentDT.setVersionCtrlNbr(new Integer(resultSet.getInt("versionCtrlNbr")));
        			nbsDocumentDT.setDocPurposeCd(resultSet.getString("docPurposeCd")); 
        			nbsDocumentDT.setCdDescTxt(resultSet.getString("cdDescTxt")); 
        			nbsDocumentDT.setSendingFacilityNm(resultSet.getString("sendingFacilityNm")); 
        			nbsDocumentDT.setAddUserId(new Long(resultSet.getLong("addUserId"))); 
        			nbsDocumentDT.setRecordStatusCd(resultSet.getString("recordStatusCd")); 
        			nbsDocumentDT.setProcessingDecisionCd(resultSet.getString("processingDecisionCd"));
        			nbsDocumentDT.setProcessingDecisiontxt(resultSet.getString("processingDecisiontxt"));
        			nbsDocumentDT.setExternalVersionCtrlNbr(new Integer(resultSet.getInt("externalVersionCtrlNbr")));
        			nbsDocumentDT.setCd(resultSet.getString("cd")); 
        			
        			if(propertyUtil.getDatabaseServerType() != null && propertyUtil.getDatabaseServerType().equalsIgnoreCase(NEDSSConstants.ORACLE_ID)){
        		        java.sql.Blob blob = (java.sql.Blob) resultSet.getBlob("docPayload");

        		      
        		        byte[] bdata = blob.getBytes(1, (int) blob.length());
        		        String txt = new String(bdata);

						nbsDocumentDT.setPayLoadTxt(txt);
        			}else     				
        				nbsDocumentDT.setPayLoadTxt(resultSet.getString("docPayload"));
        			if(propertyUtil.getDatabaseServerType() != null && propertyUtil.getDatabaseServerType().equalsIgnoreCase(NEDSSConstants.ORACLE_ID)){
        				if(resultSet.getBlob("phdcDocDerived") != null){
	        		        java.sql.Blob blob = (java.sql.Blob) resultSet.getBlob("phdcDocDerived");
	        		        byte[] bdata = blob.getBytes(1, (int) blob.length());
	        		        String txt = new String(bdata);
							nbsDocumentDT.setPhdcDocDerivedTxt(txt);
        				}
        			}else     				
        				nbsDocumentDT.setPhdcDocDerivedTxt(resultSet.getString("phdcDocDerived")); 
        			nbsDocumentDT.setPayloadViewIndCd(resultSet.getString("payloadViewIndCd"));
        			nbsDocumentDT.setNbsDocumentMetadataUid(new Long(resultSet.getLong("nbsDocumentMetadataUid")));
        			nbsDocumentDT.setRecordStatusTime(resultSet.getTimestamp("recordStatusTime"));
        			nbsDocumentDT.setProgramJurisdictionOid(new Long(resultSet.getLong("programJurisdictionOid")));
        			nbsDocumentDT.setSharedInd(resultSet.getString("sharedInd")); 
        			nbsDocumentDT.setLastChgUserId(new Long(resultSet.getLong("lastChgUserId")));        			
        			nbsDocumentDT.setNbsInterfaceUid(new Long(resultSet.getLong("nbsInterfaceUid")));    
        			nbsDocumentDT.setDocEventTypeCd(resultSet.getString("docEventTypeCd"));
        			nbsDocumentDT.setProcessingDecisionCd(resultSet.getString("processingDecisionCd"));
        			nbsDocumentDT.setProcessingDecisiontxt(resultSet.getString("processingDecisiontxt"));
        			PersonVO personVO = new PersonVO();
        			PersonDT personDT = new PersonDT();
        			personDT.setPersonUid(new Long(resultSet.getLong("personUid")));
        			personDT.setPersonParentUid(new Long(resultSet.getLong("MPRUid")));
        			personVO.setThePersonDT(personDT);
        			nbsDocumentVO.setPatientVO(personVO);
        			nbsDocumentVO.setNbsDocumentDT(nbsDocumentDT);	
        			nbsDocumentVO.setEDXEventProcessDTMap(this.getEDXEventProcessMap(nbsDocumentDT.getNbsDocumentUid()));

        			
        		logger.debug("returned Document Information");    		
    			}
        		
			    }catch(Exception e){
			  	  logger.fatal("Error in fetching Document by UID "+e.getMessage(), e); 
			      throw new NEDSSSystemException(e.toString(), e);

			  } finally
	 	         {
				  	closeResultSet(resultSet);
	 	            closeStatement(preparedStmt);
	 	            releaseConnection(dbConnection);
	 	         } //end of finally 

			  return nbsDocumentVO;

		  }
		  
		  public NBSDocumentDT insertNBSDocument( NBSDocumentDT nbsDocumentDT){ 
			  if (propertyUtil.getDatabaseServerType() != null && propertyUtil. getDatabaseServerType().equalsIgnoreCase(NEDSSConstants.ORACLE_ID)) 
				  return insertOracleNBSDocument(nbsDocumentDT);
			else
			  return insertSQLNBSDocument(nbsDocumentDT);
		  }
		  
		  public NBSDocumentDT insertNBSDocumentHist( NBSDocumentDT nbsDocumentDT){ 
			  if (propertyUtil.getDatabaseServerType() != null && propertyUtil. getDatabaseServerType().equalsIgnoreCase(NEDSSConstants.ORACLE_ID)) 
				  return insertOracleNBSDocumentHist(nbsDocumentDT);
			else
			  return insertSQLNBSDocumentHist(nbsDocumentDT);
		  }
		  
		 
	 	public NBSDocumentDT insertSQLNBSDocument( NBSDocumentDT nbsDocumentDT){
	 		String INSERT_INTO_NBS_DOCUMENT="INSERT INTO NBS_document(nbs_document_uid,doc_payload,doc_type_cd,local_id,record_status_cd,record_status_time,add_user_id," +
	 		"add_time,prog_area_cd,jurisdiction_cd,txt,program_jurisdiction_oid,shared_ind,version_ctrl_nbr,cd,last_chg_time,last_chg_user_id,doc_purpose_cd,doc_status_cd,cd_desc_txt,sending_facility_nm," +
	 		" nbs_interface_uid, sending_app_event_id, sending_app_patient_id,phdc_doc_derived,payload_view_ind_cd,nbs_document_metadata_uid, external_version_ctrl_nbr) " +
	 		"VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
	 		String INSERT_ACTIVITY = "INSERT INTO Act (act_uid, class_cd, mood_cd) VALUES (?, ?, ?)";
	 		/**
	 		 * Starts inserting a new NBS_DOCUMENT table
	 		 */
	 		long nbsDocuementUID=-1;
	 		Connection dbConnection = null;
	 		PreparedStatement preparedStmt = null;
	 		String localUID = null;
	 		UidGeneratorHelper uidGen = null;
	 		int resultCount = 0;
	 		try
	 		{
	 			/**
	 			 * Inserts into act table for a new NBS_DOCUMENT
	 			 */
	 			uidGen = new UidGeneratorHelper();
	 			nbsDocuementUID  = uidGen.getNbsIDLong(UidClassCodes.NBS_CLASS_CODE).longValue();
	 			logger.debug("New nbs_Docuement_UID is : " + nbsDocuementUID);
	 			nbsDocumentDT.setNbsDocumentUid(nbsDocuementUID);
	 			dbConnection = getConnection();
	 			preparedStmt = dbConnection.prepareStatement(INSERT_ACTIVITY);
	 			int i = 1;
	 			preparedStmt.setLong(i++, nbsDocuementUID);
	 			preparedStmt.setString(i++, NEDSSConstants.ACT_CLASS_CD_FOR_DOC);
	 			preparedStmt.setString(i++, NEDSSConstants.EVENT_MOOD_CODE);
	 			resultCount = preparedStmt.executeUpdate();
	 		}
	 		catch(Exception e)
	 		{
	 			logger.fatal("Exception while getting dbConnection for checking for an"
	 					+ " existingNBS_document -> " + nbsDocuementUID, e);
	 			logger.fatal("Exception while getting dbConnection for checking for an"
	 					+ " existingNBS_document resultCount-> " + resultCount, e);
	 			throw new NEDSSDAOSysException( e.getMessage(), e);
	 		}
	 		finally
	 		{
	 			closeStatement(preparedStmt);
	 			releaseConnection(dbConnection);
	 
	 		}
	 		try
	 		{
	 			dbConnection = getConnection();
	 			preparedStmt = dbConnection.prepareStatement(INSERT_INTO_NBS_DOCUMENT);
	 			uidGen = new UidGeneratorHelper();
	 			localUID = uidGen.getLocalID(UidClassCodes.DOCUMENT_CLASS_CODE);
	 			logger.debug("New localuid for DOC  is: " + localUID);
	 			int i = 1;
	 			nbsDocumentDT.setNbsDocumentUid(new Long(nbsDocuementUID));
	 			nbsDocumentDT.setLocalId(localUID);
	 			preparedStmt.setLong(i++,nbsDocuementUID);// 1
    			preparedStmt.setString(i++,nbsDocumentDT.getPayLoadTxt());// 2
	 			preparedStmt.setString(i++,nbsDocumentDT.getDocTypeCd());// 3
	 			preparedStmt.setString(i++,nbsDocumentDT.getLocalId());// 4
	 			preparedStmt.setString(i++,nbsDocumentDT.getRecordStatusCd());// 5
	 
	 			if (nbsDocumentDT.getRecordStatusTime()== null)// 6
	 				preparedStmt.setNull(i++, Types.TIMESTAMP);
	 			else
	 				preparedStmt.setTimestamp(i++, nbsDocumentDT.getRecordStatusTime());
	 
	 			if(nbsDocumentDT.getAddUserId()== null)// 7
	 				preparedStmt.setNull(i++, Types.INTEGER);
	 			else
	 				preparedStmt.setLong(i++, (nbsDocumentDT.getAddUserId()).longValue());
	 			
	 			if (nbsDocumentDT.getAddTime()== null)// 8
	 				preparedStmt.setNull(i++, Types.TIMESTAMP);
	 			else
	 				preparedStmt.setTimestamp(i++, nbsDocumentDT.getAddTime());
	 			
	 			preparedStmt.setString(i++,nbsDocumentDT.getProgAreaCd());// 9
	 			preparedStmt.setString(i++,nbsDocumentDT.getJurisdictionCd());// 10
	 			preparedStmt.setString(i++,nbsDocumentDT.getTxt());// 11
	 			
	 			if(nbsDocumentDT.getProgramJurisdictionOid()== null)// 12
	 				preparedStmt.setNull(i++, Types.INTEGER);
	 			else
	 				preparedStmt.setLong(i++, (nbsDocumentDT.getProgramJurisdictionOid()).longValue());
	 			
	 			preparedStmt.setString(i++,nbsDocumentDT.getSharedInd());// 13
	 			preparedStmt.setInt(i++,nbsDocumentDT.getVersionCtrlNbr().intValue());// 14
	 			preparedStmt.setString(i++,nbsDocumentDT.getCd());// 15
	 			
	 			if (nbsDocumentDT.getLastChgTime()== null)// 16
	 				preparedStmt.setNull(i++, Types.TIMESTAMP);
	 			else
	 				preparedStmt.setTimestamp(i++, nbsDocumentDT.getLastChgTime());
	 			
	 			if(nbsDocumentDT.getLastChgUserId()== null)// 17
	 			preparedStmt.setNull(i++, Types.INTEGER);
	 			else
	 				preparedStmt.setLong(i++, (nbsDocumentDT.getLastChgUserId()).longValue());
	 			
	 			preparedStmt.setString(i++,nbsDocumentDT.getDocPurposeCd());// 18
	 			preparedStmt.setString(i++,nbsDocumentDT.getDocStatusCd());// 19
	 			preparedStmt.setString(i++,nbsDocumentDT.getCdDescTxt());// 20
	 			preparedStmt.setString(i++,nbsDocumentDT.getSendingFacilityNm());// 21
	 			preparedStmt.setLong(i++,nbsDocumentDT.getNbsInterfaceUid().longValue());// 22
	 			preparedStmt.setString(i++,nbsDocumentDT.getSendingAppEventId());// 23
	 			preparedStmt.setString(i++,nbsDocumentDT.getSendingAppPatientId());// 24
    			preparedStmt.setString(i++,nbsDocumentDT.getPhdcDocDerivedTxt());// 25
	 			preparedStmt.setString(i++,nbsDocumentDT.getPayloadViewIndCd());// 26
	 			preparedStmt.setLong(i++,nbsDocumentDT.getNbsDocumentMetadataUid().longValue());// 27
	 			preparedStmt.setLong(i++,nbsDocumentDT.getExternalVersionCtrlNbr().longValue());// 28
	 			 
	 			resultCount = preparedStmt.executeUpdate();
	 			logger.debug("done insert a new nbs_document! nbsDocuementUID = " + nbsDocuementUID);
	 			return nbsDocumentDT;
	 		}
	 		catch(SQLException sex)
	 		{
	 			logger.fatal("SQLException while inserting " +
	 					"a new nbsDocument into nbs_document: \n", sex);
	 			sex.printStackTrace();
	 			throw new NEDSSDAOSysException( sex.toString(), sex );
	 		}
	 		catch(Exception ex)
	 		{
	 			logger.fatal("Error while inserting into nbs_document, id = " + nbsDocuementUID, ex);
	 			ex.printStackTrace();
	 			throw new NEDSSSystemException(ex.toString(), ex);
	 		}
	 
	 		finally
	 		{
	 			closeStatement(preparedStmt);
	 			releaseConnection(dbConnection);
	 		}
	 
	 
	 	}
	 	
	 	public NBSDocumentDT insertOracleNBSDocument( NBSDocumentDT nbsDocumentDT){
	 		String INSERT_INTO_NBS_DOCUMENT="INSERT INTO NBS_document(nbs_document_uid,doc_payload,doc_type_cd,local_id,record_status_cd,record_status_time,add_user_id," +
	 		"add_time,prog_area_cd,jurisdiction_cd,txt,program_jurisdiction_oid,shared_ind,version_ctrl_nbr,cd,last_chg_time,last_chg_user_id,doc_purpose_cd,doc_status_cd,cd_desc_txt,sending_facility_nm," +
	 		" nbs_interface_uid, sending_app_event_id, sending_app_patient_id,phdc_doc_derived,payload_view_ind_cd,nbs_document_metadata_uid,external_version_ctrl_nbr) " +
	 		"VALUES(?,EMPTY_BLOB(),?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,EMPTY_BLOB(),?,?,?)";
	 		String INSERT_ACTIVITY = "INSERT INTO Act (act_uid, class_cd, mood_cd) VALUES (?, ?, ?)";
	 		/**
	 		 * Starts inserting a new NBS_DOCUMENT table
	 		 */
	 		long nbsDocumentUID=-1;
	 		Connection dbConnection = null;
	 		PreparedStatement preparedStmt = null;
			ResultSet blobRS = null;
			Statement stmt2 = null;
	 		String localUID = null;
	 		UidGeneratorHelper uidGen = null;
	 		int resultCount = 0;
	 		try
	 		{
	 			/**
	 			 * Inserts into act table for a new NBS_DOCUMENT
	 			 */
	 			uidGen = new UidGeneratorHelper();
	 			nbsDocumentUID  = uidGen.getNbsIDLong(UidClassCodes.NBS_CLASS_CODE).longValue();
	 			logger.debug("New nbs_Docuement_UID is : " + nbsDocumentUID);
	 			nbsDocumentDT.setNbsDocumentUid(nbsDocumentUID);
	 			dbConnection = getConnection();
	 			preparedStmt = dbConnection.prepareStatement(INSERT_ACTIVITY);
	 			int i = 1;
	 			preparedStmt.setLong(i++, nbsDocumentUID);
	 			preparedStmt.setString(i++, NEDSSConstants.ACT_CLASS_CD_FOR_DOC);
	 			preparedStmt.setString(i++, NEDSSConstants.EVENT_MOOD_CODE);
	 			resultCount = preparedStmt.executeUpdate();
	 		}
	 		catch(Exception e)
	 		{
	 			logger.fatal("Exception while getting dbConnection for checking for an"
	 					+ " existingNBS_document -> " + nbsDocumentUID, e);
	 			logger.fatal("Exception while getting dbConnection for checking for an"
	 					+ " existingNBS_document resultCount-> " + resultCount, e);
	 			throw new NEDSSDAOSysException( e.getMessage(), e);
	 		}
	 		finally
	 		{
	 			closeStatement(preparedStmt);
	 			releaseConnection(dbConnection);
	 
	 		}
	 		try
	 		{
	 			dbConnection = getConnection();
	 			preparedStmt = dbConnection.prepareStatement(INSERT_INTO_NBS_DOCUMENT);
	 			uidGen = new UidGeneratorHelper();
	 			localUID = uidGen.getLocalID(UidClassCodes.DOCUMENT_CLASS_CODE);
	 			logger.debug("New localuid for DOC  is: " + localUID);
	 			int i = 1;
	 			nbsDocumentDT.setNbsDocumentUid(new Long(nbsDocumentUID));
	 			nbsDocumentDT.setLocalId(localUID);
	 			preparedStmt.setLong(i++,nbsDocumentUID);// 1
	 			preparedStmt.setString(i++,nbsDocumentDT.getDocTypeCd());// 3
	 			preparedStmt.setString(i++,nbsDocumentDT.getLocalId());// 4
	 			preparedStmt.setString(i++,nbsDocumentDT.getRecordStatusCd());// 5
	 
	 			if (nbsDocumentDT.getRecordStatusTime()== null)// 6
	 				preparedStmt.setNull(i++, Types.TIMESTAMP);
	 			else
	 				preparedStmt.setTimestamp(i++, nbsDocumentDT.getRecordStatusTime());
	 
	 			if(nbsDocumentDT.getAddUserId()== null)// 7
	 				preparedStmt.setNull(i++, Types.INTEGER);
	 			else
	 				preparedStmt.setLong(i++, (nbsDocumentDT.getAddUserId()).longValue());
	 			
	 			if (nbsDocumentDT.getAddTime()== null)// 8
	 				preparedStmt.setNull(i++, Types.TIMESTAMP);
	 			else
	 				preparedStmt.setTimestamp(i++, nbsDocumentDT.getAddTime());
	 			
	 			preparedStmt.setString(i++,nbsDocumentDT.getProgAreaCd());// 9
	 			preparedStmt.setString(i++,nbsDocumentDT.getJurisdictionCd());// 10
	 			preparedStmt.setString(i++,nbsDocumentDT.getTxt());// 11
	 			
	 			if(nbsDocumentDT.getProgramJurisdictionOid()== null)// 12
	 				preparedStmt.setNull(i++, Types.INTEGER);
	 			else
	 				preparedStmt.setLong(i++, (nbsDocumentDT.getProgramJurisdictionOid()).longValue());
	 			
	 			preparedStmt.setString(i++,nbsDocumentDT.getSharedInd());// 13
	 			preparedStmt.setInt(i++,nbsDocumentDT.getVersionCtrlNbr().intValue());// 14
	 			preparedStmt.setString(i++,nbsDocumentDT.getCd());// 15
	 			
	 			if (nbsDocumentDT.getLastChgTime()== null)// 16
	 				preparedStmt.setNull(i++, Types.TIMESTAMP);
	 			else
	 				preparedStmt.setTimestamp(i++, nbsDocumentDT.getLastChgTime());
	 			
	 			if(nbsDocumentDT.getLastChgUserId()== null)// 17
	 			preparedStmt.setNull(i++, Types.INTEGER);
	 			else
	 				preparedStmt.setLong(i++, (nbsDocumentDT.getLastChgUserId()).longValue());
	 			
	 			preparedStmt.setString(i++,nbsDocumentDT.getDocPurposeCd());// 18
	 			preparedStmt.setString(i++,nbsDocumentDT.getDocStatusCd());// 19
	 			preparedStmt.setString(i++,nbsDocumentDT.getCdDescTxt());// 20
	 			preparedStmt.setString(i++,nbsDocumentDT.getSendingFacilityNm());// 21
	 			preparedStmt.setLong(i++,nbsDocumentDT.getNbsInterfaceUid().longValue());// 22
	 			preparedStmt.setString(i++,nbsDocumentDT.getSendingAppEventId());// 23
	 			preparedStmt.setString(i++,nbsDocumentDT.getSendingAppPatientId());// 24
	 			preparedStmt.setString(i++,nbsDocumentDT.getPayloadViewIndCd());// 26
	 			preparedStmt.setLong(i++,nbsDocumentDT.getNbsDocumentMetadataUid().longValue());// 27
	 			preparedStmt.setLong(i++,nbsDocumentDT.getExternalVersionCtrlNbr().longValue());// 28
	 			 
	 			resultCount = preparedStmt.executeUpdate();
				releaseConnection(dbConnection);
				preparedStmt.close();
				
			       dbConnection = getConnection();
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
					stmt2.execute("SELECT doc_payload, phdc_doc_derived FROM NBS_document WHERE nbs_document_uid="+nbsDocumentUID +" FOR UPDATE");
					  blobRS = stmt2.getResultSet();
					  java.sql.Blob blob = null;
					  java.sql.Blob blob2 = null;
				      while(blobRS.next()){
				    	  blob = (java.sql.Blob)(blobRS.getBlob(1));
				    	  blob2 = (java.sql.Blob)(blobRS.getBlob(2));
				      }
					writeBinaryDataToBlob(blob, nbsDocumentDT.getPayLoadTxt());
					if(nbsDocumentDT.getPhdcDocDerivedTxt()!=null)
						writeBinaryDataToBlob(blob2, nbsDocumentDT.getPhdcDocDerivedTxt());
	 			logger.debug("done insert a new nbs_document! nbsDocumentUID = " + nbsDocumentUID);
	 			return nbsDocumentDT;
	 		}
	 		catch(SQLException sex)
	 		{
	 			logger.fatal("SQLException while inserting " +
	 					"a new nbsDocument into nbs_document: \n", sex);
	 			sex.printStackTrace();
	 			throw new NEDSSDAOSysException( sex.toString(), sex );
	 		}
	 		catch(Exception ex)
	 		{
	 			logger.fatal("Error while inserting into nbs_document, id = " + nbsDocumentUID, ex);
	 			ex.printStackTrace();
	 			throw new NEDSSSystemException(ex.toString(), ex);
	 		}
	 
	 		finally
	 		{
            	closeResultSet(blobRS);
            	closeStatement(stmt2);
	 			closeStatement(preparedStmt);
	 			releaseConnection(dbConnection);
	 		}
	 
	 
	 	}

	 	
	 	public NBSDocumentDT insertSQLNBSDocumentHist(NBSDocumentDT nbsDocumentDT){
	 		String INSERT_INTO_NBS_DOCUMENT_HIST="INSERT INTO NBS_DOCUMENT_HIST(nbs_document_uid,doc_payload,doc_type_cd,local_id,record_status_cd,record_status_time,add_user_id," +
	 		"add_time,prog_area_cd,jurisdiction_cd,txt,program_jurisdiction_oid,shared_ind,version_ctrl_nbr,cd,last_chg_time,last_chg_user_id,doc_purpose_cd,doc_status_cd,cd_desc_txt,sending_facility_nm," +
	 		" nbs_interface_uid, sending_app_event_id, sending_app_patient_id,phdc_doc_derived,payload_view_ind_cd,nbs_document_metadata_uid,external_version_ctrl_nbr,processing_decision_cd,processing_decision_txt ) " +
	 		"VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
	 		
	 		/**
	 		 * Starts inserting a new NBS_DOCUMENT table
	 		 */
	 		
	 		Connection dbConnection = null;
	 		PreparedStatement preparedStmt = null;
	 		int i = 1;
	 		int resultCount=0;
	 		try
	 		{
	 			dbConnection = getConnection();
	 			preparedStmt = dbConnection.prepareStatement(INSERT_INTO_NBS_DOCUMENT_HIST);

	 			preparedStmt.setLong(i++,nbsDocumentDT.getNbsDocumentUid().longValue());// 1
 			    preparedStmt.setString(i++,nbsDocumentDT.getPayLoadTxt());// 2
	 			preparedStmt.setString(i++,nbsDocumentDT.getDocTypeCd());// 3
	 			preparedStmt.setString(i++,nbsDocumentDT.getLocalId());// 4
	 			preparedStmt.setString(i++,nbsDocumentDT.getRecordStatusCd());// 5
	 
	 			if (nbsDocumentDT.getRecordStatusTime()== null)// 6
	 				preparedStmt.setNull(i++, Types.TIMESTAMP);
	 			else
	 				preparedStmt.setTimestamp(i++, nbsDocumentDT.getRecordStatusTime());
	 
	 			if(nbsDocumentDT.getAddUserId()== null)// 7
	 				preparedStmt.setNull(i++, Types.INTEGER);
	 			else
	 				preparedStmt.setLong(i++, (nbsDocumentDT.getAddUserId()).longValue());
	 			
	 			if (nbsDocumentDT.getAddTime()== null)// 8
	 				preparedStmt.setNull(i++, Types.TIMESTAMP);
	 			else
	 				preparedStmt.setTimestamp(i++, nbsDocumentDT.getAddTime());
	 			
	 			preparedStmt.setString(i++,nbsDocumentDT.getProgAreaCd());// 9
	 			preparedStmt.setString(i++,nbsDocumentDT.getJurisdictionCd());// 10
	 			preparedStmt.setString(i++,nbsDocumentDT.getTxt());// 11
	 			
	 			if(nbsDocumentDT.getProgramJurisdictionOid()== null)// 12
	 				preparedStmt.setNull(i++, Types.INTEGER);
	 			else
	 				preparedStmt.setLong(i++, (nbsDocumentDT.getProgramJurisdictionOid()).longValue());
	 			
	 			preparedStmt.setString(i++,nbsDocumentDT.getSharedInd());// 13
	 			preparedStmt.setInt(i++,nbsDocumentDT.getVersionCtrlNbr().intValue());// 14
	 			preparedStmt.setString(i++,nbsDocumentDT.getCd());// 15
	 			
	 			if (nbsDocumentDT.getLastChgTime()== null)// 16
	 				preparedStmt.setNull(i++, Types.TIMESTAMP);
	 			else
	 				preparedStmt.setTimestamp(i++, nbsDocumentDT.getLastChgTime());
	 			
	 			if(nbsDocumentDT.getLastChgUserId()== null)// 17
	 			preparedStmt.setNull(i++, Types.INTEGER);
	 			else
	 				preparedStmt.setLong(i++, (nbsDocumentDT.getLastChgUserId()).longValue());
	 			
	 			preparedStmt.setString(i++,nbsDocumentDT.getDocPurposeCd());// 18
	 			preparedStmt.setString(i++,nbsDocumentDT.getDocStatusCd());// 19
	 			preparedStmt.setString(i++,nbsDocumentDT.getCdDescTxt());// 20
	 			preparedStmt.setString(i++,nbsDocumentDT.getSendingFacilityNm());// 21
	 			preparedStmt.setLong(i++,nbsDocumentDT.getNbsInterfaceUid().longValue());// 22
	 			preparedStmt.setString(i++,nbsDocumentDT.getSendingAppEventId());// 23
	 			preparedStmt.setString(i++,nbsDocumentDT.getSendingAppPatientId());// 24
	 			preparedStmt.setString(i++,nbsDocumentDT.getPhdcDocDerivedTxt());// 25
	 			preparedStmt.setString(i++,nbsDocumentDT.getPayloadViewIndCd());// 26
	 			preparedStmt.setLong(i++,nbsDocumentDT.getNbsDocumentMetadataUid().longValue());// 27
	 			preparedStmt.setLong(i++,nbsDocumentDT.getExternalVersionCtrlNbr().longValue());// 28
	            preparedStmt.setString(i++,nbsDocumentDT.getProcessingDecisionCd());// 29
	            preparedStmt.setString(i++,nbsDocumentDT.getProcessingDecisiontxt());// 30
	 			
	 			resultCount = preparedStmt.executeUpdate();
	 			logger.debug("done insert a new nbs_document! nbsDocuementUID = " + nbsDocumentDT.getNbsDocumentUid());
	 			logger.debug("done insert a new nbs_document! resultCount = " + resultCount);
	 			return nbsDocumentDT;
	 		}
	 		catch(SQLException sex)
	 		{
	 			logger.fatal("SQLException while inserting " +
	 					"a new nbsDocument into nbs_document: \n", sex);
	 			sex.printStackTrace();
	 			throw new NEDSSDAOSysException( sex.toString(), sex );
	 		}
	 		catch(Exception ex)
	 		{
	 			logger.fatal("Error while inserting into nbs_document, id = " + nbsDocumentDT.getNbsDocumentUid(), ex);
	 			ex.printStackTrace();
	 			throw new NEDSSSystemException(ex.toString(), ex);
	 		}
	 
	 		finally
	 		{
	 			closeStatement(preparedStmt);
	 			releaseConnection(dbConnection);
	 		}

	 	
		 
	 	}
	 	public NBSDocumentDT insertOracleNBSDocumentHist(NBSDocumentDT nbsDocumentDT){
	 		String INSERT_INTO_NBS_DOCUMENT_HIST="INSERT INTO NBS_DOCUMENT_HIST(nbs_document_uid,doc_payload,doc_type_cd,local_id,record_status_cd,record_status_time,add_user_id," +
	 		"add_time,prog_area_cd,jurisdiction_cd,txt,program_jurisdiction_oid,shared_ind,version_ctrl_nbr,cd,last_chg_time,last_chg_user_id,doc_purpose_cd,doc_status_cd,cd_desc_txt,sending_facility_nm," +
	 		" nbs_interface_uid, sending_app_event_id, sending_app_patient_id,phdc_doc_derived,payload_view_ind_cd,nbs_document_metadata_uid,external_version_ctrl_nbr,processing_decision_cd,processing_decision_txt) " +
	 		"VALUES(?,EMPTY_BLOB(),?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,EMPTY_BLOB(),?,?,?,?,?)";
	 		
	 		/**
	 		 * Starts inserting a new NBS_DOCUMENT table
	 		 */
	 		
	 		Connection dbConnection = null;
	 		PreparedStatement preparedStmt = null;
			ResultSet blobRS = null;
			Statement stmt2 = null;
	 		int i = 1;
	 		int resultCount=0;
	 		try
	 		{
	 			dbConnection = getConnection();
	 			preparedStmt = dbConnection.prepareStatement(INSERT_INTO_NBS_DOCUMENT_HIST);

	 			preparedStmt.setLong(i++,nbsDocumentDT.getNbsDocumentUid().longValue());// 1
	 			preparedStmt.setString(i++,nbsDocumentDT.getDocTypeCd());// 3
	 			preparedStmt.setString(i++,nbsDocumentDT.getLocalId());// 4
	 			preparedStmt.setString(i++,nbsDocumentDT.getRecordStatusCd());// 5
	 
	 			if (nbsDocumentDT.getRecordStatusTime()== null)// 6
	 				preparedStmt.setNull(i++, Types.TIMESTAMP);
	 			else
	 				preparedStmt.setTimestamp(i++, nbsDocumentDT.getRecordStatusTime());
	 
	 			if(nbsDocumentDT.getAddUserId()== null)// 7
	 				preparedStmt.setNull(i++, Types.INTEGER);
	 			else
	 				preparedStmt.setLong(i++, (nbsDocumentDT.getAddUserId()).longValue());
	 			
	 			if (nbsDocumentDT.getAddTime()== null)// 8
	 				preparedStmt.setNull(i++, Types.TIMESTAMP);
	 			else
	 				preparedStmt.setTimestamp(i++, nbsDocumentDT.getAddTime());
	 			
	 			preparedStmt.setString(i++,nbsDocumentDT.getProgAreaCd());// 9
	 			preparedStmt.setString(i++,nbsDocumentDT.getJurisdictionCd());// 10
	 			preparedStmt.setString(i++,nbsDocumentDT.getTxt());// 11
	 			
	 			if(nbsDocumentDT.getProgramJurisdictionOid()== null)// 12
	 				preparedStmt.setNull(i++, Types.INTEGER);
	 			else
	 				preparedStmt.setLong(i++, (nbsDocumentDT.getProgramJurisdictionOid()).longValue());
	 			
	 			preparedStmt.setString(i++,nbsDocumentDT.getSharedInd());// 13
	 			preparedStmt.setInt(i++,nbsDocumentDT.getVersionCtrlNbr().intValue());// 14
	 			preparedStmt.setString(i++,nbsDocumentDT.getCd());// 15
	 			
	 			if (nbsDocumentDT.getLastChgTime()== null)// 16
	 				preparedStmt.setNull(i++, Types.TIMESTAMP);
	 			else
	 				preparedStmt.setTimestamp(i++, nbsDocumentDT.getLastChgTime());
	 			
	 			if(nbsDocumentDT.getLastChgUserId()== null)// 17
	 			preparedStmt.setNull(i++, Types.INTEGER);
	 			else
	 				preparedStmt.setLong(i++, (nbsDocumentDT.getLastChgUserId()).longValue());
	 			
	 			preparedStmt.setString(i++,nbsDocumentDT.getDocPurposeCd());// 18
	 			preparedStmt.setString(i++,nbsDocumentDT.getDocStatusCd());// 19
	 			preparedStmt.setString(i++,nbsDocumentDT.getCdDescTxt());// 20
	 			preparedStmt.setString(i++,nbsDocumentDT.getSendingFacilityNm());// 21
	 			preparedStmt.setLong(i++,nbsDocumentDT.getNbsInterfaceUid().longValue());// 22
	 			preparedStmt.setString(i++,nbsDocumentDT.getSendingAppEventId());// 23
	 			preparedStmt.setString(i++,nbsDocumentDT.getSendingAppPatientId());// 24
	 			preparedStmt.setString(i++,nbsDocumentDT.getPayloadViewIndCd());// 26
	 			preparedStmt.setLong(i++,nbsDocumentDT.getNbsDocumentMetadataUid().longValue());// 27
	 			preparedStmt.setLong(i++,nbsDocumentDT.getExternalVersionCtrlNbr().longValue());// 28
	            preparedStmt.setString(i++,nbsDocumentDT.getProcessingDecisionCd());// 29
	            preparedStmt.setString(i++,nbsDocumentDT.getProcessingDecisiontxt());// 29
	            
	 			resultCount = preparedStmt.executeUpdate();
	 			logger.debug("insertOracleNBSDocumentHist.resultCount is "+ resultCount);
				releaseConnection(dbConnection);
				preparedStmt.close();
				
			       dbConnection = getConnection();
			        boolean autoCommit = dbConnection.getAutoCommit();
					if (autoCommit == true) {
						dbConnection.setAutoCommit(false);
					}
					stmt2 = dbConnection.createStatement();
					stmt2.execute("SELECT doc_payload, phdc_doc_derived FROM NBS_document_hist WHERE nbs_document_hist_uid in(select max(nbs_document_hist_uid) from nbs_document_hist where nbs_document_uid="+nbsDocumentDT.getNbsDocumentUid().longValue()+")  FOR UPDATE");
					  blobRS = stmt2.getResultSet();
					  java.sql.Blob blob = null;
					  java.sql.Blob blob2 = null;
				      while(blobRS.next()){
				    	  blob = (java.sql.Blob)(blobRS.getBlob(1));
				    	  if(blobRS.getBlob(2) != null){
				    		  blob2 = (java.sql.Blob)(blobRS.getBlob(2));
				    	  }
				      }
					writeBinaryDataToBlob(blob, nbsDocumentDT.getPayLoadTxt());
					if(nbsDocumentDT.getPhdcDocDerivedTxt() != null ){
						writeBinaryDataToBlob(blob2, nbsDocumentDT.getPhdcDocDerivedTxt());
					}


	 			logger.debug("done insert a new nbs_document! nbsDocuementUID = " + nbsDocumentDT.getNbsDocumentUid());
	 			return nbsDocumentDT;
	 		}
	 		catch(SQLException sex)
	 		{
	 			logger.fatal("SQLException while inserting " +
	 					"a new nbsDocument into nbs_document: \n", sex);
	 			sex.printStackTrace();
	 			throw new NEDSSDAOSysException( sex.toString(), sex );
	 		}
	 		catch(Exception ex)
	 		{
	 			logger.fatal("Error while inserting into nbs_document, id = " + nbsDocumentDT.getNbsDocumentUid(), ex);
	 			ex.printStackTrace();
	 			throw new NEDSSSystemException(ex.toString(), ex);
	 		}
	 
	 		finally
	 		{
            	closeResultSet(blobRS);
            	closeStatement(stmt2);
	 			closeStatement(preparedStmt);
	 			releaseConnection(dbConnection);
	 		}

	 	
		 
	 	}
	 	
	  public Object loadObject(long nbsDocUid) throws NEDSSSystemException,
	      NEDSSSystemException {
		  try{
		    NBSDocumentDT nBSDocumentDT = selectNbsDocument(nbsDocUid);
		    nBSDocumentDT.setItNew(false);
		    nBSDocumentDT.setItDirty(false);
		    return nBSDocumentDT;
		  }catch(Exception ex){
			  logger.fatal("Exception  = "+ex.getMessage(), ex);
			  throw new NEDSSSystemException(ex.toString());
		  }
	  }
	 	 
	  private NBSDocumentDT selectNbsDocument(long nbsDocUid){
		  String GET_NBS_DOCUMENT_SQL = " SELECT"
				+ " nbsdoc.nbs_document_uid  \"nbsDocumentUid\", "
				+ " nbsdoc.local_id  \"localId\"," 
				+ " nbsdoc.doc_type_cd  \"docTypeCd\","
				+ " nbsdoc.jurisdiction_cd \"jurisdictionCd\","
				+ " nbsdoc.prog_area_cd  \"progAreaCd\","
				+ " nbsdoc.doc_status_cd \"docStatusCd\", "
				+ " nbsdoc.add_time \"addTime\", "
				+ " nbsdoc.txt \"txt\", "
				+ " nbsdoc.version_ctrl_nbr \"versionCtrlNbr\", "
				+ " nbsdoc.external_version_ctrl_nbr \"externalVersionCtrlNbr\", "
				+ " nbsdoc.doc_purpose_cd \"docPurposeCd\", "
				+ " nbsdoc.cd_desc_txt \"cdDescTxt\", "
				+ " nbsdoc.sending_facility_nm \"sendingFacilityNm\", "	
				+ " nbsdoc.add_user_id \"addUserId\", "
				+ " nbsdoc.record_status_cd \"recordStatusCd\", "
				+ " nbsdoc.cd \"cd\", "
				+ " nbsdoc.doc_payload \"docPayload\", "
				+ " nbsdoc.phdc_doc_derived \"phdcDocDerived\", "
  			    + " nbsdoc.payload_view_ind_cd \"payloadViewIndCd\", "
				+ " nbsdoc.record_status_Time \"recordStatusTime\", "
				+ " nbsdoc.program_jurisdiction_oid \"programJurisdictionOid\", "
				+ " nbsdoc.shared_ind \"sharedInd\", "
				+ " nbsdoc.last_chg_time \"lastChgTime\", "
				+ " nbsdoc.last_chg_user_id \"lastChgUserId\", "
				+ " nbsdoc.nbs_interface_uid \"nbsInterfaceUid\", "
				+ " nbsdoc.nbs_document_metadata_uid \"nbsDocumentMetadataUid\" "
				+ " FROM nbs_document nbsdoc " 
				+ " WHERE  nbsdoc.nbs_document_uid =?";
			
		    Connection dbConnection = null;
			PreparedStatement preparedStmt = null;
			ResultSet resultSet = null;
		    ResultSetMetaData resultSetMetaData = null;
	        ResultSetUtils resultSetUtils = new ResultSetUtils();
		    int i = 1;
		    
		    NBSDocumentDT nbsDocumentDT = new NBSDocumentDT();
		    try{

			    dbConnection = getConnection();

		    String docQuery =   GET_NBS_DOCUMENT_SQL;

	           	preparedStmt = dbConnection.prepareStatement(docQuery);
    		preparedStmt.setLong(i++, nbsDocUid);
	    		resultSet = preparedStmt.executeQuery();
	    		resultSetMetaData = resultSet.getMetaData();
	    		nbsDocumentDT = (NBSDocumentDT)resultSetUtils.mapRsToBean(resultSet, resultSetMetaData, nbsDocumentDT.getClass());
    		logger.debug("returned Document Information for nbsDocumentUid = "+nbsDocumentDT.getNbsDocumentUid());
		    }catch(Exception e){
		  	  logger.fatal("Error in fetching DocumentDT  by DocumentUID "+e.getMessage(), e) ; 
		      throw new NEDSSSystemException(e.toString(), e);
		    }
		    finally
	 		{
	 			closeResultSet(resultSet);
	 			closeStatement(preparedStmt);
	 			releaseConnection(dbConnection);

	 		}

		  return nbsDocumentDT;
	  }
	  
	  public Collection<Object> getDocSummaryVOColl(Long personUID, NBSSecurityObj nbsSecurityObj)
	  {
		  Connection dbConnection = null;
		  PreparedStatement preparedStmt = null;
		  ResultSet resultSet = null;
		  ResultSetMetaData resultSetMetaData = null;
		  ResultSetUtils resultSetUtils = new ResultSetUtils();
		  ArrayList<Object>  docSummaryList = new ArrayList<Object> ();
		  //int docCountFix = propertyUtil.getDocCount();
		  String docQuery = "";
		  String docDataAccessWhereClause=null;
		  
		  // This will be "VIEW" for Document
		  String docsNeedingReviewSecurity = "VIEW";
		  if(nbsSecurityObj.getPermission(NBSBOLookup.DOCUMENT,docsNeedingReviewSecurity))
		  {
		        docDataAccessWhereClause = nbsSecurityObj.
		        getDataAccessWhereClause(NBSBOLookup.DOCUMENT,
		        		docsNeedingReviewSecurity);
		        
		        docDataAccessWhereClause = docDataAccessWhereClause != null ? " AND " + docDataAccessWhereClause : "";			        
		  
		  try{

		  /*  if (propertyUtil.getDatabaseServerType() != null &&
			      propertyUtil.
			      getDatabaseServerType().equalsIgnoreCase(NEDSSConstants.ORACLE_ID)){
*/
		    	  docQuery = " SELECT"
		    		  + " nbsdoc.nbs_document_uid  \"nbsDocumentUid\", "
		    		  + " nbsdoc.shared_ind \"sharedInd\", "
					  + " nbsdoc.local_id  \"localId\"," 
					  + " nbsdoc.doc_type_cd  \"docType\","
		  			  + " nbsdoc.payload_view_ind_cd \"payloadViewIndCd\", "
		  			  + " nbsdoc.external_version_ctrl_nbr \"externalVersionCtrlNbr\", " 
					  + " person.person_parent_uid \"MPRUid\","
					  + " person.local_id \"personLocalId\","
					  + " nbsdoc.jurisdiction_cd \"jurisdiction\","
					  + " nbsdoc.prog_area_cd  \"programArea\","
					  + " nbsdoc.doc_status_cd \"docStatusCd\", "
					  + " nbsdoc.add_time \"addTime\", "
					  + " nbsdoc.txt \"txt\", "
					  + " nbsdoc.doc_purpose_cd \"docPurposeCd\","
					  + " nbsdoc.record_status_cd \"recordStatusCd\","
					  + " nbsdoc.add_time \"addTime\", "
					  + " nbsdoc.sending_facility_nm \"sendingFacilityNm\", "
					  + " nbsdoc.processing_decision_cd \"processingDecisionCd\", "
					  + " nbsdoc.cd \"cd\", "
					  + " ndm.nbs_document_metadata_uid \"nbsDocumentMetadataUid\", "
					  + " ndm.doc_type_cd \"docTypeCd\", "
					  + " nbsdoc.cd_desc_txt \"cdDescTxt\","
					  + " eep.doc_event_type_cd \"docEventTypeCd\" "
					  + " FROM nbs_document nbsdoc inner join "
					  + " nbs_document_metadata ndm on "
					  + " nbsdoc.nbs_document_metadata_uid = ndm.nbs_document_metadata_uid  inner join "
					  + " participation participation on "
					  + " nbsdoc.nbs_document_uid=participation.act_uid " 
					  + " AND participation.type_cd='"+NEDSSConstants.SUBJECT_OF_DOC+"' " 
					  + " AND Participation.act_class_cd = 'DOC'" 
					  + " AND Participation.subject_class_cd = 'PSN' " 
					  + " AND Participation.record_status_cd = 'ACTIVE' " 
					  + " inner join person person on "
					  + " participation.subject_entity_uid=person.person_uid "
					  + " left outer join edx_event_process eep on "
					  + " eep.nbs_document_uid = nbsdoc.nbs_document_uid "
					  + " and eep.doc_event_type_cd in('CASE','LabReport','MorbReport','CT') "
					  + " and eep.parsed_ind = 'N'"
					  + " WHERE   person.person_parent_uid = ? and nbsdoc.record_status_cd != 'LOG_DEL' "  
					  + docDataAccessWhereClause ;	
		    dbConnection = getConnection();	 
			preparedStmt = dbConnection.prepareStatement(docQuery);
			preparedStmt.setLong(1, personUID.longValue());			  
		    long timebegin = 0; long timeend = 0;
		    timebegin=System.currentTimeMillis();
		    logger.debug("\n timebegin  for getDocSummaryListforReview " + timebegin);
		    logger.debug("\n docQuery  for Security = "+docQuery);
		    resultSet = preparedStmt.executeQuery();		    
		    resultSetMetaData = resultSet.getMetaData();
		    
		    docSummaryList = (ArrayList<Object> ) resultSetUtils.mapRsToBeanList(resultSet,
		    			resultSetMetaData, SummaryDT.class, docSummaryList);
		    timeend = System.currentTimeMillis();
		    
		    logger.debug("\n timeend for getDocSummaryVOColl " + timeend);

		  }			 
		  catch(Exception e)
		  {
			  logger.fatal("Error in fetching Document Summary List<Object> for ViewFile "+e.getMessage(), e); 
		      throw new NEDSSSystemException(e.toString(), e);
		  }
		  finally
	 		{
	 			closeResultSet(resultSet);
	 			closeStatement(preparedStmt);
	 			releaseConnection(dbConnection);

	 		}
	  }

		  return docSummaryList;

	  }
	  
		@SuppressWarnings("deprecation")
		private void writeBinaryDataToBlob(java.sql.Blob messageBLOB, String blobData) throws Exception {
			try{
				messageBLOB.setBytes(1, blobData.getBytes());
			}catch(Exception ex){
				logger.fatal("Exception  = "+ex.getMessage(), ex);
				throw new Exception(ex.toString());
			}
		}
		
	 public boolean getInvestigationAssoWithDocumentColl(Long nbsDocumentUid)
	 {
		int resultCount = 0;
		String sqlQuery = null;
		Connection dbConnection = null;
		PreparedStatement preparedStmt = null;
		ResultSet result = null;
		try{
			
			sqlQuery = "select count(*) from Public_health_case Public_health_case, act_relationship ar " + 
					" where Public_health_case.Public_health_case_uid = ar.target_act_uid and "+
					" ar.source_act_uid=? and ar.type_cd='DocToPHC' ";
			
			logger.debug("The Query for count is:" + sqlQuery);
			dbConnection = getConnection();	 
	        preparedStmt = dbConnection.prepareStatement(sqlQuery);

	        preparedStmt.setLong(1, nbsDocumentUid.longValue());
	        result = preparedStmt.executeQuery();
	        if (result.next())
	        {
	          resultCount = result.getInt(1);
	          logger.debug("Count for this Query is :" + resultCount);
	        }
	        if (resultCount > 0)
	        {
	          logger.debug("The returned value is false");
	          return true;
	        }
	        else
	          return false;
		}
	    
        catch (Exception e)
        {
          logger.error("Error in getInvestigationAssoWithDocumentColl " + e.getMessage(), e);
          return false;
        }
        finally
        {
          closeResultSet(result);
          closeStatement(preparedStmt);
          releaseConnection(dbConnection);
        }
		 
	 }

	 public static final String TRASFER_OWNERSHIP_NBS_DOCUMENT="UPDATE "
			+ " NBS_Document "
			+ " last_chg_time=?, "
			+ " last_chg_user_id=?, " 
			+ " prog_area_cd=?, " 	
			+ " jurisdiction_cd=?, " 
			+ " program_jurisdiction_oid=?, "
			+ " version_ctrl_nbr =?, " 
			+ " WHERE nbs_document_uid=? ";
	
	@SuppressWarnings("unchecked")
	public Collection<Object> getUnprocessesNBSDcoumentDTColl(){
		String SELECT_QUEUED_CASES ="SELECT nbs_document_uid \"nbsDocumentUid\", doc_payload \"xmldocPayload\" "
      +" ,doc_type_cd \"docTypeCd\",local_id \"localId\" "
      +" ,record_status_cd \"recordStatusCd\",record_status_time \"recordStatusTime\" "
      +" ,add_user_id \"addUserId\",add_time \"addTime\" "
      +" ,prog_area_cd \"progAreaCd\",jurisdiction_cd \"jurisdictionCd\" "
      +" ,txt \"txt\" ,program_jurisdiction_oid \"programJurisdictionOid\" "
      +" ,shared_ind \"sharedInd\",version_ctrl_nbr \"versionCtrlNbr\" "
      + ",external_version_ctrl_nbr \"externalVersionCtrlNbr\" "
      +" ,cd \"cd\",last_chg_time \"lastChgTime\" "
      +" ,last_chg_user_id \"lastChgUserId\",doc_purpose_cd \"docPurposeCd\" "
      +" ,doc_status_cd \"docStatusCd\",cd_desc_txt \"cdDescTxt\" "
      +" ,sending_facility_nm \"sendingFacilityNm\",nbs_interface_uid \"nbsInterfaceUid\" "
      +" ,sending_app_event_id \"sendingAppEventId\",sending_app_patient_id \"sendingAppPatientId\" "
      +" ,phdc_doc_derived \"phdcDocDerived\",payload_view_ind_cd \"payloadViewIndCd\" "
      +" ,nbs_document_metadata_uid \"nbsDocumentMetadataUid\" FROM NBS_document where record_status_cd='UNPROCESSED'";
		
		NBSDocumentDT nbsInterfaceDT  = new NBSDocumentDT();
		ArrayList<Object>  nbsDocumentDTCollection = new ArrayList<Object> ();
		try
		{
			nbsDocumentDTCollection = (ArrayList<Object> )preparedStmtMethod(nbsInterfaceDT, nbsDocumentDTCollection, SELECT_QUEUED_CASES, NEDSSConstants.SELECT);
		}
		catch (NEDSSDAOSysException ex) {
			logger.fatal("NbsInterfaceDAOImpl.insertNBSInterface:  Exception in get queuedCollection:  ERROR = " + ex.getMessage(), ex);
			throw new NEDSSSystemException(ex.toString(), ex);
		}
		return nbsDocumentDTCollection;
	}

	public NBSDocumentDT getLastDocument(NBSDocumentVO nBSDocumentVO ){
		int resultCount = 0;
		//String sqlQuery = null;
		//Connection dbConnection = null;
		//PreparedStatement preparedStmt = null;
		//ResultSet result = null;
		//nBSDocumentVO.getNbsDocumentDT().getSendingFacilityNm();
		//nBSDocumentVO.getNbsDocumentDT().getSendingAppEventId();
		NBSDocumentDT nBSDocumentDT =  new NBSDocumentDT();
		try{
			ArrayList<Object> inArrayList= new ArrayList<Object> ();
	        ArrayList<Object>  outArrayList= new ArrayList<Object> ();
	        ArrayList<Object> arrayList = new ArrayList<Object> ();
	        inArrayList.add(nBSDocumentVO.getPatientVO().getThePersonDT().getPersonParentUid());
	        inArrayList.add(nBSDocumentVO.getNbsDocumentDT().getSendingFacilityNm());
	        inArrayList.add(nBSDocumentVO.getNbsDocumentDT().getSendingAppEventId());
	        outArrayList.add(java.sql.Types.INTEGER);
	        outArrayList.add(java.sql.Types.VARCHAR);
			String sQuery  = "{call GetVerCtrlNbr_SP(?,?,?,?,?)}";
			arrayList = (ArrayList<Object> )callStoredProcedureMethod(sQuery,inArrayList,outArrayList);
			if(arrayList != null && arrayList.size() > 0)
			{
				if (arrayList.get(0) != null) {
					nBSDocumentDT.setExternalVersionCtrlNbr((Integer)(arrayList.get(0)));
					nBSDocumentDT.setLocalId((String) arrayList.get(1));
				}
			}
			
			/* Code replaced with a Stored Procedure for for PHDC Import query optimization
			sqlQuery = "Select external_version_ctrl_nbr,local_id from NBS_document "+
			 " where NBS_document_uid in ( "+
			  " select  MAX(NBS_document.NBS_document_uid) from NBS_document, Participation where  "+
			 						" NBS_document.NBS_document_uid =Participation.act_uid "+
			 						" and Participation.type_cd='SubjOfDoc' and Participation.act_class_cd='DOC'  "+
			 						" and Participation.subject_entity_uid in  "+
			 						" (select person_uid from Person where person_parent_uid =?)  "+
			 						" and NBS_document.record_status_cd in ('PROCESSED', 'UNPROCESSED')  "+
			 						" and NBS_document.sending_facility_nm =?" +
			 						" and sending_app_event_id=?) ";
			
			logger.debug("The Query for count is:" + sqlQuery);
			dbConnection = getConnection();	 
	        preparedStmt = dbConnection.prepareStatement(sqlQuery);

	        preparedStmt.setLong(1, nBSDocumentVO.getPatientVO().getThePersonDT().getPersonParentUid());
	        preparedStmt.setString(2, nBSDocumentVO.getNbsDocumentDT().getSendingFacilityNm());
	   	        preparedStmt.setString(3, nBSDocumentVO.getNbsDocumentDT().getSendingAppEventId());
	        result = preparedStmt.executeQuery();
	        if (result.next())
	        {
	          resultCount = result.getInt(1);
	          nBSDocumentDT.setExternalVersionCtrlNbr(resultCount);
	          nBSDocumentDT.setLocalId(result.getString(2)); 
	          logger.debug("Count for the Query is :" + resultCount);
	        }
	        */
	        return nBSDocumentDT;
		}
	    
        catch (Exception e)
        {
          logger.error("Error in getInvestigationAssoWithDocumentColl " + e.getMessage(), e);
          return nBSDocumentDT;
        }
        finally
        {
        //  closeResultSet(result);
         // closeStatement(preparedStmt);
         // releaseConnection(dbConnection);
        }
	}
	public int getNumberOfCasesAssociated(Long mprUid, String conditionCode){
		
		int resultCount = 0;
	//	String sqlQuery = null;
	//	Connection dbConnection = null;
	//	PreparedStatement preparedStmt = null;
	//	ResultSet result = null;
		ArrayList<Object> inArrayList= new ArrayList<Object> ();
        ArrayList<Object>  outArrayList= new ArrayList<Object> ();
        ArrayList<Object> arrayList = new ArrayList<Object> ();
        String resultCountString=null;
		try{
	        inArrayList.add(mprUid.longValue());
	        inArrayList.add(conditionCode);
	        outArrayList.add(new Integer(java.sql.Types.SMALLINT));
			String sQuery  = "{call GetPersonCaseCount_SP(?,?,?)}";
			arrayList = (ArrayList<Object> )callStoredProcedureMethod(sQuery,inArrayList,outArrayList);
			if(arrayList != null)
			{		
				for(Iterator<Object> anIterator = arrayList.iterator(); anIterator.hasNext(); )
	            {
					resultCountString = anIterator.next().toString();
					resultCount=Integer.parseInt(resultCountString);
	            }

			}
			
			/* Code replaced with a Stored Procedure for for PHDC Import query optimization
			sqlQuery = "select count(*) from Public_health_case, Participation where "+
						" Public_health_case.public_health_case_uid =Participation.act_uid"+
						" and Participation.type_cd='SubjOfPHC' and Participation.act_class_cd='CASE'"+
						" and Participation.subject_entity_uid in ( "+
						" select person_uid from Person where person_parent_uid =?) "+
						" and Public_health_case.cd=? and Public_health_case.record_status_cd in ('ACTIVE', 'OPEN')";
			
			
			logger.debug("The Query for count is:" + sqlQuery);
			dbConnection = getConnection();	 
	        preparedStmt = dbConnection.prepareStatement(sqlQuery);

	        preparedStmt.setLong(1, mprUid.longValue());
	        preparedStmt.setString(2, conditionCode);
	        result = preparedStmt.executeQuery();
	        if (result.next())
	        {
	          resultCount = result.getInt(1);
	          logger.debug("Count for this Query is :" + resultCount);
	        }
	        */
	          return resultCount;
		}
	    
        catch (Exception e)
        {
          logger.error("Error in getInvestigationAssoWithDocumentColl " + e.getMessage(), e);
        }
        finally
        {
        //  closeResultSet(result);
        //  closeStatement(preparedStmt);
        //  releaseConnection(dbConnection);
        }
		return resultCount;
	}
	
	public void insertEventProcessDTs(EDXEventProcessDT processDT) {

		
		
		String INSERT_INTO_EDX_EVENT_PROCESS = "insert into EDX_EVENT_PROCESS(nbs_document_uid, nbs_event_uid, source_event_id, doc_event_type_cd, add_user_id, add_time, parsed_ind) values(?,?,?,?,?,?,?)";
		String INSERT_ACTIVITY = "INSERT INTO Act (act_uid, class_cd, mood_cd) VALUES (?, ?, ?)";
		long eventUid = -1;
		Connection dbConnection = null;
		PreparedStatement preparedStmt = null;
		UidGeneratorHelper uidGen = null;
		try {
			if (processDT.getDocEventTypeCd() != null
					&& !processDT.getDocEventTypeCd().equals(
							NEDSSConstants.CASE) && !processDT.getDocEventTypeCd().equals(NEDSSConstants.CLASS_CD_CONTACT)) {
				EDXEventProcessDT existingProcessDT = getEDXEventProcessDTBySourceIdandEventType(processDT
						.getSourceEventId(), processDT.getDocEventTypeCd());
				if (existingProcessDT != null && existingProcessDT.getNbsEventUid()!=null) {
					processDT
							.setNbsEventUid(existingProcessDT.getNbsEventUid());
				}
			}
			/**
			 * Inserts into act table for a new Events in NBS_DOCUMENT
			 */
			if (processDT.getNbsEventUid() == null) {
				uidGen = new UidGeneratorHelper();
				eventUid = uidGen.getNbsIDLong(UidClassCodes.NBS_CLASS_CODE)
						.longValue();
				logger.debug("New Act_UID is : " + eventUid);
				dbConnection = getConnection();
				preparedStmt = dbConnection.prepareStatement(INSERT_ACTIVITY);
				int i = 1;
				preparedStmt.setLong(i++, eventUid);
				preparedStmt.setString(i++, processDT.getDocEventTypeCd());
				preparedStmt.setString(i++, NEDSSConstants.EVENT_MOOD_CODE);
				
				preparedStmt.executeUpdate();
			}
			else
				eventUid = processDT.getNbsEventUid().longValue();
		} catch (Exception e) {
			logger.fatal(
					"Exception while getting dbConnection for Inserting new Act for a"
							+ " document -> " + eventUid, e);
			throw new NEDSSDAOSysException(e.getMessage(), e);
		}finally {
			closeStatement(preparedStmt);
			releaseConnection(dbConnection);
		}
		try {
			dbConnection = getConnection();
			preparedStmt = dbConnection
					.prepareStatement(INSERT_INTO_EDX_EVENT_PROCESS);
			int i = 1;
			preparedStmt.setLong(i++, processDT.getNbsDocumentUid());// 1
			preparedStmt.setLong(i++, eventUid);// 2
			preparedStmt.setString(i++, processDT.getSourceEventId());// 3
			preparedStmt.setString(i++, processDT.getDocEventTypeCd());// 4
			if (processDT.getAddUserId() == null)// 7
				preparedStmt.setNull(i++, Types.INTEGER);
			else
				preparedStmt.setLong(i++, processDT.getAddUserId());// 5
			if (processDT.getAddTime() == null)// 6
				preparedStmt.setNull(i++, Types.TIMESTAMP);
			else
				preparedStmt.setTimestamp(i++, processDT.getAddTime());// 6
			preparedStmt.setString(i++, processDT.getParsedInd());// 7
			 preparedStmt.executeUpdate();
		} catch (SQLException sex) {
			logger.fatal("SQLException while inserting "
					+ "a new event into EDX_EVENT_PROCESS: \n", sex);
			sex.printStackTrace();
			throw new NEDSSDAOSysException(sex.toString(), sex);
		} catch (Exception ex) {
			logger.fatal("Error while inserting into EDX_EVENT_PROCESS, id = "
					+ processDT.getSourceEventId(), ex);
			ex.printStackTrace();
			throw new NEDSSSystemException(ex.toString(), ex);
		}finally {
			closeStatement(preparedStmt);
			releaseConnection(dbConnection);
		}

	}
	
//	@SuppressWarnings("unchecked")
//	public Map<String, EDXEventProcessDT> getEDXEventProcessMap(Long nbsDocumentUid) {
//		Map<String, EDXEventProcessDT> eventProcessMap = new HashMap<String, EDXEventProcessDT>();
//		String docQuery = "";
//		try {
//			docQuery = " SELECT"
//					+ " edx_event_process_uid  \"eDXEventProcessUid\", "
//					+ " nbs_document_uid  \"nbsDocumentUid\", "
//					+ " nbs_event_uid  \"nbsEventUid\", "
//					+ " source_event_id \"sourceEventId\", "
//					+ " doc_event_type_cd \"docEventTypeCd\", "
//					+ " add_user_id \"addUserId\", " + " add_time \"addTime\", "
//					+ " parsed_ind \"parsedInd\" "
//					+ " FROM edx_event_process " + " WHERE nbs_document_uid=? ";
//			
//			logger.debug("\n docQuery  for event Process: getEDXEventProcessMap = " + docQuery);
//			ArrayList<Object> argList = new ArrayList<Object>();
//			argList.add(nbsDocumentUid);
//			EDXEventProcessDT processDT = new EDXEventProcessDT();
//			eventProcessMap = (Map<String, EDXEventProcessDT>) preparedStmtMethodForMap(
//					processDT, argList, docQuery, NEDSSConstants.SELECT,"getSourceEventId");
//			
//		} catch (Exception e) {
//			logger.error("Error in fetching event process data for document ");
//			throw new NEDSSSystemException(e.toString(), e);
//
//		}
//
//		return eventProcessMap;
//
//	}	
	
	@SuppressWarnings("unchecked")
	public Map<String, EDXEventProcessDT> getEDXEventProcessMap(Long nbsDocumentUid) {
		Map<String, EDXEventProcessDT> eventProcessMap = new HashMap<String, EDXEventProcessDT>();
		Connection dbConnection = null;
		CallableStatement sProc = null;
		ResultSet rs = null;
		try {
			dbConnection = getConnection();
			String sQuery  = "{call GETEDXEVENTPROCESSBYDOCID_SP(?)}";
			if (propertyUtil.getDatabaseServerType() != null &&
				      propertyUtil.
				      getDatabaseServerType().equalsIgnoreCase(NEDSSConstants.ORACLE_ID)) {
				sQuery  = "{call GETEDXEVENTPROCESSBYDOCID_SP(?,?)}";
			sProc = dbConnection.prepareCall(sQuery);
			sProc.setLong(1, nbsDocumentUid.longValue());
				// -10 indicates OracleType.CURSOR
				sProc.registerOutParameter(2, -10);
				sProc.executeQuery();
				rs = (ResultSet)sProc.getObject(2);

			}else{
				sProc = dbConnection.prepareCall(sQuery);
				sProc.setLong(1, nbsDocumentUid.longValue());
				 rs = sProc.executeQuery();
			}

	        while (rs.next()) {
	        	EDXEventProcessDT edxEventProcessDT = new EDXEventProcessDT();
				edxEventProcessDT.seteDXEventProcessUid(rs.getLong(1));
				edxEventProcessDT.setNbsDocumentUid(rs.getLong(2));
				edxEventProcessDT.setNbsEventUid(rs.getLong(3));
				edxEventProcessDT.setSourceEventId(rs.getString(4));
				edxEventProcessDT.setDocEventTypeCd(rs.getString(5));
				edxEventProcessDT.setAddUserId(rs.getLong(6));
				edxEventProcessDT.setAddTime(rs.getTimestamp(7));
				edxEventProcessDT.setParsedInd(rs.getString(8));
				eventProcessMap.put(edxEventProcessDT.getSourceEventId(), edxEventProcessDT);
			}
		} catch (Exception e) {
			logger.error("Error in fetching event process data for document "+e.getMessage(), e);
			throw new NEDSSSystemException(e.toString(), e);
		}
		finally {
			closeStatement(sProc);
			releaseConnection(dbConnection);
		}
		return eventProcessMap;
	}
//	
//	@SuppressWarnings("unchecked")
//	public EDXEventProcessDT getEDXEventProcessDTBySourceId(String sourceId) {
//		ArrayList<EDXEventProcessDT> eventProcessList = new ArrayList<EDXEventProcessDT>();
//		String docQuery = "";
//		try {
//			docQuery = " SELECT"
//					+ " edx_event_process_uid  \"eDXEventProcessUid\", "
//					+ " nbs_document_uid  \"nbsDocumentUid\", "
//					+ " nbs_event_uid  \"nbsEventUid\", "
//					+ " source_event_id \"sourceEventId\", "
//					+ " doc_event_type_cd \"docEventTypeCd\", "
//					+ " add_user_id \"addUserId\", " + " add_time \"addTime\" "
//					+ " FROM edx_event_process " + " WHERE source_event_id=? ";
//
//			logger.debug("\n docQuery  for event Process: getEDXEventProcessDTBySourceId= " + docQuery);
//			ArrayList<Object> argList = new ArrayList<Object>();
//			argList.add(sourceId);
//			EDXEventProcessDT processDT = new EDXEventProcessDT();
//			eventProcessList = (ArrayList<EDXEventProcessDT>) this
//					.preparedStmtMethod(processDT, argList, docQuery,
//							NEDSSConstants.SELECT);
//
//		} catch (Exception e) {
//			logger.error("Error in fetching event process data for document ");
//			throw new NEDSSSystemException(e.toString(), e);
//
//		}
//		if (eventProcessList != null && eventProcessList.size() > 0)
//			return eventProcessList.get(0);
//		else
//			return null;
//
//	}
	
	@SuppressWarnings("unchecked")
	public EDXEventProcessDT getEDXEventProcessDTBySourceId(String sourceId) {
		EDXEventProcessDT edxEventProcessDT = null;
		try {
			ArrayList<Object> inArrayList= new ArrayList<Object> ();
	        ArrayList<Object>  outArrayList= new ArrayList<Object> ();
	        ArrayList<Object> arrayList = new ArrayList<Object> ();
	        inArrayList.add(sourceId);//source_event_id
	        outArrayList.add(new Integer(java.sql.Types.BIGINT)); //edx_event_process_uid
	        outArrayList.add(new Integer(java.sql.Types.BIGINT)); //nbs_document_uid
	        outArrayList.add(new Integer(java.sql.Types.BIGINT)); //nbs_event_uid
	        outArrayList.add(java.sql.Types.VARCHAR);//source_event_id
	        outArrayList.add(java.sql.Types.VARCHAR); //doc_event_type_cd
	        outArrayList.add(new Integer(java.sql.Types.BIGINT)); //add_user_id
	        outArrayList.add(java.sql.Types.TIMESTAMP); //add_time

			String sQuery  = "{call GETEDXEVENTPROCESS_SP(?,?,?,?,?,?,?,?)}";
			arrayList = (ArrayList<Object> )callStoredProcedureMethod(sQuery,inArrayList,outArrayList);

			if(arrayList != null && arrayList.size() > 0)
			{
				edxEventProcessDT = new EDXEventProcessDT();
				edxEventProcessDT.seteDXEventProcessUid((Long)arrayList.get(0));
				edxEventProcessDT.setNbsDocumentUid((Long)arrayList.get(1));
				edxEventProcessDT.setNbsEventUid((Long)arrayList.get(2));
				edxEventProcessDT.setSourceEventId((String)arrayList.get(3));
				edxEventProcessDT.setDocEventTypeCd((String)arrayList.get(4));
				edxEventProcessDT.setAddUserId((Long)arrayList.get(5));
				edxEventProcessDT.setAddTime((Timestamp)arrayList.get(6));
			}
		} catch (Exception e) {
			logger.fatal("Error in fetching event process data for document "+e.getMessage(), e);
			throw new NEDSSSystemException(e.toString(), e);
		}
		return edxEventProcessDT;
	}
	
//	@SuppressWarnings("unchecked")
//	public EDXEventProcessDT getEDXEventProcessDTBySourceIdandEventType(String sourceId, String eventType) {
//		ArrayList<EDXEventProcessDT> eventProcessList = new ArrayList<EDXEventProcessDT>();
//		String docQuery = "";
//		try {
//			docQuery = " SELECT"
//					+ " edx_event_process_uid  \"eDXEventProcessUid\", "
//					+ " nbs_document_uid  \"nbsDocumentUid\", "
//					+ " nbs_event_uid  \"nbsEventUid\", "
//					+ " source_event_id \"sourceEventId\", "
//					+ " doc_event_type_cd \"docEventTypeCd\", "
//					+ " add_user_id \"addUserId\", " + " add_time \"addTime\", "
//					+ " parsed_ind \"parsedInd\" "
//					+ " FROM edx_event_process " + " WHERE source_event_id=?  and doc_event_type_cd=?";
//
//			logger.debug("\n docQuery  for event Process: getEDXEventProcessDTBySourceId= " + docQuery);
//			ArrayList<Object> argList = new ArrayList<Object>();
//			argList.add(sourceId);
//			argList.add(eventType);
//			EDXEventProcessDT processDT = new EDXEventProcessDT();
//			eventProcessList = (ArrayList<EDXEventProcessDT>) this
//					.preparedStmtMethod(processDT, argList, docQuery,
//							NEDSSConstants.SELECT);
//
//		} catch (Exception e) {
//			logger.error("Error in fetching event process data for document ");
//			throw new NEDSSSystemException(e.toString(), e);
//
//		}
//		if (eventProcessList != null && eventProcessList.size() > 0)
//			return eventProcessList.get(0);
//		else
//			return null;
//
//	}
	
	
	@SuppressWarnings("unchecked")
	public EDXEventProcessDT getEDXEventProcessDTBySourceIdandEventType(String sourceId, String eventType) {
		EDXEventProcessDT edxEventProcessDT = null;
		try {
			ArrayList<Object> inArrayList= new ArrayList<Object> ();
	        ArrayList<Object>  outArrayList= new ArrayList<Object> ();
	        ArrayList<Object> arrayList = new ArrayList<Object> ();
	        inArrayList.add(sourceId);//source_event_id
	        inArrayList.add(eventType);//doc_event_type_cd
	        outArrayList.add(new Integer(java.sql.Types.BIGINT)); //edx_event_process_uid
	        outArrayList.add(new Integer(java.sql.Types.BIGINT)); //nbs_document_uid
	        outArrayList.add(new Integer(java.sql.Types.BIGINT)); //nbs_event_uid
	        outArrayList.add(java.sql.Types.VARCHAR);//source_event_id
	        outArrayList.add(java.sql.Types.VARCHAR); //doc_event_type_cd
	        outArrayList.add(new Integer(java.sql.Types.BIGINT)); //add_user_id
	        outArrayList.add(java.sql.Types.TIMESTAMP); //add_time
	        outArrayList.add(java.sql.Types.VARCHAR);//parsed_ind

			String sQuery  = "{call GETEDXEVENTPROCESSWITHTYPE_SP(?,?,?,?,?,?,?,?,?,?)}";
			arrayList = (ArrayList<Object> )callStoredProcedureMethod(sQuery,inArrayList,outArrayList);

			if(arrayList != null && arrayList.size() > 0)
			{
				edxEventProcessDT = new EDXEventProcessDT();
				edxEventProcessDT.seteDXEventProcessUid((Long)arrayList.get(0));
				edxEventProcessDT.setNbsDocumentUid((Long)arrayList.get(1));
				edxEventProcessDT.setNbsEventUid((Long)arrayList.get(2));
				edxEventProcessDT.setSourceEventId((String)arrayList.get(3));
				edxEventProcessDT.setDocEventTypeCd((String)arrayList.get(4));
				edxEventProcessDT.setAddUserId((Long)arrayList.get(5));
				edxEventProcessDT.setAddTime((Timestamp)arrayList.get(6));
				edxEventProcessDT.setParsedInd((String)arrayList.get(7));
			}
		} catch (Exception e) {
			logger.fatal("Error in fetching event process data for document "+ e.getMessage(), e);
			throw new NEDSSSystemException(e.toString(), e);
		}
		return edxEventProcessDT;
	}

	
	
	@SuppressWarnings("unchecked")
	public Map<String, EDXEventProcessDT> getEDXEventProcessMapByCaseId(Long publicHealthCaseUid) {
		Map<String, EDXEventProcessDT> eventProcessMap = new HashMap<String, EDXEventProcessDT>();
		String docQuery = "";
		try {
			docQuery = " SELECT"
					+ " edx_event_process_uid  \"eDXEventProcessUid\", "
					+ " nbs_document_uid  \"nbsDocumentUid\", "
					+ " nbs_event_uid  \"nbsEventUid\", "
					+ " source_event_id \"sourceEventId\", "
					+ " doc_event_type_cd \"docEventTypeCd\", "
					+ " edx_event_process.add_user_id \"addUserId\", " + " edx_event_process.add_time \"addTime\", "
					+ " parsed_ind \"parsedInd\" "
					+ " FROM edx_event_process, act_relationship "
					+ " where edx_event_process.nbs_event_uid=act_relationship.source_act_uid "
					+ " and act_relationship.target_act_uid = ? order by nbs_document_uid";
			
			logger.debug("\n docQuery  for event Process:  getEDXEventProcessMapByCaseId= " + docQuery);
			ArrayList<Object> argList = new ArrayList<Object>();
			argList.add(publicHealthCaseUid);
			EDXEventProcessDT processDT = new EDXEventProcessDT();
			eventProcessMap = (Map<String, EDXEventProcessDT>) preparedStmtMethodForMap(
					processDT, argList, docQuery, NEDSSConstants.SELECT,"getSourceEventId");
			
		} catch (Exception e) {
			logger.fatal("Error in fetching event process data for document "+e.getMessage(), e);
			throw new NEDSSSystemException(e.toString(), e);
		}
		return eventProcessMap;
	}
	
	@SuppressWarnings("unchecked")
	public Map<String, EDXEventProcessDT> getEDXEventProcessMapBySourceEventIdandEventType(String sourceEventId, String eventType) {
		Map<String, EDXEventProcessDT> eventProcessMap = new HashMap<String, EDXEventProcessDT>();
		String docQuery = "";
		try {
			docQuery = " SELECT"
					+ " edx_event_process_uid  \"eDXEventProcessUid\", "
					+ " nbs_document_uid  \"nbsDocumentUid\", "
					+ " nbs_event_uid  \"nbsEventUid\", "
					+ " source_event_id \"sourceEventId\", "
					+ " doc_event_type_cd \"docEventTypeCd\", "
					+ " edx_event_process.add_user_id \"addUserId\", " + " edx_event_process.add_time \"addTime\", "
					+ " parsed_ind \"parsedInd\" "
					+ " FROM edx_event_process where source_event_id=? and doc_event_type_cd=?";
			
			logger.debug("\n docQuery  for event Process : getEDXEventProcessMapBySourceEventIdandEventType= " + docQuery);
			ArrayList<Object> argList = new ArrayList<Object>();
			argList.add(sourceEventId);
			argList.add(eventType);
			EDXEventProcessDT processDT = new EDXEventProcessDT();
			eventProcessMap = (Map<String, EDXEventProcessDT>) preparedStmtMethodForMap(
					processDT, argList, docQuery, NEDSSConstants.SELECT,"getSourceEventId");
			
		} catch (Exception e) {
			logger.fatal("Error in fetching event process data for document by source event id and event type"+e.getMessage(), e);
			throw new NEDSSSystemException(e.toString(), e);
		}
		return eventProcessMap;
	}
	
//	@SuppressWarnings("unchecked")
//	public ArrayList<EDXEventProcessCaseSummaryDT> getEDXEventProcessCaseSummaryByEventId(String sourceEventId) {
//		ArrayList<EDXEventProcessCaseSummaryDT> eventProcessList = new ArrayList<EDXEventProcessCaseSummaryDT>();
//		String docQuery = "";
//		try {
//			docQuery = " SELECT"
//					+ " edx_event_process_uid  \"eDXEventProcessUid\", "
//					+ " nbs_document_uid  \"nbsDocumentUid\", "
//					+ " nbs_event_uid  \"nbsEventUid\", "
//					+ " source_event_id \"sourceEventId\", "
//					+ " parsed_ind \"parsedInd\", "
//					+ " doc_event_type_cd \"docEventTypeCd\", "
//					+ " person_uid \"personUid\", "
//					+ " person_parent_uid \"personParentUid\", "
//					+ " per.local_id \"personLocalId\", "
//					+ " phc.local_id \"localId\", "
//					+ " phc.jurisdiction_cd \"jurisdictionCd\", "
//					+ " phc.prog_area_cd \"progAreaCd\", "
//					+ " phc.cd \"conditionCd\", "
//					+ " phc.program_jurisdiction_oid \"programJurisdictionOid\", "
//					+ " eep.add_user_id \"addUserId\", " + " eep.add_time \"addTime\" "
//					+ " FROM edx_event_process eep , public_health_case phc, participation par, person per "
//					+ " where  eep.nbs_event_uid = phc.public_health_case_uid "
//					+ " and eep.source_event_id=? and eep.doc_event_type_cd='CASE' and"
//					+ " phc.public_health_case_uid = par.act_uid and "
//					+ " par.subject_entity_uid = per.person_uid and par.type_cd='SubjOfPHC'";
//			logger.debug("\n docQuery  for event case summary Process: getEDXEventProcessCaseSummaryByEventId = " + docQuery);
//			ArrayList<Object> argList = new ArrayList<Object>();
//			argList.add(sourceEventId);
//			EDXEventProcessCaseSummaryDT processDT = new EDXEventProcessCaseSummaryDT();
//			eventProcessList = (ArrayList<EDXEventProcessCaseSummaryDT>) this
//					.preparedStmtMethod(processDT, argList, docQuery,
//							NEDSSConstants.SELECT);
//		} catch (Exception e) {
//			logger.error("Error in fetching event process case summary data for document by source event id and event type");
//			throw new NEDSSSystemException(e.toString(), e);
//		}
//		return eventProcessList;
//	}
	
	@SuppressWarnings("unchecked")
	public ArrayList<EDXEventProcessCaseSummaryDT> getEDXEventProcessCaseSummaryByEventId(String sourceEventId) {
		ArrayList<EDXEventProcessCaseSummaryDT> eventProcessList = new ArrayList<EDXEventProcessCaseSummaryDT>();
		EDXEventProcessCaseSummaryDT edxEventProcessDT = null;
		try {
			ArrayList<Object> inArrayList= new ArrayList<Object> ();
	        ArrayList<Object>  outArrayList= new ArrayList<Object> ();
	        ArrayList<Object> arrayList = new ArrayList<Object> ();
	        inArrayList.add(sourceEventId);//source_event_id
	        outArrayList.add(new Integer(java.sql.Types.BIGINT)); //edx_event_process_uid
	        outArrayList.add(new Integer(java.sql.Types.BIGINT)); //nbs_document_uid
	        outArrayList.add(new Integer(java.sql.Types.BIGINT)); //nbs_event_uid
	        outArrayList.add(java.sql.Types.VARCHAR);//source_event_uid
	        outArrayList.add(java.sql.Types.VARCHAR);//parsed_ind
	        outArrayList.add(java.sql.Types.VARCHAR); //doc_event_type_cd
	        outArrayList.add(new Integer(java.sql.Types.BIGINT)); //person_uid
	        outArrayList.add(new Integer(java.sql.Types.BIGINT)); //person_parent_uid
	        outArrayList.add(java.sql.Types.VARCHAR); //person.local_id
	        outArrayList.add(java.sql.Types.VARCHAR); //phc.local_id
	        outArrayList.add(java.sql.Types.VARCHAR); //jurisdiction_cd
	        outArrayList.add(java.sql.Types.VARCHAR); //prog_area_cd
	        outArrayList.add(java.sql.Types.VARCHAR); //phc.cd
	        outArrayList.add(new Integer(java.sql.Types.BIGINT)); //program_jurisdiction_oid
	        outArrayList.add(new Integer(java.sql.Types.BIGINT)); //add_user_id
	        outArrayList.add(java.sql.Types.TIMESTAMP); //add_time
	        String sQuery  = "{call GETEDXEVENTPROCESSCASESUMM_SP(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)}";
			arrayList = (ArrayList<Object> )callStoredProcedureMethod(sQuery,inArrayList,outArrayList);
			if(arrayList != null && arrayList.size() > 0)
			{
				edxEventProcessDT = new EDXEventProcessCaseSummaryDT();
				edxEventProcessDT.seteDXEventProcessUid((Long)arrayList.get(0));
				edxEventProcessDT.setNbsDocumentUid((Long)arrayList.get(1));
				edxEventProcessDT.setNbsEventUid((Long)arrayList.get(2));
				edxEventProcessDT.setSourceEventId((String)arrayList.get(3));
				edxEventProcessDT.setParsedInd((String)arrayList.get(4));
				edxEventProcessDT.setDocEventTypeCd((String)arrayList.get(5));
				edxEventProcessDT.setPersonUid((Long)arrayList.get(6));
				edxEventProcessDT.setPersonParentUid((Long)arrayList.get(7));
				edxEventProcessDT.setPersonLocalId((String)arrayList.get(8));
				edxEventProcessDT.setLocalId((String)arrayList.get(9));
				edxEventProcessDT.setJurisdictionCd((String)arrayList.get(10));
				edxEventProcessDT.setProgAreaCd((String)arrayList.get(11));
				edxEventProcessDT.setConditionCd((String)arrayList.get(12));
				edxEventProcessDT.setProgramJurisdictionOid((Long)arrayList.get(13));
				edxEventProcessDT.setAddUserId((Long)arrayList.get(14));
				edxEventProcessDT.setAddTime((Timestamp)arrayList.get(15));
				if(edxEventProcessDT.geteDXEventProcessUid()!=null)
					eventProcessList.add(edxEventProcessDT);
			}
		} catch (Exception e) {
			logger.fatal("Error in fetching event process case summary data for document by source event id and event type"+e.getMessage(), e);
			throw new NEDSSSystemException(e.toString(), e);
		}
		return eventProcessList;
	}
	
	@SuppressWarnings("unchecked")
	public String getUserNameByProviderEntityID(String providerId) {
		String sqlQuery = null;
		Connection dbConnection = null;
		PreparedStatement preparedStmt = null;
		ResultSet result = null;
		String userName = "";
		try {
			sqlQuery = " select user_first_nm, user_last_nm from auth_user where provider_uid in (select entity_uid from entity_id where root_extension_txt = ?)";
			logger.debug("\n docQuery  for  user : getUserNameByProviderEntityID= "
					+ sqlQuery);
			dbConnection = getConnection();
			preparedStmt = dbConnection.prepareStatement(sqlQuery);
			preparedStmt.setString(1, providerId);
			result = preparedStmt.executeQuery();
			while(result.next()) {
				userName = result.getString(1) + " "+ result.getString(2);
}

		} catch (Exception e) {
			logger.error("Error in fetching user Name based on provider Id");
			throw new NEDSSSystemException(e.toString(), e);
		}
		finally {
			closeStatement(preparedStmt);
			releaseConnection(dbConnection);
		}
		return userName;
	}
}
