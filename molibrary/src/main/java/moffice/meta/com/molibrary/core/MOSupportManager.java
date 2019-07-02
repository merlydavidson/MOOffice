package moffice.meta.com.molibrary.core;

import android.content.Context;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.stmt.QueryBuilder;

import org.json.JSONObject;

import java.util.Calendar;
import java.util.List;

import moffice.meta.com.molibrary.firebase.MOMesgHandler;
import moffice.meta.com.molibrary.models.dbmodels.SupportDetails;
import moffice.meta.com.molibrary.utility.CommonDataArea;
import moffice.meta.com.molibrary.utility.LogWriter;

/**
 * Created by Administrator on 04/16/2018.
 */

public class MOSupportManager {

    public static List<SupportDetails> getUnsendSupData(Context context) {
        try {
            // DataModel dataModel = new DataModel();
            List<SupportDetails> enquiryList;
            Calendar today = Calendar.getInstance();
            Dao<SupportDetails, Integer> officeDb = CommonDataArea.getHelper(context).getSupportDao();
            QueryBuilder<SupportDetails, Integer> queryBuilder = officeDb.queryBuilder();
            queryBuilder.where().eq("sendstatus", 0);
            enquiryList =queryBuilder.query();
            CommonDataArea.closeHelper();
            return enquiryList;
        }catch(Exception exp){
            return null;
        }
    }

    public static void SaveRcvdSupport(Context context, JSONObject supData) {
        try {

            supData = supData.getJSONObject("body");

            SupportDetails supportDetails  = new SupportDetails();
            supportDetails.setRecUUID(supData.getString("RecUUID"));
            supportDetails.setUserUUID(supData.getString("UserUUID"));
            supportDetails.setName(supData.getString("Name"));
            supportDetails.setPhoneNumber(supData.getString("PhoneNumber"));
            supportDetails.setProduct(supData.getString("Product"));
            supportDetails.setDescription(supData.getString("Desc"));
            supportDetails.setDate(supData.getString("Date"));
            supportDetails.setCustName(supData.getString("CustName"));

            supportDetails.setNote(supData.getString("Note"));
            supportDetails.setAssignedTo(supData.getString("AssinedTo"));
            supportDetails.setStatus(supData.getString("Status"));
            supportDetails.setDateTimeStamp(supData.getLong("TimeStamp"));
            supportDetails.setLogYear(supData.getInt("Year"));
            supportDetails.setLogMonth(supData.getInt("Month"));
            supportDetails.setLogDayOfMonth(supData.getInt("Day"));
            supportDetails.setSendStatus(1);


            synchronized (CommonDataArea.dbLock) {
                try {
                    final Dao<SupportDetails, Integer> supDb = CommonDataArea.getHelper(context).getSupportDao();
                    QueryBuilder<SupportDetails, Integer> queryBuilder = supDb.queryBuilder();
                    queryBuilder.where().eq("RecUUID", supData.getString("RecUUID"));
                    List<SupportDetails> supList = queryBuilder.query();
                    if ((supList != null) && (supList.size() > 0)) {

                        supDb.update(supportDetails);
                    } else {
                        supDb.create(supportDetails);
                    }
                    CommonDataArea.closeHelper();
                } catch (java.sql.SQLException e) {
                    e.printStackTrace();
                }
            }
        } catch (Exception exp) {
            LogWriter.writeLogException("EnquiryEntry",exp);
        }
    }

    public static void sendSupData(Context context) {
        try {
            List<SupportDetails> supList = getUnsendSupData(context);
            if((supList==null)||(supList.size()==0)) return;
            for (SupportDetails sup : supList) {
                MOMesgHandler supMesg = new MOMesgHandler();
                supMesg.MoMesgCreate(CommonDataArea.getUserUUID(), "TOALL", MOMain.MOMESG_SUPLOG_MESG);

                supMesg.addAttribute("RecUUID",sup.getRecUUID());
                supMesg.addAttribute("UserUUID",sup.getUserUUID());
                supMesg.addAttribute("Name",sup.getName());
                supMesg.addAttribute("CustName",sup.getCustName());
                supMesg.addAttribute("PhoneNumber",sup.getPhoneNumber());
                supMesg.addAttribute("Product",sup.getProduct());
                supMesg.addAttribute("Desc",sup.getDescription());
                supMesg.addAttribute("Date",sup.getDate());
                supMesg.addAttribute("Note",sup.getNote());
                supMesg.addAttribute("AssinedTo",sup.getAssignedTo());
                supMesg.addAttribute("Status",sup.getStatus());
                supMesg.addAttribute("TimeStamp",sup.getDateTimeStamp());
                supMesg.addAttribute("Year",sup.getLogYear());
                supMesg.addAttribute("Month",sup.getLogMonth());
                supMesg.addAttribute("Day",sup.getLogDayOfMonth());

                JSONObject mesg = supMesg.assembleJSONMesg();
                boolean sendSuccess = supMesg.sendMessage(mesg);
                if (sendSuccess) {
                    Dao<SupportDetails, Integer> supDb = CommonDataArea.getHelper(context).getSupportDao();
                    QueryBuilder<SupportDetails, Integer> queryBuilder = supDb.queryBuilder();
                    queryBuilder.where().eq("RecUUID", sup.getRecUUID());
                    List<SupportDetails> supList2 = queryBuilder.query();
                    if((supList2!=null)&&(supList2.size()>0)) {
                        SupportDetails supLog = supList2.get(0);
                        supLog.setSendStatus(1);
                        supDb.update(supLog);
                    }
                    CommonDataArea.closeHelper();
                }
                //todo sendSuccess is true then update the current record sendStatus to 1
            }
        } catch (Exception exp) {
            LogWriter.writeLogException("sendEnqData", exp);
        }
    }

}
