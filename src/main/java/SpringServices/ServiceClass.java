/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package SpringServices;

import Kelimeler.kelimelerDB;
import Players.deleteusercontrolSQL;
import Players.logincontrolSQL;
import Players.playerinfo_SQL;
import Players.playerloginSQL;
import PostEntityPackage.PlayerName;
import PostEntityPackage.PlayerName_RoomKey;
import PostEntityPackage.Word_RoomKey_PlayerName;
import Rooms.roomsDB;
import Players.registerplayerDB;
import PostEntityPackage.GameSettings;
import PostEntityPackage.RoomName;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.Random;
import javax.servlet.http.HttpServletRequest;

import org.json.simple.JSONObject;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

/**
 *
 * @author ASOS
 */
@RestController
@RequestMapping("/Service")
public class ServiceClass {
    
    @PostMapping(
            value = "/registerPlayer", consumes = "application/json", produces = "application/json")
    public JSONObject registerPlayer(@RequestBody playerinfo_SQL param) throws SQLException {
        JSONObject obj = new JSONObject();
        registerplayerDB reg = new registerplayerDB();
      
        try {
            String sc = reg.registerUser(param.getPlayerName(),param.getEmail(),param.getPassword(),param.getUniqId());
            obj.put("status", sc);
        } catch (Exception e) {
            obj.put("status", "fail");
        }
        
        return obj;
    }
    
    @PostMapping(
            value = "/loginplayer", consumes = "application/json", produces = "application/json")
    public JSONObject loginplayer(@RequestBody playerloginSQL param) throws SQLException {
        JSONObject obj = new JSONObject();
        registerplayerDB reg = new registerplayerDB();
      
        try {
            String sc = reg.loginuser(param.getPlayerName(),param.getPassword(),param.getUniqId());
            obj.put("status", sc);
        } catch (Exception e) {
            obj.put("status", "fail");
        }
        
        return obj;
    }
    
    @PostMapping(
            value = "/logincontrol", consumes = "application/json", produces = "application/json")
    public JSONObject logincontrol(@RequestBody logincontrolSQL param) throws SQLException {
        JSONObject obj = new JSONObject();
        registerplayerDB reg = new registerplayerDB();
      
        try {
            String sc = reg.loginControl(param.getUniqId());
            obj.put("status", sc);
        } catch (Exception e) {
            obj.put("status", "fail");
        }
        
        return obj;
    }
    
    @PostMapping(
            value = "/quitcontrol", consumes = "application/json", produces = "application/json")
    public JSONObject quitcontrol(@RequestBody logincontrolSQL param) throws SQLException {
        JSONObject obj = new JSONObject();
        registerplayerDB reg = new registerplayerDB();
        System.out.println(param.getUniqId());
        try {
            String sc = reg.QuitUser(param.getUniqId());
            obj.put("status", sc);
        } catch (Exception e) {
            obj.put("status", "fail");
        }
        
        return obj;
    }
    
    @PostMapping(
            value = "/deleteuser", consumes = "application/json", produces = "application/json")
    public JSONObject deleteuser(@RequestBody deleteusercontrolSQL param) throws SQLException {
        JSONObject obj = new JSONObject();
        registerplayerDB reg = new registerplayerDB();
        try {
            String sc = reg.DeleteUser(param.getPlayerName(),param.getPassword());
            obj.put("status", sc);
        } catch (Exception e) {
            obj.put("status", "fail");
        }
        
        return obj;
    }
    
    @PostMapping(
            value = "/CreateOnlineRoom", consumes = "application/json", produces = "application/json")
    public JSONObject createOnlineRoom(@RequestBody GameSettings param) throws SQLException {
        JSONObject obj = new JSONObject();
        roomsDB rooms = new roomsDB();
        String Status = "";
    try {
            do{
            Random rnd = new Random();
            int number = rnd.nextInt(999999);
            String roomkey = String.format("%06d", number);
            Status = rooms.createRoom(param.getroomName() ,roomkey , param.getplayerName(),param.getisPrivate(),
                    param.getisTimeOpen(),param.getNumberOfPeople(),param.getPointType(),param.getTime());
            obj.put("room_key", roomkey);
            obj.put("status", "success");

            }while(Status == "Created");
        
        } catch (Exception e) {
            obj.put("status", "fail");
        }
        
        return obj;
    }
    
