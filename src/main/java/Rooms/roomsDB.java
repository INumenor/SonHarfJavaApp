/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Rooms;

import DataBase.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import org.json.simple.JSONObject;
import sun.security.util.Debug;

/**
 *
 * @author Goktug
 */
public class roomsDB extends Connection{
    public String createRoom(String roomName,String room_key,String player_name,boolean isPrivate,boolean isTimeOpen,int NumberOfPeople,boolean PointType,int Time) throws SQLException {
        connection_open();
        String status = "fail";
        String selectquery1 = "SELECT * FROM tblonlinerooms JOIN tblbotrooms WHERE tblonlinerooms.roomKey = ? OR tblbotrooms.RoomKey = ?";
        preparedStatement = connection.prepareStatement(selectquery1);
        preparedStatement.setString(1,room_key);
        preparedStatement.setString(2,room_key);
        ResultSet rs = preparedStatement.executeQuery();
        boolean isCreated = false;
            if(rs.next())
            {
                isCreated = true;
            }
        if(isCreated == false)
        {
        String selectquery = "INSERT INTO tblonlinerooms (roomName,roomKey,players,playersTurn,isGameStarted,tour,isPrivate,isTimeOpen,NumberOfPeople,PointType,Time,isActive) VALUES(?,?,?,?,false,0,?,?,?,?,?,1)";
        preparedStatement = connection.prepareStatement(selectquery);
        preparedStatement.setString(1,roomName);
        preparedStatement.setString(2,room_key);
        preparedStatement.setString(3,player_name);
        preparedStatement.setString(4,player_name);
        preparedStatement.setBoolean(5,isPrivate);
        preparedStatement.setBoolean(6,isTimeOpen);
        preparedStatement.setInt(7,NumberOfPeople);
        preparedStatement.setBoolean(8,PointType);
        preparedStatement.setInt(9,Time);
        preparedStatement.execute();
        //ResultSet rs = preparedStatement.executeQuery();
        status = "success";
        }
        else
        {
            status = "Created or Empty";
        }
        conneciton_close();
        return status;
    }
    
    public String joinRoom(String room_key,String player_name) throws SQLException {
        String status="fail";
        String[] arr = null;
        
        connection_open();
        
        String players="";
        String selectquery = "SELECT * from tblonlinerooms WHERE roomKey = ? ";
        preparedStatement = connection.prepareStatement(selectquery);
        preparedStatement.setString(1,room_key);
        ResultSet rs = preparedStatement.executeQuery();
        if (rs.next()) 
        {   
//            if(!rs.getBoolean("isGameStarted")){
               players = rs.getString("players");
               arr = rs.getString("players").split(";;;");
//               for(int i=0; i<arr.length;i++){
//                if(arr[i].equals(player_name)){
//                    status = "playerExist";
//                    System.out.println("playerexist");
//                }
//                } 
//            }else{
//                status= "gameWasStarted";
//            }
//        }else{
//            status= "roomNotFound";
            System.out.println(players);
        }
//        if(status!="playerExist" && status!="roomNotFound"){
            players+=";;;"+player_name;
            String selectquery2 = "UPDATE tblonlinerooms SET players = ? WHERE roomKey = ? ";
            preparedStatement = connection.prepareStatement(selectquery2);
            preparedStatement.setString(1,players);
            preparedStatement.setString(2,room_key);
            preparedStatement.execute();
            status="success";
//        }
        conneciton_close();
        return status;
    }
    
