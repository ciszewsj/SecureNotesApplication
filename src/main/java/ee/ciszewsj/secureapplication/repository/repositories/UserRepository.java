package ee.ciszewsj.secureapplication.repository.repositories;

import ee.ciszewsj.secureapplication.repository.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long>, JpaSpecificationExecutor<User> {
	Optional<User> findByUsernameContainingIgnoreCase(String username);

	Optional<User> findByEmailContainingIgnoreCase(String email);

}
