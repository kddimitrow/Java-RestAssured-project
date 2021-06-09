package AllUserCases;

import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.io.File;
import java.lang.reflect.Array;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static io.restassured.RestAssured.given;

public class TestThirdUserCase {


    // set the all needed information for the First User
    String validFirstName = "DimitarQA";
    String validLastName = "DimitrovQA";
    String validEmail = ""+methodsForBody.randomStringGenerator(3)+"@ggmat.com";
    String validPassword = "Qa*123456";
    String validPasswordConfirmation = "Qa*123456";

    // set the all needed information for the Second User
    String validFirstNameSecondUser = "PeterQA";
    String validLastNameSecondUser = "KaradjovQA";
    String validEmailSecondUser = "" + methodsForBody.randomStringGenerator(3) + "@ggmatt.com";
    String validPasswordSecondUser = "Qa*123456";
    String validPasswordConfirmationSecondUser = "Qa*123456";

    //gettin first and second user response information
    int idUserResponseWhenValidProfileIsCreatedFirstUSer;

    String tokenResponseWhenValidLoginFirstUser;
    String firstNameResponseWhenValidLoginFirstUser;
    String emailResponseWhenValidLoginFirstUser;

    int idUserResponseWhenValidProfileIsCreatedSecondUSer;

    String tokenResponseWhenValidLoginSecondUSer;
    String firstNameResponseWhenValidLoginSecondUSer;
    String emailResponseWhenValidLoginSecondUSer;


    //creating object for circle information Second User
    String notJoinedCircles = "not_joined";
    String joinedCircles = "joined";

    //circle not joined response Second User
    int responseSecondCircleNotJoinedId;
    String responseSecondCircleNotJoinedName;

    //checked before usage that the specific circle exist
    String circleIdForPathParameter = "33";

    //invitation id for second user
    int invitationIdSecondUser;

    //newly-created thread id
    int createdThreadId;

    //information for thread
    int typeId = 1;
    String title = "New Thread POST QA";
    String body = "Text for body Thread QA";
    int isAnonymous = 0;
    List<String> attachments = new ArrayList<>();String  linkForThread = "https://www.youtube.com/watch?v=H7Jxy7MU0MI&ab_channel=CafeMusicBGMchannel";

    // information for tags
    List<String>  tags = new ArrayList<>();
    String  tagOne = "Tursene na Bugove";
    String  tagTwo = "Premahvane na Bugove";

    //set the community id
    String communityIdSet = "3";


    // setting the base URL and methods Api's
    String baseURI_BBTraining = "";

    String apiRegisterCreateUser = "/api/register";
    String apiLoginWithCreatedUser = "/api/auth/login";
    String apiGetCircles = "/api/circles";
    String apiFollowSpecificUser = "api/followings/{idSecondUser}/user";
    String apiCircleInputId = "/api/followings/{circleId}/circle";
    String apiCirclesIdInviteUser = "/api/circles/{circleId}/invite";
    String apiGetNotifications = "/api/notifications";
    String apiAcceptInvitation = "/api/invitations/{invitationIdAccepting}";
    String apiJoinSecondNotJoinedYetCircle = "api/followings/{secondCircleId}/circle";
    String apiCreatingThreadToNewlyJoinedCircle = "api/circles/{secondCircleId}/threads";
    String apiGetCreatedThreadById = "api/threads/{newThreadId}";
    String apiUpdateThreadById = "api/threads/{newlyThreadId}";
    String apiPutTagsToNewlyCreatedThread = "api/threads/{ThreadIdNew}/tags";
    String apiGetSpecificCircleAllInformation = "api/circles/{newlyCircle}";
    String apiFollowCommunityById = "api/followings/{communityId}/community";



    //setting the needed objects
    RegisterUser objValidFirstUserRegister = new RegisterUser(validFirstName,validLastName, validEmail, validPassword,validPasswordConfirmation);

    RegisterUser objValidSecondUserRegister = new RegisterUser(validFirstNameSecondUser, validLastNameSecondUser, validEmailSecondUser, validPasswordSecondUser, validPasswordConfirmationSecondUser);

    CirclesInformation objSetCirclesInformation = new CirclesInformation(notJoinedCircles,"");

    CirclesInformation objSetCirclesInformationJoined = new CirclesInformation(joinedCircles,"");

    Thread objSetThreadInformation = new Thread(typeId,title,body,isAnonymous);

