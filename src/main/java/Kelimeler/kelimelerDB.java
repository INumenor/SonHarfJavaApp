/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Kelimeler;

import DataBase.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

/**
 *
 * @author Goktug
 */
public class kelimelerDB  extends Connection{
    public Boolean checkWord(String word) throws SQLException {
        Boolean b=false;
        connection_open();
        
        String selectquery = "select * from tblwords2 where name = ?";
        preparedStatement = connection.prepareStatement(selectquery);
        preparedStatement.setString(1,word);
      
        ResultSet rs = preparedStatement.executeQuery();
        
        while (rs.next()) {
            b=true;
        }
        conneciton_close();
        return b;
    }
    
    public Boolean isWordUsed(String word,String roomKey) throws SQLException {
        Boolean b=false;
        connection_open();
        int key = Integer.parseInt(roomKey);
        String selectquery = "select id from tblusedwords where word = ? and roomKey = ?";
        preparedStatement = connection.prepareStatement(selectquery);
        preparedStatement.setString(1,word);
        preparedStatement.setInt(2,key);
      
        ResultSet rs = preparedStatement.executeQuery();
        
        while (rs.next()) {
            b=true;
        }
        conneciton_close();
        return b;
    }
    
    public String getRandomWord() throws SQLException {
        connection_open();
        String word="";
        boolean a=true;
        while(a){
            String selectquery = "select * from tblwords2 ORDER BY RAND() LIMIT 1";
            preparedStatement = connection.prepareStatement(selectquery);
            ResultSet rs = preparedStatement.executeQuery();
            ArrayList<String> list = new ArrayList<>();
            while (rs.next()) {
                word=rs.getString("name");
            }
            if(!word.substring(word.length()-1).equals("ğ")){
                a=false;
            }
        }
        conneciton_close();
        return word;
    }
    
    public String SingleGameGetWord(String getword) throws SQLException {
        connection_open();
        String word="";
        String lastChar = getword.substring(getword.length() - 1);
        boolean a=true;
        while(a){
            String selectquery = "SELECT * FROM tblwords2 WHERE SUBSTRING(name, 1, 1) = ? ORDER BY RAND() LIMIT 1";
            preparedStatement = connection.prepareStatement(selectquery);
            preparedStatement.setString(1, lastChar);
            ResultSet rs = preparedStatement.executeQuery();
            ArrayList<String> list = new ArrayList<>();
            while (rs.next()) {
                word=rs.getString("name");
            }
            if(!word.substring(word.length()-1).equals("ğ")){
                a=false;
            }
        }
        conneciton_close();
        return word;
    }
    
    
    public ArrayList<String> getWord(String word) throws SQLException {
        connection_open();
        
        String firstLetter = word.substring(0, 1);
        
        String selectquery = "select * from tblwords2 where name LIKE 'k%'";
        preparedStatement = connection.prepareStatement(selectquery);
        //preparedStatement.setString(1,firstLetter);
      
        ResultSet rs = preparedStatement.executeQuery();
        ArrayList<String> list = new ArrayList<>();
        while (rs.next()) {
            list.add(rs.getString("name"));
        }
        conneciton_close();
        return list;
    }
    
    public JSONObject getRoomsWord(String room_key) throws SQLException {
        connection_open();
        JSONObject obj = new JSONObject();
        try {
            String selectquery = "SELECT * from tblusedwords WHERE roomKey = ? ORDER BY tour DESC LIMIT 1";
            preparedStatement = connection.prepareStatement(selectquery);
            preparedStatement.setString(1,room_key);
            ResultSet rs = preparedStatement.executeQuery();
            if(rs.next()){
                obj.put("word",rs.getString("word"));
                obj.put("tour",rs.getInt("tour"));
            }
            
        } catch (Exception e) {
        }
        conneciton_close();
        return obj;
    }
    
    public void updateRoomsWord(String room_key,String word,String player,int tour) throws SQLException {
        connection_open();

        try {
            int itour = tour;
            String selectquery = "INSERT INTO tblusedwords (roomKey,word,player,tour) VALUES(?,?,?,?)";
            preparedStatement = connection.prepareStatement(selectquery);
            preparedStatement.setString(1,room_key);
            preparedStatement.setString(2,word);
            preparedStatement.setString(3,player);
            preparedStatement.setInt(4,itour);
            preparedStatement.execute();
            
        } catch (Exception e) {
            System.out.println("hataa update rooms word");
        }
        
        conneciton_close();

    }
}
