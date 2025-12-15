package dev.nnmod.sampleroperationcenter.domain.repository.interf;

import entity.Mission;

import java.util.List;
import java.util.Optional;

public interface IMissionRepository {
    public Long count();
    public Optional<Mission> findById(Long id);
    public List<Mission> findPage(int page, int size);
    public Mission save(Mission mission);
}
