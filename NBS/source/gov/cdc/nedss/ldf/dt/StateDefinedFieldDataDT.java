//Source file: C:\\All NEDSS Related\\scratch\\gov\\gov\\cdc\\nedss\\ldf\\dt\\StateDefinedFieldDataDT.java

package gov.cdc.nedss.ldf.dt;
import gov.cdc.nedss.util.AbstractVO;
import gov.cdc.nedss.systemservice.util.RootDTInterface;
import java.sql.Timestamp;
import gov.cdc.nedss.util.*;


public class StateDefinedFieldDataDT extends AbstractVO implements RootDTInterface
{
   private Long ldfUid;
   private String businessObjNm;
   private Timestamp addTime;
   private Long businessObjUid;
   private Timestamp lastChgTime;
   private String ldfValue;
   private Integer versionCtrlNbr;
   private String conditionCd;
   private boolean itDirty = false;
   private String codeSetNm;
   private String fieldSize;
   private String dataType;

   public String getCodeSetNm() {
	return codeSetNm;
}

public void setCodeSetNm(String codeSetNm) {
	this.codeSetNm = codeSetNm;
}

/**
    * @roseuid 3F4E4AFF016E
    */
   public StateDefinedFieldDataDT()
   {

   }

   /**
    * Access method for the ldfUid property.
    *
    * @return   the current value of the ldfUid property
    */
   public Long getLdfUid()
   {
      return ldfUid;
   }

   /**
    * Sets the value of the ldfUid property.
    *
    * @param aLdfUid the new value of the ldfUid property
    */
   public void setLdfUid(Long aLdfUid)
   {
      ldfUid = aLdfUid;
   }

   /**
    * Access method for the businessObjNm property.
    *
    * @return   the current value of the businessObjNm property
    */
   public String getBusinessObjNm()
   {
      return businessObjNm;
   }

   /**
    * Sets the value of the businessObjNm property.
    *
    * @param aBusinessObjNm the new value of the businessObjNm property
    */
   public void setBusinessObjNm(String aBusinessObjNm)
   {
      businessObjNm = aBusinessObjNm;
   }

   /**
    * Access method for the addTime property.
    *
    * @return   the current value of the addTime property
    */
   public Timestamp getAddTime()
   {
      return addTime;
   }

   /**
    * Sets the value of the addTime property.
    *
    * @param aAddTime the new value of the addTime property
    */
   public void setAddTime(Timestamp aAddTime)
   {
      addTime = aAddTime;
   }

   /**
    * Access method for the businessObjUid property.
    *
    * @return   the current value of the businessObjUid property
    */
   public Long getBusinessObjUid()
   {
      return businessObjUid;
   }

   /**
    * Sets the value of the businessObjUid property.
    *
    * @param aBusinessObjUid the new value of the businessObjUid property
    */
   public void setBusinessObjUid(Long aBusinessObjUid)
   {
      businessObjUid = aBusinessObjUid;
   }

   /**
    * Access method for the lastChgTime property.
    *
    * @return   the current value of the lastChgTime property
    */
   public Timestamp getLastChgTime()
   {
      return lastChgTime;
   }

   /**
    * Sets the value of the lastChgTime property.
    *
    * @param aLastChgTime the new value of the lastChgTime property
    */
   public void setLastChgTime(Timestamp aLastChgTime)
   {
      lastChgTime = aLastChgTime;
   }

   /**
    * Access method for the ldfValue property.
    *
    * @return   the current value of the ldfValue property
    */
   public String getLdfValue()
   {
      return ldfValue;
   }

   /**
    * Sets the value of the ldfValue property.
    *
    * @param aLdfValue the new value of the ldfValue property
    */
   public void setLdfValue(String aLdfValue)
   {
       ldfValue =aLdfValue;
   }

   /**
    * Access method for the versionCtrlNbr property.
    *
    * @return   the current value of the versionCtrlNbr property
    */
   public Integer getVersionCtrlNbr()
   {
      return versionCtrlNbr;
   }

   /**
    * Sets the value of the versionCtrlNbr property.
    *
    * @param aVersionCtrlNbr the new value of the versionCtrlNbr property
    */
   public void setVersionCtrlNbr(Integer aVersionCtrlNbr)
   {
      versionCtrlNbr = aVersionCtrlNbr;
   }

   /**
   * @param itDirty
   * @roseuid 3E3040CB01C4
   */
  public void setItDirty(boolean itDirty)
  {
       this.itDirty = itDirty;
  }

  /**
   * @return boolean
   * @roseuid 3E3040CB01E2
   */
  public boolean isItDirty()
  {
   return itDirty;
  }

  /**
   * @param itNew
   * @roseuid 3E3040CB01F6
   */
  public void setItNew(boolean itNew)
  {
       this.itNew = itNew;
  }

  /**
   * @return boolean
   * @roseuid 3E3040CB0214
   */
  public boolean isItNew()
  {
   return itNew;
  }

  /**
   * @param itDelete
   * @roseuid 3E3040CB021E
   */
  public void setItDelete(boolean itDelete)
  {
     this.itDelete = itDelete;
  }

