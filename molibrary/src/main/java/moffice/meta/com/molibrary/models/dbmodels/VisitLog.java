package moffice.meta.com.molibrary.models.dbmodels;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.j256.ormlite.field.DatabaseField;

import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * Created by Administrator on 24-11-2017.
 */

public class VisitLog {
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
    @DatabaseField(columnName = "ArrivedDeparted")
    @SerializedName("ArrivedDeparted")
    @Expose
    private int ArrivedDeparted;
    @DatabaseField(columnName = "Lati")
    @SerializedName("Lati")
    @Expose
    private double Lati;
    @DatabaseField(columnName = "Longi")
    @SerializedName("Longi")
    @Expose
    private double Longi;
    @DatabaseField(columnName = "PlaceName")
    @SerializedName("PlaceName")
    @Expose
    private String PlaceName;
    @DatabaseField(columnName = "BusinessName")
    @SerializedName("BusinessName")
    @Expose
    private String BusinessName;
    @DatabaseField(columnName = "Purpose")
    @SerializedName("Purpose")
    @Expose
    private String Purpose;
    @DatabaseField(columnName = "LogYear")
    @SerializedName("LogYear")
    @Expose
    private int LogYear;
    @DatabaseField(columnName = "LogMonth")
    @SerializedName("LogMonth")
    @Expose
    private int LogMonth;
    @DatabaseField(columnName = "LogDayOfMonth")
    @SerializedName("LogDayOfMonth")
    @Expose
    private int LogDayOfMonth;
    @DatabaseField(columnName = "LogHour")
    @SerializedName("LogHour")
    @Expose
    private int LogHour;
    @DatabaseField(columnName = "LogMinute")
    @SerializedName("LogMinute")
    @Expose
    private int LogMinute;

    @DatabaseField(columnName = "SendStatus")
    @SerializedName("SendStatus")
    @Expose
    private int SendStatus;

    @DatabaseField(columnName = "TimeStamp")
    @SerializedName("TimeStamp")
    @Expose
    private long timeStamp;

    @DatabaseField(columnName = "DateTime")
    @SerializedName("DateTime")
    @Expose
    private String dateTime;

    @DatabaseField(columnName = "HoursSpend")
    @SerializedName("HoursSpend")
    @Expose
    private long HoursSpend;

    @DatabaseField(columnName = "MinutesSpend")
    @SerializedName("MinutesSpend")
    @Expose
    private long MinutesSpend;

    public VisitLog() {
    }

    public VisitLog(int _id, String recUUID, String userUUID,
                    int userID, int arrivedDeparted, double lati,
                    double longi, String placeName, String purpose, int logYear, int logMonth,
                    int logDayOfMonth, int logHour, int logMinute, int sendStatus, long hoursSpend, long minutesSpend) {
        this._id = _id;
        RecUUID = recUUID;
        UserUUID = userUUID;
        UserID = userID;
        ArrivedDeparted=arrivedDeparted;
        Lati = lati;
        Longi = longi;
        PlaceName = placeName;
        Purpose=purpose;
        LogYear = logYear;
        LogMonth = logMonth;
        LogDayOfMonth = logDayOfMonth;
        LogHour = logHour;
        LogMinute = logMinute;
        SendStatus = sendStatus;
        HoursSpend=hoursSpend;
        MinutesSpend=minutesSpend;

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

    public String getInstUniqID() {
        return UserUUID;
    }

    public void setInstUniqID(String instUniqID) {
        UserUUID = instUniqID;
    }

    public int getUserID() {
        return UserID;
    }

    public void setUserID(int userID) {
        UserID = userID;
    }

    public int getArrivedDeparted() {
        return ArrivedDeparted;
    }

    public void setArrivedDeparted(int arrivedDeparted) {
        ArrivedDeparted = arrivedDeparted;
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

    public String getPlaceName() {
        return PlaceName;
    }

    public void setPlaceName(String placeName) {
        PlaceName = placeName;
    }
    public String getPurpose() {
        return Purpose;
    }

    public void setPurpose(String purpose) {
        Purpose = purpose;
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

    public String getUserName() {
        return UserName;
    }

    public void setUserName(String userName) {
        UserName = userName;
    }

    public String getDateTime() {
        if((dateTime==null)||(dateTime=="")) {
            dateTime= getDate(timeStamp,"dd-MM-yyyy:HH:mm");
            return dateTime;
        }
        return dateTime;
    }

    public void setDateTime(String dateTime) {
        this.dateTime = dateTime;
    }

    public long getHoursSpend() {
        return HoursSpend;
    }

    public void setHoursSpend(long hoursSpend) {
        this.HoursSpend = hoursSpend;
    }

    public long getMinutesSpend() {
        return MinutesSpend;
    }

    public void setMinutesSpend(long minutesSpend) {
        this.MinutesSpend = minutesSpend;
    }

    public long getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(long timeStamp) {
        this.timeStamp = timeStamp;
    }

    public  String getDate(long milliSeconds, String dateFormat)
    {
        // Create a DateFormatter object for displaying date in specified format.
        SimpleDateFormat formatter = new SimpleDateFormat(dateFormat);

        // Create a calendar object that will convert the date and time value in milliseconds to date.
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(milliSeconds);
        return formatter.format(calendar.getTime());
    }

    public String getBusinessName() {
        return BusinessName;
    }

    public void setBusinessName(String businessName) {
        BusinessName = businessName;
    }
}
