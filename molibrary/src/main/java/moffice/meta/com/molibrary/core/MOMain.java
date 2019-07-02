package moffice.meta.com.molibrary.core;

import android.content.ContentResolver;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.location.Location;
import android.os.Environment;
import android.provider.ContactsContract;
import android.telephony.SmsManager;
import android.widget.Toast;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.messaging.FirebaseMessaging;
import com.j256.ormlite.android.apptools.OpenHelperManager;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.stmt.QueryBuilder;
import com.j256.ormlite.stmt.Where;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.PrintWriter;
import java.net.URL;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import java.util.UUID;

import jxl.Workbook;
import jxl.WorkbookSettings;
import jxl.format.Colour;
import jxl.write.Label;
import jxl.write.WritableCellFormat;
import jxl.write.WritableHyperlink;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import jxl.write.WriteException;
import moffice.meta.com.molibrary.database.DatabaseHelperORM;
import moffice.meta.com.molibrary.firebase.MOMesgHandler;
import moffice.meta.com.molibrary.models.dbmodels.AttnLog;
import moffice.meta.com.molibrary.models.dbmodels.EnquiryDetails;
import moffice.meta.com.molibrary.models.dbmodels.LeaveList;
import moffice.meta.com.molibrary.models.dbmodels.Products;
import moffice.meta.com.molibrary.models.dbmodels.SupportDetails;
import moffice.meta.com.molibrary.models.dbmodels.UserList;
import moffice.meta.com.molibrary.models.dbmodels.VisitLog;
import moffice.meta.com.molibrary.models.dbmodels.VisitMeetLog;
import moffice.meta.com.molibrary.utility.CommonDataArea;
import moffice.meta.com.molibrary.utility.LogWriter;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by com.moffice.com.microoffice.app on 02/07/2017.
 */


public class MOMain {

    static Context context;
    private static ArrayList<String> contctNumbers;
    private static ArrayList<String> contctNames;

    //Shared pref settings names
    public static String SHAREDPREF_NAME = "MicroOffice1";
    public static String SHAREDPREF_TOPIC = "Topic";
    public static String SHAREDPREF_ADMINEMAIL = "AdminEmail";
    public static String SHAREDPREF_SECRET = "Secret";
    public static String SHAREDPREF_USERUUID = "MyUUID";
    public static String SHAREDPREF_TEMUSERUUID = "TemUUID";
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
    public final static int MO_DBUSERLIST_ASSOS_REGISTERED_ADMIN = 200;
    public final static int MO_MEETING_ITEM_NEW = 0;
    public final static int MO_MEETING_ITEM_APPROVED = 1;

    public final static String MOMESG_TOALL = "TOALL";
    public final static String MOMESG_REG = "RQ_REG";
    public final static String MOMESG_REG_ACK = "AK_REG";
    public final static String MOMESG_USERLIST_REQ = "RQ_ULIS";
    public final static String MOMESG_USERLIST_ACK = "AK_ULIS";
    public final static String MOMESG_ATTNMARK_REQ = "RQ_ATM";
    public final static String MOMESG_LEAVE_REQ = "RQ_LVM";
    public final static String MOMESG_OFFICELOC_REQ = "RQ_OLC";
    public final static String MOMESG_OFFICELOC_MESG = "MS_OLC";
    public final static String MOMESG_VISITLOG_MESG = "RQ_VIS";
    public final static String MOMESG_USERLIST_MESG = "MS_ULIS";
    public final static String MOMESG_ENQLOG_MESG = "MS_ENQ";
    public final static String MOMESG_SUPLOG_MESG = "MS_SUP";
    public final static String MOMESG_SETUSER_ADMIN = "CM_UAD";
    public final static String MOMESG_VISMEET_MSG = "RQ_MEET";
    public final static String MOMESG_PRODLIST_MESG = "MS_PROD";

    public static String SHAREDPREF_LAST_VACUUM_TIME = "last_vacuum_time";
    public static String SHAREDPREF_INSTALLED_AS_ADMIN = "InstalledAsAdmin";


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

    public final static int SENDSTATUS_NOT_SEND = 0;
    public final static int SENDSTATUS_SEND_COMPLETED= 1;
    public final static int RECSTATUS_ACTIVE= 1;
    public final static int RECSTATUS_DELETED= 0;
    /**
     * 1. Reuested
     * 2. Approved
     * 3. Rejected
     * 4. Discarded
     */
    public final static int LEAVESTATUS_REQUESTED = 1;
    public final static int LEAVESTATUS_APROVED = 2;
    public final static int LEAVESTATUS_REJECTED = 3;
    public final static int LEAVESTATUS_DISCARDED = 4;

    public final static int LEAVETYPE_CASUAL =1;
    public final static int LEAVETYPE_EARNED = 2;
    public final static int LEAVETYPE_LOP = 3;
    public final static int LEAVETYPE_SICK = 4;


    static SharedPreferences sharedpreferences;
    SharedPreferences.Editor editor;

    static DatabaseHelperORM databaseHelper = null;

