//Source file: C:\\ProjectStuff\\RationalRoseDevelopment\\gov\\cdc\\nedss\\deduplication\\ejb\\deduplicationprocessorejb\\dao\\DeDuplicationDAO.java

package gov.cdc.nedss.deduplication.ejb.deduplicationprocessor.dao;

import gov.cdc.nedss.util.*;
import gov.cdc.nedss.entity.person.dt.PersonDT;
import gov.cdc.nedss.entity.person.dt.PersonRaceDT;
import gov.cdc.nedss.entity.person.dt.PersonNameDT;
import gov.cdc.nedss.locator.dt.EntityLocatorParticipationDT;
import gov.cdc.nedss.systemservice.util.PrepareVOUtils;
import gov.cdc.nedss.exception.NEDSSDAOSysException;
import gov.cdc.nedss.exception.NEDSSSystemException;

import java.util.HashMap;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.sql.*;
import java.math.BigDecimal;

public class DeDuplicationDAO extends DAOBase
{
    private static final LogUtils logger = new LogUtils(PrepareVOUtils.class.getName());
    private static PropertyUtil propertyUtil= PropertyUtil.getInstance();
    private static final String RESET_PATIENT_REGISTRY_FOR_DEDUP = "update person set group_nbr = ?, group_time = ?, dedup_match_ind = ? where group_nbr is not null and person.cd = 'PAT'";

    private static final String SELECT_GROUP_NUMBER = "select group_nbr groupNbr from person where person.PERSON_UID = person.PERSON_PARENT_UID AND person.GROUP_NBR IS NOT NULL";
    private static final String SELECT_PIVOTS = "select distinct pn.person_uid \"personUid\", pn.as_of_date \"asOfDate\" from person_name pn, person p, person_name pn2 " +
                                                "where p.person_uid = pn.person_uid " +
                                                "and p.cd = " +
                                                "'" + NEDSSConstants.PAT + "' " +
                                                "and p.person_uid = p.person_parent_uid " +
                                                "and p.dedup_match_ind is null " +
                                                "and p.record_status_cd = 'ACTIVE' " +
                                                "and pn.person_uid = pn2.person_uid " +
                                                "and pn.nm_use_cd = 'L' " +
                                                "and pn.record_status_cd = 'ACTIVE' " +
                                                "and pn.LAST_NM is not null " +
                                                "and pn.FIRST_NM is not null " +
                                                "and pn2.nm_use_cd = 'L' " +
                                                "and pn2.record_status_cd = 'ACTIVE' " +
                                                "and pn2.LAST_NM is not null " +
                                                "and pn2.FIRST_NM is not null " +
                                                "and pn.as_of_date = pn2.as_of_date " +
                                                "and pn.AS_OF_DATE = " +
                                                "(select max(pn3.as_of_date) from person_name pn3 where " +
                                                "pn.person_uid = pn3.person_uid) " ;

    private static final String SELECT_SAME_PATIENTS_FROM_PERSON =  "select p.person_uid \"personUid\" from person p, person p1 " +
                                                                    "where p.person_uid = p.person_parent_uid " +
                                                                    "and p.CURR_SEX_CD = p1.CURR_SEX_CD " +
                                                                    "and p.cd = " +
                                                                    "'" + NEDSSConstants.PAT + "' " +
	                                                                "and p.BIRTH_TIME = p1.BIRTH_TIME " +
	                                                                "and p.ETHNIC_GROUP_IND = p1.ETHNIC_GROUP_IND " +
	                                                                "and ((p.BIRTH_GENDER_CD = p1.BIRTH_GENDER_CD) " +
	                                                                "OR (p.BIRTH_GENDER_CD is null OR p1.BIRTH_GENDER_CD is  null)) " +
	                                                                "and ((p.MULTIPLE_BIRTH_IND = p1.MULTIPLE_BIRTH_IND) " +
	  	                                                            "OR (p.MULTIPLE_BIRTH_IND is null OR p1.MULTIPLE_BIRTH_IND is  null)) " +
	                                                                "and (( p.BIRTH_ORDER_NBR = p1.BIRTH_ORDER_NBR) " +
	  	                                                            "OR (p.BIRTH_ORDER_NBR is null OR p1.BIRTH_ORDER_NBR is null)) " +
                                                                	"and ((p.DECEASED_IND_CD = p1.DECEASED_IND_CD) " +
	                                                                "OR (p.DECEASED_IND_CD is null OR p1.DECEASED_IND_CD is  null)) " +
	                                                                "and ((p.DECEASED_TIME = p1.DECEASED_TIME) " +
	                                                                "OR (p.DECEASED_TIME is null OR p1.DECEASED_TIME is  null)) " +
	                                                                "and  ((UPPER(p.MOTHERS_MAIDEN_NM) = UPPER(p1.MOTHERS_MAIDEN_NM)) " +
	                                                                "OR (p.MOTHERS_MAIDEN_NM is null OR p1.MOTHERS_MAIDEN_NM is  null)) " +
	                                                                "and p.record_status_cd = 'ACTIVE' " +
	                                                                "and p1.record_status_cd = 'ACTIVE' " +
                                                                    "and p.curr_sex_cd != 'U' " +
                                                                    "and p1.curr_sex_cd != 'U' " +
                                                                    "and (p.birth_gender_cd != 'U' OR p.BIRTH_GENDER_CD is null) " +
                                                                    "and (p1.birth_gender_cd != 'U' OR p1.BIRTH_GENDER_CD is null) " +
                                                                    "and p1.person_uid = ? ";

