package cl.lmb.mediturnos.exception;

/**
 * Excepcion base para todas las reglas de negocio del dominio MediTurnos.
 */
public abstract class MediTurnosException extends RuntimeException {
    protected MediTurnosException(String message) {
        super(message);
    }
}
