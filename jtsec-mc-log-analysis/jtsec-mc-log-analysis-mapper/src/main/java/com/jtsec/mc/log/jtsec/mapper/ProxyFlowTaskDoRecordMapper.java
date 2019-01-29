package com.jtsec.mc.log.jtsec.mapper;

import com.jtsec.mc.log.analysis.pojo.model.ProxyFlowTaskDoRecord;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Mapper
@Repository
public interface ProxyFlowTaskDoRecordMapper {
    int deleteByPrimaryKey (Integer id);

    int insert (ProxyFlowTaskDoRecord record);

    int insertSelective (ProxyFlowTaskDoRecord record);

    ProxyFlowTaskDoRecord selectByPrimaryKey (Integer id);

    int updateByPrimaryKeySelective (ProxyFlowTaskDoRecord record);

    int updateByPrimaryKey (ProxyFlowTaskDoRecord record);

	ProxyFlowTaskDoRecord queryLastRecordByVarible (@Param ("map") Map<String, Object> map);

	Integer count (@Param ("map") Map<String, Object> map);

	ProxyFlowTaskDoRecord queryRecordByVarible (@Param ("map") Map<String, Object> map);

	List<ProxyFlowTaskDoRecord> queryRecordsByVarible (@Param ("map") Map<String, Object> map);

	int insertRecord (ProxyFlowTaskDoRecord proxyFlowTaskDoRecord);
}