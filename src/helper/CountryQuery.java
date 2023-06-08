package helper;

import model.Country;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;

/** This class creates country SQL queries. */
public class CountryQuery {

    /** This method selects the countries from the database and creates Country objects.
     * @throws SQLException
     */
    public static void select() throws SQLException {
        String sql = "SELECT * FROM COUNTRIES";

        PreparedStatement ps = JDBC.connection.prepareStatement(sql);

        ResultSet rs = ps.executeQuery();

        while (rs.next()) {
            int countryId = rs.getInt("Country_ID");
            String country = rs.getString("Country");

            Country countryObj = new Country(countryId, country);

            countryObj.addCountry(countryObj);

        }
    }
}
