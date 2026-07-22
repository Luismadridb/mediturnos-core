package cl.lmb.mediturnos.service;

import cl.lmb.mediturnos.domain.Medico;
import cl.lmb.mediturnos.domain.Paciente;
import cl.lmb.mediturnos.domain.Turno;
import cl.lmb.mediturnos.exception.CapacidadExcedidaException;
import cl.lmb.mediturnos.exception.MedicoNoDisponibleException;
import cl.lmb.mediturnos.exception.TurnoInvalidoException;
import cl.lmb.mediturnos.port.AgendaMedica;
import cl.lmb.mediturnos.port.NotificadorTurno;

import java.time.Clock;
import java.time.LocalDateTime;
import java.util.Objects;
import java.util.UUID;
import java.util.function.Supplier;

/**
 * Caso de uso central del dominio: agenda, confirma y cancela turnos medicos,
 * validando las reglas de negocio y coordinando los puertos de salida
 * (notificacion y disponibilidad) mediante inyeccion por constructor.
 *
 * No depende de ningun framework: las dependencias externas son interfaces.
 */
public class AgendadorTurnos {

    private final AgendaMedica agendaMedica;
    private final NotificadorTurno notificadorTurno;
    private final Clock clock;
    private final Supplier<String> generadorId;

    public AgendadorTurnos(AgendaMedica agendaMedica, NotificadorTurno notificadorTurno, Clock clock) {
        this(agendaMedica, notificadorTurno, clock, () -> UUID.randomUUID().toString());
    }

    public AgendadorTurnos(AgendaMedica agendaMedica,
                            NotificadorTurno notificadorTurno,
                            Clock clock,
                            Supplier<String> generadorId) {
        this.agendaMedica = Objects.requireNonNull(agendaMedica, "agendaMedica es obligatoria.");
        this.notificadorTurno = Objects.requireNonNull(notificadorTurno, "notificadorTurno es obligatorio.");
        this.clock = Objects.requireNonNull(clock, "clock es obligatorio.");
        this.generadorId = Objects.requireNonNull(generadorId, "generadorId es obligatorio.");
    }

    /**
     * Agenda un nuevo turno validando fecha, disponibilidad del medico y capacidad diaria.
     */
    public Turno agendar(Paciente paciente, Medico medico, LocalDateTime fechaHora) {
        if (paciente == null) {
            throw new TurnoInvalidoException("Debe especificar un paciente para el turno.");
        }
        if (medico == null) {
            throw new TurnoInvalidoException("Debe especificar un medico para el turno.");
        }
        if (fechaHora == null) {
            throw new TurnoInvalidoException("Debe especificar una fecha y hora para el turno.");
        }
        if (!fechaHora.isAfter(LocalDateTime.now(clock))) {
            throw new TurnoInvalidoException("No es posible agendar un turno en una fecha pasada.");
        }
        if (!agendaMedica.estaDisponible(medico, fechaHora)) {
            throw new MedicoNoDisponibleException(
                    "El medico " + medico.getNombreCompleto() + " no tiene disponibilidad en " + fechaHora);
        }
        if (agendaMedica.turnosAgendadosEnElDia(medico, fechaHora) >= medico.getCapacidadMaximaDiaria()) {
            throw new CapacidadExcedidaException(
                    "El medico " + medico.getNombreCompleto() + " alcanzo su capacidad maxima para ese dia.");
        }
        return new Turno(generadorId.get(), paciente, medico, fechaHora);
    }

    /**
     * Confirma un turno pendiente y dispara la notificacion correspondiente.
     */
    public Turno confirmar(Turno turno) {
        Objects.requireNonNull(turno, "El turno a confirmar no puede ser nulo.");
        turno.confirmar();
        notificadorTurno.notificarConfirmacion(turno);
        return turno;
    }

    /**
     * Cancela un turno y dispara la notificacion correspondiente.
     */
    public Turno cancelar(Turno turno) {
        Objects.requireNonNull(turno, "El turno a cancelar no puede ser nulo.");
        turno.cancelar();
        notificadorTurno.notificarCancelacion(turno);
        return turno;
    }
}
