/*
 * ProductTransferable.java
 *
 * Created on August 17, 2004, 1:36 AM

 * Copyright © 2012 jbundle.org. All rights reserved.
 */
package org.jbundle.util.calendarpanel.dnd;

import java.util.Date;
import java.util.Map;

import org.jbundle.util.calendarpanel.CalendarPanel;
import org.jbundle.util.calendarpanel.model.CalendarConstants;
import org.jbundle.util.calendarpanel.model.CalendarItem;
import org.jbundle.util.calendarpanel.model.CalendarModel;
import org.jbundle.util.calendarpanel.util.CalendarCache;


/**
 * Object used to transfer data from here to there.
 */
public class DetailItemProperties extends CalendarItemProperties
{
    private static final long serialVersionUID = 1L;

    public static final String SOURCE = "source";
    public static final String ACTION = "action";
    /**
     * Constructor.
     */
    public DetailItemProperties()
    {
        super();
    }
    /**
     * Constructor.
     */
    public DetailItemProperties(Map m)
    {
        this();
        this.init(m);
    }
    /**
     * Constructor.
     */
    public void init(Map<String, Object> m)
    {
        super.init(m);
    }
    /**
     * This was dropped on a calendar pane, change the date to match.
     */
    public boolean setTargetDate(Object source, Date dateTarget)
    {
        if (!(source instanceof CalendarPanel))
            return false;
        CalendarPanel calendarPanel = (CalendarPanel)source;
        CalendarCache cacheItem = this.getSource(calendarPanel);
        if (cacheItem == null)
            return false;
        int iAction = this.getAction();
        CalendarItem item = cacheItem.getItem();
        long lTimeOld = 0;
        Date timeOld = null;
        if (iAction == CalendarConstants.START_ICON)
        {
            timeOld = item.getStartDate();
            if (timeOld != null)
                lTimeOld = timeOld.getTime();
            dateTarget = item.setStartDate(dateTarget);     // Tell the item in the model that the start date changed
        }
        else if (iAction == CalendarConstants.TEXT)
        {
            timeOld = item.getStartDate();
            if (timeOld != null)
                lTimeOld = timeOld.getTime();
            dateTarget = item.setStartDate(dateTarget);     // Tell the item in the model that the start date changed
        }
        else if (iAction == CalendarConstants.END_ICON)
        {
            timeOld = item.getEndDate();
            if (timeOld != null)
                lTimeOld = timeOld.getTime();
            dateTarget = item.setEndDate(dateTarget);   // Tell the item in the model that the end date changed
        }
        long lNewTime = 0;
        if (dateTarget != null)
            lNewTime = dateTarget.getTime();
        if (lNewTime != lTimeOld)
        {
            CalendarModel model = cacheItem.getModel();
            int iIndex = cacheItem.getIndex();
// Note: I do not need to tell the model to update as it is the model's responsibility to notify listeners of the change (actually this code will mess stuff up).
            model.fireTableRowsUpdated(iIndex, iIndex);     // Notify all listeners of this change
            return true;    // Success, date changed
        }
        return false;
    }
    /**
     *
     */
    public CalendarCache getSource(CalendarPanel calendarPanel)
    {
        Object intSource = this.get(DetailItemProperties.SOURCE);
        if (!(intSource instanceof Integer))
            return null;
        int iSource = ((Integer)intSource).intValue();
        return calendarPanel.getCacheItem(iSource);
    }
    /**
     *
     */
    public int getAction()
    {
        Object intAction = this.get(DetailItemProperties.ACTION);
        if (!(intAction instanceof Integer))
            return -1;
        int iAction = ((Integer)intAction).intValue();
        return iAction;
    }
    /**
     *
     */
    public void setSource(CalendarCache calendarCache)
    {
        int iIndex = calendarCache.getIndex();
        this.put(DetailItemProperties.SOURCE, new Integer(iIndex));
    }
    /**
     *
     */
    public void setAction(int iActionType)
    {
        this.put(DetailItemProperties.ACTION, new Integer(iActionType));
    }
}
