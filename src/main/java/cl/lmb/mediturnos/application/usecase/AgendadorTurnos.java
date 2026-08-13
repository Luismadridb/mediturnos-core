package cl.lmb.mediturnos.application.usecase;

import cl.lmb.mediturnos.domain.entity.Medico;
import cl.lmb.mediturnos.domain.entity.Paciente;
import cl.lmb.mediturnos.domain.entity.Turno;
import cl.lmb.mediturnos.domain.exception.CapacidadExcedidaException;
import cl.lmb.mediturnos.domain.exception.MedicoNoDisponibleException;
import cl.lmb.mediturnos.domain.exception.TurnoInvalidoException;
import cl.lmb.mediturnos.domain.port.AgendaMedica;
import cl.lmb.mediturnos.domain.port.NotificadorTurno;
import cl.lmb.mediturnos.domain.repository.TurnoRepository;

import java.time.Clock;
import java.time.LocalDateTime;
import java.util.Objects;
import java.util.UUID;
import java.util.function.Supplier;

public class AgendadorTurnos {

    private final AgendaMedica agendaMedica;
    private final NotificadorTurno notificadorTurno;
    private final TurnoRepository turnoRepository;
    private final Clock clock;
    private final Supplier<String> generadorId;

    public AgendadorTurnos(AgendaMedica agendaMedica,
                            NotificadorTurno notificadorTurno,
                            TurnoRepository turnoRepository,
                            Clock clock) {
        this(agendaMedica, notificadorTurno, turnoRepository, clock, () -> UUID.randomUUID().toString());
    }

    public AgendadorTurnos(AgendaMedica agendaMedica,
                            NotificadorTurno notificadorTurno,
                            TurnoRepository turnoRepository,
                            Clock clock,
                            Supplier<String> generadorId) {
        this.agendaMedica = Objects.requireNonNull(agendaMedica, "agendaMedica es obligatoria.");
        this.notificadorTurno = Objects.requireNonNull(notificadorTurno, "notificadorTurno es obligatorio.");
        this.turnoRepository = Objects.requireNonNull(turnoRepository, "turnoRepository es obligatorio.");
        this.clock = Objects.requireNonNull(clock, "clock es obligatorio.");
        this.generadorId = Objects.requireNonNull(generadorId, "generadorId es obligatorio.");
    }

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
        Turno turno = new Turno(generadorId.get(), paciente, medico, fechaHora);
        turnoRepository.guardar(turno);
        return turno;
    }

    public Turno confirmar(Turno turno) {
        Objects.requireNonNull(turno, "El turno a confirmar no puede ser nulo.");
        turno.confirmar();
        notificadorTurno.notificarConfirmacion(turno);
        return turno;
    }

    public Turno cancelar(Turno turno) {
        Objects.requireNonNull(turno, "El turno a cancelar no puede ser nulo.");
        turno.cancelar();
        notificadorTurno.notificarCancelacion(turno);
        return turno;
    }
}
