package gov.cdc.nedss.systemservice.ejb.mprupdateejb.bean;

import javax.ejb.*;
import java.rmi.RemoteException;

public interface MPRUpdateEngineHome extends EJBHome
{
    
    /**
     * @J2EE_METHOD  --  create
     */
    public MPRUpdateEngine create    () 
                throws CreateException, RemoteException;
}