    private static final String SELECT_SAME_PATIENTS_FROM_RACE = "select distinct pr.person_uid \"personUid\" from person_race pr, person_race pr1, person pe,  person pe1 " +
                                                                 "where pr.RACE_CATEGORY_CD = pr1.RACE_CATEGORY_CD " +
	                                                             "and pr.RACE_CD = pr1.RACE_CD " +
	                                                             "and pr.PERSON_UID = pe.PERSON_UID " +
	                                                             "and pe.ETHNIC_GROUP_IND = pe1.ETHNIC_GROUP_IND " +
                                                                 "and pe1.PERSON_UID = pr1.person_uid " +
	                                                             "and pr.record_status_cd = 'ACTIVE' " +
	                                                             "and pr1.record_status_cd = 'ACTIVE' " +
	                                                             "and pe.record_status_cd = 'ACTIVE' " +
	                                                             "and pe1.record_status_cd = 'ACTIVE' " +
	                                                             "and pr1.person_uid = ? ";

    private static final String SELECT_ENTITY_FROM_ENTITY_ORACLE = "select distinct p.person_uid \"personUid\" from  ENTITY_ID ei, person p " +
                                                                   "where  p.person_uid  = ei.ENTITY_UID (+) " +
                                                                   "and person_uid not in ( select ei.entity_uid " +
                                                                   "from  ENTITY_ID ei, ENTITY_ID ei1 " +
                                                                   "where ((ei.ROOT_EXTENSION_TXT != ei1.ROOT_EXTENSION_TXT) " +
                                                                   "OR (ei.ROOT_EXTENSION_TXT is  null or ei1.ROOT_EXTENSION_TXT is  null)) " +
                                                                   "and ei.TYPE_CD = 'SS' " +
                                                                   "and ei1.TYPE_CD = 'SS' " +
                                                                   "and ei.RECORD_STATUS_CD = 'ACTIVE' " +
                                                                   "and ei1.RECORD_STATUS_CD = 'ACTIVE' " +
                                                                   "and ei1.entity_uid = ? ) " ;
    private static final String SELECT_ENTITY_FROM_ENTITY_SQL_SERVER = "SELECT DISTINCT p.person_uid \"personUid\" from Person p " +
                                                                       "LEFT OUTER JOIN Entity_id ei  ON ei.entity_uid = p.person_uid " +
                                                                       "WHERE (p.person_uid NOT IN " +
                                                                       "(SELECT ei.entity_uid " +
                                                                       "FROM ENTITY_ID ei, ENTITY_ID ei1 " +
                                                                       "WHERE ((ei.ROOT_EXTENSION_TXT != ei1.ROOT_EXTENSION_TXT) " +
                                                                       "OR (ei.ROOT_EXTENSION_TXT IS NULL " +
                                                                       "OR ei1.ROOT_EXTENSION_TXT IS NULL)) " +
                                                                       "AND ei.TYPE_CD = 'SS' " +
                                                                       "AND ei1.TYPE_CD = 'SS' " +
                                                                       "AND ei.RECORD_STATUS_CD = 'ACTIVE' " +
                                                                       "AND ei1.RECORD_STATUS_CD = 'ACTIVE' " +
                                                                       "AND ei1.entity_uid = ? )) " ;

    private String DYNAMIC_COLLECTION_STRING;

