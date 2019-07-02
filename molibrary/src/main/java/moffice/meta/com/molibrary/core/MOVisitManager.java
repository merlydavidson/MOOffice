package moffice.meta.com.molibrary.core;

import android.content.Context;
import android.location.Location;
import android.util.Log;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.stmt.QueryBuilder;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.UUID;

import moffice.meta.com.molibrary.firebase.MOMesgHandler;
import moffice.meta.com.molibrary.models.dbmodels.VisitLog;
import moffice.meta.com.molibrary.models.dbmodels.VisitMeetLog;
import moffice.meta.com.molibrary.utility.CommonDataArea;
import moffice.meta.com.molibrary.utility.LogWriter;

/**
 * Created by Administrator on 23-11-2017.
 */

public class MOVisitManager {
    public static boolean markVisit(Context context, Location loc, int arrivedDeparted, String placeName, String purpose, String businessName,long hoursSpend, long minutesSpend) {
        try {
            UUID uuid = UUID.randomUUID();
            String recUUID = uuid.toString();
            Calendar date = Calendar.getInstance();
            Settings commonSettings = CommonDataArea.getCommonSettings();


            VisitLog visitLog = new VisitLog();
            visitLog.setRecUUID(recUUID);
            visitLog.setUserUUID(CommonDataArea.getUserUUID());
            visitLog.setArrivedDeparted(arrivedDeparted);
            visitLog.setLogYear(date.get(Calendar.YEAR));
            visitLog.setLogMonth(date.get(Calendar.MONTH) + 1);
            visitLog.setLogDayOfMonth(date.get(Calendar.DAY_OF_MONTH));
            visitLog.setLogHour(date.get(Calendar.HOUR));
            visitLog.setLogMinute(date.get(Calendar.MINUTE));
            visitLog.setPlaceName(placeName);
            visitLog.setBusinessName(businessName);
            visitLog.setPurpose(purpose);
            visitLog.setLongi(loc.getLongitude());
            visitLog.setLati(loc.getLatitude());
//                visitLog.setOfficeName(officeName);
            visitLog.setUserUUID(CommonDataArea.getUserUUID());
            visitLog.setRecUUID(recUUID);
            visitLog.setUserName(commonSettings.getName());
            visitLog.setSendStatus(0);
            visitLog.setHoursSpend(hoursSpend);
            visitLog.setMinutesSpend(minutesSpend);
            long timeStamp = date.getTimeInMillis();
            visitLog.setTimeStamp(timeStamp);
            String dateStr = getDate(timeStamp,"dd-MM-yyyy:HH:mm");
            visitLog.setDateTime(dateStr);
            /*
            try {
                final Dao<VisitLog, Integer> visitListDb = CommonDataArea.getHelper(context).getVisitLogDao();
                visitListDb.create(visitLog);
                CommonDataArea.closeHelper();
            } catch (java.sql.SQLException e) {
                e.printStackTrace();
            }
*/
            synchronized (CommonDataArea.dbLock) {
                try {
                    final Dao<VisitLog, Integer> visitListDb = CommonDataArea.getHelper(context).getVisitLogDao();
                    QueryBuilder<VisitLog, Integer> queryBuilder = visitListDb.queryBuilder();
                    queryBuilder.where().eq("RecUUID", recUUID);
                    if(CommonDataArea.isDbOpen()) {
                        List<VisitLog> visList = queryBuilder.query();
                        if ((visList != null) && (visList.size() > 0)) {
                            visitLog.set_id(visList.get(0).get_id());
                            visitListDb.update(visitLog);
                        } else {
                            visitListDb.create(visitLog);
                        }
                    }
                    CommonDataArea.closeHelper();
                } catch (java.sql.SQLException e) {
                    e.printStackTrace();
                }
            }
            return true;

        } catch (Exception exp) {
            Log.e("Err", exp.getMessage());

        }

        return false;
    }

