package com.jtsec.mc.log.analysis.config;

import com.alibaba.druid.pool.DruidDataSource;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;

import javax.sql.DataSource;

/**
 * @author NovLi
 * @Description: 监控
 * @date 2018/7/10 14:55
 */
@Configuration
@MapperScan (basePackages = AnalysisDataSourceConfiguration.PACKAGE, sqlSessionFactoryRef = "analysisSqlSessionFactory")
public class AnalysisDataSourceConfiguration {

	// 精确到 master 目录，以便跟其他数据源隔离
	static final String PACKAGE = "com.jtsec.mc.log.analysis.mapper";
	static final String MAPPER_LOCATION = "classpath:mapper/analysis/*.xml";

	@Value ("${spring.datasource.analysis.url}")
	private String url;

	@Value ("${spring.datasource.analysis.username}")
	private String user;

	@Value ("${spring.datasource.analysis.password}")
	private String password;

	@Value ("${spring.datasource.analysis.driver-class-name}")
	private String driverClass;

	@Bean (name = "analysisDataSource")
	@Primary
	public DataSource analysisDataSource () {
		DruidDataSource dataSource = new DruidDataSource ();
		dataSource.setDriverClassName (driverClass);
		dataSource.setUrl (url);
		dataSource.setUsername (user);
		dataSource.setPassword (password);
		return dataSource;
	}

	@Bean (name = "analysisTransactionManager")
	@Primary
	public DataSourceTransactionManager analysisTransactionManager () {
		return new DataSourceTransactionManager (analysisDataSource ());
	}

	@Bean (name = "analysisSqlSessionFactory")
	@Primary
	public SqlSessionFactory analysisSqlSessionFactory (@Qualifier ("analysisDataSource") DataSource analysisDataSource)
			throws Exception {
		final SqlSessionFactoryBean sessionFactory = new SqlSessionFactoryBean ();
		sessionFactory.setDataSource (analysisDataSource);
		sessionFactory.setMapperLocations (new PathMatchingResourcePatternResolver ()
				.getResources (AnalysisDataSourceConfiguration.MAPPER_LOCATION));
		return sessionFactory.getObject ();
	}
}
