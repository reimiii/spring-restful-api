# Contact API Spec

## Create Contact

Endpoint : POST /api/contacts

Request Header :

- X-API-TOKEN : Token (Mandatory)
 
Request Body :

```json
{
  "fistName": "hilmi",
  "lastName": "am",
  "email": "me@mail.com",
  "phone": "09090"
}
```

Response Body (Success) :

```json
{
  "data": {
    "id": "random string",
    "fistName": "hilmi",
    "lastName": "am",
    "email": "me@mail.com",
    "phone": "09090"
  }
}
```

Response Body (Failed) :

```json
{
  "errors": "email invalid"
}
```

## Update Contact

Endpoint : PUT /api/contacts/{idContact}

Request Header :

- X-API-TOKEN : Token (Mandatory)

Request Body :

```json
{
  "fistName": "hilmi",
  "lastName": "am",
  "email": "me@mail.com",
  "phone": "09090"
}
```
Response Body (Success) :

```json
{
  "data": {
    "id": "random string",
    "fistName": "hilmi",
    "lastName": "am",
    "email": "me@mail.com",
    "phone": "09090"
  }
}
```
Response Body (Failed) :

```json
{
  "errors": "email invalid"
}
```
## Get Contact
Endpoint : GET /api/contacts/{idContact}

Request Header :

- X-API-TOKEN : Token (Mandatory)

Response Body (Success) :

```json
{
  "data": {
    "id": "random string",
    "fistName": "hilmi",
    "lastName": "am",
    "email": "me@mail.com",
    "phone": "09090"
  }
}
```
Response Body (Failed, 404) :

```json
{
  "errors": "contact not found"
}
```
## Search Contact
Endpoint : GET /api/contacts

Query Param (using like query 'optional'): 

- name: string, contact full name
- phone: string
- email: string
- page: Integer, start from 0
- size: Integer, default 10

Request Header :

- X-API-TOKEN : Token (Mandatory)

Request Body :

Response Body (Success) :

```json
{
  "data": [
    {
      "id": "random string",
      "fistName": "hilmi",
      "lastName": "am",
      "email": "me@mail.com",
      "phone": "09090"
    }
  ],
  "paging": {
    "currentPage": 0,
    "totalPage": 10,
    "size": 10
  }
}
```
Response Body (Failed) :
```json
{
  "errors": "unauthorized"
}
```

## Remove Contact
Endpoint : DELETE /api/contacts/{idContact}

Request Header :

- X-API-TOKEN : Token (Mandatory)

Response Body (Success) :
```json
{
  "data": "OK"
}
```

Response Body (Failed) :
```json
{
  "errors": "not found"
}
```
