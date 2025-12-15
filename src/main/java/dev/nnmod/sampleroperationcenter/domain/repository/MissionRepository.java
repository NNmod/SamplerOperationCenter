package dev.nnmod.sampleroperationcenter.domain.repository;

import dev.nnmod.sampleroperationcenter.domain.repository.interf.IMissionRepository;
import entity.Mission;
import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;

import java.util.List;
import java.util.Optional;

@Stateless
public class MissionRepository implements IMissionRepository {
    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Long count() {
        return entityManager.createQuery("SELECT COUNT(m) FROM Mission m", Long.class)
                .getSingleResult();
    }

    @Override
    public Optional<Mission>findById(Long id) {
        return Optional.ofNullable(entityManager.find(Mission.class, id));
    }

    @Override
    public List<Mission> findPage(int page, int size) {
        TypedQuery<Mission> query = entityManager.createQuery("SELECT m FROM Mission m ORDER BY CASE WHEN m.finishedAt IS NULL THEN 0 ELSE 1 END ASC, m.createdAt DESC", Mission.class);
        query.setFirstResult(page * size);
        query.setMaxResults(size);
        return query.getResultList();
    }

    @Override
    public Mission save(Mission mission) {
        if (mission.getId() == null) {
            entityManager.persist(mission);
            return mission;
        } else {
            return entityManager.merge(mission);
        }
    }
}
