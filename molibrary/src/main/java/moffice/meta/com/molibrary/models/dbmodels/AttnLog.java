package moffice.meta.com.molibrary.models.dbmodels;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.j256.ormlite.field.DatabaseField;

import java.io.Serializable;

/**
 * Created by com.moffice.com.microoffice.app on 20-09-2017.
 */

public class AttnLog implements Serializable {

   // public static final String attnLog="create table AttnLog(_id integer primary key autoincrement,RecUUID string,InstUniqID string,UserID integer,LogType integer, InOut integer,Lati double,Longi double,Office string,LogYear integer,LogMonth integer,LogDayOfMonth integer,LogHour integer,LogMinute integer,SendStatus boolean)";



    @DatabaseField(columnName = "_id", generatedId = true)
    @SerializedName("_id")
    @Expose
    private int _id;


    @DatabaseField(columnName = "RecUUID")
    @SerializedName("RecUUID")
    @Expose
    private String RecUUID;

    @DatabaseField(columnName = "UserUUID")
    @SerializedName("UserUUID")
    @Expose
    private String UserUUID;

    @DatabaseField(columnName = "UserName")
    @SerializedName("UserName")
    @Expose
    private String UserName;

    @DatabaseField(columnName = "UserID")
    @SerializedName("UserID")
    @Expose
    private int UserID;

    @DatabaseField(columnName = "LogType")
    @SerializedName("LogType")
    @Expose
    private int LogType;

    @DatabaseField(columnName = "InOut")
    @SerializedName("InOut")
    @Expose
    private String InOut;

    @DatabaseField(columnName = "Lati")
    @SerializedName("Lati")
    @Expose
    private double Lati;

    @DatabaseField(columnName = "Longi")
    @SerializedName("Longi")
    @Expose
    private double Longi;

    @DatabaseField(columnName = "Office")
    @SerializedName("Office")
    @Expose
    private String Office;

    @DatabaseField(columnName = "OfficeUUID")
    @SerializedName("OfficeUUID")
    @Expose
    private String OfficeUUID;

    @DatabaseField(columnName = "LogYear")
    @SerializedName("LogYear")
    @Expose
    private int LogYear;

    @DatabaseField(columnName = "LogMonth")
    @SerializedName("LogMonth")
    @Expose
    private int LogMonth;



    @DatabaseField(columnName = "TodaysPlan")
    @SerializedName("TodaysPlan")
    @Expose
    private String todaysPlan;

    public int getDailyAllowance() {
        return DailyAllowance;
    }

    public void setDailyAllowance(int dailyAllawance) {
        DailyAllowance = dailyAllawance;
    }

    @DatabaseField(columnName = "DailyAllowance")
    @SerializedName("DailyAllowance")
    @Expose
    private int DailyAllowance;


    @DatabaseField(columnName = "placeVisit")
    @SerializedName("placeVisit")
    @Expose
    private String PlaceVisit;

    @DatabaseField(columnName = "LogDayOfMonth")
    @SerializedName("LogDayOfMonth")
    @Expose
    private int LogDayOfMonth;

    @DatabaseField(columnName = "LogHour")
    @SerializedName("LogHour")
    @Expose
    private int LogHour;

    @DatabaseField(columnName = "TimeStamp")
    @SerializedName("TimeStamp")
    @Expose
    private long timeStamp;

    @DatabaseField(columnName = "LogMinute")
    @SerializedName("LogMinute")
    @Expose
    private int LogMinute;

    //0-Not send, 1- Send , 2 - Failed
    @DatabaseField(columnName = "SendStatus")
    @SerializedName("SendStatus")
    @Expose
    private int SendStatus;

    @DatabaseField(columnName = "DateTime")
    @SerializedName("DateTime")
    @Expose
    private String dateTime;

    public AttnLog(String recUUID, String userUUID, String username,
                   int userID, int logType, String inOut, double lati,
                   double longi, String office, String officeUUID, int logYear, int logMonth,
                   int logDayOfMonth, int logHour, int logMinute, int sendStatus, String datetime) {

        this.RecUUID = recUUID;
        this.UserUUID = userUUID;
        this.UserName = username;
        this.UserID = userID;
        this.LogType = logType;
        this.InOut = inOut;
        this.setLati(lati);
        this.setLongi(longi);
        this.Office = office;
        this.OfficeUUID=officeUUID;
        this.LogYear = logYear;
        this.LogMonth = logMonth;
        this.LogDayOfMonth = logDayOfMonth;
        this.LogHour = logHour;
        this.LogMinute = logMinute;
        this.SendStatus = sendStatus;
        this.dateTime=datetime;
    }

    public AttnLog() {

    }

    public String getPlaceVisit() {
        return PlaceVisit;
    }

    public void setPlaceVisit(String placeVisit) {
        this.PlaceVisit = placeVisit;
    }
    public int get_id() {
        return _id;
    }

    public void set_id(int _id) {
        this._id = _id;
    }

    public String getRecUUID() {
        return RecUUID;
    }

    public void setRecUUID(String recUUID) {
        RecUUID = recUUID;
    }

    public int getUserID() {
        return UserID;
    }

    public void setUserID(int userID) {
        UserID = userID;
    }
    public String getUserName() {
        return UserName;
    }

    public void setUserName(String userName) {
        UserName = userName;
    }
    public int getLogType() {
        return LogType;
    }

    public void setLogType(int logType) {
        LogType = logType;
    }

    public String getTodaysPlan() {
        return todaysPlan;
    }

    public void setTodaysPlan(String todaysPlan) {
        this.todaysPlan = todaysPlan;
    }

    public String getInOut() {
        return InOut;
    }

    public void setInOut(String inOut) {
        InOut = inOut;
    }

    public double getLati() {
        return Lati;
    }

    public void setLati(double lati) {
        Lati = lati;
    }

    public double getLongi() {
        return Longi;
    }

    public void setLongi(double longi) {
        Longi = longi;
    }

    public String getOffice() {
        return Office;
    }

    public void setOffice(String office) {
        Office = office;
    }

    public int getLogYear() {
        return LogYear;
    }

    public void setLogYear(int logYear) {
        LogYear = logYear;
    }

    public int getLogMonth() {
        return LogMonth;
    }

    public void setLogMonth(int logMonth) {
        LogMonth = logMonth;
    }

    public int getLogDayOfMonth() {
        return LogDayOfMonth;
    }

    public void setLogDayOfMonth(int logDayOfMonth) {
        LogDayOfMonth = logDayOfMonth;
    }

    public int getLogHour() {
        return LogHour;
    }

    public void setLogHour(int logHour) {
        LogHour = logHour;
    }

    public int getLogMinute() {
        return LogMinute;
    }

    public void setLogMinute(int logMinute) {
        LogMinute = logMinute;
    }

    public int getSendStatus() {
        return SendStatus;
    }

    public void setSendStatus(int sendStatus) {
        SendStatus = sendStatus;
    }

    public String getUserUUID() {
        return UserUUID;
    }

    public void setUserUUID(String userUUID) {
        UserUUID = userUUID;
    }

    public String getOfficeUUID() {
        return OfficeUUID;
    }

    public void setOfficeUUID(String officeUUID) {
        OfficeUUID = officeUUID;
    }

    public String getANPM() {
        return dateTime;
    }

    public void setAMPM(String dateTime) {
        this.dateTime = dateTime;
    }

    public long getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(long timeStamp) {
        this.timeStamp = timeStamp;
    }

}
