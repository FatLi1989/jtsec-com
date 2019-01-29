package com.jtsec.mc.log.analysis.scheduler.Constants;

/**
 * @author NovLi
 * @Title: what
 * @ProjectName manager-parent
 * @Description: TODO
 * @date 2018/8/2710:04
 */
public class ScheduleConstants {
	/**
	 * 任务调度参数key
	 */
	public static final String JOB_PARAM_KEY = "JOB_PARAM_KEY" ;

	public static String REFLECT = "com.jtsec.mc.log.analysis.scheduler.job.";

	public enum Status {
		/**
		 * 正常
		 */
		NORMAL (0),
		/**
		 * 暂停
		 */
		PAUSE (1);

		private int value;

		private Status (int value) {
			this.value = value;
		}

		public int getValue () {
			return value;
		}
	}
}
