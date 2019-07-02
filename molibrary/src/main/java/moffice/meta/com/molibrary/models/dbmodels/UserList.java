package moffice.meta.com.molibrary.models.dbmodels;

import android.location.Location;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.j256.ormlite.field.DatabaseField;

import java.io.Serializable;

/**
 * Created by karan on 20-09-2017 for Save and
 * Retentive table UserList table data.
 */
public class UserList implements Serializable {

    @DatabaseField(columnName = "_id", generatedId = true)
    @SerializedName("_id")
    @Expose
    private Integer _id;
    @DatabaseField(columnName = "Name")
    @SerializedName("Name")
    @Expose
    private String Name;
    @DatabaseField(columnName = "IMENumber")
    @SerializedName("IMENumber")
    @Expose
    private String IMENumber;
    @DatabaseField(columnName = "Mobile")
    @SerializedName("Mobile")
    @Expose
    private String Mobile;
    @DatabaseField(columnName = "NickName")
    @SerializedName("NickName")
    @Expose
    private String NickName;
    @DatabaseField(columnName = "SecMobile")
    @SerializedName("SecMobile")
    @Expose
    private String SecMobile;
    @DatabaseField(columnName = "Email")
    @SerializedName("Email")
    @Expose
    private String Email;
    @DatabaseField(columnName = "Password")
    @SerializedName("Password")
    @Expose
    private String Password;
    @DatabaseField(columnName = "LastOTP")
    @SerializedName("LastOTP")
    @Expose
    private String LastOTP;
    @DatabaseField(columnName = "OTPTime")
    @SerializedName("OTPTime")
    @Expose
    private String OTPTime;
    @DatabaseField(columnName = "Status")
    @SerializedName("Status")
    @Expose
    private int Status;
    @DatabaseField(columnName = "UserGUID")
    @SerializedName("UserGUID")
    @Expose
    private String UserGUID;



    public UserList(Integer _id, String name, String IMENumber, String mobile, String nickName,
                    String secMobile, String email, String password, String lastOTP, String OTPTime,
                    int status, String userGUID, String secret, OfficeLocs[] offices) {
        this._id = _id;
        Name = name;
        this.IMENumber = IMENumber;
        Mobile = mobile;
        NickName = nickName;
        SecMobile = secMobile;
        Email = email;
        Password = password;
        LastOTP = lastOTP;
        this.OTPTime = OTPTime;
        Status = status;
        UserGUID = userGUID;
        this.secret = secret;
        this.offices = offices;
    }

    private String secret;
    public class OfficeLocs{
        boolean enabled;
        int officeNum;
        String officeName;
        Location loc;
    }
    OfficeLocs [] offices = new OfficeLocs[5];

    public UserList(){
        for(int i=0;i<5;++i) {
            offices[i] = new OfficeLocs();
        }
    }

    public String getSecret() {
        return secret;
    }

    public void setSecret(String secret) {
        this.secret = secret;
    }

    public Integer get_id() {
        return _id;
    }

    public void set_id(Integer _id) {
        this._id = _id;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getIMENumber() {
        return IMENumber;
    }

    public void setIMENumber(String IMENumber) {
        this.IMENumber = IMENumber;
    }

    public String getMobile() {
        return Mobile;
    }

    public void setMobile(String mobile) {
        Mobile = mobile;
    }

    public String getNickName() {
        return NickName;
    }

    public void setNickName(String nickName) {
        NickName = nickName;
    }

    public String getSecMobile() {
        return SecMobile;
    }

    public void setSecMobile(String secMobile) {
        SecMobile = secMobile;
    }

    public String getEmail() {
        return Email;
    }

    public void setEmail(String email) {
        Email = email;
    }

    public String getPassword() {
        return Password;
    }

    public void setPassword(String password) {
        Password = password;
    }

    public String getLastOTP() {
        return LastOTP;
    }

    public void setLastOTP(String lastOTP) {
        LastOTP = lastOTP;
    }

    public String getOTPTime() {
        return OTPTime;
    }

    public void setOTPTime(String OTPTime) {
        this.OTPTime = OTPTime;
    }

    public int getStatus() {
        return Status;
    }

    public void setStatus(int status) {
        Status = status;
    }

    public String getUserGUID() {
        return UserGUID;
    }

    public void setUserGUID(String userGUID) {
        UserGUID = userGUID;
    }

    public OfficeLocs[] getOffices() {
        return offices;
    }

    public void setOffices(OfficeLocs[] offices) {
        this.offices = offices;
    }
}
