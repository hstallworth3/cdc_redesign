package gov.cdc.nedss.systemservice.ejb.pamconversionejb.util;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.rmi.RemoteException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.TreeMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import gov.cdc.nedss.association.dt.ParticipationDT;
import gov.cdc.nedss.exception.NEDSSSystemException;
import gov.cdc.nedss.nbsactentity.dt.NbsActEntityDT;
import gov.cdc.nedss.nnd.ejb.nndmessageprocessorejb.helper.PageBuilderToMasterMessageUtil;
import gov.cdc.nedss.page.ejb.pageproxyejb.vo.act.PageActProxyVO;
import gov.cdc.nedss.pagemanagement.ejb.pagemanagementproxyejb.vo.WaTemplateVO;
import gov.cdc.nedss.pagemanagement.wa.dt.PageCondMappingDT;
import gov.cdc.nedss.pagemanagement.wa.dt.WaTemplateDT;
import gov.cdc.nedss.pagemanagement.wa.dt.WaUiMetadataDT;
import gov.cdc.nedss.pam.act.NbsCaseAnswerDT;
import gov.cdc.nedss.systemservice.ejb.pamconversionejb.dao.PamConversionDAO;
import gov.cdc.nedss.systemservice.ejb.pamconversionejb.dao.PamConversionLegacyDAO;
import gov.cdc.nedss.systemservice.ejb.pamconversionejb.dt.NBSConversionConditionDT;
import gov.cdc.nedss.systemservice.ejb.pamconversionejb.dt.NBSConversionErrorDT;
import gov.cdc.nedss.systemservice.ejb.pamconversionejb.dt.NBSConversionMappingDT;
import gov.cdc.nedss.systemservice.ejb.pamconversionejb.dt.NBSConversionMasterDT;
import gov.cdc.nedss.systemservice.nbssecurity.NBSSecurityObj;
import gov.cdc.nedss.systemservice.vo.ParticipationTypeVO;
import gov.cdc.nedss.util.JNDINames;
import gov.cdc.nedss.util.LogUtils;
import gov.cdc.nedss.util.NEDSSConstants;
import gov.cdc.nedss.webapp.nbs.action.pagemanagement.portpage.util.PortPageUtil;
import gov.cdc.nedss.webapp.nbs.action.pagemanagement.rendering.util.PageStoreUtil;
import gov.cdc.nedss.webapp.nbs.action.pagemanagement.util.PageManagementActionUtil;
import gov.cdc.nedss.webapp.nbs.action.util.CallProxyEJB;
import gov.cdc.nedss.webapp.nbs.helper.CachedDropDowns;

/**
 * @author PatelDh
 *
 */
public class PBtoPBConverterProcessor {
	
	static final LogUtils logger = new LogUtils(PBtoPBConverterProcessor.class.getName());
	static PortPageUtil portUtil = new PortPageUtil();
	
	private static HashMap<Object,Object> nbsConversionMappingMap = new HashMap<Object,Object>();
	private static HashMap<String, Long> toQuestionIdentifierAndNbsQuestionUidMap = new HashMap<String, Long>();
	
	private final static String OTHER = "OTH";
	
	/**
	 * @param runType
	 * @param conditionCdGroupId
	 * @param numberOfCases
	 * @param conditionCd
	 * @param fromPageWaTemplateUid
	 * @param toPageWaTemplateUid
	 * @param request
	 * @return
	 * @throws Exception
	 */
	public static String startConversionProcess(String runType, Long conditionCdGroupId, int numberOfCases,String conditionCd, Long fromPageWaTemplateUid, Long toPageWaTemplateUid, Long nbsConversionPageMgmtUid, String busObjType, HttpServletRequest request) throws Exception{
		try{
			logger.debug("runType: "+runType+", numberOfCases: "+numberOfCases+", conditionCd: "+conditionCd+", fromPageWaTemplateUid: "+fromPageWaTemplateUid+", toPageWaTemplateUid: "+toPageWaTemplateUid+", nbsConversionPageMgmtUid: "+nbsConversionPageMgmtUid);
			String runStatus = "";
			if(PortPageUtil.RUN_TYPE_PRERUN.equals(runType)){
				runStatus=preRunValidation(conditionCdGroupId,conditionCd,nbsConversionPageMgmtUid,request,runType, toPageWaTemplateUid, busObjType);
				
				portUtil.removeMapFromNbsConvMapping(conditionCdGroupId,request);
				
			}else if(PortPageUtil.RUN_TYPE_PRODUCTION.equals(runType)){
				runStatus=preRunValidation(conditionCdGroupId,conditionCd,nbsConversionPageMgmtUid,request,runType, toPageWaTemplateUid, busObjType);
				
				if(!portUtil.PRE_RUN_ERROR.equals(runStatus)){
					initializeMapping();
					runStatus = convertPages(conditionCd, conditionCdGroupId, numberOfCases, fromPageWaTemplateUid, toPageWaTemplateUid, nbsConversionPageMgmtUid, request);
				}
			}
			return runStatus;
		}catch(Exception ex){
			logger.fatal("Exception thrown at startConversionProcess: "+ex.getMessage(), ex);
			throw new Exception(ex);
		}
	}
	
