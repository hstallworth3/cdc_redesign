package gov.cdc.nedss.webapp.nbs.action.pagemanagement.pdfprintform;

import java.io.File;
import java.io.IOException;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.StringTokenizer;
import java.util.TreeMap;

import javax.rmi.PortableRemoteObject;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.pdfbox.cos.COSDictionary;
import org.apache.pdfbox.cos.COSName;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.interactive.annotation.PDAnnotationWidget;
import org.apache.pdfbox.pdmodel.interactive.form.PDField;

import gov.cdc.nedss.act.ctcontact.dt.CTContactAnswerDT;
import gov.cdc.nedss.act.ctcontact.dt.CTContactSummaryDT;
import gov.cdc.nedss.act.interview.dt.InterviewSummaryDT;
import gov.cdc.nedss.act.publichealthcase.dt.CaseManagementDT;
import gov.cdc.nedss.act.publichealthcase.dt.PublicHealthCaseDT;
import gov.cdc.nedss.act.treatment.dt.TreatmentAdministeredDT;
import gov.cdc.nedss.act.treatment.ejb.dao.TreatmentAdministeredDAOImpl;
import gov.cdc.nedss.association.dt.ActRelationshipDT;
import gov.cdc.nedss.association.dt.ParticipationDT;
import gov.cdc.nedss.entity.entityid.dt.EntityIdDT;
import gov.cdc.nedss.entity.organization.dt.OrganizationNameDT;
import gov.cdc.nedss.entity.organization.vo.OrganizationVO;
import gov.cdc.nedss.entity.person.dt.PersonDT;
import gov.cdc.nedss.entity.person.dt.PersonNameDT;
import gov.cdc.nedss.entity.person.dt.PersonRaceDT;
import gov.cdc.nedss.entity.person.ejb.bean.Person;
import gov.cdc.nedss.entity.person.ejb.bean.PersonHome;
import gov.cdc.nedss.entity.person.vo.PersonVO;
import gov.cdc.nedss.exception.NEDSSAppException;
import gov.cdc.nedss.exception.NEDSSSystemException;
import gov.cdc.nedss.locator.dt.EntityLocatorParticipationDT;
import gov.cdc.nedss.locator.dt.PostalLocatorDT;
import gov.cdc.nedss.locator.dt.TeleLocatorDT;
import gov.cdc.nedss.page.ejb.pageproxyejb.dt.NbsAnswerDT;
import gov.cdc.nedss.page.ejb.pageproxyejb.vo.PageProxyVO;
import gov.cdc.nedss.page.ejb.pageproxyejb.vo.act.PageActProxyVO;
import gov.cdc.nedss.pam.act.NbsCaseAnswerDT;
import gov.cdc.nedss.proxy.ejb.investigationproxyejb.vo.InvestigationSummaryVO;
import gov.cdc.nedss.proxy.ejb.observationproxyejb.vo.LabReportSummaryVO;
import gov.cdc.nedss.proxy.ejb.observationproxyejb.vo.MorbReportSummaryVO;
import gov.cdc.nedss.proxy.ejb.observationproxyejb.vo.ResultedTestSummaryVO;
import gov.cdc.nedss.proxy.ejb.pamproxyejb.vo.CTContactProxyVO;
import gov.cdc.nedss.proxy.ejb.treatmentproxyejb.vo.TreatmentProxyVO;
import gov.cdc.nedss.proxy.ejb.treatmentproxyejb.vo.TreatmentSummaryVO;
import gov.cdc.nedss.proxy.ejb.workupproxyejb.vo.WorkupProxyVO;
import gov.cdc.nedss.systemservice.ejb.mainsessionejb.bean.MainSessionCommand;
import gov.cdc.nedss.systemservice.ejb.questionmapejb.dt.NbsQuestionMetadata;
import gov.cdc.nedss.systemservice.nbscontext.NBSConstantUtil;
import gov.cdc.nedss.systemservice.nbscontext.NBSContext;
import gov.cdc.nedss.systemservice.util.MainSessionHolder;
import gov.cdc.nedss.util.JNDINames;
import gov.cdc.nedss.util.LogUtils;
import gov.cdc.nedss.util.NEDSSConstants;
import gov.cdc.nedss.util.NedssUtils;
import gov.cdc.nedss.util.StringUtils;
import gov.cdc.nedss.webapp.nbs.action.contacttracing.util.CTConstants;
import gov.cdc.nedss.webapp.nbs.action.pagemanagement.rendering.util.PageConstants;
import gov.cdc.nedss.webapp.nbs.action.pagemanagement.rendering.util.PageLoadUtil;
import gov.cdc.nedss.webapp.nbs.action.pagemanagement.rendering.util.RenderConstants;
import gov.cdc.nedss.webapp.nbs.action.pagemanagement.util.common.PageManagementCommonActionUtil;
import gov.cdc.nedss.webapp.nbs.action.util.CallProxyEJB;
import gov.cdc.nedss.webapp.nbs.form.page.PageForm;
import gov.cdc.nedss.webapp.nbs.helper.CachedDropDowns;
import gov.cdc.nedss.webapp.nbs.logicsheet.helper.CachedDropDownValues;
import gov.cdc.nedss.webapp.nbs.logicsheet.helper.QuestionsCache;

/**
 * CommonPDFPrintForm is a Common utility class to consolidate print functionality for PDF forms. 
 * The PDF forms supported by this class includes Field Record Form, Interview Record Form, and Provider Follow-up Form.
 * @author Pradeep Kumar Sharma
 * 
 *
 */
/**
 * @author pateldh
 *
 */
public abstract class CommonPDFPrintForm {
	static final LogUtils logger = new LogUtils(CommonPDFPrintForm.class.getName());
	/**DEM152MarginalRaceMap
	 * Constants
	 */
	
	/* Staging variables */
	protected static String[][] labStagingArray = new String[8][6];
	protected static final int DATE_INDEX = 0;
	protected static final int TEST_INDEX = 1;
	protected static final int RESULT_INDEX = 2;
	protected static final int LAB_INDEX = 3;
	protected static final int SPECIMEN_SOURCE_INDEX = 4;
	protected static final int FORMATTED_FOR_SORT_DATE_INDEX = 5;
	
	/*Field Form variables*/
	
	
	
	
	/*Field Form varaibles ends*/
	public static final String STATE_SHORT_NAME	= "ORD113";
	public static final String FACILITY_ADDRESS	= "ORD110,ORD111";
	public static final String FACILITY_CITY="ORD112";
	public static final String STATE_STATE_SHORT_NAME	= "ORD113";
	public static final String FACILITY_ZIP	="ORD114";

	public static final String INTERVIEWER="INTERVIEWER";
	public static final String PROVIDER="PROVIDER";
	public static final String PATIENT="PATIENT";
	public static final String PROVIDER_ADDRESS	= "ORD110_ORD111";
	public static final String PROVIDER_CITY="ORD112";
	public static final String PROVIDER_ZIP="ORD114";
	public static final String DEM118	= "DEM118";
	public static final String NBS195  = "NBS195"; //Interview Notes
	public static final String OTHER_INFO	= "OTHER_INFO";
	public static final String NBS145_	= "NBS145_";
	public static final String PROVIDER_PHONE	= "ORD121_ORD122";
	public static  int labIndex=1;
	public static  int treatmentIndex=1;
	public static  Collection<Object> treatmentCollection=new ArrayList<Object>();
	public static  Collection<Object> labCollection=new ArrayList<Object>();
	

	public static final int HalfLineLength = 58; //no longer used with new PDFBox
	public static final int FullLineLength = 140;//no longer used with new PDFBox
	public static final int NotesHalfWidthCharLimit = 1300;
	public static final int NotesFullWidthHalfPageCharLimit = 3000;
	public static final int NotesFullWidthFullPageCharLimit = 5600;

	public static final String NBS136_ORIGINAL_CONDITION = "NBS136_OP_"; //gst mod from NBS136
	public static final String ORIGINAL_CASE_ID = "CaseID_OP_"; //gst mod from NBS136
	public static final String NBS057_NBS056_ORIGINAL_INFECTIOUS_PERIOD = "NBS057_NBS056";
	public static final String NBS117_ORIGINAL_PATIENT_ID_NUMBER = "NBS117";  //local id of Original Inv of Contact started Inv
	public static final String PROVIDER_NAME_FACILITY="ProviderNameTileFacility"; //Provider Name Title Facility
	public static final String PROVIDER_FACILITY="ProviderFacility"; //provider order facility Name
	public static final String PROVIDER_NAME="ProviderName"; //INV181 provider name 

	public static final String NBS136 = "NBS136";
	public static final String EMPTY_STRING="";

	public static final String COND_CD = "COND_CD";
	public static final String UNABLE="UNABLE";

	public static final String NBS111	= "NBS111";
	public static final String INV107="INV107";
	public static final String NBS179="NBS179";
	public static final String NBS181="NBS181";
	public static final String NBS113	= "NBS113";
	public static final String NBS118	= "NBS118";
	public static final String NBS119	= "NBS119";
	public static final String NBS120	= "NBS120"; //lastSexualExposureDate
	public static final String NBS121	= "NBS121";
	public static final String NBS122	= "NBS122";
	public static final String NBS123	= "NBS123"; //lastNeedleExposureDate
	public static final String NBS125	= "NBS125";
	public static final String NBS243	= "NBS243";
	public static final String NBS290	= "NBS290";
	public static final String DEM165	= "DEM165"; //County
	public static final String DEM167	= "DEM167"; //Country
	public static final String ADDL_SEX="Addl_Sex";
	public static final String EMPLOYMENT_PHONE="Employment_Phone";
	public static final String FF_OOJ	= "K";
	public static final String INIT_FOLL_INSUFF_INFO	= "II";

	public static final String STD116 = "STD116";
	public static final String STD106 = "STD106";
	public static final String NBS152 = "NBS152";
	public static final String BIRTH_SEX_CDT="Birth_Sex_CDT";
	
	public static final String NBS250="NBS250";
	public static final String NBS251="NBS251";
	public static final String NBS252="NBS252";
	public static final String NBS253="NBS253";
	
	public static final String NBS217="NBS217";
	public static final String NBS219="NBS219";
	public static final String NBS130="NBS130";
	public static final String NBS132="NBS132";
	public static final String NBS134="NBS134";
	public static final String NBS257="NBS257";
	public static final String NBS267="NBS267";
	//Program Area - used for fields in Interview
	public static final String STD = "STD";
	public static final String HIV = "HIV";
	
	

	private	static String DEM182 = "DEM182"; //email address
	private	static String DEM182_R2 = "DEM182_R2"; //otherEmailAddress
	private	static String DEM142	= "DEM142"; //primary Lang
	private	static String Internet_Site = "Internet_Site";
	private static String NBS177_R2 = "NBS177_R2"; //pager

	//Case management Constants
	public static final String NBS111_CASE_MANAGEMENT_INITIATING_AGNCY	="NBS111";
	public static final String NBS112_CASE_MANAGEMENT_OOJ_INITG_AGNCY_RECD_DATE	="NBS112";
	public static final String NBS113_CASE_MANAGEMENT_OOJ_INITG_AGNCY_OUTC_DUE_DATE	="NBS113";
	public static final String NBS114_CASE_MANAGEMENT_OOJ_INITG_AGNCY_OUTC_SNT_DATE	="NBS114";
	public static final String NBS140_CASE_MANAGEMENT_INIT_FOLL_UP	="NBS140";
	public static final String NBS141_CASE_MANAGEMENT_INIT_FOLL_UP_CLOSED_DATE	="NBS141";
	public static final String NBS142_CASE_MANAGEMENT_INTERNET_FOLL_UP	="NBS142";
	public static final String NBS143_CASE_MANAGEMENT_INIT_FOLL_UP_NOTIFIABLE	="NBS143";
	public static final String NBS144_CASE_MANAGEMENT_INIT_FOLL_UP_CLINIC_CODE	="NBS144";
	public static final String NBS146_CASE_MANAGEMENT_SURV_ASSIGNED_DATE	="NBS146";
	public static final String NBS147_CASE_MANAGEMENT_SURV_CLOSED_DATE	="NBS147";
	public static final String NBS148_CASE_MANAGEMENT_SURV_PROVIDER_CONTACT	="NBS148";
	public static final String NBS149_CASE_MANAGEMENT_SURV_PROV_EXM_REASON	="NBS149";
	public static final String NBS150_CASE_MANAGEMENT_SURV_PROV_DIAGNOSIS	="NBS150";
	public static final String NBS151_CASE_MANAGEMENT_SURV_PATIENT_FOLL_UP	="NBS151";
	public static final String NBS153_CASE_MANAGEMENT_STATUS_900	="NBS153";
	public static final String NBS155_CASE_MANAGEMENT_SUBJ_HEIGHT	="NBS155";
	public static final String NBS156_CASE_MANAGEMENT_SUBJ_SIZE_BUILD	="NBS156";
	public static final String NBS157_CASE_MANAGEMENT_SUBJ_HAIR	="NBS157";
	public static final String NBS158_CASE_MANAGEMENT_SUBJ_COMPLEXION	="NBS158";
	public static final String NBS159_CASE_MANAGEMENT_SUBJ_OTH_IDNTFYNG_INFO	="NBS159";
	public static final String NBS160_CASE_MANAGEMENT_FIELD_RECORD_NUMBER	="NBS160";
	public static final String NBS162_CASE_MANAGEMENT_FOLL_UP_ASSIGNED_DATE	="NBS162";
	public static final String NBS164_CASE_MANAGEMENT_INIT_FOLL_UP_ASSIGNED_DATE	="NBS164";
	public static final String NBS165_CASE_MANAGEMENT_FLD_FOLL_UP_PROV_EXM_REASON	="NBS165";
	public static final String NBS166_CASE_MANAGEMENT_FLD_FOLL_UP_PROV_DIAGNOSIS	="NBS166";
	public static final String NBS167_CASE_MANAGEMENT_FLD_FOLL_UP_NOTIFICATION_PLAN	="NBS167";
	public static final String NBS168_CASE_MANAGEMENT_FLD_FOLL_UP_EXPECTED_IN	="NBS168";
	public static final String NBS169_CASE_MANAGEMENT_FLD_FOLL_UP_EXPECTED_DATE	="NBS169";
	public static final String NBS170_CASE_MANAGEMENT_FLD_FOLL_UP_EXAM_DATE	="NBS170";
	public static final String NBS173_CASE_MANAGEMENT_FLD_FOLL_UP_DISPO	="NBS173";
	public static final String NBS174_CASE_MANAGEMENT_FLD_FOLL_UP_DISPO_DATE	="NBS174";
	public static final String NBS177_CASE_MANAGEMENT_ACT_REF_TYPE_CD	="NBS177";
	public static final String NBS178_CASE_MANAGEMENT_FLD_FOLL_UP_INTERNET_OUTCOME	="NBS178";
	public static final String NBS179_CASE_MANAGEMENT_OOJ_AGENCY	="NBS179";
	public static final String NBS180_CASE_MANAGEMENT_OOJ_NUMBER	="NBS180";
	public static final String NBS181_CASE_MANAGEMENT_OOJ_DUE_DATE	="NBS181";
	public static final String NBS182_CASE_MANAGEMENT_FIELD_FOLL_UP_OOJ_OUTCOME	="NBS182";
	public static final String NBS187_CASE_MANAGEMENT_INTERVIEW_ASSIGNED_DATE	="NBS187";
	public static final String NBS189_CASE_MANAGEMENT_INIT_INTERVIEW_ASSIGNED_DATE	="NBS189";
	public static final String NBS191_CASE_MANAGEMENT_EPI_LINK_ID	="NBS191";
	public static final String NBS192_CASE_MANAGEMENT_PAT_INTV_STATUS_CD	="NBS192";
	public static final String NBS196_CASE_MANAGEMENT_CASE_CLOSED_DATE	="NBS196";

	public static final String 	INV107_Public_Health_Case_jurisdiction_cd	="INV107";
	public static final String 	INV108_Public_Health_Case_PROG_AREA_CD	="INV108";
	public static final String 	INV109_Public_Health_Case_investigation_status_cd	="INV109";
	public static final String 	INV110_Public_Health_Case_INVESTIGATOR_ASSIGNED_TIME	="INV110";
	public static final String 	INV111_Public_Health_Case_rpt_form_cmplt_time	="INV111";
	public static final String 	INV112_Public_Health_Case_RPT_SOURCE_CD	="INV112";
	public static final String 	INV120_Public_Health_Case_rpt_to_county_time	="INV120";
	public static final String 	INV121_Public_Health_Case_RPT_TO_STATE_TIME	="INV121";
	public static final String 	INV128_Public_Health_Case_hospitalized_ind_cd	="INV128";
	public static final String 	INV132_Public_Health_Case_HOSPITALIZED_ADMIN_TIME	="INV132";
	public static final String 	INV133_Public_Health_Case_hospitalized_discharge_time	="INV133";
	public static final String 	INV134_Public_Health_Case_HOSPITALIZED_DURATION_AMT	="INV134";
	public static final String 	INV136_Public_Health_Case_diagnosis_time	="INV136";
	public static final String 	INV137_Public_Health_Case_EFFECTIVE_FROM_TIME	="INV137";
	public static final String 	INV138_Public_Health_Case_effective_to_time	="INV138";
	public static final String 	INV139_Public_Health_Case_EFFECTIVE_DURATION_AMT	="INV139";
	public static final String 	INV140_Public_Health_Case_effective_duration_unit_cd	="INV140";
	public static final String 	INV143_Public_Health_Case_PAT_AGE_AT_ONSET	="INV143";
	public static final String 	INV144_Public_Health_Case_pat_age_at_onset_unit_cd	="INV144";
	public static final String 	INV145_Public_Health_Case_OUTCOME_CD	="INV145";
	public static final String 	INV146_Public_Health_Case_deceased_time	="INV146";
	public static final String 	INV147_Public_Health_Case_ACTIVITY_FROM_TIME	="INV147";
	public static final String 	INV148_Public_Health_Case_day_care_ind_cd	="INV148";
	public static final String 	INV149_Public_Health_Case_FOOD_HANDLER_IND_CD	="INV149";
	public static final String 	INV150_Public_Health_Case_outbreak_ind	="INV150";
	public static final String 	INV151_Public_Health_Case_OUTBREAK_NAME	="INV151";
	public static final String 	INV152_Public_Health_Case_disease_imported_cd	="INV152";
	public static final String 	INV153_Public_Health_Case_IMPORTED_COUNTRY_CD	="INV153";
	public static final String 	INV154_Public_Health_Case_imported_state_cd	="INV154";
	public static final String 	INV155_Public_Health_Case_IMPORTED_CITY_DESC_TXT	="INV155";
	public static final String 	INV156_Public_Health_Case_imported_county_cd	="INV156";
	public static final String 	INV157_Public_Health_Case_TRANSMISSION_MODE_CD	="INV157";
	public static final String 	INV159_Public_Health_Case_detection_method_cd	="INV159";
	public static final String 	INV163_Public_Health_Case_CASE_CLASS_CD	="INV163";
	public static final String 	INV165_Public_Health_Case_mmwr_week	="INV165";
	public static final String 	INV166_Public_Health_Case_MMWR_YEAR	="INV166";
	public static final String 	INV167_Public_Health_Case_txt	="INV167";
	public static final String 	INV168_Public_Health_Case_LOCAL_ID	="INV168";
	public static final String 	INV169_Public_Health_Case_CD	="INV169";
	public static final String 	INV174_Public_Health_Case_shared_ind	="INV174";
	public static final String 	INV178_Public_Health_Case_pregnant_ind_cd	="INV178";
	public static final String 	INV2006_Public_Health_Case_ACTIVITY_TO_TIME	="INV2006";
	public static final String 	INV257_Public_Health_Case_PRIORITY_CD	="INV257";
	public static final String 	INV258_Public_Health_Case_INFECTIOUS_FROM_DATE	="INV258";
	public static final String 	INV259_Public_Health_Case_INFECTIOUS_TO_DATE	="INV259";
	public static final String 	INV260_Public_Health_Case_CONTACT_INV_STATUS	="INV260";
	public static final String 	INV261_Public_Health_Case_CONTACT_INV_TXT	="INV261";
	public static final String 	NBS012_Public_Health_Case_SHARED_IND	="NBS012";
	public static final String 	NBS055_Public_Health_Case_PRIORITY_CD	="NBS055";
	public static final String 	NBS056_Public_Health_Case_INFECTIOUS_FROM_DATE	="NBS056";
	public static final String 	NBS057_Public_Health_Case_INFECTIOUS_TO_DATE	="NBS057";
	public static final String 	NBS058_Public_Health_Case_CONTACT_INV_STATUS	="NBS058";
	public static final String 	NBS059_Public_Health_Case_CONTACT_INV_TXT	="NBS059";
	public static final String 	NBS110_Public_Health_Case_REFERRAL_BASIS_CD	="NBS110";
	public static final String 	NBS115_Public_Health_Case_CURR_PROCESS_STATE_CD	="NBS115";
	public static final String 	SUM100_Public_Health_Case_rpt_cnty_cd	="SUM100";
	public static final String 	SUM101_Public_Health_Case_mmwr_year	="SUM101";
	public static final String 	SUM102_Public_Health_Case_mmwr_week	="SUM102";
	public static final String 	SUM105_Public_Health_Case_txt	="SUM105";
	public static final String 	SUM106_Public_Health_Case_cd	="SUM106";
	public static final String 	SUM113_Public_Health_Case_rpt_form_cmplt_time	="SUM113";
	public static final String 	SUM115_Public_Health_Case_count_interval_cd	="SUM115";
	public static final String 	SUM116_Public_Health_Case_case_class_cd	="SUM116";
	public static final String 	NBS260_Referred_For_900_Test = "NBS260";
	public static final String 	NBS262_900_Test_Preformed = "NBS262";
	public static final String 	NBS265_Partner_Informed_Of_900_Result = "NBS265";
	public static final String 	NBS266_Refer_For_Care = "NBS266";
	public static final String 	NBS242PlacesToMeetPartner = "NBS242";
	public static final String 	NBS244PlacesToHaveSex = "NBS244";
	public static final String 	NBS192PatientInterviewStatus = "NBS192";
	public static final String 	NBS223FemalePartnersPastYear = "NBS223";
	public static final String 	NBS225MalePartnersPastYear = "NBS225";
	public static final String 	NBS227TransgenderPartnersPastYear = "NBS227";
	public static final String 	NBS129FemalePartnersInterviewPeriod = "NBS129";
	public static final String 	NBS131MalePartnersInterviewPeriod  = "NBS131";
	public static final String 	NBS133TransgenderPartnersInterviewPeriod  = "NBS133";
	public static final String 	NBS214SpeaksEnglish = "NBS214";
	public static final String 	FirstLabDateLAB201 = "LAB201_";
 
	
	public static final String CODED_VALUE	= "_CD";
	public static final String CODED_VALUE_TRANSLATED="_CDT";
	public static final String REPEAT_IND="_R";
	public static final String SEPERATOR="_";
	private static String delimiter1 = "__";
	private static String _1 = "_1";
	public static final String ORIGINAL_PATIENT_CONTACT_RECORD = "_OPCR";  //values from the Contact that started the Case

	//Provider Form Fields - Starting from the top left of the form
	private	static String ORD110_ORD111 = "ORD110_ORD111"; //T1LabOrderingFacilityOrT2MorbReportingOrgAddress
	private	static String ORD112 = "ORD112";//T1LabOrderingFacilityOrT2MorbReportingOrgCity
	private	static String ORD113 = "ORD113";//T1LabOrderingFacilityOrT2MorbReportingOrgState
	private	static String ORD114 = "ORD114";//T1LabOrderingFacilityOrT2MorbReportingOrgZip
	private	static String ORD121_ORD122 = "ORD121_ORD122";//T1LabOrderingFacilityOrT2MorbReportingOrgPhone with Ext

	private	static String STD121AnatomicSite = "STD121"; //hasClinicianObservedLesionsStdMap
	protected static String LAB220_R	= "LAB220_R";//LabTestName
	protected static String Lab_Result_ = "Lab_Result_R";
	protected static String ORD3_R = "ORD3_R";//LabReportingFacility
	protected static String LAB163_R = "LAB163_R";//Specimen date
	protected static String LAB165_R = "LAB165_R";//Specimen Source
	private	static String TR101 = "TR101"; //treatment
	private	static String DEM102 = "DEM102";//Last Nm
	private	static String DEM104 = "DEM104";//First_NM
	private	static String DEM250 = "DEM250";//NickNm
	private	static String MaidenName = "Maiden_Name";
	private	static String DEM159_DEM160= "DEM159_DEM160";//street1+street2 - there is a space in form
	private	static String DEM161 = "DEM161";//City
	private	static String DEM162 = "DEM162";//State
	private	static String DEM163 = "DEM163";//Zip
	private	static String NBS006 = "NBS006";//Patient Cell
	private	static String DEM177 = "DEM177";//Hm Ph
	private	static String INV2001 = "INV2001";//Reported Age
	private	static String DEM115 = "DEM115";//DOB
	private	static String DEM113_CD = "DEM113_CD";//Cur Sex
	private	static String NBS274_CD = "NBS274_CD";//Transgender
	private	static String NBS272_CD = "NBS272_CD";
	private	static String DEM152 = "DEM152"; //Sex Unk Reason
	private	static String DEM155 = "DEM155";//Ethnicity
	private	static String NBS273 = "NBS273";//Ethnicity Unknown Reason
	private	static String DEM140 = "DEM140";//Marital Status
	private	static String DEM197 = "DEM197";//Patient local id
	
	//Interview Related form fields
	private	static String NBS186="NBS186_";
	

	protected static PDDocument pdfDocument;
	public static final String nedssDirectory = new StringBuffer(System.getProperty("nbs.dir")).append(File.separator).toString().intern(); 
	public static final String propertiesDirectory = new StringBuffer(nedssDirectory).append("Properties").append(File.separator).toString().intern();
	public static final CachedDropDownValues cache = new CachedDropDownValues();
	public static Map<Object, Object> countryMap = cache.getCountyShortDescTxtCode();
	public static TreeMap<?, ?> countyCodes = null;

