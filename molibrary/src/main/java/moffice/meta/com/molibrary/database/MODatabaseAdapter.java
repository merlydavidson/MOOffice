/**@author Visakh*/
package moffice.meta.com.molibrary.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.os.Environment;
import android.util.Log;

import org.json.JSONObject;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import moffice.meta.com.molibrary.core.MOMain;
import moffice.meta.com.molibrary.utility.LogWriter;
import moffice.meta.com.molibrary.utility.UserList;

import static moffice.meta.com.molibrary.database.MODBConstants.KEY_EMAIL;
import static moffice.meta.com.molibrary.database.MODBConstants.KEY_FNAME;
import static moffice.meta.com.molibrary.database.MODBConstants.KEY_MOBILE;
import static moffice.meta.com.molibrary.database.MODBConstants.KEY_REG_STATUS;
import static moffice.meta.com.molibrary.database.MODBConstants.KEY_USER_UUID;
import static moffice.meta.com.molibrary.database.MODatabase.attnLog;


public class MODatabaseAdapter {

    private Context context;
    private static SQLiteDatabase database;
    private static MODatabase moDatabase;
    private static final int MAX_MIN_PER_DAY = 1440;
    String curDateStr1 = "12/20/2016";
    private static String[] sleepArray = new String[MAX_MIN_PER_DAY];
    /*
        31
    February	28*
    March	31
    April	30
    May	31
    June	30
    July	31
    August	31
    September	30
    October	31
    November	30
    December	31
     */
    int[] daysInMonth = {31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31};

    public class SleepSummary {
        int totalSleepTimeMin;
        int deepSleepMin;
        int medSleepMin;
        int lightSleepTime;
        int wakeTimeMin;
    }

    public MODatabaseAdapter(Context ctx) {
        context = ctx;
    }

    public MODatabaseAdapter openDb() throws SQLException {
        if ((database != null) && (database.isOpen())) database.close();
        moDatabase = new MODatabase(context);
        database = moDatabase.getWritableDatabase();
        return this;
    }

    public void closeDb() {
        try {
            if ((database != null) && (database.isOpen())) database.close();
            moDatabase.close();
        } catch (Exception exp) {

        }
    }


    public String CopyDatabase() {
        boolean isCopied = false;
        try {
            Log.i("CopyDatabase", "Enter the first line");
            File sd = Environment.getExternalStorageDirectory();
            File data = Environment.getDataDirectory();
            if (sd.canWrite()) {
                String currentDBPath = "\\data\\com.phtl.gfit\\databases\\GfitDB";
                String backupDBPath = "GfitDB";
                File currentDB = new File("/data/data/com.phtl.gfit/databases/GfitDB");
                File backupDB = new File(sd, backupDBPath);
                if (currentDB.exists()) {
                    FileChannel src = new FileInputStream(currentDB).getChannel();
                    FileChannel dst = new FileOutputStream(backupDB).getChannel();
                    if (dst.transferFrom(src, 0, src.size()) > 0)
                        isCopied = true;
                    else isCopied = false;
                    src.close();
                    dst.close();
                    return null;
                }
            }
        } catch (Exception e) {
            Log.i("CopyDatabase", "Caught:" + e);
            isCopied = false;
            return e.toString();
        }
        return null;
    }

    public boolean saveAttnData(int year, int month,int dayofM,int hour,int min,int type,int inOut,String office, Double longi, Double lati,String userUUID,String recordUUID, boolean sendStatus) {
        try {
            if (database == null) return false;
            synchronized (database) {
                if (!database.isOpen()) return false;

                //Check whether user presents in the local DB. If not present ignore log
               String query = "select _id from UserList where UserUUID like \""+userUUID+"\"";
                Cursor mcursor = database.rawQuery(query, null);
                if ((mcursor == null)||(mcursor.getCount()==0)) {
                    LogWriter.writeLog("AttnLog","User not found for UUID->"+userUUID);
                    return false;
                }
                mcursor.moveToNext();
                int userId = mcursor.getInt(0);
                mcursor.close();
                //Check whether log is already recorded in DB by uisng RecordUUID
                query = "select RecUUID from AttnLog where RecUUID like \""+recordUUID+"\"";
                mcursor = database.rawQuery(query, null);
                if ((mcursor != null)&&(mcursor.getCount()>0)) {
                    LogWriter.writeLog("AttnLog","Attn Record already exists"+recordUUID);
                    return false;
                }
                mcursor.close();
                //RecUUID string,UserID integer,LogType as integer, InOut integer,Lati double,Longi double,Office string,LogYear integer,LogMonth integer,LogDayOfMonth integer,LogHour integer,LogMinute integer
                ContentValues values = new ContentValues();
                values.put("RecUUID", recordUUID);
                values.put("UserID", userUUID);
                values.put("LogType",type);
                values.put("InOut", inOut);
                values.put("Lati", lati);
                values.put("Lati", longi);
                values.put("LogYear", year);
                values.put("LogMonth", month);
                values.put("LogDayOfMonth",dayofM);
                values.put("LogMinute", min);
                values.put("SendStatus", sendStatus);
                database.insert("AttnLog", null, values);
                database.close();
                return true;
                //	database.close();
            }
        } catch (Exception exp) {
            LogWriter.writeLog("UserSave",exp.getMessage());
            database.execSQL(attnLog);
            return false;
        }
    }

