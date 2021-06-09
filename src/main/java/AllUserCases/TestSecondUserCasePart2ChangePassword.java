package AllUserCases;

import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.annotations.Test;
import static io.restassured.RestAssured.given;

public class TestSecondUserCasePart2ChangePassword {



    // setting the base URL and methods Api's
    String baseURI_BBTraining = "";

    String apiRegisterCreateUser = "/api/register";
    String apiLoginWithCreatedUser = "/api/auth/login";
    String apiChangingPassword = "/api/passwords/change";
    String apiLogOutCreatedUser = "/api/auth/logout";

    // set the all needed information for the user
    String validFirstName = "PeterQA";
    String validLastName = "KaradjovQA";
    String validEmail = ""+methodsForBody.randomStringGenerator(8)+"@emaig.com";
    String validPassword = "Qa*123456";
    String validPasswordConfirmation = "Qa*123456";

    String validNewChangingPassword = "Qa*123456ch";


    //gettin user Id when profile is created
    int idUserResponseWhenValidProfileIsCreated ;

    // gettting the needed response information from successful login test and store them here as global variables to use them in the other tests
    String tokenResponseWhenValidLogin ;
    String firstNameResponseWhenValidLogin ;
    String emailResponseWhenValidLogin ;


    //creating objects and set them

    RegisterUser objValidUserRegister = new RegisterUser(validFirstName,validLastName, validEmail, validPassword,validPasswordConfirmation);

    ChangePasswordSetup objChangePassword = new ChangePasswordSetup(validPassword,validNewChangingPassword,validNewChangingPassword);



     @Test(priority = 1)
    public void validRegisterUserTest(){
        RestAssured.baseURI = baseURI_BBTraining;

        Response validRegisterUserResponse =
                given()
                        .header("Content-Type","application/json")
                        .header("Accept","application/json" )
                        .body(objValidUserRegister).log().all()
                        .when()
                        .post(apiRegisterCreateUser)
                        .then()
                        .log().all()
                        .assertThat().statusCode(201)
                        .extract().response();

        String responseAsString = validRegisterUserResponse.asString();

         JsonPath jsnObjectConvert = new JsonPath(responseAsString);
         idUserResponseWhenValidProfileIsCreated = jsnObjectConvert.getInt("data.user.id");

    }



    @Test(priority = 2)
    public void loginWithNewlyRegisteredUser(){

        RestAssured.baseURI = baseURI_BBTraining;

        Response validLogFromRegisterUserResponse =
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


        String responseAsString = validLogFromRegisterUserResponse.asString();

        JsonPath jsnObjectConvert = new JsonPath(responseAsString);
        tokenResponseWhenValidLogin = jsnObjectConvert.getString("data.access_token");
        emailResponseWhenValidLogin = jsnObjectConvert.getString("data.user.email");
        firstNameResponseWhenValidLogin = jsnObjectConvert.getString("data.user.first_name");


    }



    @Test(priority = 3)
    public void validChangingPassword() {
        RestAssured.baseURI = baseURI_BBTraining;

        Response validChangedPasswordResponse =
                given()
                        .header("Content-Type", "application/json")
                        .header("Accept", "application/json")
                        .header("Authorization", "Bearer  " + this.tokenResponseWhenValidLogin + " ").log().all()
                        .body(objChangePassword).log().all()
                        .when()
                        .put(apiChangingPassword)
                        .then()
                        .log().all()
                        .assertThat().statusCode(200)
                        .extract().response();


    }



        @Test(priority = 4)
    public void logOutSuccessfully(){

        RestAssured.baseURI = baseURI_BBTraining;

        Response validLogOutResponse =
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

        String responseAsString = validLogOutResponse.asString();

        JsonPath jsnObjectConvert = new JsonPath(responseAsString);
        String logOutSuccessMessage = jsnObjectConvert.getString("message");
        Assert.assertEquals(logOutSuccessMessage, "Logged out successfully");

    }





    @Test(priority = 5)
    public void loginWithFirstRegisteredPassword() {

        RestAssured.baseURI = baseURI_BBTraining;

        Response validUnauthorizedLoginResponse =
                given()
                        .header("Content-Type", "application/json")
                        .header("Accept", "application/json")
                        .body(methodsForBody.bodyLoginUserWithAlreadyCreatedCredentials(emailResponseWhenValidLogin, objValidUserRegister.getPassword())).log().all()
                        .when()
                        .post(apiLoginWithCreatedUser)
                        .then()
                        .log().all()
                        .assertThat().statusCode(401)
                        .extract().response();

        String responseAsString = validUnauthorizedLoginResponse.asString();
        JsonPath jsnObj = new JsonPath(responseAsString);
        String loginIncorrectResponseMessage = jsnObj.getString("message");
        String expectedResponseMessage = "Incorrect email or password";
        Assert.assertEquals(loginIncorrectResponseMessage,expectedResponseMessage);


    }

    @Test(priority = 6)
    public void loginWithNewChangedPasswordUser() {

        RestAssured.baseURI = baseURI_BBTraining;

        Response validLogWithNewPasswordResponse =
                given()
                        .header("Content-Type", "application/json")
                        .header("Accept", "application/json")
                        .body(methodsForBody.bodyLoginUserWithAlreadyCreatedCredentials(emailResponseWhenValidLogin, validNewChangingPassword)).log().all()
                        .when()
                        .post(apiLoginWithCreatedUser)
                        .then()
                        .log().all()
                        .assertThat().statusCode(200)
                        .extract().response();


        String responseAsString = validLogWithNewPasswordResponse.asString();

        JsonPath jsnObjectConvert = new JsonPath(responseAsString);
        int loggedUserIDResponse = jsnObjectConvert.get("data.user.id");
        Assert.assertEquals(loggedUserIDResponse,idUserResponseWhenValidProfileIsCreated);


    }





}