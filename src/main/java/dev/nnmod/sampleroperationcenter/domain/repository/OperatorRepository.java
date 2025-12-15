package dev.nnmod.sampleroperationcenter.domain.repository;

import dev.nnmod.sampleroperationcenter.domain.repository.interf.IOperatorRepository;
import entity.Operator;
import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

import java.util.List;
import java.util.Optional;

@Stateless
public class OperatorRepository implements IOperatorRepository {
    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public List<Operator> findAll() {
        return entityManager.createQuery("SELECT o FROM Operator o", Operator.class).getResultList();
    }

    @Override
    public Optional<Operator> findById(Long id) {
        return Optional.ofNullable(entityManager.find(Operator.class, id));
    }

    @Override
    public Optional<Operator> findByLogin(String login) {
        return entityManager.createQuery("SELECT o FROM Operator o WHERE o.login = :login", Operator.class)
                .setParameter("login", login)
                .getResultList()
                .stream()
                .findFirst();
    }

    @Override
    public Operator save(Operator operator) {
        if (operator.getId() == null) {
            entityManager.persist(operator);
            return operator;
        } else {
            return entityManager.merge(operator);
        }
    }
}
