package ee.ciszewsj.secureapplication.services;

import ee.ciszewsj.secureapplication.repository.entity.User;
import ee.ciszewsj.secureapplication.repository.repositories.UserRepository;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

public class UserDetailsServiceImpl implements UserDetailsService {
	@Autowired
	private UserRepository userRepository;

	@SneakyThrows
	@Override
	public UserDetails loadUserByUsername(String username)
			throws UsernameNotFoundException {
		User user = userRepository.findByUsernameContainingIgnoreCase(username).orElse(null);
		Thread.sleep(3000);
		if (user == null) {
			throw new UsernameNotFoundException("Could not find user");
		}

		return user;
	}
}
