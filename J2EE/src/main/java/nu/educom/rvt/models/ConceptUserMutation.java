//package nu.educom.rvt.models;
//
//import javax.persistence.Column;
//import javax.persistence.Entity;
//import javax.persistence.GeneratedValue;
//import javax.persistence.GenerationType;
//import javax.persistence.Id;
//import javax.persistence.JoinColumn;
//import javax.persistence.ManyToOne;
//import javax.persistence.Table;
//
//
//@Entity
//@Table(name="concept_user_mutation")
//public class ConceptUserMutation implements BaseEntity {
//
//		@Id
//		@GeneratedValue(strategy=GenerationType.IDENTITY)
//		@Column(name="id")
//		private int id;
//	
//		@ManyToOne
//		@JoinColumn(name="concept_id")
//		private Concept concept;
//		
//		@ManyToOne
//		@JoinColumn(name="user_id")
//		private User user;
//		
//		@Column(name="active")
//		private boolean active;
//		
//		@Column(name="week")
//		private int week;
//
//		public ConceptUserMutation() {
//			super();
//		}
//		
//		public ConceptUserMutation(User user, Concept concept, boolean active) {
//			super();
//			this.user = user;
//			this.concept = concept;
//			this.active = active;
//        }
//        
//        public ConceptUserMutation(User user, Concept concept, int week) {
//			super();
//			this.user = user;
//			this.concept = concept;
//            this.week = week;
//		}
//		
//		public ConceptUserMutation(User user, Concept concept, boolean active, int week) {
//			super();
//			this.user = user;
//			this.concept = concept;
//            this.active = active;
//            this.week = week;
//		}
//
//		@Override
//		public int getId() {
//			return id;
//		}
//
//		@Override
//		public void setId(int id) {
//			this.id = id;
//		}
//
//		public Concept getConcept() {
//			return concept;
//		}
//
//		public void setConcept(Concept concept) {
//			this.concept = concept;
//		}
//
//		public User getUser() {
//			return user;
//		}
//
//		public void setUser(User user) {
//			this.user = user;
//		}
//
//		public boolean isActive() {
//			return active;
//		}
//
//		public void setActive(boolean active) {
//			this.active = active;
//		}
//
//		public int getWeek() {
//			return week;
//		}
//
//		public void setWeek(int week) {
//			this.week = week;
//		}
//}
