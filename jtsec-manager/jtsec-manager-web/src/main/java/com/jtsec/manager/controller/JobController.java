package com.jtsec.manager.controller;

import com.jtsec.common.util.response.HttpResponse;
import com.jtsec.common.util.service.ServiceResult;
import com.jtsec.manager.pojo.model.Job;
import com.jtsec.manager.pojo.vo.JobVo;
import com.jtsec.manager.service.IJobService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author NovLi
 * @Description: 定时任务模块
 * @date 2018/8/2410:39
 */
@Slf4j
@RestController
public class JobController {

	@Autowired
	private IJobService iJobService;

	/**
	 * @Description: 查询全部任务
	 * @author NovLi
	 * @date 2018/8/17 15:34
	 */
	@PostMapping ("/job/select/all")
	public HttpResponse selectJob (JobVo jobVo) {
		HttpResponse httpResponse = new HttpResponse ();
		ServiceResult<List<Job>> serviceResult = iJobService.selectJobByVarible (jobVo);
		httpResponse.setPageInfo (serviceResult.getPageInfo ());
		httpResponse.setData (serviceResult.getData ());
		return httpResponse;
	}

	/**
	 * @Description: 变更任务
	 * @author NovLi
	 * @date 2018/8/17 15:34
	 */
	@PostMapping ("/job/eidt")
	public HttpResponse editRole (JobVo jobVo) {
		HttpResponse httpResponse = new HttpResponse ();
		ServiceResult<Boolean> serviceResult = iJobService.editJob (jobVo);
		if (!serviceResult.getSuccess ()) {
			httpResponse.setCode (0);
			httpResponse.setMeg (serviceResult.getMsg ());
			return httpResponse;
		}
		httpResponse.setMeg ("信息变更成功");
		return httpResponse;
	}

	/**
	 * @Description: 删除任务
	 * @author NovLi
	 * @date 2018/8/17 15:34
	 */
	@PostMapping ("/job/del")
	public HttpResponse delRole (JobVo jobVo) {
		HttpResponse httpResponse = new HttpResponse ();
		ServiceResult<Boolean> serviceResult = iJobService.delJob (jobVo.getJobIdList ());
		if (!serviceResult.getSuccess ()) {
			httpResponse.setCode (0);
			httpResponse.setMeg (serviceResult.getMsg ());
			return httpResponse;
		}
		httpResponse.setMeg (serviceResult.getMsg ());
		return httpResponse;
	}
	
	/**
	 * @Description: 查询单条任务信息
	 * @author NovLi
	 * @date 2018/8/27 10:59 
	 */
	@GetMapping ("/job/select/{id}")
	public HttpResponse selectJobById (@PathVariable ("id") Integer id) {
		HttpResponse httpResponse = new HttpResponse ();
		ServiceResult<Job> serviceResult = iJobService.selectJobById (id);
		httpResponse.setData (serviceResult.getData ());
		return httpResponse;
	}

	/**
	 * @Description: 删除任务
	 * @author NovLi
	 * @date 2018/8/17 15:34
	 */
	@PostMapping ("/job/changeStatus")
	public HttpResponse changeStatus (Job job) {
		HttpResponse httpResponse = new HttpResponse ();
		ServiceResult<Boolean> serviceResult = iJobService.changeStatus (job);
		if (!serviceResult.getSuccess ()) {
			httpResponse.setCode (0);
			httpResponse.setMeg (serviceResult.getMsg ());
			return httpResponse;
		}
		httpResponse.setMeg (serviceResult.getMsg ());
		return httpResponse;
	}
	/**
	 * @Description: 暂停
	 * @author NovLi
	 * @date 2018/8/28 14:21 
	 */
	@GetMapping("/job/run/{jobId}")
	public HttpResponse run (@PathVariable("jobId") Integer jobId) {
		HttpResponse httpResponse = new HttpResponse ();
		ServiceResult<Boolean> serviceResult = iJobService.run (jobId);
		if (!serviceResult.getSuccess ()) {
			httpResponse.setCode (0);
			httpResponse.setMeg (serviceResult.getMsg ());
			return httpResponse;
		}
		httpResponse.setMeg (serviceResult.getMsg ());
		return httpResponse;
	}

}
