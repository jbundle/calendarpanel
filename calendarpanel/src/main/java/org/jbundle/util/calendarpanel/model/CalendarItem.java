/*
 * Copyright © 2012 jbundle.org. All rights reserved.
 */
package org.jbundle.util.calendarpanel.model;

import java.util.Date;

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
    public Object getIcon(int iIconType);
    /**
     * Get the highlight color (optional).
     */
    public int getHighlightColor();
    /**
     * Get the highlight color (optional).
     */
    public int getSelectColor();
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
    public void setIcon(Object icon, int iIconType);
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
