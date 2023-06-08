package helper;

import model.Appointment;
import model.Contact;
import model.Customer;


import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.ZoneId;

import static helper.JDBC.connection;

/** This class creates appointment SQL queries. */
public class AppointmentQuery {

    /** This method inserts a new appointment into the database.
     * @param appointmentId Appointment ID being insert
     * @param title Title being insert
     * @param description Description being insert
     * @param location Location being insert
     * @param contactId Contact Id being insert
     * @param type Type being insert
     * @param start Start being insert
     * @param end End being insert
     * @param customerId Customer ID insert
     * @param userId User ID being insert
     * @throws SQLException
     */
    public static void insert(int appointmentId, String title, String description, String location, int contactId, String type, Timestamp start,
                             Timestamp end, int customerId, int userId) throws SQLException {
        String sql = "INSERT INTO APPOINTMENTS (Appointment_ID, Title, Description, Location, Contact_ID, Type, Start, End, " +
                " Customer_ID, User_ID)" +
                " VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";


        PreparedStatement ps = connection.prepareStatement(sql);


        ps.setInt(1, appointmentId);
        ps.setString(2, title);
        ps.setString(3, description);
        ps.setString(4, location);
        ps.setInt(5, contactId);
        ps.setString(6, type);
        ps.setTimestamp(7, start);
        ps.setTimestamp(8, end);
        ps.setInt(9, customerId);
        ps.setInt(10, userId);

        ps.executeUpdate();

    }

    /** This method updates an existing appointment in the database.
     * @param appointmentId Appointment ID being updated
     * @param title Title being updated
     * @param description Description being updated
     * @param location Location being updated
     * @param contactId Contact ID being updated
     * @param type Type being updated
     * @param start Start being updated
     * @param end End being updated
     * @param customerId Customer ID being updated
     * @param userId User ID being updated
     * @throws SQLException
     */
    public static void update(int appointmentId, String title, String description, String location, int contactId, String type, Timestamp start,
                             Timestamp end, int customerId, int userId) throws SQLException {
        String sql = "UPDATE APPOINTMENTS SET Title = ?, Description = ?, Location = ?, Contact_ID = ?, Type = ?, Start = ?, " +
                "End = ?, User_ID = ?, Customer_ID = ? WHERE Appointment_ID = ?";

        PreparedStatement ps = connection.prepareStatement(sql);

        ps.setString(1, title);
        ps.setString(2, description);
        ps.setString(3, location);
        ps.setInt(4, contactId);
        ps.setString(5, type);
        ps.setTimestamp(6, start);
        ps.setTimestamp(7, end);
        ps.setInt(8, userId);
        ps.setInt(9, customerId);
        ps.setInt(10, appointmentId);

        ps.executeUpdate();
    }

    /** This method deletes an appointment in the database.
     * @param appointmentId Appointment ID being deleted
     * @throws SQLException
     */
    public static void delete(int appointmentId) throws SQLException {

        String sql = "DELETE FROM APPOINTMENTS WHERE Appointment_ID = ? ";

        PreparedStatement ps = connection.prepareStatement(sql);

        ps.setInt(1, appointmentId);

        ps.executeUpdate();

    }

    /** This method gets existing appointment records from the database and creates Appointment objects with them.
     * @throws SQLException
     */
    public static void select() throws SQLException {
        String sql = "SELECT * FROM APPOINTMENTS";

        PreparedStatement ps = connection.prepareStatement(sql);

        ResultSet rs = ps.executeQuery();

        while (rs.next()) {
            int appointmentId = rs.getInt("Appointment_ID");
            String title = rs.getString("Title");
            String description = rs.getString("Description");
            String location = rs.getString("Location");
            int contactId = rs.getInt("Contact_ID");
            String type = rs.getString("Type");
            Timestamp start = rs.getTimestamp("Start");
            Timestamp end = rs.getTimestamp("End");
            int customerId = rs.getInt("Customer_ID");
            int userId = rs.getInt("User_ID");


            Appointment appointment = new Appointment(appointmentId, title, description, location, contactId, type,
                    start.toLocalDateTime().atZone(ZoneId.systemDefault()).withZoneSameInstant(ZoneId.of("UTC")),
                    end.toLocalDateTime().atZone(ZoneId.systemDefault()).withZoneSameInstant(ZoneId.of("UTC")), customerId, userId);

            appointment.addAppointment(appointment);
            Customer.findCustomer(appointment).addAssociatedAppointment(appointment);
        }
    }

    /** This method selects all appointments associated with a particular contact.
     * @param contact The contact whose appointments are being searched for
     * @throws SQLException
     */
    public static void select(Contact contact) throws SQLException {
        String sql = "SELECT * FROM APPOINTMENTS WHERE CONTACT_ID = ?";

        PreparedStatement ps = connection.prepareStatement(sql);

        ResultSet rs = ps.executeQuery();

        while (rs.next()) {
            int appointmentId = rs.getInt("Appointment_ID");
            String title = rs.getString("Title");
            String description = rs.getString("Description");
            String location = rs.getString("Location");
            int contactId = rs.getInt("Contact_ID");
            String type = rs.getString("Type");
            Timestamp start = rs.getTimestamp("Start");
            Timestamp end = rs.getTimestamp("End");
            int customerId = rs.getInt("Customer_ID");
            int userId = rs.getInt("User_ID");


            Appointment appointment = new Appointment(appointmentId, title, description, location, contactId, type,
                    start.toLocalDateTime().atZone(ZoneId.systemDefault()).withZoneSameInstant(ZoneId.of("UTC")),
                    end.toLocalDateTime().atZone(ZoneId.systemDefault()).withZoneSameInstant(ZoneId.of("UTC")), customerId, userId);
        }
    }

    /** This method finds the totals of appointments and groups them by type and month.
     * @throws SQLException
     */
    public static void totalByType() throws SQLException {
        String sql = "SELECT MonthName(Start) Month, Year(Start) Year, Type, COUNT(Appointment_ID) as Totals FROM appointments GROUP BY " +
                "EXTRACT(YEAR_MONTH FROM Start), Type ORDER BY Year(Start), Month(Start) asc";


        PreparedStatement ps = connection.prepareStatement(sql);
        ResultSet rs = ps.executeQuery();
        while (rs.next()) {

            String month = rs.getString("Month");
            String year = rs.getString("Year");
            String type = rs.getString("Type");
            int total = rs.getInt("Totals");


            Appointment.addTypeTotal(month + " " + year + " - " + type + ": " + total);
        }
    }
}