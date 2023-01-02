package ee.ciszewsj.secureapplication.services;

import ee.ciszewsj.secureapplication.repository.entity.User;
import ee.ciszewsj.secureapplication.repository.repositories.UserRepository;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import javax.servlet.http.HttpServletRequest;

@Slf4j
public class UserDetailsServiceImpl implements UserDetailsService {
	@Autowired
	private UserRepository userRepository;

	@Autowired
	private LoginAttemptService loginAttemptService;

	@Autowired
	private HttpServletRequest request;

	@SneakyThrows
	@Override
	public UserDetails loadUserByUsername(String username)
			throws UsernameNotFoundException {

		String ip = getClientIP();
		log.error(ip);
		if (loginAttemptService.isBlocked(ip)) {
			log.error("USER IS BLOCKED");
			throw new UsernameNotFoundException("Could not find user");
		}

		User user = userRepository.findFirstByUsernameIgnoreCase(username).orElse(null);
		Thread.sleep(3000);
		if (user == null) {
			throw new UsernameNotFoundException("Could not find user");
		}

		return user;
	}

	private String getClientIP() {
		String xfHeader = request.getHeader("X-Forwarded-For");
		if (xfHeader == null) {
			return request.getRemoteAddr();
		}
		return xfHeader.split(",")[0];
	}
}
