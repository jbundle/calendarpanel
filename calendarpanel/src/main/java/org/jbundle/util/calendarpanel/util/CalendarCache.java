/*
 * Copyright © 2011 jbundle.org. All rights reserved.
 */
package org.jbundle.util.calendarpanel.util;

import java.awt.Container;
import java.awt.Rectangle;
import java.util.Date;
import java.util.Vector;

import org.jbundle.util.calendarpanel.CalendarPanel;
import org.jbundle.util.calendarpanel.model.CalendarItem;
import org.jbundle.util.calendarpanel.model.CalendarModel;


/**
 * Cache all the calendar item information to keep from re-thrashing on every panel pass.
 * Also save the list of components used to display this item, so they can be managed.
 */
public class CalendarCache extends Object
{
    protected Date m_dateDescEnd = null;
    protected int m_iLine = -1;
    protected CalendarPanel m_calendarPanel = null;
    protected boolean m_bSelected = false;

    protected CalendarModel m_model = null;
    protected int m_iIndex = -1;
    /**
     * List of screen components displaying this calendar item.
     */
    public Vector<JUnderlinedLabel> m_vectorComponents = null;

    /**
     * Constructor.
     */
    public CalendarCache()
    {
        super();
    }
    /**
     * Constructor.
     */
    public CalendarCache(CalendarPanel calendarPanel, CalendarModel model, int iIndex)
    {
        this();
        this.init(calendarPanel, model, iIndex);
    }
    /**
     * Constructor.
     */
    public void init(CalendarPanel calendarPanel, CalendarModel model, int iIndex)
    {
        m_calendarPanel = calendarPanel;
        m_iLine = -1;
        this.cacheItem(model, iIndex);
    }
    /**
     * Free this object.
     */
    public void free()
    {
        this.removeComponents();
    }
    /**
     * Get the larger of the end date and the date where the description ends
     */
    public Date getEndDesc()
    {
        Date endTime = this.getItem().getEndDate();
        if (endTime == null)
            endTime = this.getItem().getStartDate();
        if (m_dateDescEnd != null)
            if (endTime != null)
                if (m_dateDescEnd.after(endTime))
                    return m_dateDescEnd;
        return endTime;
    }
    /**
     * Get the line that this service belongs on (1 = line 1, etc).
     * If this has not been assigned a line number, and a valid ending description date has
     * been assigned, assign this line a line number.
     */
    public int getLine(Date dateDescEnd)
    {
        if (dateDescEnd != null) if (m_iLine == -1)
        {
            m_dateDescEnd = dateDescEnd;
            this.findLine();    // I have not been assigned a line, get one
        }
        return m_iLine;
    }
    /**
     * Set the line that this item belongs on.
     */
    public void setLine(int iLine)
    {
        m_iLine = iLine;
    }
    /**
     * Find an empty line to place this description
     */
    public void findLine()
    {
        long lBitmap = 0;
        CalendarItem calendarItem = this.getItem();
        if (calendarItem == null)
            return;
        Date dateCalendarStart = calendarItem.getStartDate();
        if ((dateCalendarStart == null) || (dateCalendarStart.getTime() == 0))
            return;
        Date dateDescEnd = this.getEndDesc();
        if ((dateDescEnd == null) || (dateDescEnd.getTime() == 0))
            return;
        for (int i = 0; i < m_calendarPanel.getCacheCount(true); i++)
        {
            CalendarCache cacheItem = m_calendarPanel.getCacheItem(i);
            if (this != cacheItem) if (cacheItem.getLine(null) != -1)
            { // This one has a space - check it for conflicts
                CalendarItem item = cacheItem.getItem();
                if (item == null)
                    continue;
                Date dateCurrentItemStart = item.getStartDate();
                if ((dateCurrentItemStart == null) || (dateCurrentItemStart.getTime() == 0))
                    continue;
                Date dateCurrentDescEnd = cacheItem.getEndDesc();
                if ((dateCurrentDescEnd == null) || (dateCurrentDescEnd.getTime() == 0))
                    continue;
                if ((!dateCalendarStart.after(dateCurrentDescEnd)) &&
                    (!dateDescEnd.before(dateCurrentItemStart)))
                { // We can't go on the same line
                    lBitmap |= (1 << cacheItem.getLine(null));  // Set this bit
                }
            }
        }
        int iTargetLine = 1;
        for (iTargetLine = 1; iTargetLine < 63; iTargetLine++)
        {
            if ((lBitmap & (1 << iTargetLine)) == 0)
                break;
        }
        this.setLine(iTargetLine);
    }
    /**
     * Cache all the item information for speedy recovery.
     */
    public void cacheItem(CalendarModel model, int iIndex)
    {
        if (model != null)
            m_model = model;
        m_iIndex = iIndex;
        this.setLine(-1); // Don't know where this is anymore
        m_dateDescEnd = null; // Re-calc this
    }
    /**
     * Get the item that this cache refers to.
     */
    public CalendarItem getItem()
    {
        if (m_iIndex == -1)
            return null;    // Deleted entry
        return m_model.getItem(m_iIndex);
    }
    /**
     * Get the ending icon (optional).
     */
    public void addComponent(JUnderlinedLabel component)
    {
        if (m_vectorComponents == null)
            m_vectorComponents = new Vector<JUnderlinedLabel>();
        m_vectorComponents.addElement(component);
    }
    /**
     * Get the ending icon (optional).
     */
    public void removeComponent(JUnderlinedLabel component)
    {
        if (m_vectorComponents == null)
            return;
        for (int i = 0; i < m_vectorComponents.size(); i++)
        {   // This loop is a hack around a bug in jdk1.3.0 vector.remove(object);
            if (m_vectorComponents.elementAt(i) == component)
            {
                m_vectorComponents.remove(i);
                return;
            }
        }
    }
    /**
     * This item has been underlined. Notify all components they are now underlined.
     */
    public void setUnderlined(boolean bSelected)
    {
        for (JUnderlinedLabel label : m_vectorComponents)
        {
            label.setUnderlined(bSelected);
        }
    }
    /**
     * This item has been selected. Notify all components they are now selected.
     */
    public void setSelected(boolean bSelected)
    {
        m_bSelected = bSelected;
        if (m_vectorComponents == null)
            return;
        for (JUnderlinedLabel label : m_vectorComponents)
        {
            label.setSelected(bSelected);
        }
    }
    /**
     * Is this item selected?
     */
    public boolean isSelected()
    {
        return m_bSelected;
    }
    /**
     * Remove all the components associated with this cacheitem.
     */
    public void removeComponents()
    {
        if (m_model != null)
            if (m_vectorComponents != null)
        {
            for (int iIndex = m_vectorComponents.size() - 1; iIndex >= 0; iIndex--)
            {
                JUnderlinedLabel component = m_vectorComponents.elementAt(iIndex);
                Container parent = component.getParent();
                Rectangle rect = component.getBounds();
                component.free();
                if (parent != null)
                    parent.repaint(rect.x, rect.y, rect.width, rect.height);
            }
        }
//x     this.removeAll(); // Remove any leftovers
    }
    /**
     * Get the model this item belongs to.
     */
    public CalendarModel getModel()
    {
        return m_model;
    }
    /**
     * Get the item in the model this item refers to.
     */
    public int getIndex()
    {
        return m_iIndex;
    }
    /**
     * Get the item in the model this item refers to.
     */
    public void setIndex(int iIndex)
    {
        m_iIndex = iIndex;
    }
}
