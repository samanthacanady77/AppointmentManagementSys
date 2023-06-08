package helper;

import model.Customer;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/** This class creates customer SQL queries. */
public class CustomerQuery {
    /** This method inserts a new customer into the database.
     * @param customerId Customer ID being insert
     * @param customerName Customer name being insert
     * @param address Address being insert
     * @param postalCode Postal code being insert
     * @param phone Phone being insert
     * @param divisionId Division ID being insert
     * @throws SQLException
     */
    public static void insert(int customerId, String customerName, String address, String postalCode, String phone, int divisionId) throws SQLException {
        String sql = "INSERT INTO CUSTOMERS (Customer_ID, Customer_Name, Address, Postal_Code, Phone, Division_ID)" +
                " VALUES (?, ?, ?, ?, ?, ?)";

        PreparedStatement ps = JDBC.connection.prepareStatement(sql);
        ps.setInt(1,customerId);
        ps.setString(2,customerName);
        ps.setString(3, address);
        ps.setString(4,postalCode);
        ps.setString(5, phone);
        ps.setInt(6, divisionId);

        ps.executeUpdate();
    }

    /** This method updates and existing customer record in the database.
     * @param customerId Customer ID being updated
     * @param customerName Customer Name being updated
     * @param address Address being updated
     * @param postalCode Postal Code being updated
     * @param phone Phone being updated
     * @param divisionId Division ID being updated
     * @throws SQLException
     */
    public static void update(int customerId, String customerName, String address, String postalCode, String phone, int divisionId) throws SQLException{
        String sql = "UPDATE CUSTOMERS SET Customer_Name = ?, Address = ?, Postal_Code = ?, Phone = ?, Division_ID = ? WHERE Customer_ID = ?";

        PreparedStatement ps = JDBC.connection.prepareStatement(sql);

        ps.setString(1,customerName);
        ps.setString(2, address);
        ps.setString(3,postalCode);
        ps.setString(4, phone);
        ps.setInt(5, divisionId);
        ps.setInt(6,customerId);

    }

    /** This method deletes an existing customer from the database.
     * @param customerId Customer ID being used to search for the customer
     * @throws SQLException
     */
    public static void delete(int customerId) throws SQLException {

        String sql = "DELETE FROM CUSTOMERS WHERE Customer_ID = ? ";

        PreparedStatement ps = JDBC.connection.prepareStatement(sql);

        ps.setInt(1, customerId);

        ps.executeUpdate();
    }

    /** This method selects all the customer records from the database and creates Customer objects.
     * @throws SQLException
     */
    public static void select() throws SQLException{
        String sql = "SELECT * FROM CUSTOMERS";

        PreparedStatement ps = JDBC.connection.prepareStatement(sql);

        ResultSet rs = ps.executeQuery();

        while(rs.next()){
            int customerId = rs.getInt("Customer_ID");
            String customerName = rs.getString("Customer_Name");
            String address = rs.getString("Address");
            String postalCode = rs.getString("Postal_Code");
            String phone = rs.getString("Phone");
            int divisionId = rs.getInt("Division_ID");

            Customer customer = new Customer(customerId, customerName, address, postalCode, phone, divisionId);

            customer.addCustomer(customer);

        }

    }
}
