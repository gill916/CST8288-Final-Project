package AcademicExchangePlatform.model;

import java.util.List;
import java.util.ArrayList;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * Represents an academic professional in the system.
 */
public class AcademicProfessional extends User {
    
    /**
     * The first name of the academic professional.
     */
    private String firstName;
    
    /**
     * The last name of the academic professional.
     */
    private String lastName;
    
    /**
     * The current institution of the academic professional.
     */
    private String currentInstitution;
    
    /**
     * The position of the academic professional.
     */
    private String position;
    
    /**
     * The education background of the academic professional.
     */
    private String educationBackground;
    
    /**
     * The expertise of the academic professional.
     */
    private List<String> expertise;
    
    /**
     * Whether the profile of the academic professional is complete.
     */
    private boolean profileComplete;

    /**
     * Gets the first name of the academic professional.
     * @return The first name of the academic professional
     */
    public String getFirstName() {
        return firstName;
    }

    /**
     * Sets the first name of the academic professional.
     * @param firstName The first name of the academic professional
     */
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    /**
     * Gets the last name of the academic professional.
     * @return The last name of the academic professional
     */
    public String getLastName() {
        return lastName;
    }

    /**
     * Sets the last name of the academic professional.
     * @param lastName The last name of the academic professional
     */
    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    /**
     * Gets the current institution of the academic professional.
     * @return The current institution of the academic professional
     */
    public String getCurrentInstitution() {
        return currentInstitution;
    }

    /**
     * Sets the current institution of the academic professional.
     * @param currentInstitution The current institution of the academic professional
     */
    public void setCurrentInstitution(String currentInstitution) {
        this.currentInstitution = currentInstitution;
    }

    /**
     * Gets the position of the academic professional.
     * @return The position of the academic professional
     */
    public String getPosition() {
        return position;
    }

    /**
     * Sets the position of the academic professional.
     * @param position The position of the academic professional
     */
    public void setPosition(String position) {
        this.position = position;
    }

    /**
     * Gets the education background of the academic professional.
     * @return The education background of the academic professional
     */
    public String getEducationBackground() {
        return educationBackground;
    }

    /**
     * Sets the education background of the academic professional.
     * @param educationBackground The education background of the academic professional
     */
    public void setEducationBackground(String educationBackground) {
        this.educationBackground = educationBackground;
    }

    /**
     * Gets the expertise of the academic professional.
     * @return The expertise of the academic professional
     */
    public List<String> getExpertise() {
        return expertise;
    }

    /**
     * Sets the expertise of the academic professional.
     * @param expertise The expertise of the academic professional
     */
    public void setExpertise(List<String> expertise) {
        this.expertise = expertise;
    }

    /**
     * Checks if the profile of the academic professional is complete.
     * @return true if the profile is complete, false otherwise
     */
    public boolean isProfileComplete() {
        return profileComplete;
    }

    /**
     * Sets whether the profile of the academic professional is complete.
     * @param profileComplete Whether the profile is complete
     */
    public void setProfileComplete(boolean profileComplete) {
        this.profileComplete = profileComplete;
    }

    /**
     * Completes the profile of the academic professional.
     * @param position The position of the academic professional
     * @param currentInstitution The current institution of the academic professional
     * @param educationBackground The education background of the academic professional
     * @param expertise The expertise of the academic professional
     */
    public void completeProfile(String position, String currentInstitution, 
                              String educationBackground, List<String> expertise) {
        this.position = position;
        this.currentInstitution = currentInstitution;
        this.educationBackground = educationBackground;
        this.expertise = new ArrayList<>(expertise);
        this.profileComplete = true;
    }
}
