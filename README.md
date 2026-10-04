# API Documentation

### This api was built to help in:

- Managing doctors and patients
- Managing doctor availabilities
- Booking appointments
- Preventing scheduling conflicts
- Rescheduling appointments
- Modifying appointment characteristics
- Reading doctor, patient, doctor availability and appointment information
- Viewing a doctor's availabilities list
- Filtering appointments

### The services provided by this api fall under 4 categories :

- Doctor Service
- Doctor Availability Service
- Patient Service
- Appointment Service



## Base URLs

- Doctor Service : `/api/doctor`
- Doctor Availability Service : `/api/doctoravailability`
- Patient Service : `/api/patient`
- Appointment Service : `/api/appointment`



## Doctor

### POST 

#### Purpose:
Creates a doctor record

#### Requirements:
Request body (JSON): doctor information

#### Request : 

Body:

{
"firstName" :"Arthur",
"lastName" :"Arthur",
"specialty":"Cardiology",
"phoneNumber":"+1010101",
"email": "arthur@arthur.com",
"isActive": true
}

#### Successful Response :

Status: 201 Created

Body:

{
"createdAt": "2026-10-01T10:53:36.439789Z",
"email": "arthur@arthur.com",
"firstName": "Arthur",
"id": 1,
"isActive": true,
"lastName": "Arthur",
"phoneNumber": "+1010101",
"specialty": "Cardiology"
}




### GET

#### Purpose:
Reads the doctor record

#### Requirements:
Query parameter "id": id of the doctor record to read

#### Request :

Query parameter:

id=1

#### Response :

Status: 200 OK

Body:

{
"createdAt": "2026-10-01T10:53:36.439789Z",
"email": "arthur@arthur.com",
"firstName": "Arthur",
"id": 1,
"isActive": true,
"lastName": "Arthur",
"phoneNumber": "+1010101",
"specialty": "Cardiology"
}





### PUT `/{id}`

#### Purpose:
Completely updates the doctor record (except for the 'id' and 'createdAt' fields)

#### Requirements:
- Path parameter "id": id of the doctor record to update
- Request body(JSON): should contain all the keys and the new values


#### Request :

Path parameter:

`/1`

Body:

{
"firstName" :"Martin",
"lastName" :"Martin",
"specialty":"Cardiology",
"phoneNumber":"+17931793",
"email": "martin@martin.com",
"isActive": false
}

#### Response :

Status: 200 OK

Body:

{
"createdAt": "2026-10-01T10:53:36.439789Z",
"email": "martin@martin.com",
"firstName": "Martin",
"id": 1,
"isActive": false,
"lastName": "Martin",
"phoneNumber": "+17931793",
"specialty": "Cardiology"
}

### PATCH `/{id}`

#### Purpose:
Updates targeted fields of the doctor record (except for the 'id' and 'createdAt' fields)

#### Requirements:
- Path parameter "id": id of the doctor record to update
- Request body(JSON): should contain the targeted fields and their corresponding new values


#### Request :

Path parameter:

`/1`

Body:

{
"isActive": true
}

#### Response :

Status: 200 OK

Body:

{
"createdAt": "2026-10-01T10:53:36.439789Z",
"email": "martin@martin.com",
"firstName": "Martin",
"id": 1,
"isActive": true,
"lastName": "Martin",
"phoneNumber": "+17931793",
"specialty": "Cardiology"
}





## Notes
