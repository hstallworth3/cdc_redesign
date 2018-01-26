

package gov.cdc.nedss.act.observation.dt;

import java.sql.Timestamp;
import  gov.cdc.nedss.util.*;

 /**
    * Title:            ObsValueCodedHistDT is a class
    * Description:	This is a class which sets/gets(retrieves)
    *                   all the fields to the database table
    * Copyright:	Copyright (c) 2001
    * Company: 	        Computer Sciences Corporation
    * @author           NEDSS TEAM
    * @version	        1.0
    */

public class ObsValueCodedHistDT extends AbstractVO
{
	private static final long serialVersionUID = 1L;

    private Long observationUid;
    
    private String altCd;
    
    private String altCdDescTxt;
    
    private String altCdSystemCd;
    
    private String altCdSystemDescTxt;

    private String code;
    
    private String codeDerivedInd;

    private Integer versionCtrlNbr;

    private String codeSystemCd;

    private String codeSystemDescTxt;

    private String codeVersion;

    private String displayName;

    private String originalTxt;

     private String progAreaCd = null;

     private String jurisdictionCd = null;

     private Long programJurisdictionOid = null;

     private String sharedInd = null;

    private boolean itDirty = false;

    private boolean itNew = true;

    private boolean itDelete = false;


    /**
    * Access method for the ObservationUid.
    *
    * @return Long  the current value of the ObservationUid
    */
    public Long getObservationUid()
    {
        return observationUid;
    }

    /**
    * Sets the value of the ObservationUid.
    *
    * @param Long the aObservationUid
    */
    public void setObservationUid(Long aObservationUid)
    {
        observationUid = aObservationUid;
        setItDirty(true);
    }

	
	/**
	 * gets AltCD
	 * @return String
	 */
	public String getAltCd(){
		return this.altCd;
	}

	/**
	 * sets AltCd
	 * @param aAltCd
	 */
	public void setAltCd(String aAltCd){
		this.altCd = aAltCd;
		setItDirty(true);
	}
	
	
	/**
	 * gets AltDescTxt
	 * @return String
	 */
	public String getAltCdDescTxt(){
		return this.altCdDescTxt;
	}

	/**
	 * sets AltCdDescTxt
	 * @param aAltCdDescTxt
	 */
	public void setAltCdDescTxt(String aAltCdDescTxt){
		this.altCdDescTxt = aAltCdDescTxt;
		setItDirty(true);
	}

	/**
 	*gets AltCdSystemCd 
 	* @return String
 	*/
	public String getAltCdSystemCd(){
		return this.altCdSystemCd;
	}
	
	
	/**
	 * sets AltCdSystemCd
	 * @param aAltCdSystemCd
	 */
	public void setAltCdSystemCd(String aAltCdSystemCd){
		this.altCdSystemCd = aAltCdSystemCd;
		setItDirty(true);
	}
	
	/**
	 * gets AltCdSystemDescTxt
	 * @return String
	 */
	public String getAltCdSystemDescTxt(){
		return this.altCdSystemDescTxt;
	}
	
	
	/**
	 * sets AltCdSystemDescTxt
	 * @param aAltCdSystemDescTxt
	 */
	public void setAltCdSystemDescTxt(String aAltCdSystemDescTxt){
		this.altCdSystemDescTxt = aAltCdSystemDescTxt;
		setItDirty(true);
	}
	
   /**
    * Access method for the Code.
    *
    * @return  String the current value of the Code
    */
    public String getCode()
    {
        return code;
    }

    /**
    * Sets the value of the Code.
    *
    * @param String the aCode
    */
    public void setCode(String aCode)
    {
        code = aCode;
        setItDirty(true);
    }

	/**
	 * get CodeDerivedInd
	 * @return String
	 */
	public String getCodeDerivedInd(){
		return this.codeDerivedInd;
	}
	
	/**
	 * sets CodeDerivedInd
	 * @param aCodederivedInd
	 */
	public void setCodeDerivedInd(String aCodederivedInd){
		this.codeDerivedInd = aCodederivedInd;
		setItDirty(true);
	}

   /**
    * Access method for the VersionCtrlNbr.
    *
    * @return  Integer the current value of the VersionCtrlNbr
    */
    public Integer getVersionCtrlNbr()
    {
        return versionCtrlNbr;
    }

     /**
    * Sets the value of the VersionCtrlNbr.
    *
    * @param Integer the aVersionCtrlNbr
    */
    public void setVersionCtrlNbr(Integer aVersionCtrlNbr)
    {
        versionCtrlNbr = aVersionCtrlNbr;
        setItDirty(true);
    }

   /**
    * Access method for the CodeSystemCd.
    *
    * @return   String the current value of the CodeSystemCd
    */
    public String getCodeSystemCd()
    {
        return codeSystemCd;
    }

    /**
    * Sets the value of the CodeSystemCd.
    *
    * @param String the aCodeSystemCd
    */
    public void setCodeSystemCd(String aCodeSystemCd)
    {
        codeSystemCd = aCodeSystemCd;
        setItDirty(true);
    }