    Thread objSetLinkToThread = new Thread(typeId,title,body,attachments);

    Tags objSetTagsInformation = new Tags(tags);




    @Test(priority = 1)
    public void validRegisterFirstUserTest() {
        RestAssured.baseURI = baseURI_BBTraining;


        Response validRegisterUserResponse =
                given()
                        .header("Content-Type", "application/json")
                        .header("Accept", "application/json")
                        .body(objValidFirstUserRegister).log().all()
                .when()
                        .post(apiRegisterCreateUser)
                .then()
                        .log().all()
                        .assertThat().statusCode(201)
                        .extract().response();

        String responseAsString = validRegisterUserResponse.asString();

        JsonPath jsnObjectConvert = new JsonPath(responseAsString);
        idUserResponseWhenValidProfileIsCreatedFirstUSer = jsnObjectConvert.getInt("data.user.id");

    }


    @Test(priority = 2)
    public void validRegisterSecondUserTest() {
        RestAssured.baseURI = baseURI_BBTraining;


        Response validRegisterUserResponse =
                given()
                        .header("Content-Type", "application/json")
                        .header("Accept", "application/json")
                        .body(objValidSecondUserRegister).log().all()
                .when()
                        .post(apiRegisterCreateUser)
                .then()
                        .log().all()
                        .assertThat().statusCode(201)
                        .extract().response();

        String responseAsString = validRegisterUserResponse.asString();

        JsonPath jsnObjectConvert = new JsonPath(responseAsString);
        idUserResponseWhenValidProfileIsCreatedSecondUSer = jsnObjectConvert.getInt("data.user.id");

    }


    @Test(priority = 3)
    public void loginWithNewlyRegisteredFirstUser() {

        RestAssured.baseURI = baseURI_BBTraining;


        Response validLogFromRegisterUserResponse =
                given()
                        .header("Content-Type", "application/json")
                        .header("Accept", "application/json")
                        .body(methodsForBody.bodyLoginUserWithAlreadyCreatedCredentials(objValidFirstUserRegister.getEmail(), objValidFirstUserRegister.getPassword())).log().all()
                        .when()
                        .post(apiLoginWithCreatedUser)
                        .then()
                        .log().all()
                        .assertThat().statusCode(200)
                        .extract().response();


        String responseAsString = validLogFromRegisterUserResponse.asString();

        JsonPath jsnObjectConvert = new JsonPath(responseAsString);
        tokenResponseWhenValidLoginFirstUser = jsnObjectConvert.getString("data.access_token");
        emailResponseWhenValidLoginFirstUser = jsnObjectConvert.getString("data.user.email");
        firstNameResponseWhenValidLoginFirstUser = jsnObjectConvert.getString("data.user.first_name");


    }

    @Test(priority = 4)
    public void postFirstUserFollowSecondUser() {

        RestAssured.baseURI = baseURI_BBTraining;

        Response validLogFromRegisterUserResponse =
                given()
                        .pathParam("idSecondUser", idUserResponseWhenValidProfileIsCreatedSecondUSer)
                        .header("Content-Type", "application/json")
                        .header("Accept", "application/json")
                        .header("Authorization", "Bearer  " + this.tokenResponseWhenValidLoginFirstUser + " ").log().all()
                .when()
                        .post(apiFollowSpecificUser)
                .then()
                        .log().all()
                        .assertThat().statusCode(204)
                        .extract().response();


    }

    @Test(priority = 5)
    public void postJoinSpecificCircleFirstUser() {

        RestAssured.baseURI = baseURI_BBTraining;

        Response validLogFromRegisterUserResponse =
                given()
                        .pathParam("circleId", circleIdForPathParameter)
                        .header("Content-Type", "application/json")
                        .header("Accept", "application/json")
                        .header("Authorization", "Bearer  " + this.tokenResponseWhenValidLoginFirstUser + " ").log().all()
                .when()
                        .post(apiCircleInputId)
                .then()
                        .log().all()
                        .assertThat().statusCode(204)
                        .extract().response();


    }

