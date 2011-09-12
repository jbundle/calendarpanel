/*
 * ProductTransferable.java
 *
 * Created on August 17, 2004, 1:36 AM

 * Copyright © 2011 jbundle.org. All rights reserved.
 */
package org.jbundle.util.calendarpanel.dnd;

import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;

/**
 *
 * @author  don
 */
/**
 * Object used to transfer the data.
 */
public class CalendarItemTransferable extends Object implements Transferable
{
    public static String CALENDAR_ITEM_TRANSFER_TYPE = "calendaritem";
    public static CalendarItemDataFlavor gCalendarItemDataFlavor = new CalendarItemDataFlavor();
    public static DataFlavor[] grgCalendarItemDataFlavor = {gCalendarItemDataFlavor, DataFlavor.stringFlavor};
    /**
     * Data.
     */
    protected CalendarItemProperties m_calendarItemProperties = null;
    
    /**
     * Constructor.
     */
    public CalendarItemTransferable()
    {
        super();
    }
    /**
     * Constructor.
     */
    public CalendarItemTransferable(CalendarItemProperties calendarItemProperties)
    {
        this();
        this.init(calendarItemProperties);
    }
    /**
     * Constructor.
     */
    public void init(CalendarItemProperties calendarItemProperties)
    {
        m_calendarItemProperties = calendarItemProperties;
    }
    /**
     * Returns an object which represents the data to be transferred.
     */
    public Object getTransferData(DataFlavor flavor)
    {
        if (DataFlavor.stringFlavor.equals(flavor))
            return null; // TODO Fix this to return a string representation of the drag object
        return m_calendarItemProperties;
    }
    /**
     * Returns an array of DataFlavor objects indicating the flavors the data can be provided in.
     */
    public DataFlavor[] getTransferDataFlavors()
    {
        return grgCalendarItemDataFlavor;
    }
    /**
     * Returns an array of DataFlavor objects indicating the flavors the data can be provided in.
     */
    public boolean isDataFlavorSupported(DataFlavor flavor)
    {
        if (flavor instanceof CalendarItemDataFlavor)
            return true;
        return false;
    }
}
