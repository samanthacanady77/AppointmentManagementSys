package helper;

import model.Contact;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/** This class creates contact SQL queries. */
public class ContactQuery {

    /** This method inserts a new contact into the database.
     * @param contactName
     * @param email
     * @throws SQLException
     */
    public static void insert( String contactName, String email ) throws SQLException {
        String sql = "INSERT INTO CONTACTS (Contact_Name, Email) VALUES (?, ?)";
        PreparedStatement ps = JDBC.connection.prepareStatement(sql);
        ps.setString(1,contactName);
        ps.setString(2,email);

    }

    /** This method deletes a contact in the database.
     *@param contactId Contact ID being deleted
     * @throws SQLException
     */
    public static void delete(int contactId) throws SQLException {
        String sql = "DELETE FROM CONTACTS WHERE Contact_ID = ? ";

        PreparedStatement ps = JDBC.connection.prepareStatement(sql);

        ps.setInt(1, contactId);
    }

    /** This method selects all the contacts from the database and creates Contact objects.
     * @throws SQLException
     */
    public static void select() throws SQLException {
        String sql = "SELECT * FROM CONTACTS";

        PreparedStatement ps = JDBC.connection.prepareStatement(sql);

        ResultSet rs = ps.executeQuery();

        while(rs.next()) {
            int contactId = rs.getInt("Contact_ID");
            String contactName = rs.getString("Contact_Name");
            String email = rs.getString("Email");

            Contact contact = new Contact(contactId, contactName, email);

            contact.addContact(contact);

        }

    }
}

