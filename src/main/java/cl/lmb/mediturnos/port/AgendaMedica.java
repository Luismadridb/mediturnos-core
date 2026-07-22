package cl.lmb.mediturnos.port;

import cl.lmb.mediturnos.domain.Medico;

import java.time.LocalDateTime;

/**
 * Puerto de salida que consulta la disponibilidad real de un medico
 * (agenda, base de datos, sistema externo, etc.). El dominio solo conoce el contrato.
 */
public interface AgendaMedica {
    boolean estaDisponible(Medico medico, LocalDateTime fechaHora);

    int turnosAgendadosEnElDia(Medico medico, LocalDateTime fechaHora);
}