    public ArrayList<JSONObject> getOpenRooms() throws SQLException
    {
        connection_open();
        ArrayList<JSONObject> arr = new ArrayList<JSONObject>();
        
        String selectquery = "SELECT * from tblonlinerooms Where isActive = 1";
        preparedStatement = connection.prepareStatement(selectquery);
        ResultSet rs = preparedStatement.executeQuery();
        while(rs.next())
        {
            JSONObject obj = new JSONObject();
            obj.put("RoomName",rs.getString("roomName"));
            obj.put("RoomKey",rs.getString("roomKey"));
            obj.put("Players",rs.getString("players"));
            obj.put("isGameStarted",rs.getBoolean("isGameStarted"));
            obj.put("isPrivate",rs.getBoolean("isPrivate"));
            obj.put("NumberOfPeople",rs.getInt("NumberOfPeople"));
            obj.put("PointType",rs.getBoolean("PointType"));
            obj.put("Time",rs.getInt("Time"));
            obj.put("isActive",rs.getBoolean("isActive"));
            String[] players;
            players = rs.getString("players").split(";;;");
            obj.put("kisiSayisi", players.length);
            arr.add(obj);
        }
        
        conneciton_close();
        return arr;
    }
    public ArrayList<JSONObject> getActiveRooms(String PlayerName) throws SQLException
    {
        connection_open();
        ArrayList<JSONObject> arr = new ArrayList<JSONObject>();
        
        String selectquery = "SELECT * from tblonlinerooms Where isActive = 1 AND isGameStarted = 1";
        preparedStatement = connection.prepareStatement(selectquery);
        ResultSet rs = preparedStatement.executeQuery();
        while(rs.next())
        {
            JSONObject obj = new JSONObject();
            String[] players;
            players = rs.getString("players").split(";;;");
            for(int i = 0;i<players.length;i++)
            {
             if(players[i].equals(PlayerName))
             {
                obj.put("kisiSayisi", players.length);
                obj.put("RoomName",rs.getString("roomName"));
                obj.put("RoomKey",rs.getString("roomKey"));
                obj.put("Players",rs.getString("players"));
                obj.put("isGameStarted",rs.getBoolean("isGameStarted"));
                obj.put("isPrivate",rs.getBoolean("isPrivate"));
                obj.put("NumberOfPeople",rs.getInt("NumberOfPeople"));
                obj.put("PointType",rs.getBoolean("PointType"));
                obj.put("Time",rs.getInt("Time"));
                obj.put("isActive",rs.getBoolean("isActive"));
                arr.add(obj);
             }   
            } 
        }
        
        conneciton_close();
        return arr;
    }
    
    public ArrayList<JSONObject> getSearchRooms(String RoomName) throws SQLException
    {
        connection_open();
        ArrayList<JSONObject> arr = new ArrayList<JSONObject>();
        String selectquery = "SELECT * FROM tblonlinerooms WHERE roomName LIKE ? ";
        preparedStatement = connection.prepareStatement(selectquery);
        preparedStatement.setString(1,"%"+RoomName+"%");
        ResultSet rs = preparedStatement.executeQuery();
        while(rs.next())
        {
            JSONObject obj = new JSONObject();
            obj.put("RoomName",rs.getString("roomName"));
            obj.put("RoomKey",rs.getString("roomKey"));
            obj.put("Players",rs.getString("players"));
            obj.put("isGameStarted",rs.getBoolean("isGameStarted"));
            obj.put("isPrivate",rs.getBoolean("isPrivate"));
            obj.put("NumberOfPeople",rs.getInt("NumberOfPeople"));
            obj.put("PointType",rs.getBoolean("PointType"));
            obj.put("Time",rs.getInt("Time"));
            obj.put("isActive",rs.getBoolean("isActive"));
            String[] players;
            players = rs.getString("players").split(";;;");
            obj.put("kisiSayisi", players.length);
            arr.add(obj);
        }
        
        conneciton_close();
        return arr;
    }
    
