package moffice.meta.com.molibrary.models.dbmodels;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.j256.ormlite.field.DatabaseField;

import java.io.Serializable;

/**
 * Created by karan on 20-09-2017 for Save and
 * Retentive table CompanyList table data.
 */
public class CompanyList implements Serializable {

    @DatabaseField(columnName = "CompID", generatedId = true)
    @SerializedName("CompID")
    @Expose
    private int CompID;
    @DatabaseField(columnName = "CompanyName")
    @SerializedName("CompanyName")
    @Expose
    private String CompanyName;
    @DatabaseField(columnName = "CompAddress")
    @SerializedName("CompAddress")
    @Expose
    private String CompAddress;
    @DatabaseField(columnName = "CmpPin")
    @SerializedName("CmpPin")
    @Expose
    private String CmpPin;
    @DatabaseField(columnName = "CmpState")
    @SerializedName("CmpState")
    @Expose
    private String CmpState;
    @DatabaseField(columnName = "CmpEmail")
    @SerializedName("CmpEmail")
    @Expose
    private String CmpEmail;
    @DatabaseField(columnName = "CmpPass")
    @SerializedName("CmpPass")
    @Expose
    private String CmpPass;
    @DatabaseField(columnName = "CompGUID")
    @SerializedName("CompGUID")
    @Expose
    private String CompGUID;

    public CompanyList() {
    }

    public CompanyList(int compID, String companyName, String compAddress,
                       String cmpPin, String cmpState, String cmpEmail,
                       String cmpPass, String compGUID) {
        CompID = compID;
        CompanyName = companyName;
        CompAddress = compAddress;
        CmpPin = cmpPin;
        CmpState = cmpState;
        CmpEmail = cmpEmail;
        CmpPass = cmpPass;
        CompGUID = compGUID;
    }

    public int getCompID() {
        return CompID;
    }

    public void setCompID(int compID) {
        CompID = compID;
    }

    public String getCompanyName() {
        return CompanyName;
    }

    public void setCompanyName(String companyName) {
        CompanyName = companyName;
    }

    public String getCompAddress() {
        return CompAddress;
    }

    public void setCompAddress(String compAddress) {
        CompAddress = compAddress;
    }

    public String getCmpPin() {
        return CmpPin;
    }

    public void setCmpPin(String cmpPin) {
        CmpPin = cmpPin;
    }

    public String getCmpState() {
        return CmpState;
    }

    public void setCmpState(String cmpState) {
        CmpState = cmpState;
    }

    public String getCmpEmail() {
        return CmpEmail;
    }

    public void setCmpEmail(String cmpEmail) {
        CmpEmail = cmpEmail;
    }

    public String getCmpPass() {
        return CmpPass;
    }

    public void setCmpPass(String cmpPass) {
        CmpPass = cmpPass;
    }

    public String getCompGUID() {
        return CompGUID;
    }

    public void setCompGUID(String compGUID) {
        CompGUID = compGUID;
    }
}
