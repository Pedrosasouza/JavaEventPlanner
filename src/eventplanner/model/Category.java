package eventplanner.model;

import java.awt.Color;

/**
 * Enumeração de categorias de eventos.
 * Define os tipos de eventos disponíveis no sistema.
 */
public enum Category {
    MEETING("Meeting", new Color(80, 135, 210)),
    BIRTHDAY("Birthday", new Color(215, 90, 95)),
    APPOINTMENT("Appointment", new Color(70, 155, 110));

    private final String label;
    private final Color color;

    Category(String label, Color color) {
        this.label = label;
        this.color = color;
    }

    public String getLabel() {
        return label;
    }

    public Color getColor() {
        return color;
    }

    @Override
    public String toString() {
        return label;
    }
}