    public JSONObject getRoomSettingsInfo(String RoomKey) throws SQLException
    {
        connection_open();
        JSONObject obj = new JSONObject();
        
        String selectquery = "SELECT * from tblonlinerooms Where roomKey = ? ";
        preparedStatement = connection.prepareStatement(selectquery);
        preparedStatement.setString(1,RoomKey);
        ResultSet rs = preparedStatement.executeQuery();
        while(rs.next())
        {
            obj.put("RoomName",rs.getString("roomName"));
            obj.put("RoomKey",rs.getString("roomKey"));
            obj.put("Players",rs.getString("players"));
            obj.put("isGameStarted",rs.getBoolean("isGameStarted"));
            obj.put("isPrivate",rs.getBoolean("isPrivate"));
            obj.put("NumberOfPeople",rs.getInt("NumberOfPeople"));
            obj.put("PointType",rs.getBoolean("PointType"));
            obj.put("Time",rs.getInt("Time"));
            obj.put("isActive",rs.getBoolean("isActive"));
            String[] players;
            players = rs.getString("players").split(";;;");
            obj.put("kisiSayisi", players.length);
        }
        
        conneciton_close();
        return obj;
    }
    
    public JSONObject getRoomInfo(String room_key,String player_name) throws SQLException {        
        connection_open();
        JSONObject obj = new JSONObject();
        
        try {
            String selectquery = "SELECT * from tblusedwords WHERE roomKey = ? ORDER BY tour DESC LIMIT 1";
            preparedStatement = connection.prepareStatement(selectquery);
            preparedStatement.setInt(1,Integer.parseInt(room_key));
            ResultSet rs = preparedStatement.executeQuery();
            if(rs.next()){
                obj.put("word", rs.getString("word"));
            }
            
        } catch (Exception e) {
        }
        
        String selectquery1 = "SELECT * from tblonlinerooms WHERE roomKey = ? ";
        preparedStatement = connection.prepareStatement(selectquery1);
        preparedStatement.setString(1,room_key);
        
        ResultSet rs = preparedStatement.executeQuery();
        if(rs.next()) {  
            obj.put("players", rs.getString("players"));
            obj.put("playersTurn", rs.getString("playersTurn"));
            obj.put("isGameStarted", rs.getBoolean("isGameStarted"));
            obj.put("tour", rs.getInt("tour"));
            obj.put("status", "success");
            String[] players;
            players = rs.getString("players").split(";;;");
            obj.put("kisiSayisi", players.length);
        }else{
            obj.put("status", "fail");
        }
        conneciton_close();
        return obj;
    }
        public JSONObject getBotRoomInfo(String room_key,String player_name) throws SQLException {        
        connection_open();
        JSONObject obj = new JSONObject();
        
        try {
            String selectquery = "SELECT * from tblusedwords WHERE roomKey = ? ORDER BY tour DESC LIMIT 1";
            preparedStatement = connection.prepareStatement(selectquery);
            preparedStatement.setInt(1,Integer.parseInt(room_key));
            ResultSet rs = preparedStatement.executeQuery();
            if(rs.next()){
                obj.put("isPlayer", rs.getString("player"));
                obj.put("word", rs.getString("word"));
            }
            
        } catch (Exception e) 
        {
            
        }
        
        String selectquery1 = "SELECT * from tblbotrooms WHERE roomKey = ? ";
        preparedStatement = connection.prepareStatement(selectquery1);
        preparedStatement.setString(1,room_key);
        
        ResultSet rs = preparedStatement.executeQuery();
        if(rs.next()) {  
            obj.put("Player", rs.getString("Player"));
            obj.put("isGameStarted", rs.getBoolean("isGameActive"));
            obj.put("tour", rs.getInt("tour"));
            obj.put("status", "success");
        }else{
            obj.put("status", "fail");
        }
        conneciton_close();
        return obj;
    }
    
    public String getPuan(String room_key,String player_name) throws SQLException 
    {
        connection_open();
        String a = "";
        try{
            String selectquery2 = "SELECT * FROM tblscore Where RoomKey = ? AND Player = ?";
            preparedStatement = connection.prepareStatement(selectquery2);
            preparedStatement.setString(1,room_key);
            preparedStatement.setString(2,player_name);
            ResultSet rs = preparedStatement.executeQuery();
            while(rs.next())
            {
                a =  rs.getString("Score");
            }
        }
        catch(Exception e){
            
        }
        conneciton_close();
        return a;
    }
    
