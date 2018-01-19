package gov.cdc.nedss.systemservice.ejb.nbsdocumentejb.util;

import gov.cdc.nedss.alert.ejb.alertejb.Alert;
import gov.cdc.nedss.alert.ejb.alertejb.AlertHome;
import gov.cdc.nedss.association.dao.ParticipationDAOImpl;
import gov.cdc.nedss.association.dt.ParticipationDT;
import gov.cdc.nedss.controller.ejb.entitycontrollerejb.bean.EntityController;
import gov.cdc.nedss.entity.entityid.dt.EntityIdDT;
import gov.cdc.nedss.entity.person.dt.PersonDT;
import gov.cdc.nedss.entity.person.dt.PersonEthnicGroupDT;
import gov.cdc.nedss.entity.person.dt.PersonNameDT;
import gov.cdc.nedss.entity.person.dt.PersonRaceDT;
import gov.cdc.nedss.entity.person.vo.PersonRaceVO;
import gov.cdc.nedss.entity.person.vo.PersonVO;
import gov.cdc.nedss.exception.NEDSSConcurrentDataException;
import gov.cdc.nedss.exception.NEDSSException;
import gov.cdc.nedss.exception.NEDSSSystemException;
import gov.cdc.nedss.locator.dt.EntityLocatorParticipationDT;
import gov.cdc.nedss.locator.dt.PostalLocatorDT;
import gov.cdc.nedss.locator.dt.TeleLocatorDT;
import gov.cdc.nedss.nnd.ejb.nndmessageprocessorejb.helper.MessageBuilderHelper;
import gov.cdc.nedss.page.ejb.pageproxyejb.dt.NbsAnswerDT;
import gov.cdc.nedss.phdc.CaseType;
import gov.cdc.nedss.phdc.CodedType;
import gov.cdc.nedss.phdc.CommonQuestionsType;
import gov.cdc.nedss.phdc.ContainerDocument;
import gov.cdc.nedss.phdc.ContainerDocument.Container;
import gov.cdc.nedss.phdc.HeaderType;
import gov.cdc.nedss.phdc.HierarchicalDesignationType;
import gov.cdc.nedss.phdc.IdentifierType;
import gov.cdc.nedss.phdc.IdentifiersType;
import gov.cdc.nedss.phdc.NameType;
import gov.cdc.nedss.phdc.NumericType;
import gov.cdc.nedss.phdc.PatientType;
import gov.cdc.nedss.phdc.PostalAddressType;
import gov.cdc.nedss.phdc.TelephoneType;
import gov.cdc.nedss.systemservice.dt.NBSDocumentMetadataDT;
import gov.cdc.nedss.systemservice.dt.NbsInterfaceDT;
import gov.cdc.nedss.systemservice.ejb.casenotificationejb.dao.CaseNotificationDAOImpl;
import gov.cdc.nedss.systemservice.ejb.edxdocumentejb.cdautil.EdxCDAConstants;
import gov.cdc.nedss.systemservice.ejb.edxdocumentejb.dt.EdxCDAInformationDT;
import gov.cdc.nedss.systemservice.ejb.edxdocumentejb.dt.EdxPatientMatchDT;
import gov.cdc.nedss.systemservice.ejb.edxdocumentejb.util.EdxPatientMatchingCriteriaUtil;
import gov.cdc.nedss.systemservice.ejb.jurisdictionejb.bean.Jurisdiction;
import gov.cdc.nedss.systemservice.ejb.jurisdictionejb.bean.JurisdictionHome;
import gov.cdc.nedss.systemservice.ejb.nbsdocumentejb.dao.NbsDocumentDAOImpl;
import gov.cdc.nedss.systemservice.ejb.nbsdocumentejb.dt.EDXEventProcessCaseSummaryDT;
import gov.cdc.nedss.systemservice.ejb.nbsdocumentejb.dt.EDXEventProcessDT;
import gov.cdc.nedss.systemservice.ejb.nbsdocumentejb.dt.NBSDocumentDT;
import gov.cdc.nedss.systemservice.ejb.nbsdocumentejb.vo.NBSDocumentVO;
import gov.cdc.nedss.systemservice.ejb.srtcachemanagerejb.dt.SRTGenericCodeDT;
import gov.cdc.nedss.systemservice.ejb.srtmapejb.dao.SRTMapDAOImpl;
import gov.cdc.nedss.systemservice.nbssecurity.NBSSecurityObj;
import gov.cdc.nedss.systemservice.nbssecurity.ProgramAreaJurisdictionUtil;
import gov.cdc.nedss.systemservice.nbssecurity.ProgramAreaVO;
import gov.cdc.nedss.util.JNDINames;
import gov.cdc.nedss.util.LogUtils;
import gov.cdc.nedss.util.NEDSSConstants;
import gov.cdc.nedss.util.NedssUtils;
import gov.cdc.nedss.util.PropertyUtil;
import gov.cdc.nedss.util.StringUtils;
import gov.cdc.nedss.webapp.nbs.helper.CachedDropDowns;

import java.lang.reflect.Constructor;
import java.rmi.RemoteException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.TreeMap;

import javax.ejb.CreateException;
import javax.ejb.EJBException;
import javax.rmi.PortableRemoteObject;

import org.apache.xmlbeans.XmlOptions;

/**Utlility class for generating NBS Specific objects
 * @version 4.2
 * @author Pradeep Kumar Sharma
 *
 */
public class XMLTypeToNBSObject {
	
	static final LogUtils logger = new LogUtils(XMLTypeToNBSObject.class.getName());
    PropertyUtil util = PropertyUtil.getInstance();

