package eventplanner.util;

import eventplanner.model.*;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

/**
 * Persistência de eventos em arquivo TXT.
 * Armazena e recupera eventos de um arquivo texto.
 * Formato: CATEGORIA|TITULO|DATA|HORA|LOCALIZACAO|DESCRICAO|LEMBRETE|RECORRENCIA|SERIES_ID|PARTICIPANTES
 */
public class EventStorage {
    private final Path file;

    public EventStorage(Path file) {
        this.file = file;
    }

    /**
     * Carregar todos os eventos do arquivo.
     */
    public List<Event> load() {
        List<Event> events = new ArrayList<Event>();
        if (!Files.exists(file)) {
            return events;
        }

        try (BufferedReader reader = Files.newBufferedReader(file, StandardCharsets.UTF_8)) {
            String line;
            while ((line = reader.readLine()) != null) {
                try {
                    Event event = fromLine(line);
                    if (event != null) {
                        events.add(event);
                    }
                } catch (Exception ignored) {
                }
            }
        } catch (IOException ignored) {
            return new ArrayList<Event>();
        }
        return events;
    }

    /**
     * Salvar todos os eventos no arquivo.
     */
    public void save(List<Event> events) throws IOException {
        if (file.getParent() != null) {
            Files.createDirectories(file.getParent());
        }
        try (BufferedWriter writer = Files.newBufferedWriter(file, StandardCharsets.UTF_8)) {
            for (Event event : events) {
                writer.write(toLine(event));
                writer.newLine();
            }
        }
    }

   
    private String toLine(Event event) {
        StringBuilder attendees = new StringBuilder();
        for (Attendee attendee : event.getAttendees()) {
            if (attendees.length() > 0) {
                attendees.append(";");
            }
            attendees.append(encode(attendee.getName())).append(",").append(encode(attendee.getEmail()));
        }

        return String.join("|",
                event.getCategory().name(),
                encode(event.getTitle()),
                event.getDate().toString(),
                event.getTime().toString(),
                encode(event.getLocation()),
                encode(event.getDescription()),
                String.valueOf(event.getReminderMinutes()),
                event.getRecurrence().name(),
                event.getSeriesId() == null ? "" : event.getSeriesId(),
                attendees.toString());
    }

    /**
     * Converter linha de texto para evento.
     */
    private Event fromLine(String line) {
        try {
            String[] parts = line.split("\\|", -1);
            if (parts.length < 10) {
                return null;
            }
            
            Category category = Category.valueOf(parts[0]);
            String title = decode(parts[1]);
            LocalDate date = LocalDate.parse(parts[2]);
            LocalTime time = LocalTime.parse(parts[3]);
            String location = decode(parts[4]);
            String description = decode(parts[5]);
            int reminder = Integer.parseInt(parts[6]);
            Recurrence recurrence = Recurrence.valueOf(parts[7]);

            Event event;
            if (category == Category.MEETING) {
                event = new MeetingEvent(title, date, time, location, description, reminder, recurrence);
            } else if (category == Category.BIRTHDAY) {
                event = new BirthdayEvent(title, date, time, location, description, reminder, recurrence);
            } else {
                event = new AppointmentEvent(title, date, time, location, description, reminder, recurrence);
            }
            
            if (!parts[8].trim().isEmpty()) {
                event.setSeriesId(parts[8]);
            }
            
            if (!parts[9].trim().isEmpty()) {
                String[] attendeeParts = parts[9].split(";");
                for (String item : attendeeParts) {
                    String[] nameEmail = item.split(",", -1);
                    if (nameEmail.length == 2) {
                        event.addAttendee(new Attendee(decode(nameEmail[0]), decode(nameEmail[1])));
                    }
                }
            }
            
            return event;
        } catch (Exception e) {
            System.err.println("Erro ao parsear evento: " + e.getMessage());
            return null;
        }
    }

    /**
     * Encode de string em Base64 para segurança de dados.
     */
    private String encode(String value) {
        if (value == null) {
            value = "";
        }
        return Base64.getEncoder().encodeToString(value.getBytes(StandardCharsets.UTF_8));
    }

    /**
     * Decode de string Base64.
     */
    private String decode(String value) {
        return new String(Base64.getDecoder().decode(value), StandardCharsets.UTF_8);
    }
}
