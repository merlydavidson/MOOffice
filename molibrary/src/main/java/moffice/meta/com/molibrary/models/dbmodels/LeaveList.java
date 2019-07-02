package moffice.meta.com.molibrary.models.dbmodels;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.j256.ormlite.field.DatabaseField;

import java.io.Serializable;
import java.sql.Date;

/**
 * Created by karan on 20-09-2017 for Save and
 * Retentive table SalesEnquiry table data.
 */
public class LeaveList implements Serializable {

    @DatabaseField(columnName = "LeaveID", generatedId = true)
    @SerializedName("LeaveID")
    @Expose
    private int LeaveID;

    public String getRecUUID() {
        return RecUUID;
    }

    public void setRecUUID(String recUUID) {
        RecUUID = recUUID;
    }

    @DatabaseField(columnName = "RecUUID")
    @SerializedName("RecUUID")
    @Expose
    private String RecUUID;


    @DatabaseField(columnName = "StartDatTimeStamp")
    @SerializedName("StartDatTimeStamp")
    @Expose
    private long StartDatTimeStamp;

    public long getStartDatTimeStamp() {
        return StartDatTimeStamp;
    }

    public void setStartDatTimeStamp(long startDatTimeStamp) {
        StartDatTimeStamp = startDatTimeStamp;
    }

    @DatabaseField(columnName = "StartDat")
    @SerializedName("StartDat")
    @Expose
    private String StartDat;

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public long getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(long timeStamp) {
        this.timeStamp = timeStamp;
    }

    public String getDateTime() {
        return dateTime;
    }

    public void setDateTime(String dateTime) {
        this.dateTime = dateTime;
    }

    @DatabaseField(columnName = "DateTime")
    @SerializedName("DateTime")
    @Expose
    private String dateTime;
    @DatabaseField(columnName = "TimeStamp")
    @SerializedName("TimeStamp")
    @Expose
    private long timeStamp;
    @DatabaseField(columnName = "EndDat")
    @SerializedName("EndDat")
    @Expose
    private String endDate;

    @DatabaseField(columnName = "UserName")
    @SerializedName("UserName")
    @Expose
    private String UserName;

    public String getUserName() {
        return UserName;
    }

    public void setUserName(String userName) {
        UserName = userName;
    }

    @DatabaseField(columnName = "UserUUID")
    @SerializedName("UserUUID")
    @Expose
    private String UserUUID;

    @DatabaseField(columnName = "Notes")
    @SerializedName("Notes")
    @Expose
    private String Notes;


    public String getRejectionNotes() {
        return rejectionNotes;
    }

    public void setRejectionNotes(String rejectionNotes) {
        this.rejectionNotes = rejectionNotes;
    }

    @DatabaseField(columnName = "RejectionNote")
    @SerializedName("RejectionNote")
    @Expose
    private String rejectionNotes;

    @DatabaseField(columnName = "LvStatus")
    @SerializedName("LVStatus")
    @Expose
    private int LvStatus;

    @DatabaseField(columnName = "SendStatus")
    @SerializedName("SendStatus")
    @Expose
    private int SendStatus;

    @DatabaseField(columnName = "NumDays")
    @SerializedName("NumDays")
    @Expose
    private float NumDays;

    @DatabaseField(columnName = "LvType")
    @SerializedName("LvType")
    @Expose
    private int lvType;

    //Leave record is active if set to 1 . If set to zero record is deleted
    @DatabaseField(columnName = "lvActiveRec")
    @SerializedName("lvActiveRec")
    @Expose
    private int lvActiveRec;


    public LeaveList() {
    }

    public int getLeaveID() {
        return LeaveID;
    }

    public void setLeaveID(int leaveID) {
        LeaveID = leaveID;
    }



    public String getStartDat() {
        return StartDat;
    }

    public void setStartDat(String startDat) {
        StartDat = startDat;
    }

    public String getUserUUID() {
        return UserUUID;
    }

    public void setUserUUID(String userUUID) {
        UserUUID = userUUID;
    }

    public String getNotes() {
        return Notes;
    }

    public void setNotes(String notes) {
        Notes = notes;
    }

    public int getLvStatus() {
        return LvStatus;
    }

    public void setLvStatus(int lvStatus) {
        LvStatus = lvStatus;
    }

    /**
     * 1. Reuested
     * 2. Approved
     * 3. Rejected
     * 4. Discarded
     */
    public int getSendStatus() {
        return SendStatus;
    }

    public void setSendStatus(int sendStatus) {
        SendStatus = sendStatus;
    }

   public float getNumDays() {
        return NumDays;
    }

    public void setNumDays(float numDays) {
        NumDays = numDays;
    }

    public int getLvType() {
        return lvType;
    }

    public void setLvType(int lvType) {
        this.lvType = lvType;
    }

    public int getLvActiveRec() {
        return lvActiveRec;
    }

    public void setLvActiveRec(int lvActiveRec) {
        this.lvActiveRec = lvActiveRec;
    }
}