	public NBSDocumentDT getNBSDocumentDT(NbsInterfaceDT nbsInterfaceDT) {
		NBSDocumentDT nbsDocumentDT = new NBSDocumentDT();
		nbsDocumentDT.setDocPayload(nbsInterfaceDT.getPayload());

		return nbsDocumentDT;
	}

	
	public PersonVO getPatientVO(PatientType patientType, Timestamp time,Long userId, boolean isForCreate) {
		MessageBuilderHelper messageBuilderHelper = new MessageBuilderHelper();

		PersonVO personVO = new PersonVO();
		NameType nameType = patientType.getName();

		// PersonVO
		personVO.getThePersonDT().setCd(NEDSSConstants.PAT);
		personVO.setItDirty(false);
		personVO.getThePersonDT().setItNew(true);
		personVO.getThePersonDT().setItDirty(false);
		personVO.setItNew(true);

		// PersonDT
		personVO.getThePersonDT().setVersionCtrlNbr(new Integer(1));
		personVO.getThePersonDT().setItNew(true);
		personVO.getThePersonDT().setLastChgTime(time);
		personVO.getThePersonDT().setAddTime(time);
		personVO.getThePersonDT().setLastChgUserId(userId);
		personVO.getThePersonDT().setAddUserId(userId);
		personVO.getThePersonDT().setRecordStatusCd(NEDSSConstants.ACTIVE);

		if (nameType != null) {
			personVO.getThePersonDT().setFirstNm(nameType.getFirst());
			personVO.getThePersonDT().setMiddleNm(nameType.getMiddle());
			personVO.getThePersonDT().setLastNm(nameType.getLast());
		}

		if (isForCreate)
			personVO.getThePersonDT().setPersonUid(new Long(-1));

		java.util.Date dobDate = null;

		if (patientType.getDateOfBirth() != null) {
			dobDate = patientType.getDateOfBirth().getTime();
		}

		if (dobDate != null) {
			Timestamp dobTimestamp = new Timestamp(dobDate.getTime());
			personVO.getThePersonDT().setBirthTime(dobTimestamp);
		}

		NumericType patientAge = patientType.getReportedAge();

		if (patientAge != null && patientType.getReportedAge() != null
				&& patientAge.getValue1() > -1)
		{
			int age = (int)patientAge.getValue1();
			personVO.getThePersonDT().setAgeReported(age + "");
		}
		if(patientType.getDeceasedIndicator()!=null && patientType.getDeceasedIndicator().getCode()!=null){
			personVO.getThePersonDT().setDeceasedIndCd(patientType.getDeceasedIndicator().getCode());
			personVO.getThePersonDT().setAsOfDateMorbidity(time);
		}
		if(patientType.getDeceasedDate()!=null)
			personVO.getThePersonDT().setDeceasedTime(new Timestamp(patientType.getDeceasedDate().getTimeInMillis()));
		if (patientAge != null && patientAge.getUnit() != null) {
			try {
				SRTGenericCodeDT srtAgeCode = messageBuilderHelper
						.reversePHINToNBSCodeTranslation("PHVS_AGE_UNIT",
								patientAge.getUnit().getCode(), "Standard_XREF");
				if (srtAgeCode != null)
					personVO.getThePersonDT().setAgeReportedUnitCd(
							srtAgeCode.getCode());
			} catch (Exception e) {
				logger.error("there exists no reported age code associated with the message " +
						"in Standard_XREF table for code set nm PHVS_AGE_UNIT ");
				
			}
		}

		if (patientType.getSex() != null) {
			personVO.getThePersonDT().setCurrSexCd(patientType.getSex().getCode());
		}

		// Person Race
		/*
		 * At max we can parse only one race code at a time and as per Christi
		 * we will get only one top level race category group
		 */
		ArrayList<Object> thePersonRaceDTCollection = new ArrayList<Object>();
		if (patientType.getRaceArray() != null) {
			for (int i = 0; i < patientType.getRaceArray().length; i++) {
				PersonRaceDT personRaceDT = new PersonRaceDT();
				CodedType codedType = patientType.getRaceArray()[i];
				personRaceDT.setRaceCd(codedType.getCode());
				personRaceDT.setAddTime(time);
				personRaceDT.setAddUserId(userId);
				personRaceDT.setAsOfDate(time);
				personRaceDT.setItNew(true);
				personRaceDT.setItDirty(false);
				personRaceDT
						.setRecordStatusCd(NEDSSConstants.RECORD_STATUS_ACTIVE);
				PersonRaceVO raceVO = CachedDropDowns
						.getRaceAndCategoryCode(codedType.getCode());
				personRaceDT.setRaceCategoryCd(raceVO.getRaceCategoryCd());
				if (isForCreate && raceVO.getRaceCd() != null
						&& raceVO.getRaceCategoryCd() != null) {
					personRaceDT.setPersonUid(personVO.getThePersonDT()
							.getPersonUid());
					thePersonRaceDTCollection.add(personRaceDT);
				}
			}
		}

		if (thePersonRaceDTCollection != null
				&& thePersonRaceDTCollection.size() > 0) {
			personVO.setThePersonRaceDTCollection(thePersonRaceDTCollection);
		}

		// Person Ethnicity
		ArrayList<Object> thePersonEthnicGroupDTCollection = new ArrayList<Object>();

		PersonEthnicGroupDT personEthnicGroupDT = new PersonEthnicGroupDT();
		CodedType codedType = patientType.getEthnicity();
		if (codedType != null) {
			personEthnicGroupDT.setEthnicGroupCd(codedType.getCode());
			personEthnicGroupDT.setEthnicGroupDescTxt(codedType
					.getCodeDescTxt());
			personEthnicGroupDT.setRecordStatusCd(NEDSSConstants.RECORD_STATUS_ACTIVE);
			personEthnicGroupDT.setRecordStatusTime(time);
			personEthnicGroupDT.setAddTime(time);
			personEthnicGroupDT.setAddUserId(userId);
			if (isForCreate)
				personEthnicGroupDT.setPersonUid(personVO.getThePersonDT()
						.getPersonUid());
			thePersonEthnicGroupDTCollection.add(personEthnicGroupDT);
		}

		personVO.setThePersonEthnicGroupDTCollection(thePersonEthnicGroupDTCollection);

		CodedType ethnicCodedType = patientType.getEthnicity();

		if (ethnicCodedType != null) {
			personVO.getThePersonDT().setEthnicGroupInd(
					ethnicCodedType.getCode());
			personVO.getThePersonDT().setAsOfDateEthnicity(time);
		}

		// Person Name
		PersonNameDT personNameDT = new PersonNameDT();
		personNameDT.setItNew(true);
		personNameDT.setItDirty(false);
		personNameDT.setAddTime(new Timestamp(new Date().getTime()));
		personNameDT.setAddUserId(userId);
		personNameDT.setAsOfDate(time);
		personNameDT.setNmUseCd(NEDSSConstants.LEGAL_NAME);
		personNameDT.setPersonNameSeq(new Integer(1));
		personNameDT.setStatusTime(new Timestamp(new Date().getTime()));
		personNameDT.setRecordStatusTime(new Timestamp(new Date().getTime()));
		personNameDT.setPersonUid(personVO.getThePersonDT().getPersonUid());
		personNameDT.setRecordStatusCd(NEDSSConstants.ACTIVE);
		personNameDT.setStatusCd(NEDSSConstants.STATUS_ACTIVE);

		if (nameType != null) {
			personNameDT.setFirstNm(nameType.getFirst());
			personNameDT.setLastNm(nameType.getLast());
			personNameDT.setMiddleNm(nameType.getMiddle());
			personNameDT.setAsOfDate(time);
		}

		Collection<Object> thePersonNameDTCollection = new ArrayList<Object>();
		thePersonNameDTCollection.add(personNameDT);

		if (isForCreate)
			personNameDT.setPersonUid(personVO.getThePersonDT().getPersonUid());

		personNameDT.setAsOfDate(time);

		if (thePersonNameDTCollection != null
				&& thePersonNameDTCollection.size() > 0) {
			personVO.setThePersonNameDTCollection(thePersonNameDTCollection);
		}

		Collection<Object> elPColl = new ArrayList<Object>();

		// Postal LocatorDT
		PostalAddressType postalLocatorType = patientType.getPostalAddress();

		if (postalLocatorType != null) {
			PostalLocatorDT thePostalLocatorDT = new PostalLocatorDT();
			thePostalLocatorDT.setStreetAddr1(postalLocatorType.getStreetAddressOne());
			thePostalLocatorDT.setStreetAddr2(postalLocatorType.getStreetAddressTwo());
			thePostalLocatorDT.setCityDescTxt(postalLocatorType.getCity());
			if (postalLocatorType.getCounty() != null)
				thePostalLocatorDT.setCntyCd(postalLocatorType.getCounty().getCode());
			if (postalLocatorType.getState() != null)
				thePostalLocatorDT.setStateCd(postalLocatorType.getState().getCode());
			if (postalLocatorType.getCountry() != null){
				thePostalLocatorDT.setCntryCd(getNbsCode("DEM167",postalLocatorType.getCountry(),	new Long(3560)));
			}
		
			thePostalLocatorDT.setRecordStatusCd(NEDSSConstants.RECORD_STATUS_ACTIVE);
			thePostalLocatorDT.setRecordStatusTime(time);
			thePostalLocatorDT.setZipCd(postalLocatorType.getZipCode());
			EntityLocatorParticipationDT entityPostalLocatorDT = 
					new EntityLocatorParticipationDT();
			entityPostalLocatorDT.setItNew(true);
			entityPostalLocatorDT.setItDirty(false);
			entityPostalLocatorDT.setAddTime(time);
			entityPostalLocatorDT.setAddUserId(userId);
			entityPostalLocatorDT.setStatusCd(NEDSSConstants.STATUS_ACTIVE);
			entityPostalLocatorDT.setRecordStatusCd(NEDSSConstants.RECORD_STATUS_ACTIVE);
			entityPostalLocatorDT.setEntityUid(personVO.getThePersonDT().getPersonUid());
			entityPostalLocatorDT.setCd(NEDSSConstants.HOME);
			entityPostalLocatorDT.setClassCd(NEDSSConstants.POSTAL);
			entityPostalLocatorDT.setUseCd(NEDSSConstants.HOME);
			entityPostalLocatorDT.setThePostalLocatorDT(thePostalLocatorDT);
			entityPostalLocatorDT.setAsOfDate(time);
			elPColl.add(entityPostalLocatorDT);
		}

		// TeleLoctorDT
		TelephoneType telephoneLocatorType = patientType.getTelephone();

		if (telephoneLocatorType != null) {
			EntityLocatorParticipationDT entityTeleLocatorDT = new EntityLocatorParticipationDT();
			entityTeleLocatorDT.setItNew(true);
			entityTeleLocatorDT.setItDirty(false);
			entityTeleLocatorDT.setAddTime(new Timestamp(new Date().getTime()));
			entityTeleLocatorDT.setAddUserId(userId);
			entityTeleLocatorDT.setEntityUid(personVO.getThePersonDT()
					.getPersonUid());
			entityTeleLocatorDT.setClassCd(NEDSSConstants.TELE);
			entityTeleLocatorDT.setCd(NEDSSConstants.PHONE);
			entityTeleLocatorDT.setUseCd(NEDSSConstants.HOME);
			entityTeleLocatorDT
					.setRecordStatusCd(NEDSSConstants.RECORD_STATUS_ACTIVE);
			entityTeleLocatorDT.setStatusCd(NEDSSConstants.STATUS_ACTIVE);
			entityTeleLocatorDT.setAsOfDate(time);
			TeleLocatorDT homeTeleLocatorDT = new TeleLocatorDT();
			homeTeleLocatorDT.setPhoneNbrTxt(telephoneLocatorType.getNumber());
			homeTeleLocatorDT.setExtensionTxt(telephoneLocatorType
					.getExtension());
			homeTeleLocatorDT.setItNew(false);
			homeTeleLocatorDT.setItDirty(false);
			homeTeleLocatorDT.setAddTime(time);
			homeTeleLocatorDT.setAddUserId(userId);
			homeTeleLocatorDT
					.setRecordStatusCd(NEDSSConstants.RECORD_STATUS_ACTIVE);
			homeTeleLocatorDT.setRecordStatusTime(time);
			homeTeleLocatorDT
					.setRecordStatusCd(NEDSSConstants.RECORD_STATUS_ACTIVE);
			entityTeleLocatorDT.setTheTeleLocatorDT(homeTeleLocatorDT);
			elPColl.add(entityTeleLocatorDT);
		}

		if (elPColl != null && elPColl.size() > 0)
			personVO.setTheEntityLocatorParticipationDTCollection(elPColl);
		else
			personVO.setTheEntityLocatorParticipationDTCollection(null);
		
		return personVO;
	}

