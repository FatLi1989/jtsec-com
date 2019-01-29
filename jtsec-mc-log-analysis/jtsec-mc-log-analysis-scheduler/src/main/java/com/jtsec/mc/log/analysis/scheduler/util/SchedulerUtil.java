package com.jtsec.mc.log.analysis.scheduler.util;

import com.jtsec.common.exception.JtsecException;
import com.jtsec.common.util.enums.ExceptionEnum;
import com.jtsec.mc.log.analysis.pojo.model.AnalysisJob;
import com.jtsec.mc.log.analysis.scheduler.Constants.ScheduleConstants;
import lombok.extern.slf4j.Slf4j;
import org.quartz.*;

import java.util.Date;


@Slf4j
public class SchedulerUtil {

	private final static String JOB_NAME = "TASK_" ;

	/**
	 * 获取触发器key
	 */
	public static TriggerKey getTriggerKey (Integer jobId) {
		return TriggerKey.triggerKey (JOB_NAME + jobId);
	}

	/**
	 * 获取jobKey
	 */
	public static JobKey getJobKey (Integer jobId) {
		return JobKey.jobKey (JOB_NAME + jobId);
	}

	/**
	 * 获取表达式触发器
	 */
	public static CronTrigger getCronTrigger (Scheduler scheduler, Integer jobId) {
		try {
			return (CronTrigger) scheduler.getTrigger (getTriggerKey (jobId));
		} catch (SchedulerException e) {
			log.error (e.getMessage ());
		}
		return null;
	}
    /**
     * @Description: 创建定时任务
     * @author NovLi
     * @date 2018/8/27 10:25 
     */
	 public static Integer createScheduler (Scheduler scheduler, AnalysisJob analysisJob){
		 Integer row = null;
		 try {
			 scheduler.start ();
			 // 构建job信息
			 JobDetail jobDetail = JobBuilder
					 .newJob (JtsecJob.class)
					 .withIdentity (getJobKey (analysisJob.getJobId ()))
					 .build ();

			 // 表达式调度构建器
			 CronScheduleBuilder scheduleBuilder = CronScheduleBuilder
					 .cronSchedule (analysisJob.getCronExpression ());

			 // 按新的cronExpression表达式构建一个新的trigger
			 CronTrigger trigger = TriggerBuilder.newTrigger ()
					 .withIdentity (getTriggerKey (analysisJob.getJobId ()))
					 .withSchedule (scheduleBuilder)
					 .build ();

			 // 放入参数，运行时的方法可以获取
			 jobDetail.getJobDataMap ().put (ScheduleConstants.JOB_PARAM_KEY, analysisJob);

			 scheduler.scheduleJob (jobDetail, trigger);

			 // 暂停任务
			 if (analysisJob.getStatus().equals(ScheduleConstants.Status.PAUSE.getValue())) {
				 pauseJob(scheduler, analysisJob.getJobId());
			 }
			 row =1;
		 } catch (SchedulerException e) {
			throw new JtsecException (ExceptionEnum.SchedulerException.getCode ());
		 }
		 return row;
	 }
	/**
	 * 更新定时任务
	 */
	public static Integer updateScheduleJob (Scheduler scheduler, AnalysisJob analysisJob) {
		Integer row = null;
		try {
			scheduler.start ();
			TriggerKey triggerKey = getTriggerKey (analysisJob.getJobId ());

			// 表达式调度构建器
			CronScheduleBuilder cronScheduleBuilder = CronScheduleBuilder
					.cronSchedule (analysisJob.getCronExpression ());

			CronTrigger trigger = getCronTrigger (scheduler, analysisJob.getJobId ());

			// 按新的cronExpression表达式重新构建trigger
			trigger = trigger
					.getTriggerBuilder ()
					.withIdentity (triggerKey)
					.withSchedule (cronScheduleBuilder)
					.build ();
			// 参数
			trigger.getJobDataMap ().put (ScheduleConstants.JOB_PARAM_KEY, analysisJob);
			//替换
			Date date = scheduler.rescheduleJob (triggerKey, trigger);

			// 暂停任务
			if (analysisJob.getStatus ().equals (ScheduleConstants.Status.PAUSE.getValue ())) {
				pauseJob (scheduler, analysisJob.getJobId ());
			}
			if (date != null)
				row = 1;
		} catch (SchedulerException e) {
			log.error ("SchedulerException 异常：", e);
		}
		return row;
	}
	/**
	 * 暂停任务
	 */
	public static Integer pauseJob (Scheduler scheduler, Integer jobId) {
		Integer row = null;
		try {
			scheduler.pauseJob (getJobKey (jobId));
			row = 1;
		} catch (SchedulerException e) {
			log.error (e.getMessage ());
		}
		return row;
	}

	/**
	 * 立即执行任务
	 */
	public static Integer run (Scheduler scheduler, AnalysisJob analysisJob) {
		Integer row = null;
		try {
			// 参数
			JobDataMap dataMap = new JobDataMap ();
			dataMap.put (ScheduleConstants.JOB_PARAM_KEY, analysisJob);
			scheduler.triggerJob (getJobKey (analysisJob.getJobId ()), dataMap);
			row = 1;
		} catch (SchedulerException e) {
			log.error (e.getMessage ());
		}
		return row;
	}

	/**
	 * 恢复任务
	 */
	public static Integer resumeJob (Scheduler scheduler, Integer jobId) {
		Integer row = null;
		try {
			scheduler.resumeJob (getJobKey (jobId));
			row = 1;
		} catch (SchedulerException e) {
			log.error (e.getMessage ());
		}
		return row;
	}

	/**
	 * 删除定时任务
	 */
	public static void deleteScheduleJob (Scheduler scheduler, Integer jobId) {
		try {
			scheduler.deleteJob (getJobKey (jobId));
		} catch (SchedulerException e) {
			log.error (e.getMessage ());
		}
	}
}
