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
@Table(name="bundle_concept")
public class BundleConcept {

		@Id
		@GeneratedValue(strategy=GenerationType.IDENTITY)
		@Column(name="id")
		private int id;
	
		@ManyToOne
		@JoinColumn(name="bundle_id")
		private Bundle bundle;
		
		@ManyToOne
		@JoinColumn(name="concept_id")
		private Concept concept;
		
		@Column(name="weekOffset") 
		private int weekOffset;
		
		@Column(name="startdate")
		private String startDate;
		
		@Column(name="enddate")
		private String endDate;
			
		@Column(name="bundle_id")
		private int bundleId;
		
		@Column(name="concept_id")
		private int conceptId;
		
		public BundleConcept() {
			super();
		}
		
		public BundleConcept(Bundle bundle, Concept concept, int weekOffset, String startDate) {
			super();
			this.bundle = bundle;
			this.concept = concept;
			this.weekOffset = weekOffset;
			this.startDate = startDate;
		}
		
		public BundleConcept(Bundle bundle, Concept concept, int weekOffset, String startDate, String endDate) {
			super();
			this.bundle = bundle;
			this.concept = concept;
			this.weekOffset = weekOffset;
			this.startDate = startDate;
			this.endDate = endDate;
		}
		
		public BundleConcept(int bundleId, int conceptId, int weekOffset, String startDate) {
			super();
			this.bundleId = bundleId;
			this.conceptId = conceptId;
			this.weekOffset = weekOffset;
			this.startDate = startDate;
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

		public String getStartDate() {
			return startDate;
		}

		public void setStartDate(String startDate) {
			this.startDate = startDate;
		}

		public String getEndDate() {
			return endDate;
		}

		public void setEndDate(String endDate) {
			this.endDate = endDate;
		}

		public int getBundleId() {
			return bundleId;
		}

		public void setBundleId(int bundleId) {
			this.bundleId = bundleId;
		}

		public int getConceptId() {
			return conceptId;
		}

		public void setConceptId(int conceptId) {
			this.conceptId = conceptId;
		}

		
		
}
