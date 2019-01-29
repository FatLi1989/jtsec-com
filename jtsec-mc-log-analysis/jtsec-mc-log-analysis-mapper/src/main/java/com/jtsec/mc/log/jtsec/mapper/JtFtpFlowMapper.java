package com.jtsec.mc.log.jtsec.mapper;

import com.jtsec.mc.log.analysis.pojo.model.JtFtpFlow;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

@Mapper
@Repository
public interface JtFtpFlowMapper {

    int deleteByPrimaryKey (Integer id);

    int insert (JtFtpFlow record);

    int insertSelective (JtFtpFlow record);

    JtFtpFlow selectByPrimaryKey (Integer id);

    int updateByPrimaryKeySelective (JtFtpFlow record);

    int updateByPrimaryKey (JtFtpFlow record);
}