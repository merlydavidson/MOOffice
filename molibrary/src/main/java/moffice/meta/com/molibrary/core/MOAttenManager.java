package moffice.meta.com.molibrary.core;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.util.Log;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.stmt.QueryBuilder;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import java.util.UUID;

import moffice.meta.com.molibrary.Location.GPSTracker;
import moffice.meta.com.molibrary.firebase.MOMesgHandler;
import moffice.meta.com.molibrary.models.dbmodels.AttnLog;
import moffice.meta.com.molibrary.utility.CommonDataArea;
import moffice.meta.com.molibrary.utility.LogWriter;

/**
 * Created by com.moffice.com.microoffice.app on 10/02/2017.
 */
public class MOAttenManager {
    public static String LAST_ENTERED_PLACE="";
    public static boolean markAttendance(Context context, int inOut, int attnType,String lastINRecID,String todaysPlan,String place) {
        try {
            GPSTracker gpsTracker = new GPSTracker(context);
            Location loc = gpsTracker.getLocation();
            Calendar date = Calendar.getInstance();
            String officeName="";
            String officeUUID="";
           //Re try to obtain office name
            if(MOOfficeLocManager.markType== MOOfficeLocManager.MOA_MARK_OFFICE_COORDINATES)
            {
                officeName ="NA:";
                officeUUID = "NA";
                String location=null;
                Geocoder geoCoder = new Geocoder(context, Locale.getDefault());
                try {
                    List<Address> address = geoCoder.getFromLocation(loc.getLatitude(),loc.getLongitude(), 1);
                    if (address.size() > 0) {
                        location  =address.get(0).getFeatureName() + ", " + address.get(0).getLocality() +", " + address.get(0).getAdminArea() + ", " + address.get(0).getCountryName();
                        if(location!=null) {
                            MOOfficeLocManager.markType= MOOfficeLocManager.MOA_MARK_OFFICE_LOCNAME;
                            MOOfficeLocManager.officeName = location;
                            officeName=location;
                        }
                    }
                } catch (IOException e) {}
                catch (NullPointerException e) {}

                if(MOOfficeLocManager.markType== MOOfficeLocManager.MOA_MARK_OFFICE_COORDINATES){
                    String urlStr = "https://www.google.com/maps/search/?api=1&query="+loc.getLatitude()+","+loc.getLongitude()+"\r\n";

                    officeName=urlStr;
                }

            }else  if(MOOfficeLocManager.markType== MOOfficeLocManager.MOA_MARK_OFFICE_LOCNAME)
                {
                officeName= MOOfficeLocManager.officeName;
                officeUUID="NA";;
            }else if(MOOfficeLocManager.markType== MOOfficeLocManager.MOA_MARK_OFFICEID){
                officeName= MOOfficeLocManager.officeName;
                officeUUID= MOOfficeLocManager.officeID;
            }
            String recUUID;
            if(lastINRecID==null) {
                UUID uuid = UUID.randomUUID();
                recUUID = uuid.toString();
            }
            else
                recUUID = lastINRecID;
            Settings commonSettings = CommonDataArea.getCommonSettings();


            final AttnLog attn = new AttnLog();
            if(inOut==1)
            {
                attn.setInOut("In");
            }
            else if(inOut==0)
            {
                attn.setInOut("Out");
            }

            attn.setRecUUID(recUUID);
            attn.setUserUUID(CommonDataArea.getUserUUID());
            attn.setUserName(commonSettings.getName());
            attn.setLogType(attnType);
            attn.setLati(loc.getLatitude());
            attn.setLongi(loc.getLongitude());
            attn.setOffice(officeName);
            attn.setTodaysPlan(todaysPlan);
            attn.setPlaceVisit(place);
            attn.setOfficeUUID(officeUUID);
            attn.setLogYear(date.get(Calendar.YEAR));
            attn.setLogMonth(date.get(Calendar.MONTH)+1);
            attn.setLogDayOfMonth(date.get(Calendar.DAY_OF_MONTH));
            attn.setLogHour(date.get(Calendar.HOUR));
            attn.setLogMinute(date.get(Calendar.MINUTE));
            attn.setSendStatus(0);



            SimpleDateFormat df = new SimpleDateFormat("a");
            String  currentTime = df.format(date.getTime());
           attn.setAMPM(currentTime);
           attn.setTimeStamp(date.getTimeInMillis());
            try {
               // final Dao<AttnLog ,Integer> Attndao=CommonDataArea.getHelper(context).getAttnLogDao();
              //  Attndao.create(attn);

                final Dao<AttnLog,Integer> Attndao= CommonDataArea.getHelper(context).getAttnLogDao();
                QueryBuilder<AttnLog, Integer> queryBuilder = Attndao.queryBuilder();
                queryBuilder.where().eq("RecUUID", recUUID);
                List<AttnLog> attnList =queryBuilder.query();
                if((attnList!=null)&&(attnList.size()>0))
                {
                    attn.set_id(attnList.get(0).get_id());
                    Attndao.update(attn);
                }else {
                    Attndao.create(attn);
                }
                CommonDataArea.closeHelper();
                MODataDispatchThread.triggerDispatch();
            } catch (java.sql.SQLException e) {
                e.printStackTrace();
            }
            return true;

        } catch (Exception exp) {
            Log.e("Err", exp.getMessage());
            return false;
        }
    }
    public static boolean IsLastMarkedIn(Context context){
        try {
            final Dao<AttnLog,Integer> Attndao= CommonDataArea.getHelper(context).getAttnLogDao();
            QueryBuilder<AttnLog, Integer> queryBuilder = Attndao.queryBuilder();
            queryBuilder.where().eq("UserUUID", CommonDataArea.getUserUUID());
            queryBuilder.orderBy("_id",false);
            List<AttnLog> attnList =queryBuilder.query();
            if((attnList!=null)&&(attnList.size()>0))
            {
                if(attnList.get(0).getInOut().contentEquals("In"))
                    return  true;
                else return false;

            }
        }catch (Exception exp){
            LogWriter.writeLogException("IsLastIn",exp);
        }
        return false;
    }
    public static String lastMarkedInPlace(Context context){
        try {
            final Dao<AttnLog,Integer> Attndao= CommonDataArea.getHelper(context).getAttnLogDao();
            QueryBuilder<AttnLog, Integer> queryBuilder = Attndao.queryBuilder();
            queryBuilder.where().eq("UserUUID", CommonDataArea.getUserUUID());
            queryBuilder.orderBy("_id",false);
            List<AttnLog> attnList =queryBuilder.query();
            if((attnList!=null)&&(attnList.size()>0))
            {
                if(attnList.get(0).getInOut().contentEquals("In"))
                    return  attnList.get(0).getPlaceVisit();
                else return null;

            }
        }catch (Exception exp){
            LogWriter.writeLogException("IsLastIn",exp);
        }
        return null;
    }
    public static String IsLastIn(Context context){
        try {
            final Dao<AttnLog,Integer> Attndao= CommonDataArea.getHelper(context).getAttnLogDao();
            QueryBuilder<AttnLog, Integer> queryBuilder = Attndao.queryBuilder();
            queryBuilder.where().eq("UserUUID", CommonDataArea.getUserUUID());
            queryBuilder.orderBy("_id",false);
            List<AttnLog> attnList =queryBuilder.query();
            if((attnList!=null)&&(attnList.size()>0))
            {
                if(attnList.get(0).getInOut().contentEquals("In")) {


                    return attnList.get(0).getRecUUID();
                }
                else return null;

            }
        }catch (Exception exp){
            LogWriter.writeLogException("IsLastIn",exp);
        }
        return null;
    }
    public static void SaveRcvdAttendance(Context context, JSONObject attnData) {
        try {

            attnData = attnData.getJSONObject("body");
            int year = attnData.getInt("Year");
            int month = attnData.getInt("Month");
            int day = attnData.getInt("Day");
            int hour = attnData.getInt("Hour");
            int minute = attnData.getInt("Minute");
            int type = attnData.getInt("Type");
            String attnDat =attnData.getString("INOUT");
            String officeName = attnData.getString("Office");
            String userUUID = attnData.getString("UserUUID");
            String userName = attnData.getString("UserName");
            String recUUID = attnData.getString("RecUUID");
            String longi = attnData.getString("Longi");
            String lati = attnData.getString("Lati");
            String officeUUID = attnData.getString("OfficeUUID");
            String dateTime = attnData.getString("DateTime");
            String todaysPlan = attnData.getString("TodaysPlan");
            String placeVisit = attnData.getString("placeVisit");
            String dailyAllowance = attnData.getString("DailyAllowance");

            long timeStamp = attnData.getLong("TimeStamp");
            final AttnLog attn = new AttnLog();
            attn.setInOut(attnDat);

            attn.setRecUUID(recUUID);
            attn.setUserUUID(userUUID);
            attn.setUserName(userName);
            attn.setLogType(type);
            attn.setLati(Double.parseDouble(lati));
            attn.setLongi(Double.parseDouble(longi));
            attn.setOffice(officeName);
            attn.setOfficeUUID(officeUUID);
            attn.setLogYear(year);
            attn.setLogMonth(month);
            attn.setLogDayOfMonth(day);
            attn.setLogHour(hour);
            attn.setLogMinute(minute);
            attn.setSendStatus(1);
            attn.setAMPM(dateTime);
            attn.setTimeStamp(timeStamp);
            attn.setTodaysPlan(todaysPlan);
            attn.setPlaceVisit(placeVisit);
            attn.setDailyAllowance(Integer.parseInt(dailyAllowance));


            SimpleDateFormat df = new SimpleDateFormat("a");

            try {
                final Dao<AttnLog,Integer> Attndao= CommonDataArea.getHelper(context).getAttnLogDao();
                QueryBuilder<AttnLog, Integer> queryBuilder = Attndao.queryBuilder();
                queryBuilder.where().eq("RecUUID", recUUID);
                List<AttnLog> attnList =queryBuilder.query();
                if((attnList!=null)&&(attnList.size()>0))
                {
                    attn.set_id(attnList.get(0).get_id());
                    Attndao.update(attn);
                }else {
                    Attndao.create(attn);
                }
                CommonDataArea.closeHelper();
                MODataDispatchThread.triggerDispatch();
            } catch (java.sql.SQLException e) {
                e.printStackTrace();
            }
        } catch (Exception exp) {
            LogWriter.writeLogException("AttnMesgProc",exp);
        }
    }
    public static List<AttnLog> getUnsendAttnData(Context context) {
        try {
            // DataModel dataModel = new DataModel();
            List<AttnLog> attnList;
            Calendar today = Calendar.getInstance();
            Dao<AttnLog, Integer> officeListDb = CommonDataArea.getHelper(context).getAttnLogDao();
            QueryBuilder<AttnLog, Integer> queryBuilder = officeListDb.queryBuilder();
            queryBuilder.where().eq("SendStatus", 0);
            attnList =queryBuilder.query();
            CommonDataArea.closeHelper();
            return attnList;
        }catch(Exception exp){
            return null;
        }
    }
    
