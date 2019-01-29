package domain;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication
@EnableTransactionManagement
@ComponentScan (basePackages = {})
@MapperScan ("com.jtsec.mc.dev.moitor.mapper")
public class McDevMoitorWebApplication {

	public static void main (String[] args) {
		SpringApplication.run (McDevMoitorWebApplication.class, args);
	}

}