    private String SELECT_SAME_PATIENTS_FROM_PERSON_NAME_PART1 = "select pn.person_uid \"personUid\", pn.AS_OF_DATE \"asOfDate\" from person_name pn, person_name pn1 " +
                                                                      "where UPPER(pn.LAST_NM) = UPPER(pn1.LAST_NM) " +
	                                                                  "and UPPER(pn.FIRST_NM) = UPPER(pn1.FIRST_NM) " +
	                                                                  "and pn.NM_USE_CD = 'L' " +
	                                                                  "and pn1.NM_USE_CD = 'L' " +
	                                                                  "and pn.record_status_cd ='ACTIVE' " +
 	                                                                  "and pn1.record_status_cd ='ACTIVE' " +
	                                                                  "and ((pn.LAST_NM2 is null and pn1.LAST_NM2 is null) " +
	  	                                                              "or (UPPER(pn.LAST_NM2) = UPPER(pn1.LAST_NM2))) " +
	                                                                  "and ((UPPER(pn.MIDDLE_NM) = UPPER(pn1.MIDDLE_NM)) " +
	  	                                                              "or (pn.MIDDLE_NM is null and pn1.MIDDLE_NM is null)) " +
	                                                                  "and (( UPPER(pn.MIDDLE_NM2) = UPPER(pn1.MIDDLE_NM2)) " +
	  	                                                              "or (pn.MIDDLE_NM2 is null or pn1.MIDDLE_NM2 is null)) " +
	                                                                  "and ((pn.nm_suffix = pn1.nm_suffix) " +
	  	                                                              "or (pn.nm_suffix is null or pn1.NM_SUFFIX is null)) " +
	                                                                  "and ((pn.NM_DEGREE = pn1.NM_DEGREE) " +
	  	                                                              "or (pn.NM_DEGREE is null or pn1.NM_DEGREE is null)) " +
	                                                                  "and pn1.person_uid = ? " +
	                                                                  "and pn1.AS_OF_DATE = ? " +
	                                                                  "and pn.person_uid in ( ";

    private String SELECT_SAME_PATIENTS_FROM_PERSON_NAME_PART2 = " ) order by pn.person_uid" ;

    private String SELECT_SAME_PATIENTS_FROM_PERSON_NAME;

    private String SELECT_SAME_PATIENTS_FROM_POSTAL_LOCATOR_PART1 = "select elp.entity_uid , elp.AS_OF_DATE from Entity_locator_participation elp, Postal_locator pl, Entity_locator_participation elp1, Postal_locator pl1 " +
                                                                    "where elp.LOCATOR_UID = pl.POSTAL_LOCATOR_UID " +
	                                                                "and elp1.LOCATOR_UID = pl1.POSTAL_LOCATOR_UID " +
	                                                                "and  (( pl.state_cd = pl1.state_cd ) " +
	  	                                                            "OR (pl.state_cd is null OR pl1.state_cd is  null)) " +
	                                                                "and  (( UPPER(pl.CITY_DESC_TXT) = UPPER(pl1.CITY_DESC_TXT) ) " +
	  	                                                            "OR (pl.CITY_DESC_TXT  is null OR pl1.CITY_DESC_TXT  is  null)) " +
                                                                    "and  (( pl.ZIP_CD = pl1.ZIP_CD ) " +
                                                                    "OR (pl.ZIP_CD  is null OR pl1.ZIP_CD is  null)) " +
                                                                    "and elp.USE_CD != 'BIR' " +
                                                                    "and elp.USE_CD != 'DTH' " +
                                                                    "and elp.RECORD_STATUS_CD = 'ACTIVE' " +
                                                                    "and elp1.USE_CD != 'BIR' " +
                                                                    "and elp1.USE_CD != 'DTH' " +
                                                                    "and elp1.RECORD_STATUS_CD = 'ACTIVE' " +
                                                        	        "and elp1.ENTITY_UID = ? " +
	                                                                "and elp.entity_uid in ( " ;

    private String SELECT_SAME_PATIENTS_FROM_POSTAL_LOCATOR_PART2 = " )";

    private String SELECT_SAME_PATIENTS_FROM_POSTAL_LOCATOR;

	private static final String SELECT_ENTITY_UID = "select entity_uid \"personUid\" FROM " + DataTables.ENTITY_ID_TABLE + " WHERE entity_UID = ?";

