package com.jtsec.mc.log.jtsec.mapper;

import com.jtsec.mc.log.analysis.pojo.model.JtSftsFlowDay;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

@Mapper
@Repository
public interface JtSftsFlowDayMapper {

    int deleteByPrimaryKey (Integer id);

    int insert (JtSftsFlowDay record);

    int insertSelective (JtSftsFlowDay record);

    JtSftsFlowDay selectByPrimaryKey (Integer id);

    int updateByPrimaryKeySelective (JtSftsFlowDay record);

    int updateByPrimaryKey (JtSftsFlowDay record);
}