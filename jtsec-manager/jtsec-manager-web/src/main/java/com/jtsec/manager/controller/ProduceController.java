/*
package com.jtsec.manager.controller;

import com.google.gson.Gson;
import com.jtsec.common.util.response.HttpResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.web.bind.annotation.*;

*/
/**
 * @author NovLi
 * @Title: what
 * @ProjectName jtsec
 * @Description: TODO
 * @date 2018/10/3010:01
 *//*

@Slf4j
@RestController
public class ProduceController {

	@Autowired
	@Qualifier("JtseckafkaTemplate")
	private KafkaTemplate kafkaTemplate;

	private final String topic = "jtsec-analysis";

	private Gson gson = new Gson();

	@RequestMapping(value = "/send", method = RequestMethod.POST, produces = {"application/json"})
	public HttpResponse<String> sendKafka() {
		try {
			this.kafkaTemplate.send(topic, "key", 	gson.toJson ("你好"));
			log.info("发送kafka成功.");
			return new HttpResponse();
		} catch (Exception e) {
			log.error("发送kafka失败", e);
			return new HttpResponse();
		}
	}
}
*/
