package cl.lmb.mediturnos.domain;

import cl.lmb.mediturnos.exception.TurnoInvalidoException;
import cl.lmb.mediturnos.exception.TurnoYaCanceladoException;

import java.time.LocalDateTime;
import java.util.Objects;

/**
 * Turno medico agendado entre un {@link Paciente} y un {@link Medico}.
 * Concentra las reglas de transicion de estado (PENDIENTE -> CONFIRMADO / CANCELADO).
 */
public class Turno {

    private final String id;
    private final Paciente paciente;
    private final Medico medico;
    private final LocalDateTime fechaHora;
    private EstadoTurno estado;

    public Turno(String id, Paciente paciente, Medico medico, LocalDateTime fechaHora) {
        if (id == null || id.isBlank()) {
            throw new TurnoInvalidoException("El id del turno no puede ser nulo o vacio.");
        }
        if (paciente == null) {
            throw new TurnoInvalidoException("El turno requiere un paciente.");
        }
        if (medico == null) {
            throw new TurnoInvalidoException("El turno requiere un medico.");
        }
        if (fechaHora == null) {
            throw new TurnoInvalidoException("El turno requiere una fecha y hora.");
        }
        this.id = id;
        this.paciente = paciente;
        this.medico = medico;
        this.fechaHora = fechaHora;
        this.estado = EstadoTurno.PENDIENTE;
    }

    public void confirmar() {
        if (estado == EstadoTurno.CANCELADO) {
            throw new TurnoYaCanceladoException(
                    "No se puede confirmar el turno " + id + " porque ya fue cancelado.");
        }
        this.estado = EstadoTurno.CONFIRMADO;
    }

    public void cancelar() {
        if (estado == EstadoTurno.CANCELADO) {
            throw new TurnoYaCanceladoException(
                    "El turno " + id + " ya se encontraba cancelado.");
        }
        this.estado = EstadoTurno.CANCELADO;
    }

    public boolean estaCancelado() {
        return estado == EstadoTurno.CANCELADO;
    }

    public boolean estaConfirmado() {
        return estado == EstadoTurno.CONFIRMADO;
    }

    public String getId() {
        return id;
    }

    public Paciente getPaciente() {
        return paciente;
    }

    public Medico getMedico() {
        return medico;
    }

    public LocalDateTime getFechaHora() {
        return fechaHora;
    }

    public EstadoTurno getEstado() {
        return estado;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Turno turno)) return false;
        return id.equals(turno.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
