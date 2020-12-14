/**
 * 
 */
package nu.educom.rvt.repositories;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDate;
import java.util.List;

import org.hibernate.Session;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import nu.educom.rvt.models.Concept;
import nu.educom.rvt.models.Role;
import nu.educom.rvt.models.Theme;
import nu.educom.rvt.models.User;
import nu.educom.rvt.rest.Filler;

/**
 * Test class to test the repository functions
 *
 */
class TestUserRepository {

	private Session session;

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
		session = TestHibernateSession.openSessionAndTransaction();
	}
	
	/**
	 * @throws java.lang.Exception
	 */
	@AfterEach
	void tearDown() throws Exception {
		TestHibernateSession.closeSession(session);
	}
	
	@Test
	void testCreate() throws DatabaseException {
		// Prepare
		UserRepository ur = new UserRepository(session);
		Role role = new RoleRepository(session).readById(2);
		User user = new User("testUser", "test@example.com", "1234", role);
		
		// Run
		User result = ur.create(user);
		session.getTransaction().commit();
		
		// Validate
		assertNotNull(result);
		Integer newId = result.getId();
		assertTrue(newId > 0);
		
		// To ensure it is in the database, we close the session and reopen it
		TestHibernateSession.closeSession(session);
		session = TestHibernateSession.openSession();
		
		// create new Repository
		UserRepository ur2 = new UserRepository(session);
		User result2 = ur2.readById(newId);
		// check if they are the same
		assertNotNull(result2);
		assertEquals(newId, result2.getId());
		assertEquals(user.getName(), result2.getName());
		assertEquals(user.getEmail(), result2.getEmail());
		assertEquals(user.getPassword(), result2.getPassword());
		assertEquals(user.getDateActive(), result2.getDateActive());
		assertEquals(user.getDateInactive(), result2.getDateInactive());
		
		assertEquals(result, result2); // test the equals() function of user
	}

	@Test
	void testReadAll() throws DatabaseException {
		// Prepare
		UserRepository ur = new UserRepository(session);
		
		// Run
		List<User> result = ur.readAll();
		
		// Validate
		assertNotNull(result);
		assertTrue(result.size() > 2);
	}

	@Test
	void testReadById() throws DatabaseException {
		// Prepare
		UserRepository ur = new UserRepository(session);
		
		// Run
		User result = ur.readById(1);
		// Make sure the session is closed to test EAGER loading
		TestHibernateSession.closeSession(session);
		
		// Validate
		assertNotNull(result);
		assertEquals(1, result.getId());
		assertEquals("Trainee1", result.getName());
		assertEquals("Trainee", result.getRole().getName());
		assertTrue(result.getCurrentLocations().size() == 1);
	}
	@Test
	void testReadByIdAllLocations() throws DatabaseException {
		// Prepare
		UserRepository ur = new UserRepository(session);
		
		// Run
		User result = ur.readById(1);
		
		// Validate
		assertNotNull(result);
		assertEquals(1, result.getId());
		assertEquals("Trainee1", result.getName());
		assertEquals("Trainee", result.getRole().getName());
		assertTrue(result.getCurrentLocations().size() == 1);
		assertTrue(result.getAllUserLocations().size() == 2);
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
