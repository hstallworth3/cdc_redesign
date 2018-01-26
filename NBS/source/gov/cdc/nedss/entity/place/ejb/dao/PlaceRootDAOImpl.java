/**
* Name:		PlaceRootDAOImpl.java
* Description:	This is the implementation of NEDSSDAOInterface for the
*               Place value object in the Place entity bean.
*               This class encapsulates all the JDBC calls made by the PlaceEJB
*               for a Place object. Actual logic of
*               inserting/reading/updating/deleting the data in relational
*               database tables to mirror the state of PlaceEJB is
*               implemented here.
* Copyright:	Copyright (c) 2001
* Company: 	Computer Sciences Corporation
* @author	Brent Chen & NEDSS Development Team
* @version	1.0
*/

package gov.cdc.nedss.entity.place.ejb.dao;


import gov.cdc.nedss.association.dao.ParticipationDAOImpl;
import gov.cdc.nedss.association.dao.RoleDAOImpl;
import gov.cdc.nedss.association.dt.ParticipationDT;
import gov.cdc.nedss.entity.entityid.dao.EntityIdDAOImpl;
import gov.cdc.nedss.entity.place.dt.PlaceDT;
import gov.cdc.nedss.entity.place.vo.PlaceVO;
import gov.cdc.nedss.exception.NEDSSConcurrentDataException;
import gov.cdc.nedss.exception.NEDSSDAOSysException;
import gov.cdc.nedss.exception.NEDSSSystemException;
import gov.cdc.nedss.locator.dao.EntityLocatorParticipationDAOImpl;
import gov.cdc.nedss.systemservice.util.NEDSSDAOFactory;
import gov.cdc.nedss.util.BMPBase;
import gov.cdc.nedss.util.JNDINames;
import gov.cdc.nedss.util.LogUtils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

import javax.ejb.EJBException;

public class PlaceRootDAOImpl extends BMPBase
{
    //For Log4J debug
    static final LogUtils logger = new LogUtils(PlaceRootDAOImpl.class.getName());

    private PlaceVO pvo = null;
    private long placeUID;
    private  PlaceDAOImpl placeDAO = null;
    private  EntityIdDAOImpl entityIdDAO = null;
    private  EntityLocatorParticipationDAOImpl entityLocatorParticipationDAO = null;
    //role and participation DAOs for place
    private  RoleDAOImpl roleDAOImpl = null;
    private  ParticipationDAOImpl placeParticipationDAOImpl = null;



