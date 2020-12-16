//package nu.educom.rvt.models;
//
//import javax.persistence.*;
//
//@Entity
//@Table(name="curriculum_concept")
//public class CurriculumConcept implements BaseEntity {
//	
//	@Id
//	@GeneratedValue(strategy=GenerationType.IDENTITY)
//	@Column(name="id")
//	private int id;
//	
//	@Column(name="comment")
//	private String comment;
//	
//	@ManyToOne
//	@JoinColumn(name="concept_id")
//	private Concept concept;
//	
//	@ManyToOne
//	@JoinColumn(name="star_id")
//	private Star star;
//	
//	@ManyToOne
//	@JoinColumn(name="weekblock_id")
//	private Weekblock weekblock;
//	
//	@ManyToOne
//	@JoinColumn(name="curriculum_id")
//	private Curriculum curriculum;
//	
//	public CurriculumConcept() {
//		super();
//	}
//
//	@Override
//	public int getId() {
//		return id;
//	}
//
//	@Override
//	public void setId(int id) {
//		this.id = id;
//	}
//
//	public String getComment() {
//		return comment;
//	}
//
//	public void setComment(String comment) {
//		this.comment = comment;
//	}
//
//	public Concept getConcept() {
//		return concept;
//	}
//
//	public void setConcept(Concept concept) {
//		this.concept = concept;
//	}
//
//	public Star getStar() {
//		return star;
//	}
//
//	public void setStar(Star star) {
//		this.star = star;
//	}
//
//	public Weekblock getWeekblock() {
//		return weekblock;
//	}
//
//	public void setWeekblock(Weekblock weekblock) {
//		this.weekblock = weekblock;
//	}
//
//	public Curriculum getCurriculum() {
//		return curriculum;
//	}
//
//	public void setCurriculum(Curriculum curriculum) {
//		this.curriculum = curriculum;
//	}
//}