    public static void SaveRcvdvisit(Context context, JSONObject visitData) {
        try {

            Settings commonSettings = CommonDataArea.getCommonSettings();
            visitData = visitData.getJSONObject("body");
            int year = visitData.getInt("Year");
            int month = visitData.getInt("Month");
            int day = visitData.getInt("Day");
            int hour = visitData.getInt("Hour");
            int minute = visitData.getInt("Minute");
            int arrivedDeparted = visitData.getInt("ArrivedDeparted");
            String placeName = visitData.getString("PlaceName");
            String purpose = visitData.getString("Purpose");
            String userUUID = visitData.getString("UserUUID");
            String userName = visitData.getString("UserName");
            String recUUID = visitData.getString("RecUUID");
            String bizName = visitData.getString("BizName");
            long hoursSpend=0;
            long miutesSpend=0;
            if(visitData.has("HoursSpend"))
                hoursSpend = visitData.getLong("HoursSpend");
            if(visitData.has("MinutesSpend"))
                miutesSpend = visitData.getLong("MinutesSpend");
            Double longi = visitData.getDouble("Longi");
            Double lati = visitData.getDouble("Lati");
            long timeStamp = visitData.getLong("TimeStamp");

            VisitLog visitLog = new VisitLog();
            visitLog.setRecUUID(recUUID);
            visitLog.setUserUUID(CommonDataArea.getUserUUID());
            visitLog.setArrivedDeparted(arrivedDeparted);
            visitLog.setLogYear(year);
            visitLog.setLogMonth(month);
            visitLog.setLogDayOfMonth(day);
            visitLog.setLogHour(hour);
            visitLog.setLogMinute(minute);
            visitLog.setPlaceName(placeName);
            visitLog.setBusinessName(bizName);
            visitLog.setPurpose(purpose);
//                visitLog.setOfficeName(officeName);
            visitLog.setUserUUID(userUUID);
            visitLog.setRecUUID(recUUID);
            visitLog.setUserName(userName);
            visitLog.setSendStatus(1);
            visitLog.setHoursSpend(hoursSpend);
            visitLog.setMinutesSpend(miutesSpend);
            visitLog.setTimeStamp(timeStamp);


            synchronized (CommonDataArea.dbLock) {
                try {
                    final Dao<VisitLog, Integer> visitListDb = CommonDataArea.getHelper(context).getVisitLogDao();
                    QueryBuilder<VisitLog, Integer> queryBuilder = visitListDb.queryBuilder();
                    queryBuilder.where().eq("RecUUID", recUUID);
                    List<VisitLog> visList = queryBuilder.query();
                    if ((visList != null) && (visList.size() > 0)) {
                        visitLog.set_id(visList.get(0).get_id());
                        visitListDb.update(visitLog);
                    } else {
                        visitListDb.create(visitLog);
                    }
                    CommonDataArea.closeHelper();
                } catch (java.sql.SQLException e) {
                    e.printStackTrace();
                }
            }
        } catch (Exception exp) {
            LogWriter.writeLogException("VisitRecv",exp);
        }
    }

    public static List<VisitLog> getUnsendVisData(Context context) {
        try {
            // DataModel dataModel = new DataModel();
            List<VisitLog> visList;
            Calendar today = Calendar.getInstance();
            Dao<VisitLog, Integer> officeDb = CommonDataArea.getHelper(context).getVisitLogDao();
            QueryBuilder<VisitLog, Integer> queryBuilder = officeDb.queryBuilder();
            queryBuilder.where().eq("SendStatus", 0);
            visList =queryBuilder.query();
            CommonDataArea.closeHelper();
            return visList;
        }catch(Exception exp){
            return null;
        }
    }

    public static List<VisitMeetLog> getUnsendVisMeetData(Context context) {
        try {

            List<VisitMeetLog> visMeetList;
            Calendar today = Calendar.getInstance();
            Dao<VisitMeetLog, Integer> officeDb = CommonDataArea.getHelper(context).getVisitMeetingDao();
            QueryBuilder<VisitMeetLog, Integer> queryBuilder = officeDb.queryBuilder();
            queryBuilder.where().eq("SendStatus", 0);
            visMeetList =queryBuilder.query();
            CommonDataArea.closeHelper();
            return visMeetList;
        }catch(Exception exp){
            return null;
        }
    }

