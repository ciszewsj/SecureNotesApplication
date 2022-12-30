package ee.ciszewsj.secureapplication.repository.entity;


import org.hibernate.validator.constraints.Length;

import javax.persistence.*;
import java.util.Date;

@Entity
public class RestorePasswd {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Length(max = 100)
	@Column(name = "uniq_value")
	private String link;

	private Date validTime;

	@ManyToOne
	private User user;
}
