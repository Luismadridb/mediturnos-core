package cl.lmb.mediturnos.domain.entity;

import cl.lmb.mediturnos.domain.exception.TurnoInvalidoException;
import cl.lmb.mediturnos.domain.valueobject.Rut;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;

class PacienteTest {

    @Test
    void deberiaCrearPacienteValido() {
        // Act
        Paciente paciente = new Paciente("P1", "Luis Madrid", new Rut("11.111.111-1"));

        // Assert
        assertEquals("P1", paciente.getId());
        assertEquals("Luis Madrid", paciente.getNombreCompleto());
        assertEquals("11111111-1", paciente.getRut().valor());
    }

    @Test
    void deberiaLanzarExcepcionSiIdEsNuloOVacio() {
        Rut rutValido = new Rut("11.111.111-1");
        assertThrows(IllegalArgumentException.class, () -> new Paciente(null, "Luis Madrid", rutValido));
        assertThrows(IllegalArgumentException.class, () -> new Paciente(" ", "Luis Madrid", rutValido));
    }

    @Test
    void deberiaLanzarExcepcionSiNombreEsNuloOVacio() {
        Rut rutValido = new Rut("11.111.111-1");
        assertThrows(IllegalArgumentException.class, () -> new Paciente("P1", null, rutValido));
        assertThrows(IllegalArgumentException.class, () -> new Paciente("P1", " ", rutValido));
    }

    @Test
    void deberiaLanzarExcepcionSiRutEsNulo() {
        assertThrows(IllegalArgumentException.class, () -> new Paciente("P1", "Luis Madrid", null));
    }

    @Test
    void dosPacientesConElMismoIdDeberianSerIguales() {
        // Arrange
        Paciente pacienteA = new Paciente("P1", "Luis Madrid", new Rut("11.111.111-1"));
        Paciente pacienteB = new Paciente("P1", "Otro Nombre", new Rut("22.222.222-2"));

        // Act / Assert
        assertEquals(pacienteA, pacienteB);
        assertEquals(pacienteA.hashCode(), pacienteB.hashCode());
        assertEquals(pacienteA, pacienteA);
        assertFalse(pacienteA.equals("no-es-un-paciente"));
    }
}