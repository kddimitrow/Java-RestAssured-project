package AllUserCases;

public class ChangePasswordSetup {


    public String old_password;
    public String password;
    public String password_confirmation;

    public ChangePasswordSetup(String old_password, String password, String password_confirmation) {
        this.old_password = old_password;
        this.password = password;
        this.password_confirmation = password_confirmation;
    }


    public String getOld_password() {
        return old_password;
    }

    public String getPassword() {
        return password;
    }

    public String getPassword_confirmation() {
        return password_confirmation;
    }
}
