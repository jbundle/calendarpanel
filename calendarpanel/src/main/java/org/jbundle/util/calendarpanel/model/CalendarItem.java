package org.jbundle.util.calendarpanel.model;

import java.awt.Color;
import java.util.Date;

import javax.swing.ImageIcon;

public interface CalendarItem
{
    /**
     * Types for the getVisualJavaBean call.
     */
    public static final int SINGLE_LINE_BEAN = 1;
    public static final int BANNER_SIZE_BEAN = 2;
    public static final int FULL_SIZE_BEAN = 3;

    /**
     * I'm done with this item, free the resources.
     */
    public boolean remove();
    /**
     * Get the description.
     */
    public String getDescription();
    /**
     * Get the start time of this service.
     */
    public Date getStartDate();
    /**
     * Get the ending time of this service.
     */
    public Date getEndDate();
    /**
     * Get the meal description on this date.
     */
    public String getMealDesc(Date date);
    /**
     * Get the icon (opt).
     */
    public ImageIcon getIcon(int iIconType);
    /**
     * Get the highlight color (optional).
     */
    public Color getHighlightColor();
    /**
     * Get the highlight color (optional).
     */
    public Color getSelectColor();
    /**
     * Change the start time of this service.
     */
    public Date setStartDate(Date time);
    /**
     * Change the ending time of this service.
     */
    public Date setEndDate(Date time);
    /**
     * Set the icon (opt).
     */
    public void setIcon(ImageIcon icon, int iIconType);
    /**
     * Get the display window for this object.
     */
    public Object getVisualJavaBean(int iPanelType);
    /**
     * Get the status of this object.
     */
    public int getStatus();
    /**
     * Set the status of this item.
     */
    public int setStatus(int iStatus);
}
