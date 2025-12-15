package dev.nnmod.sampleroperationcenter.domain.repository;

import dev.nnmod.sampleroperationcenter.domain.repository.interf.IDroneRepository;
import entity.Drone;
import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

import java.util.List;
import java.util.Optional;

@Stateless
public class DroneRepository implements IDroneRepository {
    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public List<Drone> findAll() {
        return entityManager.createQuery("SELECT d FROM Drone d", Drone.class).getResultList();
    }

    @Override
    public Optional<Drone> findById(Long id) {
        return Optional.ofNullable(entityManager.find(Drone.class, id));
    }

    @Override
    public Optional<Drone> findByName(String name) {
        return entityManager.createQuery("SELECT d FROM Drone d WHERE d.name = :name", Drone.class)
                .setParameter("name", name)
                .getResultList()
                .stream()
                .findFirst();
    }

    @Override
    public Drone save(Drone drone) {
        if (drone.getId() == null) {
            entityManager.persist(drone);
            return drone;
        } else {
            return entityManager.merge(drone);
        }
    }
}
