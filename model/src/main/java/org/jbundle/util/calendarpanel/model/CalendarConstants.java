/*
 * Copyright © 2012 jbundle.org. All rights reserved.
 */
package org.jbundle.util.calendarpanel.model;


/**
 * The label to put the calendar items into.
 */
public interface CalendarConstants
{
    public static final int START_ICON = 1000;
            // Note: Do not use numbers 1001 - 1009: These are for 2nd Start Icon, etc.
    public static final int END_ICON = 1010;

    public static final int TEXT = 1011;
    public static final int MEAL_TEXT = 1012;
    public static final int NOT_DRAGGABLE = 0;
    
    public static final String DELETE = "Delete";
    public static final String SELECT_ALL = "Select all";
    public static final String UP = "Up";
    public static final String DOWN = "Down";
    public static final String RIGHT = "Right";
    public static final String LEFT = "Left";
}
