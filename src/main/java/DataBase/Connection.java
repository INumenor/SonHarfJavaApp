/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DataBase;

import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.Statement;

/**
 *
 * @author ASOS
 */
public class Connection {
    
  

    public java.sql.Connection connection;
    public Statement statement;
    public PreparedStatement preparedStatement;

    public void connection_open() {
//conneciton_close();
        try {
            if (connection == null) {
                Class.forName("com.mysql.jdbc.Driver");
                
                connection = DriverManager.getConnection("jdbc:mysql://appjam.inseres.com:3306/kelimeoyunu?zeroDateTimeBehavior=CONVERT_TO_NULL", "admin", "goktug2020");
                statement = connection.createStatement();
                // System.out.println("Bağlantı Sağlandı");
            } else {
                //   System.out.println("Bağlantı Zaten Açık");
            }
        } catch (Exception e) {
            e.printStackTrace();
//            System.out.println("Open Connection Error" + e.toString());
        }
    }

    public void conneciton_close() {

        try {
            if (statement != null) {
                statement.close();
                statement = null;
            }
            if (connection != null) {
                connection.close();
                connection = null;

            }

        } catch (Exception e) {
            e.printStackTrace();
//            System.out.println("Exception closeConnection" + e.toString());
        }
    }
}
    

