package com.jtsec.mc.log.analysis.sfts.service.impl;

import com.jtsec.common.util.datahandle.DataConvert;
import com.jtsec.mc.log.analysis.api.sfts.service.JtSftsFlowService;
import com.jtsec.mc.log.analysis.pojo.model.JtSftsFlow;
import com.jtsec.mc.log.jtsec.mapper.JtSftsFlowMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Map;

/**
 * @author NovLi
 * @Title: what
 * @ProjectName jtsec
 * @Description: TODO
 * @date 2018/9/1715:35
 */
@Slf4j
@Service ("JtSftsFlowService")
public class JtSftsFlowServiceImpl implements JtSftsFlowService {

	@Autowired
	private JtSftsFlowMapper jtSftsFlowMapper;

	/**
	 * @param
	 * @return
	 * @Description: 获取安全文件中同一流向时间段中的所有数据
	 * @author NovLi
	 * @date 2018/9/18 10:13
	 */
	@Override
	public List<JtSftsFlow> queryByVaribleGroupByServerName (JtSftsFlow jtSftsFlow) {

		//实体类转换成map方便进行条件查询
		Map<String, Object> map = DataConvert
				.ConvertDataToMapWithoutException (jtSftsFlow);
		log.info ("执行【jtSftsFlowMapper】中【queryByVaribleGroupByServerName】方法参数为 = {}", map.toString ());
		return jtSftsFlowMapper.queryByVaribleGroupByServerName (map);
	}

	@Override
	public Integer insertSftsFlow (JtSftsFlow jtSftsFlow) {

		return jtSftsFlowMapper.insert (jtSftsFlow);
	}
}
