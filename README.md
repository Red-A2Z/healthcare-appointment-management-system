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
Reads a doctor record

#### Requirements:
Query parameter "id": id of the doctor record to read

#### Request :

Query parameter:

id=1

#### Successful Response :

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
Completely updates a doctor record (except for the 'id' and 'createdAt' fields)

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

#### Successful Response :

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
Updates targeted fields of a doctor record (except for the 'id' and 'createdAt' fields)

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

#### Successful Response :

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



## Doctor Availability

### POST

#### Purpose:
Creates a doctor availability record:
if the provided timeslot is contiguous with an existing one, it merges them.

#### Requirements:
Request body (JSON): doctor availability information

#### Request :

Body:

{
"doctorId" :1,
"startTime" :"2030-09-01T08:00:00",
"endTime":"2030-09-01T10:00:00"
}

#### Successful Response :

Status: 201 Created

Body:

{
"id": 1,
"doctorId": 1,
"startTime": "2030-09-01T08:00:00",
"endTime": "2030-09-01T10:00:00",
"createdAt": "2026-10-01T11:27:00.752531Z"
}


### GET

#### Purpose:
Reads a doctor availability record

#### Requirements:
Query parameter "id": id of the doctor availability record to read

#### Request :

Query parameter:

id=1

#### Successful Response :

Status: 200 OK

Body:

{
"id": 1,
"doctorId": 1,
"startTime": "2030-09-01T08:00:00",
"endTime": "2030-09-01T10:00:00",
"createdAt": "2026-10-01T11:27:00.752531Z"
}


### GET `/list`

#### Purpose:
Reads all availability records of a specified doctor

#### Requirements:
Query parameter "doctorId": id of the doctor

#### Request :

Query parameter:

doctorId=1

#### Successful Response :

Status: 200 OK

Body:

{  
"doctorActive": true,  
"doctorAvailabilityList": [    

{
"id": 1,
"doctor": {
"id": 1,
"firstName": "Martin",
"lastName": "Martin",
"specialty": "Cardiology",
"phoneNumber": "+17931793",
"email": "martin@martin.com",
"isActive": true,
"createdAt": "2026-10-01T10:53:36.439789Z"
},
"startTime": "2030-09-01T08:00:00",
"endTime": "2030-09-01T10:00:00",
"createdAt": "2026-10-01T11:27:00.752531Z"
},   
  
{
"id": 2,
"doctor": {
"id": 1,
"firstName": "Martin",
"lastName": "Martin",
"specialty": "Cardiology",
"phoneNumber": "+17931793",
"email": "martin@martin.com",
"isActive": true,
"createdAt": "2026-10-01T10:53:36.439789Z"
},
"startTime": "2030-09-01T12:00:00",
"endTime": "2030-09-01T14:00:00",
"createdAt": "2026-10-01T11:31:05.855624Z"
},  

{
"id": 3,
"doctor": {
"id": 1,
"firstName": "Martin",
"lastName": "Martin",
"specialty": "Cardiology",
"phoneNumber": "+17931793",
"email": "martin@martin.com",
"isActive": true,
"createdAt": "2026-10-01T10:53:36.439789Z"
},
"startTime": "2030-09-01T16:00:00",
"endTime": "2030-09-01T18:00:00",
"createdAt": "2026-10-01T11:31:50.409798Z"
}  

],  

"doctorId": 1
}



### PUT `/{id}`

#### Purpose:
Completely updates a doctor availability record (except for the 'id' and 'createdAt' fields)

#### Requirements:
- Path parameter "id": id of the doctor availability record to update
- Request body(JSON): should contain all the keys and the new values


#### Request :

Path parameter:

`/3`

Body:

{
"doctorId" :2,
"startTime" :"2030-09-01T18:00:00",
"endTime":"2030-09-01T19:00:00"
}

#### Successful Response :

Status: 200 OK

Body:

{
"id": 3,
"doctorId": 2,
"startTime": "2030-09-01T18:00:00",
"endTime": "2030-09-01T19:00:00",
"createdAt": "2026-10-01T11:31:50.409798Z"
}



### PATCH `/{id}`

#### Purpose:
Updates targeted fields of a doctor availability record (except for the 'id' and 'createdAt' fields)

#### Requirements:
- Path parameter "id": id of the doctor availability record to update
- Request body(JSON): should contain the targeted fields and their corresponding new values


#### Request :

Path parameter:

`/3`

Body:

{
"doctorId" :1
}

#### Successful Response :

Status: 200 OK

Body:

{
"id": 3,
"doctorId": 1,
"startTime": "2030-09-01T18:00:00",
"endTime": "2030-09-01T19:00:00",
"createdAt": "2026-10-01T11:31:50.409798Z"
}



### DELETE `/{id}`

#### Purpose:
Deletes a doctor availability record

#### Requirements:
- Path parameter "id": id of the doctor availability record to delete

#### Request :

Path parameter:

`/3`

#### Successful Response :

Status: 204 No Content  
  


## Patient

### POST

#### Purpose:
Creates a patient record

#### Requirements:
Request body (JSON): patient information

#### Request :

Body:

{
"firstName" :"Lucas",
"lastName" :"Lucas",
"dateOfBirth":"2000-01-01",
"phoneNumber":"+1231231",
"email": "lucas@lucas.com"
}

#### Successful Response :

Status: 201 Created

Body:

{
"createdAt": "2026-10-01T12:04:18.323527Z",
"dateOfBirth": "2000-01-01",
"email": "lucas@lucas.com",
"firstName": "Lucas",
"id": 1,
"lastName": "Lucas",
"phoneNumber": "+1231231"
}


### GET

#### Purpose:
Reads a patient record

#### Requirements:
Query parameter "id": id of the patient record to read

#### Request :

Query parameter:

id=1

#### Successful Response :

Status: 200 OK

Body:

{
"createdAt": "2026-10-01T12:04:18.323527Z",
"dateOfBirth": "2000-01-01",
"email": "lucas@lucas.com",
"firstName": "Lucas",
"id": 1,
"lastName": "Lucas",
"phoneNumber": "+1231231"
}


### PUT `/{id}`

#### Purpose:
Completely updates a patient record (except for the 'id' and 'createdAt' fields)

#### Requirements:
- Path parameter "id": id of the patient record to update
- Request body(JSON): should contain all the keys and the new values


#### Request :

Path parameter:

`/1`

Body:

{
"firstName" :"Albert",
"lastName" :"Albert",
"dateOfBirth":"2000-02-02",
"phoneNumber":"+3213213",
"email": "albert@albert.com"
}


#### Successful Response :

Status: 200 OK

Body:

{
"createdAt": "2026-10-01T12:04:18.323527Z",
"dateOfBirth": "2000-02-02",
"email": "albert@albert.com",
"firstName": "Albert",
"id": 1,
"lastName": "Albert",
"phoneNumber": "+3213213"
}


### PATCH `/{id}`

#### Purpose:
Updates targeted fields of a patient record (except for the 'id' and 'createdAt' fields)

#### Requirements:
- Path parameter "id": id of the patient record to update
- Request body(JSON): should contain the targeted fields and their corresponding new values


#### Request :

Path parameter:

`/1`

Body:

{
"dateOfBirth":"2000-02-01"
}

#### Successful Response :

Status: 200 OK

Body:

{
"createdAt": "2026-10-01T12:04:18.323527Z",
"dateOfBirth": "2000-02-01",
"email": "albert@albert.com",
"firstName": "Albert",
"id": 1,
"lastName": "Albert",
"phoneNumber": "+3213213"
}







## Notes
