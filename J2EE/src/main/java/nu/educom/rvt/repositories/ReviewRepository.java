package nu.educom.rvt.repositories;

import java.util.List;

import org.hibernate.Session;

import nu.educom.rvt.models.Review;
import nu.educom.rvt.models.Review.Status;

public class ReviewRepository {

	protected Session session;
	
	public ReviewRepository(Session session) {
		super();
		this.session = session;
	}

	public void create(Review review) throws DatabaseException {
		session.save(review); 
	}
	
	public Review readByKnownId(int id) throws EntryNotFoundException, DatabaseException {
		return HibernateSession.loadByKnownId(Review.class, id, session);
	}
	
	/* JH: Had een readByUser(user_id) en een readByCreator(int creator_id) verwacht, geen readAll() */ 
	public List<Review> readAll() throws DatabaseException {
		return HibernateSession.loadAllData(Review.class, session);
	}
	
	public void update(Review review) throws DatabaseException {
        Review oldReview = readByKnownId(review.getId());
		if (oldReview.getReviewStatus() != Status.PENDING) {
			throw new DatabaseException("Modifying an existing Review");
		}
		oldReview.setCommentOffice(review.getCommentOffice());
		oldReview.setCommentStudent(review.getCommentStudent());
		oldReview.setDate(review.getDate());
	}
	
	public Review updateStatus(int reviewId, Status newStatus) throws DatabaseException {
		Review review = readByKnownId(reviewId);
		if (review.getReviewStatus() != Status.PENDING) {
			throw new DatabaseException("Modifying an existing Review");
		}
		if (newStatus != Status.COMPLETED && newStatus != Status.CANCELLED) {
			throw new DatabaseException("Cannot modify review status");
		}
		if (!session.isOpen() || !session.getTransaction().isActive()) {
			throw new DatabaseException("Create called on an DB transaction that is not open");
		}
		review.setReviewStatus(newStatus);
		return review;
	}
	
	protected void delete() throws DatabaseException {
		
	}
}