	protected static Map<Object,Object> questionMap = null;
	protected static Map<Object,Object> questionKeyMap = null;
	protected static Map<Object,Object> questionUidMap = null;
	protected static Map<Object,Object> contactQuestionMap = null;
	protected static Map<Object,Object> interviewQuestionMap = null;
	protected static Map<String, String> mappedInvRepeatValues = new HashMap<String, String>();
	protected static  Map<String, String> mappedDrugValues =new HashMap<String, String>();
	protected ActRelationshipDT actRelationshipDT = null;
	//protected static  Map<String, String> mappedLabValues = new HashMap<String, String>();
	//protected static Map<String, String> mappedTreatments =  new HashMap<String, String>();
	protected static Map<String, String> mappedDiagnosis = new HashMap<String, String>();
	protected static Map<String, String> mappedReferralBasis = new HashMap<String, String>();
	protected static Map<String, String>  mappedInterviewRecord =  new HashMap<String, String>();
	protected static Map<String, String> mappedOOJ =  new HashMap<String, String>();
	protected static Map<String, String> mappedMorbValues =new HashMap<String, String>();
	protected static 	Map<String, String> mappedEntityValues=null;
	protected static Map<String, String> mappedEtc=null;
	protected static Map<Object, Object>  answerMap =null;
	protected static PageForm pageForm = null;
	protected static PageActProxyVO proxyVO = null;
	protected static PageActProxyVO coProxyVO = null;
	protected static PageActProxyVO contactProxyVO = null;
	protected static PageActProxyVO contactcoProxyVO = null;
	private static Map<String, String> BirthSexMap= new HashMap<String, String>();
	private static Map<String, String> drugHistory = new HashMap<String, String>(); //NBS233_CDT, NBS235_CDT, NBS237_CDT, NBS239_CDT, NBS234_CDT, NBS236_CDT, NBS238_CDT, NBS240_CDT
	private static Map<String, String> CurrentSexMap= new HashMap<String, String>();
	private static Map<String, String> PreferredSexMap= new HashMap<String, String>();
	private static Map<String, String> SexUnknownReasonMap= new HashMap<String, String>();
	private static Map<String, String> DEM140PMaritalMap= new HashMap<String, String>();
	private static Map<String, String> DEM152RaceCodePRaceCatMap= new HashMap<String, String>();
	private static Map<String, String> DEM155PhvsEthnicitygroupCdcUnkMap= new HashMap<String, String>();
	private static Map<String, String> INV178YnuMap= new HashMap<String, String>();
	protected static Map<String, String> IXS105NbsInterviewTypeMap= new HashMap<String, String>();
	private static Map<String, String> NBS151SurveillancePatientFollowupMap= new HashMap<String, String>();
	private static Map<String, String> NBS273PEthnUnkReasonMap= new HashMap<String, String>();
	private static Map<String, String> STD121PhvsClinicianObservedLesionsStdMap= new HashMap<String, String>();
	protected static Map<String, String> NBS150CaseDiagnosisMap= new HashMap<String, String>();
	private static Map<String, String> NBS149ExamReasonMap = new HashMap<String, String>();
	private static Map<String, String> STD119PartnerInternet = new HashMap<String, String>();
	private static Map<String, String> YNRUDStdMisMap = new  HashMap<String, String>();  //NBS262
	private static Map<String, String> YNStdMisMap = new   HashMap<String, String>();  //NBS260, NBS265, NBS266
	protected static Map<String, String> YNStdReverseMisMap = new   HashMap<String, String>();  //NBS260, NBS265, NBS266
	protected static Map<String, String> NBS192_PatientInterviewStatusMap =  new   HashMap<String, String>();  //NBS192
	protected static Map<String, String> NBS192_R_PatientInterviewStatusMap =  new   HashMap<String, String>();  //NBS192
	protected static Map<String, String> NBS129FemalePartnersInterviewPeriodMap =  new   HashMap<String, String>();  //NBS129
	protected static Map<String, String> NBS131MalePartnersInterviewPeriodMap =  new   HashMap<String, String>();  //NBS131
	protected static Map<String, String> NBS133TransgenderPartnersInterviewPeriodMap =  new   HashMap<String, String>();  //NBS133
	protected static Map<String, String> NBS125OPSpouseMap =  new   HashMap<String, String>();  //NBS125
	protected static Map<String, String> NBS223FemalePartnersPastYearMap =  new   HashMap<String, String>();  //NBS223
	protected static Map<String, String> NBS225MalePartnersPastYearMap =  new   HashMap<String, String>();  //NBS225
	protected static Map<String, String> NBS227TransgenderPartnersPastYearMap =  new   HashMap<String, String>();  //NBS225
	protected static Map<String, String> NBS242PlacesToMeetPartnerMap =  new   HashMap<String, String>();  //NBS242
	protected static Map<String, String> NBS244PlacesToHaveSexMap =  new   HashMap<String, String>();  //NBS244
	protected static Map<String, String> DEM152MarginalRaceMap =  new   HashMap<String, String>();  //NBS244
	protected static Map<String, String> formSpecificQuestionMap= new HashMap<String, String>();
	protected static Map<String, String> formSpecificQuestionAnswerMap= new HashMap<String, String>();
	
	//protected static final int LAB_COUNTER = 5;
	
	/**
	 * Initialize the translation maps
	 */
	public static void initializeValues(){
		
		formSpecificQuestionMap= new HashMap<String, String>();
		
		if(BirthSexMap.size()==0){
			BirthSexMap.put("F", "F");//Female  
			BirthSexMap.put("M", "M");//Male  
			BirthSexMap.put("U", "D");//Unknown  
		}
		if(INV178YnuMap.size()==0){
			INV178YnuMap.put("Y", "Y");//Female  
			INV178YnuMap.put("N", "N");//Male  
			INV178YnuMap.put("UNK", "UNK");//Unknown  
		}
		if(drugHistory.size()==0)
		{
			drugHistory.put("ASKU", "U");//Asked but unknown
			drugHistory.put("D", "D");//Did not asked
			drugHistory.put("N", "N");//No
			drugHistory.put("R", "R");//Refused to answer
			drugHistory.put("Y", "Y");//Yes
		}
		if(CurrentSexMap.size()==0){
			CurrentSexMap.put("F", "F");//Female  
			CurrentSexMap.put("M", "M");//Male  
			CurrentSexMap.put("U", "U");//Unknown  
		}
		if(PreferredSexMap.size()==0){
			PreferredSexMap.put("MtF", "MtF");//Male to Female  
			PreferredSexMap.put("T", "T");//Trans 
			PreferredSexMap.put("FtM", "FtM");//Female to Male  
		}
		if(SexUnknownReasonMap.size()==0){
			SexUnknownReasonMap.put("R", "R");//refused to ans
			SexUnknownReasonMap.put("D", "D");//did not ask
		}
		if(DEM140PMaritalMap.size()==0){
			//DEM140PMaritalMap.put("A", " ");//Annulled @TODO change back x to " " 
			DEM140PMaritalMap.put("D", "D");//Divorced  
			//DEM140PMaritalMap.put("I", " ");//Interlocutory  @TODO change back x to " " 
			DEM140PMaritalMap.put("L", "L");//Legally separated  
			DEM140PMaritalMap.put("M", "M");//Married  
			//DEM140PMaritalMap.put("P", " ");//Polygamous   @TODO change back x to " " 
			DEM140PMaritalMap.put("R", "R");//Refused to answer  
			DEM140PMaritalMap.put("S", "S");//"Single, never married"  
			DEM140PMaritalMap.put("T", "T");//Domestic partner  
			DEM140PMaritalMap.put("U", "U");//Unknown  
			DEM140PMaritalMap.put("W", "W");//Widowed  
		}
		if(DEM152RaceCodePRaceCatMap.size()==0){
			DEM152RaceCodePRaceCatMap.put("1002-5", "AI/AN");//American Indian or Alaska Native  
			DEM152RaceCodePRaceCatMap.put("2028-9", "A");//Asian  
			DEM152RaceCodePRaceCatMap.put("2054-5", "B");//Black or African American  
			DEM152RaceCodePRaceCatMap.put("2076-8", "NH/PI");//Native Hawaiian or Other Pacific Islander  
			DEM152RaceCodePRaceCatMap.put("2106-3", "W");//White  
			DEM152RaceCodePRaceCatMap.put("2131-1", "O");//Other Race  
			DEM152RaceCodePRaceCatMap.put("NASK", "D");//not asked  
			DEM152RaceCodePRaceCatMap.put("PHC1175", "R");//Refused to answer  
			DEM152RaceCodePRaceCatMap.put("U", "U");//Unknown  
		}
		if(DEM152MarginalRaceMap.size()==0){
			DEM152MarginalRaceMap.put("1002-5", "AI/AN");//American Indian or Alaska Native  
			DEM152MarginalRaceMap.put("2028-9", "A");//Asian  
			DEM152MarginalRaceMap.put("2054-5", "B");//Black or African American  
			DEM152MarginalRaceMap.put("2076-8", "NH/PI");//Native Hawaiian or Other Pacific Islander  
			DEM152MarginalRaceMap.put("2106-3", "W");//White  
			DEM152MarginalRaceMap.put("2131-1", "O");//Other Race  
			DEM152MarginalRaceMap.put("NASK", "D");//not asked  
			DEM152MarginalRaceMap.put("PHC1175", "R");//Refused to answer  
			DEM152MarginalRaceMap.put("U", "U");//Unknown  
		}		
		if(DEM155PhvsEthnicitygroupCdcUnkMap.size()==0){
			DEM155PhvsEthnicitygroupCdcUnkMap.put("2135-2", "2135-2");//Hispanic or Latino  
			DEM155PhvsEthnicitygroupCdcUnkMap.put("2186-5", "2186-5");//Not Hispanic or Latino  
			DEM155PhvsEthnicitygroupCdcUnkMap.put("UNK", "UNK");//unknown  
		}

		if(IXS105NbsInterviewTypeMap.size()==0){
			IXS105NbsInterviewTypeMap.put("CLUSTER", "C");//Cluster  
			IXS105NbsInterviewTypeMap.put("INITIAL", "O");//Initial/Original  
			IXS105NbsInterviewTypeMap.put("REINTVW", "R");//Re-Interview  
		}
		if(NBS151SurveillancePatientFollowupMap.size()==0){
			NBS151SurveillancePatientFollowupMap.put("PHC149", "A");//admin close  
			NBS151SurveillancePatientFollowupMap.put("RSC", "R");//rec srch clos 
			NBS151SurveillancePatientFollowupMap.put("PHC54", "S");//ooj 
			NBS151SurveillancePatientFollowupMap.put("II", "I");//insuf info
			NBS151SurveillancePatientFollowupMap.put("NPP", "N");//not prog priority
			NBS151SurveillancePatientFollowupMap.put("FF", "F");//field followup
			NBS151SurveillancePatientFollowupMap.put("PC", "P");//physician closure
		}
		 
		/*if(NBS151SurveillancePatientFollowupMap.size()==0){
			NBS151SurveillancePatientFollowupMap.put("90213003", "?");//BFP - No Follow-up  
			NBS151SurveillancePatientFollowupMap.put("FF", "F");//Field Follow-up  
			NBS151SurveillancePatientFollowupMap.put("II", "I");//Insufficient Info  
			NBS151SurveillancePatientFollowupMap.put("PC", "P");//Physician Closure  
			NBS151SurveillancePatientFollowupMap.put("PHC149", "A");//Administrative Closure  
			NBS151SurveillancePatientFollowupMap.put("PHC54", "S");//Send OOJ  
			NBS151SurveillancePatientFollowupMap.put("RSC", "R");//Record Search Closure  
		}*/
		if(NBS273PEthnUnkReasonMap.size()==0){
			NBS273PEthnUnkReasonMap.put("0", "0");//Refused to answer  
			NBS273PEthnUnkReasonMap.put("6", "6");//Not asked  
		}
		if(STD121PhvsClinicianObservedLesionsStdMap.size()==0){
			STD121PhvsClinicianObservedLesionsStdMap.put("123851003", "G");//Mouth/Oral cavity  
			STD121PhvsClinicianObservedLesionsStdMap.put("18911002", "B");//Penis  
			STD121PhvsClinicianObservedLesionsStdMap.put("20233005", "C");//Scrotum  
			STD121PhvsClinicianObservedLesionsStdMap.put("22943007", "J");//Torso  
			STD121PhvsClinicianObservedLesionsStdMap.put("281088000", "A");//Anus/Rectum  
			STD121PhvsClinicianObservedLesionsStdMap.put("66019005", "K");//"Extremities (Arms, legs, feet, hands)"  
			STD121PhvsClinicianObservedLesionsStdMap.put("69536005", "I");//Head  
			STD121PhvsClinicianObservedLesionsStdMap.put("71252005", "E");//Cervix  
			STD121PhvsClinicianObservedLesionsStdMap.put("71836000", "F");//Nasopharynx  
			STD121PhvsClinicianObservedLesionsStdMap.put("76784001", "D");//Vagina  
			STD121PhvsClinicianObservedLesionsStdMap.put("PHC1161", "H");//Eye/conjunctiva  
			STD121PhvsClinicianObservedLesionsStdMap.put("PHC1168", "N");//No lesion noted  
			STD121PhvsClinicianObservedLesionsStdMap.put("PHC1170", "O");//Other anatomic site not represented in other defined anatomic sites  
			STD121PhvsClinicianObservedLesionsStdMap.put("UNK ", "U");//Unknown  
		}
		
		if(NBS150CaseDiagnosisMap.size()==0){
			NBS150CaseDiagnosisMap.put("10273", "100");//Chancroid  
			NBS150CaseDiagnosisMap.put("10274", "200");//Chlamydia trachomatis infection  
			NBS150CaseDiagnosisMap.put("10280", "300");//Gonorrhea  
			NBS150CaseDiagnosisMap.put("10280", "350");//Gonorrhea  
			NBS150CaseDiagnosisMap.put("10307", "400");//Nongonococcal urethritis (NGU)  
			NBS150CaseDiagnosisMap.put("71011005", "410");//Infestation by Phthirus pubis (disorder)  
			NBS150CaseDiagnosisMap.put("128870005", "420");//Crusted scabies (disorder)  
			NBS150CaseDiagnosisMap.put("10308", "450");//Mucopurulent cervicitis (MPC)  
			NBS150CaseDiagnosisMap.put("419760006", "460");//Bacterial vaginosis (disorder)  
			NBS150CaseDiagnosisMap.put("35089004", "470");//Urogenital infection by Trichomonas vaginalis (disorder)  
			NBS150CaseDiagnosisMap.put("78048006", "480");//Candidiasis (disorder)  
			NBS150CaseDiagnosisMap.put("10309", "490");//"Pelvic Inflammatory Disease (PID), Unknown Etiology"  
			NBS150CaseDiagnosisMap.put("10276", "500");//Granuloma inguinale (GI)  
			NBS150CaseDiagnosisMap.put("10306", "600");//Lymphogranuloma venereum (LGV)  
			NBS150CaseDiagnosisMap.put("10311", "710");//"Syphilis, primary"  
			NBS150CaseDiagnosisMap.put("10312", "720");//"Syphilis, secondary"  
			NBS150CaseDiagnosisMap.put("10313", "730");//"Syphilis, early latent"  
			NBS150CaseDiagnosisMap.put("10315", "740");//"Syphilis, unknown latent"  
			NBS150CaseDiagnosisMap.put("10314", "745");//"Syphilis, late latent"  
			NBS150CaseDiagnosisMap.put("10319", "750");//"Syphilis, late with clinical manifestations (including late benign syphilis and cardiovascular syphilis)"  
			NBS150CaseDiagnosisMap.put("266113007", "800");//Genital warts (disorder)  
			NBS150CaseDiagnosisMap.put("423391007", "850");//Genital herpes simplex type 2 (disorder)  
			NBS150CaseDiagnosisMap.put("900", "900");//900 - HIV infection  
			NBS150CaseDiagnosisMap.put("10560", "950");//AIDS  
		}
		
		if(NBS149ExamReasonMap.size()==0){
			NBS149ExamReasonMap.put("S", "S");//Symptomatic
			NBS149ExamReasonMap.put("A", "A");//Asymptomatic
			NBS149ExamReasonMap.put("C", "C");//Contact to STD Exposure to Sexually Transmissible Disorder
			NBS149ExamReasonMap.put("P", "P");//Prenatal
			NBS149ExamReasonMap.put("D", "D");//Delivery
			NBS149ExamReasonMap.put("I", "I");//Institutional Screening
			NBS149ExamReasonMap.put("M", "M");//Community Screening
			NBS149ExamReasonMap.put("H", "H");//Health Dept Screening
			NBS149ExamReasonMap.put("U", "U");//Unk
			// NBS149ExamReasonMap.put("183688007", "Not In List"); //self referral 
		}
		if(STD119PartnerInternet.size()==0){
			STD119PartnerInternet.put("N", "N");//No
			STD119PartnerInternet.put("NASK", "NASK");//Did not asked
			STD119PartnerInternet.put("R", "R");//Refused to answer
			STD119PartnerInternet.put("U", "NASK");//Unknown
			STD119PartnerInternet.put("Y", "Y");//Yes
		}
/*		if(NBS110ReferralBasisMap.size()==0){
			NBS110ReferralBasisMap.put("P1", "P");//Check if NBS110 (Referral Basis) = P1, P2 or P3 and named in Contact Record
			NBS110ReferralBasisMap.put("P2", "P");//Check if NBS110 (Referral Basis) = P1, P2 or P3 and named in Contact Record
			NBS110ReferralBasisMap.put("P3", "P");//Check if NBS110 (Referral Basis) = P1, P2 or P3 and named in Contact Record
			NBS110ReferralBasisMap.put("S1", "A");//Check if NBS110 (Referral Basis) = S1, S2, S3, A1, A2, A3 or C1 and named in Contact Record
			NBS110ReferralBasisMap.put("S2", "A");//Check if NBS110 (Referral Basis) = S1, S2, S3, A1, A2, A3 or C1 and named in Contact Record
			NBS110ReferralBasisMap.put("S3", "A");//Check if NBS110 (Referral Basis) = S1, S2, S3, A1, A2, A3 or C1 and named in Contact Record
			NBS110ReferralBasisMap.put("A1", "A");//Check if NBS110 (Referral Basis) = S1, S2, S3, A1, A2, A3 or C1 and named in Contact Record
			NBS110ReferralBasisMap.put("A2", "A");//Check if NBS110 (Referral Basis) = S1, S2, S3, A1, A2, A3 or C1 and named in Contact Record
			NBS110ReferralBasisMap.put("A3", "A");//Check if NBS110 (Referral Basis) = S1, S2, S3, A1, A2, A3 or C1 and named in Contact Record
			NBS110ReferralBasisMap.put("C1", "A");//Check if NBS110 (Referral Basis) = S1, S2, S3, A1, A2, A3 or C1 and named in Contact Record
			NBS110ReferralBasisMap.put("T1", "T");//Check if NBS110 (Referral Basis) = T1 or T2
			NBS110ReferralBasisMap.put("T2", "T");//Check if NBS110 (Referral Basis) = T1 or T2
			NBS110ReferralBasisMap.put("P", "O");//Check if NBS110 (Referral Basis) = P1, P2, P3, S1, S2, S3, A1, A2, A3 or C1 and the investigation was started from the Patient File. 
			NBS110ReferralBasisMap.put("A", "O");//Check if NBS110 (Referral Basis) = P1, P2, P3, S1, S2, S3, A1, A2, A3 or C1 and the investigation was started from the Patient File. 
		}
		if(NBS110ReferralBasisOriginMap.size()==0){
			NBS110ReferralBasisOriginMap.put("P", "NBS110_P_CD");//Print actual referral basis code if Referral Basis is P1, P2 or P3 and named in Contact Record. If co-infection is selected, separate the referral bases by a ","
			NBS110ReferralBasisOriginMap.put("A", "NBS110_A_CD");//Print actual referral basis code if Referral Basis is S1, S2, S3, A1, A2, A3 or C1 and named in Contact Record. If co-infection is selected, separate the referral bases by a ","
			NBS110ReferralBasisOriginMap.put("T", "NBS110_T_CD");//Print actual referral basis code if Referral Basis is  T1 or T2.  If co-infection is selected, separate the referral bases by a ","
			NBS110ReferralBasisOriginMap.put("O", "NBS179_O_CD");//Print actual referral basis code if Referral Basis is P1, P2, P3, S1, S2, S3, A1, A2, A3 or C1 and the investigation was started from the Patient File (not from contact record). If co-infection is selected, separate the referral bases by a ","
			
		}*/
		if(YNRUDStdMisMap.size()==0){
			YNRUDStdMisMap.put("Y", "1");//Y=1 
			YNRUDStdMisMap.put("N", "0");//N=0 
			YNRUDStdMisMap.put("R", "9");//R=9 Refuse
			YNRUDStdMisMap.put("U", "9");//U=9 STDMIS default 
			YNRUDStdMisMap.put("NASK", "9");//Not ASK=9 
		}
		if(YNStdMisMap.size()==0){
			YNStdMisMap.put("Y", "1");//Y=1 
			YNStdMisMap.put("N", "0");//N=0 
		}
		
		if(YNStdReverseMisMap.size()==0){
			YNStdReverseMisMap.put("1", "Y");//Y=1 
			YNStdReverseMisMap.put("0", "N");//N=0 
		}		
		if(NBS192_PatientInterviewStatusMap.size()==0){
			NBS192_PatientInterviewStatusMap.put("A", "N");//Awaiting
			NBS192_PatientInterviewStatusMap.put("D", "N");//Died
			NBS192_PatientInterviewStatusMap.put("I", "Y");//Interviewed
			NBS192_PatientInterviewStatusMap.put("J", "N");//OOJ
			NBS192_PatientInterviewStatusMap.put("L", "N");//Language Barrier
			NBS192_PatientInterviewStatusMap.put("N", "N");//No CLuster Ix
			NBS192_PatientInterviewStatusMap.put("O", "N");//Other
			NBS192_PatientInterviewStatusMap.put("P", "N");//Physician Refusal
			NBS192_PatientInterviewStatusMap.put("R", "N");//Refused	
			NBS192_PatientInterviewStatusMap.put("U", "N");//Unable to Locate
		}
		
		if(NBS192_R_PatientInterviewStatusMap.size()==0){
			//NBS192R_PatientInterviewStatusMap.put("A", "");//Awaiting
			NBS192_R_PatientInterviewStatusMap.put("D", "D");//Died
			//NBS192R_PatientInterviewStatusMap.put("I", "");//Interviewed
			NBS192_R_PatientInterviewStatusMap.put("J", "O");//OOJ
			NBS192_R_PatientInterviewStatusMap.put("L", "L");//Language Barrier
			NBS192_R_PatientInterviewStatusMap.put("N", "N");//No CLuster Ix
			NBS192_R_PatientInterviewStatusMap.put("O", "O");//Other
			NBS192_R_PatientInterviewStatusMap.put("P", "P");//Physician Refusal
			NBS192_R_PatientInterviewStatusMap.put("R", "R");//Refused	
			NBS192_R_PatientInterviewStatusMap.put("U", "O");//Unable to Locate
		}
		if(NBS242PlacesToMeetPartnerMap.size()==0){
			//NBS242PlacesToMeetPartner.put("Y", "");//Y=1 
			//NBS242PlacesToMeetPartner.put("N", "");//N=0 
			NBS242PlacesToMeetPartnerMap.put("R", "R");//R=9 Refuse
			NBS242PlacesToMeetPartnerMap.put("U", "U");//U=9 STDMIS default unknown Did not Ask
		}		
		if(NBS244PlacesToHaveSexMap.size()==0){
			//NBS244PlacesToHaveSexMap.put("Y", "");//Y=1 
			//NBS244PlacesToHaveSexMap.put("N", "");//N=0 
			NBS244PlacesToHaveSexMap.put("R", "R");//R=9 Refuse
			NBS244PlacesToHaveSexMap.put("U", "U");//U=9 STDMIS default unknown Did not Ask
		}
		if (NBS223FemalePartnersPastYearMap.size()==0){
			//NBS223FemalePartnersPastYearMap.put("Y", "");//Y=1 
			//NBS223FemalePartnersPastYearMap.put("N", "");//N=0 
			NBS223FemalePartnersPastYearMap.put("R", "R");//Refuse
			NBS223FemalePartnersPastYearMap.put("U", "U");//STDMIS default unknown
		}
		if (NBS225MalePartnersPastYearMap.size()==0){
			//NBS225MalePartnersPastYearMap.put("Y", "");//Y=1 
			//NBS225MalePartnersPastYearMap.put("N", "");//N=0 
			NBS225MalePartnersPastYearMap.put("R", "R");//Refuse
			NBS225MalePartnersPastYearMap.put("U", "U");//STDMIS default unknown
		}
		if (NBS227TransgenderPartnersPastYearMap.size()==0){
			//NBS227TransgenderPartnersPastYearMap.put("Y", "");//Y=1 
			//NBS227TransgenderPartnersPastYearMap.put("N", "");//N=0 
			NBS227TransgenderPartnersPastYearMap.put("R", "R");//Refuse
			NBS227TransgenderPartnersPastYearMap.put("U", "U");//STDMIS default unknown
		}
		if (NBS129FemalePartnersInterviewPeriodMap.size()==0){
			//NBS129FemalePartnersInterviewPeriodMap.put("Y", "");//Y=1 
			//NBS129FemalePartnersInterviewPeriodMap.put("N", "");//N=0 
			NBS129FemalePartnersInterviewPeriodMap.put("R", "R");//Refuse
			NBS129FemalePartnersInterviewPeriodMap.put("U", "U");//STDMIS default unknown
		}
		if (NBS131MalePartnersInterviewPeriodMap.size()==0){
			//NBS131MalePartnersInterviewPeriodMap.put("Y", "");//Y=1 
			//NBS131MalePartnersInterviewPeriodMap.put("N", "");//N=0 
			NBS131MalePartnersInterviewPeriodMap.put("R", "R");//Refuse
			NBS131MalePartnersInterviewPeriodMap.put("U", "U");//STDMIS default unknown
		}
		if (NBS133TransgenderPartnersInterviewPeriodMap.size()==0){
			//NBS133TransgenderPartnersInterviewPeriodMap.put("Y", "");//Y=1 
			//NBS133TransgenderPartnersInterviewPeriodMap.put("N", "");//N=0 
			NBS133TransgenderPartnersInterviewPeriodMap.put("R", "R");//Refuse
			NBS133TransgenderPartnersInterviewPeriodMap.put("U", "U");//STDMIS default unknown
		}		
		if(NBS125OPSpouseMap .size()==0){ //Contact Orig Patient Spouse?
			NBS125OPSpouseMap.put("C", "Y");//yes -current  
			NBS125OPSpouseMap.put("F", "Y");//yes - former  
			NBS125OPSpouseMap.put("N", "N");//no
			NBS125OPSpouseMap.put("U", "U");//unk
			//NBS125OPSpouseMap .put("R", "R");//not supported
		}
	}

	/**
	 * SetField value (set the value in PDF form field)
	 * @param field
	 * @param name
	 * @param value
	 * @throws NEDSSAppException
	 */
	static void setField(PDField field, String name, String value)throws NEDSSAppException {
		setField(field,name,value, Boolean.TRUE);
	}
	
