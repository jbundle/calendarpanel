package org.jbundle.util.calendarpanel.util;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;

import org.jbundle.util.calendarpanel.CalendarPanel;
import org.jbundle.util.calendarpanel.dnd.CalendarDnDMouseListener;
import org.jbundle.util.calendarpanel.event.MyListSelectionEvent;


/**
 * Process mouse overs, clicks, and drags on an UnderlinedLabel.
 * NOTE: Remember to add this as a MouseListener and a MouseMotionListener!
 */
public class LabelMouseListener extends CalendarDnDMouseListener
    implements ActionListener
{
    private JUnderlinedLabel m_component = null;
    private CalendarCache m_cacheItem = null;
    private boolean m_bEnableDnD = false;

    /**
     * Constructor.
     */
    public LabelMouseListener()
    {
        super();
    }
    /**
     * Constructor.
     */
    public LabelMouseListener(CalendarCache cacheItem, JUnderlinedLabel component, boolean bEnableDnD)
    {
        this();
        this.init(cacheItem, component, bEnableDnD);
    }
    /**
     * Constructor.
     */
    public void init(CalendarCache cacheItem, JUnderlinedLabel component, boolean bEnableDnD)
    {
        super.init(component);
        m_cacheItem = cacheItem;
        m_component = component;
        m_bEnableDnD = bEnableDnD;
    }
    private javax.swing.Timer m_timer = null;
    /**
     * Mouse entered this label.
     */
    public void mouseEntered(MouseEvent e)
    {
        if (m_cacheItem != null)
            m_cacheItem.setUnderlined(true);
        if (m_timer == null)
        {
            m_timer = new javax.swing.Timer(500, this);   // Notify me in 1/2 second
            m_timer.setRepeats(false);          // Only once
            m_timer.start();
        }
        if (m_component.getTooltip() != null)
        	if (m_component.getTooltip().length() > 0)
        		if (this.getCalendarPanel() != null)
        			this.getCalendarPanel().setStatusText(m_component.getTooltip()); // Change the status display
    }
    /**
     * Mouse exited this label.
     */
    public void mouseExited(MouseEvent e)
    {
        if (m_cacheItem != null)
            m_cacheItem.setUnderlined(false);
        if (m_timer != null)
            this.removeTimer();
        if (m_component.getTooltip() != null)
        	if (m_component.getTooltip().length() > 0)
        		if (this.getCalendarPanel() != null)
        			this.getCalendarPanel().setStatusText(null); // Clear the status display
    }
    /**
     * User clicked the mouse - Don't know it they are dragging or want the display screen
     */
    public void mousePressed(MouseEvent e)
    {
        super.mousePressed(e);    // Save the point
    }
    /**
     * User clicked the mouse - Don't know if they are dragging or want the display screen
     */
    public void mouseReleased(MouseEvent e)
    {
        CalendarPanel parent = this.getCalendarPanel();
        if (m_ptMouseAtClick != null)
            if (!this.movedEnough(e))
        {   // Clicked - display content
            if (parent != null)
                if (m_cacheItem != null)
            {
                int iIndex = parent.getItemIndex(m_cacheItem);
                parent.getModel().fireTableRowSelected(this, iIndex, MyListSelectionEvent.CONTENT_SELECT | MyListSelectionEvent.CONTENT_CLICK);     // Notify the model of the new selection
                parent.requestFocus();      // Send key events here
                e.consume();    // It's mine.
                m_ptMouseAtClick = null;
            }
        }
        super.mouseReleased(e);
    }
    /**
     * 0.5 seconds passed, select the item.
     */
    public void actionPerformed(ActionEvent e)
    {
        CalendarPanel parent = this.getCalendarPanel();
        if (parent != null)
            if (m_cacheItem != null)
        {
            int iIndex = parent.getItemIndex(m_cacheItem);
            if (!parent.isShifted())
                parent.getModel().fireTableRowSelected(this, iIndex, MyListSelectionEvent.SELECT);      // Notify the model of the new selection
            else
                parent.getModel().fireTableRowSelected(this, iIndex, MyListSelectionEvent.ADD_SELECT);      // Notify the model of the new selection
            parent.requestFocus();      // Send key events here
        }
        this.removeTimer();
    }
    /**
     * Remove this timer.
     */
    public synchronized void removeTimer()
    {
        if (m_timer != null)
        {
            m_timer.stop();
            m_timer.removeActionListener(this);
            m_timer = null;
        }
    }
    /**
     * Remove this timer.
     */
    public CalendarPanel getCalendarPanel()
    {
        Component parent = m_component;
        while ((parent = parent.getParent()) != null)
        {
            if (parent instanceof CalendarPanel)
                return (CalendarPanel)parent;
        }
        return null;
    }
    /**
     * Invoked when a mouse button is pressed on a component and then 
     * dragged.  <code>MOUSE_DRAGGED</code> events will continue to be 
     * delivered to the component where the drag originated until the 
     * mouse button is released (regardless of whether the mouse position 
     * is within the bounds of the component).
     * <p> 
     * Due to platform-dependent Drag&Drop implementations, 
     * <code>MOUSE_DRAGGED</code> events may not be delivered during a native 
     * Drag&Drop operation.  
     */
    public void mouseDragged(MouseEvent e)
    {
        if (m_bEnableDnD)
        {
            super.mouseDragged(e);
        }
    }
}
    
