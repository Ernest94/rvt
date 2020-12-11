package nu.educom.rvt.models;

import java.time.LocalDateTime;

import javax.persistence.*;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import nu.educom.rvt.models.view.LocalDateTimeAdapter;

@Entity
@Table(name="review")
public class Review {

	public enum Status {
		CANCELLED,
		PENDING,
		COMPLETED
	}
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="id")
	private int id;
	
	@ManyToOne
	@JoinColumn(name="user_id")
	private User user;
	
	@Column(name="comment_student")
	private String commentStudent;
	
	@Column(name="comment_office")
	private String commentOffice;
	
	@Column(name="datetime")
	private LocalDateTime date;
	
	@Column(name="status")
	private Status reviewStatus;
	

	public Review() {
		super();
	}

	public Review(LocalDateTime date, String commentStudent, String commentOffice, Status reviewStatus) {
		super();
		this.date = date;
		this.commentStudent = commentStudent;
		this.commentOffice = commentOffice;
		this.reviewStatus = reviewStatus;
	}
	
	public Review(LocalDateTime date, String commentStudent, String commentOffice, Status reviewStatus, User user) {
		super();
		this.date = date;
		this.commentStudent = commentStudent;
		this.commentOffice = commentOffice;
		this.reviewStatus = reviewStatus;
		this.user = user;
	}
	
	
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}
	@XmlJavaTypeAdapter(LocalDateTimeAdapter.class)
	public LocalDateTime getDate() {
		return date;
	}
	@XmlJavaTypeAdapter(LocalDateTimeAdapter.class)
	public void setDate(LocalDateTime datetime) {
		this.date = datetime;
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
	@Override
	public String toString() {
		return String.format("Review(%d, %s)[%s]", getId(), getUser(), getReviewStatus());
	}

}