	public static String getNbsCode(String questionIdentifier, CodedType codeType, Long codeSetGroupId) {
		String nbsCode = "";
		if (codeType != null) {
			try {
				boolean isConceptCode = true;
				String conceptCode = codeType.getCode();
				String codeSystemCode = codeType.getCodeSystemCode();
				if (conceptCode == null || conceptCode.equalsIgnoreCase("")) {
					isConceptCode = false;
					conceptCode = codeType.getAlternateCode();
					codeSystemCode = codeType.getAlternateCodeSystemCode();
				}
					if (codeSetGroupId != null) {
						SRTMapDAOImpl map = new SRTMapDAOImpl();
						String returnedCode = map.getConceptToCode(conceptCode,
								codeSetGroupId, codeSystemCode, isConceptCode);
						if (returnedCode
								.equalsIgnoreCase(NEDSSConstants.TABLE_NOT_MAPPED)) {
							logger.warn("The conceptCode " + conceptCode
									+ " not mapped.");
							nbsCode = conceptCode;
						} else {
							nbsCode = returnedCode;
						}
					} else
						nbsCode = conceptCode;
			} catch (Exception e) {
				logger.error("EdxPHCRDocumentUtil.getNbsCode error meesage for questionIdentifier="
						+ questionIdentifier + "CodedType=" + codeType);
				logger.error("EdxPHCRDocumentUtil.getNbsCode error meesage "
						+ e);
				throw new EJBException(
						"EdxPHCRDocumentUtil.getNbsCode error meesage for questionIdentifier="
								+ questionIdentifier + "CodedType=" + codeType
								+ e, e);
			}
		}
		return nbsCode;
	}
	
	
	public NBSDocumentVO createNBSDocumentVO(NbsInterfaceDT nbsInterfaceDT, Timestamp time, NBSDocumentMetadataDT nbsDocMDT, NBSSecurityObj nbsSecurityObj) throws NEDSSException,
			RemoteException, EJBException, NEDSSSystemException,
			NEDSSConcurrentDataException, CreateException {
		NBSDocumentVO nbsDocumentVO = new NBSDocumentVO();
		try {
			String xmlPayLoadContent = nbsInterfaceDT.getXmlPayLoadContent();
			NBSDocumentDT nbsDocumentDT = null;
			// If this isn't a PHDC xml doc, then call appropriate parser
			if (!(nbsDocMDT.getDocTypeCd().equalsIgnoreCase(NEDSSConstants.EDX_PHDC_DOC_TYPE) || nbsDocMDT.getDocTypeCd().equalsIgnoreCase(NEDSSConstants.LAB_TYPE_CD)) ){
				nbsDocumentDT = parseXMLDocumentToPHDC(xmlPayLoadContent,nbsDocMDT);
				xmlPayLoadContent = nbsDocumentDT.getPhdcDocDerivedTxt();
			} else {
				nbsDocumentDT = new NBSDocumentDT();
			}

			ContainerDocument containerDoc = parseCaseTypeXml(xmlPayLoadContent);
			nbsDocumentDT.setDocumentObject(containerDoc);
			Container container = containerDoc.getContainer();
			HeaderType headerType = container.getHeader();
			CaseType caseType = container.getCase();
			if (headerType.getMessageType() != null	&& headerType.getMessageType().getCode() != null)
				nbsDocumentVO.setDocName(headerType.getMessageType().getCode());
			HierarchicalDesignationType sendingFacilityType = headerType.getSendingFacility();
			String jurisdictionCode = null;
			boolean isJurisdictionFound = false;
			if (sendingFacilityType.getNamespaceID() != null && sendingFacilityType.getUniversalID() != null) {
				String name = sendingFacilityType.getNamespaceID();
				String code = sendingFacilityType.getUniversalID();
				CaseNotificationDAOImpl caseNotificationDAOImpl = new CaseNotificationDAOImpl();
				String jurisdiction = caseNotificationDAOImpl.getJurDeriveIndCd(name, code);
				if (jurisdiction != null && jurisdiction.trim().length() > 0 && jurisdiction.equalsIgnoreCase("N")) {
					CommonQuestionsType commonQuestionsType = caseType.getCommonQuestions();
					if (commonQuestionsType != null	&& commonQuestionsType.getInvestigationInformation() != null
							&& commonQuestionsType.getInvestigationInformation() != null
							&& commonQuestionsType.getInvestigationInformation()
									.getSendingApplicationJurisdiction() != null) {
						jurisdictionCode = commonQuestionsType.getInvestigationInformation().getSendingApplicationJurisdiction().getAlternateCode();
						isJurisdictionFound = true;
					}
				}
			}

			ProgramAreaVO programAreaVO;
			PersonVO patientVO = getPatientVO(caseType.getPatient(), time,	new Long(nbsSecurityObj.getEntryID()), true);
			String conditionCode = caseType.getCondition().getCode();
			nbsDocumentVO.setConditionName(caseType.getCondition().getCodeDescTxt());

			TreeMap<Object, Object> condAndFormCdTreeMap = CachedDropDowns.getConditionCdAndInvFormCd();
			if (condAndFormCdTreeMap.get(conditionCode) == null) {
				nbsDocumentVO.setConditionFound(false);
			} else {
				nbsDocumentVO.setConditionFound(true);
				nbsDocumentVO.setConditionName(CachedDropDowns.getConditionDesc(conditionCode));
			}
			
			programAreaVO = CachedDropDowns.getProgramAreaForCondition(conditionCode);
			if (!isJurisdictionFound) {
				NedssUtils nedssUtils = new NedssUtils();
				Object lookUpJurisdiction = nedssUtils.lookupBean(JNDINames.JURISDICTION_EJB);
				JurisdictionHome jurHome = (JurisdictionHome) PortableRemoteObject.narrow(lookUpJurisdiction, JurisdictionHome.class);
				Jurisdiction jurisdiction = jurHome.create();
				Collection<Object> jurColl = jurisdiction.findJurisdictionForPatient(patientVO);
				if (jurColl != null && jurColl.size() == 1) {
					for (Iterator<Object> i = jurColl.iterator(); i.hasNext();) {
						jurisdictionCode = (String) i.next();
					}
				}
			}
			nbsDocumentDT.setPayLoadTxt(nbsInterfaceDT.getXmlPayLoadContent());
			nbsDocumentDT.setDocTypeCd(caseType.getSectionHeader().getDocumentType().getCode());
			nbsDocumentDT.setRecordStatusCd(NEDSSConstants.NND_UNPROCESSED_MESSAGE);
			nbsDocumentDT.setStatusCd(headerType.getResultStatus().getCode());
			nbsDocumentDT.setRecordStatusTime(time);
			nbsDocumentDT.setAddUserId(new Long(nbsSecurityObj.getEntryID()));
			nbsDocumentDT.setAddTime(time);
			nbsDocumentDT.setProgAreaCd(programAreaVO.getStateProgAreaCode());
			nbsDocumentDT.setJurisdictionCd(jurisdictionCode);
			nbsDocumentDT.setTxt(caseType.getSectionHeader().getDescription());
			nbsDocumentDT.setSharedInd("T");
			if (nbsDocumentDT.getNbsDocumentMetadataUid() == null)
				nbsDocumentDT.setNbsDocumentMetadataUid(nbsDocMDT.getNbsDocumentMetadataUid());
			nbsDocumentDT.setPayloadViewIndCd(NEDSSConstants.PAYLOAD_VIEW_IND_CD_PHDC); 
			
			if (!(nbsDocumentDT.getProgAreaCd() == null) && !(nbsDocumentDT.getJurisdictionCd() == null)) {
				String progAreaCd = nbsDocumentDT.getProgAreaCd();
				String jurisdictionCd = nbsDocumentDT.getJurisdictionCd();
				long pajHash = ProgramAreaJurisdictionUtil.getPAJHash(progAreaCd, jurisdictionCd);
				Long aProgramJurisdictionOid = new Long(pajHash);
				nbsDocumentDT.setProgramJurisdictionOid(aProgramJurisdictionOid);
			}

			nbsDocumentDT.setNbsInterfaceUid(nbsInterfaceDT.getNbsInterfaceUid());
			nbsDocumentDT.setVersionCtrlNbr(new Integer(1));
			nbsDocumentDT.setCd(conditionCode);
			nbsDocumentDT.setLastChgTime(time);
			nbsDocumentDT.setLastChgUserId(new Long(nbsSecurityObj.getEntryID()));
			nbsDocumentDT.setDocPurposeCd(caseType.getSectionHeader().getPurpose().getCode());

			if (caseType.getSectionHeader().getSendingApplicationEventIdentifier() != null)
				nbsDocumentDT.setSendingAppEventId(caseType.getSectionHeader().getSendingApplicationEventIdentifier());
			caseType.getPatient().getIdentifiers();
			int j = 0;
			Collection<Object> coll = new ArrayList<Object>();
			IdentifiersType identifiersType = caseType.getPatient().getIdentifiers();
			if(identifiersType != null && identifiersType.getIdentifierArray()!=null && identifiersType.getIdentifierArray().length>0)
			{
				IdentifierType[] identifierArray = identifiersType.getIdentifierArray();
			for (int i = 0; i < identifierArray.length; i++) {

				IdentifierType identifierType = identifierArray[i];
				if (identifierType != null	&& identifierType.getIDTypeCode() != null
						&& identifierType.getIDTypeCode().equalsIgnoreCase(NEDSSConstants.PHCR_LOCAL_REGISTRY_ID)) {
					if (identifierType.getIDNumber() != null && identifierType.getIDNumber().trim().length() > 0
							&& identifierType.getAssigningFacility()!=null
							&& identifierType.getAssigningFacility().getNamespaceID() != null
							&& identifierType.getAssigningFacility().getNamespaceID().trim().length() > 0
							&& identifierType.getAssigningFacility().getUniversalID() != null
							&& identifierType.getAssigningFacility().getUniversalID().trim().length() > 0
							&& identifierType.getAssigningFacility().getUniversalIDType() != null
							&& identifierType.getAssigningFacility().getUniversalIDType().trim().length() > 0
							&& identifierType.getAssigningAuthority().getNamespaceID() != null
							&& identifierType.getAssigningAuthority().getNamespaceID().trim().length() > 0
							&& identifierType.getAssigningAuthority().getUniversalID() != null
							&& identifierType.getAssigningAuthority().getUniversalID().trim().length() > 0) {
						StringBuffer buffer = new StringBuffer();
						buffer.append(identifierType.getIDTypeCode());
						buffer.append("^").append(identifierType.getIDNumber());
						buffer.append("^").append(
								identifierType.getAssigningFacility()
										.getNamespaceID());
						buffer.append("^").append(
								identifierType.getAssigningFacility()
										.getUniversalID());
						buffer.append("^").append(
								identifierType.getAssigningFacility()
										.getUniversalIDType());
						buffer.append("^").append(
								identifierType.getAssigningAuthority().getNamespaceID());
						buffer.append("^").append(
								identifierType.getAssigningAuthority().getUniversalID());
						buffer.append("^").append(identifierType.getAssigningAuthority()
										.getUniversalIDType());
						patientVO.setLocalIdentifier(buffer.toString());
					}
				} else {
					EntityIdDT entityIdDT = new EntityIdDT();
					entityIdDT.setEntityIdSeq(new Integer(j++));
					if(identifierType!=null && identifierType.getAssigningAuthority()!=null){
						entityIdDT.setAssigningAuthorityCd(identifierType.getAssigningAuthority().getUniversalID());
						entityIdDT.setAssigningAuthorityDescTxt(identifierType.getAssigningAuthority().getNamespaceID());
						entityIdDT.setAssigningAuthorityIdType(identifierType.getAssigningAuthority().getUniversalIDType());
					}else{
						logger.error("XMLTypeToNBSObject.createNBSDocumentVO identifierType OR identifierType.getAssigningAuthority is null. Please check. " + identifierType);
						//throw new NEDSSException("XMLTypeToNBSObject.createNBSDocumentVO identifierType OR identifierType.getAssigningAuthority is null. Please check.");
					}
					entityIdDT.setRootExtensionTxt(identifierType.getIDNumber());
					entityIdDT.setTypeCd(identifierType.getIDTypeCode());
					entityIdDT.setStatusCd(NEDSSConstants.STATUS_ACTIVE);
					entityIdDT.setStatusTime(new Timestamp(new Date().getTime()));
					entityIdDT.setRecordStatusTime(new Timestamp(new Date().getTime()));
					entityIdDT.setRecordStatusCd(NEDSSConstants.ACTIVE);
					entityIdDT.setItNew(true);
					entityIdDT.setItDirty(false);
					coll.add(entityIdDT);
				}
			}
			patientVO.setTheEntityIdDTCollection(coll);
			}

			nbsDocumentDT.setCdDescTxt(caseType.getCondition().getCodeDescTxt());
			nbsDocumentDT.setSendingFacilityNm(sendingFacilityType.getNamespaceID());
			nbsDocumentDT.setDocStatusCd(headerType.getResultStatus().getCode());
			nbsDocumentVO.setNbsDocumentDT(nbsDocumentDT);
			setAsOfDate(patientVO, time);

			nbsDocumentVO.setPatientVO(patientVO);

		} catch (ClassCastException e) {
			e.printStackTrace();
			logger.error("XMLTypeToNBSObject.createNBSDocumentVO  "	+ e.getMessage());
			throw new NEDSSException(e.getMessage(), e);
		} catch (RemoteException e) {
			e.printStackTrace();
			logger.error("XMLTypeToNBSObject.createNBSDocumentVO  "
					+ e.getMessage());
			throw new NEDSSException(e.getMessage(), e);
		} catch (CreateException e) {
			e.printStackTrace();
			logger.error("XMLTypeToNBSObject.createNBSDocumentVO  "
					+ e.getMessage());
			throw new NEDSSException(e.getMessage(), e);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("XMLTypeToNBSObject.createNBSDocumentVO  "
					+ e.getMessage());
			throw new NEDSSException(e.getMessage(), e);
		}

		return nbsDocumentVO;
	}

