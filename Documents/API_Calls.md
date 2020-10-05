# API calls

## Samenvatting
In dit document staan alle api calls die gebruikt worden in de frontend React vanuit de frontend perspectief.
Alle requests moeten een ok response teruggeven als de operatie geslaagd is. Ook moet er een correcte error status worden teruggegeven als er iets fout gaat.

## Inhoud
Elke request is per React component weergegeven.

* [Login](#markdown-header-Login)
* [AddUser](#markdown-header-AddUser)
* [Password](#markdown-header-Password)
* [Dossier](#markdown-header-Dossier)
* [LinkedUsers](#markdown-header-LinkedUsers)
* [Search](#markdown-header-Search)

## Requests
elke request start altijd met `/webapi`.

### Login

Type | URI | Geeft | Ontvangt
-----|-----|-------|---------
POST | `user/login`|Json -> { | Json -> {
||_email_ : String, | _id_ : int,
||_password_ : String} | _name_ : string,
||| _role_ : {id:int, name: string},
||| _location_ : {id: int, name: string}}

### AddUser
Type | URI | Geeft | Ontvangt
-----|-----|-------|---------
GET|`user/rlt`|-|Json ->  {    
|||_roles_ : [ {id: int, name: string} ],  
|||_locations_ : [ {id: int, name:string} ],
|||_teachers_ : [ {id: int, name: string} ] }
POST| `user/create`|Json -> { | -
||_name_ : string, |
||_email_ : string, |
||_role_ : {id: int, name: string}, |
||_location_ : {id: int, name: string},| 
||_dateActive_ : string}|

### Password
Type | URI | Geeft | Ontvangt
-----|-----|-------|---------
POST| `user/password`| Json -> { |-
|| _currentPassword_ : string, |
||_newPassword_ : string, |
||_userId_ : int} | 

### Dossier
Type | URI | Geeft | Ontvangt
-----|-----|-------|---------
GET| `user/dossier/` (header userId)| Pathvariable -> userId | Json -> {
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

### LinkedUsers
Type | URI | Geeft | Ontvangt
-----|-----|-------|---------
GET| `user/linking`(header userId)| PathVariable -> userId | Json ->{
||| _user_ : {_id_: int, _name_: string, _role_ : {id: int, name: string}, _location_: {id: int, name: string}},
||| _users_: [ {_id_: int, _name_:string, role_ : {id: int, name: string}, _location_: {id: int, name: string}, _hasRelation_: boolean} ] }
POST| `user/changeRelation`| Json -> {|-
|| _userId_ : int,|
|| _changedUsers_ : [{id: int}] } |

### Search
Type | URI | Geeft | Ontvangt
-----|-----|-------|---------
GET| `user/roles`| - |Json ->  {    
|||_roles_ : [ {id:int, name:string} ],  
|||_locations_ : [ {id:int, name:string} ] }
POST| `user/search`| Json -> { | Json -> {
|| _location_: {id: int, name: string},| _users_: [ {_id_: int, _name_: string, _email_: string, _role_: {id: int, name: string}, _location_: {id: int, name: string}} ]
|| _role_ : {id: int, name: string}|
|| _criteria_ : string }|
