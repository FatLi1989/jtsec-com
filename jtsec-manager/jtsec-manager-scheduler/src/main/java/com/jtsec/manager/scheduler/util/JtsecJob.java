package com.jtsec.manager.scheduler.util;

import com.jtsec.common.util.datahandle.DataConvert;
import com.jtsec.manager.scheduler.Constants.ScheduleConstants;
import lombok.extern.slf4j.Slf4j;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 * @author NovLi
 * @Title: what
 * @ProjectName manager-parent
 * @Description: TODO
 * @date 2018/8/279:37
 */
@Slf4j
public class JtsecJob implements Job {

	private ExecutorService service = Executors.newSingleThreadExecutor ();


	@Override
	public void execute (JobExecutionContext context) {

		com.jtsec.manager.pojo.model.Job job = new com.jtsec.manager.pojo.model.Job ();

		DataConvert.convertVoToEntity (job, context.getMergedJobDataMap ().get (ScheduleConstants.JOB_PARAM_KEY));

		long startTime = System.currentTimeMillis ();

		try {
			// 执行任务
			log.info ("任务开始执行 - 名称：{} 方法：{}", job.getJobName (), job.getMethodName ());
			ScheduleRunnable task = new ScheduleRunnable (job.getJobName (), job.getMethodName (), job.getParams ());
			Future<?> future = service.submit (task);
			future.get ();
			long times = System.currentTimeMillis () - startTime;
			// 任务状态 0：成功 1：失败

			log.info ("任务执行结束 - 名称：{} 耗时：{} 毫秒", job.getJobName (), times);
		} catch (Exception e) {
			log.info ("任务执行失败 - 名称：{} 方法：{}", job.getJobName (), job.getMethodName ());
			log.error ("任务执行异常  - ：", e);
			long times = System.currentTimeMillis () - startTime;
			// 任务状态 0：成功 1：失败
		}

	}
}
