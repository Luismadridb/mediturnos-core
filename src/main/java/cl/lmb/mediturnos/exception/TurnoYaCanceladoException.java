package cl.lmb.mediturnos.exception;

/**
 * Se lanza cuando se intenta confirmar o cancelar un turno que ya se encuentra cancelado.
 */
public class TurnoYaCanceladoException extends MediTurnosException {
    public TurnoYaCanceladoException(String message) {
        super(message);
    }
}
