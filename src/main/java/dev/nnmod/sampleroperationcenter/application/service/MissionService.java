package dev.nnmod.sampleroperationcenter.application.service;

import dev.nnmod.sampleroperationcenter.application.service.interf.IMissionService;
import dev.nnmod.sampleroperationcenter.domain.beans.KafkaProducerBean;
import dev.nnmod.sampleroperationcenter.domain.repository.interf.IDroneRepository;
import dev.nnmod.sampleroperationcenter.domain.repository.interf.IMissionRepository;
import dev.nnmod.sampleroperationcenter.domain.repository.interf.IOperatorRepository;
import entity.Drone;
import entity.Mission;
import entity.Operator;
import entity.Result;
import jakarta.ejb.EJB;
import jakarta.ejb.Stateless;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

@Stateless
public class MissionService implements IMissionService {
    @EJB
    IMissionRepository missionRepository;
    @EJB
    IOperatorRepository operatorRepository;
    @EJB
    IDroneRepository droneRepository;
    @Inject
    KafkaProducerBean kafkaProducer;

    @Override
    public Long countAll() {
        return missionRepository.count();
    }

    @Override
    public List<Mission> getPage(int page, int size) {
        return missionRepository.findPage(page, size);
    }

    @Override
    public Mission getOrThrow(Long id) {
        return missionRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Mission not found: " + id));
    }

    @Override
    @Transactional
    public Mission setFinishedAt(Long id, LocalDateTime finishedAt) {
        Mission mission = missionRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Mission not found: " + id));
        mission.setFinishedAt(finishedAt);
        return missionRepository.save(mission);
    }

    @Override
    @Transactional
    public Mission setResult(Long id, Float pH, Float DO, Float temp, Float turbidity, Float conductivity) {
        Mission mission = missionRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Mission not found: " + id));
        if (mission.getResult() != null)
            return mission;
        mission.setResult(new Result(mission, pH, DO, temp, turbidity, conductivity));
        mission.setProbeAt(LocalDateTime.now());
        return missionRepository.save(mission);
    }

    @Override
    @Transactional
    public Mission setProbeReport(Long id, String probeReport) {
        Mission mission = missionRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Mission not found: " + id));
        mission.getResult().setProbeReport(probeReport);
        return missionRepository.save(mission);
    }

    @Override
    @Transactional
    public Mission createMission(String operatorLogin, String droneName, Float latitude, Float longitude) {
        Operator operator = operatorRepository.findByLogin(operatorLogin)
                .orElseThrow(() -> new IllegalArgumentException("Operator not found: " + operatorLogin));
        Drone drone = droneRepository.findByName(droneName)
                .orElseThrow(() -> new IllegalArgumentException("Drone not found: " + droneName));
        Mission mission = new Mission(operator, drone, latitude, longitude);
        return missionRepository.save(mission);
    }

    @Override
    public Mission startMission(Long id) {
        Mission mission = missionRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Mission not found: " + id));
        if (mission.getStartedAt() != null)
            throw new IllegalArgumentException("Mission already started" );
        if (Objects.equals(mission.getDrone().getStatus(), "active"))
            throw new IllegalArgumentException("Drone already on a mission" );
        mission.setStartedAt(LocalDateTime.now());
        String json = String.format(
                "{\"missionId\":\"%s\",\"drone\":\"%s\",\"lat\":%f,\"lng\":%f}",
                mission.getId(), mission.getDrone().getName(), mission.getLatitude(), mission.getLongitude()
        );
        missionRepository.save(mission);
        kafkaProducer.send("drone-commands", mission.getId().toString(), json);
        return mission;
    }
}
