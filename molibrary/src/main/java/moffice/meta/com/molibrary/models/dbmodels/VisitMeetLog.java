package moffice.meta.com.molibrary.models.dbmodels;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.j256.ormlite.field.DatabaseField;

import java.util.Date;

/**
 * Created by Administrator on 24-11-2017.
 */

public class VisitMeetLog {
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

    @DatabaseField(columnName = "VisitUUID")
    @SerializedName("VisitUUID")
    @Expose
    private String VisitUUID;

    @DatabaseField(columnName = "BusinessName")
    @SerializedName("BusinessName")
    @Expose
    private String BusinessName;

    @DatabaseField(columnName = "PersonMet")
    @SerializedName("PersonMet")
    @Expose
    private String PersonMet;

    @DatabaseField(columnName = "SendStatus")
    @SerializedName("SendStatus")
    @Expose
    private
    int SendStatus;

    @DatabaseField(columnName = "Title")
    @SerializedName("Title")
    @Expose
    private String Title;

    @DatabaseField(columnName = "Note")
    @SerializedName("Note")
    @Expose
    private String Note;

    @DatabaseField(columnName = "Date")
    @SerializedName("Date")
    @Expose
    private String Date;


    @DatabaseField(columnName = "DateTimeStamp")
    @SerializedName("DateTimeStamp")
    @Expose
    private long dateTimeStamp;
    //0- new 1-Verified 2-deleted
    @DatabaseField(columnName = "Status")
    @SerializedName("Status")
    @Expose
    private int Status;

    @DatabaseField(columnName = "NextVisDate")
    @SerializedName("NextVisDate")
    @Expose
    private String nextVisDate;

    private long MinutesSpend;

    public VisitMeetLog() {
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

    public String getUserUUID() {
        return UserUUID;
    }

    public void setUserUUID(String userUUID) {
        UserUUID = userUUID;
    }

    public String getVisitUUID() {
        return VisitUUID;
    }

    public void setVisitUUID(String visitUUID) {
        VisitUUID = visitUUID;
    }

    public String getPersonMet() {
        return PersonMet;
    }

    public void setPersonMet(String personMet) {
        PersonMet = personMet;
    }

    public int getSendStatus() {
        return SendStatus;
    }

    public void setSendStatus(int sendStatus) {
        SendStatus = sendStatus;
    }

    public String getTitle() {
        return Title;
    }

    public void setTitle(String title) {
        Title = title;
    }

    public String getNote() {
        return Note;
    }

    public void setNote(String note) {
        Note = note;
    }


    public long getMinutesSpend() {
        return MinutesSpend;
    }

    public void setMinutesSpend(long minutesSpend) {
        MinutesSpend = minutesSpend;
    }

    public int getStatus() {
        return Status;
    }

    public void setStatus(int status) {
        Status = status;
    }


    public long getDateTimeStamp() {
        return dateTimeStamp;
    }

    public void setDateTimeStamp(long dateTimeStamp) {
        this.dateTimeStamp = dateTimeStamp;
    }

    public String getUserName() {
        return UserName;
    }

    public void setUserName(String userName) {
        UserName = userName;
    }

    public String getDate() {
        return Date;
    }

    public void setDate(String date) {
        Date = date;
    }

    public String getBusinessName() {
        return BusinessName;
    }

    public void setBusinessName(String businessName) {
        BusinessName = businessName;
    }

    public String getNextVisDate() {
        return nextVisDate;
    }

    public void setNextVisDate(String nextVisDate) {
        this.nextVisDate = nextVisDate;
    }
}
