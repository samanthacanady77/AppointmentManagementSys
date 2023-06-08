package model;

import helper.Alerts;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.time.*;
import java.time.format.DateTimeFormatter;

/** This class creates and manages Appointment objects and relevant methods. */
public class Appointment {
    int appointmentId;
    String title;
    String description;
    String location;
    String type;
    ZonedDateTime start;
    ZonedDateTime end;
    int customerId;
    int userId;
    int contactId;


    private static ObservableList<Appointment> allAppointments = FXCollections.observableArrayList();
    private static ObservableList<LocalTime> allTimes = FXCollections.observableArrayList();
    private static ObservableList<String> typeTotal = FXCollections.observableArrayList();

    /** This method is the appointment constructor.
     * @param appointmentId The appointment ID being created
     * @param title The title being created
     * @param description The description being created
     * @param location The location being created
     * @param contactId The contact ID being created
     * @param type The type being created
     * @param start The start being created
     * @param end The end being created
     * @param customerId The customer ID being created
     * @param userId The user Id being created
     */
    public Appointment(int appointmentId, String title, String description, String location, int contactId, String type, ZonedDateTime start,
                       ZonedDateTime end, int customerId,
                       int userId) {
        this.appointmentId = appointmentId;
        this.title = title;
        this.description = description;
        this.location = location;
        this.type = type;
        this.start = start;
        this.end = end;
        this.customerId = customerId;
        this.userId = userId;
        this.contactId = contactId;
    }

    /** This method gets the appointment ID.
     * @return Returns the appointment ID
     */
    public int getAppointmentId() {
        return appointmentId;
    }

    /** This method gets the title.
     * @return Returns the title
     */
    public String getTitle() {
        return title;
    }

    /** This method gets the description.
     * @return Returns the description
     */
    public String getDescription() {
        return description;
    }

    /** This method gets the location.
     * @return Returns the location
     */
    public String getLocation() {
        return location;
    }

    /** This method gets the type.
     * @return Returns the type
     */
    public String getType() {
        return type;
    }

    /** This method gets the start time.
     * @return Returns the start time
     */
    public ZonedDateTime getStart() {
        return start;
    }

    /** This method gets the end time.
     * @return Returns the end time
     */
    public ZonedDateTime getEnd() {
        return end;
    }

    /** This method gets the customer ID.
     * @return Returns the customer ID
     */
    public int getCustomerId() {
        return customerId;
    }

    /** This method gets the user ID.
     * @return Returns the user ID
     */
    public int getUserId() {
        return userId;
    }

    /** This method gets the contact ID.
     * @return Returns the contact ID
     */
    public int getContactId() {
        return contactId;
    }

    /** This method sets the appointment ID.
     * @param appointmentId The appointment ID being set
     */
    public void setAppointmentId(int appointmentId) {
        this.appointmentId = appointmentId;
    }
    /** This method sets the title.
     * @param title The title being set
     */
    public void setTitle(String title) {
        this.title = title;
    }
    /** This method sets the description.
     * @param description The description being set
     */
    public void setDescription(String description) {
        this.description = description;
    }
    /** This method sets the location.
     * @param location The location being set
     */
    public void setLocation(String location) {
        this.location = location;
    }
    /** This method sets the type.
     * @param type The type being set
     */
    public void setType(String type) {
        this.type = type;
    }

    /** This method sets the start time.
     * @param start The start time being set
     */
    public void setStart(ZonedDateTime start) {
        this.start = start;
    }

    /** This method sets the end time.
     * @param end The end time being set
     */
    public void setEnd(ZonedDateTime end) {
        this.end = end;
    }

    /** This method sets the customer ID
     * @param customerId The customer ID being set
     */
    public void setCustomerId(int customerId) {
        this.customerId = customerId;
    }

    /** This method sets the user ID
     * @param userId The user ID being set
     */
    public void setUserId(int userId) {
        this.userId = userId;
    }

    /** This method sets the contact ID
     * @param contactId The contact ID being set
     */
    public void setContactId(int contactId) {
        this.contactId = contactId;
    }

    /** This method adds a string to the typeTotal Observable List.
     * @param string The string being added
     */
    public static void addTypeTotal(String string){

        typeTotal.add(string);
    }

