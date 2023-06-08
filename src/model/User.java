package model;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/** This method creates and manages user objects. */
public class User {

    private int userId;
    private String username;
    private String password;
    private static User currentUser;

    private static ObservableList<User> allUsers = FXCollections.observableArrayList();

    /** This method is the User constructor
     * @param userId The user ID being created
     * @param username The username being created
     * @param password The password being created
     */
    public User(int userId, String username, String password) {
        this.userId = userId;
        this.username = username;
        this.password = password;
    }

    /** This method sets the user ID.
     * @param userId The user ID being set
     */
    public void setUserId(int userId){
        this.userId = userId;
    }

    /** This method sets the username.
     * @param username The username being set
     */
    public void setUsername(String username){
        this.username = username;
    }

    /** This method sets the pasword.
     * @param password The password being set
     */
    public void setPassword(String password){
        this.password = password;
    }

    /** This method sets the current user.
     * @param currentUserInput The current user being set
     */
    public static void setCurrentUser(User currentUserInput){
        currentUser = currentUserInput;
    }

    /** This method gets the user ID.
     * @return Returns user ID
     */
    public int getUserId(){
        return userId;
    }

    /** This method gets the username.
     * @return Returns username
     */
    public String getUsername(){
        return username;
    }

    /** This method gets the password.
     * @return Returns password
     */
    public String getPassword(){
        return password;
    }

    /** This method gets the current user.
     * @return Returns current user
     */
    public static User getCurrentUser(){
        return currentUser;
    }

    /** This method adds a user to the allUsers Observable List.
     * @param user The user being added
     */
    public void addUser(User user){
        allUsers.add(user);
    }

    /** This returns the allUsers Observable List.
     * @return Returns allUsers
     */
    public static ObservableList<User> getAllUsers(){
        return allUsers;
    }

    /** This method finds a user using an appointment.
     * @param appointment The appointment being used to search
     * @return Returns the user found
     */
    public User findUser(Appointment appointment){
        for(User user : allUsers){
            if(user.getUserId() == appointment.getUserId()){
                return user;
            }
        }
        return null;
    }

    /** This method changes the format for the user string.
     * @return Returns the formatted string
     */
    @Override
    public String toString(){
        return(username + " (ID: " + userId + ")");
    }
}
