package com.jtsec.mc.log.analysis.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.jtsec.common.util.datahandle.DataConvert;
import com.jtsec.common.util.service.ServiceResult;
import com.jtsec.mc.log.analysis.api.service.McJobService;
import com.jtsec.mc.log.analysis.mapper.JobMapper;
import com.jtsec.mc.log.analysis.pojo.model.AnalysisJob;
import com.jtsec.mc.log.analysis.pojo.vo.AnalysisJobVo;
import com.jtsec.mc.log.analysis.scheduler.Constants.ScheduleConstants;
import com.jtsec.mc.log.analysis.scheduler.util.SchedulerUtil;
import lombok.extern.slf4j.Slf4j;
import org.quartz.CronTrigger;
import org.quartz.Scheduler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author NovLi
 * @Title: what
 * @ProjectName jtsec
 * @Description: TODO
 * @date 2018/9/1315:09
 */
@Slf4j
@Service (interfaceClass = McJobService.class, retries = 0)
@Component
public class McJobServiceImpl implements McJobService {

	@Autowired
	private JobMapper jobMapper;


	@Autowired
	@Qualifier ("Scheduler")
	private Scheduler scheduler;

	/**
	 * @Description: 修改任务
	 * @author NovLi
	 * @date 2018/10/8 13:28
	 */
	@Override
	public ServiceResult<Boolean> editJob (AnalysisJobVo jobVo) {
		ServiceResult<Boolean> serviceResult;
		AnalysisJob job = (AnalysisJob) DataConvert.convertVoToEntity (new AnalysisJob (), jobVo);
		Integer result = null , results = null ;
		if (job.getJobId () == null) {
			result = jobMapper.insert (job);
			results = SchedulerUtil.createScheduler (scheduler, job);
		} else if (job.getJobId () != null) {
			result = jobMapper.updateByPrimaryKey (job);
			results = SchedulerUtil.updateScheduleJob (scheduler, job);
		}
		serviceResult = ServiceResult.judgeServiceResult (result, results);
		return serviceResult;
	}
	
	/**
	 * @Description: 通过id查询任务
	 * @author NovLi
	 * @date 2018/10/8 13:28 
	 */
	@Override
	public ServiceResult<AnalysisJob> selectJobById (Integer id) {
		AnalysisJob job = jobMapper.selectByPrimaryKey (id);
		return new ServiceResult<> (job, true);
	}
	
	/**
	 * @Description: 删除定时任务
	 * @author NovLi
	 * @date 2018/10/8 13:27 
	 */
	@Override
	public ServiceResult<Boolean> delJob (List<Integer> jobIdList) {
		ServiceResult<Boolean> serviceResult = new ServiceResult<> ();
		if (jobIdList.isEmpty ())
			return new ServiceResult<> (false, "还没勾选数据呢 ~ ~");
		Integer delJobNum = jobMapper.delBatch (jobIdList);
		for (Integer jobId : jobIdList) {
			SchedulerUtil.deleteScheduleJob (scheduler, jobId);
		}
		if (delJobNum == 0)
			return new ServiceResult<> (false, "失败了, 后台没有要删除的数据哦 ~ ~");
		return new ServiceResult<> (true, "删除成功喽 ~ ~");
	}

	/**
	 * @Description: 改变任务状态
	 * @author NovLi
	 * @date 2018/10/8 13:27
	 */
	@Override
	public ServiceResult<Boolean> changeStatus (AnalysisJob job) {
		ServiceResult<Boolean> serviceResult = new ServiceResult<> ();
		Integer result = jobMapper.updateByPrimaryKeySelective (job);
		if (ScheduleConstants.Status.NORMAL.getValue () == job.getStatus ()) {
			Integer resume = SchedulerUtil.resumeJob (scheduler, job.getJobId ());
			serviceResult = ServiceResult.judgeServiceResult (result, resume);
		} else if (ScheduleConstants.Status.PAUSE.getValue () == job.getStatus ()) {
			Integer pause = SchedulerUtil.pauseJob (scheduler, job.getJobId ());
			serviceResult = ServiceResult.judgeServiceResult (result, pause);
		}
		return serviceResult;
	}

	/**
	 * @Description: 运行定时任务
	 * @author NovLi
	 * @date 2018/10/8 13:29
	 */
	@Override
	public ServiceResult<Boolean> run (Integer jobId) {
		AnalysisJob job= jobMapper.selectByPrimaryKey (jobId);
		Integer result = SchedulerUtil.run (scheduler, job);
		if (result != null) {
			return new ServiceResult<> (true, "操作成功");
		}
		return new ServiceResult<> (false, "操作失败");
	}
	/**
	 * @Description: 初始化定时任务
	 * @author NovLi
	 * @date 2018/10/8 13:26 
	 */
	@PostConstruct
	public void init () {
		log.info ("初始化 -- job定时任务");
		Map<String, Object> map = new HashMap<> ();
		List<AnalysisJob> analysisJobList = jobMapper.selectJobByVarible (map);
		for (AnalysisJob analysisJob : analysisJobList) {
			CronTrigger cronTrigger = SchedulerUtil.getCronTrigger (scheduler, analysisJob.getJobId ());
			// 如果不存在，则创建
			if (cronTrigger == null) {
				SchedulerUtil.createScheduler (scheduler, analysisJob);
			} else {
				SchedulerUtil.updateScheduleJob (scheduler, analysisJob);
			}
		}
	}

	/**
	 * @Description: 通过变量查询定时任务
	 * @param  
	 * @author NovLi
	 * @date 2018/10/8 13:26 
	 */
	@Override
	public ServiceResult<List<AnalysisJob>> selectJobByVarible (AnalysisJobVo jobVo) {
		Map<String, Object> map = new HashMap<> ();

		ServiceResult<List<AnalysisJob>> serviceResult = new ServiceResult<> ();

		if (jobVo != null) {
			map = DataConvert.ConvertDataToMapWithoutException (jobVo);
		}
		if (jobVo.getPage () != null && jobVo.getRow () != null) {
			PageHelper.startPage (jobVo.getPage (), jobVo.getRow ());
			List<AnalysisJob> jobList = jobMapper.selectJobByVarible (map);
			PageInfo<AnalysisJob> info = new PageInfo<> (jobList);
			serviceResult.setPageInfo (info);
		} else if (jobVo.getPage () == null && jobVo.getRow () == null) {
			List<AnalysisJob> jobList = jobMapper.selectJobByVarible (map);
			serviceResult.setData (jobList);
		}
		return serviceResult;
	}

}