	private static final String SELECT_POSTAL_LOCATOR_UID = "select elp.entity_uid \"personUid\" FROM "
															+ DataTables.ENTITY_LOCATOR_PARTICIPATION_TABLE + " elp, "
															+ DataTables.POSTAL_LOCATOR_TABLE + " pl"
															+ " WHERE elp.LOCATOR_UID = pl.POSTAL_LOCATOR_UID and entity_UID = ?";

    /**
    * @roseuid 3E6F33AB00F5
    */
    public DeDuplicationDAO()
    {
    }

    /**
    * PSEUDO CODE
    * -------------------------------------------------
    * SELECT version_ctrl_nbr, person_uid from Person Where person_parent_uid =
    * <PersonVO.PersonDT.personUid from Superceded>
    *
    * Return Key = person_uid, Value = version_ctrl_nbr
    *
    * @param personUid
    * @return java.util.Hashtable
    * @roseuid 3E6E4A2401F4
    */
    @SuppressWarnings("unchecked")
	public HashMap<Object,Object> getPersonRevisions(Long personUid)
    {
        HashMap<Object,Object> hashMap = new HashMap<Object,Object>();
        PersonDT personDT = new PersonDT();
        logger.debug("personUid ---------"+personUid);
        try{
	        String preparedStatement = "SELECT version_ctrl_nbr VersionCtrlNbr, person_uid PersonUid from Person Where person_parent_uid = " + personUid;
	
	        Collection<Object> collection = (ArrayList<Object> ) preparedStmtMethod(personDT, null, preparedStatement, NEDSSConstants.SELECT);
	
	        Iterator<Object> iterator = collection.iterator();
	
	        while(iterator.hasNext())
	        {
	            hashMap.put(((PersonDT)iterator.next()).getVersionCtrlNbr(),((PersonDT)iterator.next()).getPersonUid());
	        }
	        logger.debug("preparedStatement---------"+preparedStatement);
        }catch(Exception ex){
        	logger.error("Exception occured for personUid ----- "+personUid+" ----- "+ex.getMessage(),ex);
        }
        return hashMap;
    }

    /**
     * Finds all the active groups and returns the count of the same...
     * @return Integer
     * @throws NEDSSDAOSysException
     */
    @SuppressWarnings("unchecked")
	public Integer getActiveGroupNumberCount() throws NEDSSDAOSysException
    {
    	PersonDT personDT = new PersonDT();
    	Collection<Object> collection = null;
    	try{
	        collection = (ArrayList<Object> ) preparedStmtMethod(personDT, null, SELECT_GROUP_NUMBER,  NEDSSConstants.SELECT);        
    	}catch(Exception ex){
    		logger.fatal("Exception = "+ex.getMessage(),ex);
    		throw new NEDSSDAOSysException(ex.toString(), ex);
    	}
    	return new Integer(collection.size());
    }
    /**
     * Returns the current pivots in the DataBase...
     * @return Collection<Object>
     * @throws NEDSSDAOSysException
     */
    @SuppressWarnings("unchecked")
	public Collection<Object> getPivotList() throws NEDSSDAOSysException
    {
        PersonNameDT personNameDT = new PersonNameDT();

        ArrayList<Object>  arrayList = null;

        try{
        	arrayList = (ArrayList<Object> ) preparedStmtMethod(personNameDT, null, SELECT_PIVOTS, NEDSSConstants.SELECT);
        }catch(Exception ex){
        	logger.fatal("Exception  = "+ex.getMessage(), ex);
        	throw new NEDSSSystemException(ex.toString());
        }
        return arrayList;
    }

    /**
     * Invokes a stored procedure in the database to create patients who are similar wrt to business logic
     * and returns the number of the groups just created.
     * @return Integer
     * @throws NEDSSSystemException
     */
    public BigDecimal createSimilarGroups() throws NEDSSSystemException
    {
        ArrayList<Object>  outArrayList= new ArrayList<Object> ();

        try
        {
            logger.debug("About to call stored procedure");

            outArrayList.add(new Integer(java.sql.Types.INTEGER));

            String sQuery = "{call PERSON_GROUP_SP(?)}";

            ArrayList<Object>  returnArrayList= callStoredProcedureMethod(sQuery, new ArrayList<Object> (), outArrayList);

            if(returnArrayList!= null && returnArrayList.size() > 0)
            {
                return new BigDecimal(returnArrayList.get(0).toString());
            }

            return null;
        }
        catch (Exception se)
        {
        	logger.fatal("Exception = "+se.getMessage(), se);
            throw new NEDSSSystemException("Error: SQLException while obtaining database connection.\n" + se.getMessage());
        }

    }

