package mclass.store.tripant.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;
import org.springframework.security.web.csrf.CsrfTokenRepository;
import org.springframework.security.web.header.writers.frameoptions.XFrameOptionsHeaderWriter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import lombok.RequiredArgsConstructor;
import mclass.store.tripant.member.model.service.OAuth2SecurityService;

@RequiredArgsConstructor
@EnableWebSecurity /* (debug = true) */
@Configuration
public class SecurityConfig {
	
	private final CustomAuthSuccessHandler customAuthSuccessHandler;
	private final CustomAuthFailureHandler customAuthFailureHandler;
	private final OAuth2FailureHandler oAuth2FailureHandler;
	private final OAuth2SecurityService oAuth2MemberService;
	
	@Bean
	SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
		http
		.authorizeHttpRequests((authorizeHttpRequests) -> authorizeHttpRequests
				.requestMatchers(
						new AntPathRequestMatcher("/store")
						, new AntPathRequestMatcher("/diary")
						).permitAll()
				.requestMatchers(
						new AntPathRequestMatcher("/login")
						, new AntPathRequestMatcher("/join")
						, new AntPathRequestMatcher("/join/sns")
						).anonymous()
				.requestMatchers(
						new AntPathRequestMatcher("/awake")
						).hasAuthority("SLEEP")
				.requestMatchers(
						new AntPathRequestMatcher("/my/**")
						, new AntPathRequestMatcher("/trip/**")
						, new AntPathRequestMatcher("/store/**")
						).hasAnyAuthority("MEM", "VIP")
				.requestMatchers(
						new AntPathRequestMatcher("/admin/**")
						).hasAuthority("ADMIN")
				.requestMatchers(
						new AntPathRequestMatcher("/**")
						).permitAll()
					)
		.csrf((csrf) -> csrf
				.csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse())
//				.disable()
				)
		.headers((headers) -> headers
				.addHeaderWriter(new XFrameOptionsHeaderWriter(XFrameOptionsHeaderWriter.XFrameOptionsMode.SAMEORIGIN))
				)
		.formLogin((formLogin) -> formLogin
				.loginPage("/login")
				.defaultSuccessUrl("/")
				.successHandler(customAuthSuccessHandler)
				.failureHandler(customAuthFailureHandler)
				.usernameParameter("memEmail")
				.passwordParameter("memPassword")
				)
		.oauth2Login(oauth2LoginConfigurer -> oauth2LoginConfigurer
				.loginPage("/login")
				.defaultSuccessUrl("/")
				.successHandler(customAuthSuccessHandler)
				.failureHandler(oAuth2FailureHandler)
				.userInfoEndpoint(userInfoEndpoint -> userInfoEndpoint
						.userService(oAuth2MemberService)
						)
				)
		.logout((logout) -> logout
				.logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
				.logoutSuccessUrl("/")
				.invalidateHttpSession(true)
				)
		.exceptionHandling((exceptionHandling) -> exceptionHandling
				.accessDeniedPage("/")
				)
			;
		return http.build();
	}
	
	@Bean
	BCryptPasswordEncoder bCryptPasswordEncoder() {
		return new BCryptPasswordEncoder();
	}
	
	@Bean
	AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
		return authenticationConfiguration.getAuthenticationManager();
	}
}
