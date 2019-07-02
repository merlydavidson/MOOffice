package moffice.meta.com.molibrary.firebase;

import android.content.Context;
import android.content.SharedPreferences;
import android.hardware.camera2.params.StreamConfigurationMap;
import android.widget.Toast;

import com.j256.ormlite.android.apptools.OpenHelperManager;

import org.json.JSONObject;

import java.util.UUID;

import moffice.meta.com.molibrary.core.MOAttenManager;
import moffice.meta.com.molibrary.core.MOEnquiryManager;
import moffice.meta.com.molibrary.core.MOLeaveManager;
import moffice.meta.com.molibrary.core.MOMain;
import moffice.meta.com.molibrary.core.MOOfficeLocManager;
import moffice.meta.com.molibrary.core.MOSupportManager;
import moffice.meta.com.molibrary.core.MOUserManager;
import moffice.meta.com.molibrary.core.MOVisitManager;
import moffice.meta.com.molibrary.core.Settings;
import moffice.meta.com.molibrary.database.DatabaseHelperORM;
import moffice.meta.com.molibrary.database.MODatabaseAdapter;
import moffice.meta.com.molibrary.utility.CommonDataArea;
import moffice.meta.com.molibrary.utility.LogWriter;

/**
 * Created by com.moffice.com.microoffice.app on 02/01/2017.
 */

public class MOMesgReceiver {
    String mesgType;
    String mesgTo;
    String mesgFrom;

    public void processMessage(Context context, JSONObject mesg) {
        try {
            mesgType = mesg.get("type").toString();
            mesgTo = mesg.get("MesgTo").toString(); //check mesg to is either users UUID or "ALL"
            //Users UUID stored in shared pref as "MyUUID"
            mesgFrom = mesg.get("PostBy").toString();
            LogWriter.writeLog("MesgProcessing", mesgType);
            if (CommonDataArea.getCommonSettings() == null) {
                MOMain.readSettings(context);
            }
            if (CommonDataArea.getUserUUID().contains(mesgFrom)) return;
            switch (mesgType) {
                case MOMain.MOMESG_REG:
                    LogWriter.writeLog("REG REQUEST","Reg request recvd");
                    if (CommonDataArea.getInstType() == MOMain.MO_INSTTYPE_ADMIN) //process only if com.moffice.com.microoffice.app
                    {
                        LogWriter.writeLog("REG REQUEST","Reg request recvd-Processing Admin");
                        String replyTo = mesgFrom;
                        JSONObject bodyObj = new JSONObject(mesg.get("body").toString());
                        String adminEmail = bodyObj.get("AdminEmail").toString();
                        String sharedSecret = bodyObj.get("SharedSecret").toString();
                        String storedEmail = CommonDataArea.getCommonSettings().getAdminEmail();
                        storedEmail=storedEmail.replace('@','_');

                        if ((sharedSecret.contains(CommonDataArea.getCommonSettings().getSecret())) && (adminEmail.contains(storedEmail))) {
                            processRegUserData(context, mesg, MOMain.MO_DBUSERLIST_ASSOS_REGISTERED);
                        } else {
                            String status = MOMain.ERRORCODE_PASSWORD_MISMATCH + ":Failed";
                            MOUserManager.sendRegistrationAck(context, replyTo,replyTo, status);
                        }
                    }
                    break;
                case MOMain.MOMESG_OFFICELOC_MESG:
                    processOfficeLocationMsg(context, mesg);
                    break;
                case MOMain.MOMESG_REG_ACK:
                    processRegAck(context, mesg);
                    break;
                case MOMain.MOMESG_ATTNMARK_REQ:
                    MOAttenManager.SaveRcvdAttendance(context, mesg);
                    break;
                case MOMain.MOMESG_LEAVE_REQ:
                    MOLeaveManager.saveRcvdLeave(context, mesg);
                    break;
                case MOMain.MOMESG_VISITLOG_MESG:
                    MOVisitManager.SaveRcvdvisit(context,mesg);
                   break;
                case MOMain.MOMESG_SUPLOG_MESG:
                    MOSupportManager.SaveRcvdSupport(context,mesg);
                    break;
                case MOMain.MOMESG_ENQLOG_MESG:
                    MOEnquiryManager.SaveRcvdEnq(context,mesg);
                    break;
                case MOMain.MOMESG_VISMEET_MSG:
                    MOVisitManager.SaveRcvdVisMeetLog(context,mesg);
                    break;
                case MOMain.MOMESG_OFFICELOC_REQ:
                    if (CommonDataArea.getInstType() == MOMain.MO_INSTTYPE_ADMIN) {
                        String userUUID = mesg.get("PostBy").toString();
                        MOOfficeLocManager.sendOfficeLocations(context, userUUID);
                    }
                    break;
                case MOMain.MOMESG_PRODLIST_MESG:
                    MOMain.saveProdData(context,mesg);
                    break;
                case MOMain.MOMESG_USERLIST_MESG:
                    MOUserManager.saveUserData(context,mesg);
                    break;
                case MOMain.MOMESG_SETUSER_ADMIN:
                    JSONObject bodyObj = new JSONObject(mesg.get("body").toString());
                    String uuid = bodyObj.get("UserUUID").toString();
                    boolean sttaus = (boolean)bodyObj.get("Status");
                    MOUserManager.setUserAsAdmin(context,uuid,sttaus);
                    if(CommonDataArea.getUserUUID().contentEquals(uuid)){
                        Settings set = CommonDataArea.getCommonSettings();
                        if(sttaus) {
                            set.setInstType(MOMain.MO_INSTTYPE_ADMIN);
                            MOMain.setInstallationType(context, MOMain.MO_INSTTYPE_ADMIN);
                        }else {
                            set.setInstType(MOMain.MO_INSTTYPE_ASSOS);
                            MOMain.setInstallationType(context, MOMain.MO_INSTTYPE_ASSOS);
                        }
                    }
                    break;

            }
        } catch (Exception exp) {
            LogWriter.writeLogException("ProcessingMesg", exp);
        }
    }


