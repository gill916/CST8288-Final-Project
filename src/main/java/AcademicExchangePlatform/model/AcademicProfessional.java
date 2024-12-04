package AcademicExchangePlatform.model;

import java.util.List;

public class AcademicProfessional extends User {
    private String firstName;
    private String lastName;
    private String position;
    private List<String> expertise;

    // Getters and Setters
    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public List<String> getExpertise() {
        return expertise;
    }

    public void setExpertise(List<String> expertise) {
        this.expertise = expertise;
    }

    // Method to complete profile
    public void completeProfile(String position, List<String> expertise) {
        this.position = position;
        this.expertise = expertise;
    }
}
