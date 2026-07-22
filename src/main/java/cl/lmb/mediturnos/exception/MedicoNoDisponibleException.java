package cl.lmb.mediturnos.exception;

/**
 * Se lanza cuando el medico solicitado no tiene disponibilidad en la fecha/hora requerida.
 */
public class MedicoNoDisponibleException extends MediTurnosException {
    public MedicoNoDisponibleException(String message) {
        super(message);
    }
}
