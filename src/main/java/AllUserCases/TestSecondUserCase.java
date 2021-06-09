package AllUserCases;

import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.annotations.Test;

import static io.restassured.RestAssured.given;

import java.io.File;
import java.util.Arrays;
import java.util.List;

public class TestSecondUserCase {


    // set the all needed information for the user
    String validFirstName = "GuruQa";
    String validLastName = "Master";
    String validEmail = "emai"+methodsForBody.randomStringGenerator(12)+"@maitt.com";
    String validPassword = "Qa*123456";
    String validPasswordConfirmation = "Qa*123456";


    // gettting the needed response information from successful login test and store them here as global variables to use them in the other tests
    String tokenResponseWhenValidLogin;
    int idUserResponseWhenValidLogin;
    String firstNameResponseWhenValidLogin;
    String emailResponseWhenValidLogin;


    //creating object and pass the needed information to the constructor

    String Title = "Automation QA MASTER";
    String Department = "QA Guru's";
    String Company = "MedMed";
    String Location = "BG SF ";
    String Biography = "Person";
    String Skills = "Fast Learner Faster ";

    List<Integer> registerInterests = Arrays.asList(17,18,19);


    //setting api and url
    String baseURI_BBTraining = "";

    String apiRegisterCreateUser = "/api/register";
    String apiLoginWithCreatedUser = "/api/auth/login";
    String apiSetupProfileInformationOnLoggedUser = "/api/register/setup";
    String apiRegisterInterestsInformationOnLoggedUser = "/api/register/interests";
    String apiGetUserInformation = "/api/users/me";


    //creating and setting the objects

    RegisterUser objPermanentRegisterUserCredentials = new RegisterUser(validFirstName,validLastName, validEmail, validPassword,validPasswordConfirmation);

    RegisterSetupInformation objRegisterSetupInfo = new RegisterSetupInformation(Title, Department, Company, Location, Biography, Skills);

    RegisterSetupInformation objRegisterInterests = new RegisterSetupInformation(registerInterests);




    //Test methods starts


    @Test(priority = 1)
    public void registerUserProfileTest(){
        RestAssured.baseURI = baseURI_BBTraining;

        Response validRegisterUserResponse =
                given()
                        .header("Content-Type","application/json")
                        .header("Accept","application/json" )
                        .body(objPermanentRegisterUserCredentials).log().all()
                .when()
                        .post(apiRegisterCreateUser)
                .then()
                        .log().all()
                        .assertThat().statusCode(201)
                        .extract().response();


    }

    @Test(priority = 2)
    public void loginWithRecentlyRegisteredUser() {

        RestAssured.baseURI = baseURI_BBTraining;

        Response validRegisterUserResponse =
                given()
                        .header("Content-Type", "application/json")
                        .header("Accept", "application/json")
                        .body(methodsForBody.bodyLoginUserWithAlreadyCreatedCredentials(validEmail, validPassword)).log().all()
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

    @Test(priority = 3)
    public void setupInformationForRegisteredUser() {

        RestAssured.baseURI = baseURI_BBTraining;


        Response validRegisterUserResponse =
                given()
                        //.relaxedHTTPSValidation()
                 /*       .config(
                                RestAssured.config()
                                        .encoderConfig(
                                                encoderConfig()
                                                        .encodeContentTypeAs("multipart/form-data", ContentType.TEXT)))*/
                        //.header("Content-Type","multipart/form-data")
                        //.header("Content-Type", "image/png")
                        //.contentType("multipart/form-data")
                        .header("Content-type","application/json")
                        .header("Accept", "application/json")
                        .header("Authorization", "Bearer  " + this.tokenResponseWhenValidLogin + " ").log().all()
                        //.multiPart("file", new File("C:\\Java_RestAssured_Task\\src\\main\\java\\FirstUserCase\\ProfileImgGuruQa.jpg") )
                        //.multiPart("profile_picture", new File("C:\\Java_RestAssured_Task\\src\\main\\java\\FirstUserCase\\scrum - Copy.png") )
                        //.accept(ContentType.JSON)
                        .body(objRegisterSetupInfo).log().all()
                .when()
                        .post(apiSetupProfileInformationOnLoggedUser)
                .then()
                        .log().all()
                        .assertThat().statusCode(200)
                        .extract().response();


        //creating second post for image upload, because we cannot have two different content types together multipart/form-data and application/json


        Response validRegisterUserResponseProfilePicture =
                given()
                        .header("Content-Type","multipart/form-data")
                        .header("Authorization", "Bearer  " + this.tokenResponseWhenValidLogin + " ").log().all()
                        .multiPart("profile_picture", new File("C:\\Java_RestAssured_Task\\src\\main\\java\\AllUserCases\\ProfileImgGuruQa.jpg") )
                        //.multiPart("profile_picture", new File("C:\\Java_RestAssured_Task\\src\\main\\java\\FirstUserCase\\scrum - Copy.png") )
                        .log().all()
                .when()
                        .post(apiSetupProfileInformationOnLoggedUser)
                .then()
                        .log().all()
                        .assertThat().statusCode(200)
                        .extract().response();

    }





    @Test(priority = 4)
    public void registerInterestRecentlyRegisteredUser() {

        RestAssured.baseURI = baseURI_BBTraining;

        Response getValidLoggedUserResponseInformation =
                given()
                        .header("Content-Type","application/json")
                        .header("Accept","application/json" )
                        .header("Authorization", "Bearer  " + this.tokenResponseWhenValidLogin + " ").log().all()
                        .body(objRegisterInterests).log().all()
                .when()
                        .post(apiRegisterInterestsInformationOnLoggedUser)
                .then()
                        .log().all()
                        .assertThat().statusCode(200)
                        .extract().response();


        String responseAsString = getValidLoggedUserResponseInformation.asString();
        JsonPath objJsonConvert = new JsonPath(responseAsString);
        String actualResponseMessage = objJsonConvert.getString("message");
        String expectedResponseMessage = "Registration completed.";

        Assert.assertEquals(actualResponseMessage,expectedResponseMessage);


    }


    @Test(priority = 5)
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
        JsonPath objJsonConvert = new JsonPath(responseAsString);

        String actualResponseDepartmentMessage = objJsonConvert.getString("data.profile.department");
        Assert.assertEquals(actualResponseDepartmentMessage, Department );

        String actualResponseCirclesMessage = objJsonConvert.getString("data.counters.circles");
        Assert.assertEquals(actualResponseCirclesMessage, "3");

    }


}
