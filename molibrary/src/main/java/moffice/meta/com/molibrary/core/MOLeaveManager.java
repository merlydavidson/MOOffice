package moffice.meta.com.molibrary.core;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.stmt.QueryBuilder;
import com.j256.ormlite.stmt.UpdateBuilder;

import org.json.JSONArray;
import org.json.JSONObject;

import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

import moffice.meta.com.molibrary.firebase.MOMesgHandler;
import moffice.meta.com.molibrary.models.dbmodels.AttnLog;
import moffice.meta.com.molibrary.models.dbmodels.LeaveList;
import moffice.meta.com.molibrary.utility.CommonDataArea;
import moffice.meta.com.molibrary.utility.LogWriter;

public class MOLeaveManager {
    LeaveList leaveListO;

    public MOLeaveManager(Context context, LeaveList leaveList) throws SQLException {
        this.leaveListO = leaveList;
        leaveListO.setLvActiveRec(MOMain.RECSTATUS_ACTIVE);
        final Dao<LeaveList, Integer> Attndao = CommonDataArea.getHelper(context).getLeaveLogDao();
        Attndao.create(leaveListO);
    }

    public static JSONArray getLeaveData(Context context) {
        try {
            Dao<LeaveList, Integer> leaveListChk = CommonDataArea.getHelper(context).getLeaveLogDao();

            JSONArray leaveListArray = new JSONArray();
            for (LeaveList leave : leaveListChk) {
                JSONObject leavemJson = new JSONObject();
                leavemJson.put("LeaveID", leave.getLeaveID());
                leavemJson.put("StartDat", leave.getStartDat()); //-1
                leavemJson.put("DateTime", leave.getDateTime()); //-2
                leavemJson.put("TimeStamp", leave.getTimeStamp()); //-3
                leavemJson.put("EndDat", leave.getEndDate()); //-4
                //  leavemJson.put("UserName", leave.getLogType()); //Reugular 1 ,TOUR - On Tour-2, OUT- Official Short break-3
                leavemJson.put("RecUUID", leave.getRecUUID()); //-5
                leavemJson.put("UserUUID", leave.getUserUUID()); //-6
                leavemJson.put("Notes", leave.getNotes()); //-7
                leavemJson.put("LvStatus", leave.getLvStatus()); //-8//Records UUID
               // leavemJson.put("SendStatus", leave.getLvStatus());//-9
                leavemJson.put("UserName", leave.getUserName());//-9
                leavemJson.put("NumDays", Float.toString(leave.getNumDays()));//-10
                leavemJson.put("LvType", String.valueOf(leave.getLvType()));//-11
                leavemJson.put("RejectionNote", String.valueOf(leave.getRejectionNotes()));//-12
                leavemJson.put("lvActiveRec", String.valueOf(leave.getLvActiveRec()));//-13
                //lvActiveRec

                leaveListArray.put(leavemJson);
            }

            return leaveListArray;
        } catch (Exception exp) {
            LogWriter.writeLogException("sendAttnData", exp);
        }
        return null;
    }

    public static void saveRcvdLeave(Context context, JSONObject leaveData) {
        try {
            leaveData = leaveData.getJSONObject("body");
         //   int sendStatus = leaveData.getInt("SendStatus");
            int leaveStatus = leaveData.getInt("LvStatus");//-8
            int leaveType = leaveData.getInt("LvType");//-11
            float numberOfDays = leaveData.getInt("NumDays");//-10

            String startDate = leaveData.getString("StartDat"); //-1
            String endDate = leaveData.getString("EndDat"); //-4
            String dateTime = leaveData.getString("DateTime"); //-2
            String userName = leaveData.getString("UserName");//-9
            String recUUID = leaveData.getString("RecUUID");//-5
            long timeStamp = Long.parseLong(leaveData.getString("TimeStamp")); //-3
            long startTimeStamp = Long.parseLong(leaveData.getString("StartDatTimeStamp"));
            String userUUID = leaveData.getString("UserUUID"); //-6
            String rejReason = leaveData.getString("RejectionNote");//-12
            String notes = leaveData.getString("Notes");//-7
            int activeSts = leaveData.getInt("lvActiveRec");//-13

            final LeaveList leave = new LeaveList();
            leave.setRecUUID(recUUID);
            leave.setUserUUID(userUUID);
            leave.setUserName(userName);
            leave.setSendStatus(MOMain.SENDSTATUS_SEND_COMPLETED);
            leave.setLvStatus(leaveStatus);
            leave.setLvType(leaveType);
            leave.setStartDat(startDate);
            leave.setEndDate(endDate);
            leave.setDateTime(dateTime);
            leave.setTimeStamp(timeStamp);
            leave.setStartDatTimeStamp(startTimeStamp);
            leave.setNotes(notes);
            leave.setNumDays(numberOfDays);
            leave.setRejectionNotes(rejReason);
            leave.setLvActiveRec(activeSts);
            SimpleDateFormat df = new SimpleDateFormat("a");

            try {
                final Dao<LeaveList, Integer> Leavedao = CommonDataArea.getHelper(context).getLeaveLogDao();
                QueryBuilder<LeaveList, Integer> queryBuilder = Leavedao.queryBuilder();
                queryBuilder.where().eq("RecUUID", recUUID);
                List<LeaveList> leaveList = queryBuilder.query();
                if ((leaveList != null) && (leaveList.size() > 0)) {
                    leave.setLeaveID(leaveList.get(0).getLeaveID());
                    Leavedao.update(leave);
                } else {
                    Leavedao.create(leave);
                }
                CommonDataArea.closeHelper();
                MODataDispatchThread.triggerDispatch();
            } catch (java.sql.SQLException e) {
                e.printStackTrace();
            }
        } catch (Exception exp) {
            LogWriter.writeLogException("AttnMesgProc", exp);
        }
    }


