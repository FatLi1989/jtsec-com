package com.jtsec.mc.log.jtsec.mapper;

import com.jtsec.mc.log.analysis.pojo.model.JtFtpFlowDay;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

@Mapper
@Repository
public interface JtFtpFlowDayMapper {
    int deleteByPrimaryKey (Integer id);

    int insert (JtFtpFlowDay record);

    int insertSelective (JtFtpFlowDay record);

    JtFtpFlowDay selectByPrimaryKey (Integer id);

    int updateByPrimaryKeySelective (JtFtpFlowDay record);

    int updateByPrimaryKey (JtFtpFlowDay record);
}