package eventplanner.model;

import java.time.LocalDate;
import java.time.LocalTime;

/**
 * Evento de reunião.
 * Herda de Event e implementa a categoria MEETING.
 */
public class MeetingEvent extends Event {
    public MeetingEvent(String title, LocalDate date, LocalTime time, String location,
                        String description, int reminderMinutes, Recurrence recurrence) {
        super(title, date, time, location, description, reminderMinutes, recurrence);
    }

    @Override
    public Category getCategory() {
        return Category.MEETING;
    }
}
