package org.jbundle.util.calendarpanel;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Insets;
import java.awt.Rectangle;
import java.awt.dnd.DropTarget;
import java.text.DateFormat;
import java.util.Date;
import java.util.TooManyListenersException;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;

import org.jbundle.util.calendarpanel.dnd.CalendarPaneDropTargetListener;
import org.jbundle.util.calendarpanel.model.CalendarConstants;
import org.jbundle.util.calendarpanel.util.CalendarCache;
import org.jbundle.util.calendarpanel.util.JUnderlinedLabel;


/**
 * A single pane on a calendar.
 */
public class CalendarPane extends JPanel
{
    private static final long serialVersionUID = 1L;

    protected int m_iDateOffset = -1;
    
    protected int m_iHeightSurvey = 0;
    protected Dimension m_dimLastTime = null; // Dimension on the last survey

    public static final Color DATE_TEXT_COLOR = Color.black;
    public static final Color ITEM_TEXT_COLOR = Color.blue;          // Description color
    public static final Color ITEM_LIGHT_TEXT_COLOR = Color.gray;     // Description after the end-icon
    public static final String BLANK = "";

    public static final String m_kstrTextTip = "Click to see the details, drag to change the start date"; // To resource file!
    public static final String m_kstrStartIconTip = "Drag this icon to change the start date";
    public static final String m_kstrEndIconTip = "Drag this icon to change the end date";
    
