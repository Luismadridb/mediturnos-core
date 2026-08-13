package cl.lmb.mediturnos.domain.repository;

import cl.lmb.mediturnos.domain.entity.Turno;
import java.util.Optional;

public interface TurnoRepository {
    void guardar(Turno turno);
    Optional<Turno> buscarPorId(String id);
}