    public PlaceRootDAOImpl() throws NEDSSDAOSysException, NEDSSSystemException
    {
    }
    /**
     * This method calls the insert methods to insert the Place value object into the database
	* and set the actual UID generated by UID Generator
	* @param Object -- Place Value Object
	* @return PlaceUid -- the newly created UID
	* @throws NEDSSSystemException
	*/
    public long create(Object obj) throws NEDSSSystemException
    {
    	try{
	        this.pvo = (PlaceVO)obj;
	
	        /**
	        *  Inserts PlaceDT object
	        */
	
	        if (this.pvo != null)
	        placeUID = insertPlace(this.pvo);
	        logger.debug("Place UID = " + placeUID);
	        (this.pvo.getThePlaceDT()).setPlaceUid(new Long(placeUID));
	
			/**
	        * Inserts EntityIdDT collection
	        */
	
	        if (this.pvo != null && this.pvo.getTheEntityIdDTCollection() != null)
	        insertEntityIDs(this.pvo);
	
	        /**
	        * Inserts EntityLocatorParticipationDT collection
	        */
	
	        if (this.pvo != null && this.pvo.getTheEntityLocatorParticipationDTCollection() != null)
	        insertEntityLocatorParticipations(this.pvo);
	
	        this.pvo.setItNew(false);
	        this.pvo.setItDirty(false);
	        return ((((PlaceVO)obj).getThePlaceDT().getPlaceUid()).longValue());
    	}catch(Exception ex){
    		logger.fatal("Exception  = "+ex.getMessage(), ex);
    		throw new NEDSSSystemException(ex.toString());
    	}
    }
    /**
     * This method is used to edit or update the existing record in the database
	* @param Object -- The Place Value Object
	* @throws NEDSSSystemException
	* @throws NEDSSConcurrentDataException
     */
    public void store(Object obj) throws NEDSSSystemException, NEDSSConcurrentDataException
    {
    	try{
	        this.pvo = (PlaceVO)obj;
	
	        /**
	        *  Updates PlaceDT object
	        */
	        if(this.pvo.getThePlaceDT() != null && this.pvo.getThePlaceDT().isItNew())
	        {
	            insertPlace(this.pvo);
	            this.pvo.getThePlaceDT().setItNew(false);
	            this.pvo.getThePlaceDT().setItDirty(false);
	        }
	        else if(this.pvo.getThePlaceDT() != null && this.pvo.getThePlaceDT().isItDirty())
	        {
	            updatePlace(this.pvo);
	            this.pvo.getThePlaceDT().setItDirty(false);
	            this.pvo.getThePlaceDT().setItNew(false);
	        }
	
	        /**
	         * Updates entity ids collection
	         */
	        if(this.pvo.getTheEntityIdDTCollection() != null)
	        {
	            updateEntityIDs(this.pvo);
	        }
	
	        /**
	        * Updates entity locator participations collection
	        */
	        if (this.pvo.getTheEntityLocatorParticipationDTCollection() != null)
	        {
	            updateEntityLocatorParticipations(this.pvo);
	
	        }
    	}catch(NEDSSConcurrentDataException ex){
    		logger.fatal("NEDSSConcurrentDataException  = "+ex.getMessage(), ex);
    		throw new NEDSSConcurrentDataException(ex.toString());
    	}catch(Exception ex){
    		logger.fatal("Exception  = "+ex.getMessage(), ex);
    		throw new NEDSSSystemException(ex.toString());
    	}
    }
    /**
     * Remove the Place and Objects related to it corrosponding to the UID
	* @param placeUID -- Uid of the object to be removed
	* @throws NEDSSSystemException
     */
    public void remove(long placeUID) throws NEDSSSystemException
    {
    	try{
	        /**
	        * Removes EntityLocatorParticipationDT collection
	        */
	
	        removeEntityLocatorParticipations(placeUID);
	
	        /**
	        * Removes PlaceEthnicGroupDT collection
	        */
	
	        removeEntityIDs(placeUID);
	
	        /**
	        * Removes PlaceRaceDT Collection
	        */
	
	        removePlace(placeUID);
    	}catch(Exception ex){
    		logger.fatal("Exception  = "+ex.getMessage(), ex);
    		throw new NEDSSSystemException(ex.toString());
    	}
    }
    /**
     * Load the place value object corrosponding to the placeUID
	* @param placeUID -- Uid for the object to be loaded
	* @return PlaceVO  -- The place value object
	* @throws NEDSSSystemException
     */
    public Object loadObject(long placeUID) throws NEDSSSystemException
    {
    	try{
	        this.pvo = new PlaceVO();
	
	        /**
	        *  Selects PlaceDT object
	        */
	
	        PlaceDT pDT = selectPlace(placeUID);
	        this.pvo.setThePlaceDT(pDT);
	
	        /**
	        * Selects EntityIdDT collection
	        */
	
	        Collection<Object>  idColl = selectEntityIDs(placeUID);
	        this.pvo.setTheEntityIdDTCollection(idColl);
	
	        /**
	        * Selects EntityLocatorParticipationDT collection
	        */
	
	        Collection<Object>  elpColl = selectEntityLocatorParticipations(placeUID);
	        this.pvo.setTheEntityLocatorParticipationDTCollection(elpColl);
	
	        //Selects RoleDTcollection
	
	          Collection<Object>  roleColl = selectRoleDTCollection(placeUID);
	          this.pvo.setTheRoleDTCollection(roleColl);
	
	        //SelectsParticipationDTCollection
	          Collection<Object>  parColl = selectParticipationDTCollection(placeUID);
	          this.pvo.setTheParticipationDTCollection(parColl);
	
	        this.pvo.setItNew(false);
	        this.pvo.setItDirty(false);
	        return this.pvo;
    	}catch(Exception ex){
    		logger.fatal("Exception  = "+ex.getMessage(), ex);
    		throw new NEDSSSystemException(ex.toString());
    	}
    }
    /**
     * finds if the record exists in the database
	* @param PlaceUID
	* @return placePK
     */
    public Long findByPrimaryKey(long placeUID) throws NEDSSSystemException
    {
    	try{
			/**
			 * Finds place object
			 */
			Long placePK = findPlace(placeUID);

			logger.debug("Done find by primarykey!");
			return placePK;
    	}catch(Exception ex){
    		logger.fatal("Exception  = "+ex.getMessage(), ex);
    		throw new NEDSSSystemException(ex.toString());
    	}
    }
    /**
     * insert the new record for Place
	* @param PlaceVO -- the place value object
	* @return PlaceUid -- The uid for newly created Place Object
	* @throws EJBException
	* @throws NEDSSSystemException
     */
    private long insertPlace(PlaceVO pvo) throws EJBException, NEDSSSystemException
    {
        try
        {
            if(placeDAO == null)
            {
                placeDAO = (PlaceDAOImpl)NEDSSDAOFactory.getDAO( JNDINames.PLACE_DAO_CLASS);
            }
            logger.debug("Place DT = " + pvo.getThePlaceDT());
            placeUID = placeDAO.create(pvo.getThePlaceDT());
            logger.debug("Place root uid = " + placeUID);
            pvo.getThePlaceDT().setPlaceUid(new Long(placeUID));
        }
        catch(NEDSSDAOSysException ndsex)
        {
            logger.fatal("Fails insertPlace()"+ndsex.getMessage(), ndsex);
            throw new EJBException(ndsex.toString());
        }
        catch(NEDSSSystemException ndapex)
        {
            logger.fatal("Fails insertPlace()"+ndapex.getMessage(), ndapex);
            throw new NEDSSSystemException(ndapex.toString());
        }
        return placeUID;
    }
    /**
     * insert the new record for EntityID
	* @param PlaceVO -- the place value object
	* @throws EJBException
	* @throws NEDSSSystemException
     */
    private void insertEntityIDs(PlaceVO pvo) throws NEDSSSystemException
    {
        try
        {
            if(entityIdDAO == null)
            {
                entityIdDAO = (EntityIdDAOImpl)NEDSSDAOFactory.getDAO(JNDINames.ENTITY_ID_DAO_CLASS);
            }
            placeUID = entityIdDAO.create((pvo.getThePlaceDT().getPlaceUid()).longValue(), pvo.getTheEntityIdDTCollection());
        }
        catch(NEDSSDAOSysException ndsex)
        {
            logger.fatal("Fails insertEntityIDs()"+ndsex.getMessage(), ndsex);
            throw new NEDSSSystemException(ndsex.toString());
        }
        catch(NEDSSSystemException ndapex)
        {
            logger.fatal("Fails insertEntityIDs()"+ndapex.getMessage(), ndapex);
            throw new NEDSSSystemException(ndapex.toString());
        }
    }
    /**
     * insert the new record for EntityLocatorParticipations
	* @param PlaceVO -- the place value object
	* @throws EJBException
	* @throws NEDSSSystemException
     */
    private void insertEntityLocatorParticipations(PlaceVO pvo) throws NEDSSSystemException
    {
        try
        {
            if(entityLocatorParticipationDAO == null)
            {
                entityLocatorParticipationDAO = (EntityLocatorParticipationDAOImpl)NEDSSDAOFactory.getDAO(JNDINames.ENTITY_LOCATOR_PARTICIPATION_DAO_CLASS);
            }
            placeUID = entityLocatorParticipationDAO.create((pvo.getThePlaceDT().getPlaceUid()).longValue(), pvo.getTheEntityLocatorParticipationDTCollection());
        }
        catch(NEDSSDAOSysException ndsex)
        {
            logger.fatal("Fails insertEntityLocatorParticipations()"+ndsex.getMessage(), ndsex);
            throw new NEDSSSystemException(ndsex.toString());
        }
        catch(NEDSSSystemException ndapex)
        {
            logger.fatal("Fails insertEntityLocatorParticipations()"+ndapex.getMessage(), ndapex);
            throw new NEDSSSystemException(ndapex.toString());
        }
    }
    /**
     * Load the PlaceVO object corrosponding to the passed Uid
	* @param placeUID
	* @return PlaceDT
	* @throws NEDSSSystemException
	*/
    private PlaceDT selectPlace(long placeUID) throws NEDSSSystemException
    {
        try
        {
            if(placeDAO == null)
            {
                placeDAO = (PlaceDAOImpl)NEDSSDAOFactory.getDAO(JNDINames.PLACE_DAO_CLASS);
            }
            return ((PlaceDT)placeDAO.loadObject(placeUID));
        }
        catch(NEDSSDAOSysException ndsex)
        {
            logger.fatal("Fails selectPlace()"+ndsex.getMessage(), ndsex);
            throw new NEDSSSystemException(ndsex.toString());
        }
        catch(NEDSSSystemException ndapex)
        {
            logger.fatal("Fails selectPlace()"+ndapex.getMessage(), ndapex);
            throw new NEDSSSystemException(ndapex.toString());
        }
    }
    /**
     * This method will gives you the collection of EntityID DTs
	* related to the place corrosponding to the passed UID
	* @param PlaceUid
	* @return Collection<Object>  -- Collection<Object>  of the EntityID DTs
	* @throws NEDSSSystemException
     */
    private Collection<Object>  selectEntityIDs(long aUID) throws NEDSSSystemException
    {
        try
        {
            if(entityIdDAO == null)
            {
                entityIdDAO = (EntityIdDAOImpl)NEDSSDAOFactory.getDAO(JNDINames.ENTITY_ID_DAO_CLASS);
            }
            return (entityIdDAO.load(aUID));
        }
        catch(NEDSSDAOSysException ndsex)
        {
            logger.fatal("Fails selectEntityIDs()"+ndsex.getMessage(), ndsex);
            throw new NEDSSSystemException(ndsex.toString());
        }
        catch(NEDSSSystemException ndapex)
        {
            logger.fatal("Fails selectEntityIDs()"+ndapex.getMessage(), ndapex);
            throw new NEDSSSystemException(ndapex.toString());
        }
    }
     /**
     * This method will gives you the collection of EntityLocatorParticipation DTs
	* related to the place corrosponding to the passed UID
	* @param PlaceUid
	* @return Collection<Object>  -- Collection<Object>  of the EntityLocatorParticipation DTs
	* @throws NEDSSSystemException
     */
    private Collection<Object>  selectEntityLocatorParticipations(long aUID) throws NEDSSSystemException
    {
        try
        {
            if(entityLocatorParticipationDAO == null)
            {
                entityLocatorParticipationDAO = (EntityLocatorParticipationDAOImpl)NEDSSDAOFactory.getDAO(JNDINames.ENTITY_LOCATOR_PARTICIPATION_DAO_CLASS);
            }
            return (entityLocatorParticipationDAO.load(aUID));
        }
        catch(NEDSSDAOSysException ndsex)
        {
            logger.fatal("Fails selectEntityLocatorParticipations()"+ndsex.getMessage(), ndsex);
            throw new NEDSSSystemException(ndsex.toString());
        }
        catch(NEDSSSystemException ndapex)
        {
            logger.fatal("Fails selectEntityLocatorParticipations()"+ndapex.getMessage(), ndapex);
            throw new NEDSSSystemException(ndapex.toString());
        }
    }
    /**
     * This method will remove the place object from the database corrosponding
	* to the passed UID
	* @param PlaceUID
	* @throws NEDSSSystemException
     */
    private void removePlace(long aUID) throws NEDSSSystemException
    {
        try
        {
            if(placeDAO == null)
            {
                placeDAO = (PlaceDAOImpl)NEDSSDAOFactory.getDAO(JNDINames.PLACE_DAO_CLASS);
            }
            placeDAO.remove(aUID);
        }
        catch(NEDSSDAOSysException ndsex)
        {
            cntx.setRollbackOnly();
            logger.fatal("aUID:"+aUID+" NEDSSDAOSysException  = "+ndsex.getMessage(), ndsex);
            throw new NEDSSSystemException(ndsex.getMessage());
        }
        catch(NEDSSSystemException ndapex)
        {
        	logger.fatal("aUID:"+aUID+" NEDSSSystemException  = "+ndapex.getMessage(), ndapex);
            cntx.setRollbackOnly();
            throw new NEDSSSystemException(ndapex.getMessage());
        }
    }
    /**
     * This method will remove the place object from the database corrosponding
	* to the passed UID
	* @param PlaceUID
	* @throws NEDSSSystemException
     */
    private void removeEntityIDs(long aUID) throws NEDSSSystemException
    {
        try
        {
            if(entityIdDAO == null)
            {
                entityIdDAO = (EntityIdDAOImpl)NEDSSDAOFactory.getDAO(JNDINames.ENTITY_ID_DAO_CLASS);
            }
            entityIdDAO.remove(aUID);
        }
        catch(NEDSSDAOSysException ndsex)
        {
            cntx.setRollbackOnly();
            logger.fatal("aUID:"+aUID+" NEDSSDAOSysException  = "+ndsex.getMessage(), ndsex);
            throw new NEDSSSystemException(ndsex.getMessage());
        }
        catch(NEDSSSystemException ndapex)
        {
        	logger.fatal("aUID:"+aUID+" NEDSSDAOSysException  = "+ndapex.getMessage(), ndapex);
            cntx.setRollbackOnly();
            throw new NEDSSSystemException(ndapex.getMessage());
        }
    }
   /**
     * This method will remove the EntityLocatorParticipations from the database corrosponding
	* to the passed UID
	* @param PlaceUID
	* @throws NEDSSSystemException
     */
    private void removeEntityLocatorParticipations(long aUID) throws NEDSSSystemException
    {
        try
        {
            if(entityLocatorParticipationDAO == null)
            {
                entityLocatorParticipationDAO = (EntityLocatorParticipationDAOImpl)NEDSSDAOFactory.getDAO(JNDINames.ENTITY_LOCATOR_PARTICIPATION_DAO_CLASS);
            }
            entityLocatorParticipationDAO.remove(aUID);
        }
        catch(NEDSSDAOSysException ndsex)
        {
            cntx.setRollbackOnly();
            logger.fatal("aUID:"+aUID+" NEDSSDAOSysException  = "+ndsex.getMessage(), ndsex);
            throw new NEDSSSystemException(ndsex.getMessage());
        }
        catch(NEDSSSystemException ndapex)
        {
        	logger.fatal("aUID:"+aUID+" NEDSSDAOSysException  = "+ndapex.getMessage(), ndapex);
            cntx.setRollbackOnly();
            throw new NEDSSSystemException(ndapex.getMessage());
        }
    }
    /**
     * This method will Update the Place Object in the database
	* @param PlaceVO
	* @throws NEDSSSystemException
     */
    private void updatePlace(PlaceVO pvo) throws NEDSSSystemException, NEDSSConcurrentDataException
    {
        try
        {
            if(placeDAO == null)
            {
                placeDAO = (PlaceDAOImpl)NEDSSDAOFactory.getDAO(JNDINames.PLACE_DAO_CLASS);
            }
            placeDAO.store(pvo.getThePlaceDT());
        }
          catch(NEDSSConcurrentDataException ncdaex)
        {
            logger.fatal("Fails updatePlace() due to concurrent access! "+ncdaex.getMessage(), ncdaex);
            throw new NEDSSConcurrentDataException(ncdaex.toString());
        }
        catch(NEDSSDAOSysException ndsex)
        {
            logger.fatal("Fails updatePlace()"+ndsex.getMessage(), ndsex);
            throw new NEDSSSystemException(ndsex.toString());
        }
        catch(NEDSSSystemException ndapex)
        {
            logger.fatal("Fails updatePlace()"+ndapex.getMessage(), ndapex);
            throw new NEDSSSystemException(ndapex.toString());
        }


    }
    /**
     * This method will Update the EntityIDs in the database
	* @param PlaceVO
	* @throws NEDSSSystemException
     */
    private void updateEntityIDs(PlaceVO pvo) throws NEDSSSystemException
    {
        try
        {
            if(entityIdDAO == null)
            {
                entityIdDAO = (EntityIdDAOImpl)NEDSSDAOFactory.getDAO(JNDINames.ENTITY_ID_DAO_CLASS);
            }
            entityIdDAO.store(pvo.getTheEntityIdDTCollection());
        }
        catch(NEDSSDAOSysException ndsex)
        {
            logger.fatal("Fails updateEntityIDs()"+ndsex.getMessage(), ndsex);
            throw new NEDSSSystemException(ndsex.toString());
        }
        catch(NEDSSSystemException ndapex)
        {
            logger.fatal("Fails updateEntityIDs()"+ndapex.getMessage(), ndapex);
            throw new NEDSSSystemException(ndapex.toString());
        }
    }
    /**
     * This method will Update the related EntityLocatorParticipations in the database
	* @param PlaceVO
	* @throws NEDSSSystemException
     */
    private void updateEntityLocatorParticipations(PlaceVO pvo)
              throws NEDSSSystemException
    {
        try
        {
            if(entityLocatorParticipationDAO == null)
            {
                entityLocatorParticipationDAO = (EntityLocatorParticipationDAOImpl)NEDSSDAOFactory.getDAO(JNDINames.ENTITY_LOCATOR_PARTICIPATION_DAO_CLASS);
            }
            entityLocatorParticipationDAO.store(pvo.getTheEntityLocatorParticipationDTCollection());
        }
        catch(NEDSSDAOSysException ndsex)
        {
            logger.fatal("Fails updateEntityLocatorParticipations()"+ndsex.getMessage(), ndsex);
            throw new NEDSSSystemException(ndsex.toString());
        }
        catch(NEDSSSystemException ndapex)
        {
            logger.fatal("Fails updateEntityLocatorParticipations()"+ndapex.getMessage(), ndapex);
            throw new NEDSSSystemException(ndapex.toString());
        }
    }
  /**
   * Find if the perticular record exxists in the database
   * @param PlaceUid -- Uid to be find
   * @return placeUid -- Return the Found primary Key
   * @throws NEDSSSystemException
   */
    private Long findPlace(long placeUID) throws NEDSSSystemException
    {
        Long findPK = null;
        try
        {
            if(placeDAO == null)
            {
                placeDAO = (PlaceDAOImpl)NEDSSDAOFactory.getDAO(JNDINames.PLACE_DAO_CLASS);
            }
            findPK = placeDAO.findByPrimaryKey(placeUID);
        }
        catch(NEDSSDAOSysException ndsex)
        {
            logger.fatal("placeUID: "+placeUID+" Fails findPlace()"+ndsex.getMessage(), ndsex);
            throw new NEDSSSystemException(ndsex.toString());
        }
        catch(NEDSSSystemException ndapex)
        {
            logger.fatal("placeUID: "+placeUID+" Fails findPlace()"+ndapex.getMessage(), ndapex);
            throw new NEDSSSystemException(ndapex.toString());
        }
        return findPK;
    }
    /**
   * Find if the perticular records exxists in the database
   * @param PlaceUid -- Uid to be find
   * @return placeUid -- Return the Found primary Key
   * @throws NEDSSSystemException
   */
    private void findEntityIDs(long placeUID) throws NEDSSSystemException, NEDSSSystemException
    {
        try
        {
            if(entityIdDAO == null)
            {
                entityIdDAO = (EntityIdDAOImpl)NEDSSDAOFactory.getDAO(JNDINames.ENTITY_ID_DAO_CLASS);
            }
            Long findPK = entityIdDAO.findByPrimaryKey(placeUID);
        }
        catch(NEDSSDAOSysException ndsex)
        {
            logger.fatal("placeUID: "+placeUID+" Fails findEntityIDs()"+ndsex.getMessage(), ndsex);
            throw new NEDSSSystemException(ndsex.toString());
        }
        catch(NEDSSSystemException ndapex)
        {
            logger.fatal("placeUID: "+placeUID+" Fails findEntityIDs()"+ndapex.getMessage(), ndapex);
            throw new NEDSSSystemException(ndapex.toString());
        }
    }
    /**
   * Find if the perticular record exxists in the database
   * @param PlaceUid -- Uid to be find
   * @return placeUid -- Return the Found primary Key
   * @throws NEDSSSystemException
   */
    private void findEntityLocatorParticipations(long placeUID) throws NEDSSSystemException
    {
        try
        {
            if(entityLocatorParticipationDAO == null)
            {
                entityLocatorParticipationDAO = (EntityLocatorParticipationDAOImpl)NEDSSDAOFactory.getDAO(JNDINames.ENTITY_LOCATOR_PARTICIPATION_DAO_CLASS);
            }
            Long findPK = entityLocatorParticipationDAO.findByPrimaryKey(placeUID);
        }
        catch(NEDSSDAOSysException ndsex)
        {
            logger.fatal("placeUID: "+placeUID+" Fails findEntityLocatorParticipations()"+ndsex.getMessage(), ndsex);
            throw new NEDSSSystemException(ndsex.toString());
        }
        catch(NEDSSSystemException ndapex)
        {
            logger.fatal("placeUID: "+placeUID+" Fails findEntityLocatorParticipations()"+ndapex.getMessage(), ndapex);
            throw new NEDSSSystemException(ndapex.toString());
        }
    }

