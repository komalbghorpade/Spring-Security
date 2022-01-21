package com.komal.securitydemo.security;

import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

import org.springframework.security.crypto.password.PasswordEncoder;

import org.springframework.security.web.util.matcher.AntPathRequestMatcher;


import com.komal.securitydemo.auth.ApplicationUserService;



@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class ApplicationSecurityConfig extends WebSecurityConfigurerAdapter {

	private final PasswordEncoder passwordEncoder;
	private final ApplicationUserService applicationUserService;
	
	@Autowired
	public ApplicationSecurityConfig(PasswordEncoder passwordEncoder, ApplicationUserService applicationUserService) {
		this.passwordEncoder = passwordEncoder;
		this.applicationUserService = applicationUserService;
	}
	
	@Override
	protected void configure(HttpSecurity http) throws Exception {
	http
//		.csrf().csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse())
//		.and()
		.csrf().disable()
		.authorizeRequests()
		.antMatchers("/","index","/css/*","/js/*").permitAll()
		.antMatchers("/api/**").hasRole(ApplicationUserRole.STUDENT.name())

		.anyRequest()
		.authenticated()
		.and()
		.formLogin()
		.loginPage("/login").permitAll()
		.defaultSuccessUrl("/courses", true)
		.and()
		.rememberMe()
			.tokenValiditySeconds((int)TimeUnit.DAYS.toSeconds(21))//21 =days
			.key("somethingverysecured")
		.and()
		.logout()
			.logoutUrl("/logout")
			.logoutRequestMatcher(new AntPathRequestMatcher("/logout","GET"))
			.clearAuthentication(true)
			.invalidateHttpSession(true)
			.deleteCookies("JSESSIONID","remember-me")
			.logoutSuccessUrl("/login")
		;
	}
	
	
//	@Override
//	@Bean
//	protected UserDetailsService userDetailsService() {
//		UserDetails User1 = User.builder()
//			.username("user1")
//			.password(passwordEncoder.encode("password1"))
//	//		.roles(ApplicationUserRole.STUDENT.name())
//			.authorities(ApplicationUserRole.STUDENT.getGrantedAuthorities())
//			.build();
//		
//		UserDetails User2 = User.builder()
//			.username("user2")
//			.password(passwordEncoder.encode("password2"))
//		//	.roles(ApplicationUserRole.ADMIN.name())
//			.authorities(ApplicationUserRole.ADMIN.getGrantedAuthorities())
//			.build();
//		
//		UserDetails User3 = User.builder()
//				.username("user3")
//				.password(passwordEncoder.encode("password3"))
//		//		.roles(ApplicationUserRole.ADMINTRAINEE.name())
//				.authorities(ApplicationUserRole.ADMINTRAINEE.getGrantedAuthorities())
//				.build();
//		
//		return new InMemoryUserDetailsManager(
//			User1,
//			User2,
//			User3);
//	}
	
	
	
	@Bean
	public DaoAuthenticationProvider daoAuthenticationProvider() {
		DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
		provider.setPasswordEncoder(passwordEncoder);
		provider.setUserDetailsService(applicationUserService);
		return provider;
	}

	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth.authenticationProvider(daoAuthenticationProvider());
		
	}
}
