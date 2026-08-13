package cl.lmb.mediturnos.application.usecase;

import cl.lmb.mediturnos.domain.entity.Especialidad;
import cl.lmb.mediturnos.domain.entity.Medico;
import cl.lmb.mediturnos.domain.entity.Paciente;
import cl.lmb.mediturnos.domain.entity.Turno;
import cl.lmb.mediturnos.domain.exception.CapacidadExcedidaException;
import cl.lmb.mediturnos.domain.exception.MedicoNoDisponibleException;
import cl.lmb.mediturnos.domain.exception.TurnoInvalidoException;
import cl.lmb.mediturnos.domain.exception.TurnoYaCanceladoException;
import cl.lmb.mediturnos.domain.port.AgendaMedica;
import cl.lmb.mediturnos.domain.port.NotificadorTurno;
import cl.lmb.mediturnos.domain.repository.TurnoRepository;
import cl.lmb.mediturnos.domain.valueobject.Rut;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Clock;
import java.time.LocalDateTime;
import java.time.ZoneId;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AgendadorTurnosTest {

    private static final ZoneId ZONA = ZoneId.of("America/Santiago");
    private static final LocalDateTime AHORA = LocalDateTime.of(2026, 7, 21, 9, 0);

    @Mock
    private AgendaMedica agendaMedica;

    @Mock
    private NotificadorTurno notificadorTurno;

    @Mock
    private TurnoRepository turnoRepository;

    private Clock clock;
    private Paciente paciente;
    private Medico medico;
    private AgendadorTurnos agendador;

    @BeforeEach
    void setUp() {
        // Arrange comun: reloj fijo para tener fechas deterministas en los tests
        clock = Clock.fixed(AHORA.atZone(ZONA).toInstant(), ZONA);
        paciente = new Paciente("P1", "Luis Madrid", new Rut("11.111.111-1"));
        medico = new Medico("M1", "Dra. Fernanda Soto", Especialidad.BRONCOPULMONAR, 2);
        agendador = new AgendadorTurnos(agendaMedica, notificadorTurno, turnoRepository, clock, () -> "T-FIJO");
    }

    @Test
    void deberiaAgendarUnTurnoCuandoHayDisponibilidadYCapacidad() {
        // Arrange
        LocalDateTime fecha = AHORA.plusDays(1);
        when(agendaMedica.estaDisponible(medico, fecha)).thenReturn(true);
        when(agendaMedica.turnosAgendadosEnElDia(medico, fecha)).thenReturn(0);

        // Act
        Turno turno = agendador.agendar(paciente, medico, fecha);

        // Assert
        assertNotNull(turno);
        assertEquals("T-FIJO", turno.getId());
        assertEquals(paciente, turno.getPaciente());
        assertEquals(medico, turno.getMedico());
        verify(agendaMedica, times(1)).estaDisponible(medico, fecha);
        verify(agendaMedica, times(1)).turnosAgendadosEnElDia(medico, fecha);
        verify(turnoRepository, times(1)).guardar(turno);
    }

    @Test
    void noDeberiaAgendarSiElPacienteEsNulo() {
        // Act / Assert
        assertThrows(TurnoInvalidoException.class,
                () -> agendador.agendar(null, medico, AHORA.plusDays(1)));
        verify(agendaMedica, never()).estaDisponible(any(), any());
        verify(turnoRepository, never()).guardar(any());
    }

    @Test
    void noDeberiaAgendarSiElMedicoEsNulo() {
        // Act / Assert
        assertThrows(TurnoInvalidoException.class,
                () -> agendador.agendar(paciente, null, AHORA.plusDays(1)));
        verify(agendaMedica, never()).estaDisponible(any(), any());
    }

    @Test
    void noDeberiaAgendarSiLaFechaEsNula() {
        // Act / Assert
        assertThrows(TurnoInvalidoException.class,
                () -> agendador.agendar(paciente, medico, null));
        verify(agendaMedica, never()).estaDisponible(any(), any());
    }

    @Test
    void noDeberiaAgendarUnTurnoEnElPasado() {
        // Arrange
        LocalDateTime fechaPasada = AHORA.minusDays(1);

        // Act / Assert
        assertThrows(TurnoInvalidoException.class,
                () -> agendador.agendar(paciente, medico, fechaPasada));
        verify(agendaMedica, never()).estaDisponible(any(), any());
    }

    @Test
    void noDeberiaAgendarUnTurnoExactamenteEnElInstanteActual() {
        // Act / Assert: el limite no es inclusivo, "ahora" no es una fecha futura valida
        assertThrows(TurnoInvalidoException.class,
                () -> agendador.agendar(paciente, medico, AHORA));
    }

    @Test
    void noDeberiaAgendarSiElMedicoNoTieneDisponibilidad() {
        // Arrange
        LocalDateTime fecha = AHORA.plusDays(1);
        when(agendaMedica.estaDisponible(medico, fecha)).thenReturn(false);

        // Act / Assert
        assertThrows(MedicoNoDisponibleException.class,
                () -> agendador.agendar(paciente, medico, fecha));
        verify(agendaMedica, never()).turnosAgendadosEnElDia(any(), any());
        verify(turnoRepository, never()).guardar(any());
    }

    @Test
    void noDeberiaAgendarSiElMedicoAlcanzoSuCapacidadMaximaDiaria() {
        // Arrange
        LocalDateTime fecha = AHORA.plusDays(1);
        when(agendaMedica.estaDisponible(medico, fecha)).thenReturn(true);
        when(agendaMedica.turnosAgendadosEnElDia(medico, fecha)).thenReturn(2); // capacidad = 2

        // Act / Assert
        assertThrows(CapacidadExcedidaException.class,
                () -> agendador.agendar(paciente, medico, fecha));
        verify(turnoRepository, never()).guardar(any());
    }

    @Test
    void deberiaConfirmarUnTurnoYNotificar() {
        // Arrange
        Turno turno = new Turno("T1", paciente, medico, AHORA.plusDays(1));

        // Act
        Turno resultado = agendador.confirmar(turno);

        // Assert
        assertEquals(turno, resultado);
        verify(notificadorTurno, times(1)).notificarConfirmacion(turno);
        verify(notificadorTurno, never()).notificarCancelacion(any());
    }

    @Test
    void noDeberiaConfirmarUnTurnoNulo() {
        // Act / Assert
        assertThrows(NullPointerException.class, () -> agendador.confirmar(null));
        verify(notificadorTurno, never()).notificarConfirmacion(any());
    }

    @Test
    void noDeberiaConfirmarUnTurnoYaCanceladoYNoDebeNotificar() {
        // Arrange
        Turno turno = new Turno("T1", paciente, medico, AHORA.plusDays(1));
        turno.cancelar();

        // Act / Assert
        assertThrows(TurnoYaCanceladoException.class, () -> agendador.confirmar(turno));
        verify(notificadorTurno, never()).notificarConfirmacion(any());
    }

    @Test
    void deberiaCancelarUnTurnoYNotificar() {
        // Arrange
        Turno turno = new Turno("T1", paciente, medico, AHORA.plusDays(1));

        // Act
        Turno resultado = agendador.cancelar(turno);

        // Assert
        assertEquals(turno, resultado);
        verify(notificadorTurno, times(1)).notificarCancelacion(turno);
        verify(notificadorTurno, never()).notificarConfirmacion(any());
    }

    @Test
    void noDeberiaCancelarUnTurnoNulo() {
        // Act / Assert
        assertThrows(NullPointerException.class, () -> agendador.cancelar(null));
        verify(notificadorTurno, never()).notificarCancelacion(any());
    }

    @Test
    void noDeberiaCancelarUnTurnoYaCanceladoYNoDebeNotificarDeNuevo() {
        // Arrange
        Turno turno = new Turno("T1", paciente, medico, AHORA.plusDays(1));
        turno.cancelar();

        // Act / Assert
        assertThrows(TurnoYaCanceladoException.class, () -> agendador.cancelar(turno));
        verify(notificadorTurno, never()).notificarCancelacion(any());
    }

    @Test
    void constructorSimplificadoDeberiaGenerarUnIdNoNuloAlAgendar() {
        // Arrange: se usa el constructor de 4 argumentos (generador de id por defecto)
        AgendadorTurnos agendadorPorDefecto =
                new AgendadorTurnos(agendaMedica, notificadorTurno, turnoRepository, clock);
        LocalDateTime fecha = AHORA.plusDays(1);
        when(agendaMedica.estaDisponible(medico, fecha)).thenReturn(true);
        when(agendaMedica.turnosAgendadosEnElDia(medico, fecha)).thenReturn(0);

        // Act
        Turno turno = agendadorPorDefecto.agendar(paciente, medico, fecha);

        // Assert
        assertNotNull(turno.getId());
    }

    @Test
    void deberiaRechazarDependenciasNulasEnElConstructor() {
        assertThrows(NullPointerException.class,
                () -> new AgendadorTurnos(null, notificadorTurno, turnoRepository, clock));
        assertThrows(NullPointerException.class,
                () -> new AgendadorTurnos(agendaMedica, null, turnoRepository, clock));
        assertThrows(NullPointerException.class,
                () -> new AgendadorTurnos(agendaMedica, notificadorTurno, null, clock));
        assertThrows(NullPointerException.class,
                () -> new AgendadorTurnos(agendaMedica, notificadorTurno, turnoRepository, null));
        assertThrows(NullPointerException.class,
                () -> new AgendadorTurnos(agendaMedica, notificadorTurno, turnoRepository, clock, null));
    }
}