package eventplanner.model;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Classe abstrata que representa um evento genérico.
 * Todos os eventos herdam desta classe.
 * 
 * Encapsulamento: atributos privados com getters/setters
 * Validação: integrada no construtor (sem classes separadas)
 */
public abstract class Event implements Serializable {
    private String id;
    private String seriesId;
    private String title;
    private LocalDate date;
    private LocalTime time;
    private String location;
    private String description;
    private int reminderMinutes;
    private Recurrence recurrence;
    private List<Attendee> attendees;

    /**
     * Construtor protegido com validação básica integrada.
     */
    protected Event(String title, LocalDate date, LocalTime time, String location,
                    String description, int reminderMinutes, Recurrence recurrence) {
        if (title == null || title.trim().isEmpty()) {
            throw new IllegalArgumentException("Título do evento não pode ser vazio");
        }
        
        if (date == null || time == null) {
            throw new IllegalArgumentException("Data e hora são obrigatórias");
        }
        
        if (recurrence == null) {
            throw new IllegalArgumentException("Tipo de recorrência não pode ser nulo");
        }
        
        this.id = UUID.randomUUID().toString();
        this.title = title;
        this.date = date;
        this.time = time;
        this.location = location != null ? location : "";
        this.description = description != null ? description : "";
        this.reminderMinutes = reminderMinutes;
        this.recurrence = recurrence;
        this.attendees = new ArrayList<>();
    }

    // ===== ABSTRACT METHOD =====
    
    /**
     * Cada subclasse deve implementar sua categoria.
     */
    public abstract Category getCategory();

    // ===== UTILITY METHODS =====
    
    public LocalDateTime getDateTime() {
        return LocalDateTime.of(date, time);
    }

    public boolean matches(String keyword) {
        String text = (title + " " + location + " " + description + " " + getCategory()).toLowerCase();
        return text.contains(keyword.toLowerCase());
    }

    public String getShortText() {
        return time + " - " + title + " (" + getCategory().getLabel() + ")";
    }

    public String getDetailsText() {
        StringBuilder builder = new StringBuilder();
        builder.append("Title: ").append(title).append("\n");
        builder.append("Date: ").append(date).append(" at ").append(time).append("\n");
        builder.append("Category: ").append(getCategory().getLabel()).append("\n");
        builder.append("Location: ").append(location).append("\n");
        builder.append("Reminder: ").append(reminderMinutes).append(" minutes before\n");
        builder.append("Recurrence: ").append(recurrence).append("\n\n");
        builder.append("Description:\n").append(description).append("\n\n");
        builder.append("Attendees:\n");
        if (attendees.isEmpty()) {
            builder.append("No attendees registered.");
        } else {
            for (Attendee attendee : attendees) {
                builder.append("- ").append(attendee).append("\n");
            }
        }
        return builder.toString();
    }

    
    public Event copyForDate(LocalDate newDate) {
        Event event = createCopy(getCategory(), title, newDate, time, location,
                description, reminderMinutes, recurrence);
        event.setSeriesId(seriesId);
        for (Attendee attendee : attendees) {
            event.addAttendee(new Attendee(attendee.getName(), attendee.getEmail()));
        }
        return event;
    }

    
    private Event createCopy(Category category, String title, LocalDate date, LocalTime time,
                            String location, String description, int reminderMinutes,
                            Recurrence recurrence) {
        if (category == Category.MEETING) {
            return new MeetingEvent(title, date, time, location, description, reminderMinutes, recurrence);
        } else if (category == Category.BIRTHDAY) {
            return new BirthdayEvent(title, date, time, location, description, reminderMinutes, recurrence);
        } else {
            return new AppointmentEvent(title, date, time, location, description, reminderMinutes, recurrence);
        }
    }

    // ===== GETTERS E SETTERS =====
    
    public String getId() {
        return id;
    }

    public String getSeriesId() {
        return seriesId;
    }

    public void setSeriesId(String seriesId) {
        this.seriesId = seriesId;
    }

    public boolean isRecurringInstance() {
        return seriesId != null && !seriesId.trim().isEmpty();
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        if (title == null || title.trim().isEmpty()) {
            throw new IllegalArgumentException("Título não pode ser vazio");
        }
        this.title = title;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        if (date == null) {
            throw new IllegalArgumentException("Data não pode ser nula");
        }
        this.date = date;
    }

    public LocalTime getTime() {
        return time;
    }

    public void setTime(LocalTime time) {
        if (time == null) {
            throw new IllegalArgumentException("Hora não pode ser nula");
        }
        this.time = time;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location != null ? location : "";
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description != null ? description : "";
    }

    public int getReminderMinutes() {
        return reminderMinutes;
    }

    public void setReminderMinutes(int reminderMinutes) {
        this.reminderMinutes = reminderMinutes;
    }

    public Recurrence getRecurrence() {
        return recurrence;
    }

    public void setRecurrence(Recurrence recurrence) {
        if (recurrence == null) {
            throw new IllegalArgumentException("Recorrência não pode ser nula");
        }
        this.recurrence = recurrence;
    }

    public List<Attendee> getAttendees() {
        return new ArrayList<>(attendees); 
    }

    public void addAttendee(Attendee attendee) {
        if (attendee != null && !attendees.contains(attendee)) {
            attendees.add(attendee);
        }
    }

    public void removeAttendee(Attendee attendee) {
        attendees.remove(attendee);
    }

    public void clearAttendees() {
        attendees.clear();
    }

    @Override
    public String toString() {
        return getShortText();
    }
}