    /**
     * Given a personUID as input param gets the same patients from Person...
     * @param inPersonUID
     * @return Collection<Object>
     * @throws NEDSSDAOSysException
     */
    @SuppressWarnings("unchecked")
	public Collection<Object> getSamePatientsFromPerson(Long inPersonUID) throws NEDSSDAOSysException
    {
    	try{
	    	PersonDT personDT = new PersonDT();
	
	        ArrayList<Object>  paramterArrayList= new ArrayList<Object> ();
	
	        paramterArrayList.add(inPersonUID);
	
	        ArrayList<Object>  arrayList = (ArrayList<Object> ) preparedStmtMethod(personDT, paramterArrayList, SELECT_SAME_PATIENTS_FROM_PERSON, NEDSSConstants.SELECT);
	
	        buildArrayListOfPersonUIDsGivenPersonDTCollection(arrayList);
	
	        return arrayList;
    	}catch(Exception ex){
    		logger.fatal("Exception  = "+ex.getMessage(), ex);
    		throw new NEDSSDAOSysException(ex.toString());
    	}
    }

    /**
     * Given a personUID as input param gets the same patients from RaceAndEthnic...
     * @param inPersonUID
     * @return Collection<Object>
     * @throws NEDSSDAOSysException
     */
    @SuppressWarnings("unchecked")
	public Collection<Object> getSamePatientsFromRaceAndEthnic(Long inPersonUID) throws NEDSSDAOSysException
    {
    	try{
	        PersonRaceDT personRaceDT = new PersonRaceDT();
	
	        ArrayList<Object>  paramterArrayList= new ArrayList<Object> ();
	
	        paramterArrayList.add(inPersonUID);
	
	        ArrayList<Object>  arrayList = (ArrayList<Object> ) preparedStmtMethod(personRaceDT, paramterArrayList, SELECT_SAME_PATIENTS_FROM_RACE, NEDSSConstants.SELECT);
	
	        buildArrayListOfPersonUIDsGivenPersonRaceDTCollection(arrayList);
	
	        return arrayList;
    	}catch(Exception ex){
    		logger.fatal("Exception  = "+ex.getMessage(), ex);
    		throw new NEDSSDAOSysException(ex.toString());
    	}
    }

    /**
     * Given a personUID as input param gets the same patients from Entity...
     * @param inPersonUID
     * @return Collection<Object>
     * @throws NEDSSDAOSysException
     */
    @SuppressWarnings("unchecked")
	public Collection<Object> getEntitysFromEntity(Long inPersonUID) throws NEDSSDAOSysException
    {
        PersonDT entityDT = new PersonDT();

        ArrayList<Object>  parameterArrayList= new ArrayList<Object> ();

        parameterArrayList.add(inPersonUID);

        ArrayList<Object>  arrayList;

        try{
	        if(propertyUtil.getDatabaseServerType() != null
					&& !propertyUtil.getDatabaseServerType().equalsIgnoreCase(
							NEDSSConstants.ORACLE_ID))
	        {
	            arrayList = (ArrayList<Object> ) preparedStmtMethod(entityDT, parameterArrayList, SELECT_ENTITY_FROM_ENTITY_SQL_SERVER, NEDSSConstants.SELECT);
	
	            buildArrayListOfPersonUIDsGivenPersonDTCollection(arrayList);
	
	            return arrayList;
	        }
	
	        arrayList = (ArrayList<Object> ) preparedStmtMethod(entityDT, parameterArrayList, SELECT_ENTITY_FROM_ENTITY_ORACLE, NEDSSConstants.SELECT);
	
	        buildArrayListOfPersonUIDsGivenPersonDTCollection(arrayList);
        }catch(Exception ex){
        	logger.fatal("Exception  = "+ex.getMessage(), ex);
        	throw new NEDSSDAOSysException(ex.toString());
        }
        return arrayList;

    }