    public MOMain(Context context) {
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

    // This is how, DatabaseHelperORM can be initialized for future use
    public static DatabaseHelperORM getHelper() {
        if (databaseHelper == null) {
            databaseHelper = OpenHelperManager.getHelper(context, DatabaseHelperORM.class);
        }
        return databaseHelper;
    }


    public static void setInstalledAsAdmin(Context context) throws Exception {
        SharedPreferences settings = context.getSharedPreferences(SHAREDPREF_NAME, MODE_PRIVATE);
        SharedPreferences.Editor edit = settings.edit();
        edit.putBoolean(SHAREDPREF_INSTALLED_AS_ADMIN, true);

        edit.commit();
    }

    /*
   * Insert and update data into database
   * */
    public static void saveRegistrationData(UserList userListRegister) {
        try {
            UserList userList = null;
            try {
                userList = getHelper().getById(UserList.class, userListRegister.getEmail());
            } catch (SQLException e) {
                e.printStackTrace();
            }

//            if(userList==null){
//                Dao<UserList, Integer> userListDao = getHelper().getUserListDao();
//                UserList userListInsert = new UserList();
//                userListInsert.setMobile(number);
//                userListInsert.setFName(userName);
//                userListInsert.setRegStatus(MO_DBUSERLIST_INVITED);
//                userListDao.create(userListInsert);
//            }else{
//                Dao<UserList, Integer> techerDao = null;
//                try {
//                    techerDao = getHelper().getUserListDao();
//                    UpdateBuilder<UserList, Integer> updateBuilder = techerDao.updateBuilder();
//                    // update the goal_title and goal_why fields
//                    updateBuilder.updateColumnValue("FName", userName);
//                    updateBuilder.updateColumnValue("Mobile", number);
//                    updateBuilder.updateColumnValue("IMEIcode", iemiCode);
//                    updateBuilder.updateColumnValue("UserUUID", uuid);
//                    updateBuilder.updateColumnValue("Email", email);
//                    updateBuilder.updateColumnValue("RegStatus", MOMain.MO_DBUSERLIST_INVITED);
//
//                    // but only update the rows where the description is some value
//                    updateBuilder.where().eq("_id",userList.get_id());
//                    // actually perform the update
//                    updateBuilder.update();
//                } catch (SQLException e) {
//                    e.printStackTrace();
//                }
//            }

        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    public static void init(Context context) {
        readSettings(context);
        registerTopic();
    }

    static void registerTopic() {
        try {
            String topic = CommonDataArea.getCommonSettings().getTopic();
            FirebaseMessaging.getInstance().subscribeToTopic("/topics/meta");//To recive application wide messages
            if (!topic.contains("NOTOPIC")) {
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

    private static void sendSMS(String phoneNumber, String message) {
        SmsManager sms = SmsManager.getDefault();
        sms.sendTextMessage(phoneNumber, null, message, null, null);
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
        int uuidIndxStrt = body.indexOf("Comp:", 0);
        if (uuidIndxStrt > 0) {
            int uuidIndxEnd = body.indexOf("Sec:", uuidIndxStrt);
            if (uuidIndxEnd > 0) {
                String topic = body.substring(uuidIndxStrt + 5, uuidIndxEnd);
                return topic.trim();
            } else return null;
        } else return null;
    }


    static String getSecret(String body) {
        int tIndxStrt = body.indexOf("Sec:", 0);
        if (tIndxStrt > 0) {
            String topic = body.substring(tIndxStrt + 4);
            return topic;
        } else return null;
    }

    public static Settings readSettings(Context context) {
        SharedPreferences settingsReader = context.getSharedPreferences(SHAREDPREF_NAME, MODE_PRIVATE);
        Settings settings = new Settings();

        settings.setRegStatus(settingsReader.getBoolean(SHAREDPREF_REGISTERED, false));
        settings.setName(settingsReader.getString(SHAREDPREF_USERNAME, "_NA_"));
        settings.setMobile(settingsReader.getString(SHAREDPREF_MOBILE, "_NA_"));
        settings.setCompany(settingsReader.getString(SHAREDPREF_COMPANY, "_NA_"));
        settings.setEmail(settingsReader.getString(SHAREDPREF_EMAIL, "_NA_"));
        settings.setInstType(settingsReader.getInt(SHAREDPREF_INSTTYPE, 0));
        settings.setInstTypeSet(settingsReader.getBoolean(SHAREDPREF_INSTTYPESET, false));
        settings.setUserUUID(settingsReader.getString(SHAREDPREF_USERUUID, "0"));
        CommonDataArea.setTempUUID(settingsReader.getString(SHAREDPREF_TEMUSERUUID, "0"));
        settings.setRegStatus(settingsReader.getBoolean(SHAREDPREF_REGISTERED, false));
        settings.setRegReqStatus(settingsReader.getInt(SHAREDPREF_REGREQSTATUS, 0));
        settings.setSecret(settingsReader.getString(SHAREDPREF_SECRET, "1234567890"));
        settings.setAdminEmail(settingsReader.getString(SHAREDPREF_ADMINEMAIL, "#NOEMAIL#"));
        settings.setTopic(settingsReader.getString(SHAREDPREF_TOPIC, "NOTOPIC"));

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
        String savedTopic;
        String newTopic;
        Settings settingd = CommonDataArea.getCommonSettings();
        newTopic = settingd.getAdminEmail();
        newTopic = newTopic.replace('@','_');
        SharedPreferences settings = context.getSharedPreferences(SHAREDPREF_NAME, MODE_PRIVATE);
        savedTopic = settings.getString(SHAREDPREF_TOPIC, "NOTOPIC");
        boolean topicChanged = false;
        if(!savedTopic.equals(settingd.getAdminEmail())) topicChanged=true;
        if ((savedTopic.equals("NOTOPIC"))||topicChanged) {
            SharedPreferences.Editor ed = settings.edit();
            String companyEmail = settingd.getAdminEmail();
            if ((newTopic.length() > 4) && (!newTopic.contains("NA"))) {
                FirebaseMessaging fm = FirebaseMessaging.getInstance();
                String tocken = FirebaseInstanceId.getInstance().getToken();
                fm.subscribeToTopic(newTopic);
                settingd.setTopic(newTopic);
                ed.putString(SHAREDPREF_TOPIC, newTopic);
                ed.commit();
                CommonDataArea.setCommonSettings(settingd);
                return "SUCCESS:TopicName:" + newTopic;
            } else return "FAIL:Company Name failed to meet criteria:" + companyEmail;
        } else return "FAIL:Topic already exists:" + newTopic;

    }

    public static String saveSettinsAssos(Context context, Settings settingd) throws Exception {
        SharedPreferences settings = context.getSharedPreferences(SHAREDPREF_NAME, MODE_PRIVATE);
        CommonDataArea.setCommonSettings(settingd);
        SharedPreferences.Editor ed = settings.edit();
        ed.putString(SHAREDPREF_USERNAME, settingd.getName());
        ed.putString(SHAREDPREF_MOBILE, settingd.getMobile());
        ed.putString(SHAREDPREF_EMAIL, settingd.getEmail());
        ed.putString(SHAREDPREF_SECRET, settingd.getSecret());
        ed.putString(SHAREDPREF_ADMINEMAIL, settingd.getAdminEmail());
        ed.putString(SHAREDPREF_USERUUID, settingd.getUserUUID());
        /*
            if (settingd.getRegReqStatus() == MO_REGSTATUS_ASSOS_NOTSAVED) {
                ed.putInt(SHAREDPREF_REGREQSTATUS, MO_REGSTATUS_ASSOS_PENDING);
                UUID uuid = UUID.randomUUID();
                String uuidStr = uuid.toString();
                ed.putString(SHAREDPREF_USERUUID, uuidStr);
            }
        */
        ed.commit();
        readSettings(context);//To Update the main settings object stored in CommonDataArea
        return createCompanyTopic(context);
    }

    public static void sendRegMesg( final Settings settingd){
        new Thread(new Runnable() {
            @Override
            public void run() {
                MOMesgHandler attnMesg = new MOMesgHandler();
                attnMesg.MoMesgCreate(settingd.getUserUUID(),"TOALL", MOMain.MOMESG_ENQLOG_MESG);
                attnMesg.addAttribute("Name", settingd.getName());
                attnMesg.addAttribute("Mobile", settingd.getMobile());
                attnMesg.addAttribute("UserUUID", settingd.getUserUUID());
                attnMesg.addAttribute("Email", settingd.getAdminEmail());
                attnMesg.addAttribute("Type", settingd.getInstType());

                JSONObject mesg = attnMesg.assembleJSONMesg();
                boolean sendSuccess = attnMesg.sendDevMessage(mesg);
            }
        }).start();

    }


    public String saveSettingAdmin(Context context, Settings settingd) throws Exception {
        try {
            SharedPreferences settings = context.getSharedPreferences(SHAREDPREF_NAME, MODE_PRIVATE);
            boolean registered = settings.getBoolean(SHAREDPREF_REGISTERED, false);
            if (!registered) {
                settingd.setEmail(settingd.getAdminEmail()); //set same email id as com.moffice.com.microoffice.app email
                SharedPreferences.Editor ed = settings.edit();
                ed.putString(SHAREDPREF_USERNAME, settingd.getName());
                ed.putString(SHAREDPREF_MOBILE, settingd.getMobile());
                ed.putString(SHAREDPREF_COMPANY, settingd.getCompany());
                ed.putString(SHAREDPREF_EMAIL, settingd.getEmail());
                ed.putString(SHAREDPREF_ADMINEMAIL, settingd.getAdminEmail());
                if (settingd.getUserUUID().length() < 5) {
                    String uniqueId = UUID.randomUUID().toString();
                    settingd.setUserUUID(uniqueId);
                }
                ed.putString(SHAREDPREF_USERUUID, settingd.getUserUUID());
                ed.putString(SHAREDPREF_SECRET, settingd.getSecret());
                ed.putBoolean(SHAREDPREF_REGISTERED, true);
                ed.commit();
                readSettings(context);//To Update the main settings object stored in CommonDataArea
                MOUserManager.saveData(context, true, settingd.getName(), settingd.getMobile(), settingd.getUserUUID(), "", settingd.getCompany(), MOMain.MO_DBUSERLIST_ASSOS_REGISTERED_ADMIN);
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
                    MOUserManager.saveData(context, true, settingd.getName(), settingd.getMobile(), settingd.getUserUUID(), "", settingd.getCompany(), MOMain.MO_DBUSERLIST_ASSOS_REGISTERED_ADMIN);
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
        SharedPreferences settings = context.getSharedPreferences(SHAREDPREF_NAME, MODE_PRIVATE);
        SharedPreferences.Editor edit = settings.edit();
        edit.putInt(SHAREDPREF_INSTTYPE, type);
        edit.putBoolean(SHAREDPREF_INSTTYPESET, true);
        edit.putInt(SHAREDPREF_REGREQSTATUS, 0);
        edit.commit();
        CommonDataArea.getCommonSettings().setInstType(type);
        CommonDataArea.getCommonSettings().setInstTypeSet(true);
        CommonDataArea.getCommonSettings().setRegReqStatus(0);
    }

    public void savePreferenceBooleanValue(String key,
                                           boolean value) {
        sharedpreferences = context.getSharedPreferences(
                SHAREDPREF_NAME, MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedpreferences.edit();
        editor.putBoolean(key, value);
        editor.apply();
    }

    public static void exportExcel(Context context,int month, int year) {
        try {
            String fileDbPath = Environment.getExternalStorageDirectory()
                    + "/MOffice" + "/dbbackup.xls";
            if (!(new File(Environment.getExternalStorageDirectory()
                    + "/MOffice" + "/")).exists()) {
                (new File(Environment.getExternalStorageDirectory()
                        + "/MOffice" + "/")).mkdirs();
            }
            int adjMonth = month - 1; //converting month to zero based index
            Calendar cal = Calendar.getInstance();
            cal.set(year,adjMonth,1, 0,0);
            long startTime = cal.getTimeInMillis();

            int  maxDaysMon = cal.getActualMaximum(Calendar.DAY_OF_MONTH);
            cal = Calendar.getInstance();
            cal.set(year,adjMonth,maxDaysMon,23,59);
            long endTime = cal.getTimeInMillis();

            File file = new File(fileDbPath);

            WorkbookSettings wbSettings = new WorkbookSettings();
            wbSettings.setLocale(new Locale("en", "EN"));
            WritableWorkbook workbook;

            workbook = Workbook.createWorkbook(file, wbSettings);
            getMonthlyAttnMapExcell(context,workbook,month,startTime);
            getLeaveListExcell(context, workbook,startTime,endTime);
            getEnquiryListExcel(context,workbook);
            getSupportListExcel(context,workbook);

            getMeetListExcell(context, workbook,startTime,endTime);
            getVisDataExcell(context, workbook,startTime,endTime);
            getAttenListExcell(context, workbook,startTime,endTime);
            getUserListExcel(context, workbook);


            workbook.write();

            try {
                workbook.close();
            } catch (WriteException e) {
                LogWriter.writeLogException("dbexport", e);
                return;
            }
            Toast.makeText(context, "Export to excell Completed ,Check File ->"+fileDbPath, Toast.LENGTH_LONG).show();

        } catch (Exception exp) {
            LogWriter.writeLogException("dbexport", exp);
        }

    }

    public static void getMonthlyAttnMapExcell(Context context, WritableWorkbook workbook, int month, long startTimeMonth) {
        try {
            Dao<UserList, Integer> userList = CommonDataArea.getHelper(context).getUserListDao();
            QueryBuilder<UserList, Integer> queryBuilder1 = userList.queryBuilder();
            Where<UserList,Integer> where= queryBuilder1.where();
            where.eq("Status", MOMain.MO_DBUSERLIST_ASSOS_REGISTERED);
            where.or();
            where.eq("Status", MOMain.MO_DBUSERLIST_ASSOS_REGISTERED_ADMIN);
            List<UserList> userListRes = queryBuilder1.query();
            int sheetNum =1;
            for (UserList user : userListRes) {
                WritableSheet sheet = workbook.createSheet("AttnMap-"+sheetNum+"-"+user.getName(), 0);
                ++sheetNum;
                Label label1 = new Label(0, 0, "Sunday");
                WritableCellFormat format = new WritableCellFormat();
                format.setBackground(Colour.GRAY_25);
                label1.setCellFormat(format);
                sheet.addCell(label1);

                label1 = new Label(1, 0, "Monday");
                format = new WritableCellFormat();
                format.setBackground(Colour.GRAY_25);
                label1.setCellFormat(format);
                sheet.addCell(label1);

                label1 = new Label(2, 0, "Tuesday");
                format = new WritableCellFormat();
                format.setBackground(Colour.GRAY_25);
                label1.setCellFormat(format);
                sheet.addCell(label1);

                label1 = new Label(3, 0, "Wendesday");
                format = new WritableCellFormat();
                format.setBackground(Colour.GRAY_25);
                label1.setCellFormat(format);
                sheet.addCell(label1);

                label1 = new Label(4, 0, "Thursday");
                format = new WritableCellFormat();
                format.setBackground(Colour.GRAY_25);
                label1.setCellFormat(format);
                sheet.addCell(label1);

                label1 = new Label(5, 0, "Friday");
                format = new WritableCellFormat();
                format.setBackground(Colour.GRAY_25);
                label1.setCellFormat(format);
                sheet.addCell(label1);

                label1 = new Label(6, 0, "Saturday");
                format = new WritableCellFormat();
                format.setBackground(Colour.GRAY_25);
                label1.setCellFormat(format);
                sheet.addCell(label1);

                Calendar cal = Calendar.getInstance();
                cal.setTimeInMillis(startTimeMonth);


                int daysInMonth = cal.getActualMaximum(Calendar.DAY_OF_MONTH);
                int startDay = cal.get(Calendar.DAY_OF_WEEK)-1;
                int row = 1;
                WritableCellFormat formatDateRow = new WritableCellFormat();
                formatDateRow.setBackground(Colour.BLUE);

                WritableCellFormat formatAttenIN = new WritableCellFormat();
                formatDateRow.setBackground(Colour.GREEN);

                WritableCellFormat formatAttenOff = new WritableCellFormat();
                formatDateRow.setBackground(Colour.RED);
                int i = 1;
                while (i < daysInMonth) {
                    while (startDay < 7) {
                        Dao<AttnLog, Integer> attnListChk = CommonDataArea.getHelper(context).getAttnLogDao();
                        QueryBuilder<AttnLog, Integer> queryBuilder = attnListChk.queryBuilder();
                        Where<AttnLog, Integer> where2 = queryBuilder.where();
                        where2.eq("LogType", 1);
                        where2.and();
                        where2.eq("UserUUID",user.getUserGUID());
                        where2.and();
                        where2.eq("LogMonth",month);
                        where2.and();
                        where2.eq("LogDayOfMonth",i);
                        List<AttnLog> attnList = queryBuilder.query();
                        formatDateRow = new WritableCellFormat();
                        formatDateRow.setBackground(Colour.GRAY_50);

                        String dateStr = " " + (i) + " ";
                        Label label = new Label(startDay, row, dateStr);
                        label.setCellFormat(formatDateRow);
                        sheet.addCell(label);
                        if((attnList!=null)&&(attnList.size()>0)) {
                            String placeVisit ="";
                            for(AttnLog log : attnList) {
                                placeVisit = log.getPlaceVisit();
                                if(placeVisit.length()>2) break;
                            }

                            formatAttenIN = new WritableCellFormat();
                            formatAttenIN.setBackground(Colour.GREEN);

                            label = new Label(startDay, row + 1, placeVisit);
                            label.setCellFormat(formatAttenIN);
                            sheet.addCell(label);
                        }else{
                            formatAttenIN = new WritableCellFormat();
                            formatAttenIN.setBackground(Colour.RED);

                            dateStr = "NA";

                            label = new Label(startDay, row + 1, dateStr);
                            label.setCellFormat(formatAttenIN);
                            sheet.addCell(label);
                        }
                        ++startDay;
                        ++i;
                        if (i > 31) break;
                    }

                    startDay = 0;
                    row += 2;
                }

            }
        }catch(Exception exp){
            LogWriter.writeLogException("ExcelExportAttnMap",exp);
        }
    }

    public static void getSupportListExcel(Context context, WritableWorkbook workbook) {

        try {
            WritableSheet sheet = workbook.createSheet("SupportReqList", 0);
            sheet.addCell(new Label(0, 0, "UserName")); // column and row
            sheet.addCell(new Label(1, 0, "CustomerName"));
            sheet.addCell(new Label(2, 0, "Product"));
            sheet.addCell(new Label(3, 0, "CustMobile"));
            sheet.addCell(new Label(4, 0, "Description"));
            sheet.addCell(new Label(5, 0, "Status"));
            sheet.addCell(new Label(6, 0, "Note"));
            sheet.addCell(new Label(7, 0, "UserUUID")); // column and row
            sheet.addCell(new Label(8, 0, "AssignedTo"));
            Dao<SupportDetails, Integer> supList = CommonDataArea.getHelper(context).getSupportDao();


            int row = 1;
            for (SupportDetails sup : supList) {

                sheet.addCell(new Label(0, row, sup.getName())); // column and row
                sheet.addCell(new Label(1, row, sup.getCustName()));
                sheet.addCell(new Label(2, row, sup.getProduct())); // column and row
                sheet.addCell(new Label(3, row, sup.getPhoneNumber())); // column and row
                sheet.addCell(new Label(4, row, sup.getDescription())); // column and row
                sheet.addCell(new Label(5, row, sup.getStatus())); // column and row
                sheet.addCell(new Label(6, row, sup.getNote())); // column and row
                sheet.addCell(new Label(7, row, sup.getUserUUID())); // column and row
                sheet.addCell(new Label(8, row, sup.getAssignedTo())); // column and row

                ++row;
            }
        } catch (Exception exp) {
            LogWriter.writeLogException("dbexport", exp);
        }
    }

    public static void getEnquiryListExcel(Context context, WritableWorkbook workbook) {
        try {
            WritableSheet sheet = workbook.createSheet("EnquiryList", 0);
            sheet.addCell(new Label(0, 0, "UserName")); // column and row
            sheet.addCell(new Label(1, 0, "CustomerName"));
            sheet.addCell(new Label(2, 0, "Product"));
            sheet.addCell(new Label(3, 0, "CustMobile"));
            sheet.addCell(new Label(4, 0, "Description"));
            sheet.addCell(new Label(5, 0, "Status"));
            sheet.addCell(new Label(6, 0, "Note"));
            sheet.addCell(new Label(7, 0, "UserUUID")); // column and row
            sheet.addCell(new Label(8, 0, "AssignedTo"));
            Dao<EnquiryDetails, Integer> supList = CommonDataArea.getHelper(context).getEnquiryDao();
            int row = 1;
            for (EnquiryDetails sup : supList) {
                sheet.addCell(new Label(0, row, sup.getName())); // column and row
                sheet.addCell(new Label(1, row, sup.getCustName()));
                sheet.addCell(new Label(2, row, sup.getProduct())); // column and row
                sheet.addCell(new Label(3, row, sup.getPhoneNumber())); // column and row
                sheet.addCell(new Label(4, row, sup.getDescription())); // column and row
                sheet.addCell(new Label(5, row, sup.getStatus())); // column and row
                sheet.addCell(new Label(6, row, sup.getNote())); // column and row
                sheet.addCell(new Label(7, row, sup.getUserUUID())); // column and row
                sheet.addCell(new Label(8, row, sup.getAssignedTo())); // column and row

                ++row;
            }
        } catch (Exception exp) {
            LogWriter.writeLogException("dbexport", exp);
        }
    }


    public static void getUserListExcel(Context context, WritableWorkbook workbook) {
        try {
            WritableSheet sheet = workbook.createSheet("UserList", 0);
            sheet.addCell(new Label(0, 0, "Name")); // column and row
            sheet.addCell(new Label(1, 0, "Mobile"));
            sheet.addCell(new Label(2, 0, "UserUUID")); // column and row
            sheet.addCell(new Label(3, 0, "Status"));
            sheet.addCell(new Label(4, 0, "Email"));
            Dao<UserList, Integer> userList = CommonDataArea.getHelper(context).getUserListDao();

            JSONArray userListArray = new JSONArray();
            int row = 1;
            for (UserList user : userList) {

                sheet.addCell(new Label(0, row, user.getName())); // column and row
                sheet.addCell(new Label(1, row, user.getMobile()));
                sheet.addCell(new Label(2, row, user.getUserGUID())); // column and row
                if (user.getStatus() == MO_DBUSERLIST_ASSOS_REGISTERED_ADMIN)
                    sheet.addCell(new Label(3, row, "Admin"));
                else
                    sheet.addCell(new Label(3, row, "Associate"));
                sheet.addCell(new Label(4, row, "Email"));
                ++row;
            }
        } catch (Exception exp) {
            LogWriter.writeLogException("dbexport", exp);
        }
    }

    public static void getAttenListExcell(Context context, WritableWorkbook workbook,long startTime, long endTime) {
        try {
            Dao<AttnLog, Integer> attnListChk = CommonDataArea.getHelper(context).getAttnLogDao();
            QueryBuilder<AttnLog, Integer> queryBuilder = attnListChk.queryBuilder();
            Where<AttnLog,Integer> where = queryBuilder.where();
            where.eq("LogType",1);
            where.and();
            where.between("TimeStamp",startTime,endTime);

            queryBuilder.orderBy("UserName",true);
            queryBuilder.orderBy("TimeStamp",true);
            List<AttnLog> attnList =queryBuilder.query();

            WritableSheet sheet = workbook.createSheet("AttnList", 0);
            sheet.addCell(new Label(0, 0, "UserName"));
            sheet.addCell(new Label(1, 0, "Date"));
            sheet.addCell(new Label(2, 0, "Time"));
            sheet.addCell(new Label(3, 0, "IN/OUT"));
            sheet.addCell(new Label(4, 0, "Office"));
            sheet.addCell(new Label(5, 0, "OfficeLoc"));
            sheet.addCell(new Label(6, 0, "TodaysPlan"));
            sheet.addCell(new Label(7, 0, "placeVisit"));
            sheet.addCell(new Label(8, 0, "DailyAllowance"));


            int row=1;
            for(AttnLog attnLog :attnList ) {
                sheet.addCell(new Label(0, row, attnLog.getUserName())); // column and row
                String dateStr = getDate(attnLog.getTimeStamp(),"dd-MM-yyyy");
                String dateTime = getDate(attnLog.getTimeStamp(),"HH:mm");
                sheet.addCell(new Label(1, row,  dateStr));
                sheet.addCell(new Label(2, row, dateTime));

                sheet.addCell(new Label(3, row, attnLog.getInOut()));
                sheet.addCell(new Label(4, row, attnLog.getOffice()));
                sheet.addCell(new Label(6, row, attnLog.getTodaysPlan()));
                sheet.addCell(new Label(7, row, attnLog.getPlaceVisit()));
                sheet.addCell(new Label(8, row, String.valueOf(attnLog.getDailyAllowance())));


                String urlStr = "https://www.google.com/maps/search/?api=1&query="+attnLog.getLati()+","+attnLog.getLongi()+"\r\n";
                URL url = new URL(urlStr);
                WritableHyperlink hyprLink = new WritableHyperlink(5,row,url);
                sheet.addHyperlink(hyprLink);
                ++row;
            }

        }catch(Exception exp){

        }
    }
    public static void getLeaveListExcell(Context context, WritableWorkbook workbook,long startTime,long longTime) {
        try {
            Dao<LeaveList, Integer> leaveListChk = CommonDataArea.getHelper(context).getLeaveLogDao();
            QueryBuilder<LeaveList, Integer> queryBuilder = leaveListChk.queryBuilder();
         //   queryBuilder.where().eq("LogType",1);
            //Where<LeaveList,Integer>  where = queryBuilder.where();
            //where.between("TimeStamp",startTime,longTime);
            queryBuilder.orderBy("UserName",true);
            queryBuilder.orderBy("TimeStamp",true);
            List<LeaveList> leaveList =queryBuilder.query();

            WritableSheet sheet = workbook.createSheet("LeaveList", 0);
            sheet.addCell(new Label(0, 0, "UserName"));
            sheet.addCell(new Label(1, 0, "Requested Date"));
            sheet.addCell(new Label(2, 0, "Start Date"));
            sheet.addCell(new Label(3, 0, "End Date"));
            sheet.addCell(new Label(4, 0, "Number of days"));
            sheet.addCell(new Label(5, 0, "Leave reason"));
            sheet.addCell(new Label(6, 0, "Leave Type"));
            sheet.addCell(new Label(7, 0, "Leave Status"));


            int row=1;
            for(LeaveList leaveLog :leaveList ) {
                sheet.addCell(new Label(0, row, leaveLog.getUserName())); // column and row
                String dateStr = getDate(leaveLog.getTimeStamp(),"dd-MM-yyyy");
                String dateTime = getDate(leaveLog.getTimeStamp(),"HH:mm");
                sheet.addCell(new Label(1, row,  dateStr+" "+dateTime));
                sheet.addCell(new Label(2, row, leaveLog.getStartDat()));

                sheet.addCell(new Label(3, row, leaveLog.getEndDate()));
                sheet.addCell(new Label(4, row, String.valueOf(leaveLog.getNumDays())));
                sheet.addCell(new Label(5, row, leaveLog.getNotes()));
                if(leaveLog.getLvType()==1)
                    sheet.addCell(new Label(6, row, "Casual Leave"));
                if(leaveLog.getLvType()==2)
                    sheet.addCell(new Label(6, row, "Sick Leave"));
                if(leaveLog.getLvType()==3)
                    sheet.addCell(new Label(6, row, "On Duty"));
                if(leaveLog.getLvType()==4)
                    sheet.addCell(new Label(6, row, "LOP"));


                if(leaveLog.getLvStatus()==1)
                    sheet.addCell(new Label(7, row, "Requested"));
                if(leaveLog.getLvStatus()==2)
                    sheet.addCell(new Label(7, row, "Approved"));
                if(leaveLog.getLvStatus()==3)
                    sheet.addCell(new Label(7, row, "Rejected"));
                if(leaveLog.getLvStatus()==4)
                 sheet.addCell(new Label(7, row, "Discarded"));
                ++row;
            }


        }catch(Exception exp){

        }
    }

    public static  String getDate(long milliSeconds, String dateFormat)
    {
        // Create a DateFormatter object for displaying date in specified format.
        SimpleDateFormat formatter = new SimpleDateFormat(dateFormat);

        // Create a calendar object that will convert the date and time value in milliseconds to date.
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(milliSeconds);
        return formatter.format(calendar.getTime());
    }
    public static void getMeetListExcell(Context context, WritableWorkbook workbook, long starTime , long endTime) {
    try {

        final Dao<VisitMeetLog, Integer> meetListDb = CommonDataArea.getHelper(context).getVisitMeetingDao();
        QueryBuilder<VisitMeetLog, Integer> queryBuilder = meetListDb.queryBuilder();

        Where<VisitMeetLog, Integer> where = queryBuilder.where();
        where.lt("Status",2);
        where.and();
        where.between("DateTimeStamp",starTime,endTime);

        queryBuilder.orderBy("UserName",true);
        queryBuilder.orderBy("DateTimeStamp",true);
        List<VisitMeetLog> visMeetList =queryBuilder.query();

        WritableSheet sheet = workbook.createSheet("MeetingList", 0);
        sheet.addCell(new Label(0, 0, "UserName"));
        sheet.addCell(new Label(1, 0, "Date"));
        sheet.addCell(new Label(2, 0, "Time"));
        sheet.addCell(new Label(3, 0, "BusinessName"));
        sheet.addCell(new Label(4, 0, "PersonMet"));
        sheet.addCell(new Label(5, 0, "Title"));
        sheet.addCell(new Label(6, 0, "Note"));
        sheet.addCell(new Label(7, 0, "NextVisDate"));
        sheet.addCell(new Label(8, 0, "Purpose"));
        sheet.addCell(new Label(9, 0, "Place"));

        int row=1;
        for(VisitMeetLog visMeet :visMeetList )
        {
            SimpleDateFormat format = new SimpleDateFormat("HH:mm a");
            String timeStr = format.format(visMeet.getDateTimeStamp());
            sheet.addCell(new Label(0, row, visMeet.getUserName())); // column and row
            sheet.addCell(new Label(1, row,  visMeet.getDate()));
            sheet.addCell(new Label(2,row,timeStr));
            sheet.addCell(new Label(3, row, visMeet.getBusinessName()));
            sheet.addCell(new Label(4, row, visMeet.getPersonMet()));
            sheet.addCell(new Label(5, row, visMeet.getTitle()));
            sheet.addCell(new Label(6, row, visMeet.getNote()));
            sheet.addCell(new Label(7, row, visMeet.getNextVisDate()));
            Dao<VisitLog, Integer> visitListChk = CommonDataArea.getHelper(context).getVisitLogDao();

            QueryBuilder<VisitLog, Integer> queryBuilder2 = visitListChk.queryBuilder();
            Where<VisitLog,Integer> where2 = queryBuilder2.where();
            where2.eq("RecUUID",visMeet.getVisitUUID());
            List<VisitLog> visList =queryBuilder2.query();
            if(visList.size()>0){
                sheet.addCell(new Label(8, row, visList.get(0).getPurpose()));
                sheet.addCell(new Label(9, row, visList.get(0).getPlaceName()));
            }
            ++row;
        }

    }catch(Exception exp){
        LogWriter.writeLogException("dbexport-getMeetListExcell", exp);
    }
    }

    public static void getVisDataExcell(Context context,WritableWorkbook workbook, long startTime, long endTime) {
        try {

            WritableSheet sheet = workbook.createSheet("VisitList", 0);
            sheet.addCell(new Label(0, 0, "DateTime")); // column and row
            sheet.addCell(new Label(1, 0, "UserName"));
            sheet.addCell(new Label(2, 0, "PlaceName"));
            sheet.addCell(new Label(3, 0, "BusinessName"));
            sheet.addCell(new Label(4, 0, "Purpose"));
            sheet.addCell(new Label(5, 0, "ArrivedDeparted"));
//            sheet.addCell(new Label(6, 0, "Longi"));
//            sheet.addCell(new Label(7, 0, "Lati"));
            sheet.addCell(new Label(6, 0, "LocationLink")); // column and row
            sheet.addCell(new Label(7, 0, "UserUUID")); // column and row

            Dao<VisitLog, Integer> visitListChk = CommonDataArea.getHelper(context).getVisitLogDao();

            QueryBuilder<VisitLog, Integer> queryBuilder = visitListChk.queryBuilder();
            Where<VisitLog,Integer> Where = queryBuilder.where();
            Where.between("TimeStamp",startTime,endTime);
            queryBuilder.orderByRaw("UserID,TimeStamp ASC");
            List<VisitLog> visList =queryBuilder.query();

            int row=1;
            for (VisitLog visit : visList) {

                sheet.addCell(new Label(0, row, visit.getDateTime())); // column and row
                sheet.addCell(new Label(1, row,  visit.getUserName()));
                sheet.addCell(new Label(2, row, visit.getPlaceName()));
                sheet.addCell(new Label(3, row, visit.getBusinessName()));
                sheet.addCell(new Label(4, row, visit.getPurpose()));

                if(visit.getArrivedDeparted()==1)
                    sheet.addCell(new Label(5, row, "Arrived"));
                else
                    sheet.addCell(new Label(5, row, "Departed"));

//                Number num = new Number(6, row, visit.getLongi());
//                sheet.addCell(num.copyTo(6,row));
//
//                num = new Number(7, row, visit.getLati());
//                sheet.addCell(num.copyTo(7,row));

                String urlStr = "https://www.google.com/maps/search/?api=1&query="+visit.getLati()+","+visit.getLongi()+"\r\n";
                URL url = new URL(urlStr);
                WritableHyperlink hyprLink = new WritableHyperlink(6,row,url);
                sheet.addHyperlink(hyprLink);

                sheet.addCell(new Label(7, row,  visit.getUserUUID())); // column and row
                ++row;
                /*
                JSONObject visitJson = new JSONObject();
                visitJson.put("Year", visit.getLogYear());
                visitJson.put("Month", visit.getLogMonth());
                visitJson.put("Day", visit.getLogDayOfMonth());
                visitJson.put("Hour", visit.getLogHour());
                visitJson.put("Minute", visit.getLogMinute());
                visitJson.put("DateTime", visit.getDateTime());

                visitJson.put("UserUUID", visit.getUserUUID());
                visitJson.put("UserName", visit.getUserName());
                visitJson.put("RecUUID", visit.getRecUUID()); //Records UUID

                visitJson.put("Longi", Double.toString(visit.getLongi()));
                visitJson.put("Lati", Double.toString(visit.getLati()));

                visitJson.put("PlaceName", visit.getPlaceName());
                visitJson.put("Purpose", visit.getPurpose());
                visitJson.put("ArrivedDeparted", visit.getArrivedDeparted());
                visitJson.put("ArrivedDeparted", visit.getArrivedDeparted());
                visitJson.put("TimeStamp", visit.getTimeStamp());

                visitListArray.put(visitJson);*/
            }

        } catch (Exception exp) {
            LogWriter.writeLogException("dbexport", exp);

        }
    }
    public static void exportDB(Context context) {
        try {
            String fileDbPath = Environment.getExternalStorageDirectory()
                    + "/MOffice" + "/dbbackup.json";
            if (!(new File(Environment.getExternalStorageDirectory()
                    + "/MOffice" + "/")).exists()) {
                (new File(Environment.getExternalStorageDirectory()
                        + "/MOffice" + "/")).mkdirs();
            }

            JSONObject jsonMain = new JSONObject();
            JSONObject jsonPart = new JSONObject();
            JSONArray jsonArray = new JSONArray();

            jsonArray= MOUserManager.getUserList(context);
            jsonMain.put("UserList",jsonArray);

            jsonArray= MOOfficeLocManager.getOfficeLocations(context);
            jsonMain.put("OfficeList",jsonArray);

            jsonArray=MOVisitManager.getVisData(context);
            jsonMain.put("VisitList",jsonArray);

            jsonArray=MOAttenManager.getAttnData(context);
            jsonMain.put("AttnList",jsonArray);
            jsonArray=MOLeaveManager.getLeaveData(context);
            jsonMain.put("LeaveList",jsonArray);


            try (PrintWriter out = new PrintWriter(fileDbPath)) {
                out.println(jsonMain);

        } catch (java.io.FileNotFoundException e) {
                LogWriter.writeLogException("DBExport",e);
        }
            Toast.makeText(context, "DB Export Completed ,Check File ->"+fileDbPath, Toast.LENGTH_LONG).show();
    } catch(JSONException exp){
        LogWriter.writeLogException("DBExport",exp);
        Toast.makeText(context, "DB Export FAILED ,Exception->"+exp.getMessage(), Toast.LENGTH_LONG).show();

    }
    }
    public static List<Products> getUnsendProdData(Context context) {
        try {

            List<Products> prodList;
            Calendar today = Calendar.getInstance();
            Dao<Products, Integer> officeDb = CommonDataArea.getHelper(context).getProductDao();
            QueryBuilder<Products, Integer> queryBuilder = officeDb.queryBuilder();
            queryBuilder.where().eq("sendstatus", 0);
            prodList =queryBuilder.query();
            CommonDataArea.closeHelper();
            return prodList;
        }catch(Exception exp){
            return null;
        }
    }

    public static void setExcelLicense(boolean lic, String tocken){
        SharedPreferences settings = context.getSharedPreferences(SHAREDPREF_NAME, MODE_PRIVATE);
        SharedPreferences.Editor ed = settings.edit();
        ed.putBoolean("ExcelExpEnabled",lic);
        ed.commit();
    }

    public static void setUserLicense(int numLic, String tocken){
        SharedPreferences settings = context.getSharedPreferences(SHAREDPREF_NAME, MODE_PRIVATE);
        SharedPreferences.Editor ed = settings.edit();
        ed.putInt("UserLic",numLic);
        ed.commit();
    }

    public static boolean getExcelLicense(){
        if(checkLicenseReq()) {
            SharedPreferences settings = context.getSharedPreferences(SHAREDPREF_NAME, MODE_PRIVATE);
            return settings.getBoolean("ExcelExpEnabled", false);
        }else return  true;
    }

    public static int getUserLicense(){
        if(checkLicenseReq()) {
            SharedPreferences settings = context.getSharedPreferences(SHAREDPREF_NAME, MODE_PRIVATE);
            return settings.getInt("UserLic", 5);
        }else return 1;
    }

    public static List<Products> getAllProdData(Context context) {
        try {

            List<Products> prodList;
            Calendar today = Calendar.getInstance();
            Dao<Products, Integer> officeDb = CommonDataArea.getHelper(context).getProductDao();
            QueryBuilder<Products, Integer> queryBuilder = officeDb.queryBuilder();

            prodList =queryBuilder.query();
            CommonDataArea.closeHelper();
            return prodList;
        }catch(Exception exp){
            return null;
        }
    }

    public static void saveProdData(Context context,JSONObject userData) {
        try {

            JSONObject bodyObj = new JSONObject(userData.get("body").toString());
            String recUUID = bodyObj.get("RecUUID").toString();
            String product =  bodyObj.get("ProductName").toString();

            synchronized (CommonDataArea.dbLock) {
                try {
                    final Dao<Products, Integer> prodListDb = CommonDataArea.getHelper(context).getProductDao();
                    QueryBuilder<Products, Integer> queryBuilder = prodListDb.queryBuilder();
                    queryBuilder.where().eq("RecUUID", recUUID);
                    List<Products> prodList = queryBuilder.query();
                    if ((prodList != null) && (prodList.size() > 0)) {
                        Products prod = prodList.get(0);
                        prodListDb.update(prod);
                    } else {
                        Products prod = new Products();
                        prod.setRecUUID(recUUID);
                        prod.setProductName(product);
                        prodListDb.create(prod);
                    }
                    CommonDataArea.closeHelper();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }

        } catch (Exception exp) {
            LogWriter.writeLog("UserSave", exp.getMessage());

        }
        // return MOMain.ERRORCODE_EXCEPTION+":Installation ID not match";
    }
    public static void sendProdData(final Context context,final boolean all) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    List<Products> prodList;// = getUnsendProdData(context);
                    if(all)
                        prodList = getAllProdData(context);
                    else
                        prodList = getUnsendProdData(context);
                    if((prodList==null)||(prodList.size()==0)) return;
                    for (Products prod : prodList) {
                        MOMesgHandler enqMesg = new MOMesgHandler();
                        enqMesg.MoMesgCreate(CommonDataArea.getUserUUID(), "TOALL", MOMain.MOMESG_PRODLIST_MESG);
                        enqMesg.addAttribute("ProductName", prod.getProductName());
                        enqMesg.addAttribute("RecUUID", prod.getRecUUID());

                        JSONObject mesg = enqMesg.assembleJSONMesg();
                        boolean sendSuccess = enqMesg.sendMessageOnThread(mesg);
                        if (sendSuccess) {
                            Dao<Products, Integer> prodDb = CommonDataArea.getHelper(context).getProductDao();
                            QueryBuilder<Products, Integer> queryBuilder = prodDb.queryBuilder();
                            queryBuilder.where().eq("RecUUID", prod.getRecUUID());
                            List<Products>  prodList2 = queryBuilder.query();
                            if((prodList2!=null)&&(prodList2.size()>0)) {
                                Products prodUp = prodList2.get(0);
                                prodUp.setSendStatus(1);
                                prodDb.update(prodUp);
                            }
                            CommonDataArea.closeHelper();
                        }
                        //todo sendSuccess is true then update the current record sendStatus to 1
                    }

                } catch (Exception exp) {
                    LogWriter.writeLogException("sendEnqData", exp);
                }
            }
        }).start();

    }

    static boolean checkLicenseReq(){
        String filePath = Environment.getExternalStorageDirectory()
                + "/MicroOff" + "/pres";
        File file = new File(filePath);
        if(!file.exists()){
            return true;
        }else return  false;
    }
}