    public static void sendLeaveData(Context context) {
        try {
            List<LeaveList> leaveListChk = MOLeaveManager.getUnsendLeaveData(context);
            if ((leaveListChk == null) || (leaveListChk.size() == 0)) return;
            for (LeaveList leave : leaveListChk) {
                MOMesgHandler leaveMesg = new MOMesgHandler();
                leaveMesg.MoMesgCreate(CommonDataArea.getUserUUID(), "TOALL", MOMain.MOMESG_LEAVE_REQ);
                leaveMesg.addAttribute("StartDat", leave.getStartDat());
                leaveMesg.addAttribute("EndDat", leave.getEndDate());
                leaveMesg.addAttribute("DateTime", leave.getDateTime());
                leaveMesg.addAttribute("Notes", leave.getNotes());
                leaveMesg.addAttribute("LvStatus", leave.getLvStatus());
              //  leaveMesg.addAttribute("SendStatus", leave.getSendStatus());
                leaveMesg.addAttribute("NumDays", String.valueOf(leave.getNumDays()));
                leaveMesg.addAttribute("LvType", String.valueOf(leave.getLvType()));

                leaveMesg.addAttribute("RecUUID",leave.getRecUUID());
                leaveMesg.addAttribute("UserUUID", leave.getUserUUID());
                leaveMesg.addAttribute("UserName", leave.getUserName());

                leaveMesg.addAttribute("StartDatTimeStamp", leave.getStartDatTimeStamp());
                leaveMesg.addAttribute("TimeStamp", leave.getTimeStamp());
                leaveMesg.addAttribute("RejectionNote", leave.getRejectionNotes());
                leaveMesg.addAttribute("lvActiveRec",leave.getLvActiveRec());


                JSONObject mesg = leaveMesg.assembleJSONMesg();
                boolean sendSuccess = leaveMesg.sendMessage(mesg);
                if (sendSuccess) {
                    Dao<LeaveList, Integer> leaveDb = CommonDataArea.getHelper(context).getLeaveLogDao();
                    QueryBuilder<LeaveList, Integer> queryBuilder = leaveDb.queryBuilder();
                    queryBuilder.where().eq("RecUUID", leave.getRecUUID());
                    List<LeaveList> lvList = queryBuilder.query();
                    if ((lvList != null) && (lvList.size() > 0)) {
                        LeaveList lvLog = lvList.get(0);
                        lvLog.setSendStatus(1);
                        leaveDb.update(lvLog);
                    }
                    CommonDataArea.closeHelper();
                }
                //todo sendSuccess is true then update the current record sendStatus to 1
            }
        } catch (Exception exp) {
            LogWriter.writeLogException("sendAttnData", exp);
        }

    }

    public static List<LeaveList> getUnsendLeaveData(Context context) {
        try {
            // DataModel dataModel = new DataModel();
            List<LeaveList> leaveList;
            Calendar today = Calendar.getInstance();
            Dao<LeaveList, Integer> officeListDb = CommonDataArea.getHelper(context).getLeaveLogDao();
            QueryBuilder<LeaveList, Integer> queryBuilder = officeListDb.queryBuilder();
            queryBuilder.where().eq("SendStatus", 0);
            leaveList = queryBuilder.query();
            CommonDataArea.closeHelper();
            return leaveList;
        } catch (Exception exp) {
            return null;
        }
    }

    public static void updateLeaveStatus(int leavId, String uuid, String reas, int leaveStatus, Context context) throws SQLException {
        final Dao<LeaveList, Integer> Leavedao = CommonDataArea.getHelper(context).getLeaveLogDao();
        UpdateBuilder<LeaveList, Integer> updateBuilder = Leavedao.updateBuilder();
        updateBuilder.where().eq("LeaveID", leavId).and().eq("UserUUID", uuid);


        updateBuilder.updateColumnValue("LvStatus", leaveStatus);
        updateBuilder.updateColumnValue("RejectionNote", reas);

        updateBuilder.updateColumnValue("SendStatus", MOMain.SENDSTATUS_NOT_SEND);
        if(leaveStatus==MOMain.LEAVESTATUS_DISCARDED){
            updateBuilder.updateColumnValue("lvActiveRec",MOMain.RECSTATUS_DELETED);
        }
        updateBuilder.update();
//        QueryBuilder<LeaveList, Integer> queryBuilder = Leavedao.queryBuilder();
//        queryBuilder.where().eq("RecUUID", recordId);
//        List<LeaveList> leaveList1 =queryBuilder.query();
//        if((leaveList1!=null)&&(leaveList1.size()>0))
//        {LeaveList leave=new LeaveList();
//            leave.setLvStatus(leaveStatus);
//          int res=Leavedao.update(leave);
//          Log.d("resultUpdate",String.valueOf(res));
//        }
        Toast.makeText(context, "Updated", Toast.LENGTH_SHORT).show();
    }

}