	public static  String preRunValidation(Long conditionCdGroupId,String conditionCd, Long nbsConversionPageMgmtUid, HttpServletRequest request,String runType, Long toPageWaTemplateUid, String busObjType) throws RemoteException{
		Collection<Object>  mappingErrorColl= new ArrayList<Object> ();
		ArrayList<Object> nbsConvMapList = new ArrayList<Object>();
		PamConversionDAO pamConvDAO = new PamConversionDAO();
		PamConversionLegacyDAO pamConvLegacyDAO = new PamConversionLegacyDAO();
		java.util.Date startDate= new java.util.Date();
		NBSConversionMasterDT nbsConvMasterDT =new NBSConversionMasterDT();
		Timestamp startTime = new Timestamp(startDate.getTime());
		String runStatus=portUtil.PRE_RUN_ERROR;
		
        NBSConversionConditionDT nbsConvCondDT = getNbsConversionConditionDTByCondition(conditionCd, nbsConversionPageMgmtUid,request);
		try{
			  nbsConvMapList=portUtil.getNbsConvMapping(conditionCdGroupId, request);
		      if(nbsConvMapList.size()== 0){
		    	  nbsConvMasterDT.setStatusCd("Pass");
		    	  nbsConvMasterDT.setProcessMessageTxt("No Translation Required");
		    	  nbsConvMasterDT.setProcessTypeInd(runType);
		    	  runStatus = portUtil.PRE_RUN_COMPLETE;
		       }
		      
		      // getting numeric type questions metadata to validate structured numeric and numeric type
		      ArrayList<Object> numericQuestionList = new ArrayList<Object>();
		      for(int i=0;i<nbsConvMapList.size();i++){
		    	  NBSConversionMappingDT nbsConvMappingDT =(NBSConversionMappingDT)nbsConvMapList.get(i);
		    	  if(PortPageUtil.DATA_TYPE_NUMERIC.equals(nbsConvMappingDT.getFromDataType()) && PortPageUtil.DATA_TYPE_NUMERIC.equals(nbsConvMappingDT.getToDataType())){
		    		  if(!numericQuestionList.contains(nbsConvMappingDT.getToQuestionId()))
		    			  numericQuestionList.add(nbsConvMappingDT.getToQuestionId());
		    	  }
		      }
		      Map<String, WaUiMetadataDT> numericQuestionMap = getWaUiMetadataByTemplateUidAndQuestionIdentifier(numericQuestionList, toPageWaTemplateUid, request);
		      
		      Iterator it = nbsConvMapList.iterator();
		         
		         while(it.hasNext()){
		        	 NBSConversionMappingDT nbsConvMappingDT =(NBSConversionMappingDT)it.next();
		        	 Collection<Object> errorColl = checkForMappingErrors(nbsConvMappingDT,conditionCd, numericQuestionMap);
		        	 mappingErrorColl.addAll(errorColl);
		         }
		         //Checks the map list if different From Question have been mapped to same To Question and have same Repeating Block Id Number.
		         ArrayList duplicateList = portUtil.checkIfDuplicateBlockId(nbsConvMapList);
                 mappingErrorColl.addAll(duplicateList);
		         
		         nbsConvMasterDT.setNBSConversionErrorDTCollection(mappingErrorColl);
		         StringBuffer buff= new StringBuffer();
		         
		         if(NEDSSConstants.VACCINATION_BUSINESS_OBJECT_TYPE.equals(busObjType)){
		        	 int remainingCasesToConvert = PortPageUtil.getRecordsToConvertCount(busObjType, nbsConversionPageMgmtUid, PortPageUtil.LEGACY_EVENT_DUMMY_CONDITION_CD, request);
		        	 buff.append("\nFor Vaccination "+ remainingCasesToConvert + " record(s) require conversion to the new TO page." );
		         }else if(NEDSSConstants.INVESTIGATION_BUSINESS_OBJECT_TYPE.equals(busObjType)){
		         //gets the number of cases to be transfered
		         
					/*int noCasesToBeTransferred = 0;
					noCasesToBeTransferred = pamConvLegacyDAO.getPublicHealthDTCount(conditionCd, portUtil.PORT_PAGEBUILDER);
					Integer remainingCases = noCasesToBeTransferred;
					if(nbsConvCondDT!=null){
						ArrayList<Object> convertedCaseList = PBtoPBConverterProcessor.getConvertedCasesFromNbsConversionMaster(nbsConvCondDT.getConditionCdGroupId(), PortPageUtil.CASE_CONVERSION_STATUS_CODE_PASS, request);
						if(convertedCaseList!=null){
							remainingCases = noCasesToBeTransferred - convertedCaseList.size();
						}
					}*/
		        	 
		        	 int remainingCasesToConvert = PortPageUtil.getRecordsToConvertCount(busObjType, nbsConversionPageMgmtUid, conditionCd, request);
					 buff.append("\nFor condition code \""+conditionCd +"\", "+ remainingCasesToConvert + " case(s) require conversion to the new TO page." );        
		         }
		         
					if(nbsConvMapList.size()!=0){
		              if(mappingErrorColl.size()>0){
		        	      nbsConvMasterDT.setStatusCd("Fail");
		        	      nbsConvMasterDT.setProcessMessageTxt("There were "+mappingErrorColl.size()+" mapping error(s). Please check NBS_conversion_error table for further details."+ buff.toString());
		        	      nbsConvMasterDT.setProcessTypeInd(runType);
		        	      runStatus=portUtil.PRE_RUN_ERROR;
		              }else{
		        	      nbsConvMasterDT.setProcessMessageTxt("There were "+mappingErrorColl.size()+" mapping error(s)."+ buff.toString());
		        	      nbsConvMasterDT.setStatusCd("Pass");
		        	      nbsConvMasterDT.setProcessTypeInd(runType);
		        	      runStatus=portUtil.PRE_RUN_COMPLETE;
		              }
		         }
		         nbsConvMasterDT.setNbsConversionConditionUid(nbsConvCondDT.getNbsConversionConditionUid());
		         java.util.Date endDate = new java.util.Date();
		         Timestamp endTime = new Timestamp(endDate.getTime());
		         nbsConvMasterDT.setStartTime(startTime);
		         nbsConvMasterDT.setEndTime(endTime);
		         writeToLogMaster(nbsConvMasterDT,request.getSession());
		         
	     }catch (Exception e) {
			   //logging the exception and writing it to NBS_Conversion_Master.
			   logger.error("Exception in preRunValidation method in PBtoPBConverterProcessor :"+e.getMessage(),e);
			   e.printStackTrace();
			   java.util.Date endDate = new java.util.Date();                 
			   Timestamp endTime = new Timestamp(endDate.getTime());
			   nbsConvMasterDT.setStatusCd("Fail");
			   nbsConvMasterDT.setProcessMessageTxt("Exception:"+e);
			   nbsConvMasterDT.setProcessTypeInd(runType);
			   nbsConvMasterDT.setStartTime(startTime);
			   nbsConvMasterDT.setEndTime(endTime);
			   nbsConvMasterDT.setNbsConversionConditionUid(nbsConvCondDT.getNbsConversionConditionUid());
			   
			   StringWriter errors = new StringWriter();
			   e.printStackTrace(new PrintWriter(errors));
			   String exceptionMessage = errors.toString();
			   String errorMsgToStore = exceptionMessage.substring(0,Math.min(exceptionMessage.length(), 2000));
			   nbsConvMasterDT.setProcessMessageTxt(errorMsgToStore);
				
			   writeToLogMaster(nbsConvMasterDT,request.getSession());
			   runStatus=portUtil.PRE_RUN_ERROR;
	     }
		 return runStatus ;
     }
	
	
	/**
	 * @param nBSConversionMasterDT
	 * @param session
	 */
	public static void writeToLogMaster(NBSConversionMasterDT nBSConversionMasterDT, HttpSession session){
		try {
			String sBeanJndiName = JNDINames.PAM_CONVERSION_EJB;
			String sMethod = "writeToLogMaster";
			Object[] oParams = new Object[] {nBSConversionMasterDT };
			CallProxyEJB.callProxyEJB(oParams, sBeanJndiName, sMethod, session);
		} catch (Exception e) {
			logger.error("Exception thrown at writeToLogMaster:"+e.getMessage(), e);
		}
	}
	
	
	/**
	 * @param nbsConversionErrorDT
	 * @param session
	 */
	private void writeToLogError(NBSConversionErrorDT nbsConversionErrorDT, HttpSession session){
		try {
			String sBeanJndiName = JNDINames.PAM_CONVERSION_EJB;
			String sMethod = "writeToLogError";
			Object[] oParams = new Object[] {nbsConversionErrorDT };
			CallProxyEJB.callProxyEJB(oParams, sBeanJndiName, sMethod, session);
		} catch (Exception e) {
			logger.error("Exception thrown at writeToLogError:"+e.getMessage(), e);
		}
	}
	

	
	private static Collection<Object> checkForMappingErrors(NBSConversionMappingDT nbsConvMappingDT,String conditionCd, Map<String, WaUiMetadataDT> toPageNumericQuestionMap){
		Collection<Object> errorColl = new ArrayList<Object>();
		PamConversionDAO pamConvDAO = new PamConversionDAO();
		//TO_DB_LOCATION		
		try{
			if(nbsConvMappingDT.getToDbLocation()==null || nbsConvMappingDT.getToDbLocation().trim().equalsIgnoreCase("")){
				 String errorMessage=" to_db_location missing. Please check WA_conversion_mapping table for FromQuestionId = "+nbsConvMappingDT.getFromQuestionId()+" and ToQuestionId = "+nbsConvMappingDT.getToQuestionId();
				 NBSConversionErrorDT nBSConversionErrorDT= portUtil.setNBSConversionMappingDT("TO_DB_LOCATION_MISSING",errorMessage, nbsConvMappingDT);
				errorColl.add(nBSConversionErrorDT);
			}
			else if(nbsConvMappingDT.getToDbLocation().indexOf(".")<0){
				 String errorMessage=" to_db_location missing or not in recognizable format. Please check WA_conversion_mapping table for FromQuestionId = "+nbsConvMappingDT.getFromQuestionId()+" and ToQuestionId = "+nbsConvMappingDT.getToQuestionId()+". The to_db_location needs to be populated as \"table_name.column_nm\".";
				 NBSConversionErrorDT nBSConversionErrorDT= portUtil.setNBSConversionMappingDT("TO_DB_LOCATION_MISSING",errorMessage, nbsConvMappingDT);
				errorColl.add(nBSConversionErrorDT);
			}
			else if(!nbsConvMappingDT.getToDbLocation().trim().equalsIgnoreCase("") ){
				String tableName = nbsConvMappingDT.getToDbLocation().substring(0, nbsConvMappingDT.getToDbLocation().indexOf("."));
				String columnName= nbsConvMappingDT.getToDbLocation().substring(nbsConvMappingDT.getToDbLocation().indexOf(".")+1);
				boolean isExistingTable = pamConvDAO.checkIfExistingTable(tableName);
				if(!isExistingTable){
					String errorMessage=" to_db_location missing or not in recognizable format. Please check WA_conversion_mapping table for FromQuestionId = "+nbsConvMappingDT.getFromQuestionId()+" and ToQuestionId = "+nbsConvMappingDT.getToQuestionId()+". The to_db_location TABLE:\""+ tableName+"\" does not exist.";
					 NBSConversionErrorDT nBSConversionErrorDT= portUtil.setNBSConversionMappingDT("NON_EXISTING_TABLE \""+ tableName+"\"",errorMessage, nbsConvMappingDT);
						errorColl.add(nBSConversionErrorDT);
				}
				if(isExistingTable){
					boolean isExistingColumn = pamConvDAO.checkIfExistingColumn(tableName, columnName);
					if(!isExistingColumn){
						String errorMessage=" to_db_location missing or not in recognizable format. Please check WA_conversion_mapping table for FromQuestionId = "+nbsConvMappingDT.getFromQuestionId()+" and ToQuestionId = "+nbsConvMappingDT.getToQuestionId()+". For  to_db_location  TABLE :\""+ tableName+"\",columnName:\""+columnName+"\"  does not exist.";
						 NBSConversionErrorDT nBSConversionErrorDT= portUtil.setNBSConversionMappingDT("NON_EXISTING_COLUMN for Table:"+ tableName+",columnName:"+columnName+"  does not exist",errorMessage, nbsConvMappingDT);
							errorColl.add(nBSConversionErrorDT);
					}
				}
			}
			//FROM_DB_LOCATION
			if(nbsConvMappingDT.getFromDbLocation()==null || nbsConvMappingDT.getFromDbLocation().trim().equalsIgnoreCase("")){
				 String errorMessage=" from_db_location missing. Please check WA_conversion_mapping table for FromQuestionId = "+nbsConvMappingDT.getFromQuestionId()+" and ToQuestionId = "+nbsConvMappingDT.getToQuestionId()+".";
				 NBSConversionErrorDT nBSConversionErrorDT= portUtil.setNBSConversionMappingDT("FROM_DB_LOCATION_MISSING",errorMessage, nbsConvMappingDT);
				errorColl.add(nBSConversionErrorDT);
			}
			else if(nbsConvMappingDT.getFromDbLocation().indexOf(".")<0){
				 String errorMessage=" from_db_location missing or not in recognizable format. Please check WA_conversion_mapping table for FromQuestionId = "+nbsConvMappingDT.getFromQuestionId()+" and ToQuestionId = "+nbsConvMappingDT.getToQuestionId()+". The from_db_location needs to be populated as \"table_name.column_nm\".";
				 NBSConversionErrorDT nBSConversionErrorDT= portUtil.setNBSConversionMappingDT("FROM_DB_LOCATION_MISSING",errorMessage, nbsConvMappingDT);
				errorColl.add(nBSConversionErrorDT);
			}
			else if(!nbsConvMappingDT.getFromDbLocation().trim().equalsIgnoreCase("") ){
				String tableName = nbsConvMappingDT.getFromDbLocation().substring(0, nbsConvMappingDT.getFromDbLocation().indexOf("."));
				String columnName= nbsConvMappingDT.getFromDbLocation().substring(nbsConvMappingDT.getFromDbLocation().indexOf(".")+1);
				boolean isExistingTable = pamConvDAO.checkIfExistingTable(tableName);
				if(!isExistingTable){
					String errorMessage=" from_db_location missing or not in recognizable format. Please check WA_conversion_mapping table for FromQuestionId = "+nbsConvMappingDT.getFromQuestionId()+" and ToQuestionId = "+nbsConvMappingDT.getToQuestionId()+". The from_db_location TABLE:\""+ tableName+"\" does not exist.";
					 NBSConversionErrorDT nBSConversionErrorDT= portUtil.setNBSConversionMappingDT("NON_EXISTING_TABLE "+ tableName,errorMessage, nbsConvMappingDT);
						errorColl.add(nBSConversionErrorDT);
				}
				if(isExistingTable){
					boolean isExistingColumn = pamConvDAO.checkIfExistingColumn(tableName, columnName);
					if(!isExistingColumn){
						String errorMessage=" from_db_location missing or not in recognizable format. Please check WA_conversion_mapping table for FromQuestionId = "+nbsConvMappingDT.getFromQuestionId()+" and ToQuestionId = "+nbsConvMappingDT.getToQuestionId()+". For  from_db_location  TABLE :\""+ tableName+"\",columnName:\""+columnName+"\"  does not exist.";
						 NBSConversionErrorDT nBSConversionErrorDT= portUtil.setNBSConversionMappingDT("NON_EXISTING_COLUMN for Table:\""+ tableName+"\",columnName:\""+columnName+"\"  does not exist",errorMessage, nbsConvMappingDT);
							errorColl.add(nBSConversionErrorDT);
					}
				}
			}
			
			if(nbsConvMappingDT.getFromQuestionId()==null || nbsConvMappingDT.getFromQuestionId().trim().equalsIgnoreCase("")){
				String errorMessage=" from_question_id  missing or not in recognizable format. Please check WA_conversion_mapping table for FromQuestionId = "+nbsConvMappingDT.getFromQuestionId()+" and ToQuestionId = "+nbsConvMappingDT.getToQuestionId()+". from_question_id cannot be null or Empty.";
				NBSConversionErrorDT nBSConversionErrorDT= portUtil.setNBSConversionMappingDT("FROM_QUESTION_ID_MISSING",errorMessage, nbsConvMappingDT);
				errorColl.add(nBSConversionErrorDT);
			}
			
			if(nbsConvMappingDT.getToQuestionId()==null || nbsConvMappingDT.getToQuestionId().trim().equalsIgnoreCase("")){
				String errorMessage=" to_question_id  missing or not in recognizable format. Please check WA_conversion_mapping table for FromQuestionId = "+nbsConvMappingDT.getFromQuestionId()+" and ToQuestionId = "+nbsConvMappingDT.getToQuestionId()+". to_question_id cannot be null or Empty.";
				NBSConversionErrorDT nBSConversionErrorDT= portUtil.setNBSConversionMappingDT("TO_QUESTION_ID_MISSING",errorMessage, nbsConvMappingDT);
				errorColl.add(nBSConversionErrorDT);
			}

			if(nbsConvMappingDT.getFromCodeSetNm()!=null){
				String fromCodeSetNm = nbsConvMappingDT.getFromCodeSetNm();
				String fromCode = nbsConvMappingDT.getFromCode();
				String returnVal=pamConvDAO.getValidCodeSetNm(fromCodeSetNm, fromCode);
				if(returnVal==null){
					
				}
				else if(returnVal.indexOf("INVALID_CODE_SET_NM_CODE_COMBINATION_ERROR")>-1){
					String errorMessage=" Mapped FROM_CODE_SET_NM_EXISTS_BUT_CODE_DOES_NOT_EXIST. Please check WA_conversion_mapping table for FromQuestionId = "+nbsConvMappingDT.getFromQuestionId()+" and ToQuestionId = "+nbsConvMappingDT.getToQuestionId()+"."+returnVal+".";
					NBSConversionErrorDT nBSConversionErrorDT= portUtil.setNBSConversionMappingDT("FROM_CODE_SET_NM_EXISTS_BUT_CODE_DOES_NOT_EXIST",errorMessage, nbsConvMappingDT);
					errorColl.add(nBSConversionErrorDT);
				}
				else if(returnVal.indexOf("INVALID_CODE_SET_NM_ERROR")>-1){
					String errorMessage=" Mapped FROM_CODE_SET_NM_DOES_NOT_EXIST. Please check WA_conversion_mapping table for FromQuestionId = "+nbsConvMappingDT.getFromQuestionId()+" and ToQuestionId = "+nbsConvMappingDT.getToQuestionId()+"."+returnVal+".";
					NBSConversionErrorDT nBSConversionErrorDT= portUtil.setNBSConversionMappingDT("FROM_CODE_SET_NM_DOES_NOT_EXIST",errorMessage, nbsConvMappingDT);
					errorColl.add(nBSConversionErrorDT);
				}
			}	
			if(nbsConvMappingDT.getToCodeSetNm()!=null){
				String toCodeSetNm = nbsConvMappingDT.getToCodeSetNm();
				String toCode = nbsConvMappingDT.getToCode();
				String returnVal=pamConvDAO.getValidCodeSetNm(toCodeSetNm, toCode);
				if(returnVal==null){
					
				}
				else if(returnVal.indexOf("INVALID_CODE_SET_NM_CODE_COMBINATION_ERROR")>-1){
					String errorMessage=" Mapped TO_CODE_SET_NM_EXISTS_BUT_CODE_DOES_NOT_EXIST. Please check WA_conversion_mapping table for FromQuestionId = "+nbsConvMappingDT.getFromQuestionId()+" and ToQuestionId = "+nbsConvMappingDT.getToQuestionId()+"."+returnVal+".";
					NBSConversionErrorDT nBSConversionErrorDT= portUtil.setNBSConversionMappingDT("TO_CODE_SET_NM_EXISTS_BUT_CODE_DOES_NOT_EXIST",errorMessage, nbsConvMappingDT);
					errorColl.add(nBSConversionErrorDT);
				}
				else if(returnVal.indexOf("INVALID_CODE_SET_NM_ERROR")>-1){
					String errorMessage=" Mapped TO_CODE_SET_NM_DOES_NOT_EXIST. Please check WA_conversion_mapping table for FromQuestionId = "+nbsConvMappingDT.getFromQuestionId()+" and ToQuestionId = "+nbsConvMappingDT.getToQuestionId()+"."+returnVal+".";
					NBSConversionErrorDT nBSConversionErrorDT= portUtil.setNBSConversionMappingDT("TO_CODE_SET_NM_DOES_NOT_EXIST",errorMessage, nbsConvMappingDT);
					errorColl.add(nBSConversionErrorDT);
				}
			}

			if(nbsConvMappingDT.getFromDataType()==null || nbsConvMappingDT.getFromDataType().trim().equalsIgnoreCase("")){
				String errorMessage=" from_data_type missing or not in recognizable format. Please check WA_conversion_mapping table for FromQuestionId = "+nbsConvMappingDT.getFromQuestionId()+" and ToQuestionId = "+nbsConvMappingDT.getToQuestionId()+". from_data_type cannot be null or Empty.";
				NBSConversionErrorDT nBSConversionErrorDT= portUtil.setNBSConversionMappingDT("FORM_DATA_TYPE_MISSING",errorMessage, nbsConvMappingDT);
				errorColl.add(nBSConversionErrorDT);
			}
			if(nbsConvMappingDT.getToDataType()==null || nbsConvMappingDT.getToDataType().trim().equalsIgnoreCase("")){
				String errorMessage=" to_data_type missing or not in recognizable format. Please check WA_conversion_mapping table for FromQuestionId = "+nbsConvMappingDT.getFromQuestionId()+" and ToQuestionId = "+nbsConvMappingDT.getToQuestionId()+". to_data_type cannot be null or Empty.";
				NBSConversionErrorDT nBSConversionErrorDT= portUtil.setNBSConversionMappingDT("TO_DATA_TYPE_MISSING",errorMessage, nbsConvMappingDT);
				errorColl.add(nBSConversionErrorDT);
			}

			//For CODED-CODED questions mapping ,if Map Question is Yes and Map Answer is No,then it will check if codes of FROM question present in codes of TO questions. 
			if(PortPageUtil.DATA_TYPE_CODED.equals(nbsConvMappingDT.getFromDataType()) && PortPageUtil.DATA_TYPE_CODED.equals(nbsConvMappingDT.getToDataType()) 
					&& (nbsConvMappingDT.getFromCode()==null||nbsConvMappingDT.getFromCode().trim().equalsIgnoreCase("")) && (nbsConvMappingDT.getToCode()==null||nbsConvMappingDT.getToCode().trim().equals(""))){
				ArrayList<Object> fromCodeList = (ArrayList<Object>) portUtil.getCodeListByCodeSetNm(nbsConvMappingDT.getFromCodeSetNm());
				ArrayList<Object> toCodeList = (ArrayList<Object>) portUtil.getCodeListByCodeSetNm(nbsConvMappingDT.getToCodeSetNm());
			
				ArrayList codeList = portUtil.checkIfCodesExist(fromCodeList, toCodeList);
				if(codeList.size()>0){
					StringBuffer buff= new StringBuffer();
					for(int i=0;i<codeList.size();i++){
						buff.append(codeList.get(i)+";");
					}
					String errorMessage="The following codes exist in the FROM PAGE valueset of "+nbsConvMappingDT.getFromCodeSetNm()+" associated with FROM Question "+nbsConvMappingDT.getFromQuestionId()
							                 +",but do not exist in the TO PAGE value set of "+nbsConvMappingDT.getToCodeSetNm()+" associated with TO QUESTION "+nbsConvMappingDT.getToQuestionId()+": "+buff.toString();
				    NBSConversionErrorDT nbsConversionErrorDT = portUtil.setNBSConversionMappingDT("FROM_CODE_NOT_EXISTING",errorMessage,nbsConvMappingDT);	
				    errorColl.add(nbsConversionErrorDT);
				}
			}
			
			 if(PortPageUtil.DATA_TYPE_NUMERIC.equals(nbsConvMappingDT.getFromDataType()) && PortPageUtil.DATA_TYPE_NUMERIC.equals(nbsConvMappingDT.getToDataType())){
				 WaUiMetadataDT waUiMetadataDT = toPageNumericQuestionMap.get(nbsConvMappingDT.getToQuestionId());
				 
				 if(nbsConvMappingDT.getUnitTypeCd()!=null && waUiMetadataDT!=null && !nbsConvMappingDT.getUnitTypeCd().equals(waUiMetadataDT.getUnitTypeCd())){
					 String errorMessage = "Unit Type Mismatch. Please check WA_conversion_mapping table for FromQuestionId = "+nbsConvMappingDT.getFromQuestionId()+" and ToQuestionId = "+nbsConvMappingDT.getToQuestionId()+". The NUMERIC question "+nbsConvMappingDT.getFromQuestionId()+" ("+nbsConvMappingDT.getFromLabel()+") has "+nbsConvMappingDT.getUnitTypeCd()+" units, but has been mapped to "+nbsConvMappingDT.getToQuestionId()+" ("+nbsConvMappingDT.getToLabel()+"), which has "+waUiMetadataDT.getUnitTypeCd()+" units. ";
					 NBSConversionErrorDT nBSConversionErrorDT= portUtil.setNBSConversionMappingDT("TO_DATA_TYPE_MISSING",errorMessage, nbsConvMappingDT);
					 errorColl.add(nBSConversionErrorDT);
				 }else if(nbsConvMappingDT.getUnitValue()!=null && waUiMetadataDT!=null && !nbsConvMappingDT.getUnitValue().equals(waUiMetadataDT.getUnitValue())){
					 String errorMessage = "Unit Type Mismatch. Please check WA_conversion_mapping table for FromQuestionId = "+nbsConvMappingDT.getFromQuestionId()+" and ToQuestionId = "+nbsConvMappingDT.getToQuestionId()+". The NUMERIC question "+nbsConvMappingDT.getFromQuestionId()+" ("+nbsConvMappingDT.getFromLabel()+") has "+nbsConvMappingDT.getUnitValue()+" unit value, but has been mapped to "+nbsConvMappingDT.getToQuestionId()+" ("+nbsConvMappingDT.getToLabel()+"), which has "+waUiMetadataDT.getUnitValue()+" unit value. ";
					 NBSConversionErrorDT nBSConversionErrorDT= portUtil.setNBSConversionMappingDT("TO_DATA_TYPE_MISSING",errorMessage, nbsConvMappingDT);
					 errorColl.add(nBSConversionErrorDT);
				 }
			 }
		}catch(Exception ex){
			logger.fatal("Exception thrown at checkForMappingErrors :"+ex.getMessage(), ex);
			throw new NEDSSSystemException(ex.toString());
		}
		return errorColl;
	}
	
	
	
	
	/**
	 * Actual conversion process to move data from FROM Page to TO Page.
	 * @param conditionCd
	 * @param conditionCdGroupId
	 * @param numberOfCases
	 * @param request
	 */
	private static String convertPages(String conditionCd, Long conditionCdGroupId, int numberOfCases, Long fromPageWaTemplateUid, Long toPageWaTemplateUid, Long nbsConversionPageMgmtUid, HttpServletRequest request){
		java.util.Date startDate= new java.util.Date();
		Timestamp startTime = new Timestamp(startDate.getTime());
		NBSConversionMasterDT nbsConvMasterDT = new NBSConversionMasterDT();
		PamConversionDAO pamConvDAO = new PamConversionDAO();
		NBSConversionConditionDT nbsConvCondDT = getNbsConversionConditionDTByCondition(conditionCd, nbsConversionPageMgmtUid,request);
		try{
			logger.debug("conditionCd: "+conditionCd+", conditionCdGroupId: "+conditionCdGroupId+", numberOfCases: "+numberOfCases+", fromPageWaTemplateUid: "+fromPageWaTemplateUid+", toPageWaTemplateUid: "+toPageWaTemplateUid+", nbsConversionPageMgmtUid: "+nbsConversionPageMgmtUid);
			//Load mapping
			loadMappingAndConversionMaps(conditionCdGroupId, request);
			
			//Publish the To Page if not
			publishToPage(toPageWaTemplateUid, conditionCd, request);

			loadToQuestionIdentifierAndNbsQuestionUidMap(getMappedToQuestionList(), toPageWaTemplateUid, request);
			
			//get list of public_health_case_uid associated with From Page.
			Map<Long, String> phcUidAndLocalIdMap = getNotConvertedPublicHealthCaseUids(conditionCd, conditionCdGroupId, request);
			if(phcUidAndLocalIdMap!=null)
				logger.debug("Not Converted PublicHealthCaseUids count: "+phcUidAndLocalIdMap.size());
			
			Map<Long, String>  questionIdentifierAndNbsQuestionUidMap  = new HashMap<Long, String> ();
			Collection<Object> errorColl = new ArrayList<Object>();
			
			long convertedCasesCount = 0;
			//Iterate through cases one by one and move data.
			if(phcUidAndLocalIdMap!=null && phcUidAndLocalIdMap.keySet().size() >= numberOfCases){
				Iterator it = phcUidAndLocalIdMap.entrySet().iterator();
			    while (it.hasNext() && convertedCasesCount<numberOfCases) {
			        Map.Entry element = (Map.Entry)it.next();//numberOfCases
					 
			            // Map to store discrete to repeatingBlock question, it used to re-arrange answer_group_seq_nbr to eliminate blank rows.
			        	Map<String, NbsCaseAnswerDT> repeatingBlockConvertedAnswers = new HashMap<String, NbsCaseAnswerDT>();
						Long publicHealthCaseUid = (Long) element.getKey();
						String phcLocalId = (String) element.getValue();
						logger.debug("Converting publicHealthCaseUid: "+publicHealthCaseUid+", LocalId: "+phcLocalId);
						
						nbsConvMasterDT.setActUid(publicHealthCaseUid);
						if(publicHealthCaseUid!=null){
							//Long publicHealthCaseUid = dropDownCodeDT.getLongKey();
							//Get 
							NBSSecurityObj nbsSecurityObj = (NBSSecurityObj) request.getSession().getAttribute("NBSSecurityObject");
							PageActProxyVO pageActProxyVO = (PageActProxyVO)PageBuilderToMasterMessageUtil.getPageProxyObject(publicHealthCaseUid, nbsSecurityObj);
							
							if(pageActProxyVO!=null){
								Map<Object, Object> pamAnswerDTMap = pageActProxyVO.getPageVO().getPamAnswerDTMap();
								
								// get keysets from pamAnswerDTMap
								ArrayList <Object> nbsQuestionUidList = new ArrayList<Object>();
								nbsQuestionUidList.addAll(pamAnswerDTMap.keySet());
								
								
								// get Question_identifiers from keysets and cache it, use it logging purpose. log in nedss.log file. Query to NBS_question table CacheDropDT.
								
								questionIdentifierAndNbsQuestionUidMap = getQuestionIdentifiersForNbsQuestionUids(nbsQuestionUidList,request);
								
								for(Object key : pamAnswerDTMap.keySet()){
									String questionIdentifier = questionIdentifierAndNbsQuestionUidMap.get(key);
									if(pamAnswerDTMap.get(key) instanceof NbsCaseAnswerDT){
										NbsCaseAnswerDT nbsCaseAnswerDT = (NbsCaseAnswerDT) pamAnswerDTMap.get(key);
										String mappingKey = getMappingKey(questionIdentifier, nbsCaseAnswerDT);
										
										if(nbsConversionMappingMap.containsKey(questionIdentifier) || nbsConversionMappingMap.containsKey(mappingKey)){
											logger.debug("Checking questionIdentifier: "+questionIdentifier+ " - exist in mappings");
											
											NBSConversionMappingDT conversionMappingDT = (NBSConversionMappingDT) nbsConversionMappingMap.get(mappingKey);
											if(conversionMappingDT == null){
												conversionMappingDT = (NBSConversionMappingDT) nbsConversionMappingMap.get(questionIdentifier);
											}
											logger.debug("Mapping exist for QuestionIdentifier in NBS_Conversion_Mapping table : "+questionIdentifier);
											
											//If From Question and To Question Identifiers are same then update answers
											if(questionIdentifier.equals(conversionMappingDT.getToQuestionId())){
												if(conversionMappingDT.getToCode() != null && conversionMappingDT.getToCode().length()>0){
													setAnswerText(conversionMappingDT, questionIdentifier, nbsCaseAnswerDT);
												}
												
												//Discrete to Repeating
												if(conversionMappingDT.getBlockIdNbr()!=null && conversionMappingDT.getBlockIdNbr()>0){
													if((conversionMappingDT.getFromCode() != null && conversionMappingDT.getFromCode().length()>0 && conversionMappingDT.getToCode() != null && conversionMappingDT.getToCode().length()>0) //Mapped
															|| (conversionMappingDT.getFromCode()!=null && conversionMappingDT.getToCode()!=null && conversionMappingDT.getFromCode().length()==0 && conversionMappingDT.getToCode().length()==0) // Direct Move
															|| (conversionMappingDT.getFromCode()==null && conversionMappingDT.getToCode()==null))
														repeatingBlockConvertedAnswers.put(conversionMappingDT.getBlockIdNbr()+"|"+conversionMappingDT.getToQuestionId(), nbsCaseAnswerDT);
												}
												
												nbsCaseAnswerDT.setItDirty(true);
												nbsCaseAnswerDT.setItNew(false);
												nbsCaseAnswerDT.setItDelete(false);
												logger.debug("pamAnswerDTMap updating questionIdentifier : "+questionIdentifier);
											}else{
												// If Question Identifiers for FromQuestion and ToQuestion are different then update nbs_question_ids
												Long nbsQuestionUid = toQuestionIdentifierAndNbsQuestionUidMap.get(conversionMappingDT.getToQuestionId());
												logger.debug("nbsQuestionUid to Update: " +nbsQuestionUid+ " For " +conversionMappingDT.getToQuestionId());
												nbsCaseAnswerDT.setNbsQuestionUid(nbsQuestionUid);
												
												//Update answer if toAnswer/toCode exist in mapping. If toAnswer not exist in mapping don't update answer
												if(conversionMappingDT.getToCode() != null && conversionMappingDT.getToCode().length()>0){
													setAnswerText(conversionMappingDT, questionIdentifier, nbsCaseAnswerDT);
												}
												
												//Discrete to Repeating
												if(conversionMappingDT.getBlockIdNbr()!=null && conversionMappingDT.getBlockIdNbr()>0){
													if((conversionMappingDT.getFromCode() != null && conversionMappingDT.getFromCode().length()>0 && conversionMappingDT.getToCode() != null && conversionMappingDT.getToCode().length()>0) //Mapped
															|| (conversionMappingDT.getFromCode()!=null && conversionMappingDT.getToCode()!=null && conversionMappingDT.getFromCode().length()==0 && conversionMappingDT.getToCode().length()==0) // Direct Move
															|| (conversionMappingDT.getFromCode()==null && conversionMappingDT.getToCode()==null))
														repeatingBlockConvertedAnswers.put(conversionMappingDT.getBlockIdNbr()+"|"+conversionMappingDT.getToQuestionId(), nbsCaseAnswerDT);
												}
												nbsCaseAnswerDT.setItDirty(true);
												nbsCaseAnswerDT.setItNew(false);
												nbsCaseAnswerDT.setItDelete(false);
												nbsCaseAnswerDT.setUpdateNbsQuestionUid(true);
												
												logger.debug("pamAnswerDTMap updating questionIdentifier: "+questionIdentifier);
											}
											
											//Direct move coded to text question, set description as Answer Text
											if(PortPageUtil.DATA_TYPE_CODED.equals(conversionMappingDT.getFromDataType()) && PortPageUtil.DATA_TYPE_TEXT.equals(conversionMappingDT.getToDataType())){
												String codeDesc = CachedDropDowns.getCodeDescTxtForCd(nbsCaseAnswerDT.getAnswerTxt(), conversionMappingDT.getFromCodeSetNm());
												if(codeDesc!=null)
													nbsCaseAnswerDT.setAnswerTxt(codeDesc);
											}
										}
									}else if(pamAnswerDTMap.get(key) instanceof ArrayList){
										//Multi-Select (List Box) type question
										//INV909, ARB002, 32416-0
									}
								}
	
								correctAnswerGroupSeqNumberForRepeatingBlockQuestions(repeatingBlockConvertedAnswers, toPageWaTemplateUid, request);
								
								Map<Object, Object> repeatingAnswerDTMap = pageActProxyVO.getPageVO().getPageRepeatingAnswerDTMap();
								//Add answer_group_seq_nbr column in nbs_conversion_mapping to handle repeating block question
								for(Object key : repeatingAnswerDTMap.keySet()){
									String questionIdentifier = questionIdentifierAndNbsQuestionUidMap.get(key);
									//If question not found then look up again 
									if(questionIdentifier==null){
										nbsQuestionUidList = new ArrayList<Object>();
										nbsQuestionUidList.add(key);
										Map<Long, String> map = getQuestionIdentifiersForNbsQuestionUids(nbsQuestionUidList,request);
										questionIdentifier = (String) map.get(key);
										questionIdentifierAndNbsQuestionUidMap.putAll(map);
									}
									if(repeatingAnswerDTMap.get(key) instanceof NbsCaseAnswerDT){
										NbsCaseAnswerDT nbsCaseAnswerDT = (NbsCaseAnswerDT) repeatingAnswerDTMap.get(key);
									}else if(repeatingAnswerDTMap.get(key) instanceof ArrayList){
										ArrayList nbsCaseAnswerList = (ArrayList) repeatingAnswerDTMap.get(key);
										for(int j=0;j<nbsCaseAnswerList.size();j++){
											NbsCaseAnswerDT nbsCaseAnswerDT = (NbsCaseAnswerDT) nbsCaseAnswerList.get(j);
											String mappingKey = getMappingKey(questionIdentifier, nbsCaseAnswerDT);
											if(nbsConversionMappingMap.containsKey(questionIdentifier+nbsCaseAnswerDT.getAnswerGroupSeqNbr()) || nbsConversionMappingMap.containsKey(mappingKey+nbsCaseAnswerDT.getAnswerGroupSeqNbr())){
												NBSConversionMappingDT conversionMappingDT = (NBSConversionMappingDT) nbsConversionMappingMap.get(mappingKey+nbsCaseAnswerDT.getAnswerGroupSeqNbr());
												if(conversionMappingDT == null){
													conversionMappingDT = (NBSConversionMappingDT) nbsConversionMappingMap.get(questionIdentifier+nbsCaseAnswerDT.getAnswerGroupSeqNbr());
												}
												logger.debug("Mapping exist for Repeating Block QuestionIdentifier in NBS_Conversion_Mapping table : "+questionIdentifier);
												
												//If From Question and To Question Identifiers are same then update answers
												if(questionIdentifier.equals(conversionMappingDT.getToQuestionId())){
													if(conversionMappingDT.getToCode()!=null && conversionMappingDT.getToCode().length()>0){
														setAnswerText(conversionMappingDT, questionIdentifier, nbsCaseAnswerDT);
													}
													nbsCaseAnswerDT.setItDirty(true);
													nbsCaseAnswerDT.setItNew(false);
													nbsCaseAnswerDT.setItDelete(false);
													if(conversionMappingDT.getBlockIdNbr()==null || conversionMappingDT.getBlockIdNbr()==0)
														nbsCaseAnswerDT.setAnswerGroupSeqNbr(null);
													logger.debug("if block : questionIdentifier : "+questionIdentifier);
												}else{
													// If Question Identifiers for FromQuestion and ToQuestion are different then update nbs_question_ids
													Long nbsQuestionUid = toQuestionIdentifierAndNbsQuestionUidMap.get(conversionMappingDT.getToQuestionId());
													logger.debug("nbsQuestionUid to Update: " +nbsQuestionUid+ " For " +conversionMappingDT.getToQuestionId());
													nbsCaseAnswerDT.setNbsQuestionUid(nbsQuestionUid);
													
													//Update answer if toAnswer/toCode exist in mapping. If toAnswer not exist in mapping dont update answer
													if(conversionMappingDT.getToCode() != null && conversionMappingDT.getToCode().length()>0){
														setAnswerText(conversionMappingDT, questionIdentifier, nbsCaseAnswerDT);
													}
													//Repeating to discrete update answerGroupSequence number to null
													if(conversionMappingDT.getBlockIdNbr()==null || conversionMappingDT.getBlockIdNbr()==0)
														nbsCaseAnswerDT.setAnswerGroupSeqNbr(null);
													
													nbsCaseAnswerDT.setItDirty(true);
													nbsCaseAnswerDT.setItNew(false);
													nbsCaseAnswerDT.setItDelete(false);
													nbsCaseAnswerDT.setUpdateNbsQuestionUid(true);
													
													logger.debug("else block : questionIdentifier : "+questionIdentifier);
												}
												
												//Direct move coded to text question, set description as Answer Text
												if(PortPageUtil.DATA_TYPE_CODED.equals(conversionMappingDT.getFromDataType()) && PortPageUtil.DATA_TYPE_TEXT.equals(conversionMappingDT.getToDataType())){
													String codeDesc = CachedDropDowns.getCodeDescTxtForCd(nbsCaseAnswerDT.getAnswerTxt(), conversionMappingDT.getFromCodeSetNm());
													if(codeDesc!=null)
														nbsCaseAnswerDT.setAnswerTxt(codeDesc);
												}
											}
											
										}
									}else{
										logger.debug("else repeatingAnswerDTMap.get(key): "+repeatingAnswerDTMap.get(key));
									}
								}
								
								
								//Participation type conversion
								ArrayList<Object> participationTypeQuestionList = getParticipationTypeQuestionsFromMapping();
								if(participationTypeQuestionList!=null && participationTypeQuestionList.size()>0){
									
									ArrayList<Object> participationDTList = (ArrayList<Object>) pageActProxyVO.getPublicHealthCaseVO().getTheParticipationDTCollection();
									
									ArrayList<Object> actEntityDTList = (ArrayList<Object>) pageActProxyVO.getPageVO().getActEntityDTCollection();
									for(int j=0;j<participationTypeQuestionList.size();j++){
										NBSConversionMappingDT nbsConversionMappingDT = (NBSConversionMappingDT) participationTypeQuestionList.get(j);
										if(nbsConversionMappingDT.getFromQuestionId()!=null && nbsConversionMappingDT.getToQuestionId()!=null){
											String fromQuestionTypeCode = getParticipationTypeCodeByQuestionIdentifier(nbsConversionMappingDT.getFromQuestionId()); 
											String toQuestionTypeCode = getParticipationTypeCodeByQuestionIdentifier(nbsConversionMappingDT.getToQuestionId()); 
											
											for(int k=0;k<participationDTList.size();k++){
												ParticipationDT participationDT = (ParticipationDT) participationDTList.get(k);
												if(participationDT!=null){
													if(fromQuestionTypeCode.equals(participationDT.getTypeCd())){
														participationDT.setItDelete(true);
														ParticipationDT participationDT2 =(ParticipationDT)participationDT.deepCopy();
														participationDT2.setTypeCd(toQuestionTypeCode);
														participationDT2.setItNew(true);
														participationDTList.add(participationDT2);
													}
												}
														
											}
											ArrayList<Object> actEntityDTUpdatedList = new ArrayList<Object> ();
											for(int k=0;k<actEntityDTList.size();k++){
												NbsActEntityDT nbsActEntityDT = (NbsActEntityDT) actEntityDTList.get(k);
												if(nbsActEntityDT!=null){
													if(fromQuestionTypeCode.equals(nbsActEntityDT.getTypeCd())){
														nbsActEntityDT.setItDelete(true);
														NbsActEntityDT nbsActEntityDT2 = (NbsActEntityDT) nbsActEntityDT.deepCopy();
														nbsActEntityDT2.setItDelete(false);
														nbsActEntityDT2.setItDirty(false);
														nbsActEntityDT2.setTypeCd(toQuestionTypeCode);
														nbsActEntityDT2.setItNew(true);
														actEntityDTUpdatedList.add(nbsActEntityDT2);
													}else{
														nbsActEntityDT.setItDirty(true);
													}
												}
											}
											
											actEntityDTList.addAll(actEntityDTUpdatedList);
										}
									}
								
								}else{ // Prevent to create duplicate entry in NBS_Act_Entity table while updating pageActProxyVO.
									ArrayList<Object> actEntityDTList = (ArrayList<Object>) pageActProxyVO.getPageVO().getActEntityDTCollection();
									for(int k=0;k<actEntityDTList.size();k++){
										NbsActEntityDT nbsActEntityDT = (NbsActEntityDT) actEntityDTList.get(k);
										if(nbsActEntityDT!=null){
											nbsActEntityDT.setItDirty(true);
										}
									}
									
								}
								
								
								// if it exist in mapping then update nbsCaseAnserDT's answer_txt for related values.
								// Debug question_identifier while iterating.
								
								// For question_identifer changes change nbs_question_uid.
								// log dropped questions debug, if question exist on from page but not on to page, user not mapped it. nbs_conversion_error log it.
								
								// Update pageActProxyVO
								pageActProxyVO.setItDirty(true);
								pageActProxyVO.setItNew(false);
								pageActProxyVO.setItDelete(false);
								pageActProxyVO.getPublicHealthCaseVO().getThePublicHealthCaseDT().setItDirty(true);
						        Long actUID = PageStoreUtil.sendProxyToPageEJB(pageActProxyVO, request, NEDSSConstants.CASE);
								
						        convertedCasesCount++;
						        if(actUID!=null){
							        // Add logging in NBS_Conversion_Master table for successful conversion of case, insert dropDownCodeDT.getValue(); i.e. Conversion successful for CAS10001010GA01
							        nbsConvMasterDT.setActUid(actUID);
							        nbsConvMasterDT.setProcessMessageTxt("Conversion successful for "+phcLocalId);
							        nbsConvMasterDT.setStatusCd("Pass");
							        nbsConvMasterDT.setProcessTypeInd(portUtil.RUN_TYPE_PRODUCTION);
									
									//After converting 1st case update condition Mapping Status IN_PROGESS. It helps to lock mapping.
									if(convertedCasesCount==1){
										updateNbsConversionCondition(nbsConversionPageMgmtUid, PortPageUtil.NBS_PAGE_MAPPING_STATUS_PORTING_IN_PROGRESS, conditionCdGroupId, conditionCd, request);
									}
									logger.debug("Conversion successful for - publicHealthCaseUid: "+publicHealthCaseUid+", LocalId: "+phcLocalId);
						        }else{
						        	// Add Error Log
						        	NBSConversionMappingDT nbsConvMapDT = new NBSConversionMappingDT();
						        	nbsConvMapDT.setConditionCdGroupId(conditionCdGroupId);
						        	nbsConvMapDT.setNbsConversionMappingUid(0L);
							        nbsConvMasterDT.setProcessMessageTxt("Conversion failed.Please check NBS_conversion_error table for further details.");
							        nbsConvMasterDT.setStatusCd("Fail");
							        nbsConvMasterDT.setProcessTypeInd(portUtil.RUN_TYPE_PRODUCTION);
							        String errorMessage = "Converison Failed for Case :"+phcLocalId;
							        NBSConversionErrorDT nbsConErrorDT = portUtil.setNBSConversionMappingDT("CONVERSION_FAILED",errorMessage,nbsConvMapDT);
							        errorColl.add(nbsConErrorDT);
							        nbsConvMasterDT.setNBSConversionErrorDTCollection(errorColl);
						        }
							}else{
								logger.debug("Unable to find pageActProxyVO for publicHealthCaseUid: "+publicHealthCaseUid+ ", ConditionCd: "+conditionCd);
								//Add logging in NBS_Conversion_Master table
								NBSConversionMappingDT  nbsConvMapDT = new NBSConversionMappingDT();
								nbsConvMapDT.setConditionCdGroupId(conditionCdGroupId);
								nbsConvMapDT.setNbsConversionMappingUid(0L);
						        nbsConvMasterDT.setProcessMessageTxt("Unable to find Public health case :"+publicHealthCaseUid+".Please check NBS_conversion_error table for further details.");
						        nbsConvMasterDT.setStatusCd("Fail");
						        nbsConvMasterDT.setProcessTypeInd(portUtil.RUN_TYPE_PRODUCTION);
						        String errorMessage = "Unable to find pageActProxyVO for publicHealthCaseUid: "+publicHealthCaseUid+ ", ConditionCd: "+conditionCd;
						        NBSConversionErrorDT nbsConvErrorDT =portUtil.setNBSConversionMappingDT("CASE_NOT_EXISTS", errorMessage, nbsConvMapDT);
						        errorColl.add(nbsConvErrorDT);
						        nbsConvMasterDT.setNBSConversionErrorDTCollection(errorColl);
							}
						//}
					}else{
						logger.debug("Public health cases not found for condtionCd: "+conditionCd);
						//Add logging in NBS_Conversion_Master table
						NBSConversionMappingDT nbsConvMapDT = new NBSConversionMappingDT();
						nbsConvMapDT.setConditionCdGroupId(conditionCdGroupId);
				        nbsConvMasterDT.setProcessMessageTxt("Public health cases not found.Please check NBS_conversion_error table for further details.");
				        nbsConvMasterDT.setStatusCd("Fail");
				        nbsConvMasterDT.setProcessTypeInd(portUtil.RUN_TYPE_PRODUCTION);
				        String errorMessage = "Public health cases not found for condtionCd: "+conditionCd;
				        NBSConversionErrorDT nbsConvErrDT = portUtil.setNBSConversionMappingDT("CASE_NOT_FOUND", errorMessage, nbsConvMapDT);
				        errorColl.add(nbsConvErrDT);
				        nbsConvMasterDT.setNBSConversionErrorDTCollection(errorColl);
					}
					//writing the error to NBS_Conversion_Master table and NBS_Conversion_Error table.
					java.util.Date endDate = new java.util.Date();
			        Timestamp endTime = new Timestamp(endDate.getTime());
			        nbsConvMasterDT.setStartTime(startTime);
			        nbsConvMasterDT.setEndTime(endTime);
			        nbsConvMasterDT.setNbsConversionConditionUid(nbsConvCondDT.getNbsConversionConditionUid());
					writeToLogMaster(nbsConvMasterDT,request.getSession());
				}
				
			}else{
				logger.debug("Only "+phcUidAndLocalIdMap.keySet().size()+" cases remaining, cannot convert "+numberOfCases);
				
				nbsConvMasterDT.setProcessMessageTxt("Only "+phcUidAndLocalIdMap.keySet().size()+" cases remaining, cannot convert "+numberOfCases);
		        nbsConvMasterDT.setProcessTypeInd(portUtil.RUN_TYPE_PRODUCTION);
		        nbsConvMasterDT.setActUid(null);
		        nbsConvMasterDT.setStartTime(startTime);
		        nbsConvMasterDT.setEndTime(PortPageUtil.getCurrentTimestamp());
		        nbsConvMasterDT.setNbsConversionConditionUid(nbsConvCondDT.getNbsConversionConditionUid());
				writeToLogMaster(nbsConvMasterDT,request.getSession());
			}
			
			if(phcUidAndLocalIdMap.keySet().size()==numberOfCases){
				//Update condtion_code table
				updateInvFormCdForConditionCode(toPageWaTemplateUid, conditionCd, request);
				//Update page in PAGE_COND_MAPPING
				updateTemplateUidForConditionCode(conditionCd, fromPageWaTemplateUid, toPageWaTemplateUid, request);
				
				updateNbsConversionCondition(nbsConversionPageMgmtUid, PortPageUtil.NBS_PAGE_MAPPING_STATUS_COMPLETE, conditionCdGroupId, conditionCd, request);
				updateNbsConversionPageMgmt(nbsConversionPageMgmtUid, PortPageUtil.NBS_PAGE_MAPPING_STATUS_COMPLETE, request);
			
				return portUtil.PROD_RUN_COMPLETE;
			}

		}catch(Exception ex){
			logger.error("Exception thrown at convertPages: conditionCd: "+conditionCd+", conditionCdGroupId: "+conditionCdGroupId+", numberOfCases: "+numberOfCases+", Exception: "+ex.getMessage(), ex);
			java.util.Date endDate = new java.util.Date();
	        Timestamp endTime = new Timestamp(endDate.getTime());
	        nbsConvMasterDT.setStartTime(startTime);
			nbsConvMasterDT.setEndTime(endTime);
			nbsConvMasterDT.setProcessTypeInd(portUtil.RUN_TYPE_PRODUCTION);
			nbsConvMasterDT.setStatusCd("Fail");
			nbsConvMasterDT.setNbsConversionConditionUid(nbsConvCondDT.getNbsConversionConditionUid());
			//Extract exception details
			StringWriter errors = new StringWriter();
			ex.printStackTrace(new PrintWriter(errors));
			String exceptionMessage = errors.toString();
			String errorMsgToStore = exceptionMessage.substring(0,Math.min(exceptionMessage.length(), 2000));
			nbsConvMasterDT.setProcessMessageTxt(errorMsgToStore);
			
			writeToLogMaster(nbsConvMasterDT,request.getSession());
			throw new NEDSSSystemException(ex.getMessage());
		}
		return portUtil.PROD_RUN_PARTIAL;
	}
	
