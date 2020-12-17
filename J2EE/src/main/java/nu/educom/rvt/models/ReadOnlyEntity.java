package nu.educom.rvt.models;

import java.time.LocalDate;

/**
 * A read-only entity means that when this entity needs to be modified, this record needs to be given an end date, and a new record with the new values needs to be created with the same date as start data.
 */
public interface ReadOnlyEntity extends BaseEntity {

	LocalDate getStartDate();

	LocalDate getEndDate();

	void setEndDate(LocalDate endDate);

}