package moffice.meta.com.molibrary.models.dbmodels;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.j256.ormlite.field.DatabaseField;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * Created by user on 12/14/2017.
 */

public class SupportDetails implements Serializable {
    @DatabaseField(columnName = "_id", generatedId = true)
    @SerializedName("_id")
    @Expose
    private Integer id;
    @DatabaseField(columnName = "RecUUID")
    @SerializedName("RecUUID")
    @Expose
    private String RecUUID;
    @DatabaseField(columnName = "UserUUID")
    @SerializedName("UserUUID")
    @Expose
    private String UserUUID;

    @DatabaseField(columnName = "name")
    @SerializedName("name")
    @Expose
    private String name;
    @DatabaseField(columnName = "custName")
    @SerializedName("custName")
    @Expose
    private String custName;
    @DatabaseField(columnName = "phoneNumber")
    @SerializedName("phoneNumber")
    @Expose
    private String phoneNumber;
    @DatabaseField(columnName = "description")
    @SerializedName("description")
    @Expose
    private String description;

    @DatabaseField(columnName = "status")
    @SerializedName("status")
    @Expose
    private String status;

    @DatabaseField(columnName = "SendStatus")
    @SerializedName("SendStatus")
    @Expose
    private
    int SendStatus;

    @DatabaseField(columnName = "assignedTo")
    @SerializedName("assignedTo")
    @Expose
    private String assignedTo;

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

    @DatabaseField(columnName = "DateTimeStamp")
    @SerializedName("DateTimeStamp")
    @Expose
    private long dateTimeStamp;

    @DatabaseField(columnName = "product")
    @SerializedName("product")
    @Expose
    private String product;

    @DatabaseField(columnName = "date")
    @SerializedName("date")
    @Expose
    private String date;

    @DatabaseField(columnName = "note")
    @SerializedName("note")
    @Expose
    private String note;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getAssignedTo() {
        return assignedTo;
    }

    public void setAssignedTo(String assignedTo) {
        this.assignedTo = assignedTo;
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


    public SupportDetails() {
    }

    public SupportDetails(Integer id, String name, String product, String date, String description, String note, String phoneNumber) {
        this.id = id;
        this.name = name;
        this.phoneNumber = phoneNumber;
        this.description = description;
        this.product = product;
        this.date = date;
        this.note = note;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getProduct() {
        return product;
    }

    public void setProduct(String product) {
        this.product = product;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }



    public int getSendStatus() {
        return SendStatus;
    }

    public void setSendStatus(int sendStatus) {
        SendStatus = sendStatus;
    }

    public long getDateTimeStamp() {
        return dateTimeStamp;
    }

    public void setDateTimeStamp(long dateTimeStamp) {
        this.dateTimeStamp = dateTimeStamp;
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

    public String getCustName() {
        return custName;
    }

    public void setCustName(String custName) {
        this.custName = custName;
    }
}

