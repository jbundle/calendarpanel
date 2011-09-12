/*
 * Copyright © 2011 jbundle.org. All rights reserved.
 */
package org.jbundle.thin.base.screen.calendar.sample.site;

import java.applet.Applet;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.net.URL;
import java.util.Calendar;
import java.util.Date;

import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JApplet;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ScrollPaneConstants;
import javax.swing.border.LineBorder;

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
     * For Stand-alone.
     */
    public static void main(String[] args)
    {
        JFrame frame;
        CalendarTestApplet applet = new CalendarTestApplet();
        try {
            frame = new JFrame("Calendar");
            frame.addWindowListener(new AppCloser(frame, applet));
        } catch (java.lang.Throwable ivjExc) {
            frame = null;
            System.out.println(ivjExc.getMessage());
            ivjExc.printStackTrace();
        }
        frame.getContentPane().add(BorderLayout.CENTER, applet);
        Dimension size = applet.getSize();
        if ((size == null) || ((size.getHeight() < 100) | (size.getWidth() < 100)))
            size = new Dimension(640, 400);
        frame.setSize(size);

        applet.init();       // Simulate the applet calls
        frame.setTitle("Sample calendar application");
        applet.start();

        frame.setVisible(true);
    }
    /**
     * Initialize this applet.
     */
    public void init()
    {
        this.getContentPane().setLayout(new BorderLayout());
        JPanel panel = new JPanel();
        this.getContentPane().add(panel);
        this.addSubPanels(panel);
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
        parent.setLayout(new BoxLayout(parent, BoxLayout.Y_AXIS));
        JScrollPane scroller = new JScrollPane(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED, ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED );
        scroller.setOpaque(false);
        scroller.getViewport().setOpaque(false);
        scroller.setPreferredSize(new Dimension(800, 400));
        scroller.setAlignmentX(LEFT_ALIGNMENT);
        scroller.setAlignmentY(TOP_ALIGNMENT);

        CalendarModel model = this.setupTestModel();

        CalendarPanel panel = new CalendarPanel(model, true, null);
        panel.setAlignmentY(TOP_ALIGNMENT);
        
        scroller.setViewportView(panel);
        JPanel scrollerHolder = new JPanel();
        scrollerHolder.add(scroller);
        scrollerHolder.setLayout(new BoxLayout(scrollerHolder, BoxLayout.X_AXIS));
        parent.add(scrollerHolder);
        
        JPanel bottom = new JPanel();
        bottom.setPreferredSize(new Dimension(10, 100));
        parent.add(bottom);
        panel.setAlignmentY(BOTTOM_ALIGNMENT);
        
        bottom.setLayout(new BoxLayout(bottom, BoxLayout.X_AXIS));
        CalendarForm left = new CalendarForm();
        bottom.add(left);
        left.setAlignmentX(LEFT_ALIGNMENT);        
        left.setBorder(new LineBorder(Color.BLACK));
        model.addMySelectionListener(left);
        model.addTableModelListener(left);
        
        JScrollPane middle = new JScrollPane(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED, ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED );
        JTable table = new CalendarTable(model, null, null);
        middle.getViewport().add(table);
        bottom.add(middle);
        middle.setAlignmentX(0);
        middle.setBorder(new LineBorder(Color.BLACK));
        
        CalendarPanel right = new CalendarPanel(model, false, null);
        bottom.add(right);
        right.setAlignmentX(RIGHT_ALIGNMENT);        
        right.setBorder(new LineBorder(Color.BLACK));
        return true;
    }
    /**
     * Cleans up whatever resources are being held.  If the applet is active
     * it is stopped.
     */
    public void destroy()
    {
        super.destroy();
    }

    public CalendarModel setupTestModel()
    {
        CalendarVector model = new CalendarVector(null);

        Calendar calendar = Calendar.getInstance();
        Date lStartTime;
        Date lEndTime;
        calendar.add(Calendar.DATE, 2);
        calendar.set(Calendar.HOUR_OF_DAY, 12);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        lStartTime = calendar.getTime();
        calendar.add(Calendar.DATE, 3);
        lEndTime = calendar.getTime();

        Color colorHotel = new Color(192, 255, 255);
        Color colorSelectHotel = colorHotel.darker();
        Color colorLand = new Color(192, 192, 255);
        Color colorSelectLand = colorLand.darker();
        Color colorAir = new Color(255, 192, 192);
        Color colorSelectAir = colorAir.darker();

        model.addElement(new CalendarProduct(model, lStartTime, lEndTime, "Mandarin Hotel - 3 Nights with lunch", loadImageIcon("images/Hotel.gif", null), loadImageIcon("images/Hotel.gif", null), "Lunch", colorLand, colorSelectLand, 1));
        
        calendar.add(Calendar.DATE, 2);
        lStartTime = calendar.getTime();
        calendar.add(Calendar.HOUR, 2);
        lEndTime = calendar.getTime();
        model.addElement(new CalendarProduct(model, lStartTime, lEndTime, "Doctor's appointment", loadImageIcon("images/Back.gif", null), null, null, colorHotel, colorSelectHotel, 1));

        calendar.add(Calendar.DATE, 3);
        calendar.set(Calendar.HOUR_OF_DAY, 6);
        lStartTime = calendar.getTime();
        calendar.add(Calendar.HOUR, 12);
        lEndTime = calendar.getTime();
        model.addElement(new CalendarProduct(model, lStartTime, lEndTime, "Fishing", loadImageIcon("images/Back.gif", null), loadImageIcon("images/Forward.gif", null), null, colorAir, colorSelectAir, 1));
        
        calendar.add(Calendar.DATE, -7);
        calendar.set(Calendar.HOUR_OF_DAY, 17);
        lStartTime = calendar.getTime();
        calendar.add(Calendar.HOUR, 3);
        lEndTime = calendar.getTime();
        model.addElement(new CalendarProduct(model, lStartTime, lEndTime, "Meet Annie for Dinner", loadImageIcon("images/Back.gif", null), null, "Dinner", colorHotel, colorSelectHotel, 1));

        calendar.add(Calendar.DATE, 1);
        calendar.set(Calendar.HOUR_OF_DAY, 8);
        lStartTime = calendar.getTime();
        calendar.add(Calendar.HOUR, 8);
        lEndTime = calendar.getTime();
        model.addElement(new CalendarProduct(model, lStartTime, lEndTime, "Work on project", loadImageIcon("images/Back.gif", null), loadImageIcon("images/Forward.gif", null), null, colorHotel, colorSelectHotel, 1));

        return model;
    }
    public static ImageIcon loadImageIcon(String filename, String name)
    {
        ClassLoader cl = CalendarTestApplet.class.getClassLoader();
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

    /**
     * AppCloser quits the application when the user closes the window.
     */
    public static class AppCloser extends WindowAdapter
    {
        JFrame frame = null;
        Applet app = null;
        /**
         * Constructor.
         */
        public AppCloser(JFrame frame, Applet app)
        {
            super();
            this.frame = frame;
            this.app = app;
        }
        /**
         * Close the window.
         */
        public void windowClosing(WindowEvent e)
        {
            app.stop();     // Simulate the applet calls
            app.destroy();
            frame.setVisible(false);
            frame.dispose();
            System.exit(0);
        }
    }
}
