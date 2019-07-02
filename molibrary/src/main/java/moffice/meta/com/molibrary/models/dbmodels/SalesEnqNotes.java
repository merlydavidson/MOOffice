package moffice.meta.com.molibrary.models.dbmodels;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.j256.ormlite.field.DatabaseField;

import java.sql.Date;

/**
 * Created by com.moffice.com.microoffice.app on 25-09-2017.
 */

public class SalesEnqNotes {

    // Foreign key defined to hold associations
    @DatabaseField(canBeNull = false, foreign = true, foreignAutoRefresh = true)

    public SalesEnquiry SalesEnquiry;

    @DatabaseField(columnName = "DateTime")
    @SerializedName("DateTime")
    @Expose
    private Date dateTime;

    @DatabaseField(columnName = "EnqIDUUID", generatedId = true)
    @SerializedName("EnqIDUUID")
    @Expose
    private int EnqIDUUID;

    @DatabaseField(columnName = "Note")
    @SerializedName("Note")
    @Expose
    private String Note;
    @DatabaseField(columnName = "UserID")
    @SerializedName("UserID")
    @Expose
    private int UserID;

    public SalesEnqNotes() {
    }

    public SalesEnqNotes(SalesEnquiry salesEnquiry,
                         Date DateTime, String note, int userID) {
        SalesEnquiry = salesEnquiry;
        DateTime = DateTime;
        Note = note;
        UserID = userID;
    }

    public SalesEnquiry getSalesEnquiry() {
        return SalesEnquiry;
    }

    public void setSalesEnquiry(SalesEnquiry salesEnquiry) {
        SalesEnquiry = salesEnquiry;
    }

    public String getNote() {
        return Note;
    }

    public void setNote(String note) {
        Note = note;
    }

    public int getUserID() {
        return UserID;
    }

    public void setUserID(int userID) {
        UserID = userID;
    }
}
