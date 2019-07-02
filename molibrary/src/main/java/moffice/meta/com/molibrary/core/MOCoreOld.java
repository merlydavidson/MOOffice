package moffice.meta.com.molibrary.core;

import android.content.ContentResolver;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.location.Location;
import android.provider.ContactsContract;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.messaging.FirebaseMessaging;

import java.util.ArrayList;
import java.util.UUID;

import moffice.meta.com.molibrary.utility.CommonDataArea;
import moffice.meta.com.molibrary.utility.LogWriter;

/**
 * Created by com.moffice.com.microoffice.app on 02/07/2017.
 */


public class MOCoreOld {

    Context context;
    private static ArrayList<String> contctNumbers;
    private static ArrayList<String> contctNames;

    //Shared pref settings names
    public static String SHAREDPREF_NAME = "MicroOfficeNew";
    public static String SHAREDPREF_TOPIC = "Topic";
    public static String SHAREDPREF_ADMINEMAIL = "AdminEmail";
    public static String SHAREDPREF_SECRET = "Secret";
    public static String SHAREDPREF_USERUUID = "MyUUID";
    public static String SHAREDPREF_IMEINUMBER = "IMEINumber";
    public static String SHAREDPREF_REGISTERED = "Registered";
    public static String SHAREDPREF_REGREQSTATUS = "RegReqStatus";
    public static String SHAREDPREF_USERNAME = "Name";
    public static String SHAREDPREF_MOBILE = "Mobile";
    public static String SHAREDPREF_COMPANY = "Company";
    public static String SHAREDPREF_EMAIL = "Email";
    public static String SHAREDPREF_INSTTYPE = "InstType";
    public static String SHAREDPREF_INSTTYPESET = "InstTypeSet";
    public static String SHAREDPREF_OFFICEENABLED = "Office_Enabled_";
    public static String SHAREDPREF_OFFICELATI = "Office_Lat";
    public static String SHAREDPREF_OFFICELONGI = "Office_Long";
    public static String SHAREDPREF_OFFICENAME = "Office_Name_";

    public final static int MO_INSTTYPE_ADMIN = 1;
    public final static int MO_INSTTYPE_ASSOS = 2;
    public final static int MO_REGSTATUS_ASSOS_NOTSAVED = 0; //Registration Pending need to send request
    public final static int MO_REGSTATUS_ASSOS_PENDING = 1; //Registration Pending need to send request
    public final static int MO_REGSTATUS_ASSOS_REQSENT = 2; //Registration request send to com.moffice.com.microoffice.app and reply pending
    public final static int MO_REGSTATUS_ASSOS_REJECTED = 3;
    public final static int MO_REGSTATUS_ASSOS_REGISTERED = 4;

    public final static int MO_DBUSERLIST_SAVED = 0; //Just created a new user in DB
    public final static int MO_DBUSERLIST_INVITED = 1; //Administrator send invitation SMS
    public final static int MO_DBUSERLIST_REG_REQ_REJECTED = 2; //Registration request send by assos rejected due to pwd error
    public final static int MO_DBUSERLIST_REG_REQ_SEND = 3;// Reg request send to com.moffice.com.microoffice.app
    public final static int MO_DBUSERLIST_ASSOS_REGISTERED = 100;

    public final static String MOMESG_TOALL = "TOALL";
    public final static String MOMESG_REG = "RQ_REG";
    public final static String MOMESG_REG_ACK = "AK_REG";
    public final static String MOMESG_USERLIST_REQ = "RQ_ULIS";
    public final static String MOMESG_USERLIST_ACK = "AK_ULIS";
    public final static String MOMESG_ATTNMARK_REQ = "RQ_ATM";
    public final static String MOMESG_OFFICELOC_REQ = "RQ_OLC";
    public final static String MOMESG_OFFICELOC_MESG = "MS_OLC";

