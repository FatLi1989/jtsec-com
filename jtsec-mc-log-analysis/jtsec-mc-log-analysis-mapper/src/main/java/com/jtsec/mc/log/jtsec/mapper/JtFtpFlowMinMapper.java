package com.jtsec.mc.log.jtsec.mapper;

import com.jtsec.mc.log.analysis.pojo.model.JtFtpFlowMin;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

@Mapper
@Repository
public interface JtFtpFlowMinMapper{

    int deleteByPrimaryKey (Integer id);

    int insert (JtFtpFlowMin record);

    int insertSelective (JtFtpFlowMin record);

    JtFtpFlowMin selectByPrimaryKey (Integer id);

    int updateByPrimaryKeySelective (JtFtpFlowMin record);

    int updateByPrimaryKey (JtFtpFlowMin record);
}