package entity;

import jakarta.json.bind.annotation.JsonbTransient;
import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "missions")
public class Mission {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "operatorId")
    @JsonbTransient
    private Operator operator;

    @ManyToOne
    @JoinColumn(name = "droneId")
    @JsonbTransient
    private Drone drone;

    @OneToMany(mappedBy = "mission", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Telemetry> telemetries;

    private Float latitude;
    private Float longitude;

    private LocalDateTime createdAt;

    private LocalDateTime startedAt;

    @Column(nullable = true)
    private LocalDateTime probeAt;

    @Column(nullable = true)
    private LocalDateTime finishedAt;

    @OneToOne
    @JoinColumn(name = "resultId", nullable = true)
    @JsonbTransient
    private Result result;

    public Mission() {}

    public Mission(Operator operator, Drone drone, Float latitude, Float longitude) {
        this.operator = operator;
        this.drone = drone;
        this.latitude = latitude;
        this.longitude = longitude;
        this.createdAt = LocalDateTime.now();
    }

    public Long getId() { return id; }
    public Operator getOperator() { return operator; }
    public Drone getDrone() { return drone; }
    public List<Telemetry> getTelemetries() { return telemetries; }
    public Float getLatitude() { return latitude; }
    public Float getLongitude() { return longitude; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public LocalDateTime getStartedAt() { return startedAt; }
    public LocalDateTime getProbeAt() { return probeAt; }
    public LocalDateTime getFinishedAt() { return finishedAt; }
    public Result getResult() { return result; }

    public void addTelemetry(Telemetry t) { telemetries.add(t); }

    public void setStartedAt(LocalDateTime startedAt) { this.startedAt = startedAt; }

    public void setProbeAt(LocalDateTime probeAt) {
        this.probeAt = probeAt;
    }

    public void setFinishedAt(LocalDateTime finishedAt) {
        this.finishedAt = finishedAt;
    }

    public void setResult(Result result) {
        this.result = result;
    }
}
