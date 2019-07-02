package moffice.meta.com.molibrary.models.dbmodels;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.j256.ormlite.field.DatabaseField;

public class LeaveDetails {
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

   @DatabaseField(columnName = "LeaveType")
    @SerializedName("LeaveType")
    @Expose
    private String LeaveType;


    @DatabaseField(columnName = "LeaveReason")
    @SerializedName("LeaveReason")
    @Expose
    private String LeaveReason;

    @DatabaseField(columnName = "WorkHandOver")
    @SerializedName("WorkHandOver")
    @Expose
    private String WorkHandOver;

    @DatabaseField(columnName = "LeaveStartDate")
    @SerializedName("LeaveStartDate")
    @Expose
    private String LeaveStartDate;

    @DatabaseField(columnName = "LeaveSEndDate")
    @SerializedName("LeaveSEndDate")
    @Expose
    private String LeaveSEndDate;

    public LeaveDetails() {
    }

    public LeaveDetails(String recUUID, String userUUID, String leaveType, String leaveReason, String workHandOver, String leaveStartDate, String leaveSEndDate) {
        RecUUID = recUUID;
        UserUUID = userUUID;
        LeaveType = leaveType;
        LeaveReason = leaveReason;
        WorkHandOver = workHandOver;
        LeaveStartDate = leaveStartDate;
        LeaveSEndDate = leaveSEndDate;
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

    public String getLeaveType() {
        return LeaveType;
    }

    public void setLeaveType(String leaveType) {
        LeaveType = leaveType;
    }

    public String getLeaveReason() {
        return LeaveReason;
    }

    public void setLeaveReason(String leaveReason) {
        LeaveReason = leaveReason;
    }

    public String getWorkHandOver() {
        return WorkHandOver;
    }

    public void setWorkHandOver(String workHandOver) {
        WorkHandOver = workHandOver;
    }

    public String getLeaveStartDate() {
        return LeaveStartDate;
    }

    public void setLeaveStartDate(String leaveStartDate) {
        LeaveStartDate = leaveStartDate;
    }

    public String getLeaveSEndDate() {
        return LeaveSEndDate;
    }

    public void setLeaveSEndDate(String leaveSEndDate) {
        LeaveSEndDate = leaveSEndDate;
    }
}