    /*
        public static String CMD_USER_GETLIST = "ULIS"; //get user list
        public static String CMD_USER_DAT = "UDAT";     //User data update command
        public static String CMD_USER_REG = "UREG";
        public static String CMD_ATTN_ENTRY = "AENT";
        public static String CMD_ATTN_UPD = "AUPD";
        public static String CMD_ENQ_ENT = "EENT";
        public static String CMD_ENQ_NOTE = "ENOT";
        public static String CMD_OFFICELOC_1 = "OLO1";
        public static String CMD_OFFICELOC_2 = "OLO2";
        public static String CMD_OFFICELOC_3 = "OLO3";*/
    public final static String ERRORCODE_SUCCESS = "100";
    public final static String ERRORCODE_DATABASE_ERROR = "101";
    public final static String ERRORCODE_EXCEPTION = "102";
    public final static String ERRORCODE_USER_NOT_EXIST = "103";
    public final static String ERRORCODE_USER_INSTID_NOT_MATCH = "104";
    public final static String ERRORCODE_PASSWORD_MISMATCH = "105";


    public MOCoreOld(Context context) {
        this.context = context;
    }

    public static ArrayList<String> getContctNames() {
        return contctNames;
    }

    public static ArrayList<String> getContctNumbers() {
        return contctNumbers;
    }

    boolean coreInit(Context context) {
        this.context = context;
        return true;
    }


    public static void init(Context context) {
        readSettings(context);
        registerTopic();
    }

    static void registerTopic() {
        try {
            String topic = CommonDataArea.getCommonSettings().getTopic();
            if (!topic.contains("_NA_")) {
                FirebaseMessaging.getInstance().subscribeToTopic("/topics/meta");//To recive application wide messages
                topic = "/topics/" + topic;
                FirebaseMessaging.getInstance().subscribeToTopic(topic);
            }
        } catch (Exception exp) {
            LogWriter.writeLog("FirebaseError", exp.getMessage());
        }
    }

    public static int fetchContactsFromPhone(Context context, String search) {
        contctNames = new ArrayList<String>();
        contctNumbers = new ArrayList<String>();
        ContentResolver cr = context.getContentResolver();
        Cursor cur = cr.query(ContactsContract.Contacts.CONTENT_URI, null, null, null, null);
        if (cur.getCount() > 0) {
            while (cur.moveToNext()) {
                String id = cur.getString(cur.getColumnIndex(
                        ContactsContract.Contacts._ID));
                String name = cur.getString(cur.getColumnIndex(
                        ContactsContract.Contacts.DISPLAY_NAME));
                if ((search != null) && (search.length() > 0))
                    if (!name.contains(search)) continue;
                if (Integer.parseInt(cur.getString(cur.getColumnIndex(
                        ContactsContract.Contacts.HAS_PHONE_NUMBER))) > 0) {
                    Cursor pCur = cr.query(
                            ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                            null,
                            ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = ?",
                            new String[]{id}, null);
                    while (pCur.moveToNext()) {
                        int phoneType = pCur.getInt(pCur.getColumnIndex(
                                ContactsContract.CommonDataKinds.Phone.TYPE));
                        String phoneNumber = pCur.getString(pCur.getColumnIndex(
                                ContactsContract.CommonDataKinds.Phone.NUMBER));
                        if (phoneType == ContactsContract.CommonDataKinds.Phone.TYPE_MOBILE) {
                            contctNames.add(name + "(" + phoneNumber + ")");
                            contctNumbers.add(phoneNumber);
                        }
                    }
                    pCur.close();
                }
            }
        }
        return contctNames.size();
    }

    static String getUUID(String body) {
        int uuidIndxStrt = body.indexOf("UUID", 0);
        if (uuidIndxStrt > 0) {
            int uuidIndxEnd = body.indexOf("Comp", uuidIndxStrt);
            if (uuidIndxEnd > 0) {
                String uuid = body.substring(uuidIndxStrt + 5, uuidIndxEnd);
                return uuid.trim();
            } else return null;
        } else return null;
    }

    static String getTopic(String body) {
        int tIndxStrt = body.indexOf("Comp:", 0);
        if (tIndxStrt > 0) {
            String topic = body.substring(tIndxStrt + 5);
            return topic;
        } else return null;
    }

