/*
 * ProductTransferable.java
 *
 * Created on August 17, 2004, 1:36 AM
 */

package org.jbundle.util.calendarpanel.dnd;

import java.awt.Component;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.dnd.DropTarget;
import java.awt.dnd.DropTargetAdapter;
import java.awt.dnd.DropTargetDropEvent;
import java.io.IOException;
import java.security.AccessControlException;
import java.util.Date;

import javax.swing.JTextArea;

import org.jbundle.util.calendarpanel.CalendarPane;
import org.jbundle.util.calendarpanel.CalendarPanel;


/**
 * This class packages up a product into a ProductTransferable object.
 */
public class CalendarPaneDropTargetListener extends DropTargetAdapter
{
    /**
     * Standard constructor.
     */
    public CalendarPaneDropTargetListener()
    {
        super();
    }
    /**
     * Standard constructor.
     */
    public CalendarPaneDropTargetListener(String str)
    {
        this();
        this.init(str);
    }
    /**
     * Standard constructor.
     */
    public void init(String str)
    {
    }
    /**
     * Transfer (paste) this data to this component.
     */
    public void drop(DropTargetDropEvent dtde)
    {
        Component comp = ((DropTarget)dtde.getSource()).getComponent();
        Transferable t = dtde.getTransferable();
        Point pt = dtde.getLocation();
        if (this.canImport(comp, t.getTransferDataFlavors(), true))
        {
            try {
                CalendarItemProperties calendarItemProperties = null;
                calendarItemProperties = (CalendarItemProperties)t.getTransferData(CalendarItemTransferable.gCalendarItemDataFlavor);
                if (calendarItemProperties == null)
                {
                    String properties = (String)t.getTransferData(DataFlavor.stringFlavor);
                    System.out.println("Ignoring any security error(s), using backup data flavor");
                    // Would be nice to be able to parse a text field from another program
                    if (properties == null)
                    {   // HACK JDK6 bug (filed)
                        if (this.canImport(comp, t.getTransferDataFlavors(), false))
                            calendarItemProperties = CalendarItemProperties.currentCalendarItemPropertiesHack;  // TODO hack JDK6
                    }
                }
                if (comp instanceof CalendarPane)
                    if (calendarItemProperties != null)
                {   // Always
                    CalendarPanel calendarPanel = ((CalendarPane)comp).getCalendarPanel();
                    Rectangle rect = ((CalendarPane)comp).getBounds();
                    Date dateTarget = ((CalendarPane)comp).convertXToDate(pt.x, rect.width);
                    calendarItemProperties.setTargetDate(calendarPanel, dateTarget);
                }
            } catch (UnsupportedFlavorException ufe) {
                ufe.printStackTrace();
            } catch (IOException ioe) {
                ioe.printStackTrace();
            }
        }
    }
    /**
     * Can I import any of these flavors?
     * @param checkAllFlavors If false, only check for the native (first) flavor.
     */
    public boolean canImport(Component c, DataFlavor[] flavors, boolean checkAllFlavors)
    {
        for (DataFlavor dataFlavor: CalendarItemTransferable.grgCalendarItemDataFlavor)
        {
            for (DataFlavor flavor : flavors)
            {
                if (flavor != null)
                    if (dataFlavor.getClass().isAssignableFrom(flavor.getClass()))
                    {
                        if (dataFlavor.equals(DataFlavor.stringFlavor))
                        {   // Check the format
                            
                        }
                        return true;
                    }
            }
            if (!checkAllFlavors)
                return false;                
        }
        return false;
    }
}