    /**
     * Given a personUID and AsOfDate as input params gets the same patients from PersonName...
     * @param inPersonUID
     * @param inTimeStamp
     * @param inBinarySearchTree
     * @return Collection<Object>
     * @throws NEDSSDAOSysException
     */
    @SuppressWarnings("unchecked")
	public Collection<Object> getSamePatientsFromPersonName(Long inPersonUID, Timestamp inTimeStamp, BinarySearchTree inBinarySearchTree) throws NEDSSDAOSysException
    {
    	try{
	        PersonNameDT personNameDT = new PersonNameDT();
	
	        ArrayList<Object>  parameterArrayList= new ArrayList<Object> ();
	
	        parameterArrayList.add(inPersonUID);
	
	        parameterArrayList.add(inTimeStamp);
	
	        Collection<Object> collection = inBinarySearchTree.toCollection();
	
	        setDynamicCollectionString(collection);
	
	        Iterator<Object> iterator = collection.iterator();
	
	        while(iterator.hasNext())
	        {
	            parameterArrayList.add(((Long)iterator.next()));
	        }
	
	        buildSelectSamePatientsFromPersonName();
	
	        ArrayList<Object>  arrayList = (ArrayList<Object> ) preparedStmtMethod(personNameDT, parameterArrayList, SELECT_SAME_PATIENTS_FROM_PERSON_NAME, NEDSSConstants.SELECT);
	
	        return buildMostRecentAsOfDateEntrysGivenAPersonNameOrELPDTCollection(arrayList);
    	}catch(Exception ex){
    		logger.fatal("Exception  = "+ex.getMessage(), ex);
    		throw new NEDSSDAOSysException(ex.toString());
    	}
    }

    /**
     * Given a personUID as input param gets the same patients from EntityLocatorParticipation...
     * @param inPersonUID
     * @param inBinarySearchTree
     * @return Collection<Object>
     * @throws NEDSSDAOSysException
     */
    public Collection<Object> getSamePatientsFromPostalLocator(Long inPersonUID, BinarySearchTree inBinarySearchTree) throws NEDSSDAOSysException
    {
    	try{
	        Collection<Object> collection = inBinarySearchTree.toCollection();
	
	        setDynamicCollectionString(collection);
	
	        buildSelectSamePatientsFromPostalLocator();
	
	        collection = buildELPDTCollection(inPersonUID, collection);
	
	        return buildMostRecentAsOfDateEntrysGivenAPersonNameOrELPDTCollection(collection);
    	}catch(Exception ex){
    		logger.fatal("Exception  = "+ex.getMessage(), ex);
    		throw new NEDSSDAOSysException(ex.toString());
    	}
    }

    /**
     * Given a ArrayList<Object>  builds a PersonDT Collection<Object> and puts it back in the same object...
     * @param inArrayList
     */
    private void buildArrayListOfPersonUIDsGivenPersonDTCollection(ArrayList<Object>  inArrayList)
    {
        PersonDT personDT=null;
        try{
	        int size = inArrayList.size();
	
	        if(size > 0)
	        {
	            for(int i = 0; i < size; i++)
	            {
	            	try{
		                personDT = (PersonDT)inArrayList.get(i);
		                inArrayList.set(i, personDT.getPersonUid());
	            	}catch(Exception e){
	            		logger.error("Exception while iterating through personUid ----- "+personDT.getPersonUid());
	            		logger.fatal("Exception = "+e.getMessage(), e);
	            		throw new NEDSSSystemException(e.toString(), e);
	            	}
	            }
	        }
        }catch(Exception ex){
        	logger.fatal("Exception = "+ex.getMessage(),ex);
        	throw new NEDSSSystemException(ex.toString(), ex);
        }
    }

    /**
     * Given a ArrayList<Object>  builds a PersonRaceDT Collection<Object> and puts it back in the same object...
     * @param inArrayList
     */
    private void buildArrayListOfPersonUIDsGivenPersonRaceDTCollection(ArrayList<Object>  inArrayList)
    {
        PersonRaceDT personRaceDT=null;
        try{
	        int size = inArrayList.size();
	
	        if(size > 0)
	        {
	
	            for(int i = 0; i < size; i++)
	            {
	            	try{
		                personRaceDT = (PersonRaceDT)inArrayList.get(i);
		                inArrayList.set(i, personRaceDT.getPersonUid());
	            	}catch(Exception e){
	            		logger.error("Exception while iterating through personUid ----- "+personRaceDT.getPersonUid());
	            		logger.fatal("Exception = "+e.getMessage(), e);
	            		throw new NEDSSSystemException(e.toString(), e);
	            	}
	            }
	        }
        }catch(Exception ex){
        	logger.fatal("Exception = "+ex.getMessage(),ex);
        	throw new NEDSSSystemException(ex.toString(), ex);
        }
    }
    /**
     * Given the Collection<Object> (from which to extract the content for
     * the Collection<Object> String) the Dynamic Collection<Object> String is built
     * @param inCollection
     */
    private void setDynamicCollectionString(Collection<Object> inCollection)
    {
    	try{
	        int size = inCollection.size();
	        StringBuffer stringBuffer = new StringBuffer();
	
	        for(int i=0; i < size; i++)
	        {
	            stringBuffer.append("?,");
	        }
	
	        String collectionString = stringBuffer.toString();
	        DYNAMIC_COLLECTION_STRING = collectionString.substring(0, (collectionString.length()-1));
	        logger.debug("DYNAMIC_COLLECTION_STRING ------ "+DYNAMIC_COLLECTION_STRING);
    	}catch(Exception ex){
    		logger.fatal("Exception = "+ex.getMessage(), ex);
    		throw new NEDSSSystemException(ex.toString());
    	}

    }
    /**
     * Returns the Collecton String
     * @return String
     */
    private String getDynamicCollectionString()
    {
        return DYNAMIC_COLLECTION_STRING;
    }
    /**
     * Builds the Query String by appending the same to the Dynamically built Collection<Object> String
     */
    private void buildSelectSamePatientsFromPersonName()
    {
        SELECT_SAME_PATIENTS_FROM_PERSON_NAME = SELECT_SAME_PATIENTS_FROM_PERSON_NAME_PART1 +
        getDynamicCollectionString() + SELECT_SAME_PATIENTS_FROM_PERSON_NAME_PART2;
    }

