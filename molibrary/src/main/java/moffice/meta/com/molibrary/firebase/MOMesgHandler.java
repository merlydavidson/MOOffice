package moffice.meta.com.molibrary.firebase;

/**
 * Created by bmjo on 01/27/2017.
 */
import android.util.Log;

import org.json.JSONObject;

import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import moffice.meta.com.molibrary.utility.CommonDataArea;
import moffice.meta.com.molibrary.utility.LogWriter;

public class MOMesgHandler {
        JSONObject mainObject;
        JSONObject mainAttrs;
        JSONObject mesgBody;
        static String apiKEY = "AAAAbTQDfcg:APA91bH9_IZzrF3Yy3xG4JXqyMDqO_BNCFxkinShcqPTygPtYMlMyGWWbcKMq2BNF33FOXgasX-u8wCbf_RrvH0l2ZpNmiokW6nzYL4LVjDI_-x0v9FLJoDf9TaHOtKcLu30mwTBbJoo";

    public void MoMesgCreate(String senderUUID,String toUUID,String mesgType){
            mainObject= new JSONObject();
            mainAttrs = new JSONObject();
            mesgBody = new JSONObject();
            try {
                mainAttrs.put("PostBy", senderUUID);
                mainAttrs.put("MesgTo",toUUID);
                mainAttrs.put("type",mesgType);

            }catch (Exception exp){
                LogWriter.writeLog("JSON",exp.getMessage());
            }
        }
        public void addAttribute(String name, String value){
            try {
                mesgBody.put(name, value);
            }catch (Exception exp){
                LogWriter.writeLog("JSON",exp.getMessage());
            }
        }
        public void addAttribute(String name, int value){
            try {
                mesgBody.put(name, value);
            }catch (Exception exp){
                LogWriter.writeLog("JSON",exp.getMessage());
            }
        }
        public void addAttribute(String name, long value){
            try {
                mesgBody.put(name, value);
            }catch (Exception exp){
                LogWriter.writeLog("JSON",exp.getMessage());
            }
        }
        public void addAttribute(String name, boolean value){
            try {
                mesgBody.put(name, value);
            }catch (Exception exp){
                LogWriter.writeLog("JSON",exp.getMessage());
            }
        }

        public JSONObject assembleJSONMesg()
        {
            try {
                mainAttrs.put("body",mesgBody);
                mainObject.put("mesg",mainAttrs);
                return mainObject;
            }catch (Exception exp){
                LogWriter.writeLog("JSON",exp.getMessage());
                return null;
            }
        }

        public boolean sendMessageOnThread(JSONObject message)
        {
            try {
                SendMesgRun runner = new SendMesgRun();
                runner.message = message;
                Thread thread = new Thread(runner);
                thread.start();
                thread.wait();

                return runner.status;
            }catch (Exception exp){
                Log.e("Error",exp.getMessage());
            }
            return true;
        }
    public boolean sendDevMessage(JSONObject message){
        try {
            URL url = new URL("https://android.googleapis.com/gcm/send");
            String topic = CommonDataArea.getTopic();
            if(topic==null){
                LogWriter.writeLog("GCMSend","No Valid Topic(company group) available");
                return false;
            }
            topic="/topics/meta";
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestProperty("Authorization", "key=" + apiKEY);
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setRequestMethod("POST");
            conn.setDoOutput(true);

            JSONObject gcmMesg= new JSONObject();
            gcmMesg.put("to",topic);
            gcmMesg.put("data",message);
            String json = gcmMesg.toString();
            //String json = "{\"to\": \"/topics/metahome\",\"data\": {\"message\": \"This is a Firebase Cloud Messaging Topic Message!\",}}";

            OutputStream outputStream = conn.getOutputStream();
            outputStream.write(json.toString().getBytes());
            outputStream.flush();

            // Send GCM message content.
            byte [] respByt= new byte[1000];
            InputStream inputStream = conn.getInputStream();

            inputStream.read(respByt);
            String resp = respByt.toString();

            System.out.println(resp);
            System.out.println("Check your device/emulator for notification or logcat for " +
                    "confirmation of the receipt of the GCM message.");
            return true;
        }catch(Exception exp){
            LogWriter.writeLog("exp",exp.getMessage());
            return false;
        }

    }
        public boolean sendMessage(JSONObject message){
                try {
                    URL url = new URL("https://android.googleapis.com/gcm/send");
                    String topic = CommonDataArea.getTopic();
                    if(topic==null){
                        LogWriter.writeLog("GCMSend","No Valid Topic(company group) available");
                        return false;
                    }
                    topic="/topics/"+topic;
                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                    conn.setRequestProperty("Authorization", "key=" + apiKEY);
                    conn.setRequestProperty("Content-Type", "application/json");
                    conn.setRequestMethod("POST");
                    conn.setDoOutput(true);

                    JSONObject gcmMesg= new JSONObject();
                    gcmMesg.put("to",topic);
                    gcmMesg.put("data",message);
                    String json = gcmMesg.toString();
                    //String json = "{\"to\": \"/topics/metahome\",\"data\": {\"message\": \"This is a Firebase Cloud Messaging Topic Message!\",}}";

                    OutputStream outputStream = conn.getOutputStream();
                    outputStream.write(json.toString().getBytes());
                    outputStream.flush();

                    // Send GCM message content.
                    byte [] respByt= new byte[1000];
                    InputStream inputStream = conn.getInputStream();

                    inputStream.read(respByt);
                    String resp = respByt.toString();

                    System.out.println(resp);
                    System.out.println("Check your device/emulator for notification or logcat for " +
                            "confirmation of the receipt of the GCM message.");
                    return true;
                }catch(Exception exp){
                   LogWriter.writeLog("exp",exp.getMessage());
                    return false;
                }

        }

        class SendMesgRun implements Runnable{
            public boolean status;
            public JSONObject message;
            @Override
            public void run() {
                status= sendMessage(message);
            }
        }
}
