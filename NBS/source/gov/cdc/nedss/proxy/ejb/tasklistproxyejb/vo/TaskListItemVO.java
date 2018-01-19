//Source file: C:\\CDC\\Code Frameworks\\gov\\cdc\\nedss\\helpers\\TaskListItemVO.java

package gov.cdc.nedss.proxy.ejb.tasklistproxyejb.vo;

import  gov.cdc.nedss.util.*;

public class TaskListItemVO extends AbstractVO
{

   /**
    * holds name of Task List<Object> Item (e.g. "My Program Area's Investigations")
    */
   private String taskListItemName;

   /**
    * holds count of items in the particular Task List<Object> Item (e.g. "My Program Area's
    * Investigations (32)")
    */
   private Integer taskListItemCount;

   /**
    * @roseuid 3C55DAD101BE
    */
   public TaskListItemVO()
   {

   }

   /**
    * Access method for the taskListItemName property.
    *
    * @return   the current value of the taskListItemName property
    */
   public String getTaskListItemName()
   {
      return taskListItemName;
   }

   /**
    * Sets the value of the taskListItemName property.
    *
    * @param aTaskListItemName the new value of the taskListItemName property
    */
   public void setTaskListItemName(String aTaskListItemName)
   {
      taskListItemName = aTaskListItemName;
   }

   /**
    * Access method for the taskListItemCount property.
    *
    * @return   the current value of the taskListItemCount property
    */
   public Integer getTaskListItemCount()
   {
      return taskListItemCount;
   }

   /**
    * Sets the value of the taskListItemCount property.
    *
    * @param aTaskListItemCount the new value of the taskListItemCount property
    */
   public void setTaskListItemCount(Integer aTaskListItemCount)
   {
      taskListItemCount = aTaskListItemCount;
   }

   public boolean isEqual(java.lang.Object objectname1, java.lang.Object objectname2, Class<?> voClass)
   {
    return true;
   }

   public void setItDirty(boolean itDirty)
   {

   }

    public boolean isItDirty()
   {
    return itDirty;
   }

   public void setItNew(boolean itNew)
   {

   }

    public boolean isItNew()
   {
    return itNew;
   }

   public void setItDelete(boolean itDelete)
   {

   }
    public boolean isItDelete()
   {
    return itDelete;
   }

}
