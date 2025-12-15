package dev.nnmod.sampleroperationcenter.application.service;

import dev.nnmod.sampleroperationcenter.application.service.interf.IOperatorService;
import dev.nnmod.sampleroperationcenter.domain.repository.interf.IOperatorRepository;
import entity.Operator;
import jakarta.ejb.EJB;
import jakarta.ejb.Stateless;
import jakarta.inject.Inject;
import jakarta.security.enterprise.identitystore.Pbkdf2PasswordHash;
import jakarta.transaction.Transactional;

import java.util.List;
import java.util.Optional;

@Stateless
public class OperatorService implements IOperatorService {
    @EJB
    IOperatorRepository operatorRepository;

    @Inject
    Pbkdf2PasswordHash passwordHash;

    @Override
    public List<Operator> getAllOperators() {
        return operatorRepository.findAll();
    }

    @Override
    public Operator getByLoginOrThrow(String login) {
        return operatorRepository.findByLogin(login)
                .orElseThrow(() -> new IllegalArgumentException("User not found: " + login));
    }

    @Override
    @Transactional
    public Operator createOperator(String login, String password) {
        Optional<Operator> exOperator = operatorRepository.findByLogin(login);
        if (exOperator.isPresent()) {
            throw new IllegalArgumentException("User already exists: " + login);
        }
        String encodedPassword = passwordHash.generate(password.toCharArray());
        Operator operator = new Operator(login, encodedPassword);
        return operatorRepository.save(operator);
    }
}