    /**
     * Builds the Query String by appending the same to the Dynamically built Collection<Object> String
     */
    private void buildSelectSamePatientsFromPostalLocator()
    {
        SELECT_SAME_PATIENTS_FROM_POSTAL_LOCATOR = SELECT_SAME_PATIENTS_FROM_POSTAL_LOCATOR_PART1 +
        getDynamicCollectionString() + SELECT_SAME_PATIENTS_FROM_POSTAL_LOCATOR_PART2;
    }

    /**
     * Given a Collection<Object> builds a collection object with Most Recent AsOfDates...
     * @param inCollection
     * @return Collection<Object>
     */
    private Collection<Object> buildMostRecentAsOfDateEntrysGivenAPersonNameOrELPDTCollection(Collection<Object> inCollection)
    {
        HashMap<Object,Object> hashMap = new HashMap<Object,Object>();
        Object dtObject = null;
        Long personUID = null;
        Timestamp timeStamp = null;
        try{
	        Iterator<Object> iterator = inCollection.iterator();
	        Object object;
	        while(iterator.hasNext())
	        {
	            dtObject = iterator.next();
	
	            if(dtObject instanceof PersonNameDT)
	            {
	                timeStamp = ((PersonNameDT)dtObject).getAsOfDate();
	                personUID = ((PersonNameDT)dtObject).getUid();
	            }
	            else
	            {
	                timeStamp = ((EntityLocatorParticipationDT)dtObject).getAsOfDate();
	                personUID = ((EntityLocatorParticipationDT)dtObject).getEntityUid();
	            }
	
	            //Use Synchronize because of HashMap<Object,Object>..
	            synchronized(this)
	            {
	                object = hashMap.get(personUID);
	
	                if(object == null)
	                {   //The key never was in the HashMap<Object,Object>, put it in...
	                    hashMap.put(personUID, timeStamp);
	                }
	                else
	                {
	                    //The key was there in the HashMap<Object,Object> on a earlier insert, now we need to compare the time
	                    // stamp on the same to see if it is older / newer than the one we have, if newer no action
	                    // otherwise update the same's value with our value meaning our timestamp.
	                    if(timeStamp.after((Timestamp)object))
	                    {
	                        hashMap.put(personUID, timeStamp);
	                    }
	                }
	            }
	        }
        }catch(Exception ex){
        	logger.fatal("Exception = "+ex.getMessage(),ex);
        	throw new NEDSSSystemException(ex.toString(), ex);
        }
        return hashMap.keySet();
    }

