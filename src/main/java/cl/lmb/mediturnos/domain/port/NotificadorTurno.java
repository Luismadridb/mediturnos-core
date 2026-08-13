package cl.lmb.mediturnos.domain.port;

import cl.lmb.mediturnos.domain.entity.Turno;

public interface NotificadorTurno {
    void notificarConfirmacion(Turno turno);

    void notificarCancelacion(Turno turno);
}