	/**
	 * If To page is not published then publish it before converting data
	 * @param toPageWaTemplateUid
	 * @param conditionCd
	 * @param request
	 */
	private static void publishToPage(Long toPageWaTemplateUid, String conditionCd, HttpServletRequest request){
		try{
			PageManagementActionUtil util = new PageManagementActionUtil();
			WaTemplateVO waTemplateVO =  util.getPageDetails(toPageWaTemplateUid, request.getSession());
			WaTemplateDT templateDT = waTemplateVO.getWaTemplateDT();
			logger.debug("Publishing page waTemplateUid "+toPageWaTemplateUid+", TemplateNm: "+templateDT.getTemplateNm());
			String mapName = (String) request.getAttribute("mapName");
			String fromPageName = (String) request.getAttribute("fromPageName");
			
			String versionNote = "This page was published by the system as a part of the Page Builder to Page Builder conversion process. "+
			"At the time of the conversion, all existing investigations and mapped data were ported to this page from the "+fromPageName+" page using the "+mapName+" map.";
			
			templateDT.setVersionNote(versionNote);
			// If page is in published with draft state then Publish the page.
			if(PortPageUtil.TEMPLATE_TYPE_PUBLISHED_WITH_DRAFT.equals(templateDT.getTemplateType())){
				util.publishPage(templateDT, request.getSession());
			}else if(PortPageUtil.TEMPLATE_TYPE_DRAFT.equals(templateDT.getTemplateType()) && waTemplateVO!=null && waTemplateVO.getWaPageCondMappingDTColl()!=null && waTemplateVO.getWaPageCondMappingDTColl().size()>0){
				//If page is in draft state and condition associated with it then publish it
				util.publishPage(templateDT, request.getSession());
			}else if(PortPageUtil.TEMPLATE_TYPE_DRAFT.equals(templateDT.getTemplateType())){
				
				// If page is in Draft state then Insert into page_cond_mapping then publish the page.
				PageCondMappingDT pageCondMappDT = new PageCondMappingDT();
				pageCondMappDT.setWaTemplateUid(toPageWaTemplateUid);
				pageCondMappDT.setConditionCd(conditionCd);
				pageCondMappDT.setAddTime(PortPageUtil.getCurrentTimestamp());
				pageCondMappDT.setLastChgTime(PortPageUtil.getCurrentTimestamp());
				
				String sBeanJndiName = JNDINames.PORT_PAGE_PROXY_EJB;
		    	String sMethod = "createPageCondiMapping";
				Object[] sParams = new Object[] {pageCondMappDT};
				CallProxyEJB.callProxyEJB(sParams, sBeanJndiName, sMethod, request.getSession());
				
				//Publish the page
				util.publishPage(templateDT, request.getSession());
			}
		}catch(Exception ex){
			logger.error("Exception thrown at publishToPage:"+ex.getMessage(), ex);
			throw new NEDSSSystemException(ex.getMessage());
		}
	}
	