    @PostMapping(
            value = "/JoinRoom", consumes = "application/json", produces = "application/json")
    public JSONObject joinRoom(@RequestBody PlayerName_RoomKey param) throws SQLException {
        JSONObject obj = new JSONObject();
        roomsDB rooms = new roomsDB();
        String status="fail";
        try {
           status = rooms.joinRoom(param.getRoomKey(), param.getPlayerName());
        } catch (Exception e) {
        }
        
        obj.put("status", status);
        return obj;
    }
    @PostMapping(
            value = "/getActiveRooms", consumes = "application/json", produces = "application/json")
    public ArrayList<JSONObject> getActiveRooms(@RequestBody PlayerName param) throws SQLException {
        roomsDB rooms = new roomsDB();
        ArrayList<JSONObject> arr = rooms.getActiveRooms(param.getPlayerName());
        return arr;
    }
    
    @PostMapping(
            value = "/getOpenRooms", consumes = "application/json", produces = "application/json")
    public ArrayList<JSONObject> getOpenRooms() throws SQLException {
        roomsDB rooms = new roomsDB();
        ArrayList<JSONObject> arr = rooms.getOpenRooms();
        return arr;
    }
    
    @PostMapping(
            value = "/getSearchRooms", consumes = "application/json", produces = "application/json")
    public ArrayList<JSONObject> getSearchRooms(@RequestBody RoomName param) throws SQLException {
        roomsDB rooms = new roomsDB();
        System.out.println(param.getRoomName());
        ArrayList<JSONObject> arr = rooms.getSearchRooms(param.getRoomName());
        return arr;
    }
    
    @PostMapping(
            value = "/getRoomsSettingsInfo", consumes = "application/json", produces = "application/json")
    public JSONObject getRoomsSettingsInfo(@RequestBody PlayerName_RoomKey param) throws SQLException {
        roomsDB rooms = new roomsDB();
        JSONObject obj = rooms.getRoomSettingsInfo(param.getRoomKey());
        return obj;
    }
    
    @PostMapping(
            value = "/InfoRoom", consumes = "application/json", produces = "application/json")
    public JSONObject infoRoom(@RequestBody PlayerName_RoomKey param) throws SQLException {
        JSONObject obj;
        roomsDB rooms = new roomsDB();
        
        obj = rooms.getRoomInfo(param.getRoomKey(), param.getPlayerName());
        String puan = rooms.getPuan(param.getRoomKey(), param.getPlayerName());
        obj.put("puan", puan);
        return obj;
    }
    
    @PostMapping(
            value = "/InfoBotRoom", consumes = "application/json", produces = "application/json")
    public JSONObject infoBotRoom(@RequestBody PlayerName_RoomKey param) throws SQLException {
        JSONObject obj;
        roomsDB rooms = new roomsDB();
        
        obj = rooms.getBotRoomInfo(param.getRoomKey(), param.getPlayerName());
        String puan = rooms.getPuan(param.getRoomKey(), param.getPlayerName());
        obj.put("puan", puan);
        return obj;
    }
    
    @PostMapping(
            value = "/StartOnlineGame", consumes = "application/json", produces = "application/json")
    public JSONObject StartOnlineGame(@RequestBody PlayerName_RoomKey param) throws SQLException {
        JSONObject obj = new JSONObject();
        kelimelerDB kelimeler = new kelimelerDB();
        String startingWord= kelimeler.getRandomWord();
        roomsDB rooms = new roomsDB();
        obj = rooms.StartOnlineGame(param.getRoomKey(),startingWord);
        return obj;
    }
    
