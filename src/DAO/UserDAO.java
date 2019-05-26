package DAO;

import javafx.scene.image.Image;
import model.MyImage;
import model.User;
import org.apache.commons.codec.digest.DigestUtils;

import java.io.ByteArrayInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.*;

public class UserDAO {

    private Connection conn;

    public static final String COL_ID = "id_user";
    public static final String COL_USERNAME = "username";
    public static final String COL_PASSWORD = "password";
    public static final String COL_IMAGE_PROFILE = "image_profile";
    public static final String COL_IMAGE_COVER = "image_cover";
    public static final String COL_TYPE = "type";

    public UserDAO(Connection conn)
    {
        this.conn = conn;
    }

    /**
     * Check if exist an user with username and password like the params
     * @param username
     * @param password
     * @return an User with all atributes except password
     */
    public User validUser(String username, String password) {
        ResultSet rs = null;
        User u = null;

        try {
            String query = "SELECT * FROM user where username = '" + username + "' AND password = md5('" + password + "')";

            Statement st = conn.createStatement();
            rs = st.executeQuery(query);

            if(rs.next()) {
                String typeUser = rs.getString(COL_TYPE);
                Blob imageProfile = rs.getBlob(COL_IMAGE_PROFILE);
                Blob imageCover = rs.getBlob(COL_IMAGE_COVER);

                /*byte[] dataImage = imageProfile.getBytes(1, (int)imageProfile.length());
                Image image = new Image(new ByteArrayInputStream(dataImage));*/

                u = new User();
                u.setImageCover(new MyImage(new Image(imageCover.getBinaryStream())));
                u.setImageProfile(new MyImage(new Image(imageProfile.getBinaryStream())));
                u.setUsername(username);
                u.setType(typeUser);
            }

            rs.close();
            st.close();

        } catch (SQLException ex) {
            ex.printStackTrace();
            System.out.println("Error al recuperar información...");
        }

        return  u;
    }

    public boolean insertUser(User user){
        String query = "INSERT INTO user(username, password, image_profile, image_cover, type) VALUES(?,?,?,?,?)";
        try {
            PreparedStatement ps = conn.prepareStatement(query);

            FileInputStream fisProfile = new FileInputStream(user.getImageProfile().getImageFile());
            FileInputStream fisCover = new FileInputStream(user.getImageCover().getImageFile());

            ps.setString(1, user.getUsername());
            ps.setString(2, user.getPassword());
            ps.setBinaryStream(3, fisProfile);
            ps.setBinaryStream(4, fisCover);
            ps.setString(5, user.getType());
            ps.execute();

            ps.close();
            fisCover.close();
            fisProfile.close();

            return true;
        } catch (SQLException | FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return false;
    }

    /**
     * verufy if doesn't exist the username yet
     * @return true if exist. False if doesn't already exist
     */
    public boolean existUser(String username){
        boolean exist = false;
        String query = "SELECT username FROM user where username = '" + username +"'";
        try {
            Statement statement = conn.createStatement();
            ResultSet rs = statement.executeQuery(query);

            if(rs.next())
                exist = true;

            rs.close();
            statement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return exist;
    }

    public String encryptPassword(String pass){
        return DigestUtils.md5Hex(pass);
    }
}
