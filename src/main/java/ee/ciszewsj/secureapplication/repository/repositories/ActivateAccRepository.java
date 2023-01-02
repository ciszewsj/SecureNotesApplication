package ee.ciszewsj.secureapplication.repository.repositories;

import ee.ciszewsj.secureapplication.repository.entity.ActivateAccount;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface ActivateAccRepository extends JpaRepository<ActivateAccount, String> {

}