    public static Settings readSettings(Context context) {
        SharedPreferences settingsReader = context.getSharedPreferences(SHAREDPREF_NAME, Context.MODE_PRIVATE);
        Settings settings = new Settings();

        settings.setRegStatus(settingsReader.getBoolean(SHAREDPREF_REGISTERED, false));
        settings.setName(settingsReader.getString(SHAREDPREF_USERNAME, "_NA_"));
        settings.setMobile(settingsReader.getString(SHAREDPREF_MOBILE, "_NA_"));
        settings.setCompany(settingsReader.getString(SHAREDPREF_COMPANY, "_NA_"));
        settings.setEmail(settingsReader.getString(SHAREDPREF_EMAIL, "_NA_"));
        settings.setInstType(settingsReader.getInt(SHAREDPREF_INSTTYPE, 0));
        settings.setInstTypeSet(settingsReader.getBoolean(SHAREDPREF_INSTTYPESET, false));
        settings.setUserUUID(settingsReader.getString(SHAREDPREF_USERUUID, "0"));
        settings.setRegStatus(settingsReader.getBoolean(SHAREDPREF_REGISTERED, false));
        settings.setRegReqStatus(settingsReader.getInt(SHAREDPREF_REGREQSTATUS, 0));
        settings.setSecret(settingsReader.getString(SHAREDPREF_SECRET, "1234567890"));
        settings.setAdminEmail(settingsReader.getString(SHAREDPREF_ADMINEMAIL, "#NOEMAIL#"));
        settings.setTopic(settingsReader.getString(SHAREDPREF_TOPIC, "NA"));

        if (settings.isRegStatus()) {

            //Read office locations
            for (int i = 0; i < 5; ++i) {
                // settings.offices[i] = new Settings.OfficeLocs();
                settings.offices[i].enabled = settingsReader.getBoolean(SHAREDPREF_OFFICEENABLED + i, false);
                double longi, lati;
                String lalo = settingsReader.getString(SHAREDPREF_OFFICELATI + i, "0");
                lati = Double.valueOf(lalo);
                lalo = settingsReader.getString(SHAREDPREF_OFFICELONGI + i, "0");
                longi = Double.valueOf(lalo);
                settings.offices[i].loc = new Location("GPS");
                settings.offices[i].loc.setLongitude(longi);
                settings.offices[i].loc.setLatitude(lati);
                settings.offices[i].officeName = settingsReader.getString(SHAREDPREF_OFFICENAME + i, "-NA-");
            }

        }
        CommonDataArea.setCommonSettings(settings);

        return settings;
    }

    private static String createCompanyTopic(Context context) throws Exception {
        if (CommonDataArea.getCommonSettings() == null) {
            Exception init = new Exception("readSettings not called");
        }
        String topic;
        Settings settingd = CommonDataArea.getCommonSettings();
        SharedPreferences settings = context.getSharedPreferences("MicroOffice", Context.MODE_PRIVATE);
        topic = settings.getString(SHAREDPREF_TOPIC, "NOTOPIC");
        if (topic.equals("NOTOPIC")) {
            SharedPreferences.Editor ed = settings.edit();
            String companyEmail = settingd.getAdminEmail();
            if ((companyEmail.length() > 4) && (!companyEmail.contains("NA"))) {

               /* topic = companyEmail + "_" + android.provider.Settings.Secure.getString(context.getContentResolver(), "bluetooth_address");
                topic = topic.replace(':', '_');*/
                topic = companyEmail.replace('@', '_');
                ;
                FirebaseMessaging fm = FirebaseMessaging.getInstance();
                String tocken = FirebaseInstanceId.getInstance().getToken();
                fm.subscribeToTopic(topic);
                settingd.setTopic(topic);
                ed.putString(SHAREDPREF_TOPIC, topic);
                ed.commit();
                return "SUCCESS:TopicName:" + topic;
            } else return "FAIL:Company Name failed to meet criteria:" + companyEmail;
        } else return "FAIL:Topic already exists:" + topic;

    }