	public NBSDocumentVO insertNbsDocumentVO(NbsInterfaceDT nbsInterfaceDT,NBSDocumentVO nBSDocumentVO, NBSSecurityObj nbsSecurityObj)
			throws NEDSSException {

		try {
			NedssUtils nedssUtils = new NedssUtils();
			
			// Stores Patient
			nBSDocumentVO.getPatientVO().setItDirty(false);
			nBSDocumentVO.getPatientVO().setItNew(true);
			nBSDocumentVO.getPatientVO().getThePersonDT().setItDirty(false);
			nBSDocumentVO.getPatientVO().getThePersonDT()
					.setPersonUid(new Long(-1));
			nBSDocumentVO.getPatientVO().setExt(true);
			EdxPatientMatchingCriteriaUtil edxPatientMatchingCriteriaUtil = new EdxPatientMatchingCriteriaUtil();
			EDXEventProcessCaseSummaryDT patientInvestigationSummary=null;
			
			if(nBSDocumentVO.geteDXEventProcessCaseSummaryDTMap()!=null && nBSDocumentVO.isContactRecordDoc()){// Check if the contact is already created, no need to match patient
				 patientInvestigationSummary = (EDXEventProcessCaseSummaryDT)nBSDocumentVO.geteDXEventProcessCaseSummaryDTMap().get(EdxCDAConstants.CONTACT_INV);
			}
			//If contact patient already exists, just create the revision of the same patient
			if(patientInvestigationSummary!=null){
				nBSDocumentVO.getPatientVO().getThePersonDT().setPersonParentUid(patientInvestigationSummary.getPersonParentUid());
				nBSDocumentVO.setIsMultiplePatFound(false);
				nBSDocumentVO.setIsExistingPatient(true);
				EntityController entityController = edxPatientMatchingCriteriaUtil.getEntityController();
				Long patientUid = entityController.setPatientRevision(nBSDocumentVO.getPatientVO(),
						NEDSSConstants.PAT_CR, nbsSecurityObj);
				nBSDocumentVO.getPatientVO().getThePersonDT().setPersonUid(patientUid);
			}
			//Check to see if its a lab or morb document and it have associated investigation, use the same MPR as investigation and create a new revision
			else if(nBSDocumentVO.geteDXEventProcessCaseSummaryDTMap()!=null && nBSDocumentVO.geteDXEventProcessCaseSummaryDTMap().size()>0 && (nBSDocumentVO.isLabReportDoc()|| nBSDocumentVO.isMorbReportDoc())){
				patientInvestigationSummary = (EDXEventProcessCaseSummaryDT)nBSDocumentVO.geteDXEventProcessCaseSummaryDTMap().values().iterator().next();
					nBSDocumentVO.getPatientVO().getThePersonDT().setPersonParentUid(patientInvestigationSummary.getPersonParentUid());
					nBSDocumentVO.setIsMultiplePatFound(false);
					nBSDocumentVO.setIsExistingPatient(true);
					EntityController entityController = edxPatientMatchingCriteriaUtil.getEntityController();
					Long patientUid = entityController.setPatientRevision(nBSDocumentVO.getPatientVO(),
							NEDSSConstants.PAT_CR, nbsSecurityObj);
					nBSDocumentVO.getPatientVO().getThePersonDT().setPersonUid(patientUid);
			}
			else{
				EdxPatientMatchDT edxPatientMatchFoundDT= edxPatientMatchingCriteriaUtil.getMatchingPatient(
						nBSDocumentVO.getPatientVO(), nbsSecurityObj);
				nBSDocumentVO.setIsMultiplePatFound(edxPatientMatchFoundDT.isMultipleMatch());
				// realUid =
				// entityController.setPatientRevision(nBSDocumentVO.getPatientVO(),
				// NEDSSConstants.PAT_CR,nbsSecurityObj);
				nBSDocumentVO.setIsExistingPatient(nBSDocumentVO.getPatientVO()
						.getIsExistingPatient());
			}
			// patientRevisionVO.setItDirty(true);
			// patientRevisionVO.getThePersonDT().setItDirty(true);
			// Store NBS_Document
			NbsDocumentDAOImpl nbsDocumentDAOImpl = new NbsDocumentDAOImpl();
			if(nBSDocumentVO.getPatientVO().getThePersonDT().getPersonParentUid()!=null){
				NBSDocumentDT oldNBSDocumentDT = nbsDocumentDAOImpl.getLastDocument( nBSDocumentVO);
				if(oldNBSDocumentDT.getExternalVersionCtrlNbr()!=null && oldNBSDocumentDT.getLocalId()!=null){
					nBSDocumentVO.getNbsDocumentDT().setExternalVersionCtrlNbr(oldNBSDocumentDT.getExternalVersionCtrlNbr()+1);
					nBSDocumentVO.setOriginalPHCRLocalId(oldNBSDocumentDT.getLocalId());
				}else{
					nBSDocumentVO.getNbsDocumentDT().setExternalVersionCtrlNbr(1);
				}
				// Short Term: If its an ongoing CDA investigation and property is configured to N, don't check for existing investigation
				//TODO: Move the logic to decision support management. 
				String existingInvCheck = util.getPHDCImpCheckForExistingInv();
				if(nBSDocumentVO.isOngoingCase() && existingInvCheck!=null && existingInvCheck.equals(NEDSSConstants.NO))
					nBSDocumentVO.setAssociatedInv(false);
				else if(nBSDocumentVO.isOngoingCase()){
					int numberOfCases =nbsDocumentDAOImpl.getNumberOfCasesAssociated(nBSDocumentVO.getPatientVO().getThePersonDT().getPersonParentUid(), nBSDocumentVO.getNbsDocumentDT().getCd());
					if(numberOfCases>0)
						nBSDocumentVO.setAssociatedInv(true);
					else
						nBSDocumentVO.setAssociatedInv(false);
				}

			}else{
				nBSDocumentVO.getNbsDocumentDT().setExternalVersionCtrlNbr(1);
			}
			
			
			nBSDocumentVO.getNbsDocumentDT().setPayLoadTxt(
					nbsInterfaceDT.getXmlPayLoadContent());
			NBSDocumentDT nBSDocumentDT = nbsDocumentDAOImpl
					.insertNBSDocument(nBSDocumentVO.getNbsDocumentDT());
			nBSDocumentVO.setNbsDocumentDT(nBSDocumentDT);
			nbsInterfaceDT.setNbsDocumentUid(nBSDocumentDT.getNbsDocumentUid());
			// Store ParticipationDT
			ParticipationDT participationDT = new ParticipationDT();
			participationDT.setTypeCd(NEDSSConstants.SUBJECT_OF_DOC);
			participationDT.setTypeDescTxt(NEDSSConstants.SUBJECT_OF_DOC_DESC);
			participationDT.setStatusCd(NEDSSConstants.STATUS_ACTIVE);
			participationDT.setSubjectClassCd(NEDSSConstants.CLASS_CD_PSN);
			participationDT.setActClassCd(NEDSSConstants.ACT_CLASS_CD_FOR_DOC);
			participationDT.setActUid(nBSDocumentDT.getNbsDocumentUid());
			participationDT.setSubjectEntityUid(nBSDocumentVO.getPatientVO().getThePersonDT().getPersonUid());
			participationDT
					.setRecordStatusCd(NEDSSConstants.RECORD_STATUS_ACTIVE);
			participationDT.setItNew(true);
			participationDT.setItDirty(false);

			ParticipationDAOImpl partImpl = new ParticipationDAOImpl();
			partImpl.store(participationDT);
			createEventProcessForPHCREvents(nBSDocumentVO);
			for(EDXEventProcessDT processDT:(Collection<EDXEventProcessDT>)nBSDocumentVO.getEDXEventProcessDTMap().values())
				nbsDocumentDAOImpl.insertEventProcessDTs(processDT);
			if(nBSDocumentVO.getNbsDocumentDT().getCd()!=null){
				Object lookUpAlert= nedssUtils.lookupBean(JNDINames.ALERT_EJB);
				AlertHome alertHome = (AlertHome) PortableRemoteObject.narrow(lookUpAlert, AlertHome.class);
				Alert alert= alertHome.create();
				alert.alertNBDDocumentEmailMessage(nBSDocumentVO, nbsSecurityObj);
			}

		} catch (NEDSSSystemException e) {
			logger.error("NEDSSSystemException raised in insertNbsDocumentVO"
					+ e.getMessage());
			throw new NEDSSException(e.getMessage(), e);
		} catch (ClassCastException e) {
			logger.error("ClassCastException raised in insertNbsDocumentVO"
					+ e.getMessage());
			throw new NEDSSException(e.getMessage(), e);
		} catch (RemoteException e) {
			logger.error("RemoteException raised in insertNbsDocumentVO"
					+ e.getMessage());
			throw new NEDSSException(e.getMessage(), e);
		} catch (EJBException e) {
			logger.error("EJBException raised in insertNbsDocumentVO"
					+ e.getMessage());
			throw new NEDSSException(e.getMessage(), e);
		} catch (NEDSSConcurrentDataException e) {
			logger.error("NEDSSConcurrentDataException raised in insertNbsDocumentVO"
					+ e.getMessage());
			throw new NEDSSException(e.getMessage(), e);
		} catch (CreateException e) {
			logger.error("CreateException raised in insertNbsDocumentVO"
					+ e.getMessage());
			throw new NEDSSException(e.getMessage(), e);
		} catch (Exception e) {
			logger.error("Exception raised in insertNbsDocumentVO"
					+ e.getMessage());
			throw new NEDSSException(e.getMessage(), e);
		}
		return nBSDocumentVO;

	}

