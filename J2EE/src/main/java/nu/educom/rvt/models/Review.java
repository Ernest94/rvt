package nu.educom.rvt.models;

import java.time.LocalDateTime;
import javax.persistence.*;

@Entity
@Table(name="review")
public class Review {

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="id")
	private int id;
	
	@Column(name="date")
	private LocalDateTime date;
	
	@Column(name="comment_student")
	private String commentStudent;
	
	@Column(name="comment_office")
	private String commentOffice;
	
	@ManyToOne
	@JoinColumn(name="user_id")
	private User user;
	
	@ManyToOne
	@JoinColumn(name="review_status_id")
	private ReviewStatus reviewStatus;
	
	@ManyToOne
	@JoinColumn(name="curriculum_id")
	private Curriculum curriculum;

	public Review() {
		super();
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public LocalDateTime getDate() {
		return date;
	}

	public void setDate(LocalDateTime date) {
		this.date = date;
	}

	public String getCommentStudent() {
		return commentStudent;
	}

	public void setCommentStudent(String commentStudent) {
		this.commentStudent = commentStudent;
	}

	public String getCommentOffice() {
		return commentOffice;
	}

	public void setCommentOffice(String commentOffice) {
		this.commentOffice = commentOffice;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public ReviewStatus getReviewStatus() {
		return reviewStatus;
	}

	public void setReviewStatus(ReviewStatus reviewStatus) {
		this.reviewStatus = reviewStatus;
	}

	public Curriculum getCurriculum() {
		return curriculum;
	}

	public void setCurriculum(Curriculum curriculum) {
		this.curriculum = curriculum;
	}
}