    public int setPuanOnline(String room_key,String player_name,String word) throws SQLException 
    {
        connection_open();
        int frekans = 0;
        int puan = 0;
        int score=0;
        boolean isCreated = false;
        boolean PointType = false;
        try {
            String selectquery1 ="SELECT * FROM tblonlinerooms where roomKey = ? ";
            preparedStatement = connection.prepareStatement(selectquery1);
            preparedStatement.setString(1,word);
            ResultSet rs = preparedStatement.executeQuery();
            while(rs.next())
            {
                PointType = rs.getBoolean("PointType");
            }
            if(PointType == true)
            {
                String selectquery ="SELECT * FROM tblwords2 where name = ? ";
                preparedStatement = connection.prepareStatement(selectquery);
                preparedStatement.setString(1,word);
                rs = preparedStatement.executeQuery();
                while (rs.next()) 
                {
                    frekans = rs.getInt("frekans");
                }
                frekans = 10000/frekans;
                if(0<= frekans  && frekans < 2000)
                {
                    puan = 5;
                }
                else if(2000<= frekans  && frekans < 4000)
                {
                    puan = 10;
                }
                else if(4000<= frekans  && frekans < 6000)
                {
                    puan = 15;
                }
                else if(6000<= frekans  && frekans < 8000)
                {
                    puan = 20;
                }
                else if(8000<= frekans  && frekans < 9000)
                {
                    puan = 30;
                }
                else if(9000<= frekans  && frekans <= 10000)
                {
                    puan = 50;
                }
                String selectquery2 = "SELECT * FROM tblscore Where RoomKey = ? AND Player = ?";
                preparedStatement = connection.prepareStatement(selectquery2);
                preparedStatement.setString(1,room_key);
                preparedStatement.setString(2,player_name);
                rs = preparedStatement.executeQuery();
                while(rs.next()){
                    isCreated = true;
                    score = Integer.parseInt(rs.getString("Score"));
                }
                if(isCreated == true)
                {
                    score = score + puan ;
                    selectquery2 = "UPDATE tblscore SET Score = ? WHERE RoomKey = ? and Player = ? ";
                    preparedStatement = connection.prepareStatement(selectquery2);
                    preparedStatement.setInt(1,score);
                    preparedStatement.setString(2,room_key);
                    preparedStatement.setString(3,player_name);
                    preparedStatement.execute();
                }
                else
                {
                    score = puan;
                    String selectquery3 = "INSERT INTO tblscore (Score,RoomKey,Player) VALUES(?,?,?)";
                    preparedStatement = connection.prepareStatement(selectquery3);
                    preparedStatement.setInt(1,score);
                    preparedStatement.setString(2,room_key);
                    preparedStatement.setString(3,player_name);
                    preparedStatement.execute();
                }
            }
            else
            {
                int iLength = word.length();
                
                String selectquery2 = "SELECT * FROM tblscore Where RoomKey = ? AND Player = ?";
                preparedStatement = connection.prepareStatement(selectquery2);
                preparedStatement.setString(1,room_key);
                preparedStatement.setString(2,player_name);
                rs = preparedStatement.executeQuery();
                while(rs.next()){
                    isCreated = true;
                    score = Integer.parseInt(rs.getString("Score"));
                }
                if(isCreated == true)
                {
                    score = score + iLength ;
                    selectquery2 = "UPDATE tblscore SET Score = ? WHERE RoomKey = ? and Player = ? ";
                    preparedStatement = connection.prepareStatement(selectquery2);
                    preparedStatement.setInt(1,score);
                    preparedStatement.setString(2,room_key);
                    preparedStatement.setString(3,player_name);
                    preparedStatement.execute();
                }
                else
                {
                    score = iLength;
                    String selectquery3 = "INSERT INTO tblscore (Score,RoomKey,Player) VALUES(?,?,?)";
                    preparedStatement = connection.prepareStatement(selectquery3);
                    preparedStatement.setInt(1,score);
                    preparedStatement.setString(2,room_key);
                    preparedStatement.setString(3,player_name);
                    preparedStatement.execute();
                }
            }
        } catch (Exception e) {
            
        }
        
        conneciton_close();
        return puan;
    }
    
