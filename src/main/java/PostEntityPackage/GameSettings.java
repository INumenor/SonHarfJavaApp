/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package PostEntityPackage;

/**
 *
 * @author Batuh
 */
public class GameSettings 
{
    
    private String roomName;
    private String playerName;
    private boolean isPrivate;
    private boolean isTimeOpen;
    private int NumberOfPeople;
    private boolean PointType;
    private int Time;

    public String getroomName() {
        return roomName;
    }

    public void setroomName(String roomName) {
        this.roomName = roomName;
    }
    
    public String getplayerName() {
        return playerName;
    }

    public void setplayerName(String playerName) {
        this.playerName = playerName;
    }
    
    public boolean getisPrivate() {
        return isPrivate;
    }

    public void setisPrivate(boolean isPrivate) {
        this.isPrivate = isPrivate;
    }
    
    public boolean getisTimeOpen() {
        return isTimeOpen;
    }

    public void setisTimeOpen(boolean isTimeOpen) {
        this.isTimeOpen = isTimeOpen;
    }
    
    public int getNumberOfPeople() {
        return NumberOfPeople;
    }

    public void setNumberOfPeople(int NumberOfPeople) {
        this.NumberOfPeople = NumberOfPeople;
    }
    
    public boolean getPointType() {
        return PointType;
    }

    public void setPointType(boolean PointType) {
        this.PointType = PointType;
    }
    
    public int getTime() {
        return Time;
    }

    public void setTime(int Time) {
        this.Time = Time;
    }
}
