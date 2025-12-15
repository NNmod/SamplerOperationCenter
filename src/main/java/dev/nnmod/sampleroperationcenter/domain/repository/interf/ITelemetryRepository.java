package dev.nnmod.sampleroperationcenter.domain.repository.interf;

import entity.Operator;
import entity.Telemetry;

import java.util.Optional;

public interface ITelemetryRepository {
    public Optional<Telemetry> findByDroneId(Long droneId);
    public Telemetry save(Telemetry telemetry);
}
