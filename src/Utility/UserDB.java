package Utility;

import Model.User;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * This class is used for login verification with the database.
 *It also records the username, timestamps, dates and success/failure to the logger class
 */

public class UserDB {

    private static User currentUser;

    public static User getCurrentUser() {
        return currentUser;
    }

    public static Boolean login(String username, String password) {

        try {
            String query = "SELECT * FROM users WHERE User_Name='" + username + "'AND Password='" + password + "'";
            //Connects to DB and executing query for username = DB Column User_Name and same for Password
            ResultSet rs = DBQuery.statement.executeQuery(query);
            //Creates User object and using setters for object
            if (rs.next()) {
                currentUser = new User();
                currentUser.setUserName(rs.getString("User_Name"));
                currentUser.setPassword(rs.getString("Password"));
                Logger.log(username, true);
                return true;
            } else{
                Logger.log(username, false);
                return false;
            }

        }
        catch (SQLException e) {
            System.out.println("SQLException: " + e.getMessage());
            return false;
        }
    }
}
