package cl.lmb.mediturnos.domain.exception;
/**
 * Se lanza cuando un medico ya alcanzo su capacidad maxima de turnos para el dia solicitado.
 */
public class CapacidadExcedidaException extends MediTurnosException {
    public CapacidadExcedidaException(String message) {
        super(message);
    }
}
