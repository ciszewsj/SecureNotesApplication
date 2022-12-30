package ee.ciszewsj.secureapplication.repository.entity;

import org.hibernate.validator.constraints.Length;

import javax.persistence.*;

@Entity
public class Note {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Length(max = 64)
	private String name;

	@Length(max = 4096)
	private String note;

	private Boolean isEncrypted;

	@ManyToOne
	private User user;
}
