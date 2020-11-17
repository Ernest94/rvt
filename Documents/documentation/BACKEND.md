#Documentation back-end Registratie Voortgang Trainees

## Overview
nu.educom.rvt.rest:
 - CORSFilter.java
 - Filler.java
 - Main.java
 - MyApp.java
 - MyResource.java
 - ReviewResource.java
 - ThemeConceptResource.java
 - UserResource.java
...
...


## Specific
- Main.java
This is the main file that has to be run to start the back-end of the application. First the Grizzly server is started. If the db_voortgang database doesn't exist it is made. Secondly there is a check if the roles table in the db_voortgang database is filled. When there are no roles present in the roles table the db_voortgang database is filled with 8 tables: concepts,concept_ratings,locations,reviews,roles,themes,users,user_relation.
...
...


## rest 

### Filler.java

Dit bestand checkt of de database leeg is, en als dat zo is bevat het een functie die zorgt dat de database gevuld wordt met data.
