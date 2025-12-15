package dev.nnmod.sampleroperationcenter.application.service.interf;

import entity.Mission;

import java.time.LocalDateTime;
import java.util.List;

public interface IMissionService {
    public Long countAll();
    public List<Mission> getPage(int page, int size);
    public Mission getOrThrow(Long id);
    public Mission setFinishedAt(Long id, LocalDateTime finishedAt);
    public Mission setResult(Long id, Float pH, Float DO, Float temp, Float turbidity, Float conductivity);
    public Mission setProbeReport(Long id, String probeReport);
    public Mission createMission(String operatorLogin, String droneName, Float latitude, Float longitude);
    public Mission startMission(Long id);
}