  /**
   * @return boolean
   * @roseuid 3E3040CB023C
   */
  public boolean isItDelete()
  {
   return itDelete;
  }
  /**
 * @param objectname1
 * @param objectname2
 * @param voClass
 * @return boolean
 * @roseuid 3E3040CB0188
 */
public boolean isEqual(Object objectname1, Object objectname2, Class<?> voClass)
{
 return true;
}

  /**
   * @param aSharedInd
   * @roseuid 3E3046570338
   */
  public void setSharedInd(String aSharedInd)
  {

  }
  /**
 * @return java.lang.String
 * @roseuid 3E3046570324
 */
public String getSharedInd()
{
 return null;
}

  /**
   * @return java.lang.Long
   * @roseuid 3E30465702E8
   */
  public Long getProgramJurisdictionOid()
  {
   return null;
  }

  /**
   * @param aProgramJurisdictionOid
   * @roseuid 3E30465702FC
   */
  public void setProgramJurisdictionOid(Long aProgramJurisdictionOid)
  {

  }
  /**
    * @return java.lang.Long
    * @roseuid 3E30465702CA
    */
   public Long getUid()
   {
    return null;
   }


   /**
    * @return java.lang.String
    * @roseuid 3E30465702B6
    */
   public String getSuperclass()
   {
    return null;
   }

   /**
    * @return java.sql.Timestamp
    * @roseuid 3E304657027A
    */
   public Timestamp getStatusTime()
   {
    return null;
   }

   /**
    * @param aStatusTime
    * @roseuid 3E304657028E
    */
   public void setStatusTime(Timestamp aStatusTime)
   {

   }

   /**
 * @return java.lang.String
 * @roseuid 3E304657023E
 */
public String getStatusCd()
{
 return null;
}

/**
 * @param aStatusCd
 * @roseuid 3E3046570252
 */
public void setStatusCd(String aStatusCd)
{

}
/**
 * @return java.sql.Timestamp
 * @roseuid 3E3046570202
 */
public Timestamp getRecordStatusTime()
{
 return null;
}

/**
 * @param aRecordStatusTime
 * @roseuid 3E3046570216
 */
public void setRecordStatusTime(Timestamp aRecordStatusTime)
{

}
/**
 * @return java.lang.String
 * @roseuid 3E30465701C5
 */
public String getRecordStatusCd()
{
 return null;
}

/**
 * @param aRecordStatusCd
 * @roseuid 3E30465701DA
 */
public void setRecordStatusCd(String aRecordStatusCd)
{

}
/**
  * @return java.lang.String
  * @roseuid 3E3046570193
  */
 public String getLastChgReasonCd()
 {
  return null;
 }

 /**
  * @param aLastChgReasonCd
  * @roseuid 3E30465701A7
  */
 public void setLastChgReasonCd(String aLastChgReasonCd)
 {

 }

 /**
   * @return java.lang.Long
   * @roseuid 3E3046570085
   */
  public Long getLastChgUserId()
  {
   return null;
  }

  /**
   * @param aLastChgUserId
   * @roseuid 3E3046570099
   */
  public void setLastChgUserId(Long aLastChgUserId)
  {

  }
  /**
   * @return java.lang.Long
   * @roseuid 3E3046570157
   */
  public Long getAddUserId()
  {
   return null;
  }

  /**
   * @param aAddUserId
   * @roseuid 3E304657016B
   */
  public void setAddUserId(Long aAddUserId)
  {

  }
  /**
   * @return java.lang.String
   * @roseuid 3E3046570125
   */
  public String getLocalId()
  {
   return null;
  }

  /**
   * @param aLocalId
   * @roseuid 3E3046570139
   */
  public void setLocalId(String aLocalId)
  {

  }
  /**
   * @return java.lang.String
   * @roseuid 3E30465700E9
   */
  public String getProgAreaCd()
  {
   return null;
  }

  /**
   * @param aProgAreaCd
   * @roseuid 3E30465700FD
   */
  public void setProgAreaCd(String aProgAreaCd)
  {

  }
  /**
    * @return java.lang.String
    * @roseuid 3E30465700B7
    */
   public String getJurisdictionCd()
   {
    return null;
   }

   /**
    * @param aJurisdictionCd
    * @roseuid 3E30465700CB
    */
   public void setJurisdictionCd(String aJurisdictionCd)
   {

   }

   /**
 * Access method for the conditionCd property.
 *
 * @return   the current value of the conditionCd property
 */
  public String getConditionCd()
  {
    return conditionCd;
  }

  /**
   * Sets the value of the conditionCd property.
   *
   * @param aConditionCd the new value of the conditionCd property
   */
  public void setConditionCd(String aConditionCd)
  {
    conditionCd = aConditionCd;
  }

public String getFieldSize() {
	return fieldSize;
}

public void setFieldSize(String fieldSize) {
	this.fieldSize = fieldSize;
}

public String getDataType() {
	return dataType;
}

public void setDataType(String dataType) {
	this.dataType = dataType;
}


}