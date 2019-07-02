package moffice.meta.com.molibrary.core;

import android.content.Context;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.stmt.QueryBuilder;

import org.json.JSONObject;

import java.util.Calendar;
import java.util.List;

import moffice.meta.com.molibrary.firebase.MOMesgHandler;
import moffice.meta.com.molibrary.models.dbmodels.EnquiryDetails;
import moffice.meta.com.molibrary.utility.CommonDataArea;
import moffice.meta.com.molibrary.utility.LogWriter;

/**
 * Created by Administrator on 04/16/2018.
 */

public class MOEnquiryManager {

    public static List<EnquiryDetails> getUnsendEnqData(Context context) {
        try {
            // DataModel dataModel = new DataModel();
            List<EnquiryDetails> enquiryList;
            Calendar today = Calendar.getInstance();
            Dao<EnquiryDetails, Integer> officeDb = CommonDataArea.getHelper(context).getEnquiryDao();
            QueryBuilder<EnquiryDetails, Integer> queryBuilder = officeDb.queryBuilder();
            queryBuilder.where().eq("sendstatus", 0);
            enquiryList =queryBuilder.query();
            CommonDataArea.closeHelper();
            return enquiryList;
        }catch(Exception exp){
            return null;
        }
    }

    public static void SaveRcvdEnq(Context context, JSONObject enqData) {
        try {

            enqData = enqData.getJSONObject("body");
            EnquiryDetails enquiry  = new EnquiryDetails();
            enquiry.setLogYear(enqData.getInt("Year"));
            enquiry.setLogMonth(enqData.getInt("Month"));
            enquiry.setLogDayOfMonth(enqData.getInt("Day"));
            enquiry.setRecUUID(enqData.getString("RecUUID"));
            enquiry.setUserUUID(enqData.getString("UserUUID"));
            enquiry.setName(enqData.getString("Name"));
            enquiry.setCustName(enqData.getString("CustName"));
            enquiry.setPhoneNumber(enqData.getString("PhoneNumber"));
            enquiry.setDescription(enqData.getString("Description"));
            enquiry.setStatus(enqData.getString("Status"));
            enquiry.setAssignedTo(enqData.getString("AssignedTo"));
            enquiry.setProduct(enqData.getString("Product"));
            enquiry.setDate(enqData.getString("Date"));
            enquiry.setNote("Note");
            enquiry.setSendstatus(1);

            synchronized (CommonDataArea.dbLock) {
                try {
                    final Dao<EnquiryDetails, Integer> enqDb = CommonDataArea.getHelper(context).getEnquiryDao();
                    QueryBuilder<EnquiryDetails, Integer> queryBuilder = enqDb.queryBuilder();
                    queryBuilder.where().eq("RecUUID", enqData.getString("RecUUID"));
                    List<EnquiryDetails> enqList = queryBuilder.query();
                    if ((enqList != null) && (enqList.size() > 0)) {
                        enquiry.setId(enqList.get(0).getId());
                        enqDb.update(enquiry);
                    } else {
                        enqDb.create(enquiry);
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

    public static void sendEnqData(Context context) {
        try {
            List<EnquiryDetails> enqList = getUnsendEnqData(context);
            if((enqList==null)||(enqList.size()==0)) return;
            for (EnquiryDetails enq : enqList) {
                MOMesgHandler enqMesg = new MOMesgHandler();
                enqMesg.MoMesgCreate(CommonDataArea.getUserUUID(), "TOALL", MOMain.MOMESG_ENQLOG_MESG);
                enqMesg.addAttribute("Year", enq.getLogYear());
                enqMesg.addAttribute("Month", enq.getLogMonth());
                enqMesg.addAttribute("Day", enq.getLogDayOfMonth());
                enqMesg.addAttribute("RecUUID", enq.getRecUUID());
                enqMesg.addAttribute("UserUUID", enq.getUserUUID());
                enqMesg.addAttribute("Name", enq.getName());
                enqMesg.addAttribute("CustName", enq.getCustName());
                enqMesg.addAttribute("PhoneNumber", enq.getPhoneNumber());
                enqMesg.addAttribute("Description", enq.getDescription());
                enqMesg.addAttribute("Status", enq.getStatus());
                enqMesg.addAttribute("AssignedTo", enq.getAssignedTo());
                enqMesg.addAttribute("Product", enq.getProduct());
                enqMesg.addAttribute("Date", enq.getDate());
                enqMesg.addAttribute("Note", enq.getNote());


                JSONObject mesg = enqMesg.assembleJSONMesg();
                boolean sendSuccess = enqMesg.sendMessage(mesg);
                if (sendSuccess) {
                    Dao<EnquiryDetails, Integer> visDb = CommonDataArea.getHelper(context).getEnquiryDao();
                    QueryBuilder<EnquiryDetails, Integer> queryBuilder = visDb.queryBuilder();
                    queryBuilder.where().eq("RecUUID", enq.getRecUUID());
                    List<EnquiryDetails> enqList2 = queryBuilder.query();
                    if((enqList2!=null)&&(enqList2.size()>0)) {
                        EnquiryDetails enqLog = enqList2.get(0);

                        enqLog.setSendstatus(1);
                        visDb.update(enqLog);
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