	/**
	 * Cache the mappings
	 * @param conditionCdGroupId
	 * @param request
	 * @return
	 */
	private static void loadMappingAndConversionMaps(Long conditionCdGroupId, HttpServletRequest request){
		try{
			logger.debug("conditionCdGroupId: "+conditionCdGroupId);
			
			if(nbsConversionMappingMap.size()==0){
				ArrayList<Object> nbsConversionMappingList = new ArrayList<Object>();
				String sBeanJndiName = JNDINames.PORT_PAGE_PROXY_EJB;
		    	String sMethod = "getNBSConversionMappings";
				Object[] oParams = new Object[] {conditionCdGroupId};
				Object nbsConversionMappingObj = CallProxyEJB.callProxyEJB(oParams, sBeanJndiName, sMethod, request.getSession());
				nbsConversionMappingList = (ArrayList<Object>) nbsConversionMappingObj;
				logger.debug("Total Mapping questions: "+nbsConversionMappingList.size());
				
				for(int i=0;i<nbsConversionMappingList.size();i++){
					NBSConversionMappingDT conversionMappingDT = (NBSConversionMappingDT) nbsConversionMappingList.get(i);
					String key = "";
					if(conversionMappingDT.getFromCode()!=null)
						key = conversionMappingDT.getFromQuestionId().trim()+conversionMappingDT.getFromCode().trim();
					else
						key = conversionMappingDT.getFromQuestionId().trim();
					
					//For repeating question append answerGroupSequence number as part of key.
					if(conversionMappingDT.getAnswerGroupSeqNbr()!=null && conversionMappingDT.getAnswerGroupSeqNbr()>0)
						key = key + conversionMappingDT.getAnswerGroupSeqNbr();
					
					nbsConversionMappingMap.put(key, conversionMappingDT);
					
				}
				
			}
		}catch(Exception ex){
			logger.error("Exception thrown at loading conversion mapping: conditionCdGroupId"+conditionCdGroupId+" ,Exception: "+ex.getMessage(), ex);
			throw new NEDSSSystemException(ex.getMessage());
		}
	}
	
