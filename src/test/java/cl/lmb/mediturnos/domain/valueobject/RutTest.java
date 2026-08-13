package cl.lmb.mediturnos.domain.valueobject;

import cl.lmb.mediturnos.domain.exception.TurnoInvalidoException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class RutTest {

    @Test
    void deberiaCrearRutValidoYNormalizarFormato() {
        Rut rut = new Rut("11.111.111-1");
        assertEquals("11111111-1", rut.valor());
    }

    @Test
    void deberiaAceptarDigitoVerificadorK() {
        Rut rut = new Rut("9999999-k");
        assertEquals("9999999-K", rut.valor());
    }

    @Test
    void deberiaLanzarExcepcionSiEsNuloOVacio() {
        assertThrows(TurnoInvalidoException.class, () -> new Rut(null));
        assertThrows(TurnoInvalidoException.class, () -> new Rut(" "));
    }

    @Test
    void deberiaLanzarExcepcionSiFormatoEsInvalido() {
        assertThrows(TurnoInvalidoException.class, () -> new Rut("123"));
        assertThrows(TurnoInvalidoException.class, () -> new Rut("11111111"));
        assertThrows(TurnoInvalidoException.class, () -> new Rut("abcdefgh-1"));
    }

    @Test
    void dosRutsConElMismoValorDeberianSerIguales() {
        Rut rutA = new Rut("11.111.111-1");
        Rut rutB = new Rut("11111111-1");
        assertEquals(rutA, rutB);
        assertEquals(rutA.hashCode(), rutB.hashCode());
}

    @Test
    void toStringDeberiaContenerElValor() {
        Rut rut = new Rut("11.111.111-1");
        assertTrue(rut.toString().contains("11111111-1"));
}
}