package org.jbundle.util.calendarpanel.model;

import java.util.Date;
import java.util.Vector;

import javax.swing.ImageIcon;


/**
 * Cached list of CalendarItems.
 * NOTE: This model assumes you start with an empty list and then you add the items after the initial display.
 */
public class CachedCalendarModel extends AbstractCalendarModel
    implements CalendarModel
{
    private static final long serialVersionUID = 1L;
    /**
     * The header to display at the top of the calendar panes (such as a meal icon)
     */
    protected ImageIcon headerIcon = null;		//org.jbundle.thin.opt.calendar.popup.ProductTypeInfo.getProductType(org.jbundle.thin.opt.calendar.popup.ProductConstants.MEAL).getStartIcon();
    /**
     * List of CalendarItems.
     */
    protected Vector<CalendarItem> itemList = new Vector<CalendarItem>();
    
    /**
     * Called to start the applet.  You never need to call this directly; it
     * is called when the applet's document is visited.
     */
    public CachedCalendarModel()
    {
        super();
    }
    /**
     * Called to start the applet.  You never need to call this directly; it
     * is called when the applet's document is visited.
     */
    public CachedCalendarModel(ImageIcon headerIcon)
    {
        this();
        this.init(headerIcon);
    }
    /**
     * Called to start the applet.  You never need to call this directly; it
     * is called when the applet's document is visited.
     */
    public void init(ImageIcon headerIcon)
    {
    	this.headerIcon = headerIcon;
    }
    /**
     * I'm done with the model, free the resources.
     */
    public void free()
    {
//      super.free();
    }
    /**
     * Get the row count.
     */
    public int getRowCount()
    {
        return itemList.size();
    }
    /**
     * Start date for the calendar; return null to automatically set from the model.
     */
    public Date getStartDate()
    {
        return null;
    }
    /**
     * End date for the calendar; return null to automatically set from the model.
     */
    public Date getEndDate()
    {
        return null;
    }
    /**
     * Select date for the calendar; return null to automatically set from the model.
     */
    public Date getSelectDate()
    {
        return null;
    }
   /**
     * Get this item.
     */
    public CalendarItem getItem(int i)
    {
        return (CalendarItem)itemList.elementAt(i);
    }
    /**
     * Get the icon for calendar pane header (such as a meal display for the day).
     * @return ImageIcon The optional header icon.
     */
    public ImageIcon getHeaderIcon()
    {
        return headerIcon;
    }
    /**
     * Add this item to the model and update the model.
     * @param item The item to add
     */
    public void addElement(CalendarItem item)
    {
        itemList.addElement(item);
        int index = itemList.size() - 1;
        this.fireTableRowsUpdated(index, index);
    }
    /**
     * Returns an attribute value for the cell at columnIndex and rowIndex.
     * @return The index of this item.
     */
    public int indexOf(CalendarItem item)
    {
        return itemList.indexOf(item);
    }
}
