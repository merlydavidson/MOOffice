package moffice.meta.com.molibrary.models.dbmodels;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.j256.ormlite.field.DatabaseField;

import java.io.Serializable;

/**
 * Created by karan on 20-09-2017 for Save and
 * Retentive table Offices table data.
 */
public class Offices implements Serializable {

    @DatabaseField(columnName = "ID", generatedId = true)
    @SerializedName("ID")
    @Expose
    private int ID;
    @DatabaseField(columnName = "OfficeID")
    @SerializedName("OfficeID")
    @Expose
    private String OfficeID;
    @DatabaseField(columnName = "OfficeName")
    @SerializedName("OfficeName")
    @Expose
    private String OfficeName;
    @DatabaseField(columnName = "Latitude")
    @SerializedName("Latitude")
    @Expose
    private double Latitude;
    @DatabaseField(columnName = "Longitude")
    @SerializedName("Longitude")
    @Expose
    private double Longitude;
    @DatabaseField(columnName = "Address")
    @SerializedName("Address")
    @Expose
    private String Address;

    public Offices() {
    }

    public Offices(String officeID, String officeName, double latitude,
                   double longitude, String address) {
        OfficeID = officeID;
        OfficeName = officeName;
        Latitude = latitude;
        Longitude = longitude;
        Address = address;
    }

    public int getID() {
        return ID;
    }

    public String getOfficeID() {
        return OfficeID;
    }

    public void setOfficeID(String officeID) {
        OfficeID = officeID;
    }

    public String getOfficeName() {
        return OfficeName;
    }

    public void setOfficeName(String officeName) {
        OfficeName = officeName;
    }

    public double getLatitude() {
        return Latitude;
    }

    public void setLatitude(double latitude) {
        Latitude = latitude;
    }

    public double getLongitude() {
        return Longitude;
    }

    public void setLongitude(double longitude) {
        Longitude = longitude;
    }

    public String getAddress() {
        return Address;
    }

    public void setAddress(String address) {
        Address = address;
    }
}
