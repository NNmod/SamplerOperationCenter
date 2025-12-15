package dev.nnmod.sampleroperationcenter.application.service.interf;

import entity.Operator;

import java.util.List;

public interface IOperatorService {
    public List<Operator> getAllOperators();
    public Operator getByLoginOrThrow(String login);
    public Operator createOperator(String login, String password);
}