    public static void processOfficeLocationMsg(Context context, JSONObject mesg) {
        try {
            JSONObject mesgObj = mesg.getJSONObject("body");
            String officeName = mesgObj.getString("OfficeName");
            String longi = mesgObj.getString("Longi");
            String lati = mesgObj.getString("Lati");
            String officeAddr = mesgObj.getString("Address");
            String officeID = mesgObj.getString("OfficeID");

            MOOfficeLocManager.saveOfficeLocation(officeName, officeID, longi, lati, officeAddr, context);
        } catch (Exception exp) {

        }
    }

    void processRegAck(Context context, JSONObject mesg) {
        try {
            JSONObject bodyObj = new JSONObject(mesg.get("body").toString());
            //String userUUID = mesg.get("MesgTo").toString();
            String status = bodyObj.get("Status").toString();
            String userUUID = bodyObj.get("UserUUID").toString();
            String toUUID = mesg.getString("MesgTo");

            String statusVal = Integer.toString(MOMain.MO_DBUSERLIST_ASSOS_REGISTERED, 100);
            LogWriter.writeLog("ProcessUserRegAck","Status returned->"+statusVal);
            if ((status.equals(statusVal)) && (userUUID.equals(CommonDataArea.getUserUUID()))) {
                LogWriter.writeLog("ProcessUserRegAck","Reg ack for this user and reg success");
                SharedPreferences settings = context.getSharedPreferences(MOMain.SHAREDPREF_NAME, Context.MODE_PRIVATE);
                SharedPreferences.Editor ed = settings.edit();
                ed.putInt(MOMain.SHAREDPREF_REGREQSTATUS, MOMain.MO_REGSTATUS_ASSOS_REGISTERED); //Registartion completed
                ed.putBoolean(MOMain.SHAREDPREF_REGISTERED, true);
                ed.commit();

                Settings set =CommonDataArea.getCommonSettings();
                set.setRegReqStatus(MOMain.MO_REGSTATUS_ASSOS_REGISTERED);
                CommonDataArea.setCommonSettings(set);

            } else if ((status.equals(statusVal)) && (toUUID.equals(CommonDataArea.getTempUUID()))) {
                LogWriter.writeLog("ProcessUserRegAck","Reg ack for this user and reg success");
                SharedPreferences settings = context.getSharedPreferences(MOMain.SHAREDPREF_NAME, Context.MODE_PRIVATE);
                SharedPreferences.Editor ed = settings.edit();
                ed.putInt(MOMain.SHAREDPREF_REGREQSTATUS, MOMain.MO_REGSTATUS_ASSOS_REGISTERED); //Registartion completed
                ed.putBoolean(MOMain.SHAREDPREF_REGISTERED, true);
                ed.putString(MOMain.SHAREDPREF_USERUUID, userUUID);
                ed.commit();

                Settings set =CommonDataArea.getCommonSettings();
                set.setRegReqStatus(MOMain.MO_REGSTATUS_ASSOS_REGISTERED);
                CommonDataArea.setCommonSettings(set);

            }else if(status.equals(MOMain.ERRORCODE_PASSWORD_MISMATCH)){
                LogWriter.writeLog("User Reg","Password/User error");
                Settings set =CommonDataArea.getCommonSettings();
                set.setRegReqStatus(MOMain.MO_REGSTATUS_ASSOS_REJECTED);
                CommonDataArea.setCommonSettings(set);
                CommonDataArea.setRegErrorReason("Password/User error");
            }
            else if(!status.equals("0")){
                LogWriter.writeLog("User Reg","User creation failed");
                Settings set =CommonDataArea.getCommonSettings();
                set.setRegReqStatus(MOMain.MO_REGSTATUS_ASSOS_REJECTED);
                CommonDataArea.setCommonSettings(set);
                CommonDataArea.setRegErrorReason("User creation failed");
            }
        } catch (Exception exp) {

        }
    }

