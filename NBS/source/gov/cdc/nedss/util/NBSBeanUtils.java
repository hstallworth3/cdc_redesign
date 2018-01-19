package gov.cdc.nedss.util;

import java.beans.PropertyDescriptor;
import java.lang.reflect.InvocationTargetException;

import org.apache.commons.beanutils.BeanUtilsBean;

public class NBSBeanUtils extends BeanUtilsBean {
    
    public void copyNonNullProperties(Object dest, Object orig)
            throws IllegalAccessException, InvocationTargetException 
    {
        // Validate existence of the specified beans
        if (dest == null) {
            throw new IllegalArgumentException
                    ("No destination bean specified");
        }
        if (orig == null) {
            throw new IllegalArgumentException("No origin bean specified");
        }

        PropertyDescriptor[] origDescriptors = getPropertyUtils().getPropertyDescriptors(orig);
        for (int i = 0; i < origDescriptors.length; i++) {
            String name = origDescriptors[i].getName();
            if ("class".equals(name)) {
                continue; // No point in trying to set an object's class
            }
            if (getPropertyUtils().isReadable(orig, name) &&
                getPropertyUtils().isWriteable(dest, name)) {
                try {
                    Object value = getPropertyUtils().getSimpleProperty(orig, name);
                    
                    // perform copy only if the source value is not null
                    if (value != null) {
                        copyProperty(dest, name, value);
                    }
                    
                } catch (NoSuchMethodException e) {
                    // Should not happen
                }
            }
        }
     }
}