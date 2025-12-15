package entity;

import jakarta.json.bind.annotation.JsonbTransient;
import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "results")
public class Result {
    @Id
    private Long id;

    @OneToOne
    @MapsId
    @JoinColumn(name = "missionId")
    @JsonbTransient
    private Mission mission;

    private Float pH;
    private Float dissolvedOxygen;
    private Float temp;
    private Float turbidity;
    private Float conductivity;

    private String probeReport;

    private LocalDateTime createdAt;
    private LocalDateTime reportAt;

    public Result() {}

    public Result(Mission mission, Float pH, Float dissolvedOxygen, Float temp, Float turbidity, Float conductivity) {
        this.mission = mission;
        this.pH = pH;
        this.dissolvedOxygen = dissolvedOxygen;
        this.temp = temp;
        this.turbidity = turbidity;
        this.conductivity = conductivity;
        this.createdAt = LocalDateTime.now();
    }

    public Long getId() { return id; }
    public Mission getMission() { return mission; }
    public Float getpH() { return pH; }
    public Float getDissolvedOxygen() { return dissolvedOxygen; }
    public Float getTemp() { return temp; }
    public Float getTurbidity() { return turbidity; }
    public Float getConductivity() { return conductivity; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public LocalDateTime getReportAt() { return reportAt; }
    public String getProbeReport() { return probeReport; }

    public void setProbeReport(String probeReport) {
        this.probeReport = probeReport;
        this.reportAt = LocalDateTime.now();
    }
}
