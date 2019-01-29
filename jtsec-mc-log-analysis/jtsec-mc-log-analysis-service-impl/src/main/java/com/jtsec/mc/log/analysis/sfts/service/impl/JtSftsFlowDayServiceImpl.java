package com.jtsec.mc.log.analysis.sfts.service.impl;

import com.jtsec.mc.log.analysis.api.sfts.service.JtSftsFlowDayService;
import com.jtsec.mc.log.analysis.pojo.model.JtSftsFlowDay;
import com.jtsec.mc.log.analysis.pojo.model.JtSftsFlowHour;
import com.jtsec.mc.log.jtsec.mapper.JtSftsFlowDayMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author NovLi
 * @Title: what
 * @ProjectName jtsec
 * @Description: TODO
 * @date 2018/9/1717:41
 */
@Service("JtSftsFlowDayService")
public class JtSftsFlowDayServiceImpl implements JtSftsFlowDayService {

	@Autowired
	private JtSftsFlowDayMapper jtSftsFlowDayMapper;

	@Override
	public Integer addFlowDay (JtSftsFlowHour jtSftsFlowHour) {

		JtSftsFlowDay jtSftsFlowDay = new JtSftsFlowDay (
				jtSftsFlowHour.getStartTime (), jtSftsFlowHour.getServIp (), jtSftsFlowHour.getServName (),
					jtSftsFlowHour.getFileCount (), jtSftsFlowHour.getDirection (), jtSftsFlowHour.getFlowSize ());

		Integer count = jtSftsFlowDayMapper.insertSelective (jtSftsFlowDay);

		return count;
	}
}
