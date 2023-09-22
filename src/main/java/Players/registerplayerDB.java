/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Players;

import Players.playerinfo_SQL;
import DataBase.Connection;
import com.google.protobuf.Empty;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
/**
 *
 * @author Batuh
 */
public class registerplayerDB extends Connection
{
     public String registerUser(String PlayerName,String Email,String Password,String UniqId) throws SQLException
     {
        connection_open();
        String a = "";
        boolean b = false;
         try {
             if(!PlayerName.isEmpty() && !Email.isEmpty() && !Password.isEmpty() )
             {
                    String selectquery = "SELECT * FROM tblusers Where PlayerName = ? or Email = ?";
                    preparedStatement = connection.prepareStatement(selectquery);
                    preparedStatement.setString(1,PlayerName);  
                    preparedStatement.setString(2,Email);
                    ResultSet rs = preparedStatement.executeQuery();
                    while(rs.next())
                    {
                        b=true;
                    }
                    if(b == true)
                    {
                        a = "there is an account";
                    }
                    else
                    {
                        String hash_sifre = org.apache.commons.codec.digest.DigestUtils.sha256Hex(Password);
                        selectquery = "INSERT INTO tblusers (PlayerName,Email,Password) VALUES(?,?,?)";
                        preparedStatement = connection.prepareStatement(selectquery);
                        preparedStatement.setString(1,PlayerName);
                        preparedStatement.setString(2,Email);
                        preparedStatement.setString(3,hash_sifre);
                        preparedStatement.execute();
                        a = "success";
                    }              
             }
             else{
                a = "Empty"; 
             }
            }catch (Exception e) 
                {
                  a = "error";
                }
        
        conneciton_close();
         return a;

     }
     
     public String loginuser(String PlayerName,String Password,String UniqId) throws SQLException
     {
        connection_open();
        boolean b = false;
        String a = "";
        String uniqID = "";
         try {
                String hash_sifre = org.apache.commons.codec.digest.DigestUtils.sha256Hex(Password);
                String selectquery = "SELECT * FROM tblusers Where PlayerName = ? and Password = ?";
                preparedStatement = connection.prepareStatement(selectquery);
                preparedStatement.setString(1,PlayerName);  
                preparedStatement.setString(2,hash_sifre);
                ResultSet rs = preparedStatement.executeQuery();
                while(rs.next())
                {
                    b=true;
                    uniqID = rs.getString("UniqId");
                }
                if(b == false)
                {
                    a = "fail";
                }
                if(uniqID == null)
                {
                       String selectquery2 = "UPDATE tblusers SET UniqId = ? WHERE PlayerName = ? ";
                       preparedStatement = connection.prepareStatement(selectquery2);
                       preparedStatement.setString(1,UniqId);
                       preparedStatement.setString(2,PlayerName);
                       preparedStatement.execute();
                       a = "success";
                }              
                else
                {
                   a = "dontjoin";
                }
        }catch (Exception e) 
            {
                a="brake";
            }
        
        conneciton_close();
        return a;
     }
    public String loginControl(String UniqId) throws SQLException
     {
        connection_open();
        String a = "";
        boolean b = false;
         try {
                String selectquery = "SELECT * FROM tblusers Where UniqId = ?";
                preparedStatement = connection.prepareStatement(selectquery);
                preparedStatement.setString(1,UniqId);
                ResultSet rs = preparedStatement.executeQuery();
                while(rs.next())
                {
                    b=true;
                    a = rs.getString("PlayerName");
                }
                if(b == false)
                {
                    a = "fail";
                }
                else{
                    a = a;
                }
                }catch (Exception e) 
                {
                  
                }
        
        conneciton_close();
        return a;
     }
    public String QuitUser(String UniqId) throws SQLException
     {
        connection_open();
        String stat="";
        boolean b = false;
         try {
                    String selectquery = "UPDATE tblusers SET UniqId = ? WHERE UniqId = ? ";
                    preparedStatement = connection.prepareStatement(selectquery);
                    preparedStatement.setString(1,null);
                    preparedStatement.setString(2,UniqId);
                    preparedStatement.execute();
                    stat = "success";
                }catch (Exception e) 
                {
                  stat = "fail";
                }
        
        conneciton_close();
        return stat;
     }
    public String DeleteUser(String Name , String Password) throws SQLException
     {
        connection_open();
        String stat="";
        boolean b = false;
         try {
                    String hash_sifre = org.apache.commons.codec.digest.DigestUtils.sha256Hex(Password);
                    String selectquery = "DELETE FROM tblusers WHERE PlayerName = ? AND Password = ? ";
                    preparedStatement = connection.prepareStatement(selectquery);
                    preparedStatement.setString(1,Name);
                    preparedStatement.setString(2,hash_sifre);
                    preparedStatement.execute();
                    stat = "success";
                }catch (Exception e) 
                {
                  stat = "fail";
                }
        
        conneciton_close();
        return stat;
     }
}
