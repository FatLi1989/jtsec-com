package com.jtsec.mc.log.analysis.service.impl;

import com.google.gson.Gson;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;


/**
 * @author NovLi
 * @Title: what
 * @ProjectName jtsec
 * @Description: TODO
 * @date 2018/10/2515:28
 */

@Slf4j
@Component
public class AnalysisConsumer {

	private final Gson gson = new Gson ();

	@KafkaListener (topics = {"jtsec-analysis"}, containerFactory = "kafkaListenerContainerFactory")
	public void receive (String message) {
		log.info (message);
	}
}