    /**
     * Given a PersonUID as input param builds a EntityLocationParticipationDT Collection<Object>...
     * @param inPersonUID
     * @return
     * @throws NEDSSDAOSysException
     */
    private Collection<Object> buildELPDTCollection(Long inPersonUID, Collection<Object> inCollection) throws NEDSSDAOSysException
    {
        Connection dbConnection = null;
        PreparedStatement preparedStmt = null;
        ResultSet resultSet = null;

        try{
        	dbConnection = getConnection();
        }catch(NEDSSSystemException nsex){
        	logger.fatal("SQLException while obtaining database connection for buildELPDTCollection ", nsex);
        	throw new NEDSSSystemException(nsex.toString());
        }

        try
        {
            preparedStmt = dbConnection.prepareStatement(SELECT_SAME_PATIENTS_FROM_POSTAL_LOCATOR);
            preparedStmt.setLong(1, inPersonUID.longValue());

            //Pass in the collection elements to the PreparedStatement setObject so that all the
            // params built suring the generation of the DYNAMIC_COLLECTION_STRING are accounted for...
            Iterator<Object> iterator = inCollection.iterator();
            for (int i = 2; iterator.hasNext(); i++)
            {
                preparedStmt.setObject(i, iterator.next().toString());
            }

            logger.debug("Sql for entity locator befor execution" + SELECT_SAME_PATIENTS_FROM_POSTAL_LOCATOR);
            resultSet = preparedStmt.executeQuery();
            Collection<Object> elpDTCollection  = new ArrayList<Object> ();

            while (resultSet.next())
            {
                EntityLocatorParticipationDT elpDT = new EntityLocatorParticipationDT();

                elpDT.setEntityUid(new Long(resultSet.getLong("entity_uid")));
                if(resultSet.getDate("AS_OF_DATE") != null)
                {
                    elpDT.setAsOfDate(new Timestamp(resultSet.getDate("AS_OF_DATE").getTime()));
                }
                elpDTCollection.add(elpDT);
            }

            return elpDTCollection;
        }
        catch (SQLException e)
        {
            logger.fatal("SQLException while loading " + "a EntityLocatorParticipation, entityUID = " + inPersonUID, e);
            throw new NEDSSDAOSysException(e.toString());
        }
        finally
        {
              closeResultSet(resultSet);
              closeStatement(preparedStmt);
              releaseConnection(dbConnection);
        }
    }

    /**
     * The method resets the Patient Registry
     */
    public void resetPatientRegistryForDedup() {
      Connection dbConnection = null;
        PreparedStatement preparedStmt = null;
        ResultSet resultSet = null;

        try{
        	dbConnection = getConnection();
        }catch(NEDSSSystemException nsex){
        	logger.fatal("SQLException while obtaining database connection for resetPatientRegistryForDedup ", nsex);
        	throw new NEDSSSystemException(nsex.toString());
        }
        
        try {
          int i = 1;
          preparedStmt = dbConnection.prepareStatement(RESET_PATIENT_REGISTRY_FOR_DEDUP);
          preparedStmt.setNull(i++, Types.NUMERIC);
          preparedStmt.setNull(i++, Types.DATE);
          preparedStmt.setNull(i++, Types.CHAR);
          preparedStmt.executeUpdate();
        }catch (SQLException e)
        {
        	logger.fatal("SQLException  = "+e.getMessage(), e);
            throw new NEDSSDAOSysException(e.toString());
        }
        finally
        {
              closeResultSet(resultSet);
              closeStatement(preparedStmt);
              releaseConnection(dbConnection);
        }

    }

    @SuppressWarnings("unchecked")
	public boolean hasEntityIds(Long personUid){

    	PersonDT entityDT = new PersonDT();

    	try{
	        ArrayList<Object>  parameterArrayList= new ArrayList<Object> ();
	
	        parameterArrayList.add(personUid);
	
	        ArrayList<Object>  arrayList = (ArrayList<Object> ) preparedStmtMethod(entityDT, parameterArrayList, SELECT_ENTITY_UID, NEDSSConstants.SELECT);
	
	        if(arrayList != null && arrayList.size()==1){
	        	return true;
	        }
    	}catch(Exception ex){
    		logger.fatal("Exception = "+ex.getMessage(),ex);
    		throw new NEDSSSystemException(ex.toString(), ex);
    	}
    	
        return false;
	}

    @SuppressWarnings("unchecked")
	public boolean hasPostalLocators(Long personUid){

    	PersonDT entityDT = new PersonDT();
    	try{
	        ArrayList<Object>  parameterArrayList= new ArrayList<Object> ();
	
	        parameterArrayList.add(personUid);
	
	        ArrayList<Object>  arrayList = (ArrayList<Object> ) preparedStmtMethod(entityDT, parameterArrayList, SELECT_POSTAL_LOCATOR_UID, NEDSSConstants.SELECT);
	
	        if(arrayList != null && arrayList.size()==1){
	        	return true;
	        }
    	}catch(Exception ex){
    		logger.fatal("Exception = "+ex.getMessage(),ex);
    		throw new NEDSSSystemException(ex.toString(), ex);
    	}
    	
        return false;
	}
}