    public static List<VisitLog> getAllVisData(Context context) {
        try {
            // DataModel dataModel = new DataModel();
            List<VisitLog> visList;
            Calendar today = Calendar.getInstance();
            Dao<VisitLog, Integer> officeDb = CommonDataArea.getHelper(context).getVisitLogDao();
            QueryBuilder<VisitLog, Integer> queryBuilder = officeDb.queryBuilder();
            queryBuilder.where().eq("SendStatus", 0);
            visList =queryBuilder.query();
            CommonDataArea.closeHelper();
            return visList;
        }catch(Exception exp){
            return null;
        }
    }

    public static void sendVisData(Context context) {
        try {
            List<VisitLog> visitListChk = getUnsendVisData(context);
            if((visitListChk==null)||(visitListChk.size()==0)) return;
            for (VisitLog visit : visitListChk) {
                MOMesgHandler attnMesg = new MOMesgHandler();
                attnMesg.MoMesgCreate(CommonDataArea.getUserUUID(), "TOALL", MOMain.MOMESG_VISITLOG_MESG);
                attnMesg.addAttribute("Year", visit.getLogYear());
                attnMesg.addAttribute("Month", visit.getLogMonth());
                attnMesg.addAttribute("Day", visit.getLogDayOfMonth());
                attnMesg.addAttribute("Hour", visit.getLogHour());
                attnMesg.addAttribute("Minute", visit.getLogMinute());
                attnMesg.addAttribute("DateTime", visit.getDateTime());

                attnMesg.addAttribute("UserUUID", visit.getUserUUID());
                attnMesg.addAttribute("UserName", visit.getUserName());
                attnMesg.addAttribute("RecUUID", visit.getRecUUID()); //Records UUID

                attnMesg.addAttribute("Longi", Double.toString(visit.getLongi()));
                attnMesg.addAttribute("Lati", Double.toString(visit.getLati()));

                attnMesg.addAttribute("PlaceName", visit.getPlaceName());
                attnMesg.addAttribute("BizName", visit.getBusinessName());
                attnMesg.addAttribute("Purpose", visit.getPurpose());
                attnMesg.addAttribute("ArrivedDeparted", visit.getArrivedDeparted());
                attnMesg.addAttribute("ArrivedDeparted", visit.getArrivedDeparted());
                attnMesg.addAttribute("TimeStamp", visit.getTimeStamp());

                JSONObject mesg = attnMesg.assembleJSONMesg();
                boolean sendSuccess = attnMesg.sendMessage(mesg);
                if (sendSuccess) {
                    Dao<VisitLog, Integer> visDb = CommonDataArea.getHelper(context).getVisitLogDao();
                    QueryBuilder<VisitLog, Integer> queryBuilder = visDb.queryBuilder();
                    queryBuilder.where().eq("RecUUID", visit.getRecUUID());
                    List<VisitLog> attnList = queryBuilder.query();
                    if((attnList!=null)&&(attnList.size()>0)) {
                        VisitLog visLog = attnList.get(0);
                        visLog.setSendStatus(1);
                        visDb.update(visLog);
                    }
                    CommonDataArea.closeHelper();
                }
                //todo sendSuccess is true then update the current record sendStatus to 1
            }
        } catch (Exception exp) {
            LogWriter.writeLogException("sendAttnData", exp);
        }
    }

