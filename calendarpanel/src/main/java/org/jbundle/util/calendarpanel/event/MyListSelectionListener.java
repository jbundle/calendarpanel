/**
 * MyListSelectionListener.java
 *
 * Created on September 27, 2000, 5:29 AM
 */
 
package org.jbundle.util.calendarpanel.event;

import java.util.EventListener;

/** 
 *
 * @author  Administrator
 * @version 1.0.0
 */
public interface MyListSelectionListener extends EventListener {
    
    public void selectionChanged(MyListSelectionEvent evt);

}
