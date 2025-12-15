package dev.nnmod.sampleroperationcenter.domain.repository.interf;

import entity.Operator;

import java.util.List;
import java.util.Optional;

public interface IOperatorRepository {
    public List<Operator> findAll();
    public Optional<Operator> findById(Long id);
    public Optional<Operator> findByLogin(String login);
    public Operator save(Operator operator);
}
