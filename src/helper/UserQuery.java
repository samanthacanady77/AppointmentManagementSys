package helper;

import model.User;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/** This class creates user SQL queries. */
public class UserQuery {

    /** This method selects all user records from the database and creates User objects.
     * @throws SQLException
     */
    public static void select() throws SQLException {

        String sql = "SELECT * FROM USERS";

        PreparedStatement ps = JDBC.connection.prepareStatement(sql);

        ResultSet rs = ps.executeQuery();

        while (rs.next()) {
            int userId = rs.getInt("User_ID");
            String userName = rs.getString("User_Name");
            String password = rs.getString("Password");

            User user = new User(userId, userName, password);

            user.addUser(user);

        }
    }
}
