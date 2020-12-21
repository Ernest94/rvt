package nu.educom.rvt.repositories;

public class EntryNotFoundException extends DatabaseException {

	private static final long serialVersionUID = -2210056470669968680L;

	public EntryNotFoundException(Class<?> className, int id) {
		super(className.getSimpleName() + " not found with id " + id);
	}
}
