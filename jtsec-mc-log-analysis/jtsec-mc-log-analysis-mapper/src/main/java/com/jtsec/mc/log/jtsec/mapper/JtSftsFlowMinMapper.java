package com.jtsec.mc.log.jtsec.mapper;


import com.jtsec.mc.log.analysis.pojo.model.JtSftsFlowMin;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;


@Mapper
@Repository
public interface JtSftsFlowMinMapper {

    int deleteByPrimaryKey (Integer id);

    int insert (JtSftsFlowMin record);

    int insertSelective (JtSftsFlowMin record);

    JtSftsFlowMin selectByPrimaryKey (Integer id);

    int updateByPrimaryKeySelective (JtSftsFlowMin record);

    int updateByPrimaryKey (JtSftsFlowMin record);

	List<JtSftsFlowMin> statisticsFlowMinToHourGroupByServName (@Param ("map") Map<String, Object> map);
}