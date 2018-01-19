package gov.cdc.nedss.webapp.nbs.action.pagemanagement.rendering.util;

/**
 * PageConstants defines the generic MetaData common across PAMs
 * @author nmallela
 * <p>Copyright: Copyright (c) 2008</p>
 * <p>Company: Computer Sciences Corporation</p>
 * PageConstants.java
 * Aug 5, 2008
 * @version
 */
public class PageConstants {

	//Person_Name Constants
	public static final String LAST_NM = "DEM102";
	public static final String FIRST_NM = "DEM104";
	public static final String MIDDLE_NM= "DEM105";
	public static final String SUFFIX= "DEM107";
	public static final String NAME_INFORMATION_AS_OF = "NBS095";

	//Person Constants
	public static final String DEM_DATA_AS_OF = "NBS104";
	public static final String DOB= "DEM115";
	public static final String REP_AGE= "INV2001";
	public static final String REP_AGE_UNITS= "INV2002";
	public static final String CURR_SEX= "DEM113";
	public static final String ADDITIONAL_GENDER="NBS213";
	public static final String SEX_UNKNOWN_REASON="NBS272";
	public static final String TRANSGENDER_INFORMATION="NBS274";

	public static final String IS_PAT_DECEASED= "DEM127";
	public static final String DECEASED_DATE= "DEM128";
	public static final String MAR_STAT= "DEM140";
	public static final String ETHNICITY= "DEM155";
	public static final String ETHNICITY_UNK_REASON= "NBS273";
	public static final String PATIENT_LOCAL_ID = "DEM197";
	public static final String BIRTH_SEX= "DEM114";
	public static final String SEX_AND_BIRTH_INFORMATION_AS_OF = "NBS096";
	public static final String MORTALITY_INFORMATION_AS_OF = "NBS097";
	public static final String MARITAL_STATUS_AS_OF = "NBS098";
	public static final String ETHNICITY_AS_OF = "NBS100";
	public static final String BIRTH_TIME_CALC = "DEM121";
	public static final String EHARS_ID = "NBS269";
	public static final String HEIGHT = "NBS155";
	public static final String SIZE_BUILD = "NBS156";
	public static final String HAIR = "NBS157";
	public static final String COMPLEXION = "NBS158";
	public static final String OTHER_IDENTIFYING_INFORMATION = "NBS159";

	//COntactTracing Specific
	public static final String ALIAS_NICK_NAME = "DEM250";
	public static final String PRIMARY_OCCUPATION = "DEM139";
	public static final String BIRTH_COUNTRY = "DEM126";
	public static final String PRIMARY_LANGUAGE = "DEM142";
	public static final String SPEAKS_ENGLISH = "NBS214";
	public static final String CELL_PHONE = "NBS006";
	public static final String EMAIL = "DEM182";
	//Entity_ID Constants
	public static final String SSN= "DEM133";
	public static final String SSN_AS_OF = "DEM210";

	//Entity_locator_participation Constants
	public static final String ADDRESS_INFORMATION_AS_OF = "NBS102";
	public static final String TELEPHONE_INFORMATION_AS_OF = "NBS103";

	//Postal_Locator Constants
	public static final String ADDRESS_1= "DEM159";
	public static final String ADDRESS_2= "DEM160";
	public static final String CITY= "DEM161";
	public static final String STATE= "DEM162";
	public static final String COUNTY= "DEM165";
	public static final String REPORTING_COUNTY= "AR109";
	public static final String CENSUS_TRACT= "DEM168";
	public static final String ZIP= "DEM163";
	public static final String COUNTRY= "DEM167";
	public static final String WITHIN_CITY_LIMITS= "DEM237";
	public static final String ADDRESS_COMMENTS = "DEM175";


	//Tele_Locator Constants
	public static final String H_PHONE= "DEM177";
	public static final String H_PHONE_EXT= "DEM239";
	public static final String W_PHONE= "NBS002";
	public static final String W_PHONE_EXT= "NBS003";



	//Person_race Constants
	public static final String RACE= "DEM152";
	public static final String DETAILED_RACE_ASIAN= "DEM243";
	public static final String DETAILED_RACE_HAWAII= "DEM245";
	public static final String RACE_INFORMATION_AS_OF = "NBS101";

	//Act_Id Constants
	public static final String STATE_CASE = "INV173";
	public static final String COUNTY_CASE = "INV198";
	public static final String LEGACY_CASE_ID = "INV200";

	//Public_Health_Case Constants
	public static final String GEN_COMMENTS= "DEM196";
	public static final String JURISDICTION = "INV107";
	public static final String PROGRAM_AREA = "INV108";
	public static final String INV_STATUS_CD = "INV109";
	public static final String DATE_REPORTED = "INV111";
	public static final String INV_START_DATE = "INV147";
	public static final String MMWR_WEEK = "INV165";
	public static final String MMWR_YEAR = "INV166";
	public static final String CASE_LOCAL_ID = "INV168";
	public static final String DATE_REPORTED_TO_COUNTY = "INV120";
	public static final String DATE_REPORTED_TO_STATE = "INV121";
	public static final String DIAGNOSIS_DATE = "INV136";
	public static final String ILLNESS_ONSET_DATE = "INV137";
	public static final String PAT_AGE_AT_ONSET_UNIT_CODE = "INV144";
	public static final String PAT_AGE_AT_ONSET = "INV143";
	public static final String DATE_ASSIGNED_TO_INVESTIGATION = "INV110";
	public static final String WAS_THE_PATIENT_HOSPITALIZED = "INV128";
	public static final String ADMISSION_DATE = "INV132";
	public static final String DISCHARGE_DATE = "INV133";
	public static final String DURATION_OF_STAY = "INV134";
	public static final String PREGNANCY_STATUS = "INV178";
	public static final String PREGNANT_WEEKS = "NBS128";