    /** This method returns the typeTotal Observable List.
     * @return The typeTotal Observable List of strings
     */
    public static ObservableList<String> getAllTypeTotals(){
        return typeTotal;
    }

    /** This method adds an appointment to the allAppointments Observable List.
     * @param appointment The appointment being added
     */
    public void addAppointment(Appointment appointment){
        allAppointments.add(appointment);
    }

    /** This method returns the allAppointments Observable List.
     * @return The allAppointments Observable List
     */
    public static ObservableList<Appointment> getAllAppointments(){
        return allAppointments;
    }

    /** This method updates a selected appointment.
     * @param newAppointment The appointment being used to update
     * @param index The index of the appointment being updated
     */
    public void updateAppointment(Appointment newAppointment, int index){
        for(Appointment appointment : allAppointments){
            if(allAppointments.indexOf(appointment) == index){
                allAppointments.set(index, newAppointment);
            }
        }
    }

    /**This method deletes an appointment from the allAppointments Observable List.
     * @param appointment The appointment being deleted
     */
    public void deleteAppointment(Appointment appointment){
        allAppointments.remove(appointment);
    }

    /** This method checks if there is an appointment within 15 minutes of the user logging in and sends an alert to the user. */
    public static void appointmentSoonCheck(){
        LocalDateTime systemTime = LocalDateTime.now(ZoneId.systemDefault());
        StringBuilder s = new StringBuilder();
        boolean noAppointments = false;

        for(Appointment appointment : allAppointments){
            LocalDateTime appointmentTime = appointment.getStart().withZoneSameInstant(ZoneId.systemDefault()).toLocalDateTime();

            if(appointment.getUserId() == User.getCurrentUser().getUserId()) {
                if (appointmentTime.isAfter(systemTime) & appointmentTime.isBefore(systemTime.plusMinutes(15))) {
                    s.append("You have an appointment at ").append(appointmentTime.toLocalTime())
                            .append(" with ").append(Customer.findCustomer(appointment)).append(".");

                    noAppointments = false;
                }
                else{
                    noAppointments = true;
                }
            }
        }
        if (noAppointments){
            Alerts.informationAlert("You have no appointments in the next 15 minutes.");

        }
        else{
            Alerts.informationAlert(String.valueOf(s));

        }
    }

    /** This method checks if there are any overlapping appointments before adding an appointment to the database and Observable List and
     * sends an alert to the user if so.
     * @param newAppointment The appointment being checked for overlaps
     * @return Returns true if there is an overlap, false if there is not
     */
    public boolean appointmentOverlapCheck(Appointment newAppointment){
        for(Appointment appointment : allAppointments) {
            ZonedDateTime existingAppointmentStartTime = appointment.getStart();
            ZonedDateTime existingAppointmentEndTime = appointment.getEnd();

            String existingAppointmentStartTimeLocal = existingAppointmentStartTime.withZoneSameInstant(ZoneId.systemDefault()).format(DateTimeFormatter.ofPattern("MM/dd/yyyy H:mm"));
            String existingAppointmentEndTimeLocal = existingAppointmentEndTime.withZoneSameInstant(ZoneId.systemDefault()).format(DateTimeFormatter.ofPattern("H:mm"));

            //to prevent errors when modifying an appointment and disallowing overlapping with itself
            if(appointment.getAppointmentId() != newAppointment.getAppointmentId()) {
                if (appointment.getCustomerId() == newAppointment.getCustomerId()) {

                    if (existingAppointmentStartTime.isEqual(newAppointment.getStart()) || existingAppointmentStartTime.isEqual(newAppointment.getEnd())) {
                        Alerts.errorAlert("An appointment is already scheduled for " + existingAppointmentStartTimeLocal + " to " + existingAppointmentEndTimeLocal + " with " +
                                Customer.findCustomer(appointment) + ".");
                        return true;
                    }
                    if (existingAppointmentEndTime.isEqual(newAppointment.getStart()) || existingAppointmentEndTime.isEqual(newAppointment.getEnd())) {
                        Alerts.errorAlert("An appointment is already scheduled for " + existingAppointmentStartTimeLocal + " to " + existingAppointmentEndTimeLocal + " with " +
                                Customer.findCustomer(appointment) + ".");
                        return true;
                    }

                    if (existingAppointmentStartTime.isAfter(newAppointment.getStart()) & existingAppointmentStartTime.isBefore(newAppointment.getEnd())) {
                        Alerts.errorAlert("An appointment is already scheduled for " + existingAppointmentStartTimeLocal + " to " + existingAppointmentEndTimeLocal + " with " +
                                Customer.findCustomer(appointment) + ".");
                        return true;
                    }
                    if (existingAppointmentEndTime.isAfter(newAppointment.getStart()) & existingAppointmentEndTime.isBefore(newAppointment.getEnd())) {
                        Alerts.errorAlert("An appointment is already scheduled for " + existingAppointmentStartTimeLocal + " to " + existingAppointmentEndTimeLocal + " with " +
                                Customer.findCustomer(appointment) + ".");
                        return true;
                    }
                    if(newAppointment.getStart().isAfter(existingAppointmentStartTime) & newAppointment.getEnd().isBefore(existingAppointmentEndTime)){
                        Alerts.errorAlert("An appointment is already scheduled for " + existingAppointmentStartTimeLocal + " to " + existingAppointmentEndTimeLocal + " with " +
                                Customer.findCustomer(appointment) + ".");
                        return true;
                    }
                    if(newAppointment.getEnd().isAfter(existingAppointmentStartTime) & existingAppointmentEndTime.isBefore(existingAppointmentEndTime)){
                        Alerts.errorAlert("An appointment is already scheduled for " + existingAppointmentStartTimeLocal + " to " + existingAppointmentEndTimeLocal + " with " +
                                Customer.findCustomer(appointment) + ".");
                        return true;
                    }
                }
            }
        }
        return false;
    }

