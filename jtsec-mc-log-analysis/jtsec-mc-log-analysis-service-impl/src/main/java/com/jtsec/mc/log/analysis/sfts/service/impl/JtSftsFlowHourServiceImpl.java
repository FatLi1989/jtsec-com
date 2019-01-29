package com.jtsec.mc.log.analysis.sfts.service.impl;

import com.jtsec.common.util.datahandle.DataConvert;
import com.jtsec.mc.log.analysis.api.sfts.service.JtSftsFlowHourService;
import com.jtsec.mc.log.analysis.pojo.model.JtSftsFlowHour;
import com.jtsec.mc.log.analysis.pojo.model.JtSftsFlowMin;
import com.jtsec.mc.log.jtsec.mapper.JtSftsFlowHourMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * @author NovLi
 * @Title: what
 * @ProjectName jtsec
 * @Description: TODO
 * @date 2018/9/1717:39
 */
@Service("JtSftsFlowHourService")
public class JtSftsFlowHourServiceImpl implements JtSftsFlowHourService {


	@Autowired
	private JtSftsFlowHourMapper jtSftsFlowHourMapper;


	@Override
	public Integer addFlowHour (JtSftsFlowMin jtSftsFlowMin) {



		JtSftsFlowHour jtSftsFlowHour = new JtSftsFlowHour
				(jtSftsFlowMin.getStartTime (), jtSftsFlowMin.getServIp (), jtSftsFlowMin.getServName (),
						jtSftsFlowMin.getFileCount (), jtSftsFlowMin.getDirection (), jtSftsFlowMin.getFlowSize ());

		Integer count = jtSftsFlowHourMapper.insertSelective (jtSftsFlowHour);

		return count;
	}

	@Override
	public List<JtSftsFlowHour> statisticsFlowHourToDayGroupByServName (JtSftsFlowHour jtSftsFlowHour) {

		Map<String, Object> map = DataConvert
				.ConvertDataToMapWithoutException (jtSftsFlowHour);

		List<JtSftsFlowHour> jtSftsFlowMinList = jtSftsFlowHourMapper
				.statisticsFlowHourToDayGroupByServName(map);
		return jtSftsFlowMinList;
	}
}
