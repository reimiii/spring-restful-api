# Address API Spec

## Create Address

EndPoint : POST /api/contacts/{idContact}/addresses

Request Header :

- X-API-TOKEN : Token (Mandatory)

Request Body : 

```json
{
  "street": "jalan",
  "city": "kota",
  "province": "provinsi",
  "country": "negara",
  "postalCode": "1234"
}
```

Response Body (Success) :
```json
{
  "data": {
    "id": "randomstring",
    "street": "jalan",
    "city": "kota",
    "province": "provinsi",
    "country": "negara",
    "postalCode": "1234"
  }
}
```

Response Body (Failed) :

```json
{
  "errors": "contact not found"
}
```

## Update Address

EndPoint : PUT /api/contacts/{idContact}/addresses/{idAddress}

Request Header :

- X-API-TOKEN : Token (Mandatory)

Request Body :

```json
{
  "street": "jalan",
  "city": "kota",
  "province": "provinsi",
  "country": "negara",
  "postalCode": "1234"
}
```

Response Body (Success) :
```json
{
  "data": {
    "id": "randomstring",
    "street": "jalan",
    "city": "kota",
    "province": "provinsi",
    "country": "negara",
    "postalCode": "1234"
  }
}
```

Response Body (Failed, 404) :

```json
{
  "errors": "Address not found"
}
```


## Create Address

EndPoint : GET /api/contacts/{idContact}/addresses/{idAddress}

Request Header :

- X-API-TOKEN : Token (Mandatory)

Response Body (Success) :
```json
{
  "data": {
    "id": "randomstring",
    "street": "jalan",
    "city": "kota",
    "province": "provinsi",
    "country": "negara",
    "postalCode": "1234"
  }
}
```

Response Body (Failed, 404) :

```json
{
  "errors": "Address not found"
}
```


## Remove Address
EndPoint : DELETE /api/contacts/{idContact}/addresses/{idAddress}

Request Header :

- X-API-TOKEN : Token (Mandatory)

Response Body (Success) :
```json
{
  "data": "OK"
}
```

Response Body (Failed, 404) :

```json
{
  "errors": "Address not found"
}
```



## List Address

EndPoint : PUT /api/contacts/{idContact}/addresses/

Request Header :

- X-API-TOKEN : Token (Mandatory)

Response Body (Success) :
```json
{
  "data": [
    {
      "id": "randomstring",
      "street": "jalan",
      "city": "kota",
      "province": "provinsi",
      "country": "negara",
      "postalCode": "1234"
    }
  ]
}
```

Response Body (Failed, 404) :

```json
{
  "errors": "Contact Address not found"
}
```
