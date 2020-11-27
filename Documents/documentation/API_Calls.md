
# API calls



## Samenvatting

In dit document staan alle api calls die gebruikt worden gesorteerd op hoe ze in de resources staan

Alle requests moeten een ok response teruggeven als de operatie geslaagd is. Ook moet er een correcte error status worden teruggegeven als er iets fout gaat.



## Inhoud

Elke request is per J2EE resource weergegeven.

*  [User](#markdown-header-User)

*  [Review](#markdown-header-Review)

*  [ThemeConcept](#markdown-header-ThemeConcept)

*  [Location](#mardown-header-Location)

Alle input wordt meegegeven in een JSON wrapper, tenzij het in de `URI` zit.




## Requests

elke request start altijd met `/webapi`.




### User

elk user request wordt opgevolgd door `/user`


#### Login
URI: `/login`
 Header | Input | Datatype | Output | Datatype
-----   | ------|--------- | ------ | ---------
  POST  | email | String   | id     | int
|password| String| name | string
| | | role | { id: int, name: string} |
 | | | location | {id: int, name: string} |



#### Change password
URI: `/password`

Header | Input | Datatype
-----|-----|-------|---------
Post | currentPassword | string
  |  | newPassword | string
|| userId | integer

#### Get Roles
URI: `/roles`
Header | Output | Datatype
-----|-----|-------|---------
Get | roles | [{id: integer, name: string}]
 |  | locations | [{id: int, name: string}] |

#### New User
URI: `/create`

Header | Input | Datatype
 ------ | --------| ------
POST| name | string
|| email | string,
|| role | {id: integer, name: string}
|| location | {id: integer, name: string}
|| dateActive | string |

#### Link user to user (deprecated)
URI: `/linking/{userId}`
 Header | Input | Datatype | Output | Datatype
-----   | ------|--------- | ------ | ---------
  POST  | email | String   | id     | int
| | String| name | string
| | | role | { id: int, name: string} |
 | | | location | {id: int, name: string} |

  
#### Dossier

Type | URI | Geeft | Ontvangt
-----|-----|-------|---------
GET| `user/dossier/` (header userId)| Headervariable -> userId | Json -> {
||| _name_ : string,
||| _email_ : string,
||| _role_: {id : int, name : string},
||| _location_ {id : int, name : string},
||| _dateActive_: string/Date} __Check of dit een string of date type wordt__
POST| `user/change`| Json -> { | -
|| _userId_ : int,|
|| _name_ : string,|
|| _email_ : string,|
|| _role_: {id : int, name : string},|
|| _location_ {id : int, name : string},|
|| _dateActive_: string} |




POST| `user/changeRelation`| Json -> {|-
|| _userId_ : int,|
|| _changedUsers_ : [{id: int}] } |


### Search

Type | URI | Geeft | Ontvangt
-----|-----|-------|---------
GET| `user/roles`| - |Json -> {
|||_roles_ : [ {id:int, name:string} ],
|||_locations_ : [ {id:int, name:string} ] }
POST| `user/search`| Json -> { | Json -> {
|| _location_: {id: int, name: string},| _users_: [ {_id_: int, _name_: string, _email_: string, _role_: {id: int, name: string}, _location_: {id: int, name: string}} ]
|| _role_ : {id: int, name: string}|
|| _criteria_ : string }|

### Location

URI: `locations`  

Header | Input | Datatype
-------|-------|---------
POST| name | string

