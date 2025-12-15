package dev.nnmod.sampleroperationcenter.domain.repository.interf;

import entity.Drone;
import java.util.List;
import java.util.Optional;

public interface IDroneRepository {
    public List<Drone> findAll();
    public Optional<Drone> findById(Long id);
    public Optional<Drone> findByName(String name);
    public Drone save(Drone drone);
}