	/**
	 * SetField value to PDF form field to populate value in PDF printout
	 * @param field
	 * @param name
	 * @param value
	 * @param grid
	 * @throws NEDSSAppException
	 */
	static void setField(PDField field, String name, String value,Boolean grid) throws NEDSSAppException {
		try {
			if (field != null && value!=null
					&& field.getClass()
							.getName()
							.equals("org.apache.pdfbox.pdmodel.interactive.form.PDTextbox")) {
				if(grid){
				value = setValueForGridFields(name,value);
				}
				field.setValue(value);

			} else if (field != null
					&& field.getClass()
					.getName()
					.equals("org.apache.pdfbox.pdmodel.interactive.form.PDCheckbox")) {
					logger.debug("PDCheckbox field name:" + field.getPartialName());
						//((PDCheckbox) field).check();
						field.setValue(value);
						
		
			} else if (field != null && value!=null
					&& field.getClass()
							.getName()
							.equals("org.apache.pdfbox.pdmodel.interactive.form.PDRadioButton")) {
				logger.debug("PDRadioCollection field name:" + field.getPartialName());
				if( field.getPartialName().equals("STDLAB121_CD_1")){
					logger.debug("PDRadioCollection field name:" + field.getPartialName());
				}
				
				try{
				field.setValue(value);
				}catch(IllegalArgumentException e){
					logger.error("setField: Error while setting field values for name:"+ name  +"and value="+value +" and IOException raises: " + e);
					throw new NEDSSAppException("setField: IllegalArgumentException Error while setting field values for name:",e);
				}
				
				List<PDAnnotationWidget> widgets = field.getWidgets();
				if(widgets!=null)
				for(int i=0; i<widgets.size();i++){
					Set<COSName> COSNames=	 ((COSDictionary)widgets.get(i).getAppearance().getNormalAppearance().getCOSObject()).keySet();
					String COSNameKey = ((COSName)COSNames.toArray()[0]).getName();
					if(COSNameKey.trim().equalsIgnoreCase(value.trim()) ){
						field.setValue(COSNameKey);
					}
				}
			} else {
				logger.debug("No field found with name:" + name);
				if(value != null) field.setValue(value);
			}
		} catch (IOException e) {
			logger.error("setField: Error while setting field values for name:"+ name  +"and value="+value +" and IOException raises: " + e);
			throw new NEDSSAppException("setField:IOException Error while setting field values for name:",e);
		}
	}
	
	/**
	 * Load the questions contained in the specified form.
	 * @param formCd
	 */
	@SuppressWarnings("unchecked")
	public static void loadQuestionMap(String formCd){
		questionMap = (Map<Object, Object> )QuestionsCache.getDMBQuestionMap().get(formCd);
		questionUidMap = new HashMap<Object, Object>();
		questionKeyMap = new HashMap<Object, Object>();
		Set set = questionMap.keySet();
		if(set!=null){
			Iterator it = set.iterator();
			while(it.hasNext()){
				String key = (String)it.next();
				NbsQuestionMetadata nbsQuestionDT= (NbsQuestionMetadata)questionMap.get(key);
				if (nbsQuestionDT == null)
				{
					logger.debug("CommonPDFPrintForm: Key not found in question map ");
					continue;
				}
				Long questionUid = nbsQuestionDT.getNbsQuestionUid();
				String questionIdentifier = nbsQuestionDT.getQuestionIdentifier();
				if (questionUid != null && nbsQuestionDT != null) {
					questionUidMap.put(questionUid, nbsQuestionDT);
					if (questionIdentifier != null)
						questionKeyMap.put((Object)questionUid, (Object)questionIdentifier);
				}
			}
		}
		
		
		initializeValues();
	}
	
	/**
	 * Load the questions associated with the contact form
	 * @param formCd
	 */
	@SuppressWarnings("unchecked")
	public static void loadContactQuestionMap(String formCd){
		contactQuestionMap = (Map<Object, Object> )QuestionsCache.getDMBQuestionMap().get(formCd);
	}
	
	/**
	 * Load the questions associated with the generic interview form
	 * @param formCd
	 */
	@SuppressWarnings("unchecked")
	public static void loadInterviewQuestionMap(String formCd){
		interviewQuestionMap = (Map<Object, Object> )QuestionsCache.getDMBQuestionMap().get(formCd);	
	}
	
	/**
	 * Get the questions associated with the Investigation Form
	 * @param formCd
	 */
	public static void addQuestionMap(String formCd){
		if (formCd != null && !formCd.isEmpty()){
			 @SuppressWarnings("unchecked")
			Map<Object,Object> questionMapToAdd = (Map<Object, Object> )QuestionsCache.getDMBQuestionMap().get(formCd);
			 questionMap.putAll(questionMapToAdd);
		}
	}
	
	/**
	 * Populate treatment values from the Investigation and/or the Morb Report.
	 * @param actProxyVO
	 * @param session
	 * @throws NEDSSAppException
	 */
	public static void populateTreatmentValuesWithData(PageActProxyVO actProxyVO, HttpSession session) throws NEDSSAppException {
		try {
			Collection<Object> coll = actProxyVO.getTheTreatmentSummaryVOCollection();
			processTreatmentCollection(coll, null, session);
			Collection<Object> morbColl =actProxyVO.getTheMorbReportSummaryVOCollection();
			Iterator<Object> it = morbColl.iterator();
			while(it.hasNext()){
				//Per JW, use the Provider name from the Morb Report, don't have a performing provider
				MorbReportSummaryVO morbReportSummaryVO=(MorbReportSummaryVO)it.next();
				processTreatmentCollection(morbReportSummaryVO.getTheTreatmentSummaryVOColl(),
						morbReportSummaryVO.getProviderFullNameForPrint(), session);
			}

			
		}catch (Exception e) {
			logger.error("Error while retrieving populateTreatmentValues:  "+ e);
			throw new NEDSSAppException("Error while retrieving populateTreatmentValues:  "+ e);
		}
	}
	

	/**
	 * Process the TreatmentSummaryVO collection from the Inv or Morb Report.
	 * @param coll
	 * @param session
	 * @return
	 * @throws NEDSSAppException
	 */
	private static int processTreatmentCollection(Collection<Object> coll,  String morbProviderFullName, HttpSession session) throws NEDSSAppException{
		try {
			
			if(treatmentIndex >4)
				return treatmentIndex;
			if (coll != null) {
				Iterator<Object> it = coll.iterator();
				
				while (it.hasNext()) {
					TreatmentSummaryVO treamentSummVO = (TreatmentSummaryVO) it.next();
					// treamentSummVO.getNbsDocumentUid()==null added to eliminate treatments coming from PHDC documents.
					if(!treatmentCollection.contains(treamentSummVO.getTreatmentUid()) && treamentSummVO.getNbsDocumentUid() == null ){
						StringBuffer tratmentValues= new StringBuffer();
						String dateValue = StringUtils.formatDate(treamentSummVO.getActivityFromTime());
						if (dateValue == null || dateValue.isEmpty())
							dateValue = StringUtils.formatDate(treamentSummVO.getActivityToTime()); //Morb Report puts date in to time.
						String treatmentInfo = getTreatmentNameCode(treamentSummVO).toString();;
						tratmentValues.append(checkNull(dateValue));
						tratmentValues.append(checkNull(treatmentInfo));
						String sBeanJndiName = JNDINames.TREATMENT_PROXY_EJB;
				        String sMethod = "getTreatmentProxy";
						Object[] oParams ={treamentSummVO.getTreatmentUid()};
						TreatmentProxyVO treatmentProxyVO =(TreatmentProxyVO)CallProxyEJB.callProxyEJB(oParams , sBeanJndiName, sMethod, session);
						PersonVO provider =  PageLoadUtil.getPerson(NEDSSConstants.PROVIDER_OF_TREATMENT, treatmentProxyVO.getTheTreatmentVO().getTheParticipationDTCollection(), treatmentProxyVO.getThePersonVOCollection());
						String providerFullName = EMPTY_STRING;
						if(provider != null){
							providerFullName = provider.getThePersonDT().getFullName();
							if (providerFullName != null && !providerFullName.isEmpty()){
								if(providerFullName.contains(",")){
									String firstName = providerFullName.substring(providerFullName.indexOf(",")).replace(",", "");
									String lastName = providerFullName.substring(0,providerFullName.indexOf(","));
									providerFullName=firstName+" "+lastName;
								}
									
								tratmentValues.append(", ").append(providerFullName);
							}
						}
						if (morbProviderFullName != null && !morbProviderFullName.isEmpty() &&
								(providerFullName == null || providerFullName.isEmpty())) { //use provider from Morb Rpt
							tratmentValues.append(", ").append(morbProviderFullName);
						}
						treatmentCollection.add(treatmentProxyVO.getTheTreatmentVO().getTheTreatmentDT().getTreatmentUid());
						formSpecificQuestionAnswerMap.put(TR101+REPEAT_IND+treatmentIndex, tratmentValues.toString());
						treatmentIndex++;
					}
					
				}
			} 
			return treatmentIndex;
		} catch (NEDSSAppException e) {
			logger.error("Error while retrieving processTreatmentCollection:  "+ e);
			throw new NEDSSAppException("Error while processTreatmentCollection:  "+ e);
		}
	}
	
	
	/**
	 * Process the pdf field if we have an answer for it in the Form Specific Question Answer Map
	 * @param pdfField
	 * @param i - no longer used, used to indicate main proxy or coinf proxy
	 * @throws NEDSSAppException
	 */
	protected static void processPDFFIelds( PDField pdfField, int i) throws NEDSSAppException{
		
	//For making visible the fields with format date or number
	pdfDocument.getDocumentCatalog().getAcroForm().setNeedAppearances(true);
		
	String value="";
		try {
			if(pdfField != null){
				pdfField.setReadOnly(true);
				String key =pdfField.getPartialName();
				if(formSpecificQuestionAnswerMap!=null && formSpecificQuestionAnswerMap.get(key)!=null ){
					 value=formSpecificQuestionAnswerMap.get(key);
					setField(pdfField,key,value);
				}
			}
		} catch (Exception e) {
			 e.printStackTrace();
	            throw new NEDSSAppException("Error while loading printLoad Page Case: " + e.getMessage(), e);
		}

	}
	
	/**
	 * 
	 * @param pageForm
	 * @param req
	 * @return
	 * @throws NEDSSAppException
	 */
	public static PageActProxyVO viewPrintLoadUtil(PageForm pageForm, HttpServletRequest req)  throws NEDSSAppException{
		//PageActProxyVO proxyVO =null;
		try {
			labIndex=1;
			coProxyVO=null;
			treatmentIndex=1;
			treatmentCollection=new ArrayList<Object>();
			labStagingArray = new String[8][6];

			// Call Common framework
			HttpSession session = req.getSession();
			pageForm.setActionMode(NEDSSConstants.VIEW_LOAD_ACTION);
			pageForm.setFormFieldMap(new HashMap<Object, Object>());
			String invFormCd = pageForm.getPageFormCd();
			PageLoadUtil.loadQuestions(invFormCd);
			PageLoadUtil.loadQuestionKeys(invFormCd);
			if(proxyVO!=null){
				Long publicHealthCaseUID = new Long((String)NBSContext.retrieve(session,
						NBSConstantUtil.DSInvestigationUid));
				Object[] oParams = new Object[] { NEDSSConstants.PRINT_CDC_CASE, publicHealthCaseUID };
				proxyVO =(PageActProxyVO)CallProxyEJB.callProxyEJB(oParams , JNDINames.PAGE_PROXY_EJB, 
								"getPageProxyVO", session);
			}
			
			if(pageForm.getCoinfCondInvUid()!= null && !pageForm.getCoinfCondInvUid().equals("0")){
				Long coinfectionUid= new Long(pageForm.getCoinfCondInvUid());
				getCoinfectionVO( coinfectionUid, req);
			}
		
		
			// Load common PAT, INV answers and put it in answerMap for UI & Rules
			// to work
			PageLoadUtil.setCommonAnswersForViewEdit(pageForm, proxyVO, req);
			 //Pam Specific Answers
			PageLoadUtil.setMSelectCBoxAnswersForViewEdit(pageForm, 
					updateMapWithQIds(((PageActProxyVO) proxyVO).getPageVO()
							.getPamAnswerDTMap()));
			//GST -NBSCentral 8855
			pageForm.getPageClientVO().setOldPageProxyVO(proxyVO);
			
			contactProxyVO=null;//for avoiding get the data form the previous investigation printed
			contactcoProxyVO=null;//for avoiding get the data form the previous investigation printed

		
			return proxyVO;
			}catch (Exception e) {
				 throw new NEDSSAppException("viewPrintLoadUtil Error while loading printLoad Page Case: " + e.getMessage(), e);
			}
	}
	
	
	
	/**TODO REVIEW PENDING**/	
	private static String  setValueForGridFields(String fieldName, String value) throws NEDSSAppException {
		try {
			if (fieldName.contains(NBS217) 
					||fieldName.contains(NBS219)
					||fieldName.contains(NBS130)
					||fieldName.contains(NBS132)
					||fieldName.contains(NBS134)
					||fieldName.contains(NBS136)
					||fieldName.contains(NBS250)
					){
				value = addEmptySpaces(value,"  ");
			}
			
			if(fieldName.contains(DEM118)){
				value =value.length()<2? "0" + value : value;
				value =value.length()<3? "0" + value : value;
				value =addEmptySpaces(value," ");
			}
			return value;
		} catch (Exception e) {
			throw new NEDSSAppException("CommonPDFPrintForm.setValueForGridFields, Exception thrown",e);
		}
	}
	
	/**
	 * processRepeatingQuestions method populates the repeating questions that are part of a batch(repeating questions) entry
	 * @param repeatingAnswerMap
	 * @return
	 * @throws Exception
	 */
	public static Map<String, String> processRepeatingQuestions(Map<Object, Object> repeatingAnswerMap, int investigationRepeatNumber) throws NEDSSAppException {
		Map<String, String> returnMap = new HashMap<String, String>();
		NbsQuestionMetadata metadata =null;
		try {
			Map<Object, Object> map = PageLoadUtil.questionMap;
			if (map != null) {
				Collection<Object> mappedQuestions = map.values();
				Iterator<Object> iter = mappedQuestions.iterator();
				while (iter.hasNext()) {
					metadata = (NbsQuestionMetadata) iter
							.next();
					logger.error(metadata.toString());
					if(metadata.getQuestionGroupSeqNbr()!=null && metadata.getDataLocation()!=null && metadata.getQuestionIdentifier()!=null 
							&& metadata.getDataLocation().equals(NEDSSConstants.DATA_LOCATION_CASE_ANSWER_TEXT)){
						if(questionMap.containsKey(metadata.getQuestionIdentifier())){
							populateRepeatData(repeatingAnswerMap, metadata);
						}
					}
					
					
				}
			}
		} catch (Exception e) {
			logger.error(metadata);
			logger.error(e);
			logger.error("CommonPDFPrintForm.processRepeatingQuestions Exception thrown, " + e);
			throw new NEDSSAppException("CommonPDFPrintForm.processRepeatingQuestions, Exception thrown",e);
		}
		return returnMap;
	}


	/**
	 * Get Entity Values from the Participant collection.
	 * Populates entity values in formSpecificQuestionAnswerMap
	 * @param theProxyVO
	 * @param invCnt - 1 for inv, 2 for coinfection
	 * @throws NEDSSAppException
	 */
	public static void populateEntities(PageActProxyVO theProxyVO, int invCnt) throws NEDSSAppException {
		try {
			PageActProxyVO actProxyVO = (PageActProxyVO) theProxyVO;
			Long patientUid = null;
			Long assignedToUid = null;
			Long physicianUid = null;
			Long interviewerOfPHC=null;
			Long initInterviewerOfPHC=null;
			Collection<Object> partColl = actProxyVO.getPublicHealthCaseVO().getTheParticipationDTCollection();
			if (partColl != null) {
				Iterator<Object> it = partColl.iterator();
				while (it.hasNext()) {
					ParticipationDT partDT = (ParticipationDT) it.next();
					if (partDT.getTypeCd().equals(NEDSSConstants.PHC_PATIENT)) {
						patientUid = partDT.getSubjectEntityUid();
					} else if (partDT.getTypeCd().equals(
							NEDSSConstants.Surveillance_Investigator_OF_PHC)) {
						assignedToUid = partDT.getSubjectEntityUid();
					} else if (partDT.getTypeCd().equals(
							NEDSSConstants.PHC_PHYSICIAN)) {
						physicianUid = partDT.getSubjectEntityUid();
					}else if (partDT.getTypeCd().equals(
							NEDSSConstants.INERVIEWER_OF_PHC)) {
						interviewerOfPHC = partDT.getSubjectEntityUid();
					}else if (partDT.getTypeCd().equals(
							NEDSSConstants.INIT_INERVIEWER_OF_PHC)) {
						initInterviewerOfPHC = partDT.getSubjectEntityUid();
					}
				}
			}
			//mapInterviewerData
			PersonVO interviewerVO= null;
			PersonVO initInterviewerVO= null;
			Collection<Object> coll = actProxyVO.getThePersonVOCollection();
			if (coll != null) {
				boolean isAssignedToDataPopulated= false;
				boolean isPhysicianDataPopulated= false;
				
				Iterator<Object> it = coll.iterator();
				while (it.hasNext()) {
					PersonVO personVO = (PersonVO) it.next();
					if (assignedToUid != null && personVO.getThePersonDT().getPersonUid().compareTo(assignedToUid) == 0 && !isAssignedToDataPopulated) {
						Collection<Object> entityColl = personVO
								.getTheEntityIdDTCollection();
						if (entityColl != null) {
							Iterator<Object> iter = entityColl.iterator();
							while (iter.hasNext()) {
								EntityIdDT entityDT = (EntityIdDT) iter.next();
								if (entityDT.getTypeCd().equals(NEDSSConstants.ENTITY_TYPECD_QEC)) {
									if(formSpecificQuestionMap.containsKey(NBS145_+ invCnt) && entityDT.getRootExtensionTxt() != null && !entityDT.getRootExtensionTxt().isEmpty()){
										//formSpecificQuestionAnswerMap.put(NBS145_+ invCnt, entityDT.getRootExtensionTxt());
										formSpecificQuestionAnswerMap.put(NBS145_ + invCnt,
												entityDT.getRootExtensionTxt());
										isAssignedToDataPopulated= true;
									}
								}
							}
						}
						continue;
					} else if (invCnt == 1 && personVO.getThePersonDT().getPersonUid().compareTo(patientUid) == 0) {
						PersonDT patientDT = personVO.getThePersonDT();
						if (patientDT.getAgeReported() != null  && formSpecificQuestionMap.containsKey(INV2001)){
							formSpecificQuestionAnswerMap.put(INV2001, patientDT.getAgeReported().toString());
						}
						if (patientDT.getAgeReported() != null && formSpecificQuestionMap.containsKey(DEM115)){
							formSpecificQuestionAnswerMap.put(DEM115,patientDT.getBirthTime_s());
						}
						if (formSpecificQuestionMap.containsKey(DEM197+SEPERATOR+invCnt))
							formSpecificQuestionAnswerMap.put(DEM197+SEPERATOR+invCnt, patientDT.getLocalId()); 
						
						ArrayList<Object> al = (ArrayList<Object>) personVO.getThePersonRaceDTCollection();
						if(al != null){
							for(int i=0;i<al.size();i++){
								PersonRaceDT dt = ((PersonRaceDT)al.get(i));
								String race =dt.getRaceCd();
								if(DEM152RaceCodePRaceCatMap.get(race)!=null){
									String raceCode=DEM152RaceCodePRaceCatMap.get(race);
									if(raceCode.indexOf("/")>0){
										raceCode = raceCode.substring(0, raceCode.indexOf("/"));
									}
									if(formSpecificQuestionMap.containsKey(DEM152+SEPERATOR+raceCode+CODED_VALUE_TRANSLATED)){
										formSpecificQuestionAnswerMap.put(DEM152+SEPERATOR+raceCode+CODED_VALUE_TRANSLATED,race);
									}
								}
							}
						}

						if (patientDT.getEthnicGroupInd() != null) {
							String ethnicity = DEM155PhvsEthnicitygroupCdcUnkMap.get(patientDT.getEthnicGroupInd());
							 if(formSpecificQuestionMap.containsKey(DEM155+CODED_VALUE_TRANSLATED)){
								formSpecificQuestionAnswerMap.put(DEM155+CODED_VALUE_TRANSLATED,patientDT.getEthnicGroupInd());
							}else if(formSpecificQuestionMap.containsKey(DEM155+CODED_VALUE_TRANSLATED)){
								formSpecificQuestionAnswerMap.put(DEM155+CODED_VALUE_TRANSLATED,ethnicity);
							}
						}
						if (patientDT.getEthnicUnkReasonCd() != null) {
							String ethnicityUnkReason = NBS273PEthnUnkReasonMap.get(patientDT.getEthnicUnkReasonCd());
							if(formSpecificQuestionMap.containsKey(NBS273+CODED_VALUE_TRANSLATED) && ethnicityUnkReason != null && !ethnicityUnkReason.isEmpty()){
								formSpecificQuestionAnswerMap.put(NBS273+CODED_VALUE_TRANSLATED,ethnicityUnkReason);
							}
						}
						
						
						if (patientDT.getMaritalStatusCd() != null) {
							String maritalStatus = DEM140PMaritalMap.get(patientDT.getMaritalStatusCd());
							 if(formSpecificQuestionMap.containsKey(DEM140+CODED_VALUE_TRANSLATED) &&  maritalStatus!=null  && !maritalStatus.equals("")){
								formSpecificQuestionAnswerMap.put(DEM140+CODED_VALUE_TRANSLATED,maritalStatus);
							}						
						}

						if (personVO.getThePersonDT().getCurrSexCd() != null) {
							String curSex = personVO.getThePersonDT().getCurrSexCd();	
							/*TODO map this */
							if(formSpecificQuestionMap.containsKey(DEM113_CD)) {
								if (curSex != null && !curSex.isEmpty())
									if (CurrentSexMap.containsKey(curSex)) //M, F, U supported on form
										formSpecificQuestionAnswerMap.put(DEM113_CD, CurrentSexMap.get(curSex));
							}
							String preferredSex = personVO.getThePersonDT().getPreferredGenderCd();	
							if(formSpecificQuestionMap.containsKey(NBS274_CD)) {
								if (preferredSex != null && !preferredSex.isEmpty())
									if (PreferredSexMap.containsKey(preferredSex)) //M, F, U supported on form
										formSpecificQuestionAnswerMap.put(NBS274_CD, PreferredSexMap.get(preferredSex));
							}
							String sexUnknownReason = personVO.getThePersonDT().getSexUnkReasonCd();	
							if(formSpecificQuestionMap.containsKey(NBS272_CD)) { 
								if (sexUnknownReason != null && !sexUnknownReason.isEmpty())
									if (SexUnknownReasonMap.containsKey(sexUnknownReason))//R for refused or D for did not know
										formSpecificQuestionAnswerMap.put(NBS272_CD, SexUnknownReasonMap.get(sexUnknownReason));
							}
							
						}
						//Get the Master Patient Record (MPR) for values not in the Patient Revision
						Long PersonParentUid=patientDT.getPersonParentUid();
						NedssUtils nedssUtils = new NedssUtils();
						Object obj = nedssUtils.lookupBean(JNDINames.PERSONEJB);
						logger.debug("PersonEJB lookup = " + obj.toString());
						PersonHome home = (PersonHome) PortableRemoteObject.narrow(obj,PersonHome.class);
						Person person = null;
						person = home.findByPrimaryKey(PersonParentUid);
						
						String url = null;
						String email = null;
						if(person.getPersonVO()!=null && invCnt==1){
							PersonVO mprVO = person.getPersonVO();
							if (mprVO.getThePersonDT().getBirthGenderCd() != null)
								formSpecificQuestionAnswerMap.put(BIRTH_SEX_CDT, mprVO.getThePersonDT().getBirthGenderCd());
							if (mprVO.getThePersonDT().getPrimLangCd() != null) {
								String primaryLangCd = mprVO.getThePersonDT().getPrimLangCd();
								CachedDropDownValues cddV = new CachedDropDownValues();
								TreeMap<Object,Object> langTreeMap = cddV.getLanguageCodeAsTreeMap();
						    	String primaryLangShortDesc = (String) langTreeMap.get(primaryLangCd);
						    	if (primaryLangShortDesc != null)
						    		formSpecificQuestionAnswerMap.put(DEM142+SEPERATOR+invCnt,primaryLangShortDesc);
							}
							if(formSpecificQuestionMap.containsKey(MaidenName)){
								if(mprVO.getThePersonDT().getMothersMaidenNm()!=null) {
									formSpecificQuestionAnswerMap.put(MaidenName, mprVO.getThePersonDT().getMothersMaidenNm());
								}
							}
							if (mprVO.getThePersonDT().getSpeaksEnglishCd() != null) {
								String speaksEnglishCd = mprVO.getThePersonDT().getSpeaksEnglishCd();
						    	if (speaksEnglishCd != null)
						    		formSpecificQuestionAnswerMap.put(NBS214SpeaksEnglish+SEPERATOR+invCnt+CODED_VALUE, speaksEnglishCd);
							}
							if(mprVO.getTheEntityLocatorParticipationDTCollection()!=null){
								Collection<Object> entityColl= mprVO.getTheEntityLocatorParticipationDTCollection();
								Iterator<Object> iterator  = entityColl.iterator();
								while(iterator.hasNext()){
									EntityLocatorParticipationDT elpDT= (EntityLocatorParticipationDT)iterator.next();
									if(elpDT.getClassCd().equals(NEDSSConstants.TELE) && elpDT.getCd().equals(NEDSSConstants.NET) && elpDT.getUseCd().equals(NEDSSConstants.HOME)){
										TeleLocatorDT teleDT = elpDT.getTheTeleLocatorDT();
										if((email==null || !email.isEmpty()) && (teleDT.getEmailAddress()!=null && !teleDT.getEmailAddress().isEmpty()))
											email =teleDT.getEmailAddress();
										if (formSpecificQuestionAnswerMap.get(DEM182) == null)
											formSpecificQuestionAnswerMap.put(DEM182, email);
										else
											formSpecificQuestionAnswerMap.put(DEM182_R2, email);
										if((url==null || !url.isEmpty()) && (teleDT.getUrlAddress()!=null && !teleDT.getUrlAddress().isEmpty()))
											url = teleDT.getUrlAddress();
										formSpecificQuestionAnswerMap.put(Internet_Site, url);
										if(url!=null  && !url.isEmpty() && email!=null && !email.isEmpty())
											break;
									}
									
									if(elpDT.getClassCd().equals(NEDSSConstants.TELE) && elpDT.getCd().equals(NEDSSConstants.BP)){
										String pager=elpDT.getTheTeleLocatorDT_s().getPhoneNbrTxt();
										formSpecificQuestionAnswerMap.put(NBS177_R2, pager);
									}
								}
							}
						}
						putEntityNameAndAddressIntoMap(personVO, PATIENT);

						//Physician INV182
						} else if (physicianUid != null && personVO.getThePersonDT().getPersonUid()
								.compareTo(physicianUid) == 0  && !isPhysicianDataPopulated) {
							
							//putEntityNameAndAddressIntoMap(personVO, PROVIDER);
							Map<String, String> tmpMap = putEntityNameAndAddressIntoMap(personVO, PROVIDER_NAME, true, 
									PROVIDER_ADDRESS, PROVIDER_CITY, STATE_STATE_SHORT_NAME, 
									PROVIDER_ZIP, PROVIDER_PHONE, null);
							String otherInfoStr = "";
							otherInfoStr = getProviderOtherInfo(tmpMap, pageForm.getPageClientVO().getAnswerMap());
							if (otherInfoStr != null)
								formSpecificQuestionAnswerMap.put(OTHER_INFO, otherInfoStr);
							isPhysicianDataPopulated= true;
						} //physician
						else if(interviewerOfPHC != null && personVO.getThePersonDT().getPersonUid()
								.compareTo(interviewerOfPHC) == 0  ) {
							interviewerVO=personVO;
						} //interviewer
						else if(initInterviewerOfPHC != null && personVO.getThePersonDT().getPersonUid()
								.compareTo(initInterviewerOfPHC) == 0  ) {
							initInterviewerVO =personVO;
						} //Initial Interviewer
					//If there's no physician, add the other info

				}
				//If there's no physician, the other info section is still displaying
				String otherInfoStr=getOtherInfo();
				if (otherInfoStr != null){
					String otherInfo = formSpecificQuestionAnswerMap.get(OTHER_INFO);
					if(otherInfo==null || otherInfo.isEmpty())
						formSpecificQuestionAnswerMap.put(OTHER_INFO, otherInfoStr);
				}
				
				mapInterviewerData(theProxyVO, invCnt,interviewerVO,initInterviewerVO);
			}
		} catch (Exception e) {
			logger.error("Error while retrieving populateEntityValues:  "
					+ e.toString());
			throw new NEDSSAppException("populateEntityValues error thrown:", e);
		}
	}


