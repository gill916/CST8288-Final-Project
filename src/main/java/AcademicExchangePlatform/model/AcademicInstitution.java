package AcademicExchangePlatform.model;

/**
 * Represents an academic institution in the system.
 */
public class AcademicInstitution extends User {
    /**
     * The name of the institution.
     */
    private String institutionName;
    private String address;
    private String contactEmail;
    private String contactPhone;
    private String website;
    private String description;

    // Getters and Setters
    public String getInstitutionName() {
        return institutionName;
    }

    /**
     * Sets the name of the institution.
     * @param institutionName The name of the institution
     */
    public void setInstitutionName(String institutionName) {
        this.institutionName = institutionName;
    }

    public String getAddress() {
        return address;
    }
    
    /**
     * Sets the address of the institution.
     * @param address The address of the institution
     */
    public void setAddress(String address) {
        this.address = address;
    }

    /**
     * Gets the contact email of the institution.
     * @return The contact email of the institution
     */
    public String getContactEmail() {
        return contactEmail;
    }
 
    /**
     * Sets the contact email of the institution.
     * @param contactEmail The contact email of the institution
     */
    public void setContactEmail(String contactEmail) {
        this.contactEmail = contactEmail;
    }

    /**
     * Gets the contact phone of the institution.
     * @return The contact phone of the institution
     */
    public String getContactPhone() {
        return contactPhone;
    }

    /**
     * Sets the contact phone of the institution.
     * @param contactPhone The contact phone of the institution
     */
    public void setContactPhone(String contactPhone) {
        this.contactPhone = contactPhone;
    }

    /**
     * Gets the website of the institution.
     * @return The website of the institution
     */
    public String getWebsite() {
        return website;
    }

    /**
     * Sets the website of the institution.
     * @param website The website of the institution
     */
    public void setWebsite(String website) {
        this.website = website;
    }

    /**
     * Gets the description of the institution.
     * @return The description of the institution
     */
    public String getDescription() {
        return description;
    }

    /**
     * Sets the description of the institution.
     * @param description The description of the institution
     */
    public void setDescription(String description) {
        this.description = description;
    }
 

    /**
     * Completes the profile of the institution.
     * @param address The address of the institution
     * @param contactEmail The contact email of the institution
     * @param contactPhone The contact phone of the institution
     * @param website The website of the institution
     * @param description The description of the institution
     */
    public void completeProfile(String address, String contactEmail, String contactPhone, String website, String description) {
        this.address = address;
        this.contactEmail = contactEmail;
        this.contactPhone = contactPhone;
        this.website = website;
        this.description = description;
    }

    /**
     * Checks if the profile of the institution is complete.
     * @return true if the profile is complete, false otherwise
     */
    @Override
    public boolean isProfileComplete() {
        return institutionName != null && !institutionName.isEmpty() &&
               address != null && !address.isEmpty() &&
               contactEmail != null && !contactEmail.isEmpty() &&
               contactPhone != null && !contactPhone.isEmpty();
    }
}