    public static JSONArray getAttnData(Context context)
    {
        try {
            Dao<AttnLog, Integer> attnListChk = CommonDataArea.getHelper(context).getAttnLogDao();

            JSONArray attnListArray = new JSONArray();
            for (AttnLog attn:attnListChk)
            {
                  JSONObject visitJson = new JSONObject();
                  visitJson.put("Year", attn.getLogYear());
                  visitJson.put("Month", attn.getLogMonth());
                  visitJson.put("Day", attn.getLogDayOfMonth());
                  visitJson.put("Hour", attn.getLogHour());
                  visitJson.put("Minute", attn.getLogMinute());
                  visitJson.put("Type", attn.getLogType()); //Reugular 1 ,TOUR - On Tour-2, OUT- Official Short break-3
                  visitJson.put("INOUT", attn.getInOut()); //0- Out 1- IN
                  visitJson.put("Office", attn.getOffice()); //Compare location and find out
                  visitJson.put("OfficeUUID", attn.getOfficeUUID()); //Records UUID
                  visitJson.put("UserUUID", attn.getUserUUID());
                  visitJson.put("UserName",attn.getUserName());
                  visitJson.put("RecUUID", attn.getRecUUID()); //Records UUID
                  visitJson.put("DateTime", attn.getANPM());
                  visitJson.put("TimeStamp", attn.getTimeStamp());
                  visitJson.put("TodaysPlan", attn.getTodaysPlan());
                  visitJson.put("placeVisit", attn.getPlaceVisit());
                  visitJson.put("DailyAllowance", attn.getPlaceVisit());

                  String temStr = Double.toString(attn.getLongi());
                  visitJson.put("Longi",temStr);
                 temStr = Double.toString(attn.getLati());
                  visitJson.put("Lati",temStr);

                  attnListArray.put(visitJson);
            }

            return attnListArray;
        }catch(Exception exp){
            LogWriter.writeLogException("sendAttnData",exp);
        }
        return  null;
    }

