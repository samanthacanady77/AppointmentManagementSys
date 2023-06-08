package helper;

import model.Division;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/** This class creates division SQL queries. */
public class DivisionQuery {
    /** This method selects all division records from the database and creates Division objects.
     * @throws SQLException
     */
    public static void select() throws SQLException {
        String sql = "SELECT * FROM FIRST_LEVEL_DIVISIONS";

        PreparedStatement ps = JDBC.connection.prepareStatement(sql);

        ResultSet rs = ps.executeQuery();

        while (rs.next()) {
            int divisionId = rs.getInt("Division_ID");
            String division = rs.getString("Division");
            int countryId = rs.getInt("Country_ID");

            Division divisionObj = new Division(divisionId, division, countryId);

            divisionObj.addDivision(divisionObj);
        }
    }
}