    /** This method checks if an appointment is within the business hours.
     * @param appointment The appointment being checked
     * @return Returns true if the appointment is outside business hours, false if not
     */
    public static boolean appointmentBusinessHoursCheck(Appointment appointment){
        ZonedDateTime appointmentStartTimeEST = appointment.getStart().withZoneSameInstant(ZoneId.of("America/New_York"));
        ZonedDateTime appointmentEndTimeEST = appointment.getEnd().withZoneSameInstant(ZoneId.of("America/New_York"));

        LocalTime openingHour = LocalTime.of(8,00);
        LocalTime closingHour = LocalTime.of(22,0);

        //changes the opening hours to the time of the system default timezone
        LocalTime openingHourLocal = openingHour.atDate(LocalDate.now()).atZone(ZoneId.of("America/New_York")).withZoneSameInstant(ZoneId.systemDefault()).toLocalTime();
        LocalTime closingHourLocal = closingHour.atDate(LocalDate.now()).atZone(ZoneId.of("America/New_York")).withZoneSameInstant(ZoneId.systemDefault()).toLocalTime();

        if(appointmentStartTimeEST.toLocalTime().isBefore(openingHour) || appointmentStartTimeEST.toLocalTime().isAfter(closingHour)){
            Alerts.errorAlert("Appointments must be within the business hours of 8:00 and 22:00 ET (" + openingHourLocal +
                    " - " + closingHourLocal + " " + ZoneId.systemDefault() + ").");
            return true;
        }
        if(appointmentEndTimeEST.toLocalTime().isBefore(openingHour) || appointmentEndTimeEST.toLocalTime().isAfter(closingHour)){
            Alerts.errorAlert("Appointments must be within the business hours of 8:00 and 22:00 ET (" + openingHourLocal +
                    " - " + closingHourLocal + " " + ZoneId.systemDefault() + ") .");
            return true;
        }

        return false;
    }

    /** This method gets all times within the allTimes Observable List.
     * @return Returns the allTimes Observable List
     */
    public static ObservableList<LocalTime> getAllTimes(){
        return allTimes;
    }

    /** This method creates all the times for the application in 15 minute increments. */
    public static void createTimes() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("H:mm");

        for (int i = 0; i < 25; i++) {
            allTimes.add(LocalTime.parse((i + ":00"), formatter));
            for(int k = 0; k < 1; k++){
                if(i < 24){
                    allTimes.add(LocalTime.parse((i + ":15"), formatter));
                    allTimes.add(LocalTime.parse((i + ":30"), formatter));
                    allTimes.add(LocalTime.parse((i + ":45"), formatter));
                }
            }
        }
    }


}
