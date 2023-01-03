package ee.ciszewsj.secureapplication.repository.repositories;

import ee.ciszewsj.secureapplication.repository.entity.RestorePasswd;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RestorePasswdRepository extends JpaRepository<RestorePasswd, String> {
	List<RestorePasswd> findAllByUserId(Long id);
}
