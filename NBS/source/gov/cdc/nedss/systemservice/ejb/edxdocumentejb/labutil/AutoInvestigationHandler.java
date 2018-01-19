package gov.cdc.nedss.systemservice.ejb.edxdocumentejb.labutil;

import gov.cdc.nedss.act.actid.dt.ActIdDT;
import gov.cdc.nedss.act.observation.vo.ObservationVO;
import gov.cdc.nedss.act.publichealthcase.dt.CaseManagementDT;
import gov.cdc.nedss.act.publichealthcase.vo.PublicHealthCaseVO;
import gov.cdc.nedss.association.dt.ParticipationDT;
import gov.cdc.nedss.entity.person.vo.PersonVO;
import gov.cdc.nedss.exception.NEDSSAppException;
import gov.cdc.nedss.exception.NEDSSConcurrentDataException;
import gov.cdc.nedss.exception.NEDSSSystemException;
import gov.cdc.nedss.nbsactentity.dt.NbsActEntityDT;
import gov.cdc.nedss.page.ejb.pageproxyejb.vo.act.PageActProxyVO;
import gov.cdc.nedss.pam.vo.PamVO;
import gov.cdc.nedss.proxy.ejb.pamproxyejb.vo.PamProxyVO;
import gov.cdc.nedss.systemservice.ejb.edxdocumentejb.dt.EdxLabInformationDT;
import gov.cdc.nedss.systemservice.ejb.edxdocumentejb.dt.EdxRuleManageDT;
import gov.cdc.nedss.systemservice.nbssecurity.NBSSecurityObj;
import gov.cdc.nedss.systemservice.nbssecurity.ProgramAreaVO;
import gov.cdc.nedss.util.LogUtils;
import gov.cdc.nedss.util.NEDSSConstants;
import gov.cdc.nedss.util.PropertyUtil;
import gov.cdc.nedss.webapp.nbs.action.util.RulesEngineUtil;
import gov.cdc.nedss.webapp.nbs.helper.CachedDropDowns;
import gov.cdc.nedss.webapp.nbs.logicsheet.helper.CachedDropDownValues;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.Map;

import javax.ejb.EJBException;
/**
 * 
 * @author Pradeep Kumar Sharma
 *
 */
public class AutoInvestigationHandler {
	static final LogUtils logger = new LogUtils(AutoInvestigationHandler.class.getName());
	
