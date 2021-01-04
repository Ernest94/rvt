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
