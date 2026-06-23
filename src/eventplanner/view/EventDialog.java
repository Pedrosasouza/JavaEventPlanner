package eventplanner.view;

import eventplanner.model.AppointmentEvent;
import eventplanner.model.Attendee;
import eventplanner.model.BirthdayEvent;
import eventplanner.model.Category;
import eventplanner.model.Event;
import eventplanner.model.MeetingEvent;
import eventplanner.model.Recurrence;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.plaf.basic.BasicButtonUI;

public class EventDialog extends JDialog {
    private static final Color BORDER = new Color(224, 229, 235);
    private static final Color TEXT = new Color(36, 41, 47);
    private static final Color PRIMARY = new Color(51, 103, 214);

    private boolean confirmed;
    private JTextField titleField;
    private JTextField dateField;
    private JTextField timeField;
    private JTextField locationField;
    private JTextArea descriptionArea;
    private JComboBox<Category> categoryBox;
    private JComboBox<Recurrence> recurrenceBox;
    private JComboBox<String> reminderBox;
    private JTextArea attendeeArea;

    public EventDialog(java.awt.Frame owner, LocalDate selectedDate, Event event) {
        super(owner, event == null ? "New Event" : "Edit Event", true);
        buildForm(selectedDate, event);
        pack();
        setResizable(false);
        setLocationRelativeTo(owner);
    }