	private void setAsOfDate(PersonVO pvo, Timestamp aod) {
		PersonDT dt = pvo.getThePersonDT();

			dt.setAsOfDateAdmin(aod);

		if (dt.getEthnicGroupInd() != null)
			dt.setAsOfDateEthnicity(aod);

		if (pvo.getThePersonDT().getBirthTime() != null	|| pvo.getThePersonDT().getAgeReported() != null 
				|| pvo.getThePersonDT().getCurrSexCd() != null || pvo.getThePersonDT().getBirthGenderCd() != null) {
			pvo.getThePersonDT().setAsOfDateSex(aod);
		} else {
			pvo.getThePersonDT().setAsOfDateSex(null);
		}

		if (dt.getMothersMaidenNm() != null || dt.getAdultsInHouseNbr() != null
				|| dt.getChildrenInHouseNbr() != null || dt.getEducationLevelCd() != null
				|| dt.getOccupationCd() != null	|| dt.getMaritalStatusCd() != null
				|| dt.getPrimLangCd() != null) {
			dt.setAsOfDateGeneral(aod);
		}

		if (pvo.getTheEntityIdDTCollection() != null) {
			Iterator<Object> eIdIt = pvo.getTheEntityIdDTCollection()
					.iterator();

			while (eIdIt.hasNext()) {
				EntityIdDT eIddt = (EntityIdDT) eIdIt.next();
				eIddt.setAsOfDate(aod);
			}
		}

		if (pvo.getTheEntityLocatorParticipationDTCollection() != null) {
			Iterator<Object> elpIt = pvo
					.getTheEntityLocatorParticipationDTCollection().iterator();

			while (elpIt.hasNext()) {
				EntityLocatorParticipationDT elpDt = (EntityLocatorParticipationDT) elpIt
						.next();
				elpDt.setAsOfDate(aod);
			}
		}

		if (pvo.getThePersonRaceDTCollection() != null) {
			Iterator<Object> pRaceIt = pvo.getThePersonRaceDTCollection()
					.iterator();

			while (pRaceIt.hasNext()) {
				PersonRaceDT pRaceDt = (PersonRaceDT) pRaceIt.next();
				pRaceDt.setAsOfDate(aod);
			}

		}

		if (pvo.getThePersonNameDTCollection() != null) {
			Iterator<Object> pNIt = pvo.getThePersonNameDTCollection()
					.iterator();

			while (pNIt.hasNext()) {
				PersonNameDT pnDt = (PersonNameDT) pNIt.next();
				pnDt.setAsOfDate(aod);
			}

		}
	}

