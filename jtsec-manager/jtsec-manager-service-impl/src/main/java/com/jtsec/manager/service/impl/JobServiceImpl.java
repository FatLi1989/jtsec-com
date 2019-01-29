package com.jtsec.manager.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.jtsec.common.util.datahandle.DataConvert;
import com.jtsec.common.util.service.ServiceResult;
import com.jtsec.manager.mapper.JobMapper;
import com.jtsec.manager.pojo.model.Job;
import com.jtsec.manager.pojo.vo.JobVo;
import com.jtsec.manager.scheduler.Constants.ScheduleConstants;
import com.jtsec.manager.scheduler.util.SchedulerUtil;
import com.jtsec.manager.service.IJobService;
import lombok.extern.slf4j.Slf4j;
import org.quartz.CronTrigger;
import org.quartz.Scheduler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author NovLi
 * @Title: what
 * @ProjectName manager-parent
 * @Description: TODO
 * @date 2018/8/24 10:52
 */
@Slf4j
@Service
public class JobServiceImpl implements IJobService {

	@Autowired
	private JobMapper jobMapper;
	@Autowired
	@Qualifier ("Scheduler")
	private Scheduler scheduler;


	@PostConstruct
	public void init () {
		log.info ("初始化 -- job定时任务");
		Map<String, Object> map = new HashMap<> ();
		List<Job> jobList = jobMapper.selectJobByVarible (map);
		for (Job job : jobList) {
			CronTrigger cronTrigger = SchedulerUtil.getCronTrigger (scheduler, job.getJobId ());
			// 如果不存在，则创建
			if (cronTrigger == null) {
				SchedulerUtil.createScheduler (scheduler, job);
			} else {
				SchedulerUtil.updateScheduleJob (scheduler, job);
			}
		}
	}

	@Override
	public ServiceResult<List<Job>> selectJobByVarible (JobVo jobVo) {
		Map<String, Object> map = new HashMap<> ();

		ServiceResult<List<Job>> serviceResult = new ServiceResult<> ();

		if (jobVo != null) {
			map = DataConvert.ConvertDataToMapWithoutException (jobVo);
		}
		if (jobVo.getPage () != null && jobVo.getRow () != null) {
			PageHelper.startPage (jobVo.getPage (), jobVo.getRow ());
			List<Job> jobList = jobMapper.selectJobByVarible (map);
			PageInfo<Job> info = new PageInfo<> (jobList);
			serviceResult.setPageInfo (info);
		} else if (jobVo.getPage () == null && jobVo.getRow () == null) {
			List<Job> jobList = jobMapper.selectJobByVarible (map);
			serviceResult.setData (jobList);
		}
		return serviceResult;
	}

	@Override
	public ServiceResult<Boolean> editJob (JobVo jobVo) {
		ServiceResult<Boolean> serviceResult;
		Job job = (Job) DataConvert.convertVoToEntity (new Job (), jobVo);
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

	@Override
	public ServiceResult<Job> selectJobById (Integer id) {

		Job job = jobMapper.selectByPrimaryKey (id);
		return new ServiceResult<> (job, true);
	}

	@Override
	public ServiceResult<Boolean> run (Integer jobId) {

		Job job= jobMapper.selectByPrimaryKey (jobId);
		Integer result = SchedulerUtil.run (scheduler, job);
		if (result != null) {
			return new ServiceResult<> (true, "操作成功");
		}
		return new ServiceResult<> (false, "操作失败");
	}

	@Override
	public ServiceResult<Boolean> changeStatus (Job job) {
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
}
