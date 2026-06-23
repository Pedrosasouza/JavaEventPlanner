package eventplanner.model;

import java.time.LocalDate;
import java.time.LocalTime;

/**
 * Evento de aniversário.
 * Herda de Event e implementa a categoria BIRTHDAY.
 */
public class BirthdayEvent extends Event {
    public BirthdayEvent(String title, LocalDate date, LocalTime time, String location,
                         String description, int reminderMinutes, Recurrence recurrence) {
        super(title, date, time, location, description, reminderMinutes, recurrence);
    }

    @Override
    public Category getCategory() {
        return Category.BIRTHDAY;
    }
}
