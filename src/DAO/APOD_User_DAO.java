package DAO;

import utils.FormatDate;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class APOD_User_DAO {

    private Connection conn;

    public APOD_User_DAO(Connection conn) {
        this.conn = conn;
    }

    public boolean insertSearch(int id_apod, int id_user){
        String query = "INSERT INTO apod_user (id_user, id_apod, date, hour) VALUES (?,?,?,?)";
        try {
            PreparedStatement st = conn.prepareStatement(query);
            st.setInt(1, id_user);
            st.setInt(2, id_apod);
            st.setString(3, new FormatDate().toString());
            st.setString(4, new FormatDate().getTime());
            st.execute();

            st.close();
            return true;

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }
}
