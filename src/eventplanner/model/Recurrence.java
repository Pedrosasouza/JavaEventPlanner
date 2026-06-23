package eventplanner.model;

/**
 * Enumeração de tipos de recorrência de eventos.
 * Simplificado: apenas os 4 tipos básicos, sem lógica complexa.
 */
public enum Recurrence {
    NONE("None"),
    DAILY("Daily"),
    WEEKLY("Weekly"),
    MONTHLY("Monthly");

    private final String label;

    Recurrence(String label) {
        this.label = label;
    }

    @Override
    public String toString() {
        return label;
    }
}
