/**
 * 
 */
package nu.educom.rvt.repositories;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDate;
import java.util.List;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import nu.educom.rvt.models.Concept;
import nu.educom.rvt.models.Theme;
import nu.educom.rvt.rest.Filler;

/**
 * Test class to test the repository functions
 *
 */
class TestConceptRepository {

	/**
	 * @throws java.lang.Exception
	 */
	@BeforeAll
	static void setUpBeforeClass() throws Exception {
		TestHibernateSession.switchToTestDatabase();
		Filler.fillDatabase();
	}

	/**
	 * @throws java.lang.Exception
	 */
	@BeforeEach
	void setUp() throws Exception {
		// Optional create database here for every test
	}
	
	/**
	 * @throws java.lang.Exception
	 */
	@AfterEach
	void tearDown() throws Exception {
		TestHibernateSession.closeSession();
	}
	
	@Test
	void testCreate() {
		// Prepare
		ConceptRepository cr = new ConceptRepository();
		LocalDate startDate = LocalDate.of(2020, 11, 2);
		LocalDate endDate = null;
		ThemeRepository tr = new ThemeRepository();
		Theme theme = tr.readByName("API");
		Concept concept = new Concept(theme, "testConcept", "Description of test concept",startDate, endDate);
		
		// Run
		Concept result = cr.create(concept);
		
		// Validate
		assertNotNull(result);
		Integer newId = result.getId();
		assertTrue(newId > 0);
		
		// To ensure it is in the database, we close the session and reopen it
		TestHibernateSession.closeSession();
		
		// create new Repository
		ConceptRepository cr2 = new ConceptRepository();
		Concept result2 = cr2.readById(newId);
		// check if they are the same
		assertNotNull(result2);
		assertEquals(newId, result2.getId());
		assertEquals(concept.getName(), result2.getName());
		assertEquals(concept.getDescription(), result2.getDescription());
		assertEquals(concept.getTheme(), result2.getTheme());
		assertEquals(concept.getStartDate(), result2.getStartDate());
		assertEquals(concept.getEndDate(), result2.getEndDate());
		
		assertEquals(result, result2); // test the equals() function of concept
	}

	@Test
	void testReadAll() {
		// Prepare
		ConceptRepository cr = new ConceptRepository();
		
		// Run
		List<Concept> result = cr.readAll();
		
		// Validate
		assertNotNull(result);
		assertTrue(result.size() > 50);
	}

	@Test
	void testReadById() {
		// Prepare
		ConceptRepository cr = new ConceptRepository();
		
		// Run
		Concept result = cr.readById(3);
		
		// Validate
		assertNotNull(result);
		assertEquals(3, result.getId());
		assertEquals("request/response flow", result.getName());
	}
	
	@Test
	@Disabled("Update() not implemented")
	void testUpdate() {
		// Prepare
		
		// Run
		
		// Validate
		fail("Not implemented");
	}
	
	@Test
	@Disabled("Delete() not implemented")
	void testDelete() {
		// Prepare
		
		// Run
		
		// Validate
		fail("Not implemented");
	}

}
