package com.jtsec.mc.log.analysis.api.service;

import com.jtsec.common.util.service.ServiceResult;
import com.jtsec.mc.log.analysis.pojo.model.AnalysisJob;
import com.jtsec.mc.log.analysis.pojo.vo.AnalysisJobVo;
import java.util.List;

/**
 * @author NovLi
 * @Title: what
 * @ProjectName jtsec
 * @Description: TODO
 * @date 2018/9/13 15:07
 */
public interface McJobService {

	ServiceResult<List<AnalysisJob>> selectJobByVarible (AnalysisJobVo jobVo);

	ServiceResult<Boolean> editJob (AnalysisJobVo jobVo);

	ServiceResult<AnalysisJob> selectJobById (Integer id);

	ServiceResult<Boolean> delJob (List<Integer> jobIdList);

	ServiceResult<Boolean> changeStatus (AnalysisJob job);

	ServiceResult<Boolean> run (Integer jobId);
}
