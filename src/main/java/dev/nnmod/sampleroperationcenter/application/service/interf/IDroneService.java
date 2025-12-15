package dev.nnmod.sampleroperationcenter.application.service.interf;

import entity.Drone;

import java.util.List;

public interface IDroneService {
    public List<Drone> getAllDrones();
    public Drone getOrThrow(Long id);
    public void setStatus(Long id, String status);
    public Drone createDrone(String name);
}