    public int setPuanSingle(String room_key,String player_name,String word) throws SQLException 
    {
        connection_open();
        int frekans = 0;
        int puan = 0;
        int score=0;
        boolean isCreated = false;
        try {
            String selectquery ="SELECT * FROM tblwords2 where name = ? ";
            preparedStatement = connection.prepareStatement(selectquery);
            preparedStatement.setString(1,word);
            ResultSet rs = preparedStatement.executeQuery();
            while (rs.next()) 
            {
                frekans = rs.getInt("frekans");
            }
            frekans = 10000/frekans;
            if(0<= frekans  && frekans < 2000)
            {
                puan = 5;
            }
            else if(2000<= frekans  && frekans < 4000)
            {
                puan = 10;
            }
            else if(4000<= frekans  && frekans < 6000)
            {
                puan = 15;
            }
            else if(6000<= frekans  && frekans < 8000)
            {
                puan = 20;
            }
            else if(8000<= frekans  && frekans < 9000)
            {
                puan = 30;
            }
            else if(9000<= frekans  && frekans <= 10000)
            {
                puan = 50;
            }
            String selectquery2 = "SELECT * FROM tblscore Where RoomKey = ? AND Player = ?";
            preparedStatement = connection.prepareStatement(selectquery2);
            preparedStatement.setString(1,room_key);
            preparedStatement.setString(2,player_name);
            rs = preparedStatement.executeQuery();
            while(rs.next()){
                isCreated = true;
                score = Integer.parseInt(rs.getString("Score"));
            }
            if(isCreated == true)
            {
                score = score + puan ;
                selectquery2 = "UPDATE tblscore SET Score = ? WHERE RoomKey = ? and Player = ? ";
                preparedStatement = connection.prepareStatement(selectquery2);
                preparedStatement.setInt(1,score);
                preparedStatement.setString(2,room_key);
                preparedStatement.setString(3,player_name);
                preparedStatement.execute();
            }
            else
            {
                score = puan;
                String selectquery3 = "INSERT INTO tblscore (Score,RoomKey,Player) VALUES(?,?,?)";
                preparedStatement = connection.prepareStatement(selectquery3);
                preparedStatement.setInt(1,score);
                preparedStatement.setString(2,room_key);
                preparedStatement.setString(3,player_name);
                preparedStatement.execute();
            }
        } catch (Exception e) {
            
        }
        
        conneciton_close();
        return puan;
    }
    
