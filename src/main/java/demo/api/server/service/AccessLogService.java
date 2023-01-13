package demo.api.server.service;

import demo.api.server.jpa.entity.RequestClientEntity;
import demo.api.server.jpa.repository.AccessLogQueryRepository;
import demo.api.server.mapper.primary.AccessLogMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AccessLogService {
    private final AccessLogMapper accessLogMapper;
    private final AccessLogQueryRepository queryRepository;

    public List<RequestClientEntity> findAllVerQueryDsl() {
        return queryRepository.findAll();
    }

    public List<RequestClientEntity> findAllVerMyBatis() {
        return accessLogMapper.findAll();
    }
}