	public static void putEntityNameAndAddressIntoMap(PersonVO personVO, String typeCd) {
		PostalLocatorDT postalDT = null;
		TeleLocatorDT teleDT = null;

		if (personVO.getThePersonNameDTCollection() != null) {
			Iterator<Object> personNameIt = personVO.getThePersonNameDTCollection()
					.iterator();
			while (personNameIt.hasNext()) {
				PersonNameDT personNameDT = (PersonNameDT) personNameIt.next();
				if (personNameDT.getNmUseCd().equalsIgnoreCase("L")) {  //Legal Name
					if(formSpecificQuestionMap.containsKey(DEM104) && typeCd.equals(PATIENT)){
						if(personNameDT.getFirstNm()!=null && personNameDT.getMiddleNm()!=null) {
							formSpecificQuestionAnswerMap.put(DEM104, personNameDT.getFirstNm()+", "+personNameDT.getMiddleNm());
						}else if(personNameDT.getFirstNm()!=null && personNameDT.getMiddleNm()==null) {
							formSpecificQuestionAnswerMap.put(DEM104, personNameDT.getFirstNm());
						}else if(personNameDT.getFirstNm()==null && personNameDT.getMiddleNm()!=null) {
							formSpecificQuestionAnswerMap.put(DEM104, personNameDT.getMiddleNm());
						} else
							formSpecificQuestionAnswerMap.put(DEM104, "");
					}
					if(formSpecificQuestionMap.containsKey(DEM102) && typeCd.equals(PATIENT)){
						if(personNameDT.getLastNm()!=null) {
							//add the last name to the maiden name
							String maiden = formSpecificQuestionAnswerMap.get(MaidenName);
							if(maiden!=null && !maiden.isEmpty())
								formSpecificQuestionAnswerMap.put(MaidenName, personNameDT.getLastNm()+", "+maiden);
								
							formSpecificQuestionAnswerMap.put(DEM102, personNameDT.getLastNm());
							
						} else
							formSpecificQuestionAnswerMap.put(DEM102, "");
					}

				}else if (personNameDT.getNmUseCd().equalsIgnoreCase("AL") && typeCd.equals(PATIENT)) {
					//alias name goes in DEM250
					if(formSpecificQuestionMap.containsKey(DEM250)){
						if(personNameDT.getFirstNm()!=null && !personNameDT.getFirstNm().isEmpty())
							formSpecificQuestionAnswerMap.put(DEM250, personNameDT.getFirstNm());
					}
				}

			}
		}
		formSpecificQuestionAnswerMap.put(ADDL_SEX, personVO.getThePersonDT().getAdditionalGenderCd());
		
		if (personVO.getTheEntityLocatorParticipationDTCollection() != null) {
			Iterator<Object> entIt = personVO
					.getTheEntityLocatorParticipationDTCollection().iterator();
			while (entIt.hasNext()) {
				StringBuffer stBuff = new StringBuffer("");
				EntityLocatorParticipationDT entityDT = (EntityLocatorParticipationDT) entIt
						.next();
				if (entityDT != null) {
					if (entityDT.getCd() != null
							&& entityDT.getCd()!=null
							&& entityDT.getClassCd() != null
							&& entityDT.getClassCd().equalsIgnoreCase("PST")
							&& entityDT.getRecordStatusCd() != null
							&& entityDT.getRecordStatusCd().equalsIgnoreCase("ACTIVE")
							&& entityDT.getUseCd()!=null) {
						postalDT = entityDT.getThePostalLocatorDT();
						stBuff = new StringBuffer("");
						stBuff.append((postalDT.getStreetAddr1() == null) ? ""
								: (postalDT.getStreetAddr1()));
						stBuff.append((postalDT.getStreetAddr2() == null) ? ""
								: (", " + postalDT.getStreetAddr2()));
						if(formSpecificQuestionMap.containsKey(ORD110_ORD111) && typeCd.equals(PROVIDER) && entityDT.getUseCd().equalsIgnoreCase("WP") && entityDT.getCd().equalsIgnoreCase("O")){
							//String key = ORD110_ORD111__T1LabOrderingFacilityOrT2MorbReportingOrgAddress.substring(0, ORD110_ORD111__T1LabOrderingFacilityOrT2MorbReportingOrgAddress.indexOf(delimiter1));
							formSpecificQuestionAnswerMap.put(ORD110_ORD111, stBuff.toString());

						}else if(formSpecificQuestionMap.containsKey(DEM159_DEM160) && typeCd.equals(PATIENT) && entityDT.getUseCd().equalsIgnoreCase("H") && entityDT.getCd().equalsIgnoreCase("H")){
							//String key = DEM159_160__PatientStreetDEM159.substring(0, DEM159_160__PatientStreetDEM159.indexOf(delimiter1));
							formSpecificQuestionAnswerMap.put(DEM159_DEM160 , stBuff.toString());
						}


						if(postalDT.getCityDescTxt()!=null &&  formSpecificQuestionMap.containsKey(ORD112) && typeCd.equals(PROVIDER) && entityDT.getUseCd().equalsIgnoreCase("WP") && entityDT.getCd().equalsIgnoreCase("O")){
							//String key = ORD112;__T1LabOrderingFacilityOrT2MorbReportingOrgCity.substring(0, ORD112__T1LabOrderingFacilityOrT2MorbReportingOrgCity.indexOf(delimiter1));
							formSpecificQuestionAnswerMap.put(ORD112, postalDT.getCityDescTxt());				
						}else if(formSpecificQuestionMap.containsKey(DEM161) && typeCd.equals(PATIENT) && entityDT.getUseCd().equalsIgnoreCase("H") && entityDT.getCd().equalsIgnoreCase("H")){
							//String key = DEM161__PatientCityDEM161.substring(0, DEM161__PatientCityDEM161.indexOf(delimiter1));
							formSpecificQuestionAnswerMap.put(DEM161 , postalDT.getCityDescTxt());
						}

						String stateStr = postalDT.getStateCd() == null ? "" 
								: getStateDescTxt(postalDT.getStateCd());
						//returnMap.put(stateAdrKey, stateStr);
						if(formSpecificQuestionMap.containsKey( ORD113) && typeCd.equals(PROVIDER) && entityDT.getUseCd().equalsIgnoreCase("WP") && entityDT.getCd().equalsIgnoreCase("O")){
							//String key = ORD113__T1LabOrderingFacilityOrT2MorbReportingOrgState.substring(0, ORD113__T1LabOrderingFacilityOrT2MorbReportingOrgState.indexOf(delimiter1));
							formSpecificQuestionAnswerMap.put(ORD113, stateStr);
						}else if(formSpecificQuestionMap.containsKey(DEM162) && typeCd.equals(PATIENT) && entityDT.getUseCd().equalsIgnoreCase("H") && entityDT.getCd().equalsIgnoreCase("H")){
							//String key = DEM162__PatientStateDEM162.substring(0, DEM162__PatientStateDEM162.indexOf(delimiter1));
							formSpecificQuestionAnswerMap.put(DEM162 , stateStr);
							if (formSpecificQuestionMap.containsKey(DEM165))
								if (postalDT.getCntyCd() != null) {
									countyCodes = cache.getCountyCodes(postalDT.getStateCd());
									if (countyCodes != null && countyCodes.containsKey(postalDT.getCntyCd()))
										formSpecificQuestionAnswerMap.put(DEM165, (String)countyCodes.get(postalDT.getCntyCd()));
								}
							if (formSpecificQuestionMap.containsKey(DEM167))
								if (postalDT.getCntryCd() != null) {
									if (countryMap != null && countryMap.containsKey(postalDT.getCntryCd()))
										formSpecificQuestionAnswerMap.put(DEM167, (String)countryMap.get(postalDT.getCntryCd()));
							}
						}

						if(postalDT.getZipCd()!=null && formSpecificQuestionMap.containsKey( ORD114) && typeCd.equals(PROVIDER) && entityDT.getUseCd().equalsIgnoreCase("WP") && entityDT.getCd().equalsIgnoreCase("O")){
							//String key = ORD114__T1LabOrderingFacilityOrT2MorbReportingOrgZip.substring(0, ORD114__T1LabOrderingFacilityOrT2MorbReportingOrgZip.indexOf(delimiter1));
							formSpecificQuestionAnswerMap.put(ORD114, postalDT.getZipCd());
						}else if(postalDT.getZipCd()!=null && formSpecificQuestionMap.containsKey( DEM163) && typeCd.equals(PATIENT) && entityDT.getUseCd().equalsIgnoreCase("H") && entityDT.getCd().equalsIgnoreCase("H")){
							//String key = ORD114__T1LabOrderingFacilityOrT2MorbReportingOrgZip.substring(0, ORD114__T1LabOrderingFacilityOrT2MorbReportingOrgZip.indexOf(delimiter1));
							formSpecificQuestionAnswerMap.put(DEM163, postalDT.getZipCd());
						}

					} //address fields
					else if (entityDT.getClassCd().equalsIgnoreCase(
							"TELE")
							&& entityDT.getRecordStatusCd() != null
							&& entityDT.getRecordStatusCd()
							.equalsIgnoreCase("ACTIVE")
							&& entityDT.getCd() != null
							&& entityDT.getCd().equalsIgnoreCase("PH")
							&& entityDT.getUseCd() != null
							&& entityDT.getUseCd().equalsIgnoreCase("WP")) {
						teleDT = entityDT.getTheTeleLocatorDT();

						stBuff = new StringBuffer("");
						stBuff.append((teleDT.getPhoneNbrTxt() == null) ? ""
								: (teleDT.getPhoneNbrTxt() + " "));
						String ext = "";
						if(teleDT.getExtensionTxt()!=null && !teleDT.getExtensionTxt().equals("0.0")) {
							ext = teleDT.getExtensionTxt().replace(".0", "");
							stBuff.append("Ext." + ext);
						}
						if(stBuff.toString().length()>0 && formSpecificQuestionMap.containsKey( ORD121_ORD122) && typeCd.equals(PROVIDER)){
								//String key = ORD121ExtORD122__T1LabOrderingFacilityOrT2MorbReportingOrgPhone.substring(0, ORD121ExtORD122__T1LabOrderingFacilityOrT2MorbReportingOrgPhone.indexOf(delimiter1));
							formSpecificQuestionAnswerMap.put(ORD121_ORD122, stBuff.toString());
						}else if(stBuff.toString().length()>0 && formSpecificQuestionMap.containsKey( ORD121_ORD122) && typeCd.equals(PROVIDER)){
							//String key = ORD121ExtORD122__T1LabOrderingFacilityOrT2MorbReportingOrgPhone.substring(0, ORD121ExtORD122__T1LabOrderingFacilityOrT2MorbReportingOrgPhone.indexOf(delimiter1));
							formSpecificQuestionAnswerMap.put(ORD121_ORD122, stBuff.toString());
						}else if( entityDT.getCd().equalsIgnoreCase("PH") && entityDT.getUseCd().equalsIgnoreCase("WP") && stBuff.toString().length()>0 && formSpecificQuestionMap.containsKey( EMPLOYMENT_PHONE)&& typeCd.equals(PATIENT)){
							formSpecificQuestionAnswerMap.put(EMPLOYMENT_PHONE, stBuff.toString());
						}
						
					}//phone get class cd
					else if (entityDT.getClassCd().equalsIgnoreCase(
							"TELE")
							&& entityDT.getRecordStatusCd() != null
							&& entityDT.getRecordStatusCd()
							.equalsIgnoreCase("ACTIVE")
							&& entityDT.getCd() != null
							&& entityDT.getUseCd() != null ) {
						teleDT = entityDT.getTheTeleLocatorDT();
						stBuff = new StringBuffer("");
						stBuff.append((teleDT.getPhoneNbrTxt() == null) ? ""
								: (teleDT.getPhoneNbrTxt() + " "));
						String ext = "";
						if(teleDT.getExtensionTxt()!=null && !teleDT.getExtensionTxt().equals("0.0")){
							ext = teleDT.getExtensionTxt().replace(".0", "");
							stBuff.append("Ext." + ext);
						}
						if( entityDT.getCd() != null
								&& entityDT.getCd().equalsIgnoreCase("CP") && stBuff.toString().length()>0 
								&& entityDT.getUseCd().equalsIgnoreCase("MC") && formSpecificQuestionMap.containsKey( NBS006) && typeCd.equals(PATIENT)){
							//String key = NBS006__PatientCellNBS006_UseCd.substring(0, NBS006__PatientCellNBS006_UseCd.indexOf(delimiter1));
							formSpecificQuestionAnswerMap.put(NBS006, stBuff.toString()+(" (C)"));
						}else if( entityDT.getCd().equalsIgnoreCase("PH") && entityDT.getUseCd().equalsIgnoreCase("H") && stBuff.toString().length()>0 && formSpecificQuestionMap.containsKey( DEM177)&& typeCd.equals(PATIENT)){
							//String key = DEM177__PatientHmPhDEM177_UseCd.substring(0, DEM177__PatientHmPhDEM177_UseCd.indexOf(delimiter1));
							formSpecificQuestionAnswerMap.put(DEM177, stBuff.toString()+(" (H)"));
						}else if( entityDT.getCd().equalsIgnoreCase("NET") && entityDT.getUseCd().equalsIgnoreCase("H") && stBuff.toString().length()>0 && formSpecificQuestionMap.containsKey( DEM182)&& typeCd.equals(PATIENT)){
							formSpecificQuestionAnswerMap.put(DEM182, stBuff.toString());
						}

					}//phone get class cd	
				} //entityDT != null

			} //iterator has next
		} //EntityLocatorParticipation not null
	}

	
	
	/**
	 * Put the values that are needed by the form into an answerMap.
	 * @param commonFieldsMap
	 * @param req
	 * @throws NEDSSAppException
	 */
	protected static void getMappedValues(Map<String, String> commonFieldsMap, HttpServletRequest req) throws NEDSSAppException{
		
		treatmentIndex = 1; //initialize treatment index
		labIndex = 1;//initialize lab index
		
		try {
			Set<String> mapSet=commonFieldsMap.keySet();
			Iterator<String> iterator = mapSet.iterator();
			while(iterator.hasNext()){
				String keyVal=(String)iterator.next();
				if(keyVal.contains(delimiter1)){
					String newKey = keyVal.substring(0, keyVal.indexOf(delimiter1));
					formSpecificQuestionMap.put(newKey, keyVal);
				}
			}
			
			formSpecificQuestionAnswerMap = new HashMap<String,String>();
			processMappedValues(1, proxyVO);
			if(coProxyVO!=null)
				processMappedValues(2, coProxyVO);
			getTreatmentCollection(req);
			
			//For avoiding process the same lab report twice when 2 conditions have the same lab report
			ArrayList<String> labsProcessed = new ArrayList<String>();
			ActRelationshipDT actRelationshipDT = getSourceAct(proxyVO);
			Long sourceUid=null;
			if(actRelationshipDT!=null){
				sourceUid=actRelationshipDT.getSourceActUid();
			}
			populateLabsWithSourceLabResultsFirst(sourceUid, proxyVO,  labsProcessed, 1);
			
			if(coProxyVO!=null){
			//For avoiding process the same lab report twice when 2 conditions have the same lab report
				ActRelationshipDT coActRelationshipDT = getSourceAct(coProxyVO);
				if(coActRelationshipDT!=null){
					sourceUid=coActRelationshipDT.getSourceActUid();
				}
				populateLabsWithSourceLabResultsFirst(sourceUid, coProxyVO,  labsProcessed, 2);
			}
			

			//Process Interviews into Map?
			if(proxyVO!=null)
				formSpecificQuestionAnswerMap.putAll(populateInterviewIntoMap(1, proxyVO, req));
			if(coProxyVO!=null)
				formSpecificQuestionAnswerMap.putAll(populateInterviewIntoMap(2, coProxyVO, req));

			
		} catch (Exception e) {
			throw new NEDSSAppException("Error while getMappedValues:  "+ e);
		}
		
	}
	

	/**
	 * Lab Results are populated from Morb and Lab Reports int the LabStagingArray.
	 * Put the lab fields into the form answer map.
	 * @throws NEDSSAppException
	 */
	protected static void populateLabFieldsFromStaging() throws NEDSSAppException {
		try {
        Arrays.sort(labStagingArray, new Comparator<String[]>() {
            @Override
            public int compare(final String[] entry1, final String[] entry2) {
                final String time1 = entry1[FORMATTED_FOR_SORT_DATE_INDEX];
                final String time2 = entry2[FORMATTED_FOR_SORT_DATE_INDEX];
                if ( (time1 == null || time1.length() == 0) && (time2 == null || time2.length() == 0) )
                	return 0;
                if ( (time1 == null || time1.length() == 0) && (time2 != null && time2.length() != 0) )
                	return 1;
                if ( (time1 != null && time1.length() != 0) && (time2 == null || time2.length() == 0) )
                	return -1;
                return time1.compareTo(time2);
            }
        });
		} catch (Exception e) {
			logger.error("SOrt Exception in populateLabFieldsFromStaging "+e.getMessage(), e);
		}
		
		try {
			for (int curIndex=0; curIndex < labIndex-1; ++curIndex) {
				String theLabDateKey =LAB163_R + (curIndex+1);
				formSpecificQuestionAnswerMap.put(theLabDateKey, labStagingArray[curIndex][DATE_INDEX]);
				String theLabTestKey =  LAB220_R + (curIndex+1);
				formSpecificQuestionAnswerMap.put(theLabTestKey, labStagingArray[curIndex][TEST_INDEX]);
				String theLabResultKey =  Lab_Result_+ (curIndex+1);
				formSpecificQuestionAnswerMap.put(theLabResultKey, labStagingArray[curIndex][RESULT_INDEX]);
				String theLabLabKey = ORD3_R+(curIndex+1);
				formSpecificQuestionAnswerMap.put(theLabLabKey, labStagingArray[curIndex][LAB_INDEX]);
				String theLabSpecimenSourceKey = LAB165_R +(curIndex+1);
				formSpecificQuestionAnswerMap.put(theLabSpecimenSourceKey, labStagingArray[curIndex][SPECIMEN_SOURCE_INDEX]);
				
				if (curIndex == 7)
					break;
			}
		} catch (Exception e) {
			logger.error("Exception populateLabFieldsFromStaging "+e.getMessage(), e);
			throw new NEDSSAppException("Error while populateLabsFromStaging:  "+ e);
		}
		return;
	}

