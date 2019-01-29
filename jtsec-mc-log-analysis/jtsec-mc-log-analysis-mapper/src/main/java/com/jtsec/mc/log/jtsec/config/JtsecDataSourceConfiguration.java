package com.jtsec.mc.log.jtsec.config;

import com.alibaba.druid.pool.DruidDataSource;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import javax.sql.DataSource;

/**
 * @author NovLi
 * @Description: 监控
 * @date 2018/7/10 14:55
 */
@Configuration
@MapperScan (basePackages = JtsecDataSourceConfiguration.PACKAGE, sqlSessionFactoryRef = "jtsecSqlSessionFactory")
public class JtsecDataSourceConfiguration {

	// 精确到 master 目录，以便跟其他数据源隔离
	static final String PACKAGE = "com.jtsec.mc.log.jtsec.mapper";
	static final String MAPPER_LOCATION = "classpath:mapper/jtsec/*.xml";

	@Value ("${spring.datasource.jtsec.url}")
	private String url;

	@Value ("${spring.datasource.jtsec.username}")
	private String user;

	@Value ("${spring.datasource.jtsec.password}")
	private String password;

	@Value ("${spring.datasource.jtsec.driver-class-name}")
	private String driverClass;

	@Bean (name = "jtsecDataSource")
	public DataSource jtsecDataSource () {
		DruidDataSource dataSource = new DruidDataSource ();
		dataSource.setDriverClassName (driverClass);
		dataSource.setUrl (url);
		dataSource.setUsername (user);
		dataSource.setPassword (password);
		return dataSource;
	}

	@Bean (name = "jtsecTransactionManager")
	public DataSourceTransactionManager jtsecTransactionManager () {
		return new DataSourceTransactionManager (jtsecDataSource ());
	}

	@Bean (name = "jtsecSqlSessionFactory")
	public SqlSessionFactory jtsecSqlSessionFactory (@Qualifier ("jtsecDataSource") DataSource jtsecDataSource)
			throws Exception {
		final SqlSessionFactoryBean sessionFactory = new SqlSessionFactoryBean ();
		sessionFactory.setDataSource (jtsecDataSource);
		sessionFactory.setMapperLocations (new PathMatchingResourcePatternResolver ()
				.getResources (JtsecDataSourceConfiguration.MAPPER_LOCATION));
		return sessionFactory.getObject ();
	}
}