    public static String saveSettinsAssos(Context context, Settings settingd) throws Exception {
        SharedPreferences settings = context.getSharedPreferences("MicroOffice", Context.MODE_PRIVATE);
        CommonDataArea.setCommonSettings(settingd);
        SharedPreferences.Editor ed = settings.edit();
        ed.putString(SHAREDPREF_USERNAME, settingd.getName());
        ed.putString(SHAREDPREF_MOBILE, settingd.getMobile());
        ed.putString(SHAREDPREF_EMAIL, settingd.getEmail());
        ed.putString(SHAREDPREF_SECRET, settingd.getSecret());
        ed.putString(SHAREDPREF_ADMINEMAIL, settingd.getAdminEmail());
        if (settingd.getRegReqStatus() == MO_REGSTATUS_ASSOS_NOTSAVED) {
            ed.putInt(SHAREDPREF_REGREQSTATUS, MO_REGSTATUS_ASSOS_PENDING);
            UUID uuid = UUID.randomUUID();
            String uuidStr = uuid.toString();
            ed.putString(SHAREDPREF_USERUUID, uuidStr);
        }
        ed.commit();
        readSettings(context);//To Update the main settings object stored in CommonDataArea
        return createCompanyTopic(context);

    }

    public static String saveSettingAdmin(Context context, Settings settingd) throws Exception {
        try {
            SharedPreferences settings = context.getSharedPreferences("MicroOffice", Context.MODE_PRIVATE);
            boolean registered = settings.getBoolean(SHAREDPREF_REGISTERED, false);
            if (!registered) {
                settingd.setEmail(settingd.getAdminEmail()); //set same email id as com.moffice.com.microoffice.app email
                SharedPreferences.Editor ed = settings.edit();
                ed.putString(SHAREDPREF_USERNAME, settingd.getName());
                ed.putString(SHAREDPREF_MOBILE, settingd.getMobile());
                ed.putString(SHAREDPREF_COMPANY, settingd.getCompany());
                ed.putString(SHAREDPREF_EMAIL, settingd.getEmail());
                ed.putString(SHAREDPREF_ADMINEMAIL, settingd.getAdminEmail());
                ed.putString(SHAREDPREF_USERUUID, settingd.getUserUUID());
                ed.putString(SHAREDPREF_SECRET, settingd.getSecret());

                ed.putBoolean(SHAREDPREF_REGISTERED, true);
                ed.commit();
                readSettings(context);//To Update the main settings object stored in CommonDataArea
                return createCompanyTopic(context);

            } else {
                String email = settings.getString(SHAREDPREF_EMAIL, "#NOEMAIL#");
                if (email.contains("#NOEMAIL#") || email.equals(settingd.email)) {
                    SharedPreferences.Editor ed = settings.edit();
                    ed.putString(SHAREDPREF_USERNAME, settingd.getName());
                    ed.putString(SHAREDPREF_MOBILE, settingd.getMobile());
                    ed.putString(SHAREDPREF_COMPANY, settingd.getCompany());
                    ed.putString(SHAREDPREF_ADMINEMAIL, settingd.getAdminEmail());
                    ed.putString(SHAREDPREF_EMAIL, settingd.getEmail());
                    ed.putString(SHAREDPREF_USERUUID, settingd.getUserUUID());
                    ed.putString(SHAREDPREF_SECRET, settingd.getSecret());
                    ed.putBoolean(SHAREDPREF_REGISTERED, true);
                    ed.commit();
                    readSettings(context);//To Update the main settings object stored in CommonDataArea
                    return "SUCCESS";
                } else return "FAIL:Email canot be changed";
            }
        } catch (Exception exp) {
            return "EXCEPTION:" + exp.getMessage();
        }
    }

    public static void setInstallationType(Context context, int type) throws Exception {
        if (CommonDataArea.getCommonSettings() == null) {
            Exception init = new Exception("readSettings function not called.");
            throw init;
        }
        SharedPreferences settings = context.getSharedPreferences("MicroOffice", Context.MODE_PRIVATE);
        SharedPreferences.Editor edit = settings.edit();
        edit.putInt(SHAREDPREF_INSTTYPE, type);
        edit.putBoolean(SHAREDPREF_INSTTYPESET, true);
        edit.commit();
        CommonDataArea.getCommonSettings().setInstType(type);
        CommonDataArea.getCommonSettings().setInstTypeSet(true);
    }



}
