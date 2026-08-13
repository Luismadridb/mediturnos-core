package cl.lmb.mediturnos.domain.valueobject;

import cl.lmb.mediturnos.domain.exception.TurnoInvalidoException;

public record Rut(String valor) {

    public Rut {
        if (valor == null || valor.isBlank()) {
            throw new TurnoInvalidoException("El rut no puede ser nulo o vacio.");
        }
        String limpio = valor.trim().toUpperCase().replace(".", "");
        if (!limpio.matches("^\\d{7,8}-[0-9K]$")) {
            throw new TurnoInvalidoException("Formato de rut invalido: " + valor);
        }
        valor = limpio;
    }
}