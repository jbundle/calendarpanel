/**
 * @(#)CalendarPaneLayout.java  1.32 98/04/22
 *
 * Copyright 1995-1997 by Sun Microsystems, Inc.,
 * 901 San Antonio Road, Palo Alto, California, 94303, U.S.A.
 * All rights reserved.
 *
 * This software is the confidential and proprietary information
 * of Sun Microsystems, Inc. ("Confidential Information").  You
 * shall not disclose such Confidential Information and shall use
 * it only in accordance with the terms of the license agreement
 * you entered into with Sun.
 */

package org.jbundle.util.calendarpanel;

import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.LayoutManager2;

/**
 * A border layout lays out a container, arranging and resizing
 */
public class CalendarPaneLayout implements LayoutManager2
{
    protected Dimension m_dimPreferred = null;
    protected CalendarPanel m_calendarPanel = null;
    public final static Dimension MAX_DIMENSION = new Dimension(Integer.MAX_VALUE, Integer.MAX_VALUE);

    /**
     * Constructs a new border layout with  
     * no gaps between components.
     * @since     JDK1.0
     */
    public CalendarPaneLayout()
    {
    }
    /**
     * Constructs a new border layout with  
     * no gaps between components.
     * @since     JDK1.0
     */
    public CalendarPaneLayout(CalendarPanel calendarPanel)
    {
        this();
        this.init(calendarPanel);
    }
    /**
     * Constructs a new border layout with  
     * no gaps between components.
     * @since     JDK1.0
     */
    public void init(CalendarPanel calendarPanel)
    {
        m_calendarPanel = calendarPanel;
    }

    /**
     * Adds the specified component to the layout, using the specified
     * constraint object.  For border layouts, the constraint must be
     * one of the following strings:  <code>"North"</code>,
     * <code>"South"</code>, <code>"East"</code>,
     * <code>"West"</code>, or <code>"Center"</code>.  
     * <p>
     * Most applications do not call this method directly. This method 
     * is called when a component is added to a container using the 
     * <code>Container.add</code> method with the same argument types.
     * @param   comp         the component to be added.
     * @param   constraints  an object that specifies how and where 
     *                       the component is added to the layout.
     * @see     java.awt.Container#add(java.awt.Component, java.lang.Object)
     * @exception   IllegalArgumentException  if the constraint object is not
     *                 a string, or if it not one of the five specified strings.
     * @since   JDK1.1
     */
    public void addLayoutComponent(Component comp, Object constraints)
    {
    }

    /**
     * @deprecated  replaced by <code>addLayoutComponent(Component, Object)</code>.
     */
    public void addLayoutComponent(String name, Component comp) {
      this.addLayoutComponent(comp, name);
    }

    /**
     * Removes the specified component from this border layout. This 
     * method is called when a container calls its <code>remove</code> or 
     * <code>removeAll</code> methods. Most applications do not call this 
     * method directly. 
     * @param   comp   the component to be removed.
     * @see     java.awt.Container#remove(java.awt.Component)
     * @see     java.awt.Container#removeAll()
     * @since   JDK1.0
     */
    public void removeLayoutComponent(Component comp) {
    }

    /**
     * Determines the minimum size of the <code>target</code> container 
     * using this layout manager. 
     * <p>
     * This method is called when a container calls its 
     * <code>getMinimumSize</code> method. Most applications do not call 
     * this method directly. 
     * @param   target   the container in which to do the layout.
     * @return  the minimum dimensions needed to lay out the subcomponents 
     *          of the specified container.
     * @see     java.awt.Container  
     * @see     java.awt.BorderLayout#preferredLayoutSize
     * @see     java.awt.Container#getMinimumSize()
     * @since   JDK1.0
     */
    public Dimension minimumLayoutSize(Container target)
    {
        return m_calendarPanel.getPaneMinimum();
    }
    
    /**
     * Determines the preferred size of the <code>target</code> 
     * container using this layout manager, based on the components
     * in the container. 
     * <p>
     * Most applications do not call this method directly. This method
     * is called when a container calls its <code>getPreferredSize</code> 
     * method.
     * @param   target   the container in which to do the layout.
     * @return  the preferred dimensions to lay out the subcomponents 
     *          of the specified container.
     * @see     java.awt.Container  
     * @see     java.awt.BorderLayout#minimumLayoutSize  
     * @see     java.awt.Container#getPreferredSize()
     * @since   JDK1.0
     */
    public Dimension preferredLayoutSize(Container target)
    {
//x        if (m_dimPreferred == null)
//x            m_dimPreferred = new Dimension(0, 0);
//x        m_dimPreferred.setSize(m_calendarPanel.getPanePreferred().width, Math.max(m_calendarPanel.getPanePreferred().height, target.getBounds().height));
        return m_calendarPanel.getPanePreferred();
//x        return m_dimPreferred;
    }

    /**
     * Returns the maximum dimensions for this layout given the components
     * in the specified target container.
     * @param target the component which needs to be laid out
     * @see Container
     * @see #minimumLayoutSize
     * @see #preferredLayoutSize
     */
    public Dimension maximumLayoutSize(Container target)
    {
        return MAX_DIMENSION;
    }

    /**
     * Returns the alignment along the x axis.  This specifies how
     * the component would like to be aligned relative to other 
     * components.  The value should be a number between 0 and 1
     * where 0 represents alignment along the origin, 1 is aligned
     * the furthest away from the origin, 0.5 is centered, etc.
     */
    public float getLayoutAlignmentX(Container parent)
    {
    return 0.5f;
    }

    /**
     * Returns the alignment along the y axis.  This specifies how
     * the component would like to be aligned relative to other 
     * components.  The value should be a number between 0 and 1
     * where 0 represents alignment along the origin, 1 is aligned
     * the furthest away from the origin, 0.5 is centered, etc.
     */
    public float getLayoutAlignmentY(Container parent)
    {
    return 0.5f;
    }

    /**
     * Invalidates the layout, indicating that if the layout manager
     * has cached information it should be discarded.
     */
    public void invalidateLayout(Container target) {
    }
                      
    /**
     * Lays out the container argument using this border layout. 
     * <p>
     * This method actually reshapes the components in the specified
     * container in order to satisfy the constraints of this 
     * <code>BorderLayout</code> object. The <code>North</code> 
     * and <code>South</code>components, if any, are placed at 
     * the top and bottom of the container, respectively. The 
     * <code>West</code> and <code>East</code> components are 
     * then placed on the left and right, respectively. Finally, 
     * the <code>Center</code> object is placed in any remaining 
     * space in the middle. 
     * <p>
     * Most applications do not call this method directly. This method 
     * is called when a container calls its <code>doLayout</code> method. 
     * @param   target   the container in which to do the layout.
     * @see     java.awt.Container  
     * @see     java.awt.Container#doLayout()
     * @since   JDK1.0
     */
    public void layoutContainer(Container target)
    {
        int iMaxHeight = ((CalendarPane)target).surveyComponents();
        m_calendarPanel.surveyPanePreferred(iMaxHeight);
    }
    /**
     * Returns a string representation of the state of this border layout.
     * @return    a string representation of this border layout.
     * @since     JDK1.0
     */
    public String toString()
    {
        return getClass().getName();
    }
}
