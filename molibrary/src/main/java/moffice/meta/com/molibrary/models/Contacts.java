package moffice.meta.com.molibrary.models;

/**
 * Created by com.moffice.com.microoffice.app on 18-09-2017.
 */

public class Contacts{
    String strName;
    String strEmail;
    String strContactNo;

    public Contacts() {
    }

    public Contacts(String strName, String strEmail, String strContactNo) {
        this.strName = strName;
        this.strEmail = strEmail;
        this.strContactNo = strContactNo;
    }

    public String getStrName() {
        return strName;
    }

    public void setStrName(String strName) {
        this.strName = strName;
    }

    public String getStrEmail() {
        return strEmail;
    }

    public void setStrEmail(String strEmail) {
        this.strEmail = strEmail;
    }

    public String getStrContactNo() {
        return strContactNo;
    }

    public void setStrContactNo(String strContactNo) {
        this.strContactNo = strContactNo;
    }
}
