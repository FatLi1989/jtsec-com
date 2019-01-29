package com.jtsec.manager.scheduler.config;

import lombok.extern.slf4j.Slf4j;
import org.quartz.Scheduler;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;
import javax.sql.DataSource;
import java.util.Properties;

/**
 * @author NovLi
 * @ProjectName manager-parent
 * @date 2018/8/2418:29
 */
@Slf4j
@Configuration
public class SchedulerConfig {

	@Bean("SchedulerFactoryBean")
	public SchedulerFactoryBean schedulerFactoryBean (DataSource dataSource) {
		log.info ("初始化: Scheduler");
		SchedulerFactoryBean factory = new SchedulerFactoryBean ();
		//参数
		Properties prop = new Properties ();
		//实例名称
		prop.put ("org.quartz.scheduler.instanceName", "JtsecScheduler");
		//实例id
		prop.put ("org.quartz.scheduler.instanceId", "AUTO");
		// JobStore配置
		prop.put ("org.quartz.jobStore.class", "org.quartz.impl.jdbcjobstore.JobStoreTX");
		// 线程池配置
		prop.put ("org.quartz.threadPool.class", "org.quartz.simpl.SimpleThreadPool");
		prop.put ("org.quartz.threadPool.threadCount", "20");
		prop.put ("org.quartz.threadPool.threadPriority", "5");

		prop.put ("org.quartz.jobStore.misfireThreshold", "12000");
		prop.put ("org.quartz.jobStore.tablePrefix", "QRTZ_");
		factory.setDataSource (dataSource);

		factory.setSchedulerName ("JtsecScheduler");

		// 延时启动
		factory.setStartupDelay (1);
		factory.setApplicationContextSchedulerContextKey ("applicationContextKey");
		// 可选，QuartzScheduler
		// 启动时更新己存在的Job，这样就不用每次修改targetObject后删除qrtz_job_details表对应记录了
		factory.setOverwriteExistingJobs (true);
		// 设置自动启动，默认为true
		factory.setAutoStartup (true);

		return factory;
	}


	@Bean("Scheduler")
	public Scheduler scheduler (@Qualifier("SchedulerFactoryBean") SchedulerFactoryBean schedulerFactoryBean) {
		return schedulerFactoryBean.getScheduler ();
	}
}