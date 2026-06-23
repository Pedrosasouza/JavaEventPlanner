package eventplanner.view;

import eventplanner.controller.EventManager;
import eventplanner.model.Event;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.TextStyle;
import java.util.List;
import java.util.Locale;
import java.util.function.Consumer;
import javax.swing.*;
import javax.swing.plaf.basic.BasicButtonUI;

public class CalendarPanel extends JPanel {
    private static final Color BORDER = new Color(224, 229, 235);
    private static final Color TEXT = new Color(36, 41, 47);
    private static final Color MUTED = new Color(87, 96, 106);
    private static final Color SELECTED = new Color(223, 237, 255);
    private static final Color TODAY = new Color(255, 248, 214);

    private final EventManager manager;
    private final Consumer<LocalDate> onDateSelected;
    private YearMonth currentMonth;
    private LocalDate selectedDate;
    private final JLabel monthLabel;
    private final JPanel dayPanel;

    public CalendarPanel(EventManager manager, LocalDate selectedDate, Consumer<LocalDate> onDateSelected) {
        this.manager = manager;
        this.selectedDate = selectedDate;
        this.onDateSelected = onDateSelected;
        this.currentMonth = YearMonth.from(selectedDate);
        this.monthLabel = new JLabel("", SwingConstants.CENTER);
        this.dayPanel = new JPanel(new GridLayout(0, 7, 3, 3));
        buildPanel();
        refreshCalendar();
    }

    private void buildPanel() {
        setLayout(new BorderLayout());
        setBackground(Color.WHITE);
        setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(BORDER),
                BorderFactory.createEmptyBorder(14, 14, 14, 14)));

        monthLabel.setForeground(TEXT);
        monthLabel.setFont(monthLabel.getFont().deriveFont(Font.BOLD, 22f));

        JButton previousButton = createNavigationButton("<");
        JButton nextButton = createNavigationButton(">");
        JButton todayButton = createNavigationButton("Today");

        previousButton.addActionListener(e -> {
            currentMonth = currentMonth.minusMonths(1);
            refreshCalendar();
        });
        nextButton.addActionListener(e -> {
            currentMonth = currentMonth.plusMonths(1);
            refreshCalendar();
        });
        todayButton.addActionListener(e -> {
            setSelectedDate(LocalDate.now());
            onDateSelected.accept(selectedDate);
        });

        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(Color.WHITE);
        header.setBorder(BorderFactory.createEmptyBorder(0, 0, 12, 0));
        JPanel navigation = new JPanel();
        navigation.setBackground(Color.WHITE);
        navigation.add(previousButton);
        navigation.add(todayButton);
        navigation.add(nextButton);

        header.add(monthLabel, BorderLayout.CENTER);
        header.add(navigation, BorderLayout.EAST);

        dayPanel.setBackground(Color.WHITE);
        dayPanel.setBorder(BorderFactory.createEmptyBorder(6, 0, 0, 0));

        add(header, BorderLayout.NORTH);
        add(dayPanel, BorderLayout.CENTER);
    }

    public void setSelectedDate(LocalDate date) {
        this.selectedDate = date;
        this.currentMonth = YearMonth.from(date);
        refreshCalendar();
    }

    public void refreshCalendar() {
        dayPanel.removeAll();
        String month = currentMonth.getMonth().getDisplayName(TextStyle.FULL, Locale.ENGLISH);
        monthLabel.setText(month + " " + currentMonth.getYear());

        String[] days = {"Sun", "Mon", "Tue", "Wed", "Thu", "Fri", "Sat"};
        for (String day : days) {
            JLabel label = new JLabel(day, SwingConstants.CENTER);
            label.setForeground(MUTED);
            label.setFont(label.getFont().deriveFont(Font.BOLD, 12f));
            dayPanel.add(label);
        }

        LocalDate first = currentMonth.atDay(1);
        int blanks = first.getDayOfWeek().getValue() % 7;
        for (int i = 0; i < blanks; i++) {
            dayPanel.add(new JLabel(""));
        }

        for (int day = 1; day <= currentMonth.lengthOfMonth(); day++) {
            LocalDate date = currentMonth.atDay(day);
            JButton button = new JButton(String.valueOf(day));
            styleDayButton(button);

            boolean hasEvents = hasEventOn(date);
            if (hasEvents) {
                button.setForeground(Color.WHITE);
                button.setBackground(categoryColorForDate(date));
                button.setFont(button.getFont().deriveFont(Font.BOLD));
            } else if (date.equals(LocalDate.now())) {
                button.setBackground(TODAY);
            }

            if (date.equals(selectedDate)) {
                button.setBackground(hasEvents ? button.getBackground().darker() : SELECTED);
                button.setForeground(hasEvents ? Color.WHITE : TEXT);
                button.setBorder(BorderFactory.createLineBorder(new Color(51, 103, 214), 2));
            }
            button.addActionListener(e -> {
                setSelectedDate(date);
                onDateSelected.accept(date);
            });
            dayPanel.add(button);
        }

        dayPanel.revalidate();
        dayPanel.repaint();
    }

    private JButton createNavigationButton(String text) {
        JButton button = new JButton(text);
        button.setFocusPainted(false);
        button.setBackground(new Color(246, 248, 250));
        button.setForeground(TEXT);
        button.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(BORDER),
                BorderFactory.createEmptyBorder(6, 12, 6, 12)));
        return button;
    }

    private void styleDayButton(JButton button) {
        button.setUI(new BasicButtonUI());
        button.setOpaque(true);
        button.setContentAreaFilled(true);
        button.setFocusPainted(false);
        button.setRolloverEnabled(false);
        button.setForeground(TEXT);
        button.setBackground(new Color(250, 251, 252));
        button.setPreferredSize(new Dimension(72, 56));
        button.setBorder(BorderFactory.createLineBorder(new Color(233, 236, 239)));
    }

    private boolean hasEventOn(LocalDate date) {
        return !manager.getEventsOn(date).isEmpty();
    }

    private Color categoryColorForDate(LocalDate date) {
        List<Event> dayEvents = manager.getEventsOn(date);
        if (dayEvents.isEmpty()) {
            return Color.WHITE;
        }
        return dayEvents.get(0).getCategory().getColor();
    }
}
