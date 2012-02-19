/*
 * Copyright © 2012 jbundle.org. All rights reserved.
 */
package org.jbundle.util.calendarpanel.sample.rich;

import java.util.Date;
import java.util.Vector;

import javax.swing.ImageIcon;

import org.jbundle.util.calendarpanel.CalendarPanel;
import org.jbundle.util.calendarpanel.model.AbstractCalendarModel;
import org.jbundle.util.calendarpanel.model.CalendarItem;
import org.jbundle.util.calendarpanel.model.CalendarModel;


public class CalendarVector extends AbstractCalendarModel
    implements CalendarModel
{
    private static final long serialVersionUID = 1L;

    /**
     * List of CalendarItems.
     */
    protected Vector<CalendarItem> m_vItemList = new Vector<CalendarItem>();
    
    /**
     * Called to start the applet.  You never need to call this directly; it
     * is called when the applet's document is visited.
     */
    public CalendarVector(CalendarPanel calendarPanel)
    {
        super();
    }
    /**
     * Get the row count.
     */
    public int getRowCount()
    {
        return m_vItemList.size();
    }
    /**
     * I'm done with the model, free the resources.
     */
    public void free()
    {
    }
    /**
     * Start date for the calendar; return null to automatically set from the model.
     */
    public Date getStartDate()
    {
        return null;
    }
    /**
     * End date for the calendar; return null to automatically set from the model.
     */
    public Date getEndDate()
    {
        return null;
    }
    /**
     * End date for the calendar; return null to automatically set from the model.
     */
    public Date getSelectDate()
    {
        return null;
    }
    /**
     * Get the icon for a meal.
     */
    public ImageIcon getHeaderIcon()
    {
        return CalendarTestApplet.loadImageIcon("images/Meal.gif", "Meal");
    }
    /**
     * Get this item.
     */
    public CalendarItem getItem(int i)
    {
        return (CalendarItem)m_vItemList.elementAt(i);
    }
    /**
     * Returns an attribute value for the cell at columnIndex and rowIndex.
     */
    public void addElement(CalendarItem item)
    {
        m_vItemList.addElement(item);
    }
    /**
     * Returns an attribute value for the cell at columnIndex and rowIndex.
     */
    public void remove(CalendarItem item)
    {
        m_vItemList.remove(item);
    }
    /**
     * Returns an attribute value for the cell at columnIndex and rowIndex.
     */
    public int indexOf(CalendarItem item)
    {
        return m_vItemList.indexOf(item);
    }
}