    public static void sendVisMeetData(Context context) {
        try {
            List<VisitMeetLog> visitMeetListChk = getUnsendVisMeetData(context);
            if((visitMeetListChk==null)||(visitMeetListChk.size()==0)) return;
            for (VisitMeetLog visitMeet : visitMeetListChk) {
                MOMesgHandler attnMesg = new MOMesgHandler();
                attnMesg.MoMesgCreate(CommonDataArea.getUserUUID(), "TOALL", MOMain.MOMESG_VISMEET_MSG);

                attnMesg.addAttribute("UserUUID", visitMeet.getUserUUID());
                if(visitMeet.getUserName()==null) attnMesg.addAttribute("UserName", "_NA_");
                else
                    attnMesg.addAttribute("UserName", visitMeet.getUserName());
                attnMesg.addAttribute("RecUUID", visitMeet.getRecUUID()); //Records UUID
                attnMesg.addAttribute("VisitUUID",visitMeet.getVisitUUID());
                attnMesg.addAttribute("PersonMet",visitMeet.getPersonMet());
                attnMesg.addAttribute("SendStatus", visitMeet.getSendStatus()); //Records UUID
                attnMesg.addAttribute("Title",visitMeet.getTitle());
                attnMesg.addAttribute("Note",visitMeet.getNote());
                attnMesg.addAttribute("BizName",visitMeet.getBusinessName());
                String dateStr =visitMeet.getDate();
                if( dateStr==null) dateStr = getDate(visitMeet.getDateTimeStamp(),"dd-MM-yyyy");
                attnMesg.addAttribute("Date", visitMeet.getDate()); //Records UUID
                attnMesg.addAttribute("Date", visitMeet.getDate());
                attnMesg.addAttribute("DateTimeStamp",visitMeet.getDateTimeStamp());
                attnMesg.addAttribute("Status",visitMeet.getStatus());
                attnMesg.addAttribute("NextVisDate",visitMeet.getNextVisDate());
                JSONObject mesg = attnMesg.assembleJSONMesg();
                boolean sendSuccess = attnMesg.sendMessage(mesg);
                if (sendSuccess) {
                    Dao<VisitMeetLog, Integer> visDb = CommonDataArea.getHelper(context).getVisitMeetingDao();
                    QueryBuilder<VisitMeetLog, Integer> queryBuilder = visDb.queryBuilder();
                    queryBuilder.where().eq("_id", visitMeet.get_id());
                    List<VisitMeetLog> visMeetList = queryBuilder.query();
                    if((visMeetList!=null)&&(visMeetList.size()>0)) {
                        VisitMeetLog visMeetLog = visMeetList.get(0);
                        visMeetLog.setSendStatus(1);
                        visDb.update(visMeetLog);
                    }
                    CommonDataArea.closeHelper();
                }
                //todo sendSuccess is true then update the current record sendStatus to 1
            }
        } catch (Exception exp) {
            LogWriter.writeLogException("sendAttnData", exp);
        }
    }

    public static void SaveRcvdVisMeetLog(Context context, JSONObject visitMeetData) {
        try {
            visitMeetData = visitMeetData.getJSONObject("body");
            VisitMeetLog visitMeetLog= new VisitMeetLog();

            visitMeetLog.setRecUUID(visitMeetData.getString("RecUUID"));
            visitMeetLog.setUserUUID(visitMeetData.getString("UserUUID"));
            visitMeetLog.setUserName(visitMeetData.getString("UserName"));
            visitMeetLog.setVisitUUID(visitMeetData.getString("VisitUUID"));
            visitMeetLog.setPersonMet(visitMeetData.getString("PersonMet"));
            visitMeetLog.setSendStatus(visitMeetData.getInt("SendStatus"));
            visitMeetLog.setTitle(visitMeetData.getString("Title"));
            visitMeetLog.setNote(visitMeetData.getString("Note"));
            visitMeetLog.setBusinessName(visitMeetData.getString("BizName"));
            visitMeetLog.setDate(visitMeetData.getString("Date"));
            visitMeetLog.setDateTimeStamp(visitMeetData.getLong("DateTimeStamp"));
            visitMeetLog.setStatus(visitMeetData.getInt("Status"));
            visitMeetLog.setNextVisDate(visitMeetData.getString("NextVisDate"));
            visitMeetLog.setSendStatus(1);

            synchronized (CommonDataArea.dbLock) {
                try {
                    final Dao<VisitMeetLog, Integer> visitListDb = CommonDataArea.getHelper(context).getVisitMeetingDao();
                    QueryBuilder<VisitMeetLog, Integer> queryBuilder = visitListDb.queryBuilder();
                    queryBuilder.where().eq("RecUUID", visitMeetLog.getRecUUID());
                    List<VisitMeetLog> visMeetList = queryBuilder.query();
                    if ((visMeetList != null) && (visMeetList.size() > 0)) {

                        visitListDb.update(visitMeetLog);
                    } else {
                        visitListDb.create(visitMeetLog);
                    }
                    CommonDataArea.closeHelper();
                } catch (java.sql.SQLException e) {
                    e.printStackTrace();
                }
            }
        } catch (Exception exp) {
            LogWriter.writeLogException("VisitRecv",exp);
        }
    }

