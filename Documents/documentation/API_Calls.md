
# API calls


## Samenvatting

In dit document staan alle api calls die gebruikt worden gesorteerd op hoe ze in de resources staan

Alle requests moeten een ok response teruggeven als de operatie geslaagd is. Ook moet er een correcte error status worden teruggegeven als er iets fout gaat.

## Richtlijnen

- gebruik meervoud voor zelfstandige naamwoorden: **webapi/bundles** ipv webapi/bundle
- gebruik streepjes om meerdere woorden te gebruiken: **concept-status** ipv conceptStatus of concept_status
- gebruik POST voor het aanmaken, PUT voor het veranderen en GET voor het ophalen van resources
- GET requests gebruiken geen @Consumes
- Alle input wordt meegegeven in een JSON wrapper, tenzij het in de `URI` zit.
- Geef liever specifieke views terug dan persistent objects.

## Misc
- elke request heeft de @Secured annotation die checkt of de token aanwezig en valide is, behalve de login.
- elke request start met `/webapi`
- wanneer een List van Java objects wordt geretourneerd komt dit in de frontend aan als een array in response.data

## Inhoud

Elke request is per J2EE resource weergegeven.

*  [User](#user)
*  [Review](#review)
*  [Theme](#theme)
*  [Concept](#concept)
*  [Bundle](#bundle)
*  [Location](#location)


## User


### `/login`

POST
- Receives User object containing email and password.
- Returns status 200 and token if correct user and status 401 if incorrect.


### `/users/{userId}/password`

PUT
- Change password of user with id=userId
- Receives PasswordChange object with new password, current password (if requested through password js), requester User.
- Returns status 200 and the changed user or 401 if incorrect.

This request receives a requester: the user that requests the password change. This is because admins are able to change anyone's password. The function then checks if the requester is the same as the user whose password will be changed. If this is not the case, the function checks if the requester is an admin. If the requester is an admin no current password is sent or needed.


### `/roles`

GET
- Returns all Role objects.

### `/users/`

GET
- Returns a List of all User objects.

**POST** create user
- Receives UserLocationsView object.
- Returns status 201 and created User, or 412 in case of failure.

### `/users/{userId}`

GET
- Returns User with id=userId

PUT
- Updates User with id=userId
- Receives UserLocationsView object.
- Returns status 200 on success, and 412 on invalid user.

### `/users/search`

POST
- Receives Search object.
- Returns JSON wrapper with list of UserSearch objects.

### `/users/pending/teachers/{teacherId}`

GET
- Returns JSON wrapper with list of UserSearch objects. The Usersearch objects contain all Users that have pending reviews and a location included in the locations of the teacher with id=teacherId. 

TODO: this URI is not great, perhaps this call could be using Query parameters. 

## Review

### `/users/{userId}/reviews`

GET
- Returns ConceptRatingJSON object

This call gives all *confirmed* ratings for user {userId}. If a concept has multiple ratings the more recent rating is returned. 
Notice that any concept that has been given a rating is deemed active for that user.

POST
- Creates a new pending review if not yet present for user with id=userId
- Returns ConceptRatingJSON object with all current ratings, including from pending reviews. TODO: this should ideally be done in a different call.

PUT
- Updates the current pending review
- Receives Review object
- Returns status 200

### `/users/{userId}/reviews/confirm`

POST
- Set status of current pending review to complete
- Receives Review object
- Returns 202

### `/users/{userId}/reviews/cancel`

POST
- Set status of current pending review to cancelled
- Received Review Object
- Returns 202

### `/users/{userId}/reviews/ratings/`

POST
- Change the rating and/or comment on one concept of the pending review
- Receives ConceptRatingUpdate object
- Returns status 201 if new rating, or 200 if updated existing rating

## Theme

### `/themes`

GET
- Returns all Theme objects

POST
- Creates a new Theme
- Receives a Theme object
- Returns 201 on success, or 400 if invalid

## Concept

### `/concepts`

GET
- Returns all Concept objects

POST
- Creates a new Concept
- Receives a Concept object
- Returns 201 on success, or 400 if invalid

### `/trainees/{userId}/concepts/{conceptId}`

PUT
- Change active status of concept with id=conceptId for user with id=userId
- Returns 201 if a new record is made in TraineeActive, or 200 if existing record is ended.

This function checks if there is a record already changing the status of the concept for this user. If there is one, the record is ended.
Thus the status of the concept is again what is implied by bundles. If there is no current record the function checks if the concept is in a bundle assigned to the user. If so, a record is created with active=0. If it is not in bundle a record is created with active=1. This way there is no need to receive the active status.

### `/trainees/{userId}/concepts/{conceptId}/week/{newWeek}`

PUT
- Change the week of concept with id=conceptId for user with id=userId
- Returns 201 if a new record is made in TraineeMutation, or 200 if existing record is ended.

This function always creates a new record. If a current mutation exists, it is ended.
## Bundle

### `/bundles`

GET
- Returns List of BaseBundleViews of all bundles

This will *not* return the concepts that are in the bundle, for that use `/bundles-incl-concepts`.

POST
- Creates new Bundle object
- Receives Bundle object
- Returns 201 if success, or 400 if invalid.

### `/bundles-incl-concepts`

GET
- Returns List of BundleViews.

This will return all bundles including all concepts that are in the bundles. Only use this if you need the concepts.

### `/bundles/{bundleId}/concepts`

GET
- Returns the concepts that are in bundle with id=bundleId. TODO: these should probably be ConceptViews instead. 

### `/bundles/{bundleId}`

PUT
- Updates bundle with id=bundleId
- Receives List of BundleConceptWeekOffset objects
- Returns 200 on success, 404 if bundle does not exist and 412 if the bundle ids in the BundleConceptWeekOffset do not match.

### `/creators/{userId}/bundles`

GET
- Returns List of BaseBundleViews, created by user with id=userId

Admins and Teachers can create bundles. 
### `/trainees/{userId}/bundles`

GET
- Returns List of BundleTraineeViews, bundles assigned to user with id=userId

Only Trainees can have bundles assigned to them.

PUT
- Update which bundles are assigned to user with id=userId
- Receives List of BundleTraineeViews
- Returns 200 on success

## Location

### `/locations`

GET
- Returns List of all Location objects

POST
- Creates new location
- Receives Location objects
- Returns 201 on success or 400 on invalid.

