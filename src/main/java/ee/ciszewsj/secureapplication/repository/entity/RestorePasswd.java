package ee.ciszewsj.secureapplication.repository.entity;


import javax.persistence.*;
import java.util.Date;
import java.util.UUID;

@Entity
public class RestorePasswd {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private UUID id;

	private Date validTime;

	@ManyToOne
	private User user;
}
