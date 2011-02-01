package org.jbundle.util.calendarpanel.util;

import java.awt.Point;
import java.util.EventObject;


public class CalendarDropEvent extends EventObject
{
    private static final long serialVersionUID = 1L;

//?    private CalendarItem m_calendarItem = null;
    private Point m_ptDropped = null;
    private int m_iAction = -1;

    /**
     * Called to start the applet.  You never need to call this directly; it
     * is called when the applet's document is visited.
     */
    public CalendarDropEvent(CalendarCache cacheItem, Point ptDropped, int iAction)
    {
        super(cacheItem);
        m_ptDropped = ptDropped;
        m_iAction = iAction;
    }
    /**
     * Get the point.
     */
    public Point getPoint()
    {
        return m_ptDropped;
    }
    /**
     * Get the point.
     */
    public int getID()
    {
        return m_iAction;
    }
}
