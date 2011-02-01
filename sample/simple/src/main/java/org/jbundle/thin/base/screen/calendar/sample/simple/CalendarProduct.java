package org.jbundle.thin.base.screen.calendar.sample.simple;

import java.awt.Color;
import java.util.Date;

import javax.swing.ImageIcon;

import org.jbundle.util.calendarpanel.model.CalendarConstants;
import org.jbundle.util.calendarpanel.model.CalendarItem;
import org.jbundle.util.calendarpanel.model.CalendarModel;


public class CalendarProduct extends Object
    implements CalendarItem
{
    protected CalendarVector m_model = null;

    protected String m_description = null;
    protected Date m_startTime = null;
    protected Date m_endTime = null;
    protected ImageIcon m_iconStart = null;
    protected ImageIcon m_iconEnd = null;
    protected String m_strMeals = null;
    protected Color m_colorHighlight = null;
    protected Color m_colorSelect = null;
    protected int m_iStatus = 0;

    /**
     * Constructor.
     */
    public CalendarProduct(CalendarModel model, Date startTime, Date endTime, String description, ImageIcon iconStart, ImageIcon iconEnd, String strMeals, Color colorHighlight, Color colorSelect, int iStatus)
    {
        super();
        this.init(model, startTime, endTime, description, iconStart, iconEnd, strMeals, colorHighlight, colorSelect, iStatus);
        m_strMeals = strMeals;
    }
    /**
     * Constructor.
     */
    public void init(CalendarModel model, Date startTime, Date endTime, String description, ImageIcon iconStart, ImageIcon iconEnd, String strMeals, Color colorHighlight, Color colorSelect, int iStatus)
    {
        m_startTime = startTime;
        m_endTime = endTime;
        m_description = description;
        m_model = (CalendarVector)model;
        m_iconStart = iconStart;
        m_iconEnd = iconEnd;
        m_colorHighlight = colorHighlight;
        m_colorSelect = colorSelect;
        m_iStatus = iStatus;
    }
    /**
     * I'm done with this item, free the resources.
     */
    public void free()
    {
//x     int iThisIndex = m_model.indexOf(this);
//x     m_model.fireTableRowsDeleted(iThisIndex, iThisIndex); // Notify the models to get rid of the visual
        m_model.remove(this); // Remove me!
    }
    /**
     * Delete this item.
     */
    public boolean remove()
    {
        boolean bSuccess = false;
        m_model.remove(this);
        return bSuccess;
    }
    /**
     * Get the description.
     */
    public String getDescription()
    {
        return m_description;
    }
    /**
     * Get the start time of this service.
     */
    public Date getStartDate()
    {
        return m_startTime;
    }
    /**
     * Get the ending time of this service.
     */
    public Date getEndDate()
    {
        if ((m_startTime != null) && (m_endTime != null))
        {
            if (m_endTime.before(m_startTime))
                return m_startTime;
        }
        return m_endTime;
    }
    /**
     * Get the meal description on this date.
     */
    public String getMealDesc(Date date)
    {
        if (!date.before(m_startTime)) if (!date.after(m_endTime))
            return m_strMeals;
        return null;
    }
    /**
     * Get the icon (opt).
     */
    public ImageIcon getIcon(int iIconType)
    {
        if (iIconType == CalendarConstants.END_ICON)
            return m_iconEnd;
        else if (iIconType == CalendarConstants.START_ICON)
            return m_iconStart;
        else
            return null;
    }
    /**
     * Highlight color (optional).
     */
    public Color getHighlightColor()
    {
        return m_colorHighlight;
    }
    /**
     * Highlight color (optional).
     */
    public Color getSelectColor()
    {
        return m_colorSelect;
    }
    /**
     * Change the start time of this service.
     */
    public Date setStartDate(Date time)
    {
        if ((m_startTime != null) && (m_endTime != null) && (time != null))
        {   // As a default, shift the end date the same distance as the start date
            long lChange = time.getTime() - m_startTime.getTime();
            m_endTime = new Date(m_endTime.getTime() + lChange);
        }
        m_startTime = time;
        return m_startTime;
    }
    /**
     * Change the ending time of this service.
     */
    public Date setEndDate(Date time)
    {
        m_endTime = time;
        return m_endTime;
    }
    /**
     * Set the icon (opt).
     */
    public void setIcon(ImageIcon icon, int iIconType)
    {
        if (iIconType == CalendarConstants.END_ICON)
            m_iconEnd = icon;
        if (iIconType == CalendarConstants.END_ICON)
            m_iconStart = icon;
    }
    /**
     * Get the display window for this object.
     */
    public Object getVisualJavaBean(int iPanelType)
    {
        return null;
    }
    /**
     * Get the status of this object.
     */
    public int getStatus()
    {
        return m_iStatus;
    }
    /**
     * Set the status of this item.
     */
    public int setStatus(int iStatus)
    {
        m_iStatus = iStatus;
        return m_iStatus;
    }
}
