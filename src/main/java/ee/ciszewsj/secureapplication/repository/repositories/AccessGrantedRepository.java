package ee.ciszewsj.secureapplication.repository.repositories;

import ee.ciszewsj.secureapplication.repository.entity.Note;
import ee.ciszewsj.secureapplication.repository.entity.UserWithAccess;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AccessGrantedRepository extends JpaRepository<UserWithAccess, Long> {
}
