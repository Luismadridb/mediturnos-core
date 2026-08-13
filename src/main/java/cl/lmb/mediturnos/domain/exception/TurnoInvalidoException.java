package cl.lmb.mediturnos.domain.exception;
/**
 * Se lanza cuando los datos de un turno no cumplen las reglas minimas de negocio
 * (fecha en el pasado, campos obligatorios ausentes, etc.).
 */
public class TurnoInvalidoException extends MediTurnosException {
    public TurnoInvalidoException(String message) {
        super(message);
    }
}
