package eventplanner.controller;

import eventplanner.model.Event;
import eventplanner.service.EventService;
import java.time.LocalDate;
import java.util.List;

/**
 * Controlador de eventos.
 * Gerencia a comunicacao entre a view e a camada de servico.
 */
public class EventManager {
    private final EventService service;

    public EventManager() {
        this.service = new EventService();
    }

    public List<Event> getAllEvents() {
        return service.getAllEvents();
    }

    public List<Event> getEventsOn(LocalDate date) {
        return service.getEventsOn(date);
    }

    public void addEvent(Event event) {
        service.addEvent(event);
    }

    public void updateEvent(Event original, Event updated) {
        if (original == null || updated == null) {
            return;
        }

        List<Event> events = service.getAllEvents();
        int index = events.indexOf(original);
        if (index >= 0) {
            events.set(index, updated);
        }
    }

    public void deleteEvent(Event event, boolean removeSeriesFuture) {
        service.deleteEvent(event, removeSeriesFuture);
    }

    public List<Event> searchEvents(String keyword) {
        return service.searchEvents(keyword);
    }

    public void saveEvents() {
        service.save();
    }
}
