package com.jtsec.mc.log.analysis.sfts.service.impl;

import com.jtsec.common.util.datahandle.DataConvert;
import com.jtsec.mc.log.analysis.api.sfts.service.JtSftsFlowMinService;
import com.jtsec.mc.log.analysis.pojo.model.JtSftsFlow;
import com.jtsec.mc.log.analysis.pojo.model.JtSftsFlowMin;
import com.jtsec.mc.log.jtsec.mapper.JtSftsFlowMinMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * @author NovLi
 * @Title: what
 * @ProjectName jtsec
 * @Description: TODO
 * @date 2018/9/18 16:20
 */
@Service("JtSftsFlowMinService")
public class JtSftsFlowMinServiceImpl implements JtSftsFlowMinService {

	@Autowired
	private JtSftsFlowMinMapper jtSftsFlowMinMapper;


	@Override
	public Integer addJtFlowMin (JtSftsFlow jtSftsFlow) {
		JtSftsFlowMin jtSftsFlowMin = new JtSftsFlowMin(jtSftsFlow.getTime (),
				jtSftsFlow.getFromIp (),
				jtSftsFlow.getServName (),
				jtSftsFlow.getFileCount (),
				jtSftsFlow.getDirection (),
				jtSftsFlow.getFlowSize ());
		Integer count = jtSftsFlowMinMapper.insertSelective (jtSftsFlowMin);
		return count;
	}

	@Override
	public List<JtSftsFlowMin> staFlowMinToHourGroupByServName (JtSftsFlowMin jtSftsFlowMin) {
		Map<String, Object> map = DataConvert
				.ConvertDataToMapWithoutException (jtSftsFlowMin);

		List<JtSftsFlowMin> jtSftsFlowMinList = jtSftsFlowMinMapper
				.statisticsFlowMinToHourGroupByServName(map);

		return jtSftsFlowMinList;
	}
}
