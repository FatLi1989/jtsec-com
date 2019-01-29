package com.jtsec.mc.log.analysis.api.sfts.service;

import com.jtsec.mc.log.analysis.pojo.model.JtSftsFlow;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author NovLi
 * @Title: what
 * @ProjectName jtsec
 * @Description: TODO
 * @date 2018/9/17 15:34
 */
public interface JtSftsFlowService  {

	List<JtSftsFlow> queryByVaribleGroupByServerName (JtSftsFlow jtSftsFlow);

	Integer insertSftsFlow (JtSftsFlow jtSftsFlow);
}
