package com.jtsec.common.config.kafka;

import com.fasterxml.jackson.databind.deser.std.ByteBufferDeserializer;
import com.fasterxml.jackson.databind.deser.std.StringDeserializer;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.config.KafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.listener.ConcurrentMessageListenerContainer;

import java.util.HashMap;
import java.util.Map;


/**
 * @author NovLi
 * @Title: what
 * @ProjectName jtsec
 * @Description: TODO
 * @date 2018/11/13 15:30
 */

public class ConsumerConfigs {

	public static Map<String, Object> setConsumerConfigs (String servers, String autoCommitInterval,
														  Class<org.apache.kafka.common.serialization.StringDeserializer> StringDeserializer,
														  Class<org.apache.kafka.common.serialization.ByteBufferDeserializer> ByteBufferDeserializer,
														  String groupId, String autoOffsetReset) {
		Map<String, Object> propsMap = new HashMap<> ();
		propsMap.put (ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, servers);
		propsMap.put (ConsumerConfig.AUTO_COMMIT_INTERVAL_MS_CONFIG, autoCommitInterval);
		propsMap.put (ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
		propsMap.put (ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, ByteBufferDeserializer.class);
		propsMap.put (ConsumerConfig.GROUP_ID_CONFIG, groupId);
		propsMap.put (ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, autoOffsetReset);
		return propsMap;
	}

	public static KafkaListenerContainerFactory<ConcurrentMessageListenerContainer<String, String>>
	setkafkaListenerContainerFactory (ConsumerFactory<String, String> ConsumerFactory, Integer concurrency) {
		ConcurrentKafkaListenerContainerFactory<String, String> factory = new ConcurrentKafkaListenerContainerFactory<> ();
		factory.setConsumerFactory (ConsumerFactory);
		factory.setConcurrency (concurrency);
		factory.getContainerProperties ().setPollTimeout (1500);
		return factory;
	}
}

