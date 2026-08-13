package cl.lmb.mediturnos.domain.entity;

import cl.lmb.mediturnos.domain.valueobject.Rut;

import java.util.Objects;

public class Paciente {

    private final String id;
    private final String nombreCompleto;
    private final Rut rut;

    public Paciente(String id, String nombreCompleto, Rut rut) {
        if (id == null || id.isBlank()) {
            throw new IllegalArgumentException("El id del paciente no puede ser nulo o vacio.");
        }
        if (nombreCompleto == null || nombreCompleto.isBlank()) {
            throw new IllegalArgumentException("El nombre del paciente no puede ser nulo o vacio.");
        }
        if (rut == null) {
            throw new IllegalArgumentException("El rut del paciente es obligatorio.");
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

    public Rut getRut() {
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