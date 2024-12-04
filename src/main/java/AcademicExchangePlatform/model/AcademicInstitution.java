package AcademicExchangePlatform.model;

public class AcademicInstitution extends User {
    private String institutionName;
    private String address;

    // Getters and Setters
    public String getInstitutionName() {
        return institutionName;
    }

    public void setInstitutionName(String institutionName) {
        this.institutionName = institutionName;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    // Method to complete profile
    public void completeProfile(String address) {
        this.address = address;
    }
}
