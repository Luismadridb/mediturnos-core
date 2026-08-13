package cl.lmb.mediturnos.infrastructure.persistence;

import cl.lmb.mediturnos.domain.entity.Turno;
import cl.lmb.mediturnos.domain.repository.TurnoRepository;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

public class TurnoRepositoryEnMemoria implements TurnoRepository {
    private final Map<String, Turno> almacen = new ConcurrentHashMap<>();

    @Override
    public void guardar(Turno turno) {
        almacen.put(turno.getId(), turno);
    }

    @Override
    public Optional<Turno> buscarPorId(String id) {
        return Optional.ofNullable(almacen.get(id));
    }
}