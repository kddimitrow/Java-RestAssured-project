package AllUserCases;

import java.util.List;

public class RegisterSetupInformation {

    public String title;
    public String department;
    public String company;
    public String location;
    public String biography;
    public String skills;

    // variable for the "/register/interests"
    public List<Integer> interests;


    public RegisterSetupInformation(List<Integer> interests) {
        this.interests = interests;
    }

    public List<Integer> getInterests() {
        return interests;
    }


    //constructor for the "/register/interests"
    public RegisterSetupInformation(String title, String department, String company, String location, String biography, String skills) {
        this.title = title;
        this.department = department;
        this.company = company;
        this.location = location;
        this.biography = biography;
        this.skills = skills;
    }


    public String getTitle() {
        return title;
    }

    public String getDepartment() {
        return department;
    }

    public String getCompany() {
        return company;
    }

    public String getLocation() {
        return location;
    }

    public String getBiography() {
        return biography;
    }

    public String getSkills() {
        return skills;
    }
}
