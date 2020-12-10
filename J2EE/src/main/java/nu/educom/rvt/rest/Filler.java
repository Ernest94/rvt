package nu.educom.rvt.rest;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.Session;

import nu.educom.rvt.models.*;
import nu.educom.rvt.models.Review.Status;
import nu.educom.rvt.repositories.*;
import nu.educom.rvt.services.UserService;

public class Filler {
    
	private static final Logger LOG = LogManager.getLogger();
	/* JH: Gebruik geen <tab> karakters  */
    public static Boolean isDatabaseEmpty() {
        try (Session session = HibernateSession.getSessionFactory().openSession()) {
            RoleRepository rolesRepo = new RoleRepository(session);
            List<Role> roles = rolesRepo.readAll();
            return roles.size() <= 0 ;    
        } catch (DatabaseException e) {
            // TODO LOG
            e.printStackTrace();
            return false;
        }
    }
    
    public static void fillDatabase() {
        try (Session session = HibernateSession.openSessionAndTransaction()) {
            
            LocalDateTime weekAgo = LocalDateTime.now().minus(7, ChronoUnit.DAYS);
            LocalDateTime dayAgo = LocalDateTime.now().minus(1, ChronoUnit.DAYS);
            LocalDateTime nowLDT = LocalDateTime.now();
    //        LocalDateTime endDate = null;
            
            LocalDate nowLD = LocalDate.now();
            LocalDate endDateLD = null;
            
            //FILL THE USER TABLE
            List<Role> roles = new ArrayList<Role>();
            roles.add(new Role("Admin"));
            roles.add(new Role("Docent"));
            roles.add(new Role("Trainee"));
            roles.add(new Role("Sales"));
            roles.add(new Role("Office"));
            RoleRepository roleRepo = new RoleRepository(session);
            for (Role role : roles) {
                roleRepo.create(role);
            }
            
            //FILL THE LOCATION TABLE
            List<Location> locations = new ArrayList<Location>();
            locations.add(new Location("Utrecht"));
            locations.add(new Location("Arnhem"));
            locations.add(new Location("Sittard"));
            locations.add(new Location("Eindhoven"));
            LocationRepository locationRepo = new LocationRepository(session);
            for (Location location : locations) {
                locationRepo.create(location);
            }
            
            
            
            //FILL THE USER TABLE
            User trainee1 = new User("Trainee1", "trainee1@educom.nu", "3vDOqHO*B%5i6O@HlW", roles.get(2), locations.get(0),nowLD, endDateLD);
            User trainee2 = new User("Trainee2", "trainee2@educom.nu", "3vDOqHO*B%5i6O@HlW", roles.get(2), locations.get(0),nowLD, endDateLD);
            User trainee3 = new User("Trainee3", "trainee3@educom.nu", "3vDOqHO*B%5i6O@HlW", roles.get(2), locations.get(0),nowLD, endDateLD);
    
            User docent1 = new User("Docent1", "docent1@educom.nu", "5^mBejfdV0Rt509x$n", roles.get(1), locations.get(0),nowLD,endDateLD);
    
            List<User> users = new ArrayList<User>();
            users.add(new User("Admin", "admin@educom.nu", "AyW0BdSKojK^Uw4LRQ", roles.get(0), locations.get(0),nowLD, endDateLD));
            users.add(new User("Jeffrey Manders", "jem@edu-deta.com", "a5G&36wOfL644ZJ!2y", roles.get(0), locations.get(0),nowLD, endDateLD));
            users.add(new User("Docent", "docent@educom.nu", "5^mBejfdV0Rt509x$n", roles.get(1), locations.get(0),nowLD,endDateLD));
            users.add(new User("Sales", "sales@educom.nu", "xA8PF&0yN*Ye5#2Vnz", roles.get(3), locations.get(0),nowLD, endDateLD));
            users.add(new User("Office", "office@educom.nu", "eYOPEzEDq^YMlJ7$9D", roles.get(4), locations.get(0),nowLD, endDateLD));
            
            
            users.add(trainee1);
            users.add(trainee2);
            users.add(trainee3);
            users.add(docent1);
            UserService userService = new UserService(session);
            for (User user : users) {
                userService.addUser(user);
            }        
            
            //FILL THE THEME TABLE
            List<Theme> themes = new ArrayList<Theme>();
            themes.add(new Theme("Webserver","WS","werking en configuratie van een HTTP server opzetten, Request - response principe, HTTP en HTTPS en limitaties op ongeoorloofde toegang."));
            themes.add(new Theme("IDE","IDE","werken met een geavanceerde editor of geïntegreerde ontwikkelomgeving, bouwen vanuit deze omgeving, gebruik shortcuts en debuggen."));
            themes.add(new Theme("Front end","FE","maken van een frontend deel van een applicatie met HTML, CSS, en Javascript. Met aandacht voor hoe het er voor de gebruiker netjes en overzichtelijk uitziet."));
            themes.add(new Theme("Coding","CD","principes van een de programmeertaal de syntax, gebruik van datatypen, D.R.Y. & clean code, decompositie van functies, afhandelen van URL verzoeken, werken met bestanden."));
            themes.add(new Theme("Database (relationeel)","DB","gebruik van SQL om zaken in de database gedaan te krijgen, database normalisatie en opbouwen van database diagrammen."));
            themes.add(new Theme("Connecties (API)","API","gebruik van een RESTfull API of er zelf een aanbieden."));
            themes.add(new Theme("Programming paradigms","PP","bekend zijn met zaken die in meerdere object georiënteerde talen terugkomen zoals design patronen als Factory en Singelton, encapulatie, static en abstract."));
            themes.add(new Theme("Ontwerp methodieken","OM","ontwerptechnieken die taal overschrijdend zijn zoals MVC, MVP, MVVM."));
            themes.add(new Theme("Problem solving","PS","maken van een ontwerp, code goed gescheiden houden, oplossen van bug en de voorspelbaarheid van de code."));
            themes.add(new Theme("Werken in/ aan projecten","PRJ.","technieken die belangrijk zijn voor het werken aan grote projecten zoals het overzichtelijk structureren van je bestanden werken met Agile/Scrum methodiek, versiebeheer etc."));
            themes.add(new Theme("Beeldvorming","BV","hoever kan je geleerde technieken meenemen naar nieuwe talen, wat zijn de overeenkomsten en de verschillen."));
            ThemeRepository themeRepo = new ThemeRepository(session);
            for (Theme theme : themes) {
                themeRepo.create(theme);
            }
    
            //FILL THE CONCEPT TABLE
            List<Concept> concepts = new ArrayList<Concept>();
            //thema webserver
            //week 1&2
    		concepts.add(new Concept(themes.get(0),"webserver opzetten",null,nowLD,endDateLD));
    		concepts.add(new Concept(themes.get(0),"document root, local host",null,nowLD,endDateLD));
    		concepts.add(new Concept(themes.get(0),"request/response flow",null,nowLD,endDateLD));
    		//thema IDE
    		//week 1&2
    		concepts.add(new Concept(themes.get(1),"code omgeving basis",null,nowLD,endDateLD));
    		//thema front end
    		//week 1&2
    		concepts.add(new Concept(themes.get(2),"documentopbouw",null,nowLD,endDateLD));
    		concepts.add(new Concept(themes.get(2),"pagina indeling (divisions, paragraphs, lists)",null,nowLD,endDateLD));
    		concepts.add(new Concept(themes.get(2),"interaction- anchors, forms/inputfields",null,nowLD,endDateLD));
    		concepts.add(new Concept(themes.get(2),"uiterlijk (colors, fonts, borders)",null,nowLD,endDateLD));
    		concepts.add(new Concept(themes.get(2),"weergave( inline, block, margin/padding)",null,nowLD,endDateLD));
    		//week 3&4
    		concepts.add(new Concept(themes.get(2),"wireframe voor GUI design",null,nowLD,endDateLD));
    		concepts.add(new Concept(themes.get(2),"framework",null,nowLD,endDateLD));		
    		//week5&6
    		concepts.add(new Concept(themes.get(2),"Template Enige",null,nowLD,endDateLD));
    		concepts.add(new Concept(themes.get(2),"JavaScript Front End",null,nowLD,endDateLD));
    		concepts.add(new Concept(themes.get(2),"AJAX",null,nowLD,endDateLD));
    		//week 7 t/m 9
    		concepts.add(new Concept(themes.get(2),"Advanced UI",null,nowLD,endDateLD));
    		//week 10 t/m 12
    		concepts.add(new Concept(themes.get(2),"Front-end Frameworks",null,nowLD,endDateLD));
    		//thema coding
    		//week 1&2
    		concepts.add(new Concept(themes.get(3),"variabelen,data types",null,nowLD,endDateLD));
    		concepts.add(new Concept(themes.get(3),"iteraties (loops), Selecties (if/else, switch)",null,nowLD,endDateLD));
    		concepts.add(new Concept(themes.get(3),"Functies en Scope",null,nowLD,endDateLD));
    		concepts.add(new Concept(themes.get(3),"sequenties",null,nowLD,endDateLD));
    		concepts.add(new Concept(themes.get(3),"Post-afhandeling",null,nowLD,endDateLD));
    		concepts.add(new Concept(themes.get(3),"URL parameters",null,nowLD,endDateLD));
    		concepts.add(new Concept(themes.get(3),"File Handling(read/write)",null,nowLD,endDateLD));
    		concepts.add(new Concept(themes.get(3),"bron code organisatie",null,nowLD,endDateLD));
    		concepts.add(new Concept(themes.get(3),"herbruikbare code",null,nowLD,endDateLD));
    		concepts.add(new Concept(themes.get(3),"debuggen",null,nowLD,endDateLD));
    		concepts.add(new Concept(themes.get(3),"clean code",null,nowLD,endDateLD));
    		concepts.add(new Concept(themes.get(3),"dry code",null,nowLD,endDateLD));
    		//week 3&4
    		concepts.add(new Concept(themes.get(3),"multiparts forms",null,nowLD,endDateLD));
    		concepts.add(new Concept(themes.get(3),"$_post + $_files",null,nowLD,endDateLD));
    		concepts.add(new Concept(themes.get(3),"sessions en connections",null,nowLD,endDateLD));
    		concepts.add(new Concept(themes.get(3),"defensief programmeren",null,nowLD,endDateLD));
    		concepts.add(new Concept(themes.get(3),"map en dictionary",null,nowLD,endDateLD));
    		concepts.add(new Concept(themes.get(3),"error handling (try, catch & finally)",null,nowLD,endDateLD));
    		concepts.add(new Concept(themes.get(3),"error logging",null,nowLD,endDateLD));
    		//week 5&6
    		concepts.add(new Concept(themes.get(3),"frameworks",null,nowLD,endDateLD));
    		//week 7 t/m 9
    		concepts.add(new Concept(themes.get(3),"2e taal al het voorgaande & OOP",null,nowLD,endDateLD));
    		//week 10 t/m 12
    		concepts.add(new Concept(themes.get(3),"2e taal verdieping",null,nowLD,endDateLD));		
    		//thema Database (relationeel)
    		//week 1&2
    		concepts.add(new Concept(themes.get(4),"normalisatie en databasebouw",null,nowLD,endDateLD));
    		concepts.add(new Concept(themes.get(4),"queries (Select, where, group by)",null,nowLD,endDateLD));
    		//week 3&4
    		concepts.add(new Concept(themes.get(4),"queries (join, having)",null,nowLD,endDateLD));
    		concepts.add(new Concept(themes.get(4),"nested / sub- queries",null,nowLD,endDateLD));
    		//week 7 t/m 9
    		concepts.add(new Concept(themes.get(4),"transactions (ROLLBACK en COMMIT)",null,nowLD,endDateLD));
    		//week 10 t/m 12
    		concepts.add(new Concept(themes.get(4),"kennis van no-sql",null,nowLD,endDateLD));
    		//thema connecties (API)
    		//week 3&4
    		concepts.add(new Concept(themes.get(5),"API's gebruiken",null,nowLD,endDateLD));		
    		//week 5&6
    		concepts.add(new Concept(themes.get(5),"API's bouwen",null,nowLD,endDateLD));		
    		//week 7 t/m 9
    		concepts.add(new Concept(themes.get(5),"API's bouwen",null,nowLD,endDateLD));
    		//thema programming paradigms
    		//week 1&2
    		concepts.add(new Concept(themes.get(6),"OOP (objecten, methods, properties)",null,nowLD,endDateLD));
    		//week 3&4
    		concepts.add(new Concept(themes.get(6),"OOP (encapsulation, polymorphism)",null,nowLD,endDateLD));
    		concepts.add(new Concept(themes.get(6),"OOP (abstract Methods en classes)",null,nowLD,endDateLD));
    		concepts.add(new Concept(themes.get(6),"OOP (static Methods en properties)",null,nowLD,endDateLD));
    		//week 5&6
    		concepts.add(new Concept(themes.get(6),"Design Patterns (PHP)",null,nowLD,endDateLD));
    		//week 7 t/m 9
    		concepts.add(new Concept(themes.get(6),"Design Patterns (JS)",null,nowLD,endDateLD));
    		//thema ontwerp methodieken
    		//week 7 t/m 9
    		concepts.add(new Concept(themes.get(7),"MVC",null,nowLD,endDateLD));
    		concepts.add(new Concept(themes.get(7),"MVVM",null,nowLD,endDateLD));
    		concepts.add(new Concept(themes.get(7),"MPC",null,nowLD,endDateLD));
    		//thema problem solving
    		//week 1&2
    		concepts.add(new Concept(themes.get(8),"algorithme ontwerp",null,nowLD,endDateLD));
    		concepts.add(new Concept(themes.get(8),"clean code",null,nowLD,endDateLD));
    		//week 3&4
    		concepts.add(new Concept(themes.get(8),"algorithme ontwerp",null,nowLD,endDateLD));
    		concepts.add(new Concept(themes.get(8),"algorithme ontwerp",null,nowLD,endDateLD));
    		//thema werken in/ aan projecten
    		//week 1&2
    		concepts.add(new Concept(themes.get(9),"coding styles ",null,nowLD,endDateLD));
    		concepts.add(new Concept(themes.get(9),"file structuur",null,nowLD,endDateLD));
    		concepts.add(new Concept(themes.get(9),"programma structuur",null,nowLD,endDateLD));
    		//week 3&4
    		concepts.add(new Concept(themes.get(9),"project management",null,nowLD,endDateLD));
    		concepts.add(new Concept(themes.get(9),"versiebeheer",null,nowLD,endDateLD));
    		//week 7 t/m 9
    		concepts.add(new Concept(themes.get(9),"deployment",null,nowLD,endDateLD));
    		//thema beeldvorming
    		//week 1&2
    		concepts.add(new Concept(themes.get(10),"verschilllen en overeenkomsten talen",null,nowLD,endDateLD));
            ConceptRepository conceptRepo = new ConceptRepository(session);
            for (Concept concept : concepts) {
                conceptRepo.create(concept);
            }    
    
            //MOCK DATA    
            
            //FILL THE REVIEW TABLE
            ReviewRepository reviewRepo = new ReviewRepository(session);
            Review review1 = new Review(weekAgo, "Matig bezig trainee1", "Deze trainee is meh bezig", Review.Status.PENDING, trainee1);
            Review review2 = new Review(dayAgo, "Redelijk bezig trainee1", "Deze trainee is voldoende bezig", Review.Status.PENDING, trainee1);
            Review review3 = new Review(nowLDT, "Goed bezig trainee1", "Deze trainee is prima bezig", Review.Status.PENDING, trainee1);
            Review review4 = new Review(dayAgo, "Goed bezig trainee", "Deze trainee is prima bezig", Review.Status.PENDING, trainee2);
            reviewRepo.create(review1);
            reviewRepo.create(review2);
            reviewRepo.create(review3);
            reviewRepo.create(review4);
            
            //FILL THE CONCEPT-RATING TABLE
            List<ConceptRating> conceptsRatings = new ArrayList<ConceptRating>();
            conceptsRatings.add(new ConceptRating(review1,concepts.get(0),2, "Kan beter"));
            conceptsRatings.add(new ConceptRating(review1,concepts.get(1),2, "Kan beter"));
            conceptsRatings.add(new ConceptRating(review1,concepts.get(2),2, "Kan beter"));
            conceptsRatings.add(new ConceptRating(review1,concepts.get(3),2, "Kan beter"));
            conceptsRatings.add(new ConceptRating(review1,concepts.get(4),2, "Kan beter"));
                    
            conceptsRatings.add(new ConceptRating(review2,concepts.get(1),3, "Goed, maar komop"));
            conceptsRatings.add(new ConceptRating(review2,concepts.get(2),3, "Je komt er wel"));
            conceptsRatings.add(new ConceptRating(review2,concepts.get(3),3, "Je moet iets meer letten op je stijl"));
            conceptsRatings.add(new ConceptRating(review2,concepts.get(4),3, "no comment"));
            conceptsRatings.add(new ConceptRating(review2,concepts.get(5),3));
            
            conceptsRatings.add(new ConceptRating(review3,concepts.get(0),5));
            conceptsRatings.add(new ConceptRating(review3,concepts.get(1),5));
            conceptsRatings.add(new ConceptRating(review3,concepts.get(2),5));
            conceptsRatings.add(new ConceptRating(review3,concepts.get(3),5));
            conceptsRatings.add(new ConceptRating(review3,concepts.get(4),5));
            conceptsRatings.add(new ConceptRating(review3,concepts.get(5),5));
            
            conceptsRatings.add(new ConceptRating(review4,concepts.get(0),4));
            conceptsRatings.add(new ConceptRating(review4,concepts.get(1),4));
            conceptsRatings.add(new ConceptRating(review4,concepts.get(2),4));
            conceptsRatings.add(new ConceptRating(review4,concepts.get(3),4));
            conceptsRatings.add(new ConceptRating(review4,concepts.get(4),4));
            conceptsRatings.add(new ConceptRating(review4,concepts.get(5),4));
            ConceptRatingRepository conceptRatingRepo = new ConceptRatingRepository(session);
            for (ConceptRating conceptRating : conceptsRatings) {
                conceptRatingRepo.create(conceptRating);
            }    
            
//            review1.setReviewStatus(Status.COMPLETED);
//            reviewRepo.update(review1);
//            review2.setReviewStatus(Status.COMPLETED);
//            reviewRepo.update(review2);
//            review4.setReviewStatus(Status.COMPLETED);
//            reviewRepo.update(review4);
            
            //FILL THE BUNDLE TABLE
            BundleRepository bundleRepo = new BundleRepository(session);
            
            Bundle bundle1 = new Bundle("Starters bundel", docent1, nowLD, endDateLD);
            Bundle bundle2 = new Bundle("JavaScript bundel", docent1, nowLD, endDateLD);
    
            bundleRepo.create(bundle1);
            bundleRepo.create(bundle2);
            
            //FILL THE BUNDLECONCEPT TABLE
            BundleConceptRepository bundleConceptRepo = new BundleConceptRepository(session);
            List<BundleConcept> BundleConcepts = new ArrayList<BundleConcept>();
            BundleConcepts.add(new BundleConcept(bundle1, concepts.get(0), 0, nowLD, endDateLD));
            BundleConcepts.add(new BundleConcept(bundle1, concepts.get(1), 0, nowLD, endDateLD));
            BundleConcepts.add(new BundleConcept(bundle1, concepts.get(2), 2, nowLD, endDateLD));
            BundleConcepts.add(new BundleConcept(bundle1, concepts.get(3), 0, nowLD, endDateLD));
            BundleConcepts.add(new BundleConcept(bundle1, concepts.get(5), 9, nowLD, endDateLD));
            BundleConcepts.add(new BundleConcept(bundle1, concepts.get(8), 2, nowLD, endDateLD));
            BundleConcepts.add(new BundleConcept(bundle1, concepts.get(10), 0, nowLD, endDateLD));
            BundleConcepts.add(new BundleConcept(bundle1, concepts.get(12), 0, nowLD, endDateLD));
            BundleConcepts.add(new BundleConcept(bundle2, concepts.get(2), 4, nowLD, endDateLD));
            BundleConcepts.add(new BundleConcept(bundle2, concepts.get(23), 0, nowLD, endDateLD));
            BundleConcepts.add(new BundleConcept(bundle2, concepts.get(24), 9, nowLD, endDateLD));
            BundleConcepts.add(new BundleConcept(bundle2, concepts.get(5), 0, nowLD, endDateLD));
            for (BundleConcept bundleConcept : BundleConcepts) {
                bundleConceptRepo.create(bundleConcept);
            }    
    
            //FILL THE BUNDLETRAINEE TABLE
            BundleTraineeRepository bundleTraineeRepo = new BundleTraineeRepository(session);
    
            BundleTrainee bundleTrainee1 = new BundleTrainee(trainee1, bundle1, 1, nowLD, endDateLD);
            BundleTrainee bundleTrainee2 = new BundleTrainee(trainee2, bundle1, 1, nowLD, endDateLD);
    
            bundleTraineeRepo.create(bundleTrainee1);
            bundleTraineeRepo.create(bundleTrainee2);
            session.getTransaction().commit();
        } catch (DatabaseException e) {
            LOG.error("Filling database failed", e);
        }
    }
}
