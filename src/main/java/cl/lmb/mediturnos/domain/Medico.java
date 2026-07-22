package cl.lmb.mediturnos.domain;

import java.util.Objects;

/**
 * Representa a un profesional medico habilitado para recibir turnos.
 */
public class Medico {

    private final String id;
    private final String nombreCompleto;
    private final Especialidad especialidad;
    private final int capacidadMaximaDiaria;

    public Medico(String id, String nombreCompleto, Especialidad especialidad, int capacidadMaximaDiaria) {
        if (id == null || id.isBlank()) {
            throw new IllegalArgumentException("El id del medico no puede ser nulo o vacio.");
        }
        if (nombreCompleto == null || nombreCompleto.isBlank()) {
            throw new IllegalArgumentException("El nombre del medico no puede ser nulo o vacio.");
        }
        if (especialidad == null) {
            throw new IllegalArgumentException("La especialidad del medico es obligatoria.");
        }
        if (capacidadMaximaDiaria <= 0) {
            throw new IllegalArgumentException("La capacidad maxima diaria debe ser mayor a cero.");
        }
        this.id = id;
        this.nombreCompleto = nombreCompleto;
        this.especialidad = especialidad;
        this.capacidadMaximaDiaria = capacidadMaximaDiaria;
    }

    public String getId() {
        return id;
    }

    public String getNombreCompleto() {
        return nombreCompleto;
    }

    public Especialidad getEspecialidad() {
        return especialidad;
    }

    public int getCapacidadMaximaDiaria() {
        return capacidadMaximaDiaria;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Medico medico)) return false;
        return id.equals(medico.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
