package moffice.meta.com.molibrary.models.dbmodels;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.j256.ormlite.field.DatabaseField;

import java.io.Serializable;

/**
 * Created by DANIYAL KJ on 07-Dec-17.
 */

public class Employees implements Serializable {
    @DatabaseField(columnName = "_id", generatedId = true)
    @SerializedName("_id")
    @Expose
    private int _id;
    @DatabaseField(columnName = "UserName")
    @SerializedName("UserName")
    @Expose
    private String UserName;
    @DatabaseField(columnName = "OfficeName")
    @SerializedName("OfficeName")
    @Expose
    private String OfficeName;
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

    @DatabaseField(columnName = "InOut")
    @SerializedName("InOut")
    @Expose
    private String InOut;

     public Employees()
    {

    }
    public String getEmpName() {
        return UserName;
    }

    public void setEmpName(String empName) {
        UserName = empName;
    }

    public String getOfficeName() {
        return OfficeName;
    }

    public void setOfficeName(String officeName) {
        OfficeName = officeName;
    }
    public int getLogYear() {return LogYear;}
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
    public String getInOut() {
        return InOut;
    }
    public void setInOut(String inOut) {
        InOut = inOut;
    }
}
