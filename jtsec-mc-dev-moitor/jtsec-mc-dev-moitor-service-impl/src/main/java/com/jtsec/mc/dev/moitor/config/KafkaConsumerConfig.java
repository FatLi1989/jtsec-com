package com.jtsec.mc.dev.moitor.config;

import com.jtsec.common.config.kafka.ConsumerConfigs;
import com.jtsec.mc.dev.moitor.service.impl.MoitorConsumer;
import org.apache.kafka.common.serialization.ByteBufferDeserializer;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.config.KafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.listener.ConcurrentMessageListenerContainer;

/**
 * @author NovLi
 * @Title: what
 * @ProjectName jtsec
 * @Description: TODO
 * @date 2018/10/26 17:30
 */
@Configuration
@EnableKafka
public class KafkaConsumerConfig {

	@Value ("${spring.kafka.bootstrap-servers}")
	private String servers;
	@Value ("${spring.kafka.consumer.enable-auto-commit}")
	private boolean enableAutoCommit;
	@Value ("${spring.kafka.consumer.auto-commit-interval}")
	private String autoCommitInterval;
	@Value ("${spring.kafka.consumer.group-id}")
	private String groupId;
	@Value ("${spring.kafka.consumer.auto-offset-reset}")
	private String autoOffsetReset;

	private int concurrency = 10;


	@Bean
	public KafkaListenerContainerFactory<ConcurrentMessageListenerContainer<String, String>> kafkaListenerContainerFactory () {
		return ConsumerConfigs.setkafkaListenerContainerFactory (consumerFactory (), concurrency);
	}

	private ConsumerFactory<String, String> consumerFactory () {
		return new DefaultKafkaConsumerFactory<> (
				ConsumerConfigs.setConsumerConfigs (servers, autoCommitInterval,
						StringDeserializer.class,
						ByteBufferDeserializer.class, groupId, autoOffsetReset),
				new StringDeserializer (),
				new StringDeserializer ()
		);
	}

	@Bean
	public MoitorConsumer listener ()                                                                                                                                                                                                       {
		return new MoitorConsumer ();
	}
}