	/**
	 * process the mapped values for the specified investigation
	 * @param investigationCounter
	 * @param actProxyVO
	 * @throws NEDSSAppException
	 */
	private static void processMappedValues(int investigationCounter, PageActProxyVO actProxyVO) throws NEDSSAppException{
		Long key = null; 
		String value="";
		Object resultObject=null;
		try {
			Map<Object, Object>  answerMap = actProxyVO.getPageVO().getPamAnswerDTMap();
			Set<Object> set= questionUidMap.keySet();
			Iterator<Object> it = set.iterator();
			while(it.hasNext()){
				key = (Long)it.next();
				NbsQuestionMetadata nbsQuestionDT= (NbsQuestionMetadata)questionUidMap.get(key);
				if(nbsQuestionDT.getDataLocation()==null || key==null)
					continue;
				if(nbsQuestionDT.getDataLocation().equalsIgnoreCase(NEDSSConstants.NBS_CASE_ANSWER_ANSWER_TXT)){
					resultObject =answerMap.get(key);
					if(resultObject instanceof NbsAnswerDT && answerMap!=null 
							&& answerMap.get(key)!=null)	{
						NbsAnswerDT caseDT = (NbsAnswerDT)answerMap.get(key);
						if(nbsQuestionDT!=null){
							populateNonRepeatData(caseDT.getAnswerTxt(),investigationCounter, nbsQuestionDT);
						}
					}				
				}else if(nbsQuestionDT.getDataLocation().startsWith(RenderConstants.CASE_MANAGEMENT)){
					mapValueForCaseManagementDT(nbsQuestionDT,investigationCounter, actProxyVO.getPublicHealthCaseVO().getTheCaseManagementDT());
				}else if(nbsQuestionDT.getDataLocation().startsWith(RenderConstants.PUBLIC_HEALTH_CASE)){
					mapValueForPublicHealthCaseDT(nbsQuestionDT,investigationCounter, actProxyVO.getPublicHealthCaseVO().getThePublicHealthCaseDT());
				}
				
				
			}
			 
			
			if(actProxyVO.getPageVO().getPageRepeatingAnswerDTMap()!=null  && actProxyVO.getPageVO().getPageRepeatingAnswerDTMap().size()>0) {
				processRepeatingQuestions(actProxyVO.getPageVO().getPageRepeatingAnswerDTMap(), investigationCounter);
			}
			populateEntities(actProxyVO, investigationCounter);
		}
		catch (Exception e) {
			logger.error("key"+ key);
			logger.error("value"+ value);
			throw new NEDSSAppException("Error while getMappedValues:  "+ e);
		}
	}
	/**
	 * Get values that map to the PublicHealthCase record if present.
	 * @param nbsQuestionDT
	 * @param investigationCounterNumber
	 * @param publicHealthCaseDT
	 * @throws Exception
	 */
	private static void mapValueForPublicHealthCaseDT(NbsQuestionMetadata nbsQuestionDT, int investigationCounterNumber, PublicHealthCaseDT publicHealthCaseDT) throws Exception {
	 try{
		 


		 if(nbsQuestionDT.getQuestionIdentifier().equalsIgnoreCase(	INV107_Public_Health_Case_jurisdiction_cd	)){
		          populateNonRepeatData(publicHealthCaseDT.getJurisdictionCd()	, investigationCounterNumber, nbsQuestionDT);
		 }else if(nbsQuestionDT.getQuestionIdentifier().equalsIgnoreCase(	INV108_Public_Health_Case_PROG_AREA_CD	)){
		          populateNonRepeatData(publicHealthCaseDT.getProgAreaCd()	, investigationCounterNumber, nbsQuestionDT);
		 }else if(nbsQuestionDT.getQuestionIdentifier().equalsIgnoreCase(	INV109_Public_Health_Case_investigation_status_cd	)){
			 populateNonRepeatData(publicHealthCaseDT.getInvestigationStatusCd()	, investigationCounterNumber, nbsQuestionDT);
		}else if(nbsQuestionDT.getQuestionIdentifier().equalsIgnoreCase(	INV110_Public_Health_Case_INVESTIGATOR_ASSIGNED_TIME	)){
		          populateNonRepeatData(StringUtils.formatDate(publicHealthCaseDT.getInvestigatorAssignedTime())	, investigationCounterNumber, nbsQuestionDT);
		}else if(nbsQuestionDT.getQuestionIdentifier().equalsIgnoreCase(	INV111_Public_Health_Case_rpt_form_cmplt_time	)){
		          populateNonRepeatData(StringUtils.formatDate(publicHealthCaseDT.getRptFormCmpltTime())	, investigationCounterNumber, nbsQuestionDT);
		}else if(nbsQuestionDT.getQuestionIdentifier().equalsIgnoreCase(	INV112_Public_Health_Case_RPT_SOURCE_CD	)){
		          populateNonRepeatData(publicHealthCaseDT.getRptSourceCd()	, investigationCounterNumber, nbsQuestionDT);
		}else if(nbsQuestionDT.getQuestionIdentifier().equalsIgnoreCase(	INV120_Public_Health_Case_rpt_to_county_time	)){
		          populateNonRepeatData(StringUtils.formatDate(publicHealthCaseDT.getRptToCountyTime())	, investigationCounterNumber, nbsQuestionDT);
		}else if(nbsQuestionDT.getQuestionIdentifier().equalsIgnoreCase(	INV121_Public_Health_Case_RPT_TO_STATE_TIME	)){
		          populateNonRepeatData(StringUtils.formatDate(publicHealthCaseDT.getRptToStateTime())	, investigationCounterNumber, nbsQuestionDT);
		}else if(nbsQuestionDT.getQuestionIdentifier().equalsIgnoreCase(	INV128_Public_Health_Case_hospitalized_ind_cd	)){
		          populateNonRepeatData(publicHealthCaseDT.getHospitalizedIndCd()	, investigationCounterNumber, nbsQuestionDT);
		}else if(nbsQuestionDT.getQuestionIdentifier().equalsIgnoreCase(	INV132_Public_Health_Case_HOSPITALIZED_ADMIN_TIME	)){
		          populateNonRepeatData(StringUtils.formatDate(publicHealthCaseDT.getHospitalizedAdminTime())	, investigationCounterNumber, nbsQuestionDT);
		}else if(nbsQuestionDT.getQuestionIdentifier().equalsIgnoreCase(	INV133_Public_Health_Case_hospitalized_discharge_time	)){
		          populateNonRepeatData(StringUtils.formatDate(publicHealthCaseDT.getHospitalizedDischargeTime())	, investigationCounterNumber, nbsQuestionDT);
		}else if(nbsQuestionDT.getQuestionIdentifier().equalsIgnoreCase(	INV134_Public_Health_Case_HOSPITALIZED_DURATION_AMT	)){
			if(publicHealthCaseDT.getHospitalizedDurationAmt()!=null)
		          populateNonRepeatData(publicHealthCaseDT.getHospitalizedDurationAmt().toString()	, investigationCounterNumber, nbsQuestionDT);
		}else if(nbsQuestionDT.getQuestionIdentifier().equalsIgnoreCase(	INV136_Public_Health_Case_diagnosis_time)){
		          populateNonRepeatData(StringUtils.formatDate(publicHealthCaseDT.getDiagnosisTime())	, investigationCounterNumber, nbsQuestionDT);
		}else if(nbsQuestionDT.getQuestionIdentifier().equalsIgnoreCase(	INV137_Public_Health_Case_EFFECTIVE_FROM_TIME	)){
		          populateNonRepeatData(StringUtils.formatDate(publicHealthCaseDT.getEffectiveFromTime())	, investigationCounterNumber, nbsQuestionDT);
		}else if(nbsQuestionDT.getQuestionIdentifier().equalsIgnoreCase(	INV138_Public_Health_Case_effective_to_time	)){
		          populateNonRepeatData(StringUtils.formatDate(publicHealthCaseDT.getEffectiveToTime())	, investigationCounterNumber, nbsQuestionDT);
		}else if(nbsQuestionDT.getQuestionIdentifier().equalsIgnoreCase(	INV139_Public_Health_Case_EFFECTIVE_DURATION_AMT	)){
		          populateNonRepeatData(publicHealthCaseDT.getEffectiveDurationAmt()	, investigationCounterNumber, nbsQuestionDT);
		}else if(nbsQuestionDT.getQuestionIdentifier().equalsIgnoreCase(	INV140_Public_Health_Case_effective_duration_unit_cd	)){
		          populateNonRepeatData(publicHealthCaseDT.getEffectiveDurationUnitCd()	, investigationCounterNumber, nbsQuestionDT);
		}else if(nbsQuestionDT.getQuestionIdentifier().equalsIgnoreCase(	INV143_Public_Health_Case_PAT_AGE_AT_ONSET	)){
		          populateNonRepeatData(publicHealthCaseDT.getPatAgeAtOnset()	, investigationCounterNumber, nbsQuestionDT);
		}else if(nbsQuestionDT.getQuestionIdentifier().equalsIgnoreCase(	INV144_Public_Health_Case_pat_age_at_onset_unit_cd	)){
		          populateNonRepeatData(publicHealthCaseDT.getPatAgeAtOnsetUnitCd()	, investigationCounterNumber, nbsQuestionDT);
		}else if(nbsQuestionDT.getQuestionIdentifier().equalsIgnoreCase(	INV145_Public_Health_Case_OUTCOME_CD	)){
		          populateNonRepeatData(publicHealthCaseDT.getOutcomeCd()	, investigationCounterNumber, nbsQuestionDT);
		}else if(nbsQuestionDT.getQuestionIdentifier().equalsIgnoreCase(	INV146_Public_Health_Case_deceased_time	)){
		          populateNonRepeatData(StringUtils.formatDate(publicHealthCaseDT.getDeceasedTime())	, investigationCounterNumber, nbsQuestionDT);
		}else if(nbsQuestionDT.getQuestionIdentifier().equalsIgnoreCase(	INV147_Public_Health_Case_ACTIVITY_FROM_TIME	)){
		          populateNonRepeatData(StringUtils.formatDate(publicHealthCaseDT.getActivityFromTime())	, investigationCounterNumber, nbsQuestionDT);
		}else if(nbsQuestionDT.getQuestionIdentifier().equalsIgnoreCase(	INV148_Public_Health_Case_day_care_ind_cd	)){
		          populateNonRepeatData(publicHealthCaseDT.getDayCareIndCd()	, investigationCounterNumber, nbsQuestionDT);
		}else if(nbsQuestionDT.getQuestionIdentifier().equalsIgnoreCase(	INV149_Public_Health_Case_FOOD_HANDLER_IND_CD	)){
		          populateNonRepeatData(publicHealthCaseDT.getFoodHandlerIndCd()	, investigationCounterNumber, nbsQuestionDT);
		}else if(nbsQuestionDT.getQuestionIdentifier().equalsIgnoreCase(	INV150_Public_Health_Case_outbreak_ind	)){
		          populateNonRepeatData(publicHealthCaseDT.getOutbreakInd()	, investigationCounterNumber, nbsQuestionDT);
		}else if(nbsQuestionDT.getQuestionIdentifier().equalsIgnoreCase(	INV151_Public_Health_Case_OUTBREAK_NAME	)){
		          populateNonRepeatData(publicHealthCaseDT.getOutbreakName()	, investigationCounterNumber, nbsQuestionDT);
		}else if(nbsQuestionDT.getQuestionIdentifier().equalsIgnoreCase(	INV152_Public_Health_Case_disease_imported_cd	)){
		          populateNonRepeatData(publicHealthCaseDT.getDiseaseImportedCd()	, investigationCounterNumber, nbsQuestionDT);
		}else if(nbsQuestionDT.getQuestionIdentifier().equalsIgnoreCase(	INV153_Public_Health_Case_IMPORTED_COUNTRY_CD	)){
		          populateNonRepeatData(publicHealthCaseDT.getImportedCountryCd()	, investigationCounterNumber, nbsQuestionDT);
		}else if(nbsQuestionDT.getQuestionIdentifier().equalsIgnoreCase(	INV154_Public_Health_Case_imported_state_cd	)){
		          populateNonRepeatData(publicHealthCaseDT.getImportedStateCd()	, investigationCounterNumber, nbsQuestionDT);
		}else if(nbsQuestionDT.getQuestionIdentifier().equalsIgnoreCase(	INV155_Public_Health_Case_IMPORTED_CITY_DESC_TXT	)){
		          populateNonRepeatData(publicHealthCaseDT.getImportedCityDescTxt()	, investigationCounterNumber, nbsQuestionDT);
		}else if(nbsQuestionDT.getQuestionIdentifier().equalsIgnoreCase(	INV156_Public_Health_Case_imported_county_cd	)){
		          populateNonRepeatData(publicHealthCaseDT.getImportedCountyCd()	, investigationCounterNumber, nbsQuestionDT);
		}else if(nbsQuestionDT.getQuestionIdentifier().equalsIgnoreCase(	INV157_Public_Health_Case_TRANSMISSION_MODE_CD	)){
		          populateNonRepeatData(publicHealthCaseDT.getTransmissionModeCd()	, investigationCounterNumber, nbsQuestionDT);
		}else if(nbsQuestionDT.getQuestionIdentifier().equalsIgnoreCase(	INV159_Public_Health_Case_detection_method_cd	)){
		          populateNonRepeatData(publicHealthCaseDT.getDetectionMethodCd()	, investigationCounterNumber, nbsQuestionDT);
		}else if(nbsQuestionDT.getQuestionIdentifier().equalsIgnoreCase(	INV163_Public_Health_Case_CASE_CLASS_CD	)){
		          populateNonRepeatData(publicHealthCaseDT.getCaseClassCd()	, investigationCounterNumber, nbsQuestionDT);
		}else if(nbsQuestionDT.getQuestionIdentifier().equalsIgnoreCase(	INV165_Public_Health_Case_mmwr_week	)){
		          populateNonRepeatData(publicHealthCaseDT.getMmwrWeek()	, investigationCounterNumber, nbsQuestionDT);
		}else if(nbsQuestionDT.getQuestionIdentifier().equalsIgnoreCase(	INV166_Public_Health_Case_MMWR_YEAR	)){
		          populateNonRepeatData(publicHealthCaseDT.getMmwrYear()	, investigationCounterNumber, nbsQuestionDT);
		}else if(nbsQuestionDT.getQuestionIdentifier().equalsIgnoreCase(	INV167_Public_Health_Case_txt	)){
		          populateNonRepeatData(publicHealthCaseDT.getTxt()	, investigationCounterNumber, nbsQuestionDT);
		}else if(nbsQuestionDT.getQuestionIdentifier().equalsIgnoreCase(	INV168_Public_Health_Case_LOCAL_ID	)){
		          populateNonRepeatData(publicHealthCaseDT.getLocalId()	, investigationCounterNumber, nbsQuestionDT);
		}else if(nbsQuestionDT.getQuestionIdentifier().equalsIgnoreCase(	INV169_Public_Health_Case_CD	)){
		          populateNonRepeatData(publicHealthCaseDT.getCd()	, investigationCounterNumber, nbsQuestionDT);
		}else if(nbsQuestionDT.getQuestionIdentifier().equalsIgnoreCase(	INV174_Public_Health_Case_shared_ind	)){
		          populateNonRepeatData(publicHealthCaseDT.getSharedInd()	, investigationCounterNumber, nbsQuestionDT);
		}else if(nbsQuestionDT.getQuestionIdentifier().equalsIgnoreCase(	INV178_Public_Health_Case_pregnant_ind_cd	)){
		          populateNonRepeatData(publicHealthCaseDT.getPregnantIndCd()	, investigationCounterNumber, nbsQuestionDT);
		}else if(nbsQuestionDT.getQuestionIdentifier().equalsIgnoreCase(	INV2006_Public_Health_Case_ACTIVITY_TO_TIME	)){
		          populateNonRepeatData(StringUtils.formatDate(publicHealthCaseDT.getActivityToTime())	, investigationCounterNumber, nbsQuestionDT);
		}else if(nbsQuestionDT.getQuestionIdentifier().equalsIgnoreCase(	INV257_Public_Health_Case_PRIORITY_CD	)){
		          populateNonRepeatData(publicHealthCaseDT.getPriorityCd()	, investigationCounterNumber, nbsQuestionDT);
		}else if(nbsQuestionDT.getQuestionIdentifier().equalsIgnoreCase(	INV258_Public_Health_Case_INFECTIOUS_FROM_DATE	)){
		          populateNonRepeatData(StringUtils.formatDate(publicHealthCaseDT.getInfectiousFromDate())	, investigationCounterNumber, nbsQuestionDT);
		}else if(nbsQuestionDT.getQuestionIdentifier().equalsIgnoreCase(	INV259_Public_Health_Case_INFECTIOUS_TO_DATE	)){
		          populateNonRepeatData(StringUtils.formatDate(publicHealthCaseDT.getInfectiousToDate())	, investigationCounterNumber, nbsQuestionDT);
		}else if(nbsQuestionDT.getQuestionIdentifier().equalsIgnoreCase(	INV260_Public_Health_Case_CONTACT_INV_STATUS	)){
		          populateNonRepeatData(publicHealthCaseDT.getContactInvStatus()	, investigationCounterNumber, nbsQuestionDT);
		}else if(nbsQuestionDT.getQuestionIdentifier().equalsIgnoreCase(	INV261_Public_Health_Case_CONTACT_INV_TXT	)){
		          populateNonRepeatData(publicHealthCaseDT.getContactInvTxt()	, investigationCounterNumber, nbsQuestionDT);
		}else if(nbsQuestionDT.getQuestionIdentifier().equalsIgnoreCase(	NBS012_Public_Health_Case_SHARED_IND	)){
		          populateNonRepeatData(publicHealthCaseDT.getSharedInd()	, investigationCounterNumber, nbsQuestionDT);
		}else if(nbsQuestionDT.getQuestionIdentifier().equalsIgnoreCase(	NBS055_Public_Health_Case_PRIORITY_CD	)){
		          populateNonRepeatData(publicHealthCaseDT.getPriorityCd()	, investigationCounterNumber, nbsQuestionDT);
		}else if(nbsQuestionDT.getQuestionIdentifier().equalsIgnoreCase(	NBS056_Public_Health_Case_INFECTIOUS_FROM_DATE	)){
		          populateNonRepeatData(StringUtils.formatDate(publicHealthCaseDT.getInfectiousFromDate())	, investigationCounterNumber, nbsQuestionDT);
		}else if(nbsQuestionDT.getQuestionIdentifier().equalsIgnoreCase(	NBS057_Public_Health_Case_INFECTIOUS_TO_DATE	)){
		          populateNonRepeatData(StringUtils.formatDate(publicHealthCaseDT.getInfectiousToDate())	, investigationCounterNumber, nbsQuestionDT);
		}else if(nbsQuestionDT.getQuestionIdentifier().equalsIgnoreCase(	NBS058_Public_Health_Case_CONTACT_INV_STATUS	)){
		          populateNonRepeatData(publicHealthCaseDT.getContactInvStatus()	, investigationCounterNumber, nbsQuestionDT);
		}else if(nbsQuestionDT.getQuestionIdentifier().equalsIgnoreCase(	NBS059_Public_Health_Case_CONTACT_INV_TXT	)){
		          populateNonRepeatData(publicHealthCaseDT.getContactInvTxt()	, investigationCounterNumber, nbsQuestionDT);
		}else if(nbsQuestionDT.getQuestionIdentifier().equalsIgnoreCase(	NBS110_Public_Health_Case_REFERRAL_BASIS_CD	)){
				populateNonRepeatData(publicHealthCaseDT.getReferralBasisCd()	, investigationCounterNumber, nbsQuestionDT);
		}else if(nbsQuestionDT.getQuestionIdentifier().equalsIgnoreCase(	NBS115_Public_Health_Case_CURR_PROCESS_STATE_CD	)){
		          populateNonRepeatData(publicHealthCaseDT.getCurrProcessStateCd()	, investigationCounterNumber, nbsQuestionDT);
		}else if(nbsQuestionDT.getQuestionIdentifier().equalsIgnoreCase(	SUM100_Public_Health_Case_rpt_cnty_cd	)){
		          populateNonRepeatData(publicHealthCaseDT.getRptCntyCd()	, investigationCounterNumber, nbsQuestionDT);
		}else if(nbsQuestionDT.getQuestionIdentifier().equalsIgnoreCase(	SUM101_Public_Health_Case_mmwr_year	)){
		          populateNonRepeatData(publicHealthCaseDT.getMmwrYear()	, investigationCounterNumber, nbsQuestionDT);
		}else if(nbsQuestionDT.getQuestionIdentifier().equalsIgnoreCase(	SUM102_Public_Health_Case_mmwr_week	)){
		          populateNonRepeatData(publicHealthCaseDT.getMmwrWeek()	, investigationCounterNumber, nbsQuestionDT);
		}else if(nbsQuestionDT.getQuestionIdentifier().equalsIgnoreCase(	SUM105_Public_Health_Case_txt	)){
		          populateNonRepeatData(publicHealthCaseDT.getTxt()	, investigationCounterNumber, nbsQuestionDT);
		}else if(nbsQuestionDT.getQuestionIdentifier().equalsIgnoreCase(	SUM106_Public_Health_Case_cd	)){
		          populateNonRepeatData(publicHealthCaseDT.getCd()	, investigationCounterNumber, nbsQuestionDT);
		}else if(nbsQuestionDT.getQuestionIdentifier().equalsIgnoreCase(	SUM113_Public_Health_Case_rpt_form_cmplt_time	)){
		          populateNonRepeatData(StringUtils.formatDate(publicHealthCaseDT.getRptFormCmpltTime())	, investigationCounterNumber, nbsQuestionDT);
		}else if(nbsQuestionDT.getQuestionIdentifier().equalsIgnoreCase(	SUM115_Public_Health_Case_count_interval_cd	)){
		          populateNonRepeatData(publicHealthCaseDT.getCountIntervalCd()	, investigationCounterNumber, nbsQuestionDT);
		}else if(nbsQuestionDT.getQuestionIdentifier().equalsIgnoreCase(	SUM116_Public_Health_Case_case_class_cd	)){
		          populateNonRepeatData(publicHealthCaseDT.getCaseClassCd()	, investigationCounterNumber, nbsQuestionDT);
		}
	}catch (Exception e) {
		throw new NEDSSAppException("The nbsQuestionDT is not valid as the data location is set null here", e);
	}

		
	}
	/**
	 * Initially Assigned Interview is filled in when the Interviewer is saved.
	 * If interviwer is there, initial interviewer is there.
	 * Some specific logic about reassignment. If not reassigned, we don't see Date Reassigned.
	 * @param theProxyVO 
	 * @param investigationCounter
	 * @param interviewer
	 * @param initInterviewerOfPHC
	 * @throws NEDSSAppException
	 */
	private static void mapInterviewerData(PageActProxyVO theProxyVO, int investigationCounter, PersonVO interviewer, PersonVO initInterviewerOfPHC) throws NEDSSAppException{
		

		//This doesn't depend on interviewer reassigned or not
		
		 PersonVO supPersonVO =PageLoadUtil.getPersonVO("CASupervisorOfPHC", theProxyVO);
		 if(supPersonVO != null){
			 formSpecificQuestionAnswerMap.put("NBS190_"+investigationCounter+CODED_VALUE,supPersonVO.getEntityIdDT_s(0).getRootExtensionTxt());
		 }
		 
		 PersonVO closureInvestgrOfPHC =PageLoadUtil.getPersonVO("ClosureInvestgrOfPHC", theProxyVO);
		 if(closureInvestgrOfPHC != null){
			 formSpecificQuestionAnswerMap.put("NBS197_"+investigationCounter+CODED_VALUE,closureInvestgrOfPHC.getEntityIdDT_s(0).getRootExtensionTxt());
		 }
		 
		 
		if (interviewer == null || initInterviewerOfPHC == null)
			return;
		
		try {
			
			//if Interview has been reassigned since initial assignment to another interviewer
			if(theProxyVO.getPublicHealthCaseVO().getTheCaseManagementDT().getInitInterviewAssignedDate() != null
					&& theProxyVO.getPublicHealthCaseVO().getTheCaseManagementDT().getInterviewAssignedDate() != null
					&& ( theProxyVO.getPublicHealthCaseVO().getTheCaseManagementDT().getInitInterviewAssignedDate()
										  .compareTo(theProxyVO.getPublicHealthCaseVO().getTheCaseManagementDT().getInterviewAssignedDate()) != 0
						|| initInterviewerOfPHC.getThePersonDT().getPersonParentUid().compareTo(interviewer.getThePersonDT().getPersonParentUid())!=0 )){

				 String initInterviewerId = initInterviewerOfPHC.getEntityIdDT_s(0).getRootExtensionTxt();
				 if(formSpecificQuestionMap.containsKey( "NBS188"+SEPERATOR+investigationCounter+CODED_VALUE )){
						formSpecificQuestionAnswerMap.put("NBS188"+SEPERATOR+investigationCounter+CODED_VALUE , checkNull(initInterviewerId));
				 }
				 if(formSpecificQuestionMap.containsKey( NBS189_CASE_MANAGEMENT_INIT_INTERVIEW_ASSIGNED_DATE+SEPERATOR+investigationCounter )){
						formSpecificQuestionAnswerMap.put(NBS189_CASE_MANAGEMENT_INIT_INTERVIEW_ASSIGNED_DATE+SEPERATOR+investigationCounter, StringUtils.formatDate(theProxyVO.getPublicHealthCaseVO().getTheCaseManagementDT().getInitInterviewAssignedDate()));
				 }


				 String interviewerId = interviewer.getEntityIdDT_s(0).getRootExtensionTxt();
				 if(interviewerId != null){
					 if(formSpecificQuestionMap.containsKey( "NBS186"+SEPERATOR+investigationCounter+CODED_VALUE)){
							formSpecificQuestionAnswerMap.put("NBS186"+SEPERATOR+investigationCounter+CODED_VALUE , checkNull(interviewerId));
					 }
					 if(formSpecificQuestionMap.containsKey( NBS187_CASE_MANAGEMENT_INTERVIEW_ASSIGNED_DATE+SEPERATOR+investigationCounter)){
							formSpecificQuestionAnswerMap.put(NBS187_CASE_MANAGEMENT_INTERVIEW_ASSIGNED_DATE+SEPERATOR+investigationCounter, StringUtils.formatDate(theProxyVO.getPublicHealthCaseVO().getTheCaseManagementDT().getInterviewAssignedDate()));
					 }
					 
				 }


					
			} else { //not been reassigned

				String interviewerId = interviewer.getEntityIdDT_s(0).getRootExtensionTxt();
				mappedDiagnosis.put("NBS188"+SEPERATOR+investigationCounter+CODED_VALUE, checkNull(interviewerId));
					 
			  if(formSpecificQuestionMap.containsKey( "NBS188"+SEPERATOR+investigationCounter+CODED_VALUE)){
							formSpecificQuestionAnswerMap.put("NBS188"+SEPERATOR+investigationCounter+CODED_VALUE,  checkNull(interviewerId));
			  } 
				
			  if(theProxyVO.getPublicHealthCaseVO().getTheCaseManagementDT().getInterviewAssignedDate() != null){
					 if(formSpecificQuestionMap.containsKey( NBS189_CASE_MANAGEMENT_INIT_INTERVIEW_ASSIGNED_DATE+SEPERATOR+investigationCounter )){
							formSpecificQuestionAnswerMap.put(NBS189_CASE_MANAGEMENT_INIT_INTERVIEW_ASSIGNED_DATE+SEPERATOR+investigationCounter ,  StringUtils.formatDate(theProxyVO.getPublicHealthCaseVO().getTheCaseManagementDT().getInterviewAssignedDate()));
					 }
				}
		   } //else not reassigned			 
			 
		} catch (Exception e) {
			logger.error("mapCommonEntities NedssAppException thrown"+e);
			throw new NEDSSAppException("mapCommonEntities Exception thrown", e);
		}
		return;
	}
	/**
	 * Get values that map to the CaseManagement record if present.
	 * @param nbsQuestionDT
	 * @param investigationCounterNumber
	 * @param caseManagementDT
	 * @throws Exception
	 */
	private static void mapValueForCaseManagementDT(NbsQuestionMetadata nbsQuestionDT, int investigationCounterNumber, CaseManagementDT caseManagementDT) throws Exception {
        try {
        	if(nbsQuestionDT.getQuestionIdentifier().equalsIgnoreCase(NBS155_CASE_MANAGEMENT_SUBJ_HEIGHT)){
        		populateNonRepeatData(caseManagementDT.getSubjHeight(), investigationCounterNumber, nbsQuestionDT);
        	}else if(nbsQuestionDT.getQuestionIdentifier().equalsIgnoreCase(NBS156_CASE_MANAGEMENT_SUBJ_SIZE_BUILD)){
        		populateNonRepeatData(caseManagementDT.getSubjSizeBuild(), investigationCounterNumber, nbsQuestionDT);
        	}else if(nbsQuestionDT.getQuestionIdentifier().equalsIgnoreCase(NBS157_CASE_MANAGEMENT_SUBJ_HAIR)){
        		populateNonRepeatData(caseManagementDT.getSubjHair(), investigationCounterNumber, nbsQuestionDT);
        	}else if(nbsQuestionDT.getQuestionIdentifier().equalsIgnoreCase(NBS158_CASE_MANAGEMENT_SUBJ_COMPLEXION)){
        		populateNonRepeatData(caseManagementDT.getSubjComplexion(), investigationCounterNumber, nbsQuestionDT);
        	}else if(nbsQuestionDT.getQuestionIdentifier().equalsIgnoreCase(NBS159_CASE_MANAGEMENT_SUBJ_OTH_IDNTFYNG_INFO)){
        		populateNonRepeatData(caseManagementDT.getSubjOthIdntfyngInfo(), investigationCounterNumber, nbsQuestionDT);
        	}else if(nbsQuestionDT.getQuestionIdentifier().equalsIgnoreCase(NBS111_CASE_MANAGEMENT_INITIATING_AGNCY)){
        		populateNonRepeatData(caseManagementDT.getInitiatingAgncy(), investigationCounterNumber, nbsQuestionDT);
        	}else if(nbsQuestionDT.getQuestionIdentifier().equalsIgnoreCase(NBS112_CASE_MANAGEMENT_OOJ_INITG_AGNCY_RECD_DATE)){
        		populateNonRepeatData(StringUtils.formatDate(caseManagementDT.getOojInitgAgncyRecdDate()), investigationCounterNumber, nbsQuestionDT);
        	}else if(nbsQuestionDT.getQuestionIdentifier().equalsIgnoreCase(NBS113_CASE_MANAGEMENT_OOJ_INITG_AGNCY_OUTC_DUE_DATE)){
        		populateNonRepeatData(StringUtils.formatDate(caseManagementDT.getOojInitgAgncyOutcDueDate()), investigationCounterNumber, nbsQuestionDT);
        	}else if(nbsQuestionDT.getQuestionIdentifier().equalsIgnoreCase(NBS114_CASE_MANAGEMENT_OOJ_INITG_AGNCY_OUTC_SNT_DATE)){
        		populateNonRepeatData(StringUtils.formatDate(caseManagementDT.getOojInitgAgncyOutcSntDate()), investigationCounterNumber, nbsQuestionDT);
        	}else if(nbsQuestionDT.getQuestionIdentifier().equalsIgnoreCase(NBS160_CASE_MANAGEMENT_FIELD_RECORD_NUMBER)){
        		populateNonRepeatData(caseManagementDT.getFieldRecordNumber(), investigationCounterNumber, nbsQuestionDT);
        	}else if(nbsQuestionDT.getQuestionIdentifier().equalsIgnoreCase(NBS191_CASE_MANAGEMENT_EPI_LINK_ID)){
        		populateNonRepeatData(caseManagementDT.getEpiLinkId(), investigationCounterNumber, nbsQuestionDT);
        	}else if(nbsQuestionDT.getQuestionIdentifier().equalsIgnoreCase(NBS140_CASE_MANAGEMENT_INIT_FOLL_UP)){
        		populateNonRepeatData(caseManagementDT.getInitFollUp(), investigationCounterNumber, nbsQuestionDT);
        	}else if(nbsQuestionDT.getQuestionIdentifier().equalsIgnoreCase(NBS141_CASE_MANAGEMENT_INIT_FOLL_UP_CLOSED_DATE)){
        		populateNonRepeatData(StringUtils.formatDate(caseManagementDT.getInitFollUpClosedDate()), investigationCounterNumber, nbsQuestionDT);
        	}else if(nbsQuestionDT.getQuestionIdentifier().equalsIgnoreCase(NBS142_CASE_MANAGEMENT_INTERNET_FOLL_UP)){
        		populateNonRepeatData(caseManagementDT.getInternetFollUp(), investigationCounterNumber, nbsQuestionDT);
        	}else if(nbsQuestionDT.getQuestionIdentifier().equalsIgnoreCase(NBS144_CASE_MANAGEMENT_INIT_FOLL_UP_CLINIC_CODE)){
        		populateNonRepeatData(caseManagementDT.getInitFollUpClinicCode(), investigationCounterNumber, nbsQuestionDT);
        	}else if(nbsQuestionDT.getQuestionIdentifier().equalsIgnoreCase(NBS146_CASE_MANAGEMENT_SURV_ASSIGNED_DATE)){
        		populateNonRepeatData(StringUtils.formatDate(caseManagementDT.getSurvAssignedDate()), investigationCounterNumber, nbsQuestionDT);
        	}else if(nbsQuestionDT.getQuestionIdentifier().equalsIgnoreCase(NBS147_CASE_MANAGEMENT_SURV_CLOSED_DATE)){
        		populateNonRepeatData(StringUtils.formatDate(caseManagementDT.getSurvClosedDate()), investigationCounterNumber, nbsQuestionDT);
        	}else if(nbsQuestionDT.getQuestionIdentifier().equalsIgnoreCase(NBS148_CASE_MANAGEMENT_SURV_PROVIDER_CONTACT)){
        		populateNonRepeatData(caseManagementDT.getSurvProviderContact(), investigationCounterNumber, nbsQuestionDT);
        	}else if(nbsQuestionDT.getQuestionIdentifier().equalsIgnoreCase(NBS149_CASE_MANAGEMENT_SURV_PROV_EXM_REASON)){
        		if(investigationCounterNumber==1)
        			populateNonRepeatData(caseManagementDT.getSurvProvExmReason(), 1, nbsQuestionDT);
        	}else if(nbsQuestionDT.getQuestionIdentifier().equalsIgnoreCase(NBS150_CASE_MANAGEMENT_SURV_PROV_DIAGNOSIS)){
        		populateNonRepeatData(caseManagementDT.getSurvProvDiagnosis(), investigationCounterNumber, nbsQuestionDT);
        	}else if(nbsQuestionDT.getQuestionIdentifier().equalsIgnoreCase(NBS151_CASE_MANAGEMENT_SURV_PATIENT_FOLL_UP)){
        		populateNonRepeatData(caseManagementDT.getSurvPatientFollUp(), investigationCounterNumber, nbsQuestionDT);
        	}else if(nbsQuestionDT.getQuestionIdentifier().equalsIgnoreCase(NBS143_CASE_MANAGEMENT_INIT_FOLL_UP_NOTIFIABLE)){
        		populateNonRepeatData(caseManagementDT.getInitFollUpNotifiable(), investigationCounterNumber, nbsQuestionDT);
        	}else if(nbsQuestionDT.getQuestionIdentifier().equalsIgnoreCase(NBS167_CASE_MANAGEMENT_FLD_FOLL_UP_NOTIFICATION_PLAN)){
        		populateNonRepeatData(caseManagementDT.getFldFollUpNotificationPlan(), investigationCounterNumber, nbsQuestionDT);
        	}else if(nbsQuestionDT.getQuestionIdentifier().equalsIgnoreCase(NBS177_CASE_MANAGEMENT_ACT_REF_TYPE_CD)){
        		populateNonRepeatData(caseManagementDT.getActRefTypeCd(), investigationCounterNumber, nbsQuestionDT);
        	}else if(nbsQuestionDT.getQuestionIdentifier().equalsIgnoreCase(NBS162_CASE_MANAGEMENT_FOLL_UP_ASSIGNED_DATE)){
        		populateNonRepeatData(StringUtils.formatDate(caseManagementDT.getFollUpAssignedDate()), investigationCounterNumber, nbsQuestionDT);
        	}else if(nbsQuestionDT.getQuestionIdentifier().equalsIgnoreCase(NBS164_CASE_MANAGEMENT_INIT_FOLL_UP_ASSIGNED_DATE)){
        		populateNonRepeatData(StringUtils.formatDate(caseManagementDT.getInitFollUpAssignedDate()), investigationCounterNumber, nbsQuestionDT);
        	}else if(nbsQuestionDT.getQuestionIdentifier().equalsIgnoreCase(NBS165_CASE_MANAGEMENT_FLD_FOLL_UP_PROV_EXM_REASON)){
        		populateNonRepeatData(caseManagementDT.getFldFollUpProvExmReason(), investigationCounterNumber, nbsQuestionDT);
        	}else if(nbsQuestionDT.getQuestionIdentifier().equalsIgnoreCase(NBS166_CASE_MANAGEMENT_FLD_FOLL_UP_PROV_DIAGNOSIS)){
        		populateNonRepeatData(caseManagementDT.getFldFollUpProvDiagnosis(), investigationCounterNumber, nbsQuestionDT);
        	}else if(nbsQuestionDT.getQuestionIdentifier().equalsIgnoreCase(NBS168_CASE_MANAGEMENT_FLD_FOLL_UP_EXPECTED_IN)){
        		populateNonRepeatData(caseManagementDT.getFldFollUpExpectedIn(), investigationCounterNumber, nbsQuestionDT);
        	}else if(nbsQuestionDT.getQuestionIdentifier().equalsIgnoreCase(NBS169_CASE_MANAGEMENT_FLD_FOLL_UP_EXPECTED_DATE)){
        		populateNonRepeatData(StringUtils.formatDate(caseManagementDT.getFldFollUpExpectedDate()), investigationCounterNumber, nbsQuestionDT);
        	}else if(nbsQuestionDT.getQuestionIdentifier().equalsIgnoreCase(NBS170_CASE_MANAGEMENT_FLD_FOLL_UP_EXAM_DATE)){
        		populateNonRepeatData(StringUtils.formatDate(caseManagementDT.getFldFollUpExamDate()), investigationCounterNumber, nbsQuestionDT);
        	}else if(nbsQuestionDT.getQuestionIdentifier().equalsIgnoreCase(NBS173_CASE_MANAGEMENT_FLD_FOLL_UP_DISPO)){
        		populateNonRepeatData(caseManagementDT.getFldFollUpDispo(), investigationCounterNumber, nbsQuestionDT);
        	}else if(nbsQuestionDT.getQuestionIdentifier().equalsIgnoreCase(NBS174_CASE_MANAGEMENT_FLD_FOLL_UP_DISPO_DATE)){
        		populateNonRepeatData(StringUtils.formatDate(caseManagementDT.getFldFollUpDispoDate()), investigationCounterNumber, nbsQuestionDT);
        	}else if(nbsQuestionDT.getQuestionIdentifier().equalsIgnoreCase(NBS178_CASE_MANAGEMENT_FLD_FOLL_UP_INTERNET_OUTCOME)){
        		populateNonRepeatData(caseManagementDT.getFldFollUpInternetOutcome(), investigationCounterNumber, nbsQuestionDT);
        	}else if(nbsQuestionDT.getQuestionIdentifier().equalsIgnoreCase(NBS179_CASE_MANAGEMENT_OOJ_AGENCY)){
        		populateNonRepeatData(caseManagementDT.getOojAgency(), investigationCounterNumber, nbsQuestionDT);
        	}else if(nbsQuestionDT.getQuestionIdentifier().equalsIgnoreCase(NBS180_CASE_MANAGEMENT_OOJ_NUMBER)){
        		populateNonRepeatData(caseManagementDT.getOojNumber(), investigationCounterNumber, nbsQuestionDT);
        	}else if(nbsQuestionDT.getQuestionIdentifier().equalsIgnoreCase(NBS181_CASE_MANAGEMENT_OOJ_DUE_DATE)){
        		populateNonRepeatData(StringUtils.formatDate(caseManagementDT.getOojDueDate()), investigationCounterNumber, nbsQuestionDT);
        	}else if(nbsQuestionDT.getQuestionIdentifier().equalsIgnoreCase(NBS182_CASE_MANAGEMENT_FIELD_FOLL_UP_OOJ_OUTCOME)){
        		populateNonRepeatData(caseManagementDT.getFieldFollUpOojOutcome(), investigationCounterNumber, nbsQuestionDT);
        	}else if(nbsQuestionDT.getQuestionIdentifier().equalsIgnoreCase(NBS187_CASE_MANAGEMENT_INTERVIEW_ASSIGNED_DATE)){
        		populateNonRepeatData(StringUtils.formatDate(caseManagementDT.getInterviewAssignedDate()), investigationCounterNumber, nbsQuestionDT);
        	}else if(nbsQuestionDT.getQuestionIdentifier().equalsIgnoreCase(NBS189_CASE_MANAGEMENT_INIT_INTERVIEW_ASSIGNED_DATE)){
        		populateNonRepeatData(StringUtils.formatDate(caseManagementDT.getInitInterviewAssignedDate()), investigationCounterNumber, nbsQuestionDT);
        	}else if(nbsQuestionDT.getQuestionIdentifier().equalsIgnoreCase(NBS192_CASE_MANAGEMENT_PAT_INTV_STATUS_CD)){
        		populateNonRepeatData(caseManagementDT.getPatIntvStatusCd(), investigationCounterNumber, nbsQuestionDT);
        	}else if(nbsQuestionDT.getQuestionIdentifier().equalsIgnoreCase(NBS196_CASE_MANAGEMENT_CASE_CLOSED_DATE)){
        		populateNonRepeatData(StringUtils.formatDate(caseManagementDT.getCaseClosedDate()), investigationCounterNumber, nbsQuestionDT);
        	}else if(nbsQuestionDT.getQuestionIdentifier().equalsIgnoreCase(NBS153_CASE_MANAGEMENT_STATUS_900)){
        		populateNonRepeatData(caseManagementDT.getStatus900(), investigationCounterNumber, nbsQuestionDT);
        	}


		}catch (Exception e) {
			throw new NEDSSAppException("The nbsQuestionDT is not valid as the data location is set null here", e);
		}
	}
	/**
	 * Return the Getter Name for a field.
	 * @param nbsQuestionDT
	 * @param prefix
	 * @return
	 * @throws Exception
	 */
	public static String findDynamicFieldName(NbsQuestionMetadata nbsQuestionDT, String prefix )
			throws Exception {
		String dataLoc="";
		String getterName="";
		try {
			Iterator<Object> iter = questionMap.keySet().iterator();
			while (iter.hasNext()) {
				Object key = iter.next();
				if (answerMap.get(key) != null) {

					NbsQuestionMetadata dt = (NbsQuestionMetadata) questionMap
							.get(key);
					dataLoc = dt.getDataLocation() == null ? "" : dt.getDataLocation();
					if (dataLoc.toUpperCase().startsWith(prefix + ".")) {
						int pos = dataLoc.indexOf(".");
						if (pos == -1) {
							throw new Exception("Data Location for: "+ dt.getQuestionIdentifier()+ " cannot be without '.' between Table Name and Table Column");
						}
						String colNm = dataLoc.substring(pos + 1);
						
						StringBuffer sb = new StringBuffer("get");
						StringTokenizer st = new StringTokenizer(colNm, "_");
						while (st.hasMoreTokens()) {
							String s = st.nextToken();
							s = s.substring(0, 1).toUpperCase()
									+ s.substring(1).toLowerCase();
							sb.append(s);
							getterName= sb.toString();
						}
					} else {
						continue;
					}

				}
			}
		} catch (Exception e) {
			logger.error("findDynamicFieldName: Error while finding gettname and exception is raised: "+ e);
			throw new Exception(e);
		}
		return getterName;
	}
	
	
	/**
	 * Process treatments.
	 * @param req
	 */
	private static void getTreatmentCollection( HttpServletRequest req) {
		try {
			populateTreatmentValuesWithData(proxyVO,req.getSession());
			if(treatmentIndex<5 && coProxyVO != null) 
				populateTreatmentValuesWithData(coProxyVO,req.getSession());
		} catch (Exception e) {
			logger.debug("Exception in getMappedValues- CIR - getTreatments " + e.getMessage());
		}
	}


