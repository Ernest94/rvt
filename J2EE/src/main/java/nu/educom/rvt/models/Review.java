package nu.educom.rvt.models;

import java.time.LocalDate;
import javax.persistence.*;

@Entity
@Table(name="reviews")
public class Review {

	public enum Status {
		NOT_STARTED,
		PENDING,
		COMPLETED
	}
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="id")
	private int id;
	
	@Column(name="date")
	private LocalDate date;
	
	@ManyToOne
	@JoinColumn(name="user_id")
	private User user;
	
	@Column(name="comment_student")
	private String commentStudent;
	
	@Column(name="comment_office")
	private String commentOffice;
	
	@Column(name="status")
	private Status reviewStatus;
	

	public Review() {
		super();
	}

	public Review(LocalDate date, String commentStudent, String commentOffice, Status reviewStatus) {
		super();
		this.date = date;
		this.commentStudent = commentStudent;
		this.commentOffice = commentOffice;
		this.reviewStatus = reviewStatus;
	}
	
	
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public LocalDate getDate() {
		return date;
	}

	public void setDate(LocalDate date) {
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

	public Status getReviewStatus() {
		return reviewStatus;
	}

	public void setReviewStatus(Status reviewStatus) {
		this.reviewStatus = reviewStatus;
	}


}
