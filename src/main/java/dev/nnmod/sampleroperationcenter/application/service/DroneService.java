package dev.nnmod.sampleroperationcenter.application.service;

import dev.nnmod.sampleroperationcenter.application.service.interf.IDroneService;
import dev.nnmod.sampleroperationcenter.domain.repository.interf.IDroneRepository;
import entity.Drone;
import jakarta.ejb.EJB;
import jakarta.ejb.Stateless;
import jakarta.transaction.Transactional;

import java.util.List;
import java.util.Optional;

@Stateless
public class DroneService implements IDroneService {
    @EJB
    IDroneRepository droneRepository;

    @Override
    public List<Drone> getAllDrones() {
        return droneRepository.findAll();
    }

    @Override
    public Drone getOrThrow(Long id) {
        return droneRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Drone not found: " + id));
    }

    @Override
    @Transactional
    public void setStatus(Long id, String status) {
        Drone drone = droneRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Drone not found: " + id));
        drone.setStatus(status);
        droneRepository.save(drone);
    }

    @Override
    @Transactional
    public Drone createDrone(String name) {
        Optional<Drone> exDrone = droneRepository.findByName(name);
        if (exDrone.isPresent()) {
            throw new IllegalArgumentException("Drone already exists: " + name);
        }
        Drone drone = new Drone(name);
        return droneRepository.save(drone);
    }
}
