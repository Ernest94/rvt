//package nu.educom.rvt.models;
//
//import javax.persistence.*;
//
//@Entity
//@Table(name="stars")
//public class Star implements BaseEntity {
//	
//	@Id
//	@GeneratedValue(strategy=GenerationType.IDENTITY)
//	@Column(name="id")
//	private int id;
//	
//	@Column(name="description")
//	private String description;
//	@Column(name="amount")
//	private int amount;
//	
//	public Star() {
//		super();
//	}
//	
//	public Star(String description, int amount) {
//		super();
//		this.description = description;
//		this.amount = amount;
//	}
//
//	@Override
//	public int getId() {
//		return id;
//	}
//	@Override
//	public void setId(int id) {
//		this.id = id;
//	}
//	public String getDescription() {
//		return description;
//	}
//
//	public void setDescription(String description) {
//		this.description = description;
//	}
//
//	public int getAmount() {
//		return amount;
//	}
//	public void setAmount(int amount) {
//		this.amount = amount;
//	}
//
//}
