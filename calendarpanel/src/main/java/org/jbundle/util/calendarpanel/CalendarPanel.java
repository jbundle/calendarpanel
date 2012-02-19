/*
 * Copyright © 2012 jbundle.org. All rights reserved.
 */
package org.jbundle.util.calendarpanel;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Event;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.text.DateFormat;
import java.text.FieldPosition;
import java.util.Calendar;
import java.util.Date;
import java.util.Vector;

import javax.swing.ImageIcon;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.KeyStroke;
import javax.swing.Popup;
import javax.swing.PopupFactory;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.SwingWorker;
import javax.swing.border.BevelBorder;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.AbstractTableModel;

import org.jbundle.util.calendarpanel.event.MyListSelectionEvent;
import org.jbundle.util.calendarpanel.event.MyListSelectionListener;
import org.jbundle.util.calendarpanel.model.CalendarConstants;
import org.jbundle.util.calendarpanel.model.CalendarItem;
import org.jbundle.util.calendarpanel.model.CalendarModel;
import org.jbundle.util.calendarpanel.util.CalendarCache;
import org.jbundle.util.calendarpanel.util.PaneBorder;


public class CalendarPanel extends JComponent
    implements TableModelListener, ActionListener, PropertyChangeListener, MyListSelectionListener
{
    private static final long serialVersionUID = 1L;

    public static final String DATE = "date";
    /**
     * This keeps labels from doing "Nam...".
     */
    public static Border m_borderLabel = new EmptyBorder(new Insets(0, 0, 0, -200));
    /**
     * For icons.
     */
    public static Border m_borderEmpty = new EmptyBorder(new Insets(0, 0, 0, 0));
    public static Border m_paneBorder = new PaneBorder(BevelBorder.LOWERED);
    public static Border m_paneSelectedBorder = new PaneBorder(BevelBorder.RAISED);
    /**
     * Constants.
     */
    public static final long KMS_IN_A_DAY = 24 * 60 * 60 * 1000;
    public static final int PREFERRED_HEIGHT = 75;
    public static final int PREFERRED_WIDTH = 75;
    public static final int DEFAULT_ROWS_IN_BOX = 4;
    /**
     * Display meals?
     */
    protected boolean m_bMeals = true;
    /**
     * Display icons?
     */
    protected boolean m_bIcons = true;
    /**
     * Display desc?
     */
    protected boolean m_bDescriptions = true;
    /**
     * First line to place descriptions (use 0 to overwrite date line).
     */
    protected int m_iFirstLine = 1;
    /**
     * This never changes.
     */
    protected Dimension m_dimPaneMinimum = new Dimension(CalendarPanel.PREFERRED_WIDTH, CalendarPanel.PREFERRED_HEIGHT);
    protected Dimension m_dimPanePreferred = new Dimension(CalendarPanel.PREFERRED_WIDTH, CalendarPanel.PREFERRED_HEIGHT);
    /**
     * Used to see if the height changed since last time.
     */
    protected int m_iLastPanelHeight = 0;
    /**
     * Starting height of a description line.
     */
    protected int m_iRowHeight = 3;
    protected FontMetrics m_fmLabel = null;
    
    protected boolean m_bReSetupCalendar = false;
    protected Date m_dateFirst = null;
    protected Date m_dateLast = null;
    /**
     * The default date to add into any survey (usually the current selection).
     */
    protected Date m_dateDefault = null;
    /**
     * The starting time of the first box on the screen.
     */
    protected Date m_dateFirstBox = null;
    /**
     * The largest possible time in the current calendar.
     */
    protected Calendar m_calendarLastBox = Calendar.getInstance();
    /**
     * Month cache (to see if you have to display another month panel).
     */
    protected String m_strMonth = null;
    /**
     * Scratch calendar.
     */
    protected Calendar m_calendar = Calendar.getInstance();
    /**
     * The grid bag for the panel(s).
     */
    protected GridBagLayout m_gridbag = null;
    /**
     * The current grid bag constrains (remember to reset them on a new screen).
     */
    protected GridBagConstraints m_constraints = null;
    /**
     * A scratch full date formatter.
     */
    protected DateFormat m_df = DateFormat.getDateInstance(DateFormat.FULL);
    /**
     * Scratch string buffer.
     */
    protected StringBuffer m_sb = new StringBuffer();

    protected CalendarModel m_model = null;
    
    protected Vector<CalendarCache> m_modelCache = null;
    /**
     * The optional background image.
     * NOTE: By default the background is transparent, so the background come from the parent, although
     * if you set this to the parent's background, underlined labels will be able to merge this image
     * when they display a highlighted entry.
     */
    protected ImageIcon backgroundImage = null;
    /**
     * Pane width for CalendarCache.
     */
    private int m_iPaneWidth = 0;
    /**
     * If you use a shared layout.
     */
    private CalendarPaneLayout m_layoutShared = null;
    /**
     * Status listener.
     */
    protected StatusListener statusListener = null;

    /**
     * Called to start the applet.  You never need to call this directly; it
     * is called when the applet's document is visited.
     */
    public CalendarPanel()
    {
        super();
    }
    /**
     * Called to start the applet.  You never need to call this directly; it
     * is called when the applet's document is visited.
     */
    public CalendarPanel(CalendarModel model, boolean bBigCalendar, ImageIcon imageIcon)
    {
        this();
        this.init(model, bBigCalendar, imageIcon);
    }
    /**
     * Initialize this class.
     * @param backgroundImage 
     */
    public void init(CalendarModel model, boolean bBigCalendar, ImageIcon backgroundImage)
    {
    	this.backgroundImage = backgroundImage;
    	
        this.resetConstraints();
        this.setBigCalendar(bBigCalendar);

        this.setFont(new Font("Helvetica", Font.PLAIN, 14));
        this.setLayout(m_gridbag);

        this.setModel(model, false);    // Set the model, but don't notify the listeners yet
        this.setOpaque(false);      // By default, see thru

        m_bReSetupCalendar = true;      // On next layout, set up the calendar

        this.addComponentListener(new ComponentAdapter()
        {
            public void componentResized(ComponentEvent e)
            {
                super.componentResized(e);  // Mouse clicked in this screen
                checkForRelayout(null, null);       // See if I have to re-layout this screen
            }
        });
        this.addMouseListener(new MouseAdapter()
        {
            public void mouseClicked(MouseEvent e)
            {
                super.mouseClicked(e);
                panelMouseClicked(e); // Mouse clicked in this screen
            }
        });
        // HACK - This has to be the stupidest way to figure when shift is up or down... but
        this.addKeyListener(new KeyAdapter()
        {
            public void keyPressed(KeyEvent e)
            {
                super.keyPressed(e);
                if (e.getKeyCode() == KeyEvent.VK_SHIFT)
                    bShifted = true;
            }
            public void keyReleased(KeyEvent e)
            {
                super.keyReleased(e);
                if (e.getKeyCode() == KeyEvent.VK_SHIFT)
                    bShifted = false;
            }
        });
        this.registerKeyboardAction(this, CalendarConstants.DELETE,
            KeyStroke.getKeyStroke(KeyEvent.VK_DELETE, 0), JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
        this.registerKeyboardAction(this, CalendarConstants.DELETE,
            KeyStroke.getKeyStroke(KeyEvent.VK_DELETE, ActionEvent.CTRL_MASK), JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
        this.registerKeyboardAction(this, CalendarConstants.SELECT_ALL,
            KeyStroke.getKeyStroke(KeyEvent.VK_A, Event.CTRL_MASK), JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
        this.registerKeyboardAction(this, CalendarConstants.UP,
            KeyStroke.getKeyStroke(KeyEvent.VK_UP, 0), JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
        this.registerKeyboardAction(this, CalendarConstants.DOWN,
            KeyStroke.getKeyStroke(KeyEvent.VK_DOWN, 0), JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
        this.registerKeyboardAction(this, CalendarConstants.RIGHT,
            KeyStroke.getKeyStroke(KeyEvent.VK_RIGHT, 0), JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
        this.registerKeyboardAction(this, CalendarConstants.LEFT,
            KeyStroke.getKeyStroke(KeyEvent.VK_LEFT, 0), JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
    }
    /**
     * Free this panel.
     */
    public void free()
    {
        if (m_popup != null)
            m_popup.hide();
        m_popup = null;
        m_popupComponent = null;
        for (int i = this.getCacheCount(false) - 1; i >= 0; i--)
        {
            CalendarCache calCache = this.getCacheItem(i);
            if (calCache != null)
                calCache.free();    // Here is the selected item
        }
        Component component = null;
        // Panel deselection
        for (int iComp = 0; iComp < this.getComponentCount(); iComp++)
        {
            component = this.getComponent(iComp);
            if (component instanceof CalendarPane)
                ((CalendarPane)component).free();
        }
    }
    private boolean bShifted = false;
    public boolean isShifted()
    {
        return bShifted;
    }
    /**
     * Set the model.
     */
    public void setModel(CalendarModel model, boolean bNotifyListeners)
    {
        m_model = model;
        model.addTableModelListener(this);      // Tell the Model that I am the View to be notified of changes
        model.addMySelectionListener(this);     // Tell the Model that I am the View to be notified of selection changes
    
        m_dateFirst = model.getStartDate();
        m_dateLast = model.getEndDate();
        m_dateFirst = model.getSelectDate();
        // pend(don) Tell the calendar that the model has changed, so all items can be updated!
    }
    /**
     * Initialize this calendar for a big or small calendar.
     */
    public void setBigCalendar(boolean bBigCalendar)
    {
        if (bBigCalendar)
        {
            m_bMeals = true;    // Display meals?
            m_bIcons = true;    // Display icons?
            m_bDescriptions = true;   // Display desc?
            m_iFirstLine = 1;   // First line to place descriptions (use 0 to overwrite date line)
            m_dimPaneMinimum = new Dimension(CalendarPanel.PREFERRED_WIDTH, CalendarPanel.PREFERRED_HEIGHT);    // This never changes
            m_dimPanePreferred = new Dimension(CalendarPanel.PREFERRED_WIDTH, CalendarPanel.PREFERRED_HEIGHT);
        }
        else
        {
            m_bMeals = false; // Display meals?
            m_bIcons = false; // Display icons?
            m_bDescriptions = false;    // Display desc?
            m_iFirstLine = 0;   // First line to place descriptions (use 0 to overwrite date line)
            m_dimPaneMinimum = new Dimension(20, 20); // This never changes
            m_dimPanePreferred = new Dimension(20, 20);
        }
        m_iRowHeight = 3;
//+     m_iLastPanelHeight = m_dimPanePreferred.height;
    }
    /**
     * Set up a calendar panel that spans this range.
     * With the following parameters: Minimum of 4 weeks, Max of 52 weeks.
     * Start on the first day of the week.
     * Day of week headings. Month/year headings on month breaks and at start.
     */
    public synchronized void setupCalendar(Date dateFirst, Date dateLast, boolean bSurveyInThread)
    {
        // First, get rid of any old components
        for (int i = 0; i < this.getCacheCount(false); i++)
        {
            CalendarCache calCache = this.getCacheItem(i);
            calCache.removeComponents();
        }
        for (int iComp = this.getComponentCount() - 1; iComp >= 0; iComp--)
        {
            this.remove(this.getComponent(iComp));
        }
        this.resetConstraints();
        SurveyWorker worker = null;
        if ((bSurveyInThread) && (dateFirst == null) && (dateLast == null))
        {
            worker = new SurveyWorker();
            if (dateFirst == null)
                dateFirst = new Date();
            if (dateLast == null)
                dateLast = dateFirst;
        }
        else
        {
            if (dateFirst == null)
                dateFirst = this.surveyModelDates(true);
            if (dateLast == null)
                dateLast = this.surveyModelDates(false);
        }
        dateFirst = this.truncDate(dateFirst);
        dateLast = this.truncDate(dateLast);
        m_dateFirstBox = this.getFirstDateInCalendar(dateFirst);
        
        long lDaysInTour = (dateLast.getTime() - m_dateFirstBox.getTime()) / KMS_IN_A_DAY + 1;
        if (lDaysInTour < 28)
        {   // Make sure a minimum of 4 weeks are displayed
            m_calendar.setTime(m_dateFirstBox);
            m_calendar.add(Calendar.DATE, +28);
            dateLast = m_calendar.getTime();
        }
        m_calendarLastBox.setTime(dateLast);

        this.addMonthBox(+6);

        this.addDaysOfWeek();

        // Start with the first date of the tour
        int iWeekCount = 1;
        for (int iWeek = 0; iWeek < 52; iWeek++)
        {
            m_calendar.setTime(m_dateFirstBox);
            m_calendar.add(Calendar.DATE, (iWeek + 1) * 7);
            if (m_calendar.after(m_calendarLastBox))
                break;
            iWeekCount++;
        }
        // Calc the correct last box on the screen.
        m_calendarLastBox.setTime(m_dateFirstBox);
        m_calendarLastBox.add(Calendar.DATE, iWeekCount * 7);
        m_calendarLastBox.add(Calendar.MINUTE, -1);
        
        for (int iWeek = 0; iWeek < iWeekCount; iWeek++)
        {
            this.addThisWeek(iWeek);
            if (iWeek + 1 != iWeekCount)    // Anywhere but the last row
                this.addMonthBox(iWeek * 7 + 7 + 6);
        }
        
        this.addFillBox();
        
        if (worker != null)
            worker.execute();
    }
    /**
     * Special class to do the survey work in a thread.
     * @author don
     */
    class SurveyWorker extends SwingWorker<String,Object>
    {
        Date dateFirst = null;
        Date dateLast = null;
        
        public String doInBackground()
        {
//+            BaseApplet applet = BaseApplet.getSharedInstance();
//+            if (applet != null)
            	if (dateFirst == null)
            		dateFirst = surveyModelDates(true);
            if (dateLast == null)
                dateLast = surveyModelDates(false);
            return null;
        }
        /**
         * Called on the event dispatching thread (not on the worker thread)
         * after the <code>construct</code> method has returned.
         */
        public void done()
        {
            setupCalendar(dateFirst, dateLast, false);
            invalidate();
            validate();
            checkForRelayout(dateFirst, dateLast);
        }
    }
    /**
     * Reset the constraints to the starting value.
     */
    public void resetConstraints()
    {
        if (m_gridbag == null)
            m_gridbag = new GridBagLayout();
        m_constraints = new GridBagConstraints();
        m_constraints.fill = GridBagConstraints.BOTH;
        m_constraints.weightx = 1.0;
        m_constraints.anchor = GridBagConstraints.CENTER;
        m_constraints.gridwidth = 7;
        m_constraints.gridwidth = GridBagConstraints.REMAINDER; //end row
   
    }
    /**
     * Add a panel for the month that spans all 7 columns.
     */
    public void addMonthBox(int iDayOffsetFromFirst)
    {
        Date dateTarget = getDaysOffset(m_dateFirstBox, iDayOffsetFromFirst);
        String strYear = this.getDateString(dateTarget, DateFormat.YEAR_FIELD);
        String strMonth = this.getDateString(dateTarget, DateFormat.MONTH_FIELD);
        if (strMonth.equals(m_strMonth))
            return;
        m_strMonth = strMonth;
        m_constraints.fill = GridBagConstraints.BOTH;
        m_constraints.weightx = 1.0;
        m_constraints.anchor = GridBagConstraints.CENTER;
        m_constraints.gridwidth = 7;
        m_constraints.gridwidth = GridBagConstraints.REMAINDER; //end row
        this.makeLabel(this, strMonth + " " + strYear);
    }
    /**
     * Add seven panels that describe the days of the week (Mon, tues, etc).
     */
    public void addDaysOfWeek()
    {
        m_constraints.fill = GridBagConstraints.BOTH;
        m_constraints.weightx = 1.0;
        m_constraints.anchor = GridBagConstraints.CENTER;
        m_constraints.gridwidth = 1;

        // Start with the first date of the tour
        for (int iDayOfWeek = 0; iDayOfWeek < 7; iDayOfWeek++)
        {
            Date dateTarget = getDaysOffset(m_dateFirstBox, iDayOfWeek);
            String strWeek = this.getDateString(dateTarget, DateFormat.DAY_OF_WEEK_FIELD);
            if (m_bDescriptions == false)
                if (strWeek != null)
                    if (strWeek.length() > 0)
                        strWeek = strWeek.substring(0, 1);  // Small calendar = first letter only.
            if (iDayOfWeek == 6)
                m_constraints.gridwidth = GridBagConstraints.REMAINDER; //end row
            this.makeLabel(this, strWeek);
        }
    }
    /**
     * Add the seven panels for each day in this week.
     * @param iWeek Week to add (0 = first week).
     */
    public void addThisWeek(int iWeek)
    {
        m_constraints.fill = GridBagConstraints.BOTH;
        m_constraints.weightx = 1.0;
        m_constraints.gridwidth = 1; // Continue
        m_constraints.anchor = GridBagConstraints.EAST;
        for (int iDayOfWeek = 0; iDayOfWeek < 7; iDayOfWeek++)
        {
            if (iDayOfWeek == 6)
                m_constraints.gridwidth = GridBagConstraints.REMAINDER; //end row
            this.makePanel(iDayOfWeek + (iWeek * 7));
        }
    }
    /**
     * Add a fill box to the bottom of the calendar.
     * This insures that the calendar will be at the top of the screen.
     */
    public void addFillBox()
    {
        m_constraints.fill = GridBagConstraints.BOTH;
        m_constraints.weightx = 1.0;
        m_constraints.weighty = 1.0;
        m_constraints.gridwidth = GridBagConstraints.REMAINDER; //end row
        m_constraints.gridheight = GridBagConstraints.REMAINDER; //end column
        JPanel newpanel = new JPanel();
        newpanel.setOpaque(false);
        newpanel.setPreferredSize(new Dimension(0, 0));
        m_gridbag.setConstraints(newpanel, m_constraints);
        this.add(newpanel);
    }
    /**
     * Create and add a panel for this day.
     */
    protected JPanel makePanel(int iDayOffsetFromFirst)
    {
        JPanel newpanel = new CalendarPane(iDayOffsetFromFirst, CalendarPanel.m_paneBorder);

        if (m_layoutShared == null)
            m_layoutShared = new CalendarPaneLayout(this);
        newpanel.setLayout(m_layoutShared);     // All panels share the same layout (same width, same height)

        m_gridbag.setConstraints(newpanel, m_constraints);
        this.add(newpanel);
        return newpanel;
    }
    /**
     * Create and add a label with this description.
     */
    protected void makeLabel(JComponent panel, String name)
    {
        JLabel newpanel = new JLabel(name, SwingConstants.CENTER)
        {
            private static final long serialVersionUID = 1L;

            public Dimension getPreferredSize()
            {   // This keeps these labels from affecting the pane sizes (otherwize pane can't shrink beyond "Wednesday")
                return new Dimension(0, super.getPreferredSize().height);
            }
        };
        m_gridbag.setConstraints(newpanel, m_constraints);
        newpanel.setForeground(Color.black);    // Remember... Labels are opaque
        newpanel.setBorder(CalendarPanel.m_paneBorder);
        panel.add(newpanel);
    }
    /**
     * Given the first date of the tour, get the first date of that week.
     */
    public Date getFirstDateInCalendar(Date dateTarget)
    {
        // Now get the first box on the calendar
        int iFirstDayOfWeek = m_calendar.getFirstDayOfWeek();
        m_calendar.setTime(dateTarget);
        int iTargetDayOfWeek = m_calendar.get(Calendar.DAY_OF_WEEK);
        int iOffset = -Math.abs(iTargetDayOfWeek - iFirstDayOfWeek);
        m_calendar.add(Calendar.DATE, iOffset);

        m_calendar.set(Calendar.HOUR_OF_DAY, 0);
        m_calendar.set(Calendar.MINUTE, 0);
        m_calendar.set(Calendar.SECOND, 0);
        m_calendar.set(Calendar.MILLISECOND, 0);

        return m_calendar.getTime();
    }
    /**
     * Convert this data to a string (using the supplied format).
     */
    public String getDateString(Date dateTarget, int iDateFormat)
    {
        m_sb.setLength(0);
        FieldPosition fieldPosition = new FieldPosition(iDateFormat);
        String string = null;
        string = m_df.format(dateTarget, m_sb, fieldPosition).toString();
        int iBegin = fieldPosition.getBeginIndex();
        int iEnd = fieldPosition.getEndIndex();
        string = string.substring(iBegin, iEnd);
        return string;
    }
    /**
     * Add this offset to the date and return the result.
     */
    public Date getDaysOffset(Date dateTarget, int iOffsetDays)
    {
        m_calendar.setTime(dateTarget);
        m_calendar.add(Calendar.DATE, iOffsetDays);
        dateTarget = m_calendar.getTime();
        return dateTarget;
    }
    /**
     * Round the date to the first second of the day.
     */
    public Date truncDate(Date date)
    {
        m_calendar.setTime(date);

        m_calendar.set(Calendar.HOUR_OF_DAY, 0);
        m_calendar.set(Calendar.MINUTE, 0);
        m_calendar.set(Calendar.SECOND, 0);
        m_calendar.set(Calendar.MILLISECOND, 0);

        return m_calendar.getTime();
    }
    /**
     * Get the date of the upper left hand box.
     */
    public Date getFirstDate()
    {
        return m_dateFirstBox;
    }
    /**
     * If the calendar is marked for rebuild, then re-setup the calendar panes.
     * This happens if the start or end date changes, or an out-of-bounds date is entered.
     */
    public void doLayout()
    {
        if (m_bReSetupCalendar == true)     // On next layout, set up the calendar
        {
            m_bReSetupCalendar = false;
            this.setupCalendar(m_dateFirst, m_dateLast, false); // Todo(don) Fix this, so this will run in a thread. 
        }
        super.doLayout();
    }
    /**
     * Display meal icons?.
     */
    public ImageIcon getMealIcon()
    {
        return m_model.getHeaderIcon();
    }
    /**
     * Display meal icons?
     */
    public boolean isMeals()
    {
        return m_bMeals;
    }
    /**
     * Display icons?
     */
    public boolean isIcons()
    {
        return m_bIcons;
    }
    /**
     * Display desc?
     */
    public boolean isDescriptions()
    {
        return m_bDescriptions;
    }
    /**
     * First line to place descriptions (use 0 to overwrite date line).
     */
    public int getFirstLine()
    {
        return m_iFirstLine;
    }
    /**
     * Mouse was clicked in this panel.
     * Find the correct pane and select it.
     */
     public void panelMouseClicked(MouseEvent e)
     {
        Component component = this.getComponentAt(e.getPoint());
        this.selectAll(false, true);
        boolean bFocusHere = true;
        if (component instanceof CalendarPane)
        {
            CalendarPane pane = (CalendarPane)component;
            int iX = e.getPoint().x - component.getBounds().x;
            Date dateTarget = pane.convertXToDate(iX, component.getBounds().width);
            this.firePropertyChange(DATE, null, dateTarget);  // New target date
            
            bFocusHere = !createPopup(e.getPoint());
        }
        if (bFocusHere)
            this.requestFocus();        // Send key events here
    }
     /**
      * 
      */
    protected Component m_popupComponent = null;
    protected Popup m_popup = null;
    /**
     * Set the component to popup when the user clicks a blank calendar pane.
     * @param popupComponent
     */
    public void setPopupComponent(Component popupComponent)
    {
        m_popupComponent = popupComponent;
    }
    /**
     * Create a popup window using the set component.
     * @param location The location to set the window.
     * @return True if the popup exists.
     */
    public boolean createPopup(Point location)
    {
        if (m_popupComponent == null)
            return false;
        if (m_popup != null)
            m_popup.hide();
        Component owner = this;
        SwingUtilities.convertPointToScreen(location, owner);
        this.fitComponentInWindow(location, m_popupComponent, owner);
        m_popup = PopupFactory.getSharedInstance().getPopup(owner, m_popupComponent, location.x, location.y);
        
        m_popup.show();
        m_popupComponent.requestFocus();
        m_popupComponent.addFocusListener(new HideOnLoseFocus(m_popup, m_popupComponent));
        if (m_popupComponent instanceof PopupPanel)
            ((PopupPanel)m_popupComponent).setPopupParent(m_popup);
        return true;
    }
    /**
     * Fix this (screen) location so this component completely fits in this owner.
     * This is used to keep popup windows from being created as heavy component, thereby being difficult to track loss of focus.
     * @param location
     * @param component
     * @param owner
     */
    public void fitComponentInWindow(Point location, Component component, Component owner)
    {
        Component top = owner;
        while (top.getParent() != null)
        {
            top = top.getParent();
        }
        Rectangle rect = new Rectangle(location.x, location.y, component.getWidth(), component.getHeight());
        if ((rect.getWidth() == 0) && (rect.getWidth() == 0))
            rect.setSize(150, 230); // Guess
        if (top.getBounds().contains(rect))
            return; // Good!
        if (location.x + rect.getWidth() > top.getX() + top.getWidth())
            location.x = (int) (top.getX() + top.getWidth() - rect.getWidth() - 5);
        if (location.y + rect.getHeight() > top.getY() + top.getHeight())
            location.y = (int) (top.getY() + top.getHeight() - rect.getHeight() - 5);
    }
    /**
     * Hide this popup when I loose focus.
     * @author don
     */
    class HideOnLoseFocus extends FocusAdapter
    {
        Popup m_popup = null;
        Component m_component = null;
        
        public HideOnLoseFocus(Popup popup, Component component)
        {
            super();
            m_popup = popup;
            m_component = component;
        }
        /**
         * Invoked when a component loses the keyboard focus.
         */
        public void focusLost(FocusEvent e)
        {
//System.out.println("-----------------LOST FOCUS");
            m_component.removeFocusListener(this);
            Component comp = e.getOppositeComponent();
            while (comp != null)
            {
                if (comp == m_component)
                {     // If I am losing to a component I own, don't hide until that component looses focus.
                    m_component = e.getOppositeComponent();
                    m_component.addFocusListener(this);
                    return;
                }
                comp = comp.getParent();
            }
            m_popup.hide();
        }        
    }
    /**
     * De-select the currently selected item(s).
     */
    public void selectAll(boolean bSelect, boolean bNotifyModel)
    {
        Component component = null;
        // Panel deselection
        for (int iComp = 0; iComp < this.getComponentCount(); iComp++)
        {
            component = this.getComponent(iComp);
            if (component instanceof CalendarPane)
                ((CalendarPane)component).select(CalendarPanel.m_paneBorder); // Is there a better way then to de-select all?
        }
        // Item de-selection
        for (int i = 0; i < this.getCacheCount(true); i++)
        {
            this.getCacheItem(i).setSelected(bSelect);
        }
        int iMode = bSelect ? MyListSelectionEvent.SELECT : MyListSelectionEvent.DESELECT;
        if (bNotifyModel)
            this.getModel().fireTableRowSelected(this, -1, iMode);  // This will notify any listeners (except me) that there is no selection
    }
    /**
     * De-select the currently selected item(s).
     */
    public void selectPane(int iNewDayoffsetPane)
    {
        Component component = null;
        // Panel deselection
        for (int iComp = 0; iComp < this.getComponentCount(); iComp++)
        {
            component = this.getComponent(iComp);
            if (component instanceof CalendarPane)
            {
                CalendarPane pane = (CalendarPane)component;
                if (pane.getDateOffset() == iNewDayoffsetPane)
                {
                    this.selectAll(false, true);      // Clear current selection
                    pane.select(CalendarPanel.m_paneSelectedBorder);
                }
            }
        }
    }
    /**
     * De-select the currently selected item(s).
     */
    public CalendarPane getSelectedPane()
    {
        Component component = null;
        for (int iComp = 0; iComp < this.getComponentCount(); iComp++)
        {
            component = this.getComponent(iComp);
            if (component instanceof CalendarPane)
            {
                if (((CalendarPane)component).getBorder() == CalendarPanel.m_paneSelectedBorder)
                    return (CalendarPane)component;
            }
        }
        return null;
    }
    /**
     * Get the currently selected item.
     */
    public CalendarCache getSelectedItem()
    {
        if (m_modelCache == null)
            return null;
        for (int i = 0; i < this.getCacheCount(true); i++)
        {
            CalendarCache calCache = this.getCacheItem(i);
            if (calCache != null) if (calCache.isSelected())
                return calCache;    // Here is the selected item
        }
        return null;
    }
    /**
     * These items have changed, update them on the screen.
     */
    public void selectionChanged(MyListSelectionEvent evt)
    {
        int iRowStart = evt.getRow();
        int iType = evt.getType();
        switch (iType)
        {
        case MyListSelectionEvent.SELECT:
        case MyListSelectionEvent.ADD_SELECT:
        case MyListSelectionEvent.DESELECT:
        case MyListSelectionEvent.CONTENT_SELECT:
        case MyListSelectionEvent.CONTENT_CLICK:    // Select + click
            if (iRowStart != -1)
            {
                if (iType != MyListSelectionEvent.ADD_SELECT)
                    this.selectAll(false, false);   // Do NOT notify the model, since I am getting this message from the model
                CalendarCache calCache = this.getCacheItem(iRowStart);
                boolean bSelect = true;
                if (calCache != null)
                {
                    calCache.setSelected(bSelect);
                }
            }
            break;
        }
    }
    /**
     * These items have changed, update them on the screen.
     */
    public void tableChanged(TableModelEvent event)
    {
        int iColumn = event.getColumn();
        int iRowStart = event.getFirstRow();
        int iRowEnd = event.getLastRow();
        if (iRowEnd == Integer.MAX_VALUE)
            if (iColumn != TableModelEvent.ALL_COLUMNS)
                return;     // It is common for a JTable to ask that everything in a column be updated (don't do it!).
        int iType = event.getType();
        if ((iRowStart == 0) && (iRowEnd == Integer.MAX_VALUE) && (iColumn == TableModelEvent.ALL_COLUMNS) && (iType == TableModelEvent.UPDATE))
        {   // Special case - Reset the model!
            // First, go through the cache and clear every item.
            for (int index = this.getCacheCount(false) - 1; index >= 0; index--)
            {   // Table rows start at 0.
                    // The proper way to do this is to remove() the model item after a get().
                CalendarCache calCache = this.getCacheItem(index);
                if (calCache != null)
                {
                    // Remove the item from the screen
                    m_modelCache.remove(index);
                    calCache.removeComponents();
                }
            }
            m_modelCache = null;
            iRowStart = 0;
            iRowEnd = this.getCacheCount(true) - 1;
        }
        switch (iType)
        {
        case TableModelEvent.UPDATE:
        case TableModelEvent.INSERT:
            Date dateSurveyStart = null;
            Date dateSurveyEnd = null;
            for (int index = iRowStart; index <= iRowEnd; index++)
            {
                if (iType == TableModelEvent.INSERT)
                {   // You can only insert the next record.
                    if (index != this.getCacheCount(true))
                        break;
                }
                else
                { // Update must be a current record.
                    if (index >= this.getCacheCount(true))
                        break;
                }
                CalendarCache calCache = this.getCacheItem(index);
                // First, remove the old item from the screen
                calCache.removeComponents();
                // Now, re-cache using the new information
                calCache.cacheItem(m_model, calCache.getIndex()); // Re-cache the information
                CalendarItem item = calCache.getItem();
                if (item != null)
                {
                    Date dateItemStart = item.getStartDate();
                    if (dateItemStart == null)
                        continue;
                    if ((dateSurveyStart == null) || (dateSurveyStart.after(dateItemStart)))
                        dateSurveyStart = dateItemStart;
                    Date dateItemEnd = item.getEndDate();
                    if (dateItemEnd == null)
                        dateItemEnd = dateItemStart;
                    if (dateItemEnd == null)
                        continue;
                    if ((dateSurveyEnd == null) || (dateSurveyEnd.after(dateItemEnd)))
                        dateSurveyEnd = dateItemEnd;
                }
                // Next, re-add all the components for this item to each pane
                for (int iIndex = 0; iIndex < this.getComponentCount(); iIndex++)
                {
                    Component component = this.getComponent(iIndex);
                    if (component instanceof CalendarPane)
                    {
                        CalendarPane pane = (CalendarPane)component;
                        pane.addItemLabel(calCache);
                        pane.select(null);
                        Date date = this.getDaysOffset(this.getFirstDate(), pane.getDateOffset());
     
                        if ((item != null) && (item.getStartDate() != null))
                            pane.addMealLabel(calCache, item.getMealDesc(date), -1);    // -1 Means find a location to place this desc
                    }
                }
                this.surveyPanePreferred((calCache.getLine(null) + 1) * this.getRowHeight() - 1);
                this.checkForRelayout(dateSurveyStart, dateSurveyEnd);
            }
            break;
        case TableModelEvent.DELETE:    // This is called from the 
            if (iRowEnd >= this.getCacheCount(true))
                iRowEnd = this.getCacheCount(true) - 1;     // Note cache is 0 based, rows are 0 based
            for (int index = iRowEnd; index >= iRowStart; index--)
            {   // Table rows start at 0.
                // You should rarely be asked to delete an entry (in my model) (Only on reset model).
                    // The proper way to do this is to remove() the model item after a get().
                CalendarCache calCache = this.getCacheItem(index);
                if (calCache != null)
                {
                    // Remove the item from the screen
                    m_modelCache.remove(index);
                    calCache.removeComponents();
                }
                for (int i = index; i < m_modelCache.size(); i++)
                { // Fix the cached index numbers of the rest of the entries
                    calCache = this.getCacheItem(i);
                    if (calCache != null)
                        if (calCache.getIndex() != -1)
                            calCache.setIndex(i);
                }
            }
            break;
        default:
        }
    }
//-------------------
// The following methods deal with the model
//-------------------
    /**
     * Number of cache items.
     */
    public int getCacheCount(boolean bCreateIfNew)
    {
        if (m_modelCache == null)
        { // First time, cache all info.
            if (!bCreateIfNew)
                return 0;
            int iCount = m_model.getRowCount();
            m_modelCache = new Vector<CalendarCache>();
            m_model.removeTableModelListener(this); // Eliminate echo
            for (int i = 0; i < iCount; i++)
            {
                CalendarCache cacheItem = this.getCacheItem(i);
                if (cacheItem == null)
                    break;
                if (iCount + 1 == i)
                    iCount = m_model.getRowCount();     // This may have changed.
            }
            m_model.addTableModelListener(this);
        }
        return m_modelCache.size();
    }
    /**
     * Retrieve the cached values at this location.
     * @param Zero based index of the item to retrieve.
     * @return The calendarcache item at this location.
     */
    public CalendarCache getCacheItem(int iIndex)
    {
        if (m_modelCache == null)
            m_modelCache = new Vector<CalendarCache>();
        if (iIndex < m_modelCache.size())
        {
            // Check to make sure the cache matches
            return (CalendarCache)m_modelCache.elementAt(iIndex);
        }
        else if (iIndex == m_modelCache.size())
        {
            CalendarItem item = m_model.getItem(iIndex);
            if (item == null)
                return null;
            CalendarCache calCache = new CalendarCache(this, m_model, iIndex);
            if (iIndex < m_modelCache.size())  // HACK synchronization problem
                return this.getCacheItem(iIndex);   // If cache was updated while I messed around, return the correct item
            m_modelCache.addElement(calCache);
            return calCache;
        }
        else // if (i > m_modelCache.size())
        { // Need to populate the modelcache - BE CAREFUL this is recursive code
            for (int i = m_modelCache.size(); i <= iIndex; i++)
            {
                CalendarCache cacheItem = this.getCacheItem(i);
                if (cacheItem == null)
                    break;
                if (iIndex + 1 == i)
                    iIndex = m_model.getRowCount();     // This may have changed.
                if (i == iIndex)
                    return cacheItem;
            }
        }
        return null;    // ERROR
    }
    /**
     * Get the index of this calendar cache entry.
     */
    public int getItemIndex(CalendarCache calCache)
    {
        int iRowIndex = -1;     // default = No selection
        for (int i = 0; i < this.getCacheCount(true); i++)
        {
            if (this.getCacheItem(i) == calCache)
                iRowIndex = i;
        }
        return iRowIndex;   // Notify the model of the selection
    }
    /**
     * Get the CalendarModel.
     */
    public CalendarModel getModel()
    {
        return m_model;
    }
    /**
     * Reset all the line numbers of the items in the model.
     */
    public void resetAllLines()
    {
        if ((m_model == null) || (m_modelCache == null))
            return;
        for (int i = 0; i < m_modelCache.size(); i++)
        {
            CalendarCache calCache = this.getCacheItem(i);
            if (calCache == null)
                break;
            calCache.setLine(-1);
        }
    }
    /**
     * Check to see if the pane width changed. If so, clear all the line numbers from the cache items.
     * //pending(don) Is there a better place to put this?
     */
    public void checkPaneWidth(int iWidth)
    {
        if (m_iPaneWidth != iWidth)
        {
            this.resetAllLines();
            m_iPaneWidth = iWidth;
        }
    }
    /**
     * Get the date of the upper left hand box.
     */
    public Date surveyModelDates(boolean bFirstDate)
    {
        Date date = null;
        Date dateCompare = null;
        if (m_model == null)
            return date;
        for (int i = 0; i < this.getCacheCount(true); i++)
        {
            CalendarCache calCache = this.getCacheItem(i);
            if (calCache == null)
                break;
            if (bFirstDate)
            {
                dateCompare = calCache.getItem().getStartDate();
                if ((date == null) || (dateCompare.before(date)))
                    date = dateCompare;
            }
            else
            {
                dateCompare = calCache.getItem().getEndDate();
                if (dateCompare == null)
                    dateCompare = calCache.getItem().getStartDate();
                if ((date == null) || (dateCompare.after(date)))
                    date = dateCompare;
            }
        }
        if (date == null)
            date = m_dateDefault;
        if (date == null)
            date = new Date();
        return date;
    }
    /**
     * Get the font metrics for a standard label.
     */
    public FontMetrics getFontMetrics()
    {
        return m_fmLabel;
    }
    /**
     * Get the font metrics for a standard label.
     */
    public void setFontMetrics(FontMetrics fm)
    {
        m_fmLabel = fm;
    }
    /**
     * Get the row height for a standard label.
     */
    public int getRowHeight()
    {
        return m_iRowHeight;
    }
    /**
     * Set the row height if this is greater than the current row height.
     */
    public void surveyRowHeight(int iRowHeight)
    {
        m_iRowHeight = Math.max(m_iRowHeight, iRowHeight);
    }
    /**
     * If this panel is marked for re-layout, then validate it.
     */
    public void checkForRelayout(Date dateSurveyStart, Date dateSurveyEnd)
    {
        boolean bReLayout = false;
        if (m_iLastPanelHeight < m_dimPanePreferred.height)
            bReLayout = true;
        if ((dateSurveyStart != null) && (dateSurveyEnd != null))
            if (this.getComponentCount() > 0)
        {
                // Get the first and last dates on this screen
            Date dateLastBox = null;
            if (m_calendarLastBox != null)  // Always
                dateLastBox = m_calendarLastBox.getTime();

            if (((m_dateFirstBox != null) && (dateSurveyStart.before(m_dateFirstBox)))
                || ((dateLastBox != null) && (dateSurveyEnd.after(dateLastBox))))
            {
                m_bReSetupCalendar = true;
                bReLayout = true;
            }
        }
        if (bReLayout)
        {   // After resizing out, I found out I need to layout-out again, so invalidate.
            m_iLastPanelHeight = m_dimPanePreferred.height;
            this.setLayout(this.getLayout());
            this.invalidate();
            this.validate();
        }
    }
    /**
     * Set the panel height if this is greater than the max panel height.
     * @return boolean true if height changed.
     */
    public boolean surveyPanePreferred(int iMaxHeight)
    {
        if (iMaxHeight > m_dimPanePreferred.height)
        {
            m_dimPanePreferred.height = iMaxHeight + 5;     // Add a 5 pixel buffer at the bottom
            return true;
        }
        return false;
    }
    /**
     * Set the panel height if this is greater than the max panel height.
     */
    public Dimension getPanePreferred()
    {
        return m_dimPanePreferred;
    }
    /**
     * Set the panel height if this is greater than the max panel height.
     */
    public Dimension getPaneMinimum()
    {
        return m_dimPaneMinimum;
    }
    /**
     * Process this command.
     */
    public void actionPerformed(ActionEvent e)
    {
        String strCommand = e.getActionCommand();
        if (strCommand.equalsIgnoreCase(CalendarConstants.DELETE))
        {
            CalendarCache calCache = this.getSelectedItem();
            if (calCache != null)
            {
                int iIndex = this.getItemIndex(calCache);
                CalendarItem item = calCache.getItem();
                if (item != null)
                    item.remove();      // delete the item (item should notify the listeners)
                AbstractTableModel model = (AbstractTableModel)this.getModel();
                calCache.cacheItem(null, -1); // No valid item
                model.fireTableRowsUpdated(iIndex, iIndex);   // Notify the models to get rid of the visual
            }
        }
        else if (strCommand.equalsIgnoreCase("Select all"))
            this.selectAll(true, true);
        else
        {
            CalendarPane pane = this.getSelectedPane();
            Date dateTarget = null;
            if (pane != null)
                dateTarget = pane.getThisDate();
            if (dateTarget != null)
            {
                int iOffset = 0;
                if (strCommand.equalsIgnoreCase("Up"))
                    iOffset =  -7;  // - seven days
                else if (strCommand.equalsIgnoreCase("Down"))
                    iOffset =  +7;  // - seven days
                else if (strCommand.equalsIgnoreCase("Right"))
                    iOffset =  +1;  // - seven days
                else if (strCommand.equalsIgnoreCase("Left"))
                    iOffset =  -1;  // - one days
                if (iOffset != 0)
                {
                    m_calendar.setTime(dateTarget);
                    m_calendar.add(Calendar.DATE, iOffset);
                    dateTarget = m_calendar.getTime();
                    this.firePropertyChange(DATE, null, dateTarget);  // New target date
                }
            }
        }
    }
    /** This method gets called when a bound property is changed.
     * @param evt A PropertyChangeEvent object describing the event source
     *    and the property that has changed.
     */
    public void propertyChange(PropertyChangeEvent evt)
    {
        if (DATE.equalsIgnoreCase(evt.getPropertyName()))
        {   // Somewhere, the user entered/clicked a date
            Date dateTarget = (Date)evt.getNewValue();
            this.selectAll(false, true);
            if (dateTarget != null)
            {
                Component component = this.getComponentAtDate(dateTarget);
                if (component instanceof CalendarPane)
                {
                    CalendarPane pane = (CalendarPane)component;
                    pane.select(CalendarPanel.m_paneSelectedBorder);
                }
                else if (component == null)
                {
                    m_dateDefault = dateTarget;
                    this.checkForRelayout(dateTarget, dateTarget);
                }
            }
        }
    }
    /** This method gets called when a bound property is changed.
     * @param evt A PropertyChangeEvent object describing the event source
     *    and the property that has changed.
     */
    public CalendarPane getComponentAtDate(Date dateTarget)
    {
        Date dateFirst = this.getFirstDate();
        int iOffset = (int)((dateTarget.getTime() - dateFirst.getTime()) / KMS_IN_A_DAY);
        int iCount = 0;
        for (int iComp = 0; iComp < this.getComponentCount(); iComp++)
        {
            Component component = this.getComponent(iComp);
            if (component instanceof CalendarPane)
            {
                if (iOffset == iCount)
                    return (CalendarPane)component;
                iCount++;
            }
        }
        return null;
    }
    /**
     * Set the optional background image.
     * NOTE: By default the background is transparent, so the background come from the parent, although
     * if you set this to the parent's background, underlined labels will be able to merge this image
     * when they display a highlighted entry.
     */
    public ImageIcon getBackgroundImage()
    {
    	return backgroundImage;
    }
    /**
     * Change the status display.
     * @param status
     */
    public void setStatusText(String status)
    {
    	if (statusListener != null)
    		statusListener.setStatusText(status);
    }
    /**
     * Change the status display.
     * @param status
     */
    public void setStatusListener(StatusListener listener)
    {
    	statusListener = listener;
    }
}
