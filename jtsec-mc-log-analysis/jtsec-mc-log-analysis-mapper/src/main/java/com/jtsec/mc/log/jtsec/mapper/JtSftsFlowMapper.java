package com.jtsec.mc.log.jtsec.mapper;

import com.jtsec.mc.log.analysis.pojo.model.JtSftsFlow;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Map;

@Mapper
@Repository
public interface JtSftsFlowMapper {

    int deleteByPrimaryKey (Integer id);

    int insert (JtSftsFlow record);

    int insertSelective (JtSftsFlow record);

    JtSftsFlow selectByPrimaryKey (Integer id);

    int updateByPrimaryKeySelective (JtSftsFlow record);

    int updateByPrimaryKey (JtSftsFlow record);

	List<JtSftsFlow> queryByVarible (@Param ("map") Map<String, Object> map);

	List<JtSftsFlow> queryByVaribleGroupByServerName (@Param ("map") Map<String, Object> map);
}