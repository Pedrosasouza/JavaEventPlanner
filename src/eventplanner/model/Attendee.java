package eventplanner.model;

import java.io.Serializable;

/**
 * Classe que representa um participante de um evento.
 * Encapsulamento: atributos privados com getters.
 */
public class Attendee implements Serializable {
    private String name;
    private String email;

    public Attendee(String name, String email) {
        this.name = name;
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    @Override
    public String toString() {
        if (email == null || email.trim().isEmpty()) {
            return name;
        }
        return name + " <" + email + ">";
    }
}
