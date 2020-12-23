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

import nu.educom.rvt.models.Location;
import nu.educom.rvt.models.Role;
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
		Role role = new RoleRepository(session).readByKnownId(2);
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
		User result2 = ur2.readByKnownId(newId);
		// check if they are the same
		assertNotNull(result2);
		assertEquals(newId, result2.getId());
		assertEquals(user.getName(), result2.getName());
		assertEquals(user.getEmail(), result2.getEmail());
		assertEquals(user.getPassword(), result2.getPassword());
		assertEquals(user.getStartDate(), result2.getStartDate());
		assertEquals(user.getEndDate(), result2.getEndDate());
		
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
		User result = ur.readByKnownId(1);
		// Make sure the session is closed to test EAGER loading
		TestHibernateSession.closeSession(session);
		
		// Validate
		assertNotNull(result);
		assertEquals(1, result.getId());
		assertEquals("Trainee1", result.getName());
		assertEquals("Trainee", result.getRole().getName());
		assertEquals(1, result.getCurrentLocations().size());
	}
	@Test
	void testReadByIdAllLocations() throws DatabaseException {
		// Prepare
		UserRepository ur = new UserRepository(session);
		
		// Run
		User result = ur.readByKnownId(1);
		
		// Validate
		assertNotNull(result);
		assertEquals(1, result.getId());
		assertEquals("Trainee1", result.getName());
		assertEquals("Trainee", result.getRole().getName());
		assertEquals(1, result.getCurrentLocations().size()); // "TestLocatie"
		assertEquals(2, result.getAllUserLocations().size()); // inactive "Arnhem" & active "Testlocatie"
	}
	
	@Test
	void testUpdate() throws DatabaseException {
		// Prepare
		UserRepository ur = new UserRepository(session);
		Role role = new RoleRepository(session).readByKnownId(3);
		User newUser = new User("Test", "test@example.com", "22211", role, LocalDate.ofYearDay(1999, 24), LocalDate.now());
		newUser.setId(3);
		
		// Run
		User result = ur.update(newUser);
		
		// Validate
		assertNotNull(result);
		assertEquals(3, result.getId());
		assertEquals("Test", result.getName());
		assertEquals("Trainee", result.getRole().getName());
		assertEquals("test@example.com", result.getEmail());
		assertNotEquals("22211", result.getPassword()); // Password should not be updated
		assertEquals(LocalDate.ofYearDay(1999, 24), result.getStartDate());
		assertEquals(LocalDate.now(), result.getEndDate());
		assertTrue(result.getCurrentLocations().size() == 1);
	}
	
	@Test
	void testUpdateLocations() throws DatabaseException {
		// Prepare
		UserRepository ur = new UserRepository(session);
		List<Location> locations = new LocationRepository(session).readAll();
		User user = ur.readByKnownId(2);
		
		// Run
		ur.updateLocations(user, locations);
		session.getTransaction().commit();
		
		// Validate
		TestHibernateSession.closeSession(session);
		session = TestHibernateSession.openSessionAndTransaction();		
		UserRepository ur2 = new UserRepository(session);
		User result = ur2.readByKnownId(2);
		
		assertNotNull(result);
		assertEquals(2, result.getId());
		assertEquals("Trainee2", result.getName());
		assertEquals("Trainee", result.getRole().getName());
		assertEquals(5, result.getCurrentLocations().size()); // Testlocatie, Utrecht, Arnhem, Sittard en Eindhoven
		assertEquals(6, result.getAllUserLocations().size()); // inactive Arnhem + active Utrecht, Arnhem, Sittard en Eindhoven
		
		// Follow-up remove 1 location
		locations.remove(new Location("Sittard"));
		user = ur2.readByKnownId(2);
		
		// Run
		ur2.updateLocations(user, locations);
		session.getTransaction().commit();
		
		// Validate
		TestHibernateSession.closeSession(session);
		session = TestHibernateSession.openSession();		
		UserRepository ur3 = new UserRepository(session);
		User result2 = ur3.readByKnownId(2);
		
		assertNotNull(result2);
		assertEquals(2, result2.getId());
		assertEquals("Trainee2", result2.getName());
		assertEquals("Trainee", result2.getRole().getName());
		assertEquals(4, result2.getCurrentLocations().size()); // Testlocatie, Utrecht, Arnhem en Eindhoven
		assertEquals(6, result2.getAllUserLocations().size()); // inactive Arnhem & Sittard + active TestLocatie, Utrecht, Arnhem en Eindhoven
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
