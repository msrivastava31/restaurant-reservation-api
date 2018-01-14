# restaurant-reservation-api

This system is a 3-tier lightweight DynamoDB backed HTTP RESTful API that allows the users to create, update, get or 
cancel reservations made for a restaurant. The data points required to interface with this API to just 
name on the reservation, date, time and number of guests in order to make it very generic. This will 
make it easy for prospective clients to use the system.

## Architecture
The application has 3 tiers
### Tier 1
This is the client tier. Since the system is essentially a RESTful API, there’s no frontend/UI
component to it. Hence, we must rely on 3rd party applications such as POSTMAN or raw CLI
commands such as curl to simulate this tier. It uses HTTP/TCP to communicate to Tier 2.
### Tier 2
This is the middle tier of the system. It contains both the servlet as well as the Enterprise Java
Beans (EJB). The servlet is a HTTP servlet and honors GET, POST, PUT & DELETE requests to
/reservation endpoint. The EJB is local to the servlet and is deployed within the same application 
server. The EJB is the liaison between the servlet and the persistence layer (tier 3) and uses 
HTTP/TCP to communicate with it.
### Tier 3
This is the persistence layer of the API. The APU uses Amazon’s Dynamo DB, which is a highly scalable, 
fault-tolerant, fast NoSQL database offered by Amazon Web Services.

## Request-Response Characteristics
A client of this API can Create, Update, Retrieve or Cancel a Reservation

### Create Reservation
#### Request
POST /reservation/
```
{
  "name": "Name",
  "date": "2017-12-12",
  "numGuests": 2,
  "time": "18:00"
}
```
#### Response
HTTP 200 OK
```
{
  "id": "a12-b54a",
  "name": "Name",
  "date": "2017-12-12",
  "numGuests": 2,
  "time": "18:00"
}
```

### Update Reservation
#### Request
PUT /reservation/<reservation_id>
```
{
  "name": "Name",
  "date": "2017-12-10",
  "numGuests": 4,
  "time": "18:00"
}
```
#### Response
HTTP 200 OK
```
{
  "id": "a12-b54a",
  "name": "Name",
  "date": "2017-12-10",
  "numGuests": 4,
  "time": "18:00"
}
```

### Get Reservation Details
#### Request
GET /reservation/<reservation_id>
#### Response
HTTP 200 OK
```
{
  "id": "a12-b54a",
  "name": "Name",
  "date": "2017-12-10",
  "numGuests": 4,
  "time": "18:00"
}
```

### Cancel Reservation
#### Request
DELETE /reservation/<reservation_id>
#### Response
HTTP 200 OK

## Error Characteristics
The API will return HTTP 400 (Bad Request) with an Error JSON response if some basic validations
fail. For example, if a reservation id doesn’t exist in a Get/Update/Cancel Reservation request, the user
will receive an error as:
```
{
  "id": 400,
  "message": "Reservation with id a12-b54 does not exist"
}
```
