package DAO;

import javafx.scene.image.Image;
import model.APODBean;
import model.MyImage;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.*;

public class APODDAO {

    public static final String COL_ID = "id_apod";
    public static final String COL_TITLE = "title";
    public static final String COL_DATE = "date";
    public static final String COL_IMAGE = "image";
    public static final String COL_IMAGE_CREDIT = "image_credit";
    public static final String COL_EXPLANATION = "explanation";
    public static final String COL_URL = "url";
    public static final String COL_MEDIA_TYPE = "media_type";
    public static final String COL_SERVICE_VERSION = "service_version";

    private Connection conn;

    public APODDAO(Connection conn) {
        this.conn = conn;
    }

    /**
     * Insert an APOD into DB. Image migth be null becouse videos doesn't save yet. Only images
     * @param date
     * @return
     */
    public APODBean getAPODByDate(String date) {
        ResultSet rs;
        APODBean apod = null;
        try {
            String query = "SELECT * FROM apod WHERE date LIKE '" + date + "'";

            Statement st = conn.createStatement();
            rs = st.executeQuery(query);

            if (rs.next()) {
                apod = new APODBean();
                apod.setId(rs.getInt(COL_ID));
                apod.setTitle(rs.getString(COL_TITLE));
                apod.setExplanation(rs.getString(COL_EXPLANATION));
                apod.setCopyright(rs.getString(COL_IMAGE_CREDIT));
                apod.setDate(rs.getDate(COL_DATE).toString());
                apod.setMedia_type(rs.getString(COL_MEDIA_TYPE));
                apod.setUrl(rs.getString(COL_URL));
                apod.setService_version(rs.getString(COL_SERVICE_VERSION));

                Blob blobImage = rs.getBlob(COL_IMAGE);
                // blob puede venir nulo porque los videos no se guardan. Por el momento solo imagenes
                if (blobImage != null)
                    apod.setImage(new MyImage(new Image(blobImage.getBinaryStream())));
            }

            rs.close();
            st.close();

        } catch (SQLException ex) {
            ex.printStackTrace();
            System.out.println("Error al recuperar información...");
        }

        return apod;
    }

    public boolean insertAPOD(APODBean apod) {
        String query = "INSERT INTO apod(id_apod, title, date, image, image_credit, explanation, url, media_type, service_version) " +
                "VALUES(?,?,?,?,?,?,?,?,?)";
        try {
            PreparedStatement ps = conn.prepareStatement(query);
            FileInputStream fisImage = null;

            int id = getNextID();

            ps.setInt(1, id);
            ps.setString(2, apod.getTitle());
            ps.setDate(3, Date.valueOf(apod.getDate()));
            ps.setString(5, apod.getCopyright());
            ps.setString(6, apod.getExplanation());
            ps.setString(7, apod.getUrl());
            ps.setString(8, apod.getMedia_type());
            ps.setString(9, apod.getService_version());

            if (apod.getImage().getImageFile() != null) {
                fisImage = new FileInputStream(apod.getImage().getImageFile());
                ps.setBinaryStream(4, fisImage);
            }else
                ps.setBinaryStream(4, null);

            ps.execute();

            ps.close();
            if (fisImage != null)
                fisImage.close();

            return true;
        } catch (SQLException | FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return false;
    }

    public int getNextID(){
        String query = "SELECT MAX(id_apod) as next FROM apod;";
        try {
            Statement st = conn.createStatement();
            ResultSet rs = st.executeQuery(query);

            if(rs.next())
                return rs.getInt("next") + 1;

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return 1;
    }
}