    public static void sendAttnData(Context context)
    {
        try {
            List<AttnLog> attnListChk = MOAttenManager.getUnsendAttnData(context);
            if((attnListChk==null)||(attnListChk.size()==0)) return;
            for (AttnLog attn:attnListChk)
            {
                MOMesgHandler attnMesg = new MOMesgHandler();
                attnMesg.MoMesgCreate(CommonDataArea.getUserUUID(), "TOALL", MOMain.MOMESG_ATTNMARK_REQ);
                attnMesg.addAttribute("Year", attn.getLogYear());
                attnMesg.addAttribute("Month", attn.getLogMonth());
                attnMesg.addAttribute("Day", attn.getLogDayOfMonth());
                attnMesg.addAttribute("Hour", attn.getLogHour());
                attnMesg.addAttribute("Minute", attn.getLogMinute());
                attnMesg.addAttribute("Type", attn.getLogType()); //Reugular 1 ,TOUR - On Tour-2, OUT- Official Short break-3
                attnMesg.addAttribute("INOUT", attn.getInOut()); //0- Out 1- IN
                attnMesg.addAttribute("Office", attn.getOffice()); //Compare location and find out
                attnMesg.addAttribute("OfficeUUID", attn.getOfficeUUID()); //Records UUID
                attnMesg.addAttribute("UserUUID", attn.getUserUUID());
                attnMesg.addAttribute("UserName",attn.getUserName());
                attnMesg.addAttribute("RecUUID", attn.getRecUUID()); //Records UUID
                attnMesg.addAttribute("DateTime", attn.getANPM());
                attnMesg.addAttribute("TimeStamp", attn.getTimeStamp());
                attnMesg.addAttribute("TodaysPlan", attn.getTodaysPlan());
                attnMesg.addAttribute("placeVisit", attn.getPlaceVisit());
                attnMesg.addAttribute("DailyAllowance", attn.getDailyAllowance());

                String temStr = Double.toString(attn.getLongi());
                attnMesg.addAttribute("Longi",temStr);
                temStr = Double.toString(attn.getLati());
                attnMesg.addAttribute("Lati",temStr);
                JSONObject mesg = attnMesg.assembleJSONMesg();
                boolean sendSuccess = attnMesg.sendMessage(mesg);
                if(sendSuccess){
                    Dao<AttnLog, Integer> attnDb = CommonDataArea.getHelper(context).getAttnLogDao();
                    QueryBuilder<AttnLog, Integer> queryBuilder = attnDb.queryBuilder();
                    queryBuilder.where().eq("RecUUID", attn.getRecUUID());
                    List<AttnLog> attnList =queryBuilder.query();
                    if((attnList!=null)&&(attnList.size()>0)) {
                        AttnLog attnLog = attnList.get(0);
                        attnLog.setSendStatus(1);
                        attnDb.update(attnLog);
                    }
                    CommonDataArea.closeHelper();
                }
                //todo sendSuccess is true then update the current record sendStatus to 1
            }
        }catch(Exception exp){
            LogWriter.writeLogException("sendAttnData",exp);
        }

    }
}
