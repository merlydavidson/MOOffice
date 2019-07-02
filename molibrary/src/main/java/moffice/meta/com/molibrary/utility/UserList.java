package moffice.meta.com.molibrary.utility;

/**
 * Created by karan on 20-09-2017 for Save and
 * Retentive table UserList table data.
 */
public class UserList {
    String userName;
    String number;
    String uuid;
    String iemiCode;
    String email;
    int RegStatus;

    public UserList() {
    }

    public UserList(String userName, String number, String uuid, String iemiCode, String email, int RegStatus) {
        this.userName = userName;
        this.number = number;
        this.uuid = uuid;
        this.iemiCode = iemiCode;
        this.email = email;
        this.RegStatus = RegStatus;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getIemiCode() {
        return iemiCode;
    }

    public void setIemiCode(String iemiCode) {
        this.iemiCode = iemiCode;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public int getRegStatus() {
        return RegStatus;
    }

    public void setRegStatus(int regStatus) {
        RegStatus = regStatus;
    }
}
