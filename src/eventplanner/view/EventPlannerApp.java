package eventplanner.view;

import eventplanner.controller.EventManager;
import eventplanner.model.Event;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.time.LocalDate;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.plaf.basic.BasicButtonUI;

public class EventPlannerApp extends JFrame {
    private static final Color BACKGROUND = new Color(245, 247, 250);
    private static final Color PANEL_BACKGROUND = Color.WHITE;
    private static final Color PRIMARY = new Color(51, 103, 214);
    private static final Color TEXT = new Color(36, 41, 47);

    private final EventManager manager;
    private LocalDate selectedDate;
    private final CalendarPanel calendarPanel;
    private final EventListPanel eventListPanel;

    public EventPlannerApp() {
        super("Java Event Planner");
        this.manager = new EventManager();
        this.selectedDate = LocalDate.now();
        this.calendarPanel = new CalendarPanel(manager, selectedDate, this::selectDate);
        this.eventListPanel = new EventListPanel();

        initComponents();
        updateEventList();

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(new Dimension(1000, 660));
        setLocationRelativeTo(null);
    }

    private void initComponents() {
        getContentPane().setBackground(BACKGROUND);

        JPanel root = new JPanel(new BorderLayout(12, 12));
        root.setBackground(BACKGROUND);
        root.setBorder(BorderFactory.createEmptyBorder(16, 16, 16, 16));

        root.add(createHeader(), BorderLayout.NORTH);
        root.add(calendarPanel, BorderLayout.CENTER);
        root.add(eventListPanel, BorderLayout.EAST);

        JPanel actionPanel = new JPanel();
        actionPanel.setBackground(PANEL_BACKGROUND);
        actionPanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(224, 229, 235)),
                BorderFactory.createEmptyBorder(10, 12, 10, 12)));
        actionPanel.add(createButton("Add", this::addEvent, PRIMARY, Color.WHITE));
        actionPanel.add(createButton("Edit", this::editSelectedEvent, new Color(246, 248, 250), TEXT));
        actionPanel.add(createButton("Delete", this::deleteSelectedEvent, new Color(255, 235, 238), new Color(176, 42, 55)));
        actionPanel.add(createButton("Save", this::saveEvents, new Color(232, 245, 233), new Color(35, 116, 67)));

        root.add(actionPanel, BorderLayout.SOUTH);
        setContentPane(root);
    }

    private JPanel createHeader() {
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(BACKGROUND);
        header.setBorder(BorderFactory.createEmptyBorder(0, 2, 4, 2));

        JLabel title = new JLabel("Java Event Planner");
        title.setForeground(TEXT);
        title.setFont(title.getFont().deriveFont(Font.BOLD, 24f));

        JLabel subtitle = new JLabel("Organize eventos, recorrencias e participantes");
        subtitle.setForeground(new Color(87, 96, 106));
        subtitle.setFont(subtitle.getFont().deriveFont(13f));

        JPanel textPanel = new JPanel(new BorderLayout(0, 2));
        textPanel.setBackground(BACKGROUND);
        textPanel.add(title, BorderLayout.NORTH);
        textPanel.add(subtitle, BorderLayout.SOUTH);

        header.add(textPanel, BorderLayout.WEST);
        return header;
    }

    private JButton createButton(String text, Runnable action, Color background, Color foreground) {
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
                BorderFactory.createLineBorder(new Color(210, 216, 224)),
                BorderFactory.createEmptyBorder(8, 18, 8, 18)));
        button.addActionListener(e -> action.run());
        return button;
    }

    private void selectDate(LocalDate date) {
        this.selectedDate = date;
        updateEventList();
    }

    private void updateEventList() {
        eventListPanel.updateEvents(manager.getEventsOn(selectedDate));
        calendarPanel.refreshCalendar();
    }

    private void addEvent() {
        EventDialog dialog = new EventDialog(this, selectedDate, null);
        dialog.setVisible(true);
        if (dialog.isConfirmed()) {
            manager.addEvent(dialog.toEvent(null));
            updateEventList();
        }
    }

    private void editSelectedEvent() {
        Event event = eventListPanel.getSelectedEvent();
        if (event == null) {
            eventListPanel.showMessage("Select an event first.");
            return;
        }
        EventDialog dialog = new EventDialog(this, selectedDate, event);
        dialog.setVisible(true);
        if (dialog.isConfirmed()) {
            Event updated = dialog.toEvent(event);
            manager.updateEvent(event, updated);
            selectedDate = updated.getDate();
            calendarPanel.setSelectedDate(selectedDate);
            updateEventList();
        }
    }

    private void deleteSelectedEvent() {
        Event event = eventListPanel.getSelectedEvent();
        if (event == null) {
            eventListPanel.showMessage("Select an event first.");
            return;
        }
        manager.deleteEvent(event, true);
        updateEventList();
    }

    private void saveEvents() {
        manager.saveEvents();
        eventListPanel.showMessage("Events saved.");
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            configureLookAndFeel();
            new EventPlannerApp().setVisible(true);
        });
    }

    private static void configureLookAndFeel() {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException
                | UnsupportedLookAndFeelException ex) {
            System.err.println("Could not load system look and feel: " + ex.getMessage());
        }
    }
}