	/**
	 * If null, return empty string else return value
	 * @param s
	 * @return
	 */
	protected static String checkNull(String s){
		return  s != null? s: "";
	}
	
	/**
	 * Get the Treatment into one string.
	 * @param treamentSummVO
	 * @return
	 * @throws NEDSSAppException
	 */
	private static StringBuffer getTreatmentNameCode(TreatmentSummaryVO treamentSummVO) throws NEDSSAppException{
		try {
			StringBuffer treatmentNameCode =new StringBuffer();
				if(treamentSummVO.getTreatmentNameCode() != null && treamentSummVO.getTreatmentNameCode().equals("OTH")){
					TreatmentAdministeredDAOImpl dao = new TreatmentAdministeredDAOImpl();
					Collection<Object> coll =dao.load(treamentSummVO.getTreatmentUid());
					Object[] array =coll.toArray();
					if(array.length >0) {

						TreatmentAdministeredDT dt = (TreatmentAdministeredDT) array[0];
						if(dt.getCd() != null){
						String desc =CachedDropDowns.getCodeDescTxtForCd(dt.getCd(),"TREAT_DRUG");
						String text = desc != null? desc : dt.getCd();
						treatmentNameCode.append(text).append(", ");
						}
						if(dt.getDoseQty() != null){
							treatmentNameCode.append(dt.getDoseQty()).append(" ");
						}
						if(dt.getDoseQtyUnitCd()  != null){
							String desc =CachedDropDowns.getCodeDescTxtForCd(dt.getDoseQtyUnitCd() ,"TREAT_DOSE_UNIT");
							String text = desc != null? desc : dt.getDoseQtyUnitCd();
							treatmentNameCode.append(text).append(", ");
							}
						if(dt.getRouteCd() != null){
							String desc =CachedDropDowns.getCodeDescTxtForCd(dt.getRouteCd(),"TREAT_ROUTE");
							String text = desc != null? desc : dt.getRouteCd();
							treatmentNameCode.append(text).append(", ");
							}
						if(dt.getIntervalCd() != null){
							String desc =CachedDropDowns.getCodeDescTxtForCd(dt.getIntervalCd(),"TREAT_FREQ_UNIT");
							String text = desc != null? desc : dt.getIntervalCd();
							treatmentNameCode.append(text).append(" X ");
							}
						if(dt.getEffectiveDurationAmt() != null){
							treatmentNameCode.append(dt.getEffectiveDurationAmt()).append(" ");
						}

						if(dt.getEffectiveDurationUnitCd() != null){
							String desc =CachedDropDowns.getCodeDescTxtForCd(dt.getEffectiveDurationUnitCd(),"TREAT_DUR_UNIT");
							String text = desc != null? desc : dt.getEffectiveDurationUnitCd();
							treatmentNameCode.append(text);
							}
					}


				}else{
					treatmentNameCode.append(treamentSummVO.getCustomTreatmentNameCode());
				}
				return treatmentNameCode;
		} catch (NEDSSSystemException e) {
			throw new NEDSSAppException("getTreatmentNameCode excdeption thrown", e);
		}
	}


	
	
	/**
	 * checkInvStartedFromConRec - if Investigation started from Contact and get values from Original Investigation that created the contact
	 * @param invProxyVO
	 * @param invCnt - 1 for Inv, 2 for CoInv
	 * @param session
	 * @return null if not started from contact else map of values needed from source investigation if present:
	 * 		 NBS136_ORIGINAL_CONDITION - condition of originating Inv. Usually same. Could be different.
	 * 		 NBS117_ORIGINAL_PATIENT_ID_NUMBER - local is i.e.CAS10048000GA01 of originating inv.
	 * 		 NBS057_NBS056_ORIGINAL_INFECTIOUS_PERIOD - infectious from - infectious to in months on the Contact Tracing tab
	 * @throws NEDSSAppException
	 */
	public static Map<String, String> checkInvStartedFromConRec(PageProxyVO invProxyVO, int invCnt, HttpSession session) throws NEDSSAppException {

		PageActProxyVO contactAux;
		HashMap<String, String> returnMap = new HashMap<String,String>();

		try {
			Collection<Object> contactColl = ((PageActProxyVO) invProxyVO)
					.getTheCTContactSummaryDTCollection();

			Iterator<Object> itr = contactColl.iterator();
			Boolean isFrnCon = Boolean.FALSE;
			String contactCond = null;
			while (itr.hasNext()) {

				CTContactSummaryDT dt = (CTContactSummaryDT) itr.next();

				if(dt.isPatientNamedByContact()&&dt.getSubjectEntityPhcUid() != null){
					Long conPhcUid = dt.getSubjectEntityPhcUid();
					if(conPhcUid != null){
						contactAux = (PageActProxyVO) PageLoadUtil
								.getProxyObject(
										String.valueOf(conPhcUid),
										session);

						if (contactAux.getTheCTContactSummaryDTCollection() != null) {
							Iterator<Object> it = contactAux
									.getTheCTContactSummaryDTCollection().iterator();
							while (it.hasNext()) {
								CTContactSummaryDT condt = (CTContactSummaryDT) it.next();
								if (condt.isContactNamedByPatient()
										&& condt.getContactEntityPhcUid() != null
										&& condt.getContactEntityPhcUid().equals(
												invProxyVO.getPublicHealthCaseVO()
												.getThePublicHealthCaseDT()
												.getPublicHealthCaseUid())) {

									//we're assuming question key map is already populated with the correct page data
									Map<Object, Object> thisAnswerMap = PageLoadUtil.updateMapWithQIds(((PageActProxyVO) contactAux)
											.getPageVO().getPamAnswerDTMap());
									isFrnCon = Boolean.TRUE;
									if (thisAnswerMap.containsKey(NBS136))
										contactCond = getInvAnsFromMap(thisAnswerMap, NBS136);
								
					
									if (contactCond == null || contactCond.isEmpty()) {
										contactCond = contactAux.getPublicHealthCaseVO().getThePublicHealthCaseDT().getCd();
										if(contactCond != null){
											CachedDropDownValues cddv = new CachedDropDownValues();
											String shortcd = cddv.getDiagnosisCodeForConceptCode(contactCond);
											contactCond =shortcd != null ? shortcd :contactCond;
										}
									}
									
									//OP Condition and OP Case ID should only print if investigation was started from contact record
									if(!"T1".equals(invProxyVO.getPublicHealthCaseVO().getThePublicHealthCaseDT().getReferralBasisCd()) && 
											!"T2".equals(invProxyVO.getPublicHealthCaseVO().getThePublicHealthCaseDT().getReferralBasisCd()) ) {
										returnMap.put(NBS136_ORIGINAL_CONDITION+invCnt+CODED_VALUE, contactCond);
										//ToDo: Should NBS117 be the Original Patient Local Id not the Case ID. i.e. PSN10065016GA01
										returnMap.put(ORIGINAL_CASE_ID+invCnt, contactAux.getPublicHealthCaseVO().getThePublicHealthCaseDT().getLocalId());
									}
									returnMap.put(NBS117_ORIGINAL_PATIENT_ID_NUMBER, contactAux.getPublicHealthCaseVO().getThePublicHealthCaseDT().getLocalId());
									//get originating inv infectious period in months
									//Interview Period (mos) 1 and Interview Period (mos) 2 fpor coinf
									Timestamp infectiousFromDate =  contactAux.getPublicHealthCaseVO().getThePublicHealthCaseDT().getInfectiousFromDate();
									Timestamp infectiousToDate = contactAux.getPublicHealthCaseVO().getThePublicHealthCaseDT().getInfectiousToDate();
									if (infectiousFromDate != null && infectiousToDate!= null) {
										try {
											int m1 = infectiousFromDate.getYear() * 12 + infectiousFromDate.getMonth();
											int m2 = infectiousToDate .getYear() * 12 + infectiousToDate .getMonth();
											Integer infectiousNumberOfMonths = (Integer) m2 - m1 + 1;
											if (infectiousNumberOfMonths < 1)
												infectiousNumberOfMonths = 1;
											returnMap.put(NBS057_NBS056_ORIGINAL_INFECTIOUS_PERIOD, infectiousNumberOfMonths.toString());
										} catch (Exception e) {
											logger.error("Error in CommonPDFPrintForm computing infectious period of source investigation", e.getMessage());
										}	
									}

									//per ND-1007 put from original contact record if present
									CTContactProxyVO originalContactRecordProxy = PageLoadUtil.getCTContactProxyObject(condt.getCtContactUid(), session);
									Map<Object, Object>  ctAnswerMap = 	PageLoadUtil.updateMapWithQIds(originalContactRecordProxy.getcTContactVO().getCtContactAnswerDTMap());
									if (ctAnswerMap.containsKey(NBS118))
										returnMap.put(NBS118+ORIGINAL_PATIENT_CONTACT_RECORD, getContactAnsFromMap(ctAnswerMap,NBS118));
									if (ctAnswerMap.containsKey(NBS119))
										returnMap.put(NBS119+ORIGINAL_PATIENT_CONTACT_RECORD, getContactAnsFromMap(ctAnswerMap,NBS119));
									if (ctAnswerMap.containsKey(NBS120))
										returnMap.put(NBS120+ORIGINAL_PATIENT_CONTACT_RECORD, getContactAnsFromMap(ctAnswerMap,NBS120));
									if (ctAnswerMap.containsKey(NBS121))
										returnMap.put(NBS121+ORIGINAL_PATIENT_CONTACT_RECORD, getContactAnsFromMap(ctAnswerMap,NBS121));	
									if (ctAnswerMap.containsKey(NBS122))
										returnMap.put(NBS122+ORIGINAL_PATIENT_CONTACT_RECORD, getContactAnsFromMap(ctAnswerMap,NBS122));
									if (ctAnswerMap.containsKey(NBS123))
										returnMap.put(NBS123+ORIGINAL_PATIENT_CONTACT_RECORD, getContactAnsFromMap(ctAnswerMap,NBS123));										
									logger.debug("\n --> put Original Contact Record Sexual and Needle Sharing Exposure Dates into returnMap <-- \n");
									
									Long originalInterview = condt.getNamedDuringInterviewUid();
									if (originalInterview != null && 
											originalInterview.longValue() != CTConstants.StdInitiatedWithoutInterviewLong.longValue()) {
										//retrieve the original interview for values IXS102_FR1_1, IXS102_FR1_2 and IXS105_1_CDT, IXS105_2_CDT
										if (contactAux.getTheInterviewSummaryDTCollection()!=null && contactAux.getTheInterviewSummaryDTCollection().size()>0){
											Iterator iterator= contactAux.getTheInterviewSummaryDTCollection().iterator();
											boolean interviewNotFound = true;
											while(iterator.hasNext() && interviewNotFound){
												InterviewSummaryDT interviewSummaryDT = (InterviewSummaryDT)iterator.next();
												if(interviewSummaryDT != null && 
														interviewSummaryDT.getInterviewUid().longValue() == originalInterview.longValue() ){
													interviewNotFound = false;
													if (interviewSummaryDT.getInterviewTypeCd() != null)
														returnMap.put("IXS105_"+invCnt+CODED_VALUE_TRANSLATED, IXS105NbsInterviewTypeMap.get(interviewSummaryDT.getInterviewTypeCd()));
													if (interviewSummaryDT.getInterviewerQuickCd() != null)
														returnMap.put("IXS102_FR1_"+invCnt, interviewSummaryDT.getInterviewerQuickCd());
												} //interviewSummaryDT != null
											}//while 
										}//interviewSumary not null
									}//original interview not null
									
								} //this contact started Inv
								if(isFrnCon) break;
							} //contact summary iter
							if(isFrnCon) break;
						} //contact summary not null
						if(isFrnCon) break;
					}//contactColl has next 
				} //dt.isPatientNamedByContact()&&dt.getSubjectEntityPhcUid() != null
				
			} //contact collection iter
			if (returnMap.isEmpty())
				return null;


			return returnMap;

		} catch (Exception e) {
			logger.error("checkInvStartedFromConRec exception thrown " +e);
			throw new NEDSSAppException("checkInvStartedFromConRec exception thrown", e);
		}
	}
	
	/**
	 * For CDT (translated code) form fields, translate the NBS code into the code needed for the form
	 * @param questionIdentifier
	 * @param answerTxt
	 * @return
	 */
	protected static String mapCode(String questionIdentifier, String answerTxt) {
		if(questionIdentifier.equalsIgnoreCase(STD121AnatomicSite)){
			answerTxt=STD121PhvsClinicianObservedLesionsStdMap.get(answerTxt);
		}else if(questionIdentifier.equalsIgnoreCase(NBS151_CASE_MANAGEMENT_SURV_PATIENT_FOLL_UP)){
			answerTxt=NBS151SurveillancePatientFollowupMap.get(answerTxt);
		}else if(questionIdentifier.equalsIgnoreCase(INV178_Public_Health_Case_pregnant_ind_cd)){
			answerTxt=INV178YnuMap.get(answerTxt);
			answerTxt=INV178YnuMap.get(answerTxt);
		}else if(questionIdentifier.equalsIgnoreCase(NBS149_CASE_MANAGEMENT_SURV_PROV_EXM_REASON)){
			answerTxt=NBS149ExamReasonMap.get(answerTxt);
		}else if(questionIdentifier.equalsIgnoreCase("NBS233")||questionIdentifier.equalsIgnoreCase("NBS235")||questionIdentifier.equalsIgnoreCase("NBS237")||questionIdentifier.equalsIgnoreCase("NBS239")|| questionIdentifier.equalsIgnoreCase("NBS234")||questionIdentifier.equalsIgnoreCase("NBS236")||questionIdentifier.equalsIgnoreCase("NBS238")||questionIdentifier.equalsIgnoreCase("NBS240")){
				answerTxt=drugHistory.get(answerTxt);
		}else if(questionIdentifier.equalsIgnoreCase("STD119")){
			answerTxt=STD119PartnerInternet.get(answerTxt);
		}else if(questionIdentifier.equalsIgnoreCase(NBS167_CASE_MANAGEMENT_FLD_FOLL_UP_NOTIFICATION_PLAN)){
			if(answerTxt!=null && answerTxt.length()==1) 
				answerTxt="0"+answerTxt;
		}else if(questionIdentifier.equalsIgnoreCase(NBS143_CASE_MANAGEMENT_INIT_FOLL_UP_NOTIFIABLE)){
			if(answerTxt!=null && answerTxt.length()==1) 
				answerTxt="0"+answerTxt;
		}else if(questionIdentifier.equalsIgnoreCase(NBS177_CASE_MANAGEMENT_ACT_REF_TYPE_CD)){
			if(answerTxt!=null && answerTxt.length()==1) 
				answerTxt="0"+answerTxt;
		}else if(questionIdentifier.equalsIgnoreCase(NBS252)){
			if(answerTxt!=null && answerTxt.length()==1) 
				answerTxt="0"+answerTxt;
		}else if(questionIdentifier.equalsIgnoreCase(NBS257)){
			if(answerTxt!=null && answerTxt.length()==1) 
				answerTxt="0"+answerTxt;
		}else if(questionIdentifier.equalsIgnoreCase(NBS267)){
			if(answerTxt!=null && answerTxt.length()==1) 
				answerTxt="0"+answerTxt;
		}else if(questionIdentifier.equalsIgnoreCase(STD106)){ //gst revisit differ on IX than FR?
			if(answerTxt!=null && answerTxt.length()==1) 
				answerTxt="0"+answerTxt;
		}else if(questionIdentifier.equalsIgnoreCase(NBS260_Referred_For_900_Test)){
			answerTxt=YNStdMisMap.get(answerTxt);
		}else if(questionIdentifier.equalsIgnoreCase(NBS262_900_Test_Preformed)){
			answerTxt=YNRUDStdMisMap.get(answerTxt);
		}else if(questionIdentifier.equalsIgnoreCase(NBS265_Partner_Informed_Of_900_Result)){
			answerTxt=YNStdMisMap.get(answerTxt);	
		}else if(questionIdentifier.equalsIgnoreCase(NBS266_Refer_For_Care)){
			answerTxt=YNStdMisMap.get(answerTxt);	
		}else if (questionIdentifier.equalsIgnoreCase(NBS242PlacesToMeetPartner)) {
			answerTxt=NBS242PlacesToMeetPartnerMap.get(answerTxt); 
		}else if (questionIdentifier.equalsIgnoreCase(NBS244PlacesToHaveSex)) {
			answerTxt=NBS244PlacesToHaveSexMap.get(answerTxt); 
		}else if (questionIdentifier.equalsIgnoreCase(NBS223FemalePartnersPastYear)) {
			answerTxt=NBS223FemalePartnersPastYearMap.get(answerTxt); 
		}else if (questionIdentifier.equalsIgnoreCase(NBS225MalePartnersPastYear)) {
			answerTxt=NBS225MalePartnersPastYearMap.get(answerTxt); 
		}else if (questionIdentifier.equalsIgnoreCase(NBS227TransgenderPartnersPastYear)) {
			answerTxt=NBS227TransgenderPartnersPastYearMap.get(answerTxt); 
		}else if (questionIdentifier.equalsIgnoreCase(NBS129FemalePartnersInterviewPeriod)) {
			answerTxt=NBS129FemalePartnersInterviewPeriodMap.get(answerTxt); 
		}else if (questionIdentifier.equalsIgnoreCase(NBS131MalePartnersInterviewPeriod)) {
			answerTxt=NBS131MalePartnersInterviewPeriodMap.get(answerTxt); 
		}else if (questionIdentifier.equalsIgnoreCase(NBS133TransgenderPartnersInterviewPeriod)) {
			answerTxt=NBS133TransgenderPartnersInterviewPeriodMap.get(answerTxt); 
		}else if (questionIdentifier.equalsIgnoreCase(NBS192PatientInterviewStatus)) {
			answerTxt = NBS192_PatientInterviewStatusMap.get(answerTxt);
		}
		
		return answerTxt;
	}
	
	
	/**
	 * Convert the race code into the one the form requires.
	 * @param race
	 * @param i
	 */
	protected static void convertRaceCD(String race, int i) {
		if(formSpecificQuestionMap.containsKey(DEM152)){
			formSpecificQuestionAnswerMap.put(DEM152,DEM152RaceCodePRaceCatMap.get(race));
		}else if(formSpecificQuestionMap.containsKey(DEM152+SEPERATOR+race)){
			formSpecificQuestionAnswerMap.put(DEM152+SEPERATOR+race,DEM152RaceCodePRaceCatMap.get(race));
		}
	}
	

