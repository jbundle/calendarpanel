/*
 * Copyright © 2011 jbundle.org. All rights reserved.
 */
package org.jbundle.util.calendarpanel.model;

import javax.swing.ImageIcon;
import javax.swing.event.EventListenerList;
import javax.swing.table.AbstractTableModel;

import org.jbundle.util.calendarpanel.event.MyListSelectionEvent;
import org.jbundle.util.calendarpanel.event.MyListSelectionListener;


/**
 * The abstract calendar model is a model that handles the basic functions for the calendar model.
 */
public abstract class AbstractCalendarModel extends AbstractTableModel
    implements CalendarModel
{
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	/**
     * Returns the number of columns managed by the data source object.
     */
    public int getColumnCount()
    {
        return CalendarModel.DESCRIPTION + 1;
    }
    /**
     * Returns the name of the column at columnIndex.
     */
    public String getColumnName(int columnIndex)
    {
        switch (columnIndex)
        {
            case CalendarModel.ICON:
                return "Type";
            case CalendarModel.START_TIME:
                return "Start time";
            case CalendarModel.DESCRIPTION:
                return "Description";
        }
        return null;
    }
    /**
     *  Returns Object.class by default
     */
    public Class<?> getColumnClass(int columnIndex)
    {
        switch (columnIndex)
        {
            case CalendarModel.ICON:
                return ImageIcon.class;
            case CalendarModel.START_TIME:
                return String.class;
            case CalendarModel.DESCRIPTION:
                return String.class;
        }
        return super.getColumnClass(columnIndex);
    }
    /**
     *  This default implementation returns false for all cells
     */
    public boolean isCellEditable(int rowIndex, int columnIndex)
    {
        if (columnIndex == CalendarModel.START_TIME)
            return true;
        return super.isCellEditable(rowIndex, columnIndex);
    }
    /**
     * Returns an attribute value for the cell at columnIndex and rowIndex.
     */
    public Object getValueAt(int rowIndex, int columnIndex)
    {
        CalendarItem item = this.getItem(rowIndex);
        if (columnIndex == CalendarModel.ICON)
            return item.getIcon(CalendarConstants.START_ICON);
        if (columnIndex == CalendarModel.START_TIME)
            return item.getStartDate();
        if (columnIndex == CalendarModel.DESCRIPTION)
            return item.getDescription();
        return null;
    }
    /**
     *  This empty implementation is provided so users don't have to implement
     *  this method if their data model is not editable.
     */
    public void setValueAt(Object aValue, int rowIndex, int columnIndex)
    {
        super.setValueAt(aValue, rowIndex, columnIndex);
    }
    /**
     * Notify listeners this row is selected; pass a -1 to de-select all rows.
     */
    public void fireTableRowSelected(Object source,int iRowIndex,int iSelectionType)
    {
        this.fireMySelectionChanged(new MyListSelectionEvent(source, this, iRowIndex, iSelectionType));
    }
    /**
     * The listener list.
     */
    protected EventListenerList listenerList = new EventListenerList();
    /**
     * Add a listener to my list.
     */
    public void addMySelectionListener(MyListSelectionListener l)
    {
        listenerList.add(MyListSelectionListener.class, l);
    }
    /**
     * Remove a listener from my list.
     */
    public void removeMySelectionListener(MyListSelectionListener l)
    {
        listenerList.remove(MyListSelectionListener.class, l);
    }
    /**
     * Notify all listeners that have registered interest for
     * notification on this event type.  The event instance 
     * is lazily created using the parameters passed into 
     * the fire method.
     */
    protected void fireMySelectionChanged(MyListSelectionEvent event)
    {
        // Guaranteed to return a non-null array
        Object[] listeners = listenerList.getListenerList();
        // Process the listeners last to first, notifying
        // those that are interested in this event
        for (int i = listeners.length-2; i>=0; i-=2)
        {
            if (listeners[i]==MyListSelectionListener.class)
                if (listeners[i] != event.getSource())  // Don't send it back to source
            { // Send this message
                ((MyListSelectionListener)listeners[i+1]).selectionChanged(event);
            }
        }
    }
}