    @Test(priority = 6)
    public void postFirstUserInviteSecondUserToSpecificCircle() {

        RestAssured.baseURI = baseURI_BBTraining;

        //circle invitation
        String noteInformation = "Message Proba za invitation v Opredelen krugg";

        Response validLogFromRegisterUserResponse =
                given()
                        .pathParam("circleId", circleIdForPathParameter)
                        .header("Content-Type", "application/json")
                        .header("Accept", "application/json")
                        .header("Authorization", "Bearer  " + this.tokenResponseWhenValidLoginFirstUser + " ").log().all()
                        .body(methodsForBody.jsonBodyOfInvitingUserToSpecificCircle(idUserResponseWhenValidProfileIsCreatedSecondUSer, validEmailSecondUser, noteInformation))
                .when()
                        .post(apiCirclesIdInviteUser)
                .then()
                        .log().all()
                        .assertThat().statusCode(204)
                        .extract().response();


    }



    @Test(priority = 7)
    public void loginWithNewlyRegisteredSecondUser() {

        RestAssured.baseURI = baseURI_BBTraining;


        Response validLogFromRegisterUserResponse =
                given()
                        .header("Content-Type", "application/json")
                        .header("Accept", "application/json")
                        .body(methodsForBody.bodyLoginUserWithAlreadyCreatedCredentials(objValidSecondUserRegister.getEmail(), objValidSecondUserRegister.getPassword())).log().all()
                .when()
                        .post(apiLoginWithCreatedUser)
                .then()
                        .log().all()
                        .assertThat().statusCode(200)
                        .extract().response();


        String responseAsString = validLogFromRegisterUserResponse.asString();

        JsonPath jsnObjectConvert = new JsonPath(responseAsString);
        tokenResponseWhenValidLoginSecondUSer = jsnObjectConvert.getString("data.access_token");
        emailResponseWhenValidLoginSecondUSer = jsnObjectConvert.getString("data.user.email");
        firstNameResponseWhenValidLoginSecondUSer = jsnObjectConvert.getString("data.user.first_name");

    }

    @Test(priority = 8)
    public void getNotificationsForSecondUser() {

        RestAssured.baseURI = baseURI_BBTraining;


        Response validLogFromRegisterUserResponse =
                given()
                        .header("Content-Type", "application/json")
                        .header("Accept", "application/json")
                        .header("Authorization", "Bearer  " + this.tokenResponseWhenValidLoginSecondUSer + " ").log().all()
                .when()
                        .get(apiGetNotifications)
                .then()
                        .log().all()
                        .assertThat().statusCode(200)
                        .extract().response();


        String responseAsString = validLogFromRegisterUserResponse.asString();

        JsonPath jsonObjectConvert = new JsonPath(responseAsString);


        int dataArraySize = jsonObjectConvert.getInt(("data.size()"));

        for (int i = 0; i <dataArraySize ; i++) {
            String dataType = jsonObjectConvert.getString("data["+i+"].data.type");
            if (dataType.equalsIgnoreCase("invite_to_circle")){
                invitationIdSecondUser = jsonObjectConvert.getInt("data["+i+"].data.object.invitation_id");
                System.out.println(invitationIdSecondUser);
                break;
            }
        }



    }


    @Test(priority = 9)
    public void postFollowSpecificCommunitySecondUser() {

        RestAssured.baseURI = baseURI_BBTraining;

        Response validLogFromRegisterUserResponse =
                given()
                        .pathParam("communityId",communityIdSet )
                        .header("Content-Type", "application/json")
                        .header("Accept", "application/json")
                        .header("Authorization", "Bearer  " + this.tokenResponseWhenValidLoginSecondUSer + " ").log().all()
                .when()
                        .post(apiFollowCommunityById)
                .then()
                        .log().all()
                        .assertThat().statusCode(204)
                        .extract().response();

    }

    @Test(priority = 10)
    public void postAcceptTheInvitationForSecondUser() {

        RestAssured.baseURI = baseURI_BBTraining;

        Response validLogFromRegisterUserResponse =
                given()
                        .pathParam("invitationIdAccepting", invitationIdSecondUser)
                        .header("Content-Type", "application/json")
                        .header("Accept", "application/json")
                        .header("Authorization", "Bearer  " + this.tokenResponseWhenValidLoginSecondUSer + " ").log().all()
                .when()
                        .post(apiAcceptInvitation)
                .then()
                        .log().all()
                        .assertThat().statusCode(204)
                        .extract().response();

    }

