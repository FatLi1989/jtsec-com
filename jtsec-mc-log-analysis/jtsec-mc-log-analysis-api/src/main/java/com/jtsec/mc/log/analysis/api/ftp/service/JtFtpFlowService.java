package com.jtsec.mc.log.analysis.api.ftp.service;

import com.jtsec.mc.log.analysis.pojo.model.JtFtpFlow;

import java.util.List;
import java.util.Map;

/**
 * @author NovLi
 * @Title: what
 * @ProjectName jtsec
 * @Description: TODO
 * @date 2018/9/17 14:30
 */
public interface JtFtpFlowService {
	List<JtFtpFlow> queryByVarible (Map<String,Object> map);
}