	/**
	 * Get the coinfection investigation from the workup proxy in PageLoadUtil to show the conditions in the list.
	 * Note: This takes some time to process. Is there a better way?
	 * @param request
	 * @return
	 * @throws NEDSSAppException
	 * @throws Exception
	 */
	public static Map<String, String> getCoinfectionInvestigations(HttpServletRequest request) throws NEDSSAppException,Exception{
		Map<String, String> returnMap = new HashMap<String, String>();

		Long publicHealthCaseUID = new Long((String)NBSContext.retrieve(request.getSession(),
				NBSConstantUtil.DSInvestigationUid));
		Object[] oParam1 = new Object[] { NEDSSConstants.PRINT_CDC_CASE, publicHealthCaseUID };
		proxyVO =(PageActProxyVO)CallProxyEJB.callProxyEJB(oParam1 , JNDINames.PAGE_PROXY_EJB, 
						"getPageProxyVO", request.getSession());
		String coinfCd = proxyVO.getPublicHealthCaseVO().getThePublicHealthCaseDT().getCoinfectionId();
		Long currPHCUid = proxyVO.getPublicHealthCaseVO().getThePublicHealthCaseDT().getPublicHealthCaseUid();
		PersonVO personVO;
		try {
			personVO = PageLoadUtil.getPersonVO(NEDSSConstants.PHC_PATIENT, proxyVO);

			Long personUID = personVO.getThePersonDT().getPersonParentUid();

			 String sBeanJndiName = JNDINames.WORKUP_PROXY_EJB;// "WorkupProxyEJBRef";
	         String sMethod = "getWorkupProxy";
	         Object[] oParams = new Object[] {personUID};
	         MainSessionHolder holder = new MainSessionHolder();
	         MainSessionCommand msCommand;

				msCommand = holder.getMainSessionCommand(request.getSession());

	         ArrayList<?> arr = msCommand.processRequest(sBeanJndiName, sMethod,
	                                                  oParams);
	         WorkupProxyVO workupProxyVO = (WorkupProxyVO) arr.get(0);

			Collection<Object> coll = workupProxyVO.getTheInvestigationSummaryVOCollection();

			for(Object o : coll){
				if (o instanceof InvestigationSummaryVO){
					if (((InvestigationSummaryVO)o).getCoinfectionId() != null &&((InvestigationSummaryVO)o).getCoinfectionId().equals(coinfCd) && ((InvestigationSummaryVO)o).getPublicHealthCaseUid().longValue() != currPHCUid.longValue()){
						returnMap.put(((InvestigationSummaryVO)o).getPublicHealthCaseUid().toString(), ((InvestigationSummaryVO)o).getConditionCodeText());
					}
				}
			}
		} catch (NEDSSAppException e) {
			logger.error("Error while retrieving getCoInfectionInvestigation:  "
					+ e.toString());
			throw new NEDSSAppException(e.toString());
		} catch (Exception e) {
			logger.error("Error while retrieving getCoInfectionInvestigation:  "
					+ e.toString());
			throw new Exception(e.toString());
		}

		return returnMap;
	}
	
	/**
	 * If a coinfection was specified get the VO and put it into the variable coProxyVO.
	 * @param coinfectionUid
	 * @param request
	 * @throws NEDSSAppException
	 * @throws Exception
	 */
	private static void getCoinfectionVO(Long coinfectionUid, HttpServletRequest request) throws NEDSSAppException,Exception{
		try{
			Object[] oParams = new Object[] { NEDSSConstants.PRINT_CDC_CASE, new Long(coinfectionUid)};
			coProxyVO =(PageActProxyVO)CallProxyEJB.callProxyEJB(oParams , JNDINames.PAGE_PROXY_EJB, "getPageProxyVO", request.getSession());
		} catch (Exception e) {
			logger.error("Error while retrieving getCoInfectionInvestigation:  "
					+ e.toString());
			throw new NEDSSAppException("Error while retrieving getCoInfectionInvestigation:  "+ e.toString());
		}
	}

	/**
	 * Populate from the repeat answer map if present.
	 * @param repeatingAnswerMap
	 * @param metadata
	 * @throws NEDSSAppException
	 */
	@SuppressWarnings("unchecked")
	private static void populateRepeatData(Map<Object, Object> repeatingAnswerMap, NbsQuestionMetadata metadata )
			throws NEDSSAppException {
		try {
			if (repeatingAnswerMap.containsKey(metadata.getNbsQuestionUid())) {
				ArrayList<Object> list = (ArrayList<Object>) repeatingAnswerMap.get(metadata.getNbsQuestionUid());

				if(metadata.getNbsUiComponentUid().compareTo((long) 1019)==0){
					populateNotes(list, metadata);
				}else{
					if (list != null && list.size() > 0) {
						Iterator<Object> it = list.iterator();
						while (it.hasNext()) {
							NbsAnswerDT caseDT = (NbsAnswerDT) it.next();


							if(formSpecificQuestionMap.containsKey(metadata.getQuestionIdentifier()+ REPEAT_IND)){
								formSpecificQuestionAnswerMap.put(metadata.getQuestionIdentifier()+ REPEAT_IND,  mapCode(metadata.getQuestionIdentifier(), caseDT.getAnswerTxt()));
							}else if(formSpecificQuestionMap.containsKey(metadata.getQuestionIdentifier()+ REPEAT_IND+ caseDT.getAnswerGroupSeqNbr())){
								formSpecificQuestionAnswerMap.put(metadata.getQuestionIdentifier()+ REPEAT_IND+ caseDT.getAnswerGroupSeqNbr(),  mapCode(metadata.getQuestionIdentifier(), caseDT.getAnswerTxt()));
							}else if(formSpecificQuestionMap.containsKey(metadata.getQuestionIdentifier()+ REPEAT_IND+ caseDT.getAnswerGroupSeqNbr()+ CODED_VALUE_TRANSLATED)){
								formSpecificQuestionAnswerMap.put(metadata.getQuestionIdentifier()+ REPEAT_IND+ caseDT.getAnswerGroupSeqNbr()+ CODED_VALUE_TRANSLATED, mapCode(metadata.getQuestionIdentifier(), caseDT.getAnswerTxt()));
							}else if(formSpecificQuestionMap.containsKey(metadata.getQuestionIdentifier()+ REPEAT_IND+ caseDT.getAnswerGroupSeqNbr()+ CODED_VALUE)){
								formSpecificQuestionAnswerMap.put(metadata.getQuestionIdentifier()+ REPEAT_IND+ caseDT.getAnswerGroupSeqNbr()+ CODED_VALUE, mapCode(metadata.getQuestionIdentifier(), caseDT.getAnswerTxt()));
							}else if(formSpecificQuestionMap.containsKey(metadata.getQuestionIdentifier()+ REPEAT_IND+ caseDT.getAnswerGroupSeqNbr()+_1+ CODED_VALUE)){ // handles NBS250_R1_1_CD type of questions
								formSpecificQuestionAnswerMap.put(metadata.getQuestionIdentifier()+ REPEAT_IND+ caseDT.getAnswerGroupSeqNbr()+_1+ CODED_VALUE, mapCode(metadata.getQuestionIdentifier(), caseDT.getAnswerTxt()));
							}else if(formSpecificQuestionMap.containsKey(metadata.getQuestionIdentifier()+ REPEAT_IND+ caseDT.getAnswerGroupSeqNbr()+_1)){ // handles NBS251_R1_1 type of questions
								formSpecificQuestionAnswerMap.put(metadata.getQuestionIdentifier()+ REPEAT_IND+ caseDT.getAnswerGroupSeqNbr()+_1, mapCode(metadata.getQuestionIdentifier(), caseDT.getAnswerTxt()));
							}

						}

					}
				}
			}
		} catch (Exception e) {
			logger.error("CommonPDFPrintForm.populateRepeatData Exception thrown, "+ e.getMessage(), e);
			throw new NEDSSAppException("CommonPDFPrintForm.populateRepeatData Exception thrown, ", e);
		}
	}
	
	/**
	 * Populate note field. Only called from repeat.
	 * Note: Fatima says this is unnecessary with the new PDFBox lib.
	 * @param coll
	 * @param metadata
	 */
	private static void populateNotes(Collection coll, NbsQuestionMetadata metadata){
		String notes="";
		Iterator<NbsCaseAnswerDT> it = coll.iterator();
		while (it.hasNext()) {
			NbsCaseAnswerDT caseDT =  it.next();
			logger.debug("caseDT is:" + caseDT.toString());
			if(caseDT.getAnswerTxt() != null ){
				logger.debug("caseDT uid is :" + caseDT.getNbsCaseAnswerUid());
				notes = notes +  caseDT.getAnswerTxt()/*addNewlinesToNotes(caseDT.getAnswerTxt(),metadata.getQuestionIdentifier())*/ + "\n" ;
			}//notes.length();
		}
		//truncating the notes and appending '...' to fit in the pdf notes box
		
			//For notes textbox stretching full page width and Half Page Length
			if (metadata.getQuestionIdentifier().equals(NBS152)){
				if( notes.length()>NotesFullWidthHalfPageCharLimit ){
				notes=notes.substring(0, NotesFullWidthHalfPageCharLimit)+"...";
				}
			}
			//For notes textbox stretching full page width and Full Page Length
			else if (metadata.getQuestionIdentifier().equals(NBS195)){
				if( notes.length()>NotesFullWidthFullPageCharLimit ){
				notes=notes.substring(0, NotesFullWidthFullPageCharLimit)+"...";
				}
			}
			//For notes textbox stretching half page width
			else{ 
				if( notes.length()>NotesHalfWidthCharLimit ){
				notes=notes.substring(0, NotesHalfWidthCharLimit)+"...";
				}
			}
		
		String previousNotes="";//For not losing the notes from the initiating investigation with the notes from the co-infection investigation
		if(formSpecificQuestionAnswerMap.get(metadata.getQuestionIdentifier())!=null){
			
			previousNotes=formSpecificQuestionAnswerMap.get(metadata.getQuestionIdentifier());
			notes=previousNotes+notes;
		}
		
		formSpecificQuestionAnswerMap.put(metadata.getQuestionIdentifier() , notes);

	
	}

	
	/**
	 * populateNonRepeatData - translate if necessary
	 * @param answertext
	 * @param investigationCounterNumber
	 * @param metadata
	 * @throws NEDSSAppException
	 */
	@SuppressWarnings("unchecked")
	private static void populateNonRepeatData(
			String answertext, int investigationCounterNumber, NbsQuestionMetadata metadata)
					throws NEDSSAppException {
		try {

			if(formSpecificQuestionMap.containsKey(metadata.getQuestionIdentifier()) && investigationCounterNumber == 1){
				formSpecificQuestionAnswerMap.put(metadata.getQuestionIdentifier(), mapCode(metadata.getQuestionIdentifier(),answertext));
			}else if(formSpecificQuestionMap.containsKey(metadata.getQuestionIdentifier()+ SEPERATOR+investigationCounterNumber)){
				formSpecificQuestionAnswerMap.put(metadata.getQuestionIdentifier()+ SEPERATOR+investigationCounterNumber, mapCode(metadata.getQuestionIdentifier(),answertext));
			}else if(formSpecificQuestionMap.containsKey(metadata.getQuestionIdentifier()+CODED_VALUE) && investigationCounterNumber == 1){
				formSpecificQuestionAnswerMap.put(metadata.getQuestionIdentifier()+ CODED_VALUE, mapCode(metadata.getQuestionIdentifier(), answertext));
			}else if(formSpecificQuestionMap.containsKey(metadata.getQuestionIdentifier()+ SEPERATOR+investigationCounterNumber+CODED_VALUE)){
				formSpecificQuestionAnswerMap.put(metadata.getQuestionIdentifier()+ SEPERATOR+investigationCounterNumber+CODED_VALUE, mapCode(metadata.getQuestionIdentifier(), answertext));
			}else if(formSpecificQuestionMap.containsKey(metadata.getQuestionIdentifier()+ SEPERATOR+investigationCounterNumber+CODED_VALUE_TRANSLATED)){
				formSpecificQuestionAnswerMap.put(metadata.getQuestionIdentifier()+ SEPERATOR+investigationCounterNumber+CODED_VALUE_TRANSLATED, mapCode(metadata.getQuestionIdentifier(), answertext));
			} else if(formSpecificQuestionMap.containsKey(metadata.getQuestionIdentifier()+CODED_VALUE_TRANSLATED) && investigationCounterNumber == 1){
				formSpecificQuestionAnswerMap.put(metadata.getQuestionIdentifier()+CODED_VALUE_TRANSLATED, mapCode(metadata.getQuestionIdentifier(), answertext));
			}


		}
		catch (Exception e) {
			logger.error(e);
			logger.error("CommonPDFPrintForm.populateNonRepeatData Exception thrown, "+ e);
			throw new NEDSSAppException("CommonPDFPrintForm.populateNonRepeatData Exception thrown, ", e);
		}
	}

	/**
	 * Put a space between chars to show correctly on the form.
	 * @param st
	 * @return
	 */
	public static String addEmptySpaces(String st) {
		return addEmptySpaces(st," ");
	}
	
	/**
	 * Put a space between chars to show correctly on the form.
	 * @param st
	 * @param space
	 * @return
	 */
	public static String addEmptySpaces(String st, String space) {
		StringBuffer sb = new StringBuffer();
		
		sb.append(space);
		for (int i = 0; i < st.length(); i++) {
			sb.append(st.charAt(i)).append(space);
		}
		return sb.toString();
	}
	
	/**
	 * Get the STD-MIS condition code to display.
	 * @param cd
	 * @return
	 */
	protected static String getAltCond(String cd) {
		String text;
		text = cd;
		if(text != null){
			CachedDropDownValues cddv = new CachedDropDownValues();
			String shortcd = cddv.getDiagnosisCodeForConceptCode(text);
			if((shortcd!=null && shortcd.equalsIgnoreCase("350")) || (text!=null && text.equalsIgnoreCase("10280")) )//Gonorrhea and Gonorrhea resistant have the same concept_code=10280
				shortcd="300";
			
			text = shortcd != null ? shortcd : text;
			
		}
		return text;
	}
	
	/**
	 * Get the juris desc for the Field Record from.
	 * @param cd
	 * @return
	 */
	protected static String getJurisdictionDesc(String cd) {
		String text;
		text = cd;
		if(text != null){
			CachedDropDownValues cddv = new CachedDropDownValues();
			String description = cddv.getJurisdictionDesc(text);

			text = description != null ? description : text;
		}
		return text;
	}
	
	/**
	 * getStateByCode() it returns the name of the state from the code
	 * 
	 * @param cd
	 * @return
	 */
	protected static String getStateByCode(String cd){
		
		String text;
		text = cd;
		if(text != null){
			CachedDropDownValues cddv = new CachedDropDownValues();
			String description=null;
			
			if(cddv.getStateTreeMap().get(text)!=null)
				description = cddv.getStateTreeMap().get(text).toString();

			text = description != null ? description : text;
		}
		return text;
	}

	/**
	 * Get the AR (Lab or Morb) that is the source of the investigation , if any
	 * Uses the time to determine the source.
	 * @param proxyVO
	 * @return
	 * @throws NEDSSAppException
	 */
	public static ActRelationshipDT getSourceAct(PageProxyVO proxyVO) throws NEDSSAppException {
		try {
			Timestamp addTime = proxyVO.getPublicHealthCaseVO()
					.getThePublicHealthCaseDT().getAddTime();
			Collection<Object> coll = proxyVO.getPublicHealthCaseVO()
					.getTheActRelationshipDTCollection();
			Iterator<Object> iterator = coll.iterator();
			while (iterator.hasNext()) {
				ActRelationshipDT ar = (ActRelationshipDT) iterator.next();
				//			DateFormat format =  DateFormat.getDateInstance(DateFormat.MEDIUM);
				//
				//			if (ar.getFromTime() != null
				//					&& format.format(addTime).equals(format.format(ar.getFromTime())) )
				if (ar.getFromTime() != null
						&& addTime.compareTo(ar.getFromTime())==0 )
					return ar;
			}
		} catch (Exception e) {
			logger.error("Error while retrieving getSourceAct:  "+ e);
			throw new NEDSSAppException("Error while retrieving getSourceAct:  "+e);
		}

		return null;

	}

	/**
	 * populateLabsWithSOurceLabresultsFirst - if a lab result is the source of an Investigation, process it first. 
	 * If a morb started the investigation, process all the labs in the morb first.
	 * @param sourceObsUid
	 * @param actProxyVO
	 * @param labsProcessed
	 * @param invCnt 
	 * @throws Exception
	 */
	private static void populateLabsWithSourceLabResultsFirst(Long sourceObsUid,  PageActProxyVO actProxyVO , ArrayList<String> labsProcessed, int invCnt) throws Exception {
		Timestamp firstLabDate = null;
		try {

			//if the sourceObsUid is not null, see if the source is a lab or morb
			//and get the lab collection and process it first.
			if (sourceObsUid != null) {
				Collection<Object> sourceLabSummaryCollection = null;
				if (actProxyVO.getTheMorbReportSummaryVOCollection() != null && !actProxyVO.getTheMorbReportSummaryVOCollection().isEmpty()) {
					//only one morb report
					Iterator<Object> morbIt  = 	actProxyVO.getTheMorbReportSummaryVOCollection().iterator();
					MorbReportSummaryVO morbSummaryVO = (MorbReportSummaryVO)morbIt.next();
					//Is this a T2?
					if (morbSummaryVO.getUid() != null && morbSummaryVO.getUid().longValue() == sourceObsUid.longValue() && !morbSummaryVO.isMorbFromDoc()) {
						sourceLabSummaryCollection = morbSummaryVO.getTheLabReportSummaryVOColl();
						if (sourceLabSummaryCollection != null && !sourceLabSummaryCollection.isEmpty()) {
							Iterator<Object> labCollIter = sourceLabSummaryCollection.iterator();
							//process all labs in the morb into the staging area
							while (labCollIter.hasNext()) {
								LabReportSummaryVO labRptSumVO = (LabReportSummaryVO) labCollIter.next();
								if(!labsProcessed.contains(labRptSumVO.getLocalId())){
									processLabIntoLabStagingList(labRptSumVO, labStagingArray, morbSummaryVO.getProviderDataForPrintVO().getFacility());
									labsProcessed.add(labRptSumVO.getLocalId());
								}
							}
						}
					}
				}
				if (sourceLabSummaryCollection == null) { //must be a T2
					Collection<Object> labColl = actProxyVO.getTheLabReportSummaryVOCollection();
					if (labColl != null && !labColl.isEmpty()) {
						//find the lab that was the source observation 
						Iterator<Object> labCollIter = labColl.iterator();
						while (labCollIter.hasNext()) {
							LabReportSummaryVO labRptSumVO = (LabReportSummaryVO) labCollIter.next();
							Timestamp labDate = labRptSumVO.getDateCollected();
							if (labDate == null)
								labDate = labRptSumVO.getDateReceived();
							if (firstLabDate == null && labDate!=null)
								firstLabDate = labDate;
							else if (firstLabDate != null && labDate != null) {
								if (firstLabDate.after(labDate))
									firstLabDate = labDate;
							}
							if (labRptSumVO.getObservationUid().longValue() == sourceObsUid.longValue() && !labRptSumVO.isLabFromDoc())
								if(!labsProcessed.contains(labRptSumVO.getLocalId())){
									processLabIntoLabStagingList(labRptSumVO, labStagingArray, null);
									labsProcessed.add(labRptSumVO.getLocalId());
								}
						}
						//now put the rest into the staging area if there is room
						labCollIter = labColl.iterator();
						while (labCollIter.hasNext()) {
							LabReportSummaryVO labRptSumVO = (LabReportSummaryVO) labCollIter.next();
							if (labRptSumVO.getObservationUid().longValue() != sourceObsUid.longValue() && !labRptSumVO.isLabFromDoc()) {
								if(!labsProcessed.contains(labRptSumVO.getLocalId())){
									processLabIntoLabStagingList(labRptSumVO, labStagingArray, null);
									labsProcessed.add(labRptSumVO.getLocalId());
								}
							}
						}

					}
				}
			}

			//Now go through the lab collection and see if we can add to our list
			Collection<Object> coll = actProxyVO.getTheLabReportSummaryVOCollection();
			//if there is a source observation (investigation started from a lab) - use that one
			if (coll != null && !coll.isEmpty() && labIndex< 5){
				Iterator<Object> collIter = coll.iterator();
				while (collIter.hasNext()) {
					LabReportSummaryVO labReportSummaryVO = (LabReportSummaryVO) collIter.next();
					Timestamp labDate = labReportSummaryVO.getDateCollected();
					if (labDate == null)
						labDate = labReportSummaryVO.getDateReceived();
					if (firstLabDate == null && labDate!=null)
						firstLabDate = labDate;
					else if (firstLabDate != null && labDate != null) {
						if (firstLabDate.after(labDate))
							firstLabDate = labDate;
					}
					if(!labsProcessed.contains(labReportSummaryVO.getLocalId()) && !labReportSummaryVO.isLabFromDoc()){
						processLabIntoLabStagingList(labReportSummaryVO, labStagingArray, null);
						labsProcessed.add(labReportSummaryVO.getLocalId());
					}
				} //has next
			} //coll != null


			if(actProxyVO.getTheMorbReportSummaryVOCollection() != null 
					&& !actProxyVO.getTheMorbReportSummaryVOCollection().isEmpty() && labIndex< 5){
				Iterator<Object> it  = 	actProxyVO.getTheMorbReportSummaryVOCollection().iterator();
				MorbReportSummaryVO summaryVO = (MorbReportSummaryVO)it.next();
				if(summaryVO.getTheLabReportSummaryVOColl()!=null && !summaryVO.isMorbFromDoc()){
					Iterator<Object> collIter = summaryVO.getTheLabReportSummaryVOColl().iterator();
					while (collIter.hasNext()) {
						LabReportSummaryVO labReportSummaryVO = (LabReportSummaryVO) collIter.next();
						if(!labsProcessed.contains(labReportSummaryVO.getLocalId())){
							processLabIntoLabStagingList(labReportSummaryVO, labStagingArray, summaryVO.getProviderDataForPrintVO().getFacility());
							labsProcessed.add(labReportSummaryVO.getLocalId());
							if (labIndex==4)
								break;
						}
					}
				}
			}
			//This field is the earliest lab date
			if (firstLabDate != null) {
				String firstLabDateStr = StringUtils.formatDate(firstLabDate);
				if (firstLabDateStr != null)
					formSpecificQuestionAnswerMap.put(FirstLabDateLAB201+invCnt, firstLabDateStr);
			}
		} catch (Exception e) {
			logger.debug("Error while processing populateLabsWithSourceLabResultsFirst:  "+ e);
			logger.error("Error while processing populateLabsWithSourceLabResultsFirst:  " + e);
			throw new NEDSSAppException("Application error while processing populateLabsWithSourceLabResultsFirst:  " + e);
		}

	}

	/**
	 * processLabIntoLabStagingList - Process the lab into the two dimentional string array staging area
	 * @param labReportSummaryVO
	 * @param theLabStagingArray
	 * @throws NEDSSAppException
	 */
	protected static void processLabIntoLabStagingList(LabReportSummaryVO labReportSummaryVO,
			String[][] theLabStagingArray, String morbRptFacilityName) throws NEDSSAppException {

		boolean organismNamePresent = false;
		
		try {
		if (labReportSummaryVO
				.getTheResultedTestSummaryVOCollection() != null && !labReportSummaryVO.isLabFromDoc()) {
			Iterator<Object> iter = labReportSummaryVO.getTheResultedTestSummaryVOCollection().iterator();
			while (iter.hasNext()) {
				ResultedTestSummaryVO resultedSummaryVO = (ResultedTestSummaryVO) iter.next();
				//build the Result string
				StringBuffer labResultSB = new StringBuffer("");
				labResultSB
				.append(resultedSummaryVO
						.getNumericResultCompare() == null ? EMPTY_STRING
								: " "+resultedSummaryVO
								.getNumericResultCompare());
				labResultSB
				.append(resultedSummaryVO
						.getNumericResultValue1() == null ? EMPTY_STRING
								: " "+resultedSummaryVO
								.getNumericResultValue1());
				labResultSB
				.append(resultedSummaryVO
						.getNumericResultSeperator() == null ? EMPTY_STRING
								: " "+resultedSummaryVO
								.getNumericResultSeperator());
				labResultSB
				.append(resultedSummaryVO
						.getNumericResultValue2() == null ? EMPTY_STRING
								: " "+resultedSummaryVO
								.getNumericResultValue2());
				labResultSB
				.append(resultedSummaryVO
						.getNumericResultUnits() == null ? EMPTY_STRING
								: " "+resultedSummaryVO
								.getNumericResultUnits());
				
				if (resultedSummaryVO.getOrganismName() != null && !resultedSummaryVO.getOrganismName().isEmpty()) {
					if (!(resultedSummaryVO.getCodedResultValue() != null && resultedSummaryVO.getCodedResultValue().contains(resultedSummaryVO.getOrganismName()))) {
						if (labResultSB.length() > 0)
							labResultSB.append("/");
						labResultSB.append(resultedSummaryVO.getOrganismName()).append(" ");
						organismNamePresent = true;
					}
				}
					
				if (resultedSummaryVO.getTextResultValue() != null && !resultedSummaryVO.getTextResultValue().isEmpty()) {
					if (labResultSB.length() > 0)
						labResultSB.append("/");
					labResultSB.append(resultedSummaryVO.getTextResultValue()).append(" ");
				}
				String labResultStr =  null;
				if(resultedSummaryVO.getCodedResultValue()!=null && !(labResultSB.length() == 0) && !labResultSB.toString().trim().equals("")){
					if (organismNamePresent)
						labResultStr =  labResultSB.toString();
					else
						labResultStr =  resultedSummaryVO.getCodedResultValue()+"/"+labResultSB.toString();
					
				}else if(!(labResultSB.length() == 0) && !labResultSB.toString().trim().equals("") && resultedSummaryVO.getCodedResultValue()==null){
					labResultStr = labResultSB.toString();
					
				}else if( ((labResultSB.length() == 0) || labResultSB.toString().trim().equals("")) && resultedSummaryVO.getCodedResultValue()!=null){
					labResultStr = resultedSummaryVO.getCodedResultValue();
				}
				
				labStagingArray[labIndex-1][TEST_INDEX] =  resultedSummaryVO.getResultedTest();
				if (labResultStr != null)
					labStagingArray[labIndex-1][RESULT_INDEX] =  labResultStr;
				else
					labStagingArray[labIndex-1][RESULT_INDEX] = " ";
				//Per JW, for Morb use the Morb Facility for the Lab facility
				String theReportingFacility = labReportSummaryVO.getReportingFacility();
				if (theReportingFacility == null || theReportingFacility.isEmpty()) {
					if (morbRptFacilityName != null && !morbRptFacilityName.isEmpty())
						theReportingFacility = morbRptFacilityName;
				}
				labStagingArray[labIndex-1][LAB_INDEX] = 
							checkNull(theReportingFacility);
				
				//Date for every resulted test of a lab report
				Timestamp  labDate = null;
				if (labReportSummaryVO.getDateCollected() != null)
					labDate = labReportSummaryVO.getDateCollected();
				else
					labDate = labReportSummaryVO.getDateReceived();
				String labDateStr = formatDate(labDate);
				labStagingArray[labIndex-1][DATE_INDEX] = labDateStr;
				labStagingArray[labIndex-1][FORMATTED_FOR_SORT_DATE_INDEX] = formatDateForSort(labDate);
				
				String specimenSource = labReportSummaryVO.getSpecimenSource();
				labStagingArray[labIndex-1][SPECIMEN_SOURCE_INDEX] = specimenSource;
				
				labIndex++;
				
				if(labIndex > 8)
					break;
			} //hasNext lab result
		} //resulted test coll not null
		
		} catch (Exception e) {
			logger.error("Error while processing processLabIntoLabStagingList:  " + e.getMessage(), e);
			throw new NEDSSAppException("Application error occurred while processing processLabIntoLabStagingList:  " + e);
		}
		return; 
	}
	
