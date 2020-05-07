# SendIT
> SendIT is a courier service that helps users deliver parcels to different destinations. SendIT provides courier quotes based on weight categories.

### Stage
Development

## GitHub Project Management Board
https://github.com/ucheka22/senditapp.git

## API Documentation
https://mysenditapp.herokuapp.com/api/v1/

## UI On gh-pages
Not Ready Yet..

## Table of Content
 * [Getting Started](#getting-started)

 * [Test](#test)
 
 * [API End Points Test Using Postman](#api-end-points-test-using-postman)
 
 * [Features](#features)
 
 * [Built With](#built-with)
 
 * [Author](#author)


## Getting Started

### Installation
1. Clone this repository into your local machine:
```
e.g git clone https://github.com/ucheka22/senditapp.git
```

4. Install postman to test all endpoints on port 8080.

### BaseURI
Use the baseURI  ```https://mysenditapp.herokuapp.com/api/v1```.

### API End Points Test Using Postman

<table>
<tr><th>HTTP VERB</th><th>ENDPOINT</th><th>DESCRIPTION</th></tr>

<tr><td>POST</td> <td>auth/signup</td><td> Create user account</td></tr>

<tr><td>POST</td> <td>/auth/signin</td>  <td>Sign a user</td></tr>

<tr><td>POST</td> <td>/parcels</td><td>Create a parcel delivery order</td></tr>

<tr><td>GET</td><td>/parcels</td><td>Fetch all parcel delivery orders (Admin only)</td></tr>

<tr><td>GET</td> <td>/parcels/{parcelId}</td><td>Fetch a specific parcel delivery order</td></tr>

<tr><td>GET</td> <td>/users/<userId>/parcels</td>  <td>Fetch all parcel delivery orders by a specific user</td></tr>

<tr><td>PUT</td> <td>/parcels/{parcelId}/cancel</td><td>Cancel the specific parcel delivery order/td></tr>

<tr><td>PUT</td> <td>/parcels/{parcelId}/destination</td><td>Change the location of a specific parcel delivery order/td></tr>

<tr><td>PUT</td> <td>/parcels/{parcelId}/status</td><td>Change the status of a specific parcel delivery order/td></tr>

<tr><td>PUT</td> <td>/parcels/{parcelId}/presentLocation</td><td>Change the present location of a specific parcel delivery order/td></tr>

</table>

## Features

 ### Main Features
 * The user can create user accounts and can sign in to the app. 
 * The user can change the destination of a parcel delivery order. 
 * The user can view all parcel delivery orders he/she has created. 
 * Admin can view all parcel delivery orders in the application. 
 * Admin can change the status of a parcel delivery order. 
 * Admin can change the present location of a parcel delivery order
 
 ### Additional Features

 * The application should display a Google Map with Markers showing the pickup location and the destination. 
 * The application should display a Google Map with a line connecting both Markers (pickup location and the		destination). 
 * The application should display a Google Map with computed travel distance and journey duration between the pickup location and the destination. 
 * The user gets real-time email notification when Admin changes the status or the present location of a parcel.

## Built With

* SERVER : Java and Spring Boot

* STYLING : Material UI.

* FRONT-END : React 

* DATABASE : PostgreSQL

* Testing : Unit Testing : TestNG and Rest Assured;

## Author
*  [Ucheka chike](https://twitter.com/ucheka_wilson)

## License
This project is licensed under the MIT license - see the LICENSE.md file for details.

