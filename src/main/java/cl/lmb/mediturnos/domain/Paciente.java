package cl.lmb.mediturnos.domain;

import java.util.Objects;

/**
 * Representa a un paciente dentro del sistema de agendamiento MediTurnos.
 * Entidad de dominio pura, sin dependencias de infraestructura.
 */
public class Paciente {

    private final String id;
    private final String nombreCompleto;
    private final String rut;

    public Paciente(String id, String nombreCompleto, String rut) {
        if (id == null || id.isBlank()) {
            throw new IllegalArgumentException("El id del paciente no puede ser nulo o vacio.");
        }
        if (nombreCompleto == null || nombreCompleto.isBlank()) {
            throw new IllegalArgumentException("El nombre del paciente no puede ser nulo o vacio.");
        }
        if (rut == null || rut.isBlank()) {
            throw new IllegalArgumentException("El rut del paciente no puede ser nulo o vacio.");
        }
        this.id = id;
        this.nombreCompleto = nombreCompleto;
        this.rut = rut;
    }

    public String getId() {
        return id;
    }

    public String getNombreCompleto() {
        return nombreCompleto;
    }

    public String getRut() {
        return rut;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Paciente paciente)) return false;
        return id.equals(paciente.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
