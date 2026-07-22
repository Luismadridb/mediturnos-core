package cl.lmb.mediturnos.domain;

import cl.lmb.mediturnos.exception.TurnoInvalidoException;
import cl.lmb.mediturnos.exception.TurnoYaCanceladoException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class TurnoTest {

    private Paciente paciente;
    private Medico medico;
    private LocalDateTime fechaHora;

    @BeforeEach
    void setUp() {
        // Arrange (comun a todos los tests)
        paciente = new Paciente("P1", "Luis Madrid", "11.111.111-1");
        medico = new Medico("M1", "Dra. Fernanda Soto", Especialidad.BRONCOPULMONAR, 10);
        fechaHora = LocalDateTime.now().plusDays(1);
    }

    @Test
    void deberiaCrearTurnoEnEstadoPendiente() {
        // Arrange (usa el setUp)

        // Act
        Turno turno = new Turno("T1", paciente, medico, fechaHora);

        // Assert
        assertEquals(EstadoTurno.PENDIENTE, turno.getEstado());
        assertFalse(turno.estaCancelado());
        assertFalse(turno.estaConfirmado());
        assertEquals(paciente, turno.getPaciente());
        assertEquals(medico, turno.getMedico());
        assertEquals(fechaHora, turno.getFechaHora());
        assertEquals("T1", turno.getId());
    }

    @Test
    void deberiaLanzarExcepcionSiIdEsNuloOVacio() {
        // Arrange / Act / Assert
        assertThrows(TurnoInvalidoException.class, () -> new Turno(null, paciente, medico, fechaHora));
        assertThrows(TurnoInvalidoException.class, () -> new Turno(" ", paciente, medico, fechaHora));
    }

    @Test
    void deberiaLanzarExcepcionSiPacienteEsNulo() {
        // Act / Assert
        assertThrows(TurnoInvalidoException.class, () -> new Turno("T1", null, medico, fechaHora));
    }

    @Test
    void deberiaLanzarExcepcionSiMedicoEsNulo() {
        // Act / Assert
        assertThrows(TurnoInvalidoException.class, () -> new Turno("T1", paciente, null, fechaHora));
    }

    @Test
    void deberiaLanzarExcepcionSiFechaHoraEsNula() {
        // Act / Assert
        assertThrows(TurnoInvalidoException.class, () -> new Turno("T1", paciente, medico, null));
    }

    @Test
    void deberiaConfirmarUnTurnoPendiente() {
        // Arrange
        Turno turno = new Turno("T1", paciente, medico, fechaHora);

        // Act
        turno.confirmar();

        // Assert
        assertEquals(EstadoTurno.CONFIRMADO, turno.getEstado());
        assertTrue(turno.estaConfirmado());
    }

    @Test
    void deberiaCancelarUnTurnoPendiente() {
        // Arrange
        Turno turno = new Turno("T1", paciente, medico, fechaHora);

        // Act
        turno.cancelar();

        // Assert
        assertEquals(EstadoTurno.CANCELADO, turno.getEstado());
        assertTrue(turno.estaCancelado());
    }

    @Test
    void noDeberiaPermitirConfirmarUnTurnoYaCancelado() {
        // Arrange
        Turno turno = new Turno("T1", paciente, medico, fechaHora);
        turno.cancelar();

        // Act / Assert
        assertThrows(TurnoYaCanceladoException.class, turno::confirmar);
    }

    @Test
    void noDeberiaPermitirCancelarUnTurnoDosVeces() {
        // Arrange
        Turno turno = new Turno("T1", paciente, medico, fechaHora);
        turno.cancelar();

        // Act / Assert
        assertThrows(TurnoYaCanceladoException.class, turno::cancelar);
    }

    @Test
    void dosTurnosConElMismoIdDeberianSerIguales() {
        // Arrange
        Turno turnoA = new Turno("T1", paciente, medico, fechaHora);
        Turno turnoB = new Turno("T1", paciente, medico, fechaHora.plusHours(1));

        // Act / Assert
        assertEquals(turnoA, turnoB);
        assertEquals(turnoA.hashCode(), turnoB.hashCode());
        assertEquals(turnoA, turnoA);
        assertFalse(turnoA.equals("no-es-un-turno"));
    }
}
