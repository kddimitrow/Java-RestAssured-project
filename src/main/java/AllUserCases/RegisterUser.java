package AllUserCases;

public class RegisterUser {

        private String first_name;
        private String last_name;
        private String email;
        private String password;
        private String password_confirmation;




    public RegisterUser(String first_name, String last_name, String email, String password, String password_confirmation) {
        this.first_name = first_name;
        this.last_name = last_name;
        this.email = email;
        this.password = password;
        this.password_confirmation = password_confirmation;
    }

    public String getFirst_name() {
        return first_name;
    }

    public String getLast_name() {
        return last_name;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public String getPassword_confirmation() {
        return password_confirmation;
    }
}
