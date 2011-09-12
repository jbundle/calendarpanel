/*
 * ProductTransferable.java
 *
 * Created on August 17, 2004, 1:36 AM

 * Copyright © 2011 jbundle.org. All rights reserved.
 */
package org.jbundle.util.calendarpanel.dnd;

import java.awt.datatransfer.Transferable;

import javax.swing.JComponent;
import javax.swing.TransferHandler;

import org.jbundle.util.calendarpanel.util.CalendarCache;
import org.jbundle.util.calendarpanel.util.JUnderlinedLabel;


/**
 * This class packages up a product into a ProductTransferable object.
 */
public class CalendarItemTransferHandler extends TransferHandler {
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/**
     * Standard constructor.
     */
    public CalendarItemTransferHandler(String strType)
    {
        super(strType);
    }
    /**
     * Actions that I support.
     */
    public int getSourceActions(JComponent c)
    {
        return COPY_OR_MOVE;
    }
    /**
     * Create the transferable object using this source object.
     */
    protected Transferable createTransferable(JComponent c)
    {
        return new CalendarItemTransferable(this.exportProduct(c));
    }
    /**
     * Transfer (paste) this data to this component.
     */
    public boolean importData(JComponent c, Transferable t)
    {
        return false;   // A calendar item is never a dnd destination.
    }
    /**
     * Figure out the product that is connected to this component and create a ProductProperties object to transfer it.
     */
    protected CalendarItemProperties exportProduct(JComponent c)
    {
        if (c instanceof JUnderlinedLabel)
        {
            int iActionType = ((JUnderlinedLabel)c).getComponentType();
            CalendarCache itemCache = ((JUnderlinedLabel)c).getItemCache();
            if (itemCache != null)
            {
                DetailItemProperties calendarItemProperties = new DetailItemProperties(null);
                calendarItemProperties.setAction(iActionType);
                calendarItemProperties.setSource(itemCache);
                return calendarItemProperties;
            }
        }
        return null;
    }
}
