package com.jtsec.manager.scheduler.job;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * @author NovLi
 * @Title: whatcom.jtsec.manager.scheduler.job.Test.test()
 * @ProjectName jtsec
 * @Description: TODO
 * @date 2018/9/1215:03
 */
@Slf4j
public class JobSys {

	public void test(String param) {
		System.out.println (param);
	}

	public JobSys () {
		log.info ("加载Test");
	}
}