    @Test(priority = 11)
    public void getCirclesListWhichNotJoinedSecondUser(){

        RestAssured.baseURI = baseURI_BBTraining;



        Response validLogFromRegisterUserResponse =
                given()
                        .header("Content-Type","application/json")
                        .header("Accept","application/json" )
                        .header("Authorization", "Bearer  " + this.tokenResponseWhenValidLoginSecondUSer + " ").log().all()
                        .body(objSetCirclesInformation).log().all()
                .when()
                        .get(apiGetCircles)
                .then()
                        .log().all()
                        .assertThat().statusCode(200)
                        .extract().response();


        String responseAsString = validLogFromRegisterUserResponse.asString();

        JsonPath objJsonConvert = new JsonPath(responseAsString);
        responseSecondCircleNotJoinedId = objJsonConvert.getInt("data[1].id");
        responseSecondCircleNotJoinedName = objJsonConvert.getString("data[1].name");

    }


    @Test(priority = 12)
    public void joinSecondCircleWhichNotJoinedSecondUser(){

        RestAssured.baseURI = baseURI_BBTraining;


        Response validLogFromRegisterUserResponse =
                given()
                        .pathParam("secondCircleId", responseSecondCircleNotJoinedId)
                        .header("Content-Type","application/json")
                        .header("Accept","application/json" )
                        .header("Authorization", "Bearer  " + this.tokenResponseWhenValidLoginSecondUSer + " ").log().all()
                .when()
                        .post(apiJoinSecondNotJoinedYetCircle)
                .then()
                        .log().all()
                        .assertThat().statusCode(204)
                        .extract().response();

    }


    @Test(priority = 13)
    public void getCirclesListWhichIsJoinedSecondUser(){

        RestAssured.baseURI = baseURI_BBTraining;


        Response validLogFromRegisterUserResponse =
                given()
                        .header("Content-Type","application/json")
                        .header("Accept","application/json" )
                        .header("Authorization", "Bearer  " + this.tokenResponseWhenValidLoginSecondUSer + " ").log().all()
                        .body(objSetCirclesInformationJoined).log().all()
                .when()
                        .get(apiGetCircles)
                .then()
                        .log().all()
                        .assertThat().statusCode(200)
                        .extract().response();


        String responseAsString = validLogFromRegisterUserResponse.asString();

        JsonPath objJsonConvert = new JsonPath(responseAsString);
        ArrayList<String> actualResponseJoinedCircle = objJsonConvert.get("data.name");
        String convertResponseToString = actualResponseJoinedCircle.get(0);

        Assert.assertEquals(convertResponseToString, this.responseSecondCircleNotJoinedName);

    }



    @Test(priority = 14)
    public void postCreatingThreadToTheNewlyJoinedCircleSecondUser(){

    Response validLogFromRegisterUserResponse =
            given()
                    .pathParam("secondCircleId", responseSecondCircleNotJoinedId)
                    .header("Content-Type","application/json")
                    .header("Accept","application/json" )
                    .header("Authorization", "Bearer  " + this.tokenResponseWhenValidLoginSecondUSer + " ").log().all()
                    .body(objSetThreadInformation).log().all()
            .when()
                    .post(apiCreatingThreadToNewlyJoinedCircle)
            .then()
                    .log().all()
                    .assertThat().statusCode(201)
                    .extract().response();

        String responseAsString = validLogFromRegisterUserResponse.asString();
        JsonPath objJsonConvert = new JsonPath(responseAsString);
        createdThreadId = objJsonConvert.getInt("data.id");


}

    @Test(priority = 15)
    public void getTheNewlyCreatedThreadSecondUser(){

        Response validLogFromRegisterUserResponse =
                given()
                        .pathParam("newThreadId", createdThreadId )
                        .header("Content-Type","application/json")
                        .header("Accept","application/json" )
                        .header("Authorization", "Bearer  " + this.tokenResponseWhenValidLoginSecondUSer + " ").log().all()
                .when()
                        .get(apiGetCreatedThreadById )
                .then()
                        .log().all()
                        .assertThat().statusCode(200)
                        .extract().response();

        String responseAsString = validLogFromRegisterUserResponse.asString();
        JsonPath objJsonConvert = new JsonPath(responseAsString);
        int authorId = objJsonConvert.getInt("data.author.id");

        Assert.assertEquals(authorId, idUserResponseWhenValidProfileIsCreatedSecondUSer);



    }


