package nu.educom.rvt.models;

import java.time.LocalDate;

import javax.persistence.Column; 
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.Where;

import com.fasterxml.jackson.annotation.JsonBackReference;

@Entity
@Where(clause = "enddate IS NULL")
@Table(name="bundle_concept")
public class BundleConcept {

		@Id
		@GeneratedValue(strategy=GenerationType.IDENTITY)
		@Column(name="id")
		private int id;
		
		@JsonBackReference
		@ManyToOne
		@JoinColumn(name="bundle_id")
		private Bundle bundle;
		
		@ManyToOne
		@JoinColumn(name="concept_id")
		private Concept concept;
		
		@Column(name="weekOffset") 
		private int weekOffset;
		
		@Column(name="startdate")
		private LocalDate startDate;
		
		@Column(name="enddate")
		private LocalDate endDate;
		
		public BundleConcept() {
			super();
		}
		
		public BundleConcept(Bundle bundle, Concept concept, int weekOffset, LocalDate startDate) {
			super();
			this.bundle = bundle;
			this.concept = concept;
			this.weekOffset = weekOffset;
			this.startDate = startDate;
		}
		
		public BundleConcept(Bundle bundle, Concept concept, int weekOffset, LocalDate startDate, LocalDate endDate) {
			super();
			this.bundle = bundle;
			this.concept = concept;
			this.weekOffset = weekOffset;
			this.startDate = startDate;
			this.endDate = endDate;
		}
		
		public int getId() {
			return id;
		}

		public void setId(int id) {
			this.id = id;
		}
		
		public Bundle getBundle() {
			return bundle;
		}

		public void setBundle(Bundle bundle) {
			this.bundle = bundle;
		}

		public Concept getConcept() {
			return concept;
		}

		public void setConcept(Concept concept) {
			this.concept = concept;
		}

		public int getWeekOffset() {
			return weekOffset;
		}

		public void setWeekOffset(int weekOffset) {
			this.weekOffset = weekOffset;
		}

		public LocalDate getStartDate() {
			return startDate;
		}

		public void setStartDate(LocalDate startDate) {
			this.startDate = startDate;
		}

		public LocalDate getEndDate() {
			return endDate;
		}

		public void setEndDate(LocalDate endDate) {
			this.endDate = endDate;
		}		
}
