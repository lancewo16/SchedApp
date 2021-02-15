package Model;

import java.time.LocalDateTime;

/**
 * The firstLevelDivision class is used for filtering the selected country
 */

public class FirstLevelDivision {

    public Integer divisionID;
    private String division;
    private LocalDateTime createDate;
    private String createdBy;
    private LocalDateTime lastUpdate;
    private String lastUpdatedBy;
    private int countryID;

    //Constructor for Add Customer FLD Combo Box
    public FirstLevelDivision(Integer divisionID) {
        this.divisionID=divisionID;
    }

    /**
     * The following is the Constructor for FirstLevelDivision
     * @param lastUpdatedBy
     * @param divisionID
     * @param createDate
     * @param createdBy
     * @param lastUpdate
     * @param countryID
     * @param division
     */

    public FirstLevelDivision(Integer divisionID, String division, LocalDateTime createDate, String createdBy, LocalDateTime lastUpdate, String lastUpdatedBy, int countryID) {
        this.divisionID = divisionID;
        this.division = division;
        this.createDate = createDate;
        this.createdBy = createdBy;
        this.lastUpdate = lastUpdate;
        this.lastUpdatedBy = lastUpdatedBy;
        this.countryID = countryID;
    }

    public Integer getDivisionID() {
        return divisionID;
    }

    public void setDivisionID(Integer divisionID) {
        this.divisionID = divisionID;
    }

    public String getDivision() {
        return division;
    }

    public void setDivision(String division) {
        this.division = division;
    }

    public LocalDateTime getCreateDate() {
        return createDate;
    }

    public void setCreateDate(LocalDateTime createDate) {
        this.createDate = createDate;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public LocalDateTime getLastUpdate() {
        return lastUpdate;
    }

    public void setLastUpdate(LocalDateTime lastUpdate) {
        this.lastUpdate = lastUpdate;
    }

    public String getLastUpdatedBy() {
        return lastUpdatedBy;
    }

    public void setLastUpdatedBy(String lastUpdatedBy) {
        this.lastUpdatedBy = lastUpdatedBy;
    }

    public int getCountryID() {
        return countryID;
    }

    public void setCountryID(int countryID) {
        this.countryID = countryID;
    }

    /** Convert hashmap to String for FLD objects in combobox of Customer
     *     (credit to Mr. Kinkead Webinar on Combo Boxes)
     */


    @Override
    public String toString() {
        return ("Division: "+division + " ID: " + divisionID);

    }
}
