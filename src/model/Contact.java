package model;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/** This class creates and manages the Contact objects and relevant methods. */
public class Contact {
    private int contactId;
    private String contactName;
    private String email;

    private static ObservableList<Contact> allContacts = FXCollections.observableArrayList();
    private static ObservableList<Appointment> allAssociatedAppointments = FXCollections.observableArrayList();

    /** This method is the Contact constructor.
     * @param contactId The contact ID being created
     * @param contactName The contact name being created
     * @param email The email being created
     */
    public Contact(int contactId, String contactName, String email){
        this.contactId = contactId;
        this.contactName = contactName;
        this.email = email;
    }

    /** This method returns the allContact Observable List.
     *@return Returns the contact Observable List.
     */
    public static ObservableList<Contact> getAllContacts() {
        return allContacts;
    }

    /** This method gets the contact ID.
     * @return Returns the contact ID
     */
    public int getContactId(){
        return contactId;
    }

    /** This method gets the contact name.
     * @return Returns the contact name
     */
    public String getContactName(){
        return contactName;
    }

    /** This method gets the email address.
     * @return Returns the email address
     */
    public String getEmail(){
        return email;
    }

    /** This method sets the contact ID.
     * @param contactId The contact ID being set
     */
    public void setContactId(int contactId) {
        this.contactId = contactId;
    }

    /** This method sets the contact name.
     * @param contactName The contact name being set
     */
    public void setContactName(String contactName) {
        this.contactName = contactName;
    }

    /** This method sets the email address.
     * @param email The email being set
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /** This method adds a contact to the allContact Observable List.
     * @param contact The contact being added
     */
    public void addContact(Contact contact) {
        allContacts.add(contact);
    }

    /** This method adds an appointment to the allAssociatedAppointments Observable List.
     * @param appointment The appointment being added
     */
    public void addAssociatedAppointment(Appointment appointment){
        allAssociatedAppointments.add(appointment);
    }

    /** This method returns the allAssociatedAppointments Observable List.
     * @return The allAssociatedAppointments Observable List
     */
    public ObservableList<Appointment> getAllAssociatedAppointments(){
        return allAssociatedAppointments;
    }

    /** This method deletes an appointment from the allAssociatedAppointments Observable List.
     * @param appointment The appointment being deleted
     */
    public void deleteAssociatedAppointment(Appointment appointment){
        allAssociatedAppointments.remove(appointment);
    }

    /** This method sets the allAssociatedAppointments list according to the contact given and their respective contact IDs.
     * @param contact The contact whose appointments are being added
     */
    public void setAllAssociatedAppointments(Contact contact) {
       allAssociatedAppointments.clear();
        for(Appointment appointment : Appointment.getAllAppointments()){
            if(appointment.getContactId() == contact.getContactId()){
                allAssociatedAppointments.add(appointment);
            }
        }
    }

    /** This method deletes all appointments associated with a contact.
     * @param contact The contact whose associated appointments are being deleted
     */
    public void deleteAllAssociatedAppointments(Contact contact) {
        for (Appointment appointment : allAssociatedAppointments) {
            if(appointment.getContactId() == contact.getContactId()) {
                Appointment.getAllAppointments().remove(appointment);
            }
        }
        allAssociatedAppointments.clear();
    }

    /** This method updates an associated appointment.
     * @param newAppointment The appointment being used to update
     * @param index The index of the appointment being updated
     */
    public void updateAssociatedAppointment(Appointment newAppointment, int index){
        for(Appointment appointment : allAssociatedAppointments){
            if(allAssociatedAppointments.indexOf(appointment) == index){
                allAssociatedAppointments.set(index, newAppointment);
            }
        }
    }

    /** This method finds a contact using appointment information.
     * @param appointment The appointment being used to find the contact.
     * @return Returns the contact found
     */
    public static Contact findContact(Appointment appointment){
        for(Contact contact : allContacts){
            if(contact.getContactId() == appointment.getContactId()){
                return contact;
            }
        }
        return null;
    }

    /** This method find a contact using contact ID.
     * @param contactId The contact ID being used to search
     * @return Returns the contact that was found
     */
    public static Contact findContact(int contactId){
        for(Contact contact : allContacts){
            if(contact.getContactId() == contactId){
                return contact;
            }
        }
        return null;
    }

    /** This method changes the string output of Contact objects to this format.
     * @return Returns the formatted Contact string
     */
    @Override
    public String toString(){
        return(contactName + " (ID: " + contactId + ")");
    }

}