	/**
	 * @param mappedToQuestionList
	 * @param request
	 * @return
	 */
	private static HashMap<String, Long> loadToQuestionIdentifierAndNbsQuestionUidMap(ArrayList<Object> questionList, Long waTemplateUid, HttpServletRequest request){
		try{
			logger.debug("waTemplateUid: "+waTemplateUid);
			
			//Caching questionIdentifiers and nbsQuestionUid map, it used lookup nbs_question_uid for question_identifier.
			HashMap<String, Long> map = new HashMap<String, Long>();
			String sBeanJndiName = JNDINames.PORT_PAGE_PROXY_EJB;
			String sMethod = "getNbsQuestionUidsForQuestionIdentifiers";
			Object[] sParams = new Object[] {questionList, waTemplateUid};
			Object questionIdentifierAndNbsQuestionUidObj = CallProxyEJB.callProxyEJB(sParams, sBeanJndiName, sMethod, request.getSession());
			map  = (HashMap<String, Long>) questionIdentifierAndNbsQuestionUidObj;
			toQuestionIdentifierAndNbsQuestionUidMap.putAll(map);
			
			logger.debug("Size of toQuestionIdentifierAndNbsQuestionUidMap "+toQuestionIdentifierAndNbsQuestionUidMap.size());
			
			for (String key : toQuestionIdentifierAndNbsQuestionUidMap.keySet()) {
				 logger.debug("QuestionIdentifier as key: " + key + ", NbsQuestionUid as value: " + toQuestionIdentifierAndNbsQuestionUidMap.get(key));
			}
					
		}catch(Exception ex){
			logger.error("Exception thrown at loadToQuestionIdentifierAndNbsQuestionUidMap:"+ex.getMessage(), ex);
			throw new NEDSSSystemException(ex.getMessage());
		}
		return toQuestionIdentifierAndNbsQuestionUidMap;
	}
	
	
	private static ArrayList<Object> getMappedToQuestionList(){
		ArrayList<Object> toQuestionList = new ArrayList<Object>();
		try{
			for(Map.Entry<Object,Object> entry : nbsConversionMappingMap.entrySet()){
	            NBSConversionMappingDT conversionMappingDT = (NBSConversionMappingDT)entry.getValue();
	            if(conversionMappingDT!=null && !toQuestionList.contains(conversionMappingDT.getToQuestionId()))
	            	toQuestionList.add(conversionMappingDT.getToQuestionId());
	        }
			logger.debug("Size of toQuestionList: "+toQuestionList.size());
		}catch(Exception ex){
			logger.error("Exception thrown at getMappedToQuestionList:"+ex.getMessage(), ex);
			throw new NEDSSSystemException(ex.getMessage());
		}
		return toQuestionList;
	}
	/**
	 * 
	 */
	private static void initializeMapping(){
		nbsConversionMappingMap = new HashMap<Object,Object>();
		toQuestionIdentifierAndNbsQuestionUidMap = new HashMap<String, Long>();
	}
	