   /**
    * Access method for the CodeSystemDescTxt.
    *
    * @return   String the current value of the CodeSystemDescTxt
    */
    public String getCodeSystemDescTxt()
    {
        return codeSystemDescTxt;
    }

    /**
    * Sets the value of the CodeSystemDescTxt.
    *
    * @param String the aCodeSystemDescTxt
    */
    public void setCodeSystemDescTxt(String aCodeSystemDescTxt)
    {
        codeSystemDescTxt = aCodeSystemDescTxt;
        setItDirty(true);
    }

   /**
    * Access method for the CodeVersion.
    *
    * @return  String the current value of the CodeVersion
    */
    public String getCodeVersion()
    {
        return codeVersion;
    }

     /**
    * Sets the value of the CodeVersion.
    *
    * @param String the aCodeVersion
    */
    public void setCodeVersion(String aCodeVersion)
    {
        codeVersion = aCodeVersion;
        setItDirty(true);
    }

   /**
    * Access method for the DisplayName.
    *
    * @return  String the current value of the DisplayName
    */
    public String getDisplayName()
    {
        return displayName;
    }

    /**
    * Sets the value of the DisplayName.
    *
    * @param String the aDisplayName
    */
    public void setDisplayName(String aDisplayName)
    {
        displayName = aDisplayName;
        setItDirty(true);
    }

   /**
    * Access method for the OriginalTxt.
    *
    * @return  String the current value of the OriginalTxt
    */
    public String getOriginalTxt()
    {
        return originalTxt;
    }

    /**
    * Sets the value of the OriginalTxt.
    *
    * @param string the aOriginalTxt
    */
    public void setOriginalTxt(String aOriginalTxt)
    {
        originalTxt = aOriginalTxt;
        setItDirty(true);
    }

     /**
    * Access method for the ProgAreaCd.
    *
    * @return  String the current value of the ProgAreaCd
    */

       public String getProgAreaCd()
   {
      return progAreaCd;
   }

   /**
    * Sets the value of the ProgAreaCd.
    *
    * @param String the aProgAreaCd
    */
      public void setProgAreaCd(String aProgAreaCd)
   {
      progAreaCd = aProgAreaCd;
      setItDirty(true);
   }

      /**
    * Access method for the JurisdictionCd.
    *
    * @return String  the current value of the JurisdictionCd
    */
       public String getJurisdictionCd ()
   {
      return jurisdictionCd ;
   }

   /**
    * Sets the value of the JurisdictionCd.
    *
    * @param String the aJurisdictionCd
    */
      public void setJurisdictionCd (String aJurisdictionCd )
   {
      jurisdictionCd = aJurisdictionCd;
      setItDirty(true);
   }


     /**
    * Access method for the ProgramJurisdictionOid.
    *
    * @return  String the current value of the ProgramJurisdictionOid
    */
       public Long getProgramJurisdictionOid ()
   {
      return programJurisdictionOid;
   }

   /**
    * Sets the value of the ProgramJurisdictionOid.
    *
    * @param String the aProgramJurisdictionOid
    */
      public void setProgramJurisdictionOid (Long aProgramJurisdictionOid )
   {
      programJurisdictionOid = aProgramJurisdictionOid;
      setItDirty(true);
   }

     /**
    * Access method for the SharedInd.
    *
    * @return  String the current value of the SharedInd
    */
       public String getSharedInd()
   {
      return sharedInd;
   }

   /**
    * Sets the value of the SharedInd.
    *
    * @param String the aSharedInd
    */
      public void setSharedInd(String aSharedInd)
   {
      sharedInd = aSharedInd;
      setItDirty(true);
   }


   /**
    * get the value of the boolean property.
    * @param objectname1 Object the object name
    * @param objectname2 Object the object name
    * @param voClass Class the class
    * @return isEqual the value of the boolean value
    */
    public boolean isEqual(java.lang.Object objectname1, java.lang.Object objectname2, Class<?> voClass) {
    voClass =  (( ObsValueCodedHistDT) objectname1).getClass();
    NedssUtils compareObjs = new NedssUtils();
    return (compareObjs.equals(objectname1,objectname2,voClass));
     }


     /**
    * Sets the value of the itDirty property.
    *
    * @param itDirty boolean the new value of the ItDirty property
    */
   public void setItDirty(boolean itDirty) {
      this.itDirty = itDirty;
   }


   /**
    * get the value of the boolean property.
    *
    * @return ItDirty the value of the boolean vlue
    */
   public boolean isItDirty() {
     return itDirty;
   }


   /**
    * Sets the value of the ItNew property.
    *
    * @param itNew boolean the new value of the boolean property
    */
   public void setItNew(boolean itNew) {
     this.itNew = itNew;
   }


   /**
    * get the value of the itNew property.
    *
    * @return itNew the value of the boolean property
    */
   public boolean isItNew() {
     return itNew;
   }

   /**
    * gets the value of the ItDelete property.
    *
    * @return ItDelete the new value of the boolean property
    */
   public boolean isItDelete() {
     return itDelete;
   }


   /**
    * Sets the value of the ItDelete property.
    *
    * @param itDelete boolean the value of the boolean property
    */
    public void setItDelete(boolean itDelete) {
      this.itDelete = itDelete;
   }



}