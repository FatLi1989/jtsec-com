package com.jtsec.manager.service;

import com.jtsec.common.util.service.ServiceResult;
import com.jtsec.manager.pojo.model.Job;
import com.jtsec.manager.pojo.vo.JobVo;
import java.util.List;

/**
 * @author NovLi
 * @Title: what
 * @ProjectName manager-parent
 * @Description: TODO
 * @date 2018/8/2410:51
 */
public interface IJobService {

	ServiceResult<List<Job>> selectJobByVarible (JobVo jobVo);

	ServiceResult<Boolean> editJob (JobVo jobVo);

	ServiceResult<Job> selectJobById (Integer id);

	ServiceResult<Boolean> delJob (List<Integer> jobIdList);

	ServiceResult<Boolean> changeStatus (Job job);

	ServiceResult<Boolean> run (Integer jobId);

}
