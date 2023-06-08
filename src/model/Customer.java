package model;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/** This class creates and manages Customer objects and relevant methods */
public class Customer {

    private int customerId;
    private String customerName;
    private String address;
    private String postalCode;
    private String phone;
    private int divisionId;

    private static ObservableList<Customer> allCustomers = FXCollections.observableArrayList();
    private ObservableList<Appointment> allAssociatedAppointments = FXCollections.observableArrayList();

    /** This method is the customer constructor.
     * @param customerId The customer ID being created
     * @param customerName The customer name being created
     * @param address The address being created
     * @param postalCode The postal code being created
     * @param phone The phone being created
     * @param divisionId The divison ID being created
     */
    public Customer(int customerId, String customerName, String address, String postalCode, String phone, int divisionId) {
        this.customerId = customerId;
        this.customerName = customerName;
        this.address = address;
        this.postalCode = postalCode;
        this.phone = phone;
        this.divisionId = divisionId;
    }

    /** This method sets the customer ID
     * @param customerId The customer ID being set
     */
    void setCustomerId(int customerId) {
        this.customerId = customerId;
    }

    /** This method sets the customer name.
     * @param customerName The customer name being set
     */
    void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    /** This method sets the address.
     * @param address The address being set
     */
    void setAddress(String address) {
        this.address = address;
    }

    /** This method sets the postal code.
     * @param postalCode The postal code being set
     */
    void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }

    /** This method sets the phone number.
     * @param phone The phone number being set
     */
    void setPhone(String phone) {
        this.phone = phone;
    }


    /** This method gets the customer ID.
     * @return Returns customer ID
     */
    public int getCustomerId() {
        return customerId;
    }

    /** This method gets the customer name.
     * @return Returns customer name
     */
    public String getCustomerName() {
        return customerName;
    }

    /** This method gets the address.
     * @return Returns address
     */
    public String getAddress() {
        return address;
    }

    /** This method gets the postal code.
     * @return Returns the postal code
     */
    public String getPostalCode() {
        return postalCode;
    }

    /** This method gets the phone number.
     * @return Returns phone number
     */
    public String getPhone() {
        return phone;
    }

    /** This method gets the division ID.
     * @return Returns division ID
     */
    public int getDivisionId() {
        return divisionId;
    }


    /** This method returns the allCustomers Observable List.
     * @return Returns allCustomers
     */
    public static ObservableList<Customer> getAllCustomers() {
        return allCustomers;
    }

    /** This method adds a customer to the allCustomers Observable List.
     * @param customer The customer being added
     */
    public void addCustomer(Customer customer) {
        allCustomers.add(customer);
    }

    /** This method deletes a customer from the allCustomers Observable List.
     * @param customer The customer being deleted
     */
    public void deleteCustomer(Customer customer) {
        deleteAllAssociatedAppointments(customer);
        allCustomers.remove(customer);
    }

    /** This method updated a customer.
     * @param newCustomer The customer being used to updated
     * @param index The index of the customer being updated
     */
    public void updateCustomer(Customer newCustomer, int index){
        for(Customer customer : allCustomers){
            if(allCustomers.indexOf(customer) == index){
                allCustomers.set(index, newCustomer);
            }
        }
    }

    /** This method finds a customer using an appointment.
     * @param appointment The appointment being used to search
     * @return Returns the customer found
     */
    public static Customer findCustomer(Appointment appointment){
        for(Customer customer : allCustomers){
            if(customer.getCustomerId() == appointment.getCustomerId()){
                return customer;
            }
        }
        return null;
    }

    /** This method finds a customer using a customer ID.
     * @param customerId The customer ID being used to search
     * @return Returns the customer found
     */
    public static Customer findCustomer(int customerId){
        for(Customer customer : allCustomers){
            if(customer.getCustomerId() == customerId){
                return customer;
            }
        }
        return null;
    }

    /** This method adds an appointment to the allAssociatedAppointments Observable List
     * @param appointment The appointment being added
     */
    public void addAssociatedAppointment(Appointment appointment){
        allAssociatedAppointments.add(appointment);
    }

    /** This method returns allAssociatedAppointments
     * @return Returns allAssociatedAppointments
     */
    public ObservableList<Appointment> getAllAssociatedAppointments(){
        return allAssociatedAppointments;
    }

    /** This method deletes an appointment from the allAssociatedAppointments Observable List
     * @param appointment The appointment being deleted.
     */
    public void deleteAssociatedAppointment(Appointment appointment){
        allAssociatedAppointments.remove(appointment);
    }

    /** This method deletes all appointments associated with a customer from the allAppointments Observable List. It then clears
     * allAssociatedAppointments.
     * @param customer The customer whose appointments are being deleted
     */
    public void deleteAllAssociatedAppointments(Customer customer) {
        for (Appointment appointment : allAssociatedAppointments) {
            if(appointment.getCustomerId() == customer.getCustomerId()) {
                Appointment.getAllAppointments().remove(appointment);
            }
        }
        allAssociatedAppointments.clear();
    }

    /** This method updates an appointment in the allAssociatedAppointments Observable List.
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

    /** This method formats string customer data.
     * @return Returns the formatted string
     */
    @Override
    public String toString(){
        return(customerName + " (ID: " + customerId + ")");
    }
}