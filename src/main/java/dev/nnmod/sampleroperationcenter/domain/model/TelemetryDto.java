package dev.nnmod.sampleroperationcenter.domain.model;

import java.time.LocalDateTime;

public class TelemetryDto {
    public Long missionId;
    public String drone;
    public Float batteryLevel;
    public Float altitude;
    public Float speed;
    public Float latitude;
    public Float longitude;
    public Float thrust;
    public Float roll;
    public Float pitch;
    public Float yaw;
    public LocalDateTime timestamp;
    public ProbeDto probe;
    public Boolean isLanded;
}