	private NBSDocumentDT parseXMLDocumentToPHDC(String xmlPayLoadContent,
			NBSDocumentMetadataDT nbsDocMDT) throws NEDSSException {
		// Use reflection to initiate parser for this xml document type
		// Parsers should have 'public void parseXMLMessage(String
		// xmlPayLoadContent)' method

		try {

			Class<?> c = Class.forName(nbsDocMDT.getParserClassNm());
			Constructor<?> constructor = c.getConstructor(String.class);
			NbsXMLMessageParserBase parserClass = (NbsXMLMessageParserBase) constructor
					.newInstance(xmlPayLoadContent);
			NBSDocumentDT nbsDocumentDT = parserClass.parseXMLMessage();
			return nbsDocumentDT;
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("XMLTypeToNBSObject.parseXMLDocumentToPHDC  "
					+ e.getMessage());
			throw new NEDSSException(e.getMessage(), e);
		}

	}
	
	
	public ContainerDocument parseCaseTypeXml(String xmlPayLoadContent)
			throws Exception {
		ContainerDocument containerDoc = null;
		try {
			ArrayList<Object> validationErrors = new ArrayList<Object>();
			XmlOptions validationOptions = new XmlOptions();
			validationOptions.setErrorListener(validationErrors);

			containerDoc = ContainerDocument.Factory.parse(xmlPayLoadContent);

			boolean isValid = containerDoc.validate(validationOptions);

			// Print the errors if the XML is invalid.
			if (!isValid) {
				Iterator<Object> iter = validationErrors.iterator();
				StringBuffer buff = new StringBuffer();
				buff.append("Inbound message failed in XMLTypeToNBSObject.parseCaseTypeXml.");
				while (iter.hasNext()) {
					buff.append(iter.next() + "\n");
				}
				logger.error(buff.toString());
				logger.error("XMLTypeToNBSObject.parseCaseTypeXml:-Error thrown as XMl is invalid"+buff.toString());
				throw new Exception("Invalid XML "+buff.toString());
			}
		} catch (Exception e) {
			logger.error(e.toString());
			e.printStackTrace();
			throw new Exception(e);
		}

		//logger.debug("Received XML:\n" + containerDoc.toString());
		return containerDoc;
	}

