package cl.lmb.mediturnos.domain;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;

class PacienteTest {

    @Test
    void deberiaCrearPacienteValido() {
        // Act
        Paciente paciente = new Paciente("P1", "Luis Madrid", "11.111.111-1");

        // Assert
        assertEquals("P1", paciente.getId());
        assertEquals("Luis Madrid", paciente.getNombreCompleto());
        assertEquals("11.111.111-1", paciente.getRut());
    }

    @Test
    void deberiaLanzarExcepcionSiIdEsNuloOVacio() {
        assertThrows(IllegalArgumentException.class, () -> new Paciente(null, "Luis Madrid", "11.111.111-1"));
        assertThrows(IllegalArgumentException.class, () -> new Paciente(" ", "Luis Madrid", "11.111.111-1"));
    }

    @Test
    void deberiaLanzarExcepcionSiNombreEsNuloOVacio() {
        assertThrows(IllegalArgumentException.class, () -> new Paciente("P1", null, "11.111.111-1"));
        assertThrows(IllegalArgumentException.class, () -> new Paciente("P1", " ", "11.111.111-1"));
    }

    @Test
    void deberiaLanzarExcepcionSiRutEsNuloOVacio() {
        assertThrows(IllegalArgumentException.class, () -> new Paciente("P1", "Luis Madrid", null));
        assertThrows(IllegalArgumentException.class, () -> new Paciente("P1", "Luis Madrid", " "));
    }

    @Test
    void dosPacientesConElMismoIdDeberianSerIguales() {
        // Arrange
        Paciente pacienteA = new Paciente("P1", "Luis Madrid", "11.111.111-1");
        Paciente pacienteB = new Paciente("P1", "Otro Nombre", "22.222.222-2");

        // Act / Assert
        assertEquals(pacienteA, pacienteB);
        assertEquals(pacienteA.hashCode(), pacienteB.hashCode());
        assertEquals(pacienteA, pacienteA);
        assertFalse(pacienteA.equals("no-es-un-paciente"));
    }
}