    //botla
    @PostMapping(
            value = "/SingelStartGame", consumes = "application/json", produces = "application/json")
    public JSONObject SingelStartGame(@RequestBody PlayerName param) throws SQLException {
        
        JSONObject obj = new JSONObject();
        kelimelerDB kelimeler = new kelimelerDB();
        Random rnd = new Random();
        String Status = "";
        do{
            int number = rnd.nextInt(999999);
            String roomkey = String.format("%06d", number);
        
            String startingWord= kelimeler.getRandomWord();
            roomsDB rooms = new roomsDB();
            obj = rooms.StartBotGame(roomkey,param.getPlayerName(),startingWord);
            obj.put("roomkey", roomkey);
        
        }while(Status == "Created");
        
        return obj;
    }
    
    @PostMapping(
            value = "/OnlineSendWord", consumes = "application/json", produces = "application/json")
    public JSONObject sendWord(@RequestBody Word_RoomKey_PlayerName param) throws SQLException {
        kelimelerDB kelimeler = new kelimelerDB();
        roomsDB rooms = new roomsDB();
        JSONObject obj = new JSONObject();
        JSONObject gameobj;
        gameobj = kelimeler.getRoomsWord(param.getRoomKey());
        String gameWord=(String) gameobj.get("word");
        int tour = (int) gameobj.get("tour");
        String firstLetter = param.getWord().substring(0, 1);
        
        if(firstLetter.equals(gameWord.substring(gameWord.length() - 1))){
            if(kelimeler.checkWord(param.getWord())){
                if(kelimeler.isWordUsed(param.getWord(), param.getRoomKey())){
                    obj.put("status", "wordWasUsed");
                }
                else{
                    if(param.getWord().substring(param.getWord().length()-1).equals("ğ")){
                        kelimeler.updateRoomsWord(param.getRoomKey(), kelimeler.getRandomWord(), param.getPlayerName(), tour+1);
                        rooms.updateGameInfo(param.getRoomKey(), param.getPlayerName(), tour);
                    }else{
                        kelimeler.updateRoomsWord(param.getRoomKey(), param.getWord(), param.getPlayerName(), tour+1);
                        rooms.updateGameInfo(param.getRoomKey(), param.getPlayerName(), tour);
                    }
                int puan = rooms.setPuanOnline(param.getRoomKey(), param.getPlayerName() ,param.getWord());
                obj.put("Puan",puan);
                obj.put("status", "success");
                }
            }else{
                obj.put("status", "wordNotFound");
            }
        }else{
            obj.put("status", "firstLetterMatchFail");
        }
        
        return obj;
    }
    
    @PostMapping(
            value = "/getRandomWord", consumes = "application/json", produces = "application/json")
    public JSONObject getRandomWord() throws SQLException {
        kelimelerDB kelimeler = new kelimelerDB();
        JSONObject obj = new JSONObject();
        obj.put("word", kelimeler.getRandomWord());
        return obj;
    }
    
    //botla
    @PostMapping(
            value = "/SinglebotRandomWord", consumes = "application/json", produces = "application/json")
    public JSONObject SinglebotRandomWord(@RequestBody Word_RoomKey_PlayerName param) throws SQLException {
        kelimelerDB kelimeler = new kelimelerDB();
        roomsDB rooms = new roomsDB();
        JSONObject obj = new JSONObject();
        JSONObject gameobj;
        gameobj = kelimeler.getRoomsWord(param.getRoomKey());
        String gameWord=(String) gameobj.get("word");
        int tour = (int) gameobj.get("tour");
        String firstLetter = param.getWord().substring(0, 1);
        
        if(firstLetter.equals(gameWord.substring(gameWord.length() - 1))){
            if(kelimeler.checkWord(param.getWord())){
                if(kelimeler.isWordUsed(param.getWord(), param.getRoomKey())){
                    obj.put("status", "wordWasUsed");
                }
                else{
                    if(param.getWord().substring(param.getWord().length()-1).equals("ğ")){
                        System.out.println("Burdayım");
                        kelimeler.updateRoomsWord(param.getRoomKey(), param.getWord(), param.getPlayerName(), tour+1);
                        kelimeler.updateRoomsWord(param.getRoomKey(), kelimeler.SingleGameGetWord(kelimeler.getRandomWord()), "bot", tour+2);
                        rooms.updateBotGameInfo(param.getRoomKey(), param.getPlayerName(), tour);
                    }else{
                        kelimeler.updateRoomsWord(param.getRoomKey(), param.getWord(), param.getPlayerName(), tour+1);
                        kelimeler.updateRoomsWord(param.getRoomKey(), kelimeler.SingleGameGetWord(param.getWord()), "bot", tour+2);
                        rooms.updateBotGameInfo(param.getRoomKey(), param.getPlayerName(), tour);
                    }
                    int puan = rooms.setPuanSingle(param.getRoomKey(), param.getPlayerName() ,param.getWord());
                obj.put("status", "success");
                }
            }else{
                obj.put("status", "wordNotFound");
            }
        }else{
            obj.put("status", "firstLetterMatchFail");
        }
        
        return obj;
    }
    @PostMapping(
            value = "/setMinusPuan", consumes = "application/json", produces = "application/json")
    public JSONObject setMinusPuan(@RequestBody PlayerName_RoomKey param) throws SQLException {
        JSONObject obj = new JSONObject();
        roomsDB rooms = new roomsDB();
        obj.put("puan", rooms.setMinusPuan(param.getRoomKey(),param.getPlayerName()));
        return obj;
    }
    