	public HeaderType parseHeaderTypeXml(String xmlPayLoadContent)
			throws Exception {
		HeaderType headerTypeDoc = null;
		try {
			ArrayList<Object> validationErrors = new ArrayList<Object>();
			XmlOptions validationOptions = new XmlOptions();
			validationOptions.setErrorListener(validationErrors);

			headerTypeDoc = HeaderType.Factory.parse(xmlPayLoadContent);

			boolean isValid = headerTypeDoc.validate(validationOptions);

			if (!isValid) {
				Iterator<Object> iter = validationErrors.iterator();
				StringBuffer buff = new StringBuffer();
				buff.append("Inbound message failed in XMLTypeToNBSObject.parseHeaderTypeXml. ");
				while (iter.hasNext()) {
					buff.append(iter.next() + "\n");
				}
				logger.error(buff.toString());
				throw new Exception(buff.toString());
			}

		} catch (Exception e) {
			logger.error(e.toString());
			e.printStackTrace();
			throw new Exception(e);
		}
		logger.debug("Received XML: \n\n" + headerTypeDoc.toString());
		return headerTypeDoc;
	}

	public PatientType getPatient(CaseType caseType) {
		PatientType PatientType = caseType.getPatient();
		return PatientType;
	}
	
	@SuppressWarnings("unchecked")
	public void createEventProcessForPHCREvents(NBSDocumentVO nbsDocVO) {
		NBSDocumentDT nbsDocumentDT = nbsDocVO.getNbsDocumentDT();
		if (nbsDocumentDT.getEventIdMap() == null || nbsDocumentDT.getEventIdMap() .size()==0)
			return;
		// Treatments
		ArrayList<Object> treatmentIds = (ArrayList<Object>) nbsDocumentDT
				.getEventIdMap().get(NEDSSConstants.TREATMENT_ACT_TYPE_CD);
		prepareEventProcessList(treatmentIds, nbsDocVO,
				NEDSSConstants.TREATMENT_ACT_TYPE_CD);
		//Lab Reports
		ArrayList<Object> labIds = (ArrayList<Object>) nbsDocumentDT
				.getEventIdMap().get(NEDSSConstants.LABRESULT_CODE);
		prepareEventProcessList(labIds, nbsDocVO, NEDSSConstants.LABRESULT_CODE);
		//Morb Reports
		ArrayList<Object> morbIds = (ArrayList<Object>) nbsDocumentDT
				.getEventIdMap().get(NEDSSConstants.MORBIDITY_CODE);
		prepareEventProcessList(morbIds, nbsDocVO, NEDSSConstants.MORBIDITY_CODE);
		//Cases
		ArrayList<Object> caseIds = (ArrayList<Object>) nbsDocumentDT
				.getEventIdMap().get(NEDSSConstants.CLASS_CD_CASE);
		prepareEventProcessList(caseIds, nbsDocVO, NEDSSConstants.CLASS_CD_CASE);
		//Contact Record
		ArrayList<Object> contactRecordIds = (ArrayList<Object>) nbsDocumentDT
				.getEventIdMap().get(NEDSSConstants.CLASS_CD_CONTACT);
		prepareEventProcessList(contactRecordIds, nbsDocVO, NEDSSConstants.CLASS_CD_CONTACT);
	}

