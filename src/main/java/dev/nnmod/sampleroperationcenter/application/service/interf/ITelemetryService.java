package dev.nnmod.sampleroperationcenter.application.service.interf;

import entity.Telemetry;

import java.time.LocalDateTime;
import java.util.Optional;

public interface ITelemetryService {
    public Optional<Telemetry> findByDroneId(Long droneId);
    public Telemetry createTelemetry(Long missionId, String droneName, Float batteryLevel, Float altitude, Float speed,
                                     Float latitude, Float longitude, Float thrust, Float roll, Float pitch, Float yaw, LocalDateTime timestamp);
}
