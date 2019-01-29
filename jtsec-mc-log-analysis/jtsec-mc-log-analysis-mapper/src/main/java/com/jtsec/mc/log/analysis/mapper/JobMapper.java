package com.jtsec.mc.log.analysis.mapper;

import com.jtsec.mc.log.analysis.pojo.model.AnalysisJob;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Mapper
@Repository
public interface JobMapper {

    int deleteByPrimaryKey (Integer key);

    int insert (AnalysisJob record);

    int insertSelective (AnalysisJob record);

    AnalysisJob selectByPrimaryKey (Integer key);

    int updateByPrimaryKeySelective (AnalysisJob record);

    int updateByPrimaryKey (AnalysisJob record);

    List<AnalysisJob> selectJobByVarible (@Param ("map") Map<String, Object> map);

	Integer delBatch (@Param ("list") List<Integer> jobIdList);
}