package ee.ciszewsj.secureapplication.repository.repositories;

import ee.ciszewsj.secureapplication.repository.entity.Note;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NoteRepository extends JpaRepository<Note, Long> {

}
