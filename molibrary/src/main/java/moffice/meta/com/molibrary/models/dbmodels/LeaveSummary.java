package moffice.meta.com.molibrary.models.dbmodels;

public class LeaveSummary {
    String userName;
    int casualLeave;
    int earnedlLeave;
    int lopLeave;
    int totalLeave;

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    String uuid;


    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public int getCasualLeave() {
        return casualLeave;
    }

    public void setCasualLeave(int casualLeave) {
        this.casualLeave = casualLeave;
    }

    public int getEarnedlLeave() {
        return earnedlLeave;
    }

    public void setEarnedlLeave(int earnedlLeave) {
        this.earnedlLeave = earnedlLeave;
    }

    public int getLopLeave() {
        return lopLeave;
    }

    public void setLopLeave(int lopLeave) {
        this.lopLeave = lopLeave;
    }

    public int getTotalLeave() {
        return totalLeave;
    }

    public void setTotalLeave(int totalLeave) {
        this.totalLeave = totalLeave;
    }
}
