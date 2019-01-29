package com.jtsec.mc.log.analysis.api.sfts.service;

import com.jtsec.mc.log.analysis.pojo.model.JtSftsFlow;
import com.jtsec.mc.log.analysis.pojo.model.JtSftsFlowMin;

import java.util.List;

/**
 * @author NovLi
 * @Title: what
 * @ProjectName jtsec
 * @Description: TODO
 * @date 2018/9/18 16:21
 */
public interface JtSftsFlowMinService {

	Integer addJtFlowMin (JtSftsFlow jtSftsFlow);

	List<JtSftsFlowMin> staFlowMinToHourGroupByServName (JtSftsFlowMin jtSftsFlowMin);
}