	public static final String DID_THE_PATIENT_DIE = "INV145";
	public static final String IS_PERSON_ASSOCIATED_WITH_DAYCAREFACILITY = "INV148";
	public static final String IS_THIS_PERSON_FOOD_HANDLER = "INV149";
	public static final String IMPORTED_COUNTRY = "INV153";
	public static final String IMPORTED_STATE = "INV154";
	public static final String IMPORTED_CITY = "INV155";
	public static final String IMPORTED_COUNTY  = "INV156";
	public static final String INVESTIGATION_DEATH_DATE ="INV146";
	public static final String OUTBREAK_INDICATOR ="INV150";
	public static final String OUTBREAK_NAME ="INV151";

	// The following field should be converted to INV-
	public static final String CASE_ADD_TIME = "INV194";
	public static final String CASE_ADD_USERID = "INV195";
	//public static final String CASE_LC_USERTIME = "TUB251";
	//public static final String CASE_LC_USERID = "TUB252";
	public static final String SHARED_IND = "NBS012";
	//public static final String CASE_ADD_TIME = "TUB261";
	public static final String CONDITION_CD = "INV169";
	public static final String RECORD_STATUS_CD = "INV230";
	public static final String RECORD_STATUS_TIME = "INV234";
	public static final String STATUS_CD = "INV235";
	public static final String PROGRAM_JURISDICTION_OID = "INV213";
	public static final String VERSION_CTRL_NBR = "INV237";
	public static final String CASE_CLS_CD = "INV163";
	public static final String TUB_GEN_COMMENTS = "INV167";
	//Common Constants
	public static final String REQ_FOR_NOTIF = "REQ_FOR_NOTIF";
	public static final String NO_REQ_FOR_NOTIF_CHECK = "NO_REQ_FOR_NOTIF_CHECK";
	public static final String FIELD_LIST_TO_HILIGHT = "field_list_to_hilight";

	//R2.0.2 Additions
	public static final String REPORTING_SOURCE = "INV112";
	public static final String ILLNESS_END_DATE = "INV138";
	public static final String ILLNESS_DURATION = "INV139";
	public static final String ILLNESS_DURATION_UNITS = "INV140";
	public static final String DISEASE_IMPORT_CD = "INV152";
	public static final String TRANSMISN_MODE_CD = "INV157";
	public static final String DETECTION_METHOD_CD = "INV159";
	public static final String CONFIRM_METHOD_CD = "INV161";
	public static final String CONFIRM_DATE = "INV162";

	//R4.5 Additions

	public static final String REFERRAL_BASIS_CD="NBS110";
	public static final String CURR_PROCESS_STAGE_CD="NBS115";
	public static final String INV_CLOSED_DATE="INV2006";
	//public static final String INV_PRIORITY_CD="";

	//contact tracing
	public static final String CONTACT_PRIORITY = "NBS055";
	public static final String INFECTIOUS_PERIOD_FROM = "NBS056";
	public static final String INFECTIOUS_PERIOD_TO = "NBS057";
	public static final String CONTACT_STATUS = "NBS058";
	public static final String CONTACT_COMMENTS = "NBS059";

	//interview
	public static final String INTERVIEW_STATUS = "IXS100";
	public static final String INTERVIEW_DATE = "IXS101";
	public static final String INTERVIEWER = "IXS102";
	public static final String INTERVIEWEE_ROLE = "IXS103";
	public static final String INTERVIEWEE = "IXS104";
	public static final String INTERVIEW_TYPE = "IXS105";
	public static final String INTERVIEW_LOCATION = "IXS106";
	public static final String INTERVIEW_NOTES = "IXS111";
	public static final String HIV900_SiteId = "IXS107";
	public static final String HIV900_StateZipCd = "IXS108";
	public static final String HIV900_SiteType = "IXS109";
	public static final String HIV_INTERVENTIONS = "IXS110";
	
	//Case Management

	//STD 4.5
	public static final String CASE_DIAGNOSIS = "NBS136";
	public static final String CASE_DIAGNOSIS_CODESET = "CASE_DIAGNOSIS";
	public static final String DIAGNOSIS_CAN_CHANGE_CONDITION[] =  {"710","720","730","740","745","750","790","900","950"};


	public static final String INTERNET_FOLLOWUP = "NBS142";
	public static final String NOTIFIABLE = "NBS143";

	public static final String INVESTIGATOR_UID = "CONINV180Uid";
	public static final String INVESTIGATOR_SEARCH_RESULT = "CONINV180SearchResult";
	
	public static final String VACC_INFO_SOURCE_CD="VAC147";
	public static final String MATERIAL_CD="VAC101";
	public static final String ACTIVITY_FROM_TIME="VAC103";
	public static final String TARGET_SITE_CD="VAC104";
	public static final String AGE_AT_VACC="VAC105";
	public static final String AGE_AT_VACC_UNIT_CD="VAC106";
	public static final String VACC_MFGR_CD="VAC107";
	public static final String MATERIAL_LOT_NM="VAC108";
	public static final String MATERIAL_EXPIRATION_TIME="VAC109";
	public static final String VACC_DOSE_NBR="VAC120";
	
	public static final String EVENT_SUMMARY_LOCAL_ID="LocalId";
	public static final String EVENT_SUMMARY_CREATED_ON="CreatedOn";
	public static final String EVENT_SUMMARY_CREATED_BY="CreatedBy";
	public static final String EVENT_SUMMARY_UPDATED_ON="UpdatedOn";
	public static final String EVENT_SUMMARY_UPDATED_BY="UpdatedBy";
	
	public static final String VACCINE_TYPE="VaccineType";
	
}
