package cl.lmb.mediturnos.domain.port;

import cl.lmb.mediturnos.domain.entity.Medico;

import java.time.LocalDateTime;

public interface AgendaMedica {
    boolean estaDisponible(Medico medico, LocalDateTime fechaHora);

    int turnosAgendadosEnElDia(Medico medico, LocalDateTime fechaHora);
}