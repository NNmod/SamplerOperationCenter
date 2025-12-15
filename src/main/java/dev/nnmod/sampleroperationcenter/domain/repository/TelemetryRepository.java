package dev.nnmod.sampleroperationcenter.domain.repository;

import dev.nnmod.sampleroperationcenter.domain.repository.interf.ITelemetryRepository;
import entity.Telemetry;
import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

import java.util.Optional;

@Stateless
public class TelemetryRepository implements ITelemetryRepository {
    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Optional<Telemetry> findByDroneId(Long droneId) {
        return entityManager.createQuery("SELECT t FROM Telemetry t WHERE t.drone.id = :droneId ORDER BY t.createdAt DESC", Telemetry.class)
                .setParameter("droneId", droneId)
                .getResultList()
                .stream()
                .findFirst();
    }

    @Override
    public Telemetry save(Telemetry telemetry) {
        if (telemetry.getId() == null) {
            entityManager.persist(telemetry);
            return telemetry;
        } else {
            return entityManager.merge(telemetry);
        }
    }
}
