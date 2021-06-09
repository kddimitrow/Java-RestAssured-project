package AllUserCases;

import org.apache.commons.lang3.RandomStringUtils;

import java.util.List;

public class methodsForBody {




    public static String bodyLoginUserWithAlreadyCreatedCredentials(String email, String password){

        String bodyReturn = "{\n" +
                "  \"email\": \""+email+"\",\n" +
                "  \"password\": \""+password+"\"\n" +
                "}";

        return bodyReturn;

    }


    public static String randomStringGenerator(int stringLength){
        String generatedString = RandomStringUtils.randomAlphanumeric(stringLength);
        return generatedString;
    }

    public static String jsonBodyOfInvitingUserToSpecificCircle(int idsOfInvitedUsers, String invitedUsersEmails, String noteInformation){
        String jsonBodyInvitation = "{\n" +
                "  \"users\": {\n" +
                "    \"existing\": [\n" +
                "      "+idsOfInvitedUsers+"\n" +
                "    ],\n" +
                "    \"new\": [\n" +
                "      \""+invitedUsersEmails+"\"\n" +
                "    ]\n" +
                "  },\n" +
                "  \"note\": \""+noteInformation+"\"\n" +
                "}";

        return jsonBodyInvitation;
    }

}
