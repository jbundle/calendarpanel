package org.jbundle.util.calendarpanel.model;

import java.util.Date;

import javax.swing.ImageIcon;
import javax.swing.table.TableModel;

import org.jbundle.util.calendarpanel.event.MyListSelectionListener;


/**
 * Calendar model.
 */
public interface CalendarModel extends TableModel
{
    public final static int ICON = 0;
    public final static int START_TIME = 1;
    public final static int DESCRIPTION = 2;

    /**
     * Add a listener to the list that's notified each time a change to the data model occurs.
     */
//  public void addTableModelListener(TableModelListener l);
    /**
     * Returns the lowest common denominator Class in the column.
     */
//    public Class<?> getColumnClass(int columnIndex);
    /**
     * Returns the number of columns managed by the data source object.
     */
//  public int getColumnCount();
    /**
     * Returns the name of the column at columnIndex.
     */
//  public String getColumnName(int columnIndex);
    /**
     * Returns the number of records managed by the data source object.
     */
//  public int getRowCount();
    /**
     * Returns an attribute value for the cell at columnIndex and rowIndex.
     */
//  public Object getValueAt(int rowIndex, int columnIndex);
    /**
     * Returns true if the cell at rowIndex and columnIndex is editable.
     */
//  public boolean isCellEditable(int rowIndex, int columnIndex);
    /**
     * Remove a listener from the list that's notified each time a change to the data model occurs.
     */
//    public void removeTableModelListener(TableModelListener l);
    /**
     * Sets an attribute value for the record in the cell at columnIndex and rowIndex.
     */
//    public void setValueAt(Object aValue, int rowIndex, int columnIndex);
///////////// These are specifically for the calendarModel!
    /**
     * I'm done with the model, free the resources.
     */
    public void free();
    /**
     * Get this Calendar Item.
     */
    public CalendarItem getItem(int i);
    /**
     * Start date for the calendar; return null to automatically set from the model.
     */
    public Date getStartDate();
    /**
     * End date for the calendar; return null to automatically set from the model.
     */
    public Date getEndDate();
    /**
     * Initial select date for the calendar; return null to automatically set from the model.
     */
    public Date getSelectDate();
    /**
     * Get the icon for calendar pane header (such as a meal display for the day).
     * @return ImageIcon The optional header icon.
     */
    public ImageIcon getHeaderIcon();
    /**
     * Notify listeners this row is selected; pass a -1 to de-select all rows.
     * @param iSelectionType NormalSelection(mouseover for .5), ContentSelection(clicked), AddSelection(shift-click)
     */
    public void fireTableRowSelected(Object source, int iRowIndex, int iSelectionType);
    /**
     * Add a listener to my list.
     */
    public void addMySelectionListener(MyListSelectionListener l);
    /**
     * Remove a listener from my list.
     */
    public void removeMySelectionListener(MyListSelectionListener l);
    /**
     * Notify the listeners of the rows being updated.
     */
    public void fireTableRowsUpdated(int firstRow, int lastRow);
}
