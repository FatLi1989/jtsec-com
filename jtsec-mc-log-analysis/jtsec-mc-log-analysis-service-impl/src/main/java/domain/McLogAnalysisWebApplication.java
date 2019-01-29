package domain;

import com.alibaba.dubbo.spring.boot.annotation.EnableDubboConfiguration;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication
@EnableDubboConfiguration
@EnableTransactionManagement
@ComponentScan (basePackages = {"com.jtsec.mc.log", "com.jtsec.common", "com.jtsec.mc.log.analysis.scheduler.job"})
public class McLogAnalysisWebApplication {

	public static void main (String[] args) {
		SpringApplication.run (McLogAnalysisWebApplication.class, args);
	}
}
