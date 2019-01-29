package com.jtsec.mc.log.analysis.ftp.service.impl;

import com.jtsec.mc.log.analysis.api.ftp.service.JtFtpFlowService;
import com.jtsec.mc.log.analysis.pojo.model.JtFtpFlow;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * @author NovLi
 * @Title: what
 * @ProjectName jtsec
 * @Description: TODO
 * @date 2018/9/1714:31
 */
@Service("JtFtpFlowService")
public class JtFtpFlowServiceImpl implements JtFtpFlowService {

	@Override
	public List<JtFtpFlow> queryByVarible (Map<String, Object> map) {
		return null;
	}
}
