package ee.ciszewsj.secureapplication.repository.entity;


import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import java.util.Date;

@Entity
public class RestorePasswd {
	@Id
	private Long id;

	private String link;

	private Date validTime;

	@ManyToOne
	private User user;
}