    @PostMapping(
            value = "/getLenghtLeaderboard", consumes = "application/json", produces = "application/json")
    public ArrayList<JSONObject> getLenghtLeaderboard() throws SQLException {
        kelimelerDB kelimeler = new kelimelerDB();
        roomsDB rooms = new roomsDB();
        ArrayList<JSONObject> arr = rooms.getWordLenghtLeaderboard();
        return arr;
    }
    @PostMapping(
            value = "/getHardLeaderboard", consumes = "application/json", produces = "application/json")
    public ArrayList<JSONObject> getHardLeaderboard() throws SQLException {
        kelimelerDB kelimeler = new kelimelerDB();
        roomsDB rooms = new roomsDB();
        ArrayList<JSONObject> arr = rooms.getHardLeaderboard();
        return arr;
    }
    @PostMapping(
            value = "/getSingleLeaderboard", consumes = "application/json", produces = "application/json")
    public ArrayList<JSONObject> getSingleLeaderboard() throws SQLException {
        kelimelerDB kelimeler = new kelimelerDB();
        roomsDB rooms = new roomsDB();
        ArrayList<JSONObject> arr = rooms.getSingleLeaderboard();
        return arr;
    }
    
    @PostMapping(
            value = "/quitGame", consumes = "application/json", produces = "application/json")
    public JSONObject quitGame(@RequestBody PlayerName_RoomKey param) throws SQLException {
        JSONObject obj = new JSONObject();
        roomsDB rooms = new roomsDB();
        boolean isUserQuited = rooms.quitGame(param.getPlayerName(), param.getRoomKey());
        if(isUserQuited){
            obj.put("status", "success");
        }else{
            obj.put("status", "fail");
        }
        return obj;
    }
    
    //buradan sonrası chatapp için tahsis edilmiştir, çok da umursamayın yani
    @PostMapping(
            value = "/sendVoice", consumes = "application/json", produces = "application/json")
    public JSONObject sendVoice(@RequestBody Word_RoomKey_PlayerName param) throws SQLException {
        JSONObject obj = new JSONObject();
        roomsDB rooms = new roomsDB();
        rooms.sendVoice(param.getWord(), param.getPlayerName(), param.getRoomKey());
        obj.put("status", "succes");
        return obj;
    }
    
    @PostMapping(
            value = "/getVoice", consumes = "application/json", produces = "application/json")
    public JSONObject getVoice(@RequestBody Word_RoomKey_PlayerName param) throws SQLException {
        JSONObject obj = new JSONObject();
        roomsDB rooms = new roomsDB();
        String base64 = rooms.getVoice();
        obj.put("base64", base64);
        return obj;
    }
    
    @GetMapping("/error")
    public String getErrorPath() {
        return "/error";
    }

}
