package nu.educom.rvt.models;

import javax.persistence.Column; 
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name="concept_rating")
public class ConceptRating {

		@Id
		@GeneratedValue(strategy=GenerationType.IDENTITY)
		@Column(name="id")
		private int id;
	
		@ManyToOne
		@JoinColumn(name="review_id")
		private Review review;
		
		@ManyToOne
		@JoinColumn(name="concept_id")
		private Concept concept;
		
		@Column(name="rating") 
		private int rating;
		
		@Column(name="comment") 
		private String comment;

		public ConceptRating() {
			super();
		}
		
		public ConceptRating(Review review, Concept concept, int rating) {
			super();
			this.review = review;
			this.concept = concept;
			this.rating = rating;
		}
		
		public ConceptRating(Review review, Concept concept, int rating, String comment) {
			super();
			this.review = review;
			this.concept = concept;
			this.rating = rating;
			this.comment = comment;
		}
		
		public int getId() {
			return id;
		}

		public void setId(int id) {
			this.id = id;
		}

		public Review getReview() {
			return review;
		}

		public void setReview(Review review) {
			this.review = review;
		}

		public Concept getConcept() {
			return concept;
		}

		public void setConcept(Concept concept) {
			this.concept = concept;
		}

		public int getRating() {
			return rating;
		}

		public void setRating(int rating) {
			this.rating = rating;
		}
		
		public String getComment() {
			return comment;
		}
		
		public void setComment(String comment) {
			this.comment = comment;
		}
		
		
}
