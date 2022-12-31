package ee.ciszewsj.secureapplication.repository.entity;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;

import javax.persistence.*;

@Entity
@Setter
@Getter
public class Note {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Length(max = 64)
	private String name;

	@Length(max = 10000)
	private String note;

	private Boolean isEncrypted;

	@ManyToOne
	private User user;
}
