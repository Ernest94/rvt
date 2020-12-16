//package nu.educom.rvt.models;
//
//import javax.persistence.*;
//
//@Entity
//@Table(name="weekblocks")
//public class Weekblock implements BaseEntity {
//
//	
//	@Id
//	@GeneratedValue(strategy=GenerationType.IDENTITY)
//	@Column(name="id")
//	private int id;
//	
//	@Column(name="name")
//	private String name;
//	@Column(name="Sequence")
//	private int sequence;
//	
//	public Weekblock() {
//		super();
//	}
//	
//	
//	public Weekblock(String name, int sequence) {
//		super();
//		this.name = name;
//		this.sequence = sequence;
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
//	public String getName() {
//		return name;
//	}
//	public void setName(String name) {
//		this.name = name;
//	}
//	public int getSequence() {
//		return sequence;
//	}
//	public void setSequence(int sequence) {
//		this.sequence = sequence;
//	}
//}