    private void buildForm(LocalDate selectedDate, Event event) {
        setLayout(new BorderLayout());

        JPanel form = new JPanel(new GridBagLayout());
        form.setBackground(Color.WHITE);
        form.setBorder(BorderFactory.createEmptyBorder(18, 20, 14, 20));

        GridBagConstraints c = new GridBagConstraints();
        c.insets = new Insets(6, 4, 6, 4);
        c.fill = GridBagConstraints.HORIZONTAL;
        c.weightx = 1;

        titleField = new JTextField(24);
        dateField = new JTextField(selectedDate.toString());
        timeField = new JTextField("09:00");
        locationField = new JTextField(24);
        descriptionArea = new JTextArea(4, 24);
        categoryBox = new JComboBox<Category>(Category.values());
        recurrenceBox = new JComboBox<Recurrence>(Recurrence.values());
        reminderBox = new JComboBox<String>(new String[] {
                "0 minutes", "10 minutes", "60 minutes", "1 day", "3 days", "1 week"
        });
        attendeeArea = new JTextArea(4, 24);

        styleTextField(titleField);
        styleTextField(dateField);
        styleTextField(timeField);
        styleTextField(locationField);
        styleTextArea(descriptionArea);
        styleTextArea(attendeeArea);
        styleComboBox(categoryBox);
        styleComboBox(recurrenceBox);
        styleComboBox(reminderBox);

        if (event != null) {
            titleField.setText(event.getTitle());
            dateField.setText(event.getDate().toString());
            timeField.setText(event.getTime().toString());
            locationField.setText(event.getLocation());
            descriptionArea.setText(event.getDescription());
            categoryBox.setSelectedItem(event.getCategory());
            recurrenceBox.setSelectedItem(event.getRecurrence());
            reminderBox.setSelectedItem(reminderToLabel(event.getReminderMinutes()));
            StringBuilder attendees = new StringBuilder();
            for (Attendee attendee : event.getAttendees()) {
                attendees.append(attendee.getName()).append(",").append(attendee.getEmail()).append("\n");
            }
            attendeeArea.setText(attendees.toString());
        }

        addRow(form, c, 0, "Title", titleField);
        addRow(form, c, 1, "Date", dateField);
        addRow(form, c, 2, "Time", timeField);
        addRow(form, c, 3, "Location", locationField);
        addRow(form, c, 4, "Category", categoryBox);
        addRow(form, c, 5, "Reminder", reminderBox);
        addRow(form, c, 6, "Recurrence", recurrenceBox);
        addRow(form, c, 7, "Description", new JScrollPane(descriptionArea));
        addRow(form, c, 8, "Attendees", new JScrollPane(attendeeArea));

        JPanel buttons = new JPanel();
        buttons.setBackground(Color.WHITE);
        buttons.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(1, 0, 0, 0, BORDER),
                BorderFactory.createEmptyBorder(12, 20, 16, 20)));

        JButton save = createDialogButton("Save", PRIMARY, Color.WHITE);
        JButton cancel = createDialogButton("Cancel", new Color(246, 248, 250), TEXT);

        save.addActionListener(e -> {
            try {
                validateForm();
                confirmed = true;
                dispose();
            } catch (DateTimeParseException ex) {
                JOptionPane.showMessageDialog(this, "Please use date yyyy-mm-dd and time HH:mm.",
                        "Invalid event", JOptionPane.WARNING_MESSAGE);
            } catch (IllegalArgumentException ex) {
                JOptionPane.showMessageDialog(this, "Erro: " + ex.getMessage(), "Invalid event",
                        JOptionPane.WARNING_MESSAGE);
            }
        });
        cancel.addActionListener(e -> dispose());
        buttons.add(save);
        buttons.add(cancel);

        add(form, BorderLayout.CENTER);
        add(buttons, BorderLayout.SOUTH);
    }

    private void validateForm() {
        getTitleValue();
        getDateValue();
        getTimeValue();
        getReminderMinutes();
    }

    private void addRow(JPanel form, GridBagConstraints c, int y, String label, java.awt.Component component) {
        c.gridy = y;
        c.gridx = 0;
        c.weightx = 0;

        JLabel fieldLabel = new JLabel(label);
        fieldLabel.setForeground(TEXT);
        fieldLabel.setFont(fieldLabel.getFont().deriveFont(Font.BOLD, 12f));
        form.add(fieldLabel, c);

        c.gridx = 1;
        c.weightx = 1;
        form.add(component, c);
    }

    private void styleTextField(JTextField field) {
        field.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(BORDER),
                BorderFactory.createEmptyBorder(7, 8, 7, 8)));
    }

    private void styleTextArea(JTextArea area) {
        area.setLineWrap(true);
        area.setWrapStyleWord(true);
        area.setBorder(BorderFactory.createEmptyBorder(7, 8, 7, 8));
    }

    private void styleComboBox(JComboBox<?> comboBox) {
        comboBox.setBackground(Color.WHITE);
        comboBox.setBorder(BorderFactory.createLineBorder(BORDER));
    }

    private JButton createDialogButton(String text, Color background, Color foreground) {
        JButton button = new JButton(text);
        button.setUI(new BasicButtonUI());
        button.setBackground(background);
        button.setForeground(foreground);
        button.setOpaque(true);
        button.setContentAreaFilled(true);
        button.setFocusPainted(false);
        button.setRolloverEnabled(false);
        button.setFont(button.getFont().deriveFont(Font.BOLD, 13f));
        button.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(BORDER),
                BorderFactory.createEmptyBorder(8, 18, 8, 18)));
        return button;
    }

    public boolean isConfirmed() {
        return confirmed;
    }

    public Event toEvent(Event original) {
        String title = getTitleValue();
        LocalDate date = getDateValue();
        LocalTime time = getTimeValue();
        String location = locationField.getText();
        String description = descriptionArea.getText();
        int reminderMinutes = getReminderMinutes();
        Recurrence recurrence = getRecurrenceValue();
        Category category = getCategoryValue();

        Event event;
        if (category == Category.MEETING) {
            event = new MeetingEvent(title, date, time, location, description, reminderMinutes, recurrence);
        } else if (category == Category.BIRTHDAY) {
            event = new BirthdayEvent(title, date, time, location, description, reminderMinutes, recurrence);
        } else {
            event = new AppointmentEvent(title, date, time, location, description, reminderMinutes, recurrence);
        }

        if (original != null) {
            event.setSeriesId(original.getSeriesId());
        }

        for (Attendee attendee : getAttendees()) {
            event.addAttendee(attendee);
        }

        return event;
    }

    private String getTitleValue() {
        String title = titleField.getText().trim();
        if (title.isEmpty()) {
            throw new IllegalArgumentException("Titulo nao pode ser vazio");
        }
        return title;
    }

    private LocalDate getDateValue() {
        return LocalDate.parse(dateField.getText().trim());
    }

    private LocalTime getTimeValue() {
        return LocalTime.parse(timeField.getText().trim());
    }

    private Category getCategoryValue() {
        return (Category) categoryBox.getSelectedItem();
    }

    private Recurrence getRecurrenceValue() {
        return (Recurrence) recurrenceBox.getSelectedItem();
    }

    private int getReminderMinutes() {
        String label = (String) reminderBox.getSelectedItem();
        if ("10 minutes".equals(label)) return 10;
        if ("60 minutes".equals(label)) return 60;
        if ("1 day".equals(label)) return 1440;
        if ("3 days".equals(label)) return 4320;
        if ("1 week".equals(label)) return 10080;
        return 0;
    }

    private String reminderToLabel(int minutes) {
        if (minutes == 10) return "10 minutes";
        if (minutes == 60) return "60 minutes";
        if (minutes == 1440) return "1 day";
        if (minutes == 4320) return "3 days";
        if (minutes == 10080) return "1 week";
        return "0 minutes";
    }

    private List<Attendee> getAttendees() {
        List<Attendee> attendees = new ArrayList<>();
        String[] lines = attendeeArea.getText().split("\\R");
        for (String line : lines) {
            if (line.trim().isEmpty()) {
                continue;
            }
            String[] parts = line.split(",", 2);
            String name = parts[0].trim();
            String email = parts.length > 1 ? parts[1].trim() : "";
            if (!name.isEmpty()) {
                attendees.add(new Attendee(name, email));
            }
        }
        return attendees;
    }
}
