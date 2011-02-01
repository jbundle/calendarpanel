/*
 * ProductTransferable.java
 *
 * Created on August 17, 2004, 1:36 AM
 */

package org.jbundle.util.calendarpanel.dnd;

import java.awt.datatransfer.DataFlavor;

/**
 * Data type expected for product transfers.
 */
public class CalendarItemDataFlavor extends DataFlavor
{    
    /**
     *
     */
    public CalendarItemDataFlavor()
    {
        super(CalendarItemProperties.class, "Calendar Information");
    }

}