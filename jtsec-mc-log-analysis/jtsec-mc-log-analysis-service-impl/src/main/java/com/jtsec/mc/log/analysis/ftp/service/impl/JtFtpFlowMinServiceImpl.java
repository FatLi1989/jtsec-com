package com.jtsec.mc.log.analysis.ftp.service.impl;

import com.jtsec.mc.log.analysis.api.ftp.service.JtFtpFlowMinService;
import com.jtsec.mc.log.analysis.api.sfts.service.JtSftsFlowMinService;
import com.jtsec.mc.log.analysis.pojo.model.JtSftsFlow;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author NovLi
 * @ProjectName jtsec
 * @date 2018/9/1717:03
 */
@Slf4j
@Service("JtFtpFlowMinService")
public class JtFtpFlowMinServiceImpl implements JtFtpFlowMinService {

     @Autowired
	 private JtSftsFlowMinService jtSftsFlowMinService;

	/**
	 * @Description: 按分钟添加记录
	 * @param  
	 * @author NovLi
	 * @date 2018/9/18 10:04 
	 */
	@Override
	public void addJtFlowMin (JtSftsFlow jtSftsFlow) {
		log.info("queryFlowLog[direction:" + jtSftsFlow.getDirection () + "," +
				" servName:" + jtSftsFlow.getServName () + "," +
				" start: + " + jtSftsFlow.getStartTime () + "," +
				" end" + jtSftsFlow.getEndTime () +" ]");
	}
}