	public static ArrayList<EDXEventProcessDT> prepareEventProcessList(
			ArrayList<Object> ids, NBSDocumentVO nbsDocVO, String eventType) {
		ArrayList<EDXEventProcessDT> eventProcessDTList = new ArrayList<EDXEventProcessDT>();
		NBSDocumentDT nbsDocumentDT = nbsDocVO.getNbsDocumentDT();
		if (ids != null && ids.size() > 0) {
			for (Object id : ids) {
				EDXEventProcessDT processDT = new EDXEventProcessDT();
				processDT.setAddUserId(nbsDocumentDT.getAddUserId());
				processDT.setAddTime(nbsDocumentDT.getAddTime());
				processDT.setDocEventTypeCd(eventType);
				processDT.setSourceEventId((String) id);
				processDT.setNbsDocumentUid(nbsDocumentDT.getNbsDocumentUid());
				processDT.setDocEventSource(nbsDocumentDT
						.getSendingFacilityNm());
				processDT.setParsedInd(NEDSSConstants.NO);
				if (nbsDocVO.getEDXEventProcessDTMap() == null) {
					nbsDocVO.setEDXEventProcessDTMap(new HashMap<String, EDXEventProcessDT>());
				}
				nbsDocVO.getEDXEventProcessDTMap().put(
						processDT.getSourceEventId(), processDT);
				eventProcessDTList.add(processDT);
			}
		}
		return eventProcessDTList;
	}

	public static ArrayList<EDXEventProcessDT> prepareEventProcessListForNonDocEvents(
			ArrayList<Object> ids, EdxCDAInformationDT information,
			String eventType) {
		ArrayList<EDXEventProcessDT> eventProcessDTList = new ArrayList<EDXEventProcessDT>();
		if (ids != null && ids.size() > 0) {
			for (Object id : ids) {
				EDXEventProcessDT processDT = new EDXEventProcessDT();
				processDT.setAddUserId(information
						.getRuleAlgorothmManagerDT().getDocumentDT().getAddUserId());
				processDT.setAddTime(information
						.getRuleAlgorothmManagerDT().getDocumentDT().getAddTime());
				processDT.setDocEventTypeCd(eventType);
				processDT.setSourceEventId((String) id);
				processDT.setNbsDocumentUid(information
						.getRuleAlgorothmManagerDT().getDocumentDT()
						.getNbsDocumentUid());
				processDT.setDocEventSource(information
						.getRuleAlgorothmManagerDT().getDocumentDT()
						.getSendingFacilityNm());
				processDT.setParsedInd(NEDSSConstants.YES);
				eventProcessDTList.add(processDT);
			}
		}
		return eventProcessDTList;
	}
	
	public static String getNBSCodeFromPHINCodes(String codeSetNm, String code, String tableName) {
		if(tableName==null || ! tableName.equalsIgnoreCase(NEDSSConstants.CODE_VALUE_GENERAL))
			return code;
		String translatedCode = CachedDropDowns.getNBSCodeFromPHINCodes().get(
				codeSetNm + "^" + code);
		if(translatedCode==null){
			String error = "The code "+code+" can not be translated for code set name "+ codeSetNm +" from "+tableName +" table.";
			logger.info(error);
		}
		return translatedCode;
	}
}
