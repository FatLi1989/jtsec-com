package com.jtsec.mc.dev.moitor.mapper;

import com.jtsec.mc.dev.moitor.pojo.model.AgentUserInfo;
import org.apache.ibatis.annotations.Param;

import java.util.Map;

public interface AgentUserInfoMapper {

	int deleteByPrimaryKey (Integer id);

	int insert (AgentUserInfo record);

	int insertSelective (AgentUserInfo record);

	AgentUserInfo selectByPrimaryKey (Integer id);

	int updateByPrimaryKeySelective (AgentUserInfo record);

	int updateByPrimaryKey (AgentUserInfo record);

	Integer edit (AgentUserInfo agentUserInfo);
}