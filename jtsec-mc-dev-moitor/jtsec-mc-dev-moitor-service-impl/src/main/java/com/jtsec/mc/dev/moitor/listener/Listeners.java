package com.jtsec.mc.dev.moitor.listener;

import com.jtsec.mc.dev.moitor.system.InnerServUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import javax.annotation.PostConstruct;

/**
 * @author NovLi
 * @Title: what
 * @ProjectName jtsec
 * @Description: TODO
 * @date 2018/11/15 10:03
 */
@Slf4j
@Component
public class Listeners {

	public Listeners() {}

	@PostConstruct
	public void contextInitialized () {

		log.info("服务器启动");
		// 启动系统内部服务
		InnerServUtil.startInnerServ();
	}
}
