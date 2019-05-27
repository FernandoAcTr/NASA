package DAO;

import utils.FormatDate;

import java.sql.*;

public class APOD_User_DAO {

    private Connection conn;

    public APOD_User_DAO(Connection conn) {
        this.conn = conn;
    }

    public boolean insertSearch(int id_apod, int id_user) {
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

    public String[][] fetchSearch(String initialDate, String endDate) {
        String select = "SELECT u.id_user, u.username, a.id_apod, a.title, au.date, au.hour" +
                " FROM apod_user au JOIN apod a on au.id_apod = a.id_apod JOIN user u on au.id_user = u.id_user";

        String where = " WHERE au.date BETWEEN '" + initialDate + "' AND '" + endDate + "'";

        if (initialDate == null)
            if (endDate == null)
                where = "";
            else
                where = " WHERE au.date <= '" + endDate + "'";
        else if (endDate == null)
            where = " WHERE au.date >= '" + initialDate + "'";

        String query = select + where;
        String data[][] = null;

        try {
            Statement st = conn.createStatement();
            ResultSet rs = st.executeQuery(query);

            rs.last();
            int rowCount = rs.getRow();
            int colCount = rs.getMetaData().getColumnCount();
            data = new String[rowCount][];

            rs.first();

            for (int row = 0; row < rowCount; row++) {
                data[row] = new String[colCount];

                for (int col = 0; col < colCount; col++) {
                    data[row][col] = rs.getString(col+1);
                }
                rs.next();
            }

            rs.close();
            st.close();

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return data;
    }
}
