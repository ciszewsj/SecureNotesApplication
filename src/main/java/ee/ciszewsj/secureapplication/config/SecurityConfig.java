package ee.ciszewsj.secureapplication.config;

import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

@EnableWebSecurity
public class SecurityConfig {
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
				.anyRequest().permitAll()
				.and().httpBasic();
		return http.build();
	}

	@Bean
	public UserDetailsService userDetailsService() {
		UserDetails userDetails =
				User.withDefaultPasswordEncoder()
						.username("admin")
						.password("admin")
						.roles("admin")
						.build();
		return new InMemoryUserDetailsManager(userDetails);
	}
}