    /**
     * A pane that represents a single day in the calendar.
     */
    public CalendarPane()
    {
        super();
    }
    /**
     * A pane that represents a single day in the calendar.
     */
    public CalendarPane(int iDateOffset, Border border)
    {
        this();
        this.init(iDateOffset, border);
    }
    /**
     * A pane that represents a single day in the calendar.
     */
    public void init(int iDateOffset, Border border)
    {
        m_iDateOffset = iDateOffset;

        if (border != null)
            this.setBorder(border);
        this.setForeground(Color.black);
        this.setOpaque(false);
        // I accept calendar item drops (either product or detail items)
        DropTarget dt = new DropTarget();
        this.setDropTarget(dt);
        try {
            dt.addDropTargetListener(new CalendarPaneDropTargetListener(null));
        } catch (TooManyListenersException ex) {
            ex.printStackTrace();
        }
    }
    /**
     * Free this pane.
     */
    public void free()
    {
        this.setDropTarget(null);
    }
    /**
     * Called from the layout manager to setup this panel's components.
     * Actually, I remove all the components from the pane and add them back.
     */
    public int surveyComponents()
    {
        if ((m_dimLastTime != null)
                && (m_dimLastTime.width == this.getBounds().width)
                && (m_dimLastTime.height == this.getBounds().height))
            return m_iHeightSurvey;   // No changes in layout
        if (m_dimLastTime == null)
            m_dimLastTime = new Dimension(0, 0);
        m_dimLastTime.setSize(this.getBounds().width, this.getBounds().height);
        this.getCalendarPanel().checkPaneWidth(m_dimLastTime.width);        // Reset the model lines if the width changed
        m_iHeightSurvey = 0;
        for (int iIndex = this.getComponentCount() - 1; iIndex >= 0; iIndex--)
        {
            Component component = this.getComponent(iIndex);
            if (component instanceof JUnderlinedLabel)
                ((JUnderlinedLabel)component).free();
        }
        this.removeAll(); // Remove any leftovers
        // First, add the date label
        this.addDateLabel();
        
        int iXNextMeal = 0;   // Initialize
        // Next line
        for (int i = 0; i < this.getCalendarPanel().getCacheCount(true); i++)
        {
            CalendarCache itemCache = this.getCalendarPanel().getCacheItem(i);
            this.addItemLabel(itemCache);
            iXNextMeal = this.addMealLabel(itemCache, itemCache.getItem().getMealDesc(this.getThisDate()), iXNextMeal);
        }

        return m_iHeightSurvey;
    }
    /**
     * Add the date label. The date label is the first line in a calendar pane.
     * It always contains the day of the month and often contains meals or other information.
     */
    public void addDateLabel()
    {
        Rectangle rect = this.getBounds();
        Insets insets = this.getInsets();
        JLabel label = null;
        String strName = this.getDayDesc();
        this.add(label = new JLabel(strName));
        label.setForeground(CalendarPane.DATE_TEXT_COLOR);
        if (this.getCalendarPanel().getFontMetrics() == null)
            this.getCalendarPanel().setFontMetrics(label.getFontMetrics(label.getFont()));
        int iStringWidth = this.getCalendarPanel().getFontMetrics().stringWidth(strName);
        CalendarPanel calendarPanel = this.getCalendarPanel();
        int iLabelHeight = this.getCalendarPanel().getFontMetrics().getHeight();        // If you don't overwrite the label, use it for the std row height
        if (calendarPanel.getFirstLine() > 0)
            this.getCalendarPanel().surveyRowHeight(iLabelHeight);      // If you don't overwrite the label, use it for the std row height
        label.setBounds(new Rectangle(rect.width - insets.right - iStringWidth - 1, 1 + insets.top, rect.width, iLabelHeight));
    }
    /**
     * Add the meal label.
     * @param iXNextMeal Next location to place the meal (if -1, find a free location).
     */
    public int addMealLabel(CalendarCache itemCache, String strMeal, int iXNextMeal)
    {
        if ((strMeal == null) || (strMeal.length() == 0))
            return iXNextMeal;
        Insets insets = this.getInsets();
        CalendarPanel calendarPanel = this.getCalendarPanel();
        if (!calendarPanel.isMeals())
            return iXNextMeal;
        // Are there any meals?
        JLabel label = null;
        Component component = this.getComponentAt(insets.left + 1, insets.top + 1);
        if (!(component instanceof JUnderlinedLabel))
        {
            if (iXNextMeal == -1)
                iXNextMeal = 0;
            label = this.addIcon(null, calendarPanel.getMealIcon(), insets.left + 1, insets.top + 1, null, null, CalendarConstants.NOT_DRAGGABLE, null);
            iXNextMeal += label.getWidth() + insets.left + 2;
        }
        else if (iXNextMeal == -1) if (component != null)
        {   // Special case - find a place to stick this meal
            iXNextMeal += 1 + component.getBounds().width + insets.left + 2;
            int iStringLength = this.getCalendarPanel().getFontMetrics().stringWidth(strMeal);
            int iXStart = -1;
            for (; iXNextMeal < this.getBounds().width; iXNextMeal++)
            {
                component = this.getComponentAt(iXNextMeal, insets.top + 1);
                if (component != this)
                    iXStart = -1;   // Keep trying
                { // Good, there is space at this location
                    if (iXStart == -1)
                        iXStart = iXNextMeal;
                    else
                    {
                        if (iXNextMeal - iXStart > iStringLength)
                        {
                            iXNextMeal = iXStart + 1;
                            break;      // Yeah, there is space here
                        }
                    }
                }
            }
        }
        label = this.addLabel(itemCache, strMeal, iXNextMeal, 1 + insets.top, true, -1, null, null, CalendarConstants.TEXT, null);

        label.setForeground(CalendarPane.ITEM_TEXT_COLOR);
        iXNextMeal += label.getWidth() + 1;
        return iXNextMeal;
    }
    /**
     *  Add the label(s) for this item that fall into this panel.
     *  1. Start Icon (at start date)
     *  2. Description up to end date (opt)
     *  3. End Icon (opt)
     *  4. Description up to end of desc (opt if desc is displayed)
     */
    public void addItemLabel(CalendarCache itemCache)
    {
        Rectangle rect = this.getBounds();
        Insets insets = this.getInsets();
        JLabel label = null;
        long lEndDate = this.getThisDate().getTime() + CalendarPanel.KMS_IN_A_DAY - 1;
        if ((itemCache.getItem() == null)
            || (itemCache.getItem().getStartDate() == null))
                return;     // No Start date -> doesn't go on calendar
        if (itemCache.getItem().getStartDate().getTime() >= lEndDate)
            return;     // Starts after this day
        int iXStartService = this.convertDateToX(itemCache.getItem().getStartDate(), rect.width);
        Date dateEnd = itemCache.getItem().getEndDate();
        if (dateEnd == null)
            dateEnd = itemCache.getItem().getStartDate();
        int iXEndService = this.convertDateToX(dateEnd, rect.width);
        CalendarPanel calendarPanel = this.getCalendarPanel();
        String strDescription = BLANK;
        if (calendarPanel.isDescriptions())
            strDescription = itemCache.getItem().getDescription();
        int iWidthDescription = this.getCalendarPanel().getFontMetrics().stringWidth(strDescription);
    // First, add the (optional) start icon
        iXStartService += insets.left;
        ImageIcon image = null;
        ImageIcon imageEnd = null;
        if (calendarPanel.isIcons())
        {
            image = itemCache.getItem().getIcon(CalendarConstants.START_ICON);
            if (image != null) if (image.getIconWidth() < 0)
                image = null;
            imageEnd = itemCache.getItem().getIcon(CalendarConstants.END_ICON);
            if (imageEnd != null) if (imageEnd.getIconWidth() < 0)
                imageEnd = null;
        }
        int iWidthService = iXEndService - iXStartService;
        int iWidthImages = (image == null ? 0 : image.getIconWidth()) + (imageEnd == null ? 0 : imageEnd.getIconWidth());
        if ((iWidthDescription + iWidthImages < 5) && (iWidthService < 5))
            iWidthService = 5;      // Minimum length in pixels
        int iTotalWidth = Math.max(iWidthDescription + iWidthImages, iWidthService);
        int iWidthStartDesc = Math.max(0, iWidthService - iWidthImages);
        int iLine = itemCache.getLine(null);
        if (iLine == -1)    // If first time, survey this item
            iLine = itemCache.getLine(this.convertXToDate(iXStartService + iTotalWidth, rect.width));
        iLine = iLine + (calendarPanel.getFirstLine() - 1);     // Start on line
        int y = iLine * this.getCalendarPanel().getRowHeight();
        if (iXStartService + iTotalWidth < 0)
            return;
        if (iXStartService > rect.width)
            return;
        Color colorHighlight = itemCache.getItem().getHighlightColor();
        Color colorSelect = itemCache.getItem().getSelectColor();
        if (image != null) if (iXStartService + image.getIconWidth() >= 0)
        {
            label = this.addIcon(itemCache, image, iXStartService, insets.top + y, colorHighlight, colorSelect, CalendarConstants.START_ICON, m_kstrStartIconTip);
            ((JUnderlinedLabel)label).setStartRound(true);  // Round the first label
        }
        if (image != null)
            iXStartService += image.getIconWidth();
        // Make sure the underlined label has all the "flashing/cycling" icons that the model contains.
        if (image != null) if (label != null)
        {
            for (int iIndex = CalendarConstants.START_ICON + 1; iIndex < CalendarConstants.END_ICON; iIndex++)
            {
                ImageIcon icon = itemCache.getItem().getIcon(iIndex);
                if (icon != null)
                {
                    ((JUnderlinedLabel)label).addIcon(icon, iIndex - CalendarConstants.START_ICON);
                }
            }
        }

    // Next, add the (optional) description highlighted to show it is active
        if (iWidthStartDesc > 0) if (iXStartService + iWidthStartDesc >= 0)
            label = this.addLabel(itemCache, strDescription, iXStartService, insets.top + y, true, iWidthStartDesc, colorHighlight, colorSelect, CalendarConstants.TEXT, m_kstrTextTip);
        if (iWidthStartDesc > 0)
            iXStartService += iWidthStartDesc;
    // Next, add the (optional) end icon
        if (iXStartService <= rect.width)
            if (imageEnd != null) if (iXStartService + image.getIconWidth() >= 0)
                label = this.addIcon(itemCache, imageEnd, iXStartService, insets.top + y, colorHighlight, colorSelect, CalendarConstants.END_ICON, m_kstrEndIconTip);
        if (imageEnd != null) 
            iXStartService += imageEnd.getIconWidth();
        if (label != null)
            if (iXStartService <= rect.width)
                ((JUnderlinedLabel)label).setEndRound(true);  // Round the last label
    // Last, add the (optional) end description
        if (iXStartService <= rect.width)
            if (iWidthDescription - iWidthStartDesc > 0)
        {
            label = this.addLabel(itemCache, strDescription, iXStartService, insets.top + y, true, iWidthDescription - iWidthStartDesc, null, null, CalendarConstants.TEXT, m_kstrTextTip);
            label.setBorder(new EmptyBorder(new Insets(0, -iWidthStartDesc, 0, -200)));
            iXStartService += iWidthDescription - iWidthStartDesc;
        }
    }
    /**
     *  Check to see if this point is larger then the greatest point so far
     */
    public void surveyThis(int iY)
    {
        m_iHeightSurvey = Math.max(m_iHeightSurvey, iY);
    }
    /**
     *  Convert this date to an x value.
     */
    public int convertDateToX(Date date, int iPaneWidth)
    {
        return (int)((((double)(date.getTime() - this.getThisDate().getTime())) / CalendarPanel.KMS_IN_A_DAY) * iPaneWidth);
    }
    /**
     *  Convert this x value to a date.
     */
    public Date convertXToDate(int x, int iPaneWidth)
    {
        return new Date((long)(((((double)x) / iPaneWidth) * CalendarPanel.KMS_IN_A_DAY) + this.getThisDate().getTime()));
    }
    /**
     *  Add this icon to this pane.
     */
    public JLabel addIcon(CalendarCache itemCache, ImageIcon image, int x, int y, Color colorHighlighted, Color colorSelect, int iComponentType, String strToolTip)
    {
    	JUnderlinedLabel label = null;
        this.add(label = new JUnderlinedLabel(itemCache, image, true, colorHighlighted, colorSelect, iComponentType, strToolTip));
        if (this.getCalendarPanel() != null)
        	label.setBackgroundImage(this.getCalendarPanel().getBackgroundImage());
        label.setBorder(CalendarPanel.m_borderEmpty);
        if (image != null)
        {
            this.getCalendarPanel().surveyRowHeight(image.getIconHeight());
            label.setBounds(new Rectangle(x, y, image.getIconWidth(), this.getCalendarPanel().getRowHeight()));
        }
        this.surveyThis(y + this.getCalendarPanel().getRowHeight());
        return label;
    }
    /**
     *  Add this description to the pane.
     */
    public JLabel addLabel(CalendarCache itemCache, String strDesc, int x, int y, boolean bSelectable, int iStringLength, Color colorHighlighted, Color colorSelect, int iComponentType, String strToolTip)
    {
    	JUnderlinedLabel label = null;
        if (y < 6) if (colorHighlighted != null)
            colorHighlighted = colorHighlighted.darker();
        if (y < 6) if (colorSelect != null)
            colorSelect = colorSelect.darker();
        this.add(label = new JUnderlinedLabel(itemCache, strDesc, bSelectable, colorHighlighted, colorSelect, iComponentType, strToolTip));
        if (this.getCalendarPanel() != null)
        	label.setBackgroundImage(this.getCalendarPanel().getBackgroundImage());
        if (iStringLength == -1)
            iStringLength = this.getCalendarPanel().getFontMetrics().stringWidth(label.getText());
        label.setBorder(CalendarPanel.m_borderLabel);
        label.setBounds(new Rectangle(x, y, iStringLength, this.getCalendarPanel().getRowHeight()));
        this.surveyThis(y + this.getCalendarPanel().getRowHeight());
        return label;
    }
    /**
     *  Get calendar panel.
     */
    public CalendarPanel getCalendarPanel()
    {
        return (CalendarPanel)this.getParent();
    }
    /**
     *  Get calendar panel.
     */
    public Date getThisDate()
    {
        return this.getCalendarPanel().getDaysOffset(this.getCalendarPanel().getFirstDate(), m_iDateOffset);
    }
    /**
     *  Get the day of month to put in the upper right of this pane.
     */
    public String getDayDesc()
    {
        return this.getCalendarPanel().getDateString(this.getThisDate(), DateFormat.DATE_FIELD);
    }
    /**
     *  Change your border to reflect selected or not.
     */
    public void select(Border newBorder)
    {
        Border oldBorder = this.getBorder();
        if (newBorder != null) if (newBorder != oldBorder)
        {
            this.setBorder(newBorder);
            this.repaint();
        }
    }
    /**
     *  Change your border to refelect selected or not.
     */
    public int getDateOffset()
    {
        return m_iDateOffset;
    }
}
