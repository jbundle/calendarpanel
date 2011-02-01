package org.jbundle.util.calendarpanel.util;

import java.awt.Component;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionAdapter;
import java.util.Date;

import org.jbundle.util.calendarpanel.CalendarPane;
import org.jbundle.util.calendarpanel.CalendarPanel;


/**
 * This is a listener which modifies a text box as the mouse moves over the calendar pane.
 */
public class MouseDateListener extends MouseMotionAdapter
    implements MouseListener
{
//x   public JTextField m_componentDate = null;
    protected CalendarPanel m_calendarPanel = null;
    
    public MouseDateListener(CalendarPanel calendarPanel)
    {
        super();
        m_calendarPanel = calendarPanel;
    }
    /**
     * Mouse is over this pane, change the date.
     */
    public void mouseMoved(MouseEvent e)
    {
        CalendarPanel calPanel = (CalendarPanel)e.getComponent();
        Component component = calPanel.getComponentAt(e.getPoint());
        if (component == null)
            return;
        if (component instanceof JUnderlinedLabel)
            component = component.getParent();
        if (component instanceof CalendarPane)
        {
            CalendarPane pane = (CalendarPane)component;
            int iX = e.getPoint().x - component.getBounds().x;
            Date dateTarget = pane.convertXToDate(iX, component.getBounds().width);
            this.firePropertyChange("dateDisplay", null, dateTarget);
        }
        else
            this.firePropertyChange("dateDisplay", null, null);   // No flyover date (change back to target date)
    }

    /**
     * Invoked when the mouse has been clicked on a component.
     */
    public void mouseClicked(MouseEvent e)
    {
    }
    /**
     * Invoked when the mouse enters a component.
     */
    public void mouseEntered(MouseEvent e) 
    {
    }
    /**
     * Invoked when the mouse exits a component.
     */
    public void mouseExited(MouseEvent e)
    {
        /*CalendarPanel calPanel = (CalendarPanel)*/e.getComponent();
        this.firePropertyChange("dateDisplay", null, null);   // No flyover date (change back to target date) 
    }
    /**
     * Invoked when a mouse button has been pressed on a component.
     */
    public void mousePressed(MouseEvent e) 
    {
    }
    /**
     * Invoked when a mouse button has been released on a component.
     */
    public void mouseReleased(MouseEvent e) 
    {
    }
    /** Propery change support */
    protected transient java.beans.PropertyChangeSupport propertyChange = new java.beans.PropertyChangeSupport(this);
    /**
     * The addPropertyChangeListener method was generated to support the propertyChange field.
     */
    public synchronized void addPropertyChangeListener(java.beans.PropertyChangeListener listener) {
        propertyChange.addPropertyChangeListener(listener);
    }
    /**
     * The firePropertyChange method was generated to support the propertyChange field.
     */
    public void firePropertyChange(String propertyName, Object oldValue, Object newValue) {
        if (propertyChange != null)
            propertyChange.firePropertyChange(propertyName, oldValue, newValue);
    }
}
