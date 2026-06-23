package eventplanner.view;

import eventplanner.model.Event;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.util.List;
import javax.swing.BorderFactory;
import javax.swing.DefaultListModel;
import javax.swing.DefaultListCellRenderer;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.ListSelectionModel;

public class EventListPanel extends JPanel {
    private static final Color BORDER = new Color(224, 229, 235);
    private static final Color TEXT = new Color(36, 41, 47);
    private static final Color MUTED = new Color(87, 96, 106);

    private final DefaultListModel<Event> listModel;
    private final JList<Event> eventList;
    private final JTextArea detailsArea;

    public EventListPanel() {
        setLayout(new BorderLayout());
        setBackground(Color.WHITE);
        setPreferredSize(new Dimension(320, 0));
        setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(BORDER),
                BorderFactory.createEmptyBorder(14, 14, 14, 14)));

        listModel = new DefaultListModel<Event>();
        eventList = new JList<Event>(listModel);
        eventList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        eventList.setCellRenderer(new EventCellRenderer());
        eventList.setFixedCellHeight(48);
        eventList.setBorder(BorderFactory.createEmptyBorder(4, 0, 4, 0));
        eventList.addListSelectionListener(e -> showSelectedDetails());

        detailsArea = new JTextArea();
        detailsArea.setEditable(false);
        detailsArea.setLineWrap(true);
        detailsArea.setWrapStyleWord(true);
        detailsArea.setForeground(TEXT);
        detailsArea.setBackground(new Color(250, 251, 252));
        detailsArea.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        detailsArea.setPreferredSize(new Dimension(280, 220));

        JLabel title = new JLabel("Events for selected day");
        title.setForeground(TEXT);
        title.setFont(title.getFont().deriveFont(Font.BOLD, 16f));
        title.setBorder(BorderFactory.createEmptyBorder(0, 0, 10, 0));

        add(title, BorderLayout.NORTH);
        add(new JScrollPane(eventList), BorderLayout.CENTER);
        add(new JScrollPane(detailsArea), BorderLayout.SOUTH);
    }

    public void updateEvents(List<Event> events) {
        listModel.clear();
        for (Event event : events) {
            listModel.addElement(event);
        }
        if (events.isEmpty()) {
            detailsArea.setText("No events for this date.");
        } else {
            detailsArea.setText("Select an event to view details.");
        }
    }

    public Event getSelectedEvent() {
        return eventList.getSelectedValue();
    }

    private void showSelectedDetails() {
        Event event = eventList.getSelectedValue();
        if (event != null) {
            detailsArea.setText(event.getDetailsText());
        }
    }

    public void showMessage(String message) {
        JOptionPane.showMessageDialog(this, message);
    }

    private static class EventCellRenderer extends DefaultListCellRenderer {
        @Override
        public Component getListCellRendererComponent(JList<?> list, Object value, int index,
                boolean isSelected, boolean cellHasFocus) {
            JLabel label = (JLabel) super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
            label.setBorder(BorderFactory.createEmptyBorder(8, 10, 8, 10));
            label.setFont(label.getFont().deriveFont(Font.PLAIN, 13f));

            if (value instanceof Event event) {
                label.setText(event.getTime() + "  " + event.getTitle());
                label.setIcon(new CategoryIcon(event.getCategory().getColor()));
            }

            if (isSelected) {
                label.setBackground(new Color(223, 237, 255));
                label.setForeground(TEXT);
            } else {
                label.setBackground(Color.WHITE);
                label.setForeground(MUTED);
            }

            return label;
        }
    }

    private static class CategoryIcon implements javax.swing.Icon {
        private final Color color;

        CategoryIcon(Color color) {
            this.color = color;
        }

        @Override
        public void paintIcon(Component c, java.awt.Graphics g, int x, int y) {
            g.setColor(color);
            g.fillOval(x + 2, y + 4, 10, 10);
        }

        @Override
        public int getIconWidth() {
            return 18;
        }

        @Override
        public int getIconHeight() {
            return 18;
        }
    }
}