    public static JSONArray getVisData(Context context) {
        try {

            Dao<VisitLog, Integer> visitListChk = CommonDataArea.getHelper(context).getVisitLogDao();

            JSONArray visitListArray = new JSONArray();
            for (VisitLog visit : visitListChk) {
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
                visitJson.put("BizName",visit.getBusinessName());
                visitJson.put("Purpose", visit.getPurpose());
                visitJson.put("ArrivedDeparted", visit.getArrivedDeparted());
                visitJson.put("ArrivedDeparted", visit.getArrivedDeparted());
                visitJson.put("TimeStamp", visit.getTimeStamp());
                visitListArray.put(visitJson);
            }

            return visitListArray;
        } catch (Exception exp) {
            LogWriter.writeLogException("sendAttnData", exp);
            return null;
        }
    }
    public static List<VisitLog> getTodayVisitData(Context context) {
        try {
            // DataModel dataModel = new DataModel();
            List<VisitLog> visitList;
            Calendar today = Calendar.getInstance();
            Dao<VisitLog, Integer> officeListDb = CommonDataArea.getHelper(context).getVisitLogDao();

            QueryBuilder<VisitLog, Integer> queryBuilder =
                    officeListDb.queryBuilder();

            queryBuilder.where().eq("LogYear", today.get(Calendar.YEAR));
            visitList = queryBuilder.query();
            CommonDataArea.closeHelper();
            return visitList;
        } catch (Exception exp) {
            return null;
        }
    }

    public static boolean IsLastMarkedArrived(Context context){
        try {
            final Dao<VisitLog,Integer> Attndao= CommonDataArea.getHelper(context).getVisitLogDao();
            QueryBuilder<VisitLog, Integer> queryBuilder = Attndao.queryBuilder();
            queryBuilder.where().eq("UserUUID", CommonDataArea.getUserUUID());
            queryBuilder.orderBy("_id",false);
            List<VisitLog> attnList =queryBuilder.query();
            if((attnList!=null)&&(attnList.size()>0))
            {
                if(attnList.get(0).getArrivedDeparted()==1)
                    return  true;
                else return false;

            }
        }catch (Exception exp){
            LogWriter.writeLogException("Check arrived dept",exp);
        }
        return false;
    }

    public static String getDate(long milliSeconds, String dateFormat)
    {
        // Create a DateFormatter object for displaying date in specified format.
        SimpleDateFormat formatter = new SimpleDateFormat(dateFormat);

        // Create a calendar object that will convert the date and time value in milliseconds to date.
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(milliSeconds);
        return formatter.format(calendar.getTime());
    }
    public  static boolean setMeetingInfoReviewed(Context context,VisitMeetLog log){
        try {
            List<VisitMeetLog> meetList = null;
            VisitMeetLog meetLog;
            final Dao<VisitMeetLog, Integer> visitMeetDb = CommonDataArea.getHelper(context).getVisitMeetingDao();

                QueryBuilder<VisitMeetLog, Integer> queryBuilder = visitMeetDb.queryBuilder();
                queryBuilder.where().eq("_id", log.get_id());
                meetList = queryBuilder.query();

                if((meetList!=null)&&(meetList.size()==1)) {
                    meetLog=meetList.get(0);
                    meetLog.setStatus(1);
                    meetLog.setSendStatus(0);
                    visitMeetDb.update(meetLog);

            }
            return true;
        }catch(Exception exp){
            return false;
        }
    }

