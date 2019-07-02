package moffice.meta.com.molibrary.models;

/**
 * Created by com.moffice.com.microoffice.app on 04-08-2017.
 */

public class AttendanceListItems {

    String Date;
    String name;
    String TimeIn;
    String TimeOut;
    String TotalWorked;

    public AttendanceListItems() {
    }

    public AttendanceListItems(String date, String name, String timeIn, String timeOut, String totalWorked) {
        Date = date;
        this.name = name;
        TimeIn = timeIn;
        TimeOut = timeOut;
        TotalWorked = totalWorked;
    }

    public String getDate() {
        return Date;
    }

    public void setDate(String date) {
        Date = date;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTimeIn() {
        return TimeIn;
    }

    public void setTimeIn(String timeIn) {
        TimeIn = timeIn;
    }

    public String getTimeOut() {
        return TimeOut;
    }

    public void setTimeOut(String timeOut) {
        TimeOut = timeOut;
    }

    public String getTotalWorked() {
        return TotalWorked;
    }

    public void setTotalWorked(String totalWorked) {
        TotalWorked = totalWorked;
    }
}
