package org.jbundle.thin.base.screen.calendar.sample.site;

import java.awt.Dimension;
import java.text.DateFormat;
import java.util.Date;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.TableModel;

import org.jbundle.util.calendarpanel.event.MyListSelectionEvent;
import org.jbundle.util.calendarpanel.event.MyListSelectionListener;
import org.jbundle.util.calendarpanel.model.CalendarItem;
import org.jbundle.util.calendarpanel.model.CalendarModel;
import org.jbundle.util.jcalendarbutton.JCalendarButton;
import org.jbundle.util.jcalendarbutton.JTimeButton;

public class CalendarForm extends JPanel implements TableModelListener, MyListSelectionListener {
    private static final long serialVersionUID = 1L;

    /** Creates new form NewJFrame */
    public CalendarForm() {
        initComponents();
    }

    /** This method is called from within the constructor to
     * initialize the form.
     */
    private void initComponents() {

        this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        
        this.add(Box.createVerticalStrut(10));
        
        JPanel panel1 = new JPanel();
        this.add(panel1);
        panel1.setLayout(new BoxLayout(panel1, BoxLayout.X_AXIS));
        panel1.add(Box.createHorizontalStrut(5));
        JLabel descLabel = new JLabel("Description:");
        panel1.add(descLabel);
        panel1.add(Box.createHorizontalStrut(5));
        descField = new JTextField(30);
        panel1.add(descField);
        descField.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                String description = descField.getText();
                setDescription(description, true);
            }
        });
        JPanel panel = new JPanel();
        panel1.add(panel);
        panel.setPreferredSize(new Dimension(200, 10));
        this.add(Box.createVerticalStrut(5));
        
        JPanel panel2 = new JPanel();
        panel2.setLayout(new BoxLayout(panel2, BoxLayout.X_AXIS));
        panel2.add(Box.createHorizontalStrut(5));
        JLabel startLabel = new JLabel("Start Date:");
        panel2.add(startLabel);
        startField = new JTextField(20);
        panel2.add(Box.createHorizontalStrut(5));
        startField.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                String dateTime = startField.getText();
                setStartTime(dateTime, true);
            }
        });
        panel2.add(startField);
        this.add(panel2);
        startDateButton = new JCalendarButton();
        startDateButton.addPropertyChangeListener(new java.beans.PropertyChangeListener() {
            public void propertyChange(java.beans.PropertyChangeEvent evt) {
                if (evt.getNewValue() instanceof Date)
                    setStartTime((Date)evt.getNewValue(), true);
            }
        });
        panel2.add(startDateButton);
        startTimeButton = new JTimeButton();
        startTimeButton.addPropertyChangeListener(new java.beans.PropertyChangeListener() {
            public void propertyChange(java.beans.PropertyChangeEvent evt) {
                if (evt.getNewValue() instanceof Date)
                    setStartTime((Date)evt.getNewValue(), true);
            }
        });
        panel2.add(startTimeButton);
        panel = new JPanel();
        panel2.add(panel);
        panel.setPreferredSize(new Dimension(200, 10));
        this.add(Box.createVerticalStrut(5));

        JPanel panel3 = new JPanel();
        this.add(panel3);
        panel3.setLayout(new BoxLayout(panel3, BoxLayout.X_AXIS));
        panel3.add(Box.createHorizontalStrut(5));
        JLabel endLabel = new JLabel("End Date:");
        panel3.add(endLabel);
        panel3.add(Box.createHorizontalStrut(5));
        endField = new JTextField(20);
        endField.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                String date = endField.getText();
                setEndTime(date, true);
            }
        });
        panel3.add(endField);
        endDateButton = new JCalendarButton();
        endDateButton.addPropertyChangeListener(new java.beans.PropertyChangeListener() {
            public void propertyChange(java.beans.PropertyChangeEvent evt) {
                if (evt.getNewValue() instanceof Date)
                    setEndTime((Date)evt.getNewValue(), true);
            }
        });
        panel3.add(endDateButton);
        endTimeButton = new JTimeButton();
        endTimeButton.addPropertyChangeListener(new java.beans.PropertyChangeListener() {
            public void propertyChange(java.beans.PropertyChangeEvent evt) {
                if (evt.getNewValue() instanceof Date)
                    setEndTime((Date)evt.getNewValue(), true);
            }
        });
        panel3.add(endTimeButton);
        panel = new JPanel();
        panel3.add(panel);
        panel.setPreferredSize(new Dimension(200, 10));
        
        JPanel panel4 = new JPanel();
        this.add(panel4);
        panel4.setPreferredSize(new Dimension(10, 250));
    }

    /**
     * Validate and set the datetime field on the screen given a date.
     * @param dateTime The datetime object
     */
    public void setDescription(String description, boolean changeModel)
    {
        System.out.println(description + rowIndex);
        if (changeModel)
            if (table != null)
                if (rowIndex >= 0)
            {
                CalendarItem calendarItem = ((CalendarModel)table).getItem(rowIndex);
                if (calendarItem instanceof CalendarProduct)    // Always
                    ((CalendarProduct)calendarItem).setDescription(description);
                ((CalendarModel)table).fireTableRowsUpdated(rowIndex, rowIndex);
            }
    }
    /**
     * Validate and set the datetime field on the screen given a datetime string.
     * @param dateTime The datetime string
     */
    public void setStartTime(String dateString, boolean changeModel)
    {
        Date date = null;
        try {
            if ((dateString != null) && (dateString.length() > 0))
                date = dateTimeFormat.parse(dateString);
        } catch (Exception e)   {
            date = null;
        }
        this.setStartTime(date, changeModel);
    }
    /**
     * Validate and set the datetime field on the screen given a date.
     * @param dateTime The datetime object
     */
    public void setStartTime(Date dateTime, boolean changeModel)
    {
        String dateString = "";
        if (dateTime != null)
            dateString = dateTimeFormat.format(dateTime);
        startField.setText(dateString);
        startDateButton.setTargetDate(dateTime);
        startTimeButton.setTargetDate(dateTime);

        if (changeModel)
            if (table != null)
                if (rowIndex >= 0)
            {
                ((CalendarModel)table).getItem(rowIndex).setStartDate(dateTime);
                ((CalendarModel)table).fireTableRowsUpdated(rowIndex, rowIndex);
            }
    }
    /**
     * Validate and set the datetime field on the screen given a datetime string.
     * @param dateTime The datetime string
     */
    public void setEndTime(String dateTimeString, boolean changeModel)
    {
        Date dateTime = null;
        try {
            if ((dateTimeString != null) && (dateTimeString.length() > 0))
                dateTime = dateTimeFormat.parse(dateTimeString);
        } catch (Exception e)   {
            dateTime = null;
        }
        this.setEndTime(dateTime, changeModel);
    }
    /**
     * Validate and set the datetime field on the screen given a date.
     * @param dateTime The datetime object
     */
    public void setEndTime(Date dateTime, boolean changeModel)
    {
        String dateTimeString = "";
        if (dateTime != null)
            dateTimeString = dateTimeFormat.format(dateTime);
        endField.setText(dateTimeString);
        endDateButton.setTargetDate(dateTime);
        endTimeButton.setTargetDate(dateTime);
        
        if (changeModel)
            if (table != null)
                if (rowIndex >= 0)
            {
                ((CalendarModel)table).getItem(rowIndex).setEndDate(dateTime);
                ((CalendarModel)table).fireTableRowsUpdated(rowIndex, rowIndex);
            }
    }

    // Variables declaration
    private JTextField descField;
    private JTextField endField;
    private JTextField startField;
    private JCalendarButton startDateButton;
    private JTimeButton startTimeButton;
    private JCalendarButton endDateButton;
    private JTimeButton endTimeButton;

    public static DateFormat timeFormat = DateFormat.getTimeInstance(DateFormat.SHORT);
    public static DateFormat dateTimeFormat = DateFormat.getDateTimeInstance(DateFormat.MEDIUM, DateFormat.SHORT);

    TableModel table = null;
    int rowIndex = -1;
    @Override
    public void selectionChanged(MyListSelectionEvent evt) {
        table = null;
        rowIndex = -1;
        if (evt.getType() == MyListSelectionEvent.SELECT)
        {
            table = evt.getModel();
            rowIndex = evt.getRow();
            updateForm();
        }
    }

    @Override
    public void tableChanged(TableModelEvent evt) {
        if ((evt.getType() == TableModelEvent.UPDATE)
            || (evt.getType() == TableModelEvent.INSERT))
        {
            table = (TableModel)evt.getSource();
            rowIndex = evt.getLastRow();
            updateForm();
        }
    }
    
    protected void updateForm()
    {
        if (rowIndex >= 0)
        {
            String desc = table.getValueAt(rowIndex, CalendarModel.DESCRIPTION).toString();
            descField.setText(desc);
            Date startTime = (Date)table.getValueAt(rowIndex, CalendarModel.START_TIME);
            setStartTime(startTime, false);
            Date endTime = ((CalendarModel)table).getItem(rowIndex).getEndDate();
            setEndTime(endTime, false);
        }
    }

}

