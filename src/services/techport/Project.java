package services.techport;

import java.util.ArrayList;

public class Project{
    private int id;
    private String lastUpdated;
    private String title;
    private String acronym;
    private String status;
    private String startDate;
    private String endDate;
    private String description;
    private String benefits;
    private String technologyMaturityStart;
    private String technologyMaturityCurrent;
    private String technologyMaturityEnd;
    private ArrayList<String> destinations;
    private String supportedMissionType;
    private String responsibleProgram;
    private Organization leadOrganization;
    private ArrayList<String> workLocations;
    private ArrayList<String> programDirectors;
    private ArrayList<String> projectManagers;
    private ArrayList<String> principalInvestigators;
    private ArrayList<String> coInvestigators;
    private String website;
    private ArrayList<LibraryItem> libraryItems;
    private ArrayList<String> closeoutDocuments;
    private ArrayList<Organization> supportingOrganizations;
    private ArrayList<Organization> coFundingPartners;
    private ArrayList<ImpactArea> primaryTas;
    private ArrayList<ImpactArea> additionalTas;

    // <editor-fold defaultstate="collapsed" desc=" getters() ">

    public int getId() {
        return id;
    }

    public String getLastUpdated() {
        return lastUpdated;
    }

    public String getTitle() {
        return title;
    }

    public String getAcronym() {
        return acronym;
    }

    public String getStatus() {
        return status;
    }

    public String getStartDate() {
        return startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public String getDescription() {
        return description;
    }

    public String getBenefits() {
        return benefits;
    }

    public String getTechnologyMaturityStart() {
        return technologyMaturityStart;
    }

    public String getTechnologyMaturityCurrent() {
        return technologyMaturityCurrent;
    }

    public String getTechnologyMaturityEnd() {
        return technologyMaturityEnd;
    }

    public ArrayList<String> getDestinations() {
        return destinations;
    }

    public String getSupportedMissionType() {
        return supportedMissionType;
    }

    public String getResponsibleProgram() {
        return responsibleProgram;
    }

    public Organization getLeadOrganization() {
        return leadOrganization;
    }

    public ArrayList<String> getWorkLocations() {
        return workLocations;
    }

    public ArrayList<String> getProgramDirectors() {
        return programDirectors;
    }

    public ArrayList<String> getProjectManagers() {
        return projectManagers;
    }

    public ArrayList<String> getPrincipalInvestigators() {
        return principalInvestigators;
    }

    public String getWebsite() {
        return website;
    }

    public ArrayList<LibraryItem> getLibraryItems() {
        return libraryItems;
    }

    public ArrayList<String> getCloseoutDocuments() {
        return closeoutDocuments;
    }

    public ArrayList<Organization> getSupportingOrganizations() {
        return supportingOrganizations;
    }

    public ArrayList<Organization> getCoFundingPartners() {
        return coFundingPartners;
    }

    public ArrayList<ImpactArea> getPrimaryTas() {
        return primaryTas;
    }

    public ArrayList<ImpactArea> getAdditionalTas() {
        return additionalTas;
    }

    public ArrayList<String> getCoInvestigators() {
        return coInvestigators;
    }

    // </editor-fold>


    public void setTitle(String title) {
        this.title = title;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Override
    public String toString() {
        if(title != null && !title.isEmpty()){
            return title;
        }else
            return id+"";
    }
}

