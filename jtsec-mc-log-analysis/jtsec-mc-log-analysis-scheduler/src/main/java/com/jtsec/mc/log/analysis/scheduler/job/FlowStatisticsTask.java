package com.jtsec.mc.log.analysis.scheduler.job;

import com.jtsec.mc.log.analysis.api.service.FlowStatisticsService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author NovLi
 * @ProjectName jtsec
 * @Description: TODO
 * @date 2018/9/14 10:31
 */
@Slf4j
@Component ("FlowStatisticsTask")
public class FlowStatisticsTask {
	public FlowStatisticsTask () {
		System.out.println ("init FlowStatisticsTask");
	}

	/**
	 * @Description: 执行安全文件传输
	 * @author NovLi
	 * @date 2018/9/14 14:36
	 */
	@Autowired
	private FlowStatisticsService flowStatisticsService;


	public void workRun (String param) {

		log.info ("流量统计传递参数={}", param);
		flowStatisticsService.execute (param);
	}
}
