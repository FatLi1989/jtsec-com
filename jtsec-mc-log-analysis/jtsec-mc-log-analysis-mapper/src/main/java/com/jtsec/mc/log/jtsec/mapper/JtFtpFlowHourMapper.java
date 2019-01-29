package com.jtsec.mc.log.jtsec.mapper;

import com.jtsec.mc.log.analysis.pojo.model.JtFtpFlowHour;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

@Mapper
@Repository
public interface JtFtpFlowHourMapper {
    int deleteByPrimaryKey (Integer id);

    int insert (JtFtpFlowHour record);

    int insertSelective (JtFtpFlowHour record);

    JtFtpFlowHour selectByPrimaryKey (Integer id);

    int updateByPrimaryKeySelective (JtFtpFlowHour record);

    int updateByPrimaryKey (JtFtpFlowHour record);
}