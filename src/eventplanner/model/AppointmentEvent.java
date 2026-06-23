package eventplanner.model;

import java.time.LocalDate;
import java.time.LocalTime;

/**
 * Evento de compromisso/consulta.
 * Herda de Event e implementa a categoria APPOINTMENT.
 */
public class AppointmentEvent extends Event {
    public AppointmentEvent(String title, LocalDate date, LocalTime time, String location,
                            String description, int reminderMinutes, Recurrence recurrence) {
        super(title, date, time, location, description, reminderMinutes, recurrence);
    }

    @Override
    public Category getCategory() {
        return Category.APPOINTMENT;
    }
}