    /**
     * get collection of RoleDT from RoleDAOImpl entered
	* @param UID -- Uid for which the roles are requird
	* @return Collection<Object>  -- the collection of the RoleDT
	* @throws NEDSSSystemException
     */

    private Collection<Object>  selectRoleDTCollection(long aUID)
      throws NEDSSSystemException
    {
        try  {
            if(roleDAOImpl == null) {
                roleDAOImpl = (RoleDAOImpl)NEDSSDAOFactory.getDAO(JNDINames.ROLE_DAO_CLASS);
            }
            logger.debug("aUID in selectRoleDTCollection  = " + aUID);
            return (roleDAOImpl.load(aUID));


        } catch(NEDSSDAOSysException ndsex) {
            logger.fatal("aUID: "+aUID+" Fails selectRoleDTCollection()"+ndsex.getMessage(), ndsex);
            throw new NEDSSSystemException(ndsex.toString());

        } catch(NEDSSSystemException ndapex) {
            logger.fatal("aUID: "+aUID+" Fails selectRoleDTCollection()"+ndapex.getMessage(), ndapex);
            throw new NEDSSSystemException(ndapex.toString());
        }
    }

   /**
     * get collection of Participation  from ParticipationDAOImpl
	* @param UID -- Uid for which the Participations are requird
	* @return Collection<Object>  -- the collection of the ParticipationDT
	* @throws NEDSSSystemException
     */

