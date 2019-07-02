package moffice.meta.com.molibrary.core;

import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.telephony.SmsManager;
import android.widget.Toast;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.messaging.FirebaseMessaging;
import com.j256.ormlite.android.apptools.OpenHelperManager;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.stmt.DeleteBuilder;
import com.j256.ormlite.stmt.QueryBuilder;
import com.j256.ormlite.stmt.UpdateBuilder;
import com.j256.ormlite.stmt.Where;

import org.json.JSONArray;
import org.json.JSONObject;

import java.sql.SQLException;
import java.util.List;
import java.util.UUID;

import moffice.meta.com.molibrary.firebase.MOMesgHandler;
import moffice.meta.com.molibrary.models.dbmodels.Offices;
import moffice.meta.com.molibrary.models.dbmodels.UserList;
import moffice.meta.com.molibrary.utility.CommonDataArea;
import moffice.meta.com.molibrary.utility.LogWriter;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by com.moffice.com.microoffice.app on 10/02/2017.
 */

public class MOUserManager {

    //Registration Section
    //Registration Process
    //1-- Invitation SMS send by com.moffice.com.microoffice.app
    //2-- User Reads the Invitaion SMS
    //3-- Send Registration Request to Admin with IMEI code
    //4-- Admin app accepts and verify against prev registration. If not registered store in
    //-- Database and send positive ack. If already exists and IMEI code same then accept and send
    //-- positive ack. Else send a negative ack
    //5--User app recives the invitation and save in DB/Registry and allow user to use app
    //6--User app on reciving negative ack mark that and display to user.
    public static String inviteAssociate(Context context, String assoContactName, String mobileNumber) {
        try {
            UUID uuid = UUID.randomUUID();
            String uuidStr = uuid.toString();

            SharedPreferences settings = context.getSharedPreferences(MOMain.SHAREDPREF_NAME, MODE_PRIVATE);
            String topic = settings.getString(MOMain.SHAREDPREF_TOPIC, "NA");
            String secret = settings.getString(MOMain.SHAREDPREF_SECRET, "NA");
            if (topic.contains("NA")) {
                Toast.makeText(context, "Please register before add associates", Toast.LENGTH_LONG).show();
                return null;
            }
            sendSMS(mobileNumber, "MicroOffice\r\nUUID:" + uuidStr + "\r\nComp:" + topic + "\r\nSec:"+secret+"\r\n");
            synchronized (CommonDataArea.dbLock) {
                    saveData(context,true, assoContactName, mobileNumber, uuidStr, "", "",MOMain.MO_DBUSERLIST_INVITED);
            }
            return uuidStr;
        } catch (Exception exp) {
            LogWriter.writeLogException("InviteAssocite", exp);
            return null;
        }
    }