    @Test(priority = 16)
    public void postUpdateNewlyCreatedThreadSecondUser(){
        attachments.add(linkForThread);

        Response validLogFromRegisterUserResponse =
                given()
                        .pathParam("newlyThreadId", createdThreadId)
                        .header("Content-Type","application/json")
                        .header("Accept","application/json" )
                        .header("Authorization", "Bearer  " + this.tokenResponseWhenValidLoginSecondUSer + " ").log().all()
                        .body(objSetLinkToThread).log().all()
                .when()
                        .post(apiUpdateThreadById)
                .then()
                        .log().all()
                        .assertThat().statusCode(200)
                        .extract().response();

    }


    @Test(priority = 17)
    public void putTagsToNewlyCreatedThreadSecondUser(){
        tags.add(tagOne);
        tags.add(tagTwo);

        Response validLogFromRegisterUserResponse =
                given()
                        .pathParam("ThreadIdNew", createdThreadId)
                        //.header("Content-Type","multipart/form-data")
                        .header("Content-Type","application/json")
                        .header("Accept","application/json" )
                        .header("Authorization", "Bearer  " + this.tokenResponseWhenValidLoginSecondUSer + " ").log().all()
                        .body(objSetTagsInformation).log().all()
                .when()
                        .put(apiPutTagsToNewlyCreatedThread)
                .then()
                        .log().all()
                        .assertThat().statusCode(204)
                        .extract().response();

    }

    @Test(priority = 18)
    public void getNewlyCreatedThreadIsInTheRightCircleSecondUser(){

        Response validLogFromRegisterUserResponse =
                given()
                        .pathParam("newThreadId", createdThreadId )
                        .header("Content-Type","application/json")
                        .header("Accept","application/json" )
                        .header("Authorization", "Bearer  " + this.tokenResponseWhenValidLoginSecondUSer + " ").log().all()
                .when()
                        .get(apiGetCreatedThreadById )
                .then()
                        .log().all()
                        .assertThat().statusCode(200)
                        .extract().response();

        String responseAsString = validLogFromRegisterUserResponse.asString();

        JsonPath objJsonConvert = new JsonPath(responseAsString);
        int responseThreadCircleId = objJsonConvert.getInt("data.circle.id");

        Assert.assertEquals(responseThreadCircleId, responseSecondCircleNotJoinedId);



    }

    @Test(priority = 19)
    public void getNewlyCreatedThreadAndVerifyInformationSecondUser(){

        Response validLogFromRegisterUserResponse =
                given()
                        .pathParam("newThreadId", createdThreadId )
                        .header("Content-Type","application/json")
                        .header("Accept","application/json" )
                        .header("Authorization", "Bearer  " + this.tokenResponseWhenValidLoginSecondUSer + " ").log().all()
                .when()
                        .get(apiGetCreatedThreadById )
                .then()
                        .log().all()
                        .assertThat().statusCode(200)
                        .extract().response();

        String responseAsString = validLogFromRegisterUserResponse.asString();

        JsonPath objJsonConvert = new JsonPath(responseAsString);
        String responseThreadTitle = objJsonConvert.getString("data.title");
        Assert.assertEquals(responseThreadTitle, title);

        String responseThreadBody = objJsonConvert.getString("data.body");
        Assert.assertEquals(responseThreadBody, body);

        String responseThreadAuthorFirstName = objJsonConvert.getString("data.author.first_name");
        Assert.assertEquals(responseThreadAuthorFirstName, validFirstNameSecondUser);

    }




    @Test(priority = 20)
    public void getTotalNumberOfThreadsInNewlyJoinedCircleSecondUser(){

        RestAssured.baseURI = baseURI_BBTraining;


        Response validLogFromRegisterUserResponse =
                given()
                        .pathParam("newlyCircle", responseSecondCircleNotJoinedId)
                        .header("Content-Type", "application/json")
                        .header("Accept", "application/json")
                        .header("Authorization", "Bearer  " + this.tokenResponseWhenValidLoginFirstUser + " ").log().all()
                .when()
                        .get(apiGetSpecificCircleAllInformation)
                .then()
                        .log().all()
                        .assertThat().statusCode(200)
                        .extract().response();

        String responseAsString = validLogFromRegisterUserResponse.asString();

        JsonPath objJsonConvert = new JsonPath(responseAsString);

        int responseTotalThreadsInTheCircle = objJsonConvert.getInt("data.threads.size()");
        String convertTotalThreads = String.valueOf(responseTotalThreadsInTheCircle);
        System.out.println("Total number of Threads: " + convertTotalThreads + ", in circle with number:"+responseSecondCircleNotJoinedId+"");

    }



}
