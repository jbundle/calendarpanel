/*
 * Copyright © 2012 jbundle.org. All rights reserved.
 */
package org.jbundle.util.calendarpanel.util;

import java.awt.Color;
import java.awt.Component;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Insets;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.RoundRectangle2D;

import javax.swing.ImageIcon;
import javax.swing.JApplet;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.event.TableModelEvent;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableModel;

import org.jbundle.util.calendarpanel.CalendarPane;
import org.jbundle.util.calendarpanel.dnd.CalendarItemTransferHandler;
import org.jbundle.util.calendarpanel.dnd.CalendarItemTransferable;
import org.jbundle.util.calendarpanel.model.CalendarConstants;

/**
 * The label to put the calendar items into.
 */
public class JUnderlinedLabel extends JLabel
    implements ActionListener, TableCellRenderer
{
    private static final long serialVersionUID = 1L;

    protected static int m_iUnderline = -1;
    protected static FontMetrics m_fm = null;
    
    protected CalendarCache m_itemCache = null;
    protected boolean bSelectable = false;
    protected int m_iStringWidth = 0;
    protected Color m_colorNormal = null;
    protected Color m_colorHighlight = null;
    protected Color m_colorSelect = null;
    
    protected boolean m_bUnderlined = false;
    private int m_iComponentType = CalendarConstants.TEXT;
    private String m_strToolTip = null;
    protected LabelMouseListener m_mouseListener = null;
    protected ImageIcon m_imageBackground = null;
    
    protected boolean m_bStartRound = false;
    protected boolean m_bEndRound = false;
    
    /**
     * Constructor.
     */
    public JUnderlinedLabel(CalendarCache itemCache, String strLabel, boolean bSelectable, Color colorHighlight, Color colorSelect, int iComponentType, String strToolTip)
    {
        super(strLabel);
        this.init(itemCache, strLabel, bSelectable, colorHighlight, colorSelect, iComponentType, null, strToolTip, true);
    }
    /**
     * Constructor.
     */
    public JUnderlinedLabel(CalendarCache itemCache, ImageIcon icon, boolean bSelectable, Color colorHighlight, Color colorSelect, int iComponentType, String strToolTip)
    {
        super(icon);
        this.init(itemCache, null, bSelectable, colorHighlight, colorSelect, iComponentType, icon, strToolTip, true);
    }
    /**
     * Constructor.
     */
    public JUnderlinedLabel(CalendarCache itemCache, String strLabel, boolean bSelectable, Color colorHighlight, Color colorSelect, int iComponentType, String strToolTip, boolean bEnableDnD)
    {
        super(strLabel);
        this.init(itemCache, strLabel, bSelectable, colorHighlight, colorSelect, iComponentType, null, strToolTip, bEnableDnD);
    }
    /**
     * Constructor.
     */
    public JUnderlinedLabel(CalendarCache itemCache, ImageIcon icon, boolean bSelectable, Color colorHighlight, Color colorSelect, int iComponentType, String strToolTip, boolean bEnableDnD)
    {
        super(icon);
        this.init(itemCache, null, bSelectable, colorHighlight, colorSelect, iComponentType, icon, strToolTip, bEnableDnD);
    }
    /**
     * Constructor.
     */
    public void init(CalendarCache itemCache, String strLabel, boolean bSelectable, Color colorHighlight, Color colorSelect, int iComponentType, ImageIcon icon, String strToolTip, boolean bEnableDnD)
    {
        m_itemCache = itemCache;
        m_iComponentType = iComponentType;
        m_strToolTip = strToolTip;
        if (itemCache != null)
            itemCache.addComponent(this);
        if ((colorHighlight != null) || (icon != null))
            this.setForeground(CalendarPane.ITEM_TEXT_COLOR);
        else
            this.setForeground(CalendarPane.ITEM_LIGHT_TEXT_COLOR);
        if (strLabel != null)
        {
            if (m_fm == null)
                m_fm = this.getFontMetrics(this.getFont());
            m_iUnderline = m_fm.getDescent();
        }
        if (bSelectable)
        {
            if (strLabel != null)
                m_iStringWidth = m_fm.stringWidth(strLabel);
            if (icon != null)
                m_iStringWidth = icon.getIconWidth();
        }
        m_colorHighlight = colorHighlight;
        m_colorSelect = colorSelect;
        if (m_colorSelect == null) if (m_colorHighlight != null)
            m_colorSelect = m_colorHighlight.darker();
        // Add a listener to get mouseovers and clicks
        if (bEnableDnD)
        {
            m_mouseListener = new LabelMouseListener(itemCache, this, true);
            this.addMouseListener(m_mouseListener);
            this.addMouseMotionListener(m_mouseListener);
        }
        this.setTransferHandler(new CalendarItemTransferHandler(CalendarItemTransferable.CALENDAR_ITEM_TRANSFER_TYPE));
//x        if (m_imageBackground == null)
//x            m_imageBackground = org.jbundle.thin.base.screen.BaseApplet.getSharedInstance().getBackgroundImage();  // Use the same background image
    }
    /**
     * Get rid of the resources and remove this component.
     */
    public void free()
    {
        if (m_itemCache != null)
            m_itemCache.removeComponent(this);      // Take me off your list
        if (m_mouseListener != null)
            this.removeMouseListener(m_mouseListener);
        m_mouseListener = null;
        m_itemCache = null;
        if (m_timer != null)
            this.removeTimer();
        if (this.getParent() != null) //?
            this.getParent().remove(this);
        m_imageBackground = null;
    }

    static private RoundRectangle2D.Double gClip = new RoundRectangle2D.Double(0, 0, 0, 0, 4, 4);
    
    /**
     * The label to put the calendar items into.
     */
    public void paintComponent(Graphics g)
    {
        Rectangle rectangle = this.getBounds();
        Insets insets = this.getInsets();
        if (m_colorNormal == null)
            m_colorNormal = this.getForeground(); // First time... get text color
        Color colorOld = g.getColor();

        if (rectangle.x < 0)
        {   // Don't paint over the cell borders
            Rectangle rectClip = g.getClipBounds();
            rectClip.x += 2;
            rectClip.width -= 2;
            g.setClip(rectClip);
        }

        if (m_colorHighlight != null)
        {
            g.setColor(m_colorHighlight);
            if (m_itemCache != null) if (m_itemCache.isSelected())
                g.setColor(m_colorSelect);
            if ((m_bStartRound) || (m_bEndRound))
                if (rectangle.height > 8)
            {   // Set a rounded clip area to round the rec.
                Rectangle rectClip = g.getClipBounds();
                int x = rectClip.x;
                int width = rectClip.width;
                int xShift = 4;
                if (rectangle.x < 0)
                    xShift = xShift - 2;
                if (!m_bStartRound)
                {
                    x = x - xShift;
                    width = width + xShift;
                }
                else if (!m_bEndRound)
                {
                    width = width + xShift;
                }
                gClip.setRoundRect(x, rectClip.y, width, rectClip.height, 4, 4);
                g.setClip(gClip);
            }
            g.fillRect(0, 0, rectangle.width, rectangle.height);
            if (m_imageBackground != null) //?? && ((checkImage(m_imageBackground, this) & ERROR) != 0))
            {
                int iOffsetx = 0;
                int iOffsety = 0;
                Point ptThis = this.getLocationOnScreen();
                Component parent = this;
                while ((parent = parent.getParent()) != null)
                {
                    if (parent instanceof JApplet)
                    {
                        Point ptTile = parent.getLocationOnScreen();
                        iOffsetx = ptTile.x - ptThis.x;
                        iOffsety = ptTile.y - ptThis.y;
                        break;
                    }
                }
                for (int y = 0 + iOffsety; y < rectangle.height; y = y + m_imageBackground.getIconHeight())
                {
                    for (int x = 0 + iOffsetx; x < rectangle.width; x = x + m_imageBackground.getIconWidth())
                    {
//+ JDK 1.2                     if (g.hitClip(x, y, m_imageBackground.getIconWidth(), m_imageBackground.getIconHeight()))
                            m_imageBackground.paintIcon(this, g, x, y);
                    }
                }
            }
        }

        if ((m_itemCache != null) && (m_itemCache.isSelected()))
        {
            this.setForeground(Color.black);
            g.setColor(Color.black);        // For the underline
        }
        else
        {
            this.setForeground(m_colorNormal);
            g.setColor(colorOld);
        }
        if (m_bUnderlined) if (m_iUnderline != -1)
            g.drawLine(1, rectangle.height - m_iUnderline + 1, rectangle.width - 1 + insets.left, rectangle.height - m_iUnderline + 1);
        super.paintComponent(g);
        g.setColor(colorOld); // Set the color(s) back.
    }
    /**
     * This item has been selected. Change the view.
     */
    public void setSelected(boolean bSelected)
    {
        this.repaint();
    }
    /**
     * This item has been selected. Change the view.
     */
    public void setUnderlined(boolean bUnderlined)
    {
        m_bUnderlined = bUnderlined;
        this.repaint();
    }
    /**
     * Text/Start or End Icon.
     */
    public int getComponentType()
    {
        return m_iComponentType;
    }
    /**
     * Get the item cache.
     */
    public CalendarCache getItemCache()
    {
        return m_itemCache;
    }
    /**
     * Get the tooltip
     */
    public String getTooltip()
    {
        return m_strToolTip;
    }
    /**
     * If this control is in a JTable, this is how to render it.
     */
    public java.awt.Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column)
    {
        if (m_tableModel == null)
        {
            m_iThisRow = row;
            m_tableModel = table.getModel();
        }
//+     ImageIcon icon = this.getImageIcon(value);
//+     this.setIcon(icon);
        return this;
    }
    protected TableModel m_tableModel = null;
    protected int m_iThisRow = -1;

    private static final int MAX_ICONS = 16;
    private ImageIcon m_rgIcons[] = null;
    private javax.swing.Timer m_timer = null;
    private int m_iCurrentIcon = 0;
    /**
     * Add this icon to the list of icons alternating for this label.
     */
    public void addIcon(ImageIcon icon, int iIndex)
    {
        if (m_rgIcons == null)
        {
            m_rgIcons = new ImageIcon[MAX_ICONS];
            m_rgIcons[0] = (ImageIcon)this.getIcon(); // Always at location 0
        }
        if (iIndex < MAX_ICONS)
            m_rgIcons[iIndex] = icon;
        if (m_timer == null)
            if (this.getIconCount() > 1)
        {
            m_timer = new javax.swing.Timer(500, this);         // Remind me to change graphics every 1/2 second
            m_timer.start();
        }
    }
    /**
     * Remove this icon from the list of icons alternating for this label.
     */
    public void removeIcon(int iIndex)
    {
        if (m_rgIcons != null)
        {
            m_rgIcons[iIndex] = null; // Always at location 0
            if (m_timer != null)
                if (this.getIconCount() <= 1)
                    this.removeTimer();   // No icons left
            if (this.getIconCount() == 0)
                m_rgIcons = null;
        }
    }
    /**
     * Count the icons.
     * @return The icon count.
     */
    public int getIconCount()
    {
        int iCount = 0;
        for (int i = 0; i < MAX_ICONS; i++)
        {
            if (m_rgIcons[i] != null)
                iCount++;
        }
        return iCount;
    }
    /**
     * 0.5 seconds passed, select the item.
     * <p>NOTE: This method does not cycle through the icons, it flashes the START_ICON, then the next
     * icon, then the start again, then the next one in the list.
     * <p>ie., If the count is even, display icon 0; if not, display (icon / 2 + 1).
     */
    public void actionPerformed(ActionEvent e)
    {
        m_iCurrentIcon++;
        if (e.getSource() instanceof javax.swing.Timer)
        {
            if (m_tableModel != null)
            {
                TableModelEvent event = new TableModelEvent(m_tableModel, 0, Integer.MAX_VALUE, m_iThisRow);
                ((AbstractTableModel)m_tableModel).fireTableChanged(event);
            }
            else
            {
                Object objValue = Integer.toString(m_itemCache.getItem().getStatus());  // This is where the value is cached
                ImageIcon icon = this.getImageIcon(objValue);
                if (icon != null)
                {
                    this.setIcon(icon);
                    Rectangle rect = this.getBounds();
                    this.repaint(rect);
                }
            }
        }
    }
    /**
     * If this is the status value, display the correct icon.
     */
    public ImageIcon getImageIcon(Object value)
    {
        int i = 0;
        if (value == null)
            return m_rgIcons[0];
        String strType = value.toString();
        int iType = 0;
        try   {
            iType = Integer.parseInt(strType);
        } catch (NumberFormatException ex)  {
        }

        int iIconCount = 0;
        for (i = 1; i < MAX_ICONS; i++)
        {
            if (m_rgIcons[i] != null)
                if (((1 << i) & iType) != 0)    // Only count icons in this type
                    iIconCount++;
        }
        if (iIconCount == 0)
            iIconCount = 1;
        int iRelIndex;
        if ((iType & 1) == 0)
        {   // Cycle through all the icons
            iRelIndex = m_iCurrentIcon % iIconCount;    // Remainder of division
        }
        else
        {   // Alternate from 0, next, 0, next, etc
            if ((m_iCurrentIcon & 1) == 0)
                iRelIndex = MAX_ICONS;      // Icon 0
            else
            {
                int iIconIndex = m_iCurrentIcon / 2 + 1;
                iRelIndex = iIconIndex % iIconCount;    // Remainder of division
            }
        }
        for (i = 1; i < MAX_ICONS; i++)
        {
            if (m_rgIcons[i] != null)
                if (((1 << i) & iType) != 0)    // Only count icons in this type
                    iRelIndex--;
            if (iRelIndex < 0)
                break;
        }
        if (i >= MAX_ICONS)
            i = 0;

        return m_rgIcons[i];
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
     * 
     * @param bStartRound
     */
    public void setStartRound(boolean bStartRound)
    {
        m_bStartRound = bStartRound;
    }
    /**
     * 
     * @param bEndRound
     */
    public void setEndRound(boolean bEndRound)
    {
        m_bEndRound = bEndRound;
    }
    /**
     * Set the image reference for this label.
     */
    public void setBackgroundImage(ImageIcon imageIcon)
    {
    	this.m_imageBackground = imageIcon;
    }
}
