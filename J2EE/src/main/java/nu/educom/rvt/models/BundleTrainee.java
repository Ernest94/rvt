package nu.educom.rvt.models;

import java.time.LocalDate;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name="bundle_trainee")
public class BundleTrainee {

		@Id
		@GeneratedValue(strategy=GenerationType.IDENTITY)
		@Column(name="id")
		private int id;
	
		@ManyToOne
		@JoinColumn(name="user_id")
		private User user;
		
		@ManyToOne
		@JoinColumn(name="bundle_id")
		private Bundle bundle;
		
		@Column(name="startWeek") 
		private int startWeek;
		
		@Column(name="startdate")
		private String startDate;
		
		@Column(name="enddate")
		private String endDate;
		
		
		public BundleTrainee() {
			super();
		}
		
		public BundleTrainee(User user, Bundle bundle, int startWeek, String startDate) {
			super();
			this.user = user;
			this.bundle = bundle;
			this.startWeek = startWeek;
			this.startDate = startDate;
		}
		
		public BundleTrainee(User user, Bundle bundle, int startWeek, String startDate, String endDate) {
			super();
			this.user = user;
			this.bundle = bundle;
			this.startWeek = startWeek;
			this.startDate = startDate;
			this.endDate = endDate;
		}

		public int getId() {
			return id;
		}

		public void setId(int id) {
			this.id = id;
		}

		public User getUser() {
			return user;
		}

		public void setUser(User user) {
			this.user = user;
		}

		public Bundle getBundle() {
			return bundle;
		}

		public void setBundle(Bundle bundle) {
			this.bundle = bundle;
		}

		public int getStartWeek() {
			return startWeek;
		}

		public void setStartWeek(int startWeek) {
			this.startWeek = startWeek;
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

	
}