    public void saveUserData(boolean insert ,String userName, String number, String uuid,String iemiCode,String email) {
        try {
            if (database == null) return;
            synchronized (database) {
                if (!database.isOpen()) return;

                String userUUID = uuid;
                String query = "select _id from UserList where UserUUID like \""+uuid+"\"";
                Cursor mcursor = database.rawQuery(query, null);
                if (mcursor == null) return;
                if (mcursor.getCount() == 0) // No record present for the day Insert new row
                {
                    ContentValues values = new ContentValues();
                    values.put("FName", userName);
                    values.put("Mobile", number);
                    values.put("IMEIcode",iemiCode);
                    values.put("UserUUID",uuid);
                    values.put("Email",email);
                    values.put("RegStatus", MOMain.MO_DBUSERLIST_INVITED);
                    database.insert("UserList", null, values);
                }
                else {
                    mcursor.moveToFirst();
                        int id = mcursor.getInt(mcursor.getColumnIndex("_id"));
                    String whereClause = "where _id="+id;
                    ContentValues values = new ContentValues();
                    values.put("FName", userName);
                    values.put("Mobile", number);
                    values.put("IMEIcode",iemiCode);
                    values.put("UserUUID",uuid);
                    values.put("Email",email);
                    values.put("RegStatus", MOMain.MO_DBUSERLIST_INVITED);
                    database.update ("UserList",values,whereClause,null);
                }
                mcursor.close();
                //	database.close();
            }
        } catch (Exception exp) {
            LogWriter.writeLog("saveUserData",exp.getMessage());
        }
    }

    public static List<UserList> getAllUserList(Context mContext){
        List<UserList> mUserListArr = new ArrayList<>();
        MODatabaseAdapter db = new MODatabaseAdapter(mContext);
        try {
            db.openDb();
            if (database == null) return mUserListArr;
            synchronized (database) {
                if (!database.isOpen()) return mUserListArr;
                String query = "select * from UserList";
                Cursor cursor = database.rawQuery(query, null);
                if (cursor == null) return mUserListArr;
                if (cursor.moveToFirst()) {
                    while (!cursor.isAfterLast()) {
                        UserList userList = new UserList();
                        userList.setNumber(cursor.getString(cursor.getColumnIndex(KEY_MOBILE)));
                        userList.setEmail(cursor.getString(cursor.getColumnIndex(KEY_EMAIL)));
                        userList.setUserName(cursor.getString(cursor.getColumnIndex(KEY_FNAME)));
                        userList.setRegStatus(cursor.getInt(cursor.getColumnIndex(KEY_REG_STATUS)));
                        userList.setUuid(cursor.getString(cursor.getColumnIndex(KEY_USER_UUID)));

                        mUserListArr.add(userList);
                        cursor.moveToNext();
                    }
                }

                cursor.close();
            }
            return mUserListArr;
        }catch (Exception exp){
            LogWriter.writeLog("getAllUserList",exp.getMessage());
        }
        db.closeDb();
        return null;
    }

    public Cursor getAttnData()
    {
        try {
            String query = "select * from AttnLog ";
            Cursor mcursor = database.rawQuery(query, null);
            return mcursor;
        }catch (Exception exp){
            Log.e("Exp",exp.getMessage());
        }
        return null;
 }

