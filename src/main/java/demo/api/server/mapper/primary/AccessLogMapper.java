package demo.api.server.mapper.primary;

import demo.api.server.jpa.entity.RequestClientEntity;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface AccessLogMapper {
    List<RequestClientEntity> findAll();
}
