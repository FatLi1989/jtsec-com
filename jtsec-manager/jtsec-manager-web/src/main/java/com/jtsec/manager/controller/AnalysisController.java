package com.jtsec.manager.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.jtsec.common.util.response.HttpResponse;
import com.jtsec.common.util.service.ServiceResult;
import com.jtsec.mc.log.analysis.pojo.model.AnalysisJob;
import com.jtsec.mc.log.analysis.pojo.vo.AnalysisJobVo;
import com.jtsec.mc.log.analysis.api.service.McJobService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;

@Slf4j
@RestController
public class AnalysisController {

    @Reference
    private McJobService mcJobService;

    @PostMapping( value = "/analysis/job/select/all")
    public HttpResponse selectJob (AnalysisJobVo jobVo) {
        HttpResponse httpResponse = new HttpResponse<> ();

        ServiceResult<List<AnalysisJob>> result = mcJobService.selectJobByVarible(jobVo);
        httpResponse.setPageInfo (result.getPageInfo ());
        httpResponse.setData (result.getData ());
        return httpResponse;
    }

	/**
	 * @Description: 变更任务
	 * @author NovLi
	 * @date 2018/8/17 15:34
	 */
	@PostMapping ("/analysis/job/eidt")
	public HttpResponse editRole (AnalysisJobVo jobVo) {
		HttpResponse httpResponse = new HttpResponse ();
		ServiceResult<Boolean> serviceResult = mcJobService.editJob (jobVo);
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
	@PostMapping ("/analysis/job/del")
	public HttpResponse delRole (AnalysisJobVo jobVo) {
		HttpResponse httpResponse = new HttpResponse ();
		ServiceResult<Boolean> serviceResult = mcJobService.delJob (jobVo.getJobIdList ());
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
	@GetMapping ("/analysis/job/select/{id}")
	public HttpResponse selectJobById (@PathVariable ("id") Integer id) {
		HttpResponse httpResponse = new HttpResponse ();
		ServiceResult<AnalysisJob> serviceResult = mcJobService.selectJobById (id);
		httpResponse.setData (serviceResult.getData ());
		return httpResponse;
	}

	/**
	 * @Description: 删除任务
	 * @author NovLi
	 * @date 2018/8/17 15:34
	 */
	@PostMapping ("/analysis/job/changeStatus")
	public HttpResponse changeStatus (AnalysisJob job) {
		HttpResponse httpResponse = new HttpResponse ();
		ServiceResult<Boolean> serviceResult = mcJobService.changeStatus (job);
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
	@GetMapping("/analysis/job/run/{jobId}")
	public HttpResponse run (@PathVariable("jobId") Integer jobId) {
		HttpResponse httpResponse = new HttpResponse ();
		ServiceResult<Boolean> serviceResult = mcJobService.run (jobId);
		if (!serviceResult.getSuccess ()) {
			httpResponse.setCode (0);
			httpResponse.setMeg (serviceResult.getMsg ());
			return httpResponse;
		}
		httpResponse.setMeg (serviceResult.getMsg ());
		return httpResponse;
	}
}
