package model;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/** This class creates and manages Country objects and relevant methods. */
public class Country {
    int countryId;
    String country;


    private static ObservableList<Country> allCountries = FXCollections.observableArrayList();
    private ObservableList<Division> allAssociatedDivisions = FXCollections.observableArrayList();

    /** This is the country constructor.
     * @param countryId The country ID being created
     * @param country The country being created
     */
    public Country(int countryId, String country) {
        this.countryId = countryId;
        this.country = country;
    }

    /** This method gets the country ID.
     * @return Returns the country ID
     */
    public int getCountryId() {
        return countryId;
    }

    /** This method gets the country.
     * @return Returns the country
     */
    public String getCountry() {
        return country;
    }

    /** This method sets the country ID.
     * @param countryId The country ID being set
     */
    public void setCountryId(int countryId) {
        this.countryId = countryId;
    }

    /** This method sets the country.
     * @param country The country being set
     */
    public void setCountry(String country) {
        this.country = country;
    }

    /** This method adds a country to the allCountries Observable List.
     * @param country The country being added
     */
    public void addCountry(Country country){
        allCountries.add(country);
    }

    /** This method returns the allCountries Observable List.
     * @return Returns allCountries
     */
    public static ObservableList<Country> getAllCountries(){
        return allCountries;
    }

    /**This method adds a division to the allAssociatedDivision Observable List.
     * @param division The divison being added
     */
    public void addAssociatedDivision(Division division){
        allAssociatedDivisions.add(division);
    }

    /** This method returns allAssociatedDivisions.
     * @return
     */
    public ObservableList<Division> getAllAssociatedDivisions(){
        return allAssociatedDivisions;
    }

    /** This method finds the associated divisions for a country. It then adds them to allAssociatedDivisions if the list is empty and clears
     * the list before adding if not.
     * @param country The country being used to search
     */
    public void findAssociatedDivisions(Country country){
        if(allAssociatedDivisions.size() == 0) {
            for (Division division : Division.getAllDivisions()) {
                if (country.getCountryId() == division.getCountryId()) {
                    allAssociatedDivisions.add(division);
                }
            }
        }
        else{
            allAssociatedDivisions.clear();
            for (Division division : Division.getAllDivisions()) {
                if (country.getCountryId() == division.getCountryId()) {
                    allAssociatedDivisions.add(division);
                }
            }
        }
    }

    /** This method finds a country using a customer.
     * @param customer The customer being used to search
     * @return Returns the country found
     */
    public static Country findCountry(Customer customer){
        for(Division division : Division.getAllDivisions()){
            if(division.getDivisionId() == customer.getDivisionId()){
                for(Country country : allCountries){
                    if(country.getCountryId() == division.getCountryId()){
                        return country;
                    }
                }
            }
        }
        return null;
    }

    /** This method changes the format of country strings.
     * @return The country string in the new format
     */
    @Override
    public String toString(){
        return(country);
    }
}