	/**
	 * @param nbsQuestionUidList
	 * @param request
	 * @return
	 */
	private static Map getQuestionIdentifiersForNbsQuestionUids(ArrayList<Object> nbsQuestionUidList, HttpServletRequest request){
		try{
			String sBeanJndiName = JNDINames.PORT_PAGE_PROXY_EJB;
	    	String sMethod = "getQuestionIdentifiersForNbsQuestionUid";
			Object[] sParams = new Object[] {nbsQuestionUidList};
			Object questionIdentifierAndNbsQuestionUidObj = CallProxyEJB.callProxyEJB(sParams, sBeanJndiName, sMethod, request.getSession());
			Map<Long, String>  questionIdentifierAndNbsQuestionUidMap  = (HashMap<Long, String>) questionIdentifierAndNbsQuestionUidObj;
			return questionIdentifierAndNbsQuestionUidMap;
		}catch(Exception ex){
			logger.error("Exception thrown getQuestionIdentifiersForNbsQuestionUids :"+ex.getMessage(), ex);
			throw new NEDSSSystemException(ex.getMessage());
		}
	}
	
	
	/**
	 * Updates NBS_SRTE.Condtion_code table
	 * @param waTemplateUid
	 * @param conditionCd
	 * @param request
	 */
	private static void updateInvFormCdForConditionCode(Long waTemplateUid, String conditionCd, HttpServletRequest request){
		try{
			logger.debug("conditionCd: "+conditionCd+", waTemplateUid: "+waTemplateUid);
			
			PageManagementActionUtil util = new PageManagementActionUtil();
			WaTemplateVO waTemplateVO =  util.getPageDetails(waTemplateUid, request.getSession());
			WaTemplateDT waTemplateDT = waTemplateVO.getWaTemplateDT();
			
			Timestamp currentTime = PortPageUtil.getCurrentTimestamp();
			
			String sBeanJndiName = JNDINames.PAM_CONVERSION_EJB;
	    	String sMethod = "updateConditionCode";
			Object[] sParams = new Object[] {conditionCd,waTemplateDT, currentTime};
			CallProxyEJB.callProxyEJB(sParams, sBeanJndiName, sMethod, request.getSession());
			
		}catch(Exception ex){
			logger.error("Exception thrown updateInvFormCdForConditionCode, conditionCd: "+conditionCd+", waTemplateUid: "+waTemplateUid+", Exception: "+ex.getMessage(), ex);
			throw new NEDSSSystemException(ex.getMessage());
		}
	}
	
	
	/**
	 * Update waTemplateUid in Page_cond_mapping table
	 * @param conditionCd
	 * @param fromPageWaTemplateUid
	 * @param toPageWaTemplateUid
	 * @param request
	 */
	private static void updateTemplateUidForConditionCode(String conditionCd, Long fromPageWaTemplateUid, Long toPageWaTemplateUid, HttpServletRequest request){
		try{
			
			logger.debug("conditionCd: "+conditionCd+", fromPageWaTemplateUid: "+fromPageWaTemplateUid+", toPageWaTemplateUid: "+toPageWaTemplateUid);
			String sBeanJndiName = JNDINames.PORT_PAGE_PROXY_EJB;
	    	String sMethod = "updatePageConditionMappingByCondtionCdAndTempalteUid";
			Object[] sParams = new Object[] {conditionCd,fromPageWaTemplateUid, toPageWaTemplateUid};
			CallProxyEJB.callProxyEJB(sParams, sBeanJndiName, sMethod, request.getSession());
			
		}catch(Exception ex){
			logger.error("Exception thrown updateTemplateUidForConditionCode,conditionCd: "+conditionCd+", fromPageWaTemplateUid: "+fromPageWaTemplateUid+", toPageWaTemplateUid: "+toPageWaTemplateUid+", Exception: "+ex.getMessage(), ex);
			throw new NEDSSSystemException(ex.getMessage());
		}
	}
	
	
	
