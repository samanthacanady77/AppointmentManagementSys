package model;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/** This class creates and manages Division objects and relevant methods. */
public class Division {
    private int divisionId;
    private String division;

    private int countryId;

    private static ObservableList<Division> allDivisions = FXCollections.observableArrayList();

    /** This method is the division constructor.
     * @param divisionId The division ID being created
     * @param division The division being created
     * @param countryId The country ID being created
     */
    public Division(int divisionId, String division, int countryId){
        this.divisionId = divisionId;
        this.division = division;

        this.countryId = countryId;
    }

    /** This method gets the division ID.
     * @return Returns the division ID
     */
    public int getDivisionId() {
        return divisionId;
    }

    /** this method gets the division.
     * @return Returns the division
     */
    public String getDivision() {
        return division;
    }

    /** This method gets the country ID.
     * @return Returns the country ID
     */
    public int getCountryId() {
        return countryId;
    }

    /** This method sets the division ID.
     * @param divisionId The division ID being set
     */
    public void setDivisionId(int divisionId) {
        this.divisionId = divisionId;
    }

    /** This method sets the division.
     * @param division The divison being set
     */
    public void setDivision(String division) {
        this.division = division;
    }

    /** This method sets the country ID.
     * @param countryId The country ID being set
     */
    public void setCountryId(int countryId) {
        this.countryId = countryId;
    }

    /** This method returns the allDivisions Observable List
     * @return Returns allDivisions
     */
    public static ObservableList<Division> getAllDivisions(){
        return allDivisions;
    }

    /** This method adds a division to the allDivisions Observable List.
     * @param division The division being added
     */
    public static void addDivision(Division division){
        allDivisions.add(division);
    }

    /** This method finds a division using a customer.
     * @param customer The customer being used to search
     * @return Returns the division found
     */
    public static Division findDivision(Customer customer){
        for(Division division : allDivisions){
            if(division.getDivisionId() == customer.getDivisionId()){
                return division;
            }
        }
        return null;
    }

    /** This method finds a division using the division ID.
     * @param selectedDivisionId The division ID being used to search
     * @return Returns the division found
     */
    public static String findDivision(int selectedDivisionId){
        for(Division division : allDivisions){
            if(division.getDivisionId() == selectedDivisionId){
                return division.getDivision();
            }
        }
        return null;
    }

    /** This method changes the format of the division string.
     * @return Returns the formatted string
     */
    @Override
    public String toString(){
        return(division);
    }
}
