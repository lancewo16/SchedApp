package Model;

/**
 * This contact class is used for populating the contacts' associated attributes and for the GUI to display the names in various combo boxes
 */
public class Contact {

    private  int contactID;
    private String contactName;
    private String contactEmail;

    /**
     * The following is the Constructor for Contact
     * @param contactEmail
     * @param contactID
     * @param contactName
     */
    public Contact(int contactID, String contactName, String contactEmail) {
        this.contactID = contactID;
        this.contactName = contactName;
        this.contactEmail = contactEmail;
    }

    public Contact() {

    }

    public Contact(int contactID) {
        this.contactID = contactID;
    }

    public  int getContactID() {
        return contactID;
    }

    public void setContactID(int contactID) {
        this.contactID = contactID;
    }

    public String getContactName() {
        return contactName;
    }

    public void setContactName(String contactName) {
        this.contactName = contactName;
    }

    public String getContactEmail() {
        return contactEmail;
    }

    public void setContactEmail(String contactEmail) {
        this.contactEmail = contactEmail;
    }


    /**Conversion of the hashmap to String for Country objects in the combobox of Customer
     * (Credit to course instructor, Mr. Kinkead, in his Webinar on Combo Boxes)
     * */

    @Override
    public String toString() {

        return ("Contact Name: " +(contactName )+ " Contact ID: " + contactID);
    }
}