    public String saveUserData(JSONObject userData, int status) {
        try {
            if (database == null) return MOMain.ERRORCODE_DATABASE_ERROR+":Database Error";;
            synchronized (database) {
                boolean canUpdate = false;
                if (!database.isOpen()) return  MOMain.ERRORCODE_DATABASE_ERROR+":Database Error";

                JSONObject bodyObj =new JSONObject( userData.get("body").toString());
                String userUUID = bodyObj.get("UserUUID").toString();
                String query = "select * from UserList where UserUUID like \""+userUUID+"\"";
                Cursor mcursor = database.rawQuery(query, null);
                if (mcursor == null) return MOMain.ERRORCODE_USER_NOT_EXIST+":User not exists";
                if (mcursor.getCount() == 0) {
                    ContentValues values = new ContentValues();
                    if (bodyObj.get("Name") != null)
                        values.put("FName", bodyObj.get("Name").toString());
                    if (bodyObj.get("Mobile") != null)
                        values.put("Mobile", bodyObj.get("Mobile").toString());
                    if (bodyObj.get("InstUniqueID") != null)
                        values.put("InstUniqID", bodyObj.get("InstUniqueID").toString());
                    if (bodyObj.get("Email") != null)
                        values.put("Email", bodyObj.get("Email").toString());
                    if (bodyObj.get("UserUUID") != null)
                        values.put("UserUUID", bodyObj.get("UserUUID").toString());
                    values.put("RegStatus", status);
                    database.insert("UserList", null, values);
                }else
                    {
                        mcursor.moveToFirst();
                        int id = mcursor.getInt(0);
                        ContentValues values = new ContentValues();
                        if (bodyObj.get("Name") != null)
                            values.put("FName", bodyObj.get("Name").toString());
                        if (bodyObj.get("Mobile") != null)
                            values.put("Mobile", bodyObj.get("Mobile").toString());
                        if (bodyObj.get("InstUniqueID") != null)
                            values.put("InstUniqID", bodyObj.get("InstUniqueID").toString());
                        if (bodyObj.get("Email") != null)
                            values.put("Email", bodyObj.get("Email").toString());
                        if (bodyObj.get("UserUUID") != null)
                            values.put("UserUUID", bodyObj.get("UserUUID").toString());
                        values.put("RegStatus", status);
                        database.update("UserList", values, " _id=" + id, null);
                    }

                    mcursor.close();
                    return MOMain.MO_REGSTATUS_ASSOS_REQSENT+":Success";
                    //	database.close();
            }
        } catch (Exception exp) {
            LogWriter.writeLog("UserSave",exp.getMessage());
            return MOMain.ERRORCODE_EXCEPTION+":Exception";
        }
       // return MOMain.ERRORCODE_EXCEPTION+":Installation ID not match";
    }

    public String processRegRequest(JSONObject userData) {
        try {
            if (database == null) return MOMain.ERRORCODE_DATABASE_ERROR+":Database Error";;
            synchronized (database) {
                if (!database.isOpen()) return  MOMain.ERRORCODE_DATABASE_ERROR+":Database Error";
                JSONObject bodyObj = new JSONObject(userData.get("body").toString());
                String userUUID = bodyObj.get("UserUUID").toString();
                String instUniqID= bodyObj.get("InstUniqueID").toString();
                String userName= bodyObj.get("Name").toString();
                String userMobile= bodyObj.get("Mobile").toString();
                String userEmail = bodyObj.get("Email").toString();
                String query = "select * from UserList where UserUUID like \""+userUUID+"\"";
                Cursor mcursor = database.rawQuery(query, null);

                if (mcursor == null) {
                    saveUserData(true,userName,userMobile,userUUID,instUniqID,userEmail);
                } else {
                    mcursor.moveToFirst();
                    int id = mcursor.getInt(0);
                    ContentValues values = new ContentValues();

                        values.put("FName", userName);

                        values.put("Mobile", userMobile);

                        values.put("InstUniqID", instUniqID);

                        values.put("Email", userEmail);

                        values.put("UserUUID", bodyObj.get("UserUUID").toString());

                    database.update("UserList", values, " _id=" + id, null);


                    mcursor.close();
                    return MOMain.ERRORCODE_SUCCESS + ":Success";
                }
                    //	database.close();
            }
        } catch (Exception exp) {
            LogWriter.writeLog("UserSave",exp.getMessage());
            return MOMain.ERRORCODE_EXCEPTION+":Exception";
        }
         return MOMain.ERRORCODE_EXCEPTION+":Installation ID not match";
    }

    public boolean checkPedoStepHigh(int stepCount) {
        if (database == null) return false;
        synchronized (database) {
            if (!database.isOpen()) return false;

            Calendar curDate = Calendar.getInstance();
            Calendar dayStart = Calendar.getInstance();
            dayStart.set(curDate.get(Calendar.YEAR), curDate.get(Calendar.MONTH), curDate.get(Calendar.DATE), 0, 0, 0);
            long curDayMills = dayStart.getTimeInMillis() + 60000; //to makse sure time falls in current day
            dayStart.add(Calendar.DAY_OF_MONTH, 1);
            long nextDay = dayStart.getTimeInMillis() - 60000;//to makse sure time falls in current day

            String query = "select _id from PedoData where PedoDate >=" + curDayMills + " and PedoDate <=" + nextDay + " and StepCount<=" + stepCount;
            Cursor mcursor = database.rawQuery(query, null);
            if (mcursor == null) return true;
            if (mcursor.getCount() > 0) // No record present for the day Insert new row
            {
                mcursor.close();
                return true;
            }
            mcursor.close();
        }
        return false;
    }


}
