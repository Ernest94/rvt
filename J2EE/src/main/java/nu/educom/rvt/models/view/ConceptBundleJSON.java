package nu.educom.rvt.models.view;
import java.io.Serializable;
import java.util.List;


public class ConceptBundleJSON implements Serializable{

		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		private List<ConceptView> concepts;
		private List<BundleView> bundlesConcepts;
		
		public ConceptBundleJSON(List<ConceptView> concepts, List<BundleView> bundlesConcepts) {
			this.concepts = concepts;
			this.bundlesConcepts = bundlesConcepts;
		}
		
		public List<ConceptView> getConcepts() {
			return concepts;
		}
		public void setConcepts(List<ConceptView> concepts) {
			this.concepts = concepts;
		}
		public List<BundleView> getBundlesConcepts() {
			return bundlesConcepts;
		}
		public void setBundlesConcepts(List<BundleView> bundlesConcepts) {
			this.bundlesConcepts = bundlesConcepts;
		}
		public static long getSerialversionuid() {
			return serialVersionUID;
		}
		

}
//package nu.educom.rvt.models.view;
//import java.io.Serializable;
//import java.util.List;
//
//import nu.educom.rvt.models.BundleConcept;
//import nu.educom.rvt.models.Concept;
//
//
//public class ConceptBundleJSON implements Serializable{
//
//		/**
//		 * 
//		 */
//		private static final long serialVersionUID = 1L;
//		private List<Concept> concepts;
//		private List<BundleConcept> bundlesConcepts;
//		
//		public ConceptBundleJSON(List<Concept> concepts, List<BundleConcept> bundlesConcepts) {
//			this.concepts = concepts;
//			this.bundlesConcepts = bundlesConcepts;
//		}
//
//		public List<Concept> getConcepts() {
//			return concepts;
//		}
//
//		public void setConcepts(List<Concept> concepts) {
//			this.concepts = concepts;
//		}
//
//		public List<BundleConcept> getBundlesConcepts() {
//			return bundlesConcepts;
//		}
//
//		public void setBundlesConcepts(List<BundleConcept> bundlesConcepts) {
//			this.bundlesConcepts = bundlesConcepts;
//		}
//
//		public static long getSerialversionuid() {
//			return serialVersionUID;
//		}
//		
//		
//}
