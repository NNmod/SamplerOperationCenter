package entity;

import jakarta.json.bind.annotation.JsonbTransient;
import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "telemetries")
public class Telemetry {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "missionId")
    @JsonbTransient
    private Mission mission;

    @ManyToOne
    @JoinColumn(name = "droneId")
    @JsonbTransient
    private Drone drone;

    private LocalDateTime createdAt;

    private Float batteryLevel;
    private Float altitude;
    private Float speed;
    private Float latitude;
    private Float longitude;
    private Float thrust;
    private Float roll;
    private Float pitch;
    private Float yaw;

    public Telemetry() {}

    public Telemetry(Mission mission, Drone drone, Float batteryLevel, Float altitude, Float speed, Float latitude, Float longitude, Float thrust, Float roll, Float pitch, Float yaw, LocalDateTime timestamp) {
        this.mission = mission;
        this.drone = drone;
        this.batteryLevel = batteryLevel;
        this.altitude = altitude;
        this.speed = speed;
        this.latitude = latitude;
        this.longitude = longitude;
        this.thrust = thrust;
        this.roll = roll;
        this.pitch = pitch;
        this.yaw = yaw;
        this.createdAt =timestamp;
    }

    public Long getId() { return id; }
    public Mission getMission() { return mission; }
    public Drone getDrone() { return drone; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public Float getBatteryLevel() { return batteryLevel; }
    public Float getAltitude() { return altitude; }
    public Float getSpeed() { return speed; }
    public Float getLatitude() { return latitude; }
    public Float getLongitude() { return longitude; }
    public Float getThrust() { return thrust; }
    public Float getRoll() { return roll; }
    public Float getPitch() { return pitch; }
    public Float getYaw() { return yaw; }
}
