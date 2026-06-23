package eventplanner.service;

import eventplanner.model.Event;
import eventplanner.model.Recurrence;
import eventplanner.util.EventStorage;
import java.io.IOException;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class EventService {
    private final EventStorage storage;
    private final List<Event> events;

    public EventService() {
        this.storage = new EventStorage(Paths.get("data", "events.txt"));
        this.events = storage.load();
    }

    public List<Event> getAllEvents() {
        return events;
    }

    public List<Event> getEventsOn(LocalDate date) {
        List<Event> result = new ArrayList<Event>();
        for (Event event : events) {
            if (event.getDate().equals(date)) {
                result.add(event);
            }
        }
        return result;
    }

    public void addEvent(Event event) {
        events.add(event);
        if (event.getRecurrence() != Recurrence.NONE) {
            String seriesId = UUID.randomUUID().toString();
            event.setSeriesId(seriesId);

            int amount = event.getRecurrence() == Recurrence.DAILY ? 30 : 12;
            for (int i = 1; i < amount; i++) {
                LocalDate nextDate = nextDate(event.getDate(), event.getRecurrence(), i);
                Event copy = event.copyForDate(nextDate);
                copy.setSeriesId(seriesId);
                events.add(copy);
            }
        }
    }

    public void save() {
        try {
            storage.save(events);
        } catch (IOException ex) {
            System.err.println("Erro ao salvar eventos: " + ex.getMessage());
        }
    }

    public void deleteEvent(Event event, boolean removeSeriesFuture) {
        if (event == null) return;
        if (removeSeriesFuture && event.getSeriesId() != null && !event.getSeriesId().isEmpty()) {
            events.removeIf(e -> event.getSeriesId().equals(e.getSeriesId()) && !e.getDate().isBefore(event.getDate()));
            return;
        }
        events.remove(event);
    }

    public List<Event> searchEvents(String keyword) {
        List<Event> result = new ArrayList<>();
        String k = keyword == null ? "" : keyword.toLowerCase();
        for (Event e : events) {
            if (e.matches(k)) result.add(e);
        }
        return result;
    }

    private LocalDate nextDate(LocalDate start, Recurrence recurrence, int step) {
        if (recurrence == Recurrence.DAILY) return start.plusDays(step);
        if (recurrence == Recurrence.WEEKLY) return start.plusWeeks(step);
        if (recurrence == Recurrence.MONTHLY) return start.plusMonths(step);
        return start;
    }
}