    public static void sendUserList(final Context context, final String userUUID) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {

                    Dao<UserList, Integer> userList = CommonDataArea.getHelper(context).getUserListDao();
                    int i=0;
                    for( UserList user :userList ){

                        MOMesgHandler attnMesg = new MOMesgHandler();
                        attnMesg.MoMesgCreate(CommonDataArea.getUserUUID(), userUUID, MOMain.MOMESG_USERLIST_MESG);
                        attnMesg.addAttribute("Name", user.getName());
                        attnMesg.addAttribute("Mobile", user.getMobile());
                        attnMesg.addAttribute("UserUUID", user.getUserGUID());
                        attnMesg.addAttribute("Status", user.getStatus());
                        attnMesg.addAttribute("Email", user.getEmail());

                        ++i;
                        JSONObject mesg = attnMesg.assembleJSONMesg();
                        boolean sendSuccess = attnMesg.sendMessage(mesg);
                    }

                }catch(Exception exp){

                }
            }
        }).start();

    }
    public static void deleteUser(String UUid,Context context) throws SQLException {
        DeleteBuilder<UserList, Integer> deleteBuilder =
                MOMain.getHelper().getUserListDao().deleteBuilder();

        deleteBuilder.where().eq("UserGUID", UUid);
        deleteBuilder.delete();




    }
    public static JSONArray getUserList(Context context) {
        try {

            Dao<UserList, Integer> userList = CommonDataArea.getHelper(context).getUserListDao();

            JSONArray userListArray = new JSONArray();
            for( UserList user :userList ){
                JSONObject userJson = new JSONObject();
                userJson.put("Name", user.getName());
                userJson.put("Mobile", user.getMobile());
                userJson.put("UserUUID", user.getUserGUID());
                userJson.put("Status", user.getStatus());
                userJson.put("Email", user.getEmail());
                userListArray.put(userJson);

            }

            return userListArray;
        }catch(Exception exp){
            return null;
        }
    }


    public static int saveUserData(Context context,JSONObject userData, int status) {
        try {

            JSONObject bodyObj = new JSONObject(userData.get("body").toString());
            String userUUID = bodyObj.get("UserUUID").toString();
            String name = "";
            String mobile = "";
            String email = "";
            String uuid = "";

            if (bodyObj.get("Name") != null)
                name = bodyObj.get("Name").toString();
            if (bodyObj.get("Mobile") != null)
                mobile = bodyObj.get("Mobile").toString();
            if (bodyObj.get("UserUUID") != null)
                uuid = bodyObj.get("UserUUID").toString();
            if (bodyObj.get("Email") != null)
                email = bodyObj.get("Email").toString();


            return  saveData(context,false, name, mobile, uuid, "", email,status);

        } catch (Exception exp) {
            LogWriter.writeLog("UserSave", exp.getMessage());
            return 0;
        }
        // return MOMain.ERRORCODE_EXCEPTION+":Installation ID not match";
    }

    public static int saveUserData(Context context,JSONObject userData) {
        try {

            JSONObject bodyObj = new JSONObject(userData.get("body").toString());
            String userUUID = bodyObj.get("UserUUID").toString();
            String name = "";
            String mobile = "";
            String email = "";
            String uuid = "";
            int status=0;

            if (bodyObj.has("Name"))
                name = bodyObj.get("Name").toString();
            if (bodyObj.has("Mobile"))
                mobile = bodyObj.get("Mobile").toString();
            if (bodyObj.has("UserUUID"))
                uuid = bodyObj.get("UserUUID").toString();
            if (bodyObj.has("Email"))
                email = bodyObj.get("Email").toString();
            if (bodyObj.has("Status"))
                status = Integer.parseInt(bodyObj.get("Status").toString());

            return  saveData(context,false, name, mobile, uuid, "", email,status);

        } catch (Exception exp) {
            LogWriter.writeLog("UserSave", exp.getMessage());
            return 0;
        }
        // return MOMain.ERRORCODE_EXCEPTION+":Installation ID not match";
    }

    /*
    * Insert and update data into database
    * */
    public static int saveData(Context context,boolean insert, String userName, String number, String uuid, String iemiCode, String email,int status) {
        try {
            List<UserList> userList = null;
            try {
                // userList = MOMain.getHelper().getById(UserList.class, uuid);
                QueryBuilder<UserList, Integer> queryBuilder =
                        CommonDataArea.getHelper(context).getUserListDao().queryBuilder();
                // get the WHERE object to build our query
                Where<UserList, Integer> where = queryBuilder.where();
                // the name field must be equal to "foo"UserList
                where.in("UserGUID", uuid);
                where.or();
                where.like("Email","%"+email+"%");
                userList =queryBuilder.query();


                CommonDataArea.closeHelper();
            } catch (SQLException e) {
                LogWriter.writeLogException("saveData",e);
                e.printStackTrace();
            }

            if ((userList == null)||(userList.size()==0)) {
                Dao<UserList, Integer> userListDao = CommonDataArea.getHelper(context).getUserListDao();
                UserList userListInsert = new UserList();
                userListInsert.setMobile(number);
                userListInsert.setName(userName);
                userListInsert.setUserGUID(uuid);
                userListInsert.setStatus(status);
                userListDao.create(userListInsert);
                CommonDataArea.closeHelper();
                LogWriter.writeLog("saveData","User creation success");
                return 1;

            } else {
                Dao<UserList, Integer> techerDao = null;
                try {
                    techerDao = CommonDataArea.getHelper(context).getUserListDao();
                    UpdateBuilder<UserList, Integer> updateBuilder = techerDao.updateBuilder();
                    // update the goal_title and goal_why fields
                    updateBuilder.updateColumnValue("Name", userName);
                    updateBuilder.updateColumnValue("Mobile", number);
                    updateBuilder.updateColumnValue("IMENumber", iemiCode);
                    //updateBuilder.updateColumnValue("UserUUID", uuid);
                    updateBuilder.updateColumnValue("Email", email);
                    updateBuilder.updateColumnValue("Status", status);

                    // but only update the rows where the description is some value
                    updateBuilder.where().eq("_id", userList.get(0).get_id());
                    // actually perform the update
                    updateBuilder.update();
                    CommonDataArea.closeHelper();
                    LogWriter.writeLog("saveData","User update success");
                    return 2;
                } catch (SQLException e) {
                    LogWriter.writeLogException("saveData",e);
                    e.printStackTrace();
                    return 0;
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
            return 0;
        }

    }

    public static void setUserAsAdmin(Context context,String uuid,boolean enable){
        Dao<UserList, Integer> techerDao = null;
        try {
            techerDao = CommonDataArea.getHelper(context).getUserListDao();
            UpdateBuilder<UserList, Integer> updateBuilder = techerDao.updateBuilder();
            if(enable)
                updateBuilder.updateColumnValue("Status", MOMain.MO_DBUSERLIST_ASSOS_REGISTERED_ADMIN);
            else
                updateBuilder.updateColumnValue("Status", MOMain.MO_DBUSERLIST_ASSOS_REGISTERED);
            // but only update the rows where the description is some value
            updateBuilder.where().eq("UserGUID", uuid);
            // actually perform the update
            updateBuilder.update();
            CommonDataArea.closeHelper();

        } catch (SQLException e) {
            e.printStackTrace();

        }
    }

    private static void sendSMS(String phoneNumber, String message) {
        SmsManager sms = SmsManager.getDefault();
        sms.sendTextMessage(phoneNumber, null, message, null, null);
    }

    //Function returns true if it find an invitation
    public static boolean readInvitationSMS(Context context) throws Exception {
        if (CommonDataArea.getCommonSettings() == null) {
            Exception init = new Exception("readSettings not called");
        }
        try {
            //Check in the fisrt 100 sms only
            Uri uri = Uri.parse("content://sms/inbox");

            Cursor c = context.getContentResolver().query(uri, null, null, null, null);
            String body = null;
            String number = null;
            c.moveToFirst();
            int i = 0;
            while (i < 100) {
                body = c.getString(c.getColumnIndexOrThrow("body")).toString();
                number = c.getString(c.getColumnIndexOrThrow("address")).toString();

                if (body.contains("MicroOffice")) {
                    String uuid = MOMain.getUUID(body);
                    String topic = MOMain.getTopic(body);
                    String secret = MOMain.getSecret(body);
                    String adminMail = topic.replace('_','@');
                    if ((uuid != null) && (topic != null)) {
                        /*todo Visakh Display a Activity to display company Nick Name (extract from topic )
                        User UUID. Accept User Name, Email etc save in Shared pref
                        Send a GCM message to allow others to update it in their DB
                        */
                        FirebaseMessaging fm = FirebaseMessaging.getInstance();
                        String tocken = FirebaseInstanceId.getInstance().getToken();
                        fm.subscribeToTopic(topic);

                        SharedPreferences settings = context.getSharedPreferences(MOMain.SHAREDPREF_NAME, Context.MODE_PRIVATE);
                        SharedPreferences.Editor ed = settings.edit();
                        ed.putString(MOMain.SHAREDPREF_TOPIC, topic);
                        ed.putString(MOMain.SHAREDPREF_USERUUID, uuid);
                        ed.putString(MOMain.SHAREDPREF_ADMINEMAIL, adminMail);
                       // ed.putInt(MOMain.SHAREDPREF_REGREQSTATUS, 1);
                        ed.putString(MOMain.SHAREDPREF_SECRET, secret);
                        ed.commit();
                        CommonDataArea.getCommonSettings().setUserUUID(uuid);
                        CommonDataArea.getCommonSettings().setTopic(topic);
                        CommonDataArea.getCommonSettings().setSecret(secret);
                        CommonDataArea.getCommonSettings().setAdminEmail(adminMail);
                        return true;
                    }
                }
                c.moveToNext();
            }
            c.close();
        } catch (Exception exp) {
            LogWriter.writeLog("Read SMS", exp.getMessage());

        }
        //SMS not found set temp UUID
        LogWriter.writeLog("Read SMS","SMS not found creating temp UUID");
        SharedPreferences settings = context.getSharedPreferences(MOMain.SHAREDPREF_NAME, Context.MODE_PRIVATE);
        String uuidStr="0";
        uuidStr=settings.getString(MOMain.SHAREDPREF_TEMUSERUUID, "0");
        if(uuidStr.equals("0")) {
            SharedPreferences.Editor ed = settings.edit();
            UUID uuid = UUID.randomUUID();
            uuidStr = uuid.toString();
            ed.putString(MOMain.SHAREDPREF_TEMUSERUUID, uuidStr);
            ed.commit();
            CommonDataArea.setTempUUID(uuidStr);
        }
        return false;
    }

    public static void sendRegistrationRequest(Context context, String name, String mobile, String email, String adminEmail, String secret) {
        LogWriter.writeLog("RegRequest","Sending reg reuest");
        MOMesgHandler attnMesg = new MOMesgHandler();
        if(CommonDataArea.getUserUUID().equals("0")){
            SharedPreferences settings = context.getSharedPreferences(MOMain.SHAREDPREF_NAME, Context.MODE_PRIVATE);
            String uuidStr="0";
            uuidStr=settings.getString(MOMain.SHAREDPREF_TEMUSERUUID, "0");
            CommonDataArea.setTempUUID(uuidStr);
            attnMesg.MoMesgCreate(uuidStr, MOMain.MOMESG_TOALL, MOMain.MOMESG_REG);
        }else {
            attnMesg.MoMesgCreate(CommonDataArea.getUserUUID(), MOMain.MOMESG_TOALL, MOMain.MOMESG_REG);
        }
        attnMesg.addAttribute("Name", name);
        if (mobile == null) mobile = "0";
        attnMesg.addAttribute("Mobile", mobile);
        if (email == null) email = "_NA_";
        attnMesg.addAttribute("Email", email);
        attnMesg.addAttribute("AdminEmail", adminEmail);
        attnMesg.addAttribute("UserUUID", CommonDataArea.getUserUUID());
        attnMesg.addAttribute("SharedSecret", secret);
        String bleMacID = android.provider.Settings.Secure.getString(context.getContentResolver(), "bluetooth_address");
        if (bleMacID == null) bleMacID = "emulator_" + CommonDataArea.getUserUUID();
        attnMesg.addAttribute("InstUniqueID", bleMacID);

        JSONObject mesg = attnMesg.assembleJSONMesg();
        LogWriter.writeLog("RegReq",mesg.toString());
        boolean sendSuccess = attnMesg.sendMessageOnThread(mesg);
        SharedPreferences settings = context.getSharedPreferences(MOMain.SHAREDPREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor ed = settings.edit();
        ed.putInt(MOMain.SHAREDPREF_REGREQSTATUS, MOMain.MO_REGSTATUS_ASSOS_REQSENT);
        ed.commit();
    }

    public static void sendRegistrationAck(Context context, String toUserID,String reqUserUUID, String status) throws Exception {
        if (CommonDataArea.getCommonSettings() == null) {
            Exception init = new Exception("readSettings not called");
        }
        MOMesgHandler attnMesg = new MOMesgHandler();
        attnMesg.MoMesgCreate(CommonDataArea.getUserUUID(), toUserID, MOMain.MOMESG_REG_ACK);
        String[] parts = status.split(":");
        if ((parts != null) && (parts.length > 1)) {
            attnMesg.addAttribute("Status", parts[0]);
            attnMesg.addAttribute("Reason", parts[1]);
            attnMesg.addAttribute("UserUUID", reqUserUUID);
        } else {
            LogWriter.writeLog("Registration", "Status String error" + status);

            return;
        }
        String bleMacID = android.provider.Settings.Secure.getString(context.getContentResolver(), "bluetooth_address");
        attnMesg.addAttribute("InstUniqueID", bleMacID);

        JSONObject mesg = attnMesg.assembleJSONMesg();
        boolean sendSuccess = attnMesg.sendMessage(mesg);
    }

    public static void sendAdminEnabled(Context context, final String reqUserUUID,final boolean enable,boolean useThread)  {
        if(useThread) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    MOMesgHandler attnMesg = new MOMesgHandler();
                    attnMesg.MoMesgCreate(CommonDataArea.getUserUUID(), MOMain.MOMESG_TOALL, MOMain.MOMESG_SETUSER_ADMIN);
                    attnMesg.addAttribute("UserUUID", reqUserUUID);
                    attnMesg.addAttribute("Status", enable);
                    JSONObject mesg = attnMesg.assembleJSONMesg();
                    boolean sendSuccess = attnMesg.sendMessage(mesg);
                }
            }).start();

        }else {
            MOMesgHandler attnMesg = new MOMesgHandler();
            attnMesg.MoMesgCreate(CommonDataArea.getUserUUID(), MOMain.MOMESG_TOALL, MOMain.MOMESG_SETUSER_ADMIN);
            attnMesg.addAttribute("UserUUID", reqUserUUID);
            attnMesg.addAttribute("Status", enable);
            JSONObject mesg = attnMesg.assembleJSONMesg();
            boolean sendSuccess = attnMesg.sendMessage(mesg);
        }
    }


}
