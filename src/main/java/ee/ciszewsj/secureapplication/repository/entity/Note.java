package ee.ciszewsj.secureapplication.repository.entity;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class Note {
	@Id
	private Long id;

	private String name;

	private String note;

	private Boolean isEncrypted;
}
