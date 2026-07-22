package cl.lmb.mediturnos.domain;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;

class MedicoTest {

    @Test
    void deberiaCrearMedicoValido() {
        // Act
        Medico medico = new Medico("M1", "Dra. Fernanda Soto", Especialidad.BRONCOPULMONAR, 10);

        // Assert
        assertEquals("M1", medico.getId());
        assertEquals("Dra. Fernanda Soto", medico.getNombreCompleto());
        assertEquals(Especialidad.BRONCOPULMONAR, medico.getEspecialidad());
        assertEquals(10, medico.getCapacidadMaximaDiaria());
    }

    @Test
    void deberiaLanzarExcepcionSiIdEsNuloOVacio() {
        assertThrows(IllegalArgumentException.class,
                () -> new Medico(null, "Dra. Soto", Especialidad.BRONCOPULMONAR, 10));
        assertThrows(IllegalArgumentException.class,
                () -> new Medico(" ", "Dra. Soto", Especialidad.BRONCOPULMONAR, 10));
    }

    @Test
    void deberiaLanzarExcepcionSiNombreEsNuloOVacio() {
        assertThrows(IllegalArgumentException.class,
                () -> new Medico("M1", null, Especialidad.BRONCOPULMONAR, 10));
        assertThrows(IllegalArgumentException.class,
                () -> new Medico("M1", " ", Especialidad.BRONCOPULMONAR, 10));
    }

    @Test
    void deberiaLanzarExcepcionSiEspecialidadEsNula() {
        assertThrows(IllegalArgumentException.class,
                () -> new Medico("M1", "Dra. Soto", null, 10));
    }

    @Test
    void deberiaLanzarExcepcionSiCapacidadMaximaNoEsPositiva() {
        assertThrows(IllegalArgumentException.class,
                () -> new Medico("M1", "Dra. Soto", Especialidad.BRONCOPULMONAR, 0));
        assertThrows(IllegalArgumentException.class,
                () -> new Medico("M1", "Dra. Soto", Especialidad.BRONCOPULMONAR, -1));
    }

    @Test
    void dosMedicosConElMismoIdDeberianSerIguales() {
        // Arrange
        Medico medicoA = new Medico("M1", "Dra. Soto", Especialidad.BRONCOPULMONAR, 10);
        Medico medicoB = new Medico("M1", "Dr. Otro", Especialidad.CARDIOLOGIA, 5);

        // Act / Assert
        assertEquals(medicoA, medicoB);
        assertEquals(medicoA.hashCode(), medicoB.hashCode());
        assertEquals(medicoA, medicoA);
        assertFalse(medicoA.equals("no-es-un-medico"));
    }
}
