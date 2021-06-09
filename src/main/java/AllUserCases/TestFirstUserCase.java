package AllUserCases;

import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import org.hamcrest.Matchers;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.lessThan;

public class TestFirstUserCase {

    // set the all needed information for the user
    String validFirstName = "Peter";
    String validLastName = "Karadjov";
    String invalidEmail = "saturnmail.com";
    String validEmail = ""+methodsForBody.randomStringGenerator(12)+"@maggd.com";
    String validPassword = "Qa*123456";
    String validPasswordConfirmation = "Qa*123456";

    // gettting the needed response information from successful login test and store them here as global variables to use them in the other tests
    String tokenResponseWhenValidLogin ;
    int idUserResponseWhenValidLogin ;
    String firstNameResponseWhenValidLogin ;
    String emailResponseWhenValidLogin ;

    // setting the base URL and methods Api's
    String baseURI_BBTraining = "";

    String apiRegisterCreateUser = "/api/register";
    String apiLoginWithCreatedUser = "/api/auth/login";
    String apiGetUserInformation = "/api/users/me";
    String apiLogOutCreatedUser = "/api/auth/logout";

    //creating valid and invalid user information
    RegisterUser objInvalidEmail = new RegisterUser(validFirstName,validLastName, invalidEmail, validPassword,validPasswordConfirmation);

    RegisterUser objValidUserRegister = new RegisterUser(validFirstName,validLastName, validEmail, validPassword,validPasswordConfirmation);




    @Test(priority = 1)
    public void invalidEmailTest(){
         RestAssured.baseURI = baseURI_BBTraining;

         Response invalidEmailResponse =
                  given()
                         .header("Content-Type","application/json")
                         .header("Accept","application/json" )
                         .body(objInvalidEmail).log().all()
                 .when()
                         .post(apiRegisterCreateUser)
                 .then()
                         .log().all()
                         .assertThat().statusCode(422)
                         .extract().response();

         long rspTimeSeconds = invalidEmailResponse.getTimeIn(TimeUnit.SECONDS);
         long rspTimeMiliseconds = invalidEmailResponse.timeIn(TimeUnit.MILLISECONDS);
         long rspTime = invalidEmailResponse.time();

         String responseAsString = invalidEmailResponse.asString();

         JsonPath jsnObjectConvert = new JsonPath(responseAsString);
         String responseInvalidEmailMessage = jsnObjectConvert.getString("errors.email[0]");
         String expectedMessage = "The email must be a valid email address.";

         Assert.assertEquals(responseInvalidEmailMessage, expectedMessage);

        System.out.println("Response time in Miliseconds: "+ rspTimeMiliseconds);
        System.out.println("Response time in Seconds: "+ rspTimeSeconds);
        System.out.println("Response time in default: "+ rspTime);
     }


    @Test(priority = 2)
    public void validRegisterUserTest(){
        RestAssured.baseURI = baseURI_BBTraining;

        Response validRegisterUserResponse =
                given()
                        .header("Content-Type","application/json")
                        .body(objValidUserRegister).log().all()
                .when()
                        .post(apiRegisterCreateUser)
                .then()
                        .log().all()
                        .assertThat().statusCode(201)
                        .extract().response();


    }

    @Test(priority = 3)
    public void loginWithNewlyRegisteredUser(){

        RestAssured.baseURI = baseURI_BBTraining;



        Response validRegisterUserResponse =
                given()
                        .header("Content-Type","application/json")
                        .header("Accept","application/json" )
                        .body(methodsForBody.bodyLoginUserWithAlreadyCreatedCredentials(objValidUserRegister.getEmail(),objValidUserRegister.getPassword())).log().all()
                .when()
                        .post(apiLoginWithCreatedUser)
                .then()
                        .log().all()
                        .assertThat().statusCode(200)
                        .extract().response();


        String responseAsString = validRegisterUserResponse.asString();

        JsonPath jsnObjectConvert = new JsonPath(responseAsString);
        tokenResponseWhenValidLogin = jsnObjectConvert.getString("data.access_token");
        idUserResponseWhenValidLogin = jsnObjectConvert.getInt("data.user.id");
        emailResponseWhenValidLogin = jsnObjectConvert.getString("data.user.email");
        firstNameResponseWhenValidLogin = jsnObjectConvert.getString("data.user.first_name");


    }


    @Test(priority = 4)
    public void checkingRegisterInformationIsTheSameAsProvided() {

        RestAssured.baseURI = baseURI_BBTraining;

        Response getValidLoggedUserResponseInformation =
                given()
                        .header("Content-Type","application/json")
                        .header("Accept","application/json" )
                        .header("Authorization", "Bearer  " + this.tokenResponseWhenValidLogin + " ").log().all()
                .when()
                        .get(apiGetUserInformation)
                .then()
                        .log().all()
                        .assertThat().statusCode(200)
                        .extract().response();


        String responseAsString = getValidLoggedUserResponseInformation.asString();

        JsonPath jsnObjectConvert = new JsonPath(responseAsString);

        String actualFirstName = jsnObjectConvert.getString("data.first_name");
        Assert.assertEquals(actualFirstName,objValidUserRegister.getFirst_name());

        String actualEmailAddress = jsnObjectConvert.getString("data.email");
        Assert.assertEquals(actualEmailAddress,objValidUserRegister.getEmail().toLowerCase()); //?



    }

    @Test(priority = 5)
    public void logOutSuccessfully(){

        RestAssured.baseURI = baseURI_BBTraining;

        Response getLogOutValidUserResponse =
                given()
                        .header("Content-Type","application/json")
                        .header("Accept","application/json" )
                        .header("Authorization", "Bearer  " + this.tokenResponseWhenValidLogin + " ").log().all()
                .when()
                        .post(apiLogOutCreatedUser)
                .then()
                        .log().all()
                        .assertThat().statusCode(200)
                        .extract().response();

        String responseAsString = getLogOutValidUserResponse.asString();

        JsonPath jsnObjectConvert = new JsonPath(responseAsString);
        String logOutSuccessMessage = jsnObjectConvert.getString("message");
        Assert.assertEquals(logOutSuccessMessage, "Logged out successfully");

        long rspTime = getLogOutValidUserResponse.time();
        System.out.println("Log out response time in miliseconds: " + rspTime);


    }

}