	/**
	 * @param nbsConversionPageMgmtUid
	 * @param mappingStatusCd
	 * @param request
	 */
	public static void updateNbsConversionPageMgmt(Long nbsConversionPageMgmtUid, String mappingStatusCd, HttpServletRequest request){
		try{
			
			logger.debug("nbsConversionPageMgmtUid: "+nbsConversionPageMgmtUid+", mappingStatusCd: "+mappingStatusCd);
			String sBeanJndiName = JNDINames.PORT_PAGE_PROXY_EJB;
	    	String sMethod = "updateNbsConversionPageMgmt";
			Object[] sParams = new Object[] {nbsConversionPageMgmtUid,mappingStatusCd};
			CallProxyEJB.callProxyEJB(sParams, sBeanJndiName, sMethod, request.getSession());
			
		}catch(Exception ex){
			logger.error("Exception thrown updateNbsConversionPageMgmt nbsConversionPageMgmtUid: "+nbsConversionPageMgmtUid+", mappingStatusCd: "+mappingStatusCd+", Exception: "+ex.getMessage(), ex);
			throw new NEDSSSystemException(ex.getMessage());
		}
	}
	
	
	public static void updateNbsConversionCondition(Long nbsConversionPageMgmtUid, String statusCd, Long conditionCdGroupId, String conditionCd, HttpServletRequest request){
		try{
			
			logger.debug("nbsConversionPageMgmtUid: "+nbsConversionPageMgmtUid+", statusCd: "+statusCd+", conditionCdGroupId: "+conditionCdGroupId+", conditionCd: "+conditionCd);
			String sBeanJndiName = JNDINames.PORT_PAGE_PROXY_EJB;
	    	String sMethod = "updateNbsConversionConditionAfterPorting";
			Object[] sParams = new Object[] {nbsConversionPageMgmtUid,statusCd,conditionCdGroupId,conditionCd};
			CallProxyEJB.callProxyEJB(sParams, sBeanJndiName, sMethod, request.getSession());
			
		}catch(Exception ex){
			logger.error("Exception thrown updateNbsConversionCondition,nbsConversionPageMgmtUid: "+nbsConversionPageMgmtUid+", statusCd: "+statusCd+", conditionCdGroupId: "+conditionCdGroupId+", conditionCd: "+conditionCd+", Exception: "+ex.getMessage(), ex);
			throw new NEDSSSystemException(ex.getMessage());
		}
	}
	
	/**
	 * @return list of participation type questions from NBSQuestionMapping
	 */
	private static ArrayList<Object> getParticipationTypeQuestionsFromMapping(){
		try{
			ArrayList<Object> partTypeQuestionList= new ArrayList<Object>();
			ArrayList<Object> mappedQuestionList = new ArrayList<Object>(nbsConversionMappingMap.values());
			for(int i=0;i<nbsConversionMappingMap.size();i++){
				NBSConversionMappingDT nbsConversionMappingDT = (NBSConversionMappingDT) mappedQuestionList.get(i);
				if(PortPageUtil.DATA_TYPE_PART.equals(nbsConversionMappingDT.getFromDataType())){
					partTypeQuestionList.add(nbsConversionMappingDT);
				}
			}
			return partTypeQuestionList;
		}catch(Exception ex){
			logger.error("Exception thrown getParticipationTypeQuestionsFromMapping :"+ex.getMessage(), ex);
			throw new NEDSSSystemException(ex.getMessage());
		}
	}
	
	private static String getParticipationTypeCodeByQuestionIdentifier(String questionIdentifier){
		try{
			logger.debug("questionIdentifier: "+questionIdentifier);
			String typeCode = null;
			TreeMap<Object, Object> participationTypeCaseMap = CachedDropDowns.getParticipationTypeList();
			
			ParticipationTypeVO parTypeVO = null;
	    	Iterator parTypeIter = participationTypeCaseMap.entrySet().iterator();
	    	while (parTypeIter.hasNext()) {
	    		Map.Entry mapPair = (Map.Entry)parTypeIter.next();
	    		String keyVal = (String) mapPair.getKey();
	    		parTypeVO = (ParticipationTypeVO) mapPair.getValue();
	    		if (parTypeVO.getQuestionIdentifier().equalsIgnoreCase(questionIdentifier) && NEDSSConstants.CASE.equals(parTypeVO.getActClassCd())) {
	    			typeCode = parTypeVO.getTypeCd();
	    		}
	    	}
			return typeCode;
		}catch(Exception ex){
			logger.error("Exception thrown getParticipationTypeCodeByQuestionIdentifier questionIdentifier: "+questionIdentifier+", Exception: "+ex.getMessage(), ex);
			throw new NEDSSSystemException(ex.getMessage());
		}
		
	}
	
	
	public static ArrayList<Object> getConvertedCasesFromNbsConversionMaster(Long conditionCdGroupId, String status, HttpServletRequest request){
		try{
			logger.debug("conditionCdGroupId: "+conditionCdGroupId+", status: "+status);
			String sBeanJndiName = JNDINames.PORT_PAGE_PROXY_EJB;
	    	String sMethod = "getConvertedCasesFromNbsConversionMaster";
			Object[] sParams = new Object[] {conditionCdGroupId,status};
			Object publicHealthCaseUidObj = CallProxyEJB.callProxyEJB(sParams, sBeanJndiName, sMethod, request.getSession());
			ArrayList<Object> publicHealthCaseUidList  = (ArrayList<Object>) publicHealthCaseUidObj;
			return publicHealthCaseUidList;
		}catch(Exception ex){
			logger.error("Exception thrown getConvertedCasesFromNbsConversionMaster :"+ex.getMessage(), ex);
			throw new NEDSSSystemException(ex.getMessage());
		}
	}
	
	private static Map<Long, String> getNotConvertedPublicHealthCaseUids(String conditionCd, Long conditionCdGroupId, HttpServletRequest request){
		try{
			logger.debug("conditionCd: "+conditionCd+", conditionCdGroupId: "+conditionCdGroupId);
			//get list of public_health_case_uid associated with From Page.
			
			String sBeanJndiName = JNDINames.PORT_PAGE_PROXY_EJB;
	    	String sMethod = "getPublicHealthCaseUidsByCondition";
			Object[] sParams = new Object[] {conditionCd};
			Object publicHealthCaseUidObj = CallProxyEJB.callProxyEJB(sParams, sBeanJndiName, sMethod, request.getSession());
			Map<Long, String> phcUidAndLocalIdMap= (Map<Long, String>) publicHealthCaseUidObj;
			
			ArrayList<Object> convertedPHCUidList = getConvertedCasesFromNbsConversionMaster(conditionCdGroupId, PortPageUtil.CASE_CONVERSION_STATUS_CODE_PASS, request);
			
			for(int i=0;i<convertedPHCUidList.size();i++){
				Long phcUid = (Long) convertedPHCUidList.get(i);
				phcUidAndLocalIdMap.remove(phcUid);
			}
			return phcUidAndLocalIdMap;
		}catch(Exception ex){
			logger.error("Exception thrown getNotConvertedPublicHealthCaseUids, conditionCd: "+conditionCd+", conditionCdGroupId: "+conditionCdGroupId+", Exception: "+ex.getMessage(), ex);
			throw new NEDSSSystemException(ex.getMessage());
		}
	}
	
