/*
 * Copyright © 2011 jbundle.org. All rights reserved.
 */
package org.jbundle.util.calendarpanel.sample.simple;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.URL;
import java.util.Calendar;
import java.util.Date;

import javax.swing.AbstractAction;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JApplet;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ScrollPaneConstants;

import org.jbundle.util.calendarpanel.CalendarPanel;
import org.jbundle.util.calendarpanel.model.CalendarModel;


public class CalendarTestApplet extends JApplet
{
    private static final long serialVersionUID = 1L;

    /**
     *  OrderEntry Class Constructor.
     */
    public CalendarTestApplet()
    {
        super();
    }
    /**
     *  OrderEntry Class Constructor.
     */
    public CalendarTestApplet(String args[])
    {
        this();
//        this.init(args);
    }
    /**
     * Initializes the applet.  You never need to call this directly; it is
     * called automatically by the system once the applet is created.
     */
    public void init()
    {
        super.init();
    }
    /**
     * Called to start the applet.  You never need to call this directly; it
     * is called when the applet's document is visited.
     */
    public void start()
    {
        super.start();
    }
    /**
     * Add any applet sub-panel(s) now.
     */
    public boolean addSubPanels(Container parent)
    {
        parent.setLayout(new BoxLayout(parent, BoxLayout.X_AXIS));
        JScrollPane scroller = new JScrollPane(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED, ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED );
        scroller.setOpaque(false);
        scroller.getViewport().setOpaque(false);
        scroller.setPreferredSize(new Dimension(800, 400));
        scroller.setAlignmentX(LEFT_ALIGNMENT);
        scroller.setAlignmentY(TOP_ALIGNMENT);

        CalendarModel model = this.setupTestModel();

//        ImageIcon backgroundImage = this.getBackgroundImage();	// Calendar panel is transparent, but this helps with rendering see-thru components 
        CalendarPanel panel = new CalendarPanel(model, true, null);
//        panel.setStatusListener(new TaskCalendarStatusHandler(this));
        
        scroller.setViewportView(panel);
        parent.add(scroller);
        
        ActionListener listener = new MyAction();
//        panel.setPopupComponent(new JPopupPanel(this, listener));
        return true;
    }
    class MyAction extends AbstractAction
    {

        @Override
        public void actionPerformed(ActionEvent e) {
        //+    System.out.println("Action: " + e);
            
        }
        
    }
    /**
     * Called to stop the applet.  This is called when the applet's document is
     * no longer on the screen.  It is guaranteed to be called before destroy()
     * is called.  You never need to call this method directly
     */
    public void stopTask()
    {
//        super.stopTask();
    }
    /**
     * Cleans up whatever resources are being held.  If the applet is active
     * it is stopped.
     */
    public void destroy()
    {
        super.destroy();
    }
    /**
     * For Stand-alone.
     */
    public static void main(String[] args)
    {
//        BaseApplet.main(args);
        CalendarTestApplet applet = null;//CalendarTestApplet.getSharedInstance();
        if (applet == null)
            applet = new CalendarTestApplet(args);
        JFrame frame = new JFrame("Calendar");
        frame.setBounds(0, 0, 400, 400);
//        frame.getContentPane().setLayout(new BorderLayout());
        frame.getContentPane().add(applet);
        applet.getContentPane().setLayout(new BorderLayout());
        JPanel panel = new JPanel();
        applet.getContentPane().add(panel);
        applet.addSubPanels(panel);
        frame.setVisible(true);
    }

    public CalendarModel setupTestModel()
    {
        CalendarVector model = new CalendarVector(null);
        JApplet applet = this;

        Calendar m_calendar = Calendar.getInstance();
        Date lStartTime;
        Date lEndTime;
        m_calendar.set(1998, Calendar.JUNE, 11, 2, 0, 0);
        lStartTime = m_calendar.getTime();
        m_calendar.set(1998, Calendar.JUNE, 13, 0, 0, 0);
        lEndTime = m_calendar.getTime();

        Color colorHotel = new Color(192, 255, 255);    // HACK Light blue
        Color colorSelectHotel = colorHotel.darker();
        Color colorLand = new Color(192, 192, 255);   // HACK Light blue
        Color colorSelectLand = colorLand.darker();
        Color colorAir = new Color(255, 192, 192);  // HACK Light blue
        Color colorSelectAir = colorAir.darker();
        
        
        model.addElement(new CalendarProduct(model, lStartTime, lEndTime, "Mandarin Hotel - 3 Nights $200.00", this.loadImageIcon("tour/buttons/Hotel.gif", null), this.loadImageIcon("tour/buttons/Hotel.gif", null), "M", colorHotel, colorSelectHotel, 1));
        
        m_calendar.set(1998, Calendar.JUNE, 9, 12, 0, 0);
        lStartTime = m_calendar.getTime();
        m_calendar.set(1998, Calendar.JUNE, 9, 15, 0, 0);
        lEndTime = m_calendar.getTime();
        model.addElement(new CalendarProduct(model, lStartTime, lEndTime, "Airport/Hotel Transfer - SIC", this.loadImageIcon("tour/buttons/Land.gif", null), null, "T", colorLand, colorSelectLand, 1));

        m_calendar.set(1998, Calendar.JUNE, 10, 20, 0, 0);
        lStartTime = m_calendar.getTime();
        m_calendar.set(1998, Calendar.JUNE, 12, 20, 0, 0);
        lEndTime = m_calendar.getTime();
        model.addElement(new CalendarProduct(model, lStartTime, lEndTime, "Flight", this.loadImageIcon("tour/buttons/Air.gif", null), null, "F", colorAir, colorSelectAir, 1));
        
        m_calendar.set(1998, Calendar.JUNE, 10, 3, 0, 0);
        lStartTime = m_calendar.getTime();
        m_calendar.set(1998, Calendar.JUNE, 15, 12, 0, 0);
        lEndTime = m_calendar.getTime();
        model.addElement(new CalendarProduct(model, lStartTime, lEndTime, "Dusit Thani Hotel", this.loadImageIcon("images/tour/buttons/Hotel.gif", null), this.loadImageIcon("tour/buttons/Hotel.gif", null), "D", colorHotel, colorSelectHotel, 1));

        m_calendar.set(1998, Calendar.JUNE, 12, 12, 0, 0);
        lStartTime = m_calendar.getTime();
        m_calendar.set(1998, Calendar.JUNE, 15, 12, 0, 0);
        lEndTime = m_calendar.getTime();
        model.addElement(new CalendarProduct(model, lStartTime, lEndTime, "Orchid Hotel", this.loadImageIcon("tour/buttons/Hotel.gif", null), this.loadImageIcon("tour/buttons/Hotel.gif", null), "D", colorHotel, colorSelectHotel, 1));

        return model;
    }
    public ImageIcon loadImageIcon(String filename, String name)
    {
        ClassLoader cl = this.getClass().getClassLoader();
        URL url = null;
        try {
            url = cl.getResource(filename);
        } catch (Exception e) {
            e.printStackTrace();    // Keep trying
        }
        if (url != null)
            return new ImageIcon(url);
        return null;
    }
}
