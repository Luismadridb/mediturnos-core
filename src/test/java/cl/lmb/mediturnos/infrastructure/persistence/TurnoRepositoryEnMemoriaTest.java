package cl.lmb.mediturnos.infrastructure.persistence;

import cl.lmb.mediturnos.domain.entity.Especialidad;
import cl.lmb.mediturnos.domain.entity.Medico;
import cl.lmb.mediturnos.domain.entity.Paciente;
import cl.lmb.mediturnos.domain.entity.Turno;
import cl.lmb.mediturnos.domain.valueobject.Rut;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class TurnoRepositoryEnMemoriaTest {

    private TurnoRepositoryEnMemoria repositorio;
    private Turno turno;

    @BeforeEach
    void setUp() {
        repositorio = new TurnoRepositoryEnMemoria();
        Paciente paciente = new Paciente("P1", "Luis Madrid", new Rut("11.111.111-1"));
        Medico medico = new Medico("M1", "Dra. Fernanda Soto", Especialidad.BRONCOPULMONAR, 10);
        turno = new Turno("T1", paciente, medico, LocalDateTime.now().plusDays(1));
    }

    @Test
    void deberiaGuardarYRecuperarUnTurnoPorId() {
        // Act
        repositorio.guardar(turno);
        Optional<Turno> resultado = repositorio.buscarPorId("T1");

        // Assert
        assertTrue(resultado.isPresent());
        assertEquals(turno, resultado.get());
    }

    @Test
    void deberiaRetornarVacioSiElTurnoNoExiste() {
        // Act
        Optional<Turno> resultado = repositorio.buscarPorId("NO-EXISTE");

        // Assert
        assertTrue(resultado.isEmpty());
    }
}