    void processRegUserData(Context context, JSONObject mesg, int statusVal) {
        try {
            LogWriter.writeLog("processRegUserData","Function entry");
            JSONObject bodyObj = new JSONObject(mesg.get("body").toString());
            String userUUID = bodyObj.get("UserUUID").toString();
            String toUUID=mesg.getString("PostBy");
            if(userUUID.equals("0")){
                //Create an ID for new user
                UUID uuid = UUID.randomUUID();
                userUUID = uuid.toString();
                bodyObj.put("UserUUID",userUUID);
                mesg.put("body",bodyObj);
            }
            int status = MOUserManager.saveUserData(context, mesg, MOMain.MO_DBUSERLIST_ASSOS_REGISTERED);
            if (status > 0) {
                //Send Office locations
                LogWriter.writeLog("processRegUserData","Sending reg success ack");
                MOUserManager.sendRegistrationAck(context, toUUID, userUUID,String.valueOf(MOMain.MO_DBUSERLIST_ASSOS_REGISTERED) + ":OK");
                //User userID as to from now on
                LogWriter.writeLog("processRegUserData","Sending office list");
                MOOfficeLocManager.sendOfficeLocations(context, toUUID);
                LogWriter.writeLog("processRegUserData","Sending product list");
                MOMain.sendProdData(context,true);
                LogWriter.writeLog("processRegUserData","Sending User list");
                MOUserManager.sendUserList(context,userUUID);
            } else {
                LogWriter.writeLog("processRegUserData","Sending office list");
                MOUserManager.sendRegistrationAck(context, toUUID, userUUID,"0:Failed");
            }
        } catch (Exception exp) {
            LogWriter.writeLog("REgistration", "Exception->" + exp.getMessage());
        }
        //mesgRep.
    }
}
