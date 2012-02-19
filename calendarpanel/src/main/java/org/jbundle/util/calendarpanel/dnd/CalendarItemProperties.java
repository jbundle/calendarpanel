/*
 * ProductTransferable.java
 *
 * Created on August 17, 2004, 1:36 AM

 * Copyright © 2012 jbundle.org. All rights reserved.
 */
package org.jbundle.util.calendarpanel.dnd;

import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Object used to transfer data from here to there.
 */
public abstract class CalendarItemProperties extends HashMap<String,Object>
    implements Serializable
{
    protected static transient CalendarItemProperties currentCalendarItemPropertiesHack = null;
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/**
     * Constructor.
     */
    public CalendarItemProperties()
    {
        super();
    }
    /**
     * Constructor.
     */
    public CalendarItemProperties(Map<String, Object> m)
    {
        this();
        this.init(m);
    }
    /**
     * Constructor.
     */
    public void init(Map<String, Object> m)
    {
        if (m != null)
            this.putAll(m);
        currentCalendarItemPropertiesHack = this;
    }
    /**
     * This was dropped on a calendar pane, change the date to match.
     */
    public abstract boolean setTargetDate(Object source, Date dateTarget);
}