	private static PublicHealthCaseVO createPublicHealthCaseVO(ObservationVO observationVO, EdxLabInformationDT edxLabInformationDT,  NBSSecurityObj securityObj){
		PublicHealthCaseVO phcVO = new PublicHealthCaseVO();
		
		phcVO.getThePublicHealthCaseDT().setLastChgTime(new java.sql.Timestamp(new Date().getTime()));
		
		phcVO.getThePublicHealthCaseDT().setPublicHealthCaseUid(new Long(edxLabInformationDT.getNextUid()-1));
		//edxLabInformationDT.setNextUid(edxLabInformationDT.getNextUid());
		phcVO.getThePublicHealthCaseDT().setJurisdictionCd((observationVO.getTheObservationDT().getJurisdictionCd()));
		phcVO.getThePublicHealthCaseDT().setRptFormCmpltTime(observationVO.getTheObservationDT().getRptToStateTime());
		//phcVO.getThePublicHealthCaseDT().setCaseClassCd(EdxELRConstants.ELR_CONFIRMED_CD);
		
		phcVO.getThePublicHealthCaseDT().setAddTime(new Timestamp(new Date().getTime()));
		phcVO.getThePublicHealthCaseDT().setAddUserId(Long.valueOf(securityObj.getEntryID()));
		phcVO.getThePublicHealthCaseDT().setCaseTypeCd(EdxELRConstants.ELR_INDIVIDUAL);
		ProgramAreaVO programAreaVO= CachedDropDowns.getProgramAreaForCondition(edxLabInformationDT.getConditionCode());
		phcVO.getThePublicHealthCaseDT().setCd(programAreaVO.getConditionCd());
		phcVO.getThePublicHealthCaseDT().setProgAreaCd(programAreaVO.getStateProgAreaCode());
		phcVO.getThePublicHealthCaseDT().setSharedInd(NEDSSConstants.TRUE);
		phcVO.getThePublicHealthCaseDT().setCdDescTxt(programAreaVO.getConditionShortNm());
		phcVO.getThePublicHealthCaseDT().setGroupCaseCnt(new Integer(1));
		phcVO.getThePublicHealthCaseDT().setInvestigationStatusCd(EdxELRConstants.ELR_OPEN_CD);
		phcVO.getThePublicHealthCaseDT().setActivityFromTime(new Timestamp(new Date().getTime()));
		Calendar now = Calendar.getInstance();
		String dateValue = (now.get(Calendar.MONTH)+1) +"/" + now.get(Calendar.DATE) +"/" + now.get(Calendar.YEAR);
		RulesEngineUtil reu = new RulesEngineUtil();
    	int[] weekAndYear = reu.CalcMMWR(dateValue);
		phcVO.getThePublicHealthCaseDT().setMmwrWeek(weekAndYear[0]+"");
		phcVO.getThePublicHealthCaseDT().setMmwrYear(weekAndYear[1]+"");
		phcVO.getThePublicHealthCaseDT().setStatusCd(EdxELRConstants.ELR_ACTIVE_CD);
		if (edxLabInformationDT.getConditionCode() != null) {
			phcVO.setCoinfectionCondition(CachedDropDowns.getConditionCoinfectionMap().containsKey(edxLabInformationDT.getConditionCode())? true:false);
			if (phcVO.isCoinfectionCondition()) {
				logger.debug("AutoInvestigationHandler.createPublicHealthCaseVO set flag to create an new coinfection id for the case");
				phcVO.getThePublicHealthCaseDT().setCoinfectionId(NEDSSConstants.COINFCTION_GROUP_ID_NEW_CODE);
			}
		}
		phcVO.getThePublicHealthCaseDT().setItNew(true);
		phcVO.getThePublicHealthCaseDT().setItDirty(false);
		phcVO.setItNew(true);
		phcVO.setItDirty(false);
		
		try{
			boolean isSTDProgramArea = PropertyUtil.isStdOrHivProgramArea(phcVO.getThePublicHealthCaseDT().getProgAreaCd());
			if (isSTDProgramArea) {
				//gt-ND-4592 - STD_HIV_DATAMART Fails To Populate Investigations Created From An ELR
				// per Pradeep need an empty case mgt
				CaseManagementDT caseMgtDT = new CaseManagementDT();
				caseMgtDT.setPublicHealthCaseUid(phcVO.getThePublicHealthCaseDT().getPublicHealthCaseUid());
				//caseMgtDT.setItNew(true); //not currently used
				//caseMgtDT.setItDirty(false); //not currently used
				caseMgtDT.setCaseManagementDTPopulated(true);
				phcVO.setTheCaseManagementDT(caseMgtDT);
			}
		} catch(Exception ex){
				logger.error("Exception setting CaseManagementDT to PHC = "+ex.getMessage(), ex);
				throw new NEDSSSystemException("Unexpected exception setting CaseManagementDT to PHC -->" +ex.toString());
		}
		
		return phcVO;
	} 
	public  static Object autoCreateInvestigation( ObservationVO observationVO, EdxLabInformationDT edxLabInformationDT, NBSSecurityObj securityObj) throws NEDSSAppException{
		PageActProxyVO pageActProxyVO = null;
		PamProxyVO pamProxyVO = null;
		PublicHealthCaseVO phcVO=createPublicHealthCaseVO(observationVO, edxLabInformationDT,  securityObj);
		Collection<Object> theActIdDTCollection = new ArrayList<Object>();
			ActIdDT actIDDT = new ActIdDT();
			actIDDT.setItNew(true);
			actIDDT.setActIdSeq(new Integer(1));
			actIDDT.setTypeCd(NEDSSConstants.ACT_ID_STATE_TYPE_CD);
			theActIdDTCollection.add(actIDDT);
			phcVO.setTheActIdDTCollection(theActIdDTCollection);
		if(phcVO.getThePublicHealthCaseDT().getCd().equalsIgnoreCase(NEDSSConstants.TUBERCULOSIS_KEY)){
			ActIdDT actID1DT = new ActIdDT();
			actID1DT.setItNew(true);
			actID1DT.setActIdSeq(new Integer(2));
			actID1DT.setTypeCd("CITY");
			theActIdDTCollection.add(actID1DT);
			phcVO
					.setTheActIdDTCollection(theActIdDTCollection);
		}
		if (edxLabInformationDT.getConditionCode().equalsIgnoreCase(NEDSSConstants.VARICELLA_KEY)
				|| edxLabInformationDT.getConditionCode().equalsIgnoreCase(NEDSSConstants.TUBERCULOSIS_KEY)) {
			pamProxyVO = new PamProxyVO();
			pamProxyVO.setItNew(true);
			pamProxyVO.setItDirty(false);
			pamProxyVO.setPublicHealthCaseVO(phcVO);
		} else {
			pageActProxyVO = new PageActProxyVO();
			pageActProxyVO.setItNew(true);
			pageActProxyVO.setItDirty(false);
			pageActProxyVO.setPublicHealthCaseVO(phcVO);
		}
		try {
			Object obj=null;
			
			//Object obj = transferValuesTOActProxyVO(pageActProxyVO,pamProxyVO, personVOCollection, observationVO);
			if(pageActProxyVO!=null)	
				obj=pageActProxyVO;
			else
				obj=pamProxyVO;
			return obj;
		} catch (NEDSSSystemException e) {
			logger.error("AutoInvestigationHandler-autoCreateInvestigation NEDSSSystemException raised"+e);
			throw new NEDSSAppException("AutoInvestigationHandler-autoCreateInvestigation NEDSSSystemException raised"+e);
		} catch (ClassCastException e) {
			logger.error("AutoInvestigationHandler-autoCreateInvestigation ClassCastException raised"+e);
			throw new NEDSSAppException("AutoInvestigationHandler-autoCreateInvestigation ClassCastException raised"+e);
		}  catch (EJBException e) {
			logger.error("AutoInvestigationHandler-autoCreateInvestigation EJBException raised"+e);
			throw new NEDSSAppException("AutoInvestigationHandler-autoCreateInvestigation EJBException raised"+e);
		}
	
	}
	