    public int setMinusPuan(String room_key,String player_name) throws SQLException 
    {
        connection_open();
        boolean isCreated = false;
        int score=0;
        try {
                System.out.println(room_key);
                System.out.println(player_name);
                String selectquery2 = "SELECT * FROM tblscore Where RoomKey = ? AND Player = ?";
                preparedStatement = connection.prepareStatement(selectquery2);
                preparedStatement.setString(1,room_key);
                preparedStatement.setString(2,player_name);
                ResultSet rs = preparedStatement.executeQuery();
                    while(rs.next())
                    {
                        score = rs.getInt("Score");
                    }
                System.out.println(score);
                String selectquery = "UPDATE tblscore SET Score = ? WHERE RoomKey = ? and Player = ? ";
                preparedStatement = connection.prepareStatement(selectquery);
                preparedStatement.setInt(1,score-20);
                preparedStatement.setString(2,room_key);
                preparedStatement.setString(3,player_name);
                preparedStatement.execute();
        } 
        catch (Exception e) 
        {
            
        }
        conneciton_close();
        return score-20;
    }
    
    
    public JSONObject StartBotGame(String Room_key,String Player,String StartingWord) throws SQLException {        
        connection_open();
        JSONObject obj = new JSONObject();
        
        try {
            
            String status = "fail";

                String selectquery2 = "SELECT * FROM tblbotrooms JOIN tblonlinerooms WHERE tblonlinerooms.roomKey = ? OR tblbotrooms.RoomKey = ?";
                preparedStatement = connection.prepareStatement(selectquery2);
                preparedStatement.setString(1,Room_key);
                preparedStatement.setString(2,Room_key);
                ResultSet rs = preparedStatement.executeQuery();
                boolean isCreated = false;
                    if(rs.next())
                    {
                        isCreated = true;
                }
            if(isCreated == false){
            
                String selectquery = "INSERT INTO tblusedwords (word,roomKey,player,tour) VALUES(?,?,'bot',1)";
                preparedStatement = connection.prepareStatement(selectquery);
                preparedStatement.setString(1,StartingWord);
                preparedStatement.setInt(2,Integer.parseInt(Room_key));
                preparedStatement.execute();


                String selectquery1 = "INSERT INTO tblbotrooms SET RoomKey = ?, Player = ? , isGameActive = true , Tour = 1 ";
                preparedStatement = connection.prepareStatement(selectquery1);
                preparedStatement.setString(1,Room_key);
                preparedStatement.setString(2,Player);
                preparedStatement.execute();
                obj.put("status", "success");
            }
        } catch (Exception e) {
            obj.put("status", "fail");
        }
        
        conneciton_close();
        return obj;
    }
    public JSONObject StartOnlineGame(String room_key,String startingWord) throws SQLException {        
        connection_open();
        JSONObject obj = new JSONObject();
        String[] players = null;
        try {
            String selectquery2 = "SELECT * FROM tblonlinerooms WHERE roomKey = ?";
            preparedStatement = connection.prepareStatement(selectquery2);
            preparedStatement.setString(1,room_key);
            ResultSet rs = preparedStatement.executeQuery();
            if(rs.next())
            {
              players = rs.getString("players").split(";;;");
            }
            if(players.length >= 2)
            {
                String selectquery = "INSERT INTO tblusedwords (word,roomKey,player,tour) VALUES(?,?,'',1)";
                preparedStatement = connection.prepareStatement(selectquery);
                preparedStatement.setString(1,startingWord);
                preparedStatement.setInt(2,Integer.parseInt(room_key));
                preparedStatement.execute();


                String selectquery1 = "UPDATE tblonlinerooms SET isGameStarted = true, tour = 1 WHERE roomKey = ? ";
                preparedStatement = connection.prepareStatement(selectquery1);
                preparedStatement.setString(1,room_key);
                preparedStatement.execute();
                obj.put("status", "success");
            }
            else
            {
                obj.put("status", "Min number of players");
            }
        } catch (Exception e) {
            obj.put("status", "fail");
        }
        
        conneciton_close();
        return obj;
    }
    
    public JSONObject updateGameInfo(String room_key,String player,int tour) throws SQLException {        
        connection_open();
        JSONObject obj = new JSONObject();
        String playersTurn="error";
        String[] players = null;
        try {
            String selectquery = "SELECT * FROM tblonlinerooms WHERE roomKey = ? ";
            preparedStatement = connection.prepareStatement(selectquery);
            preparedStatement.setString(1,room_key);
            ResultSet rs = preparedStatement.executeQuery();
            if(rs.next()){
                players = rs.getString("players").split(";;;");
            }
            
            for(int i=0;i<players.length;i++){
                if(players[i].equals(player)){
                    if(i+1==players.length){
                        playersTurn=players[0];
                    }else{
                        playersTurn=players[i+1];
                    }
                }
            }
            
            String selectquery1 = "UPDATE tblonlinerooms SET playersTurn = ? ,tour = ? WHERE roomKey = ? ";
            preparedStatement = connection.prepareStatement(selectquery1);
            preparedStatement.setString(1,playersTurn);
            preparedStatement.setInt(2,tour);
            preparedStatement.setString(3,room_key);
            

            preparedStatement.execute();
            obj.put("status", "success");
        } catch (Exception e) {
            obj.put("status", "fail");
        }
        
        conneciton_close();
        return obj;
    }
    