	/**
	 * Format timestamp into date string
	 * @param timestamp
	 * @return
	 */
	protected static String formatDate(Timestamp timestamp) {
		java.util.Date date = null;
		SimpleDateFormat formatter = new SimpleDateFormat("MM/dd/yyyy");
		if(timestamp != null) date = new java.util.Date(timestamp.getTime());
	      if (date == null) {
	          return new String("");
	       }
	       else {
	          return formatter.format(date);
	       }
	}
	/**
	 * Format timestamp into date string
	 * @param timestamp
	 * @return
	 */
	protected static String formatDateForSort(Timestamp timestamp) {
		java.util.Date date = null;
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy/MM/dd");
		if(timestamp != null) date = new java.util.Date(timestamp.getTime());
	      if (date == null) {
	          return new String("");
	       }
	       else {
	          return formatter.format(date);
	       }
	}

/**
 * Put name and address information into a return map.
 * @param proVO
 * @param nameKey
 * @param includePrefix
 * @param streetAdrKey
 * @param cityAdrKey
 * @param stateAdrKey
 * @param zipAdrKey
 * @param wkPhoneKey
 * @param emailKey
 * @return <String, String> map of values
 */
	public static Map<String, String> putEntityNameAndAddressIntoMap(PersonVO proVO, String nameKey, boolean includePrefix, String streetAdrKey, String cityAdrKey, String stateAdrKey, String zipAdrKey, String wkPhoneKey, String emailKey) {

		Map<String, String> returnMap = new HashMap<String, String>();

		PersonDT personDT = null;
		PostalLocatorDT postalDT = null;
		TeleLocatorDT teleDT = null;

		StringBuffer stBuff = new StringBuffer("");
		personDT = proVO.getThePersonDT();

		if (proVO.getThePersonNameDTCollection() != null) {

			Iterator personNameIt = proVO.getThePersonNameDTCollection()
					.iterator();
			while (personNameIt.hasNext()) {
				PersonNameDT personNameDT = (PersonNameDT) personNameIt.next();
				if (personNameDT.getNmUseCd().equalsIgnoreCase("L")) {
					if (includePrefix)
						stBuff.append((personNameDT.getNmPrefix() == null) ? ""
								: (personNameDT.getNmPrefix() + " "));
					stBuff.append((personNameDT.getFirstNm() == null) ? ""
							: (personNameDT.getFirstNm() + " "));
					stBuff.append((personNameDT.getLastNm() == null) ? ""
							: (personNameDT.getLastNm()));
					stBuff.append((personNameDT.getNmSuffix() == null) ? ""
							: (", " + personNameDT.getNmSuffix()));
					stBuff.append(
							(personNameDT.getNmDegree() == null) ? ""
									: (", " + personNameDT.getNmDegree()));
					if (nameKey != null)
						returnMap.put(nameKey, stBuff.toString());
				}
			}
		}

		if (proVO.getTheEntityLocatorParticipationDTCollection() != null) {
			Iterator entIt = proVO
					.getTheEntityLocatorParticipationDTCollection().iterator();
			while (entIt.hasNext()) {
				EntityLocatorParticipationDT entityDT = (EntityLocatorParticipationDT) entIt
						.next();
				if (entityDT != null) {
					if (entityDT.getCd() != null
							&& entityDT.getCd().equalsIgnoreCase("O")
							&& entityDT.getClassCd() != null
							&& entityDT.getClassCd().equalsIgnoreCase("PST")
							&& entityDT.getRecordStatusCd() != null
							&& entityDT.getRecordStatusCd().equalsIgnoreCase(
									"ACTIVE")
									&& entityDT.getUseCd().equalsIgnoreCase("WP")) {
						postalDT = entityDT.getThePostalLocatorDT();
						stBuff = new StringBuffer("");
						stBuff.append((postalDT.getStreetAddr1() == null) ? ""
								: (postalDT.getStreetAddr1()));
						stBuff.append((postalDT.getStreetAddr2() == null) ? ""
								: (", " + postalDT.getStreetAddr2()));
						if (streetAdrKey != null)
							returnMap.put(streetAdrKey, stBuff.toString());
						if (cityAdrKey != null) {
							String cityStr = postalDT.getCityDescTxt() == null ? ""
									: postalDT.getCityDescTxt();
							returnMap.put(cityAdrKey, cityStr);

						}
						if (stateAdrKey != null) {
							String stateStr = postalDT.getStateCd() == null ? "" 
									: getStateDescTxt(postalDT.getStateCd());
							returnMap.put(stateAdrKey, stateStr);
						}
						if (zipAdrKey != null)
							returnMap.put(zipAdrKey, postalDT.getZipCd() == null ? ""
									: postalDT.getZipCd());

					} //address fields

					if (entityDT.getClassCd() != null) {
						if (entityDT.getClassCd().equalsIgnoreCase(
								"TELE")
								&& entityDT.getRecordStatusCd() != null
								&& entityDT.getRecordStatusCd()
								.equalsIgnoreCase("ACTIVE")
								&& entityDT.getCd() != null
								&& (entityDT.getCd().equalsIgnoreCase("O") || entityDT.getCd().equalsIgnoreCase("PH")  || entityDT.getCd().equalsIgnoreCase("FAX"))
								&& entityDT.getUseCd() != null
								&& entityDT.getUseCd().equalsIgnoreCase("WP")) {
							teleDT = entityDT.getTheTeleLocatorDT();

							if (emailKey != null)
								returnMap.put(emailKey, teleDT.getEmailAddress() == null ? ""
										: (teleDT.getEmailAddress()));
							if (wkPhoneKey != null) {
								stBuff = new StringBuffer("");
								stBuff.append((teleDT.getPhoneNbrTxt() == null) ? ""
										: (teleDT.getPhoneNbrTxt() + " "));
								String ext = "";
								//ELRs store an .0 after the extension
								if(teleDT.getExtensionTxt()!=null && !teleDT.getExtensionTxt().equals("0.0")){
									ext = teleDT.getExtensionTxt().replace(".0", "");
									stBuff.append("Ext." + ext);
								}
								returnMap.put(wkPhoneKey, stBuff.toString());

							}
						}
					}//phone get class cd
				}
			}
		}
		return returnMap;
	}
	
	/**
	 * If the PHC has a value in NBS291 (ND-3847) which is a participation OrgAsClinicOfPHC then this
	 * address should show. In particular, they want the Fax number from this.
	 * @param proVO
	 * @param facilityVO
	 * @param nameKey
	 * @param includePrefix
	 * @param streetAdrKey
	 * @param cityAdrKey
	 * @param stateAdrKey
	 * @param zipAdrKey
	 * @param wkPhoneKey
	 * @param emailKey
	 * @return
	 */
	public static Map<String, String> putEntityNameAndAddressIntoMap(PersonVO proVO, OrganizationVO orgVO, String nameKey, boolean includePrefix, String streetAdrKey, String cityAdrKey, String stateAdrKey, String zipAdrKey, String wkPhoneKey, String emailKey) {

		Map<String, String> returnMap = new HashMap<String, String>();

		PersonDT personDT = null;
		PostalLocatorDT postalDT = null;
		TeleLocatorDT teleDT = null;

		StringBuffer stBuff = new StringBuffer("");
		personDT = proVO.getThePersonDT();

		if (proVO.getThePersonNameDTCollection() != null) {

			Iterator personNameIt = proVO.getThePersonNameDTCollection()
					.iterator();
			while (personNameIt.hasNext()) {
				PersonNameDT personNameDT = (PersonNameDT) personNameIt.next();
				if (personNameDT.getNmUseCd().equalsIgnoreCase("L")) {
					if (includePrefix)
						stBuff.append((personNameDT.getNmPrefix() == null) ? ""
								: (personNameDT.getNmPrefix() + " "));
					stBuff.append((personNameDT.getFirstNm() == null) ? ""
							: (personNameDT.getFirstNm() + " "));
					stBuff.append((personNameDT.getLastNm() == null) ? ""
							: (personNameDT.getLastNm()));
					stBuff.append((personNameDT.getNmSuffix() == null) ? ""
							: (", " + personNameDT.getNmSuffix()));
					stBuff.append(
							(personNameDT.getNmDegree() == null) ? ""
									: (", " + personNameDT.getNmDegree()));
					if (nameKey != null)
						returnMap.put(nameKey, stBuff.toString());
				}
			}
		}
		if (orgVO.getTheOrganizationNameDTCollection() != null) {
		      Collection<Object>  names = orgVO.getTheOrganizationNameDTCollection();
		      if (names != null) {
		    	  Iterator<Object>  iter = names.iterator();
		    	  while (iter.hasNext()) {
		    		  OrganizationNameDT name = (OrganizationNameDT) iter.next();
		    		  if (name != null) {
		            // for organizationInfo
		    			  if (name.getNmTxt() != null) {
		    				  String facilityName = name.getNmTxt();
		    				  stBuff.append("/" + facilityName);
		    				  if (nameKey != null)
		  						returnMap.put(nameKey, stBuff.toString());
		    			  }
		          } //not null
		        } //hasNext
		      } //org names not null
		} //name collection not null
		if (orgVO.getTheEntityLocatorParticipationDTCollection() != null) {
			Iterator entIt = orgVO
					.getTheEntityLocatorParticipationDTCollection().iterator();
			while (entIt.hasNext()) {
				EntityLocatorParticipationDT entityDT = (EntityLocatorParticipationDT) entIt
						.next();
				if (entityDT != null) {
					if (entityDT.getCd() != null
							&& entityDT.getCd().equalsIgnoreCase("O")
							&& entityDT.getClassCd() != null
							&& entityDT.getClassCd().equalsIgnoreCase("PST")
							&& entityDT.getRecordStatusCd() != null
							&& entityDT.getRecordStatusCd().equalsIgnoreCase(
									"ACTIVE")
									&& entityDT.getUseCd().equalsIgnoreCase("WP")) {
						postalDT = entityDT.getThePostalLocatorDT();
						stBuff = new StringBuffer("");
						stBuff.append((postalDT.getStreetAddr1() == null) ? ""
								: (postalDT.getStreetAddr1()));
						stBuff.append((postalDT.getStreetAddr2() == null) ? ""
								: (", " + postalDT.getStreetAddr2()));
						if (streetAdrKey != null)
							returnMap.put(streetAdrKey, stBuff.toString());
						if (cityAdrKey != null) {
							String cityStr = postalDT.getCityDescTxt() == null ? ""
									: postalDT.getCityDescTxt();
							returnMap.put(cityAdrKey, cityStr);

						}
						if (stateAdrKey != null) {
							String stateStr = postalDT.getStateCd() == null ? "" 
									: getStateDescTxt(postalDT.getStateCd());
							returnMap.put(stateAdrKey, stateStr);
						}
						if (zipAdrKey != null)
							returnMap.put(zipAdrKey, postalDT.getZipCd() == null ? ""
									: postalDT.getZipCd());

					} //address fields

					if (entityDT.getClassCd() != null) {
						if (entityDT.getClassCd().equalsIgnoreCase(
								"TELE")
								&& entityDT.getRecordStatusCd() != null
								&& entityDT.getRecordStatusCd()
								.equalsIgnoreCase("ACTIVE")
								&& entityDT.getCd() != null
								&& (entityDT.getCd().equalsIgnoreCase("O") || entityDT.getCd().equalsIgnoreCase("PH") || entityDT.getCd().equalsIgnoreCase("FAX"))
								&& entityDT.getUseCd() != null
								&& entityDT.getUseCd().equalsIgnoreCase("WP")) {
							
							teleDT = entityDT.getTheTeleLocatorDT();

							if (emailKey != null)
								returnMap.put(emailKey, teleDT.getEmailAddress() == null ? ""
										: (teleDT.getEmailAddress()));
							if (wkPhoneKey != null) {
								if (returnMap.get(wkPhoneKey) != null)
									stBuff = new StringBuffer(returnMap.get(wkPhoneKey) + "\n");
								else 
									stBuff = new StringBuffer("");
								stBuff.append((teleDT.getPhoneNbrTxt() == null) ? ""
										: (teleDT.getPhoneNbrTxt() + " "));
								String ext = "";
								if(teleDT.getExtensionTxt()!=null && !teleDT.getExtensionTxt().equals("0.0")){
									ext = teleDT.getExtensionTxt().replace(".0", "");
									stBuff.append("Ext." + ext);
								}
								stBuff.append("(" + entityDT.getCd() + ") ");
								returnMap.put(wkPhoneKey, stBuff.toString());
							}
						} //if tele
					}//phone get class cd
				} //entityDT not null
			}
		}
		returnMap.put("ClinicPresent", "NBS291"); //flag - this takes precidence over the Lab
		return returnMap;
	}
	
	
	private static String getOtherInfo(){
		
		if (answerMap == null)
			return null;
		String otherInfo = null;
		if (answerMap.containsKey(PageConstants.OTHER_IDENTIFYING_INFORMATION)) {
			otherInfo = (String) answerMap.get(PageConstants.OTHER_IDENTIFYING_INFORMATION);
		}
		
		
		if (otherInfo != null && !otherInfo.isEmpty()) {
			otherInfo+="\n";
		}
		
		
		return otherInfo;
		
	}
	
	
	/**
	 * OTHER_INFO containsINV182 (physician and associated address); NBS159 (other identifying info)
	 * @param mappedEntityValues
	 * @param answerMap
	 * @return
	 */
	private static String getProviderOtherInfo(
			Map<String, String> mappedEntityValues,
			Map<Object, Object> answerMap) {
		StringBuffer strBuff = new StringBuffer("");


		if (mappedEntityValues == null || answerMap == null)
			return null;
		String otherInfo = null;
		if (answerMap.containsKey(PageConstants.OTHER_IDENTIFYING_INFORMATION)) {
			otherInfo = (String) answerMap.get(PageConstants.OTHER_IDENTIFYING_INFORMATION);
		}

		try{

			if (mappedEntityValues.containsKey(PROVIDER_NAME)) {
				strBuff.append(mappedEntityValues.get(PROVIDER_NAME));
				if (otherInfo == null || otherInfo.isEmpty())
					strBuff.append("\n");
				else 
					strBuff.append(", ");
				if (mappedEntityValues.containsKey(PROVIDER_ADDRESS)) {
					strBuff.append(mappedEntityValues.get(PROVIDER_ADDRESS));
					if (otherInfo == null || otherInfo.isEmpty())
						strBuff.append("\n");
					else 
						strBuff.append(", ");
				}
				if (mappedEntityValues.containsKey(PROVIDER_CITY)) {
					strBuff.append(mappedEntityValues.get(PROVIDER_CITY));
					if (mappedEntityValues.containsKey(STATE_STATE_SHORT_NAME)) {
						strBuff.append(", ");
						strBuff.append(mappedEntityValues.get(STATE_STATE_SHORT_NAME));
					}
					if (mappedEntityValues.containsKey(PROVIDER_ZIP)) {
						strBuff.append(" ");
						strBuff.append(mappedEntityValues.get(PROVIDER_ZIP));
					}
					strBuff.append("\n");
				}
				if (mappedEntityValues.containsKey(PROVIDER_PHONE)) {
					strBuff.append(mappedEntityValues.get(PROVIDER_PHONE));
					strBuff.append(" (W)");
				}				

			}

			if (otherInfo != null && !otherInfo.isEmpty()) {
				strBuff.append("\n");
				strBuff.append(otherInfo);
			}

		}catch(Exception e){
			logger.error("getProviderOtherInfo: Error while getting Other Info value:" + e.getMessage());
			return "";
		}
		return strBuff.toString();
	}

	/**
	 * Get the 2 digit state code from the cache i.e. 13=GA
	 * @param sStateCd
	 * @return
	 */

	protected static String getStateDescTxt(String sStateCd) {

		String desc = "";
		if (sStateCd != null && !sStateCd.trim().equals("")) {
			CachedDropDownValues srtValues = new CachedDropDownValues();
			//TreeMap treemap = srtValues.getStateCodes2("USA");
			TreeMap treemap = srtValues.getStateAbbreviationsByCode();
			if (treemap != null) {
				if (sStateCd != null && treemap.get(sStateCd) != null) {
					desc = (String) treemap.get(sStateCd);
				}
			}
		}

		return desc;
	}
	/**
	 * Put interview values into form Ans Map if present.
	 * @param investigationCounter
	 * @param actProxyVO
	 * @param req
	 * @return map with Interview Values
	 * @throws NEDSSAppException
	 */
	private static Map<String, String>  populateInterviewIntoMap(int investigationCounter, PageActProxyVO actProxyVO, HttpServletRequest req) throws NEDSSAppException {

		Map<String, String> returnMap = new HashMap<String,String>();
		//Map<Object, Object>  thisAnswerMap = updateMapWithQIds(((PageActProxyVO) proxyVO).getPageVO()
		//		.getPamAnswerDTMap());


		if(actProxyVO.getPublicHealthCaseVO().getTheCaseManagementDT().getPatIntvStatusCd()!= null){
			String patientInterviewStatus = actProxyVO.getPublicHealthCaseVO().getTheCaseManagementDT().getPatIntvStatusCd();
			String status = NBS192_R_PatientInterviewStatusMap.get(patientInterviewStatus);
			returnMap.put("NBS192_I2_"+investigationCounter+CODED_VALUE_TRANSLATED, status);
		}
		
		if(actProxyVO.getPublicHealthCaseVO().getThePublicHealthCaseDT().getLocalId() != null){
			returnMap.put("New_CaseNo_"+investigationCounter, actProxyVO.getPublicHealthCaseVO().getThePublicHealthCaseDT().getLocalId());
		}
		//per Anthony Meriweather, only use Diagnosis Reported to CDC
		//sooo comment this out..shouldn't be in this method anyway!
		//String condCd = actProxyVO.getPublicHealthCaseVO().getThePublicHealthCaseDT().getCd() ;
		//if(condCd != null){
		//	String shortcd = getAltCond(condCd);
		//	condCd = shortcd != null ? shortcd : condCd;
		//	returnMap.put(NBS136+SEPERATOR+investigationCounter+CODED_VALUE, condCd);		
		//}

		if(actProxyVO.getTheInterviewSummaryDTCollection()!=null && actProxyVO.getTheInterviewSummaryDTCollection().size()>0){
			Iterator iterator= actProxyVO.getTheInterviewSummaryDTCollection().iterator();
			while(iterator.hasNext()){
				InterviewSummaryDT interviewSummaryDT = (InterviewSummaryDT)iterator.next();
				if(interviewSummaryDT != null){
					if(interviewSummaryDT.getInterviewTypeCd()!= null){
						returnMap.put("IXS105_" + investigationCounter ,mapCode("IXS105",interviewSummaryDT.getInterviewTypeCd()));
						if(interviewSummaryDT.getInterviewTypeCd().equalsIgnoreCase("INITIAL")) {
							if	(proxyVO.getPublicHealthCaseVO().getTheCaseManagementDT().getFldFollUpDispoDate() != null ){
								String dispodate = DateFormat.getDateInstance(DateFormat.MEDIUM).format(proxyVO.getPublicHealthCaseVO().getTheCaseManagementDT().getFldFollUpDispoDate());
								returnMap.put("NBS174_" + investigationCounter, dispodate);
							}

							if(interviewSummaryDT.getInterviewerQuickCd()!=null){
								returnMap.put("IXS102_IR_I1_"+investigationCounter+CODED_VALUE, interviewSummaryDT.getInterviewerQuickCd());
							}
							if(interviewSummaryDT.getInterviewDate() != null){
								returnMap.put("IXS101_" + investigationCounter, StringUtils.formatDate(interviewSummaryDT.getInterviewDate()));
								returnMap.put("IXS101_IR_I1_" + investigationCounter, StringUtils.formatDate(interviewSummaryDT.getInterviewDate()));
							}
						}
						if(interviewSummaryDT.getInterviewStatusCd() != null){
							returnMap.put("IXS100_" + investigationCounter ,mapCode("IXS100",interviewSummaryDT.getInterviewStatusCd()));
						}



						if(interviewSummaryDT.getInterviewLocCd()!=null){
							returnMap.put("IXS106_IR_"+investigationCounter+CODED_VALUE , interviewSummaryDT.getInterviewLocCd());
						}

						if(interviewSummaryDT.getInterviewTypeCd().equalsIgnoreCase("REINTVW")){
							returnMap.put("IXS101_IR_I2_" + investigationCounter ,StringUtils.formatDate(interviewSummaryDT.getInterviewDate()));
							returnMap.put("IXS102_IR_I2_"+investigationCounter+CODED_VALUE  ,interviewSummaryDT.getInterviewerQuickCd());
						}

						if(interviewSummaryDT.getThe900SiteId()!=null){
							returnMap.put("IXS107_IR_" + investigationCounter, interviewSummaryDT.getThe900SiteId());
						}

						String interviewPhcInvFormCd =PageManagementCommonActionUtil.checkIfPublishedPageExists(req, NEDSSConstants.INTERVIEW_BUSINESS_OBJECT_TYPE, actProxyVO.getPublicHealthCaseVO().getThePublicHealthCaseDT().getCd());
						try {
							PageLoadUtil.loadQuestions(interviewPhcInvFormCd);
						} catch (Exception e1) {
							logger.error("populateInterviewIntoMap: Exception loading questions -> " +e1.getMessage(), e1);
						}
						PageActProxyVO interviewProxyVO = (PageActProxyVO) PageLoadUtil.getProxyObject(
								interviewSummaryDT.getInterviewUid().toString(),
								NEDSSConstants.INTERVIEW_BUSINESS_OBJECT_TYPE, req.getSession());
						PageLoadUtil.loadQuestionKeys(interviewPhcInvFormCd);
						Map<Object, Object> interviewAnsMap = PageLoadUtil.updateMapWithQIds(interviewProxyVO.getPageVO().getAnswerDTMap());
						if(interviewAnsMap.containsKey("IXS107") || interviewAnsMap.containsKey("IXS108") ||interviewAnsMap.containsKey("IXS109")){
							String ixs107Val = getAnsFromMap(interviewAnsMap, "IXS107");
							String ixs108Val = getAnsFromMap(interviewAnsMap, "IXS108");
							String ixs109Val = getAnsFromMap(interviewAnsMap, "IXS109");

							if(ixs107Val!=null)
								returnMap.put("IXS107_1", ixs107Val);
							if(ixs108Val!=null)
								returnMap.put("IXS108_1", ixs108Val);
							if(ixs109Val!=null)
								returnMap.put("IXS109_1_CD", ixs109Val);

							returnMap.put("IXS107_IR_" + investigationCounter, ixs107Val);
						}

					}
				}
			}
		}

		try {
			PersonVO fldFupInvestgrOfPHC = PageLoadUtil.getPersonVO("FldFupInvestgrOfPHC", actProxyVO);

			if(fldFupInvestgrOfPHC != null){
				String investigatorId = fldFupInvestgrOfPHC.getEntityIdDT_s(0).getRootExtensionTxt();
				returnMap.put("NBS175_" + investigationCounter, checkNull(investigatorId));
			}
			
			PersonVO initInterviewer = PageLoadUtil.getPersonVO("InitInterviewerOfPHC", actProxyVO);
			if(initInterviewer!=null){
				String initInvestigatorId = initInterviewer.getEntityIdDT_s(0).getRootExtensionTxt();
				returnMap.put("NBS188_" + investigationCounter+CODED_VALUE, checkNull(initInvestigatorId));
				
				PersonVO interviewer = PageLoadUtil.getPersonVO("InterviewerOfPHC", actProxyVO);	
				
				if(interviewer!=null){
					String interviewerId = interviewer.getEntityIdDT_s(0).getRootExtensionTxt();

					if(interviewerId != null && initInvestigatorId != null && !initInvestigatorId.equalsIgnoreCase(interviewerId)){
						returnMap.put(NBS186 + investigationCounter + CODED_VALUE, checkNull(interviewerId));
					}else{
						returnMap.put("NBS187_" + investigationCounter , "");
					}
				}
			}



		} catch (NEDSSAppException e) {
			logger.debug(e.getMessage());
			logger.error("populateInterview: Error occurred during processing ", e.getMessage());
			throw new NEDSSAppException("Application error occurred while processing populateInterview:  " + e);
		} catch (Exception e) {
			logger.debug(e.getMessage());
			logger.error("populateInterview: Error occurred during processing ", e.getMessage());
			throw new NEDSSAppException("Application error occurred while processing populateInterview:  " + e);
		}
		return returnMap;
	}
	

		/**
		 * The answerMap is keyed with the long value of the nbs_question_uid.
		 * The returns the map with the question_identifier (i.e DEM110) as the key
		 * @param answerMap
		 * @return
		 */
		public static Map<Object, Object> updateMapWithQIds(
				Map<Object, Object> answerMap) {
			Map<Object, Object> returnMap = new HashMap<Object, Object>();
			if (answerMap != null && answerMap.size() > 0) {
				Iterator<Object> iter = answerMap.keySet().iterator();
				while (iter.hasNext()) {
					Long key = (Long) iter.next();
					returnMap.put(questionKeyMap.get(key), answerMap.get(key));
				}
			}

			return returnMap;
		}
		/**
		 * Return the specified answer from the Contact Answer Map
		 * @param conAnsMap
		 * @param questionId
		 * @return answerTxt
		 */
		protected static String getContactAnsFromMap(Map<Object, Object> conAnsMap,
				String questionId) {
			CTContactAnswerDT contactAnswerDT = (CTContactAnswerDT) conAnsMap.get(questionId);
			if (contactAnswerDT != null)
				return contactAnswerDT.getAnswerTxt();
			return null;
		}
		/**
		 * Return the answer from the Case ans map
		 * @param invAnsMap
		 * @param questionId
		 * @return
		 */
		protected static String getInvAnsFromMap(Map<Object, Object> invAnsMap,
				String questionId) {
			NbsCaseAnswerDT invAnswerDT = (NbsCaseAnswerDT) invAnsMap.get(questionId);
			if (invAnswerDT != null)
				return invAnswerDT.getAnswerTxt();
			return null;
		}	

		/**
		 * Return the answer from the Case ans map
		 * @param invAnsMap
		 * @param questionId
		 * @return
		 */
		protected static String getAnsFromMap(Map<Object, Object> invAnsMap,
				String questionId) {
			NbsAnswerDT invAnswerDT = (NbsAnswerDT) invAnsMap.get(questionId);
			if (invAnswerDT != null)
				return invAnswerDT.getAnswerTxt();
			return null;
		}	

}
