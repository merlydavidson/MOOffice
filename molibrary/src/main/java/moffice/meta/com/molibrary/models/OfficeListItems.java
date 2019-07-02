package moffice.meta.com.molibrary.models;

/**
 * Created by com.moffice.com.microoffice.app on 03-08-2017.
 */

public class OfficeListItems {

    int iSno;
    String strOfficeName;
    String dLat, dLong;

    public OfficeListItems() {}

    public OfficeListItems(int iSno, String strOfficeName, String dLat, String dLong) {
        this.iSno = iSno;
        this.strOfficeName = strOfficeName;
        this.dLat = dLat;
        this.dLong = dLong;
    }

    public int getiSno() {
        return iSno;
    }

    public void setiSno(int iSno) {
        this.iSno = iSno;
    }

    public String getStrOfficeName() {
        return strOfficeName;
    }

    public void setStrOfficeName(String strOfficeName) {
        this.strOfficeName = strOfficeName;
    }

    public String getdLat() {
        return dLat;
    }

    public void setdLat(String dLat) {
        this.dLat = dLat;
    }

    public String getdLong() {
        return dLong;
    }

    public void setdLong(String dLong) {
        this.dLong = dLong;
    }
}
