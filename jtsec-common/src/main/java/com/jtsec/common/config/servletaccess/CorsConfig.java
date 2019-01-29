package com.jtsec.common.config.servletaccess;

/**
 * @author NovLi
 * @Title: 后端解决跨域
 * @ProjectName database_parent
 * @Description: TODO
 * @date 2018/7/416:44
 */

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

/**
 * 解决前端站点(主要为JavaScript发起的Ajax请求)访问的跨域问题
 */
@Configuration
public class CorsConfig{
	/**
	 * 允许任何域名使用 允许任何头 允许任何方法（post、get等）
	 */
    private CorsConfiguration buildConfig() {
		CorsConfiguration corsConfiguration = new CorsConfiguration();
		// addAllowedOrigin 不能设置为* 因为与 allowCredential 冲突,需要设置为具体前端开发地址
		corsConfiguration.addAllowedOrigin ("*");
		corsConfiguration.addAllowedHeader ("*");
		corsConfiguration.addAllowedMethod ("*");
		// allowCredential 需设置为true
		corsConfiguration.setAllowCredentials (true);
    	return corsConfiguration;
	}
	@Bean
	public CorsFilter corsFilter() {
		UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource ();
		source.registerCorsConfiguration ("/**", buildConfig());
		return new CorsFilter(source);
	}



}