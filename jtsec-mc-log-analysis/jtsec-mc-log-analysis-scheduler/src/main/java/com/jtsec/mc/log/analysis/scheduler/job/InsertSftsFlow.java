package com.jtsec.mc.log.analysis.scheduler.job;

import com.jtsec.mc.log.analysis.api.sfts.service.JtSftsFlowService;
import com.jtsec.mc.log.analysis.pojo.model.JtSftsFlow;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author NovLi
 * @Title:
 * @ProjectName jtsec
 * @Description: TODO
 * @date 2019/1/16 17:27
 */
@Slf4j
@Component("InsertSftsFlow")
public class InsertSftsFlow {
	@Autowired
	JtSftsFlowService jtSftsFlowService;


	@Transactional
	public void insertInfo () {
		JtSftsFlow jtSftsFlow = new JtSftsFlow();
		jtSftsFlow.setFromIp ("127.0.0.0");
		jtSftsFlow.setServName ("serv");
		jtSftsFlow.setCliIp ("192.168.8.187");
		jtSftsFlow.setCliPort ("8021");
		jtSftsFlow.setDirection ("0");
		jtSftsFlow.setFileName ("文件名");
		jtSftsFlow.setFileType ("png");
		jtSftsFlow.setFilePath ("/jtsec/com");
		jtSftsFlow.setFileSize (40);
		Integer result = jtSftsFlowService.insertSftsFlow(jtSftsFlow);
		jtSftsFlow.setDirection ("1");
		jtSftsFlow.setFileSize (70);
	    jtSftsFlowService.insertSftsFlow(jtSftsFlow);
		if (result > 0)  {
			log.info ("【InsertSftsFlow】方法【insertInfo】执行成功");
		} else {
			log.info ("【InsertSftsFlow】方法【insertInfo】执行失败");
		}
	}

}