    public  static boolean setMeetingDeleted(Context context,VisitMeetLog log){
        try {
            List<VisitMeetLog> meetList = null;
            VisitMeetLog meetLog;
            final Dao<VisitMeetLog, Integer> visitMeetDb = CommonDataArea.getHelper(context).getVisitMeetingDao();
            if (log.getRecUUID() != null) {
                QueryBuilder<VisitMeetLog, Integer> queryBuilder = visitMeetDb.queryBuilder();
                queryBuilder.where().eq("RecUUID", log.getRecUUID());
                meetList = queryBuilder.query();

                if((meetList!=null)&&(meetList.size()==1)) {
                    meetLog=meetList.get(0);
                    meetLog.setStatus(2);
                    meetLog.setSendStatus(0);
                    visitMeetDb.update(meetLog);
                }
            }
            return true;
        }catch(Exception exp){
            return false;
        }
    }
    public static boolean saveMeetingInfo(Context context,String personMet,String bizName, String deptTitle,long timeStamp, String note,int status,String visUUID,String recUUID,String nextVisDate) {
        synchronized (CommonDataArea.dbLock) {
            try {
                VisitMeetLog meetLog;
                List<VisitMeetLog> meetList=null;
                final Dao<VisitMeetLog, Integer> visitMeetDb = CommonDataArea.getHelper(context).getVisitMeetingDao();
                if (recUUID != null) {
                    QueryBuilder<VisitMeetLog, Integer> queryBuilder = visitMeetDb.queryBuilder();
                    queryBuilder.where().eq("RecUUID", recUUID);
                    meetList = queryBuilder.query();

                }

                if ((meetList != null) && (meetList.size() > 0)) {
                    meetLog = meetList.get(0);
                    if (CommonDataArea.getInstType() == MOMain.MO_INSTTYPE_ADMIN) {
                        meetLog.setTitle(deptTitle);
                        meetLog.setPersonMet(personMet);
                        meetLog.setDateTimeStamp(timeStamp);
                        meetLog.setNote(note);
                        meetLog.setStatus(status);
                        meetLog.setBusinessName(bizName);
                        meetLog.setNextVisDate(nextVisDate);
                        visitMeetDb.update(meetLog);
                    } else {
                        if (meetLog.getStatus() != MOMain.MO_MEETING_ITEM_APPROVED) {
                            meetLog.setTitle(deptTitle);
                            meetLog.setPersonMet(personMet);
                            meetLog.setDateTimeStamp(timeStamp);
                            meetLog.setNote(note);
                            meetLog.setBusinessName(bizName);
                            meetLog.setNextVisDate(nextVisDate);
                            visitMeetDb.update(meetLog);
                        }
                    }
                } else {

                    UUID uuid = UUID.randomUUID();
                    recUUID = uuid.toString();
                    String dateStr = getDate(timeStamp,"dd-MM-yyyy");
                    meetLog = new VisitMeetLog();
                    meetLog.setTitle(deptTitle);
                    meetLog.setPersonMet(personMet);
                    meetLog.setDateTimeStamp(timeStamp);
                    meetLog.setDate(dateStr);
                    meetLog.setNote(note);
                    meetLog.setStatus(status);
                    meetLog.setRecUUID(recUUID);
                    meetLog.setVisitUUID(visUUID);
                    meetLog.setUserUUID(CommonDataArea.getUserUUID());
                    meetLog.setUserName(CommonDataArea.getCommonSettings().getName());
                    meetLog.setBusinessName(bizName);
                    meetLog.setNextVisDate(nextVisDate);
                    visitMeetDb.create(meetLog);
                    CommonDataArea.closeHelper();
                    return true;
                }
                CommonDataArea.closeHelper();
                return true;
            } catch (java.sql.SQLException e) {
                e.printStackTrace();
                return false;
            } catch (Exception exp) {
                return false;
            }
        }
    }
}
