package moffice.meta.com.molibrary.utility;

import android.content.Context;
import android.location.Location;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.PolylineOptions;
import com.j256.ormlite.android.apptools.OpenHelperManager;

import java.util.ArrayList;
import java.util.List;

import moffice.meta.com.molibrary.core.MODataDispatchThread;
import moffice.meta.com.molibrary.core.Settings;
import moffice.meta.com.molibrary.database.DatabaseHelperORM;

/**
 * Created by com.moffice.com.microoffice.app on 02/01/2017.
 */

public class CommonDataArea {


    public static int PLACE_OFFICE_LOC=100;

    private static boolean registered;
    private static boolean instTypeSelected = false;
    private static int instType=0;
    public static  Object dbLock = new Object();
    public static  Object dispThreadRel = new Object();
    private static Settings commonSettings;
    private static String regErrorReason ="NA";

    private static String tempUUID;

    //read from shared pref
    private static String userUUID;
    private static String topic;
    public static Location office1;
    public static Location office2;
    public static Location office3;
    public static Location placeLoc;
    public static String placeName;
    public static List<MarkerToDraw> markers;
    public static PolylineOptions polylineOptions;
    public static ArrayList<LatLng> arraylistLAti = new ArrayList<LatLng>();;


    public static MODataDispatchThread dispThread;

    public static boolean isInstTypeSelected() throws Exception{

        return commonSettings.isInstTypeSet();
    }

    public static int getInstType() throws  Exception{
        if(commonSettings==null) {
            Exception exp = new Exception("MCore.readSettings not called");
        }
        return commonSettings.getInstType();
    }



    public static Settings getCommonSettings() throws Exception{
        if(commonSettings==null) {
            Exception exp = new Exception("MCore.readSettings not called");
        }
        return commonSettings;
    }

    public static void setCommonSettings(Settings commonSettings) {
        CommonDataArea.commonSettings = commonSettings;
    }

    public static String getUserUUID() {
        CommonDataArea.userUUID = commonSettings.getUserUUID();;
        return commonSettings.getUserUUID();
    }

    public static String getTopic() {
        return commonSettings.getTopic();
    }


    public static boolean isRegistered() {
        return commonSettings.isRegStatus();
    }

    static DatabaseHelperORM databaseHelperORM = null;
    public static DatabaseHelperORM getHelper(Context con) {

        if (databaseHelperORM == null) {
            databaseHelperORM = OpenHelperManager.getHelper(con, DatabaseHelperORM.class);
            return databaseHelperORM;
        } else if(databaseHelperORM.isOpen()){
            return databaseHelperORM;
        } else {
            databaseHelperORM = OpenHelperManager.getHelper(con, DatabaseHelperORM.class);
            return databaseHelperORM;
        }

    }

    public static void closeHelper() {
     /*   if (databaseHelperORM != null) {
            OpenHelperManager.releaseHelper();
            databaseHelperORM=null;
        }*/
    }
    public static  boolean isDbOpen()
    {
        if (databaseHelperORM != null) {
           return databaseHelperORM.isOpen();
        }
        return  false;
    }


    public static String getTempUUID() {
        return tempUUID;
    }

    public static void setTempUUID(String tempUUID) {
        CommonDataArea.tempUUID = tempUUID;
    }

    public static String getRegErrorReason() {
        return regErrorReason;
    }

    public static void setRegErrorReason(String regErrorReason) {
        CommonDataArea.regErrorReason = regErrorReason;
    }
}

