/*
 * LabelsMouseListener.java
 *
 * Created on August 17, 2004, 1:30 AM

 * Copyright © 2011 jbundle.org. All rights reserved.
 */
package org.jbundle.util.calendarpanel.dnd;

import java.awt.Point;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;

import javax.swing.JComponent;
import javax.swing.TransferHandler;

/**
 *
 * @author  don
 */
/**
 * Copies data to transferhandler on mouse drag.
 */
public class CalendarDnDMouseListener extends MouseAdapter
    implements MouseMotionListener
{
    protected Point m_ptMouseAtClick = null;

    /**
     * Constructor.
     */
//    public CalendarDnDMouseListener(ThinTableModel thinTableModel, JComponent component)
//    {
//        super();
//    }
    /**
     * Constructor.
     */
    public CalendarDnDMouseListener()
    {
        super();
    }
    /**
     * Constructor.
     */
    public CalendarDnDMouseListener(JComponent component)
    {
        this();
        this.init(component);
    }
    /**
     * Constructor.
     */
    public void init(JComponent component)
    {
    }
    /**
     * Mouse dragging.
     */
    public void mousePressed(MouseEvent e)
    { // Start dragging
        m_ptMouseAtClick = e.getPoint();    // Save the point
    }
    /**
     * User clicked the mouse - Don't know if they are dragging or want the display screen
     */
    public void mouseReleased(MouseEvent e)
    {
        m_ptMouseAtClick = null;
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
        if (this.movedEnough(e))
        {
            JComponent c = (JComponent)e.getSource();
            TransferHandler handler = c.getTransferHandler();
            handler.exportAsDrag(c, e, TransferHandler.COPY);
            m_ptMouseAtClick = null;    // DnD has it now
        }
    }
    /**
     * Invoked when the mouse cursor has been moved onto a component
     * but no buttons have been pushed.
     */
    public void mouseMoved(MouseEvent e)
    {
        // Required for mouse motion listener
    }
    /**
     * Did the mouse move enough to be considered a drag?
     */
    public boolean movedEnough(MouseEvent e)
    {
        if (m_ptMouseAtClick != null)
            if ((Math.abs(e.getPoint().x - m_ptMouseAtClick.x) >= 3)
                || (Math.abs(e.getPoint().y - m_ptMouseAtClick.y) >= 3))
                    return true;
        return false;
    }
}