    public JSONObject updateBotGameInfo(String room_key,String player,int tour) throws SQLException 
    {        
        connection_open();
        JSONObject obj = new JSONObject();
        try{
            String selectquery1 = "UPDATE tblbotrooms SET tour = ? WHERE roomKey = ? ";
            preparedStatement = connection.prepareStatement(selectquery1);
            preparedStatement.setInt(1,tour);
            preparedStatement.setString(2,room_key);
            
            preparedStatement.execute();
            obj.put("status", "success");
        } catch (Exception e) {
            obj.put("status", "fail");
        }
        
        conneciton_close();
        return obj;
    }
    
    public ArrayList<JSONObject> getLeaderboard() throws SQLException{
        connection_open();
        ArrayList<JSONObject> arr = new ArrayList();
        JSONObject obj = new JSONObject();
        try {
            String selectquery ="SELECT COUNT(word)*10 as puan, player, roomKey FROM tblusedwords GROUP BY player,roomKey ORDER BY puan DESC LIMIT 3";
            preparedStatement = connection.prepareStatement(selectquery);
            ResultSet rs = preparedStatement.executeQuery();
            while (rs.next()) { 
                obj = new JSONObject();
                obj.put("player", rs.getString("player"));
                obj.put("puan", rs.getInt("puan"));
                arr.add(obj);
            }
            
        } catch (Exception e) {
            
        }
        
        conneciton_close();
        return arr;
    }
    public ArrayList<JSONObject> getWordLenghtLeaderboard() throws SQLException{
        connection_open();
        ArrayList<JSONObject> arr = new ArrayList();
        JSONObject obj = new JSONObject();
        try {
            String selectquery ="SELECT tblscore.Player FROM tblscore INNER JOIN tblonlinerooms ON tblonlinerooms.roomKey = tblscore.RoomKey WHERE tblonlinerooms.PointType = 0  ORDER BY tblscore.Score DESC LIMIT 3";
            preparedStatement = connection.prepareStatement(selectquery);
            ResultSet rs = preparedStatement.executeQuery();
            while (rs.next()) { 
                obj = new JSONObject();
                obj.put("player", rs.getString("tblscore.Player"));
                arr.add(obj);
            }
            
        } catch (Exception e) {
            
        }
        
        conneciton_close();
        return arr;
    }
    public ArrayList<JSONObject> getHardLeaderboard() throws SQLException{
        connection_open();
        ArrayList<JSONObject> arr = new ArrayList();
        JSONObject obj = new JSONObject();
        try {
            String selectquery ="SELECT tblscore.Player FROM tblscore INNER JOIN tblonlinerooms ON tblonlinerooms.roomKey = tblscore.RoomKey WHERE tblonlinerooms.PointType = 1  ORDER BY tblscore.Score DESC LIMIT 3";
            preparedStatement = connection.prepareStatement(selectquery);
            ResultSet rs = preparedStatement.executeQuery();
            while (rs.next()) { 
                obj = new JSONObject();
                obj.put("player", rs.getString("tblscore.Player"));
                arr.add(obj);
            }
            
        } catch (Exception e) {
            
        }
        
        conneciton_close();
        return arr;
    }
    public ArrayList<JSONObject> getSingleLeaderboard() throws SQLException{
        connection_open();
        ArrayList<JSONObject> arr = new ArrayList();
        JSONObject obj = new JSONObject();
        try {
            String selectquery ="SELECT tblscore.Player FROM tblscore INNER JOIN tblbotrooms ON tblbotrooms.roomKey = tblscore.RoomKey ORDER BY tblscore.Score DESC LIMIT 3";
            preparedStatement = connection.prepareStatement(selectquery);
            ResultSet rs = preparedStatement.executeQuery();
            while (rs.next()) { 
                obj = new JSONObject();
                obj.put("player", rs.getString("tblscore.Player"));
                arr.add(obj);
            }
            
        } catch (Exception e) {
            
        }
        
        conneciton_close();
        return arr;
    }
    