 private Collection<Object>  selectParticipationDTCollection(long aUID)
      throws NEDSSSystemException
    {
        try  {
            if(placeParticipationDAOImpl == null) {
                placeParticipationDAOImpl = (ParticipationDAOImpl)NEDSSDAOFactory.getDAO(JNDINames.PLACE_PARTICIPATION_DAO_CLASS);
            }
            logger.debug("aUID in selectParticipationDTCollection  = " + aUID);
            return (placeParticipationDAOImpl.load(aUID));


        } catch(NEDSSDAOSysException ndsex) {
            logger.fatal("aUID: "+aUID+" Fails selectParticipationDTCollection()"+ndsex.getMessage(), ndsex);
            throw new NEDSSSystemException(ndsex.toString());

        } catch(NEDSSSystemException ndapex) {
            logger.fatal("aUID: "+aUID+" Fails selectParticipationDTCollection()"+ndapex.getMessage(), ndapex);
            throw new NEDSSSystemException(ndapex.toString());
        }
    }

    /**
     * set a collection of participationDT and return the participationDTs with sequences
	* @param partDTs -- the participation Collection<Object>  to set
	* @throws NEDSSSystemException
     */

 public void setParticipation(Collection<Object> partDTs)
      throws NEDSSSystemException
    {
        try
        {
            if(placeParticipationDAOImpl == null)
            {
                placeParticipationDAOImpl = (ParticipationDAOImpl)NEDSSDAOFactory.getDAO(JNDINames.PLACE_PARTICIPATION_DAO_CLASS);
            }
           Iterator<Object>  iter = partDTs.iterator();
            while(iter.hasNext())
            {
                ParticipationDT partDT = (ParticipationDT)iter.next();
                logger.debug("Calling store on partDAO");
                placeParticipationDAOImpl.store(partDT);

            }


        } catch(NEDSSDAOSysException ndsex) {
            logger.fatal("Fails setParticipation()"+ndsex.getMessage(), ndsex);
            throw new NEDSSSystemException(ndsex.toString());

        } catch(NEDSSSystemException ndapex) {
            logger.fatal("Fails setParticipation()"+ndapex.getMessage(), ndapex);
            throw new NEDSSSystemException(ndapex.toString());
        }
    }
/**
     *set collection of RoleDTs and return them with sequence values assigned
	* @param roleDTs -- the roleDT Collection<Object>  to set
	* @throws NEDSSSystemException
     */

 public Collection<Object>  setRoleDTCollection(Collection<Object> roleDTs)
      throws NEDSSSystemException
    {
        ArrayList<Object> returnRoles = new ArrayList<Object> ();
        try
        {
            if(roleDAOImpl == null)
            {
                roleDAOImpl = (RoleDAOImpl)NEDSSDAOFactory.getDAO(JNDINames.ROLE_DAO_CLASS);
            }

            returnRoles = (ArrayList<Object> )roleDAOImpl.setRoleDTCollection(roleDTs);
            logger.debug("Size of the collection returned is: " + returnRoles.size());
            return returnRoles;


        } catch(NEDSSDAOSysException ndsex) {
            logger.fatal("Fails setRoleDTCollection()"+ndsex.getMessage(), ndsex);
            throw new NEDSSSystemException(ndsex.toString());

        } catch(NEDSSSystemException ndapex) {
            logger.fatal("Fails setRoleDTCollection()"+ndapex.getMessage(), ndapex);
            throw new NEDSSSystemException(ndapex.toString());
        }
    }

}//end of PlaceRootDAOImpl class