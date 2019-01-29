package com.jtsec.mc.log.jtsec.mapper;

import com.jtsec.mc.log.analysis.pojo.model.JtSftsFlowHour;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Map;

@Mapper
@Repository
public interface JtSftsFlowHourMapper {

    int deleteByPrimaryKey (Integer id);

    int insert (JtSftsFlowHour record);

    int insertSelective (JtSftsFlowHour record);

    JtSftsFlowHour selectByPrimaryKey (Integer id);

    int updateByPrimaryKeySelective (JtSftsFlowHour record);

    int updateByPrimaryKey (JtSftsFlowHour record);

	List<JtSftsFlowHour> statisticsFlowHourToDayGroupByServName (@Param ("map") Map<String, Object> map);
}