    public boolean quitGame(String player,String roomKey) throws SQLException{
        connection_open();
        boolean isUserQuited = false;
        boolean isPlayersChanged = false;
        
        try {
            String playersTurn="";
            String players="";
            String selectquery ="SELECT * from tblonlinerooms WHERE roomKey = ?";
            preparedStatement = connection.prepareStatement(selectquery);
            preparedStatement.setString(1,roomKey);
            ResultSet rs = preparedStatement.executeQuery();
            while (rs.next()) { 
                players = rs.getString("players");
                playersTurn = rs.getString("playersTurn");
            }
            
            
            String[] arr = null;
            arr = players.split(";;;");
            if(playersTurn.equals(player)){
                for(int i=0;i<arr.length;i++){
                    System.out.println(arr[i]);
                if(arr[i].equals(player)){
                    if(i+1==arr.length){
                        playersTurn=arr[0];
                    }else{
                        playersTurn=arr[i+1];
                    }
                }
            }
            }
            
            if(!players.equals(players.replace(player+";;;", "")) ){
                players = players.replace(player+";;;", "");
                isPlayersChanged=true;
            }else if(!players.equals(players.replace(";;;"+player, ""))){
                players = players.replace(";;;"+player, "");
                isPlayersChanged=true;
            }else if(!players.equals(players.replace(player, "")) ){
                players = players.replace(player, "");
                isPlayersChanged=true;
            }
            if(arr.length < 2)
            {
            String updatequery ="UPDATE tblonlinerooms SET players = ?, playersTurn = ? , isActive = 0 WHERE roomKey = ?";
            preparedStatement = connection.prepareStatement(updatequery);
            preparedStatement.setString(1,players);
            preparedStatement.setString(2,playersTurn);
            preparedStatement.setString(3,roomKey);
            preparedStatement.execute();  
            }
            else
            {
            String updatequery ="UPDATE tblonlinerooms SET players = ?, playersTurn = ? WHERE roomKey = ?";
            preparedStatement = connection.prepareStatement(updatequery);
            preparedStatement.setString(1,players);
            preparedStatement.setString(2,playersTurn);
            preparedStatement.setString(3,roomKey);
            preparedStatement.execute();
            isUserQuited=true;  
            }
        } catch (Exception e) {
        }
        
        conneciton_close();
        if(isUserQuited && isPlayersChanged){
            return true;
        }else{
            return false;
        }
        
    }
    
    //buradan sonrası voiceappppppppppppppppppppppp
    public void sendVoice(String base64,String name,String roomKey) throws SQLException{
        connection_open();
        
        try {
            String selectquery ="INSERT INTO tblchats (base64,name,roomKey) VALUES(?,?,?)";
            preparedStatement = connection.prepareStatement(selectquery);
            preparedStatement.setString(1,base64);
            preparedStatement.setString(2,name);
            preparedStatement.setString(3,roomKey);
            

            preparedStatement.execute();
            
        } catch (Exception e) {
            
        }
        
        conneciton_close();
    }
    
    public String getVoice() throws SQLException{
        connection_open();
        String base64="";
        try {
            String selectquery ="SELECT * from tblchats WHERE id=5";
            preparedStatement = connection.prepareStatement(selectquery);
            ResultSet rs = preparedStatement.executeQuery();
            if(rs.next()){
                base64 = rs.getString("base64");
            }
            
        } catch (Exception e) {
            
        }
        
        conneciton_close();
        
        return base64;
    }
}