	public static Object transferValuesTOActProxyVO(PageActProxyVO pageActProxyVO,PamProxyVO pamActProxyVO, Collection<Object> personVOCollection, ObservationVO rootObservationVO, Collection<Object> entities, Map<Object, Object> questionIdentifierMap) throws NEDSSAppException{
		try {
			PersonVO patientVO =null;
			boolean isOrgAsReporterOfPHCPartDT=false;
			boolean isPhysicianOfPHCDT=false;
			Collection<Object> coll = rootObservationVO.getTheParticipationDTCollection();
			Collection<Object> partColl = new ArrayList<Object>();
			Collection<Object> nbsActEntityDTColl = new ArrayList<Object>();
			long personUid=-1;
			if(pageActProxyVO!=null)
				personUid=pageActProxyVO.getPublicHealthCaseVO().getThePublicHealthCaseDT().getPublicHealthCaseUid()-1;
			else{
				personUid=pamActProxyVO.getPublicHealthCaseVO().getThePublicHealthCaseDT().getPublicHealthCaseUid()-1;
			}
			if(personVOCollection!=null){
				Iterator<Object> it=personVOCollection.iterator();
				while (it.hasNext()){
					PersonVO personVO=(PersonVO)it.next();
					if(personVO.getThePersonDT().getCd().equals("PAT")){
						patientVO= personVO;
						patientVO.getThePersonDT().setPersonUid(new Long(personUid));
						Collection<Object> thePersonVOCollection = new ArrayList<Object>();
						thePersonVOCollection.add(patientVO);
						patientVO.setItNew(true);
						patientVO.setItDirty(false);
						patientVO.getThePersonDT().setItDirty(false);
						patientVO.getThePersonDT().setItNew(true);
						personVO.getThePersonDT().setElectronicInd(NEDSSConstants.ELECTRONIC_IND);
						personVO.getThePersonDT().setStatusTime(new Timestamp(new Date().getTime()));
						if(pageActProxyVO!=null)
							pageActProxyVO.setThePersonVOCollection(thePersonVOCollection);
						else{
							pamActProxyVO.setThePersonVOCollection(thePersonVOCollection);
						}
						break;
					}
				}
			}
			if(entities!=null && entities.size()>0){
				Iterator iterator = entities.iterator();
				while(iterator.hasNext()){
					EdxRuleManageDT edxRuleManageDT =(EdxRuleManageDT)iterator.next();
					ParticipationDT participationDT = new ParticipationDT();
					participationDT.setTypeCd(edxRuleManageDT.getParticipationTypeCode());
					participationDT.setSubjectEntityUid(edxRuleManageDT.getParticipationUid());
					participationDT.setSubjectClassCd(edxRuleManageDT.getParticipationClassCode());
					if(participationDT.getTypeCd().equals("OrgAsReporterOfPHC")){
						isOrgAsReporterOfPHCPartDT=true;
					}else if(participationDT.getTypeCd().equals("PhysicianOfPHC")){
						isPhysicianOfPHCDT=true;
					}
					
					createActEntityObject(participationDT,pageActProxyVO,pamActProxyVO,nbsActEntityDTColl,partColl);
				}
			}
			if(coll!=null){
				
				Iterator<Object> it=coll.iterator();
				while (it.hasNext()){
					ParticipationDT partDT = (ParticipationDT)it.next();
					boolean createActEntity=false;
					String typeCd =partDT.getTypeCd();
					if(typeCd.equalsIgnoreCase(EdxELRConstants.ELR_AUTHOR_CD)&& partDT.getSubjectClassCd().equals(EdxELRConstants.ELR_ORG) && !isOrgAsReporterOfPHCPartDT){
						createActEntity=true;
						partDT.setTypeCd("OrgAsReporterOfPHC");
					}
					if(typeCd.equalsIgnoreCase(EdxELRConstants.ELR_ORDER_CD)&& partDT.getSubjectClassCd().equals(EdxELRConstants.ELR_PERSON_CD) && !isPhysicianOfPHCDT ){
						createActEntity=true;
						partDT.setTypeCd("PhysicianOfPHC");
					}
					//gst- ND-4326 Physician not getting populated..
					if(typeCd.equalsIgnoreCase(EdxELRConstants.ELR_ORDERER_CD) && partDT.getSubjectClassCd().equals(EdxELRConstants.ELR_PERSON_CD) && !isPhysicianOfPHCDT ){
						createActEntity=true;
						partDT.setTypeCd("PhysicianOfPHC");
					}
					//Transfer the ordering facility over if it is on the PageBuilder page
					if(typeCd.equalsIgnoreCase(EdxELRConstants.ELR_ORDERER_CD)&& 
							partDT.getSubjectClassCd().equals(EdxELRConstants.ELR_ORG) &&
							partDT.getCd().equals(EdxELRConstants.ELR_OP_CD) &&
							questionIdentifierMap != null &&
							questionIdentifierMap.containsKey("NBS291")){
						createActEntity=true;
						partDT.setTypeCd("OrgAsClinicOfPHC");
					}					
					if(typeCd.equalsIgnoreCase(EdxELRConstants.ELR_PATIENT_SUBJECT_CD) ){
						createActEntity=true;
						partDT.setTypeCd("SubjOfPHC");
						partDT.setSubjectEntityUid(personUid);
					}
					if(createActEntity){
						createActEntityObject(partDT,pageActProxyVO,pamActProxyVO,nbsActEntityDTColl,partColl);
					}
					
				}
					
			}
			
			if(pageActProxyVO!=null){
				pageActProxyVO.setTheParticipationDTCollection(partColl);
				PamVO pamVO = null;
				if(pageActProxyVO.getPageVO()!=null)
					pamVO=pageActProxyVO.getPageVO();
				else
					pamVO = new PamVO();
				pamVO.setActEntityDTCollection(nbsActEntityDTColl);
				pageActProxyVO.setPageVO(pamVO);
				return pageActProxyVO;
			}
			else{
				pamActProxyVO.setTheParticipationDTCollection(partColl);
				PamVO pamVO = null;
				if(pamActProxyVO.getPamVO()!=null)
					pamVO=pamActProxyVO.getPamVO();
				else
					pamVO = new PamVO();
				pamVO.setActEntityDTCollection(nbsActEntityDTColl);
				pamActProxyVO.setPamVO(pamVO);
				return pamActProxyVO;
			}
				
		} catch (Exception e) {
			logger.error("AutoInvestigationHandler-transferValuesTOActProxyVO Exception raised"+e);
			throw new NEDSSAppException("AutoInvestigationHandler-transferValuesTOActProxyVO Exception raised"+e);
		}
	}
	
	
	private static void createActEntityObject(ParticipationDT partDT, PageActProxyVO pageActProxyVO, PamProxyVO pamActProxyVO, Collection<Object> nbsActEntityDTColl, Collection<Object> partColl ){
		CachedDropDownValues srtc = new CachedDropDownValues();
		
		partDT.setActClassCd(NEDSSConstants.CLASS_CD_CASE);
		if(pageActProxyVO!=null)
			partDT.setActUid(pageActProxyVO.getPublicHealthCaseVO().getThePublicHealthCaseDT().getPublicHealthCaseUid());
		else
			partDT.setActUid(pamActProxyVO.getPublicHealthCaseVO().getThePublicHealthCaseDT().getPublicHealthCaseUid());
		//partDT.setTypeCd(typeCd.trim());
		partDT.setTypeDescTxt(srtc.getDescForCode("PAR_TYPE", partDT.getTypeCd()));
		partDT.setStatusCd(NEDSSConstants.STATUS_ACTIVE);
		partDT.setRecordStatusCd(NEDSSConstants.RECORD_STATUS_ACTIVE);
		partDT.setStatusTime(new java.sql.Timestamp(new Date().getTime()));
		partDT.setItNew(true);
		partDT.setItDirty(false);
		partColl.add(partDT);
		
		NbsActEntityDT nbsActEntityDT = new NbsActEntityDT();

		if(pageActProxyVO!=null){
			nbsActEntityDT.setAddTime(pageActProxyVO.getPublicHealthCaseVO().getThePublicHealthCaseDT().getAddTime());
			nbsActEntityDT.setLastChgTime(pageActProxyVO.getPublicHealthCaseVO().getThePublicHealthCaseDT().getLastChgTime());
			nbsActEntityDT.setLastChgUserId(pageActProxyVO.getPublicHealthCaseVO().getThePublicHealthCaseDT().getLastChgUserId());
			nbsActEntityDT.setRecordStatusCd(pageActProxyVO.getPublicHealthCaseVO().getThePublicHealthCaseDT().getRecordStatusCd());
			nbsActEntityDT.setAddUserId(pageActProxyVO.getPublicHealthCaseVO().getThePublicHealthCaseDT().getAddUserId());
		}else{
			nbsActEntityDT.setAddTime(pamActProxyVO.getPublicHealthCaseVO().getThePublicHealthCaseDT().getAddTime());
			nbsActEntityDT.setLastChgTime(pamActProxyVO.getPublicHealthCaseVO().getThePublicHealthCaseDT().getLastChgTime());
			nbsActEntityDT.setLastChgUserId(pamActProxyVO.getPublicHealthCaseVO().getThePublicHealthCaseDT().getLastChgUserId());
			nbsActEntityDT.setRecordStatusCd(pamActProxyVO.getPublicHealthCaseVO().getThePublicHealthCaseDT().getRecordStatusCd());
			nbsActEntityDT.setAddUserId(pamActProxyVO.getPublicHealthCaseVO().getThePublicHealthCaseDT().getAddUserId());
		}
		nbsActEntityDT.setActUid(partDT.getActUid());
		nbsActEntityDT.setEntityUid(partDT.getSubjectEntityUid());
		nbsActEntityDT.setEntityVersionCtrlNbr(new Integer(1));
		nbsActEntityDT.setRecordStatusTime(partDT.getRecordStatusTime());
		nbsActEntityDT.setTypeCd(partDT.getTypeCd());
		nbsActEntityDT.setItNew(true);
		nbsActEntityDT.setItDirty(false);
		nbsActEntityDTColl.add(nbsActEntityDT);
	
	}
}
