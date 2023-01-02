package ee.ciszewsj.secureapplication.config;

import ee.ciszewsj.secureapplication.services.LoginAttemptService;
import ee.ciszewsj.secureapplication.services.UserDetailsServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.authentication.event.AuthenticationFailureBadCredentialsEvent;
import org.springframework.security.authentication.event.AuthenticationSuccessEvent;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.security.SecureRandom;
import java.util.Locale;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
	@Bean
	public UserDetailsService userDetailsService() {
		return new UserDetailsServiceImpl();
	}

	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder(11, new SecureRandom());
	}

	@Bean
	public DaoAuthenticationProvider authenticationProvider() {
		DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
		authProvider.setUserDetailsService(userDetailsService());
		authProvider.setPasswordEncoder(passwordEncoder());
		return authProvider;
	}

	@Bean
	public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
		http
				.formLogin()
				.loginPage("/login")
				.failureUrl("/login-error")
				.and()
				.logout()
				.logoutUrl("/perform_logout")
				.logoutSuccessUrl("/")
				.invalidateHttpSession(true)
				.deleteCookies("JSESSIONID")
				.and()
				.authorizeRequests()
				.antMatchers("/", "/login", "/register", "/perform_logout", "/login-error",
						"/activate/*", "/restore-passwd", "/reset_passwd/*").permitAll()
				.anyRequest().authenticated()
				.and().httpBasic();
		return http.build();
	}

	@Component
	public static class AuthenticationFailureListener implements
			ApplicationListener<AuthenticationFailureBadCredentialsEvent> {

		@Autowired
		private HttpServletRequest request;

		@Autowired
		private LoginAttemptService loginAttemptService;

		@Override
		public void onApplicationEvent(AuthenticationFailureBadCredentialsEvent e) {
			final String xfHeader = request.getHeader("X-Forwarded-For");
			if (xfHeader == null) {
				loginAttemptService.loginFailed(request.getRemoteAddr());
			} else {
				loginAttemptService.loginFailed(xfHeader.split(",")[0]);
			}
		}
	}

	@Component
	public static class AuthenticationSuccessEventListener implements
			ApplicationListener<AuthenticationSuccessEvent> {

		@Autowired
		private HttpServletRequest request;
		
		@Autowired
		private LoginAttemptService loginAttemptService;

		@Override
		public void onApplicationEvent(final AuthenticationSuccessEvent e) {
			final String xfHeader = request.getHeader("X-Forwarded-For");

			if (xfHeader == null) {
				loginAttemptService.loginSucceeded(request.getRemoteAddr());
			} else {
				loginAttemptService.loginSucceeded(xfHeader.split(",")[0]);
			}
		}
	}

}
