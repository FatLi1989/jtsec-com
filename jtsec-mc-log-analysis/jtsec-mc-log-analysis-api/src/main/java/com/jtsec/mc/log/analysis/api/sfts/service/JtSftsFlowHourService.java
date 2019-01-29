package com.jtsec.mc.log.analysis.api.sfts.service;

import com.jtsec.mc.log.analysis.pojo.model.JtFtpFlowHour;
import com.jtsec.mc.log.analysis.pojo.model.JtSftsFlow;
import com.jtsec.mc.log.analysis.pojo.model.JtSftsFlowHour;
import com.jtsec.mc.log.analysis.pojo.model.JtSftsFlowMin;

import java.util.List;

/**
 * @author NovLi
 * @Title: what
 * @ProjectName jtsec
 * @Description: TODO
 * @date 2018/9/17 17:38
 */
public interface JtSftsFlowHourService {

	Integer addFlowHour (JtSftsFlowMin jtSftsFlowMin);

	List<JtSftsFlowHour> statisticsFlowHourToDayGroupByServName (JtSftsFlowHour jtSftsFlowHour);
}




