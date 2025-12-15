package dev.nnmod.sampleroperationcenter.application.service;

import dev.nnmod.sampleroperationcenter.application.service.interf.ITelemetryService;
import dev.nnmod.sampleroperationcenter.domain.repository.interf.IDroneRepository;
import dev.nnmod.sampleroperationcenter.domain.repository.interf.IMissionRepository;
import dev.nnmod.sampleroperationcenter.domain.repository.interf.ITelemetryRepository;
import entity.Drone;
import entity.Mission;
import entity.Telemetry;
import jakarta.ejb.EJB;
import jakarta.ejb.Stateless;
import jakarta.transaction.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;

@Stateless
public class TelemetryService implements ITelemetryService {
    @EJB
    ITelemetryRepository telemetryRepository;
    @EJB
    IMissionRepository missionRepository;
    @EJB
    IDroneRepository droneRepository;

    @Override
    public Optional<Telemetry> findByDroneId(Long droneId) {
        return telemetryRepository.findByDroneId(droneId).or(Optional::empty);
    }

    @Override
    @Transactional
    public Telemetry createTelemetry(Long missionId, String droneName, Float batteryLevel, Float altitude, Float speed,
                                     Float latitude, Float longitude, Float thrust, Float roll, Float pitch, Float yaw,
                                     LocalDateTime timestamp) {
        Mission mission = missionRepository.findById(missionId)
                .orElseThrow(() -> new IllegalArgumentException("Mission not found: " + missionId));
        Drone drone = droneRepository.findByName(droneName)
                .orElseThrow(() -> new IllegalArgumentException("Drone not found: " + droneName));
        Telemetry telemetry = new Telemetry(mission, drone, batteryLevel, altitude, speed, latitude, longitude, thrust, roll, pitch, yaw, timestamp);
        mission.addTelemetry(telemetry);
        return telemetryRepository.save(telemetry);
    }
}
