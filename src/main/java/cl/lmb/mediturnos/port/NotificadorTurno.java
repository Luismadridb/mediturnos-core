package cl.lmb.mediturnos.port;

import cl.lmb.mediturnos.domain.Turno;

/**
 * Puerto de salida para notificar eventos de un turno (ej: SMS, email, push).
 * La implementacion concreta (infraestructura) queda fuera del dominio puro.
 */
public interface NotificadorTurno {
    void notificarConfirmacion(Turno turno);

    void notificarCancelacion(Turno turno);
}