	public static NBSConversionConditionDT getNbsConversionConditionDTByCondition(String conditionCd, Long nbsConversionPageMgmtUid, HttpServletRequest request){
		try{
			logger.debug("nbsConversionPageMgmtUid: "+nbsConversionPageMgmtUid+", conditionCd: "+conditionCd);
			String sBeanJndiName = JNDINames.PORT_PAGE_PROXY_EJB;
	    	String sMethod = "getNBSConversionConditionDTForCondition";
			Object[] sParams = new Object[] {conditionCd,nbsConversionPageMgmtUid};
			Object object = CallProxyEJB.callProxyEJB(sParams, sBeanJndiName, sMethod, request.getSession());
			NBSConversionConditionDT nbsConvCondDT= (NBSConversionConditionDT) object;
			return nbsConvCondDT;
		}catch(Exception ex){
			logger.error("Exception thrown getNbsConversionConditionDTByCondition, nbsConversionPageMgmtUid: "+nbsConversionPageMgmtUid+", conditionCd: "+conditionCd+", Exception: "+ex.getMessage(), ex);
			throw new NEDSSSystemException(ex.getMessage());
		}
	}
	
	/**
	 * @param questionIdentiferList
	 * @param waTemplateUid
	 * @param request
	 * @return
	 */
	public static Map<String, WaUiMetadataDT> getWaUiMetadataByTemplateUidAndQuestionIdentifier(ArrayList<Object> questionIdentiferList, Long waTemplateUid, HttpServletRequest request){
		try{
			
			logger.debug("waTemplateUid: "+waTemplateUid);
			String sBeanJndiName = JNDINames.PORT_PAGE_PROXY_EJB;
	    	String sMethod = "getWaUiMetadataByTemplateUidAndQuestionIdentifier";
			Object[] sParams = new Object[] {questionIdentiferList,waTemplateUid};
			Object object = CallProxyEJB.callProxyEJB(sParams, sBeanJndiName, sMethod, request.getSession());
			Map<String, WaUiMetadataDT> questionMap= (HashMap<String, WaUiMetadataDT>) object;
			return questionMap;
		}catch(Exception ex){
			logger.error("Exception thrown getWaUiMetadataByTemplateUidAndQuestionIdentifier, waTemplateUid: "+waTemplateUid+", Exception: "+ex.getMessage(), ex);
			throw new NEDSSSystemException(ex.getMessage());
		}
	}
	
	
	/** Returns map of batch group question for page i.e. {2=[NBS_SYM_001, NBS247, GA77000]}
	 * @param waTemplateUid
	 * @param request
	 * @return
	 */
	public static Map<Integer, ArrayList<Object>> getBatchGroupQuestionsByPage(Long waTemplateUid, HttpServletRequest request){
		try{
			logger.debug("waTemplateUid: "+waTemplateUid);
			String sBeanJndiName = JNDINames.PORT_PAGE_PROXY_EJB;
	    	String sMethod = "getBatchGroupQuestionsByPage";
			Object[] sParams = new Object[] {waTemplateUid};
			Object object = CallProxyEJB.callProxyEJB(sParams, sBeanJndiName, sMethod, request.getSession());
			Map<Integer, ArrayList<Object>> batchQuestionMap= (HashMap<Integer, ArrayList<Object>>) object;
			return batchQuestionMap;
		}catch(Exception ex){
			logger.error("Exception thrown getBatchGroupQuestionsByPage waTemplateUid: "+waTemplateUid+", Exception: "+ex.getMessage(), ex);
			throw new NEDSSSystemException(ex.getMessage());
		}
	}
	
	/**
	 * @param repeatingBlockConvertedAnswers
	 * @param waTemplateUid
	 * @param request
	 */
	private static void correctAnswerGroupSeqNumberForRepeatingBlockQuestions(Map<String, NbsCaseAnswerDT> repeatingBlockConvertedAnswers, Long waTemplateUid, HttpServletRequest request){
		logger.debug("waTemplateUid: "+waTemplateUid);
		try{
			if(repeatingBlockConvertedAnswers.size()>0){
				Map<Integer, ArrayList<Object>> batchQuestionMap = getBatchGroupQuestionsByPage(waTemplateUid, request);
				for (Integer key : batchQuestionMap.keySet()) {
					int answerGroupSeqNbr=1;
					ArrayList<Object> questionGroupInBatchList = batchQuestionMap.get(key);
					int maxBlockId = findMaxBlockIdNumberForQuestion(questionGroupInBatchList);
					for(int j=1;j<=maxBlockId;j++){
						boolean updated = false;
						for(int i=0;i<questionGroupInBatchList.size();i++){
							String questionIdentifier = (String) questionGroupInBatchList.get(i);
							NbsCaseAnswerDT answerDT = repeatingBlockConvertedAnswers.get(j+"|"+questionIdentifier);
							if(answerDT!=null && answerDT.getAnswerTxt()!=null && answerDT.getAnswerTxt().length()>0){
								answerDT.setAnswerGroupSeqNbr(answerGroupSeqNbr);
								updated = true;
							}
						}
						if(updated)
							answerGroupSeqNbr++;
					}
				}
			}
			
		}catch(Exception ex){
			logger.error("Exception thrown correctAnswerGroupSeqNumberForRepeatingBlockQuestions waTemplateUid: "+waTemplateUid+", Exception: "+ex.getMessage(), ex);
			throw new NEDSSSystemException(ex.getMessage());
		}
	}
	
	/**
	 * @param questionGroupInBatchList
	 * @return
	 */
	private static int findMaxBlockIdNumberForQuestion(ArrayList questionGroupInBatchList){
		int maxBlockIdNbr = 1;
		try{
			ArrayList<Object> mappedQuestionList = new ArrayList<Object>(nbsConversionMappingMap.values());
			for(int j=0;j<questionGroupInBatchList.size();j++){
				String questionIdentifier = (String) questionGroupInBatchList.get(j);
				for(int i=0;i<nbsConversionMappingMap.size();i++){
					NBSConversionMappingDT nbsConversionMappingDT = (NBSConversionMappingDT) mappedQuestionList.get(i);
					if(questionIdentifier!=null && questionIdentifier.equals(nbsConversionMappingDT.getToQuestionId())){
						if(nbsConversionMappingDT.getBlockIdNbr()!=null && nbsConversionMappingDT.getBlockIdNbr() > maxBlockIdNbr)
							maxBlockIdNbr = nbsConversionMappingDT.getBlockIdNbr();
					}
				}
			}
			logger.debug("maxBlockIdNbr: "+maxBlockIdNbr);
			return maxBlockIdNbr;
		}catch(Exception ex){
			logger.error("Exception thrown findMaxBlockIdNumberForQuestion :"+ex.getMessage(), ex);
			throw new NEDSSSystemException(ex.getMessage());
		}
	}
	
	/**
	 * @param questionIdentifier
	 * @param nbsCaseAnswerDT
	 * @return
	 */
	private static String getMappingKey(String questionIdentifier, NbsCaseAnswerDT nbsCaseAnswerDT){
		try{
			logger.debug("questionIdentifier: "+questionIdentifier+", nbsCaseAnswerDT.getAnswerTxt(): "+nbsCaseAnswerDT.getAnswerTxt());
			String mappingKey = questionIdentifier+nbsCaseAnswerDT.getAnswerTxt();
			if(nbsCaseAnswerDT.getAnswerTxt()!=null)
				mappingKey = questionIdentifier+nbsCaseAnswerDT.getAnswerTxt().trim();
			//Find key for dropdown with Other option
			if(nbsCaseAnswerDT.getAnswerTxt().indexOf(OTHER+"^")==0){
				NBSConversionMappingDT conversionMappingDT = (NBSConversionMappingDT) nbsConversionMappingMap.get(questionIdentifier+OTHER);
				if(conversionMappingDT!=null && PortPageUtil.DATA_TYPE_CODED.equals(conversionMappingDT.getFromDataType())){
					mappingKey = questionIdentifier+OTHER;
				}
			}
			logger.debug("mappingKey:"+mappingKey);
			return mappingKey;
		}catch(Exception ex){
			logger.error("Exception thrown getMappingKey questionIdentifier: "+questionIdentifier+", Exception: "+ex.getMessage(), ex);
			throw new NEDSSSystemException(ex.getMessage());
		}
	}
	
	
	/**
	 * @param questionIdentifier
	 * @param nbsCaseAnswerDT
	 * @return
	 */
	private static String getOtherTextFromNbsCaseAnswer(String questionIdentifier, NbsCaseAnswerDT nbsCaseAnswerDT){
		try{
			logger.debug("questionIdentifier: "+questionIdentifier+", nbsCaseAnswerDT.getAnswerTxt(): "+nbsCaseAnswerDT.getAnswerTxt());
			String otherText = "";
			//Find key for dropdown with Other option
			if(nbsCaseAnswerDT.getAnswerTxt().indexOf(OTHER+"^")==0){
				NBSConversionMappingDT conversionMappingDT = (NBSConversionMappingDT) nbsConversionMappingMap.get(questionIdentifier+OTHER);
				if(conversionMappingDT!=null && PortPageUtil.DATA_TYPE_CODED.equals(conversionMappingDT.getFromDataType()) && PortPageUtil.DATA_TYPE_CODED.equals(conversionMappingDT.getToDataType())){
					otherText = nbsCaseAnswerDT.getAnswerTxt().substring(nbsCaseAnswerDT.getAnswerTxt().indexOf('^')+1);
				}
			}
			logger.debug("otherText:"+otherText);
			return otherText;
		}catch(Exception ex){
			logger.error("Exception thrown getOtherTextFromNbsCaseAnswer, questionIdentifier: "+questionIdentifier+", Exception: "+ex.getMessage(), ex);
			throw new NEDSSSystemException(ex.getMessage());
		}
	}
	
	
	/**
	 * @param conversionMappingDT
	 * @param questionIdentifier
	 * @param nbsCaseAnswerDT
	 */
	private static void setAnswerText(NBSConversionMappingDT conversionMappingDT, String questionIdentifier, NbsCaseAnswerDT nbsCaseAnswerDT){
		try{
			logger.debug("questionIdentifier: "+questionIdentifier);
			String otherText = getOtherTextFromNbsCaseAnswer( questionIdentifier,  nbsCaseAnswerDT);
			if(otherText.length()>0){
				nbsCaseAnswerDT.setAnswerTxt(conversionMappingDT.getToCode()+"^"+otherText);
			}else{
				nbsCaseAnswerDT.setAnswerTxt(conversionMappingDT.getToCode());
			}
		}catch(Exception ex){
			logger.error("Exception thrown setAnswerText, questionIdentifier: "+questionIdentifier+", Exception: "+ex.getMessage(), ex);
			throw new NEDSSSystemException(ex.getMessage());
		}
	}
}
