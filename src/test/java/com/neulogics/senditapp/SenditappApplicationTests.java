package com.neulogics.senditapp;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;


import java.util.stream.Stream;

import org.json.JSONException;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.neulogics.senditapp.exception.ParcelNotFoundException;
import com.neulogics.senditapp.models.Parcel;
import com.neulogics.senditapp.models.User;
import com.neulogics.senditapp.respository.UserRepository;

import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class SenditappApplicationTests {

	// *******Before your run test first create two users in the database,one admin user and a normal user
	
	String adminToken;
	
	String normalUserToken;
	
	Parcel parcelOne,parcelTwo,parcelThree;
	
	@Autowired
	private UserRepository userRepo;	

	User dbUser,dbNormalUser;
	
	RequestSpecification requestSpec;


	@SuppressWarnings("unchecked")
	@BeforeAll
	public void authorization() throws JSONException {
		
		
		RestAssured.baseURI = "https://mysenditapp.herokuapp.com/api/v1";


		String[] adminList= {"admin","user"};
		JSONArray adminRoles = new JSONArray();
		Stream.of(adminList)
		.forEach(adminRoles::add);
		
		String[] userList = {"user"};
		JSONArray userRoles = new JSONArray();
		Stream.of(userList)
		.forEach(userRoles::add);
		
		JSONObject adminUserJSON = new JSONObject();
		adminUserJSON.put("username", "John Doe");
		adminUserJSON.put("password", "john22doe");
		adminUserJSON.put("email", "johndoe@gmail.com");
		adminUserJSON.put("roles",adminRoles);
		System.out.println(adminUserJSON);
		System.out.println(adminRoles);

		JSONObject normalUserJSON = new JSONObject();
		normalUserJSON.put("username", "Wilson Achiver");
		normalUserJSON.put("password", "wilson22achiver");
		normalUserJSON.put("email", "wilsonachiver@gmail.com");
		normalUserJSON.put("roles",userRoles);
		
		
		// Create the parcel for test
		parcelOne = new Parcel();
		parcelOne.setPickupLocation("New Owerri,Imo State");
		parcelOne.setDestination("Ojo,Lagos State");
		parcelOne.setPrice(25020.690);
		
		parcelTwo = new Parcel();
		parcelTwo.setPickupLocation("Lekki,Lagos State");
		parcelTwo.setDestination("Abeokuta,Ogun State");
		parcelTwo.setPrice(35020.690);
		
		parcelThree = new Parcel();
		parcelThree.setPickupLocation("Calabar,Cross River");
		parcelThree.setDestination("Benin,Edo State");
		parcelThree.setPrice(45020.690);
		
		
		given()
		.contentType(ContentType.JSON)
		.body(adminUserJSON)
		.when()
			.post("/auth/signup")
		.then()
			.contentType(ContentType.JSON)
			.statusCode(200);
		
		given()
			.contentType(ContentType.JSON)
			.body(normalUserJSON)
		.when()
			.post("/auth/signup")
		.then()
			.contentType(ContentType.JSON)
			.statusCode(200);

		dbUser = userRepo.findByUsername(adminUserJSON.get("username").toString());
		dbNormalUser = userRepo.findByUsername(normalUserJSON.get("username").toString());	
		
		//Login A User And Return the Token
		 adminToken = 
				given()
					.contentType(ContentType.JSON)
					.body(adminUserJSON.toJSONString())
				.when()
					.post("/auth/signin")
				.then()
					.contentType(ContentType.JSON)
					.statusCode(200)
				.extract()
					.path("accessToken");
		 
		RequestSpecBuilder builder = new RequestSpecBuilder()
				.setContentType(ContentType.JSON)
				.addHeader("Authorization", "Bearer "+adminToken);

		requestSpec =  builder.build();

		

		
		// Login another user and return token
		 normalUserToken = 
					given()
						.contentType(ContentType.JSON)
						.body(normalUserJSON.toJSONString())
					.when()
						.post("/auth/signin")
					.then()
					.log().all()
						.contentType(ContentType.JSON)
						.statusCode(200)
					.extract()
						.path("accessToken");
		 
		 
		 		
		
		//Create Parcels one and two for further testing
							given()
								.spec(requestSpec)
								.body(parcelOne)
							.when()
								.post("/parcels")   
							.then()
								.statusCode(201);
				
							given()
								.spec(requestSpec)
								.body(parcelTwo)
							.when()
								.post("/parcels")   
							.then()
								.statusCode(201);
	}



	// POST /api/v1/parcels
	@Test()
	@Order(1)
	@DisplayName("User should be able to create parcel order")
	public void postParcelTest() {	
		given()
			.spec(requestSpec)
			.body(parcelThree)
		.when()
			.post("/parcels")   
		.then()
			.statusCode(201)
			.body("pickupLocation",equalTo(parcelThree.getPickupLocation()))
			.body("destination",equalTo(parcelThree.getDestination()))
			.body("parcelId", is(notNullValue()));
		
	}

@Test()
@Order(2)
@DisplayName("User should successfully get parcel by ParcelId")
public void getParcelByIdTest() {	
	int parcelId = 1;
	given()
		.pathParam("parcelId", parcelId)
		.spec(requestSpec)
	.when()
		.get("/parcels/{parcelId}")
	.then()
		.statusCode(200)
		.body("parcelId",equalTo(parcelId));
}

@Test()
@Order(3)
@Disabled("Ignored")
@DisplayName("Admin should successfully retrieve all parcel orders")
public void getAllParcelOrdersTest() {
	given()
		.spec(requestSpec)
	.when()
		.get("/parcels")
	.then()
		.statusCode(200)
		.body("size()", is(notNullValue()));
}


@Test()
@Order(4)
@DisplayName("Specific user should be able to get all parcel order(s) such user created")
public void getParcelOrdersBy_A_SpecificUser() throws ParcelNotFoundException{
	
	given()
		.pathParam("userId", dbUser.getUserId())
		.spec(requestSpec)
	.when()
		.get("/users/{userId}/parcels")
	.then()
		.statusCode(200);
}
@Test()
@Order(5)
@DisplayName("Getting parcel order for user without parcel order shoild return 404")
public void getParcelOrdersBy_ForUserWithOutParcelOrder(){
	given()
		.pathParam("userId",dbNormalUser.getUserId())
		.contentType(ContentType.JSON)
		.header("Authorization","Bearer "+normalUserToken)
	.when()
		.get("/users/{userId}/parcels")
	.then()
		.statusCode(404)
		.body("message",equalTo( "No parcel order(s) for this user"));
}
@Test()
@Order(6)
@DisplayName("User should be able to change status of parcel order not yet delivered")
@Disabled("Ignored")
public void changeParcelStatusTest1() {
	int parcelId = 1;
	Parcel parcelToChangeStatus = new Parcel();
	parcelToChangeStatus.setStatus("delivered");
	
	given()
		.pathParam("parcelId", parcelId)
		.spec(requestSpec)
		.body(parcelToChangeStatus)
	.when()
		.put("/parcels/{parcelId}/status")
	.then()
		.statusCode(200)
		.body("parcelId", equalTo(parcelId))
		.body("status", equalTo(parcelToChangeStatus.getStatus()))
		.body("presentLocation",equalTo(parcelToChangeStatus.getDestination()));
}

@Test()
@Order(7)
@Disabled("Ignored")
@DisplayName("User should be able to change the destination of a parcel not yet delivered")
public void changeParcelOrder_DestinationTest() {
	int parcelId = 2;
	Parcel parcelToChangeDestination = new Parcel();
	parcelToChangeDestination.setDestination("Ketu,Lagos state");
	
	given()
		.pathParam("parcelId", parcelId)
		.spec(requestSpec)
		.body(parcelToChangeDestination)
	.when()
		.put("/parcels/{parcelId}/status")
	.then()
		.statusCode(200)
		.body("destination", equalTo(parcelToChangeDestination.getDestination()));
}

@Test()
@Order(8)
@DisplayName("User should not be able to change destination of parcel already delivered")
@Disabled("Ignore")
public void cannotChangeDestinationOfParcelOrderMarked_DELIVEREDTest() {
	int parcelId = 1;
	Parcel parcelToChangeDestination = new Parcel();
	parcelToChangeDestination.setDestination("Ketu,Lagos state");
	
	given()
		.pathParam("parcelId", parcelId)
		.spec(requestSpec)
		.body(parcelToChangeDestination)
	.when()
		.put("/parcels/{parcelId}/status")
	.then()
		.statusCode(400)
		.body("message", equalTo("Cannot change destination of already delivered parcel"));
}
@Test()
@Order(9)
@DisplayName("User should be able to cancel parcel order not yet delivered")
public void cancelAParcelOrderTest() {
	int parcelId = 2;
	given()
		.pathParam("parcelId", parcelId)
		.spec(requestSpec)
	.when()
		.put("/parcels/{parcelId}/cancel")
	.then()
		.statusCode(200)
		.body("parcelId", equalTo(parcelId))
		.body("status",equalTo("cancelled"));
}

@Test()
@Order(10)
@Disabled("Ignored")
@DisplayName("User should not be able to cancel parcel order already delivered")
public void cannot_CancelAParcelOrderMarked_DELIVERED_Test() {
	int parcelId = 1;
	
	given()
		.pathParam("parcelId", parcelId)
		.spec(requestSpec)
	.when()
		.put("/parcels/{parcelId}/cancel")
	.then()
		.contentType(ContentType.JSON)
		.statusCode(400)
		.body("message",equalTo("Parcel has already been delivered"));
}

@Test()
@Order(11)
@Disabled("Ignored")
@DisplayName("Only user who created parcel order can cancel order")
public void onlyCreatorOfParcelOrder_canCancellOrderTest() {
	int parcelId = 2;
	
	given()
		.pathParam("parcelId", parcelId)
		.header("Authorization","Bearer "+normalUserToken)
	.when()
		.put("/parcels/{parcelId}/cancel")
	.then()
		.statusCode(400)
		.body("message",equalTo("Only user who created parcel order can cancel order"));
}



@Test()
@Order(12)
@Disabled("Ignored")
@DisplayName("User should be able to update parcel present location")
public void changeParcelPresentLocationTest() {
	int parcelId = 1;
	Parcel parcelToChangePresentLocation = new Parcel();
	parcelToChangePresentLocation.setPresentLocation("Asaba,Delta state");
	
	given()
		.pathParam("parcelId", parcelId)
		.spec(requestSpec)
		.body(parcelToChangePresentLocation)
	.when()
		.put("/parcels/{parcelId}/present_location")
	.then()
		.statusCode(200);
}
}