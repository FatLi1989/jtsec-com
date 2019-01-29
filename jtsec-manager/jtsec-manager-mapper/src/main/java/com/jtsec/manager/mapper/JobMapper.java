package com.jtsec.manager.mapper;

import com.jtsec.manager.pojo.model.Job;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Map;

@Mapper
@Repository
public interface JobMapper {

    int deleteByPrimaryKey (Integer key);

    int insert (Job record);

    int insertSelective (Job record);

    Job selectByPrimaryKey (Integer key);

    int updateByPrimaryKeySelective (Job record);

    int updateByPrimaryKey (Job record);

    List<Job> selectJobByVarible (@Param ("map") Map<String, Object> map);

	Integer delBatch (@Param ("list") List<Integer> jobIdList);
}